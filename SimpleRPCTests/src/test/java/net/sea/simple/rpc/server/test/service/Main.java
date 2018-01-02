package net.sea.simple.rpc.server.test.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import net.sea.simple.rpc.server.annotation.RPCService;

@RPCService(serviceName = "ddd")
public class Main {
	@Qualifier("IDemoService2")
	private IDemoService demoService;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("net.sea");

		String[] beannames = context.getBeanNamesForType(Main.class);
		Main bean = context.getBean(Main.class);
		// 当加上@AliasFor时, 输出"mainbean"
		// 当去掉@AliasFor注解后, 输出"main"
		System.out.println(beannames[0]);
		RPCService annotation = bean.getClass().getAnnotation(RPCService.class);

		System.out.println(annotation.serviceName());
		// System.out.println(annotation.value());

		IDemoService bean2 = context.getBean("IDemoService", IDemoService.class);
		bean2.say("Tom");

		context.close();
		say();

		Method[] methods = Main.class.getDeclaredMethods();
		for (Method method2 : methods) {
			System.out.print(method2.getName() + ":");
			Class<?>[] parameterTypes = method2.getParameterTypes();
			System.out.println(parameterTypes.length);
			for (Class<?> class1 : parameterTypes) {
				System.out.println(class1.getName());
			}
		}

	}

	static void say(String... name) {
		for (String string : name) {
			System.out.println("hello " + string);
		}
	}

	static void tell() {
		System.out.println("-----------");
	}
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@interface MainBean {

	// @AliasFor(annotation = Component.class, attribute = "value")
	@AliasFor("value")
	String beanName() default "";

	@AliasFor("beanName")
	String value() default "";
}
