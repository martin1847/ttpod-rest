package com.ttpod.rest.common.doc;


import com.mongodb.BasicDBObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <b>系统暴露的外部调用API</b>
 *
 * <br>
 *
 *
 */
class UnmodifDBObject extends BasicDBObject{

    final Map inner;
    UnmodifDBObject(Map init){
        super(init);
        inner= Collections.unmodifiableMap(init);
    }

    public BasicDBObject append( String key , Object val ){
        throw new UnsupportedOperationException("UnmodifDBObject");
    }
    public Object put( String key , Object val ){
        throw new UnsupportedOperationException("UnmodifDBObject");
    }
    public Object remove(Object key) {
        throw new UnsupportedOperationException("UnmodifDBObject");
    }
    public void putAll(Map m) {
        throw new UnsupportedOperationException("UnmodifDBObject");
    }
    public void clear() {
        throw new UnsupportedOperationException("UnmodifDBObject");
    }
    public Set  keySet() {
        return inner.keySet();
    }

    public Set entrySet() {
        return inner.entrySet();
    }

    public Collection values() {
        return inner.values();
    }
}