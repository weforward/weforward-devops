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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.weforward.util.docker.ext.Util;

/**
 * docker容器
 * 
 * @author daibo
 *
 */
public class DockerContainer {
	/** 容器id */
	private String Id;
	/** 容器名称 */
	private String[] Names;
	/** 镜像 */
	private String Image;
	/** 镜像id */
	private String ImageID;
	/** 命令 */
	private String Command;
	/** 创建时间 */
	private long Created;
	/** 端口 */
	private Port[] Ports;
	/** 标签 */
	private String[] Labels;
	/** 此容器创建或更改的文件的大小 */
	private long SizeRw;
	/** 此容器中所有文件的总大小 */
	private long SizeRootFs;
	/** 容器状态，如Exited */
	private String State;
	/** 附加更可读的状态描述，如Exit 0 */
	private String Status;
	/** 主机配置 */
	private HostConfigurantion HostConfig;
	/** 网络设置 */
	private NetworkingConfigurantion NetworkSettings;
	/** 挂载点 */
	private Mount[] Mounts;

	public DockerContainer(JSONObject json) {
		Id = json.optString("Id");
		Names = Util.toStringArray(json.getJSONArray("Names"));
		Image = json.optString("Image");
		ImageID = json.optString("ImageID");
		Command = json.optString("Command");
		Created = json.optLong("Created");

		{
			JSONArray array = json.optJSONArray("Ports");
			if (null != array) {
				Ports = new Port[array.length()];
				for (int i = 0; i < Ports.length; i++) {
					Ports[i] = new Port(array.getJSONObject(i));
				}
			}
		}

		Labels = Util.toStringArray(json.optJSONArray("Labels"));
		SizeRw = json.optLong("SizeRw");
		SizeRootFs = json.optLong("SizeRootFs");
		State = json.optString("State");
		Status = json.optString("Status");
		{
			JSONObject obj = json.optJSONObject("HostConfig");
			if (null != obj) {
				HostConfig = new HostConfigurantion(obj);
			}
		}
		{
			JSONObject obj = json.optJSONObject("NetworkSettings");
			if (null != obj) {
				NetworkSettings = new NetworkingConfigurantion(obj);
			}
		}

		{
			JSONArray array = json.optJSONArray("Mounts");
			if (null != array) {
				Mounts = new Mount[array.length()];
				for (int i = 0; i < Mounts.length; i++) {
					Mounts[i] = new Mount(array.getJSONObject(i));
				}
			}
		}

	}

	public String getId() {
		return Id;
	}

	public String[] getNames() {
		return Names;
	}

	public String getImage() {
		return Image;
	}

	public String getImageID() {
		return ImageID;
	}

	public String getCommand() {
		return Command;
	}

	public long getCreated() {
		return Created;
	}

	public Port[] getPorts() {
		return Ports;
	}

	public long getSizeRw() {
		return SizeRw;
	}

	public long getSizeRootFs() {
		return SizeRootFs;
	}

	public String[] getLabels() {
		return Labels;
	}

	public String getState() {
		return State;
	}

	public String getStatus() {
		return Status;
	}

	/** 主机配置 */
	public HostConfigurantion getHostConfig() {
		return HostConfig;
	}

	/** 网络设置 */
	public NetworkingConfigurantion getNetworkSettings() {
		return NetworkSettings;
	}

	@Override
	public String toString() {
		return Id + "," + Arrays.toString(Names);
	}

	public static class Port {
		/** ip 地址 */
		private String IP;
		/** 容器内端口 */
		private int PrivatePort;
		/** 暴露到主机的端口 */
		private int PublicPort;
		/** 类型 */
		private String Type;

		public Port(JSONObject json) {
			IP = json.optString("IP");
			PrivatePort = json.optInt("PrivatePort");
			PublicPort = json.optInt("PublicPort");
			Type = json.optString("Type");
		}

		/** ip 地址 */
		public String getIP() {
			return IP;
		}

		/** 容器内端口 */
		public int getPrivatePort() {
			return PrivatePort;
		}

		/** 暴露到主机的端口 */
		public int getPublicPort() {
			return PublicPort;
		}

		/** 类型 */
		public String getType() {
			return Type;
		}
	}

	/**
	 * 主机配置
	 * 
	 * @author daibo
	 *
	 */
	public static class HostConfigurantion {
		/** 网络模式 */
		private String NetworkMode;

		public HostConfigurantion(JSONObject json) {
			NetworkMode = json.optString("NetworkMode");
		}

		public String getNetworkMode() {
			return NetworkMode;
		}
	}

	/**
	 * 网络设置
	 * 
	 * @author daibo
	 *
	 */
	public static class NetworkingConfigurantion {

		private Map<String, Networks> Networks;

		public NetworkingConfigurantion(JSONObject json) {
			JSONObject obj = json.optJSONObject("Networks");
			Networks = new HashMap<>();
			if (null != obj) {
				for (String key : obj.keySet()) {
					Networks.put(key, new Networks(obj.getJSONObject(key)));
				}
			}
		}

		public Map<String, Networks> getNetworks() {
			return Networks;
		}
	}

	/**
	 * 网络
	 * 
	 * @author daibo
	 *
	 */
	public static class Networks {
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
		private String IPPrefixLen;
		/** IPv6网关 */
		private String IPv6Gateway;
		/** 全局IPv6地址 */
		private String GlobalIPv6Address;
		/** 全局IPv6地址掩码长度 */
		private String GlobalIPv6PrefixLen;
		/** mac地址 */
		private String MacAddress;
		/** 驱动程序Opts 是驱动程序选项和值的映射。这些选项直接传递给驱动程序,并且特定于驱动程序。 */
		private Map<String, String> DriverOpts;

		public Networks(JSONObject json) {
			JSONObject obj = json.optJSONObject("IPAMConfig");
			if (null != obj) {
				IPAMConfig = new IPAMConfig(obj);
			}
			Links = Util.toStringArray(json.optJSONArray("Links"));
			Aliases = Util.toStringArray(json.optJSONArray("Aliases"));
			NetworkID = json.optString("NetworkID");
			EndpointID = json.optString("EndpointID");
			Gateway = json.optString("Gateway");
			IPAddress = json.optString("IPAddress");
			IPPrefixLen = json.optString("IPPrefixLen");
			IPv6Gateway = json.optString("IPv6Gateway");
			GlobalIPv6Address = json.optString("GlobalIPv6Address");
			GlobalIPv6PrefixLen = json.optString("GlobalIPv6PrefixLen");
			MacAddress = json.optString("MacAddress");
			obj = json.optJSONObject("DriverOpts");
			if (null != obj) {
				DriverOpts = new HashMap<>();
				for (String key : obj.keySet()) {
					DriverOpts.put(key, obj.getString(key));
				}
			}

		}

		/** IPAM配置 */
		public IPAMConfig getIPAMConfig() {
			return IPAMConfig;
		}

		/** 链接 */
		public String[] getLinks() {
			return Links;
		}

		/** 别名 */
		public String[] getAliases() {
			return Aliases;
		}

		/** 网络id */
		public String getNetworkID() {
			return NetworkID;
		}

		/** 端点id */
		public String getEndpointID() {
			return EndpointID;
		}

		/** 网关 */
		public String getGateway() {
			return Gateway;
		}

		/** IPv4地址 */
		public String getIPAddress() {
			return IPAddress;
		}

		/** IPv4地址掩码长度 */
		public String getIPPrefixLen() {
			return IPPrefixLen;
		}

		/** IPv6网关 */
		public String getIPv6Gateway() {
			return IPv6Gateway;
		}

		/** 全局IPv6地址 */
		public String getGlobalIPv6Address() {
			return GlobalIPv6Address;
		}

		/** 全局IPv6地址掩码长度 */
		public String getGlobalIPv6PrefixLen() {
			return GlobalIPv6PrefixLen;
		}

		/** mac地址 */
		public String getMacAddress() {
			return MacAddress;
		}

		/** 驱动程序Opts 是驱动程序选项和值的映射。这些选项直接传递给驱动程序,并且特定于驱动程序。 */
		public Map<String, String> getDriverOpts() {
			return DriverOpts;
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
		/** 类型 有效值"bind" "volume" "tmpfs" */
		private String Type;
		/** 是否可读写 */
		private boolean RW;
		/** 模式 */
		private String Mode;
		/** 驱动 */
		private String Driver;
		/** 扩展模式 */
		private String Propagation;

		public Mount(JSONObject json) {
			Target = json.optString("Destination");// 文档好像有误，这里返回的是Destination
			Source = json.optString("Source");
			Type = json.optString("Type");
			// 下面是测试是返回会实际值
			RW = json.optBoolean("RW");
			Mode = json.optString("Mode");
			Driver = json.optString("Driver");
			Propagation = json.optString("Propagation");
		}

		/** 容器内路径 */
		public String getTarget() {
			return Target;
		}

		/** 挂载源 (如 一个存储券名,一个主机路径 */
		public String getSource() {
			return Source;
		}

		/** 类型 有效值"bind" "volume" "tmpfs" */
		public String getType() {
			return Type;
		}

		/** 是否可读写 */
		public boolean isRW() {
			return RW;
		}

		/** 模式 */
		public String getMode() {
			return Mode;
		}

		/** 驱动 */
		public String getDriver() {
			return Driver;
		}

		/** 扩展模式 */
		public String getPropagation() {
			return Propagation;
		}
	}

}
