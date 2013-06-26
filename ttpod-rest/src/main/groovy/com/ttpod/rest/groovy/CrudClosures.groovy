package com.ttpod.rest.groovy

import groovy.transform.CompileStatic

/**
 * date: 13-6-5 下午12:50
 * @author: yangyang.cong@ttpod.com
 */
@CompileStatic
interface CrudClosures {

    Closure Str = {it};
    Closure Int = {String str->  (str == null || str.isEmpty()) ? null : Integer.valueOf(str)  };
    Closure Ne0 = {!'0'.equals(it)};
    Closure Eq1 = {'1'.equals(it)};
    Closure Timestamp = {System.currentTimeMillis()};


    Closure IntNotNull ={String str-> Integer.valueOf(str) };
}