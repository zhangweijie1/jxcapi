package com.mrguo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * @ClassName: MybatisPlusConfig
 * @Description:
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/10 10:01 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Configuration
@MapperScan("com.mrguo.dao")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
