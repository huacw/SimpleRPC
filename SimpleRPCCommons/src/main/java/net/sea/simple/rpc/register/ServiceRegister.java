package net.sea.simple.rpc.register;

import java.util.List;

import net.sea.simple.rpc.server.loadbalancer.strategy.zk.ZKRoundLoadBalancerStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.IZkStateListener;
import com.github.zkclient.ZkClient;

import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.RegisterCenterConfig;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.loadbalancer.LoadBalancerStrategy;
import net.sea.simple.rpc.server.loadbalancer.context.LoadBalancerContext;
import net.sea.simple.rpc.server.loadbalancer.context.zk.ZKLoadBalancerContext;
import net.sea.simple.rpc.utils.JsonUtils;
import net.sea.simple.rpc.utils.ZKUtils;

/**
 * 服务注册器
 *
 * @author sea
 */
public class ServiceRegister {
    //注册配置
    private static RegisterCenterConfig config;
    private Register REG_INST;

    public ServiceRegister(RegisterCenterConfig config) {
        ServiceRegister.config = config;
        REG_INST = Register.newInstance();
    }

    /**
     * 注册器
     *
     * @author sea
     */
    private static class Register {
        private Logger logger = Logger.getLogger(getClass());
        private volatile static Register register = null;
        private ZkClient client;

        private Register() {
            init();
        }

        /**
         * 初始化方法
         */
        private void init() {
            checkConfig(config);
            client = new ZkClient(config.getZkServers(), config.getSessionTimeout(), config.getConnetionTimeout());
        }

        /**
         * 检查配置
         *
         * @param config
         */
        private void checkConfig(RegisterCenterConfig config) {
            if (config == null || StringUtils.isBlank(config.getZkServers())) {
                throw new RPCServerRuntimeException(String.format("【\n%s\n】配置缺失",
                                                                  "register.center.config.zkServers或\nregister:\n\tcenter:\n\t\tconfig:\n\t\t\tzkServers"));
            }
        }

        /**
         * 添加服务节点
         *
         * @param service
         * @return
         */
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
         * 获取负载均衡策略,默认为net.sea.simple.rpc.server.loadbalancer.strategy.zk.ZKRoundLoadBalancerStrategy（轮询策略）
         *
         * @return
         * @throws InstantiationException
         * @throws IllegalAccessException
         * @throws ClassNotFoundException
         */
        private LoadBalancerStrategy getLoadBalancerStrategy() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
            String loadBalancer = config.getLoadBalancer();
            if (StringUtils.isBlank(loadBalancer)) {
                return new ZKRoundLoadBalancerStrategy();
            } else {
                return (LoadBalancerStrategy) Class.forName(loadBalancer).newInstance();
            }
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
         * 创建实例
         *
         * @return
         */
        public static Register newInstance() {
            if (register == null) {
                synchronized (ServiceRegister.class) {
                    if (register == null) {
                        register = new Register();
                    }
                }
            }
            return register;
        }
    }

    /**
     * zk监听
     *
     * @author sea
     */
    private static class Watcher implements IZkChildListener, IZkStateListener, IZkDataListener {

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
            // TODO Auto-generated method stub
            System.out.println("parentPath = [" + parentPath + "], currentChildren = [" + currentChildren + "]");

        }

    }

    /**
     * 注册服务
     *
     * @param service
     * @return
     */
    public boolean register(ServiceInfo service) {
        return REG_INST.addNode(service);
    }

    /**
     * 重新注册服务
     *
     * @param service
     * @return
     */
    public boolean registerAgain(ServiceInfo service) {
        return REG_INST.reconnect(service);
    }

    /**
     * 根据服务名称注销服务
     *
     * @param serviceName
     * @return
     */
    public boolean unregister(String serviceName) {
        return unregister(new ServiceInfo(serviceName));
    }

    /**
     * 根据服务名称和地址注销服务
     *
     * @param serviceName
     * @param host
     * @return
     */
    public boolean unregister(String serviceName, String host) {
        return unregister(new ServiceInfo(serviceName, host));
    }

    /**
     * 注销服务
     *
     * @param service
     * @return
     */
    public boolean unregister(ServiceInfo service) {
        return REG_INST.removeNode(service);
    }

    /**
     * 根据服务名称查询RPC服务
     *
     * @param serviceName
     * @return
     */
    public ServiceInfo findService(String serviceName) {
        return findService(new ServiceInfo(serviceName));
    }

    /**
     * 根据服务名称和地址查询PRC服务
     *
     * @param serviceName
     * @param host
     * @return
     */
    public ServiceInfo findService(String serviceName, String host) {
        return findService(new ServiceInfo(serviceName, host));
    }

    /**
     * 查找RPC服务
     *
     * @param service
     * @return
     */
    public ServiceInfo findService(ServiceInfo service) {
        return REG_INST.findNode(service);
    }

    /**
     * 根据服务名称查询RPC服务是否存在
     *
     * @param serviceName
     * @return
     */
    public boolean hasService(String serviceName) {
        return hasService(new ServiceInfo(serviceName));
    }

    /**
     * 根据服务名称和地址查询PRC服务是否存在
     *
     * @param serviceName
     * @param host
     * @return
     */
    public boolean hasService(String serviceName, String host) {
        return hasService(new ServiceInfo(serviceName, host));
    }

    /**
     * 查询RPC服务是否存在
     *
     * @param service
     * @return
     */
    public boolean hasService(ServiceInfo service) {
        return REG_INST.hasNode(service);
    }
}
