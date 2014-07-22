package com.ttpod.rest.common.doc;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class   IMessageCode extends HashMap<String,Integer>{
    public static final Map OK = Collections.unmodifiableMap(
            new HashMap() {{put("code", 1);}}
    );
    public static final Map CODE0 = Collections.unmodifiableMap(
            new HashMap() {{put("code", 0);}}
    );
    public static final Map CODE1 = OK;
}
