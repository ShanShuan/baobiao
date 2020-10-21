package com.pc.huangshan.sercrity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootConfiguration
public class AdminLoginAdapter extends WebMvcConfigurerAdapter {

    @Autowired
    AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/**/**")
                .excludePathPatterns("/login/**").excludePathPatterns("/**/**.html")
                .excludePathPatterns("/**/**.css").excludePathPatterns("/**/**.js")
                .excludePathPatterns("/images/**");
        super.addInterceptors(registry);
    }
}
