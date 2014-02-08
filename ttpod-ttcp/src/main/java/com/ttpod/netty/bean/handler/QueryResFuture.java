package com.ttpod.netty.bean.handler;

import com.ttpod.netty.bean.QueryRes;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * date: 14-2-8 下午7:11
 * @author: yangyang.cong@ttpod.com
 */
public class QueryResFuture extends FutureTask<QueryRes> {


    static final Callable innerNouse = new Callable() {
        @Override
        public Object call() throws Exception {
            return "";
        }
    };

    public QueryResFuture() {
        super(innerNouse);
    }

    public void set(QueryRes v){
        super.set(v);
    }
}
