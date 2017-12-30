package net.sea.simple.rpc.server.spring;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import net.sea.simple.rpc.server.annotation.RPCService;
import net.sea.simple.rpc.server.annotation.RPCServiceMethod;
import net.sea.simple.rpc.server.meta.ServiceMeta;
import net.sea.simple.rpc.server.meta.ServiceMethodMeta;
import net.sea.simple.rpc.server.utils.RPCCache;

/**
 * 获取注册的服务信息
 * 
 * @author sea
 *
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
				Class<? extends Object> clazz = ctx.getBean(beanName).getClass();
				// 注册RPC服务对象别名
				ctx.registerAlias(beanName, clazz.getAnnotation(RPCService.class).serviceName());
				// 注册RPC服务
				registerRPCService(clazz);
			}
		}
	}

	/**
	 * 注册RPC服务
	 * 
	 * @param clazz
	 *            RPC服务类
	 */
	private void registerRPCService(Class<? extends Object> clazz) {
		RPCService annotation = clazz.getAnnotation(RPCService.class);
		String serviceName = annotation.serviceName();
		ServiceMeta meta = new ServiceMeta();
		meta.setServiceName(serviceName);
		Method[] methods = clazz.getMethods();
		if (methods == null || methods.length == 0) {
			return;
		}
		// 注册RPC服务方法
		meta.setMethodMeta(registerRPCServiceMethods(methods));
		// 添加到系统缓存
		RPCCache.newCache().registerService(meta);
		logger.debug(String.format("注册服务：%s\t【%s】", serviceName, clazz.getName()));
	}

	/**
	 * 注册RPC服务方法
	 * 
	 * @param methods
	 * @return
	 */
	private ServiceMethodMeta registerRPCServiceMethods(Method[] methods) {
		ServiceMethodMeta methodMeta = new ServiceMethodMeta();
		for (Method method : methods) {
			RPCServiceMethod annotationMethod = method.getAnnotation(RPCServiceMethod.class);
			if (annotationMethod == null) {
				continue;
			}
			String methodName = annotationMethod.methodName();
			if (StringUtils.isBlank(methodName)) {
				methodName = method.getName();
			}
			methodMeta.addMethod(methodName, method);
		}
		return methodMeta;
	}

}
