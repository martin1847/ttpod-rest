package com.example.session;

import com.ttpod.rest.web.spring.SimpleSessionInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;


/**
 * if you want save session in redis .
 *
 * just impl here your code . fetch from redis.
 *
 * then annotation your controller with @RestWithSession
 *
 * @see com.ttpod.rest.anno.RestWithSession
 *
 */
public class MySessionInterceptor extends SimpleSessionInterceptor<Serializable> {

    @Override
    public Map<String, Serializable> fetchSession(HttpServletRequest req) {

        String key = parseToken(req);
        // TODO impl session hanlder yourself !
//        Map<String,String> session = redis.hgetAll(key);

        return null;
    }


}
