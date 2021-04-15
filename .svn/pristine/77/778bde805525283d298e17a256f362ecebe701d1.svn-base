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

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.DebugParam;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocReturn;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.SearchServiceParams;
import cn.weforward.protocol.gateway.exception.KeeperException;
import cn.weforward.protocol.ops.ServiceExt;

/**
 * 调试方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
public class DebugMethods {

	static final Logger _Logger = LoggerFactory.getLogger(DebugMethods.class);

	@Resource
	protected Keeper m_Keeper;
	@Resource
	protected ProjectService m_ProjectService;

	@WeforwardMethod
	@DocMethod(description = "调试服务方法")
	@DocReturn(description = "调试服务方法返回值")
	public DtObject service(DebugParam params) throws ApiException {
		String scriptArgs = params.getScriptArgs();
		String scriptName = params.getScriptName();
		String scriptSource = params.getScriptSource();
		String serviceNo = params.getServiceNo();
		String serviceName = params.getServiceName();
		ServiceExt s = getService(serviceNo, serviceName);
		if (null == s) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "找不到对应的服务");
		}
		String rid = s.getRunningId();
		if (StringUtil.isEmpty(rid)) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "对象的服务未配置实例ID");
		}
		Running running = m_ProjectService.getRunning(getMyOrganization(), rid);
		if (null == running) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "找不到对应的实例[" + rid + "]");
		}
		if (!running.isRight(Running.RIGHT_UPGRADE)) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "需要有部署实例权限才能执行脚本");
		}
		DtObject back = null;
		try {
			back = m_Keeper.debugService(serviceName, serviceNo, scriptSource, scriptName, scriptArgs);
		} catch (KeeperException e) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, e.getMessage());
		} finally {
			_Logger.info("debug user:{},params:{},back:{}", WeforwardSession.TLS.getUser(), params, back);
		}
		return back;
	}

	private ServiceExt getService(String serviceNo, String serviceName) {
		SearchServiceParams params = new SearchServiceParams();
		params.setKeyword(serviceName);
		ResultPage<ServiceExt> rp = m_Keeper.searchService(params);
		for (ServiceExt s : ResultPageHelper.toForeach(rp)) {
			if (StringUtil.eq(s.getName(), serviceName) && StringUtil.eq(s.getNo(), serviceNo)) {
				return s;
			}
		}
		return null;
	}

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
