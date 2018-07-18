package net.sea.simple.rpc.register.center.zk.loadbalancer.strategy;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.loadbalancer.strategy.LoadBalancerStrategy;
import net.sea.simple.rpc.register.center.zk.loadbalancer.context.ZKLoadBalancerContext;
import net.sea.simple.rpc.register.center.zk.utils.ZKUtils;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.JsonUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽象策略
 *
 * @author sea
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public abstract class AbstractZKLoadBalancerStrategy implements LoadBalancerStrategy<ZKLoadBalancerContext> {
    @Override
    public ServiceInfo choose(ZKLoadBalancerContext ctx) {
        List<String> children = new ArrayList<>();
        String node = findServiceNodes(ctx, children).concat("/").concat(chooseNode(dealNodes(children)));
        byte[] bytes = ctx.getZkClient().readData(node);
        return JsonUtils.toBean(new String(bytes, CommonConstants.DEFAULT_CHARSET), ServiceInfo.class);
    }

    @Override
    public boolean hasNextServiceNode(ZKLoadBalancerContext ctx) {
        List<String> children = new ArrayList<>();
        findServiceNodes(ctx, children);
        return isLastNode(children);
    }

    /**
     * 查询服务节点
     *
     * @param ctx
     * @param children
     * @return
     */
    private String findServiceNodes(ZKLoadBalancerContext ctx, List<String> children) {
        ServiceInfo service = ctx.getService();
        ZkClient client = ctx.getZkClient();
        if (!ZKUtils.hasNode(client, service)) {
            throw new RPCServerRuntimeException(String.format("未找到服务：%s", service.getServiceName()));
        }
        String serviceNameNode = ZKUtils.getServiceNameNode(service);
        children.addAll(client.getChildren(serviceNameNode));
        if (CollectionUtils.isEmpty(children)) {
            throw new RPCServerRuntimeException(String.format("服务：%s未找到节点", service.getServiceName()));
        }
        return serviceNameNode;
    }

    /**
     * 选择节点
     *
     * @param nodes
     * @return
     */
    protected abstract String chooseNode(List<String> nodes);

    /**
     * 选择节点
     *
     * @param nodes
     * @return
     */
    protected abstract boolean isLastNode(List<String> nodes);

    /**
     * 处理节点信息
     *
     * @param nodes
     * @return
     */
    private List<String> dealNodes(List<String> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }
        List<String> newNodes = new ArrayList<>();
        for (String node : nodes) {
            if (node.startsWith("[")) {
                node = node.substring(1);
            }
            if (node.endsWith("]")) {
                node = node.substring(0, node.length() - 1);
            }
            newNodes.add(node.trim());
        }
        return newNodes;
    }
}
