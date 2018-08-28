package net.sea.simple.rpc.client.test;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author sea
 * @Date 2018/8/7 16:54
 * @Version 1.0
 */
public class LogbackCtxPatternLayout extends PatternLayout {
    static {
        defaultConverterMap.put("S", CtxConverter.class.getName());
    }
}
