package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.request.RPCRequest;
import net.sea.simple.rpc.data.request.RPCRequestBody;
import net.sea.simple.rpc.utils.XStreamUtil;

/**
 * 请求回调
 *
 * @author sea
 */
public class RequestCallBack extends Callback<RPCRequest> {

    @Override
    protected void setStatusCode(ByteBuf buf, RPCHeader header) {
    }

    @Override
    protected void doParseBody(RPCRequest request, String body) {
        request.setRequestBody(XStreamUtil.xmlToBean(body, RPCRequestBody.class));
    }

    @Override
    protected RPCRequest newMessage() {
        return new RPCRequest();
    }

}
