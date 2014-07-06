package com.ttpod.rest.web.spring;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * date: 13-6-3 上午10:49
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface SessionInterceptor<ValueType> extends HandlerInterceptor {

    Map<String,ValueType> getSession();


    String access_token = "access_token";

}
