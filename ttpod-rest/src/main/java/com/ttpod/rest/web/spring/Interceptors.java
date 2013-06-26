package com.ttpod.rest.web.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptors {
      /** HandlerInterceptor beanName **/
     String[] value() default {"sessionInterceptor"};
}