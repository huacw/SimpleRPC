package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.data.codec.MarshallingDecoder;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.exception.RPCServerException;

/**
 * 响应回调
 * 
 * @author sea
 *
 */
public class ResponseCallback extends Callback {

	@Override
	protected void parseBody(RPCMessage message, MarshallingDecoder marshallingDecoder, ByteBuf buf)
			throws RPCServerException {
		RPCResponse request = (RPCResponse) message;
		request.setResponseBody(marshallingDecoder.decode(buf));
	}

	@Override
	protected RPCMessage newMessage() {
		return new RPCResponse();
	}

}
