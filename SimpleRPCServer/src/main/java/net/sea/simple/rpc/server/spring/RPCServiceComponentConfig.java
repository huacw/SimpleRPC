package net.sea.simple.rpc.server.spring;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.annotation.RPCService;
import net.sea.simple.rpc.server.meta.ServiceMeta;
import net.sea.simple.rpc.server.meta.ServiceMethodMeta;
import net.sea.simple.rpc.server.utils.RPCCache;
import org.springframework.util.CollectionUtils;

/**
 * 获取注册的服务信息
 *
 * @author sea
 */
@Configuration
public class RPCServiceComponentConfig implements ApplicationContextAware {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        String[] beanNames = context.getBeanNamesForAnnotation(RPCService.class);
        if (context instanceof GenericApplicationContext) {
            GenericApplicationContext ctx = (GenericApplicationContext) context;
            for (String beanName : beanNames) {
                Object bean = ctx.getBean(beanName);
                Class<? extends Object> clazz = bean.getClass();
                // 注册RPC服务对象别名
//                String alias = clazz.getAnnotation(RPCService.class).serviceName();
//                if (!StringUtils.isEmpty(alias)) {
//                    ctx.registerAlias(beanName, alias);
//                }
                // 注册RPC服务
                registerRPCService(clazz, beanName, ctx);
            }
        }
    }

    /**
     * 注册RPC服务
     *
     * @param clazz    RPC服务类
     * @param beanName
     * @param ctx
     */
    private void registerRPCService(Class<? extends Object> clazz, String beanName, GenericApplicationContext ctx) {
        RPCService annotation = clazz.getAnnotation(RPCService.class);
        Class<?>[] publishClasses = annotation.publishClasses();
        if (publishClasses == null || publishClasses.length == 0) {
            return;
        }
        for (Class<?> publishClass : publishClasses) {
            try {
                clazz.asSubclass(publishClass);
            } catch (ClassCastException e) {
                throw new RPCServerRuntimeException(
                        String.format("类【%s】不是要发布的类型【%s】", clazz.getName(), publishClass.getName()));
            }
            ServiceMeta meta = new ServiceMeta();
            String serviceName = publishClass.getName();
            meta.setServiceName(serviceName);
            logger.debug(String.format("注册服务：%s\t【%s】", serviceName, clazz.getName()));
            // 注册RPC服务对象别名
            ctx.registerAlias(beanName, serviceName);
            if (!registerMethods(publishClass, annotation, meta)) {
                continue;
            }
            // 添加到系统缓存
            RPCCache.newCache().registerService(meta);
        }

    }

    /**
     * 注册rpc服务方法
     *
     * @param publishClass
     * @param rpcService
     * @param meta
     * @return 方法注册成功时返回true，否则返回false
     */
    private boolean registerMethods(Class<?> publishClass, RPCService rpcService, ServiceMeta meta) {
        Method[] classMethods = publishClass.getMethods();
        if (classMethods == null || classMethods.length == 0) {
            return false;
        }
        String[] publishMethods = rpcService.publishMethods();
        // 注册RPC服务方法
        List<String> publishMethodList = Arrays.asList(publishMethods);
        ServiceMethodMeta methodMeta = new ServiceMethodMeta();
        for (Method method : classMethods) {
            String methodName = method.getName();
            if (CollectionUtils.isEmpty(publishMethodList) || publishMethodList.contains(methodName)) {
                methodMeta.addMethod(methodName, method);
                logger.debug(String.format("注册的方法为：%s", methodName));
            }
        }
        meta.setMethodMeta(methodMeta);
        return true;
    }

}
