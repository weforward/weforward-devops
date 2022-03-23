package cn.weforward.devops.alarm.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.devops.alarm.Alarm;
import cn.weforward.devops.alarm.AlarmService;

/**
 * 报警服务实现
 * 
 * @author daibo
 *
 */
public class AlarmServiceImpl extends AlarmDiImpl implements AlarmService {

	public AlarmServiceImpl(PersisterFactory factory) {
		super(factory);
	}

	@Override
	public Alarm create(Date time, String content, String detailUrl, String... labels) {
		List<String> list = Collections.emptyList();
		if (null != labels && labels.length > 0) {
			list = Arrays.asList(labels);
		}
		return new SimpleAlarm(this, time, content, detailUrl, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultPage<Alarm> searchAlarms(Date begin, Date end, String keywords) {
		ResultPage<? extends Alarm> rp = m_PsAlarm.search(begin, end);
		rp = ResultPageHelper.reverseResultPage(rp);
		if (StringUtil.isEmpty(keywords)) {
			return (ResultPage<Alarm>) rp;
		}
		List<Alarm> list = new ArrayList<>();
		for (Alarm a : ResultPageHelper.toForeach(rp)) {
			if (!StringUtil.toString(a.getContent()).contains(keywords)) {
				continue;
			}
			list.add(a);
		}
		return ResultPageHelper.toResultPage(list);
	}

}
