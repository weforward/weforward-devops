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
package cn.weforward.devops.weforward.view;

import java.util.List;

import cn.weforward.common.util.Bytes;
import cn.weforward.common.util.TransList;
import cn.weforward.devops.project.MachineInfo;
import cn.weforward.devops.project.MachineInfo.Dist;
import cn.weforward.protocol.doc.annotation.DocAttribute;

/**
 * 机器信息
 * 
 * @author daibo
 *
 */
public class MachineInfoView {
	protected MachineInfo m_Info;

	public MachineInfoView(MachineInfo info) {
		m_Info = info;
	}

	private MachineInfo getInfo() {
		return m_Info;
	}

	/** CPU数 */
	public String getCpuNum() {
		return getInfo().getCpuNum() + "核";
	}

	/** 系统负载（0%～CPU数*100%） */
	@DocAttribute(description = "系统负载（0%～CPU数*100%）")
	public String getLoadAverage() {
		return getInfo().getLoadAverage() + "%";
	}

	/** 可用内存 */
	@DocAttribute(description = "可用内存")
	public String getMemoryUsable() {
		return Bytes.formatHumanReadable(getInfo().getMemoryUsable());
	}

	/** 总内存 */
	@DocAttribute(description = "总内存")
	public String getMemoryTotal() {
		return Bytes.formatHumanReadable(getInfo().getMemoryTotal());
	}

	@DocAttribute(description = "磁盘")
	public List<DistView> getDists() {
		return new TransList<DistView, Dist>(getInfo().getDists()) {

			@Override
			protected DistView trans(Dist src) {
				return new DistView(src);
			}
		};
	}

	public static class DistView {
		/** 名称 */
		protected Dist m_Dist;

		public DistView(Dist d) {
			m_Dist = d;
		}

		private Dist getDist() {
			return m_Dist;
		}

		@DocAttribute(description = "名称")
		public String getName() {
			return getDist().getName();
		}

		/** 可用磁盘空间 */
		@DocAttribute(description = "可用磁盘空间")
		public String getDistUsable() {
			return Bytes.formatHumanReadable(getDist().getDistUsable());
		}

		/** 总磁盘空间 */
		@DocAttribute(description = "总磁盘空间）")
		public String getDistTotal() {
			return Bytes.formatHumanReadable(getDist().getDistTotal());
		}

	}

}
