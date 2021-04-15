package cn.weforward.devops;

import org.junit.Test;

import cn.weforward.rlog.LogPage;
import cn.weforward.rlog.impl.RlogResultImpl;

public class FileTest {
	@Test
	public void test() {

		try {
			LogPage page = RlogResultImpl.valueOf("1.txt");
			System.out.println(page.get(Integer.MAX_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
