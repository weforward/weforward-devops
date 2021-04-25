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
package cn.weforward.devops.project.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.annotation.Inherited;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.Reloadable;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.OpProcessor;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.Prop;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractMachine;
import cn.weforward.devops.user.Organization;
import cn.weforward.util.CaUtil;
import cn.weforward.util.HttpInvoker;

/**
 * 代理机器
 * 
 * @author daibo
 *
 */
@Inherited
public class ProxyMachine extends AbstractMachine implements Reloadable<ProxyMachine> {

	/** 主机 */
	@ResourceExt(component = Host.class)
	protected List<Host> m_Hosts;
	/** 页面目录 */
	@Resource
	protected String m_Root;
	/** 密码 */
	@Resource
	protected String m_Password;
	/** ssh操作客户端 */
	protected JSch m_JSch;

	private HttpInvoker m_Invoker;

	protected ProxyMachine(ProjectDi di) {
		super(di);
	}

	public ProxyMachine(ProjectDi di, Organization org, String name, String url, String root, String password) {
		super(di, org, name);
		genPersistenceId();
		setUrl(url);
		m_Root = root;
		m_Password = password;
		markPersistenceUpdate();
	}

	private HttpInvoker getInvoker() throws IOException {
		if (null == m_Invoker) {
			m_Invoker = new HttpInvoker(3, 10);
		}
		return m_Invoker;
	}

	/**
	 * 服务器链接
	 * 
	 * @param url user@host:port
	 */
	public void setUrl(String urls) {
		if (StringUtil.isEmpty(urls)) {
			m_Hosts = Collections.emptyList();
			markPersistenceUpdate();
			return;
		}
		String[] arr = urls.split("\\;");
		List<Host> hosts = new ArrayList<>(arr.length);
		for (String url : arr) {
			String user = "";
			String host = "";
			int port = 22;
			int begin = 0;
			int end = url.length();
			int index = url.indexOf("@");
			if (index >= 0) {
				user = url.substring(0, index);
				begin = index + 1;
			}
			index = url.lastIndexOf(":");
			if (index >= 0) {
				port = NumberUtil.toInt(url.substring(index + 1), 22);
				end = index;
			}
			host = url.substring(begin, end);
			hosts.add(new Host(user, host, port));
		}
		m_Hosts = hosts;
		markPersistenceUpdate();

	}

	/** 页面目录 */
	public void setRoot(String v) {
		if (StringUtil.eq(m_Root, v)) {
			return;
		}
		m_Root = v;
		markPersistenceUpdate();
	}

	/** 密码 */
	public void setPassword(String v) {
		if (StringUtil.eq(m_Password, v)) {
			return;
		}
		m_Password = v;
		markPersistenceUpdate();
	}

	/* 获取客户端 */
	private JSch getJSch() throws JSchException {
		if (null == m_JSch) {
			m_JSch = new JSch();
			for (File file : CaUtil.getSshPrvKeys()) {
				_Logger.info("addIdentity" + file.getAbsolutePath());
				m_JSch.addIdentity(file.getAbsolutePath());

			}
		}
		return m_JSch;
	}

	/* 开启会话 */
	private List<Session> openSessions() throws JSchException {
		List<Host> hosts = getHosts();
		List<Session> list = new ArrayList<>(hosts.size());
		for (Host host : hosts) {
			Session session = getJSch().getSession(host.m_User, host.m_Host, host.m_Port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			if (!StringUtil.isEmpty(m_Password)) {
				session.setPassword(m_Password);
			}
			session.setConfig(config);
			session.setUserInfo(DEFAULT_USERINFO);
			list.add(session);
		}
		return list;
	}

	public List<Host> getHosts() {
		return m_Hosts;
	}

	@Override
	public List<Prop> getProps() {
		List<Prop> prop = new ArrayList<>();
		List<Host> hosts = getHosts();
		if (hosts.size() == 0) {
			prop.add(new Prop("url", ""));
		} else if (hosts.size() == 1) {
			prop.add(new Prop("url", hosts.get(0).toString()));
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(hosts.get(0));
			for (int i = 1; i < hosts.size(); i++) {
				sb.append(";");
				sb.append(hosts.get(i));
			}
			prop.add(new Prop("url", sb.toString()));
		}
		if (!StringUtil.isEmpty(m_Root)) {
			prop.add(new Prop("root", m_Root));
		}
		return prop;
	}

	@Override
	public void upgrade(Running running, List<Env> userenvs, List<Bind> userbinds, String version,
			OpProcessor processor) {
		version = VersionInfo.getVersion(version);
		Project project = running.getProject();
		String cname = project.getName();
		processor(processor, "开始升级项目" + cname + "/" + version);
		String url = getBusinessDi().getProxyDistUrl() + cname + "/" + version;
		File dstPath = new File(System.getProperty("java.io.tmpdir") + "/devops-" + cname + "-" + version);
		if (!dstPath.exists()) {
			dstPath.mkdir();
		}
		org.apache.http.HttpResponse response = null;
		try {
			processor(processor, "开始导出项目文件");
			HttpGet get = new HttpGet(url + "/file.zip");
			response = getInvoker().execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new IOException("响应码异常" + status);
			}
			File file = new File(dstPath.getAbsolutePath() + File.separator + "file.zip");
			saveFile(file, response.getEntity().getContent());
			unZipFiles(file, dstPath.getAbsolutePath());
			file.delete();
			processor(processor, "成功导出项目文件");
		} catch (Exception e) {
			processor(processor, "升级异常，拉取文件出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				_Logger.error("忽略关闭异常", e);
			}
		}
		List<Session> sessions = Collections.emptyList();
		try {
			processor(processor, "开始链接服务器");
			sessions = openSessions();
			for (Session session : sessions) {
				session.connect(10000);
			}
			processor(processor, "成功链接服务器");
			for (int i = 0; i < sessions.size(); i++) {
				Session session = sessions.get(i);
				processor(processor, "上传项目文件到session" + (i + 1));
				Channel channel = session.openChannel("sftp");
				channel.connect(10000);
				ChannelSftp sftp = (ChannelSftp) channel;
				channel = session.openChannel("shell");
				channel.connect(10000);
				ChannelShell shell = (ChannelShell) channel;
				String projectdir = m_Root + "/" + fixIfNeed(cname);
				mkdirIfNoExists(sftp, projectdir);
				String currentProjectDir = projectdir + "/" + version;
				mkdirIfNoExists(sftp, currentProjectDir);
				for (File src : dstPath.listFiles()) {
					upload(sftp, src.getAbsolutePath(), currentProjectDir, processor);
				}
				processor(processor, "成功上传项目文件到session" + (i + 1));
				String latest = projectdir + "/latest";
				String olddir = projectdir + "/old";
				rmdirIfExists(sftp, shell, olddir, processor);
				renameIfExists(sftp, latest, olddir, processor);
				lnIfExists(sftp, shell, "./" + version, latest, processor);
			}
			processor(processor, "项目升级成功");
		} catch (JSchException | SftpException | IOException e) {
			processor(processor, "升级异常，上传文件到服务器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		} finally {
			dstPath.deleteOnExit();
			for (Session s : sessions) {
				if (s.isConnected()) {
					s.disconnect();
				}
			}
		}
	}

	/**
	 * 解压文件到指定目录 解压后的文件名，和之前一致
	 * 
	 * @param zipFile 待解压的zip文件
	 * @param descDir 指定目录
	 */
	public static void unZipFiles(File file, String descDir) throws IOException {
		ZipFile zip = null;
		try {
			zip = new ZipFile(file);
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				String outPath = (pathFile.getAbsolutePath() + File.separator + zipEntryName);
				File outFile = new File(outPath);
				if (entry.isDirectory()) {
					outFile.mkdirs();
				} else {
					File parent = outFile.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					InputStream in = zip.getInputStream(entry);
					FileOutputStream out = new FileOutputStream(outPath);
					byte[] bs = new byte[1024];
					int len;
					while ((len = in.read(bs)) > 0) {
						out.write(bs, 0, len);
					}
					in.close();
					out.close();
				}
			}
		} finally {
			if (null != zip) {
				zip.close();
			}
		}
	}

	private void saveFile(File file, InputStream in) throws IOException {
		if (file.exists()) {
			file.delete();
		} else {
			File dir = file.getParentFile();
			dir.mkdirs();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] bs = new byte[1024];
			int l;
			while ((l = in.read(bs)) != -1) {
				out.write(bs, 0, l);
			}
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (Throwable e) {
			}
		}
	}

	private static String fixIfNeed(String cname) {
		if (cname.startsWith("hy-") || cname.startsWith("wf-")) {
			return cname.substring(3);
		}
		return cname;
	}

	@Override
	public void rollback(Project project, OpProcessor processor) {
		List<Session> sessions = Collections.emptyList();
		try {
			processor(processor, "开始链接服务器");
			sessions = openSessions();
			for (Session session : sessions) {
				session.connect(10000);
			}
			processor(processor, "成功链接服务器");
			String cname = project.getName();
			String projectdir = m_Root + "/" + fixIfNeed(cname);
			String latest = projectdir + "/latest";
			String olddir = projectdir + "/old";
			String bakdir = projectdir + "/bak";
			for (Session session : sessions) {
				Channel channel = session.openChannel("sftp");
				channel.connect(10000);
				ChannelSftp sftp = (ChannelSftp) channel;
				channel = session.openChannel("shell");
				channel.connect(10000);
				ChannelShell shell = (ChannelShell) channel;
				if (!exists(sftp, olddir)) {
					processor(processor, "回滚异常，不存在old目录");
					return;
				}
				rmdirIfExists(sftp, shell, bakdir, processor);
				renameIfExists(sftp, latest, bakdir, processor);
				renameIfExists(sftp, olddir, latest, processor);
			}
			processor(processor, "项目回滚成功");
		} catch (JSchException | SftpException | IOException e) {
			processor(processor, "升级异常，上传文件到服务器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		} finally {
			for (Session s : sessions) {
				if (s.isConnected()) {
					s.disconnect();
				}
			}
		}

	}

	@Override
	public NameItem queryState(Project project) {
		if (project instanceof HtmlProject) {
			for (String uri : ((HtmlProject) project).getAccessUrls()) {
				URL url;
				try {
					url = new URL(uri);
				} catch (MalformedURLException e) {
					continue;// 格式不对的uri忽略
				}
				HttpURLConnection response = null;
				try {
					response = (HttpURLConnection) url.openConnection();
					response.setConnectTimeout(1000);
					response.setReadTimeout(1000);
					response.connect();
					int code = response.getResponseCode();
					if (code == 200) {
						return Running.STATE_RUNNING;
					} else {
						return Running.STATE_STOPED;
					}
				} catch (IOException e) {
					_Logger.warn("查询状态异常", e);
					return Running.STATE_UNKNOWN.changeValue("异常状态:" + e.getMessage());
				} finally {
					try {
						if (null != response) {
							response.disconnect();
						}
					} catch (Exception e) {
						_Logger.warn("忽略关闭异常", e);
					}
				}
			}

		}
		return Running.STATE_UNKNOWN;
	}

	@Override
	public VersionInfo queryCurrentVersion(Running running) {
		ChannelSftp sftp;
		List<Session> sessions = Collections.emptyList();
		Project project = running.getProject();
		try {
			sessions = openSessions();
			for (Session session : sessions) {
				session.connect();
				sftp = (ChannelSftp) session.openChannel("sftp");
				sftp.connect();
				String projectdir = m_Root + "/" + fixIfNeed(project.getName()) + "/latest";
				String v = sftp.realpath(projectdir);
				return new VersionInfo(v.substring(v.lastIndexOf("/") + 1));
			}
			return new VersionInfo("no version");
		} catch (JSchException | SftpException e) {
			return new VersionInfo("error:" + e.getMessage());
		} finally {
			for (Session s : sessions) {
				if (s.isConnected()) {
					s.disconnect();
				}
			}
		}

	}

	@Override
	public ResultPage<String> queryLogs(Project project) {
		return ResultPageHelper.empty();
	}

	@Override
	public void clear(Project project, int maxHistory) {
		List<Session> sessions = Collections.emptyList();
		try {
			sessions = openSessions();
			for (Session session : sessions) {
				session.connect(10000);
			}
		} catch (Exception e) {
			throw new RuntimeException("链接服务器异常", e);
		}
		try {
			String cname = project.getName();
			String projectdir = m_Root + "/" + fixIfNeed(cname);
			for (int i = 0; i < sessions.size(); i++) {
				Session session = sessions.get(i);
				Channel channel = session.openChannel("sftp");
				channel.connect(10000);
				ChannelSftp sftp = (ChannelSftp) channel;
				@SuppressWarnings("unchecked")
				Vector<ChannelSftp.LsEntry> vs = (Vector<ChannelSftp.LsEntry>) sftp.ls(projectdir);
				List<ChannelSftp.LsEntry> list = new ArrayList<>();

				for (ChannelSftp.LsEntry l : vs) {
					String[] arr = l.getFilename().split("\\.");
					boolean isVersion;
					if (arr.length > 0) {
						isVersion = true;
						for (String v : arr) {
							if (!NumberUtil.isNumber(v)) {
								isVersion = false;
								break;
							}
						}
					} else {
						isVersion = false;
					}
					if (isVersion) {
						list.add(l);
					}
				}
				Collections.sort(list, _BY_REVEISION);
				if (list.size() <= maxHistory) {
					continue;
				}
				channel = session.openChannel("shell");
				channel.connect(10000);
				ChannelShell shell = (ChannelShell) channel;
				for (ChannelSftp.LsEntry e : list.subList(maxHistory, list.size())) {
					String path = projectdir + "/" + e.getFilename();
					rmdirIfExists(sftp, shell, path, null);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("执行命令服务器异常", e);
		} finally {
			for (Session s : sessions) {
				if (s.isConnected()) {
					s.disconnect();
				}
			}
		}

	}

	/* 移除 */
	private void rmdirIfExists(ChannelSftp sftp, ChannelShell shell, String path, OpProcessor processor)
			throws SftpException, IOException {
		SftpATTRS attrs = null;
		try {
			attrs = sftp.stat(path);
		} catch (Exception e) {
		}
		if (attrs != null) {
			OutputStream outstream = shell.getOutputStream();
			InputStream instream = shell.getInputStream();
			String cmd = "rm -r " + path + "\n";
			processor(processor, cmd);
			outstream.write((cmd).getBytes());
			outstream.flush();
			out: for (int i = 0; i < 30; i++) {
				String back = read(instream);
				if (!StringUtil.isEmpty(back)) {
					String[] arr = back.split("\r\n");
					for (int index = 0; index < arr.length; index++) {
						String v = arr[index];
						if (v.contains("~$ ") && index == arr.length - 1) {
							break out;
						}
						if (!StringUtil.isEmpty(v)) {
							// processor(processor, v);
						}
					}
				} else {
					synchronized (shell) {
						try {
							shell.wait(10000);
						} catch (InterruptedException e) {
							throw new IOException("正常中断");
						}
					}
				}
			}
		}
	}

	/* 移除 */
	private void lnIfExists(ChannelSftp sftp, ChannelShell shell, String oldpath, String newpath, OpProcessor processor)
			throws SftpException, IOException {
//		SftpATTRS attrs = null;
//		try {
//			attrs = sftp.stat(oldpath);
//		} catch (Exception e) {
//		}
//		if (attrs != null) {
		OutputStream outstream = shell.getOutputStream();
		InputStream instream = shell.getInputStream();
		String cmd = "ln -s " + oldpath + " " + newpath + "\n";
		processor(processor, cmd);
		outstream.write((cmd).getBytes());
		outstream.flush();
		out: for (int i = 0; i < 30; i++) {
			String back = read(instream);
			if (!StringUtil.isEmpty(back)) {
				String[] arr = back.split("\r\n");
				for (int index = 0; index < arr.length; index++) {
					String v = arr[index];
					if (v.contains("~$ ") && index == arr.length - 1) {
						break out;
					}
					if (!StringUtil.isEmpty(v)) {
						// processor(processor, v);
					}
				}
			} else {
				synchronized (shell) {
					try {
						shell.wait(10000);
					} catch (InterruptedException e) {
						throw new IOException("正常中断");
					}
				}
			}
		}
		// }
	}

	private static String read(InputStream instream) throws IOException {
		// 获取命令执行的结果
		if (instream.available() > 0) {
			byte[] data = new byte[instream.available()];
			int nLen = instream.read(data);
			if (nLen < 0) {
				throw new IOException("network error.");
			}
			// 转换输出结果并打印出来
			return new String(data, 0, nLen);
		} else {
			return null;
		}
	}

	/* 目录是否存在 */
	private static boolean exists(ChannelSftp sftp, String path) throws SftpException {
		SftpATTRS attrs = null;
		try {
			attrs = sftp.stat(path);
		} catch (Exception e) {
		}
		return (attrs != null);
	}

	/* 创建目录 */
	private static void mkdirIfNoExists(ChannelSftp sftp, String path) throws SftpException {
		SftpATTRS attrs = null;
		try {
			attrs = sftp.stat(path);
		} catch (Exception e) {
		}
		if (attrs == null) {
			sftp.mkdir(path);
		}
	}

	/* 重命名文件 */
	private void renameIfExists(ChannelSftp sftp, String oldpath, String newpath, OpProcessor processor)
			throws SftpException {
		SftpATTRS attrs = null;
		try {
			attrs = sftp.stat(oldpath);
		} catch (Exception e) {
		}
		if (attrs == null) {
			return;
		}
		sftp.rename(oldpath, newpath);
		processor(processor, "mv " + oldpath + "  " + newpath);
	}

	/* 上传文件 */
	private void upload(ChannelSftp c, String src, String dest, OpProcessor processor) throws SftpException {
		File file = new File(src);
		if (file.isDirectory()) {
			String dir = dest + "/" + file.getName();
			// processor(processor, "mkdir " + dir);
			mkdirIfNoExists(c, dir);
			for (String f : file.list(FILETER)) {
				String csrc = src + "/" + f;
				String cdest = dest + "/" + file.getName();
				upload(c, csrc, cdest, processor);
			}
		} else {
			String srcfile = file.getAbsolutePath();
			String destfile = dest + "/" + file.getName();
			// processor(processor, "upload " + srcfile + " to " + destfile);
			c.put(srcfile, destfile, ChannelSftp.OVERWRITE);
		}

	}

	public static class Host {

		/** 服务器地址 */
		@Resource
		protected String m_Host;
		/** 用户 */
		@Resource
		protected String m_User;
		/** 端口 */
		@Resource
		protected int m_Port;

		public Host() {

		}

		public Host(String user, String host, int port) {
			m_User = user;
			m_Host = host;
			m_Port = port;
		}

		@Override
		public String toString() {
			return m_User + "@" + m_Host + ":" + m_Port;
		}
	}

	/** 要排除的目录 */
	private static final List<String> EXCLUDE = Arrays.asList(".svn", "classes");
	/** 过滤器 */
	private static final FilenameFilter FILETER = new FilenameFilter() {

		public boolean accept(File dir, String name) {
			return !EXCLUDE.contains(name);
		}
	};
	/** 用户信息 */
	private static final UserInfo DEFAULT_USERINFO = new UserInfo() {

		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return null;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptYesNo(String message) {
			return true;
		}

		public void showMessage(String message) {
		}
	};

	/** 版本排序 */
	public final static Comparator<ChannelSftp.LsEntry> _BY_REVEISION = new Comparator<ChannelSftp.LsEntry>() {

		@Override
		public int compare(ChannelSftp.LsEntry o1, ChannelSftp.LsEntry o2) {
			String[] arr1 = o1.getFilename().split("\\.");
			String[] arr2 = o2.getFilename().split("\\.");
			int l = Math.min(arr1.length, arr2.length);
			for (int i = 0; i < l; i++) {
				int v = NumberUtil.toInt(arr2[i], 0) - NumberUtil.toInt(arr1[i], 0);
				if (v == 0) {
					continue;
				}
				return v;
			}
			int v = arr2.length - arr1.length;
			if (v == 0) {

				return o2.getFilename().compareTo(o1.getFilename());
			}
			return v;
		}
	};

	@Override
	public boolean onReloadAccepted(Persister<ProxyMachine> persister, ProxyMachine other) {
		m_Name = other.m_Name;
		m_Binds = other.m_Binds;
		m_Envs = other.m_Envs;
		m_Owner = other.m_Owner;
		m_GroupRights = other.m_GroupRights;
		m_Hosts = other.m_Hosts;
		m_Root = other.m_Root;
		m_Password = other.m_Password;
		return true;
	}
}
