package cn.weforward.devops.project;

import java.util.TimeZone;

import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.StringUtil;

/**
 * 环境变量工具
 * 
 * @author daibo
 *
 */
public class Envs {

	/** 默认时区 */
	public static final String DEFAULT_TZ = System.getProperty("cn.weforward.devops.tz", TimeZone.getDefault().getID());
	/** 默认杀应用时间 */
	public static final int DEFAULT_KILL_TIME = NumberUtil.toInt(System.getProperty("cn.weforward.devops.killtime"),
			6 * 60);
	/** 项目版本环境变量 */
	public static final String PROJECT_VERSION_KEY = "PROJECT_VERSION";
	/** 定制java选项环境变量 */
	public static final String WF_JAVA_OPTIONS_KEY = "WF_JAVA_OPTIONS";
	/** 服务器Id的环境变量 */
	public static final String SERVER_ID_KEY = "SERVER_ID";
	/** 杀应用时间环境变量 */
	public static final String KILL_TIME_KEY = "KILL_TIME";
	/** 项目名环境变量 */
	public static final String PROJECT_NAME_KEY = "PROJECT_NAME";
	/** 实例id环境变量 */
	public static final String RUNNING_ID_KEY = "RUNNING_ID";

	/**
	 * 获取服务器id
	 * 
	 * @param machine 机器
	 * @return 服务器id
	 */
	public static String getServerid(Machine machine) {
		String v = findValue(machine, null, SERVER_ID_KEY);
		if (!StringUtil.isEmpty(v)) {
			return v;
		} else {
			return "";
		}
	}

	/**
	 * 获取杀应用时长
	 * 
	 * @param machine 机器
	 * @param project 项目
	 * @return 杀应用时长
	 */
	public static int getKillTime(Machine machine, Project project) {
		String v = findValue(machine, project, KILL_TIME_KEY);
		if (!StringUtil.isEmpty(v)) {
			return NumberUtil.toInt(v, DEFAULT_KILL_TIME);
		} else {
			return DEFAULT_KILL_TIME;
		}
	}

	/**
	 * 查找环境变量
	 * 
	 * @param machine 机器
	 * @param project 项目
	 * @param key     变量名
	 * @return 对应的环境变量
	 */
	public static String findValue(Machine machine, Project project, String key) {
		if (null != project) {
			for (Env env : project.getEnvs()) {
				if (StringUtil.eq(key, env.getKey())) {
					return env.getValue();
				}
			}
		}
		if (null != machine) {
			for (Env env : machine.getEnvs()) {
				if (StringUtil.eq(key, env.getKey())) {
					return env.getValue();
				}
			}
		}
		return System.getenv(key);
	}
}
