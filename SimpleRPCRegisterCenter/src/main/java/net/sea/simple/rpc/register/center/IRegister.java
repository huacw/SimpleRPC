package net.sea.simple.rpc.register.center;

import net.sea.simple.rpc.server.ServiceInfo;

import java.util.List;

/**
 * 注册器接口
 *
 * @author sea
 * @Date 2018/6/5 11:50
 * @Version 1.0
 */
public interface IRegister {
    /**
     * 获取注册器名称
     *
     * @return
     */
    String getRegisterName();

    /**
     * 获取注册中心签名
     *
     * @return
     */
    String getRGSign();

    /**
     * 添加服务节点
     *
     * @param service
     * @return
     */
    boolean addNode(ServiceInfo service);

    /**
     * 删除服务节点
     *
     * @param service
     * @return
     */
    boolean removeNode(ServiceInfo service);

    /**
     * 服务重连
     *
     * @param service
     * @return
     */
    boolean reconnect(ServiceInfo service);

    /**
     * 查找服务节点
     *
     * @param service
     * @return
     */
    ServiceInfo findNode(ServiceInfo service);

    /**
     * 服务节点是否存在
     *
     * @param service
     * @return
     */
    boolean hasNode(ServiceInfo service);

    /**
     * 是否还有可用服务节点
     *
     * @param serviceName
     * @return
     */
    boolean hasNextServiceNode(String serviceName);

    /**
     * 查询所有可用服务
     *
     * @return
     */
    List<ServiceInfo> findAllAvailableServices();
}

