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
package cn.weforward.devops.weforward;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.alarm.AlarmService;
import cn.weforward.devops.weforward.util.LimitHits;
import cn.weforward.devops.weforward.view.GatewayVo;
import cn.weforward.devops.weforward.view.ServiceVo;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.doc.annotation.DocMethod;

/**
 * 报警方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_GATEWAY)
public class AlarmerMethods {

	private static final Logger _Logger = LoggerFactory.getLogger(AlarmerMethods.class);
	@Resource
	protected AlarmService m_AlarmService;
	@Value("${alarmer.handler.url}")
	protected String m_HandlerUrl;
	/** 报警调用脚本 */
	@Value("${alarmer.cmd:}")
	protected String m_Cmd;
	protected ProcessBuilder m_ProcessBuilder;

	protected LimitHits m_Hits = new LimitHits(30 * 60 * 60);

	@DocMethod(description = "服务宕机/超时通知")
	@WeforwardMethod
	public void onServiceTimeout(ServiceVo params) {
		push(params.getName() + "(" + params.getNo() + ")服务宕机，在网关（" + params.getGateway() + "）上",
				params.getRunningId());
	}

	@DocMethod(description = "服务不可用通知")
	@WeforwardMethod
	public void onServiceUnavailable(ServiceVo params) {
		push(params.getName() + "(" + params.getNo() + ")服务不可用，在网关（" + params.getGateway() + "）上",
				params.getRunningId());
	}

	@DocMethod(description = "服务过载通知")
	@WeforwardMethod
	public void onServiceOverload(ServiceVo params) {
		push(params.getName() + "(" + params.getNo() + ")服务过载，在网关（" + params.getGateway() + "）上",
				params.getRunningId());
	}

	@DocMethod(description = "网关宕机/超时通知")
	@WeforwardMethod
	public void onGatewayTimeout(GatewayVo params) {
		push(params.getHost() + "(" + params.getId() + ")网关宕机，在网关（" + params.getGateway() + "）上", null);
	}

	private void push(String sms, String id) {
		if (!m_Hits.hitIfLess(sms, 1)) {
			return;
		}
		try {
			String url;
			if (!StringUtil.isEmpty(m_HandlerUrl) && !StringUtil.isEmpty(id)) {
				url = m_HandlerUrl + id;
			} else {
				url = null;
			}
			m_AlarmService.create(new Date(), sms, url);
		} catch (Throwable e) {
			_Logger.error("创建报警异常", e);
		}
		runCmd(sms, id);
	}

	public void runCmd(String msg, String id) {
		if (null == m_Cmd || m_Cmd.length() == 0) {
			return;
		}
		// 执行外部脚本
		try {
			ProcessBuilder pb = m_ProcessBuilder;
			if (null == pb) {
				pb = new ProcessBuilder();
				pb.command(m_Cmd);
				pb.redirectErrorStream(true);
				pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
				m_ProcessBuilder = pb;
			}
			Map<String, String> ev = pb.environment();
			if (!StringUtil.isEmpty(id)) {
				ev.put("alarmer_running", id);
			}
			if (!StringUtil.isEmpty(msg)) {
				ev.put("alarmer_msg", msg);
			}
			Process process;
			process = pb.start();
			if (process.waitFor(10, TimeUnit.SECONDS) && !process.isAlive()) {
				_Logger.info("已运行外部命令，退出值：" + process.exitValue() + "，命令行：" + m_Cmd);
			} else {
				_Logger.info("外部命令执行超过10秒，命令行：" + m_Cmd);
				process.destroy();
			}
			return;
		} catch (Exception e) {
			m_ProcessBuilder = null;
			_Logger.error(m_Cmd, e);
		}
	}
}
