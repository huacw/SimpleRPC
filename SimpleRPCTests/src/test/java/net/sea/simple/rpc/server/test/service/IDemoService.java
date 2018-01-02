package net.sea.simple.rpc.server.test.service;

import net.sea.simple.rpc.server.annotation.RPCServiceMethod;

/**
 * 样例service
 * 
 * @author sea
 *
 */
public interface IDemoService {
	/**
	 * 交谈
	 * 
	 * @param name
	 */
	@RPCServiceMethod
	public void say(String name);

	/**
	 * 取款
	 * 
	 * @param account
	 * @param money
	 * @return
	 */
	public String withdrawMoney(String account, float money);
}
