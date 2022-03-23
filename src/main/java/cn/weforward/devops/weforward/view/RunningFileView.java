package cn.weforward.devops.weforward.view;

import java.util.Date;

import cn.weforward.common.util.Bytes;
import cn.weforward.devops.project.RunningFile;
import cn.weforward.protocol.doc.annotation.DocAttribute;

public class RunningFileView {

	protected RunningFile m_File;

	protected RunningFileView(RunningFile file) {
		m_File = file;
	}

	@DocAttribute(description = "文件名")
	public String getName() {
		return m_File.getName();
	}

	@DocAttribute(description = "文件大小")
	public String getLength() {
		return Bytes.formatHumanReadable(m_File.length());
	}

	@DocAttribute(description = "创建时间")
	public Date getCreateTime() {
		return m_File.getCreateTime();
	}

	public static RunningFileView valueOf(RunningFile e) {
		return null == e ? null : new RunningFileView(e);
	}
}
