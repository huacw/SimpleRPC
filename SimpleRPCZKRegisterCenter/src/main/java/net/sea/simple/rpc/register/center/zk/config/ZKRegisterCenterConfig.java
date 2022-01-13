package net.sea.simple.rpc.register.center.zk.config;

import net.sea.simple.rpc.register.center.config.RegisterCenterConfig;
import net.sea.simple.rpc.register.center.zk.constants.ZKConstants;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * zookeeper注册中心配置
 *
 * @author sea
 * @Date 2018/7/13 15:31
 * @Version 1.0
 */
@Component
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "register.center.config.zk")
//@Profile(ZKConstants.REGISTER_CENTER_PROFILE)
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
