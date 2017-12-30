package net.sea.simpl.rpc.server;

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