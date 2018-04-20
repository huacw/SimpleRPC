package net.sea.simple.rpc.data.response;

import net.sea.simple.rpc.data.RPCBody;
import net.sea.simple.rpc.data.RPCMessage;

/**
 * RPC响应对象
 * 
 * @author sea
 *
 */
public class RPCResponse extends RPCMessage {

	@Override
	protected void setBody(RPCBody body) {
		this.body = body;
	}

	/**
	 * 设置响应消息体
	 * 
	 * @param body
	 */
	public void setResponseBody(RPCResponseBody body) {
		setBody(body);
	}
}
