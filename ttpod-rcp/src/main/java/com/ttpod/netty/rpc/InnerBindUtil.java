package com.ttpod.netty.rpc;

/**
 * date: 14-2-9 下午2:00
 *
 * @author: yangyang.cong@ttpod.com
 */
public abstract class InnerBindUtil {


    public static short id(RequestBean requestBean){
        return requestBean._req_id;
    }

    public static short id(ResponseBean responseBean){
        return responseBean._req_id;
    }

    public static short bind(RequestBean requestBean, short id){
        requestBean._req_id = id;
        return id;
    }

    public static void bind(ResponseBean responseBean, short id){
        responseBean._req_id = id;
    }

}


