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
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.array.LabelElement;
import cn.weforward.devops.project.Prop;

/**
 * 服务配置元素
 * 
 * 实例>项目>机器>全局
 * 
 * @author daibo
 *
 */
public class ServiceProperties implements LabelElement, Iterable<Prop> {
	@Resource
	protected String m_Id;

	@ResourceExt(component = Prop.class)
	protected List<Prop> m_Props = Collections.emptyList();

	public static final ServiceProperties EMPTY = new ServiceProperties("", Collections.emptyList());

	protected ServiceProperties() {
	}

	public ServiceProperties(String sid, List<Prop> props) {
		m_Id = sid;
		m_Props = props;
	}

	@Override
	public String getIdForLabel() {
		return m_Id;
	}

	@Override
	public Iterator<Prop> iterator() {
		return getProps().iterator();
	}

	public List<Prop> getProps() {
		return m_Props;
	}

}
