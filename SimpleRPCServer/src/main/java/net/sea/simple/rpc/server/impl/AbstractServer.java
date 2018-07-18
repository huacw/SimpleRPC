package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.register.center.ServiceRegister;
import net.sea.simple.rpc.server.IRPCServer;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.config.ServerConfig;
import net.sea.simple.rpc.utils.HostUtils;
import net.sea.simple.rpc.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;

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
        addListener(config, new RegCallbackImpl(config));
        return true;
    }

    /**
     * 添加服务监听
     *
     * @param config
     * @param callback
     * @throws RPCServerException
     */
    protected abstract void addListener(ServerConfig config, RegCallback callback) throws RPCServerException;

    private void startService(Class<?> bootClass) {
        new SpringApplication(bootClass).run();
    }

    /**
     * 注册回调接口
     */
    @FunctionalInterface
    protected interface RegCallback {
        public boolean execute();
    }

    /**
     * 注册回调实现
     */
    protected class RegCallbackImpl implements RegCallback {
        private ServerConfig config;

        public RegCallbackImpl(ServerConfig config) {
            this.config = config;
        }

        @Override
        public boolean execute() {
            if (config == null) {
                throw new RPCServerRuntimeException("RPC服务配置为空");
            }
            //拉取服务路由
            if (!config.isOpened()) {
                return true;
            }
            // 注册服务
//            ServiceRegister serviceRegister = new ServiceRegister(SpringUtils.getBean(RegisterCenterConfig.class));
            ServiceRegister serviceRegister = ServiceRegister.newInstance();
            boolean result = serviceRegister.register(createServiceInfo(config));
            if (result) {//注册成功后开启心跳检测
                startHeartbeat(config);
            }
            return result;
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
        info.setHost(StringUtils.isEmpty(serviceIp) ? HostUtils.getLocalIP() : serviceIp);
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
                ServiceRegister serviceRegister = ServiceRegister.newInstance();
                serviceRegister.registerAgain(createServiceInfo(config));
            }
        }).start();
    }
}
