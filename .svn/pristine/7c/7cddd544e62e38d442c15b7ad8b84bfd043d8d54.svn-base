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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cn.weforward.util.HttpInvoker;
import cn.weforward.util.docker.ext.AbstractClient;
import cn.weforward.util.docker.ext.Util;

/**
 * 
 * docker Registry API 客户端
 * 
 * https://docs.docker.com/registry/spec/api/#listing-image-tags
 * 
 * @author daibo
 *
 */
public class DockerRegistryClient extends AbstractClient {

	public DockerRegistryClient() throws IOException {
		super();
	}

	public DockerRegistryClient(int connectionSecond, int soSecond) throws IOException {
		super(connectionSecond, soSecond);
	}

	public DockerRegistryClient(int connectionSecond, int soSecond, String certPath, String keyPath, String caPath)
			throws IOException {
		super(connectionSecond, soSecond, certPath, keyPath, caPath);
	}

	/**
	 * Listing Repositories
	 * 
	 * @throws DockerException
	 * 
	 */
	public DockerRepositories repositories() throws DockerException {
		String uri = genUri("_catalog");
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerRepositories(new JSONObject(result));
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

	protected String genUri(String path) {
		return m_Url + "v2/" + path;
	}

	/**
	 * 标签列表
	 * 
	 * Listing Image Tags
	 * 
	 * 
	 * @return
	 * @throws DockerException
	 */
	public DockerTagsList tagslist(String name) throws DockerException {
		String uri = genUri(name + "/tags/list");
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				if (status.getStatusCode() == 404) {
					return new DockerTagsList(name);
				}
				throw error(status, result);
			}
			return new DockerTagsList(new JSONObject(result));
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
	 * PULLING AN IMAGE MANIFEST
	 * 
	 * @param name
	 * @param tag
	 * @return
	 * @throws DockerException
	 */
	public DockerManifests pulling(String name, String tag) throws DockerException {
		String uri = genUri(name + "/manifests/" + tag);
		HttpUriRequest get = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw error(status, result);
			}
			return new DockerManifests(new JSONObject(result));
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
	 * Deleting an Image
	 * 
	 * @param name
	 * @param tag
	 * @return
	 * @throws DockerException
	 */
	public void deleting(String name, String tag) throws DockerException {
		String uri = genUri(name + "/manifests/" + tag);
		HttpUriRequest get = new HttpDelete(uri);
		HttpResponse response = null;
		try {
			response = execute(get);
			StatusLine status = response.getStatusLine();
			String result = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 202) {
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

}
