package net.sea.simple.rpc.server;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.config.ServerConfig;
import net.sea.simple.rpc.server.enumeration.ServiceType;
import net.sea.simple.rpc.server.exception.UnsupportedServerTypeException;
import net.sea.simple.rpc.server.impl.MsgServer;
import net.sea.simple.rpc.server.impl.ServiceServer;
import net.sea.simple.rpc.server.impl.TaskServer;

import java.util.EnumMap;

/**
 * 服务器工厂
 *
 * @author sea
 */
public class ServerFactory {
    private static EnumMap<ServiceType, Class<? extends IRPCServer>> serverClasses = new EnumMap<>(ServiceType.class);

    static {
        serverClasses.put(ServiceType.service, ServiceServer.class);
        serverClasses.put(ServiceType.asynMsg, MsgServer.class);
        serverClasses.put(ServiceType.task, TaskServer.class);
    }

    /**
     * 创建服务器
     *
     * @param type 服务器类型
     * @return 服务器对象
     * @throws RPCServerException
     */
    private static IRPCServer createSever(ServiceType type) throws RPCServerException {
        Class<? extends IRPCServer> clazz = serverClasses.get(type);
        if (clazz == null) {
            throw new RPCServerException("非法的服务类型");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RPCServerException(e);
        }
    }

    /**
     * 启动服务器
     *
     * @param type 服务器类型
     * @return 启动状态
     * @throws UnsupportedServerTypeException
     */
    public static boolean run(ServiceType type) throws RPCServerException {
        try {
            return createSever(type).start(getBootClass());
        } catch (Exception e) {
            throw new RPCServerException(e);
        }
    }

    private static Class<?> getBootClass() throws ClassNotFoundException {
        return Class.forName(Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1].getClassName());
    }

    /**
     * 启动服务器
     *
     * @param type   服务器类型
     * @param config 服务器配置参数
     * @return 启动状态
     * @throws RPCServerException
     */
    public static boolean run(ServiceType type, ServerConfig config) throws RPCServerException {
        try {
            return createSever(type).start(getBootClass(), config);
        } catch (Exception e) {
            throw new RPCServerException(e);
        }
    }
}
