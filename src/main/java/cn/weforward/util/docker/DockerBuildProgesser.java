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

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

/**
 * docker build进度处理
 * 
 * @author daibo
 *
 */
public interface DockerBuildProgesser {
	/**
	 * 状态处理
	 * 
	 * @param status
	 */
	void progesser(BuildStatus status);

	/**
	 * 构建状态
	 * 
	 * @author daibo
	 *
	 */
	class BuildStatus {
		/** 唯一id */
		private String id;
		/** 内容 */
		private String content;

		public BuildStatus(JSONObject json, AtomicInteger inc) {
			if (json.has("stream")) {
				String stream = json.optString("stream");
				if (stream.equals("\n")) {
					id = "stream-" + inc.incrementAndGet();
				} else if (stream.endsWith("\n")) {
					id = "stream-" + inc.getAndIncrement();
				} else {
					id = "stream-" + inc.get();
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < stream.length(); i++) {
					char ch = stream.charAt(i);
					if (ch == '\n' || ch == '\u001b' || ch == '\u2018' || ch == '\u2019') {
						continue;
					}
					sb.append(ch);
				}
				content = sb.toString();

			} else {
				id = json.optString("id");
				content = json.toString();
			}
		}

		public String getId() {
			return id;
		}

		@Override
		public String toString() {
			return content;
		}

	}

}
