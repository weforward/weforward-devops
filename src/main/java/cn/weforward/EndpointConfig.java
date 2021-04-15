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

import java.net.MalformedURLException;
import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import cn.weforward.common.util.StringUtil;
import cn.weforward.framework.ext.CurrentUserMethod;
import cn.weforward.framework.ext.DebugMethod;
import cn.weforward.framework.ext.DocumentMethod;
import cn.weforward.framework.ext.MethodsAware;
import cn.weforward.framework.ext.UserAuthorizer;
import cn.weforward.framework.ext.VersionMethod;
import cn.weforward.framework.ext.WeforwardService;
import cn.weforward.metrics.RemoteMeterRegistry;
import cn.weforward.protocol.AccessLoader;
import cn.weforward.protocol.ops.UserService;
import cn.weforward.protocol.support.CommonServiceCodes;
import cn.weforward.trace.RemoteTraceRegistry;
import cn.weforward.trace.TraceRegistry;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * 微服务端点配置
 * 
 * @author daibo
 *
 */
@ComponentScan("cn.weforward.devops.weforward")
public class EndpointConfig {
	/** 服务名 */
	@Value("${weforward.name}")
	protected String m_DevopsName;
	/** 服务主机 */
	@Value("${weforward.host:127.0.0.1}")
	protected String m_DevopsHost;
	/** 服务端口 */
	@Value("${weforward.port:15000}")
	protected int m_DevopsPort;
	/** 服务网关地址 */
	@Value("${weforward.apiUrl:}")
	protected String m_ApiUrl;
	/** 服务访问id */
	@Value("${weforward.service.accessId:}")
	protected String m_ServiceAccessId;
	/** 服务访问key */
	@Value("${weforward.service.accessKey:}")
	protected String m_ServiceAccessKey;
	/** 服务器id */
	@Value("${weforward.serverid}")
	protected String m_ServerId;
	/** 是否开启debug模式 */
	@Value("${weforward.debugEnabled:false}")
	protected boolean m_DebugEnabled;
	/** 指标链接 */
	@Value("${metrics.url}")
	protected String m_MetricsUrl;
	/** 追踪链接 */
	@Value("${trace.url}")
	protected String m_TraceUrl;

	/** 用户服务 */
	@Resource
	protected UserService m_UserService;

	@Autowired(required = false)
	protected AccessLoader m_AccessLoader;

	/** Weforward服务 */
	@Bean
	WeforwardService service(MeterRegistry meterRegistry, TraceRegistry traceRegistry) throws Exception {
		String d = "/devops/";
		WeforwardService s = new WeforwardService(m_DevopsName, m_DevopsHost, m_DevopsPort, d);
		s.setDebugEnabled(m_DebugEnabled);
		s.setServicesUrl(m_ApiUrl);
		s.setAccessId(m_ServiceAccessId);
		s.setAccessKey(m_ServiceAccessKey);
		s.setNo(m_ServerId);
		s.setDescription("用于管理微服务的项目");
		s.setStatusCodeClassName(CommonServiceCodes.class.getName());
		s.setMeterRegistry(meterRegistry);
		s.setTraceRegister(traceRegistry);
		UserAuthorizer a = new UserAuthorizer();
		a.setUserService(m_UserService);
		a.setIgnoreUris(Arrays.asList(d + "_cuser", d + "_version", d + "auth/*"));
		s.setUserAuthorizer(a);
		s.setAccessLoader(m_AccessLoader);
		return s;
	}

	@Bean
	MeterRegistry meterRegister() throws MalformedURLException {
		String metricsUrl = m_MetricsUrl;
		if (StringUtil.isEmpty(metricsUrl)) {
			return null;
		}
		RemoteMeterRegistry register = new RemoteMeterRegistry(metricsUrl);
		register.setServiceId(m_ServerId);
		register.setServiceNo(m_ServerId);
		register.setServiceName(m_DevopsName);
		return register;
	}

	@Bean
	TraceRegistry traceRegistry() throws MalformedURLException {
		String traceUrl = m_TraceUrl;
		if (StringUtil.isEmpty(traceUrl)) {
			return null;
		}
		RemoteTraceRegistry register = new RemoteTraceRegistry(traceUrl);
		register.setServiceId(m_ServerId);
		register.setServiceNo(m_ServerId);
		register.setServiceName(m_DevopsName);
		return register;
	}

	/** 管理员脚本方法 */
	@Bean
	DebugMethod debug(WeforwardService service) {
		return new DebugMethod(service);
	}

	/** 版本方法 */
	@Bean
	cn.weforward.framework.ext.VersionMethod version(WeforwardService service) {
		return new VersionMethod(service, Arrays.asList(this.getClass().getName()));
	}

	/** 查看当前用户 */
	@Bean
	CurrentUserMethod cuser(WeforwardService service) {
		return new CurrentUserMethod(service);
	}

	/** 文档方法 */
	@Bean
	DocumentMethod doc(WeforwardService service) {
		return new DocumentMethod(service);
	}

	/** 控制器发现 */
	@Bean
	MethodsAware methodsAware(WeforwardService service) {
		return new MethodsAware(service);
	}

}
