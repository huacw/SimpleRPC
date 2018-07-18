package net.sea.simple.rpc.register.center.zk.loadbalancer.context;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.register.center.loadbalancer.context.LoadBalancerContext;

/**
 * 负载均衡策略上下文
 *
 * @author sea
 * @Date 2018/4/25 15:27
 * @Version 1.0
 */
public class ZKLoadBalancerContext extends LoadBalancerContext {
    private ZkClient zkClient;

    public ZKLoadBalancerContext(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }
}
