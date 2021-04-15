package cn.weforward.devops.project.impl;

import java.io.File;
import java.util.Date;

import cn.weforward.devops.project.RunningFile;

public class RunningFileWrap implements RunningFile {

	protected File m_File;

	public RunningFileWrap(File file) {
		m_File = file;
	}

	@Override
	public String getName() {
		return m_File.getName();
	}

	@Override
	public long length() {
		return m_File.length();
	}

	@Override
	public Date getCreateTime() {
		return new Date(m_File.lastModified());
	}
}
