package net.sea.simple.rpc.utils;

import java.nio.charset.Charset;
import java.util.UUID;

import com.sun.jmx.snmp.ThreadContext;
import io.netty.buffer.ByteBuf;
import net.sea.simple.rpc.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 上下文工具类
 *
 * @author sea
 */
public class ContextUtils {
    private static ContextUtils context;
    private ThreadLocal<String> localContainer = new ThreadLocal<>();

    private ContextUtils() {
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static ContextUtils getContext() {
        if (context == null) {
            synchronized (ThreadContext.class) {
                if (context == null) {
                    context = new ContextUtils();
                }
            }
        }
        return context;
    }

    /**
     * 获取上下文id
     *
     * @return
     */
    public String getContextId() {
        String contextId = localContainer.get();
        if (StringUtils.isBlank(contextId)) {
            contextId = genSessionId();
            localContainer.set(contextId);
        }
        return contextId;
    }

    /**
     * 生成sessionId
     *
     * @return
     */
    private static String genSessionId() {
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
