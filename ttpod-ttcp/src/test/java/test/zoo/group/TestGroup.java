package test.zoo.group;

import com.ttpod.netty.rpc.pool.GroupMemberObserver;
import com.ttpod.netty.rpc.pool.impl.DefaultGroupManager;
import org.apache.zookeeper.*;

import java.util.List;

/**
 * date: 14-2-13 下午4:19
 *
 * @author: yangyang.cong@ttpod.com
 */
public class TestGroup {

    public static void main(String[] args) throws Exception{

        final ZooKeeper zk = Zoo.connect("192.168.8.12:2181");
        new DefaultGroupManager(zk,"cyy",new GroupMemberObserver() {
            public void onChange(List<String> currentNodes) {
                System.out.println( "onChange : " +  currentNodes);
            }
        });

        System.in.read();
    }
}
