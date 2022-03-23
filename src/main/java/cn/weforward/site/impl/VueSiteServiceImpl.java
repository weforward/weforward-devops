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
package cn.weforward.site.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.weforward.common.Dictionary;
import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.util.FileUtil;
import cn.weforward.common.util.StringUtil;
import cn.weforward.site.Resource;
import cn.weforward.site.SiteService;

/**
 * 基于vue的站点服务
 * 
 * 结构大概如下
 * 
 * <pre>
 *  devops
 *    index.html
 *    js
 *    css
 * 
 * </pre>
 * 
 * @author daibo
 *
 */
public class VueSiteServiceImpl implements RestfulService, SiteService {
	/** 站点服务 */
	protected String m_SitePath;
	/** 网关地址 */
	protected String m_ApiUrl;
	/** 服务名 */
	protected String m_ServiceName;
	/** config文件 */
	private Resource m_Config;

	public VueSiteServiceImpl(String path) {
		m_SitePath = path;
	}

	public void setApiUrl(String url) {
		m_ApiUrl = url;
	}

	public void setServiceName(String name) {
		m_ServiceName = name;
	}

	private Resource getConfig() throws IOException {
		if (null == m_Config && !StringUtil.isEmpty(m_ApiUrl)) {
			StringBuilder sb = new StringBuilder();
			String[] arr = m_ApiUrl.split(";");
			if (arr.length == 1) {
				sb.append("\"");
				int start = arr[0].indexOf("://");
				int end = arr[0].lastIndexOf("/");
				sb.append(arr[0].substring(start + 1, end));
				sb.append("\"");
			} else {
				sb.append("\"");
				int start = arr[0].indexOf("://");
				int end = arr[0].lastIndexOf("/");
				sb.append(arr[0].substring(start + 1, end));
				sb.append("\"");
				for (int i = 1; i < arr.length; i++) {
					sb.append(",\"");
					start = arr[i].indexOf("://");
					end = arr[i].lastIndexOf("/");
					sb.append(arr[i].substring(start + 1, end));
					sb.append("\"");
				}
			}
			String result = "window._WEFORWARD_CONFIG={\"hosts\":[" + sb.toString() + "],\"serviceName\":\"devops:"
					+ m_ServiceName + "\"};";
			m_Config = new StringResource(result);
		}
		return m_Config;
	}

	private Resource findFile(String name) throws IOException {
		String path = FileUtil.getAbsolutePath(name, m_SitePath);
		return FileResources.get(path);
	}

	@Override
	public void precheck(RestfulRequest request, RestfulResponse response) throws IOException {

	}

	@Override
	public void service(RestfulRequest request, RestfulResponse response) throws IOException {
		response.setHeader("WF-Biz", "site");
		String path = request.getUri();
		int index = path.lastIndexOf('.');
		Resource file = null;
		if (index < 0) {
			file = findFile("index.html");
		} else {
			// 结构为 /项目名/js/wfconfig.js
			int i = 0;
			int max = 2;
//			if (path.contains("/js/") || path.contains("/css/") || path.contains("/img/")) {
//				max = 2;
//			} else {
//				max = 1;
//			}
			int count = 0;
			for (; i < path.length() && count < max; i++) {
				if (path.charAt(i) == '/') {
					count++;
				}
			}
			String name = path.substring(i);
			if (name.endsWith("wfconfig.js")) {
				file = getConfig();
			} else {
				file = findFile(name);
			}
		}

		outFile(file, request, response);
	}

	private void outFile(Resource file, RestfulRequest request, RestfulResponse response) throws IOException {
		if (null == file || !file.exists()) {
			response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
			response.openOutput().close();
			return;
		}
		String modified = getHeader(request, "if-modified-since");
		if (!StringUtil.isEmpty(modified)) {
			if (StringUtil.eq(modified, file.getLastModified())) {
				response.setStatus(RestfulResponse.STATUS_NOT_MODIFIED);
				response.openOutput().close();
				return;
			}
		}
		response.setHeader("Last-Modified", file.getLastModified());
		String suffix = getSuffix(file.getName());
		String type = ContentType.get(suffix);
		if (!StringUtil.isEmpty(type)) {
			response.setHeader("Content-Type", type);
		}
		response.setStatus(RestfulResponse.STATUS_OK);
		try (OutputStream out = response.openOutput(); InputStream in = file.getStream()) {
			byte[] bs = new byte[1024];
			int l;
			while (-1 != (l = in.read(bs))) {
				out.write(bs, 0, l);
			}
		}
	}

	private static String getSuffix(String name) {
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return null;
		}
		int split = name.lastIndexOf('\\');
		if (split > index) {
			return null;
		}
		return name.substring(index);
	}

	/* 获取头信息 */
	private static String getHeader(RestfulRequest request, String name) {
		Dictionary<String, String> headers = request.getHeaders();
		if (null == headers) {
			return "";
		}
		return headers.get(name);
	}

	@Override
	public void timeout(RestfulRequest request, RestfulResponse response) throws IOException {

	}

}
