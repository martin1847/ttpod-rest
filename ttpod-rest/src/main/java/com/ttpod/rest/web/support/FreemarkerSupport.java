package com.ttpod.rest.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller Support MultiAction
 *
 * 1 .方法入参为  HttpServletRequest 和 HttpServletResponse
 * 2 .返回值为 void ModelAndView Map String
 *      其中map为对象，可以输出为json 或者 xml
 *      String 就是跳转到对应模板
 *
 * date: 13-3-13 下午1:24
 *
 * @author: yangyang.cong@ttpod.com
 */
public class FreemarkerSupport extends ControllerSupport {
    static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerSupport.class);
    protected ModelAndView invokeMethod(
            String methodName, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        boolean isFreemarker = isFreemarker(request);

        if(!isFreemarker){
            return super.invokeMethod(methodName, request, response);
        }


        MethodExec method = this.handlerMethodMap.get(methodName);
        if (method == null) {
            return new ModelAndView();// 不做方法调用，直接访问模板
        }
        long b = System.currentTimeMillis();
        Object returnValue = method.exec(this, request, response);
        long cost = System.currentTimeMillis() - b;
        if(cost > SLOW_REQ_TIME){
            LOGGER.info(" slow request : {} ,cost : {} ms ",request.getServletPath(),cost);
        }
        if (returnValue instanceof Map) {
            Map<String,Object> map  =  (Map<String,Object>)returnValue;
            for (Map.Entry<String,Object> entry: map.entrySet()){
                request.setAttribute(entry.getKey(),entry.getValue());
            }
        }else if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }

        return new ModelAndView();
    }

//    static final ThreadLocal<Boolean> freemark_local = new ThreadLocal<Boolean>(){
//        protected Boolean initialValue(){
//            return Boolean.FALSE;
//        }
//    };
//
    protected boolean isFreemarker( HttpServletRequest request){
       return request.getRequestURI().endsWith(".xhtml");
    }



}
