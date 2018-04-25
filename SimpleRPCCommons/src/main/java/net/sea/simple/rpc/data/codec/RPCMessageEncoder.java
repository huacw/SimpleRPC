package net.sea.simple.rpc.data.codec;

import java.io.IOException;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.utils.ByteBufUtils;
import net.sea.simple.rpc.utils.ContextUtils;
import net.sea.simple.rpc.utils.MapUtils;

/**
 * RPC服务编码器
 *
 * @author sea
 */
public final class RPCMessageEncoder extends MessageToByteEncoder<RPCMessage> {

    protected MarshallingEncoder marshallingEncoder;

    public RPCMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RPCMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new RPCServerException("The encode message is null");
        RPCHeader header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode());
        ByteBufUtils.writeString(sendBuf, header.getVersion());
        byte type = header.getType();
        sendBuf.writeByte(type);
        //写出响应码
        if (type == CommonConstants.RESPONSE_MESSAGE_TYPE) {
            sendBuf.writeInt(header.getStatusCode());
        }
        sendBuf.writeByte(header.getPriority());
        ByteBufUtils.writeString(sendBuf, header.getLocalHost());
        ByteBufUtils.writeString(sendBuf, header.getRemoteHost());
        ContextUtils.writeSessionId(sendBuf, header.getSessionId());
        ByteBufUtils.writeMap(sendBuf, header.getExProperties());
        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);//设置报文长度
    }
}
