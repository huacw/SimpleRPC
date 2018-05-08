package net.sea.simple.rpc.server.service.impl;

import net.sea.simple.rpc.contract.DemoService;
import net.sea.simple.rpc.server.annotation.RPCService;

/**
 * @author chengwu.hua
 * @Date 2018/4/20 14:51
 * @Version 1.0
 */
@RPCService(publishClasses = {DemoService.class})
public class DemoServiceImpl implements DemoService {
    @Override
    public String say(String name) {
        return "Hello," + name;
    }
}
