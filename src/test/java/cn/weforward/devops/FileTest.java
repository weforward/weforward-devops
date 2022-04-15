package cn.weforward.devops;

import org.junit.Test;

public class FileTest {
	@Test
	public void test() {
		System.out.println(getSuffix("file.zip"));
	}

	private static String getSuffix(String name) {
		int index = name.indexOf(".");
		if (index > 0) {
			return name.substring(index);
		}
		return null;
	}
}
