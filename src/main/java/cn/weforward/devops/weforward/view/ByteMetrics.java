package cn.weforward.devops.weforward.view;

import java.util.Date;

import cn.weforward.common.util.Bytes;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 字节指标
 * 
 * @author daibo
 *
 */
@DocObject(description = "字节指标")
public class ByteMetrics {

	protected Date m_Time;

	protected long m_Value;

	public ByteMetrics(Date time, long value) {
		m_Time = time;
		m_Value = value;
	}

	@DocAttribute(description = "时间")
	public Date getTime() {
		return m_Time;
	}

	@DocAttribute(description = "值，单位字节")
	public long getValue() {
		return m_Value;
	}

	@DocAttribute(description = "格式化显示的值")
	public String getFormatValue() {
		return Bytes.formatHumanReadable(m_Value);
	}

}
