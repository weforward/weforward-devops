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

import java.io.IOException;

import cn.weforward.common.crypto.Base64;
import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.util.StringUtil;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.ops.User;

/**
 * http验证器
 * 
 * @author daibo
 *
 */
public class HttpAuth {

	protected UserAuth m_UserAuth;

	protected String m_AuthorizationType = "Basic";

	public HttpAuth(UserAuth auth) {
		m_UserAuth = auth;
	}

	public void setAuthorizationType(String type) {
		m_AuthorizationType = type;
	}

	public User auth(RestfulRequest request, RestfulResponse response) throws IOException {
		if (null == m_UserAuth) {
			return null;
		}
		String authorization = request.getHeaders().get("Authorization");
		if (StringUtil.eq(m_AuthorizationType, "Basic")) {
			if (!StringUtil.isEmpty(authorization)) {
				String basic = m_AuthorizationType + " ";
				if (authorization.startsWith(basic)) {
					authorization = authorization.substring(basic.length());
					String code = new String(Base64.decode(authorization));
					int index = code.indexOf(":");
					String username = code.substring(0, index);
					String password = code.substring(index + 1);
					User user = check(username, password);
					if (null != user) {
						return user;
					}
				}
			}
			response.setHeader("WWW-Authenticate", m_AuthorizationType);
			return null;
		}
//		else if (Misc.eq(m_AuthorizationType, "Digest")) {
//			String realmName = "ourlinc";
//			String key = Misc.md5Hash(m_Password);
//			if (!Misc.isEmpty(authorization)) {
//				DigestData digestAuth = new DigestData(authorization);
//				if (Misc.eq(digestAuth.getUsername(), m_Username) && digestAuth.validateAndDecode(key, realmName)
//						&& !digestAuth.isNonceExpired()) {
//					String serverDigestMd5 = digestAuth.calculateServerDigest(m_Password, request.getVerb());
//					if (serverDigestMd5.equals(digestAuth.getResponse())) {
//						return true;
//					}
//				}
//
//			}
//			// compute a nonce (do not use remote IP address due to proxy farms)
//			// format of nonce is:
//			// base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
//			long nonceValiditySeconds = 8 * 60 * 60;
//
//			long expiryTime = System.currentTimeMillis() + (nonceValiditySeconds * 1000);
//			String signatureValue = Misc.md5Hash(expiryTime + ":" + key);
//			String nonceValue = expiryTime + ":" + signatureValue;
//			String nonceValueBase64 = new String(Base64.encode(nonceValue.getBytes()));
//
//			// qop is quality of protection, as defined by RFC 2617.
//			// we do not use opaque due to IE violation of RFC 2617 in not
//			// representing opaque on subsequent requests in same session.
//			String authenticateHeader = "Digest realm=\"" + realmName + "\", " + "qop=\"auth\", nonce=\""
//					+ nonceValueBase64 + "\"";
//			response.setHeader("WWW-Authenticate", authenticateHeader);
//			return false;
//		} else {
		throw new UnsupportedOperationException("不支持的验证方法" + m_AuthorizationType);
//		}

	}

	public User check(String userName, String password) {
		if (isMayAccessId(userName)) {
			User user = m_UserAuth.checkAccess(userName, password);
			if (null != user) {
				return user;
			}
		}
		User user = m_UserAuth.checkPassword(userName, password);
		if (null != user) {
			return user;
		}
		return null;
	}

	private boolean isMayAccessId(String id) {
		return null != id && id.startsWith(Access.KIND_USER + Access.SPEARATOR_STR);
	}

}
