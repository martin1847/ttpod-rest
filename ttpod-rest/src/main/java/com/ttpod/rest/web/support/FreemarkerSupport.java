package com.ttpod.rest.web.support;

import com.ttpod.rest.common.util.WebUtils;
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
    static final Logger log = LoggerFactory.getLogger(FreemarkerSupport.class);
    protected ModelAndView invokeMethod(
            String methodName, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        MethodExec method = this.handlerMethodMap.get(methodName);
        freemark_local.set(request.getRequestURI().endsWith(".xhtml"));
        if (method == null) {
            if(isFreemarker()){
                return new ModelAndView();// 不做方法调用，直接访问模板
            }
            return super.handlerMethodNotFound(request, response);
        }
        long b = System.currentTimeMillis();
        Object returnValue = method.exec(this, request, response);
        if (returnValue instanceof Map) {
            long cost = System.currentTimeMillis() - b;
            if(cost > 1500){
                log.info(" slow request : {} ,cost : {} ms ",request.getServletPath(),cost);
            }
            Map<String,Object> map  =  (Map<String,Object>)returnValue;
            if(isFreemarker()){
                WebUtils.populate(request, map);
                return new ModelAndView();
            }
            return map2View.exchange(map);
        }else if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        else if (returnValue instanceof String) {
            return new ModelAndView((String) returnValue);
        }
        else {
            return null;
        }
    }

    static final ThreadLocal<Boolean> freemark_local = new ThreadLocal<Boolean>(){
        protected Boolean initialValue(){
            return Boolean.FALSE;
        }
    };

    protected boolean isFreemarker(){
       return freemark_local.get();
    }



}
