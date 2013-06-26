package com.ttpod.rest.web.data;

import groovy.transform.CompileStatic;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 提供json 格式输出
 * date: 12-8-20 下午3:13
 *
 * @author: yangyang.cong@ttpod.com
 */
@CompileStatic
public class JsonExchange implements Map2View {

    private static final SimpleJsonView jsonView = SimpleJsonView.instance();
    @Override
    public ModelAndView exchange(Map data) {
        return new ModelAndView(jsonView,data);
    }
}
