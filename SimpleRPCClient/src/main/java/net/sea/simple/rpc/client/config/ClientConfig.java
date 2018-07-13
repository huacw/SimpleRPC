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
    private boolean retryEnable = CommonConstants.DEFAULT_CLIENT_RETRY_ENABLE;//客户端重试是否开启
    private int retryCount = CommonConstants.DEFAULT_CLIENT_RETRY_COUNT;//客户端重试次数

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isRetryEnable() {
        return retryEnable;
    }

    public void setRetryEnable(boolean retryEnable) {
        this.retryEnable = retryEnable;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
