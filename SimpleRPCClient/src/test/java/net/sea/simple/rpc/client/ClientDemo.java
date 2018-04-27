package net.sea.simple.rpc.client;

import net.sea.simple.rpc.client.annotation.RPCClient;
import net.sea.simple.rpc.contract.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author chengwu.hua
 * @Date 2018/4/24 14:50
 * @Version 1.0
 */
@Service
public class ClientDemo {
    @RPCClient(appName = "net.sea.demo.service")
    private DemoService demoService;
    @RPCClient(appName = "net.sea.demo.service")
    private DemoService demoService1;

    public void sayHello() {
        demoService.say("Jack");
    }
}
