package com.ttpod.netty.rpc.pool.impl;

import com.ttpod.netty.rpc.pool.GroupManager;
import com.ttpod.netty.rpc.pool.GroupMemberObserver;
import com.ttpod.netty.util.Zoo;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * date: 14-2-13 下午6:40
 *
 * @author: yangyang.cong@ttpod.com
 */
public class DefaultGroupManager implements Runnable,GroupManager {

    ZooKeeper zk;
    String groupName;
    GroupMemberObserver montior;

    public DefaultGroupManager(String zkAddress, String groupName){
        this(Zoo.connect(zkAddress),groupName,null);
    }
    public DefaultGroupManager(ZooKeeper zk, String groupName,GroupMemberObserver montior) {
        this.zk = zk;

        if(!groupName.startsWith("/")){
            groupName = "/" + groupName;
        }
        this.groupName = groupName;

        this.montior = montior;
        try {
            Stat stat = zk.exists(groupName,false);
            if (null == stat){// Auto Create
                zk.create(groupName,
                        null /* data */,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

        if(montior != null){
            final ExecutorService exe = Executors.newSingleThreadExecutor();
            exe.execute(this);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    exe.shutdownNow();
                }
            }));
        }
    }

    public String join(String memberName, byte[] data){
        String path =  groupName + "/" + memberName ;
         try {
             return zk.create(path,
                     data,
                     ZooDefs.Ids.OPEN_ACL_UNSAFE,
                     CreateMode.EPHEMERAL);
         } catch (KeeperException | InterruptedException e) {
             e.printStackTrace();
             throw new RuntimeException(memberName+ " join group " + groupName +" Faild !!!",e);
         }
     }

    @Override
    public String name() {
        return groupName;
    }


    void beginMontior() throws InterruptedException {
        zk.getChildren(groupName,false, new AsyncCallback.ChildrenCallback() {
//                @Override
//                public void process(WatchedEvent event) {
//                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
//                        semaphore.release();
//                    }
//                }

            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                if(rc == 0){
                    montior.onChange(children);
                    semaphore.release();
                }
            }
        },null);
//        if (children.isEmpty()) {
//            System.out.printf("No members in group %s\n", groupName);
//        }
//        return children;
    }


    private void montior() throws InterruptedException {

        try {
            List<String> children = zk.getChildren(groupName, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        semaphore.release();
                    }
                }
            });
            montior.onChange(children);
            if (children.isEmpty()) {
                System.out.printf("No members in group %s\n", groupName);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
            throw new RuntimeException("list catch KeeperException",e);
        }

    }
    private Semaphore semaphore = new Semaphore(1);

    public void run(){
        try {
            semaphore.acquire();
            while (true) {
                montior();
                semaphore.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void deleteAllMember()
            throws KeeperException, InterruptedException {
        try {
            List<String> children = zk.getChildren(groupName, false);
            for (String child : children) {
                zk.delete(groupName + "/" + child, -1);
            }
//            zk.delete(path, -1);
        }
        catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
        }
    }

}
