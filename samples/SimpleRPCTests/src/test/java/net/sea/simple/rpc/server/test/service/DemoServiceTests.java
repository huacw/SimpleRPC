package net.sea.simple.rpc.server.test.service;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.impl.ServiceServer;

/**
 * DemoService测试类
 * 
 * @author sea
 *
 */
public class DemoServiceTests {

	// @Test
	// public void testDemoService() {
	// AnnotationConfigApplicationContext context = new
	// AnnotationConfigApplicationContext("net.sea");
	// IDemoService bean = context.getBean(IDemoService.class);
	// // IDemoService bean = (IDemoService)
	// // context.getAliases("demoServiceImpl");
	// String[] beanNamesForAnnotation =
	// context.getBeanNamesForType(IDemoService.class);
	// for (String string : beanNamesForAnnotation) {
	// System.out.println(string);
	// }
	// bean.say("Jim");
	// context.close();
	// }

	public static void main(String[] args) {
		// AnnotationConfigApplicationContext context = new
		// AnnotationConfigApplicationContext("net.sea");
		//
		// context.registerAlias("demoServiceImpl", "ddd");
		// String[] beannames =
		// context.getBeanNamesForType(DemoServiceImpl.class);
		//
		// // 当加上@AliasFor时, 输出"mainbean"
		// // 当去掉@AliasFor注解后, 输出"main"
		// System.out.println(beannames[0]);
		// System.out.println(context.getAliases("ddd")[0]);
		// IDemoService bean = context.getBean("ddd", IDemoService.class);
		// bean.say("Tom");
		//
		// context.close();
		try {
			new ServiceServer().start(DemoServiceTests.class);
		} catch (RPCServerException e) {
			e.printStackTrace();
		}
	}

}
