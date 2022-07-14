package cn.weforward.devops.weforward;

import javax.annotation.Resource;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.*;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.metrics.MetricsService;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.client.util.IdBean;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;

import java.util.Date;

/**
 * 追踪方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
@DocMethods(index = 300)
public class TracerMethods {
	@Resource
	protected MetricsService m_MetricsService;

	@WeforwardMethod
	@DocMethod(description = "搜索追踪树", index = 1)
	public ResultPage<TracerTreeView> search(TracerParam params) throws ApiException {
		String id = params.getId();
		ResultPage<TracerSpanTree> rp;
		if (!StringUtil.isEmpty(id)) {
			TracerSpanTree tree = m_MetricsService.getTracer(getMyOrganization(),id);
			if (null != tree) {
				rp = ResultPageHelper.singleton(tree);
			} else {
				rp = ResultPageHelper.empty();
			}
		} else {
			ValidateUtil.isEmpty(params.getBegin(), "开始时间不能为空");
			ValidateUtil.isEmpty(params.getEnd(), "结束时间不能为空");
			rp = m_MetricsService.searchTracer(getMyOrganization(),params.getBegin(), params.getEnd(), params.getServiceName(),
					params.getServiceNo(), params.getMethod(),params.getMinDuration(),params.getMaxDuration());
		}
		return TransResultPage.valueOf(rp, (e) -> TracerTreeView.valueOf(e));
	}

	@WeforwardMethod
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "追踪id", necessary = true) })
	@DocMethod(description = "获取追踪树", index = 2)
	public TracerTreeDetailView get(IdBean params) throws ApiException {
		ValidateUtil.isEmpty(params.getId(), "id不能为空");
		return TracerTreeDetailView.valueOf(m_MetricsService.getTracer(getMyOrganization(), params.getId()));
	}

	@WeforwardMethod
	@DocMethod(description = "追踪分析常量值", index = 3)
	public TracerConstantView getTracerConstant(){
		return new TracerConstantView();
	}

	@WeforwardMethod
	@DocMethod(description = "接口调用情况", index = 4)
	public ApiInvokeInfoView searchApiInvokeInfo(ApiInvokeInfoParam params) throws ApiException {
		ValidateUtil.isEmpty(params.getServiceName(), "服务名不能为空");
		ValidateUtil.isEmpty(params.getServiceNo(), "服务(实例)编号不能为空");
		return ApiInvokeInfoView.valueOf(m_MetricsService.getApiInvokeInfo(getMyOrganization(),params.getBegin(),params.getEnd(),params.getServiceName(),params.getServiceNo(),null),params.getBegin(),params.getEnd());
	}

	@WeforwardMethod
	@DocMethod(description = "接口方法调用统计", index = 5)
	@DocParameter({
			@DocAttribute(index = 1,name = "serviceName",type = String.class,description = "服务名", necessary = true),
			@DocAttribute(index = 2,name = "serviceNo",type = String.class,description = "服务编号", necessary = true),
			@DocAttribute(index = 3,name = "methodName",type = String.class,description = "方法名", necessary = true),
			@DocAttribute(index = 4,name = "startTime",type = String.class,description = "查询开始时间，格式：2022-07-01 00:00:00", necessary = true),
			@DocAttribute(index = 5,name = "endTime",type = String.class,description = "查询结束时间，格式：2022-07-01 01:00:00", necessary = true),

	})
	public ApiInvokeStatView searchApiStat(FriendlyObject params) throws ApiException {
		ValidateUtil.isEmpty(params.getString("serviceName"), "服务名不能为空");
		ValidateUtil.isEmpty(params.getString("serviceNo"), "服务(实例)编号不能为空");
		ValidateUtil.isEmpty(params.getString("methodName"), "方法名不能为空");
		ValidateUtil.isEmpty(params.getString("startTime"), "查询的开始时间");
		ValidateUtil.isEmpty(params.getString("endTime"), "查询的结束时间");
		Date t1 = TimeUtil.parseDate(params.getString("startTime"));
		Date t2 = TimeUtil.parseDate(params.getString("endTime"));
		return ApiInvokeStatView.valueOf(m_MetricsService.getApiInvokeInfo(getMyOrganization(),t1,t2,params.getString("serviceName"),params.getString("serviceNo"), params.getString("methodName")),t1,t2);
	}

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
