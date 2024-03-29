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
package cn.weforward.devops.weforward;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.weforward.view.MachineInfoParam;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.framework.exception.ForwardException;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.doc.annotation.DocMethod;

/**
 * 机器服务相关方法
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_SERVICE)
public class MachineMethods {

	final static Logger _Logger = LoggerFactory.getLogger(MachineMethods.class);

	@Resource
	protected ProjectService m_ProjectService;

	@DocMethod(description = "机器心跳方法")
	@WeforwardMethod
	public void heartbeat(MachineInfoParam param) {
		Machine m = m_ProjectService.findMachine(WeforwardSession.TLS.getSession().getAccessId(), param.getName());
		ForwardException.forwardToIfNeed(m);
		if (null != m) {
			m.setInfo(param.getInfo());
		}
	}
}
