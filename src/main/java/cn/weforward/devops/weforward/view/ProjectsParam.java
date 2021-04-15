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
package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 项目参数
 * 
 * @author daibo
 *
 */
@DocObject(description = "项目搜索参数")
public class ProjectsParam {
	/** 关键字 */
	protected String m_Keywords;

	/** 运维组id */
	protected String m_Groupid;
	/** 类型 */
	protected String m_Type;

	public void setKeywords(String keywords) {
		m_Keywords = keywords;
	}

	@DocAttribute(description = "参数关键字", example = "aaa")
	public String getKeywords() {
		return m_Keywords;
	}

	public void setGroupid(String groupid) {
		m_Groupid = groupid;
	}

	@DocAttribute(description = "运维组id")
	public String getGroupid() {
		return m_Groupid;
	}

	public void setType(String type) {
		m_Type = type;
	}

	@DocAttribute(description = "类型，如:JAVA|HTML")
	public String getType() {
		return m_Type;
	}

}
