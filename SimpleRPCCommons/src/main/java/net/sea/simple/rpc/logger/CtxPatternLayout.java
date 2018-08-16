package net.sea.simple.rpc.logger;

import net.sea.simple.rpc.utils.ContextUtils;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 日志输出格式
 *
 * @author sea
 * @Date 2018/8/7 16:24
 * @Version 1.0
 */
public class CtxPatternLayout extends PatternLayout {
    @Override
    protected PatternParser createPatternParser(String pattern) {
        PatternParser parser = new CtxPatternParser(pattern);
        return parser;
    }

    /**
     * 上下文解析类
     */
    private class CtxPatternParser extends PatternParser {
        CtxPatternParser(String pattern) {
            super(pattern);
        }

        @Override
        protected void finalizeConverter(char c) {
            if (c == 'S') {
                PatternConverter pc = new CtxPatternConverter();
                currentLiteral.setLength(0);
                addConverter(pc);
                return;
            }

            super.finalizeConverter(c);
        }
    }

    /**
     * 上下文转换类
     */
    private class CtxPatternConverter extends PatternConverter {
        @Override
        protected String convert(LoggingEvent event) {
            return ContextUtils.getContext().getContextId();
        }
    }
}
