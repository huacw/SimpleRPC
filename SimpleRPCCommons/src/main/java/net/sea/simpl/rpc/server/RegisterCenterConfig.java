package net.sea.simpl.rpc.server;

/**
 * 注册中心配置
 * 
 * @author sea
 *
 */
public class RegisterCenterConfig {
	/**
	 * 服务选择策略
	 * 
	 * @author sea
	 *
	 */
	public enum ServiceChoiceStrategy {
		polling("轮询"), random("随机"), weight("负载比重");
		private String desc;

		private ServiceChoiceStrategy(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	private String zkServers;// zk服务器地址
	private int timeout = 10000;// 超时时间
	private ServiceChoiceStrategy strategy = ServiceChoiceStrategy.polling;// 服务选择策略

	public String getZkServers() {
		return zkServers;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ServiceChoiceStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ServiceChoiceStrategy strategy) {
		this.strategy = strategy;
	}

}
