package net.sea.simple.rpc.client.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * RPC服务代理类型
 * 
 * @author sea
 *
 */
public class ServiceProxy implements MethodInterceptor {
	private volatile static ServiceProxy proxy;

	private ServiceProxy() {
	}

	/**
	 * 创建代理实例
	 * 
	 * @return
	 */
	public static ServiceProxy newProxy() {
		if (proxy == null) {
			synchronized (ServiceProxy.class) {
				if (proxy == null) {
					proxy = new ServiceProxy();
				}
			}
		}
		return proxy;
	}

	private Enhancer enhancer = new Enhancer();

	/**
	 * 创建服务代理
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T newServiceProxy(Class<T> clazz) {
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(proxy);
		// 通过字节码技术动态创建子类实例
		return (T) enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return methodProxy.invokeSuper(obj, args);
	}

}
