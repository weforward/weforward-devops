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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 清单
 * 
 * @author daibo
 *
 */
public class DockerManifests {
	private int schemaVersion;
	/** 名称 */
	private String name;
	/** 标签 */
	private String tag;
	private String architecture;
	private List<FsLayer> fsLayers;
	/** 历史 */
	private List<History> history;

	public DockerManifests(JSONObject json) {
		schemaVersion = json.optInt("schemaVersion");
		name = json.optString("name");
		tag = json.optString("tag");
		architecture = json.optString("architecture");
		{
			JSONArray array = json.optJSONArray("fsLayers");
			if (null == array) {
				fsLayers = Collections.emptyList();
			} else {
				List<FsLayer> list = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					list.add(new FsLayer(array.getJSONObject(i)));
				}
				fsLayers = list;
			}
		}
		{
			JSONArray array = json.optJSONArray("history");
			if (null == array) {
				history = Collections.emptyList();
			} else {
				List<History> list = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					list.add(new History(array.getJSONObject(i)));
				}
				history = list;
			}
		}

	}

	/** 名称 */
	public String getName() {
		return name;
	}

	/** 标签 */
	public String getTag() {
		return tag;
	}

	/** 历史 */
	public List<History> getHistory() {
		return history;
	}

	public static class FsLayer {

		private String blobSum;

		public FsLayer(JSONObject json) {
			blobSum = json.optString("blobSum");
		}

		@Override
		public String toString() {
			return "{ \"blobSum\":\"" + blobSum + "\"}";
		}
	}

	public static class History {
		private String parent;
		private Date created;
		private String id;

		// "throwaway": true,
		// "Cmd": {
		// "container_config": "/bin/sh -c #(nop) USER boot"
		// }
		public History(JSONObject json) {
			String v1Compatibility = json.optString("v1Compatibility");
			json = new JSONObject(v1Compatibility);
			parent = json.optString("parent");
			String str = json.optString("created");
			created = p(str);
			id = json.optString("id");
		}

		static Calendar c;
		static {
			c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("GMT"));
		}

		private synchronized static Date p(String str) {
			c.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
			c.set(Calendar.MONTH, Integer.parseInt(str.substring(5, 7)) - 1);
			c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(8, 10)));
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(11, 13)));
			c.set(Calendar.MINUTE, Integer.parseInt(str.substring(14, 16)));
			c.set(Calendar.SECOND, Integer.parseInt(str.substring(17, 19)));
			c.set(Calendar.MILLISECOND, Integer.parseInt(str.substring(20, 23)));
			return c.getTime();
		}

		public String getId() {
			return id;
		}

		public String getParent() {
			return parent;
		}

		public Date getCreated() {
			return created;
		}

		@Override
		public String toString() {
			return "{ \"id\":\"" + id + "\" , \"created\":\"" + created + "\" , \"parent\":\"" + parent + "\" }";
		}
	}

	public String toString() {
		return "{ \"schemaVersion\":\"" + schemaVersion + "\" , \"name\":\"" + name + "\" , \"tag\":\"" + tag
				+ "\" , \"architecture\":\"" + architecture + "\" , \"fsLayers\":\"" + fsLayers + "\" , \"history\":\""
				+ history + "\"}";
	}

}
