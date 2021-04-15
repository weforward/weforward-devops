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
package cn.weforward.util.docker;

import org.json.JSONObject;

import cn.weforward.util.docker.DockerRun.Device;
import cn.weforward.util.docker.DockerRun.PathAndRate;
import cn.weforward.util.docker.DockerRun.PathAndWeight;
import cn.weforward.util.docker.DockerRun.RestartPolicyConfigurantion;
import cn.weforward.util.docker.DockerRun.Ulimit;
import cn.weforward.util.docker.ext.Util;

/**
 * docker更新
 * 
 * @author daibo
 *
 */
public class DockerUpdate {
	/** 表示此容器相对于其他容器的相对 CPU 权重的整数值 */
	private Integer CpuShares;
	/** 内存限制(以字节为单位)。 */
	private Long Memory;
	/**
	 * 创建容器的c组的路径。如果路径不是绝对路径,则该路径被视为相对于init进程的 cgroups 路径。如果C组不存在,则创建它们。
	 */
	private String CgroupParent;
	/** IO块权值0~1000 */
	private Integer BlkioWeight;
	/** IO块权值 */
	private PathAndWeight[] BlkioWeightDevice;
	/** 读取限制（每秒字节数） */
	private PathAndRate[] BlkioDeviceReadBps;
	/** 写入限制（每秒字节数） */
	private PathAndRate[] BlkioDeviceWriteBps;
	/** 读取限制（每秒IO数） */
	private PathAndRate[] BlkioDeviceReadIOps;
	/** 写入限制（每秒IO数） */
	private PathAndRate[] BlkioDeviceWriteIOps;
	/** CPU 周期的长度(以微秒为单位)。 */
	private Integer CpuPeriod;
	/** 容器在 CPU 周期内可以获取的 CPU 时间的微秒。 */
	private Integer CpuQuota;
	/** CPU 实时周期的长度(以微秒为单位)。设置为 0,以便不分配分配给实时任务的时间。 */
	private Integer CpuRealtimePeriod;
	/** CPU 实时运行时间的长度(以微秒为单位)。设置为 0,以便不分配分配给实时任务的时间 */
	private Integer CpuRealtimeRuntime;
	/** 允许执行的 CPU,如0-3,0,1 */
	private String CpusetCpus;
	/** 允许执行的内存节点 (MEM) (0-3, 0,1)。仅在NUMA系统上有效。 */
	private String CpusetMems;
	/** 容器中的所有设备 */
	private Device[] devices;
	/** 要应用于容器的组规则列表 */
	private String[] DeviceCgroupRules;
	/** 磁盘限制(以字节为单位)。 */
	private Integer DiskQuota;
	/** 内核内存限制(以字节为单位)。 */
	private Integer KernelMemory;
	/** 内存限制(以字节为单位)。 */
	private Integer MemoryReservation;
	/** 总内存限制(内存 + 交换)。设置为 -1 以启用无限交换。 */
	private Integer MemorySwap;
	/** 调整容器的内存交换行为。接受介于 0 和 100 之间的整数。 */
	private Integer MemorySwappiness;
	/** CPU 配额(单位为 10的负９次方 CPU)。. */
	private Integer NanoCPUs;
	/** 禁止在OOM(内容不足)时杀死容器 */
	private Boolean OomKillDisable;
	/** 在容器内运行一个 init,该 init 可转发信号并获取进程。如果为空,则省略此字段,并使用默认值(如在守护进程上配置)。 */
	private Boolean Init;
	/** 调整容器的 pid 限制。设置 -1,无限制 */
	private Integer PidsLimit;
	/** 要在容器中设置的资源限制列表,如{"Name": "nofile", "Soft": 1024, "Hard": 2048} */
	private Ulimit[] Ulimits;
	/**
	 * 可用CPU的数量(仅限windows系统)。
	 * 
	 * 在 Windows Server 容器上,处理器资源控件是互斥的。优先级的顺序是 CPUCount 首先,然后是 CPUShares
	 * 最后是CPUPercent
	 */
	private Integer CpuCount;
	/**
	 * 可用 CPU 的可用百分比(仅限 Windows)。
	 * 
	 * 在 Windows Server 容器上,处理器资源控件是互斥的。优先级的顺序是 CPUCount 首先,然后是 CPUShares 和
	 * CPU%最后。
	 */
	private Integer CpuPercent;
	/** 容器系统驱动器的最大IOps(仅限windows系统) */
	private Integer IOMaximumIOps;
	/** 容器系统驱动器的最大 IO(以字节为单位)(仅限windows系统) */
	private Integer IOMaximumBandwidth;
	/** 容器退出时要应用的行为。默认值为不重新启动。 */
	private RestartPolicyConfigurantion RestartPolicy;

	public DockerUpdate() {

	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		Util.addIfNotNull(json, "CpuShares", CpuShares);
		Util.addIfNotNull(json, "Memory", Memory);
		Util.addIfNotNull(json, "CgroupParent", CgroupParent);
		Util.addIfNotNull(json, "BlkioWeight", BlkioWeight);
		Util.addIfNotNull(json, "BlkioWeightDevice", PathAndWeight.toJson(BlkioWeightDevice));
		Util.addIfNotNull(json, "BlkioDeviceReadBps", PathAndRate.toJson(BlkioDeviceReadBps));
		Util.addIfNotNull(json, "BlkioDeviceWriteBps",
				PathAndRate.toJson(BlkioDeviceWriteBps));
		Util.addIfNotNull(json, "BlkioDeviceReadIOps",
				PathAndRate.toJson(BlkioDeviceReadIOps));
		Util.addIfNotNull(json, "BlkioDeviceWriteIOps",
				PathAndRate.toJson(BlkioDeviceWriteIOps));
		Util.addIfNotNull(json, "CpuPeriod", CpuPeriod);
		Util.addIfNotNull(json, "CpuQuota", CpuQuota);
		Util.addIfNotNull(json, "CpuRealtimePeriod", CpuRealtimePeriod);
		Util.addIfNotNull(json, "CpuRealtimeRuntime", CpuRealtimeRuntime);
		Util.addIfNotNull(json, "CpusetCpus", CpusetCpus);
		Util.addIfNotNull(json, "CpusetMems", CpusetMems);
		Util.addIfNotNull(json, "devices", Device.toJson(devices));
		Util.addIfNotNull(json, "DeviceCgroupRules",
				Util.toJSONArray(DeviceCgroupRules));
		Util.addIfNotNull(json, "DiskQuota", DiskQuota);
		Util.addIfNotNull(json, "KernelMemory", KernelMemory);
		Util.addIfNotNull(json, "MemoryReservation", MemoryReservation);
		Util.addIfNotNull(json, "MemorySwap", MemorySwap);
		Util.addIfNotNull(json, "MemorySwappiness", MemorySwappiness);
		Util.addIfNotNull(json, "NanoCPUs", NanoCPUs);
		Util.addIfNotNull(json, "OomKillDisable", OomKillDisable);
		Util.addIfNotNull(json, "Init", Init);
		Util.addIfNotNull(json, "PidsLimit", PidsLimit);
		Util.addIfNotNull(json, "Ulimits", Ulimit.toJson(Ulimits));
		Util.addIfNotNull(json, "CpuCount", CpuCount);
		Util.addIfNotNull(json, "CpuPercent", CpuPercent);
		Util.addIfNotNull(json, "IOMaximumIOps", IOMaximumIOps);
		Util.addIfNotNull(json, "IOMaximumBandwidth", IOMaximumBandwidth);
		Util.addIfNotNull(json, "RestartPolicy",
				RestartPolicyConfigurantion.toJson(RestartPolicy));
		return json;
	}

	public Integer getCpuShares() {
		return CpuShares;
	}

	public void setCpuShares(Integer cpuShares) {
		CpuShares = cpuShares;
	}

	public Long getMemory() {
		return Memory;
	}

	public void setMemory(Long memory) {
		Memory = memory;
	}

	public String getCgroupParent() {
		return CgroupParent;
	}

	public void setCgroupParent(String cgroupParent) {
		CgroupParent = cgroupParent;
	}

	public Integer getBlkioWeight() {
		return BlkioWeight;
	}

	public void setBlkioWeight(Integer blkioWeight) {
		BlkioWeight = blkioWeight;
	}

	public PathAndWeight[] getBlkioWeightDevice() {
		return BlkioWeightDevice;
	}

	public void setBlkioWeightDevice(PathAndWeight[] blkioWeightDevice) {
		BlkioWeightDevice = blkioWeightDevice;
	}

	public PathAndRate[] getBlkioDeviceReadBps() {
		return BlkioDeviceReadBps;
	}

	public void setBlkioDeviceReadBps(PathAndRate[] blkioDeviceReadBps) {
		BlkioDeviceReadBps = blkioDeviceReadBps;
	}

	public PathAndRate[] getBlkioDeviceWriteBps() {
		return BlkioDeviceWriteBps;
	}

	public void setBlkioDeviceWriteBps(PathAndRate[] blkioDeviceWriteBps) {
		BlkioDeviceWriteBps = blkioDeviceWriteBps;
	}

	public PathAndRate[] getBlkioDeviceReadIOps() {
		return BlkioDeviceReadIOps;
	}

	public void setBlkioDeviceReadIOps(PathAndRate[] blkioDeviceReadIOps) {
		BlkioDeviceReadIOps = blkioDeviceReadIOps;
	}

	public PathAndRate[] getBlkioDeviceWriteIOps() {
		return BlkioDeviceWriteIOps;
	}

	public void setBlkioDeviceWriteIOps(PathAndRate[] blkioDeviceWriteIOps) {
		BlkioDeviceWriteIOps = blkioDeviceWriteIOps;
	}

	public Integer getCpuPeriod() {
		return CpuPeriod;
	}

	public void setCpuPeriod(Integer cpuPeriod) {
		CpuPeriod = cpuPeriod;
	}

	public Integer getCpuQuota() {
		return CpuQuota;
	}

	public void setCpuQuota(Integer cpuQuota) {
		CpuQuota = cpuQuota;
	}

	public Integer getCpuRealtimePeriod() {
		return CpuRealtimePeriod;
	}

	public void setCpuRealtimePeriod(Integer cpuRealtimePeriod) {
		CpuRealtimePeriod = cpuRealtimePeriod;
	}

	public Integer getCpuRealtimeRuntime() {
		return CpuRealtimeRuntime;
	}

	public void setCpuRealtimeRuntime(Integer cpuRealtimeRuntime) {
		CpuRealtimeRuntime = cpuRealtimeRuntime;
	}

	public String getCpusetCpus() {
		return CpusetCpus;
	}

	public void setCpusetCpus(String cpusetCpus) {
		CpusetCpus = cpusetCpus;
	}

	public String getCpusetMems() {
		return CpusetMems;
	}

	public void setCpusetMems(String cpusetMems) {
		CpusetMems = cpusetMems;
	}

	public Device[] getDevices() {
		return devices;
	}

	public void setDevices(Device[] devices) {
		this.devices = devices;
	}

	public String[] getDeviceCgroupRules() {
		return DeviceCgroupRules;
	}

	public void setDeviceCgroupRules(String[] deviceCgroupRules) {
		DeviceCgroupRules = deviceCgroupRules;
	}

	public Integer getDiskQuota() {
		return DiskQuota;
	}

	public void setDiskQuota(Integer diskQuota) {
		DiskQuota = diskQuota;
	}

	public Integer getKernelMemory() {
		return KernelMemory;
	}

	public void setKernelMemory(Integer kernelMemory) {
		KernelMemory = kernelMemory;
	}

	public Integer getMemoryReservation() {
		return MemoryReservation;
	}

	public void setMemoryReservation(Integer memoryReservation) {
		MemoryReservation = memoryReservation;
	}

	public Integer getMemorySwap() {
		return MemorySwap;
	}

	public void setMemorySwap(Integer memorySwap) {
		MemorySwap = memorySwap;
	}

	public Integer getMemorySwappiness() {
		return MemorySwappiness;
	}

	public void setMemorySwappiness(Integer memorySwappiness) {
		MemorySwappiness = memorySwappiness;
	}

	public Integer getNanoCPUs() {
		return NanoCPUs;
	}

	public void setNanoCPUs(Integer nanoCPUs) {
		NanoCPUs = nanoCPUs;
	}

	public Boolean getOomKillDisable() {
		return OomKillDisable;
	}

	public void setOomKillDisable(Boolean oomKillDisable) {
		OomKillDisable = oomKillDisable;
	}

	public Boolean getInit() {
		return Init;
	}

	public void setInit(Boolean init) {
		Init = init;
	}

	public Integer getPidsLimit() {
		return PidsLimit;
	}

	public void setPidsLimit(Integer pidsLimit) {
		PidsLimit = pidsLimit;
	}

	public Ulimit[] getUlimits() {
		return Ulimits;
	}

	public void setUlimits(Ulimit[] ulimits) {
		Ulimits = ulimits;
	}

	public Integer getCpuCount() {
		return CpuCount;
	}

	public void setCpuCount(Integer cpuCount) {
		CpuCount = cpuCount;
	}

	public Integer getCpuPercent() {
		return CpuPercent;
	}

	public void setCpuPercent(Integer cpuPercent) {
		CpuPercent = cpuPercent;
	}

	public Integer getIOMaximumIOps() {
		return IOMaximumIOps;
	}

	public void setIOMaximumIOps(Integer iOMaximumIOps) {
		IOMaximumIOps = iOMaximumIOps;
	}

	public Integer getIOMaximumBandwidth() {
		return IOMaximumBandwidth;
	}

	public void setIOMaximumBandwidth(Integer iOMaximumBandwidth) {
		IOMaximumBandwidth = iOMaximumBandwidth;
	}

	public RestartPolicyConfigurantion getRestartPolicy() {
		return RestartPolicy;
	}

	public void setRestartPolicy(RestartPolicyConfigurantion restartPolicy) {
		RestartPolicy = restartPolicy;
	}

	public static class Result {

		private String[] Warnings;

		public Result(JSONObject json) {
			Warnings = Util.toStringArray(json.optJSONArray("Warnings"));
		}

		public String[] getWarnings() {
			return Warnings;
		}

	}

}
