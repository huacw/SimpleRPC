package net.sea.simple.rpc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author chengwu.hua
 * @Date 2018/4/20 16:50
 * @Version 1.0
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据类型获取spring对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<? extends T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据名称获取spring对象
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
