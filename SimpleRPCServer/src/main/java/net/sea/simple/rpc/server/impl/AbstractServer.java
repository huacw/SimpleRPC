package net.sea.simple.rpc.server.impl;

import net.sea.simpl.rpc.register.ServiceRegister;
import net.sea.simpl.rpc.server.ServiceInfo;
import net.sea.simple.rpc.server.IRPCServer;
import net.sea.simple.rpc.server.config.ServerConfig;

/**
 * 抽象服务器
 * 
 * @author sea
 *
 */
public abstract class AbstractServer implements IRPCServer {
	protected ServiceRegister serviceRegister;

	@Override
	public boolean start() {
		return start(parseConfig());
	}

	@Override
	public boolean start(ServerConfig config) {
		// TODO Auto-generated method stub
		// 启动服务
		// 注册服务
		serviceRegister.register(createServiceInfo(config));
		return false;
	}

	/**
	 * 创建服务信息对象
	 * 
	 * @param config
	 * @return
	 */
	protected abstract ServiceInfo createServiceInfo(ServerConfig config);

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
