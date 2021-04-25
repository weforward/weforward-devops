package cn.weforward.devops.user;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.ops.AccessExt;

/**
 * 凭证管理
 * 
 * @author daibo
 *
 */
public class AccessKeeper {

	protected Keeper m_Keeper;

	public AccessKeeper(Keeper keeper) {
		m_Keeper = keeper;
	}

	public AccessExt getAccess(String accessId) {
		ResultPage<AccessExt> rp = m_Keeper.listAccess(Access.KIND_SERVICE, null, null);
		for (AccessExt a : ResultPageHelper.toForeach(rp)) {
			if (StringUtil.eq(accessId, a.getAccessId())) {
				return a;
			}
		}
		return null;
	}

	public AccessExt checkAccess(String accessId, String accessKey) {
		AccessExt a = getAccess(accessId);
		if (null == a) {
			return null;
		}
		if (!StringUtil.eq(a.getAccessKeyBase64(), accessKey) && !StringUtil.eq(a.getAccessKeyHex(), accessKey)) {
			return null;
		}
		return a;
	}
}
