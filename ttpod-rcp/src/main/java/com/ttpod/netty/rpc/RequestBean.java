package com.ttpod.netty.rpc;

/**
 * date: 14-1-28 上午11:31
 *
 * @author: yangyang.cong@ttpod.com
 */
public class RequestBean {



    public static final byte NAME_SERVICE = 0;
    public static final byte FIRST_SERVICE = 1;
    public static final byte SECOND_SERVICE = 2;
    public static final byte THIRD_SERVICE = 3;

    byte service ;
    short page ;
    short size ;
    String data ;

    public RequestBean(){
    }

    public RequestBean(Enum service, short page, short size, String data) {
        this((byte) service.ordinal(),page,size,data);
    }

    public RequestBean(byte service, short page, short size, String data) {
        this();
        this.service = service;
        this.page = page;
        this.size = size;
        this.data = data;
    }

    public byte getService() {
        return service;
    }

    public void setService(byte service) {
        this.service = service;
    }

    public short getPage() {
        return page;
    }

    public void setPage(short page) {
        this.page = page;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                "_req_id=" + _req_id +
                ", service=" + service +
                ", page=" + page +
                ", size=" + size +
                ", data='" + data + '\'' +
                '}';
    }

    short _req_id;
}
