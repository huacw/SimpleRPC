package net.sea.simple.rpc.server;

import net.sea.simple.rpc.constants.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 注册中心配置
 *
 * @author sea
 */
@ConfigurationProperties(prefix = "register.center.config")
@Component
public class RegisterCenterConfig {
    private String zkServers;// zk服务器地址
    private int sessionTimeout = CommonConstants.DEFAULT_HEART_TIMEOUT;// 会话超时时间
    private int connetionTimeout = CommonConstants.DEFAULT_CONNECTION_TIMEOUT;// 连接超时时间

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnetionTimeout() {
        return connetionTimeout;
    }

    public void setConnetionTimeout(int connetionTimeout) {
        this.connetionTimeout = connetionTimeout;
    }


    @Override
    public String toString() {
        return "RegisterCenterConfig{" +
                "zkServers='" + zkServers + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connetionTimeout=" + connetionTimeout +
                '}';
    }
}
