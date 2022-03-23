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

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;

import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.Prop;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.support.datatype.FriendlyObject;

/**
 * 配置方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_SERVICE)
public class ConfigMethods {

	static final org.slf4j.Logger _Logger = LoggerFactory.getLogger(ConfigMethods.class);

	@Resource
	protected ProjectService m_ProjectService;

	@WeforwardMethod
	public List<Prop> serviceprops(FriendlyObject params) throws ApiException {
		String projectName = params.getString("projectName");
		String serverid = params.getString("serverid");
		WeforwardSession session = WeforwardSession.TLS.getSession();
		if (null == session) {
			return Collections.emptyList();
		}
		return m_ProjectService.loadServiceProperties(projectName, serverid, session.getAccessId());
	}

}
