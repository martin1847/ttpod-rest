package com.ttpod.netty.rpc.handler.impl;

import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.handler.ResponseObserver;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * date: 14-2-8 下午7:11
 * @author: yangyang.cong@ttpod.com
 */
public class ResponseFuture extends FutureTask<ResponseBean>  implements ResponseObserver {


    private static final Callable<ResponseBean> innerNotUse = new Callable<ResponseBean>() {
        @Override
        public ResponseBean call() throws Exception {
            return null;
        }
    };

    public ResponseFuture() {
        super(innerNotUse);
    }

    @Override
    public void onSuccess(ResponseBean response) {
        set(response);
    }
}
