package net.sea.simple.rpc.utils;

import org.h2.mvstore.ConcurrentArrayList;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC服务本地缓存
 *
 * @author sea
 * @Date 2018/8/7 14:13
 * @Version 1.0
 */
public class RPCServiceCache {
    // RPC服务缓存
    private Map<String, List<String>> SERVICE_CACHE = new ConcurrentHashMap<>();
    // 缓存对象
    private volatile static RPCServiceCache cache = null;

    /**
     * 实例化缓存对象
     *
     * @return
     */
    public static RPCServiceCache newCache() {
        if (cache == null) {
            synchronized (RPCServiceCache.class) {
                if (cache == null) {
                    cache = new RPCServiceCache();
                }
            }
        }
        return cache;
    }

    /**
     * 添加服务节点的缓存
     *
     * @param serviceName
     * @param nodes
     */
    public void addCache(String serviceName, List<String> nodes) {
        SERVICE_CACHE.put(serviceName, nodes);
    }

    /**
     * 添加服务节点的缓存
     *
     * @param serviceName
     * @param node
     */
    public void addCache(String serviceName, String node) {
        List<String> nodes = getCache(serviceName);
        if (nodes == null) {
            nodes = new ArrayList<>();
            SERVICE_CACHE.put(serviceName, nodes);
        }
        synchronized (this) {
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }
    }

    /**
     * 删除服务节点的缓存
     *
     * @param serviceName
     * @param node
     * @return
     */
    public boolean delCache(String serviceName, String node) {
        List<String> nodes = getCache(serviceName);
        if (CollectionUtils.isEmpty(nodes)) {
            return true;
        }
        return nodes.remove(node);
    }

    /**
     * 删除服务节点的缓存
     *
     * @param serviceName
     * @return
     */
    public boolean delCache(String serviceName) {
        return SERVICE_CACHE.remove(serviceName) != null;
    }

    /**
     * 获取服务节点
     *
     * @param serviceName
     * @return
     */
    public List<String> getCache(String serviceName) {
        return SERVICE_CACHE.get(serviceName);
    }
}
