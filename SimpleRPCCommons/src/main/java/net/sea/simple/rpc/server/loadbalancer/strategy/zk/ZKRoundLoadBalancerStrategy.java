package net.sea.simple.rpc.server.loadbalancer.strategy.zk;

import java.util.List;

/**
 * 轮询策略
 *
 * @author sea
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public class ZKRoundLoadBalancerStrategy extends AbstractZKLoadBalancerStrategy {
    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        String node = nodes.remove(0);
        nodes.add(nodes.size(), node);
        return node;
    }
}
