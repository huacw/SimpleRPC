package net.sea.simple.rpc.register.center.zk.loadbalancer.context;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.register.center.cache.IServiceRouterCache;
import net.sea.simple.rpc.register.center.loadbalancer.context.LoadBalancerContext;
import net.sea.simple.rpc.utils.SpringUtils;

/**
 * 负载均衡策略上下文
 *
 * @author sea
 * @Date 2018/4/25 15:27
 * @Version 1.0
 */
public class ZKLoadBalancerContext extends LoadBalancerContext {
    private ZkClient zkClient;
    private IServiceRouterCache serviceRouterCache;

    public ZKLoadBalancerContext(ZkClient zkClient) {
        this.zkClient = zkClient;
        this.serviceRouterCache = SpringUtils.getBean(IServiceRouterCache.class);
    }

    public ZKLoadBalancerContext(ZkClient zkClient, IServiceRouterCache serviceRouterCache) {
        this.zkClient = zkClient;
        this.serviceRouterCache = serviceRouterCache;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public IServiceRouterCache getServiceRouterCache() {
        return serviceRouterCache;
    }
}
