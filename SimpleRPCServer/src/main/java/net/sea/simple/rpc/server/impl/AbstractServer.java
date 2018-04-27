package net.sea.simple.rpc.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.data.codec.RPCMessageDecoder;
import net.sea.simple.rpc.data.codec.RPCMessageEncoder;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.register.ServiceRegister;
import net.sea.simple.rpc.server.IRPCServer;
import net.sea.simple.rpc.server.RegisterCenterConfig;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.config.ServerConfig;
import net.sea.simple.rpc.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 抽象服务器
 *
 * @author sea
 */
public abstract class AbstractServer implements IRPCServer {
    protected Logger logger = Logger.getLogger(getClass());

    @Override
    public boolean start(Class<?> bootClass) throws RPCServerException {
        return start(bootClass, null);
    }

    @Override
    public boolean start(Class<?> bootClass, ServerConfig config) throws RPCServerException {
        // 启动服务
        startService(bootClass);
        if (config == null) {
            config = parseConfig();
        }
        //添加监听
        addListener(config);
        //拉取服务路由
        if (!config.isOpened()) {
            return true;
        }
        // 注册服务
        ServiceRegister serviceRegister = new ServiceRegister(SpringUtils.getBean(RegisterCenterConfig.class));
        boolean result = serviceRegister.register(createServiceInfo(config));
        if (result) {//注册成功后开启心跳检测
            startHeartbeat(config);
        }
        return result;
    }

    /**
     * 添加服务监听
     *
     * @param config
     * @throws RPCServerException
     */
    protected abstract void addListener(ServerConfig config) throws RPCServerException;

    private void startService(Class<?> bootClass) {
        new SpringApplication(bootClass).run();
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    protected String getLocalIP() {
        List<String> result = new ArrayList<>();
        Enumeration<NetworkInterface> netInterfaces;
        String defaultIP = "127.0.0.1";
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        result.add(ip.getHostAddress());
                    }
                }
            }
            return CollectionUtils.isEmpty(result) ? defaultIP : result.get(0);
        } catch (Exception e) {
            logger.error("获取本地IP异常", e);
            return defaultIP;
        }
    }

    /**
     * 创建服务信息对象
     *
     * @param config
     * @return
     */
    private ServiceInfo createServiceInfo(ServerConfig config) {
        ServiceInfo info = new ServiceInfo(config.getServiceName());
        String serviceIp = config.getServiceIp();
        info.setHost(StringUtils.isEmpty(serviceIp) ? getLocalIP() : serviceIp);
        info.setPort(config.getPort());
        info.setServiceType(getServiceType());
        return info;
    }

    /**
     * 获取服务类型
     *
     * @return
     */
    protected abstract String getServiceType();

    /**
     * 解析配置文件
     *
     * @return
     */
    protected ServerConfig parseConfig() {
        return SpringUtils.getBean(ServerConfig.class);
    }

    /**
     * 启动心跳检测服务
     *
     * @param config
     */
    protected void startHeartbeat(final ServerConfig config) {
        new Thread(() -> {
            while (true) {
                //休眠1s
                try {
                    Thread.sleep(CommonConstants.DEFAULT_HEART_TIMEOUT);
                } catch (InterruptedException e) {
                    logger.warn("心跳程序休眠异常", e);
                }
                //1s后重新注册服务
                ServiceRegister serviceRegister = new ServiceRegister(SpringUtils.getBean(RegisterCenterConfig.class));
                serviceRegister.registerAgain(createServiceInfo(config));
            }
        }).start();
    }
}
