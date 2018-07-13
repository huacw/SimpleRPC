package net.sea.simple.rpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.sea.simple.rpc.utils.SpringUtils;

/**
 * @author sea
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
