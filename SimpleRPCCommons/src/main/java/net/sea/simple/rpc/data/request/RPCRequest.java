package net.sea.simple.rpc.data.request;

import net.sea.simple.rpc.data.RPCBody;
import net.sea.simple.rpc.data.RPCMessage;

/**
 * RPC请求消息
 * 
 * @author sea
 *
 */
public class RPCRequest extends RPCMessage {

	@Override
	protected void setBody(RPCBody body) {
		this.body = body;
	}

	/**
	 * 设置消息体
	 * 
	 * @param body
	 */
	public void setRequestBody(RPCRequestBody body) {
		setBody(body);
	}

}
