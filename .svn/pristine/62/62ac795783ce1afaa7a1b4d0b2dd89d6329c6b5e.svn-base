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
package cn.weforward.devops.alarm;

import java.util.Date;

import cn.weforward.common.ResultPage;

/**
 * 报警服务
 * 
 * @author daibo
 *
 */
public interface AlarmService {
	/**
	 * 创建报警
	 * 
	 * @param time      时间
	 * @param content   内容
	 * @param detailUrl 详情链接
	 * @param labels    标签
	 * @return 报警对象
	 */
	Alarm create(Date time, String content, String detailUrl, String... labels);

	/**
	 * 查询报警记录
	 * 
	 * @param start    开始
	 * @param end      结束
	 * @param keywords 关键字
	 * @return
	 */
	ResultPage<Alarm> searchAlarms(Date begin, Date end, String keywords);
}
