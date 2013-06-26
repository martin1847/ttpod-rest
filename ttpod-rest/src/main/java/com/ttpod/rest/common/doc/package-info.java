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
        inner= init;
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
    private transient Set keySet = null;
    private transient Set  entrySet = null;
    private transient Collection  values = null;

    public Set  keySet() {
        if (keySet==null)
            keySet = Collections.unmodifiableSet(inner.keySet());
        return keySet;
    }

    public Set entrySet() {
        if (entrySet==null)
            entrySet = Collections.unmodifiableSet(inner.entrySet());
        return entrySet;
    }

    public Collection values() {
        if (values==null)
            values = Collections.unmodifiableCollection(inner.values());
        return values;
    }
}