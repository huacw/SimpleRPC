package net.sea.simple.rpc.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.enumeration.ServiceType;
import net.sea.simple.rpc.server.utils.RPCCache;

/**
 * @author sea
 * @Date 2018/4/20 14:28
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "net.sea")
public class ServiceTest {
    public static void main(String[] args) throws RPCServerException {
        ServerFactory.run(ServiceType.service);
        System.out.println("注册的服务元数据 = [" + RPCCache.newCache().findService("net.sea.simple.rpc.server.service.DemoService") + "]");
    }
}
