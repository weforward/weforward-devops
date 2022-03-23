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

/**
 * docker检查
 * 
 * @author daibo
 *
 */
/**
 * @author daibo
 *
 */
public class DockerInspect {
	/** 容器id */
	private String Id;
	/** 创建时间 */
	private String Created;
	/** 正在运行的命令的路径 */
	private String Path;
	/** 正在运行的命令的参数 */
	private String[] Args;
	/** 容器状态 */
	private InspectState State;
	/** 容器静态 */
	private String Image;
	/** resolv配置路径 */
	private String ResolvConfPath;
	/** hostname文件路径 */
	private String HostnamePath;
	/** hosts文件路径 */
	private String HostsPath;
	/** 日志文件路径 */
	private String LogPath;
	/** 容器名 */
	private String Name;
	/** 重启次数 */
	private int RestartCount;

	private String Driver;
	/** 平台 */
	private String Platform;

	private String MountLabel;

	private String ProcessLabel;

	private String AppArmorProfile;

	private String ExecIDs;
	/** 主机配置 */
	private DockerRun.HostConfigurantion HostConfig;
	/*** 有关容器的图形驱动程序的信息 */
	private InspectGraphDriver GraphDriver;
	/** 此容器创建或更改的文件的大小 */
	private int SizeRw;
	/** 此容器中所有文件的总大小 */
	private int SizeRootFs;
	/** 挂载点 */
	private DockerContainer.Mount[] Mounts;
	/** 在主机之间可移植的容器的配置 */
	private InspectConfig Config;
	/** 网络设置公开 API 中的网络设置 */
	private InspectNetworkSettings NetworkSettings;

	public DockerInspect(JSONObject json) {
		Id = json.optString("Id");
		Created = json.optString("Created");
		Path = json.optString("Path");
		Args = Util.toStringArray(json.optJSONArray("Args"));
		{
			JSONObject obj = json.optJSONObject("State");
			if (null != obj) {
				State = new InspectState(obj);
			}
		}
		Image = json.optString("Image");
		ResolvConfPath = json.optString("ResolvConfPath");
		HostnamePath = json.optString("HostnamePath");
		HostsPath = json.optString("HostsPath");
		LogPath = json.optString("LogPath");
		Name = json.optString("Name");
		RestartCount = json.optInt("RestartCount");
		Driver = json.optString("Driver");
		Platform = json.optString("Platform");
		MountLabel = json.optString("MountLabel");
		ProcessLabel = json.optString("ProcessLabel");
		AppArmorProfile = json.optString("AppArmorProfile");
		ExecIDs = json.optString("ExecIDs");
		{
			JSONObject obj = json.optJSONObject("HostConfig");
			if (null != obj) {
				HostConfig = new DockerRun.HostConfigurantion(obj);
			}
		}
		{
			JSONObject obj = json.optJSONObject("GraphDriver");
			if (null != obj) {
				GraphDriver = new InspectGraphDriver(obj);
			}
		}
		SizeRw = json.optInt("SizeRw");
		SizeRootFs = json.optInt("SizeRootFs");
		{
			JSONArray array = json.optJSONArray("Mounts");
			if (null != array) {
				Mounts = new DockerContainer.Mount[array.length()];
				for (int i = 0; i < Mounts.length; i++) {
					Mounts[i] = new DockerContainer.Mount(array.getJSONObject(i));
				}
			}
		}
		{
			JSONObject obj = json.optJSONObject("Config");
			if (null != obj) {
				Config = new InspectConfig(obj);
			}
		}
		{
			JSONObject obj = json.optJSONObject("NetworkSettings");
			if (null != obj) {
				NetworkSettings = new InspectNetworkSettings(obj);
			}
		}
	}

	public String getId() {
		return Id;
	}

	public String getCreated() {
		return Created;
	}

	public String getPath() {
		return Path;
	}

	public String[] getArgs() {
		return Args;
	}

	public InspectState getState() {
		return State;
	}

	public String getImage() {
		return Image;
	}

	public String getResolvConfPath() {
		return ResolvConfPath;
	}

	public String getHostnamePath() {
		return HostnamePath;
	}

	public String getHostsPath() {
		return HostsPath;
	}

	public String getLogPath() {
		return LogPath;
	}

	public String getName() {
		return Name;
	}

	public int getRestartCount() {
		return RestartCount;
	}

	public String getDriver() {
		return Driver;
	}

	public String getPlatform() {
		return Platform;
	}

	public String getMountLabel() {
		return MountLabel;
	}

	public String getProcessLabel() {
		return ProcessLabel;
	}

	public String getAppArmorProfile() {
		return AppArmorProfile;
	}

	public String getExecIDs() {
		return ExecIDs;
	}

	public DockerRun.HostConfigurantion getHostConfig() {
		return HostConfig;
	}

	public InspectGraphDriver getGraphDriver() {
		return GraphDriver;
	}

	public int getSizeRw() {
		return SizeRw;
	}

	public int getSizeRootFs() {
		return SizeRootFs;
	}

	public DockerContainer.Mount[] getMounts() {
		return Mounts;
	}

	public InspectConfig getConfig() {
		return Config;
	}

	public InspectNetworkSettings getNetworkSettings() {
		return NetworkSettings;
	}

	@Override
	public String toString() {
		return "{ \"Id\":\"" + Id + "\" , \"Name\":\"" + Name + "\" }";
	}

	public static class InspectState {
		/**
		 * 容器状态
		 * 
		 * 有效值："created" "running" "paused" "restarting" "removing" "exited""dead"
		 */
		private String Status;
		/** 是否运行中 */
		private boolean Running;
		/** 是否已暂停 */
		private boolean Paused;
		/** 是否重启中 */
		private boolean Restarting;
		/** 这个容器是否因为内存耗尽而被杀死 */
		private boolean OOMKilled;
		/** 这个容器是否死了 */
		private boolean Dead;
		/** 此容器的进程 ID */
		private int Pid;
		/** 此容器的最后一个退出代码 */
		private int ExitCode;
		/** 错误信息 */
		private String Error;
		/** 上次启动此容器的时间 */
		private String StartedAt;
		/** 此容器上次退出的时间 */
		private String FinishedAt;

		public InspectState(JSONObject json) {
			Status = json.optString("Status");
			Running = json.optBoolean("Running");
			Paused = json.optBoolean("Paused");
			Restarting = json.optBoolean("Restarting");
			OOMKilled = json.optBoolean("OOMKilled");
			Dead = json.optBoolean("Dead");
			Pid = json.optInt("Pid");
			ExitCode = json.optInt("ExitCode");
			Error = json.optString("Error");
			StartedAt = json.optString("StartedAt");
			FinishedAt = json.optString("FinishedAt");
		}

		public String getStatus() {
			return Status;
		}

		public boolean isRunning() {
			return Running;
		}

		public boolean isPaused() {
			return Paused;
		}

		public boolean isRestarting() {
			return Restarting;
		}

		public boolean isOOMKilled() {
			return OOMKilled;
		}

		public boolean isDead() {
			return Dead;
		}

		public int getPid() {
			return Pid;
		}

		public int getExitCode() {
			return ExitCode;
		}

		public String getError() {
			return Error;
		}

		public String getStartedAt() {
			return StartedAt;
		}

		public String getFinishedAt() {
			return FinishedAt;
		}

	}

	public static class InspectGraphDriver {
		private Map<String, String> Data;
		private String Name;

		public InspectGraphDriver(JSONObject json) {
			Data = Util.toStringMap(json.optJSONObject("Data"));
			Name = json.optString("Name");
		}

		public Map<String, String> getData() {
			return Data;
		}

		public String getName() {
			return Name;
		}
	}

	public static class InspectConfig {
		/** 要用于容器的主机名,有效的 RFC 1123 主机名。 */
		private String Hostname;
		/** 要用于容器的域名 */
		private String Domainname;
		/** 容器内运行命令的用户 */
		private String User;
		/** 是否启用标准输入 */
		private boolean AttachStdin;
		/** 是否启用标准输出 */
		private boolean AttachStdout;
		/** 是否启用标准错误 */
		private boolean AttachStderr;
		/** 端口映射 {"<port>/<tcp|udp|sctp>": {}} */
		private Map<String, URI> ExposedPorts;
		/** 是否将标准流附加到TTY,包括未关闭的标准输入 */
		private boolean Tty;
		/** 是否打开标准输入 */
		private boolean OpenStdin;
		/** 是否在一个客户端链接断开后就关闭标准输入 */
		private boolean StdinOnce;
		/** 环境变量 如["FOO=bar","BAZ=quux"]，如果要删除环境变量，则不带=，如["FOO","BAZ"] */
		private String[] Env;
		/** 容器运行的命令 */
		private String[] Cmd;
		/** 健康检查 */
		private DockerRun.HealthConfig Healthcheck;
		/** 参数是否已转义(Windows有效) */
		private boolean ArgsEscaped;
		/** 容器的镜像 */
		private String Image;
		/** 存储卷映射 */
		private Map<String, DockerRun.Volume> Volumes;
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

		public InspectConfig(JSONObject json) {
			Hostname = json.optString("Hostname");
			Domainname = json.optString("Domainname");
			User = json.optString("User");
			AttachStdin = json.optBoolean("AttachStdin");
			AttachStdout = json.optBoolean("AttachStdout");
			AttachStderr = json.optBoolean("AttachStderr");
			Tty = json.optBoolean("Tty");
			OpenStdin = json.optBoolean("OpenStdin");
			StdinOnce = json.optBoolean("StdinOnce");
			Env = Util.toStringArray(json.optJSONArray("Env"));
			Cmd = Util.toStringArray(json.optJSONArray("Cmd"));
			{
				JSONObject obj = json.optJSONObject("ExposedPorts");
				if (null != obj) {
					ExposedPorts = new HashMap<>();
					for (String key : obj.keySet()) {
						JSONObject cobj = obj.getJSONObject(key);
						URI value = URI
								.create("docker://" + cobj.optString("HostIp") + ":" + cobj.optString("HostPort"));
						ExposedPorts.put(key, value);
					}
				}

			}
			ArgsEscaped = json.optBoolean("ArgsEscaped");

			{
				JSONObject obj = json.optJSONObject("ExposedPorts");
				if (null != obj) {
					Healthcheck = new DockerRun.HealthConfig(obj);
				}
			}
			Image = json.optString("Image");
			{
				JSONObject obj = json.optJSONObject("ExposedPorts");
				if (null != obj) {
					Volumes = new HashMap<>();
					for (String key : obj.keySet()) {
						DockerRun.Volume value = new DockerRun.Volume(obj.getJSONObject(key));
						Volumes.put(key, value);
					}
				}
			}
			WorkingDir = json.optString("WorkingDir");
			Entrypoint = Util.toStringArray(json.optJSONArray("Entrypoint"));
			NetworkDisabled = json.optBoolean("NetworkDisabled");
			MacAddress = json.optString("MacAddress");
			OnBuild = Util.toStringArray(json.optJSONArray("OnBuild"));
			Labels = Util.toStringMap(json.optJSONObject("Labels"));
			StopSignal = json.optString("StopSignal");
			StopTimeout = json.optInt("StopTimeout");
			Shell = Util.toStringArray(json.optJSONArray("Shell"));
		}

		public String getHostname() {
			return Hostname;
		}

		public String getDomainname() {
			return Domainname;
		}

		public String getUser() {
			return User;
		}

		public boolean isAttachStdin() {
			return AttachStdin;
		}

		public boolean isAttachStdout() {
			return AttachStdout;
		}

		public boolean isAttachStderr() {
			return AttachStderr;
		}

		public Map<String, URI> getExposedPorts() {
			return ExposedPorts;
		}

		public boolean isTty() {
			return Tty;
		}

		public boolean isOpenStdin() {
			return OpenStdin;
		}

		public boolean isStdinOnce() {
			return StdinOnce;
		}

		public String[] getEnv() {
			return Env;
		}

		public String[] getCmd() {
			return Cmd;
		}

		public DockerRun.HealthConfig getHealthcheck() {
			return Healthcheck;
		}

		public boolean isArgsEscaped() {
			return ArgsEscaped;
		}

		public String getImage() {
			return Image;
		}

		public Map<String, DockerRun.Volume> getVolumes() {
			return Volumes;
		}

		public String getWorkingDir() {
			return WorkingDir;
		}

		public String[] getEntrypoint() {
			return Entrypoint;
		}

		public Boolean getNetworkDisabled() {
			return NetworkDisabled;
		}

		public String getMacAddress() {
			return MacAddress;
		}

		public String[] getOnBuild() {
			return OnBuild;
		}

		public Map<String, String> getLabels() {
			return Labels;
		}

		public String getStopSignal() {
			return StopSignal;
		}

		public Integer getStopTimeout() {
			return StopTimeout;
		}

		public String[] getShell() {
			return Shell;
		}

	}

	public static class InspectNetworkSettings {
		/** 网桥网络名称 */
		private String Bridge;
		/** 沙盒ID唯一表示容器的网络堆栈 */
		private String SandboxID;
		/** 指示是否应在虚拟接口上启用发夹 NAT */
		private boolean HairpinMode;
		/** IPv6 单播地址,使用链接本地前缀 */
		private String LinkLocalIPv6Address;
		/** IPv6 单播地址的前缀长度 */
		private int LinkLocalIPv6PrefixLen;
		/**
		 * 描述将容器端口映射到主机端口,使用容器的端口号和协议作为格式中的键<port>/<protocol>,如, 80/udp
		 * 如果为多个协议映射容器的端口,则将单独的条目添加到映射表中。
		 */
		private Map<String, URI[]> Ports;
		/** 沙盒密钥标识沙盒 */
		private String SandboxKey;

		private InspectIPaddress[] SecondaryIPAddresses;

		private InspectIPaddress[] SecondaryIPv6Addresses;

		private Map<String, DockerRun.EndpointsConfigurantion> Networks;

		public InspectNetworkSettings(JSONObject json) {

			Bridge = json.optString("Bridge");
			SandboxID = json.optString("SandboxID");
			HairpinMode = json.optBoolean("HairpinMode");
			LinkLocalIPv6Address = json.optString("LinkLocalIPv6Address");
			LinkLocalIPv6PrefixLen = json.optInt("json");
			{
				JSONObject obj = json.optJSONObject("Ports");
				if (null != obj) {
					Ports = new HashMap<>();
					for (String key : obj.keySet()) {
						JSONArray arr = obj.getJSONArray(key);
						URI[] uris = new URI[arr.length()];
						for (int i = 0; i < arr.length(); i++) {
							JSONObject cobj = arr.getJSONObject(i);
							uris[i] = URI
									.create("docker://" + cobj.optString("HostIp") + ":" + cobj.optString("HostPort"));
						}
						Ports.put(key, uris);

					}
				}
			}
			SandboxKey = json.optString("SandboxKey");
			{
				JSONArray arr = json.optJSONArray("SecondaryIPAddresses");
				if (null != arr) {
					SecondaryIPAddresses = new InspectIPaddress[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						SecondaryIPAddresses[i] = new InspectIPaddress(arr.getJSONObject(i));
					}
				}
			}
			{
				JSONArray arr = json.optJSONArray("SecondaryIPv6Addresses");
				if (null != arr) {
					SecondaryIPv6Addresses = new InspectIPaddress[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						SecondaryIPAddresses[i] = new InspectIPaddress(arr.getJSONObject(i));
					}
				}
			}
			{
				JSONObject obj = json.optJSONObject("Networks");
				if (null != obj) {
					Networks = new HashMap<>();
					for (String key : obj.keySet()) {
						DockerRun.EndpointsConfigurantion value = new DockerRun.EndpointsConfigurantion(
								obj.getJSONObject(key));
						Networks.put(key, value);
					}

				}
			}
		}

		public String getBridge() {
			return Bridge;
		}

		public String getSandboxID() {
			return SandboxID;
		}

		public boolean isHairpinMode() {
			return HairpinMode;
		}

		public String getLinkLocalIPv6Address() {
			return LinkLocalIPv6Address;
		}

		public int getLinkLocalIPv6PrefixLen() {
			return LinkLocalIPv6PrefixLen;
		}

		public Map<String, URI[]> getPorts() {
			return Ports;
		}

		public String getSandboxKey() {
			return SandboxKey;
		}

		public InspectIPaddress[] getSecondaryIPAddresses() {
			return SecondaryIPAddresses;
		}

		public InspectIPaddress[] getSecondaryIPv6Addresses() {
			return SecondaryIPv6Addresses;
		}

		public Map<String, DockerRun.EndpointsConfigurantion> getNetworks() {
			return Networks;
		}

	}

	public static class InspectIPaddress {
		/** Ip地址 */
		private String Addr;
		/** IP 地址的掩码长度 */
		private int PrefixLen;

		public InspectIPaddress(JSONObject json) {
			Addr = json.optString("Addr");
			PrefixLen = json.optInt("PrefixLen");
		}

		public String getAddr() {
			return Addr;
		}

		public int getPrefixLen() {
			return PrefixLen;
		}
	}

}
