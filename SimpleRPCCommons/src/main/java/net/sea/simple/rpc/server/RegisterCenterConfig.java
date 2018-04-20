package net.sea.simple.rpc.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 注册中心配置
 * 
 * @author sea
 *
 */
@Component
public class RegisterCenterConfig {
	@Value("${zkServers}")
	private String zkServers;// zk服务器地址
	@Value("${sessionTimeout}")
	private int sessionTimeout = 100;// 会话超时时间
	@Value("${connetionTimeout}")
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
