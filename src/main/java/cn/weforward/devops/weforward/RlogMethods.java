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

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.LogDetial;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.rlog.Content;
import cn.weforward.rlog.Directory;
import cn.weforward.rlog.LogPage;
import cn.weforward.rlog.RemoteLogService;
import cn.weforward.rlog.Server;
import cn.weforward.rlog.Subject;

/**
 * 远程日志方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
public class RlogMethods {
	@Resource
	protected RemoteLogService m_RlogService;

	@WeforwardMethod
	@DocMethod(description = "服务器列表")
	public List<Server> servers() {
		return m_RlogService.listServer(getMyOrganization());
	}

	@WeforwardMethod
	@DocMethod(description = "目录列表")
	@DocParameter(@DocAttribute(name = "server", type = String.class, description = "服务器"))
	public ResultPage<Directory> directorys(FriendlyObject params) {
		return m_RlogService.listDirectory(getMyOrganization(), params.getString("server"));
	}

	@WeforwardMethod
	@DocMethod(description = "主题列表")
	@DocParameter({ @DocAttribute(name = "server", type = String.class, description = "服务器"),
			@DocAttribute(name = "directory", type = String.class, description = "目录") })
	public ResultPage<Subject> subjects(FriendlyObject params) throws ApiException {
		try {
			return m_RlogService.listSubject(getMyOrganization(), params.getString("server"),
					params.getString("directory"));
		} catch (IOException e) {
			throw new ApiException(ApiException.CODE_INTERNAL_ERROR, e.getMessage(), e);
		}
	}

	@WeforwardMethod
	@DocMethod(description = "内容")
	@DocParameter({ @DocAttribute(name = "server", type = String.class, description = "服务器"),
			@DocAttribute(name = "directory", type = String.class, description = "目录"),
			@DocAttribute(name = "subject", type = String.class, description = "主题") })
	public Content content(FriendlyObject params) throws ApiException {
		try {
			return m_RlogService.getContent(getMyOrganization(), params.getString("server"),
					params.getString("directory"), params.getString("subject"));
		} catch (IOException e) {
			throw new ApiException(ApiException.CODE_INTERNAL_ERROR, e.getMessage(), e);
		}
	}

	@WeforwardMethod
	@DocMethod(description = "详情列表")
	@DocParameter({ @DocAttribute(name = "server", type = String.class, description = "服务器"),
			@DocAttribute(name = "directory", type = String.class, description = "目录"),
			@DocAttribute(name = "keywords", type = String.class, description = "关键字"),
			@DocAttribute(name = "op", type = String.class, description = "操作，first：最前；last：最后；pre：向前；next：向后") })
	public LogDetial detail(FriendlyObject params) throws ApiException {
		try {
			String k = params.getString("keywords");
			String op = params.getString("op");

			LogPage logs = m_RlogService.getDetail(getMyOrganization(),
					params.getString("server"),
					params.getString("directory"));
			long p = params.getLong("page", logs.getPageCount() + 1);
			LogDetial d = new LogDetial();
			if ("first".equals(op)) {
				String rs = logs.get(1);
				d.setContent(rs);
				d.setPage(p);
				d.setPageCount(logs.getPageCount());
				return d;
			} else if ("last".equals(op)) {
				String rs = logs.get(logs.getPageCount());
				d.setContent(rs);
				d.setPage(p);
				d.setPageCount(logs.getPageCount());
				return d;
			}
			int step;
			if ("pre".equals(op)) {
				step = -1;// 向前搜索
			} else if ("next".equals(op)) {
				step = 1;// 向后搜索
			} else {
				step = -1;// 默认向前
			}
			long cp = p;
			while (logs.gotoPage(cp = (cp + step))) {
				p = cp;
				String rs = logs.get(cp);
				if (StringUtil.isEmpty(k) || rs.contains(k)) {
					d.setContent(rs);
					break;
				}
			}
			d.setPage(p);
			d.setPageCount(logs.getPageCount());
			return d;
		} catch (IOException e) {
			throw new ApiException(ApiException.CODE_INTERNAL_ERROR, e.getMessage(), e);
		}
	}

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
