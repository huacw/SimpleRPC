package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.server.IRPCServer;
import net.sea.simple.rpc.server.config.ServerConfig;

/**
 * 抽象服务器
 * 
 * @author sea
 *
 */
public abstract class AbstractServer implements IRPCServer {
	@Override
	public boolean start() {
		return start(parseConfig());
	}

	/**
	 * 解析配置文件
	 * 
	 * @return
	 */
	protected ServerConfig parseConfig() {
		// TODO
		return new ServerConfig();
	}

}
