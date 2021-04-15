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
 * 绑定
 * 
 * @author daibo
 *
 */
public class Bind {
	/** 源目录 */
	@Resource
	protected String m_Source;
	/** 挂载目录 */
	@Resource
	protected String m_Target;
	/** 模式，可选 */
	@Resource
	protected String m_Mode;
	/** 模式-读写 */
	public static final String MODE_RW = "rw";
	/** 模式-只读 */
	public static final String MODE_RO = "ro";

	protected Bind() {

	}

	/**
	 * 构造
	 * 
	 * @param s 源目录
	 * @param t 挂载目录
	 */
	public Bind(String s, String t) {
		m_Source = s;
		m_Target = t;
	}

	/**
	 * 构造
	 * 
	 * @param s    源目录
	 * @param t    挂载目录
	 * @param mode 模式
	 */
	public Bind(String s, String t, String mode) {
		m_Source = StringUtil.toString(s).trim();
		m_Target = StringUtil.toString(t).trim();
		m_Mode = StringUtil.toString(mode).trim();
		;
	}

	/**
	 * 模式
	 * 
	 * @return MODE_XXX
	 */
	public String getMode() {
		return m_Mode;
	}

	/**
	 * 源目录
	 * 
	 * @return
	 */
	public String getSource() {
		return m_Source;
	}

	/**
	 * 挂载目录
	 * 
	 * @return
	 */
	public String getTarget() {
		return m_Target;
	}

	@Override
	public String toString() {
		return m_Source + ":" + m_Target + ":" + m_Mode;
	}
}