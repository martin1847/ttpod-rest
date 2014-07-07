package com.ttpod.rest.common.doc;


import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum  IMessageCode {
    CODE0(0,"{\"code\":0}"),CODE1(1,"{\"code\":1}");


    private IMessageCode (int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    int code;
    String msg;

    public int getCode(){
        return code;
    }


    public String getMessage(){
        return msg;
    }

    @Override
    @JsonValue
    public String toString() {
        return msg;
    }

    public static final Map OK = Collections.unmodifiableMap(
            new HashMap() {
                {
                    put("code", 1);
                }
            }
    );



}
