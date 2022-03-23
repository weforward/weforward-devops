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
package cn.weforward.util.docker.ext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import cn.weforward.util.HttpInvoker;

/**
 * 抽象客户端
 * 
 * @author daibo
 *
 */
public class AbstractClient {

	static {
		try {
			Class<?> clazz = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
			Security.addProvider((Provider) clazz.getConstructor().newInstance());
		} catch (Throwable e) {
		}

	}
	/** 调用器 */
	protected HttpInvoker m_Invoker;
	/** 请求链接 */
	protected String m_Url;
	/** 请求容器 */
	protected HttpClientContext m_Context;
	/** 用户名 */
	protected String m_Username;
	/** 密码 */
	protected String m_Password;

	public AbstractClient() throws IOException {
		this(10, 30);
	}

	public AbstractClient(int connectionSecond, int soSecond) throws IOException {
		m_Invoker = new HttpInvoker(connectionSecond, soSecond);
	}

	public AbstractClient(int connectionSecond, int soSecond, String certPath, String keyPath, String caPath)
			throws IOException {
		SSLContext sslContext = createSSLContentIfNeed(certPath, keyPath, caPath);
		m_Invoker = new HttpInvoker(connectionSecond, soSecond, sslContext);

	}

	public void setUrl(String url) {
		if (null != url && !url.endsWith("/")) {
			url += "/";
		}
		m_Url = url;
	}

	public void setUsername(String username) {
		m_Username = username;
		createContextIfNeed();
	}

	public void setPassword(String password) {
		m_Password = password;
		createContextIfNeed();
	}

	private void createContextIfNeed() {
		if (Util.isEmpty(m_Username) || Util.isEmpty(m_Password)) {
			m_Context = null;
		} else {
			HttpClientContext context = new HttpClientContext();
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(m_Username, m_Password));
			context.setCredentialsProvider(credentialsProvider);
			m_Context = context;
		}
	}

	private static SSLContext createSSLContentIfNeed(String certPath, String keyPath, String caPath)
			throws IOException {
		if (Util.isEmpty(certPath) || Util.isEmpty(keyPath) || Util.isEmpty(caPath)) {
			return null;
		}
		try {
			String certPemPath = certPath;
			String keyPemPath = keyPath;
			String caPemPath = caPath;
			// 服务器给客户端的证书和密钥
			PrivateKey key;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(keyPemPath)))) {
				key = loadPrivateKey(reader);
			}
			List<Certificate> certs;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(certPemPath)))) {
				certs = loadCertificates(reader);
			}
			char[] passwordchar = String.valueOf(new Random().nextLong()).toCharArray();
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(null);
			keyStore.setKeyEntry("key", key, passwordchar, certs.toArray(new Certificate[certs.size()]));
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, passwordchar);
			// ca证书
			KeyStore trustKey = KeyStore.getInstance("JKS");
			trustKey.load(null);
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(caPemPath)))) {
				int index = 1;
				for (Certificate c : loadCertificates(reader)) {
					trustKey.setCertificateEntry("ca-" + index, c);
					index++;
				}
			}
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustKey);
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
			return sslContext;
		} catch (Throwable e) {
			throw new IOException("证书异常", e);
		}
	}

	protected String genUri(String path) {
		return m_Url + path;
	}

	protected HttpResponse execute(HttpUriRequest request) throws IOException {
		HttpResponse res;
		if (null == m_Context) {
			res = m_Invoker.execute(request);
		} else {
			res = m_Invoker.execute(request, m_Context);
		}
		return res;
	}

	private static PrivateKey loadPrivateKey(final Reader reader)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		try (PEMParser pemParser = new PEMParser(reader)) {
			Object readObject = pemParser.readObject();
			while (readObject != null) {
				PrivateKeyInfo privateKeyInfo = getPrivateKeyInfoOrNull(readObject);
				if (privateKeyInfo != null) {
					return new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
				}
				readObject = pemParser.readObject();
			}
		}
		return null;
	}

	private static PrivateKeyInfo getPrivateKeyInfoOrNull(Object pemObject) throws NoSuchAlgorithmException {
		PrivateKeyInfo privateKeyInfo = null;
		if (pemObject instanceof PEMKeyPair) {
			PEMKeyPair pemKeyPair = (PEMKeyPair) pemObject;
			privateKeyInfo = pemKeyPair.getPrivateKeyInfo();
		} else if (pemObject instanceof PrivateKeyInfo) {
			privateKeyInfo = (PrivateKeyInfo) pemObject;
		}
		return privateKeyInfo;
	}

	private static List<Certificate> loadCertificates(Reader reader) throws IOException, CertificateException {
		try (PEMParser pemParser = new PEMParser(reader)) {
			List<Certificate> certificates = new ArrayList<>();
			JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter()
					.setProvider(BouncyCastleProvider.PROVIDER_NAME);
			Object certObj = pemParser.readObject();
			if (certObj instanceof X509CertificateHolder) {
				X509CertificateHolder certificateHolder = (X509CertificateHolder) certObj;
				certificates.add(certificateConverter.getCertificate(certificateHolder));
			}
			return certificates;
		}
	}

}
