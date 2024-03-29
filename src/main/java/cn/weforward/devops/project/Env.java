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

import javax.annotation.Resource;

import cn.weforward.common.util.StringUtil;

/**
 * 环境变量
 * 
 * @author daibo
 *
 */
public class Env {
	/** 键 */
	@Resource
	protected String m_Key;
	/** 值 */
	@Resource
	protected String m_Value;

	protected Env() {

	}

	public Env(String key, String value) {
		m_Key = StringUtil.toString(key).trim();
		m_Value = StringUtil.toString(value).trim();
	}

	public String getKey() {
		return m_Key;
	}

	public String getValue() {
		return m_Value;
	}

	@Override
	public String toString() {
		return m_Key + ":" + m_Value;
	}
}
