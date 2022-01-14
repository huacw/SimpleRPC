package net.sea.simple.rpc.register.center.cache.config;

import lombok.Getter;
import lombok.Setter;
import net.sea.simple.rpc.register.center.IRegister;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:缓存配置
 * @Author: hcw
 * @Date: 2022/1/14 15:57
 */
@Setter
@Getter
public class CacheConfig {
    @Autowired
    private IRegister register;
    //缓存刷新周期，默认1分钟
    private long refreshPeriod = CacheConstants.DEFAULT_REFRESH_PERIOD;
}
