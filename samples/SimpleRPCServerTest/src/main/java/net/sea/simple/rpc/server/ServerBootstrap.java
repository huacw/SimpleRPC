package net.sea.simple.rpc.server;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.utils.RPCCache;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sea
 * @Date 2018/7/5 15:34
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "net.sea")
public class ServerBootstrap {
    public static void main(String[] args) throws RPCServerException {
        ServerFactory.run(ServiceType.service);
        System.out.println("注册的服务元数据 = [" + RPCCache.newCache().findService("net.sea.simple.rpc.contract.DemoService") + "]");
    }
}
