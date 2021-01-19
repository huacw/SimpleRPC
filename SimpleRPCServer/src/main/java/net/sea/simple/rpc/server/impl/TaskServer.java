package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.ServiceType;
import net.sea.simple.rpc.server.config.ServerConfig;

/**
 * 任务类服务器
 * 
 * @author sea
 *
 */
public class TaskServer extends AbstractServer {

	@Override
	protected void addListener(ServerConfig config, RegCallback callback) throws RPCServerException {

	}

	@Override
	protected String getServiceType() {
		return ServiceType.task.name();
	}

}
