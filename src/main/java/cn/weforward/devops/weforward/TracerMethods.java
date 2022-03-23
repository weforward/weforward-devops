package cn.weforward.devops.weforward;

import javax.annotation.Resource;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.TracerParam;
import cn.weforward.devops.weforward.view.TracerTreeDetailView;
import cn.weforward.devops.weforward.view.TracerTreeView;
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
					params.getServiceNo(), params.getMethod());
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

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
