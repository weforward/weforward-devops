package cn.weforward.devops.weforward.view;

import java.util.Date;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 数字/次数指标
 * 
 * @author daibo
 *
 */
@DocObject(description = "数字/次数指标")
public class NumberMetrics {

	protected Date m_Time;

	protected long m_Value;

	public NumberMetrics(Date time, long value) {
		m_Time = time;
		m_Value = value;
	}

	@DocAttribute(description = "时间")
	public Date getTime() {
		return m_Time;
	}

	@DocAttribute(description = "数值")
	public long getValue() {
		return m_Value;
	}

}
