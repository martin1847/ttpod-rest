/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttpod.rest.web.spring;

import com.mongodb.*;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory bean for construction of a MongoOptions instance
 * 
 * @author Graeme Rocher
 * @Author Mark Pollack
 */
public class DbFactoryBean implements FactoryBean<DB>, InitializingBean {

    private Mongo mongo;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }


    @Override
    public DB getObject() throws Exception {
        return mongo.getDB(name);
    }

    @Override
    public Class<?> getObjectType() {
        return DB.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
