package org.net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 *
 * @author sea
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public class ZKRoundLoadBalancerStrategy extends AbstractZKLoadBalancerStrategy {
    private ThreadLocal<Integer> localContainer = new ThreadLocal<>();

    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        String node = nodes.remove(0);
        nodes.add(nodes.size(), node);
        Integer index = localContainer.get();
        if (index == null) {
            index = 1;
        } else {
            index++;
        }
        localContainer.set(index);
        return node;
    }

    @Override
    protected boolean isLastNode(List<String> nodes) {
        return localContainer.get() == nodes.size();
    }
}
