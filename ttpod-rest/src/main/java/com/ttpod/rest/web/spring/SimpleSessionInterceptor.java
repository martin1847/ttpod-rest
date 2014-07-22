package com.ttpod.rest.web.spring;

import com.ttpod.rest.common.doc.ParamKey;
import com.ttpod.rest.web.view.SimpleJsonView;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 * abstract session Object as Map<String,ValueType>
 *
 *   key type is know as String .
 *   value type is free.
 *
 * date: 14-7-6 18:33
 *
 * @author: yangyang.cong@ttpod.com
 */
public abstract class SimpleSessionInterceptor<ValueType> extends HandlerInterceptorAdapter implements SessionInterceptor<ValueType> {


    public abstract Map<String, ValueType> fetchSession(HttpServletRequest req);


    private static final ThreadLocal<Map<String, Object>> sessionHolder = new ThreadLocal<Map<String, Object>>();


    @SuppressWarnings("unchecked")
    public static<ValueType> Map<String, ValueType> session() {
        return (Map<String, ValueType>) sessionHolder.get();
    }

    public Map<String, ValueType> getSession() {
        return  session();
    }

    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        sessionHolder.remove();
    }

    @SuppressWarnings("unchecked")
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {

        Map<String, ValueType> obj = fetchSession(request);
        if (null != obj && obj.size() > 0) {
            sessionHolder.set((Map<String, Object>) obj);
            return true;
        }

        handleNotAuthorized(request, response, notAuthorized);
        return false;
    }

    protected String notAuthorized = "{\"code\":403,\"msg\":\"ACCESS_TOKEN无效\"}";

    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, String json)
            throws ServletException, IOException {
        String callback = request.getParameter(ParamKey.In.callback);
        if (null != callback && callback.length() > 0) {
            json = callback + '(' + json + ')';
        }
        SimpleJsonView.rennderJson(json, response);
    }

    public static String parseToken(HttpServletRequest request) {
        // bearer type allows a request parameter as well
        return request.getParameter(access_token);
    }



}
