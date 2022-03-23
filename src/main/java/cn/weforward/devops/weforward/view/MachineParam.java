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
package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;

/**
 * 机器参数
 * 
 * @author daibo
 *
 */
public class MachineParam extends BaseParam {
	/** id */
	protected String m_Id;
	/** 名称 */
	protected String m_Name;
	/** 所有者 */
	protected String m_Owner;
	/** 类型 */
	protected String m_Type;
	/** 链接 */
	protected String m_Url;
	/** 根目录 */
	protected String m_Root;
	/** 密码 */
	protected String m_Password;
	/** 证书路径 */
	protected String m_CertPath;
	/** 密钥路径 */
	protected String m_KeyPath;
	/** CA证书路径 */
	protected String m_CaPath;

	public MachineParam() {

	}

	@DocAttribute(description = "唯一id", index = 1)
	public String getId() {
		return m_Id;
	}

	public void setId(String id) {
		m_Id = id;
	}

	@DocAttribute(description = "名称", index = 2)
	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	@DocAttribute(description = "拥有者", index = 3)
	public String getOwner() {
		return m_Owner;
	}

	public void setOwner(String owner) {
		m_Owner = owner;
	}

	@DocAttribute(description = "类型", index = 4)
	public String getType() {
		return m_Type;
	}

	public void setType(String type) {
		m_Type = type;
	}

	@DocAttribute(description = "链接")
	public String getUrl() {
		return m_Url;
	}

	public void setUrl(String url) {
		m_Url = url;
	}

	@DocAttribute(description = "根目录")
	public String getRoot() {
		return m_Root;
	}

	public void setRoot(String root) {
		m_Root = root;
	}

	public String getCertPath() {
		return m_CertPath;
	}

	public void setCertPath(String certPath) {
		m_CertPath = certPath;
	}

	public String getKeyPath() {
		return m_KeyPath;
	}

	public void setKeyPath(String keyPath) {
		m_KeyPath = keyPath;
	}

	public String getCaPath() {
		return m_CaPath;
	}

	public void setCaPath(String caPath) {
		m_CaPath = caPath;
	}

	public String getPassword() {
		return m_Password;
	}

	public void setPassword(String password) {
		m_Password = password;
	}

}
