package net.sea.simple.rpc.register.center.cache.config;

import net.sea.simple.rpc.register.center.cache.IServiceRouterCache;
import net.sea.simple.rpc.register.center.cache.impl.FileServiceRouterCache;
import net.sea.simple.rpc.register.center.cache.impl.MemoryServiceRouterCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description: 路由缓存配置
 * @Author: hcw
 * @Date: 2022/1/14 11:21
 */
@Configuration
@ConditionalOnProperty(name = "register.center.cache.enable", havingValue = "true")
public class RouterCacheConfig {

    @Autowired
    private CacheConfig config;

    @Bean("memoryServiceRouterCache")
    @Primary
    @ConditionalOnProperty(name = "register.center.cache.type", havingValue = "memory", matchIfMissing = true)
    public IServiceRouterCache memoryServiceRouterCache() {
        MemoryServiceRouterCache memoryServiceRouterCache = new MemoryServiceRouterCache();
        memoryServiceRouterCache.initCache(config);
        return memoryServiceRouterCache;
    }

    @Bean("fileServiceRouterCache")
    @ConditionalOnProperty(name = "register.center.cache.type", havingValue = "file")
    public IServiceRouterCache fileServiceRouterCache() {
        FileServiceRouterCache fileServiceRouterCache = new FileServiceRouterCache();
        fileServiceRouterCache.initCache(config);
        return fileServiceRouterCache;
    }
}
