/**
 * Copyright (c) 2019,2020 honintech
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package cn.weforward.dist.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.util.FileUtil;
import cn.weforward.common.util.SimpleKvPair;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.UrlUtil;
import cn.weforward.dist.DistService;
import cn.weforward.framework.web.upload.WebFileUpload;
import cn.weforward.framework.web.upload.WebForm;
import cn.weforward.util.CaUtil;
import cn.weforward.util.HttpAuth;

/**
 * 发布服务
 * 
 * @author daibo
 *
 */
public class DistServiceImpl implements RestfulService, DistService {
	/** 日志 */
	final static Logger _Logger = LoggerFactory.getLogger(DistServiceImpl.class);

	/** 发布包路径 */
	protected String m_DistPath;
	/** http验证 */
	protected HttpAuth m_Auth;
	/** 临时文件计数 */
	private AtomicLong m_TempCount = new AtomicLong();

	public DistServiceImpl(String distPath) {
		String path = FileUtil.getAbsolutePath(distPath, null);
		m_DistPath = path.endsWith(File.separator) ? path : path + File.separator;
		File distfile = new File(path);
		if (!distfile.exists()) {
			distfile.mkdirs();
		}

	}

	public void setAuth(HttpAuth auth) {
		m_Auth = auth;
	}

	public HttpAuth getAuth() {
		return m_Auth;
	}

	@Override
	public void precheck(RestfulRequest request, RestfulResponse response) throws IOException {
		String verb = request.getVerb();
		if ("OPTIONS".equals(verb)) {
			/*
			 * 可能是跨域的预检请求（preflight request）
			 * 
			 * @see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
			 */
			if (!StringUtil.isEmpty(request.getHeaders().get("Access-Control-Request-Method"))) {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "*");
				response.setHeader("Access-Control-Allow-Headers",
						"Authorization,Content-Type,Content-Encoding,User-Agent,X-Requested-With,Accept,Accept-Encoding");
				// 减少预检请求的次数
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setStatus(RestfulResponse.STATUS_OK);
			} else {
				response.setStatus(RestfulResponse.STATUS_BAD_REQUEST);
			}
			response.openOutput().close();
			return;
		}
		String path = request.getUri();
		if (!path.startsWith("/dist/")) {
			return;// 只验证dist
		}
		HttpAuth auth = getAuth();
		if (null != auth && !auth.auth(request, response)) {
			response.setStatus(RestfulResponse.STATUS_UNAUTHORIZED);
			response.openOutput().close();
			return;
		}
	}

	@Override
	public void service(RestfulRequest request, RestfulResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers",
				"Authorization,Content-Type,Content-Encoding,User-Agent,X-Requested-With,Accept,Accept-Encoding");
		// 减少预检请求的次数
		response.setHeader("Access-Control-Max-Age", "3600");
		String verb = request.getVerb();
		if ("OPTIONS".equals(verb)) {
			/*
			 * 可能是跨域的预检请求（preflight request）
			 * 
			 * @see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
			 */
			if (!StringUtil.isEmpty(request.getHeaders().get("Access-Control-Request-Method"))) {
				response.setStatus(RestfulResponse.STATUS_OK);
			} else {
				response.setStatus(RestfulResponse.STATUS_BAD_REQUEST);
			}
			response.openOutput().close();
			return;
		}
		String path = request.getUri();
		if (path.startsWith("/_tool/")) {
			tool(7, path, request, response);
		} else if (path.startsWith("/tool/")) {
			tool(6, path, request, response);
		} else if (path.startsWith("/ca/")) {
			String name = path.substring(4);
			if (StringUtil.eq(name, "sshpublickey")) {
				outFile(CaUtil.getSshPublicKey(), response);
			} else {
				response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
				response.openOutput().close();
			}
		} else if (path.startsWith("/dist/")) {
			dist(path, request, response);
		} else {
			response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
			response.openOutput().close();
		}

	}

	private void dist(String path, RestfulRequest request, RestfulResponse response) throws IOException {
		path = path.substring("/dist/".length());
		String arr[] = path.split("/");
		if (arr.length == 2) {
			String group = arr[0];
			String project = arr[1];
			if (StringUtil.eq(project, "__upload")) {
				upload(path, group, request, response);
				return;
			}
			JSONArray array = new JSONArray();
			List<File> list = listFile(group + File.separator + project);
			if (!list.isEmpty()) {
				JSONObject o = new JSONObject();
				File f = list.get(0);
				o.put("name", f.getName());
				long m = f.lastModified();
				if (f.isDirectory()) {
					File[] fs = f.listFiles();
					if (null != fs) {
						for (File child : fs) {
							m = Math.max(m, child.lastModified());
						}
					}
				}
				o.put("time", TimeUtil.formatDateTime(new Date(m)));
				array.put(o);
				for (int i = 1; i < list.size(); i++) {
					f = list.get(i);
					o = new JSONObject();
					o.put("name", f.getName());
					m = f.lastModified();
					if (f.isDirectory()) {
						File[] fs = f.listFiles();
						if (null != fs) {
							for (File child : fs) {
								m = Math.max(m, child.lastModified());
							}
						}
					}
					o.put("time", TimeUtil.formatDateTime(new Date(m)));
					array.put(o);
				}
			}
			response.setHeader("content-type", "application/json");
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write(array.toString().getBytes());
			}
		} else if (arr.length == 3) { // 如:"/java/crm/1.0";
			String group = arr[0];
			String project = arr[1];
			String tag = arr[2];
			JSONArray array = new JSONArray();
			List<File> list = listFile(group + File.separator + project + File.separator + tag + File.separator);
			if (!list.isEmpty()) {
				JSONObject o = new JSONObject();
				File f = list.get(0);
				o.put("name", f.getName());
				o.put("time", TimeUtil.formatDateTime(new Date(f.lastModified())));
				array.put(o);
				for (int i = 1; i < list.size(); i++) {
					f = list.get(i);
					o = new JSONObject();
					o.put("name", f.getName());
					o.put("time", TimeUtil.formatDateTime(new Date(f.lastModified())));
					array.put(o);
				}
			}
			response.setHeader("content-type", "application/json");
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write(array.toString().getBytes());
			}
			return;
		} else if (arr.length == 4) {// 如:"/java/crm/1.0/crm.jar";
			String group = arr[0];
			String project = arr[1];
			String tag = arr[2];
			String name = arr[3];
			String method = request.getVerb();
			String file = group + File.separator + project + File.separator + tag + File.separator + name;
			if ("get".equalsIgnoreCase(method)) {
				File f = getFile(file);
				outFile(f, response);
				return;
			} else if ("POST".equalsIgnoreCase(method)) {
				InputStream in = request.getContent();
				File f = getFile(file);
				WebFileUpload fileUpload = new WebFileUpload(Arrays.asList(SimpleKvPair.valueOf("file", f)));
				// 处理上传流
				fileUpload.input(in);
				WebForm form = fileUpload.getForm("file");
				if (null == form) {
					response.setStatus(RestfulResponse.STATUS_BAD_REQUEST);
					response.openOutput().close();
					return;
				}
				// setFile(file, form.getStream());
				response.setStatus(RestfulResponse.STATUS_OK);
				response.openOutput().close();
				return;
			} else {
				response.setStatus(RestfulResponse.STATUS_BAD_REQUEST);
				response.openOutput().close();
				return;
			}
		} else {
			response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
			response.openOutput().close();
		}
	}

	private void upload(String path, String group, RestfulRequest request, RestfulResponse response)
			throws IOException {
		String method = request.getVerb();
		if ("get".equalsIgnoreCase(method)) {
			StringBuilder sb = new StringBuilder();
			sb.append("<form action=\"__upload\" enctype=\"multipart/form-data\" method=\"post\">");
			sb.append("项目:<input type=\"text\" name=\"project\"><br/>");
			sb.append("标签:<input type=\"text\" name=\"tag\"><br/>");
			sb.append("文件:<input type=\"file\" name=\"file\"><br/>");
			sb.append("<input type=\"submit\" value=\"提交\">");
			sb.append("</form>");
			try (OutputStream out = response.openOutput()) {
				response.setStatus(RestfulResponse.STATUS_OK);
				text(response, out, sb.toString());
			}
			return;
		} else if ("POST".equalsIgnoreCase(method)) {
			InputStream in = request.getContent();
			File tmp = null;
			try {
				tmp = getFile("/_temp/" + System.currentTimeMillis() + "-" + m_TempCount.incrementAndGet() + ".tmp");
				WebFileUpload fileUpload = new WebFileUpload(Arrays.asList(SimpleKvPair.valueOf("file", tmp)));
				// 处理上传流
				fileUpload.input(in);
				WebForm form = fileUpload.getForm("file");
				WebForm project = fileUpload.get("project");
				WebForm tag = fileUpload.get("tag");
				if (null == form || null == project || null == tag) {
					try (OutputStream out = response.openOutput()) {
						response.setStatus(RestfulResponse.STATUS_OK);
						text(response, out, "error params");
					}
					return;
				}
				String file = group + File.separator + checkPath(project.getString()) + File.separator
						+ checkPath(tag.getString()) + File.separator + form.getFileName();
				File real = getFile(file);
				File dir = real.getParentFile();
				dir.mkdirs();
				tmp.renameTo(real);
			} finally {
				if (null != tmp) {
					tmp.delete();
				}
			}
			// setFile(file, form.getStream());
			try (OutputStream out = response.openOutput()) {
				response.setStatus(RestfulResponse.STATUS_OK);
				text(response, out, "ok");
			}
			return;
		}

	}

	private static String checkPath(String v) {
		if (v.indexOf('/') >= 0 || v.indexOf('\\') >= 0) {
			throw new UnsupportedOperationException("不支持\\和/");
		}
		return v;
	}

	private void text(RestfulResponse response, OutputStream out, String msg) throws IOException {
		response.setHeader("Content-Type", "text/html;charset=utf-8");
		out.write("<!DOCTYPE html>".getBytes());
		out.write("<html>".getBytes());
		out.write("<head>".getBytes());
		out.write("<meta charset=\"utf-8\">".getBytes());
		out.write(
				"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,user-scalable=no,viewport-fit=cover\">"
						.getBytes());
		out.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">".getBytes());
		out.write("</head>".getBytes());
		out.write("<body>".getBytes());
		out.write(msg.getBytes());
		out.write("</body>".getBytes());
		out.write("</html>".getBytes());
	}

	private void outFile(File f, RestfulResponse response) throws IOException {
		if (null == f) {
			response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
			response.openOutput().close();
			return;
		}
		response.setStatus(RestfulResponse.STATUS_OK);
		try (OutputStream out = response.openOutput(); FileInputStream in = new FileInputStream(f)) {
			// NIO transfer
			FileChannel channel = in.getChannel();
			channel.transferTo(0, channel.size(), (WritableByteChannel) out);
		}
	}

	private void tool(int offset, String path, RestfulRequest request, RestfulResponse response) throws IOException {
		// 内部工具类
		String name = path.substring(offset);
		String jar = null;
		try {
			// 看看是不是基本springboot的fatjar
			CodeSource cs = Class.forName("org.springframework.boot.loader.JarLauncher").getProtectionDomain()
					.getCodeSource();
			if (null != cs) {
				URL url = cs.getLocation();
				if (null != url) {
					jar = UrlUtil.decodeUrl(url.getFile());
				}
			}
		} catch (SecurityException e) {
		} catch (ClassNotFoundException e) {
		}
		if (StringUtil.isEmpty(jar)) {
			URL url = getDefaultClassLoader().getResource("tool/" + name);
			if (null != url) {
				File file = FileResources.get(url.getFile());
				if (file.exists()) {
					response.setStatus(RestfulResponse.STATUS_OK);
					try (InputStream in = new FileInputStream(file); OutputStream out = response.openOutput()) {
						byte[] bs = new byte[1024];
						int l;
						while (-1 != (l = in.read(bs))) {
							out.write(bs, 0, l);
						}
					}
					return;
				}
			}
		} else {
			JarFile file = JarResources.get(jar);
			JarEntry entry = file.getJarEntry("BOOT-INF/classes/tool/" + name);
			if (null == entry) {
				response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
				response.openOutput().close();
				return;
			}
			response.setStatus(RestfulResponse.STATUS_OK);
			try (InputStream in = file.getInputStream(entry); OutputStream out = response.openOutput()) {
				byte[] bs = new byte[1024];
				int l;
				while (-1 != (l = in.read(bs))) {
					out.write(bs, 0, l);
				}
			}
			return;
		}
		response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
		response.openOutput().close();
		return;
	}

	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = DistServiceImpl.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap
				// ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the
					// caller can live with null...
				}
			}
		}
		return cl;
	}

	private List<File> listFile(String dir) {
		File file = getFile(dir);
		if (file.exists()) {
			return Arrays.asList(file.listFiles());
		}
		return Collections.emptyList();
	}

	private File getFile(String filepath) {
		String org = null;// TODO
		File file;
		if (StringUtil.isEmpty(org)) {
			file = new File(m_DistPath + filepath);
		} else {
			file = new File(m_DistPath + org + File.separator + filepath);
		}
		if (file.exists()) {
			return file;
		}
		return null;
	}

	@Override
	public void timeout(RestfulRequest request, RestfulResponse response) throws IOException {

	}
}
