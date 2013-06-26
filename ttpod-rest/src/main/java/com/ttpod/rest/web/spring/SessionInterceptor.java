package com.ttpod.rest.web.spring;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * date: 13-6-3 上午10:49
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface SessionInterceptor extends HandlerInterceptor {

    public Map<String,Object> getSession();

}
