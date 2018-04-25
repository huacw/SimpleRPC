package net.sea.simple.rpc.server.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.meta.ServiceMeta;

/**
 * 缓存类
 *
 * @author sea
 */
public final class RPCCache {
    // RPC服务缓存
    private Map<String, ServiceMeta> SERVICE_CACHE = new ConcurrentHashMap<>();
    // 缓存对象
    private volatile static RPCCache cache = null;

    /**
     * 实例化缓存对象
     *
     * @return
     */
    public static RPCCache newCache() {
        if (cache == null) {
            synchronized (RPCCache.class) {
                if (cache == null) {
                    cache = new RPCCache();
                }
            }
        }
        return cache;
    }

    /**
     * 注册rpc服务缓存
     *
     * @param meta
     */
    public synchronized void registerService(ServiceMeta meta) {
        if (meta == null) {
            throw new RPCServerRuntimeException("无效的服务");
        }
        String serviceName = meta.getServiceName();
        if (SERVICE_CACHE.containsKey(serviceName)) {
            throw new RPCServerRuntimeException(String.format("重复注册的RPC服务，服务名：%s", serviceName));
        } else {
            SERVICE_CACHE.put(serviceName, meta);
        }
    }

    /**
     * 注销rpc服务缓存
     *
     * @param serviceName
     * @return
     */
    public synchronized ServiceMeta unregisterService(String serviceName) {
        if (StringUtils.isBlank(serviceName)) {
            throw new RPCServerRuntimeException("无效的服务");
        }
        return SERVICE_CACHE.remove(serviceName);
    }

    /**
     * 注册rpc服务缓存（不判断是否已经注册）
     *
     * @param meta
     */
    public synchronized void repeatRegisterService(ServiceMeta meta) {
        if (meta == null) {
            throw new RPCServerRuntimeException("无效的服务");
        }
        SERVICE_CACHE.put(meta.getServiceName(), meta);
    }

    /**
     * 获取服务名原数据
     *
     * @param serviceName
     * @return
     */
    public synchronized ServiceMeta findService(String serviceName) {
        return SERVICE_CACHE.get(serviceName);
    }

    /**
     * 获取服务名原数据
     *
     * @param serviceType
     * @return
     */
    public synchronized ServiceMeta findService(Class<?> serviceType) {
        return findService(serviceType.getName());
    }
}
