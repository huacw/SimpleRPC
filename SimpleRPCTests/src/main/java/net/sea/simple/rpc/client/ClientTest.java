package net.sea.simple.rpc.client;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.ServerFactory;
import net.sea.simple.rpc.server.enumeration.ServiceType;
import net.sea.simple.rpc.server.utils.RPCCache;
import net.sea.simple.rpc.utils.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chengwu.hua
 * @Date 2018/4/24 14:50
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "net.sea")
public class ClientTest {
    public static void main(String[] args) {
        new SpringApplication(ClientTest.class).run();
        ClientDemo bean = SpringUtils.getBean(ClientDemo.class);
        bean.sayHello();
    }
}
