package net.sea.simple.rpc.client.test;

import net.sea.simple.rpc.client.annotation.RPCClient;
import net.sea.simple.rpc.contract.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengwu.hua
 * @Date 2018/4/24 14:50
 * @Version 1.0
 */
@Service
public class ClientDemo {
//    @RPCClient(appName = "net.sea.demo.service")
    @Autowired
    private DemoService demoService;
//    @RPCClient(appName = "net.sea.demo.service")
    @Autowired
    private DemoService demoService1;

    public void sayHello() {
        System.out.println(demoService.say("Jack"));
        String[] towns = demoService.queryTowns();
        for (String town:towns){
            System.out.println(town);
        }
        System.out.println(demoService.test());
        demoService.hi();
    }
}
