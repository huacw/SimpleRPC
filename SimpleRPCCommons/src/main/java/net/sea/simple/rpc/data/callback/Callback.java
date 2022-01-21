package net.sea.simple.rpc.data.callback;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.RPCMessage;
import net.sea.simple.rpc.data.codec.MarshallingDecoder;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.utils.ByteBufUtils;
import net.sea.simple.rpc.utils.CRCCodeUtil;
import net.sea.simple.rpc.utils.ContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;

/**
 * 数据回调
 *
 * @author sea
 */
public abstract class Callback<M extends RPCMessage> {
    protected Logger logger = Logger.getLogger(getClass());

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
            M message = newMessage();
            RPCHeader header = new RPCHeader();
            header.setCrcCode(buf.readInt());
            header.setLength(buf.readInt());
            header.setType(buf.readByte());
            header.setVersion(ByteBufUtils.readString(buf));
            //设置状态码
            setStatusCode(buf, header);
            header.setPriority(buf.readByte());
            header.setLocalHost(ByteBufUtils.readString(buf));
            header.setRemoteHost(ByteBufUtils.readString(buf));
            String sessionId = ContextUtils.readSessionId(buf);
            header.setSessionId(sessionId);
            //串联上下文id
            ContextUtils.getContext().setContextId(sessionId);
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

    protected abstract void setStatusCode(ByteBuf buf, RPCHeader header);

    /**
     * 检查报文的crc校验码
     *
     * @param bodyStr
     * @param crcCode
     */
    private void checkCrc(String bodyStr, int crcCode) {
        bodyStr = StringUtils.isBlank(bodyStr) ? "" : bodyStr;
        if (CRCCodeUtil.getCRC(bodyStr.getBytes(StandardCharsets.UTF_8)) == crcCode) {
            logger.info("crc校验码通过");
            return;
        }
        throw new RPCServerRuntimeException("crc校验码检查未通过");
    }


    /**
     * 解析消息体
     *
     * @param message
     * @param marshallingDecoder
     * @param buf
     * @throws RPCServerException
     */
    protected void parseBody(M message, MarshallingDecoder marshallingDecoder, ByteBuf buf) throws RPCServerException {
        String body = marshallingDecoder.decode(buf);
        //检查crc校验码
        checkCrc(body, message.getHeader().getCrcCode());
        doParseBody(message, body);
    }

    /**
     * 设置消息体
     *
     * @param message
     * @param body
     */
    protected abstract void doParseBody(M message, String body);

    /**
     * 创建消息对象
     *
     * @return
     */
    protected abstract M newMessage();
}
