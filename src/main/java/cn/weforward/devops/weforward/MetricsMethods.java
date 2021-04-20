package cn.weforward.devops.weforward;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.util.Bytes;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.weforward.view.ByteMetrics;
import cn.weforward.devops.weforward.view.NumberMetrics;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.metrics.ManyMetrics;
import cn.weforward.metrics.MetricsService;
import cn.weforward.metrics.OneMetrics;
import cn.weforward.metrics.WeforwardMetrics;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tags;

/**
 * 指标方法集
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
@DocMethods(index = 200)
public class MetricsMethods {
	@Resource
	protected MetricsService m_MetricsService;

	@DocMethod(description = "网关标签", index = 0)
	@WeforwardMethod
	public List<String> gwTags(FriendlyObject params) {
		Tags tags = Tags.of("gatewayId", "*");
		Meter.Id id = new Meter.Id(WeforwardMetrics.GATEWAY_UP_TIME, tags, null, null, Meter.Type.GAUGE);
		return m_MetricsService.showTags(getMyOrganization(), id);
	}

	@DocMethod(description = "网关启动时间", index = 1)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id") })
	@WeforwardMethod
	public Date gwStartTime(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_START_TIME, tags, null, null, Meter.Type.GAUGE);
		return getDate(mid);
	}

	@DocMethod(description = "网关持续时间", index = 2)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id") })
	@WeforwardMethod
	public String gwUpTime(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_UP_TIME, tags, null, null, Meter.Type.GAUGE);
		return getTime(mid);
	}

	@DocMethod(description = "网关已用内存", index = 3)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id") })
	@WeforwardMethod
	public String gwMemoryUsed(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		return getBytes(new Meter.Id(WeforwardMetrics.GATEWAY_MEMORY_USED, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "网关已分配内存", index = 4)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id") })
	@WeforwardMethod
	public String gwMemoryAlloc(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		return getBytes(new Meter.Id(WeforwardMetrics.GATEWAY_MEMORY_ALLOC, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "网关内存上限", index = 5)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id") })
	@WeforwardMethod
	public String gwMemoryMax(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		return getBytes(new Meter.Id(WeforwardMetrics.GATEWAY_MEMORY_MAX, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "网关已用内存范围", index = 6)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> gwMemoryUsedRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_MEMORY_USED, tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1l,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关已分配内存范围", index = 7)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> gwMemoryAllocRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_MEMORY_ALLOC, tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1l,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关RPC次数范围", index = 8)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwRpcCountRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_RPC_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关RPC并发数范围", index = 9)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwRpcConcurrentRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_RPC_CONCURRENT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关Stream次数范围", index = 10)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwStreamcCountRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_STREAM_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关Stream并发数范围", index = 11)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwStreamConcurrentRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_STREAM_CONCURRENT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关CPU使用率范围", index = 12)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwCpuUsageRateRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_CPU_USAGE_RATE, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关线程数范围", index = 13)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwThreadCountRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_THREAD_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关FullGC次数范围", index = 14)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwGcFullCountRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_GC_FULL_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "网关FullGC时间范围", index = 15)
	@DocParameter({ @DocAttribute(name = "id", type = String.class, description = "网关id"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwGcFullTimeRange(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		ValidateUtil.isEmpty(id, "网关id不能为空");
		Tags tags = Tags.of("gatewayId", id);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_GC_FULL_TIME, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务名标签", index = 16)
	@WeforwardMethod
	public List<String> msNameTags(FriendlyObject params) {
		Tags tags = Tags.of("serviceName", "*");
		Meter.Id id = new Meter.Id(WeforwardMetrics.UP_TIME, tags, null, null, Meter.Type.GAUGE);
		return m_MetricsService.showTags(getMyOrganization(), id);
	}

	@DocMethod(description = "服务编号标签", index = 17)
	@WeforwardMethod
	public List<String> msNoTags(FriendlyObject params) {
		Tags tags = Tags.of("serviceNo", "*");
		Meter.Id id = new Meter.Id(WeforwardMetrics.UP_TIME, tags, null, null, Meter.Type.GAUGE);
		return m_MetricsService.showTags(getMyOrganization(), id);
	}

	@DocMethod(description = "服务启动时间", index = 18)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号") })
	@WeforwardMethod
	public Date msStartTime(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.START_TIME, tags, null, null, Meter.Type.GAUGE);
		return getDate(mid);
	}

	@DocMethod(description = "服务持续时间", index = 19)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号") })
	@WeforwardMethod
	public String msUpTime(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.UP_TIME, tags, null, null, Meter.Type.GAUGE);
		return getTime(mid);
	}

	@DocMethod(description = "服务已用内存", index = 20)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号") })
	@WeforwardMethod
	public String msMemoryUsed(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		return getBytes(new Meter.Id(WeforwardMetrics.MEMORY_USED, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "服务已分配内存", index = 21)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号") })
	@WeforwardMethod
	public String msMemoryAlloc(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		return getBytes(new Meter.Id(WeforwardMetrics.MEMORY_ALLOC, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "服务内存上限", index = 22)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号") })
	@WeforwardMethod
	public String msMemoryMax(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		return getBytes(new Meter.Id(WeforwardMetrics.MEMORY_MAX, tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "服务已用内存范围", index = 23)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> msMemoryUsedRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.MEMORY_USED, tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "max(\"VALUE\")", null, begin, end, 1, params.getInt("interval"));
	}

	@DocMethod(description = "服务已分配内存范围", index = 24)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> msMemoryAllocRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.MEMORY_ALLOC, tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "max(\"VALUE\")", null, begin, end, 1, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务最大内存范围", index = 25)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> msMemoryMaxRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.MEMORY_MAX, tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "max(\"VALUE\")", null, begin, end, 1, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务CPU使用率范围", index = 26)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msCpuUsageRateRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.CPU_USAGE_RATE, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务线程数范围", index = 27)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msThreadCountRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.THREAD_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务FullGC次数范围", index = 28)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msGcFullCountRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GC_FULL_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务FullGC时间范围", index = 29)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msGcFullTimeRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GC_FULL_TIME, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC并发数范围", index = 30)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msRpcCurrentRequestRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.RPC_CURRENT_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "max(\"VALUE\")", null, begin, end, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC次数范围", index = 31)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msRpcRequestCountRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.RPC_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.COUNT.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC错误次数范围", index = 32)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msRpcRequestCountErrorRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Tags exclude = Tags.of("code", "0");
		Meter.Id mid = new Meter.Id(WeforwardMetrics.RPC_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.COUNT.name() + "\")", exclude, begin, end,
				params.getInt("interval", 0));

	}

	@WeforwardMethod
	public List<NumberMetrics> msRpcRequestTotalimelRange(FriendlyObject params) throws ApiException {
		return msRpcRequestTotaltimelRange(params);
	}

	@DocMethod(description = "服务RPC耗时范围", index = 33)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msRpcRequestTotaltimelRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.RPC_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.TOTAL_TIME.name() + "\")/sum(\"" + Statistic.COUNT + "\")", null,
				begin, end, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC分钟内最大耗时范围", index = 34)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msRpcRequestMaxlRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.RPC_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "max(\"" + Statistic.MAX.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务Stream并发数范围", index = 35)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msStreamCurrentRequestRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.STREAM_CURRENT_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "max(\"VALUE\")", null, begin, end, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务Stream次数范围", index = 36)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msStreamRequestCountRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.STREAM_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.COUNT.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务Stream错误次数范围", index = 37)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msStreamRequestCountErrorRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Tags exclude = Tags.of("code", "0");
		Meter.Id mid = new Meter.Id(WeforwardMetrics.STREAM_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.COUNT.name() + "\")", exclude, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务Stream耗时范围", index = 38)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msStreamRequestTotalimelRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.STREAM_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.TOTAL_TIME.name() + "\")/sum(\"" + Statistic.COUNT + "\")", null,
				begin, end, params.getInt("interval", 0));
	}

	@DocMethod(description = "服务Stream分钟内最大耗时范围", index = 39)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> msStreamRequestMaxlRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.STREAM_REQUEST_KEY, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "max(\"" + Statistic.MAX.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器名标签", index = 40)
	@WeforwardMethod
	public List<String> agentNameTags(FriendlyObject params) {
		Tags tags = Tags.of("name", "*");
		Meter.Id id = new Meter.Id("weforward.agent.uptime", tags, null, null, Meter.Type.GAUGE);
		return m_MetricsService.showTags(getMyOrganization(), id);
	}

	@DocMethod(description = "容器路径标签", index = 41)
	@WeforwardMethod
	public List<String> agentPathTags(FriendlyObject params) {
		Tags tags = Tags.of("path", "*");
		Meter.Id id = new Meter.Id("weforward.agent.disttotal", tags, null, null, Meter.Type.GAUGE);
		return m_MetricsService.showTags(getMyOrganization(), id);
	}

	@DocMethod(description = "容器启动时间", index = 42)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public Date agentStartTime(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Meter.Id mid = new Meter.Id("weforward.agent.starttime", tags, null, null, Meter.Type.GAUGE);
		return getDate(mid);
	}

	@DocMethod(description = "容器持续时间", index = 43)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentUpTime(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Meter.Id mid = new Meter.Id("weforward.agent.uptime", tags, null, null, Meter.Type.GAUGE);
		return getTime(mid);
	}

	@DocMethod(description = "容器可用内存", index = 44)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentMemoryUsable(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		return getBytes(new Meter.Id("weforward.agent.memoryusable", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器可用内存范围", index = 45)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentMemoryUsableRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.memoryusable", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器已用内存", index = 46)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentMemoryUsed(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		return getBytes(new Meter.Id("weforward.agent.memoryused", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器已用内存范围", index = 47)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentMemoryUsedRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.memoryused", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器总内存", index = 48)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentMemoryTotal(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		return getBytes(new Meter.Id("weforward.agent.memorytotal", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器总内存范围", index = 49)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentMemoryTotalRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.memorytotal", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "CPU数", index = 50)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentCpuNum(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		return getBytes(new Meter.Id("weforward.agent.cpunum", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器负载", index = 51)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名") })
	@WeforwardMethod
	public String agentLoadAverage(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		return getBytes(new Meter.Id("weforward.agent.loadaverage", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器负载范围", index = 52)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentLoadAverageRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.loadaverage", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器负载最大值范围", index = 53)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentLoadAverageMaxRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		Tags tags = Tags.of("name", name);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.cpunum", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 100,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器可用空间", index = 54)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true) })
	@WeforwardMethod
	public String agentDistUsable(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return "";
		}
		Tags tags = Tags.of("name", name, "path", path);
		return getBytes(new Meter.Id("weforward.agent.distusable", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器可用空间范围", index = 55)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentDistUsableRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return Collections.emptyList();
		}
		Tags tags = Tags.of("name", name, "path", path);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.distusable", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器已用空间", index = 56)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true) })
	@WeforwardMethod
	public String agentDistUsed(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return "";
		}
		Tags tags = Tags.of("name", name, "path", path);
		return getBytes(new Meter.Id("weforward.agent.distused", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器已用空间范围", index = 57)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentDistUsedRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return Collections.emptyList();
		}
		Tags tags = Tags.of("name", name, "path", path);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.distused", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "容器总空间", index = 58)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true) })
	@WeforwardMethod
	public String agentDistTotal(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return "";
		}
		Tags tags = Tags.of("name", name, "path", path);
		return getBytes(new Meter.Id("weforward.agent.disttotal", tags, null, null, Meter.Type.GAUGE));
	}

	@DocMethod(description = "容器总空间范围", index = 59)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "容器名", necessary = true),
			@DocAttribute(name = "path", type = String.class, description = "路径", necessary = true),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<ByteMetrics> agentDistTotalRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "容器名不能为空");
		String path = params.getString("path");
		if (StringUtil.isEmpty(path)) {
			return Collections.emptyList();
		}
		Tags tags = Tags.of("name", name, "path", path);
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Meter.Id mid = new Meter.Id("weforward.agent.disttotal", tags, null, null, Meter.Type.GAUGE);
		return searchBytes(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end, 1,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC并发数范围(网关数据)", index = 60)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwMsRpcCurrentRequestRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_RPC_CONCURRENT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "mean(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC次数范围(网关数据)", index = 61)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwMsRpcRequestCountRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_SERVICE_RPC_COUNT, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.VALUE.name() + "\")", null, begin, end,
				params.getInt("interval", 0));
	}

	@DocMethod(description = "服务RPC错误次数范围(网关数据)", index = 62)
	@DocParameter({ @DocAttribute(name = "name", type = String.class, description = "服务名"),
			@DocAttribute(name = "no", type = String.class, description = "服务编号"),
			@DocAttribute(name = "begin", type = Date.class, description = "开始时间"),
			@DocAttribute(name = "end", type = Date.class, description = "结束时间"),
			@DocAttribute(name = "interval", type = int.class, description = "时间间隔，单位分钟", example = "2") })
	@WeforwardMethod
	public List<NumberMetrics> gwMsRpcRequestCountErrorRange(FriendlyObject params) throws ApiException {
		String name = params.getString("name");
		String no = params.getString("no");
		ValidateUtil.isEmpty(name, "服务名不能为空");
		ValidateUtil.isEmpty(no, "服务编号不能为空");
		Date begin = params.getDate("begin");
		Date end = params.getDate("end");
		Tags tags = Tags.of("serviceName", name, "serviceNo", no);
		Tags exclude = Tags.of("code", "0");
		Meter.Id mid = new Meter.Id(WeforwardMetrics.GATEWAY_SERVICE_RPC_FAIL, tags, null, null, Meter.Type.GAUGE);
		return searchNumbers(mid, "sum(\"" + Statistic.VALUE.name() + "\")", exclude, begin, end,
				params.getInt("interval", 0));
	}

	private Date getDate(Meter.Id mid) {
		OneMetrics metrics = m_MetricsService.getLately(getMyOrganization(), mid);
		if (null == metrics) {
			return null;
		}
		return new Date((long) (metrics.getValue() * 1000));
	}

	private String getTime(Meter.Id mid) {
		OneMetrics metrics = m_MetricsService.getLately(getMyOrganization(), mid);
		if (null == metrics) {
			return null;
		}
		long v = (long) (metrics.getValue() * 1000l);
		long s = v / 1000;
		long m = s / 60;
		s = s % 60;
		long h = m / 60;
		m = m % 60;
		long d = h / 24;
		h = h % 24;
		if (d > 0) {
			return d + "天" + h + "小时" + m + "分" + s + "秒";
		} else if (h > 0) {
			return h + "小时" + m + "分" + s + "秒";
		} else if (m > 0) {
			return m + "分" + s + "秒";
		} else {
			return s + "秒";
		}
	}

	private String getBytes(Meter.Id mid) {
		OneMetrics metrics = m_MetricsService.getLately(getMyOrganization(), mid);
		if (null == metrics) {
			return null;
		}
		long v = ((long) (metrics.getValue()));
		return Bytes.formatHumanReadable(v);
	}

	private List<ByteMetrics> searchBytes(Meter.Id mid, String name, Tags exclude, Date begin, Date end, long value,
			int interval) {
		ManyMetrics m = m_MetricsService.search(getMyOrganization(), mid, name, exclude, begin, end, interval);
		if (null == m) {
			return null;
		}
		List<OneMetrics> list = m.getItems();
		if (null == list || list.isEmpty()) {
			return Collections.emptyList();
		}
		return TransList.valueOf(list, (e) -> new ByteMetrics(e.getTime(), ((long) (e.getValue())) * value));
	}

	private List<NumberMetrics> searchNumbers(Meter.Id mid, String name, Tags exclude, Date begin, Date end,
			int interval) {
		ManyMetrics m = m_MetricsService.search(getMyOrganization(), mid, name, exclude, begin, end, interval);
		if (null == m) {
			return null;
		}
		List<OneMetrics> list = m.getItems();
		if (null == list || list.isEmpty()) {
			return Collections.emptyList();
		}
		return TransList.valueOf(list, (e) -> new NumberMetrics(e.getTime(), ((long) (e.getValue()))));
	}

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}
}
