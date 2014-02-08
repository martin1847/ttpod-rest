package com.ttpod.netty.rpc.handler.impl;

import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.handler.ResponseObserver;

/**
 * date: 14-2-8 下午11:10
 *
 * @author: yangyang.cong@ttpod.com
 */
public class BlockingResponseObserver  implements ResponseObserver{
    volatile ResponseBean response;
    public void onSuccess(ResponseBean response) {
        this.response = response;
        synchronized (this) {
            notify();
        }
    }
}
