package com.ttpod.netty.rpc;

/**
 * date: 14-1-28 上午11:31
 *
 * @author: yangyang.cong@ttpod.com
 */
public class RequestBean {






    public short reqId ;

    byte service =0;
    short page =1 ;
    short size =50;
    String q = "TTPOD";

    public RequestBean(){
//        this((short) (ID.incrementAndGet() & 0xffff));
    }

    public RequestBean(short reqId){
        this.reqId = reqId;
    }

    public RequestBean(QueryServie service, short page, short size, String q) {
        this();
        this.service = service.flag();
        this.page = page;
        this.size = size;
        this.q = q;
    }

    public enum QueryServie{
        SONG,SINGER,ALBUM;

        public byte flag(){
            return (byte) ordinal();
        }
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

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                "service=" + service +
                ", page=" + page +
                ", size=" + size +
                ", q='" + q + '\'' +
                '}';
    }
}
