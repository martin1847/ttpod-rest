package com.ttpod.rest.common.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

/**
 * date: 12-11-16 下午1:35
 *
 * @author: yangyang.cong@ttpod.com
 */
public abstract class HttpEntityHandler<T> implements ResponseHandler<T> {


    public T handleResponse(HttpResponse response) throws IOException{
        int code = response.getStatusLine().getStatusCode();
        if(code != HttpStatus.SC_OK){
            throw new HttpStatusException(code,response.toString());
        }
        return handle(response.getEntity());
    }

    public abstract T handle(HttpEntity entity) throws IOException;
    public abstract String getName();
}
