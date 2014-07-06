package com.example.web

import com.ttpod.rest.anno.Rest

import javax.servlet.http.HttpServletRequest

@Rest
class MyController extends Base {

    def hello(HttpServletRequest req) {
        [code: 1, data: "Hello, ${req['name']} !"]
    }

}