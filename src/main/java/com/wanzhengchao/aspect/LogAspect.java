package com.wanzhengchao.aspect;


import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 16.10.13.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.wanzhengchao.controller.indexController.*(..))")
    public void beforeMethod(JoinPoint jp) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : jp.getArgs()) {
            sb.append("arg" + arg.toString());
        }
        logger.info("before" + sb.toString());
    }

    @After("execution(* com.wanzhengchao.controller.indexController.*(..))")
    public void afterMethod() {
        logger.info("after" + new Date());
    }


}
