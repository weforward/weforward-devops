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
package cn.weforward;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import cn.weforward.boot.CloudPropertyPlaceholderConfigurer;
import cn.weforward.common.ResultPage;
import cn.weforward.common.crypto.Hex;
import cn.weforward.common.util.BackgroundExecutor;
import cn.weforward.common.util.ListUtil;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.data.persister.PersisterSet;
import cn.weforward.data.persister.support.SimplePersisterSet;
import cn.weforward.data.util.DelayFlusher;
import cn.weforward.data.util.Flusher;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.ServiceName;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.http.HttpKeeper;
import cn.weforward.protocol.gateway.vo.RightTableItemVo;
import cn.weforward.protocol.gateway.vo.RightTableItemWrap;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.protocol.ops.secure.RightTable;

/**
 * spring配置
 * 
 * @author daibo
 *
 */
@Configuration
@Import({ EndpointConfig.class, DevopsConfig.class, RlogConfig.class, DistConfig.class, MetricsConfig.class,
		SiteConfig.class })
public class SpringConfig {
	/** 日志记录器 */
	private static final Logger _Logger = LoggerFactory.getLogger(SpringConfig.class);

	private static String WEFORWARD_SERVICE_ACCESSID_KEY = "weforward.service.accessId";
	private static String WEFORWARD_SERVICE_ACCESSKEY_KEY = "weforward.service.accessKey";

	/** 用户目录 */
	private static String USER_DIR = System.getProperty("user.dir");

	static Resource genProperty(String name) {
		return new FileSystemResource(USER_DIR + "/conf/" + name + ".properties");
	}

	/** 配置 */
	@Bean
	static CloudPropertyPlaceholderConfigurer configurer() {
		CloudPropertyPlaceholderConfigurer c = new CloudPropertyPlaceholderConfigurer() {
			@Override
			protected Properties mergeProperties() throws IOException {
				Properties prop = super.mergeProperties();
				String accessKeySecret = System.getProperty("accessKey.secret");
				if (!StringUtil.isEmpty(accessKeySecret)) {
					setDefaultIfNeed(prop, "weforward.user.password", accessKeySecret);
					if (accessKeySecret.length() != 64) {
						try {
							accessKeySecret = Hex.encode(Access.Helper.secretToAccessKey(accessKeySecret));
						} catch (NoSuchAlgorithmException e) {
							throw new RuntimeException("算法异常", e);
						}
					}
					setDefaultIfNeed(prop, "weforward.user.secretKey", accessKeySecret);
				}
				String host = System.getProperty("weforward.host");
				if (StringUtil.isEmpty(host)) {
					host = getServiceIp(null);
					_Logger.info("自动获取的HostIP:" + host);
				}
				host = setDefaultIfNeed(prop, "weforward.host", host);
				_Logger.info("最终的HostIP:" + host);
				String defaultGateWayUrl = "http://" + host + ":5661/";
				String apiUrl = setDefaultIfNeed(prop, "weforward.apiUrl", defaultGateWayUrl);
				setDefaultIfNeed(prop, "weforward.gatewayUrl", apiUrl);
				String internalAccessSecret = System.getProperty("internalAccess.secret");
				if (!StringUtil.isEmpty(internalAccessSecret)) {
					String accessId = prop.getProperty(WEFORWARD_SERVICE_ACCESSID_KEY);
					String accessKey = prop.getProperty(WEFORWARD_SERVICE_ACCESSKEY_KEY);
					if (StringUtil.isEmpty(accessId) || StringUtil.isEmpty(accessKey)) {
						HttpKeeper keeper;
						try {
							keeper = new HttpKeeper(apiUrl, internalAccessSecret);
						} catch (NoSuchAlgorithmException e) {
							throw new RuntimeException("算法异常", e);
						}
						AccessExt access = init(keeper);
						if (StringUtil.isEmpty(accessId)) {
							prop.put(WEFORWARD_SERVICE_ACCESSID_KEY, access.getAccessId());
						}
						if (StringUtil.isEmpty(accessKey)) {
							prop.put(WEFORWARD_SERVICE_ACCESSKEY_KEY, access.getAccessKeyBase64());
						}

					}
				}
				return prop;
			}

			private String setDefaultIfNeed(Properties prop, String key, String value) {
				String old = prop.getProperty(key);
				if (!StringUtil.isEmpty(old)) {
					return old;
				}
				old = System.getProperty(key);
				if (!StringUtil.isEmpty(old)) {
					return old;
				}
				prop.put(key, value);
				return value;
			}
		};
		c.setDisableCloud(true);
		Resource l1 = genProperty("devops");
		if (l1.exists()) {
			c.setLocations(l1);
		}

		return c;
	}

	public static String getServiceIp(String prefix) {
		InetAddress best = null;
		Enumeration<NetworkInterface> ifs;
		try {
			ifs = NetworkInterface.getNetworkInterfaces();
			loop_ifs: while (ifs.hasMoreElements()) {
				NetworkInterface ni = ifs.nextElement();
				if (StringUtil.toString(ni.getName()).startsWith("docker")) {
					continue;// 忽略docker的网段
				}
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					InetAddress ip = ias.nextElement();
					// 找到一个非loopback地址
					if (!ip.isLoopbackAddress() && !ip.isLinkLocalAddress()
							&& StringUtil.toString(ip.getHostAddress()).indexOf(":") == -1) {
						if (null != prefix && ip.getHostAddress().startsWith(prefix)) {
							best = ip;
							break loop_ifs;
						}
						byte[] adds = ip.getAddress();
						// 只找IPV4
						if (null == adds || adds.length < 4) {
							continue;
						}
						byte[] bestAddrs = (null == best) ? null : best.getAddress();
						if (-64 == adds[0] && -88 == adds[1]) {
							// 192.168.x.x
							if (null == bestAddrs) {
								best = ip;
							}
						} else if (-84 == adds[0] && adds[1] >= 16 && adds[1] <= 31) {
							// 172.16~31.x.x段，优先于192.168.x.x
							if (null == bestAddrs || (-64 == bestAddrs[0] && -88 == bestAddrs[1])) {
								best = ip;
							}
						} else if (10 == adds[0]) {
							// 10.x.x.x段，优先于172.x.x.x
							if (null == bestAddrs || (-64 == bestAddrs[0] && -88 == bestAddrs[1])
									|| -84 == adds[0] && adds[1] >= 16 && adds[1] <= 31) {
								best = ip;
							}
						} else {
							// 外网IP
							best = ip;
							break loop_ifs;
						}
					}
				}
			}
		} catch (SocketException e) {
			throw new IllegalStateException("获取IP异常", e);
		}
		return (null == best) ? null : best.getHostAddress();
	}

	static AccessExt init(Keeper keeper) {
		AccessExt access = initDevopsServiceAccess(keeper);
		initKeeperApiRightTable(keeper, access);
		initDevopsServiceRightTable(keeper);
		initServiceRegisterApiRightTable(keeper);
		return access;
	}

	static AccessExt initDevopsServiceAccess(Keeper keeper) {
		String serviceName = "devops";
		AccessExt serviceAccess = null;
		ResultPage<AccessExt> accesses = keeper.listAccess(Access.KIND_SERVICE, null, serviceName);
		for (AccessExt acc : ResultPageHelper.toForeach(accesses)) {
			if (acc.getSummary().equals(serviceName)) {
				serviceAccess = acc;
				break;
			}
		}
		if (null == serviceAccess) {
			serviceAccess = keeper.createAccess(Access.KIND_SERVICE, AccessExt.DEFAULT_GROUP, serviceName);
			_Logger.info("创建微服务'" + serviceName + "'的访问凭证");
		} else {
			_Logger.info("微服务'" + serviceName + "'的访问凭证已存在");
		}
		return serviceAccess;
	}

	static void initDevopsServiceRightTable(Keeper keeper) {
		String serviceName = "devops";
		RightTable table = keeper.getRightTable(serviceName);
		if (null == table || ListUtil.isEmpty(table.getItems())) {
			{
				RightTableItemVo ri = new RightTableItemVo();
				ri.allow = true;
				ri.name = "user";
				ri.accessKind = Access.KIND_USER;
				table = keeper.appendRightRule(serviceName, new RightTableItemWrap(ri));
			}
			{
				RightTableItemVo ri = new RightTableItemVo();
				ri.allow = true;
				ri.name = "service";
				ri.accessKind = Access.KIND_SERVICE;
				table = keeper.appendRightRule(serviceName, new RightTableItemWrap(ri));
			}
			{
				RightTableItemVo ri = new RightTableItemVo();
				ri.allow = true;
				ri.name = "guest";
				ri.accessKind = null;
				table = keeper.appendRightRule(serviceName, new RightTableItemWrap(ri));
			}
			_Logger.info("创建微服务'" + serviceName + "'权限表");
		} else {
			_Logger.info("微服务'" + serviceName + "'权限表已存在");
		}
	}

	static void initKeeperApiRightTable(Keeper keeper, AccessExt devopsAccess) {
		String apiName = ServiceName.KEEPER.name;
		RightTable table = keeper.getRightTable(apiName);
		if (null != table && table.getItems().size() >= 2) {
			_Logger.info("Api'" + apiName + "'权限表已存在");
		} else {
			RightTableItemVo vo = new RightTableItemVo();
			vo.accessId = devopsAccess.getAccessId();
			vo.allow = true;
			vo.name = "init";
			vo.description = "devops";
			table = keeper.appendRightRule(apiName, new RightTableItemWrap(vo));
			_Logger.info("追加微服务'devops'访问Api'" + apiName + "'的权限");
		}
	}

	static void initServiceRegisterApiRightTable(Keeper keeper) {
		String apiName = ServiceName.SERVICE_REGISTER.name;
		RightTable table = keeper.getRightTable(apiName);
		if (null == table || ListUtil.isEmpty(table.getItems())) {
			RightTableItemVo vo = new RightTableItemVo();
			vo.accessKind = Access.KIND_SERVICE;
			vo.allow = true;
			vo.name = "init";
			table = keeper.appendRightRule(apiName, new RightTableItemWrap(vo));
			_Logger.info("创建Api'" + apiName + "'权限表");
		} else {
			_Logger.info("Api'" + apiName + "'权限表已存在");
		}
	}

	/** 线程池 */
	@Bean
	ThreadPool threadPool() {
		return new ThreadPool(1000, "common");
	}

	/*** 任务执行器（线程池） */
	@Bean
	TaskExecutor taskExecutor() {
		return new BackgroundExecutor(2, 10, 1);
	}

	/*** 持久器集，用于互通多个DataHub中的对象，否则之间对象的引用在反射时将找不回 */
	@Bean
	PersisterSet persisters() {
		return new SimplePersisterSet();
	}

	/*** 延时提交刷写器 用于在每次请求后刷写 */
	@Bean
	Flusher flusher() {
		DelayFlusher f = new DelayFlusher(10);
		f.setName("comm");
		f.setMaxSuspend(1000);
		return f;
	}

	@Bean
	Properties globalProperties() throws IOException {
		Properties prop = new Properties();
		Resource res = genProperty("global");
		if (res.exists()) {
			prop.load(res.getInputStream());
		}
		return prop;
	}
}
