package net.sea.simple.rpc.spring;

import net.sea.simple.rpc.client.utils.RPCClient;
import net.sea.simple.rpc.contract.DemoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author sea
 * @Date 2018/7/5 14:35
 * @Version 1.0
 */
@Configuration
@Lazy
@Order(Ordered.LOWEST_PRECEDENCE + 10)
public class SpringAutoConfig {
    private String svr = "net.sea.demo.service";
    private String version = "2.0";

    @Bean
    @ConditionalOnMissingBean
    public DemoService demoServiceProxy() {
        return RPCClient.get(svr, version, DemoService.class);
    }
}
