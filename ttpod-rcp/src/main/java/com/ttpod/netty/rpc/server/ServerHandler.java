package com.ttpod.netty.rpc.server;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;


/**
 * date: 14-2-9 下午1:12
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ServerHandler {


    void setProcessors(ServerProcessor[] processors);


    ResponseBean handleRequest(final RequestBean request)throws Exception;

}


