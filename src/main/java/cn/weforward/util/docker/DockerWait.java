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

import org.json.JSONObject;

/**
 * docker停止等待
 * 
 * @author daibo
 *
 */
public class DockerWait {
	/** 退出代码 */
	private Integer StatusCode;

	private DockerWaitError Error;

	public DockerWait(JSONObject json) {
		StatusCode = json.optInt("StatusCode");
		JSONObject obj = json.optJSONObject("Error");
		if (null != obj) {
			Error = new DockerWaitError(obj);
		}

	}

	public Integer getStatusCode() {
		return StatusCode;
	}

	public DockerWaitError getError() {
		return Error;
	}

	public static class DockerWaitError {

		private String Message;

		public DockerWaitError(JSONObject json) {
			Message = json.optString("Message");
		}

		public String getMessage() {
			return Message;
		}
	}
}
