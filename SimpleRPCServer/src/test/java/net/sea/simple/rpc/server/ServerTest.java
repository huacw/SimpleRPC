package net.sea.simple.rpc.server;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.enumeration.ServiceType;
import net.sea.simple.rpc.server.exception.UnsupportedServerTypeException;
import net.sea.simple.rpc.server.utils.RPCCache;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chengwu.hua
 * @Date 2018/4/20 14:28
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "net.sea")
public class ServerTest {
    public static void main(String[] args) throws RPCServerException {
        ServerFactory.run(ServiceType.service);
        System.out.println("注册的服务元数据 = [" + RPCCache.newCache().findService("net.sea.simple.rpc.contract.DemoService") + "]");
    }
}
