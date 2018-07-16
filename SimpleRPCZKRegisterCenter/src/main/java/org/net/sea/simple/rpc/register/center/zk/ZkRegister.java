package org.net.sea.simple.rpc.register.center.zk;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.server.RegisterCenterConfig;
import net.sea.simple.rpc.server.ServiceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.net.sea.simple.rpc.register.center.zk.config.ZKRegisterCenterConfig;
import org.net.sea.simple.rpc.register.center.zk.constants.ZKConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zookeeper服务注册器
 *
 * @author sea
 * @Date 2018/7/13 15:49
 * @Version 1.0
 */
@Component
public class ZkRegister implements IRegister {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ZKRegisterCenterConfig config;
    private ZkClient client;

    private ZkRegister() {
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        checkConfig(config);
        client = new ZkClient(config.getZkServers(), config.getSessionTimeout(), config.getConnectionTimeout());
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
    public boolean addNode(ServiceInfo service) {
        return false;
    }

    @Override
    public boolean removeNode(ServiceInfo service) {
        return false;
    }

    @Override
    public boolean reconnect(ServiceInfo service) {
        return false;
    }

    @Override
    public ServiceInfo findNode(ServiceInfo service) {
        return null;
    }

    @Override
    public boolean hasNode(ServiceInfo service) {
        return false;
    }
}
