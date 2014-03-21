package com.ttpod.rest.common.util.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * date: 2012.10.22
 *
 * @author: yangyang.cong@ttpod.com
 */
public abstract class HttpClientUtil4_1 {

    static final Logger log = LoggerFactory.getLogger(HttpClientUtil4_1.class);

    public static final Charset UTF8 =Charset.forName("UTF-8");

    public static final Charset GB18030 =  Charset.forName("GB18030");

    static final int  TIME_OUT  = Integer.getInteger("http.timeout", 40000);

    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.4 (KHTML, like Gecko) Safari/537.4";

    static HttpClient HTTP_CLIENT = bulidHttpClient();
    static HttpClient  bulidHttpClient(){
//        SchemeRegistry registry = new SchemeRegistry();
//        registry.register(new Scheme("http",  80, PlainSocketFactory.getSocketFactory()));
//        registry.register(new Scheme("https",  443, SSLSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
        cm.setMaxTotal(800);
        cm.setDefaultMaxPerRoute(200);

        cm.setMaxPerRoute(new HttpRoute(new HttpHost("localhost")),500);
        cm.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")),500);
        cm.setMaxPerRoute(new HttpRoute(new HttpHost("ttus.ttpod.com")),500);
        HttpParams defaultParams = new  BasicHttpParams();

        defaultParams.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, TIME_OUT);
        defaultParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);//连接超时
        defaultParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);//读取超时

        defaultParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        defaultParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,UTF8.name());
        //defaultParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,"HTTP/1.1");
        defaultParams.setParameter(CoreProtocolPNames.USER_AGENT,USER_AGENT);


//        CacheConfig cacheConfig = new CacheConfig();
//        cacheConfig.setMaxCacheEntries(5000);
//        cacheConfig.setMaxObjectSize(8192 * 4);

        HttpClient client = new DefaultHttpClient(cm,defaultParams);
        // 500 错误 重试一次 bw, also retry by seeds..
//        client = new AutoRetryHttpClient(client,new ServiceUnavailableRetryStrategy() {
//            @Override
//            public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
//                return executionCount <= 2 &&
//                        response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
//            }
//            @Override
//            public long getRetryInterval() {
//                return 1500;
//            }
//        });

        client = new DecompressingHttpClient(client);

        return client;
        //return new CachingHttpClient(client, cacheConfig);
    }

//    static final ExecutorService EXE =
//            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100,
//                    120L, TimeUnit.SECONDS,
//                    new SynchronousQueue<Runnable>(),
//                    new ThreadFactory(){
//                        AtomicInteger begin = new AtomicInteger();
//                        @Override
//                        public Thread newThread(Runnable r) {
//                            Thread thread =new Thread(r);
//                            thread.setName("HttpClient-HtmlDownload-Thread-" + begin.incrementAndGet());
//                            return thread;
//                        }
//                    });

    public static String get(String url,Map<String,String> HEADERS)throws IOException{
        HttpGet get = new HttpGet(url);
        return execute(get,HEADERS,null);
    }


    public static String get(String url,Map<String,String> HEADERS,Charset forceCharset)throws IOException{
        HttpGet get = new HttpGet(url);
        return execute(get,HEADERS,forceCharset);
    }


    public static String post(String url,Map<String,String> params,Map<String,String> headers) throws IOException{
        HttpPost post = new HttpPost(url);
        if(params !=null &&  ! params.isEmpty()){
            List<NameValuePair> ps = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String,String> kv : params.entrySet()){
                ps.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(ps));
        }
        return execute(post,headers,null);
    }


    public static <T> T  http(HttpClient  client,HttpRequestBase request,Map<String,String> headers,HttpEntityHandler<T> handler)
            throws IOException {
        if(headers !=null &&  ! headers.isEmpty()){
            for (Map.Entry<String,String> kv : headers.entrySet()){
                request.addHeader(kv.getKey(),kv.getValue());
            }
        }
        long begin = System.currentTimeMillis();
        try{
            return client.execute(request,handler,null);
//            entity = response.getEntity();
//            int code = response.getStatusLine().getStatusCode();
//            if(code != HttpStatus.SC_OK){
//                throw new HttpStatusException(code,request.getURI().toString());
//            }
//
//
//            return callBack.handle(entity);
        }catch (ConnectTimeoutException e){
                log.error(" catch ConnectTimeoutException ,closeExpiredConnections &  closeIdleConnections for 30 s. ");
                client.getConnectionManager().closeExpiredConnections();
                client.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
                throw  e;
        }finally {
            // netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'
            // CLOSE_WAIT =  DefaultMaxPerRoute
            // HttpClient4使用 InputStream.close() 来确认连接关闭
            // CLOST_WAIT 僵死连接数 （占用一个路由的连接）
            //EntityUtils.consumeQuietly(entity);
            // 被动关闭连接 (目标服务器发生异常主动关闭了链接) 之后自己并没有释放连接，那就会造成CLOSE_WAIT的状态
            log.info(handler.getName() + "  {},cost {} ms",request.getURI(),System.currentTimeMillis() - begin);
        }
    }


    private static String execute(final HttpRequestBase request,Map<String,String> headers,final Charset forceCharset)throws IOException{
        return http(HTTP_CLIENT,request,headers,new HttpEntityHandler<String>() {
            @Override
            public String handle(HttpEntity entity) throws IOException{
                if (entity == null) {
                    return null;
                }
//                        if(log.isDebugEnabled()){
//                            CacheResponseStatus responseStatus = (CacheResponseStatus) localContext.getAttribute(
//                                CachingHttpClient.CACHE_RESPONSE_STATUS);
//
//                            if(CacheResponseStatus.CACHE_HIT == responseStatus){
//                                log.debug("A response hit cache with no requests :{}",request.getURI());
//                            }
//                        }
                byte[] content = EntityUtils.toByteArray(entity);
                if(forceCharset != null){
                    return new String(content,forceCharset) ;
                }
                String html;
                Charset charset =null;
                ContentType contentType = ContentType.get(entity);
                if(contentType !=null){
                    charset = contentType.getCharset();
                }
                if(charset ==null){
                    charset =GB18030;
                }
                html = new String(content,charset) ;
                charset = checkMetaCharset(html,charset);
                if(charset!=null){
                    html = new String(content,charset);
                }
                return html;
            }
            public String getName() {
                return request.getMethod();
            }
        });
    }


    private static Charset checkMetaCharset(String html,Charset use){
        String magic ="charset=";
        int index = html.indexOf(magic);
        if(index >0 && index < 1000){
            index+=magic.length();
            int end = html.indexOf('"',index);
            if(end > index){
                try{

                    String charSetString = html.substring(index,end).toLowerCase();

                    if(charSetString.length() > 10){
                        return null;
                    }
                    //GBK GB2312 --> GB18030
                    if(charSetString.startsWith("gb")){
                        return GB18030.equals(use) ? null : GB18030;
                    }
                    Charset curr = Charset.forName(charSetString);
                    if(!curr.equals(use)){
                        return curr;
                    }
                }catch (Exception e){
                   log.error("Get MetaCharset error",e);
                }
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception{


        Map map = new HashMap();
        map.put("Cookie","BDUSS=JTbUhoeWhST3V5TTVoMXlvZXcyeUUwNHI1eS1Xc3BvNnFnU340MjhlMTE3TDVSQVFBQUFBJCQAAAAAAAAAAApBLRAPsZEvAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAYIArMAAAALD2RHMAAAAA6p5DAAAAAAAxMC4zOC4yOHWe0VB1ntFQN3");

        System.out.println(HttpClientUtil.get("http://music.baidu.com/song/13859395/download", map));


    }


}
