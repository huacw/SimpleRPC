package net.sea.simple.rpc.server.loadbalancer.strategy.zk;

import java.util.List;
import java.util.Random;

/**
 * 随机策略
 *
 * @author sea
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public class ZKRandomLoadBalancerStrategy extends AbstractZKLoadBalancerStrategy {
    private static Random random = new Random();

    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        return nodes.get(random.nextInt(nodes.size()));
    }
}
