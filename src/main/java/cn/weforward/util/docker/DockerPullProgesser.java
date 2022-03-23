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

import cn.weforward.util.docker.ext.Util;

/**
 * docker pull进度处理
 * 
 * @author daibo
 *
 */
public interface DockerPullProgesser {
	/**
	 * 状态处理
	 * 
	 * @param status
	 */
	void progesser(PullStatus status);

	/**
	 * 拉取状态
	 * 
	 * @author daibo
	 *
	 */
	class PullStatus {
		/** 唯一id */
		private String id;
		/** 状态 */
		private String status;
		/** 进度 */
		private String progress;
		/** 进度详情 */
		private ProgressDetail progressDetail;

		public PullStatus(JSONObject json) {
			id = json.optString("id");
			status = json.optString("status");
			progress = json.optString("progress");
			JSONObject progressDetailJson = json.optJSONObject("progressDetail");
			if (null != progressDetail) {
				progressDetail = new ProgressDetail(progressDetailJson);
			}
		}

		public String getId() {
			return id;
		}

		public String getStatus() {
			return status;
		}

		public String getProgress() {
			return progress;
		}

		public ProgressDetail getProgressDetail() {
			return progressDetail;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"status\":\"");
			sb.append(status);
			sb.append("\"");
			if (null != progressDetail) {
				sb.append(",\"progressDetail\":");
				sb.append(progressDetail);
				sb.append("\"");
			}
			if (!Util.isEmpty(progress)) {
				sb.append(",\"progress\":\"");
				sb.append(progress);
				sb.append("\"");
			}
			if (!Util.isEmpty(id)) {
				sb.append(",\"id\":\"");
				sb.append(id);
				sb.append("\"");
			}
			sb.append("}");
			return sb.toString();
		}

	}

	/**
	 * 拉取进度详情
	 * 
	 * @author daibo
	 *
	 */
	class ProgressDetail {
		/** 当前进度 */
		private long current;
		/** 总数 */
		private long total;

		public ProgressDetail(JSONObject json) {
			current = json.optLong("current");
			total = json.optLong("total");
		}

		public long getCurrent() {
			return current;
		}

		public long getTotal() {
			return total;
		}

		@Override
		public String toString() {
			return "{\"\":" + current + ",\"\":" + total + "}";
		}
	}
}
