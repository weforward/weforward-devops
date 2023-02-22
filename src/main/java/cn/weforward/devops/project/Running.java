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
package cn.weforward.devops.project;

import java.io.OutputStream;
import java.util.List;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.Persistent;

/**
 * 运行的项目
 * 
 * @author daibo
 *
 */
public interface Running extends Persistent {
	/** 状态-运行中 */
	NameItem STATE_RUNNING = NameItem.valueOf("运行中", 0);
	/** 状态-启动中 */
	NameItem STATE_STARTING = NameItem.valueOf("启动中", 1);
	/** 状态-停止中 */
	NameItem STATE_STOPING = NameItem.valueOf("停止中", 2);
	/** 状态-已停止 */
	NameItem STATE_STOPED = NameItem.valueOf("已停止", 3);
	/** 状态-未知 */
	NameItem STATE_UNKNOWN = NameItem.valueOf("--", -1);
	/** 状态-全部 */
	NameItems STATES = NameItems.valueOf(STATE_RUNNING, STATE_STARTING, STATE_STOPING, STATE_STOPED, STATE_UNKNOWN);
	/** 权限-编辑 */
	int RIGHT_EDIT = 1 << 0;
	/** 权限-升级 */
	int RIGHT_UPGRADE = 1 << 1;
	/** 权限-回滚 */
	int RIGHT_ROLLBACK = 1 << 2;
	/** 权限-启动 */
	int RIGHT_START = 1 << 3;
	/** 权限-停止 */
	int RIGHT_STOP = 1 << 4;
	/** 权限-重启 */
	int RIGHT_RESTART = 1 << 5;
	/** 权限-可见 */
	int RIGHT_SHOW = 1 << 6;

	/**
	 * 唯一表示持久化对象的标识
	 * 
	 * @see UniteId
	 * @return 联合标识
	 */
	UniteId getId();

	/**
	 * 设置项目
	 * 
	 * @param project
	 */
	void setProject(Project project);

	/**
	 * 项目
	 * 
	 * @return
	 */
	Project getProject();

	/**
	 * 设置机器
	 * 
	 * @param machine
	 */
	void setMachine(Machine machine);

	/**
	 * 机器
	 * 
	 * @return
	 */
	Machine getMachine();

	/**
	 * 项目配置
	 * 
	 * @return
	 */
	List<RunningProp> getProps();

	/**
	 * 设置项目配置
	 * 
	 * @param props
	 */
	void setProps(List<RunningProp> props);

	/**
	 * 升级
	 * 
	 * @param version 版本
	 * @param note    备注说明
	 * @return
	 */
	OpTask upgrade(String version, String note);

	/**
	 * 回滚版本
	 * 
	 * @return
	 */
	OpTask rollback(String note);

	/**
	 * 启动
	 * 
	 * @return
	 */
	OpTask start(String note);

	/**
	 * 关闭
	 * 
	 * @return
	 */
	OpTask stop(String note);

	/**
	 * 重启
	 * 
	 * @return
	 */
	OpTask restart(String note);

	/**
	 * 状态
	 * 
	 * @return
	 */
	NameItem getState();

	/**
	 * 获取当前版本
	 */
	VersionInfo getCurrentVersion();

	/**
	 * 获取版本
	 * 
	 * @return
	 */
	List<VersionInfo> getUpgradeVersions();

	/**
	 * 操作日志
	 * 
	 * @return
	 */
	ResultPage<BusinessLog> getOpLogs();

	/**
	 * 系统日志
	 * 
	 * @return
	 */
	ResultPage<String> getSysLogs();

	/**
	 * 是否有指定权限
	 * 
	 * @param r
	 * @return
	 */
	boolean isRight(int r);

	/**
	 * 是否支持打印堆栈
	 * 
	 * @return
	 */
	boolean isSupportPrintStack();

	/**
	 * 打印堆栈
	 * 
	 * @param filename 文件名 为null则自动生成
	 * @return 文件名
	 */
	OpTask printStack(String filename);

	/**
	 * 列表堆栈文件
	 * 
	 * @return
	 */
	List<RunningFile> listStacks();

	/**
	 * 获取堆栈文件
	 * 
	 * @return
	 */
	RunningFile getStack(String filename);

	/**
	 * 下载堆栈文件
	 * 
	 * @param filename
	 * @param out
	 */
	void downStack(String filename, OutputStream out);

	/**
	 * 是否支持打印内存
	 * 
	 * @return
	 */
	boolean isSupportMemoryMap();

	/**
	 * 打印内存
	 * 
	 * @param filename 文件名 为null则自动生成
	 * @return 文件名
	 */
	OpTask printMemoryMap(String filename);

	/**
	 * 列表内存文件
	 * 
	 * @return
	 */
	List<RunningFile> listMemoryMaps();

	/**
	 * 获取内存文件
	 * 
	 * @return
	 */
	RunningFile getMemoryMap(String filename);

	/**
	 * 下载内存文件
	 * 
	 * @param filename
	 * @param out
	 */
	void downMemoryMap(String filename, OutputStream out);
	/**
	 * 删除实例
	 */
	void delete();

}
