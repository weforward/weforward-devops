package cn.weforward.devops;

import org.junit.Test;

import cn.weforward.util.FileClearOnNum;

public class FileClearTest {
	@Test
	public void test() {
		FileClearOnNum f = new FileClearOnNum("D://test/", "测试", 2);
		synchronized (f) {
			try {
				f.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
