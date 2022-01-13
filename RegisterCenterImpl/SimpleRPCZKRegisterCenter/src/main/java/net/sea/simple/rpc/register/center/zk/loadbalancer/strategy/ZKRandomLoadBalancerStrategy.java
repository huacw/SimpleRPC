package net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import net.sea.simple.rpc.utils.RPCServiceCache;

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
    private static final String CURRENT_NODES = "load_balancer_strategy_current_nodes";


    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        RPCServiceCache rpcServiceCache = RPCServiceCache.newCache();
        String node = nodes.get(random.nextInt(nodes.size()));
        Set<String> nodeSet = rpcServiceCache.getAttr(CURRENT_NODES);
        if (nodeSet == null) {
            nodeSet = new HashSet<>(16);
        }
        nodeSet.add(node);
        rpcServiceCache.putAttr(CURRENT_NODES, nodeSet);
        return node;
    }

    @Override
    protected boolean isLastNode(List<String> nodes) {
        nodes.removeAll(RPCServiceCache.newCache().getAttr(CURRENT_NODES));
        return nodes.isEmpty();
    }
}
