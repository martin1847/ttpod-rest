#TTPOD REST

---

CoC 理念的一套HTTP JSON API框架，基于SpringMVC 和 Groovy.


## Overview

* Write Your Controller In Groovy

``` groovy
@Rest
class MyController extends ControllerSupport7 {
    def hello(HttpServletRequest req) {
        [code: 1, data: "Hello, ${req['name']} !"]
    }

}
```

* Open http://localhost:8080/`my`/`hello`?name=world

``` json
{"code":1,"data":"Hello, world !"}
```



##How To Use


### 拉取 example 目录

``` bash use svn
$ svn checkout https://github.com/mahuabian/ttpod-rest/trunk/example
```
OR
``` bash use git
$ git archive --remote git@github.com:mahuabian/ttpod-rest.git HEAD:example | tar xf -
```


### Maven

```xml
<project>
    <repositories>
        <repository>
            <id>ttpod-repo</id>
            <url>http://dl.bintray.com/shangqingxiaai/maven/</url>
        </repository>
    </repositories>
</project>


<dependency>
    <groupId>com.ttpod</groupId>
    <artifactId>ttpod-rest</artifactId>
    <version>${rest.version}</version>
</dependency>    
```

where ${rest.version} is the last ttpod-rest version, current is `1.3`.

### Gradle

``` groovy
  compile 'com.ttpod:ttpod-rest:1.3'
```  

  




License is Apache2, see:

  http://www.apache.org/licenses/LICENSE-2.0.html
  






