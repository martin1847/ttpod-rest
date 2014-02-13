package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;
import com.ttpod.netty.rpc.pool.CloseableChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Notify_Future {
    public static void main(String[] args) throws Exception {
        CloseableChannelFactory client = new Client(new InetSocketAddress("127.0.0.1", 6666),
                new DefaultClientInitializer());
        System.out.println("Begin Loop");
        // Read commands from the stdin.
        final ClientHandler handler = client.newChannel().pipeline().get(DefaultClientHandler.class);
        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024, THREADS));

        Benchmark notify = new Benchmark("Notify",handler,exe,THREADS);
        Benchmark future = new Benchmark("Future",handler,exe,THREADS){
            protected void rpcCall(RequestBean req) {
                try {
                    handler.rpc(req, 1000);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };
        Benchmark.VS(notify, future, 15);


        client.shutdown();
        exe.shutdown();

    }
  
}
