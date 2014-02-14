package test.zoo.group;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * date: 14-2-13 下午4:18
 *
 * @author: yangyang.cong@ttpod.com
 */
public  class Zoo {


    public static ZooKeeper connect(String zkAddress){
        try {
            final CountDownLatch connectedSignal = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper(zkAddress,3000,new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
            connectedSignal.await();
            return zooKeeper;
//            zooKeeper.getChildren();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("cann't conn to : "+ zkAddress,e);
        }
    }
}
