package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.InnerBindUtil;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;
import com.ttpod.netty.rpc.pool.ChannelPool;
import com.ttpod.netty.rpc.pool.CloseableChannelFactory;
import com.ttpod.netty.rpc.pool.impl.ZkChannelPool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class PoolClient {
    public static void main(String[] args) throws Exception {
        ChannelPool<ClientHandler> pool = new ZkChannelPool("192.168.8.12:2181","com.ttpod.search");
        System.out.println("Pls Input a  word ..");
//      final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            RequestBean req = new RequestBean(RequestBean.QueryServie.SONG, (short) 1, (short) 50, line);
            ResponseBean res = pool.next().rpc(req);
            System.out.println(line + "  ->  rpc["+ InnerBindUtil.id(req) +"] -> " +res );
            if ("bye".equals(line)) {
                pool.shutdown();
                break;
            }
        }

    }
}
