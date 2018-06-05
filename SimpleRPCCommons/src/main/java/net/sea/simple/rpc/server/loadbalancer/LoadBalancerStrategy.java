package net.sea.simple.rpc.server.loadbalancer;

import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.loadbalancer.context.LoadBalancerContext;

/**
 * 负载均衡策略
 *
 * @author chengwu.hua
 * @Date 2018/4/25 15:24
 * @Version 1.0
 */
@FunctionalInterface
public interface LoadBalancerStrategy<T extends LoadBalancerContext> {
    /**
     * 选择服务节点
     *
     * @param context
     * @return
     */
    public ServiceInfo choose(T context);
}
