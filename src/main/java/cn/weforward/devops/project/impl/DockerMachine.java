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
package cn.weforward.devops.project.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.io.BytesOutputStream;
import cn.weforward.common.util.Bytes;
import cn.weforward.common.util.ListUtil;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.data.annotation.Inherited;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.Reloadable;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Envs;
import cn.weforward.devops.project.MachineInfo;
import cn.weforward.devops.project.MachineInfo.Dist;
import cn.weforward.devops.project.OpProcessor;
import cn.weforward.devops.project.OpProcessor.Status;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.Prop;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractMachine;
import cn.weforward.devops.user.Organization;
import cn.weforward.util.CaUtil;
import cn.weforward.util.docker.DockerBuildProgesser;
import cn.weforward.util.docker.DockerClient;
import cn.weforward.util.docker.DockerContainer;
import cn.weforward.util.docker.DockerException;
import cn.weforward.util.docker.DockerImage;
import cn.weforward.util.docker.DockerImageInspect;
import cn.weforward.util.docker.DockerInspect;
import cn.weforward.util.docker.DockerInspect.InspectConfig;
import cn.weforward.util.docker.DockerInspect.InspectState;
import cn.weforward.util.docker.DockerLog;
import cn.weforward.util.docker.DockerRun;
import cn.weforward.util.docker.DockerRun.HostConfigurantion;
import cn.weforward.util.docker.DockerRun.LogConfigurantion;
import cn.weforward.util.docker.DockerRun.RestartPolicyConfigurantion;

/**
 * 基于docker的机器
 * 
 * @author daibo
 *
 */
@Inherited
public class DockerMachine extends AbstractMachine implements Reloadable<DockerMachine> {
	/** 工作空间 */
	private static final String WORKSPACE = "/wf/ms/";
	/** 用户目录 */
	private static final String USER_DIR = "/home/boot/";

	/** 项目修订版本标签 */
	private static final String REVEISION_LABEL = "revieson";
	private static final String DEFAULT_XMS = "128m";
	private static final String DEFAULT_XMX = "256m";
	private static final String DEFAULT_XSS = "256k";
	private static final String DEFAULT_MAX_DIRECT_MEMORYSIZE = "64m";
	private static final String DEFAULT_INITIATINGHEAPOCCUPANCYPERCENT = "70";
	/** 链接 */
	@Resource
	protected String m_Url;
	/** docker客户端 */
	private DockerClient m_Client;

	private Comparator<File> _BY_TIME = new Comparator<File>() {

		@Override
		public int compare(File o1, File o2) {
			long v = o1.lastModified() - o2.lastModified();
			if (v > 0) {
				return 1;
			} else if (v < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	};

	protected DockerMachine(ProjectDi di) {
		super(di);
	}

	public DockerMachine(ProjectDi di, Organization org, String name, String url) {
		super(di, org, name);
		genPersistenceId();
		m_Url = url;
		persistenceUpdateNow();
	}

	/* 客户端 */
	private DockerClient getClient() throws IOException {
		if (null == m_Client) {
			String url = m_Url;
			if (StringUtil.isEmpty(url)) {
				return null;
			}
			m_Client = new DockerClient(3, 1000, CaUtil.getDockerCertPath(), CaUtil.getDockerKeyPath(),
					CaUtil.getDockerCaPath());
			m_Client.setUrl(url);
		}
		return m_Client;
	}

	/** 链接 */
	public void setUrl(String v) {
		if (StringUtil.eq(m_Url, v)) {
			return;
		}
		m_Url = v;
		m_Client = null;
		markPersistenceUpdate();
	}

	@Override
	public List<Prop> getProps() {
		List<Prop> prop = new ArrayList<>();
		if (!StringUtil.isEmpty(m_Url)) {
			prop.add(new Prop("url", m_Url));
		}
		if (null != m_Info) {
			prop.add(new Prop("load", m_Info.getLoadAverage() / m_Info.getCpuNum() + "%"));
			prop.add(new Prop("memory", Bytes.formatHumanReadable(m_Info.getMemoryUsable()) + "/"
					+ Bytes.formatHumanReadable(m_Info.getMemoryTotal())));
			List<Dist> dists = m_Info.getDists();
			if (null != dists) {
				for (Dist d : dists) {
					prop.add(new Prop("dist " + d.getName(), Bytes.formatHumanReadable(d.getDistUsable()) + "/"
							+ Bytes.formatHumanReadable(d.getDistTotal())));
				}
			}
		}
		return prop;
	}

	/* 构建 */
	public boolean build(Running running, String revieson, OpProcessor processor) {
		Project project = running.getProject();
		Date time = VersionInfo.getTime(revieson);
		String version = VersionInfo.getVersion(revieson);
		String cname = project.getName();
		processor(processor, "开始构建项目" + cname + "/" + version);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "构建异常，获取docker客户端出错，" + e.toString());
			_Logger.error("构建异常", e);
			return false;
		}
		String t = cname + ":" + version;
		String resurl = getBusinessDi().getResourceUrl();
		String remote = resurl + "Dockerfile";
		Map<String, String> buildargs = new HashMap<>();
		String url = genUrl(project);
		buildargs.put("TZ", Envs.DEFAULT_TZ);
		buildargs.put("JAR_FILE",
				url + version + "/" + cname + ".jar?t=" + (time.getTime() / 1000) + " -O " + cname + ".jar");
		buildargs.put("USER", getAccessId(running));
		buildargs.put("PASS", getAccessKey(running));
		buildargs.put("RES_URL", resurl);
		Map<String, String> labels = new HashMap<>();
		labels.put("revieson", revieson);
		try {
			client.build(remote, buildargs, labels, t, new DockerBuildProgesserWrap(processor));
		} catch (DockerException e) {
			processor(processor, "构建异常，build出错，" + e.toString());
			_Logger.error("构建异常", e);
			return false;
		}
		processor(processor, "构建完成");
		return true;
	}

	@Override
	public synchronized void upgrade(Running running, List<Env> userenvs, List<Bind> userbinds, String revieson,
			OpProcessor processor, String note) {
		if (isOutOfMemory(getInfo())) {
			processor(processor, "升级失败，服务器内存不足");
			return;
		}
		String version = VersionInfo.getVersion(revieson);
		Project project = running.getProject();
		String cname = project.getName();
		processor(processor, "开始升级项目" + cname + "/" + version);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "升级异常，获取docker客户端出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		String image = cname + ":" + version;
		// 先看看自己有没有
		DockerImageInspect inspect;
		try {
			inspect = client.image(image);
		} catch (DockerException e) {
			processor(processor, "升级异常，查询镜像出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		boolean needBuild = note.contains("重新构建");
		if (needBuild || null == inspect || !StringUtil.eq(inspect.getLabel(REVEISION_LABEL), revieson)) {
			if (!build(running, revieson, processor)) {
				return;
			}
		}
		try {
			String oldName = cname + "-old";
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { oldName });
			List<DockerContainer> list = filter(client.ps(10, false, false, filters), oldName);
			for (DockerContainer c : list) {
				processor(processor, "清理旧备份容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				client.remove(c.getId(), false, true, null);
			}
			int t = Envs.getKillTime(this, project);
			for (int i = 0; i < t; i++) {
				list = filter(client.ps(10, false, false, filters), oldName);
				if (list.isEmpty()) {
					break;
				}
				processor(processor, "等待清理结束" + (t - i) + "s");
				synchronized (this) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			if (!list.isEmpty()) {
				processor(processor, "升级异常，无法清理旧容器");
				return;
			} else {
				processor(processor, "清理结束 ");
			}
		} catch (DockerException e) {
			processor(processor, "升级异常，清理旧容器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
			for (DockerContainer c : client.ps(100, true, false, filters)) {
				boolean match = false;
				for (String n : c.getNames()) {
					if (StringUtil.eq(n, "/" + cname)) {
						match = true;
					}
				}
				if (!match) {
					continue;
				}
				processor(processor, "停止当前容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				int t = Envs.getKillTime(this, project);
				client.stop(c.getId(), t - 60);
				DockerInspect d = null;
				for (int i = 0; i < t; i++) {
					processor(processor, "等待容器停止" + (t - i) + "s");
					d = client.inspect(c.getId(), false);
					if (!d.getState().isRunning()) {
						break;
					}
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
				if (null == d || d.getState().isRunning()) {
					processor(processor, "升级异常，无法停止并备份旧容器");
					return;
				}
				processor(processor, "备份容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				client.rename(c.getId(), cname + "-old");
			}
		} catch (DockerException e) {
			processor(processor, "升级异常，停止并备份容器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		DockerRun run = new DockerRun();
		run.setImage(image);
		List<Env> envs = new ArrayList<>();
		envs = addEnvIfNoExists(envs, userenvs);
		envs = addEnvIfNoExists(envs, project.getEnvs());
		envs = addEnvIfNoExists(envs, getEnvs());
		envs = addEnvIfNoExists(envs, getDefaultEnvs(running));
		envs = addEnvIfNoExists(envs, Collections.singletonList(new Env(Envs.PROJECT_VERSION_KEY, version)));
		String[] earr = new String[envs.size()];
		for (int i = 0; i < earr.length; i++) {
			Env e = envs.get(i);
			earr[i] = e.getKey() + "=" + e.getValue();
		}
		run.setEnv(earr);
		List<Bind> binds = new ArrayList<>();
		binds = addBindIfNoExists(binds, userbinds);
		binds = addBindIfNoExists(binds, project.getBinds());
		binds = addBindIfNoExists(binds, getBinds());
		List<String> defaultbinds = getDefaultBinds(project);
		String[] barr = new String[binds.size() + defaultbinds.size()];
		int bi = 0;
		for (; bi < defaultbinds.size(); bi++) {
			barr[bi] = defaultbinds.get(bi);
		}
		for (; bi < barr.length; bi++) {
			Bind b = binds.get(bi - defaultbinds.size());
			if (StringUtil.isEmpty(b.getMode())) {
				barr[bi] = b.getSource() + ":" + b.getTarget();
			} else {
				barr[bi] = b.getSource() + ":" + b.getTarget() + ":" + b.getMode();
			}
		}
		HostConfigurantion config = new HostConfigurantion();
		config.setBinds(barr);
		config.setNetworkMode("host");
		config.setRestartPolicy(RestartPolicyConfigurantion.UNLESS_STOPPED);
		Map<String, String> defaultLogConfig = new HashMap<>();
		defaultLogConfig.put("max-size", "100m");
		LogConfigurantion logConfig = new LogConfigurantion("json-file", defaultLogConfig);
		config.setLogConfig(logConfig);
		run.setHostConfig(config);
		DockerRun.Result r;
		try {
			r = client.run(cname, run);
			processor(processor, "运行容器" + r.getId() + "/" + Arrays.toString(r.getWarnings()));
		} catch (DockerException e) {
			processor(processor, "升级异常，运行容器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		try {
			client.start(r.getId(), null);
			processor(processor, "启动容器" + r.getId() + "成功");
		} catch (DockerException e) {
			processor(processor, "升级异常，启动容器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		processor(processor, "升级成功");
	}

	@Override
	public synchronized void rollback(Running running, OpProcessor processor) {
		Project project = running.getProject();
		String cname = project.getName();
		processor(processor, "开始回滚项目" + cname);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "回滚异常，获取docker客户端出错，" + e.toString());
			_Logger.error("回滚异常", e);
			return;
		}
		DockerContainer old = null;
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname + "-old" });
			for (DockerContainer c : client.ps(1, false, false, filters)) {
				old = c;
				break;
			}
		} catch (DockerException e) {
			processor(processor, "回滚异常，清理旧容器出错，" + e.toString());
			_Logger.error("升级异常", e);
			return;
		}
		if (null == old) {
			processor(processor, "回滚异常，没有old容器");
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname + "-bak" });
			List<DockerContainer> list = client.ps(1, false, false, filters);
			for (DockerContainer c : list) {
				processor(processor, "清理旧备份容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				client.remove(c.getId(), false, true, null);
			}
			int t = 5;
			for (int i = 0; i < t; i++) {
				list = client.ps(1, false, false, filters);
				if (list.isEmpty()) {
					break;
				}
				processor(processor, "等待清理结束" + (t - i) + "s");
				synchronized (this) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			if (!list.isEmpty()) {
				processor(processor, "回滚异常，无法清理旧容器");
				return;
			} else {
				processor(processor, "清理结束 ");
			}
		} catch (DockerException e) {
			processor(processor, "回滚异常，清理旧容器出错，" + e.toString());
			_Logger.error("回滚异常", e);
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
			for (DockerContainer c : client.ps(1, true, false, filters)) {
				processor(processor, "停止当前容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				int t = Envs.getKillTime(this, project);
				client.stop(c.getId(), t - 60);
				DockerInspect d = null;
				for (int i = 0; i < t; i++) {
					processor(processor, "等待容器停止" + (t - i) + "s");
					d = client.inspect(c.getId(), false);
					if (!d.getState().isRunning()) {
						break;
					}
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
				if (null == d || d.getState().isRunning()) {
					processor(processor, "回滚异常，无法停止并备份旧容器");
					return;
				}
				processor(processor, "备份容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				client.rename(c.getId(), cname + "-bak");
			}
		} catch (DockerException e) {
			processor(processor, "回滚异常，停止并备份容器出错，" + e.toString());
			_Logger.error("回滚异常", e);
			return;
		}
		try {
			client.rename(old.getId(), cname);
			client.start(old.getId(), null);
		} catch (DockerException e) {
			processor(processor, "回滚异常，启动备份容器出错，" + e.toString());
			_Logger.error("回滚异常", e);
			return;
		}

	}

	public void restart(Project project, OpProcessor processor) {
		String cname = project.getName();
		processor(processor, "开始重启项目" + cname);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "重启异常，获取docker客户端出错，" + e.toString());
			_Logger.error("重启异常", e);
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
			List<DockerContainer> list = filter(client.ps(100, true, false, filters), cname);
			if (list.isEmpty()) {
				processor(processor, "未找到需要重启的容器");
				return;
			}
			for (DockerContainer c : list) {
				processor(processor, "重启容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				int t = Envs.getKillTime(this, project);
				client.restart(c.getId(), t);
				DockerInspect d = null;
				for (int i = 0; i < t; i++) {
					processor(processor, "等待容器重启" + (t - i) + "s");
					d = client.inspect(c.getId(), false);
					if (d.getState().isRunning()) {
						break;
					}
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
				if (null == d || !d.getState().isRunning()) {
					processor(processor, "无法重启容器");
					continue;
				}
				processor(processor, "重启容器完成");
			}
		} catch (DockerException e) {
			processor(processor, "启动异常，停止并备份容器出错，" + e.toString());
			_Logger.error("启动异常", e);
			return;
		}
		processor(processor, "重启项目" + cname + "完成");
	}

	public void start(Project project, OpProcessor processor) {
		if (isOutOfMemory(getInfo())) {
			processor(processor, "启动失败，服务器内存不足");
			return;
		}
		String cname = project.getName();
		processor(processor, "开始启动项目" + cname);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "启动异常，获取docker客户端出错，" + e.toString());
			_Logger.error("启动异常", e);
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
			List<DockerContainer> list = filter(client.ps(100, true, false, filters), cname);
			if (list.isEmpty()) {
				processor(processor, "未找到需要启动的容器");
				return;
			}
			for (DockerContainer c : list) {
				processor(processor, "启动容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				client.start(c.getId(), null);
				processor(processor, "启动容器完成");
			}
		} catch (DockerException e) {
			processor(processor, "启动异常，停止并备份容器出错，" + e.toString());
			_Logger.error("启动异常", e);
			return;
		}
		processor(processor, "启动项目" + cname + "完成");
	}

	public void stop(Project project, OpProcessor processor) {
		String cname = project.getName();
		processor(processor, "开始停止项目" + cname);
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			processor(processor, "停止异常，获取docker客户端出错，" + e.toString());
			_Logger.error("停止异常", e);
			return;
		}
		try {
			Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
			List<DockerContainer> list = filter(client.ps(100, true, false, filters), cname);
			if (list.isEmpty()) {
				processor(processor, "未找到需要停止的容器");
				return;
			}
			for (DockerContainer c : list) {
				processor(processor, "停止容器" + c.getId() + "/" + Arrays.toString(c.getNames()));
				int t = Envs.getKillTime(this, project);
				client.stop(c.getId(), t - 60);
				DockerInspect d = null;
				for (int i = 0; i < t; i++) {
					processor(processor, "等待容器停止" + (t - i) + "s");
					d = client.inspect(c.getId(), false);
					if (!d.getState().isRunning()) {
						break;
					}
					synchronized (this) {
						try {
							this.wait(1000);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
				if (null == d || d.getState().isRunning()) {
					processor(processor, "无法停止容器");
					continue;
				}
				processor(processor, "停止容器完成");
			}
		} catch (DockerException e) {
			processor(processor, "停止异常，停止容器出错，" + e.toString());
			_Logger.error("停止异常", e);
			return;
		}
		processor(processor, "停止项目" + cname + "完成");
	}

	@Override
	public NameItem queryState(Project project) {
		try {
			DockerClient client = getClient();
			if (null == client) {
				return Running.STATE_STOPED;
			}
			DockerInspect inspert;
			try {
				inspert = client.inspect(project.getName(), false);
			} catch (DockerException e) {
				if (e.getMessage().contains("No such container")) {
					return Running.STATE_STOPED;
				} else {
					throw e;
				}
			}
			if (null == inspert) {
				return Running.STATE_STOPED;
			}
			InspectState state = inspert.getState();
			String s = state.getStatus();
			switch (state.getStatus()) {
			case "created":
				return Running.STATE_STARTING;
			case "running":
				return Running.STATE_RUNNING;
			case "paused":
				return Running.STATE_STOPED;
			case "restarting":
				return Running.STATE_STARTING;
			case "removing":
				return Running.STATE_STOPING;
			case "exited":
				return Running.STATE_STOPED;
			case "dead":
				return Running.STATE_STOPED;
			default:
				return Running.STATE_UNKNOWN.changeValue("未知状态:" + s);
			}
		} catch (IOException | DockerException e) {
			_Logger.warn("查询状态异常", e);
			return Running.STATE_UNKNOWN.changeValue("异常状态:" + e.getMessage());
		}
	}

	@Override
	public VersionInfo queryCurrentVersion(Running running) {
		DockerInspect inspert;
		Project project = running.getProject();
		try {
			DockerClient client = getClient();
			if (null == client) {
				return new VersionInfo("no version");
			}
			inspert = client.inspect(project.getName(), false);
		} catch (DockerException | IOException e) {
			return new VersionInfo("error:" + e.getMessage());
		}
		if (null == inspert) {
			return new VersionInfo("no version");
		}
		InspectConfig config = inspert.getConfig();
		if (null == config) {
			return new VersionInfo("no version");
		}
		if (null == config.getEnv()) {
			return new VersionInfo("no version");
		}
		for (String v : config.getEnv()) {
			if (v.startsWith(Envs.PROJECT_VERSION_KEY)) {
				return new VersionInfo(v.substring(Envs.PROJECT_VERSION_KEY.length() + 1));
			}
		}
		return new VersionInfo("no version");
	}

	@Override
	public ResultPage<String> queryLogs(Project project) {
		try {
			List<DockerLog> list = getClient().logs(project.getName(), true, true, Boolean.TRUE, null, null, "100");
			List<String> result = new TransList<String, DockerLog>(list) {

				@Override
				protected String trans(DockerLog src) {
					return src.toString();
				}
			};
			return ResultPageHelper.toResultPage(result);
		} catch (Throwable e) {
			throw new IllegalArgumentException("获取日志异常", e);
		}
	}

	public String queryStack(Project project) {
		try {
			String name = project.getName();
			String id = getClient().createExec(name, new String[] { "pgrep", "-f", "java" });
			String pid = getClient().exec(id);
			id = getClient().createExec(name, new String[] { "jstack", "-l", pid });
			return getClient().exec(id);
		} catch (Throwable e) {
			throw new IllegalArgumentException("获取堆栈异常", e);
		}
	}

	public String printStack(Project project) {
		try {
			String name = project.getName();
			// 获取进程id
			String id = getClient().createExec(name, new String[] { "pgrep", "-f", "java" });
			String pid = getClient().exec(id);
			// 打印堆栈
			id = getClient().createExec(name, new String[] { "jstack", "-l", pid });
			String content = getClient().exec(id);
			return content;
		} catch (Throwable e) {
			throw new IllegalArgumentException("打印堆栈异常", e);
		}
	}

	public void printStack(Project project, String filename, OpProcessor processor) {
		filename = StringUtil.isEmpty(filename) ? TimeUtil.formatCompactDateTime(new Date()) + ".txt" : filename;
		processor(processor, "开始打印" + filename + "文件");
		try {
			String name = project.getName();
			processor(processor, "开始获取进程id");
			// 获取进程id
			String id = getClient().createExec(name, new String[] { "pgrep", "-f", "java" });
			String pid = getClient().exec(id);
			processor(processor, "获取成功，进程id:" + pid);
			// 打印堆栈
			processor(processor, "开始打印堆栈");
			id = getClient().createExec(name, new String[] { "jstack", "-l", pid });
			String content = getClient().exec(id);
			processor(processor, "打印成功");
			File dir = getBusinessDi().getStackDir(this, project);
			processor(processor, "开始保存数据");
			try (FileOutputStream out = new FileOutputStream(new File(dir, filename))) {
				out.write(content.getBytes());
			}
			processor(processor, "保存成功");
			processor(processor, "打印" + filename + "文件完成");
		} catch (Throwable e) {
			processor(processor, "打印" + filename + "文件异常:" + e.getMessage());
			_Logger.error("打印堆栈异常", e);
		}
	}

	public List<File> listStacks(Project project) {
		try {
			File dir = getBusinessDi().getStackDir(this, project);
			File[] files = dir.listFiles();
			Arrays.sort(files, _BY_TIME);
			return ListUtil.reverse(Arrays.asList(files));
		} catch (Throwable e) {
			throw new IllegalArgumentException("列出堆栈异常", e);
		}
	}

	public void downStack(Project project, String filename, OutputStream out) {
		try {
			File dir = getBusinessDi().getStackDir(this, project);
			try (FileInputStream in = new FileInputStream(new File(dir, filename))) {
				BytesOutputStream.transfer(in, out, 0);
			}
		} catch (Throwable e) {
			throw new IllegalArgumentException("下载堆栈异常", e);
		}
	}

	public void printMemoryMap(Project project, String filename, OpProcessor processor) {
		String file = TimeUtil.formatCompactDateTime(new Date());
		filename = StringUtil.isEmpty(filename) ? file + ".tar" : filename;
		processor(processor, "开始打印" + filename + "文件");
		try {
			String name = project.getName();
			String tmpdir = "/tmp/memorymapdir/";
			processor(processor, "开始创建临时目录");
			// 创建目录
			String id = getClient().createExec(name, new String[] { "mkdir", "-p", tmpdir });
			getClient().exec(id);
			processor(processor, "创建临时目录成功");
			// 获取进程id
			processor(processor, "开始获取进程id");
			id = getClient().createExec(name, new String[] { "pgrep", "-f", "java" });
			String pid = getClient().exec(id);
			processor(processor, "获取成功，进程id:" + pid);

			// 打印内存
			processor(processor, "开始打印内存");
			String binfile = tmpdir + file + ".bin";
			id = getClient().createExec(name, new String[] { "jmap", "-dump:format=b,file=" + binfile, pid });
			getClient().exec(id);
			processor(processor, "打印成功");
			processor(processor, "开始保存数据");
			File dir = getBusinessDi().getMermoryMapDir(this, project);
			try (FileOutputStream out = new FileOutputStream(new File(dir, filename))) {
				getClient().archive(project.getName(), binfile, out);
			}
			processor(processor, "保存成功");

			// 清理数据
			processor(processor, "开始清理缓存数据");
			id = getClient().createExec(name, new String[] { "rm", binfile });
			getClient().exec(id);
			processor(processor, "清理成功");

			processor(processor, "打印" + filename + "文件完成");
		} catch (Throwable e) {
			processor(processor, "打印" + filename + "文件异常:" + e.getMessage());
			_Logger.error("打印堆栈异常", e);
		}
	}

	public List<File> listMemoryMaps(Project project) {
		try {
			File dir = getBusinessDi().getMermoryMapDir(this, project);
			File[] files = dir.listFiles();
			Arrays.sort(files, _BY_TIME);
			return ListUtil.reverse(Arrays.asList(files));
		} catch (Throwable e) {
			throw new IllegalArgumentException("列出内存异常", e);
		}
	}

	public void downMemoryMap(Project project, String filename, OutputStream out) {
		try {
			File dir = getBusinessDi().getMermoryMapDir(this, project);
			try (FileInputStream in = new FileInputStream(new File(dir, filename))) {
				BytesOutputStream.transfer(in, out, 0);
			}
		} catch (Throwable e) {
			throw new IllegalArgumentException("下载堆栈异常", e);
		}
	}

	/* 默认环境变量 */
	private List<Env> getDefaultEnvs(Running running) {
		Project project = running.getProject();
		List<Env> list = new ArrayList<>();
		list.add(new Env(Envs.PROJECT_NAME_KEY, project.getName()));
		list.add(new Env(Envs.RUNNING_ID_KEY, running.getPersistenceId().getId()));
		String myJavaOption = findEnvValue(project.getEnvs(), "MY_JAVA_OPTIONS", "");
		StringBuilder sb = new StringBuilder();
		sb.append(myJavaOption);
		String xms = DEFAULT_XMS;
		String xmx = DEFAULT_XMX;
		String xss = DEFAULT_XSS;
		String maxDirectMemorySize = DEFAULT_MAX_DIRECT_MEMORYSIZE;
		boolean disabelG1GC = false;
		for (Env e : project.getEnvs()) {
			if (StringUtil.eq(e.getKey(), "Xms")) {
				xms = e.getValue();
			} else if (StringUtil.eq(e.getKey(), "Xmx")) {
				xmx = e.getValue();
			} else if (StringUtil.eq(e.getKey(), "Xss")) {
				xss = e.getValue();
			} else if (StringUtil.eq(e.getKey(), "maxDirectMemorySize")) {
				maxDirectMemorySize = e.getValue();
			} else if (StringUtil.eq(e.getKey(), "disabelG1GC")) {
				disabelG1GC = "true".equalsIgnoreCase(e.getValue());
			}
		}
		if (!myJavaOption.contains("-Xms")) {
			sb.append(" -Xms");
			sb.append(xms);
		}
		if (!myJavaOption.contains("-Xmx")) {
			sb.append(" -Xmx");
			sb.append(xmx);
		}
		if (!myJavaOption.contains("-Xss")) {
			sb.append(" -Xss");
			sb.append(xss);
		}
		if (!myJavaOption.contains("-XX:MaxDirectMemorySize=")) {
			sb.append(" -XX:MaxDirectMemorySize=");
			sb.append(maxDirectMemorySize);
		}
		if (!disabelG1GC) {
			if (!myJavaOption.contains("-XX:+UseG1GC")) {
				sb.append(" -XX:+UseG1GC");
			}
			if (!myJavaOption.contains("-XX:InitiatingHeapOccupancyPercent")) {
				sb.append(" -XX:InitiatingHeapOccupancyPercent=");
				sb.append(DEFAULT_INITIATINGHEAPOCCUPANCYPERCENT);
			}
		}
		String gatewayUrl = getBusinessDi().getGatewayUrl();
		String apiUrl = getBusinessDi().getApiUrl();
		String host = getHost();
		String accessId = null;
		String accessKey = null;
		List<RunningProp> runningprops = running.getProps();
		for (RunningProp prop : runningprops) {
			if (StringUtil.eq(RunningProp.WEFORWARD_GATEWAYURL, prop.getKey())) {
				apiUrl = prop.getValue();
			} else if (StringUtil.eq(RunningProp.WEFORWARD_APIURL, prop.getKey())) {
				apiUrl = prop.getValue();
			} else if (StringUtil.eq(RunningProp.WEFORWARD_HOST, prop.getKey())) {
				host = prop.getValue();
			} else if (StringUtil.eq(RunningProp.WEFORWARD_SERVICE_ACCESS_ID, prop.getKey())) {
				accessId = prop.getValue();
			} else if (StringUtil.eq(RunningProp.WEFORWARD_SERVICE_ACCESS_KEY, prop.getKey())) {
				accessKey = prop.getValue();
			}
		}
		sb.append(" -D").append(RunningProp.WEFORWARD_GATEWAYURL).append("=").append(gatewayUrl);
		sb.append(" -D").append(RunningProp.WEFORWARD_APIURL).append("=").append(apiUrl);
		sb.append(" -D").append(RunningProp.WEFORWARD_HOST).append("=").append(host);
		Organization org = project.getOrganization();
		if (null != org) {
			sb.append(" -D").append(RunningProp.WEFORWARD_NAMESPACE).append("=").append(org.getId() + ".");
		}
		if (null != accessId) {
			sb.append(" -D").append(RunningProp.WEFORWARD_SERVICE_ACCESS_ID).append("=").append(accessId.trim());
		}
		if (null != accessKey) {
			sb.append(" -D").append(RunningProp.WEFORWARD_SERVICE_ACCESS_KEY).append("=").append(accessKey.trim());
		}
		if (project instanceof JavaProject) {
			List<Integer> ports = ((JavaProject) project).getServerPorts();
			if (null != ports && !ports.isEmpty()) {
				sb.append(" -D").append(RunningProp.WEFORWARD_PORT).append("=");
				sb.append(ports.get(0));
			}
			if (ports.size() > 1) {
				for (int i = 0; i < ports.size(); i++) {
					sb.append(" -D");
					sb.append(RunningProp.WEFORWARD_PORT);
					sb.append(String.valueOf(i + 1));
					sb.append("=");
					sb.append(ports.get(i));
				}
			}
		}
		list.add(new Env(Envs.WF_JAVA_OPTIONS_KEY, sb.toString()));
		return list;
	}

	private String getHost() {
		String url = m_Url;
		if (StringUtil.isEmpty(url)) {
			return "";
		}
		int start = url.indexOf("://") + 3;
		int end = url.lastIndexOf(":");
		return url.substring(start, end);
	}

	/* 默认绑定 */
	private List<String> getDefaultBinds(Project project) {
		List<String> list = new ArrayList<>();
//		list.add(LOCALTIME_PATH + ":/etc/localtime:ro"); // 解决时区问题
//		list.add(TIMEZONE_PATH + ":/etc/timezone:ro"); // 解决时区问题
		list.add(WORKSPACE + project.getName() + "/conf/:" + USER_DIR + "conf/:ro");
		list.add(WORKSPACE + project.getName() + "/script/:" + USER_DIR + "script/:ro");
		list.add(WORKSPACE + project.getName() + "/res/:" + USER_DIR + "res/:ro");
		return list;
	}

	private String findEnvValue(List<Env> envs, String key, String defaultValue) {
		Env e = findEnv(envs, key);
		return null == e ? defaultValue : e.getValue();
	}

	private Env findEnv(List<Env> envs, String key) {
		for (Env e : envs) {
			if (StringUtil.eq(e.getKey(), key)) {
				return e;
			}
		}
		return null;
	}

	private static class DockerBuildProgesserWrap implements DockerBuildProgesser {
		OpProcessor m_Processor;

		public DockerBuildProgesserWrap(OpProcessor p) {
			m_Processor = p;
		}

		@Override
		public void progesser(BuildStatus status) {
			if (null != m_Processor) {
				m_Processor.progesser(new Status("build-" + status.getId(), status.toString()));
			}
		}

	}

	@Override
	public void clear(Running running, int maxHistory) {
		Project project = running.getProject();
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			throw new RuntimeException("获取客户端异常", e);
		}
		if (null == client) {
			return;
		}
		String image = project.getName();
		Map<String, String[]> filters = new HashMap<String, String[]>();
		filters.put("reference", new String[] { image });
		filters.put("label", new String[] { REVEISION_LABEL });
		List<DockerImage> images;
		try {
			images = client.images(false, false, filters);
		} catch (Exception e) {
			throw new IllegalArgumentException("查询镜像异常", e);
		}
		if (images.size() <= maxHistory) {
			return;
		}
		Collections.sort(images, _BY_REVEISION);
		for (DockerImage info : images.subList(maxHistory, images.size())) {
			try {
				client.delete(info.getId(), false, true);
			} catch (Exception e) {
				_Logger.warn("忽略删除镜像异常", e);
			}
		}

	}


	public void remove(Project project) {
		String cname = project.getName();
		DockerClient client;
		try {
			client = getClient();
		} catch (IOException e) {
			_Logger.error("删除异常", e);
			return;
		}
		try {
			remove(client, cname, project);
			remove(client, cname + "-old", project);
		} catch (DockerException e) {
			_Logger.error("删除异常", e);
			return;
		}
	}

	private void remove(DockerClient client, String cname, Project project) throws DockerException {
		Map<String, String[]> filters = Collections.singletonMap("name", new String[] { cname });
		List<DockerContainer> list = filter(client.ps(100, true, false, filters), cname);
		if (list.isEmpty()) {
			return;
		}
		for (DockerContainer c : list) {
			int t = Envs.getKillTime(this, project);
			client.stop(c.getId(), t - 60);
			DockerInspect d = null;
			for (int i = 0; i < t; i++) {
				d = client.inspect(c.getId(), false);
				if (!d.getState().isRunning()) {
					break;
				}
				synchronized (this) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			if (null == d || d.getState().isRunning()) {
				continue;
			}
			client.remove(c.getId(), true, true, true);
		}
	}

	/** 版本排序 */
	private final static Comparator<DockerImage> _BY_REVEISION = new Comparator<DockerImage>() {

		@Override
		public int compare(DockerImage o1, DockerImage o2) {
			String rv1 = StringUtil.toString(o1.getLabel(REVEISION_LABEL));
			String rv2 = StringUtil.toString(o2.getLabel(REVEISION_LABEL));
			String[] arr1 = VersionInfo.getVersion(rv1).split("\\.");
			String[] arr2 = VersionInfo.getVersion(rv2).split("\\.");
			int l = Math.min(arr1.length, arr2.length);
			for (int i = 0; i < l; i++) {
				int v = NumberUtil.toInt(arr2[i], 0) - NumberUtil.toInt(arr1[i], 0);
				if (v == 0) {
					continue;
				}
				return v;
			}
			int v = arr2.length - arr1.length;
			if (v == 0) {

				return VersionInfo.getTime(rv1).compareTo(VersionInfo.getTime(rv1));
			}
			return v;
		}
	};

	private static boolean isOutOfMemory(MachineInfo info) {
		if (null == info) {
			return false;
		}
		if (info.getMemoryTotal() <= 0) {
			return false;
		}
		long rate = (info.getMemoryUsable() * 100) / info.getMemoryTotal();
		return rate <= 10;
	}

	/* 添加环境变量 */
	private static List<Env> addEnvIfNoExists(List<Env> result, List<Env> items) {
		if (null == items) {
			if (null == result) {
				return new ArrayList<>();
			}
			return result;
		}
		for (Env item : items) {
			boolean exists = false;
			if (StringUtil.isEmpty(item.getKey())) {
				continue;
			}
			for (Env env : result) {
				if (StringUtil.eq(env.getKey(), item.getKey())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				result.add(item);
			}
		}
		return result;
	}

	/* 添加绑定 */
	private static List<Bind> addBindIfNoExists(List<Bind> result, List<Bind> items) {
		if (null == items) {
			if (null == result) {
				return new ArrayList<>();
			}
			return result;
		}
		for (Bind item : items) {
			boolean exists = false;
			for (Bind env : result) {
				if (StringUtil.isEmpty(env.getSource())) {
					continue;
				}
				if (StringUtil.eq(env.getSource(), item.getSource())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public boolean onReloadAccepted(Persister<DockerMachine> persister, DockerMachine other) {
		m_Name = other.m_Name;
		m_Binds = other.m_Binds;
		m_Envs = other.m_Envs;
		m_Owner = other.m_Owner;
		m_GroupRights = other.m_GroupRights;
		m_Url = other.m_Url;
		return true;
	}

	private static List<DockerContainer> filter(List<DockerContainer> ps, String myName) {
		List<DockerContainer> list = new ArrayList<>();
		for (DockerContainer c : ps) {
			for (String name : c.getNames()) {
				if (StringUtil.eq(name, "/" + myName)) {
					list.add(c);
					break;
				}
			}
		}
		return list;
	}

}
