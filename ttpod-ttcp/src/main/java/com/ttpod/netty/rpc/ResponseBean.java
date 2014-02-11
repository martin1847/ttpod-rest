package com.ttpod.netty.rpc;

/**
 * date: 14-2-7 上午11:55
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ResponseBean {

    int code;
    int rows;
    int pages;

    Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", rows=" + rows +
                ", pages=" + pages +
                ", data=" + data +
                ", _req_id=" + _req_id +
                '}';
    }

    short _req_id;
}
