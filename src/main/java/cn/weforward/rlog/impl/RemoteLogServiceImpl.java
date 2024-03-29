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
package cn.weforward.rlog.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.Destroyable;
import cn.weforward.common.ResultPage;
import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.sys.Timestamp;
import cn.weforward.common.util.FileUtil;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.array.Label;
import cn.weforward.data.array.LabelSet;
import cn.weforward.data.array.LabelSetFactory;
import cn.weforward.data.util.FieldMapper;
import cn.weforward.devops.user.Organization;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.rlog.Content;
import cn.weforward.rlog.Directory;
import cn.weforward.rlog.LogPage;
import cn.weforward.rlog.RemoteLogService;
import cn.weforward.rlog.Server;
import cn.weforward.rlog.Subject;
import cn.weforward.util.HttpAccessAuth;

/**
 * 远程日志类
 * 
 * @author daibo
 *
 */
public class RemoteLogServiceImpl implements RestfulService, RemoteLogService, Destroyable {

	/** 日志 */
	private static final Logger _Logger = LoggerFactory.getLogger(RemoteLogItemImpl.class);
	/** 日志文件后缀 */
	public static final String LOG_SUFFIX = ".log";
	/** 字符编码 */
	public static final Charset CHARSET = Charset.forName("utf-8");
	/** 日志级别-追踪 */
	public static final int LEVEL_TRACE = 5;
	/** 日志级别-调试 */
	public static final int LEVEL_DEBUG = 4;
	/** 日志级别-信息 */
	public static final int LEVEL_INFO = 3;
	/** 日志级别-警告 */
	public static final int LEVEL_WARN = 2;
	/** 日志级别-错误 */
	public static final int LEVEL_ERROR = 1;
	/** 日志级别-关闭 */
	public static final int LEVEL_OFF = 0;
	/** 日志级别 */
	public static int LEVEL = LEVEL_ERROR;

	private static final Comparator<Server> _ByName = new Comparator<Server>() {

		@Override
		public int compare(Server o1, Server o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return 1;
			}
			if (o2 == null) {
				return -1;
			}
			String n1 = StringUtil.toString(o1.getName());
			String n2 = StringUtil.toString(o2.getName());
			// int l = n1.length() - n2.length();
			// if (l == 0) {
			return n1.compareTo(n2);
			// }
			// return l;
		}
	};
	static {
		String v = System.getProperty("cn.weforward.rlog.level");
		if (!StringUtil.isEmpty(v)) {
			LEVEL = NumberUtil.toInt(v, LEVEL_ERROR);
		}
	}

	/** 时间戳 */
	protected static Timestamp m_Timestamp = Timestamp.getInstance(Timestamp.POLICY_DEFAULT);
	/** 标签集 */
	protected LabelSet<RemoteLogSubjectElement> m_Labels;
	/** 日志路径 */
	protected String m_LogPath;

	protected HttpAccessAuth m_Auth;

	public RemoteLogServiceImpl(LabelSetFactory factory, String logpath, HttpAccessAuth auth) {
		m_Labels = factory.createLabelSet("rlogsubject", FieldMapper.valueOf(RemoteLogSubjectElement.class));
		m_Auth = auth;
		String path = FileUtil.getAbsolutePath(logpath, null);
		m_LogPath = path.endsWith(File.separator) ? path : path + File.separator;
		File logfile = new File(m_LogPath);
		if (!logfile.exists()) {
			logfile.mkdirs();
		}
	}

	private static boolean isLevel(int l) {
		return LEVEL >= l;
	}

	public static boolean isTraceEnabled() {
		return isLevel(LEVEL_TRACE);
	}

	public static boolean isDebugEnabled() {
		return isLevel(LEVEL_DEBUG);
	}

	public static boolean isInfoEnabled() {
		return isLevel(LEVEL_INFO);
	}

	public static boolean isWarnEnabled() {
		return isLevel(LEVEL_WARN);
	}

	public static boolean isErrorEnabled() {
		return isLevel(LEVEL_ERROR);
	}

	private static String readRequest(RestfulRequest request) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			String json;
			in = request.getContent();
			bos = new ByteArrayOutputStream(4 * 1024);
			byte[] buf = new byte[1024];
			int len;
			while (-1 != (len = in.read(buf))) {
				bos.write(buf, 0, len);
			}
			buf = null;
			json = bos.toString("UTF-8");
			return json;
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != bos) {
				bos.close();
			}
		}
	}

	public static void trace(String msg) {
		if (isTraceEnabled()) {
			System.out.println(msg);
		}
	}

	public static void debug(String msg) {
		if (isDebugEnabled()) {
			System.out.println(msg);
		}
	}

	public static void info(String msg) {
		if (isInfoEnabled()) {
			System.out.println(msg);
		}

	}

	public static void warn(String msg, Throwable e) {
		if (isWarnEnabled()) {
			System.out.println(msg);
			e.printStackTrace(System.out);
		}
	}

	public static void error(String msg, Throwable e) {
		if (isErrorEnabled()) {
			System.err.println(msg);
			e.printStackTrace(System.err);
		}
	}

	public void write(String org, RemoteLogItem item) {
		if (isTraceEnabled()) {
			System.out.println(item.getServer() + "," + item.getSubject() + "," + item.getLevel());
		}
		String d = TimeUtil.formatCompactDate(new Date());
		try {
			String path = getLogPath(org) + item.getServer() + File.separator;
			Out out = Out.hit(path, d);
			String label = RemoteLogSubjectElement.genLabel(org, item.getServer(), d);
			String timestamp = String.valueOf(m_Timestamp.next(0));
			RemoteLogSubjectElement log = out.write(timestamp, item.getSubject(), item.getContent());
			if (item.getLevel().equalsIgnoreCase("ERROR")) {
				m_Labels.add(label, log);
			}
		} catch (IOException e) {
			error("写入错误", e);
		}
	}

	@Override
	public List<Server> listServer(Organization org) {
		File file = findDir(getLogPath(org.getId()));
		if (!file.exists()) {
			return Collections.emptyList();
		}
		String[] arr = file.list();
		if (null == arr) {
			return Collections.emptyList();
		}
		List<Server> list = new ArrayList<Server>();
		for (String v : arr) {
			list.add(new ServerVo(v));
		}
		Collections.sort(list, _ByName);
		return list;
	}

	@Override
	public ResultPage<Directory> listDirectory(Organization org, String server) {
		File file = findDir(getLogPath(org.getId()) + File.separator + server);
		if (!file.exists()) {
			return ResultPageHelper.empty();
		}
		String[] arr = file.list();
		if (null == arr) {
			return ResultPageHelper.empty();
		}
		Arrays.sort(arr, DirectoryVo._BY_NAME_DESC);
		ResultPage<Directory> result = new TransResultPage<Directory, String>(
				ResultPageHelper.toResultPage(Arrays.asList(arr))) {

			@Override
			protected Directory trans(String v) {
				String name = v.substring(0, v.length() - LOG_SUFFIX.length());
				Label<? extends RemoteLogSubjectElement> l = m_Labels
						.getLabel(RemoteLogSubjectElement.genLabel(org.getId(), server, name));
				int num = 0;
				if (null != l) {
					num = l.resultPage().getCount();
				}
				String path = getLogPath(org.getId()) + server + File.separator + v;
				return new DirectoryVo(name, path, num);
			}
		};
		return result;
	}

	@Override
	public ResultPage<Subject> listSubject(Organization org, String server, String directory) throws IOException {
		Label<RemoteLogSubjectElement> l = m_Labels
				.getLabel(RemoteLogSubjectElement.genLabel(org.getId(), server, directory));
		if (null == l) {
			return ResultPageHelper.empty();
		}
		ResultPage<Subject> rp = new TransResultPage<Subject, RemoteLogSubjectElement>(l.resultPage()) {

			@Override
			protected Subject trans(RemoteLogSubjectElement src) {
				return new SubjectVo(src);
			}
		};
		rp = ResultPageHelper.reverseResultPage(rp);
		return rp;
	}

	@Override
	public Content getContent(Organization org, String server, String directory, String subject) throws IOException {
		RemoteLogSubjectElement ele = m_Labels.get(RemoteLogSubjectElement.genLabel(org.getId(), server, directory),
				subject);
		if (null == ele) {
			return null;
		}
		long off = ele.getOffset();
		int length = ele.getLength();
		File file = sureDir(getLogPath(org.getId()) + File.separator + server);
		File log = new File(file, directory + LOG_SUFFIX);
		if (!log.exists()) {
			return null;
		}
		RandomAccessFile rfile = null;
		FileChannel channel = null;
		try {
			rfile = new RandomAccessFile(log, "r");
			channel = rfile.getChannel();
			ByteBuffer dst = ByteBuffer.allocate(length);
			int v = channel.read(dst, off);
			if (v <= 0) {
				return null;
			}
			dst.flip();
			byte[] bs = new byte[v];
			dst = dst.get(bs);
			return new ContentVo(new SubjectVo(ele), new String(bs, CHARSET));
		} finally {
			if (null != rfile) {
				try {
					rfile.close();
				} catch (IOException e) {
					warn("忽略关闭出错", e);
				}
			}
			if (null != channel && channel.isOpen()) {
				try {
					channel.close();
				} catch (IOException e) {
					warn("忽略关闭出错", e);
				}
			}
		}
	}

	@Override
	public LogPage getDetail(Organization org, String server, String directory) throws IOException {
		if (server.startsWith(".") || directory.startsWith(".")) {
			throw new IllegalArgumentException("参数不合法");
		}
		String logname = getLogPath(org.getId()) + server + File.separator + directory + LOG_SUFFIX;
		return RlogResultImpl.valueOf(logname);
	}

	static File sureDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			sureDir(file.getParent());
			file.mkdirs();
		}
		return file;
	}

	static File findDir(String path) {
		return new File(path);
	}

	public String getLogPath(String org) {
		return m_LogPath + org + File.separator;
	}

	static class Out {

		private String name;

		private long offset;

		private OutputStream logout;

		public static ConcurrentHashMap<String, Out> CACHE = new ConcurrentHashMap<String, RemoteLogServiceImpl.Out>();

		public static Out hit(String path, String name) throws FileNotFoundException {
			Out out = CACHE.get(path);
			if (null != out && StringUtil.eq(out.name, name)) {
				return out;
			}
			File dir = sureDir(path);
			Out newout = new Out();
			newout.name = name;
			File file = new File(dir, name + LOG_SUFFIX);
			newout.offset = file.length();
			newout.logout = new FileOutputStream(file, true);
			if (null != out) {
				out.close();
				CACHE.put(path, newout);
				return newout;
			} else {
				Out old = CACHE.putIfAbsent(path, newout);
				if (old == null) {
					return newout;
				}
				newout.close();
				return old;
			}
		}

		public RemoteLogSubjectElement write(String timestamp, String subject, String content) throws IOException {
			long off = offset;
			byte[] bytes = content.getBytes(CHARSET);
			int length = bytes.length;
			logout.write(bytes);
			// logout.flush();
			offset = offset + bytes.length;
			return new RemoteLogSubjectElement(timestamp, subject, off, length);
		}

		private void close() {
			if (null != logout) {
				try {
					logout.close();
				} catch (IOException e) {
					warn("忽略关闭出错", e);
				}
			}
		}
	}

	static class RemoteLogItemImpl implements RemoteLogItem {

		public String server;
		public String subject;
		public String content;
		public String level;

		@Override
		public String getServer() {
			return server;
		}

		@Override
		public String getSubject() {
			return subject;
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public String getLevel() {
			return StringUtil.toString(level);
		}

	}

	@Override
	public void precheck(RestfulRequest request, RestfulResponse response) throws IOException {
	}

	@Override
	public void service(RestfulRequest request, RestfulResponse response) throws IOException {
		response.setHeader("WF-Biz", "rlog");
		AccessExt access;
		try {
			access = m_Auth.auth(request, response);
		} catch (Throwable e) {
			response.setStatus(RestfulResponse.STATUS_SERVICE_UNAVAILABLE);
			response.openOutput().close();
			return;
		}
		if (null == access) {
			response.setStatus(RestfulResponse.STATUS_UNAUTHORIZED);
			response.openOutput().close();
			return;
		}
		String json = readRequest(request);
		if (StringUtil.isEmpty(json)) {
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write("empty log".getBytes());
			}
			return;
		}
		JSONObject mapped;
		try {
			mapped = new JSONObject(json);
		} catch (JSONException e) {
			response.setStatus(RestfulResponse.STATUS_BAD_REQUEST);
			try (OutputStream out = response.openOutput()) {
				out.write("bad log format".getBytes());
			}
			return;
		}
		try {
			RemoteLogItemImpl item = new RemoteLogItemImpl();
			item.server = mapped.optString("server");
			item.subject = mapped.optString("subject");
			item.content = mapped.optString("content");
			item.level = mapped.optString("level");
			write(access.getGroupId(), item);
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write("success".getBytes());
			}
		} catch (Throwable e) {
			_Logger.info("调用异常:" + request.getClientIp() + "," + request.getUri() + "," + request.getVerb(), e);
		}
	}

	@Override
	public void timeout(RestfulRequest request, RestfulResponse response) throws IOException {

	}

	@Override
	public void destroy() {
		for (Out o : Out.CACHE.values()) {
			o.close();
		}
	}
}
