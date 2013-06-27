package com.ttpod.rest.ext;

import com.ttpod.rest.common.util.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * date: 13-6-18 下午12:06
 *
 * @author: yangyang.cong@ttpod.com
 */
public  final class RestExtension {

//    public static boolean isNotBlank(String self) {
//        return StringUtils.isNotBlank(self);
//    }


    public static String getAt(HttpServletRequest req,String param) {
        return req.getParameter(param);
    }


    public static Integer getInt(HttpServletRequest req,String param) {
        return Integer.valueOf(req.getParameter(param));
    }


    public static final Charset UTF8 =Charset.forName("UTF8");

    public  static byte[] asBytes(String string){
        return (string == null ? null : string.getBytes(UTF8));
    }

    public  static String asString(byte[] data){
        return data ==null?null : new String (data,UTF8);
    }

    public  static Map<String, Object> asJSON(String string){
        return (string == null ? null : JSONUtil.jsonToMap(string));
    }
    public  static List asJSONList(String string){
        return (string == null ? null : JSONUtil.jsonToBean(string, ArrayList.class));
    }

    public static String asJsonString(Object obj){
        return (obj == null ? null : JSONUtil.beanToJson(obj));
    }

}
