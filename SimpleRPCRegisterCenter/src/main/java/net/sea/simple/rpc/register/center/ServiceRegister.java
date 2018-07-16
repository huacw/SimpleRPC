package net.sea.simple.rpc.register.center;

import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.SpringUtils;

/**
 * 服务注册器
 *
 * @author sea
 * @Date 2018/7/13 15:54
 * @Version 1.0
 */
public class ServiceRegister {
    /**
     * 注册器对象
     */
    private static IRegister REG_INST;
    /**
     * 服务注册器对象
     */
    private static ServiceRegister serviceRegister;
    /**
     * 单例对象锁
     */
    private static Object lock;

    private ServiceRegister() {
    }

    /**
     * 实例化对象
     *
     * @return
     */
    public static ServiceRegister newInstance() {
        if (serviceRegister == null) {
            synchronized (lock) {
                if (serviceRegister == null) {
                    serviceRegister = new ServiceRegister();
                    REG_INST = SpringUtils.getBean(RegisterFactory.class).newRegister();
                }
            }
        }
        return serviceRegister;
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

    /**
     * 根据服务名称查询RPC服务还有可用几点
     *
     * @param serviceName
     * @return
     */
    public boolean hasNextServiceNode(String serviceName) {
        return REG_INST.hasNextServiceNode(serviceName);
    }
}
