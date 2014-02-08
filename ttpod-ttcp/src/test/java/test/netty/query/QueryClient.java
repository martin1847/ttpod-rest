package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.QueryRes;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import com.ttpod.netty.bean.codec.QueryResDecoder;
import com.ttpod.netty.bean.handler.QueryClientHandler;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO Comment here.
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryClient {
    public static void main(String[] args) throws Exception {
        final QueryReqEncoder queryReqEncoder = new QueryReqEncoder();
        final ChannelHandler queryResDecoder = new QueryResDecoder();
        new Client(new InetSocketAddress("127.0.0.1", 6666),
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //new QueryReqDecoder(),new QueryReqEncoder(),
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                        p.addLast("queryResDecoder",queryResDecoder);
                        p.addLast("queryReqEncoder",queryReqEncoder);

                        p.addLast(new QueryClientHandler());
                    }
                },
                new Client.ChannelCallback() {
                    @Override
                    public void useChannel(Channel searchChannel) throws InterruptedException {
                        System.out.println("Begin Loop");
                        // Read commands from the stdin.
                        ChannelFuture lastWriteFuture = null;
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        final QueryClientHandler  handler =searchChannel.pipeline().get(QueryClientHandler.class);

                        final int THREADS = 1000;
                        ExecutorService exe = Executors.newFixedThreadPool(THREADS);

                        for(int i =THREADS;i>0; i--){
                            final char pre = (char)i;
                            exe.execute(new Runnable() {
                                @Override
                                public void run() {
                                    String q = pre+ Thread.currentThread().getName();
                                    QueryReq req =  new QueryReq(QueryReq.QueryServie.SONG, (short) 1, (short) 50, q);
                                    QueryRes msg = handler.doSearch(req);

                                    if(! msg.toString().contains(q)){
                                        System.err.println( q +  "\t" + req.reqId + "\t" + msg);
                                    }
                                }
                            });
                        }

//                        final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                        for (; ; ) {
                            String line = null;
                            try {
                                line = in.readLine();
                            } catch (IOException e) {
                            }
                            System.out.println(line + "  ->  doSearch -> " +

                                    // Sends the received line to the server.
                                    handler.doSearch(
                                            new QueryReq(QueryReq.QueryServie.SONG, (short) 1, (short) 50, line)
                                    ));

                            // If user typed the 'bye' command, wait until the server closes
                            // the connection.
                            if ("bye".equals(line.toLowerCase())) {
                                searchChannel.closeFuture().sync();
                                break;
                            }
                        }
                        exe.shutdown();

//                        // Wait until all messages are flushed before closing the channel.
//                        if (lastWriteFuture != null) {
//                            lastWriteFuture.sync();
//                        }
                    }
                }
        );


    }
}
