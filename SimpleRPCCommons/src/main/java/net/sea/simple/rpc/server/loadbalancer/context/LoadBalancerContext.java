package net.sea.simple.rpc.server.loadbalancer.context;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.server.ServiceInfo;

/**
 * 负载均衡策略上下文
 *
 * @author chengwu.hua
 * @Date 2018/4/25 15:27
 * @Version 1.0
 */
public class LoadBalancerContext {
    private ServiceInfo service;

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }
}
