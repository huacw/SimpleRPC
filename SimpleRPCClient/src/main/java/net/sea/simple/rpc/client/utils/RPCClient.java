package net.sea.simple.rpc.client.utils;

import net.sea.simple.rpc.client.proxy.ServiceProxy;
import net.sea.simple.rpc.constants.CommonConstants;

/**
 * RPC客户端
 *
 * @author sea
 */
public final class RPCClient {
    /**
     * 获取服务对象实体
     *
     * @param appName 服务名
     * @param clazz   服务的类名
     * @return 服务对象实体
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String appName, Class<T> clazz) {
        return get(appName, CommonConstants.DEFAULT_SERVICE_VERSION, clazz);
    }

    /**
     * 获取服务对象实体
     *
     * @param appName 服务名
     * @param version 版本号
     * @param clazz   服务的类名
     * @return 服务对象实体
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String appName, String version, Class<T> clazz) {
        return (T) ServiceProxy.newProxy(appName, version, clazz).newServiceProxy();
    }
}
