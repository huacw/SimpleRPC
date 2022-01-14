package net.sea.simple.rpc.register.center.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 使用文件进行缓存
 * @Author: hcw
 * @Date: 2022/1/14 15:14
 */
@Configuration
@ConfigurationProperties(prefix = "register.center.cache.file")
@ConditionalOnProperty(name = "register.center.cache.type", havingValue = "file")
@Setter
@Getter
public class FileCacheConfig extends CacheConfig {
    private String cacheFileDir;//缓存文件目录
    private String cacheFileName;//缓存文件名
    private String cacheFilePath;//缓存文件绝对地址
}
