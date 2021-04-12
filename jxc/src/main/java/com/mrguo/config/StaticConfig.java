package com.mrguo.config;

import com.mrguo.entity.config.PriceType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/43:58 PM
 * @updater 郭成兴
 * @updatedate 2020/6/43:58 PM
 */

@Data
@Configuration
@PropertySource(encoding = "UTF-8", value = "classpath:application-init.properties")
@ConfigurationProperties(prefix = "init")
public class StaticConfig {
    private List<PriceType> priceTypes;
    private List<String> filterTableName;
    private Map<String, String> permissions;
    private Map<String, String> permissionsParent;
    private List<String> apiWhiteList;
}
