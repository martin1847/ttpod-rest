package com.ttpod.rest.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * json javabean 之间的转换工具;
 * servlet 输出工具
 *
 * @author yangyang.cong
 */
public abstract class JSONUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSONFACTORY =new JsonFactory();

    /**
     * 转换Java Bean 为 json
     */
    public static String beanToJson(Object o) {
        StringWriter sw = new StringWriter(300);
        JsonGenerator gen = null;
        try {
            gen = JSONFACTORY.createGenerator(sw);
            MAPPER.writeValue(gen, o);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        } finally {
            if (gen != null) try {
                gen.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 转换Java Bean 为 HashMap
     */
    @SuppressWarnings("unchecked")
    public static<Value> Map<String, Value> beanToMap(Object o) {
        try {
            return (Map) MAPPER.readValue(beanToJson(o), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }


    /**
     * 转换Json String 为 HashMap
     */
    @SuppressWarnings("unchecked")
    public static<Object> Map<String, Object> jsonToMap(String json) {
        try {
            return (Map) MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }


    /**
     * 转换Json String 为 JavaBean
     */
    public static <T> T jsonToBean(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
            //return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        try{
            Class GString = Class.forName("groovy.lang.GString");
            SimpleModule gStringModule = new SimpleModule();
            gStringModule.addSerializer(GString, new JsonSerializer() {
                @Override
                public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                    jgen.writeString(String.valueOf(value));
                }
            });
            MAPPER.registerModule(gStringModule);
        }catch (Throwable ignored){}



        MAPPER.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static void validateJSON(String json) throws IOException {
        JsonParser parser =null;
        try {
            parser = JSONFACTORY.createParser(json);
            while (parser.nextToken() != null) {
            }
        }finally {
               if(null != parser ) parser.close();
        }

    }

//    public interface ToJson{
//        String toJsonString();
//    }
}