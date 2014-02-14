package com.ttpod.netty.rpc.pool;

import java.util.List;

/**
 * date: 14-2-13 下午6:39
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface GroupMemberObserver {
    void onChange(List<String> currentNodes);
}
