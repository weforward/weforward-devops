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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * docker 容器状态
 * 
 * @author daibo
 *
 */
public class DockerStats {
	/** 容器id */
	public String id;
	/** 容器名 */
	public String name;

	public String read;

	public String preread;

	public Pidsstats pids_stats;

	public BlkioStats blkio_stats;

	public Integer num_procs;

	public CpuStats cpu_stats;

	public PrecpuStats precpu_stats;

	public MemoryStats memory_stats;

	public DockerStats(JSONObject json) {
		id = json.optString("id");
		preread = json.optString("preread");
		name = json.optString("name");
		read = json.optString("read");
		{
			JSONObject obj = json.optJSONObject("pids_stats");
			if (null != obj) {
				pids_stats = new Pidsstats(obj);
			}
		}
		{
			JSONObject obj = json.optJSONObject("blkio_stats");
			if (null != obj) {
				blkio_stats = new BlkioStats(obj);
			}
		}
		num_procs = json.optInt("num_procs");

		{
			JSONObject obj = json.optJSONObject("cpu_stats");
			if (null != obj) {
				cpu_stats = new CpuStats(obj);
			}
		}

		{
			JSONObject obj = json.optJSONObject("precpu_stats");
			if (null != obj) {
				precpu_stats = new PrecpuStats(obj);
			}
		}

		{
			JSONObject obj = json.optJSONObject("memory_stats");
			if (null != obj) {
				memory_stats = new MemoryStats(obj);
			}
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getPreread() {
		return preread;
	}

	public void setPreread(String preread) {
		this.preread = preread;
	}

	public static class Pidsstats {

		private Integer current;

		public Pidsstats(JSONObject json) {
			current = json.optInt("current");
		}

		public Integer getCurrent() {
			return current;
		}
	}

	public static class BlkioStats {

		public OpAndValue[] io_service_bytes_recursive;

		public OpAndValue[] io_serviced_recursive;

		public OpAndValue[] io_queue_recursive;

		public OpAndValue[] io_service_time_recursive;

		public OpAndValue[] io_wait_time_recursive;

		public OpAndValue[] io_merged_recursive;

		public OpAndValue[] io_time_recursive;

		public OpAndValue[] sectors_recursive;

		public BlkioStats(JSONObject json) {
			io_service_bytes_recursive = OpAndValue
					.fromJson(json.optJSONArray("io_service_bytes_recursive"));
			io_serviced_recursive = OpAndValue.fromJson(json.optJSONArray("io_serviced_recursive"));
			io_queue_recursive = OpAndValue.fromJson(json.optJSONArray("io_queue_recursive"));
			io_service_time_recursive = OpAndValue
					.fromJson(json.optJSONArray("io_service_time_recursive"));
			io_wait_time_recursive = OpAndValue
					.fromJson(json.optJSONArray("io_wait_time_recursive"));
			io_merged_recursive = OpAndValue.fromJson(json.optJSONArray("io_merged_recursive"));
			io_time_recursive = OpAndValue.fromJson(json.optJSONArray("io_time_recursive"));
			sectors_recursive = OpAndValue.fromJson(json.optJSONArray("sectors_recursive"));
		}

		public OpAndValue[] getIo_service_bytes_recursive() {
			return io_service_bytes_recursive;
		}

		public OpAndValue[] getIo_serviced_recursive() {
			return io_serviced_recursive;
		}

		public OpAndValue[] getIo_queue_recursive() {
			return io_queue_recursive;
		}

		public OpAndValue[] getIo_service_time_recursive() {
			return io_service_time_recursive;
		}

		public OpAndValue[] getIo_wait_time_recursive() {
			return io_wait_time_recursive;
		}

		public OpAndValue[] getIo_merged_recursive() {
			return io_merged_recursive;
		}

		public OpAndValue[] getIo_time_recursive() {
			return io_time_recursive;
		}

		public OpAndValue[] getSectors_recursive() {
			return sectors_recursive;
		}

	}

	public static class OpAndValue {
		private Integer major;
		private Integer minor;
		private String op;
		private Integer value;

		public OpAndValue(JSONObject json) {
			major = json.optInt("major");
			minor = json.optInt("minor");
			op = json.optString("op");
			value = json.optInt("value");
		}

		public static OpAndValue[] fromJson(JSONArray array) {
			if (null == array) {
				return null;
			}
			OpAndValue[] arr = new OpAndValue[array.length()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = fromJson(array.optJSONObject(i));
			}
			return arr;
		}

		public static OpAndValue fromJson(JSONObject json) {
			if (null == json) {
				return null;
			}
			return new OpAndValue(json);
		}

		public Integer getMajor() {
			return major;
		}

		public Integer getMinor() {
			return minor;
		}

		public String getOp() {
			return op;
		}

		public Integer getValue() {
			return value;
		}

	}

	public static class CpuStats {

		private CpuUsage cpu_usage;
		private Long system_cpu_usage;
		private int online_cpus;
		private ThrottlingData throttling_data;

		public CpuStats(JSONObject json) {
			{
				JSONObject obj = json.optJSONObject("cpu_usage");
				if (null != obj) {
					cpu_usage = new CpuUsage(obj);
				}
			}
			system_cpu_usage = json.optLong("system_cpu_usage");
			online_cpus = json.optInt("online_cpus");
			{
				JSONObject obj = json.optJSONObject("throttling_data");
				if (null != obj) {
					throttling_data = new ThrottlingData(obj);
				}
			}
		}

		public CpuUsage getCpu_usage() {
			return cpu_usage;
		}

		public Long getSystem_cpu_usage() {
			return system_cpu_usage;
		}

		public int getOnline_cpus() {
			return online_cpus;
		}

		public ThrottlingData getThrottling_data() {
			return throttling_data;
		}

	}

	public static class CpuUsage {

		private Long total_usage;
		private Long[] percpu_usage;
		private Long usage_in_kernelmode;
		private Long usage_in_usermode;

		public CpuUsage(JSONObject json) {
			total_usage = json.optLong("total_usage");
			{
				JSONArray array = json.optJSONArray("percpu_usage");
				if (null != array) {
					percpu_usage = new Long[array.length()];
					for (int i = 0; i < array.length(); i++) {
						percpu_usage[i] = array.optLong(i);
					}
				}
			}
			usage_in_kernelmode = json.optLong("usage_in_kernelmode");
			usage_in_usermode = json.optLong("usage_in_usermode");
		}

		public Long getTotal_usage() {
			return total_usage;
		}

		public Long[] getPercpu_usage() {
			return percpu_usage;
		}

		public Long getUsage_in_kernelmode() {
			return usage_in_kernelmode;
		}

		public Long getUsage_in_usermode() {
			return usage_in_usermode;
		}

	}

	public static class PrecpuStats {

		private CpuUsage cpu_usage;
		private Long system_cpu_usage;
		private int online_cpus;
		private ThrottlingData throttling_data;

		public PrecpuStats(JSONObject json) {
			{
				JSONObject obj = json.optJSONObject("cpu_usage");
				if (null != obj) {
					cpu_usage = new CpuUsage(obj);
				}
			}
			system_cpu_usage = json.optLong("system_cpu_usage");
			online_cpus = json.optInt("online_cpus");
			{
				JSONObject obj = json.optJSONObject("throttling_data");
				if (null != obj) {
					throttling_data = new ThrottlingData(obj);
				}
			}
		}

		public CpuUsage getCpu_usage() {
			return cpu_usage;
		}

		public Long getSystem_cpu_usage() {
			return system_cpu_usage;
		}

		public int getOnline_cpus() {
			return online_cpus;
		}

		public ThrottlingData getThrottling_data() {
			return throttling_data;
		}

	}

	public static class ThrottlingData {
		private Integer periods;
		private Integer throttled_periods;
		private Integer throttled_time;

		public ThrottlingData(JSONObject json) {
			periods = json.optInt("periods");
			throttled_periods = json.optInt("throttled_periods");
			throttled_time = json.optInt("throttled_time");
		}

		public Integer getPeriods() {
			return periods;
		}

		public Integer getThrottled_periods() {
			return throttled_periods;
		}

		public Integer getThrottled_time() {
			return throttled_time;
		}

	}

	public static class MemoryStats {
		private Long usage;
		private Long max_usage;
		private MemoryStats_Stats stats;
		private Long limit;

		public MemoryStats(JSONObject json) {
			usage = json.optLong("usage");
			max_usage = json.optLong("max_usage");
			{
				JSONObject obj = json.optJSONObject("stats");
				if (null != obj) {
					stats = new MemoryStats_Stats(obj);
				}
			}
			limit = json.optLong("limit");
		}

		public Long getUsage() {
			return usage;
		}

		public Long getMax_usage() {
			return max_usage;
		}

		public MemoryStats_Stats getStats() {
			return stats;
		}

		public Long getLimit() {
			return limit;
		}

	}

	public static class MemoryStats_Stats {
		private Long active_anon;
		private Long active_file;
		private Long cache;
		private Long dirty;
		private Long hierarchical_memory_limit;
		private Long hierarchical_memsw_limit;
		private Long inactive_anon;
		private Long inactive_file;
		private Long mapped_file;
		private Long pgfault;
		private Long pgmajfault;
		private Long pgpgin;
		private Long pgpgout;
		private Long rss;
		private Long rss_huge;
		private Long total_active_anon;
		private Long total_active_file;
		private Long total_cache;
		private Long total_dirty;
		private Long total_inactive_anon;
		private Long total_inactive_file;
		private Long total_mapped_file;
		private Long total_pgfault;
		private Long total_pgmajfault;
		private Long total_pgpgin;
		private Long total_pgpgout;
		private Long total_rss;
		private Long total_rss_huge;
		private Long total_unevictable;
		private Long total_writeback;
		private Long unevictable;
		private Long writeback;

		public MemoryStats_Stats(JSONObject json) {
			active_anon = json.optLong("active_anon");
			active_file = json.optLong("active_file");
			cache = json.optLong("cache");
			dirty = json.optLong("dirty");
			hierarchical_memory_limit = json.optLong("hierarchical_memory_limit");
			hierarchical_memsw_limit = json.optLong("hierarchical_memsw_limit");
			inactive_anon = json.optLong("inactive_anon");
			inactive_file = json.optLong("inactive_file");
			mapped_file = json.optLong("mapped_file");
			pgfault = json.optLong("pgfault");
			pgmajfault = json.optLong("pgmajfault");
			pgpgin = json.optLong("pgpgin");
			pgpgout = json.optLong("pgpgout");
			rss = json.optLong("rss");
			rss_huge = json.optLong("rss_huge");
			total_active_anon = json.optLong("total_active_anon");
			total_active_file = json.optLong("total_active_file");
			total_cache = json.optLong("total_cache");
			total_dirty = json.optLong("total_dirty");
			total_inactive_anon = json.optLong("total_inactive_anon");
			total_inactive_file = json.optLong("total_inactive_file");
			total_mapped_file = json.optLong("total_mapped_file");
			total_pgfault = json.optLong("total_pgfault");
			total_pgmajfault = json.optLong("total_pgmajfault");
			total_pgpgin = json.optLong("total_pgpgin");
			total_pgpgout = json.optLong("total_pgpgout");
			total_rss = json.optLong("total_rss");
			total_rss_huge = json.optLong("total_rss_huge");
			total_unevictable = json.optLong("total_unevictable");
			total_writeback = json.optLong("total_writeback");
			unevictable = json.optLong("unevictable");
			writeback = json.optLong("writeback");
		}

		public Long getActive_anon() {
			return active_anon;
		}

		public Long getActive_file() {
			return active_file;
		}

		public Long getCache() {
			return cache;
		}

		public Long getDirty() {
			return dirty;
		}

		public Long getHierarchical_memory_limit() {
			return hierarchical_memory_limit;
		}

		public Long getHierarchical_memsw_limit() {
			return hierarchical_memsw_limit;
		}

		public Long getInactive_anon() {
			return inactive_anon;
		}

		public Long getInactive_file() {
			return inactive_file;
		}

		public Long getMapped_file() {
			return mapped_file;
		}

		public Long getPgfault() {
			return pgfault;
		}

		public Long getPgmajfault() {
			return pgmajfault;
		}

		public Long getPgpgin() {
			return pgpgin;
		}

		public Long getPgpgout() {
			return pgpgout;
		}

		public Long getRss() {
			return rss;
		}

		public Long getRss_huge() {
			return rss_huge;
		}

		public Long getTotal_active_anon() {
			return total_active_anon;
		}

		public Long getTotal_active_file() {
			return total_active_file;
		}

		public Long getTotal_cache() {
			return total_cache;
		}

		public Long getTotal_dirty() {
			return total_dirty;
		}

		public Long getTotal_inactive_anon() {
			return total_inactive_anon;
		}

		public Long getTotal_inactive_file() {
			return total_inactive_file;
		}

		public Long getTotal_mapped_file() {
			return total_mapped_file;
		}

		public Long getTotal_pgfault() {
			return total_pgfault;
		}

		public Long getTotal_pgmajfault() {
			return total_pgmajfault;
		}

		public Long getTotal_pgpgin() {
			return total_pgpgin;
		}

		public Long getTotal_pgpgout() {
			return total_pgpgout;
		}

		public Long getTotal_rss() {
			return total_rss;
		}

		public Long getTotal_rss_huge() {
			return total_rss_huge;
		}

		public Long getTotal_unevictable() {
			return total_unevictable;
		}

		public Long getTotal_writeback() {
			return total_writeback;
		}

		public Long getUnevictable() {
			return unevictable;
		}

		public Long getWriteback() {
			return writeback;
		}

	}

	@Override
	public String toString() {
		return "{ \"id\":\"" + id + "\" , \"name\":\"" + name + "\" }";
	}
}
