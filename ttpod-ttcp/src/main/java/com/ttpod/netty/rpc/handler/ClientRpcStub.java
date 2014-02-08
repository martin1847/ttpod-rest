package com.ttpod.netty.rpc.handler;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;

/**
 * date: 14-2-8 下午10:27
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ClientRpcStub {

    /**
     * async call.
     */
    void rpc(RequestBean req, ResponseObserver observer);

    /**
     * sync call.
     */
    ResponseBean rpc(RequestBean req);

    /**
     * sync call.
     */
    ResponseBean rpc(RequestBean req,int timeOutMills);

}
