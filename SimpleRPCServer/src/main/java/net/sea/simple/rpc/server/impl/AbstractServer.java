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

	@Override
	public boolean start(ServerConfig config) {
		// TODO Auto-generated method stub
		// 启动服务
		// 注册服务
		return false;
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

	/**
	 * 启动心跳检测服务
	 * 
	 * @param config
	 */
	protected void startHeartbeat(ServerConfig config) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		}).start();
	}
}
