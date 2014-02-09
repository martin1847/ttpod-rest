package com.ttpod.netty.rpc.client;

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
    short nextId();


    class Map extends IdGen implements OutstandingContainer {
        final ConcurrentHashMap<Short,ResponseObserver> outstandings = new ConcurrentHashMap<>(128);

        public ResponseObserver remove(short reqId) {
            return outstandings.remove(reqId);
        }

        public ResponseObserver put(short reqId, ResponseObserver observer) {
            return outstandings.put(reqId,observer);
        }
    }

    int UNSIGN_SHORT_OVER_FLOW = 0xFFFF;

    // TODO beanckmark with RingBuffer . https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
    class Array extends IdGen implements OutstandingContainer  {
        final ResponseObserver[] array = new ResponseObserver[UNSIGN_SHORT_OVER_FLOW + 1];
        public ResponseObserver remove(short reqId) {
            return put(reqId,null);
        }
        public ResponseObserver put(short reqId, ResponseObserver observer) {
            int index = reqId & UNSIGN_SHORT_OVER_FLOW;
            ResponseObserver old = array[index];
            array[index] = observer;
            return old;
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
class IdGen{
    private final AtomicInteger seq = new AtomicInteger();
    public short nextId(){
        return (short) seq.incrementAndGet();//& UNSIGN_SHORT_OVER_FLOW;
    }
}