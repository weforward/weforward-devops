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
package cn.weforward.util.nexus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.common.util.TaskExecutor.Task;

/**
 * 定期清理nexus的repository
 * 
 * @author daibo
 *
 */
public class NexusRepositoryClear implements Runnable {
	/** 日志 */
	private static final Logger _Logger = LoggerFactory.getLogger(NexusRepositoryClear.class);
	/** 链接 */
	protected String m_Url;
	/** 用户名 */
	protected String m_UserName;
	/** 密码 */
	protected String m_Password;
	/** 库 */
	protected String m_Repository;
	/** 数量 */
	protected int m_KeepNum;
	/** 任务 */
	protected Task m_Task;
	/** 客户端 */
	protected NexusClient m_Client;

	public NexusRepositoryClear(String url, String userName, String password, String repository) {
		m_Url = url;
		m_UserName = userName;
		m_Password = password;
		m_Repository = repository;
	}

	public void setTaskExecutor(TaskExecutor exe) {
		if (null != m_Task) {
			m_Task.cancel();
		}
		m_Task = exe.execute(this, TaskExecutor.OPTION_NONE, 5 * 60 * 1000,
				cn.weforward.common.util.TimeUtil.DAY_MILLS);
	}

	public void setKeepNum(int num) {
		m_KeepNum = num;
	}

	public String getUrl() {
		return m_Url;
	}

	public String getUserName() {
		return m_UserName;
	}

	public String getPassword() {
		return m_Password;
	}

	public NexusClient getClient() throws IOException {
		if (null == m_Client) {
			m_Client = new NexusClient();
			m_Client.setUrl(getUrl());
			m_Client.setUserName(getUserName());
			m_Client.setPassword(getPassword());
		}
		return m_Client;
	}

	@Override
	public void run() {
		try {
			_Logger.info(
					"check nexus repository, url=" + m_Url + ",repository=" + m_Repository + ",keepnum=" + m_KeepNum);
			int keepnum = m_KeepNum;
			NexusClient client = getClient();
			Map<String, List<ComponentsItem>> map = new HashMap<String, List<ComponentsItem>>();
			String continuationToken = null;
			do {
				Components c = client.components(m_Repository, continuationToken);
				for (ComponentsItem item : c.getItems()) {
					List<ComponentsItem> list = map.get(item.getName());
					if (null == list) {
						list = new ArrayList<>();
						map.put(item.getName(), list);
					}
					list.add(item);
				}
				continuationToken = c.getContinuationToken();
			} while (!StringUtil.isEmpty(continuationToken));
			for (Entry<String, List<ComponentsItem>> e : map.entrySet()) {
				List<ComponentsItem> items = e.getValue();
				if (items.size() <= keepnum) {
					continue;
				}
				Collections.sort(items);
				for (int i = 0; i < items.size() - keepnum; i++) {
					ComponentsItem item = items.get(i);
					_Logger.info("删除 " + item.getName() + "," + item.getVersion());
					client.deleteComponent(item.getId());
				}
			}
		} catch (Exception e) {
			_Logger.error("清理Nexus库出错", e);
		}
	}

}
