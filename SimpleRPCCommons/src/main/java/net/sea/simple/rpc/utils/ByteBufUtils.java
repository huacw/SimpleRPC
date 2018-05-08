package net.sea.simple.rpc.utils;

import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengwu.hua
 * @Date 2018/4/25 10:55
 * @Version 1.0
 */
public class ByteBufUtils {
    /**
     * 读取sessionId
     *
     * @param buf
     * @return
     */
    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        if (length == 0) {
            return null;
        }
        byte[] dst = new byte[length];
        buf.readBytes(dst);
        return new String(dst, CommonConstants.DEFAULT_CHARSET);
    }

    /**
     * 读取sessionId
     *
     * @param buf
     * @param str
     * @return
     */
    public static void writeString(ByteBuf buf, String str) {
        //字符串为空时的处理
        if (StringUtils.isBlank(str)) {
            buf.writeInt(0);
            return;
        }
        int length = str.length();
        buf.writeInt(length);
        buf.writeBytes(str.getBytes(CommonConstants.DEFAULT_CHARSET), 0, length);
    }

    /**
     * 读取sessionId
     *
     * @param buf
     * @return
     */
    public static Map<String, String> readMap(ByteBuf buf) {
        int length = buf.readInt();
        if (length == 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            map.put(readString(buf), readString(buf));
        }
        return map;
    }

    /**
     * 读取sessionId
     *
     * @param buf
     * @param map
     * @return
     */
    public static void writeMap(ByteBuf buf, Map<String, String> map) {
        //字符串为空时的处理
        if (MapUtils.isEmpty(map)) {
            buf.writeInt(0);
            return;
        }
        buf.writeInt(map.size());
        map.forEach((key, value) -> {
            writeString(buf, key);
            writeString(buf, value);
        });
    }
}
