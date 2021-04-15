package script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import cn.weforward.framework.ext.WeforwardScript;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;

/**
 * 重索引
 * 
 * @author HeavyCheng
 *
 */
public class Reindex implements WeforwardScript {

	static Logger _Logger = LoggerFactory.getLogger(Reindex.class);

	@Override
	public DtObject handle(ApplicationContext context, String path, FriendlyObject params) {
		SimpleDtObject result = new SimpleDtObject();
		try {
			String op = params.getString("op");
			if ("project_reindex".equals(op)) {

//				@SuppressWarnings("unchecked")
//				ResultPage<Project> projects =  (ResultPage<Project>)  service.searchProjects(null);
				int c = 0;
//				int count = projects.getCount();
//				for(Project p:ResultPageHelper.toForeach(projects)){
//					String id =p.getPersistenceId().getId();
//					try {
//						SimpleProject sp = (SimpleProject) p;
//						//sp.reindex();
//						result.put(p.getName()+"："+sp.getRunnings().getCount(), true);
//						_Logger.info("reindex " + id+"，" +p.getName()+ " " + (c++) + "/" + count);
//					} catch (Throwable error) {
//						_Logger.warn("忽略reindex异常" + id, error);
//					}
//				}
				_Logger.info("reindex done");
				result.put("reindex:" + c, true);
			}
//			TaskExecutor exe = context.getBean(TaskExecutor.class);
//			String op = params.getString("op");
//			String name = params.getString("name");
//			List<DtBase> list = new ArrayList<>();
//			factory.getPersisters().getNames().forEachRemaining(e -> list.add(new SimpleDtString(e)));
//			result.put("names", SimpleDtList.valueOf(list));
//			if (!StringUtil.isEmpty(name)) {
//				ResultPage<?> rp = factory.getPersisters().getPersister(name).startsWith(null);
//				result.put("count", rp.getCount());
//				if ("reindex".equals(op)) {
//					exe.execute(new Runnable() {
//
//						@Override
//						public void run() {
//							int c = 0;
//							int count = rp.getCount();
//							for (Object e : ResultPageHelper.toForeach(rp)) {
//								if (e instanceof Searchable) {
//									String id = ((Persistent) e).getPersistenceId().getId();
//									try {
//										((Searchable) e).reindex();
//										_Logger.info("reindex " + id + " " + (c++) + "/" + count);
//									} catch (Throwable error) {
//										_Logger.warn("忽略reindex异常" + id, error);
//									}
//								}
//							}
//							_Logger.info("reindex done");
//						}
//					});
//					result.put("reindex", true);
//				}
//			}
		} catch (Throwable e) {
			result.put("error", e.getMessage());
			_Logger.warn("调用出错", e);
		}
		return result;
	}

}
