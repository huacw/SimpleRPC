package net.sea.simple.rpc.client.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import lombok.Setter;
import net.sea.simple.rpc.client.config.ClientConfig;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.data.codec.RPCMessageDecoder;
import net.sea.simple.rpc.data.codec.RPCMessageEncoder;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.utils.SpringUtils;
import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 客户端连接池
 *
 * @author sea
 * @Date 2018/8/9 18:00
 * @Version 1.0
 */
public class ClientConnectionPool {
    private Logger logger = Logger.getLogger(getClass());
    private FixedChannelPool connPool = null;
    /**
     * 连接是否有效标记
     */
    private boolean validFlag = true;
    /**
     * 最后使用时间戳
     */
    @Setter
    private long lastUseTime;
    /**
     * 客户端连接
     */
    private ClientConnection conn;


    public ClientConnectionPool(ClientConnection conn) {
        this.conn = conn;
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        ClientConfig clientConfig = conn.getClientConfig();
        if (clientConfig == null) {
            clientConfig = SpringUtils.getBean(ClientConfig.class);
        }

        bootstrap.group(group)
                .channel(ClientConnection.RPCClientChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .remoteAddress(conn.getHost(), conn.getPort());
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline()
//                                .addLast("decoder", new RPCMessageDecoder(1024 * 1024, 4, 4))
//                                .addLast("encoder", new RPCMessageEncoder())
//                                .addLast("readTimeoutHandler", readTimeoutHandler)
//                                .addLast("clientHandler", new ClientHandler());
//                    }
//                });
        connPool = new FixedChannelPool(bootstrap, new PoolHandler(clientConfig), clientConfig.getMaxConnections());
        //长时间未使用连接进行释放
        watchConnection(group);
//        //监控连接是否异常
//        new Thread(() -> {
//            Timer timer = new Timer("timer");
//            final long start = System.currentTimeMillis();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Channel channel = bootstrap.connect().channel();
//                    System.out.println(LocalDateTime.now() + "," + Thread.currentThread().getName() + " run");
//                    validFlag = channel.isActive();
//                    System.out.println("validFlag:" + validFlag);
//                    if (validFlag) {
//                        channel.close();
//                        group.shutdownGracefully();
//                        timer.cancel();
//                        return;
//                    }
//                }
//            }, 1000, 1000);
//        }).start();
    }

    /**
     * 监测客户端连接使用情况
     * @param group
     */
    private void watchConnection(NioEventLoopGroup group) {
        logger.info("启动监测客户端连接使用线程");
        new Thread(() -> {
            Timer timer = new Timer("监测客户端连接使用情况");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("检测客户端连接");
                    if (System.currentTimeMillis() - lastUseTime > CommonConstants.MAX_CLIENT_IDLE_TIME) {
                        logger.info("客户端长时间未连接，连接释放");
                        connPool.close();
                        group.shutdownGracefully();
                        timer.cancel();
                    }
                }
            }, 1000, 1000);
        }).start();
    }

    /**
     * 连接池handler
     */
    private class PoolHandler extends AbstractChannelPoolHandler {
        private Logger logger = Logger.getLogger(getClass());
        private ClientConfig clientConfig;

        public PoolHandler(ClientConfig clientConfig) {
            this.clientConfig = clientConfig;
        }

        @Override
        public void channelCreated(Channel ch) throws Exception {
            logger.info("channel created");
            ClientConnection.RPCClientChannel channel = (ClientConnection.RPCClientChannel) ch;
            SocketChannelConfig config = channel.config();
            config.setKeepAlive(true);
            config.setTcpNoDelay(true);
            channel.pipeline()
                    .addLast("decoder", new RPCMessageDecoder(CommonConstants.MAX_MESSAGE_LENGTH, 4, 4))
                    .addLast("encoder", new RPCMessageEncoder())
                    .addLast("readTimeoutHandler", new ReadTimeoutHandler(clientConfig.getConnectionTimeout()))
                    .addLast("clientHandler", new ClientHandler());
        }
    }

    /**
     * 客户端消息处理器
     *
     * @author sea
     */
    private class ClientHandler extends SimpleChannelInboundHandler<RPCResponse> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
            ClientConnection.RPCClientChannel channel = (ClientConnection.RPCClientChannel) ctx.channel();
            channel.set(msg);
        }
    }

    /**
     * 申请连接，没有申请到(或者网络断开)，返回null
     *
     * @param milliseconds
     * @return
     */
    public Channel acquire(int milliseconds) {
        try {
            Future<Channel> fch = connPool.acquire();
            Channel channel = fch.get(milliseconds, TimeUnit.MILLISECONDS);
            return channel;
        } catch (Exception e) {
            logger.warn(String.format("获取客户端连接失败，错误信息：%s", e.getMessage()));
        }
        return null;
    }

    //释放连接
    public void release(Channel channel) {
        try {
            if (channel != null) {
                connPool.release(channel);
            }
        } catch (Exception e) {
            logger.warn(String.format("释放客户端连接失败，错误信息：%s", e.getMessage()));
        }
    }

}
