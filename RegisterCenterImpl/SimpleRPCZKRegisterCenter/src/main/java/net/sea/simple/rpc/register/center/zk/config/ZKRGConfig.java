package net.sea.simple.rpc.register.center.zk.config;

import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.register.center.zk.ZkRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description: nacos配置中心配置
 * @Author: hcw
 * @Date: 2022/1/13 14:46
 */
@Configuration
@Import(ZKRegisterCenterConfig.class)
@ConditionalOnProperty(name = "register.center.config.zk.enable", havingValue = "true")
public class ZKRGConfig {
    @Autowired
    private ZKRegisterCenterConfig config;

    @Bean
    public IRegister newRegister() {
        return new ZkRegister(config);
    }
}
