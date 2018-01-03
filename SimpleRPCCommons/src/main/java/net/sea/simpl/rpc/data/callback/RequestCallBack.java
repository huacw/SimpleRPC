package net.sea.simpl.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simpl.rpc.data.RPCMessage;
import net.sea.simpl.rpc.data.codec.MarshallingDecoder;
import net.sea.simpl.rpc.data.request.RPCRequest;
import net.sea.simpl.rpc.exception.RPCServerException;

/**
 * 请求回调
 * 
 * @author sea
 *
 */
public class RequestCallBack extends Callback {

	@Override
	protected void parseBody(RPCMessage message, MarshallingDecoder marshallingDecoder, ByteBuf buf)
			throws RPCServerException {
		RPCRequest request = (RPCRequest) message;
		request.setRequestBody(marshallingDecoder.decode(buf));
	}

	@Override
	protected RPCMessage newMessage() {
		return new RPCRequest();
	}

}
