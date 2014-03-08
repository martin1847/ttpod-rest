package test.http

import com.ttpod.rest.common.util.http.HttpClientUtil
import com.ttpod.rest.common.util.http.HttpClientUtil4_3

/**
 * date: 14-3-8 下午2:28
 * @author: yangyang.cong@ttpod.com
 */
class TestTtus extends GroovyTestCase{

    def a="""
    14:21:10.800 INFO c.t.r.c.u.h.HttpClientUtil <GET  http://ttus.ttpod.com/user/show?access_token=517b323e7591e951c1000010,cost 21 ms>
14:21:10.809 ERROR c.t.r.c.u.h.HttpClientUtil <获取 TTUS session ERROR :>
java.lang.ClassCastException: java.lang.Long cannot be cast to java.lang.Integer
\tat org.apache.http.params.AbstractHttpParams.getIntParameter(AbstractHttpParams.java:70) ~[httpcore-4.3.jar:4.3]
\tat org.apache.http.client.params.HttpClientParamConfig.getRequestConfig(HttpClientParamConfig.java:54) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.AbstractHttpClient.doExecute(AbstractHttpClient.java:806) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:72) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:57) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.DecompressingHttpClient.execute(DecompressingHttpClient.java:158) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.DecompressingHttpClient.execute(DecompressingHttpClient.java:203) ~[httpclient-4.3.1.jar:4.3.1]
\tat org.apache.http.impl.client.DecompressingHttpClient.execute(DecompressingHttpClient.java:191) ~[httpclient-4.3.1.jar:4.3.1]
\tat com.ttpod.rest.common.util.http.HttpClientUtil.http(HttpClientUtil.java:152) ~[ttpod-rest-1.0.4.jar:na]
\tat com.ttpod.rest.common.util.http.HttpClientUtil.execute(HttpClientUtil.java:179) ~[ttpod-rest-1.0.4.jar:na]
\tat com.ttpod.rest.common.util.http.HttpClientUtil.get(HttpClientUtil.java:126) ~[ttpod-rest-1.0.4.jar:na]
\tat com.ttpod.user.web.UserFrom\$3.build(UserFrom.java:61) ~[classes/:na]
\tat com.ttpod.user.web.OAuth2SimpleInterceptor.fetchFromUserSystem(OAuth2SimpleInterceptor.java:116) ~[classes/:na]
\tat com.ttpod.user.web.OAuth2SimpleInterceptor.preHandle(OAuth2SimpleInterceptor.java:74) ~[classes/:na]

    """

    static final String TTUS = "http://ttus.ttpod.com/user/show?access_token=";

    //TODO benchMark with 4.3 and 4.2
    void testHttp(){

        def access_token = "df2ff60320d2f949266d62252d66d5fd"
            String json = HttpClientUtil4_3.get(TTUS + access_token, null, HttpClientUtil.UTF8);
        println json
    }

}
