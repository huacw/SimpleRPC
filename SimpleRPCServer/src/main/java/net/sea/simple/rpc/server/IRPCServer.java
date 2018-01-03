package net.sea.simple.rpc.server;

import net.sea.simple.rpc.server.config.ServerConfig;

/**
 * RPC服务接口
 * 
 * @author sea
 *
 */
public interface IRPCServer {
	/**
	 * 服务器启动
	 * 
	 * @param bootClass
	 *            启动类
	 * @param config
	 *            启动参数
	 * 
	 * @return 返回是否启动成功，true-启动成功，false-启动失败
	 */
	public boolean start(Class<?> bootClass, ServerConfig config);

	/**
	 * 服务器启动
	 * 
	 * @param bootClass
	 *            启动类
	 * 
	 * @return 返回是否启动成功，true-启动成功，false-启动失败
	 */
	public boolean start(Class<?> bootClass);

}
