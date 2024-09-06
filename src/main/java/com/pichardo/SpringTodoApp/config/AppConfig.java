package com.pichardo.SpringTodoApp.config;

import com.pichardo.SpringTodoApp.mapper.EntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EntityMapper entityMapper() {
        return new EntityMapper();
    }
}