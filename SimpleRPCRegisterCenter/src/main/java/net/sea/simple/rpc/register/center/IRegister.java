package net.sea.simple.rpc.register.center;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import net.sea.simple.rpc.server.ServiceInfo;

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
    public String getRegisterName();

    /**
     * 添加服务节点
     *
     * @param service
     * @return
     */
    public boolean addNode(ServiceInfo service);

    /**
     * 删除服务节点
     *
     * @param service
     * @return
     */
    public boolean removeNode(ServiceInfo service);

    /**
     * 服务重连
     *
     * @param service
     * @return
     */
    public boolean reconnect(ServiceInfo service);

    /**
     * 查找服务节点
     *
     * @param service
     * @return
     */
    public ServiceInfo findNode(ServiceInfo service);

    /**
     * 服务节点是否存在
     *
     * @param service
     * @return
     */
    public boolean hasNode(ServiceInfo service);
}

