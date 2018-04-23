package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.server.enumeration.ServiceType;

/**
 * 服务类服务器
 *
 * @author sea
 */
public class ServiceServer extends AbstractServer {

    @Override
    protected String getServiceType() {
        return ServiceType.service.name();
    }

}
