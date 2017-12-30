package net.sea.simpl.rpc.data;

/**
 * RPC消息对象
 * 
 * @author sea
 *
 * @param <T>
 */
public class RPCMessage<T extends RPCBody> {
	private RPCHeader header;
	private T body;

	public RPCHeader getHeader() {
		return header;
	}

	public void setHeader(RPCHeader header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
