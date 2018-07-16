package net.sea.simple.rpc.register.center.loadbalancer.strategy;

import net.sea.simple.rpc.register.center.loadbalancer.context.LoadBalancerContext;
import net.sea.simple.rpc.server.ServiceInfo;

/**
 * 负载均衡策略
 *
 * @author sea
 * @Date 2018/4/25 15:24
 * @Version 1.0
 */
public interface LoadBalancerStrategy<T extends LoadBalancerContext> {
    /**
     * 选择服务节点
     *
     * @param context
     * @return
     */
    public ServiceInfo choose(T context);

    /**
     * 是否还有下一个节点
     *
     * @param context
     * @return
     */
    public boolean hasNextServiceNode(T context);
}
