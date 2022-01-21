package net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import net.sea.simple.rpc.register.center.zk.loadbalancer.context.ZKLoadBalancerContext;
import net.sea.simple.rpc.register.center.zk.utils.ZKUtils;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.RPCServiceCache;
import org.springframework.util.CollectionUtils;

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
    protected synchronized ServiceInfo getServiceInfoFromCache(ZKLoadBalancerContext ctx) {
        ServiceInfo service = ctx.getService();
        List<ServiceInfo> serviceInfoList = ctx.getServiceRouterCache().findServiceInfo(service.getServiceName(), service.getVersion());
        if (CollectionUtils.isEmpty(serviceInfoList)) {
            return null;
        }
        return choose(serviceInfoList, (ServiceInfo node) -> ZKUtils.getServiceNameNode(node));
    }

    @FunctionalInterface
    private interface NodeConverter<T> {
        String convert(T t);
    }

    /**
     * 选择节点
     *
     * @param nodes
     * @param converter
     * @param <T>
     * @return
     */
    private <T> T choose(List<T> nodes, NodeConverter<T> converter) {
        RPCServiceCache rpcServiceCache = RPCServiceCache.newCache();
        T node = nodes.get(random.nextInt(nodes.size()));
        Set<String> nodeSet = rpcServiceCache.getAttr(CURRENT_NODES);
        if (nodeSet == null) {
            nodeSet = new HashSet<>(16);
        }
        nodeSet.add(converter.convert(node));
        rpcServiceCache.putAttr(CURRENT_NODES, nodeSet);
        return node;
    }

    @Override
    protected synchronized String chooseNode(List<String> nodes) {
        return choose(nodes, (String node) -> node);
    }

    @Override
    protected boolean isLastNode(List<String> nodes) {
        nodes.removeAll(RPCServiceCache.newCache().getAttr(CURRENT_NODES));
        return nodes.isEmpty();
    }
}
