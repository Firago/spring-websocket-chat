package com.dfirago.wschat.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

/**
 * @author dmfi
 */
@Configuration
public class RedisConfig {

    @Bean
    public static RedisServerBean redisServer() {
        return new RedisServerBean();
    }

    static class RedisServerBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {

        private RedisServer server;

        @Override
        public void afterPropertiesSet() throws Exception {
            server = new RedisServer(Protocol.DEFAULT_PORT);
            server.start();
        }

        @Override
        public void destroy() throws Exception {
            if (server != null) {
                server.stop();
            }
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry bdr) throws BeansException {
            
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory clbf) throws BeansException {
            
        }

    }

}
