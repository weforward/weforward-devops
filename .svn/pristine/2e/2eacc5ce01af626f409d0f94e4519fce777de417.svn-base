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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Docker版本
 * 
 * @author daibo
 *
 */
public class DockerVersion {
	/** 平台 */
	private DockerVersionPlatform Platform;
	/** 组件 */
	private DockerVersionComponent[] Components;
	/** 版本 */
	private String Version;
	/** api版本 */
	private String ApiVersion;
	/** 最小兼容的api版本 */
	private String MinAPIVersion;
	/** git提交 */
	private String GitCommit;
	/** go语言版本 */
	private String GoVersion;
	/** 操作系统 */
	private String Os;
	/** 架构 */
	private String Arch;
	/** 内核版本 */
	private String KernelVersion;
	/** 是否实验的 */
	private boolean Experimental;
	/** build时间 */
	private String BuildTime;

	public DockerVersion(JSONObject json) {
		JSONObject obj = json.optJSONObject("Platform");
		if (null != obj) {
			Platform = new DockerVersionPlatform(obj);
		}
		JSONArray arr = json.optJSONArray("Components");
		if (null != arr) {
			Components = new DockerVersionComponent[arr.length()];
			for (int i = 0; i < arr.length(); i++) {
				Components[i] = new DockerVersionComponent(arr.getJSONObject(i));
			}
		}
		Version = json.optString("Version");
		ApiVersion = json.optString("ApiVersion");
		MinAPIVersion = json.optString("MinAPIVersion");
		GitCommit = json.optString("GitCommit");
		GoVersion = json.optString("GoVersion");
		Os = json.optString("Os");
		Arch = json.optString("Arch");
		KernelVersion = json.optString("KernelVersion");
		Experimental = json.optBoolean("Platform");
		BuildTime = json.optString(BuildTime);
	}

	public DockerVersionPlatform getPlatform() {
		return Platform;
	}

	public DockerVersionComponent[] getComponents() {
		return Components;
	}

	public String getVersion() {
		return Version;
	}

	public String getApiVersion() {
		return ApiVersion;
	}

	public String getMinAPIVersion() {
		return MinAPIVersion;
	}

	public String getGitCommit() {
		return GitCommit;
	}

	public String getGoVersion() {
		return GoVersion;
	}

	public String getOs() {
		return Os;
	}

	public String getArch() {
		return Arch;
	}

	public String getKernelVersion() {
		return KernelVersion;
	}

	public boolean isExperimental() {
		return Experimental;
	}

	public String getBuildTime() {
		return BuildTime;
	}

	public static class DockerVersionPlatform {

		private String Name;

		public DockerVersionPlatform(JSONObject json) {
			Name = json.optString("Name");
		}

		public String getName() {
			return Name;
		}
	}

	public static class DockerVersionComponent {

		private String Name;

		private String Version;

		private Object Details;

		public DockerVersionComponent(JSONObject json) {
			Name = json.optString("Name");
			Version = json.optString("Version");
			Details = json.get("Details");
		}

		public String getName() {
			return Name;
		}

		public String getVersion() {
			return Version;
		}

		public Object getDetails() {
			return Details;
		}
	}

	@Override
	public String toString() {
		return "{ \"Version\":\"" + Version + "\" , \"ApiVersion\":\"" + ApiVersion
				+ "\" , \"Os\":\"" + Os + "\" , \"Arch\":\"" + Arch + "\" , \"KernelVersion\":\""
				+ KernelVersion + "\"}";
	}
}
