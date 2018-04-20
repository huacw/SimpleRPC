package net.sea.simple.rpc.data.codec;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.sea.simple.rpc.data.MessageType;

/**
 * rpc消息解码器
 * 
 * @author sea
 *
 */
public class RPCMessageDecoder extends LengthFieldBasedFrameDecoder {

	private MarshallingDecoder marshallingDecoder;

	public RPCMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		marshallingDecoder = new MarshallingDecoder();
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		// 解析消息类型
		ByteBuf bufCopy = frame.copy();
		byte type = bufCopy.skipBytes(8).readByte();
		// RPCMessage message = null;
		// RPCHeader header = new RPCHeader();
		// header.setCrcCode(frame.readInt());
		// header.setLength(frame.readInt());
		// header.setType(frame.readByte());
		// header.setPriority(frame.readByte());
		// header.setSessionId(ContextUtils.readSessionId(frame));
		//
		// int size = frame.readInt();
		// if (size > 0) {
		// Map<String, String> attch = new HashMap<>(size);
		// int keySize = 0;
		// byte[] keyArray = null;
		// String key = null;
		// for (int i = 0; i < size; i++) {
		// keySize = frame.readInt();
		// keyArray = new byte[keySize];
		// frame.readBytes(keyArray);
		// key = new String(keyArray, "UTF-8");
		// attch.put(key, marshallingDecoder.decode(frame));
		// }
		// keyArray = null;
		// key = null;
		// header.setExProperties(attch);
		// }
		// if (frame.readableBytes() > 4) {
		// message.setBody(marshallingDecoder.decode(frame));
		// }
		// message.setHeader(header);
		return MessageType.valueOf(type).getCallback().parseMessage(frame, marshallingDecoder);
	}
}
