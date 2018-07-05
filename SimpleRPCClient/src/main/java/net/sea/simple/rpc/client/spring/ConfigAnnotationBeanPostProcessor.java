package net.sea.simple.rpc.client.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import net.sea.simple.rpc.client.annotation.RPCClient;
import net.sea.simple.rpc.client.proxy.ServiceProxy;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;

/**
 * @author chengwu.hua
 * @Date 2018/4/24 14:14
 * @Version 1.0
 */
@Component
public class ConfigAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {
    //创建简单类型转换器
    private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                RPCClient client = field.getAnnotation(RPCClient.class);
                if (client == null) {
                    return;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new RPCServerRuntimeException("@RPCClient an annotation is not supported on static fields");
                }
                Object value = ServiceProxy.newProxy(client, field.getType()).newServiceProxy();
                if (value != null) {
                    Object _value = typeConverter.convertIfNecessary(value, field.getType());
                    ReflectionUtils.makeAccessible(field);
                    field.set(bean, _value);
                }
            }
        });
        return true;
    }
}
