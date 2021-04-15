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
package cn.weforward.devops.project.impl;

import java.util.Collections;
import java.util.List;

import cn.weforward.common.NameItem;
import cn.weforward.data.annotation.Inherited;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractProject;
import cn.weforward.devops.user.Organization;

/**
 * html项目
 * 
 * @author daibo
 *
 */
@Inherited
public class HtmlProject extends AbstractProject {
	/** 访问地址 */
	@ResourceExt(component = String.class)
	protected List<String> m_AccessUrls;

	protected HtmlProject(ProjectDi di) {
		super(di);
	}

	public HtmlProject(ProjectDi di, Organization org, String name) {
		super(di, org, name);
	}

	/** 访问地址 */
	public void setAccessUrls(List<String> urls) {
		checkRight(RIGHT_UPDATE);
		m_AccessUrls = urls;
		markPersistenceUpdate();
	}

	/** 访问地址 */
	public List<String> getAccessUrls() {
		return null == m_AccessUrls ? Collections.emptyList() : m_AccessUrls;
	}

	@Override
	public NameItem getType() {
		return TYPE_HTML;
	}
}
