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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.dist.DistService;
import cn.weforward.dist.impl.DistServiceImpl;
import cn.weforward.protocol.aio.http.RestfulServer;
import cn.weforward.protocol.aio.netty.NettyHttpServer;
import cn.weforward.util.FileClear;
import cn.weforward.util.HttpAuth;
import cn.weforward.util.UserAuth;

/**
 * 发布配置
 * 
 * @author daibo
 *
 */
public class DistConfig {
	/** 存储路径 */
	@Value("${dist.distpath}")
	protected String m_DistPath;
	/** 允许访问的服务id */
	@Value("${dist.allowIps:}")
	protected String m_AllowIps;
	/** 可信的代理服务器id */
	@Value("${dist.proxyIps:}")
	protected String m_ProxyIps;
	/** 是否验证用户 */
	@Value("${dist.authuser}")
	protected boolean m_AuthUser;
	/** 端口 */
	@Value("${dist.port}")
	protected int m_Port;
	/** 最大请求包,单位m */
	@Value("${dist.maxhttpsize:600}")
	protected int m_MaxHttpSize;
	/** 最大历史 */
	@Value("${dist.maxHistory}")
	protected int m_MaxHistory;
	/** 用户验证器 */
	@Resource
	protected UserAuth m_UserAuth;

	@Bean
	DistService distService() {
		DistServiceImpl s = new DistServiceImpl(m_DistPath);
		if (m_AuthUser) {
			s.setAuth(new HttpAuth(m_UserAuth));
		}
		return s;
	}

	@Bean
	NettyHttpServer distApiServer(RestfulService distService, ThreadPool threadPool) throws Exception {
		if (m_Port <= 0) {
			return null;
		}
		NettyHttpServer s = new NettyHttpServer(m_Port);
		s.setName("dist");
		// s.setGzipEnabled(true);
		// s.setElapseTime(3000);
		// XXXm的包
		s.setMaxHttpSize(m_MaxHttpSize * 1024 * 1024);
		s.setIdle(1800);
		RestfulServer server = new RestfulServer(distService);
		server.setAllowIps(m_AllowIps);
		server.setProxyIps(m_ProxyIps);
		server.setQuickHandle(true);
		ThreadPool mypool = new ThreadPool(threadPool);
		mypool.setName("dist");
		server.setExecutor(mypool);
		s.setHandlerFactory(server);
		s.start();
		return s;
	}

	@Bean
	FileClear distClear() {
		if (0 <= m_MaxHistory) {
			return null;
		}
		return new FileClear(m_DistPath, "dist-clear", m_MaxHistory);
	}

}
