package net.sea.simpl.rpc.data.codec;

import java.io.IOException;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.sea.simpl.rpc.constants.CommonConstants;
import net.sea.simpl.rpc.data.RPCHeader;
import net.sea.simpl.rpc.data.RPCMessage;
import net.sea.simpl.rpc.exception.RPCServerException;
import net.sea.simpl.rpc.utils.ContextUtils;

/**
 * RPC服务编码器
 * 
 * @author sea
 *
 * @param <T>
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
		sendBuf.writeInt(header.getLength());
		sendBuf.writeByte(header.getType());
		sendBuf.writeByte(header.getPriority());
		ContextUtils.writeSessionId(sendBuf, header.getSessionId());
		sendBuf.writeInt(header.getExProperties().size());
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, String> param : header.getExProperties().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes(CommonConstants.DEFAULT_CHARSET);
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			marshallingEncoder.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
			marshallingEncoder.encode(msg.getBody(), sendBuf);
		} else {
			sendBuf.writeInt(0);
		}
		sendBuf.setInt(4, sendBuf.readableBytes() - 8);
	}
}
