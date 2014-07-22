package com.ttpod.rest.web.view;

import com.ttpod.rest.common.util.JSONUtil;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * date: 14-7-22 17:19
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ObjectJsonView extends SimpleJsonView{
    private ObjectJsonView() {
    }

    protected static final String VALUE_KEY = "_";

    static final ObjectJsonView instance = new ObjectJsonView();
    protected String toJson(Map<String, ?> model){
        return  JSONUtil.beanToJson(model.get(VALUE_KEY));
    }


    public static ModelAndView asJson(Object o){
        return new ModelAndView(instance,VALUE_KEY,o);
    }
}
