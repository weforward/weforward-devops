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

import java.util.List;

import cn.weforward.data.UniteId;

/**
 * 任务
 * 
 * @author daibo
 *
 */
public interface OpTask {
	/** 行为-升级 */
	int ACTION_UPGRADE = 0;
	/** 行为-启动 */
	int ACTION_START = 1;
	/** 行为-停止 */
	int ACTION_STOP = 2;
	/** 行为-重启 */
	int ACTION_RESTART = 3;
	/** 行为-回滚 */
	int ACTION_ROLLBACK = 4;
	/** 行为-构建 */
	int ACTION_BUILD = 5;
	/** 行为-打印堆栈 */
	int ACTION_PRINT_STACK = 6;
	/** 行为-打印内存 */
	int ACTION_PRINT_MEMORYMAP = 7;

	/**
	 * 状态
	 * 
	 * @return
	 */
	UniteId getId();

	/**
	 * 当前状态
	 * 
	 * @return
	 */
	OpProcessor.Status getCurrentStatus();

	/**
	 * 状态集合
	 * 
	 * @return
	 */
	List<OpProcessor.Status> getStatus();

	/**
	 * 是否已结束
	 * 
	 * @return
	 */
	boolean isDone();
}
