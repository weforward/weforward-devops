package cn.weforward.devops.weforward.view;

import java.util.Date;
import java.util.List;

import cn.weforward.devops.alarm.Alarm;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "报警视图")
public class AlarmView {

	protected Alarm m_Alarm;

	protected AlarmView(Alarm a) {
		m_Alarm = a;
	}

	@DocAttribute(description = "唯一id")
	public String getId() {
		return m_Alarm.getId().getOrdinal();
	}

	@DocAttribute(description = "时间")
	public Date getTime() {
		return m_Alarm.getTime();
	}

	@DocAttribute(description = "内容")
	public String getContent() {
		return m_Alarm.getContent();
	}

	@DocAttribute(description = "详情链接")
	public String getDetailUrl() {
		return m_Alarm.getDetailUrl();
	}

	@DocAttribute(description = "标签")
	public List<String> getLabels() {
		return m_Alarm.getLabels();
	}

	public static AlarmView valueOf(Alarm a) {
		return null == a ? null : new AlarmView(a);
	}

}
