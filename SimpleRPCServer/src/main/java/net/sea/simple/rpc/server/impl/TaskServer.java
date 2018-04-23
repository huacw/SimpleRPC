package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.server.enumeration.ServiceType;

/**
 * 任务类服务器
 * 
 * @author sea
 *
 */
public class TaskServer extends AbstractServer {

	@Override
	protected String getServiceType() {
		return ServiceType.task.name();
	}

}
