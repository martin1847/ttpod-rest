package com.ttpod.rest.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

interface MethodExec{
    Object exec(Object obj,HttpServletRequest req,HttpServletResponse res) throws Throwable;
}

class TwoArg implements MethodExec{
    final Method m;
    TwoArg(Method m){
        this.m=m;
    }
    @Override
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws ReflectiveOperationException {
        return m.invoke(obj,req,res);
    }
}

class ZeroArg implements MethodExec{
    final Method m;
    ZeroArg(Method m){
        this.m=m;
    }
    @Override
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws ReflectiveOperationException {
        return m.invoke(obj);
    }
}

class RequestArg implements MethodExec{
    final Method m;
    RequestArg(Method m){
        this.m=m;
    }
    @Override
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws ReflectiveOperationException {
        return m.invoke(obj,req);
    }
}
class ResponseArg implements MethodExec{
    final Method m;
    ResponseArg(Method m){
        this.m=m;
    }
    @Override
    public Object exec(Object obj, HttpServletRequest req, HttpServletResponse res) throws ReflectiveOperationException {
        return m.invoke(obj,res);
    }
}