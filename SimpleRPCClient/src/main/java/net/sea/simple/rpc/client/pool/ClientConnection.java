package net.sea.simple.rpc.client.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.sea.simple.rpc.client.config.ClientConfig;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 客户端连接
 *
 * @author sea
 * @Date 2018/8/9 17:35
 * @Version 1.0
 */
public class ClientConnection{
    private String host;
    private int port;
    private ClientConfig clientConfig;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ClientConnection that = (ClientConnection) o;

        return new EqualsBuilder()
                .append(port, that.port)
                .append(host, that.host)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(host)
                .append(port)
                .toHashCode();
    }

    /**
     * RPC客户端通道
     *
     * @author sea
     */
    public static class RPCClientChannel extends NioSocketChannel {

        private RPCResponse responseMessage;

        private ReentrantLock lock = new ReentrantLock();

        private Condition hasMessage = lock.newCondition();

        public void reset() {
            this.responseMessage = null;
        }

        public RPCResponse get(long timeout) throws InterruptedException {
            lock.lock();
            try {
                long start = System.currentTimeMillis();
                //在超时时间内取十次返回结果
                long waitInterval = timeout / 10;
                boolean timeoutFlag = false;
                while (responseMessage == null) {
                    boolean ok = hasMessage.await(waitInterval, TimeUnit.MILLISECONDS);
                    timeoutFlag = System.currentTimeMillis() - start > timeout;
                    if (ok || timeoutFlag) {
                        break;
                    }
                }
                if(timeoutFlag){
                    throw new RPCServerRuntimeException("RPC服务调用超时，请重试！");
                }
            } finally {
                lock.unlock();
            }
            return responseMessage;
        }

        public void set(RPCResponse message) {
            lock.lock();
            try {
                responseMessage = message;
                hasMessage.signal();
            } finally {
                lock.unlock();
            }
        }

    }
}
