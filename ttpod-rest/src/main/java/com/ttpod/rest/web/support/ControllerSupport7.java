package com.ttpod.rest.web.support;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * MethodHandle
 *
 * invokeDynamic
 *
 * 2013-06-09 15:55
 *
 *
 * @author: yangyang.cong@ttpod.com
 */
//@Slf4j
public class ControllerSupport7 extends ControllerSupport {
    static final Logger log = LoggerFactory.getLogger(ControllerSupport7.class);


    protected MethodExec zeroArg(Method method,Object self){
        return staticZeroArg(method,self);
    }
    protected MethodExec twoArg(Method method,Object self){
        return staticTwoArg(method, self);
    }
    protected MethodExec requestArg(Method method,Object self){
        return staticRequestArg(method, self);
    }
    protected MethodExec responseArg(Method method,Object self){
        return staticResponseArg(method, self);
    }


    static MethodExec staticZeroArg(Method method,Object self){
        return new ZeroArg7(findMH(method,self,NO_PTYPES));
    }
    static MethodExec staticTwoArg(Method method,Object self){
        return new TwoArg7(findMH(method,self,new Class[]{HttpServletRequest.class,HttpServletResponse.class}));
    }
    static MethodExec staticRequestArg(Method method,Object self){
        MethodHandle mh = findMH(method,self,new Class[]{HttpServletRequest.class});
        return   mh.type().returnType() == Map.class ?  new RequestArg7Map(mh) :  new RequestArg7(mh);
    }
    static MethodExec staticResponseArg(Method method,Object self){
        return new ResponseArg7(findMH(method,self,new Class[]{HttpServletResponse.class}));
    }


    static final Class[] NO_PTYPES = {};

    static {
        System.setProperty("java.lang.invoke.MethodHandle.DEBUG_NAMES","true");
    }


    static MethodHandle findMH(Method method,Object reveiver,Class[] ptypes) {
        // MethodType：代表“方法类型”，包含了方法的返回值（methodType()的第一个参数）和具体参数（methodType()第二个及以后的参数）。
        MethodType mt = MethodType.methodType(method.getReturnType(), ptypes);
        // lookup()方法来自于MethodHandles.lookup，这句的作用是在指定类中查找符合给定的方法名称、方法类型，并且符合调用权限的方法句柄。
        // 因为这里调用的是一个虚方法，按照Java语言的规则，方法第一个参数是隐式的，代表该方法的接收者，也即是this指向的对象，
        // 这个参数以前是放在参数列表中进行传递，现在提供了bindTo()方法来完成这件事情。
        try {
            Class selfClass = reveiver.getClass();
            MethodHandle mh = MethodHandles.publicLookup().findVirtual(selfClass, method.getName(), mt).bindTo(reveiver);
            //.asType(mt);
            ControllerSupport7.log.info("findMethodHandle : {}.{}",selfClass.getSimpleName(),mh);
            return mh;
        } catch (Exception e) {
            throw new RuntimeException("findMethodHandle Error : "+ reveiver.getClass() +"."+ method.getName(),e);
        }
    }

}
