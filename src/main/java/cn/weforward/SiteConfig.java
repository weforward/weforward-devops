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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.protocol.aio.http.RestfulServer;
import cn.weforward.protocol.aio.netty.NettyHttpServer;
import cn.weforward.site.impl.VueSiteServiceImpl;

/**
 * 站点配置
 * 
 * @author daibo
 *
 */
public class SiteConfig {
	/** 服务地址 */
	@Value("${weforward.apiUrl}")
	protected String m_ApiUrl;
	/** 服务名 */
	@Value("${weforward.name}")
	protected String m_Name;
	/** 服务地址 */
	@Value("${weforward.site.apiUrl:}")
	protected String m_SiteApiUrl;
	/** 服务名 */
	@Value("${weforward.site.name:}")
	protected String m_SiteName;
	/** 网站文件路径 */
	@Value("${site.path:}")
	protected String m_SitePath;
	/** 端口 */
	@Value("${site.port}")
	protected int m_Port;

	@Bean
	VueSiteServiceImpl siteService() {
		VueSiteServiceImpl impl = new VueSiteServiceImpl(m_SitePath);
		impl.setApiUrl(StringUtil.isEmpty(m_SiteApiUrl) ? m_ApiUrl : m_SiteApiUrl);
		impl.setServiceName(StringUtil.isEmpty(m_SiteName) ? m_Name : m_SiteName);
		return impl;
	}

	@Bean
	NettyHttpServer siteApiServer(RestfulService siteService, ThreadPool threadPool) throws Exception {
		if (m_Port <= 0 || StringUtil.isEmpty(m_SitePath)) {
			return null;
		}
		NettyHttpServer s = new NettyHttpServer(m_Port);
		s.setName("site");
		s.setIdle(10);
		RestfulServer server = new RestfulServer(siteService);
		ThreadPool mypool = new ThreadPool(threadPool);
		mypool.setName("site");
		server.setExecutor(mypool);
		s.setHandlerFactory(server);
		s.start();
		return s;
	}

}
