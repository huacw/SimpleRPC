package org.net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 随机策略
 *
 * @author sea
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public class ZKRandomLoadBalancerStrategy extends AbstractZKLoadBalancerStrategy {
    private static Random random = new Random();
    private ThreadLocal<Set<String>> localContainer = new ThreadLocal<>();

    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        String node = nodes.get(random.nextInt(nodes.size()));
        Set<String> nodeSet = localContainer.get();
        if (nodeSet == null) {
            nodeSet = new HashSet<>(16);
        }
        nodeSet.add(node);
        localContainer.set(nodeSet);
        return node;
    }

    @Override
    protected boolean isLastNode(List<String> nodes) {
        nodes.removeAll(localContainer.get());
        return nodes.isEmpty();
    }
}
