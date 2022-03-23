package cn.weforward.devops.user.di;

import cn.weforward.data.persister.BusinessDi;
import cn.weforward.protocol.ops.User;

/**
 * 依赖di
 * 
 * @author daibo
 *
 */
public interface GroupDi extends BusinessDi {

	User getUser(String id);

}
