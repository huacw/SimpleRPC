package net.sea.simple.rpc.client.test;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import net.sea.simple.rpc.utils.ContextUtils;

/**
 * @author sea
 * @Date 2018/8/7 16:56
 * @Version 1.0
 */
public class CtxConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return ContextUtils.getContext().getContextId();
    }
}
