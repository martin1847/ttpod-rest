package com.ttpod.rest.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.util.Map;

/**
 * jdk 7 invoke Dynamic Support
 * 
 * date: 13-6-9 下午2:37
 *
 * @author: yangyang.cong@ttpod.com
 */


abstract class MethodHandleExec implements MethodExec{
    final MethodHandle m;
    MethodHandleExec(MethodHandle m){
        this.m=m;
    }
}

class TwoArg7 extends MethodHandleExec{
    TwoArg7(MethodHandle m) {
        super(m);
    }
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        return m.invokeExact(req, res);
    }
}

class ZeroArg7 extends MethodHandleExec{
    ZeroArg7(MethodHandle m) {
        super(m);
    }
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        return m.invokeExact();
    }
}
class RequestArg7  extends MethodHandleExec{
    RequestArg7(MethodHandle m) {
        super(m);
    }
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        return m.invokeExact(req);
    }
}

class RequestArg7Map extends RequestArg7{
    RequestArg7Map(MethodHandle m) {
        super(m);
    }
    public Map exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        return (Map)m.invokeExact(req);
    }
}
class ResponseArg7 extends MethodHandleExec{
    ResponseArg7(MethodHandle m) {
        super(m);
    }
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        return m.invokeExact(res);
    }
}