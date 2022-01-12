package net.sea.simple.rpc.server.config;

import net.sea.simple.rpc.constants.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务器配置
 *
 * @author sea
 */
@ConfigurationProperties(prefix = "rpc.server.config")
@Component
public class ServerConfig {
    private String serviceName;//服务名
    private String serviceIp;//服务地址
    private int port;//服务端口
    private boolean opened;//是否从配置中心拉取路由
    private Long heartPeriod;//服务心跳时间
    private String version = CommonConstants.DEFAULT_SERVICE_VERSION;//版本号

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public Long getHeartPeriod() {
        return heartPeriod;
    }

    public void setHeartPeriod(Long heartPeriod) {
        this.heartPeriod = heartPeriod;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
