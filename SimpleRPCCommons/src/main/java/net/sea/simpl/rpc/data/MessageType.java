package net.sea.simpl.rpc.data;

import net.sea.simpl.rpc.data.callback.Callback;
import net.sea.simpl.rpc.data.callback.RequestCallBack;
import net.sea.simpl.rpc.data.callback.ResponseCallback;
import net.sea.simpl.rpc.exception.RPCServerRuntimeException;

/**
 * 消息类型
 * 
 * @author sea
 *
 */
public enum MessageType {
	request(1, new RequestCallBack(), "请求回调"), response(2, new ResponseCallback(), "响应回调");

	private int type;
	private Callback callback;
	private String desc;

	private MessageType(int type, Callback callback, String desc) {
		this.type = type;
		this.callback = callback;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public Callback getCallback() {
		return callback;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 根据类型编码获取枚举
	 * 
	 * @param type
	 * @return
	 * @throws RPCServerRuntimeException
	 *             未找到指定编码的类型抛出
	 */
	public static MessageType valueOf(int type) {
		MessageType[] values = MessageType.values();
		MessageType result = null;
		for (MessageType messageType : values) {
			if (type == messageType.type) {
				result = messageType;
				break;
			}
		}
		if (result == null) {
			throw new RPCServerRuntimeException(String.format("无效的类型：%d", type));
		}
		return result;
	}
}
