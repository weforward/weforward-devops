package cn.weforward.devops;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.weforward.common.util.StringUtil;
import cn.weforward.util.docker.AuthInfo;
import cn.weforward.util.docker.DockerClient;
import cn.weforward.util.docker.DockerException;
import cn.weforward.util.docker.DockerImage;

public class DockerTest {
	static String URL = System.getProperty("url");
	static AuthInfo INFO;
	static {
		INFO = new AuthInfo(System.getProperty("hub"), System.getProperty("u"), System.getProperty("p"), null);
	}

	@Test
	public void test() {
		if (StringUtil.isEmpty(URL)) {
			return;
		}
		DockerClient client;
		// String image = System.getProperty("hub") + "/coupon:1.0.1";
		try {
			client = new DockerClient();
			client.setUrl(URL);
			client.setAuthInfos(Arrays.asList(INFO));
			Map<String, String[]> filters = new HashMap<String, String[]>();
			filters.put("reference", new String[] { "ota" });
			filters.put("label", new String[] { "revieson" });
//			Map<String, String[]> filters = Collections.singletonMap("reference",
//					new String[] { "git.navboy.com:5000/ota" });
			// filters.put("label", new String[] { "MAINTAINER=Zipkin https://zipkin.io/"
			// });
//			filters.put("since", new String[] { "git.navboy.com:5000/kefu:1.0.0" });
//			DockerPullProgesser progesser = new PrintStreamPullProgesser(System.out);
//			client.pull(image, progesser);
			List<DockerImage> images = client.images(false, false, filters);
			// Collections.sort(images, DockerMachine._BY_REVEISION);

			for (DockerImage image : images) {
//				String[] rep = image.getRepoTags();
//				if (null == rep || rep.length < 1) {
				System.out.println(image.getId() + "," + image.getLabels());
//				} else {
//					System.out.println(
//							rep[0] + "," + image.getId() + "," + image.getParentId() + "," + image.getLabels());
//				}

				// System.out.println(image);
			}
//			String id = client.createExec("weforward-devops", new String[] { "ls", "/home/boot/stack/" });
//			String dirString = client.exec(id);
//			System.out.println(dirString);
//			List<String> list = Arrays.asList(dirString.split("\n"));
//			System.out.println(list);
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			client.archive("weforward-devops", "/home/boot/stack/1.txt", out);
//			TarArchiveInputStream in = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
//			TarArchiveEntry e;
//			while ((e = in.getNextTarEntry()) != null) {
//				System.out.println("---" + e.getName() + "---");
//				byte data[] = new byte[1024];
//				int l = 0;
//				while ((l = in.read(data)) != -1) {
//					System.out.println(new String(data, 0, l));
//				}
//
//			}
		} catch (DockerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
