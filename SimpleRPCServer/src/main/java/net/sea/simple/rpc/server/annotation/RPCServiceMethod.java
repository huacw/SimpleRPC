package net.sea.simple.rpc.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rpc服务方法注解
 * 
 * @author sea
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RPCServiceMethod {
	/**
	 * rpc服务方法名
	 * 
	 * @return
	 */
	public String methodName() default "";
}
