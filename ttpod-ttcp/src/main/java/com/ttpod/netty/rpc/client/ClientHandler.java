package com.ttpod.netty.rpc.client;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;

import java.util.concurrent.TimeoutException;

/**
 * date: 14-2-8 下午10:27
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ClientHandler {

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
    ResponseBean rpc(RequestBean req,int timeOutMills) throws TimeoutException;

}
