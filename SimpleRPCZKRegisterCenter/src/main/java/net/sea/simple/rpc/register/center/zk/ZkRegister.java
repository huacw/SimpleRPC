package net.sea.simple.rpc.register.center.zk;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.IZkStateListener;
import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import net.sea.simple.rpc.register.center.loadbalancer.context.LoadBalancerContext;
import net.sea.simple.rpc.register.center.loadbalancer.strategy.LoadBalancerStrategy;
import net.sea.simple.rpc.register.center.zk.config.ZKRegisterCenterConfig;
import net.sea.simple.rpc.register.center.zk.constants.ZKConstants;
import net.sea.simple.rpc.register.center.zk.loadbalancer.context.ZKLoadBalancerContext;
import net.sea.simple.rpc.register.center.zk.loadbalancer.strategy.ZKRoundLoadBalancerStrategy;
import net.sea.simple.rpc.register.center.zk.utils.ZKUtils;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.JsonUtils;
import net.sea.simple.rpc.utils.RPCServiceCache;
import net.sea.simple.rpc.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * zookeeper服务注册器
 *
 * @author sea
 * @Date 2018/7/13 15:49
 * @Version 1.0
 */
@Component
@Profile(ZKConstants.REGISTER_CENTER_PROFILE)
public class ZkRegister implements IRegister {
    private Logger logger = Logger.getLogger(getClass());
    private ZKRegisterCenterConfig config;
    private ZkClient client;
    /**
     * 负载均衡器的映射
     */
    private Map<String, LoadBalancerStrategy> loadBalancerStrategyMapper = new HashMap<>(16);

    private ZkRegister() {
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        config = (ZKRegisterCenterConfig) SpringUtils.getBean(RegisterCenterConfig.class);
        logger.info("注册中心类型为：zookeeper");
        checkConfig(config);
        client = new ZkClient(config.getZkServers(), config.getSessionTimeout(), config.getConnectionTimeout());
    }

    /**
     * 添加服务节点
     *
     * @param service
     * @return
     */
    @Override
    public boolean addNode(ServiceInfo service) {
        if (service == null) {
            throw new RPCServerRuntimeException("无效的服务");
        }
        String serviceName = service.getServiceName();
        if (StringUtils.isBlank(serviceName)) {
            throw new IllegalArgumentException("无效的服务名");
        }
        String host = service.getHost();
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("无效的服务主机");
        }
        // 创建以服务命名的服务节点(临时节点)
        String serviceNodePath = buildNodePath(service, host);
        client.createEphemeral(serviceNodePath, buildServerInfoData(service));
        // 添加监听
        addWatcher(getServiceNameNode(service));
        logger.info(String.format("注册服务：%s", service.toString()));
        return true;
    }

    /**
     * 构建服务节点
     *
     * @param service
     * @param host
     * @return
     */
    private String buildNodePath(ServiceInfo service, String host) {
        String namePath = getServiceNameNode(service);
        if (!client.exists(namePath)) {
            client.createPersistent(namePath, true);
        }
        return namePath.concat("/").concat(host);
    }

    /**
     * @param service
     * @return
     */
    @Override
    public boolean reconnect(ServiceInfo service) {
        String path = buildNodePath(service, service.getHost());
        byte[] datas = buildServerInfoData(service);
        if (client.exists(path)) {
            client.writeData(path, datas);
        } else {
            client.createEphemeral(path, datas);
        }
        logger.info(String.format("注册服务：%s", service.toString()));
        return true;
    }

    /**
     * 构建服务信息
     *
     * @param service
     * @return
     */
    private byte[] buildServerInfoData(ServiceInfo service) {
        return JsonUtils.toJson(service).getBytes(CommonConstants.DEFAULT_CHARSET);
    }

    /**
     * 获取服务节点地址
     *
     * @param service
     * @return
     */
    private String getServiceNameNode(ServiceInfo service) {
        return ZKUtils.getServiceNameNode(service);
    }

    /**
     * 添加监听
     *
     * @param serviceNodePath
     */
    private void addWatcher(String serviceNodePath) {
        Watcher watcher = new Watcher();
        client.subscribeChildChanges(serviceNodePath, watcher);
        client.subscribeDataChanges(serviceNodePath, watcher);
        client.subscribeStateChanges(watcher);
    }

    /**
     * 删除服务节点
     *
     * @param service
     * @return
     */
    public boolean removeNode(ServiceInfo service) {
        String serviceName = service.getServiceName();
        if (StringUtils.isBlank(serviceName)) {
            return true;
        }
        String serviceNameNode = getServiceNameNode(service);
        String host = service.getHost();
        if (StringUtils.isBlank(host)) {
            return client.deleteRecursive(serviceNameNode);
        }
        return client.delete(serviceNameNode.concat("/").concat(host));
    }

    /**
     * 查找服务节点
     *
     * @param service
     * @return
     */
    public ServiceInfo findNode(ServiceInfo service) {
        try {
            LoadBalancerStrategy loadBalancerStrategy = getLoadBalancerStrategy();
            LoadBalancerContext context = new ZKLoadBalancerContext(client);
            context.setService(service);
            return loadBalancerStrategy.choose(context);
        } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e) {
            throw new RPCServerRuntimeException(e);
        }
    }

    /**
     * 获取负载均衡策略,默认为net.sea.simple.rpc.register.center.zk.loadbalancer.strategy.ZKRoundLoadBalancerStrategy（轮询策略）
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private LoadBalancerStrategy getLoadBalancerStrategy() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String loadBalancer = config.getLoadBalancer();
        LoadBalancerStrategy loadBalancerStrategy = null;
        if (StringUtils.isBlank(loadBalancer)) {
            String defaultLoadBalancerKey = "default";
            loadBalancerStrategy = loadBalancerStrategyMapper.get(defaultLoadBalancerKey);
            if (loadBalancerStrategy == null) {
                loadBalancerStrategy = new ZKRoundLoadBalancerStrategy();
                loadBalancerStrategyMapper.put(defaultLoadBalancerKey, loadBalancerStrategy);
            }
        } else {
            loadBalancerStrategy = loadBalancerStrategyMapper.get(loadBalancer);
            if (loadBalancerStrategy == null) {
                loadBalancerStrategy = (LoadBalancerStrategy) Class.forName(loadBalancer).newInstance();
                loadBalancerStrategyMapper.put(loadBalancer, loadBalancerStrategy);
            }
        }
        return loadBalancerStrategy;
    }

    /**
     * 服务节点是否存在
     *
     * @param service
     * @return
     */
    public boolean hasNode(ServiceInfo service) {
        return ZKUtils.hasNode(client, service);
    }

    /**
     * zk监听
     *
     * @author sea
     */
    private class Watcher implements IZkChildListener, IZkStateListener, IZkDataListener {

        @Override
        public void handleDataChange(String dataPath, byte[] data) throws Exception {
            // TODO Auto-generated method stub
            System.out.println("dataPath = [" + dataPath + "], data = [" + data + "]");
        }

        @Override
        public void handleDataDeleted(String dataPath) throws Exception {
            // TODO Auto-generated method stub
            System.out.println("dataPath = [" + dataPath + "]");
        }

        @Override
        public void handleStateChanged(KeeperState state) throws Exception {
            // TODO Auto-generated method stub
            System.out.println("===========current state:" + state);
        }

        @Override
        public void handleNewSession() throws Exception {
            // TODO Auto-generated method stub
            System.out.println("===========handleNewSession");
        }

        @Override
        public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
            System.out.println("parentPath = [" + parentPath + "], currentChildren = [" + currentChildren + "]");
            RPCServiceCache.newCache().addCache(parentPath.substring(parentPath.lastIndexOf("/") + 1), currentChildren);
        }
    }

    /**
     * 检查配置
     *
     * @param config
     */
    private void checkConfig(ZKRegisterCenterConfig config) {
        if (config == null || StringUtils.isBlank(config.getZkServers())) {
            throw new RPCServerRuntimeException(String.format("【\n%s\n】配置缺失",
                                                              "register.center.config.zk.zkServers或\nregister:\n\tcenter:\n\t\tconfig:\n\t\t\tzk:\n\t\t\t\tzkServers"));
        }
    }

    @Override
    public String getRegisterName() {
        return ZKConstants.REGISTER_CENTER_TYPE;
    }

    @Override
    public boolean hasNextServiceNode(String serviceName) {
        try {
            LoadBalancerStrategy loadBalancerStrategy = getLoadBalancerStrategy();
            LoadBalancerContext context = new ZKLoadBalancerContext(client);
            context.setService(new ServiceInfo(serviceName));
            return loadBalancerStrategy.hasNextServiceNode(context);
        } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e) {
            throw new RPCServerRuntimeException(e);
        }
    }
}
