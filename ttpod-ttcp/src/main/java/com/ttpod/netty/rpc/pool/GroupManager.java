package com.ttpod.netty.rpc.pool;

/**
 * date: 14-2-13 下午6:37
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface GroupManager {

    String join(String memberName, byte[] data);

    String name();
}
