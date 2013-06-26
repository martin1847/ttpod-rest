package com.ttpod.rest.common.doc;

import com.ttpod.rest.common.util.JSONUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface IMessageCode extends JSONUtil.ToJson{


    int getCode();


    String getMessage();


    IMessageCode CODE0 = new IMessageCode() {
        public int getCode() {
            return 0;
        }
        public String getMessage() {
            return "OK";
        }
        public String toJsonString() {
            return "{\"code\":0}";
        }
    };


    Map OK = Collections.unmodifiableMap(
            new HashMap(){
                {
                    put("code",1);
                }
            }
    );

}
