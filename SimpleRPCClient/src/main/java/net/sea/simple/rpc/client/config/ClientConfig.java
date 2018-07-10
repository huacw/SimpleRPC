package net.sea.simple.rpc.client.config;

import net.sea.simple.rpc.constants.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sea
 * @Date 2018/7/10 17:53
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "rpc.client.config")
@Component
public class ClientConfig {
    private int connectionTimeout = CommonConstants.DEFAULT_CLIENT_CONNECTION_TIMEOUT;//客户端超时时间

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
