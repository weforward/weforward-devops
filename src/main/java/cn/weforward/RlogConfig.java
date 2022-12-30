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

import cn.weforward.common.util.ThreadPool;
import cn.weforward.data.array.LabelSetFactory;
import cn.weforward.devops.user.AccessKeeper;
import cn.weforward.protocol.aio.http.RestfulServer;
import cn.weforward.protocol.aio.netty.NettyHttpServer;
import cn.weforward.rlog.impl.RemoteLogServiceImpl;
import cn.weforward.util.FileClear;
import cn.weforward.util.FileClearOnTime;
import cn.weforward.util.HttpAccessAuth;

/**
 * 远程日志配置
 * 
 * @author daibo
 *
 */
public class RlogConfig {
	/** 存储目录 */
	@Value("${rlog.logpath}")
	protected String m_LogPath;
	/** 端口 */
	@Value("${rlog.port}")
	protected int m_Port;
	/** 最大请求包,单位m */
	@Value("${dist.maxhttpsize:600}")
	protected int m_MaxHttpSize;
	/** 最大历史 */
	@Value("${rlog.maxHistory}")
	protected int m_MaxHistory;
	/** 凭证管理者 */
	@Resource
	protected AccessKeeper m_AccessKeeper;

	@Bean
	RemoteLogServiceImpl rlogService(LabelSetFactory factory) {
		RemoteLogServiceImpl s = new RemoteLogServiceImpl(factory, m_LogPath, new HttpAccessAuth(m_AccessKeeper));
		return s;
	}

	@Bean
	NettyHttpServer rlogApiServer(RemoteLogServiceImpl rlogService, ThreadPool threadPool) throws Exception {
		if (m_Port <= 0) {
			return null;
		}
		NettyHttpServer s = new NettyHttpServer(m_Port);
		s.setName("rlog");
		// s.setGzipEnabled(true);
		// s.setElapseTime(3000);
		// XXXm的包
		s.setMaxHttpSize(m_MaxHttpSize * 1024 * 1024);
		s.setIdle(10);
		RestfulServer server = new RestfulServer(rlogService);
		ThreadPool mypool = new ThreadPool(threadPool);
		mypool.setName("rlog");
		server.setExecutor(mypool);
		s.setHandlerFactory(server);
		s.start();
		return s;
	}

	@Bean
	FileClear rlogClear() {
		if (m_MaxHistory <= 0) {
			return null;
		}
		return new FileClearOnTime(m_LogPath, "rlog-clear", m_MaxHistory);
	}
}
