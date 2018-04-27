package net.sea.simple.rpc.server.loadbalancer.strategy.zk;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.loadbalancer.LoadBalancerStrategy;
import net.sea.simple.rpc.server.loadbalancer.context.LoadBalancerContext;
import net.sea.simple.rpc.server.loadbalancer.context.zk.ZKLoadBalancerContext;
import net.sea.simple.rpc.utils.JsonUtils;
import net.sea.simple.rpc.utils.ZKUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽象策略
 *
 * @author chengwu.hua
 * @Date 2018/4/25 15:45
 * @Version 1.0
 */
public abstract class AbstractZKLoadBalancerStrategy implements LoadBalancerStrategy {
    @Override
    public ServiceInfo choose(LoadBalancerContext context) {
        ZKLoadBalancerContext ctx = (ZKLoadBalancerContext) context;
        ServiceInfo service = ctx.getService();
        ZkClient client = ctx.getZkClient();
        if (!ZKUtils.hasNode(client, service)) {
            throw new RPCServerRuntimeException(String.format("未找到服务：%s", service.getServiceName()));
        }
        String serviceNameNode = ZKUtils.getServiceNameNode(service);
        List<String> children = client.getChildren(serviceNameNode);
        if (CollectionUtils.isEmpty(children)) {
            throw new RPCServerRuntimeException(String.format("服务：%s未找到节点", service.getServiceName()));
        }
        String node = serviceNameNode.concat("/").concat(chooseNode(dealNodes(children)));
        byte[] bytes = client.readData(node);
        return JsonUtils.toBean(new String(bytes, CommonConstants.DEFAULT_CHARSET), ServiceInfo.class);
    }

    /**
     * 选择节点
     *
     * @param nodes
     * @return
     */
    protected abstract String chooseNode(List<String> nodes);

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
