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
package cn.weforward.rlog.impl;

import java.util.Comparator;

import cn.weforward.rlog.Directory;

/**
 * 目录
 * 
 * @author daibo
 *
 */
public class DirectoryVo implements Directory {
	/** 排序 */
	final static Comparator<String> _BY_NAME_DESC = new Comparator<String>() {

		@Override
		public int compare(String n1, String n2) {
			if (null == n1 && null == n2) {
				return 0;
			} else if (null == n1) {
				return 1;
			} else if (null == n2) {
				return -1;
			}
			return n2.compareTo(n1);
		}

	};
	/** 名称 */
	protected String m_Name;
	/** 路径 */
	protected String m_Path;
	/** 数量 */
	protected int m_Num;

	public DirectoryVo(String name, String path, int num) {
		m_Name = name;
		m_Path = path;
		m_Num = num;
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public int getNum() {
		return m_Num;
	}

}
