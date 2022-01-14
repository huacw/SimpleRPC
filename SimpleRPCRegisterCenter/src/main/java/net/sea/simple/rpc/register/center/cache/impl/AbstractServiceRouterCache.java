package net.sea.simple.rpc.register.center.cache.impl;

import lombok.extern.slf4j.Slf4j;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.register.center.cache.IServiceRouterCache;
import net.sea.simple.rpc.register.center.cache.config.CacheConfig;
import net.sea.simple.rpc.server.ServiceInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 抽象的路由缓存
 * @Author: hcw
 * @Date: 2022/1/14 11:18
 */
@Slf4j
public abstract class AbstractServiceRouterCache implements IServiceRouterCache {
    protected IRegister register;
    protected CacheConfig config;
    //服务带版本号映射
    protected Map<String, List<ServiceInfo>> serviceWithVersionMapper = new ConcurrentHashMap<>(16);

    public AbstractServiceRouterCache() {
        log.info("当前缓存类型：{}", getCacheType());
    }

    /**
     * 初始化缓存配置
     *
     * @param cacheConfig
     */
    @Override
    public void initCache(CacheConfig cacheConfig) {
        this.config = cacheConfig;
        this.register = cacheConfig.getRegister();
        initAfter();
        //加载所有可用服务路由
        loadAvailableServices();
        //启动定时刷新任务
        startRefreshRouterTask();
    }

    /**
     * 初始化配置后的处理
     */
    protected void initAfter() {

    }

    /**
     * 获取缓存类型
     *
     * @return
     */
    protected abstract String getCacheType();

    /**
     * 启动定时刷新服务路由的任务
     */
    private void startRefreshRouterTask() {
        long refreshPeriod = config.getRefreshPeriod();
        String cacheType = getCacheType();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("缓存【{}】刷新所有可用服务开始", cacheType);
                try {
                    loadAvailableServices();
                    log.info("缓存【{}】刷新所有可用服务结束", cacheType);
                } catch (Exception e) {
                    log.error(String.format("缓存【%s】刷新所有可用服务异常", cacheType), e);
                }
            }
        }, refreshPeriod, refreshPeriod);
    }

    @Override
    public List<ServiceInfo> findServiceInfo(String serviceName, String version) {
        return serviceWithVersionMapper.get(String.format("%s_%s", serviceName, version));
    }

    @Override
    public List<ServiceInfo> findServiceInfo(String serviceName) {
        return findServiceInfo(serviceName, CommonConstants.DEFAULT_SERVICE_VERSION);
    }
}
