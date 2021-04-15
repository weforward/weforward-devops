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

import org.json.JSONArray;
import org.json.JSONObject;

import cn.weforward.util.docker.ext.Util;

/**
 * docker 容器进程
 * 
 * @author daibo
 *
 */
public class DockerTop {
	/** 标题 */
	protected String[] Titles;
	/** 进程 */
	protected String[][] Processes;

	public DockerTop(JSONObject json) {
		Titles = Util.toStringArray(json.getJSONArray("Titles"));
		JSONArray arr = json.optJSONArray("Processes");
		if (null != arr && null != Titles) {
			Processes = new String[arr.length()][Titles.length];
			for (int i = 0; i < arr.length(); i++) {
				JSONArray carr = arr.getJSONArray(i);
				for (int j = 0; j < carr.length(); j++) {
					Processes[i][j] = carr.optString(j);
				}
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (null != Titles) {
			sb.append(Arrays.toString(Titles));

		}
		if (null != Processes) {
			for (int i = 0; i < Processes.length; i++) {
				sb.append("\n");
				sb.append(Arrays.toString(Processes[i]));
			}
		}
		return sb.toString();
	}

}
