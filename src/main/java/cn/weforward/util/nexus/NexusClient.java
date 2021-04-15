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
package cn.weforward.util.nexus;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cn.weforward.common.crypto.Base64;
import cn.weforward.common.util.StringUtil;
import cn.weforward.util.HttpInvoker;
import cn.weforward.util.docker.ext.Util;

/**
 * nexus操作客户端
 *
 * @author daibo
 *
 */
public class NexusClient {
	/** 链接 */
	protected String m_Url;
	/** 用户名 */
	protected String m_UserName;
	/** 密码 */
	protected String m_Password;
	/** 调用器 */
	protected HttpInvoker m_Invoker;

	public NexusClient() throws IOException {
		m_Invoker = new HttpInvoker(3, 10);
	}

	public void setUrl(String url) {
		m_Url = url;
	}

	public void setUserName(String username) {
		m_UserName = username;
	}

	public void setPassword(String password) {
		m_Password = password;
	}

	/**
	 * 获取组件
	 * 
	 * @param repository
	 * @return
	 * @throws NexusException
	 */
	public Components components(String repository, String continuationToken) throws NexusException {
		String uri = m_Url + "service/rest/v1/components?repository=" + repository;
		if (!StringUtil.isEmpty(continuationToken)) {
			uri += "&continuationToken=" + continuationToken;
		}
		HttpUriRequest request = new HttpGet(uri);
		setAuth(request);
		HttpResponse response = null;
		try {
			response = m_Invoker.execute(request);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new Components(new JSONObject(result));
		} catch (IOException e) {
			throw new NexusException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	/**
	 * 删除组件
	 * 
	 * @param id
	 * @throws NexusException
	 */
	public void deleteComponent(String id) throws NexusException {
		String uri = m_Url + "service/rest/v1/components/" + id;
		HttpUriRequest request = new HttpDelete(uri);
		setAuth(request);
		HttpResponse response = null;
		try {
			response = m_Invoker.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 204) {
				String result = EntityUtils.toString(response.getEntity());
				throw error(status, result);
			}
		} catch (IOException e) {
			throw new NexusException("IO异常:" + e.getMessage(), e);
		} finally {
			try {
				HttpInvoker.consume(response);
			} catch (IOException e) {
				// 忽略
			}
		}
	}

	// 设置验证
	private void setAuth(HttpUriRequest request) {
		request.setHeader("Authorization", "Basic " + Base64.encode((m_UserName + ":" + m_Password).getBytes()));
	}

	// 错误处理
	private NexusException error(StatusLine status, String result) {
		if (Util.isEmpty(result)) {
			return new NexusException("服务器返回异常码:" + status.getStatusCode());
		}
		JSONObject json;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			return new NexusException("服务器数据异常:" + Util.limit(result, 235));
		}
		String message = json.optString("message");
		if (Util.isEmpty(message)) {
			return new NexusException("服务器数据异常:" + Util.limit(result, 235));
		} else {
			return new NexusException("Docker异常:" + message);
		}
	}

}
