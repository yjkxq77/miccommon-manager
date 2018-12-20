package com.mc.manager.frame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域允许
 *
 * @author Liu Chunfu
 * @date 2018-11-09 下午5:05
 **/
@Configuration
@Slf4j
public class CorsConfiguration {

    /**
     * 跨域
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                log.warn("允许跨域...");
                registry.addMapping("/**");
            }
        };
    }
}
