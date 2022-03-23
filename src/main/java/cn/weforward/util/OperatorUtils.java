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
package cn.weforward.util;

import cn.weforward.data.UniteId;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.ops.User;

/**
 * 操作工具类
 * 
 * @author daibo
 *
 */
public class OperatorUtils {
	/** 操作人为系统时的标识 */
	public static final UniteId OPERATOR_SYSTEM = UniteId.valueOf("sys");

	/**
	 * 获取操作人
	 * 
	 * @return
	 */
	public static UniteId getOperator() {
		User user = WeforwardSession.TLS.getOperator();
		if (null == user) {
			return OPERATOR_SYSTEM;
		}
		return UniteId.valueOf(user.getId()).changeCaption(user.getName());
	}
}
