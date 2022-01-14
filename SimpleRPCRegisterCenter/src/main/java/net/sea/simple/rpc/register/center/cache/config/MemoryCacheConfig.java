package net.sea.simple.rpc.register.center.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: hcw
 * @Date: 2022/1/14 15:14
 */
@Configuration
@ConfigurationProperties(prefix = "register.center.cache.memory")
@ConditionalOnProperty(name = "register.center.cache.type", havingValue = "memory", matchIfMissing = true)
@Setter
@Getter
public class MemoryCacheConfig extends CacheConfig {
}
