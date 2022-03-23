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

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 证书工具类
 * 
 * @author daibo
 *
 */
public class CaUtil {
	/** docker路径 */
	private static final String DOCKER_PATH = "./ca/docker/";
	/** ssh路径 */
	private static final String SSH_PATH = "./ca/ssh/";

	/**
	 * ssh私钥文件
	 * 
	 * @return
	 */
	public static List<File> getSshPrvKeys() {
		// 写死路径
		File file = new File(SSH_PATH + "id_rsa");
		return file.exists() ? Collections.singletonList(file) : Collections.emptyList();
	}

	/**
	 * ssh公钥文件
	 * 
	 * @return
	 */
	public static File getSshPublicKey() {
		// 写死路径
		File file = new File(SSH_PATH + "id_rsa.pub");
		return file.exists() ? file : null;
	}

	/**
	 * 获取docker的ca证书
	 * 
	 * @return
	 */
	public static String getDockerCaPath() {
		// 写死路径
		File file = new File(DOCKER_PATH + "ca.pem");
		return file.exists() ? file.getAbsolutePath() : null;
	}

	/**
	 * 获取docker的服务器证书
	 * 
	 * @return
	 */
	public static String getDockerCertPath() {
		// 写死路径
		File file = new File(DOCKER_PATH + "cert.pem");
		return file.exists() ? file.getAbsolutePath() : null;
	}

	/**
	 * 获取docker的服务器key
	 * 
	 * @return
	 */
	public static String getDockerKeyPath() {
		// 写死路径
		File file = new File(DOCKER_PATH + "key.pem");
		return file.exists() ? file.getAbsolutePath() : null;
	}
}
