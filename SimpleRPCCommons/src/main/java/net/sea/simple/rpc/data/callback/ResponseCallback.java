package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.data.response.RPCResponseBody;
import net.sea.simple.rpc.utils.XStreamUtil;

/**
 * 响应回调
 *
 * @author sea
 */
public class ResponseCallback extends Callback<RPCResponse> {

    @Override
    protected void setStatusCode(ByteBuf buf, RPCHeader header) {
        header.setStatusCode(buf.readInt());
    }

    @Override
    protected void doParseBody(RPCResponse request, String body) {
        request.setResponseBody(XStreamUtil.xmlToBean(body, RPCResponseBody.class));
    }

    @Override
    protected RPCResponse newMessage() {
        return new RPCResponse();
    }

}
