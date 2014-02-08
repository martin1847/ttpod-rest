package com.ttpod.netty.rpc.handler;

import com.ttpod.netty.rpc.ResponseBean;

/**
 * date: 14-2-8 下午10:20
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ResponseObserver {

    void onSuccess(ResponseBean response);
}
