package net.sea.simpl.rpc.data;

/**
 * RPC消息体
 * 
 * @author sea
 *
 */
public class RPCBody {
	private String version;// 服务版本号

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
