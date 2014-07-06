#TTPOD REST
==============


CoC 理念的一套HTTP JSON API框架，基于SpringMVC 和 Groovy.


## Overview

1. Write Your Controller In Groovy

``` groovy
@Rest
class MyController extends ControllerSupport7 {
    def hello(HttpServletRequest req) {
        [code: 1, data: "Hello, ${req['name']} !"]
    }

}
```

2. Open http://localhost:8080/`my`/`hello`?name=world

``` json
{"code":1,"data":"Hello, world !"}
```



##How To Use







