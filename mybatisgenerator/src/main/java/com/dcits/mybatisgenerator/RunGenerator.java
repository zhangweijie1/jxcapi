package com.dcits.mybatisgenerator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * @author zxh
 * @ClassName: 生成器
 * @Description:
 * @date 2018/10/29 16:17
 * @updater zxh
 * @updatedate 2018/10/29 16:17
 */
public class RunGenerator {
	public static void main(String[] args) throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		ConfigurationParser cp = new ConfigurationParser(warnings);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream configFile=ClassLoader.getSystemResourceAsStream("generatorConfig.xml");

		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
}
