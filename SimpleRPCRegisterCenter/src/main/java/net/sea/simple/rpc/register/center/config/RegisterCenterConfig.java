package net.sea.simple.rpc.register.center.config;

import net.sea.simple.rpc.constants.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 注册中心配置
 *
 * @author sea
 */
public abstract class RegisterCenterConfig {
    protected String registerCenterType = CommonConstants.DEFAULT_REGISTER_CENTER_TYPE;// 注册中心类型
    private int sessionTimeout = CommonConstants.DEFAULT_SESSION_TIMEOUT;// 会话超时时间
    private int connectionTimeout = CommonConstants.DEFAULT_CONNECTION_TIMEOUT;// 连接超时时间
    private String loadBalancer;//负载均衡策略

    public String getRegisterCenterType() {
        return registerCenterType;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(String loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public String toString() {
        return "RegisterCenterConfig{" +
                "registerCenterType='" + registerCenterType + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}
