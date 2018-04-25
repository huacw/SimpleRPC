package net.sea.simple.rpc.client.proxy;

import io.netty.channel.ChannelFuture;
import net.sea.simple.rpc.client.annotation.RPCClient;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.register.ServiceRegister;
import net.sea.simple.rpc.server.RegisterCenterConfig;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.SpringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RPC服务代理类型
 *
 * @author sea
 */
public class ServiceProxy implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();
    private Class<?> clazz;
    private RPCClient client;
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
    public synchronized static ServiceProxy newProxy(RPCClient client, Class<?> clazz) {
        ServiceProxy serviceProxy = null;
        serviceProxy = proxyCache.get(clazz);
        if (serviceProxy == null) {
            serviceProxy = new ServiceProxy();
            serviceProxy.clazz = clazz;
            serviceProxy.client = client;
            proxyCache.putIfAbsent(clazz, serviceProxy);
        }
        return serviceProxy;
    }

    /**
     * 创建服务代理
     *
     * @return
     */
    public <T> T newServiceProxy() {
        T result = (T) cache.get(clazz);
        if (result == null) {
            // 设置需要创建子类的类
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(proxyCache.get(clazz));
            // 通过字节码技术动态创建子类实例
            result = (T) enhancer.create();
            // 放入缓存中
            cache.putIfAbsent(clazz, result);
        }
        return result;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //连接RPC服务
        ChannelFuture future = connectRPCService();
        return methodProxy.invokeSuper(obj, args);
    }

    /**
     * 连接RPC服务
     *
     * @return
     */
    private ChannelFuture connectRPCService() {
        ServiceRegister serviceRegister = new ServiceRegister(SpringUtils.getBean(RegisterCenterConfig.class));
        ServiceInfo serviceInfo = serviceRegister.findService(this.client.appName());
        return null;
    }

}
