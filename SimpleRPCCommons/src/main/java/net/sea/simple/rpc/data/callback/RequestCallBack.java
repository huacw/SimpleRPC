package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.data.codec.MarshallingDecoder;
import net.sea.simple.rpc.data.request.RPCRequest;
import net.sea.simple.rpc.data.request.RPCRequestBody;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.utils.ByteBufUtils;
import net.sea.simple.rpc.utils.XStreamUtil;

/**
 * 请求回调
 *
 * @author sea
 */
public class RequestCallBack extends Callback {

    @Override
    protected void parseBody(RPCMessage message, MarshallingDecoder marshallingDecoder, ByteBuf buf)
            throws RPCServerException {
        RPCRequest request = (RPCRequest) message;
//		request.setRequestBody(marshallingDecoder.decode(buf));
//      request.setRequestBody(XStreamUtil.xmlToBean(ByteBufUtils.readString(buf), RPCRequestBody.class));
        request.setRequestBody(XStreamUtil.xmlToBean(marshallingDecoder.decode(buf), RPCRequestBody.class));
    }

    @Override
    protected RPCMessage newMessage() {
        return new RPCRequest();
    }

}
