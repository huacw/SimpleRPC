package net.sea.simple.rpc.server.spring;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import net.sea.simpl.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.annotation.RPCService;
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
				Object bean = ctx.getBean(beanName);
				Class<? extends Object> clazz = bean.getClass();
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
		Class<?>[] publishClasses = annotation.publishClasses();
		if (publishClasses == null || publishClasses.length == 0) {
			return;
		}
		List<Method> methods = new ArrayList<>();
		for (Class<?> publishClass : publishClasses) {
			try {
				clazz.asSubclass(publishClass);
			} catch (ClassCastException e) {
				throw new RPCServerRuntimeException(
						String.format("类【%s】不是要发布的类型【%s】", clazz.getName(), publishClass.getName()));
			}
			Method[] publishMethods = publishClass.getMethods();
			if (publishMethods == null || publishMethods.length == 0) {
				continue;
			}
			methods.addAll(Arrays.asList(publishMethods));
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
	private ServiceMethodMeta registerRPCServiceMethods(List<Method> methods) {
		ServiceMethodMeta methodMeta = new ServiceMethodMeta();
		for (Method method : methods) {
			String methodName = method.getName();
			methodMeta.addMethod(methodName, method);
		}
		return methodMeta;
	}

}
