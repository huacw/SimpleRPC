package net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import java.util.List;

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
        Integer index = localContainer.get();
        if (index == null) {
            index = 0;
        } else {
            index++;
        }
        index = index % nodes.size();
        localContainer.set(index);
        return nodes.get(index);
    }

    @Override
    protected boolean isLastNode(List<String> nodes) {
        return localContainer.get() == nodes.size() - 1;
    }
}
