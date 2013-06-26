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

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
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
public class MongoFactoryBean implements FactoryBean<Mongo>, InitializingBean {


    private Mongo mongo;

    private String urls;

    public void setAutoSlaveOk(boolean autoSlaveOk) {
        this.autoSlaveOk = autoSlaveOk;
    }

    private boolean autoSlaveOk = true;

    public void setOptions(MongoOptions options) {
        this.options = options;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    private MongoOptions options;
    @Override
    public Mongo getObject() throws Exception {
        return mongo;
    }

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] host_ports = urls.split(",");
        List<ServerAddress> addList = new ArrayList<>(host_ports.length);
        for (String host_port : host_ports){
            String[] kv = host_port.split(":");
            addList.add(new ServerAddress(kv[0],Integer.valueOf(kv[1])));
        }

        if(autoSlaveOk && addList.size() > 1){
            options.readPreference = ReadPreference.secondaryPreferred();
        }

        this.mongo = new Mongo(addList,options);
    }
}
