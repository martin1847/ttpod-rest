package com.ttpod.netty.rpc.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * date: 14-2-9 上午2:02
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface OutstandingContainer {

    ResponseObserver remove(short reqId);


    ResponseObserver put(short reqId,ResponseObserver observer);


    OutstandingContainer MAP = new OutstandingContainer() {
        final ConcurrentHashMap<Short,ResponseObserver> outstandings = new ConcurrentHashMap<>(128);


        @Override
        public ResponseObserver remove(short reqId) {
            return outstandings.remove(reqId);
        }

        @Override
        public ResponseObserver put(short reqId, ResponseObserver observer) {
            return outstandings.put(reqId,observer);
        }
    };

    int UNSIGN_SHORT_OVER_FLOW = 0xFFFF;

    OutstandingContainer ARRAY_0xFFFF = new OutstandingContainer() {
        ResponseObserver[] array = new ResponseObserver[UNSIGN_SHORT_OVER_FLOW + 1];


        @Override
        public ResponseObserver remove(short reqId) {
            return put(reqId,null);
        }

        @Override
        public ResponseObserver put(short reqId, ResponseObserver observer) {
            int index = reqId & UNSIGN_SHORT_OVER_FLOW;
            ResponseObserver old = array[index];
            array[index] = observer;
            return old;
        }
    };

    class ID{
        private static final AtomicInteger ID = new AtomicInteger();
        public static short next(){
            return (short) ID.incrementAndGet();//& UNSIGN_SHORT_OVER_FLOW;
        }
    }

//
//    OutstandingContainer Ring_Buffer =
//            RingBuffer<ResponseObserver> ringBuffer = RingBuffer.createMultiProducer(new EventFactory<ResponseObserver>() {
//                @Override
//                public ResponseObserver newInstance() {
//                    return null;
//                }
//            },UNSIGN_SHORT_OVER_FLOW, WaitStrategy)
}
