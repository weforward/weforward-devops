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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.weforward.util.docker.ext.Util;

/***
 * docker运行命令
 * 
 * @author daibo
 *
 */
public class DockerRun {

	/** 容器主机名，一个有效的RFC 1123主机名 */
	private String Hostname;
	/** 容器域名 */
	private String Domainname;
	/** 容器内运行命令的用户 */
	private String User;
	/** 是否启用标准输入 */
	private Boolean AttachStdin = false;
	/** 是否启用标准输出 */
	private Boolean AttachStdout = true;
	/** 是否启用标准错误 */
	private Boolean AttachStderr = true;
	/** 端口映射 {"<port>/<tcp|udp|sctp>": {}} */
	private Map<String, URI> ExposedPorts;
	/** 是否将标准流附加到TTY,包括未关闭的标准输入 */
	private Boolean Tty = false;
	/** 是否打开标准输入 */
	private Boolean OpenStdin = false;
	/** 是否在一个客户端链接断开后就关闭标准输入 */
	private Boolean StdinOnce = false;
	/** 环境变量 如["FOO=bar","BAZ=quux"]，如果要删除环境变量，则不带=，如["FOO","BAZ"] */
	private String[] Env;
	/** 容器运行的命令 */
	private String[] Cmd;
	/** 健康检查 */
	private HealthConfig Healthcheck;
	/** 参数是否已转义(Windows有效) */
	private boolean ArgsEscaped;
	/** 容器的镜像 */
	private String Image;
	/** 存储卷映射 */
	private Map<String, Volume> Volumes;
	/** 运行命令的工作目录 */
	private String WorkingDir;
	/** 入口点(默认执行的命令) */
	private String[] Entrypoint;
	/** 禁止网络 */
	private Boolean NetworkDisabled;
	/** mac地址 */
	private String MacAddress;
	/** 在镜像Dockerfile中定义的元数据 */
	private String[] OnBuild;
	/** 标签,key/value属性 */
	private Map<String, String> Labels;
	/** 停止信号，停止容器是发出的信息 */
	private String StopSignal = "SIGTERM";
	/** 停止容器的超时时间 */
	private Integer StopTimeout;
	/** 跑 RUN、CMD 和 ENTRYPOINT 时使用shell命令 */
	private String[] Shell;
	/** 主机配置 */
	private HostConfigurantion HostConfig;
	/** 网络配置 */
	private NetworkingConfigurantion NetworkingConfig;

	public String getHostname() {
		return Hostname;
	}

	public void setHostname(String hostname) {
		Hostname = hostname;
	}

	public String getDomainname() {
		return Domainname;
	}

	public void setDomainname(String domainname) {
		Domainname = domainname;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public Boolean getAttachStdin() {
		return AttachStdin;
	}

	public void setAttachStdin(Boolean attachStdin) {
		AttachStdin = attachStdin;
	}

	public Boolean getAttachStdout() {
		return AttachStdout;
	}

	public void setAttachStdout(Boolean attachStdout) {
		AttachStdout = attachStdout;
	}

	public Boolean getAttachStderr() {
		return AttachStderr;
	}

	public void setAttachStderr(Boolean attachStderr) {
		AttachStderr = attachStderr;
	}

	public Map<String, URI> getExposedPorts() {
		return ExposedPorts;
	}

	public void setExposedPorts(Map<String, URI> exposedPorts) {
		ExposedPorts = exposedPorts;
	}

	public Boolean getTty() {
		return Tty;
	}

	public void setTty(Boolean tty) {
		Tty = tty;
	}

	public Boolean getOpenStdin() {
		return OpenStdin;
	}

	public void setOpenStdin(Boolean openStdin) {
		OpenStdin = openStdin;
	}

	public Boolean getStdinOnce() {
		return StdinOnce;
	}

	public void setStdinOnce(Boolean stdinOnce) {
		StdinOnce = stdinOnce;
	}

	public String[] getEnv() {
		return Env;
	}

	public void setEnv(String[] env) {
		Env = env;
	}

	public String[] getCmd() {
		return Cmd;
	}

	public void setCmd(String[] cmd) {
		Cmd = cmd;
	}

	public HealthConfig getHealthcheck() {
		return Healthcheck;
	}

	public void setHealthcheck(HealthConfig healthcheck) {
		Healthcheck = healthcheck;
	}

	public boolean isArgsEscaped() {
		return ArgsEscaped;
	}

	public void setArgsEscaped(boolean argsEscaped) {
		ArgsEscaped = argsEscaped;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public Map<String, Volume> getVolumes() {
		return Volumes;
	}

	public void setVolumes(Map<String, Volume> volumes) {
		Volumes = volumes;
	}

	public String getWorkingDir() {
		return WorkingDir;
	}

	public void setWorkingDir(String workingDir) {
		WorkingDir = workingDir;
	}

	public String[] getEntrypoint() {
		return Entrypoint;
	}

	public void setEntrypoint(String[] entrypoint) {
		Entrypoint = entrypoint;
	}

	public Boolean getNetworkDisabled() {
		return NetworkDisabled;
	}

	public void setNetworkDisabled(Boolean networkDisabled) {
		NetworkDisabled = networkDisabled;
	}

	public String getMacAddress() {
		return MacAddress;
	}

	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}

	public String[] getOnBuild() {
		return OnBuild;
	}

	public void setOnBuild(String[] onBuild) {
		OnBuild = onBuild;
	}

	public Map<String, String> getLabels() {
		return Labels;
	}

	public void setLabels(Map<String, String> labels) {
		Labels = labels;
	}

	public String getStopSignal() {
		return StopSignal;
	}

	public void setStopSignal(String stopSignal) {
		StopSignal = stopSignal;
	}

	public Integer getStopTimeout() {
		return StopTimeout;
	}

	public void setStopTimeout(Integer stopTimeout) {
		StopTimeout = stopTimeout;
	}

	public String[] getShell() {
		return Shell;
	}

	public void setShell(String[] shell) {
		Shell = shell;
	}

	public void setHostConfig(HostConfigurantion hostConfig) {
		HostConfig = hostConfig;
	}

	public HostConfigurantion getHostConfig() {
		return HostConfig;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		Util.addIfNotNull(json, "Hostname", Hostname);
		Util.addIfNotNull(json, "Domainname", Domainname);
		Util.addIfNotNull(json, "User", User);
		Util.addIfNotNull(json, "AttachStdin", AttachStdin);
		Util.addIfNotNull(json, "AttachStdout", AttachStdout);
		Util.addIfNotNull(json, "AttachStderr", AttachStderr);
		if (null != ExposedPorts) {
			JSONObject j = new JSONObject();
			for (Map.Entry<String, URI> e : ExposedPorts.entrySet()) {
				JSONObject jj = new JSONObject();
				URI uri = e.getValue();
				if (null != uri) {
					jj.put("HostIp", Util.toString(uri.getHost()));
					jj.put("HostPort", uri.getPort());
				}
				j.put(e.getKey(), jj);
			}
			json.put("ExposedPorts", j);
		}
		Util.addIfNotNull(json, "Tty", Tty);
		Util.addIfNotNull(json, "OpenStdin", OpenStdin);
		Util.addIfNotNull(json, "StdinOnce", StdinOnce);
		Util.addIfNotNull(json, "Env", Util.toJSONArray(Env));
		Util.addIfNotNull(json, "Cmd", Util.toJSONArray(Cmd));
		if (null != Healthcheck) {
			JSONObject j = new JSONObject();
			if (null != Healthcheck.Test) {
				json.put("Test", Util.toJSONArray(Healthcheck.Test));
			}
			if (null != Healthcheck.Interval) {
				json.put("Interval", Healthcheck.Interval);
			}
			if (null != Healthcheck.Timeout) {
				json.put("Timeout", Healthcheck.Timeout);
			}
			if (null != Healthcheck.Retries) {
				json.put("Retries", Healthcheck.Retries);
			}
			if (null != Healthcheck.StartPeriod) {
				json.put("StartPeriod", Healthcheck.StartPeriod);
			}
			json.put("Healthcheck", j);
		}
		Util.addIfNotNull(json, "ArgsEscaped", ArgsEscaped);
		Util.addIfNotNull(json, "Image", Image);
		Util.addIfNotNull(json, "WorkingDir", WorkingDir);
		Util.addIfNotNull(json, "Entrypoint", Util.toJSONArray(Entrypoint));
		Util.addIfNotNull(json, "NetworkDisabled", NetworkDisabled);
		if (null != Volumes) {
			JSONObject j = new JSONObject();
			for (Map.Entry<String, Volume> e : Volumes.entrySet()) {
				JSONObject jj = new JSONObject();
				Volume v = e.getValue();
				if (null != v) {
					jj.put("Bind", Util.toString(v.getBind()));
					jj.put("Mode", v.getMode());
				}
				j.put(e.getKey(), jj);
			}
			json.put("Volumes", j);
		}
		Util.addIfNotNull(json, "MacAddress", MacAddress);
		Util.addIfNotNull(json, "OnBuild", OnBuild);
		Util.addIfNotNull(json, "Labels", Util.toJSONObject(Labels));
		Util.addIfNotNull(json, "StopSignal", StopSignal);
		Util.addIfNotNull(json, "StopTimeout", StopTimeout);
		Util.addIfNotNull(json, "Shell", Util.toJSONArray(Shell));
		Util.addIfNotNull(json, "HostConfig", HostConfigurantion.toJson(HostConfig));
		Util.addIfNotNull(json, "NetworkingConfig", NetworkingConfigurantion.toJson(NetworkingConfig));
		return json;
	}

	/**
	 * 健康配置
	 * 
	 * @author daibo
	 *
	 */
	public static class HealthConfig {
		/**
		 * 测试工作 可选值
		 * <li>[] 从镜像或父镜像继承</li>
		 * <li>["NONE"] 禁止健康检查</li>
		 * <li>["CMD", args...] 直接执行参数
		 * <li>
		 * <li>["CMD-SHELL", command] 使用系统的默认 shell 运行命令</li>
		 */
		private String[] Test;
		/** 两次检查之间的等待时间(以纳秒为单位),取值为0或>1000000 (1毫秒)的值。0 表示继承 */
		private Integer Interval;
		/** 一次检查的超时时间(以纳秒为单位),取值为0或>1000000 (1毫秒)的值。0 表示继承 */
		private Integer Timeout;
		/** 将容器视为不正常所需的连续失败数。0 表示继承。 */
		private Integer Retries;
		/** 启动检查检查的时间(以纳秒为单位),取值为0或>1000000 (1毫秒)的值。0 表示继承 */
		private Integer StartPeriod;

		public HealthConfig() {

		}

		public HealthConfig(JSONObject json) {
			Test = Util.toStringArray(json.getJSONArray("Test"));
			Interval = json.optInt("Interval");
			Timeout = json.optInt("Timeout");
			Retries = json.optInt("Retries");
			StartPeriod = json.optInt("StartPeriod");
		}

		public void setTest(String[] test) {
			Test = test;
		}

		public String[] getTest() {
			return Test;
		}

		public void setInterval(Integer interval) {
			Interval = interval;
		}

		public Integer getInterval() {
			return Interval;
		}

		public void setTimeout(Integer timeout) {
			Timeout = timeout;
		}

		public Integer getTimeout() {
			return Timeout;
		}

		public void setRetries(Integer retries) {
			Retries = retries;
		}

		public Integer getRetries() {
			return Retries;
		}

		public void setStartPeriod(Integer startPeriod) {
			StartPeriod = startPeriod;
		}

		public Integer getStartPeriod() {
			return StartPeriod;
		}
	}

	/**
	 * 挂载卷
	 * 
	 * @author daibo
	 *
	 */
	public static class Volume {
		/** 绑定目录 */
		private String Bind;
		/** 读写方式，如rw(可读可写),ro(只读) */
		private String Mode;

		public Volume(JSONObject json) {
			Bind = json.optString("Bind");
			Mode = json.optString("Mode");
		}

		public Volume(String bind, String mode) {
			this.Bind = bind;
			this.Mode = mode;
		}

		public void setBind(String bind) {
			this.Bind = bind;
		}

		public String getBind() {
			return Bind;
		}

		public void setMode(String mode) {
			Mode = mode;
		}

		public String getMode() {
			return Mode;
		}
	}

	/**
	 * 主机配置
	 * 
	 * @author daibo
	 *
	 */
	public static class HostConfigurantion {
		/** 表示此容器相对于其他容器的相对 CPU 权重的整数值。 */
		private Integer CpuShares;
		/** 限制可使用的内存字节数 */
		private Integer Memory;
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
		 * 在 Windows Server 容器上,处理器资源控件是互斥的。优先级的顺序是 CPUCount 首先,然后是 CPUShares 和 CPU%最后。
		 */
		private Integer CpuPercent;
		/** 容器系统驱动器的最大IOps(仅限windows系统) */
		private Integer IOMaximumIOps;
		/** 容器系统驱动器的最大 IO(以字节为单位)(仅限windows系统) */
		private Integer IOMaximumBandwidth;
		/**
		 * 绑定到容器的存储列表
		 * 
		 * 格式如下
		 * <li>host-src:container-dest 将主机路径绑定到容器中。主机 src 和容器 dest 都必须是绝对路径。</li>
		 * <li>host-src:container-dest:ro 只读绑定</li>
		 * <li>volume-name:container-dest 将卷驱动程序管理的卷绑定到容器中。容器 dest 必须是绝对路径。</li>
		 * <li>volume-name:container-dest:ro 只读绑定</li>
		 */
		private String[] Binds;
		/** 写入容器 ID 的文件的路径 */
		private String ContainerIDFile;
		/** 容器的日志配置 */
		private LogConfigurantion LogConfig;
		/** 网络模式 可选则为 bridge, host, none和container:<name|id>,如果是其它值，则表示自定义网络 */
		private String NetworkMode;
		/**
		 * 描述将容器端口映射到主机端口,使用容器的端口号和协议作为<port>/<protocol>格式(例如 80/udp)的密钥。
		 * 如果为多个协议映射容器的端口,则将单独的条目添加到映射表中。
		 */
		private Map<String, URI[]> PortBindings;
		/** 容器退出时要应用的行为。默认值为不重新启动。 */
		private RestartPolicyConfigurantion RestartPolicy;
		/** 当容器的进程退出时自动删除容器，如果设置了RestartPolicy,则这不起作用。 */
		private Boolean AutoRemove;
		/** 此容器用于装载卷的驱动程序。 */
		private String VolumeDriver;
		/** 要从另一个容器继承的卷的列表,指定<container name>[:<ro|rw>] */
		private String[] VolumesFrom;
		/** 挂载点 */
		private Mount[] Mounts;
		/** 要添加到容器的内核功能的列表 */
		private String[] CapAdd;
		/** 要从容器中丢弃的内核功能的列表 */
		private String[] CapDrop;
		/** 供容器使用的DNS服务器的列表 */
		private String[] Dns;
		/** DNS搜索域的列表 */
		private String[] DnsOptions;
		/** 要添加到主机的/etc/hosts的列表，格式为["hostname:IP"] */
		private String[] ExtraHosts;
		/** 容器进程将以其他组形式运行的列表 */
		private String[] GroupAdd;
		/**
		 * 容器的IPC共享模式
		 * 
		 * 可选:
		 * <li>"none" 自己的专用IPC命名空间,未装入/dev/shm</li>
		 * <li>"private" 自己的专IPC命名空间</li>
		 * <li>"shareable" 自己的专用IPC命名空间,可以与其他容器共享</li>
		 * <li>"container:<name|id>" 加入另一个(可共享)容器的 IPC 命名空间</li>
		 * <li>"host" 使用主机系统的IPC命名空间</li>
		 * 
		 * 如果未指定,则使用守护进程默认值,可以是"私有"或"可共享",具体取决于守护进程版本和配置。
		 * 
		 */
		private String IpcMode;
		/** 要用于容器的C组 */
		private String Cgroup;
		/** 容器的链接列表 格式为container_name:alias. */
		private String[] Links;
		/** 包含给容器的分数的整数值,用于调整 OOM 杀手首选项。 */
		private Integer OomScoreAdj;
		/**
		 * 为容器设置 PID(进程)命名空间模式 格式
		 * <li>container:<name|id> 加入另一个容器的 PID 命名空间</li>
		 * <li>host:在容器内使用主机的 PID 命名空间</li>
		 */
		private String PidMode;
		/** 使容器完全访问主机 */
		private Boolean Privileged;
		/**
		 * 为所有容器的公开端口分配临时主机端口
		 *
		 * 当容器停止并分配容器启动时,端口将取消分配。重新启动容器时,分配的端口可能会更改。
		 * 
		 * 端口是从依赖于内核的短暂端口范围中选择的。例如,在 Linux 上,范围由/proc/sys/net/ipv4/ip_local_port_range
		 */
		private Boolean PublishAllPorts;
		/** 是滞将容器的根文件系统装入为只读 */
		private Boolean ReadonlyRootfs;
		/** 用于自定义MLS系统(如 SELinux)的标签的字符串值列表 */
		private String[] SecurityOpt;
		/** 此容器的存储驱动程序选项 如｛"size": "120G"} */
		private Map<String, String> StorageOpt;
		/**
		 * 容器目录的映射,应替换为 tmpfs 装载及其相应的装载选项 如{ "/run": "rw,noexec,nosuid,size=65536k" }
		 */
		private Map<String, String> Tmpfs;
		/** 要用于容器的 UTS 命名空间 */
		private String UTSMode;
		/** 启用用户名空间重新映射选项时,设置容器的用户名空间模式 */
		private String UsernsMode;
		/** /dev/shm的字节大小，如果省略,系统将使用 64MB */
		private Integer ShmSize;
		/** 要在容器中设置的内核参数(系统)的列表。如{"net.ipv4.ip_forward": "1"} */
		private Map<String, String> Sysctls;
		/** 此容器的Runtime */
		private String Runtime;
		/** 初始控制台大小,格式为[height, width]（仅windwons系统有效） */
		private Integer[] ConsoleSize;
		/** 有效值:"default" "process" "hyperv" 容器的隔离技术()（仅windwons系统有效 */
		private String Isolation;

		public HostConfigurantion() {

		}

		public HostConfigurantion(JSONObject json) {
			CpuShares = json.optInt("CpuShares");
			Memory = json.optInt("Memory");
			CgroupParent = json.optString("CgroupParent");
			BlkioWeight = json.optInt("BlkioWeight");
			BlkioWeightDevice = PathAndWeight.fromJson(json.optJSONArray("BlkioWeightDevice"));
			BlkioDeviceReadBps = PathAndRate.fromJson(json.optJSONArray("BlkioDeviceReadBps"));
			BlkioDeviceWriteBps = PathAndRate.fromJson(json.optJSONArray("BlkioDeviceWriteBps"));
			BlkioDeviceReadIOps = PathAndRate.fromJson(json.optJSONArray("BlkioDeviceReadIOps"));
			BlkioDeviceWriteIOps = PathAndRate.fromJson(json.optJSONArray("BlkioDeviceWriteIOps"));
			CpuPeriod = json.optInt("CpuPeriod");
			CpuQuota = json.optInt("CpuQuota");

			CpuRealtimePeriod = json.optInt("CpuRealtimePeriod");
			CpuRealtimeRuntime = json.optInt("CpuRealtimeRuntime");
			CpusetCpus = json.optString("CpusetCpus");
			CpusetMems = json.optString("CpusetMems");
			devices = Device.fromJson(json.optJSONArray("devices"));
			DeviceCgroupRules = Util.toStringArray(json.optJSONArray("DeviceCgroupRules"));
			DiskQuota = json.optInt("DiskQuota");
			KernelMemory = json.optInt("KernelMemory");
			MemoryReservation = json.optInt("MemoryReservation");
			MemorySwap = json.optInt("MemorySwap");
			MemorySwappiness = json.optInt("MemorySwappiness");
			NanoCPUs = json.optInt("NanoCPUs");
			OomKillDisable = json.optBoolean("OomKillDisable");
			Init = json.optBoolean("Init");
			PidsLimit = json.optInt("PidsLimit");
			Ulimits = Ulimit.fromJson(json.optJSONArray("Ulimits"));
			CpuCount = json.optInt("CpuCount");
			CpuPercent = json.optInt("cpuPercent");
			IOMaximumIOps = json.optInt("IOMaximumIOps");
			IOMaximumBandwidth = json.optInt("IOMaximumBandwidth");
			Binds = Util.toStringArray(json.optJSONArray("Binds"));
			ContainerIDFile = json.optString("ContainerIDFile");
			LogConfig = LogConfigurantion.fromJson(json.optJSONObject("LogConfig"));
			NetworkMode = json.optString("NetworkMode");
			{
				JSONObject obj = json.optJSONObject("PortBindings");
				if (null != PortBindings) {
					PortBindings = new HashMap<String, URI[]>();
					for (String key : obj.keySet()) {
						JSONArray arr = obj.getJSONArray(key);
						URI[] uris = new URI[arr.length()];
						for (int i = 0; i < arr.length(); i++) {
							JSONObject cobj = arr.getJSONObject(i);
							uris[i] = URI
									.create("docker://" + cobj.optString("HostIp") + ":" + cobj.optString("HostPort"));
						}
						PortBindings.put(key, uris);
					}
				}
			}
			RestartPolicy = RestartPolicyConfigurantion.fromJson(json.optJSONObject("RestartPolicy"));
			AutoRemove = json.optBoolean("AutoRemove");
			VolumeDriver = json.optString("VolumeDriver");
			VolumesFrom = Util.toStringArray(json.optJSONArray("VolumesFrom"));
			Mounts = Mount.fromJson(json.optJSONArray("Mounts"));
			CapAdd = Util.toStringArray(json.optJSONArray("CapAdd"));
			CapDrop = Util.toStringArray(json.optJSONArray("CapDrop"));
			Dns = Util.toStringArray(json.optJSONArray("Dns"));
			DnsOptions = Util.toStringArray(json.optJSONArray("DnsOptions"));
			ExtraHosts = Util.toStringArray(json.optJSONArray("ExtraHosts"));
			GroupAdd = Util.toStringArray(json.optJSONArray("GroupAdd"));
			IpcMode = json.optString("IpcMode");
			Cgroup = json.optString("Cgroup");
			Links = Util.toStringArray(json.optJSONArray("Links"));
			OomScoreAdj = json.optInt("OomScoreAdj");
			PidMode = json.optString("PidMode");
			Privileged = json.optBoolean("Privileged");
			PublishAllPorts = json.optBoolean("PublishAllPorts");
			ReadonlyRootfs = json.optBoolean("ReadonlyRootfs");
			SecurityOpt = Util.toStringArray(json.optJSONArray("SecurityOpt"));
			StorageOpt = Util.toStringMap(json.optJSONObject("StorageOpt"));
			Tmpfs = Util.toStringMap(json.optJSONObject("Tmpfs"));
			UTSMode = json.optString("UTSMode");
			UsernsMode = json.optString("UsernsMode");
			ShmSize = json.optInt("ShmSize");
			Sysctls = Util.toStringMap(json.optJSONObject("Sysctls"));
			Runtime = json.optString("Runtime");
			ConsoleSize = Util.toIntArray(json.optJSONArray("ConsoleSize"));
			Isolation = json.optString("Isolation");
		}

		public static JSONObject toJson(HostConfigurantion config) {
			if (null == config) {
				return null;
			}
			JSONObject json = new JSONObject();
			Util.addIfNotNull(json, "CpuShares", config.CpuShares);
			Util.addIfNotNull(json, "Memory", config.Memory);
			Util.addIfNotNull(json, "CgroupParent", config.CgroupParent);
			Util.addIfNotNull(json, "BlkioWeight", config.BlkioWeight);
			Util.addIfNotNull(json, "BlkioWeightDevice", PathAndWeight.toJson(config.BlkioWeightDevice));
			Util.addIfNotNull(json, "BlkioDeviceReadBps", PathAndRate.toJson(config.BlkioDeviceReadBps));
			Util.addIfNotNull(json, "BlkioDeviceWriteBps", PathAndRate.toJson(config.BlkioDeviceWriteBps));
			Util.addIfNotNull(json, "BlkioDeviceReadIOps", PathAndRate.toJson(config.BlkioDeviceReadIOps));
			Util.addIfNotNull(json, "BlkioDeviceWriteIOps", PathAndRate.toJson(config.BlkioDeviceWriteIOps));
			Util.addIfNotNull(json, "CpuPeriod", config.CpuPeriod);
			Util.addIfNotNull(json, "CpuQuota", config.CpuQuota);
			Util.addIfNotNull(json, "CpuRealtimePeriod", config.CpuRealtimePeriod);
			Util.addIfNotNull(json, "CpuRealtimeRuntime", config.CpuRealtimeRuntime);
			Util.addIfNotNull(json, "CpusetCpus", config.CpusetCpus);
			Util.addIfNotNull(json, "CpusetMems", config.CpusetMems);
			Util.addIfNotNull(json, "devices", Device.toJson(config.devices));
			Util.addIfNotNull(json, "DeviceCgroupRules", Util.toJSONArray(config.DeviceCgroupRules));
			Util.addIfNotNull(json, "DiskQuota", config.DiskQuota);
			Util.addIfNotNull(json, "KernelMemory", config.KernelMemory);
			Util.addIfNotNull(json, "MemoryReservation", config.MemoryReservation);
			Util.addIfNotNull(json, "MemorySwap", config.MemorySwap);
			Util.addIfNotNull(json, "MemorySwappiness", config.MemorySwappiness);
			Util.addIfNotNull(json, "NanoCPUs", config.NanoCPUs);
			Util.addIfNotNull(json, "OomKillDisable", config.OomKillDisable);
			Util.addIfNotNull(json, "Init", config.Init);
			Util.addIfNotNull(json, "PidsLimit", config.PidsLimit);
			Util.addIfNotNull(json, "Ulimits", Ulimit.toJson(config.Ulimits));
			Util.addIfNotNull(json, "CpuCount", config.CpuCount);
			Util.addIfNotNull(json, "CpuPercent", config.CpuPercent);
			Util.addIfNotNull(json, "IOMaximumIOps", config.IOMaximumIOps);
			Util.addIfNotNull(json, "IOMaximumBandwidth", config.IOMaximumBandwidth);
			Util.addIfNotNull(json, "Binds", Util.toJSONArray(config.Binds));
			Util.addIfNotNull(json, "ContainerIDFile", config.ContainerIDFile);
			Util.addIfNotNull(json, "LogConfig", LogConfigurantion.toJson(config.LogConfig));
			Util.addIfNotNull(json, "NetworkMode", config.NetworkMode);
			if (null != config.PortBindings) {
				JSONObject obj = new JSONObject();
				for (Map.Entry<String, URI[]> e : config.PortBindings.entrySet()) {
					JSONArray arr = new JSONArray();
					URI[] uris = e.getValue();
					if (null != uris) {
						for (URI uri : uris) {
							JSONObject childjson = new JSONObject();
							childjson.put("HostIp", uri.getHost());
							childjson.put("HostPort", String.valueOf(uri.getHost()));
							arr.put(childjson);
						}
					}
					obj.put(e.getKey(), arr);
				}
				json.put("PortBindings", obj);
			}
			/** 容器退出时要应用的行为。默认值为不重新启动。 */
			Util.addIfNotNull(json, "RestartPolicy", RestartPolicyConfigurantion.toJson(config.RestartPolicy));
			Util.addIfNotNull(json, "AutoRemove", config.AutoRemove);
			Util.addIfNotNull(json, "VolumeDriver", config.VolumeDriver);
			Util.addIfNotNull(json, "VolumesFrom", Util.toJSONArray(config.VolumesFrom));
			Util.addIfNotNull(json, "Mounts", Mount.toJson(config.Mounts));
			Util.addIfNotNull(json, "CapAdd", Util.toJSONArray(config.CapAdd));
			Util.addIfNotNull(json, "CapDrop", Util.toJSONArray(config.CapDrop));
			Util.addIfNotNull(json, "Dns", Util.toJSONArray(config.Dns));
			Util.addIfNotNull(json, "DnsOptions", Util.toJSONArray(config.DnsOptions));
			Util.addIfNotNull(json, "ExtraHosts", Util.toJSONArray(config.ExtraHosts));
			Util.addIfNotNull(json, "GroupAdd", Util.toJSONArray(config.GroupAdd));
			Util.addIfNotNull(json, "IpcMode", config.IpcMode);
			Util.addIfNotNull(json, "Cgroup", config.Cgroup);
			Util.addIfNotNull(json, "Links", Util.toJSONArray(config.Links));
			Util.addIfNotNull(json, "OomScoreAdj", config.OomScoreAdj);
			Util.addIfNotNull(json, "PidMode", config.PidMode);
			Util.addIfNotNull(json, "Privileged", config.Privileged);
			Util.addIfNotNull(json, "PublishAllPorts", config.PublishAllPorts);
			Util.addIfNotNull(json, "ReadonlyRootfs", config.ReadonlyRootfs);
			Util.addIfNotNull(json, "SecurityOpt", Util.toJSONArray(config.SecurityOpt));
			Util.addIfNotNull(json, "StorageOpt", Util.toJSONObject(config.StorageOpt));
			Util.addIfNotNull(json, "Tmpfs", Util.toJSONObject(config.Tmpfs));
			Util.addIfNotNull(json, "UTSMode", config.UTSMode);
			Util.addIfNotNull(json, "UsernsMode", config.UsernsMode);
			Util.addIfNotNull(json, "ShmSize", config.ShmSize);
			Util.addIfNotNull(json, "Sysctls", Util.toJSONObject(config.Sysctls));
			Util.addIfNotNull(json, "Runtime", config.Runtime);
			Util.addIfNotNull(json, "ConsoleSize", Util.toJSONArray(config.ConsoleSize));
			Util.addIfNotNull(json, "Isolation", config.Isolation);
			return json;
		}

		public Integer getCpuShares() {
			return CpuShares;
		}

		public void setCpuShares(Integer cpuShares) {
			CpuShares = cpuShares;
		}

		public Integer getMemory() {
			return Memory;
		}

		public void setMemory(Integer memory) {
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

		public void setBinds(String[] binds) {
			this.Binds = binds;
		}

		public String[] getBinds() {
			return Binds;
		}

		public String getContainerIDFile() {
			return ContainerIDFile;
		}

		public void setContainerIDFile(String containerIDFile) {
			ContainerIDFile = containerIDFile;
		}

		public LogConfigurantion getLogConfig() {
			return LogConfig;
		}

		public void setLogConfig(LogConfigurantion logConfig) {
			LogConfig = logConfig;
		}

		public void setNetworkMode(String networkMode) {
			NetworkMode = networkMode;
		}

		public String getNetworkMode() {
			return NetworkMode;
		}

		public Map<String, URI[]> getPortBindings() {
			return PortBindings;
		}

		public void setPortBindings(Map<String, URI[]> portBindings) {
			PortBindings = portBindings;
		}

		public RestartPolicyConfigurantion getRestartPolicy() {
			return RestartPolicy;
		}

		public void setRestartPolicy(RestartPolicyConfigurantion restartPolicy) {
			RestartPolicy = restartPolicy;
		}

		public Boolean getAutoRemove() {
			return AutoRemove;
		}

		public void setAutoRemove(Boolean autoRemove) {
			AutoRemove = autoRemove;
		}

		public String getVolumeDriver() {
			return VolumeDriver;
		}

		public void setVolumeDriver(String volumeDriver) {
			VolumeDriver = volumeDriver;
		}

		public String[] getVolumesFrom() {
			return VolumesFrom;
		}

		public void setVolumesFrom(String[] volumesFrom) {
			VolumesFrom = volumesFrom;
		}

		public Mount[] getMounts() {
			return Mounts;
		}

		public void setMounts(Mount[] mounts) {
			Mounts = mounts;
		}

		public String[] getCapAdd() {
			return CapAdd;
		}

		public void setCapAdd(String[] capAdd) {
			CapAdd = capAdd;
		}

		public String[] getCapDrop() {
			return CapDrop;
		}

		public void setCapDrop(String[] capDrop) {
			CapDrop = capDrop;
		}

		public String[] getDns() {
			return Dns;
		}

		public void setDns(String[] dns) {
			Dns = dns;
		}

		public String[] getDnsOptions() {
			return DnsOptions;
		}

		public void setDnsOptions(String[] dnsOptions) {
			DnsOptions = dnsOptions;
		}

		public String[] getExtraHosts() {
			return ExtraHosts;
		}

		public void setExtraHosts(String[] extraHosts) {
			ExtraHosts = extraHosts;
		}

		public String[] getGroupAdd() {
			return GroupAdd;
		}

		public void setGroupAdd(String[] groupAdd) {
			GroupAdd = groupAdd;
		}

		public String getIpcMode() {
			return IpcMode;
		}

		public void setIpcMode(String ipcMode) {
			IpcMode = ipcMode;
		}

		public String getCgroup() {
			return Cgroup;
		}

		public void setCgroup(String cgroup) {
			Cgroup = cgroup;
		}

		public String[] getLinks() {
			return Links;
		}

		public void setLinks(String[] links) {
			Links = links;
		}

		public Integer getOomScoreAdj() {
			return OomScoreAdj;
		}

		public void setOomScoreAdj(Integer oomScoreAdj) {
			OomScoreAdj = oomScoreAdj;
		}

		public String getPidMode() {
			return PidMode;
		}

		public void setPidMode(String pidMode) {
			PidMode = pidMode;
		}

		public Boolean getPrivileged() {
			return Privileged;
		}

		public void setPrivileged(Boolean privileged) {
			Privileged = privileged;
		}

		public Boolean getPublishAllPorts() {
			return PublishAllPorts;
		}

		public void setPublishAllPorts(Boolean publishAllPorts) {
			PublishAllPorts = publishAllPorts;
		}

		public Boolean getReadonlyRootfs() {
			return ReadonlyRootfs;
		}

		public void setReadonlyRootfs(Boolean readonlyRootfs) {
			ReadonlyRootfs = readonlyRootfs;
		}

		public String[] getSecurityOpt() {
			return SecurityOpt;
		}

		public void setSecurityOpt(String[] securityOpt) {
			SecurityOpt = securityOpt;
		}

		public Map<String, String> getStorageOpt() {
			return StorageOpt;
		}

		public void setStorageOpt(Map<String, String> storageOpt) {
			StorageOpt = storageOpt;
		}

		public Map<String, String> getTmpfs() {
			return Tmpfs;
		}

		public void setTmpfs(Map<String, String> tmpfs) {
			Tmpfs = tmpfs;
		}

		public String getUTSMode() {
			return UTSMode;
		}

		public void setUTSMode(String uTSMode) {
			UTSMode = uTSMode;
		}

		public String getUsernsMode() {
			return UsernsMode;
		}

		public void setUsernsMode(String usernsMode) {
			UsernsMode = usernsMode;
		}

		public Integer getShmSize() {
			return ShmSize;
		}

		public void setShmSize(Integer shmSize) {
			ShmSize = shmSize;
		}

		public Map<String, String> getSysctls() {
			return Sysctls;
		}

		public void setSysctls(Map<String, String> sysctls) {
			Sysctls = sysctls;
		}

		public String getRuntime() {
			return Runtime;
		}

		public void setRuntime(String runtime) {
			Runtime = runtime;
		}

		public Integer[] getConsoleSize() {
			return ConsoleSize;
		}

		public void setConsoleSize(Integer[] consoleSize) {
			ConsoleSize = consoleSize;
		}

		public String getIsolation() {
			return Isolation;
		}

		public void setIsolation(String isolation) {
			Isolation = isolation;
		}

	}

	/**
	 * 路径和权重
	 * 
	 * @author daibo
	 *
	 */
	public static class PathAndWeight {

		private String Path;

		private Integer Weight;

		public PathAndWeight(String path, Integer weight) {
			Path = path;
			Weight = weight;
		}

		public String getPath() {
			return Path;
		}

		public Integer getWeight() {
			return Weight;
		}

		public static PathAndWeight[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			PathAndWeight[] arr = new PathAndWeight[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.getJSONObject(i));
			}
			return arr;
		}

		public static PathAndWeight fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new PathAndWeight(json.optString("Path"), json.optInt("Weight"));
		}

		public static JSONArray toJson(PathAndWeight[] arr) {
			if (null == arr) {
				return null;
			}
			JSONArray jarr = new JSONArray();
			for (PathAndWeight v : arr) {
				JSONObject json = new JSONObject();
				json.put("Path", v.Path);
				json.put("Weight", v.Weight);
				jarr.put(json);
			}
			return jarr;
		}
	}

	/**
	 * 路径和比率
	 * 
	 * @author daibo
	 *
	 */
	public static class PathAndRate {

		private String Path;

		private Integer Rate;

		public PathAndRate(String path, Integer rate) {
			Path = path;
			Rate = rate;
		}

		public String getPath() {
			return Path;
		}

		public Integer getRate() {
			return Rate;
		}

		public static PathAndRate[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			PathAndRate[] arr = new PathAndRate[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.optJSONObject(i));
			}
			return arr;
		}

		public static PathAndRate fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new PathAndRate(json.optString("Path"), json.optInt("Rate"));
		}

		public static JSONArray toJson(PathAndRate[] arr) {
			if (null == arr) {
				return null;
			}
			JSONArray jarr = new JSONArray();
			for (PathAndRate v : arr) {
				JSONObject json = new JSONObject();
				json.put("Path", v.Path);
				json.put("Rate", v.Rate);
				jarr.put(json);
			}
			return jarr;
		}
	}

	/**
	 * 路径和类型
	 * 
	 * @author daibo
	 *
	 */
	public static class PathAndKind {

		private String Path;

		private Integer Kind;

		public PathAndKind(String path, Integer kind) {
			Path = path;
			Kind = kind;
		}

		public String getPath() {
			return Path;
		}

		public Integer getKind() {
			return Kind;
		}

		public static PathAndKind[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			PathAndKind[] arr = new PathAndKind[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.optJSONObject(i));
			}
			return arr;
		}

		public static PathAndKind fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new PathAndKind(json.optString("Path"), json.optInt("Kind"));
		}

		public static JSONArray toJson(PathAndKind[] arr) {
			if (null == arr) {
				return null;
			}
			JSONArray jarr = new JSONArray();
			for (PathAndKind v : arr) {
				JSONObject json = new JSONObject();
				json.put("Path", v.Path);
				json.put("Kind", v.Kind);
				jarr.put(json);
			}
			return jarr;
		}

		@Override
		public String toString() {
			return "{ \"Path\":\"" + Path + "\" , \"Kind\":\"" + Kind + "\"}";
		}
	}

	/**
	 * 设备
	 * 
	 * @author daibo
	 *
	 */
	public static class Device {

		private String PathOnHost;

		private String PathInContainer;

		private String CgroupPermissions;

		public Device(String pathOnHost, String pathInContainer, String cgroupPermissions) {
			PathOnHost = pathInContainer;
			PathInContainer = pathInContainer;
			CgroupPermissions = cgroupPermissions;
		}

		public String getPathOnHost() {
			return PathOnHost;
		}

		public String getPathInContainer() {
			return PathInContainer;
		}

		public String getCgroupPermissions() {
			return CgroupPermissions;
		}

		public static Device[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			Device[] arr = new Device[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.getJSONObject(i));
			}
			return arr;
		}

		public static Device fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new Device(json.optString("PathOnHost"), json.optString("PathInContainer"),
					json.optString("CgroupPermissions"));
		}

		public static JSONArray toJson(Device[] devices) {
			if (null == devices) {
				return null;
			}
			JSONArray arr = new JSONArray();
			for (Device device : devices) {
				JSONObject json = new JSONObject();
				json.put("PathOnHost", device.PathOnHost);
				json.put("PathInContainer", device.PathInContainer);
				json.put("CgroupPermissions", device.CgroupPermissions);
				arr.put(json);
			}
			return arr;

		}

	}

	/**
	 * 限制
	 * 
	 * @author daibo
	 *
	 */
	public static class Ulimit {

		private String Name;

		private String Soft;

		private String Hard;

		public Ulimit(String name, String soft, String hard) {
			Name = name;
			Soft = soft;
			Hard = hard;
		}

		public String getName() {
			return Name;
		}

		public String getSoft() {
			return Soft;
		}

		public String getHard() {
			return Hard;
		}

		public static Ulimit[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			Ulimit[] arr = new Ulimit[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.getJSONObject(i));
			}
			return arr;
		}

		public static Ulimit fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new Ulimit(json.optString("Name"), json.optString("Soft"), json.optString("Hard"));
		}

		public static JSONArray toJson(Ulimit[] ulimits) {
			if (null == ulimits) {
				return null;
			}
			JSONArray arr = new JSONArray();
			for (Ulimit l : ulimits) {
				JSONObject json = new JSONObject();
				json.put("Name", l.Name);
				json.put("Soft", l.Soft);
				json.put("Hard", l.Hard);
				arr.put(json);
			}
			return arr;
		}
	}

	/**
	 * 日志配置
	 * 
	 * @author daibo
	 *
	 */
	public static class LogConfigurantion {
		/**
		 * 有效值为 "json-file" "syslog" "journald" "gelf" "fluentd" "awslogs" "splunk"
		 * "etwlogs" "none"
		 */
		private String Type;
		/** 配置 */
		private Map<String, String> Config;

		public LogConfigurantion(String type, Map<String, String> config) {
			Type = type;
			Config = config;
		}

		public static LogConfigurantion fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new LogConfigurantion(json.optString("Type"), Util.toStringMap(json.optJSONObject("Config")));
		}

		public static JSONObject toJson(LogConfigurantion config) {
			if (null == config) {
				return null;
			}
			JSONObject json = new JSONObject();
			Util.addIfNotNull(json, "Type", config.Type);
			if (null != config.Config) {
				JSONObject childJosn = new JSONObject();
				for (Map.Entry<String, String> entry : config.Config.entrySet()) {
					childJosn.put(entry.getKey(), entry.getValue());
				}
				json.put("Config", childJosn);
			}
			return json;
		}

		/**
		 * 有效值为 "json-file" "syslog" "journald" "gelf" "fluentd" "awslogs" "splunk"
		 * "etwlogs" "none"
		 */
		public String getType() {
			return Type;
		}

		/** 配置 */
		public Map<String, String> getConfig() {
			return Config;
		}

	}

	/**
	 * 重启策略配置
	 * 
	 * @author daibo
	 *
	 */
	public static class RestartPolicyConfigurantion {
		public static final RestartPolicyConfigurantion ALWAYS = new RestartPolicyConfigurantion("always", 0);
		public static final RestartPolicyConfigurantion UNLESS_STOPPED = new RestartPolicyConfigurantion(
				"unless-stopped", 0);
		/***
		 * 有效值:""，"always"，"unless-stopped"，"on-failure"
		 * 
		 * <li>""表示不重新启动</li>
		 * <li>"always" 表示始终重新启动</li>
		 * <li>"unless-stopped" 除非用户已手动停止容器,否则始终重新启动</li>
		 * <li>"on-failure" 仅当容器退出代码非零时重新启动</li>
		 */
		private String Name;
		/** 当Name为"on-failure"有效，最大的重试次数 */
		private Integer MaximumRetryCount;

		public RestartPolicyConfigurantion(String name, Integer maximumRetryCount) {
			Name = name;
			MaximumRetryCount = maximumRetryCount;
		}

		public static RestartPolicyConfigurantion fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new RestartPolicyConfigurantion(json.optString("Name"), json.optInt("MaximumRetryCount"));
		}

		public static JSONObject toJson(RestartPolicyConfigurantion config) {
			if (null == config) {
				return null;
			}
			JSONObject json = new JSONObject();
			json.put("Name", config.Name);
			if (null != config.MaximumRetryCount) {
				json.put("MaximumRetryCount", config.MaximumRetryCount);
			}
			return json;

		}

		/***
		 * 有效值:""，"always"，"unless-stopped"，"on-failure"
		 * 
		 * <li>""表示不重新启动</li>
		 * <li>"always" 表示始终重新启动</li>
		 * <li>"unless-stopped" 除非用户已手动停止容器,否则始终重新启动</li>
		 * <li>"on-failure" 仅当容器退出代码非零时重新启动</li>
		 */
		public String getName() {
			return Name;
		}

		/** 当Name为"on-failure"有效，最大的重试次数 */
		public Integer getMaximumRetryCount() {
			return MaximumRetryCount;
		}
	}

	/**
	 * 挂载
	 * 
	 * @author daibo
	 *
	 */
	public static class Mount {
		/** 容器内路径 */
		private String Target;
		/** 挂载源 (如 一个存储券名,一个主机路径 */
		private String Source;
		/**
		 * 类型 有效值"bind" "volume" "tmpfs"
		 * <li>"bind" 将文件或目录从主机装载到容器中</li>
		 * <li>"volume" 创建具有给定名称和选项的卷(或使用具有相同名称和选项的预先存在的卷)。删除容器时不会删除这些卷。</li>
		 * <li>"tmpfs"使用给定选项创建 tmpfs,tmpfs不能指定source</li>
		 */
		private String Type;
		/** 是否为只读 */
		private Boolean ReadOnly;
		/** 装载的一致性要求:default, consistent, cached, or delegated. */
		private String Consistency;
		/** bind选项配置 */
		private BindOptionsConfigurantion BindOptions;
		/** volume选项配置 */
		private VolumeOptionsConfigurantion VolumeOptions;
		/** tmpfs选项配置 */
		private TmpfsOptionsConfigurantion TmpfsOptions;

		public Mount(String target, String source, String type, Boolean readOnly, String consistency) {
			Target = target;
			Source = source;
			Type = type;
			ReadOnly = readOnly;
			Consistency = consistency;
		}

		public static Mount[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			Mount[] arr = new Mount[array.length()];
			for (int i = 0; i < array.length(); i++) {
				arr[i] = fromJson(array.optJSONObject(i));
			}
			return arr;
		}

		private static Mount fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			Mount m = new Mount(json.optString("Target"), json.optString("Source"), json.optString("Type"),
					json.optBoolean("ReadOnly"), json.optString("Consistency"));
			m.setBindOptions(BindOptionsConfigurantion.fromJson(json.optJSONObject("BindOptions")));
			m.setVolumeOptions(VolumeOptionsConfigurantion.fromJson(json.optJSONObject("VolumeOptions")));
			m.setTmpfsOptions(TmpfsOptionsConfigurantion.fromJson(json.optJSONObject("TmpfsOptions")));
			return m;
		}

		public static JSONArray toJson(Mount[] mounts) {
			if (null == mounts) {
				return null;
			}
			JSONArray arr = new JSONArray();
			for (Mount mount : mounts) {
				JSONObject json = new JSONObject();
				Util.addIfNotNull(json, "Target", mount.Target);
				Util.addIfNotNull(json, "Source", mount.Source);
				Util.addIfNotNull(json, "Type", mount.Type);
				Util.addIfNotNull(json, "ReadOnly", mount.ReadOnly);
				Util.addIfNotNull(json, "Consistency", mount.Consistency);
				Util.addIfNotNull(json, "BindOptions", BindOptionsConfigurantion.toJson(mount.BindOptions));
				Util.addIfNotNull(json, "VolumeOptions", VolumeOptionsConfigurantion.toJson(mount.VolumeOptions));
				Util.addIfNotNull(json, "TmpfsOptions", TmpfsOptionsConfigurantion.toJson(mount.TmpfsOptions));
				arr.put(json);
			}
			return arr;
		}

		public String getTarget() {
			return Target;
		}

		public String getSource() {
			return Source;
		}

		public String getType() {
			return Type;
		}

		public Boolean getReadOnly() {
			return ReadOnly;
		}

		public String getConsistency() {
			return Consistency;
		}

		public BindOptionsConfigurantion getBindOptions() {
			return BindOptions;
		}

		public void setBindOptions(BindOptionsConfigurantion bindOptions) {
			BindOptions = bindOptions;
		}

		public VolumeOptionsConfigurantion getVolumeOptions() {
			return VolumeOptions;
		}

		public void setVolumeOptions(VolumeOptionsConfigurantion volumeOptions) {
			VolumeOptions = volumeOptions;
		}

		public TmpfsOptionsConfigurantion getTmpfsOptions() {
			return TmpfsOptions;
		}

		public void setTmpfsOptions(TmpfsOptionsConfigurantion tmpfsOptions) {
			TmpfsOptions = tmpfsOptions;
		}

	}

	public static class BindOptionsConfigurantion {
		/** 传播模式 有效值"private" "rprivate" "shared" "rshared" "slave" "rslave" */
		private String Propagation;

		public BindOptionsConfigurantion(String propagation) {
			Propagation = propagation;
		}

		public String getPropagation() {
			return Propagation;
		}

		public static BindOptionsConfigurantion fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new BindOptionsConfigurantion(json.optString("Propagation"));

		}

		public static JSONObject toJson(BindOptionsConfigurantion bind) {
			if (null == bind) {
				return null;
			}
			JSONObject json = new JSONObject();
			json.put("Propagation", bind.Propagation);
			return json;
		}

	}

	public static class VolumeOptionsConfigurantion {
		/** 是否使用目标中的数据填充卷 */
		private Boolean NoCopy;
		/** 用户定义的元信息 key/value值 */
		private Map<String, String> Labels;
		/** 驱动程序特定选项的映射 */
		private Map<String, Map<String, String>> DriverConfig;

		public VolumeOptionsConfigurantion(Boolean noCopy, Map<String, String> labels,
				Map<String, Map<String, String>> driverConfig) {
			NoCopy = noCopy;
			Labels = labels;
			DriverConfig = driverConfig;
		}

		public static VolumeOptionsConfigurantion fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			boolean noCopy = json.optBoolean("NoCopy");
			Map<String, String> labels = Util.toStringMap(json.optJSONObject("Labels"));
			JSONObject obj = json.optJSONObject("DriverConfig");
			Map<String, Map<String, String>> driverConfig;
			if (null != obj) {
				driverConfig = new HashMap<String, Map<String, String>>();
				for (String key : obj.keySet()) {
					driverConfig.put(key, Util.toStringMap(obj.getJSONObject(key)));
				}
			} else {
				driverConfig = null;
			}
			return new VolumeOptionsConfigurantion(noCopy, labels, driverConfig);
		}

		public static JSONObject toJson(VolumeOptionsConfigurantion volume) {
			if (null == volume) {
				return null;
			}
			JSONObject json = new JSONObject();
			Util.addIfNotNull(json, "NoCopy", volume.NoCopy);
			if (null != volume.Labels) {
				JSONObject childjson = new JSONObject();
				for (Map.Entry<String, String> e : volume.Labels.entrySet()) {
					childjson.put(e.getKey(), e.getValue());
				}
				json.put("Labels", childjson);
			}
			if (null != volume.DriverConfig) {
				JSONObject childjson = new JSONObject();
				for (Map.Entry<String, Map<String, String>> e : volume.DriverConfig.entrySet()) {
					Map<String, String> v = e.getValue();
					JSONObject cchildjson = new JSONObject();
					for (Map.Entry<String, String> ee : v.entrySet()) {
						cchildjson.put(ee.getKey(), ee.getValue());
					}
					childjson.put(e.getKey(), cchildjson);
				}
				json.put("DriverConfig", childjson);
			}
			return json;
		}

		/** 是否使用目标中的数据填充卷 */
		public Boolean getNoCopy() {
			return NoCopy;
		}

		/** 用户定义的元信息 key/value值 */
		public Map<String, String> getLabels() {
			return Labels;
		}

		/** 驱动程序特定选项的映射 */
		public Map<String, Map<String, String>> getDriverConfig() {
			return DriverConfig;
		}
	}

	public static class TmpfsOptionsConfigurantion {
		/** tmpfs 装载的大小以字节为单位。 */
		private Integer SizeBytes;
		/** mpfs 的权限模式值 */
		private Integer Mode;

		public TmpfsOptionsConfigurantion(Integer sizeBytes, Integer mode) {
			SizeBytes = sizeBytes;
			Mode = mode;
		}

		public static TmpfsOptionsConfigurantion fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new TmpfsOptionsConfigurantion(json.optInt("SizeBytes"), json.optInt("Mode"));
		}

		public static JSONObject toJson(TmpfsOptionsConfigurantion tmpfs) {
			if (null == tmpfs) {
				return null;
			}
			JSONObject json = new JSONObject();
			Util.addIfNotNull(json, "SizeBytes", tmpfs.SizeBytes);
			Util.addIfNotNull(json, "Mode", tmpfs.Mode);
			return json;
		}

		public Integer getSizeBytes() {
			return SizeBytes;
		}

		public Integer getMode() {
			return Mode;
		}

	}

	/**
	 * 网络配置
	 * 
	 * @author daibo
	 *
	 */
	public static class NetworkingConfigurantion {
		/** 端点配置 */
		private Map<String, EndpointsConfigurantion> EndpointsConfig;

		public NetworkingConfigurantion(Map<String, EndpointsConfigurantion> endpointsConfig) {
			EndpointsConfig = endpointsConfig;
		}

		public Map<String, EndpointsConfigurantion> getEndpointsConfig() {
			return EndpointsConfig;
		}

		public static JSONObject toJson(NetworkingConfigurantion config) {
			if (null == config) {
				return null;
			}
			JSONObject json = new JSONObject();
			if (null != config.EndpointsConfig) {
				for (Map.Entry<String, EndpointsConfigurantion> e : config.EndpointsConfig.entrySet()) {
					EndpointsConfigurantion cc = e.getValue();
					JSONObject child = new JSONObject();
					if (null != cc) {
						if (null != cc.IPAMConfig) {
							JSONObject cchild = new JSONObject();
							Util.addIfNotNull(cchild, "IPv4Address", cc.IPAMConfig.IPv4Address);
							Util.addIfNotNull(cchild, "IPv6Address", cc.IPAMConfig.IPv6Address);
							Util.addIfNotNull(cchild, "LinkLocalIPs", Util.toJSONArray(cc.IPAMConfig.LinkLocalIPs));
							child.put("IPAMConfig", cchild);
						}
						Util.addIfNotNull(child, "Links", Util.toJSONArray(cc.Links));
						Util.addIfNotNull(child, "Aliases", Util.toJSONArray(cc.Aliases));
						Util.addIfNotNull(child, "NetworkID", cc.NetworkID);
						Util.addIfNotNull(child, "EndpointID", cc.EndpointID);
						Util.addIfNotNull(child, "Gateway", cc.Gateway);
						Util.addIfNotNull(child, "IPAddress", cc.IPAddress);
						Util.addIfNotNull(child, "IPPrefixLen", cc.IPPrefixLen);
						Util.addIfNotNull(child, "IPv6Gateway", cc.IPv6Gateway);
						Util.addIfNotNull(child, "GlobalIPv6Address", cc.GlobalIPv6Address);
						Util.addIfNotNull(child, "GlobalIPv6PrefixLen", cc.GlobalIPv6PrefixLen);
						Util.addIfNotNull(child, "MacAddress", cc.MacAddress);
						Util.addIfNotNull(child, "DriverOpts", Util.toJSONObject(cc.DriverOpts));
					}
					json.put(e.getKey(), child);
				}
			}
			return json;
		}
	}

	/**
	 * 端点配置
	 * 
	 * @author daibo
	 *
	 */
	public static class EndpointsConfigurantion {
		/** IPAM配置 */
		private IPAMConfig IPAMConfig;
		/** 链接 */
		private String[] Links;
		/** 别名 */
		private String[] Aliases;
		/** 网络id */
		private String NetworkID;
		/** 端点id */
		private String EndpointID;
		/** 网关 */
		private String Gateway;
		/** IPv4地址 */
		private String IPAddress;
		/** IPv4地址掩码长度 */
		private int IPPrefixLen;
		/** IPv6网关 */
		private String IPv6Gateway;
		/** 全局IPv6地址 */
		private String GlobalIPv6Address;
		/** 全局IPv6地址掩码长度 */
		private int GlobalIPv6PrefixLen;
		/** mac地址 */
		private String MacAddress;
		/** 驱动程序Opts 是驱动程序选项和值的映射。这些选项直接传递给驱动程序,并且特定于驱动程序。 */
		private Map<String, String> DriverOpts;

		public EndpointsConfigurantion() {

		}

		public EndpointsConfigurantion(JSONObject json) {
			{
				JSONObject obj = json.optJSONObject("IPAMConfig");
				if (null != obj) {
					IPAMConfig = new IPAMConfig(obj);
				}
			}
			Links = Util.toStringArray(json.optJSONArray("Links"));
			Aliases = Util.toStringArray(json.optJSONArray("Aliases"));
			NetworkID = json.optString("NetworkID");
			EndpointID = json.optString("NetworkID");
			Gateway = json.optString("Gateway");
			IPAddress = json.optString("IPAddress");
			IPPrefixLen = json.optInt("IPPrefixLen");
			IPv6Gateway = json.optString("IPv6Gateway");
			GlobalIPv6Address = json.optString("GlobalIPv6Address");
			GlobalIPv6PrefixLen = json.optInt("GlobalIPv6PrefixLen");
			MacAddress = json.optString("MacAddress");
			DriverOpts = Util.toStringMap(json.optJSONObject("DriverOpts"));
		}

		public IPAMConfig getIPAMConfig() {
			return IPAMConfig;
		}

		public void setIPAMConfig(IPAMConfig iPAMConfig) {
			IPAMConfig = iPAMConfig;
		}

		public String[] getLinks() {
			return Links;
		}

		public void setLinks(String[] links) {
			Links = links;
		}

		public String[] getAliases() {
			return Aliases;
		}

		public void setAliases(String[] aliases) {
			Aliases = aliases;
		}

		public String getNetworkID() {
			return NetworkID;
		}

		public void setNetworkID(String networkID) {
			NetworkID = networkID;
		}

		public String getEndpointID() {
			return EndpointID;
		}

		public void setEndpointID(String endpointID) {
			EndpointID = endpointID;
		}

		public String getGateway() {
			return Gateway;
		}

		public void setGateway(String gateway) {
			Gateway = gateway;
		}

		public String getIPAddress() {
			return IPAddress;
		}

		public void setIPAddress(String iPAddress) {
			IPAddress = iPAddress;
		}

		public int getIPPrefixLen() {
			return IPPrefixLen;
		}

		public void setIPPrefixLen(int iPPrefixLen) {
			IPPrefixLen = iPPrefixLen;
		}

		public String getIPv6Gateway() {
			return IPv6Gateway;
		}

		public void setIPv6Gateway(String iPv6Gateway) {
			IPv6Gateway = iPv6Gateway;
		}

		public String getGlobalIPv6Address() {
			return GlobalIPv6Address;
		}

		public void setGlobalIPv6Address(String globalIPv6Address) {
			GlobalIPv6Address = globalIPv6Address;
		}

		public int getGlobalIPv6PrefixLen() {
			return GlobalIPv6PrefixLen;
		}

		public void setGlobalIPv6PrefixLen(int globalIPv6PrefixLen) {
			GlobalIPv6PrefixLen = globalIPv6PrefixLen;
		}

		public String getMacAddress() {
			return MacAddress;
		}

		public void setMacAddress(String macAddress) {
			MacAddress = macAddress;
		}

		public Map<String, String> getDriverOpts() {
			return DriverOpts;
		}

		public void setDriverOpts(Map<String, String> driverOpts) {
			DriverOpts = driverOpts;
		}

	}

	/**
	 * IPAM配置
	 * 
	 * @author daibo
	 *
	 */
	public static class IPAMConfig {
		/** IPv4地址 */
		private String IPv4Address;
		/** IPv6地址 */
		private String IPv6Address;
		/** 本地链接地址 */
		private String[] LinkLocalIPs;

		public IPAMConfig(String ipv4Address, String ipv6Address, String[] linkLocalIps) {
			IPv4Address = ipv4Address;
			IPv6Address = ipv6Address;
			LinkLocalIPs = linkLocalIps;
		}

		public IPAMConfig(JSONObject json) {
			IPv4Address = json.optString("IPv4Address");
			IPv6Address = json.optString("IPv6Address");
			LinkLocalIPs = Util.toStringArray(json.optJSONArray("LinkLocalIPs"));
		}

		/** IPv4地址 */
		public String getIPv4Address() {
			return IPv4Address;
		}

		/** IPv6地址 */
		public String getIPv6Address() {
			return IPv6Address;
		}

		/** 本地链接地址 */
		public String[] getLinkLocalIPs() {
			return LinkLocalIPs;
		}

	}

	/***
	 * 运行run的结果
	 * 
	 * @author daibo
	 *
	 */
	public static class Result {
		/** 容器id */
		private String Id;
		/** 警告 */
		private String[] Warnings;

		public Result(JSONObject json) {
			Id = json.optString("Id");
			Warnings = Util.toStringArray(json.optJSONArray("Warnings"));
		}

		/** 容器id */
		public String getId() {
			return Id;
		}

		/** 警告 */
		public String[] getWarnings() {
			return Warnings;
		}
	}

}
