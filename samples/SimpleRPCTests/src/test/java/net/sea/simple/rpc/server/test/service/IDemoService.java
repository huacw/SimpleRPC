package net.sea.simple.rpc.server.test.service;

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
