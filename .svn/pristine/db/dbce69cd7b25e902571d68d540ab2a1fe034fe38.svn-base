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
package cn.weforward;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import cn.weforward.boot.CloudPropertyPlaceholderConfigurer;
import cn.weforward.common.util.BackgroundExecutor;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.data.persister.PersisterSet;
import cn.weforward.data.persister.support.SimplePersisterSet;
import cn.weforward.data.util.DelayFlusher;
import cn.weforward.data.util.Flusher;

/**
 * spring配置
 * 
 * @author daibo
 *
 */
@Configuration
@Import({ EndpointConfig.class, DevopsConfig.class, RlogConfig.class, DistConfig.class, MetricsConfig.class,
		SiteConfig.class })
public class SpringConfig {
	/** 用户目录 */
	private static String USER_DIR = System.getProperty("user.dir");

	static Resource genProperty(String name) {
		return new FileSystemResource(USER_DIR + "/conf/" + name + ".properties");
	}

	/** 配置 */
	@Bean
	static CloudPropertyPlaceholderConfigurer configurer() {
		CloudPropertyPlaceholderConfigurer c = new CloudPropertyPlaceholderConfigurer();
		c.setDisableCloud(true);
		Resource l1 = genProperty("devops");
		if (l1.exists()) {
			c.setLocations(l1);
		}
		return c;
	}

	/** 线程池 */
	@Bean
	ThreadPool threadPool() {
		return new ThreadPool(1000, "common");
	}

	/*** 任务执行器（线程池） */
	@Bean
	TaskExecutor taskExecutor() {
		return new BackgroundExecutor(2, 10, 1);
	}

	/*** 持久器集，用于互通多个DataHub中的对象，否则之间对象的引用在反射时将找不回 */
	@Bean
	PersisterSet persisters() {
		return new SimplePersisterSet();
	}

	/*** 延时提交刷写器 用于在每次请求后刷写 */
	@Bean
	Flusher flusher() {
		DelayFlusher f = new DelayFlusher(10);
		f.setName("comm");
		f.setMaxSuspend(1000);
		return f;
	}

	@Bean
	Properties globalProperties() throws IOException {
		Properties prop = new Properties();
		Resource res = genProperty("global");
		if (res.exists()) {
			prop.load(res.getInputStream());
		}
		return prop;
	}
}
