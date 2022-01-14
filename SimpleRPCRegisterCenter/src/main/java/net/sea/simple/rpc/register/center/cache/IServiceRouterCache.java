package net.sea.simple.rpc.register.center.cache;

import net.sea.simple.rpc.register.center.cache.config.CacheConfig;
import net.sea.simple.rpc.server.ServiceInfo;

import java.util.List;

/**
 * @Description: 服务路由缓存
 * @Author: hcw
 * @Date: 2022/1/13 17:07
 */
public interface IServiceRouterCache {

    /**
     * 添加注册器
     *
     * @param cacheConfig
     */
    void initCache(CacheConfig cacheConfig);

    /**
     * 加载所有可用的服务列表
     */
    void loadAvailableServices();

    /**
     * 根据服务名及版本号查找可用服务
     *
     * @param serviceName 服务名
     * @param version     版本号
     * @return
     */
    List<ServiceInfo> findServiceInfo(String serviceName, String version);

    /**
     * 根据服务名及版本号查找可用服务
     *
     * @param serviceName 服务名
     * @return
     */
    List<ServiceInfo> findServiceInfo(String serviceName);
}
