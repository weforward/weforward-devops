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
package cn.weforward.devops.project;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.data.annotation.Inherited;

/**
 * 实例属性
 * 
 * @author daibo
 *
 */
@Inherited
public class RunningProp extends Prop {

	public static final String WEFORWARD_APIURL = "weforward.apiUrl";
	public static final String WEFORWARD_HOST = "weforward.host";
	public static final String WEFORWARD_PORT = "weforward.port";
	public static final String WEFORWARD_SERVICE_ACCESS_ID = "weforward.service.accessId";
	public static final String WEFORWARD_SERVICE_ACCESS_KEY = "weforward.service.accessKey";

	private static final List<String> BOOT_START_PROP = Arrays.asList(WEFORWARD_APIURL, WEFORWARD_HOST,
			WEFORWARD_SERVICE_ACCESS_ID, WEFORWARD_SERVICE_ACCESS_KEY);
	/** 范围 */
	@Resource
	protected int m_Scope;
	/** 属性范围-实例 */
	public static int SCOPE_RUNNING = 0;
	/** 属性范围-项目 */
	public static int SCOPE_PROJECT = 1;

	public RunningProp(String key, String value, int scope) {
		super(key, value);
		m_Scope = scope;
	}

	public static boolean isBootStartProp(String key) {
		return BOOT_START_PROP.contains(key);
	}

	/** 范围 */
	public int getScope() {
		return m_Scope;
	}

}
