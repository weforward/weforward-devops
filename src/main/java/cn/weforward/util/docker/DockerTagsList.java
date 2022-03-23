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

import java.util.Arrays;

import org.json.JSONObject;

import cn.weforward.util.docker.ext.Util;

/**
 * 标签列表
 * 
 * @author daibo
 *
 */
public class DockerTagsList {
	/** 名称 */
	private String name;
	/** 标签 */
	private String[] tags;

	public DockerTagsList(JSONObject json) {
		name = json.optString("name");
		tags = Util.toStringArray(json.optJSONArray("tags"));
	}

	public DockerTagsList(String name) {
		this.name = name;
		this.tags = new String[0];
	}

	public String getName() {
		return name;
	}

	public String[] getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "{ 'name':'" + name + "' , 'tags':'" + Arrays.toString(tags) + "'}";
	}
}
