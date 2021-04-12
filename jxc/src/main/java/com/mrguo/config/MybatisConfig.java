package com.mrguo.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将mybatis查询返回的Map中的key转换为驼峰命名
 * @author xieds
 * @date 2018/11/5 10:10
 * @updater xieds
 * @updatedate 2018/11/5 10:10
 */
@Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setObjectWrapperFactory(new MapWrapperFactory());
            }
        };
    }

}