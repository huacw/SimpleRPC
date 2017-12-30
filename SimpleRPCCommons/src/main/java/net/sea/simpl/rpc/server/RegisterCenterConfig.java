package net.sea.simpl.rpc.server;

/**
 * 注册中心配置
 * 
 * @author sea
 *
 */
public class RegisterCenterConfig {
	private String zkServers;// zk服务器地址
	private int sessionTimeout = 100;// 会话超时时间
	private int connetionTimeout = 30000;// 连接超时时间
	private ServiceChoiceStrategy strategy = ServiceChoiceStrategy.polling;// 服务选择策略

	public String getZkServers() {
		return zkServers;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnetionTimeout() {
		return connetionTimeout;
	}

	public void setConnetionTimeout(int connetionTimeout) {
		this.connetionTimeout = connetionTimeout;
	}

	public ServiceChoiceStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ServiceChoiceStrategy strategy) {
		this.strategy = strategy;
	}

}
