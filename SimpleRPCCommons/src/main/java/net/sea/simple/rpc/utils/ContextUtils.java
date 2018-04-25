package net.sea.simple.rpc.utils;

import java.nio.charset.Charset;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 上下文工具类
 *
 * @author sea
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
        return ByteBufUtils.readString(buf);
    }

    /**
     * 写入sessionId
     *
     * @param buf
     * @param sessionId
     * @return
     */
    public static void writeSessionId(ByteBuf buf, String sessionId) {
        ByteBufUtils.writeString(buf, sessionId);
    }
}
