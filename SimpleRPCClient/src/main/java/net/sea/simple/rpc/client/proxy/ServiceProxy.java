package net.sea.simple.rpc.client.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import net.sea.simple.rpc.client.annotation.RPCClient;
import net.sea.simple.rpc.client.config.ClientConfig;
import net.sea.simple.rpc.client.pool.ClientConnection;
import net.sea.simple.rpc.client.pool.ClientConnectionPool;
import net.sea.simple.rpc.client.pool.ClientConnectionPoolManager;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.request.RPCRequest;
import net.sea.simple.rpc.data.request.RPCRequestBody;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.data.response.RPCResponseBody;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.ServiceRegister;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.ServiceType;
import net.sea.simple.rpc.utils.ContextUtils;
import net.sea.simple.rpc.utils.HostUtils;
import net.sea.simple.rpc.utils.JsonUtils;
import net.sea.simple.rpc.utils.SpringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RPC服务代理类型
 *
 * @author sea
 */
public class ServiceProxy implements MethodInterceptor {
    private Logger logger = Logger.getLogger(getClass());
    private Enhancer enhancer = new Enhancer();
    private Class<?> clazz;
    //    private RPCClient client;
    private String appName;
    private static ConcurrentMap<Class<?>, Object> cache = new ConcurrentHashMap<>();
    private static ConcurrentMap<Class<?>, ServiceProxy> proxyCache = new ConcurrentHashMap<>();

    private ServiceProxy() {
    }

    /**
     * 创建代理实例
     *
     * @param client 服务名
     * @param clazz  服务接口
     * @return
     */
    public static synchronized ServiceProxy newProxy(RPCClient client, Class<?> clazz) {
        ServiceProxy serviceProxy = null;
        serviceProxy = proxyCache.get(clazz);
        if (serviceProxy == null) {
            serviceProxy = new ServiceProxy();
            serviceProxy.clazz = clazz;
            serviceProxy.appName = client.appName();
            proxyCache.putIfAbsent(clazz, serviceProxy);
        }
        return serviceProxy;
    }

    /**
     * 创建代理实例
     *
     * @param appName 服务名
     * @param clazz   服务接口
     * @return
     */
    public static synchronized ServiceProxy newProxy(String appName, Class<?> clazz) {
        ServiceProxy serviceProxy = null;
        serviceProxy = proxyCache.get(clazz);
        if (serviceProxy == null) {
            serviceProxy = new ServiceProxy();
            serviceProxy.clazz = clazz;
            if (StringUtils.isNoneBlank(appName)) {
                serviceProxy.appName = appName;
            } else {
                serviceProxy.appName = clazz.getPackage().getName();
            }
            proxyCache.putIfAbsent(clazz, serviceProxy);
        }
        return serviceProxy;
    }

    /**
     * 创建服务代理
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T newServiceProxy() {
        T result = (T) cache.get(clazz);
        if (result == null) {
            // 设置需要创建子类的类
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(proxyCache.get(clazz));
            // 不拦截构造函数
            enhancer.setInterceptDuringConstruction(false);
            // 通过字节码技术动态创建子类实例
            result = (T) enhancer.create();
            // 放入缓存中
            cache.putIfAbsent(clazz, result);
        }
        return result;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        logger.debug(String.format("args:%s \n clazz:%s", JsonUtils.toJson(args), clazz.getName()));
        try (RPCNettyClient client = new RPCNettyClient();) {
            return client.invoke(method, args);
        }
    }

    /**
     * RPC客户端
     *
     * @author sea
     */
    private class RPCNettyClient implements Closeable {
        private ServiceInfo serviceInfo;
        private ClientConnectionPool pool;
        private Channel channel;

        public RPCNettyClient() throws InterruptedException {
            initClient();
        }

        private void initClient() throws InterruptedException {
            ClientConfig clientConfig = SpringUtils.getBean(ClientConfig.class);

            ServiceRegister serviceRegister = ServiceRegister.newInstance();
            serviceInfo = serviceRegister.findService(appName);
            logger.info(String.format("获取的服务信息：%s", serviceInfo.toString()));
            pool = ClientConnectionPoolManager.getClientConnectionPool(serviceInfo.getHost(), serviceInfo.getPort(), clientConfig);
        }

        /**
         * 执行方法
         *
         * @param method
         * @param args
         * @return
         */
        public Object invoke(Method method, Object[] args) throws InterruptedException {
            RPCRequest request = new RPCRequest();
            request.setHeader(buildRpcHeader());
            request.setRequestBody(buildRpcBody(method, args));
//          RPCClientChannel channel = (RPCClientChannel) this.future.channel();
            channel = pool.acquire(CommonConstants.DEFAULT_FETCH_CONNECTION_TIMEOUT);
            ClientConnection.RPCClientChannel clientChannel = (ClientConnection.RPCClientChannel) channel;
            clientChannel.reset();
            clientChannel.writeAndFlush(request);
            if (!ServiceType.service.name().equals(serviceInfo.getServiceType())) {
                logger.info("异步服务，提交后直接返回");
                return Optional.empty();
            }
            RPCResponse response = clientChannel.get(CommonConstants.DEFAULT_CONNECTION_TIMEOUT);
            RPCHeader responseHeader = response.getHeader();
            if (responseHeader.getStatusCode() != CommonConstants.SUCCESS_CODE) {
                RPCResponseBody body = (RPCResponseBody) response.getBody();
                Throwable result = (Throwable) body.getResult();
                logger.error("", result);
                throw new RPCServerRuntimeException(result);
            } else {
                RPCResponseBody body = (RPCResponseBody) response.getBody();
                return body.getResult();
            }
        }

        /**
         * 构建请求内容
         *
         * @param method
         * @param args
         * @return
         */
        private RPCRequestBody buildRpcBody(Method method, Object[] args) {
            RPCRequestBody body = new RPCRequestBody();
            body.setArgs(Arrays.asList(args));
            body.setMethod(method.getName());
            body.setServiceName(clazz.getName());
            return body;
        }

        /**
         * 构建请求头
         *
         * @return
         */
        private RPCHeader buildRpcHeader() {
            RPCHeader header = new RPCHeader();
            header.setType(CommonConstants.REQUEST_MESSAGE_TYPE);
            header.setVersion(CommonConstants.DEFAULT_SERVICE_VERSION);
            header.setPriority(CommonConstants.REQUEST_NORMAL_PRIORITY);
            header.setSessionId(ContextUtils.getContext().getContextId());
            header.setLocalHost(HostUtils.getLocalIP());
            header.setRemoteHost(serviceInfo.getHost());
            return header;
        }

        @Override
        public void close() throws IOException {
//            this.future.channel().close();
//            this.group.shutdownGracefully();
//            logger.info("rpc客户端关闭");
            pool.release(channel);
        }
    }
}
