package com.ttpod.netty.rpc.pool;

import io.netty.channel.Channel;

/**
 * date: 14-2-13 上午11:14
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ChannelPool<ClientHandler> {


    ClientHandler next();


//    void add(Channel channel);

    void remove(Channel c);

    void shutdown();

}