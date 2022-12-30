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

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.devops.user.AccessKeeper;
import cn.weforward.dist.DistService;
import cn.weforward.dist.impl.DistServiceImpl;
import cn.weforward.protocol.aio.http.RestfulServer;
import cn.weforward.protocol.aio.netty.NettyHttpServer;
import cn.weforward.util.FileClear;
import cn.weforward.util.HttpAccessAuth;
import cn.weforward.util.HttpDevopsKeyAuth;
import cn.weforward.util.HttpUserAuth;
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
	/** 端口 */
	@Value("${dist.port}")
	protected int m_Port;
	/** 最大请求包,单位m */
	@Value("${dist.maxhttpsize:600}")
	protected int m_MaxHttpSize;
	/** 最大历史 */
	@Value("${dist.maxHistory}")
	protected int m_MaxHistory;
	@Value("${dist.toolpath:}")
	protected String m_ToolPath;
	/** 用户验证器 */
	@Resource
	protected UserAuth m_UserAuth;
	/** 凭证验证器 */
	@Resource
	protected AccessKeeper m_AccessKeeper;

	@Autowired(required = false)
	protected HttpDevopsKeyAuth m_DevopsKeyAuth;

	@Value("${dist.openupload:false}")
	protected boolean m_OpenUpload;
	@Value("${dist.opendownload:false}")
	protected boolean m_OpenDownload;
	/** 只允许snapshot版本覆盖的项目，如：default/java/;default/html/ */
	@Value("${dist.m_versionverify:}")
	protected String m_VersionVerify;

	@Bean
	DistService distService() {
		DistServiceImpl s = new DistServiceImpl(m_DistPath);
		s.setUserAuth(new HttpUserAuth(m_UserAuth));
		s.setAccessAuth(new HttpAccessAuth(m_AccessKeeper));
		s.setDevopsKeyAuth(m_DevopsKeyAuth);
		s.setToolPath(m_ToolPath);
		s.setOpenUpload(m_OpenUpload);
		s.setOpenDownload(m_OpenDownload);
		if (!StringUtil.isEmpty(m_VersionVerify)) {
			s.setVersionVerify(Arrays.asList(m_VersionVerify.split(";")));
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
		if (m_MaxHistory<=0 ) {
			return null;
		}
		return new FileClear(m_DistPath, "dist-clear", m_MaxHistory);
	}

}
