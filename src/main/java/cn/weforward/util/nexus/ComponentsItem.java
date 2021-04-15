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

import org.json.JSONObject;

import cn.weforward.common.util.VersionUtil;

/**
 * 组件项
 * 
 * @author daibo
 *
 */
public class ComponentsItem implements Comparable<ComponentsItem> {
	/** 唯一id */
	protected String id;
	/** 所属库 */
	protected String repository;
	/** 格式 */
	protected String format;
	/** 组 */
	protected String group;
	/** 名称 */
	protected String name;
	/** 版本 */
	protected String version;

	public ComponentsItem(JSONObject json) {
		id = json.optString("id");
		repository = json.optString("repository");
		format = json.optString("format");
		group = json.optString("group");
		name = json.optString("name");
		version = json.optString("version");
	}

	public String getId() {
		return id;
	}

	public String getRepository() {
		return repository;
	}

	public String getFormat() {
		return format;
	}

	public String getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public int compareTo(ComponentsItem o) {
		int v = name.compareTo(o.name);
		if (v == 0) {
			return VersionUtil.compareTo(version, o.version);
		}
		return v;
	}

}
