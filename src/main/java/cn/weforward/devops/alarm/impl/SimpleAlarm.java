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
package cn.weforward.devops.alarm.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.util.TimeUtil;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.devops.alarm.Alarm;
import cn.weforward.devops.alarm.di.AlarmDi;

/**
 * 简单的报警实现
 * 
 * @author daibo
 *
 */
public class SimpleAlarm extends AbstractPersistent<AlarmDi> implements Alarm {

	@Resource
	protected Date m_Time;
	@Resource
	protected String m_Content;
	@Resource
	protected String m_DetailUrl;
	@ResourceExt(component = String.class)
	protected List<String> m_Labels;

	protected SimpleAlarm(AlarmDi di) {
		super(di);
	}

	public SimpleAlarm(AlarmDi di, Date time, String content, String detailUrl, List<String> lables) {
		super(di);
		genPersistenceId(TimeUtil.formatCompactDate(time));
		m_Time = time;
		m_Content = content;
		m_DetailUrl = detailUrl;
		m_Labels = lables;
		markPersistenceUpdate();
	}

	@Override
	public UniteId getId() {
		return getPersistenceId();
	}

	@Override
	public Date getTime() {
		return m_Time;
	}

	@Override
	public String getContent() {
		return m_Content;
	}

	@Override
	public String getDetailUrl() {
		return m_DetailUrl;
	}

	@Override
	public List<String> getLabels() {
		return m_Labels;
	}

}
