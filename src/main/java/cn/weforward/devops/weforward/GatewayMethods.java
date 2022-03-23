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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.AccessView;
import cn.weforward.devops.weforward.view.RightTableInfoView;
import cn.weforward.devops.weforward.view.ServiceInfoView;
import cn.weforward.devops.weforward.view.TrafficTableInfoView;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.datatype.DtBase;
import cn.weforward.protocol.datatype.DtNumber;
import cn.weforward.protocol.datatype.DtString;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.ServiceSummary;
import cn.weforward.protocol.gateway.vo.RightTableItemVo;
import cn.weforward.protocol.gateway.vo.RightTableItemWrap;
import cn.weforward.protocol.gateway.vo.TrafficTableItemVo;
import cn.weforward.protocol.gateway.vo.TrafficTableItemWrap;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.protocol.ops.ServiceExt;
import cn.weforward.protocol.ops.secure.RightTable;
import cn.weforward.protocol.ops.traffic.TrafficTable;
import cn.weforward.protocol.support.datatype.FriendlyObject;

/**
 * 网关Keeper相关接口
 * 
 * @author daibo
 * @author zhangpengji
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
public class GatewayMethods {
	@Autowired(required = false)
	protected Keeper m_Keeper;
	@Resource
	protected OrganizationProvider m_OrganizationProvider;

	static final org.slf4j.Logger _Logger = LoggerFactory.getLogger(GatewayMethods.class);

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

	@WeforwardMethod
	public ResultPage<Organization> merchants(FriendlyObject params) {
		if (null == m_Keeper) {
			return ResultPageHelper.empty();
		}
		String k = params.getString("keywords");
		return m_OrganizationProvider.search(k);
	}

	@DocMethod(title = "创建Access")
	@WeforwardMethod
	public AccessView access(FriendlyObject params) throws ApiException {
		String op = params.getString("op");
		AccessExt info = null;
		if ("create".equals(op)) {
			String kind = params.getString("kind");
			String group = getMyOrganization().getId();
			String summary = params.getString("summary");
			info = m_Keeper.createAccess(kind, group, summary);
		}
		return AccessView.valueOf(info, m_OrganizationProvider);
	}

	@DocMethod(title = "列举Access", description = "按类型、组、关键字列举Access")
	@DocParameter({ @DocAttribute(name = "kind", type = String.class, necessary = true),
			@DocAttribute(name = "group", type = String.class), @DocAttribute(name = "keyword", type = String.class) })
	@WeforwardMethod
	public ResultPage<AccessView> accesses(FriendlyObject params) throws ApiException {
		if (null == m_Keeper) {
			return ResultPageHelper.empty();
		}
		String kind = params.getString("kind");
		String group = getMyOrganization().getId();
		String keyword = params.getString("keyword");
		ResultPage<AccessExt> rp = m_Keeper.listAccess(kind, group, keyword);
		return new TransResultPage<AccessView, AccessExt>(rp) {

			@Override
			protected AccessView trans(AccessExt src) {
				return AccessView.valueOf(src, m_OrganizationProvider);
			}
		};
	}

	@WeforwardMethod
	public ResultPage<String> serviceNames(FriendlyObject params) {
		if (null == m_Keeper) {
			return ResultPageHelper.empty();
		}
		String name = params.getString("name");
		String group = getMyOrganization().getId();
		return m_Keeper.listServiceName(name, group);
	}

	@DocMethod(description = "列举已注册的微服务实例")
	@DocParameter({ @DocAttribute(name = "name", type = String.class) })
	@WeforwardMethod
	public List<ServiceInfoView> serviceDetails(FriendlyObject params) {
		if (null == m_Keeper) {
			return Collections.emptyList();
		}
		String name = params.getString("name");
		String group = getMyOrganization().getId();
		ResultPage<ServiceExt> rp = m_Keeper.listService(name, group);
		rp.setPageSize(rp.getCount());
		rp.gotoPage(1);
		List<ServiceInfoView> list = new ArrayList<>();
		for (ServiceExt info : rp) {
			list.add(new ServiceInfoView(info));
		}
		return list;
	}

	@WeforwardMethod
	public RightTableInfoView right(FriendlyObject params) {
		String name = params.getString("name");
		String group = getMyOrganization().getId();
		if (!m_Keeper.isExistService(name, group)) {
			return null;
		}
		String op = params.getString("op");
		RightTable info;
		if ("append".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			RightTableItemVo item = new RightTableItemVo();
			item.setName(m.getString("name"));
			item.setAccessKind(m.getString("access_kind"));
			item.setAccessId(m.getString("access_id"));
			item.setAccessGroup(m.getString("access_group"));
			item.setDescription(m.getString("description"));
			item.setAllow(m.getBoolean("allow", false));
			info = m_Keeper.appendRightRule(name, new RightTableItemWrap(item));
		} else if ("replace".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			String replaceName = m.getString("replace_name");
			int index = getInt(m, "index", 0);
			RightTableItemVo item = new RightTableItemVo();
			item.setName(m.getString("name"));
			item.setAccessKind(m.getString("access_kind"));
			item.setAccessId(m.getString("access_id"));
			item.setAccessGroup(m.getString("access_group"));
			item.setDescription(m.getString("description"));
			item.setAllow(m.getBoolean("allow", false));
			info = m_Keeper.replaceRightRule(name, new RightTableItemWrap(item), index, replaceName);
		} else if ("remove".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			String removeName = m.getString("remove_name");
			int index = getInt(m, "index", 0);
			info = m_Keeper.removeRightRule(name, index, removeName);
		} else if ("move".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			int from = getInt(m, "from", 0);
			int to = getInt(m, "to", 0);
			info = m_Keeper.moveRightRule(name, from, to);
		} else {
			info = m_Keeper.getRightTable(name);
			if (null == info) {
				_Logger.warn("right找不到[" + name + "]");
			}
		}
		return RightTableInfoView.valueOf(name, info, m_OrganizationProvider);
	}

	@WeforwardMethod
	public TrafficTableInfoView traffic(FriendlyObject params) {
		String name = params.getString("name");
		String op = params.getString("op");
		String group = getMyOrganization().getId();
		if (!m_Keeper.isExistService(name, group)) {
			return null;
		}
		TrafficTable info;
		if ("append".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			TrafficTableItemVo item = new TrafficTableItemVo(m.getString("no"), m.getString("version"));
			String n = m.getString("name");
			if (!StringUtil.isEmpty(n)) {
				item.setName(n);
			}
			/** 权重。由1~100，-100表示其为后备，默认为1 */
			item.setWeight(getInt(m, "weight", 1));
			/** 设置连续失败此值后将标记项为失败且不使用，直至fail_timeout后才重新使用，默认为1 */
			item.setMaxFails(getInt(m, "max_fails", 1));
			/** 标记为失败的项重新再使用的时间（秒），默认为60秒（1分钟） */
			item.setFailTimeout(getInt(m, "fail_timeout", 60));
			/** 控制并发量防止过载，默认为0（不限制） */
			item.setMaxConcurrent(getInt(m, "max_concurrent", 0));
			/** 连接超时值（秒），默认为10秒 */
			item.setConnectTimeout(getInt(m, "connect_timeout", 10));
			/** 读取超时值（秒），默认为20秒 */
			item.setReadTimeout(getInt(m, "read_timeout", 20));
			info = m_Keeper.appendTrafficRule(name, new TrafficTableItemWrap(item));
		} else if ("replace".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			String replaceName = m.getString("replace_name");
			int index = getInt(m, "index", 0);
			TrafficTableItemVo item = new TrafficTableItemVo(m.getString("no"), m.getString("version"));
			String n = m.getString("name");
			if (!StringUtil.isEmpty(n)) {
				item.setName(n);
			}
			/** 权重。由1~100，-100表示其为后备，默认为1 */
			item.setWeight(getInt(m, "weight", 1));
			/** 设置连续失败此值后将标记项为失败且不使用，直至fail_timeout后才重新使用，默认为1 */
			item.setMaxFails(getInt(m, "max_fails", 1));
			/** 标记为失败的项重新再使用的时间（秒），默认为60秒（1分钟） */
			item.setFailTimeout(getInt(m, "fail_timeout", 60));
			/** 控制并发量防止过载，默认为0（不限制） */
			item.setMaxConcurrent(getInt(m, "max_concurrent", 0));
			/** 连接超时值（秒），默认为10秒 */
			item.setConnectTimeout(getInt(m, "connect_timeout", 10));
			/** 读取超时值（秒），默认为20秒 */
			item.setReadTimeout(getInt(m, "read_timeout", 20));
			info = m_Keeper.replaceTrafficRule(name, new TrafficTableItemWrap(item), index, replaceName);
		} else if ("remove".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			String removeName = m.getString("remove_name");
			int index = getInt(m, "index", 0);
			info = m_Keeper.removeTrafficRule(name, index, removeName);
		} else if ("move".equals(op)) {
			FriendlyObject m = new FriendlyObject(params.getObject("item"));
			int from = getInt(m, "from", 0);
			int to = getInt(m, "to", 0);
			info = m_Keeper.moveTrafficRule(name, from, to);
		} else {
			info = m_Keeper.getTrafficTable(name);
			if (null == info) {
				_Logger.warn("traffic找不到[" + name + "]");
			}
		}
		return TrafficTableInfoView.valueOf(name, info);
	}

	@DocMethod(description = "列举微服务的概要信息")
	@DocParameter({ @DocAttribute(name = "name", type = String.class) })
	@WeforwardMethod
	public ResultPage<ServiceSummary> serviceSummarys(FriendlyObject params) {
		if (null == m_Keeper) {
			return ResultPageHelper.empty();
		}
		String group = getMyOrganization().getId();
		ResultPage<ServiceSummary> summarys = m_Keeper.listServiceSummary(params.getString("name"), group);
		return summarys;
	}

	private int getInt(FriendlyObject m, String name, int defaultValue) {
		DtBase base = m.getBase(name);
		if (null == base) {
			return defaultValue;
		} else if (base instanceof DtNumber) {
			return ((DtNumber) base).valueInt();
		} else if (base instanceof DtString) {
			return NumberUtil.toInt(((DtString) base).value(), defaultValue);
		} else {
			return NumberUtil.toInt(base.toString(), defaultValue);
		}
	}

}
