package net.sea.simple.rpc.contract;

import java.util.Map;

/**
 * @author sea
 * @Date 2018/4/20 14:42
 * @Version 1.0
 */
public interface DemoService {
    public String say(String name);
    public String[] queryTowns();
    public Map<String,String> test();
    public void hi();
}
