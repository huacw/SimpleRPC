package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.data.codec.MarshallingDecoder;
import net.sea.simple.rpc.data.request.RPCRequestBody;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.data.response.RPCResponseBody;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.utils.ByteBufUtils;
import net.sea.simple.rpc.utils.GZIPUtils;
import net.sea.simple.rpc.utils.XStreamUtil;

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
//		request.setResponseBody(marshallingDecoder.decode(buf));
//		request.setResponseBody(XStreamUtil.xmlToBean(ByteBufUtils.readString(buf), RPCResponseBody.class));
		request.setResponseBody(XStreamUtil.xmlToBean(marshallingDecoder.decode(buf), RPCResponseBody.class));
	}

	@Override
	protected RPCMessage newMessage() {
		return new RPCResponse();
	}

}
