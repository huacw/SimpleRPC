package net.sea.simple.rpc.data.callback;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.data.codec.MarshallingDecoder;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.utils.ByteBufUtils;
import net.sea.simple.rpc.utils.ContextUtils;

/**
 * 数据回调
 *
 * @author sea
 */
public abstract class Callback {
    /**
     * 解析消息
     *
     * @param buf
     * @param marshallingDecoder
     * @return
     * @throws RPCServerException
     */
    public RPCMessage parseMessage(ByteBuf buf, MarshallingDecoder marshallingDecoder) throws RPCServerException {
        try {
            RPCMessage message = newMessage();
            RPCHeader header = new RPCHeader();
            header.setCrcCode(buf.readInt());
            header.setLength(buf.readInt());
            header.setVersion(ByteBufUtils.readString(buf));
            byte type = buf.readByte();
            header.setType(type);
            //设置响应码
            if (type == CommonConstants.RESPONSE_MESSAGE_TYPE) {
                header.setStatusCode(buf.readInt());
            }
            header.setPriority(buf.readByte());
            header.setLocalHost(ByteBufUtils.readString(buf));
            header.setRemoteHost(ByteBufUtils.readString(buf));
            header.setSessionId(ContextUtils.readSessionId(buf));
            header.setExProperties(ByteBufUtils.readMap(buf));
            message.setHeader(header);
            if (buf.readableBytes() > 4) {
                parseBody(message, marshallingDecoder, buf);
            }
            return message;
        } catch (Exception e) {
            throw new RPCServerException(e);
        }
    }

    /**
     * 解析消息体
     *
     * @param message
     * @param marshallingDecoder
     * @param buf
     * @throws RPCServerException
     */
    protected abstract void parseBody(RPCMessage message, MarshallingDecoder marshallingDecoder, ByteBuf buf)
            throws RPCServerException;

    /**
     * 创建消息对象
     *
     * @return
     */
    protected abstract RPCMessage newMessage();
}
