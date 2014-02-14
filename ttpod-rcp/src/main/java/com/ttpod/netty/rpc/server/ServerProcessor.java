package com.ttpod.netty.rpc.server;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;

/**
 * date: 14-2-9 下午1:00
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ServerProcessor {

    ResponseBean handle(RequestBean req) throws Exception;

}
