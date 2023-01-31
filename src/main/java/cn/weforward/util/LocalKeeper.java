package cn.weforward.util;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.weforward.common.ResultPage;
import cn.weforward.common.crypto.Base64;
import cn.weforward.common.crypto.Hex;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.SearchServiceParams;
import cn.weforward.protocol.gateway.ServiceSummary;
import cn.weforward.protocol.gateway.exception.KeeperException;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.protocol.ops.ServiceExt;
import cn.weforward.protocol.ops.secure.AclTable;
import cn.weforward.protocol.ops.secure.AclTableItem;
import cn.weforward.protocol.ops.secure.RightTable;
import cn.weforward.protocol.ops.secure.RightTableItem;
import cn.weforward.protocol.ops.traffic.TrafficTable;
import cn.weforward.protocol.ops.traffic.TrafficTableItem;

/**
 * 本机的keeper实现
 * 
 * @author daibo
 *
 */
public class LocalKeeper implements Keeper {

	protected LocalAccessExt m_Access;

	protected LocalServiceExt m_Service;

	public LocalKeeper(String accessId, String accessKey, String host, int port, String serverName, String serviceNo) {
		m_Access = new LocalAccessExt(accessId, accessKey);
		m_Service = new LocalServiceExt(host, port, serverName, serviceNo);
	}

	@Override
	public AccessExt getAccess(String id) {
		if (StringUtil.eq(id, m_Access.getAccessId())) {
			return m_Access;
		}
		return null;
	}

	@Override
	public ResultPage<AccessExt> listAccess(String kind, String group) {
		return listAccess(kind, group, null);
	}

	@Override
	public ResultPage<AccessExt> listAccess(String kind, String group, String keyword) {
		if (!StringUtil.isEmpty(keyword) && !m_Access.getSummary().contains(keyword)) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(kind) && !StringUtil.eq(kind, m_Access.getKind())) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(group) && !StringUtil.eq(group, m_Access.getGroupId())) {
			return ResultPageHelper.empty();
		}
		return ResultPageHelper.singleton(m_Access);
	}

	@Override
	public List<String> listAccessGroup(String kind) {
		if (!StringUtil.isEmpty(kind) && !StringUtil.eq(kind, m_Access.getKind())) {
			return Collections.emptyList();
		}
		return Collections.singletonList(m_Access.getAccessId());
	}

	@Override
	public AccessExt createAccess(String kind, String group, String summary) {
		return null;
	}

	@Override
	public AccessExt updateAccess(String accessId, String summary, Boolean valid) {
		return null;
	}

	@Override
	public ResultPage<String> listServiceName(String keyword) {
		return listServiceName(keyword, null);
	}

	@Override
	public ResultPage<String> listServiceName(String keyword, String accessGroup) {
		if (!StringUtil.isEmpty(accessGroup) && !StringUtil.eq(accessGroup, m_Access.getGroupId())) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(keyword) && !m_Service.getName().contains(keyword)) {
			return ResultPageHelper.empty();
		}
		return ResultPageHelper.singleton(m_Service.getName());
	}

	@Override
	public ResultPage<ServiceExt> listService(String name) {
		return listService(name, null);
	}

	@Override
	public ResultPage<ServiceExt> listService(String name, String accessGroup) {
		if (!StringUtil.isEmpty(accessGroup) && !StringUtil.eq(accessGroup, m_Access.getGroupId())) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(name) && !m_Service.getName().equals(name)) {
			return ResultPageHelper.empty();
		}
		return ResultPageHelper.singleton(m_Service);
	}

	@Override
	public ResultPage<ServiceExt> searchService(SearchServiceParams params) {
		String keyword = null == params ? null : params.getKeyword();
		String runningId = null == params ? null : params.getRunningId();
		if (!StringUtil.isEmpty(keyword) && !m_Service.getName().contains(keyword)) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(runningId) && !Objects.equals(m_Service.getRunningId(), runningId)) {
			return ResultPageHelper.empty();
		}
		return ResultPageHelper.singleton(m_Service);
	}

	@Override
	public RightTable getRightTable(String name) {
		return null;
	}

	@Override
	public RightTable appendRightRule(String name, RightTableItem item) {
		return null;
	}

	@Override
	public RightTable insertRightRule(String name, RightTableItem item, int index) {
		return null;
	}

	@Override
	public RightTable replaceRightRule(String name, RightTableItem item, int index, String replaceName) {
		return null;
	}

	@Override
	public RightTable removeRightRule(String name, int index, String removeName) {
		return null;
	}

	@Override
	public RightTable moveRightRule(String name, int from, int to) {
		return null;
	}

	@Override
	public RightTable setRightRules(String name, List<RightTableItem> items) {
		return null;
	}

	@Override
	public TrafficTable getTrafficTable(String name) {
		return null;
	}

	@Override
	public TrafficTable appendTrafficRule(String name, TrafficTableItem item) {
		return null;
	}

	@Override
	public TrafficTable insertTrafficRule(String name, TrafficTableItem item, int index) {
		return null;
	}

	@Override
	public TrafficTable replaceTrafficRule(String name, TrafficTableItem item, int index, String replaceName) {
		return null;
	}

	@Override
	public TrafficTable removeTrafficRule(String name, int index, String removeName) {
		return null;
	}

	@Override
	public TrafficTable moveTrafficRule(String name, int from, int to) {
		return null;
	}

	@Override
	public TrafficTable setTrafficRules(String name, List<TrafficTableItem> items) {
		return null;
	}

	@Override
	public AclTable getAclTable(String name) {
		return null;
	}

	@Override
	public AclTable appendAclRule(String name, AclTableItem item) {
		return null;
	}

	@Override
	public AclTable replaceAclRule(String name, AclTableItem item, int index, String replaceName) {
		return null;
	}

	@Override
	public AclTable removeAclRule(String name, int index, String removeName) {
		return null;
	}

	@Override
	public AclTable moveAclRule(String name, int from, int to) {
		return null;
	}

	@Override
	public DtObject debugService(String serviceName, String serviceNo, String scriptSource, String scriptName,
			String scriptArgs) throws KeeperException {
		return null;
	}

	@Override
	public ResultPage<ServiceSummary> listServiceSummary(String keyword) {
		return listServiceSummary(keyword, null);
	}

	@Override
	public ResultPage<ServiceSummary> listServiceSummary(String keyword, String accessGroup) {
		if (!StringUtil.isEmpty(accessGroup) && !StringUtil.eq(accessGroup, m_Access.getGroupId())) {
			return ResultPageHelper.empty();
		}
		if (!StringUtil.isEmpty(keyword) && !m_Service.getName().contains(keyword)) {
			return ResultPageHelper.empty();
		}
		return ResultPageHelper.singleton(m_Service);
	}

	@Override
	public boolean isExistService(String name, String accessGroup) {
		return listService(name, accessGroup).getCount() > 0;
	}

	public static class LocalAccessExt implements AccessExt {
		protected String m_AccessId;
		protected byte[] m_AccessKey;

		LocalAccessExt(String accessId, String accessKey) {
			m_AccessId = accessId;
			try {
				m_AccessKey = Hex.decode(accessKey);
			} catch (Exception e) {
				m_AccessKey = Base64.decode(accessKey);
			}
		}

		@Override
		public String getAccessId() {
			return m_AccessId;
		}

		@Override
		public byte[] getAccessKey() {
			return m_AccessKey;
		}

		@Override
		public String getAccessKeyHex() {
			return Hex.encode(getAccessKey());
		}

		@Override
		public String getAccessKeyBase64() {
			return Base64.encode(getAccessKey());
		}

		@Override
		public String getKind() {
			return KIND_SERVICE;
		}

		@Override
		public String getTenant() {
			return null;
		}

		@Override
		public String getOpenid() {
			return null;
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public String getGroupId() {
			return DEFAULT_GROUP;
		}

		@Override
		public String getSummary() {
			return "本机";
		}

		@Override
		public void setSummary(String summary) {

		}

		@Override
		public void setValid(boolean valid) {

		}

	}

	public static class LocalServiceExt extends ServiceSummary implements ServiceExt {

		protected String m_No;
		protected String m_Domain;
		protected int m_Port;

		public LocalServiceExt(String host, int port, String name, String no) {
			super.setName(name);
			super.setStatus(0);
			super.setSummary("本机服务");
			m_Domain = host;
			m_Port = port;
			m_No = no;
		}

		@Override
		public String getDomain() {
			return m_Domain;
		}

		@Override
		public int getPort() {
			return m_Port;
		}

		@Override
		public List<String> getUrls() {
			return Collections.singletonList("http://" + getDomain() + ":" + getPort() + "/" + getName());
		}

		@Override
		public String getNo() {
			return m_No;
		}

		@Override
		public String getVersion() {
			return "1.0";
		}

		@Override
		public String getCompatibleVersion() {
			return null;
		}

		@Override
		public String getBuildVersion() {
			return null;
		}

		@Override
		public int getHeartbeatPeriod() {
			return 0;
		}

		@Override
		public String getNote() {
			return null;
		}

		@Override
		public String getDocumentMethod() {
			return null;
		}

		@Override
		public String getDebugMethod() {
			return null;
		}

		@Override
		public String getRunningId() {
			return null;
		}

		@Override
		public int getRequestMaxSize() {
			return 0;
		}

		@Override
		public long getMarks() {
			return 0;
		}

		@Override
		public String getOwner() {
			return null;
		}

		@Override
		public int getState() {
			return 0;
		}

		@Override
		public Date getHeartbeat() {
			return new Date();
		}

		@Override
		public boolean isTimeout() {
			return false;
		}

		@Override
		public boolean isInaccessible() {
			return false;
		}

		@Override
		public boolean isUnavailable() {
			return false;
		}

		@Override
		public boolean isOverload() {
			return false;
		}

	}

}
