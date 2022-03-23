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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.weforward.util.docker.ext.Util;

/**
 * Docker信息
 * 
 * @author daibo
 *
 */
public class DockerInfo {
	/** 守护进程唯一id */
	private String ID;
	/** 主机上的容器数 */
	private int Containers;
	/** 主机上运行中的容器数 */
	private int ContainersRunning;
	/** 主机上暂停中的容器数 */
	private int ContainersPaused;
	/** 主机上已停止的容器数 */
	private int ContainersStopped;
	/** 主机上的镜像数（包含已标记的和未标记的） */
	private int Images;
	/** 正在使用的存储驱动程序的名称 */
	private String Driver;
	/** 特定于存储驱动程序的信息 */
	private String[][] DriverStatus;
	/**
	 * 持久 Docker 状态的根目录。
	 * 
	 * Linux 默认是/var/lib/docker windows默认是 C:\ProgramData\docker
	 */
	private String DockerRootDir;
	/** 有关此节点的状态信息(独立 Swarm API) */
	private String SystemStatus;
	/** 每种类型的可用插件 */
	private Map<String, String[]> Plugins;
	/** 指示主机是否启用内存限制支持 */
	private Boolean MemoryLimit;
	/** 指示主机是否启用了内存交换限制支持 */
	private Boolean SwapLimit;
	/** 指示主机是否启用了内核内存限制支持 */
	private Boolean KernelMemory;
	/** 指示主机是否支持 CPU CFS(完全公平调度程序)周期 */
	private Boolean CpuCfsPeriod;
	/** 指示主机是否支持 CPU CFS(完全公平的调度程序)配额 */
	private Boolean CpuCfsQuota;
	/** 指示主机是否支持 CPU 共享限制 */
	private Boolean CPUShares;
	/** 指示主机是否支持 CPUsets (cpuset.cpus, cpuset.mems) */
	private Boolean CPUSet;
	/** 指示主机上是否支持 OOM 杀伤器禁用 */
	private Boolean OomKillDisable;
	/** 指示 IPv4 转发已启用 */
	private Boolean IPv4Forwarding;
	/** 指示 bridge-nf-call-iptables 在主机上是否可用 */
	private Boolean BridgeNfIptables;
	/** 指示 bridge-nf-call-ip6tables 在主机上是否可用 */
	private Boolean BridgeNfIp6tables;
	/** 指示守护进程是否在调试模式下运行 /启用了调试级日志记录 */
	private Boolean Debug;
	/***
	 * 守护进程正在使用的文件描述器的总数。
	 * 
	 * 仅当启用调试模式时,才会返回此信息。
	 */
	private int NFd;
	/**
	 * 当前存在的例程数。
	 * 
	 * 仅当启用调试模式时,才会返回此信息。
	 */
	private int NGoroutines;
	/** RFC 3339 格式的当前系统时间,纳秒 */
	private String SystemTime;
	/** 要用作新容器的默认日志记录驱动程序。 */
	private String LoggingDriver;
	/**
	 * 默认"cgroupfs" 有效值:"cgroupfs","systemd"
	 * 
	 * 用于管理组的驱动程序。
	 * 
	 */
	private String CgroupDriver;
	/** 已订阅的事件侦听器数。 */
	private int NEventsListener;
	/**
	 * 主机的内核版本。
	 * 
	 * linux上，该信息从uname获取
	 *
	 * windows上,该信息从HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\
	 * 注册值获取
	 */
	private String KernelVersion;
	/** 主机上的操作系统 如: "Ubuntu 16.04.2 LTS" or "Windows Server 2016 Datacenter" */
	private String OperatingSystem;
	/** 主机操作系统的通用类型,和go语言的GOOS返回一样 如: "linux" or "windows */
	private String OSType;
	/** 主机的硬件体系结构,和go语言的GOARCH返回一样 */
	private String Architecture;
	/**
	 * 守护进程可用的逻辑 CPU 数
	 * 
	 * 在守护进程启动时,通过查询操作系统来检查可用 CPU 的数量。启动守护进程后操作系统 CPU 分配的更改不会反映。
	 */
	private int NCPU;
	/** 主机上可用的物理内存总量(以千字节 (kB) 为单位)。 */
	private int MemTotal;
	/**
	 * 默认https://index.docker.io/v1/
	 * 
	 * 用于图像搜索的索引服务器的地址/URL,并作为 Docker中心云和 Docker云的用户身份验证的默认
	 */
	private String IndexServerAddress;
	/** 注册表ServiceConfig 存储守护进程注册表服务配置 */
	private RegistryConfigurantion RegistryConfig;
	/** 用户定义的资源 可以是整数，如SSD=3 或GPU=UUID1 */
	private GenericResource[] GenericResources;
	/** 为守护进程配置的 HTTP 代理。此值从 HTTP_PROXY 环境变量获取。容器不会自动继承此配置 */
	private String HttpProxy;
	/** 为守护进程配置的 HTTPS 代理。此值从 HTTPS_PROXY 环境变量获取。容器不会自动继承此配置 */
	private String HttpsProxy;
	/** 不使用代理。此值从NO_PROXY 环境变量获取。容器不会自动继承此配置 */
	private String NoProxy;
	/** 主机的Hostname */
	private String Name;
	/** 用户自定义标签 */
	private String[] Labels;
	/** 指示在守护进程上是否启用了实验功能。 */
	private Boolean ExperimentalBuild;
	/** 服务器版本 */
	private String ServerVersion;
	/**
	 * 分布式存储后端的 URL
	 * 
	 * 存储后端用于多主机网络(存储网络和终结点信息)和节点发现机制。
	 */
	private String ClusterStore;
	/**
	 * 引擎为节点发现目的通告的网络终结点。群集广告是host:port组合,其他主机可到达该主机上的守护进程。
	 */
	private String ClusterAdvertise;
	/**
	 * 默认值是{ "runc": { "path": "docker-runc" } }
	 * 
	 * 在守护程序上配置的符合 OCI 的运行时列表。键保存用于引用运行时的"名称"。
	 * 
	 * Docker 守护进程依赖于符合 OCI 的运行时(通过容器守护进程调用)作为 Linux 内核命名空间、c组和 SELinux 的接口。
	 * 
	 * 默认运行时是 runc,并自动配置。其他运行时可以由用户配置,并将在此处列出。
	 */
	private Map<String, Runtime> Runtimes;
	/**
	 * 默认为"runc"
	 * 
	 * 启动容器时使用的默认 OCI 运行时的名称。
	 *
	 * 可以在创建时覆盖每个容器的默认值。
	 */
	private String DefaultRuntime;
	/** 表示有关集群的一般信息。 */
	private SwarmInfo Swarm;
	/**
	 * 默认值false
	 * 
	 * 指示是否启用了实时还原。
	 * 
	 * 如果启用,则容器在守护进程关闭时保持运行;如果检测到正在运行的容器,则在守护进程启动时保持运行。
	 */
	private Boolean LiveRestoreEnabled;
	/***
	 * 默认值"default" 有效值"default" "hyperv" "process"
	 * 
	 * 表示要用作容器的默认的隔离技术。支持的值特定于平台。
	 * 
	 * 如果在守护进程启动、Windows 客户端上未指定隔离值,则默认值为 hyperv,在 Windows 服务器上,默认为process
	 * 
	 * 此选项当前未在其他平台上使用。
	 */
	private String Isolation;
	/** Docker-init 二进制文件的名称和可选路径。 */
	private String InitBinary;
	/** 从构建的Git-commit (SHA1)提交保存二进制文件,作为报告版本字符串的外部工具，如 containerd 或runc */
	private IDAndExpected ContainerdCommit;
	/** 从构建的Git-commit (SHA1)提交保存二进制文件,作为报告版本字符串的外部工具，如 containerd 或runc */
	private IDAndExpected RuncCommit;
	/** 从构建的Git-commit (SHA1)提交保存二进制文件,作为报告版本字符串的外部工具，如 containerd 或runc */
	private IDAndExpected InitCommit;
	/**
	 * 在守护程序上启用的安全功能列表,如 apparmor、seccomp、SELinux 和用户名空间(用户)
	 * 
	 * 可能存在每个安全功能的其他配置选项,并作为键/值对的逗号分隔列表包含在内。
	 */
	private String[] SecurityOptions;

	public DockerInfo(JSONObject json) {
		ID = json.optString("ID");
		Containers = json.optInt("Containers");
		ContainersRunning = json.optInt("ContainersRunning");
		ContainersPaused = json.optInt("ContainersPaused");
		ContainersStopped = json.optInt("ContainersStopped");
		Images = json.optInt("Images");
		Driver = json.optString("Driver");
		Object driverStatusObj = json.opt("DriverStatus");
		if (driverStatusObj instanceof JSONArray) {
			JSONArray jarr = (JSONArray) driverStatusObj;
			String[][] arr = new String[jarr.length()][];
			for (int i = 0; i < jarr.length(); i++) {
				JSONArray childarray = jarr.getJSONArray(i);
				String[] childarr = new String[childarray.length()];
				for (int j = 0; j < childarray.length(); j++) {
					childarr[j] = childarray.getString(j);
				}
				arr[i] = childarr;
			}
			DriverStatus = arr;
		}
		SystemStatus = json.optString("SystemStatus");
		Object pluginsObj = json.opt("Plugins");
		if (pluginsObj instanceof JSONObject) {
			JSONObject jobj = (JSONObject) pluginsObj;
			Map<String, String[]> map = new HashMap<>();
			for (String key : jobj.keySet()) {
				JSONArray jarr = jobj.optJSONArray(key);
				String[] arr;
				if (null != jarr) {
					arr = new String[jarr.length()];
					for (int i = 0; i < jarr.length(); i++) {
						arr[i] = jarr.getString(i);
					}
				} else {
					arr = null;
				}
				map.put(key, arr);
			}
			Plugins = map;
		}
		MemoryLimit = json.getBoolean("MemoryLimit");
		SwapLimit = json.getBoolean("SwapLimit");
		KernelMemory = json.getBoolean("KernelMemory");
		CpuCfsPeriod = json.getBoolean("CpuCfsPeriod");
		CpuCfsQuota = json.getBoolean("CpuCfsQuota");
		CPUShares = json.getBoolean("CPUShares");
		CPUSet = json.getBoolean("CPUSet");
		IPv4Forwarding = json.getBoolean("IPv4Forwarding");
		BridgeNfIptables = json.getBoolean("BridgeNfIptables");
		BridgeNfIp6tables = json.getBoolean("BridgeNfIp6tables");
		Debug = json.getBoolean("Debug");
		NFd = json.optInt("NFd");
		OomKillDisable = json.getBoolean("MemoryLimit");
		NGoroutines = json.optInt("NGoroutines");
		SystemTime = json.optString("SystemTime");
		LoggingDriver = json.optString("LoggingDriver");
		CgroupDriver = json.optString("CgroupDriver");
		NEventsListener = json.optInt("NEventsListener");
		KernelVersion = json.optString("KernelVersion");
		OperatingSystem = json.optString("OperatingSystem");
		OSType = json.optString("OSType");
		Architecture = json.optString("Architecture");
		IndexServerAddress = json.optString("IndexServerAddress");
		NCPU = json.optInt("NCPU");
		MemTotal = json.optInt("MemTotal");
		DockerRootDir = json.optString("DockerRootDir");
		HttpProxy = json.optString("HttpProxy");
		HttpsProxy = json.optString("HttpsProxy");
		NoProxy = json.optString("NoProxy");
		Name = json.optString("Name");
		JSONArray jarr = json.getJSONArray("Labels");
		Labels = new String[jarr.length()];
		for (int i = 0; i < jarr.length(); i++) {
			Labels[i] = jarr.optString(i);
		}
		ExperimentalBuild = json.optBoolean("ExperimentalBuild");
		ServerVersion = json.optString("ServerVersion");
		ClusterStore = json.optString("ClusterStore");
		ClusterAdvertise = json.optString("ClusterAdvertise");
		// private Map<String, Object> Runtimes;
		DefaultRuntime = json.optString("DefaultRuntime");
		JSONObject jobj = json.optJSONObject("Swarm");
		if (null != jobj) {
			Swarm = new SwarmInfo(jobj);
		}
		LiveRestoreEnabled = json.optBoolean("ExperimentalBuild");
		Isolation = json.optString("Isolation");
		InitBinary = json.optString("InitBinary");
		jobj = json.optJSONObject("ContainerdCommit");
		if (null != jobj) {
			ContainerdCommit = new IDAndExpected(jobj);
		}
		jobj = json.optJSONObject("RuncCommit");
		if (null != jobj) {
			RuncCommit = new IDAndExpected(jobj);
		}
		jobj = json.optJSONObject("InitCommit");
		if (null != jobj) {
			InitCommit = new IDAndExpected(jobj);
		}
		jarr = json.getJSONArray("SecurityOptions");
		SecurityOptions = new String[jarr.length()];
		for (int i = 0; i < jarr.length(); i++) {
			SecurityOptions[i] = jarr.optString(i);
		}
		jobj = json.optJSONObject("RegistryConfig");
		if (null != jobj) {
			RegistryConfig = new RegistryConfigurantion(jobj);
		}
		jarr = json.optJSONArray("GenericResources");
		if (null != jarr) {
			GenericResources = new GenericResource[jarr.length()];
			for (int i = 0; i < jarr.length(); i++) {
				GenericResources[i] = new GenericResource(jarr.getJSONObject(i));
			}
		}
		jobj = json.optJSONObject("Runtimes");
		if (null != jobj) {
			Runtimes = new HashMap<>();
			for (String key : jobj.keySet()) {
				JSONObject child = jobj.optJSONObject(key);
				if (null == child) {
					continue;
				}
				Runtimes.put(key, new Runtime(child));
			}
		}
	}

	public String getID() {
		return ID;
	}

	public int getContainers() {
		return Containers;
	}

	public int getContainersRunning() {
		return ContainersRunning;
	}

	public int getContainersPaused() {
		return ContainersPaused;
	}

	public int getContainersStopped() {
		return ContainersStopped;
	}

	public int getImages() {
		return Images;
	}

	public String getDriver() {
		return Driver;
	}

	public String[][] getDriverStatus() {
		return DriverStatus;
	}

	public String getSystemStatus() {
		return SystemStatus;
	}

	public Map<String, String[]> getPlugins() {
		return Plugins;
	}

	public Boolean getMemoryLimit() {
		return MemoryLimit;
	}

	public Boolean getSwapLimit() {
		return SwapLimit;
	}

	public Boolean getKernelMemory() {
		return KernelMemory;
	}

	public Boolean getCpuCfsPeriod() {
		return CpuCfsPeriod;
	}

	public Boolean getCpuCfsQuota() {
		return CpuCfsQuota;
	}

	public Boolean getCPUShares() {
		return CPUShares;
	}

	public Boolean getCPUSet() {
		return CPUSet;
	}

	public Boolean getIPv4Forwarding() {
		return IPv4Forwarding;
	}

	public Boolean getBridgeNfIptables() {
		return BridgeNfIptables;
	}

	public Boolean getBridgeNfIp6tables() {
		return BridgeNfIp6tables;
	}

	public Boolean getDebug() {
		return Debug;
	}

	public int getNFd() {
		return NFd;
	}

	public Boolean getOomKillDisable() {
		return OomKillDisable;
	}

	public int getNGoroutines() {
		return NGoroutines;
	}

	public String getSystemTime() {
		return SystemTime;
	}

	public String getLoggingDriver() {
		return LoggingDriver;
	}

	public String getCgroupDriver() {
		return CgroupDriver;
	}

	public int getNEventsListener() {
		return NEventsListener;
	}

	public String getKernelVersion() {
		return KernelVersion;
	}

	public String getOperatingSystem() {
		return OperatingSystem;
	}

	public String getOSType() {
		return OSType;
	}

	public String getArchitecture() {
		return Architecture;
	}

	public String getIndexServerAddress() {
		return IndexServerAddress;
	}

	public RegistryConfigurantion getRegistryConfig() {
		return RegistryConfig;
	}

	public int getNCPU() {
		return NCPU;
	}

	public int getMemTotal() {
		return MemTotal;
	}

	public GenericResource[] getGenericResources() {
		return GenericResources;
	}

	public String getDockerRootDir() {
		return DockerRootDir;
	}

	public String getHttpProxy() {
		return HttpProxy;
	}

	public String getHttpsProxy() {
		return HttpsProxy;
	}

	public String getNoProxy() {
		return NoProxy;
	}

	public String getName() {
		return Name;
	}

	public String[] getLabels() {
		return Labels;
	}

	public Boolean getExperimentalBuild() {
		return ExperimentalBuild;
	}

	public String getServerVersion() {
		return ServerVersion;
	}

	public String getClusterStore() {
		return ClusterStore;
	}

	public String getClusterAdvertise() {
		return ClusterAdvertise;
	}

	public Map<String, Runtime> getRuntimes() {
		return Runtimes;
	}

	public String getDefaultRuntime() {
		return DefaultRuntime;
	}

	public SwarmInfo getSwarm() {
		return Swarm;
	}

	public Boolean getLiveRestoreEnabled() {
		return LiveRestoreEnabled;
	}

	public String getIsolation() {
		return Isolation;
	}

	public String getInitBinary() {
		return InitBinary;
	}

	public IDAndExpected getContainerdCommit() {
		return ContainerdCommit;
	}

	public IDAndExpected getRuncCommit() {
		return RuncCommit;
	}

	public IDAndExpected getInitCommit() {
		return InitCommit;
	}

	public String[] getSecurityOptions() {
		return SecurityOptions;
	}

	@Override
	public String toString() {
		return "{ \"ID\":\"" + ID + "\"　, \"Containers\":\"" + Containers + "\" , \"ContainersRunning\":\""
				+ ContainersRunning + "\" , \"ContainersPaused\":\"" + ContainersPaused
				+ "\" , \"ContainersStopped\":\"" + ContainersStopped + "\" ,\"Images\":\"" + Images + "\"}";
	}

	/**
	 * 注册配置
	 * 
	 * @author daibo
	 *
	 */
	public static class RegistryConfigurantion {

		private String[] AllowNondistributableArtifactsCIDRs;

		private String[] AllowNondistributableArtifactsHostnames;

		private String[] InsecureRegistryCIDRs;

		private Map<String, IndexConfigurantion> IndexConfigs;

		private String[] Mirrors;

		public RegistryConfigurantion(JSONObject json) {
			AllowNondistributableArtifactsCIDRs = Util
					.toStringArray(json.optJSONArray("AllowNondistributableArtifactsCIDRs"));
			AllowNondistributableArtifactsCIDRs = Util
					.toStringArray(json.optJSONArray("AllowNondistributableArtifactsHostnames"));
			AllowNondistributableArtifactsCIDRs = Util.toStringArray(json.optJSONArray("InsecureRegistryCIDRs"));
			JSONObject child = json.optJSONObject("IndexConfigs");
			if (null != child) {
				IndexConfigs = new HashMap<>();
				for (String key : child.keySet()) {
					JSONObject cc = child.optJSONObject(key);
					if (null == cc) {
						continue;
					}
					IndexConfigs.put(key, new IndexConfigurantion(cc));
				}
			}
			AllowNondistributableArtifactsCIDRs = Util.toStringArray(json.optJSONArray("Mirrors"));
		}

		public String[] getAllowNondistributableArtifactsCIDRs() {
			return AllowNondistributableArtifactsCIDRs;
		}

		public String[] getAllowNondistributableArtifactsHostnames() {
			return AllowNondistributableArtifactsHostnames;
		}

		public String[] getInsecureRegistryCIDRs() {
			return InsecureRegistryCIDRs;
		}

		public Map<String, IndexConfigurantion> getIndexConfigs() {
			return IndexConfigs;
		}

		public String[] getMirrors() {
			return Mirrors;
		}

	}

	/**
	 * 索引配置
	 * 
	 * @author daibo
	 *
	 */
	public static class IndexConfigurantion {

		private String Name;

		private String[] Mirrors;

		private Boolean Secure;

		private Boolean Official;

		public IndexConfigurantion(JSONObject json) {
			Name = json.optString("Name");
			Mirrors = Util.toStringArray(json.optJSONArray("Mirrors"));
			Secure = json.optBoolean("Secure");
			Official = json.optBoolean("Official");
		}

		public String getName() {
			return Name;
		}

		public String[] getMirrors() {
			return Mirrors;
		}

		public Boolean getSecure() {
			return Secure;
		}

		public Boolean getOfficial() {
			return Official;
		}

	}

	public static class GenericResource {

		private KindAndValue NamedResourceSpe;
		private KindAndValue DiscreteResourceSpec;

		public GenericResource(JSONObject json) {
			JSONObject nchild = json.optJSONObject("NamedResourceSpe");
			if (null != nchild) {
				NamedResourceSpe = new KindAndValue(nchild);
			}
			JSONObject dchild = json.optJSONObject("DiscreteResourceSpec");
			if (null != dchild) {
				DiscreteResourceSpec = new KindAndValue(dchild);
			}
		}

		public KindAndValue getNamedResourceSpe() {
			return NamedResourceSpe;
		}

		public KindAndValue getDiscreteResourceSpec() {
			return DiscreteResourceSpec;
		}
	}

	/**
	 * Kind和value
	 * 
	 * @author daibo
	 *
	 */
	public static class KindAndValue {

		private String Kind;

		private String Value;

		public KindAndValue(JSONObject json) {
			Kind = json.optString("Kind");
			Value = json.optString("Value");
		}

		public String getKind() {
			return Kind;
		}

		public String getValue() {
			return Value;
		}
	}

	/**
	 * 运行
	 * 
	 * @author daibo
	 *
	 */
	public static class Runtime {
		/**
		 * OCI 可执行二进制文件的名称和可选路径。
		 * 
		 * 如果省略路径,守护进程将搜索主机的$PATH二进制文件并使用第一个结果。
		 */
		private String path;
		/** 调用时要传递给运行时的命令行参数列表 */
		private String[] runtimeArgs;

		public Runtime(JSONObject json) {
			path = json.optString("path");
			runtimeArgs = Util.toStringArray(json.optJSONArray("runtimeArgs"));
		}

		public String getPath() {
			return path;
		}

		public String[] getRuntimeArgs() {
			return runtimeArgs;
		}
	}

	/**
	 * 集群信息
	 * 
	 * @author daibo
	 *
	 */
	public static class SwarmInfo {

		private String NodeID;

		private String NodeAddr;

		private String LocalNodeState;

		private Boolean ControlAvailable;

		private String Error;

		private RemoteManager[] RemoteManagers;

		private int Nodes;

		private int Managers;

		private ClusterInfo Cluster;

		public SwarmInfo(JSONObject json) {
			NodeID = json.optString("NodeID");
			NodeAddr = json.optString("NodeAddr");
			LocalNodeState = json.optString("LocalNodeState");
			ControlAvailable = json.optBoolean("ControlAvailable");
			Error = json.optString("Error");
			JSONArray childarr = json.optJSONArray("RemoteManagers");
			if (null != childarr) {
				RemoteManagers = new RemoteManager[childarr.length()];
				for (int i = 0; i < childarr.length(); i++) {
					RemoteManagers[i] = new RemoteManager(childarr.getJSONObject(i));
				}
			}
			Nodes = json.optInt("ControlAvailable");
			Managers = json.optInt("Managers");
			JSONObject childobj = json.optJSONObject("Cluster");
			if (null != childobj) {
				Cluster = new ClusterInfo(childobj);
			}
		}

		public String getNodeID() {
			return NodeID;
		}

		public String getNodeAddr() {
			return NodeAddr;
		}

		public String getLocalNodeState() {
			return LocalNodeState;
		}

		public Boolean getControlAvailable() {
			return ControlAvailable;
		}

		public String getError() {
			return Error;
		}

		public RemoteManager[] getRemoteManagers() {
			return RemoteManagers;
		}

		public int getNodes() {
			return Nodes;
		}

		public int getManagers() {
			return Managers;
		}

		public ClusterInfo getCluster() {
			return Cluster;
		}

	}

	/**
	 * 远程管理
	 * 
	 * @author daibo
	 *
	 */
	public static class RemoteManager {

		private String NodeID;
		private String Addr;

		public RemoteManager(JSONObject json) {
			NodeID = json.optString("NodeID");
			Addr = json.optString("Addr");
		}

		public String getNodeID() {
			return NodeID;
		}

		public String getAddr() {
			return Addr;
		}
	}

	/**
	 * 集群信息
	 * 
	 * @author daibo
	 *
	 */
	public static class ClusterInfo {

		private String ID;

		private ClusterVersion Version;

		private String CreatedAt;

		private String UpdatedAt;

		private ClusterSpec Spec;

		private ClusterTLSInfo TLSInfo;

		private boolean RootRotationInProgress;

		public ClusterInfo(JSONObject json) {
			ID = json.optString("ID");
			JSONObject vobj = json.optJSONObject("Version");
			if (null != vobj) {
				Version = new ClusterVersion(vobj);
			}
			CreatedAt = json.optString("CreatedAt");
			UpdatedAt = json.optString("UpdatedAt");
			JSONObject sobj = json.optJSONObject("Spec");
			if (null != sobj) {
				Spec = new ClusterSpec(sobj);
			}
			JSONObject tobj = json.optJSONObject("TLSInfo");
			if (null != tobj) {
				TLSInfo = new ClusterTLSInfo(tobj);
			}
			RootRotationInProgress = json.optBoolean("RootRotationInProgress");
		}

		public String getID() {
			return ID;
		}

		public ClusterVersion getVersion() {
			return Version;
		}

		public String getCreatedAt() {
			return CreatedAt;
		}

		public String getUpdatedAt() {
			return UpdatedAt;
		}

		public ClusterSpec getSpec() {
			return Spec;
		}

		public ClusterTLSInfo getTLSInfo() {
			return TLSInfo;
		}

		public boolean isRootRotationInProgress() {
			return RootRotationInProgress;
		}

	}

	public static class ClusterVersion {

		private int Index;

		public ClusterVersion(JSONObject json) {
			Index = json.optInt("Index");
		}

		public int getIndex() {
			return Index;
		}
	}

	public static class ClusterSpec {

		private String Name;

		private Map<String, String> Labels;

		private ClusterSpecOrchestration Orchestration;

		private ClusterSpecRaft Raft;

		private ClusterSpecDispatcher Dispatcher;

		private ClusterSpecCAConfig CAConfig;

		private ClusterSpecEncryptionConfig EncryptionConfig;

		private ClusterSpecTaskDefaults TaskDefaults;

		public ClusterSpec(JSONObject json) {
			Name = json.optString("Name");
			Labels = Util.toStringMap(json.optJSONObject("Labels"));
			{
				JSONObject obj = json.optJSONObject("Orchestration");
				if (null != obj) {
					Orchestration = new ClusterSpecOrchestration(obj);
				}
			}

			{
				JSONObject obj = json.optJSONObject("Raft");
				if (null != obj) {
					Raft = new ClusterSpecRaft(obj);
				}
			}
			{
				JSONObject obj = json.optJSONObject("Dispatcher");
				if (null != obj) {
					Dispatcher = new ClusterSpecDispatcher(obj);
				}
			}
			{
				JSONObject obj = json.optJSONObject("CAConfig");
				if (null != obj) {
					CAConfig = new ClusterSpecCAConfig(obj);
				}
			}
			{
				JSONObject obj = json.optJSONObject("EncryptionConfig");
				if (null != obj) {
					EncryptionConfig = new ClusterSpecEncryptionConfig(obj);
				}
			}
			{
				JSONObject obj = json.optJSONObject("TaskDefaults");
				if (null != obj) {
					TaskDefaults = new ClusterSpecTaskDefaults(obj);
				}
			}

		}

		public String getName() {
			return Name;
		}

		public Map<String, String> getLabels() {
			return Labels;
		}

		public ClusterSpecOrchestration getOrchestration() {
			return Orchestration;
		}

		public ClusterSpecRaft getRaft() {
			return Raft;
		}

		public ClusterSpecDispatcher getDispatcher() {
			return Dispatcher;
		}

		public ClusterSpecCAConfig getCAConfig() {
			return CAConfig;
		}

		public ClusterSpecEncryptionConfig getEncryptionConfig() {
			return EncryptionConfig;
		}

		public ClusterSpecTaskDefaults getTaskDefaults() {
			return TaskDefaults;
		}

	}

	public static class ClusterSpecOrchestration {

		private int TaskHistoryRetentionLimit;

		public ClusterSpecOrchestration(JSONObject json) {
			TaskHistoryRetentionLimit = json.optInt("TaskHistoryRetentionLimit");
		}

		public int getTaskHistoryRetentionLimit() {
			return TaskHistoryRetentionLimit;
		}
	}

	public static class ClusterSpecRaft {

		private int SnapshotInterval;
		private int KeepOldSnapshots;
		private int LogEntriesForSlowFollowers;
		private int ElectionTick;
		private int HeartbeatTick;

		public ClusterSpecRaft(JSONObject json) {
			SnapshotInterval = json.optInt("SnapshotInterval");
			KeepOldSnapshots = json.optInt("KeepOldSnapshots");
			LogEntriesForSlowFollowers = json.optInt("LogEntriesForSlowFollowers");
			ElectionTick = json.optInt("ElectionTick");
			HeartbeatTick = json.optInt("HeartbeatTick");
		}

		public int getSnapshotInterval() {
			return SnapshotInterval;
		}

		public int getKeepOldSnapshots() {
			return KeepOldSnapshots;
		}

		public int getLogEntriesForSlowFollowers() {
			return LogEntriesForSlowFollowers;

		}

		public int getElectionTick() {
			return ElectionTick;
		}

		public int getHeartbeatTick() {
			return HeartbeatTick;
		}
	}

	public static class ClusterSpecDispatcher {

		private int HeartbeatPeriod;

		public ClusterSpecDispatcher(JSONObject json) {
			HeartbeatPeriod = json.optInt("HeartbeatPeriod");
		}

		public int getHeartbeatPeriod() {
			return HeartbeatPeriod;
		}
	}

	public static class ClusterSpecCAConfig {

		private int NodeCertExpiry;

		private ClusterSpecCAConfigExternalCA[] ExternalCAs;

		private String SigningCACert;

		private String SigningCAKey;

		private String ForceRotate;

		public ClusterSpecCAConfig(JSONObject json) {
			NodeCertExpiry = json.optInt("NodeCertExpiry");
			JSONArray arr = json.optJSONArray("ExternalCAs");
			if (null != arr) {
				ExternalCAs = new ClusterSpecCAConfigExternalCA[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					ExternalCAs[i] = new ClusterSpecCAConfigExternalCA(arr.getJSONObject(i));
				}
			}
			SigningCACert = json.optString("SigningCACert");
			SigningCAKey = json.optString("SigningCAKey");
			ForceRotate = json.optString("ForceRotate");
		}

		public int getNodeCertExpiry() {
			return NodeCertExpiry;
		}

		public ClusterSpecCAConfigExternalCA[] getExternalCAs() {
			return ExternalCAs;
		}

		public String getSigningCACert() {
			return SigningCACert;
		}

		public String getSigningCAKey() {
			return SigningCAKey;
		}

		public String getForceRotate() {
			return ForceRotate;
		}
	}

	public static class ClusterSpecCAConfigExternalCA {

		private String Protocol;
		private String URL;
		private Map<String, String> Options;
		private String CACert;

		public ClusterSpecCAConfigExternalCA(JSONObject json) {
			Protocol = json.optString("Protocol");
			URL = json.optString("URL");
			Options = Util.toStringMap(json.optJSONObject("Options"));
			CACert = json.optString("CACert");
		}

		public String getProtocol() {
			return Protocol;
		}

		public String getURL() {
			return URL;
		}

		public Map<String, String> getOptions() {
			return Options;
		}

		public String getCACert() {
			return CACert;
		}
	}

	public static class ClusterSpecEncryptionConfig {

		private boolean AutoLockManagers;

		public ClusterSpecEncryptionConfig(JSONObject json) {
			AutoLockManagers = json.optBoolean("AutoLockManagers");
		}

		public boolean getAutoLockManagers() {
			return AutoLockManagers;
		}
	}

	public static class ClusterSpecTaskDefaults {

		private ClusterSpecTaskDefaultsLogDriver LogDriver;

		public ClusterSpecTaskDefaults(JSONObject json) {
			JSONObject child = json.optJSONObject("LogDriver");
			if (null != child) {
				LogDriver = new ClusterSpecTaskDefaultsLogDriver(child);
			}
		}

		public ClusterSpecTaskDefaultsLogDriver getLogDriver() {
			return LogDriver;
		}
	}

	public static class ClusterSpecTaskDefaultsLogDriver {

		private String Name;
		private Map<String, String> Options;

		public ClusterSpecTaskDefaultsLogDriver(JSONObject json) {
			Name = json.optString("Name");
			Options = Util.toStringMap(json.optJSONObject("Options"));
		}

		public String getName() {
			return Name;
		}

		public Map<String, String> getOptions() {
			return Options;

		}

	}

	public static class ClusterTLSInfo {

		private String TrustRoot;

		private String CertIssuerSubject;

		private String CertIssuerPublicKey;

		public ClusterTLSInfo(JSONObject json) {
			TrustRoot = json.optString("TrustRoot");
			CertIssuerSubject = json.optString("CertIssuerSubject");
			CertIssuerPublicKey = json.optString("CertIssuerPublicKey");
		}

		public String getTrustRoot() {
			return TrustRoot;
		}

		public String getCertIssuerSubject() {
			return CertIssuerSubject;
		}

		public String getCertIssuerPublicKey() {
			return CertIssuerPublicKey;
		}

	}

	/**
	 * ID和Expected
	 * 
	 * @author daibo
	 *
	 */
	public static class IDAndExpected {
		/** 外部工具的实际提交 ID */
		private String ID;
		/** 预期 */
		private String Expected;

		public IDAndExpected(JSONObject json) {
			ID = json.optString("ID");
			Expected = json.optString("Expected");
		}

		public String getID() {
			return ID;
		}

		public String getExpected() {
			return Expected;
		}

	}
}
