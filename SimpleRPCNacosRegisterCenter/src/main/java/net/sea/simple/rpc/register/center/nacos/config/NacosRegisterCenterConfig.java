package net.sea.simple.rpc.register.center.nacos.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import net.sea.simple.rpc.register.center.nacos.constants.NacosConstants;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: hcw
 * @Date: 2021/1/19 10:28
 */
@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "register.center.config.nacos")
@Profile(NacosConstants.REGISTER_CENTER_PROFILE)
public class NacosRegisterCenterConfig extends RegisterCenterConfig {
    private NacosRegisterCenterConfig() {
        registerCenterType = NacosConstants.REGISTER_CENTER_TYPE;
    }

    /**
     * nacos注册中心地址
     */
    @Setter
    @Getter
    private String serverAddresses;
    /**
     * 注册的命名空间
     */
    @Setter
    @Getter
    private String namespace = "public";

    /**
     * 是否需要权限认证
     */
    @Setter
    @Getter
    private boolean needAuth = false;
    /**
     * nacos服务器用户名
     */
    @Setter
    @Getter
    private String username;
    /**
     * nacos服务器用户名密码
     */
    @Setter
    @Getter
    private String password;
}
