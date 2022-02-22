package net.sea.simple.rpc.client.pool;

import net.sea.simple.rpc.client.config.ClientConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端连接池
 *
 * @author sea
 * @Date 2018/8/9 17:30
 * @Version 1.0
 */
public class ClientConnectionPoolManager {
    /**
     * 连接映射
     */
    private static final Map<ClientConnection, ClientConnectionPool> connectionMap = new ConcurrentHashMap<>();
    /**
     * 锁对象
     */
    private static volatile Object lock = new Object();

    private ClientConnectionPoolManager() {
    }

    /**
     * 获取客户端连接池
     *
     * @param host 服务器地址
     * @param port 端口
     * @return 客户端连接池
     */
    public static ClientConnectionPool getClientConnectionPool(String host, int port) {
        return getClientConnectionPool(host, port, null);
    }

    /**
     * 获取客户端连接池
     *
     * @param host   服务器地址
     * @param port   端口
     * @param config 客户端参数配置
     * @return 客户端连接池
     */
    public static ClientConnectionPool getClientConnectionPool(String host, int port, ClientConfig config) {
        ClientConnection conn = new ClientConnection(host, port);
        ClientConnectionPool pool = connectionMap.get(conn);
        if (pool == null) {
            synchronized (lock) {
                pool = connectionMap.get(conn);
                if (pool == null) {
                    conn.setClientConfig(config);
                    pool = new ClientConnectionPool(conn);
                    connectionMap.put(conn, pool);
                }
            }
        }
        pool.setLastUseTime(System.currentTimeMillis());
        return pool;
    }
}
