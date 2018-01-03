package net.sea.simple.rpc.server.impl;

import org.springframework.boot.SpringApplication;

import net.sea.simpl.rpc.register.ServiceRegister;
import net.sea.simpl.rpc.server.ServiceInfo;
import net.sea.simpl.rpc.utils.PropertiesConfigUtils;
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
	public boolean start(Class<?> bootClass) {
		return start(bootClass, parseConfig());
	}

	@Override
	public boolean start(Class<?> bootClass, ServerConfig config) {
		// 启动服务
		startService(bootClass);
		// 注册服务
		serviceRegister.register(createServiceInfo(config));
		return true;
	}

	private void startService(Class<?> bootClass) {
		new SpringApplication(bootClass).run();
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
		ServerConfig serverConfig = new ServerConfig();
		String zkServers = PropertiesConfigUtils.getDefaultProperty("zk.servers");
		return serverConfig;
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
