package net.sea.simple.rpc.client.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author chengwu.hua
 * @Date 2018/4/24 14:12
 * @Version 1.0
 */
@Component
public class ExtendedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private Properties props;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        this.props = props;
    }

    public Properties getProps() {
        return props;
    }
}
