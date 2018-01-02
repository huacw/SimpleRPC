package net.sea.simple.rpc.server.test.service.impl;

import net.sea.simple.rpc.server.annotation.RPCService;
import net.sea.simple.rpc.server.test.service.IDemoService;

/**
 * 样例service实现
 * 
 * @author sea
 *
 */
@RPCService(serviceName = "IDemoService2")
public class DemoServiceImpl implements IDemoService {

	@Override
	public void say(String name) {
		System.out.println("hello service1," + name);
	}

	@Override
	public String withdrawMoney(String account, float money) {
		System.out.println(account + "取款：" + money);
		return "取款成功，请取卡";
	}

}
