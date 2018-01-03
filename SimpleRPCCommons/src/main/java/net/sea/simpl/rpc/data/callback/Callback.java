package net.sea.simpl.rpc.data.callback;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.sea.simpl.rpc.constants.CommonConstants;
import net.sea.simpl.rpc.data.RPCHeader;
import net.sea.simpl.rpc.data.RPCMessage;
import net.sea.simpl.rpc.data.codec.MarshallingDecoder;
import net.sea.simpl.rpc.exception.RPCServerException;
import net.sea.simpl.rpc.utils.ContextUtils;

/**
 * 数据回调
 * 
 * @author sea
 *
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
			header.setSessionId(ContextUtils.readSessionId(buf));
			header.setType(buf.readByte());
			header.setPriority(buf.readByte());
			int size = buf.readInt();
			if (size > 0) {
				Map<String, String> attch = new HashMap<>(size);
				int keySize = 0;
				byte[] keyArray = null;
				String key = null;
				for (int i = 0; i < size; i++) {
					keySize = buf.readInt();
					keyArray = new byte[keySize];
					buf.readBytes(keyArray);
					key = new String(keyArray, CommonConstants.DEFAULT_CHARSET);
					attch.put(key, (String) marshallingDecoder.decode(buf));
				}
				keyArray = null;
				key = null;
				header.setExProperties(attch);
			}
			if (buf.readableBytes() > 4) {
				parseBody(message, marshallingDecoder, buf);
			}
			message.setHeader(header);
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
