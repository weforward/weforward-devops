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
package cn.weforward.util.docker.ext;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 工具类
 * 
 * @author daibo
 *
 */
public class Util {
	/** 空字符串数组 */
	private static final String[] EMPTY_STRING = new String[0];
	/** 空整数数组 */
	private static final Integer[] EMPTY_INTEGER = new Integer[0];

	private Util() {

	}

	/**
	 * 值是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return null == value || value.length() == 0;
	}

	/**
	 * 限制输出字符数
	 * 
	 * @param result
	 * @param limit
	 * @return
	 */
	public static String limit(String result, int limit) {
		if (isEmpty(result)) {
			return result;
		}
		return result.length() > limit ? result.substring(0, limit - 3) + "..." : result;
	}

	/**
	 * 转换成整数
	 * 
	 * @param v
	 * @param defaultValue
	 * @return
	 */
	public static int toInt(String v, int defaultValue) {
		return isEmpty(v) ? defaultValue : Integer.parseInt(v);
	}

	/**
	 * 转换成字符串
	 * 
	 * @param v
	 * @return
	 */
	public static String toString(String v) {
		return null == v ? "" : v;
	}

	/**
	 * 转换成字符串数组
	 * 
	 * @param array
	 * @return
	 */
	public static String[] toStringArray(JSONArray array) {
		if (null == array) {
			return EMPTY_STRING;
		}
		String[] arr = new String[array.length()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = array.optString(i);
		}
		return arr;
	}

	public static Integer[] toIntArray(JSONArray array) {
		if (null == array) {
			return EMPTY_INTEGER;
		}
		Integer[] arr = new Integer[array.length()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = array.optInt(i);
		}
		return arr;
	}

	/**
	 * 转换成JSON数组
	 * 
	 * @param array
	 * @return
	 */
	public static JSONArray toJSONArray(Object[] arr) {
		if (null == arr) {
			return null;
		}
		JSONArray array = new JSONArray();
		for (int i = 0; i < arr.length; i++) {
			array.put(arr[i]);
		}
		return array;
	}

	/**
	 * 转换成json对象
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject toJSONObject(Map<String, String> map) {
		if (null == map) {
			return null;
		}
		JSONObject json = new JSONObject();
		for (Map.Entry<String, String> e : map.entrySet()) {
			json.put(e.getKey(), e.getValue());
		}
		return json;
	}

	/**
	 * 转换成map
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, String> toStringMap(JSONObject json) {
		if (null == json) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		for (String key : json.keySet()) {
			map.put(key, json.optString(key));
		}
		return map;
	}

	/**
	 * 转换成map
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Integer> toIntMap(JSONObject json) {
		if (null == json) {
			return null;
		}
		Map<String, Integer> map = new HashMap<>();
		for (String key : json.keySet()) {
			map.put(key, json.optInt(key));
		}
		return map;
	}

	/**
	 * 添加值
	 * 
	 * @param json
	 * @param key
	 * @param v
	 */
	public static void addIfNotNull(JSONObject json, String key, Object v) {
		if (null == v) {
			return;
		}
		json.put(key, v);
	}

	public static String getAbsolutePath(String path, String root) {
		// 使用user.dir作为相对目录的根目录
		path = Util.toString(path);
		char ch = (path.length() > 0) ? path.charAt(0) : ' ';
		// 要注意windows的路径 c:\xxx 之类
		if ('/' != ch && '\\' != ch && (path.length() < 2 || path.charAt(1) != ':')) {
			if (null == root) {
				root = System.getProperty("user.dir");
			}
			String sp = System.getProperty("file.separator");
			if (null == sp || 0 == sp.length()) {
				sp = "/";
			}
			ch = root.charAt(root.length() - 1);
			if ('/' == ch || '\\' == ch || sp.charAt(0) == ch) {
				root = root.substring(0, root.length() - 1);
			}
			// 转换上层路径“../”
			while (path.startsWith("../")) {
				int idx = root.lastIndexOf(sp);
				if (-1 == idx) {
					break;
				}
				root = root.substring(0, idx);
				path = path.substring(3, path.length());
			}
			path = root + sp + path;
		}
		return path;
	}

}
