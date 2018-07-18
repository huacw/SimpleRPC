package net.sea.simple.rpc.register.center;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 注册器工厂
 *
 * @author sea
 * @Date 2018/7/13 13:58
 * @Version 1.0
 */
//@Component
class RegisterFactory {
    @Autowired
    private List<IRegister> registers;
    @Autowired
    private RegisterCenterConfig config;

    /**
     * 实例化服务注册器
     *
     * @return
     */
    public IRegister newRegister() {
        if (config == null) {
            throw new RPCServerRuntimeException("注册中心配置为空");
        }
        if (CollectionUtils.isEmpty(registers)) {
            throw new RPCServerRuntimeException("系统缺失服务注册器");
        }
        String registerCenterType = config.getRegisterCenterType();
        if (StringUtils.isEmpty(registerCenterType)) {
            throw new RPCServerRuntimeException("注册器类型为空");
        }
        for (IRegister register : registers) {
            if (registerCenterType.equalsIgnoreCase(register.getRegisterName())) {
                return register;
            }
        }
        throw new RPCServerRuntimeException(String.format("未知的注册器类型：【%s】", registerCenterType));
    }
}
