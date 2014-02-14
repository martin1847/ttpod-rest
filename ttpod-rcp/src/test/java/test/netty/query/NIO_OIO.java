package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;
import com.ttpod.netty.rpc.pool.CloseableChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class NIO_OIO {
    public static void main(String[] args) throws Exception {

        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024, THREADS));

        CloseableChannelFactory nioClient = new Client(new InetSocketAddress("127.0.0.1", 6666),
                new DefaultClientInitializer());
        final ClientHandler NIOhandler = nioClient.newChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark New = new Benchmark("New",NIOhandler,exe,THREADS);

        CloseableChannelFactory oioClient = new Client(new InetSocketAddress("127.0.0.1", 6666),false,new DefaultClientInitializer());
        final ClientHandler OIOhandler = nioClient.newChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark Old = new Benchmark("Old",OIOhandler,exe,THREADS);
        Benchmark.VS(New, Old, 15);


        nioClient.shutdown();
        oioClient.shutdown();
        exe.shutdown();

    }
}


