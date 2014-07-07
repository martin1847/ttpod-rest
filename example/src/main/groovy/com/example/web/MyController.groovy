package com.example.web

import com.ttpod.rest.anno.Rest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 公开的实例方法，
 * 1.无参数
 * 2.一个参数 HttpServletRequest
 * 3.二个参数 HttpServletRequest HttpServletResponse
 *
 * 才会被当作API暴露出去.
 *
 */
@Rest
class MyController extends Base {

    def hello(HttpServletRequest req) {
        [code: 1, data: "Hello, ${req['name']} !"]
    }


    def now(){
        [code:1,data:new Date().format("yyyyMMdd HH:mm:ss")]
    }

    def res(HttpServletRequest req,HttpServletResponse res){
        def out = res.getWriter()
        out << """<html>
                        <h1>Hello,World </h1>
                   </html>
            """
    }

}