package org.net.sea.simple.rpc.register.center.zk.config;

import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import org.net.sea.simple.rpc.register.center.zk.constants.ZKConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * zookeeper注册中心配置
 *
 * @author sea
 * @Date 2018/7/13 15:31
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "register.center.config.zk")
@Profile("zk-reg-center")
public class ZKRegisterCenterConfig extends RegisterCenterConfig {
    public ZKRegisterCenterConfig() {
        registerCenterType = ZKConstants.REGISTER_CENTER_TYPE;
    }

    private String zkServers;// zk服务器地址

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }
}
