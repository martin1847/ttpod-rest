package com.ttpod.rest.web.support;

import com.ttpod.rest.common.doc.IMessageCode;
import com.ttpod.rest.common.doc.ParamKey;
import com.ttpod.rest.web.data.JsonExchange;
import com.ttpod.rest.web.data.Map2View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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
//@Slf4j
public class ControllerSupport extends MultiActionController {
    static final Logger LOGGER = LoggerFactory.getLogger(ControllerSupport.class);

    static final boolean DEBUG =LOGGER.isDebugEnabled();

    static final ThreadLocal<Map<String,Long>> EXEC_MAP = new ThreadLocal<Map<String, Long>>();

//    static {
//        log.debug( "Enabled debug in com.ttpod.weibo.web : " + DEBUG);
//
//        System.out.println(
//                "Enabled debug in com.ttpod.weibo.web : " + DEBUG
//
//        );
//    }
    //@Resource


    Map2View map2View;


    final Map<String, MethodExec> handlerMethodMap;
    public ControllerSupport(){
        super();
        try {
            Field handlerMethodMapField  = MultiActionController.class.getDeclaredField("handlerMethodMap");
            handlerMethodMapField.setAccessible(true);
            this.handlerMethodMap = (Map<String, MethodExec>) handlerMethodMapField.get(this);


            Method[] methods = getClass().getDeclaredMethods();

            handlerMethodMap.clear();

            for (Method method : methods) {

                int md = method.getModifiers();
                if(! Modifier.isPublic(md)   || Modifier.isStatic(md) || method.getName().contains("$")){
                    continue;
                }
                Class returnType = method.getReturnType();

                if(        returnType != Object.class
                        && returnType != String.class
                        && returnType != void  .class
                        && ! Map         .class.isAssignableFrom(returnType)
                        && ! IMessageCode.class.isAssignableFrom(returnType)
                        && ! ModelAndView.class.isAssignableFrom(returnType)
                    ){
                    continue;
                }

                Class[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length == 0){
                    handlerMethodMap.put(method.getName(), zeroArg(method,this));
                }else if (parameterTypes.length == 1){
                    if(HttpServletRequest.class.equals(parameterTypes[0]) ){
                        handlerMethodMap.put(method.getName(), requestArg(method,this));
                    }else if(HttpServletResponse.class.equals(parameterTypes[0])){
                        handlerMethodMap.put(method.getName(), responseArg(method,this));
                    }
                }else  if (parameterTypes.length == 2
                        && HttpServletRequest.class.equals(parameterTypes[0])
                        && HttpServletResponse.class.equals(parameterTypes[1])
                        && !"handleRequest".equals(method.getName()) ){
                    handlerMethodMap.put(method.getName(), twoArg(method,this));
                }


            }

            map2View = new JsonExchange();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MethodExec zeroArg(Method method,Object self){
        return new ZeroArg(method);
    }
    protected MethodExec twoArg(Method method,Object self){
        return new TwoArg(method);
    }
    protected MethodExec requestArg(Method method,Object self){
        return new RequestArg(method);
    }
    protected MethodExec responseArg(Method method,Object self){
        return new ResponseArg(method);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String methodName = getMethodNameResolver().getHandlerMethodName(request);
        try {
            return invokeMethod(methodName, request, response);
        } catch (Throwable throwable) {
            if(throwable instanceof Exception){
                throw (Exception)throwable;
            }else{
                LOGGER.error(" handleRequest Error. ",throwable);
                throw new Exception(throwable);
            }
        }
    }

    protected ModelAndView handlerMethodNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    static final int SLOW_REQ_TIME = Integer.getInteger("rest.slow_req_mill",1500);
    protected ModelAndView invokeMethod(
            String methodName, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        MethodExec method = this.handlerMethodMap.get(methodName);
        if (method == null) {
            return handlerMethodNotFound(request,response);
        }

        long b = System.currentTimeMillis();
        if(DEBUG){
            EXEC_MAP.set(new HashMap<String, Long>());
        }

        Object returnValue = method.exec(this, request, response);

        if (returnValue instanceof Map) {

            Map map  =  (Map)returnValue;

            long cost = System.currentTimeMillis() - b;
            if(DEBUG){
                map = new HashMap(map);
                map.put(ParamKey.Out.exec,cost);
            }else if( map != IMessageCode.OK &&  map.containsKey(ParamKey.Out.msg)){
                map.remove(ParamKey.Out.msg);
            }
            if(cost > SLOW_REQ_TIME){
                LOGGER.info(" slow request : {} ,cost : {} ms ",request.getServletPath(),cost);
            }
            return map2View.exchange(map);
        }else if (returnValue instanceof IMessageCode) {

            IMessageCode messageCode = (IMessageCode) returnValue;
            Map<String,Object> map = new HashMap<String, Object>();
            map.put(ParamKey.Out.code,messageCode.getCode());


            long cost = System.currentTimeMillis() - b;
            if(DEBUG){
                map.put(ParamKey.Out.msg,messageCode.getMessage());
                map.put(ParamKey.Out.exec,cost);
            }


            if(cost > SLOW_REQ_TIME){
                LOGGER.info(" slow request : {} ,cost : {} ms ",request.getServletPath(),cost);
            }

            return map2View.exchange(map);
        }
        else if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        else if (returnValue instanceof String) {
            return new ModelAndView((String) returnValue);
        }
        else {
            return null;
        }
    }


    protected void logExec(String desc,long execTime){
        EXEC_MAP.get().put(desc,execTime);
    }

}
