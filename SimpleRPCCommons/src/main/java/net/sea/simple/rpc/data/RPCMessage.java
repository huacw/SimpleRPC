package net.sea.simple.rpc.data;

/**
 * RPC消息对象
 * 
 * @author sea
 *
 */
public abstract class RPCMessage {
	private RPCHeader header;
	protected RPCBody body;

	public RPCHeader getHeader() {
		return header;
	}

	public void setHeader(RPCHeader header) {
		this.header = header;
	}

	public RPCBody getBody() {
		return body;
	}

	/**
	 * 设置消息体
	 * 
	 * @param body
	 */
	protected abstract void setBody(RPCBody body);
}
