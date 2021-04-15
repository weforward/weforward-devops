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
package cn.weforward.util.docker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.weforward.common.util.UrlUtil;
import cn.weforward.util.HttpInvoker;
import cn.weforward.util.docker.DockerBuildProgesser.BuildStatus;
import cn.weforward.util.docker.DockerPullProgesser.PullStatus;
import cn.weforward.util.docker.DockerPushProgesser.PushStatus;
import cn.weforward.util.docker.exception.NoSuchImageException;
import cn.weforward.util.docker.ext.AbstractClient;
import cn.weforward.util.docker.ext.Frame;
import cn.weforward.util.docker.ext.FrameReader;
import cn.weforward.util.docker.ext.StreamType;
import cn.weforward.util.docker.ext.Util;

/**
 * docker服务
 *
 * <a href="https://docs.docker.com/engine/api/v1.37">API参考文档</a>
 */
public class DockerClient extends AbstractClient {
	/** 验证信息 服务地址/验证信息Base64(JSON) */
	private Map<String, String> m_Authentications = new HashMap<>();

	private String m_Version;

	public DockerClient() throws IOException {
		super();
	}

	public DockerClient(int connectionSecond, int soSecond) throws IOException {
		super(connectionSecond, soSecond);
	}

	public DockerClient(int connectionSecond, int soSecond, String certPath, String keyPath, String caPath)
			throws IOException {
		super(connectionSecond, soSecond, certPath, keyPath, caPath);
	}

	/**
	 * 指定版本
	 * 
	 * @param v
	 */
	public void setVersion(String v) {
		m_Version = v;
	}

	@Override
	protected String genUri(String path) {
		if (Util.isEmpty(m_Version)) {
			return super.genUri(path);
		}
		return m_Url + m_Version + "/" + path;
	}

	/**
	 * 设置验证信息
	 * 
	 * @param infos
	 */
	public void setAuthInfos(List<AuthInfo> infos) {
		for (AuthInfo info : infos) {
			m_Authentications.put(info.getServeraddress(), toBase64Json(info));
		}
	}

	/**
	 * 验证
	 * 
	 * @param serveraddress 服务地址
	 * @param username      用户
	 * @param password      密码
	 * @param email         邮箱
	 * @return
	 * @throws DockerException
	 */
	public DockerAuth auth(String serveraddress, String username, String password, String email)
			throws DockerException {
		JSONObject json = new JSONObject();
		if (!Util.isEmpty(serveraddress)) {
			json.put("serveraddress", serveraddress);
		}
		if (!Util.isEmpty(username)) {
			json.put("username", username);
		}
		if (!Util.isEmpty(password)) {
			json.put("password", password);
		}
		if (!Util.isEmpty(email)) {
			json.put("email", email);
		}
		String uri = genUri("auth");
		HttpPost post = new HttpPost(uri);
		post.setHeader("Content-Type", "application/json");
		HttpResponse response = null;
		try {
			StringEntity entity = new StringEntity(json.toString());
			post.setEntity(entity);
			response = execute(post);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			DockerAuth auth = new DockerAuth(new JSONObject(result));
			if (!Util.isEmpty(auth.getIdentityToken())) {
				synchronized (this) {
					json = new JSONObject();
					json.put("IdentityToken", auth.getIdentityToken());
					m_Authentications.put(serveraddress, toBase64Json(json));
				}
			}
			return new DockerAuth(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 列出容器
	 * 
	 * @param limit      返回最近创建的容器，包含未运行的，负数则无效
	 * @param retrunAll  是否返回所有容器，包含未运行的
	 * @param returnSize 是否返回容器大小属性 SizeRw 和 SizeRootFs.
	 * @param filters    过滤容器,如：{"status": ["paused"]}
	 * @return
	 * @throws DockerException
	 */
	public List<DockerContainer> ps(int limit, boolean retrunAll, boolean returnSize, Map<String, String[]> filters)
			throws DockerException {
		StringBuilder sb = new StringBuilder();
		sb.append(genUri("containers/json"));
		sb.append("?all=");
		sb.append(retrunAll);
		sb.append("&size=");
		sb.append(returnSize);
		if (limit > 0) {
			sb.append("&limit=");
			sb.append(limit);
		}
		HttpResponse response = null;
		try {
			if (null != filters) {
				JSONObject obj = new JSONObject();
				for (Entry<String, String[]> entry : filters.entrySet()) {
					String[] arr = entry.getValue();
					JSONArray jarr;
					if (null == arr || arr.length == 0) {
						jarr = new JSONArray();
					} else {
						jarr = new JSONArray();
						for (String v : arr) {
							jarr.put(v);
						}
					}
					obj.put(entry.getKey(), jarr);
				}
				sb.append("&filters=");
				sb.append(URLEncoder.encode(obj.toString(), "utf-8"));
			}
			String uri = sb.toString();
			HttpUriRequest get = new HttpGet(uri);
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			JSONArray json = new JSONArray(result);
			List<DockerContainer> list = new ArrayList<>();
			for (int i = 0; i < json.length(); i++) {
				list.add(new DockerContainer(json.getJSONObject(i)));
			}
			return list;
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 运行容器
	 * 
	 * @param name
	 * @param run
	 * @return
	 * @throws DockerException
	 */
	public DockerRun.Result run(String name, DockerRun run) throws DockerException {
		String uri = genUri("containers/create") + (Util.isEmpty(name) ? "" : ("?name=" + name));
		HttpPost post = new HttpPost(uri);
		post.setHeader("Content-Type", "application/json");
		HttpResponse response = null;
		try {
			StringEntity entity = new StringEntity(run.toJson().toString());
			post.setEntity(entity);
			response = execute(post);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 201) {
				throw error(status, result);
			}
			return new DockerRun.Result(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 检查容器
	 * 
	 * @param id         容器id 或名称
	 * @param returnSize 是否返回容器大小属性 SizeRw 和 SizeRootFs.
	 * @throws DockerException
	 */
	public DockerInspect inspect(String id, boolean returnSize) throws DockerException {
		String uri = genUri("containers/" + id + "/json") + "?size=" + returnSize;
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerInspect(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 列出在容器内运行的进程
	 * 
	 * @param id      容器id
	 * @param ps_args 查询参数
	 * @return
	 * @throws DockerException
	 */
	public DockerTop top(String id, String ps_args) throws DockerException {
		if (Util.isEmpty(ps_args)) {
			ps_args = "-ef";
		}
		String uri = genUri("containers/" + id + "/top") + "?ps_args=" + ps_args;
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerTop(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 查询容器日志
	 * 
	 * @param id             容器id
	 * @param stdout         显示标准输出
	 * @param stderr         显示错误输出
	 * @param showtimestamps 显示时间戳
	 * @param since          仅返回自此时间以来的日志,作为 UNIX 时间戳
	 * @param until          仅在此时间之前返回日志,作为 UNIX 时间戳
	 * 
	 * @param tail           仅从日志末尾返回此日志行数。指定为整数或全部以输出所有日志行。 all返回全部
	 * @return
	 * @throws DockerException
	 */
	public List<DockerLog> logs(String id, boolean stdout, boolean stderr, Boolean showtimestamps, Integer since,
			Integer until, String tail) throws DockerException {
		if (showtimestamps == null) {
			showtimestamps = false;
		}
		if (since == null) {
			since = 0;
		}
		if (until == null) {
			until = 0;
		}
		if (Util.isEmpty(tail)) {
			tail = "all";
		}
		String uri = genUri("containers/" + id + "/logs") + "?stdout=" + stdout + "&stderr=" + stderr + "&timestamps="
				+ showtimestamps + "&since=" + since + "&until=" + until + "&tail=" + tail;
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
			HttpEntity entity = response.getEntity();
			ContentType contentType = ContentType.get(entity);
			Charset charset = null;
			if (contentType != null) {
				charset = contentType.getCharset();
				if (charset == null) {
					final ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
					charset = defaultContentType != null ? defaultContentType.getCharset() : null;
				}
			}
			if (charset == null) {
				charset = Consts.UTF_8;
			}
			FrameReader reader = new FrameReader(entity.getContent());
			Frame frame = null;
			List<DockerLog> list = new ArrayList<>();
			while ((frame = reader.readFrame()) != null) {
				byte[] bs = frame.getPayload();
				String v = new String(bs, charset).trim();
				if (Util.isEmpty(v)) {
					continue;
				}
				String type;
				StreamType stype = frame.getStreamType();
				switch (stype) {
				case STDOUT:
					type = "INFO";
					break;
				case STDERR:
					type = "ERROR";
					break;
				default:
					type = stype.name().toUpperCase();
					break;
				}
				if (Util.isEmpty(v)) {
					continue;
				}
				ZonedDateTime time = null;
				if (null != showtimestamps && showtimestamps) {
					try {
						int index = v.indexOf('Z') + 1;
						time = ZonedDateTime.parse(v.substring(0, index)).withZoneSameInstant(ZoneId.systemDefault());
						v = v.substring(index);
					} catch (DateTimeParseException e) {
						// 忽略时间异常
					}
				}
				list.add(new DockerLog(type, time, v));
			}
			reader.close();
			return list;
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 获取文件系统修改
	 * 
	 * @param id 容器id
	 * @throws DockerException
	 * @return kind 0: Modified 1: Added 2: Deleted
	 */
	public List<DockerRun.PathAndKind> changes(String id) throws DockerException {
		String uri = genUri("containers/" + id + "/changes");
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return Arrays.asList(DockerRun.PathAndKind.fromJson(new JSONArray(result)));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 导出容器
	 * 
	 * @param id 容器id
	 * @throws DockerException
	 */
	public void export(String id) throws DockerException {
		String uri = genUri("containers/" + id + "/export");
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 根据资源使用情况获取容器统计信息
	 * 
	 * @param id
	 * @throws DockerException
	 */
	public DockerStats stats(String id) throws DockerException {
		// 流式传输输出。如果为 false,则统计信息将输出一次,然后它将断开连接。
		boolean stream = false;
		String uri = genUri("containers/" + id + "/stats?stream=" + stream);
		HttpGet get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerStats(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 调整容器 TTY 的大小
	 * 
	 * @param id 容器
	 * @param h  字符中的 tty 会话的高度
	 * @param w  字符中的 tty 会话宽度
	 * @throws DockerException
	 */
	public void resize(String id, int h, int w) throws DockerException {
		String uri = genUri("containers/" + id + "/resize?h=" + h + "&w=" + w);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 启动容器
	 * 
	 * @param id         容器id或名称
	 * @param detachKeys 分离键 覆盖分离容器的键序列。格式是单个字符 [a-Z] 或 ctrl-<value>其中<value>是: a-z,
	 *                   *, *, * , , 或 *.中的一个
	 * @throws DockerException
	 */
	public void start(String id, String detachKeys) throws DockerException {
		String uri = genUri("containers/" + id + "/start")
				+ (Util.isEmpty(detachKeys) ? "" : "detachKeys=" + detachKeys);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 停止容器
	 * 
	 * @param id 容器id或名称
	 * @param t  多久后杀掉容器，单位秒
	 * @throws DockerException
	 */
	public void stop(String id, Integer t) throws DockerException {
		String uri = genUri("containers/" + id + "/stop") + (null == t ? "" : "?t=" + t);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204 && status.getStatusCode() != 304) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 重启容器
	 * 
	 * @param id 容器id或名称
	 * @param t  多久后杀掉容器，单位秒
	 * @throws DockerException
	 */
	public void restart(String id, Integer t) throws DockerException {
		String uri = genUri("containers/" + id + "/restart") + (null != t ? "" : "t=" + t);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 杀死容器
	 * 
	 * @param id     容器id
	 * @param signal 信号，默认SIGKILL
	 * @throws DockerException
	 */
	public void kill(String id, String signal) throws DockerException {
		String uri = genUri("containers/" + id + "/kill") + (null != signal ? "" : "signal=" + signal);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 更新容器
	 * 
	 * @param id     容器
	 * @param update 更新内容
	 * @return
	 * @throws DockerException
	 */
	public DockerUpdate.Result update(String id, DockerUpdate update) throws DockerException {
		String uri = genUri("containers/" + id + "/update");
		HttpPost post = new HttpPost(uri);
		post.setHeader("Content-Type", "application/json");
		HttpResponse response = null;
		try {
			StringEntity entity = new StringEntity(update.toJson().toString());
			post.setEntity(entity);
			response = execute(post);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerUpdate.Result(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 重命名容器
	 * 
	 * @param id   容器id
	 * @param name 容器新名称
	 * @throws DockerException
	 */
	public void rename(String id, String name) throws DockerException {
		String uri = genUri("containers/" + id + "/rename") + (null == name ? "" : "?name=" + name);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 暂停容器
	 * 
	 * 使用cgroups暂停容器中的所有流程。使用 SIGSTOP 信号
	 * 
	 * @param id
	 * @throws DockerException
	 */
	public void pasue(String id) throws DockerException {
		String uri = genUri("containers/" + id + "/pause");
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 停止暂停容器
	 * 
	 * 
	 * @param id
	 * @throws DockerException
	 */
	public void unpasue(String id) throws DockerException {
		String uri = genUri("containers/" + id + "/unpause");
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 停止容器，并等待到返回退出代码
	 * 
	 * @param id
	 * @param condition 等待容器状态达到给定条件,即"not-running"(默认)、"next-exit"或"removed"。
	 * @throws DockerException
	 */
	public DockerStats wait(String id, String condition) throws DockerException {
		String uri = genUri("containers/" + id + "/wait") + (condition == null ? "" : "?condition=" + condition);
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerStats(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 移除容器
	 * 
	 * @param id    容器id
	 * @param v     是否移除相关存储券
	 * @param force 如果容器运行中，是否强制杀掉后移除
	 * @param link  是否移除相关链接
	 * @throws DockerException
	 */
	public void remove(String id, Boolean v, Boolean force, Boolean link) throws DockerException {
		String uri = genUri("containers/" + id);
		HttpDelete post = new HttpDelete(uri);
		HttpResponse response = null;
		try {
			List<BasicNameValuePair> list = new ArrayList<>();
			if (null != v) {
				list.add(new BasicNameValuePair("v", String.valueOf(v)));
			}
			if (null != force) {
				list.add(new BasicNameValuePair("force", String.valueOf(force)));
			}
			if (null != link) {
				list.add(new BasicNameValuePair("link", String.valueOf(link)));
			}
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 删除已停止的容器
	 * 
	 * @param filters 要在清除列表中处理的筛选器
	 * 
	 *                可选
	 * 
	 *                <li>until=<timestamp> 在此时间戳之前创建的容器。<timestamp>可以是相对于守护进程计算的
	 *                Unix 时间戳、日期格式化时间戳或 Go 持续时间字符串(例如 10m、1h30m)
	 *                <li>label (label=<key>, label=<key>=<value>, label!=<key>, or
	 *                label!=<key>=<value>) 清理匹配到标签的容器
	 *                <li>
	 * @throws DockerException
	 * 
	 * 
	 */
	public DockerPrune prune(Map<String, String[]> filters) throws DockerException {
		StringBuilder sb = new StringBuilder();
		sb.append(genUri("containers/prune"));
		HttpResponse response = null;
		try {
			if (null != filters) {
				JSONObject obj = new JSONObject();
				for (Entry<String, String[]> entry : filters.entrySet()) {
					String[] arr = entry.getValue();
					JSONArray jarr;
					if (null == arr || arr.length == 0) {
						jarr = new JSONArray();
					} else {
						jarr = new JSONArray();
						for (String v : arr) {
							jarr.put(v);
						}
					}
					obj.put(entry.getKey(), jarr);
				}
				sb.append("?filters=");
				sb.append(URLEncoder.encode(obj.toString(), "utf-8"));
			}
			String uri = sb.toString();
			HttpUriRequest get = new HttpPost(uri);
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerPrune(new JSONObject(result));
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

//	public void queryArchive(String id, String path) throws DockerException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(genUri("containers/" + id + "/archive"));
//		HttpResponse response = null;
//		try {
//			sb.append("?path=");
//			sb.append(URLEncoder.encode(path, "utf-8"));
//			String uri = sb.toString();
//			HttpUriRequest get = new HttpHead(uri);
//			response = execute(get);
//			StatusLine status = response.getStatusLine();
//			if (status.getStatusCode() != 200) {
//				throw error(status, "");
//			}
//			String base64 = response.getFirstHeader("X-Docker-Container-Path-Stat").getValue();
//			String json = new String(cn.weforward.common.crypto.Base64.decode(base64), "utf-8");
//			System.out.println(json);
//		} catch (IOException e) {
//			throw new DockerException("IO异常:" + e.getMessage(), e);
//		} finally {
//			try {
//				HttpInvoker.consume(response);
//			} catch (IOException e) {
//				// 忽略
//			}
//		}
//	}

	public void archive(String id, String path, OutputStream out) throws DockerException {
		StringBuilder sb = new StringBuilder();
		sb.append(genUri("containers/" + id + "/archive"));
		HttpResponse response = null;
		try {
			sb.append("?path=");
			sb.append(URLEncoder.encode(path, "utf-8"));
			String uri = sb.toString();
			HttpUriRequest get = new HttpGet(uri);
			response = execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
			try (InputStream in = response.getEntity().getContent()) {
				byte[] buffer = new byte[1024];
				int l = 0;
				while ((l = in.read(buffer)) != -1) {
					out.write(buffer, 0, l);
				}
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 信息
	 * 
	 * @return
	 * @throws DockerException
	 */
	public DockerInfo info() throws DockerException {
		String uri = genUri("info");
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			JSONObject json = new JSONObject(result);
			return new DockerInfo(json);
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 镜像列表
	 * 
	 * @return
	 * @throws DockerException
	 */
	public DockerImageInspect image(String name) throws DockerException {
		String uri = genUri("images/" + name + "/json");
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				if (status.getStatusCode() == 404) {
					return null;
				}
				throw error(status, result);
			}
			JSONObject json = new JSONObject(result);
			return new DockerImageInspect(json);
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 镜像列表
	 * 
	 * @param all     Show all images. Only images from a final layer (no children)
	 *                are shown by default
	 * @param digests Show digest information as a RepoDigests field on each image.
	 * 
	 * 
	 * @return
	 * @throws DockerException
	 */
	public List<DockerImage> images(boolean all, boolean digests, Map<String, String[]> filters)
			throws DockerException {
		HttpResponse response = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(genUri("images/json"));
			sb.append("?all=").append(all);
			sb.append("&digests=").append(digests);
			if (null != filters) {
				JSONObject obj = new JSONObject();
				for (Entry<String, String[]> entry : filters.entrySet()) {
					String[] arr = entry.getValue();
					JSONArray jarr;
					if (null == arr || arr.length == 0) {
						jarr = new JSONArray();
					} else {
						jarr = new JSONArray();
						for (String v : arr) {
							jarr.put(v);
						}
					}
					obj.put(entry.getKey(), jarr);
				}
				sb.append("&filters=");
				sb.append(URLEncoder.encode(obj.toString(), "utf-8"));
			}
			HttpUriRequest get = new HttpGet(sb.toString());
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			JSONArray json = new JSONArray(result);
			List<DockerImage> list = new ArrayList<>();
			for (int i = 0; i < json.length(); i++) {
				list.add(new DockerImage(json.getJSONObject(i)));
			}
			return list;
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}

	}

	/**
	 * 拉取镜像
	 * 
	 * @param name      [registry]/镜像名:[版本号] registry.example.com/myimage:latest.
	 * @param progesser 进度处理
	 * @throws DockerException
	 */
	public void pull(String name, DockerPullProgesser progesser) throws DockerException {
		String uri = genUri("images/create");
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			String X_Registry_Auth = loadX_Registry_AuthByImageName(name);
			if (!Util.isEmpty(X_Registry_Auth)) {
				post.addHeader("X-Registry-Auth", X_Registry_Auth.toString());
			}
			List<BasicNameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("fromImage", name));
			post.setEntity(new UrlEncodedFormEntity(params));
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				if (status.getStatusCode() == 404) {
					throw new NoSuchImageException();
				}
				throw error(status, result);
			}
			InputStream in = response.getEntity().getContent();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while (null != (line = reader.readLine())) {
					if (null != progesser) {
						progesser.progesser(new PullStatus(new JSONObject(line)));
					}
				}
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.toString(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 构建镜像
	 * 
	 * @return
	 * @throws DockerException
	 */
	public void build(String remote, Map<String, String> buildargs, Map<String, String> labels, String t,
			DockerBuildProgesser progesser) throws DockerException {
		JSONObject jsonBuildArgs = new JSONObject();
		for (Map.Entry<String, String> entry : buildargs.entrySet()) {
			jsonBuildArgs.put(entry.getKey(), entry.getValue());
		}
		JSONObject jsonlabels = new JSONObject();
		for (Map.Entry<String, String> entry : labels.entrySet()) {
			jsonlabels.put(entry.getKey(), entry.getValue());
		}
		String uri = genUri("build") + "?remote=" + UrlUtil.encodeUrl(remote) + "&buildargs="
				+ UrlUtil.encodeUrl(jsonBuildArgs.toString()) + "&t=" + t + "&labels="
				+ UrlUtil.encodeUrl(jsonlabels.toString());
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
			InputStream in = response.getEntity().getContent();
			AtomicInteger inc = new AtomicInteger();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while (null != (line = reader.readLine())) {
					if (null != progesser) {
						progesser.progesser(new BuildStatus(new JSONObject(line), inc));
					}
				}
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 创建镜像标签
	 * 
	 * @param name
	 * @param repo
	 * @param tag
	 * @throws DockerException
	 */
	public void tag(String name, String repo, String tag) throws DockerException {
		String uri = genUri("images/" + name + "/tag") + "?repo=" + repo + "&tag=" + tag;
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 201) {
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 推送镜像
	 * 
	 * @param name
	 * @param tag
	 * @throws DockerException
	 */
	public void push(String name, String tag, DockerPushProgesser progesser) throws DockerException {
		String uri = genUri("images/" + name + "/push") + "?tag=" + tag;
		HttpPost post = new HttpPost(uri);
		HttpResponse response = null;
		try {
			String X_Registry_Auth = loadX_Registry_AuthByImageName(name);
			if (!Util.isEmpty(X_Registry_Auth)) {
				post.addHeader("X-Registry-Auth", X_Registry_Auth.toString());
			}
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
			InputStream in = response.getEntity().getContent();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while (null != (line = reader.readLine())) {
					if (null != progesser) {
						progesser.progesser(new PushStatus(new JSONObject(line)));
					}
				}
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.toString(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 推送镜像
	 * 
	 * @param name    镜像名
	 * @param force   Remove the image even if it is being used by stopped
	 *                containers or has other tags
	 * 
	 * @param noprune Do not delete untagged parent images
	 * 
	 * @throws DockerException
	 */
	public void delete(String name, boolean force, boolean noprune) throws DockerException {
		String uri = genUri("images/" + name) + "?force=" + force + "&noprune=" + noprune;
		HttpDelete post = new HttpDelete(uri);
		HttpResponse response = null;
		try {
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.toString(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 版本
	 * 
	 * @return
	 * @throws DockerException
	 */
	public DockerVersion version() throws DockerException {
		String uri = genUri("version");
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			JSONObject json = new JSONObject(result);
			return new DockerVersion(json);
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/*
	 * 加载验证信息
	 * 
	 * @param name
	 * 
	 * @return
	 */
	private String loadX_Registry_AuthByImageName(String name) {
		int index = name.indexOf('/');
		if (index <= 0) {
			return null;
		}
		return m_Authentications.get(name.substring(0, index));
	}

	private String toBase64Json(AuthInfo info) {
		if (null == info) {
			return null;
		}
		JSONObject json = new JSONObject();
		json.put("username", info.getUsername());
		json.put("password", info.getPassword());
		json.put("email", info.getEmail());
		json.put("serveraddress", info.getServeraddress());
		return toBase64Json(json);
	}

	private String toBase64Json(JSONObject json) {
		return Base64.encodeBase64String(json.toString().getBytes());
	}

	// 错误处理
	private DockerException error(StatusLine status, String result) {
		if (Util.isEmpty(result)) {
			return new DockerException("服务器返回异常码:" + status.getStatusCode());
		}
		JSONObject json;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			return new DockerException("服务器数据异常:" + Util.limit(result, 235));
		}
		String message = json.optString("message");
		if (Util.isEmpty(message)) {
			return new DockerException("服务器数据异常:" + Util.limit(result, 235));
		} else {
			return new DockerException("Docker异常:" + message);
		}
	}

	/**
	 * 创建命令
	 * 
	 * @param id   容器id或名称
	 * @param cmds 要运行的命令
	 * @return
	 * @throws DockerException
	 */
	public String createExec(String id, String[] cmds) throws DockerException {
		String uri = genUri("containers/" + id + "/exec");
		HttpPost post = new HttpPost(uri);
		JSONObject json = new JSONObject();
		json.put("AttachStdin", false);
		json.put("AttachStdout", true);
		json.put("AttachStderr", true);
		json.put("DetachKeys", "ctrl-p,ctrl-q");
		JSONArray cmdArray = new JSONArray();
		for (String c : cmds) {
			cmdArray.put(c);
		}
		json.put("Cmd", cmdArray);
		HttpResponse response = null;
		try {
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(json.toString()));
			response = execute(post);
			String result = EntityUtils.toString(response.getEntity());
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == 201) {
				return new JSONObject(result).optString("Id");
			} else {
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 执行命令
	 * 
	 * @param id 创建的命令id {@link #createExec(String, String[])
	 * @return
	 * @throws DockerException
	 */
	public String exec(String id) throws DockerException {
		String uri = genUri("exec/" + id + "/start");
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(uri);
			post.setHeader("Content-Type", "application/json");
			JSONObject json = new JSONObject();
			json.put("Detach", false);
			json.put("Tty", false);
			post.setEntity(new StringEntity(json.toString()));
			response = execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				ContentType contentType = ContentType.get(entity);
				Charset charset = null;
				if (contentType != null) {
					charset = contentType.getCharset();
					if (charset == null) {
						final ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
						charset = defaultContentType != null ? defaultContentType.getCharset() : null;
					}
				}
				if (charset == null) {
					charset = HTTP.DEF_CONTENT_CHARSET;
				}
				StringBuilder sb = new StringBuilder();
				try (FrameReader reader = new FrameReader(entity.getContent())) {
					Frame frame = null;
					while ((frame = reader.readFrame()) != null) {
						byte[] bs = frame.getPayload();
						String v = new String(bs, charset).trim();
						if (Util.isEmpty(v)) {
							continue;
						}
						sb.append(v);
					}
				}
				return sb.toString();
			} else {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new DockerException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}
}
