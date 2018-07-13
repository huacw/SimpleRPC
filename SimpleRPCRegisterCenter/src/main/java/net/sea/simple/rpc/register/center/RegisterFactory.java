package net.sea.simple.rpc.register.center;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;

/**
 * 注册器工厂
 *
 * @author sea
 * @Date 2018/7/13 13:58
 * @Version 1.0
 */
public class RegisterFactory {
    private RegisterFactory() {
    }

    /**
     * 实例化服务注册器
     *
     * @return
     */
    public static IRegister newRegister(RegisterCenterConfig config) {
        if (config == null) {
            throw new RPCServerRuntimeException("注册中心配置为空");
        }
        String registerCenterType = config.getRegisterCenterType();
        switch (registerCenterType) {
            case "zk":
                ;
            case "etcd":
                ;
            default:
                new RPCServerRuntimeException(String.format("不支持的注册中心类型【%s】", registerCenterType));
        }
        return null;
    }
}
