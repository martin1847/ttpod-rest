package com.ttpod.netty.rpc.client;

import com.ttpod.netty.rpc.ResponseBean;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * date: 14-2-8 下午10:20
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ResponseObserver {

    void onSuccess(ResponseBean response);

    class Blocking implements ResponseObserver{
        public volatile ResponseBean response;
        public void onSuccess(ResponseBean response) {
            this.response = response;
            synchronized (this) {
                notify();
            }
        }

        public ResponseBean get(){
            synchronized (this) {
                while (null == response) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            return response;
        }

    }

    class Future extends FutureTask<ResponseBean> implements ResponseObserver {
        private static final Callable<ResponseBean> innerNotUse = new Callable<ResponseBean>() {
            public ResponseBean call() throws Exception {
                return null;
            }
        };
        public Future() {
            super(innerNotUse);
        }
        public void onSuccess(ResponseBean response) {
            set(response);
        }
    }

}
