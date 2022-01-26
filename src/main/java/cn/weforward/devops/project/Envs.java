package cn.weforward.devops.project;

import java.util.TimeZone;

import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.StringUtil;

public class Envs {
	/** kill时间键值 */
	public static final String KILL_TIME_KEY_NAME = "KILL_TIME";
	/** 默认时区 */
	public static final String DEFAULT_TZ = System.getProperty("cn.weforward.devops.tz", TimeZone.getDefault().getID());

	public static final int DEFAULT_KILL_TIME = NumberUtil.toInt(System.getProperty("cn.weforward.devops.killtime"),
			6 * 60);

	public static String getServerid(Machine machine) {
		for (Env e : machine.getEnvs()) {
			if (StringUtil.eq(e.getKey(), "SERVER_ID")) {
				return e.getValue();
			}
		}
		return "";
	}

	public static int getKillTime(Machine machine, Project project) {
		for (Env env : project.getEnvs()) {
			if (StringUtil.eq(KILL_TIME_KEY_NAME, env.getKey())) {
				return NumberUtil.toInt(env.getValue(), DEFAULT_KILL_TIME);
			}
		}
		for (Env env : machine.getEnvs()) {
			if (StringUtil.eq(KILL_TIME_KEY_NAME, env.getKey())) {
				return NumberUtil.toInt(env.getValue(), DEFAULT_KILL_TIME);
			}
		}
		return DEFAULT_KILL_TIME;
	}
}
