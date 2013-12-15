package com.ttpod.rest.common.util.jackson;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ttpod.rest.common.util.JSONUtil;

import java.io.IOException;

/**
 * JsonRawString :
 * This can be useful for injecting values already serialized in JSON.
 *
 * date: 13-12-15 下午2:17
 *
 * @author: yangyang.cong@ttpod.com
 */
public final class JsonRawString {
    final String value;

//    static final char JSON_ARRAY = '[';
//    static final char JSON_OBJECT = '{';
    public JsonRawString(String value) {
       try {
           JSONUtil.validateJSON(value);
       }catch (IOException e){
           throw new RuntimeException("Invalid JSON   => " + value,e);
       }
        this.value = value;
    }


    public JsonRawString(Object value) {
        this.value = JSONUtil.beanToJson(value);
    }

    @JsonRawValue
    @JsonValue
    public String toString() {
        return value ;
    }
}
