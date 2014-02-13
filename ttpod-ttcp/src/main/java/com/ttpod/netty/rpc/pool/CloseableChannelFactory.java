package com.ttpod.netty.rpc.pool;

import io.netty.channel.Channel;

/**
 * date: 14-2-13 上午11:22
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface CloseableChannelFactory extends io.netty.bootstrap.ChannelFactory<Channel> {

    void shutdown();

}
