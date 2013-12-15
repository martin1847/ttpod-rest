package com.ttpod.rest.common.util;

import groovy.transform.CompileStatic;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

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
@CompileStatic
public abstract class JSONUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSONFACTORY =new JsonFactory();


    /**
     * 输出结果到客户端.简单的打印信息.<br>
     * 设置如下响应头:
     * text/plain;charset=utf-8
     * "Cache-Control", "no-cache"
     *
     * @param response HttpServletResponse
     * @param str      输出目标
     */
//    public static void printObj(HttpServletResponse response, Object str) {
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("text/plain;charset=utf-8");
//        response.setHeader("Cache-Control", "no-cache");
//        try {
//            PrintWriter out = response.getWriter();
//            out.print(str);
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            throw new RuntimeException("HttpServletResponse 打印输出失败", e);
//        }
//    }

    /**
     * 转换Java Bean 为 json
     */
    public static String beanToJson(Object o) {
        StringWriter sw = new StringWriter(300);
        JsonGenerator gen = null;
        try {
            gen = JSONFACTORY.createJsonGenerator(sw);
            MAPPER.writeValue(gen, o);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        } finally {
            if (gen != null) try {
                gen.close();
            } catch (IOException e) {
            }
        }
    }

    public static String beanToJson(ToJson o) {
        return o.toJsonString();
    }

    /**
     * 把对象转换为json格式字符串输出
     *
     * @param response 当前响应response
     * @param bean     要输出的对象
     */
//    public static void printObjAsJson(HttpServletResponse response, Object bean) {
//        printObj(response, beanToJson(bean));
//    }

    /**
     * 转换Java Bean 为 HashMap
     */
    public static Map<String, Object> beanToMap(Object o) {
        try {
            return (Map) MAPPER.readValue(beanToJson(o), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }


    /**
     * 转换Json String 为 HashMap
     */
    public static Map<String, Object> jsonToMap(String json) {
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
        MAPPER.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }


    public static void validateJSON(String json) throws IOException {
        JsonParser parser = JSONFACTORY.createJsonParser(json);
        while (parser.nextToken() != null) {
        }
    }

    public interface ToJson{
        String toJsonString();
    }
}