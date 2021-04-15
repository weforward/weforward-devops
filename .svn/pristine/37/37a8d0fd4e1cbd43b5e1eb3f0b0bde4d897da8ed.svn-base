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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Docker镜像
 * 
 * @author daibo
 *
 */
public class DockerImageInspect {
	/** 镜像id */
	private String Id;
	/** 容器数 */
	private int Containers;

	private ContainerConfig ContainerConfig;

	public DockerImageInspect(JSONObject json) {
		Id = json.optString("Id");
		Containers = json.optInt("Containers");
		ContainerConfig = new ContainerConfig(json.optJSONObject("ContainerConfig"));
	}

	public String getId() {
		return Id;
	}

	public int getContainers() {
		return Containers;
	}

	public ContainerConfig getContainerConfig() {
		return ContainerConfig;
	}

	@Override
	public String toString() {
		return Id + "," + Containers;
	}

	public static class ContainerConfig {

		public String Hostname;

		public Map<String, String> Labels;

		public ContainerConfig(JSONObject json) {
			Hostname = json.optString("Hostname");
			JSONObject labels = json.optJSONObject("Labels");
			Labels = new HashMap<>();
			if (null != labels) {
				for (String key : labels.keySet()) {
					Labels.put(key, labels.optString(key));
				}
			}

		}

		public String getHostname() {
			return Hostname;
		}

		public Map<String, String> getLabels() {
			return Labels;
		}

		public String getLabel(String key) {
			return Labels.get(key);
		}

	}

	public String getLabel(String key) {
		return getContainerConfig().getLabel(key);
	}

}
