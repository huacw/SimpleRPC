package net.sea.simple.rpc.server.service.impl;

import net.sea.simple.rpc.contract.DemoService;
import net.sea.simple.rpc.server.annotation.RPCService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sea
 * @Date 2018/4/20 14:51
 * @Version 1.0
 */
@RPCService(publishClasses = {DemoService.class}, version = "2.0")
public class DemoServiceImpl implements DemoService {
    @Override
    public String say(String name) {
        System.out.println("say");
        return "Hello," + name;
    }

    @Override
    public String[] queryTowns() {
        System.out.println("queryTowns");
        return new String[]{"华西村1", "义乌", "同里", "周庄"};
    }

    @Override
    public Map<String, String> test() {
        System.out.println("test");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "test1");
        map.put("key2", "test2");
        map.put("1", "test1");
        return map;
    }

    @Override
    public void hi() {
        System.out.println("hi system out");
//        throw new RPCServerRuntimeException("test");
    }
}
