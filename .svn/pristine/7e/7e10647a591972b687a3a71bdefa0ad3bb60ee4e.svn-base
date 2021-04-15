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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 组件结果
 * 
 * @author daibo
 *
 */
public class Components {
	/** 继续获取数据的token */
	protected String continuationToken;
	/** 数据项 */
	protected List<ComponentsItem> items;

	public Components(JSONObject json) {
		continuationToken = json.optString("continuationToken");
		items = new ArrayList<>();
		JSONArray array = json.optJSONArray("items");
		for (int i = 0; i < array.length(); i++) {
			items.add(new ComponentsItem(array.optJSONObject(i)));
		}
	}

	public String getContinuationToken() {
		return continuationToken;
	}

	public List<ComponentsItem> getItems() {
		return items;
	}

}
