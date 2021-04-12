package com.mrguo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName 
 * @Description 启动类
 * @author 郭成兴
 * @date 2019/8/6 2:28 PM
 * @updater 郭成兴
 * @updatedate 2019/8/6 2:28 PM
 */
@EnableSwagger2
@MapperScan("com.mrguo.dao")
@SpringBootApplication
public class JxcApplication {
	public static void main(String[] args) {
		SpringApplication.run(JxcApplication.class, args);
	}
}
