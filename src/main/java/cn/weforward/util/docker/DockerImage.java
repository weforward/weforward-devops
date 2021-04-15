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

import cn.weforward.util.docker.ext.Util;

/**
 * Docker镜像
 * 
 * @author daibo
 *
 */
public class DockerImage {
	/** 镜像id */
	private String Id;
	/** 父镜像id */
	private String ParentId;
	/** 库标签 */
	private String[] RepoTags;
	/** 库字典 */
	private String[] RepoDigests;
	/** 创建时间 */
	private long Created;
	/** 大小 */
	private long Size;
	/** 虚拟大小 */
	private long VirtualSize;
	/** 共享大小 */
	private long SharedSize;
	/** 容器数 */
	private int Containers;
	public Map<String, String> Labels;

	public DockerImage(JSONObject json) {
		Id = json.optString("Id");
		ParentId = json.optString("ParentId");
		RepoTags = Util.toStringArray(json.optJSONArray("RepoTags"));
		RepoDigests = Util.toStringArray(json.optJSONArray("RepoDigests"));
		Created = json.optLong("Created");
		Size = json.optLong("Size");
		VirtualSize = json.optLong("VirtualSize");
		SharedSize = json.optLong("SharedSize");
		Containers = json.optInt("Containers");
		JSONObject labels = json.optJSONObject("Labels");
		Labels = new HashMap<>();
		if (null != labels) {
			for (String key : labels.keySet()) {
				Labels.put(key, labels.optString(key));
			}
		}
	}

	public String getId() {
		return Id;
	}

	public String getParentId() {
		return ParentId;
	}

	public String[] getRepoTags() {
		return RepoTags;
	}

	public String[] getRepoDigests() {
		return RepoDigests;
	}

	public Map<String, String> getLabels() {
		return Labels;
	}

	public String getLabel(String key) {
		return Labels.get(key);
	}

	public long getCreated() {
		return Created;
	}

	public long getSize() {
		return Size;
	}

	public long getVirtualSize() {
		return VirtualSize;
	}

	public long getSharedSize() {
		return SharedSize;
	}

	public int getContainers() {
		return Containers;
	}

	@Override
	public String toString() {
		return Id + "," + Containers;
	}

}
