package net.sea.simpl.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simpl.rpc.data.RPCMessage;
import net.sea.simpl.rpc.data.codec.MarshallingDecoder;
import net.sea.simpl.rpc.data.response.RPCResponse;
import net.sea.simpl.rpc.exception.RPCServerException;

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
