package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;
import io.netty.channel.ChannelPipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Array_Map {
    public static void main(String[] args) throws Exception {

        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024, THREADS));

        Client arrayClient = new Client(new InetSocketAddress("127.0.0.1", 6666),
                new DefaultClientInitializer());
        final ClientHandler NIOhandler = arrayClient.getChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark Array = new Benchmark("Array",NIOhandler,exe,THREADS);

        Client mapClient = new Client(new InetSocketAddress("127.0.0.1", 6666),new DefaultClientInitializer(){
            @Override
            protected void initClientHandler(ChannelPipeline p) {
                p.addLast(new DefaultClientHandler(new OutstandingContainer.Map()));
            }
        });
        final ClientHandler mapHandler = mapClient.getChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark Map = new Benchmark("Map",mapHandler,exe,THREADS);
        Benchmark.VS(Array, Map, 15);


        arrayClient.close();
        mapClient.close();
        exe.shutdown();

    }
}


