package com.alfatest.configuration;

import com.alfatest.aspect.EntityLoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class EntityLoggingConfiguration {

    @Bean
    public EntityLoggingAspect getLoggingAspect(){
        return new EntityLoggingAspect();
    }
}
