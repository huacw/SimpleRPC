package net.sea.simple.rpc.utils;

import java.util.UUID;

import io.netty.buffer.ByteBuf;

/**
 * 上下文工具类
 * 
 * @author sea
 *
 */
public class ContextUtils {
	/**
	 * 生成sessionId
	 * 
	 * @return
	 */
	public static String genSessionId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 读取sessionId
	 * 
	 * @param buf
	 * @return
	 */
	public static String readSessionId(ByteBuf buf) {
		int sessionLength = buf.readInt();
		return new String(buf.readBytes(new byte[sessionLength], 0, sessionLength).array());
	}

	/**
	 * 读取sessionId
	 * 
	 * @param buf
	 * @return
	 */
	/**
	 * 
	 * @param buf
	 * @param sessionId
	 */
	public static void writeSessionId(ByteBuf buf, String sessionId) {
		int length = sessionId.length();
		buf.writeInt(length);
		buf.writeBytes(sessionId.getBytes(), 0, length);
	}
}
