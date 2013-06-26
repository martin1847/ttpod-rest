package com.ttpod.rest.web.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 自动处理标记的拦截器
 *
 * date: 13-3-7 上午10:39
 *
 * @author: yangyang.cong@ttpod.com
 */
public class InterceptorAnnotationAwareClassNameHandlerMapping extends ControllerClassNameHandlerMapping {
    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        HandlerExecutionChain chain =super.getHandlerExecutionChain(handler,request);
        HandlerInterceptor[] interceptors = detectInterceptors(chain.getHandler().getClass());
        if(null != interceptors)
        chain.addInterceptors(interceptors);
        return chain;
    }

    protected HandlerInterceptor[] detectInterceptors(Class handlerClass) {

        Interceptors interceptorAnnot = AnnotationUtils.findAnnotation(handlerClass, Interceptors.class);
        if (interceptorAnnot != null) {
            String[] beanNames = interceptorAnnot.value();
            if (beanNames != null) {
                HandlerInterceptor[] result = new HandlerInterceptor[beanNames.length];
                ApplicationContext ctx  = getApplicationContext();
                int i = 0;
                for (String beanName : beanNames) {
                    result[i++] = (HandlerInterceptor)ctx.getBean(beanName);
                }
                return result;
            }
        }
        return null;
    }
}
