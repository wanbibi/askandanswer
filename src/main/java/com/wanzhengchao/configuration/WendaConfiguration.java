package com.wanzhengchao.configuration;

import com.wanzhengchao.inteceptor.LoginRequiredInterceptor;
import com.wanzhengchao.inteceptor.PassportInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 16.10.15.
 */
@Component
public class WendaConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInteceptor passportInteceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInteceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
