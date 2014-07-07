#TTPOD REST



CoC 理念的一套HTTP JSON API框架，基于SpringMVC 和 Groovy，内置了 MongoDB CRUD模版（JSON和MongoDB天然好基友）！！


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

                               
* Open http://localhost:8080/`my`/`hello`?name=World


``` json
{"code":1,"data":"Hello, World !"}
```

That's ALL !!!

* the main **Convention**


As you see,`my` is the Controller Name (first lowercase) and `hello` is the method Name.This is The main `Convention`.   
Don't forget the  `@Rest` annotation on the controller.    
>`@Rest` =  `@Controller` (SpringMVC) + `@CompileStatic` (Groovy)    
>`@RestWithSesson` = `@Controller` +  `@CompileStatic` + `@Interceptors`


##Quick Start


* 拉取 example 目录


``` bash
$ svn checkout https://github.com/mahuabian/ttpod-rest/trunk/example
```
* 目录结构

```
src
|-- main
|   |-- groovy
|   |   `-- com
|   |       `-- example
|   |           `-- web
|   |               |-- CrudController.groovy
|   |               `-- MyController.groovy
|   |-- java
|   |   `-- com
|   |       `-- example
|   |           |-- session
|   |           |   `-- MySessionInterceptor.java
|   |           `-- web
|   |               `-- Base.java
|   |-- resources
|   |   |-- application.properties
|   |   |-- logback.xml
|   |   |-- spring
|   |   |   |-- application.xml
|   |   |   `-- mongodb.xml
|   |   `-- test.properties
|   `-- webapp
|       `-- WEB-INF
|           |-- dispatcher-servlet.xml
|           `-- web.xml
```

* 代码行数 :example/src/main

```
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
XML                              5             31             19            124
Groovy                           2             20             27             44
Java                             2             11             21             16
-------------------------------------------------------------------------------
SUM:                             9             62             67            184
```

* 运行构建

``` bash
$ mvn jetty:run
$ gradle jettyRun
```


## 环境依赖

### JDK 6
  
   项目支持 jdk6 和 jdk7 ，默认分支为jdk6。

   jdk7提供了`dynamic`指令， 已经使用替代反射调用。如果jdk6的话，只能使用反射。
   
   [jdk7 use ControllSupport7 with MethodHandle](https://github.com/mahuabian/ttpod-rest/blob/master/ttpod-rest/src/main/java/com/ttpod/rest/web/support/ControllerSupport7.java)
   
   [jdk6 use ControllSupport with reflect ](https://github.com/mahuabian/ttpod-rest/blob/master/ttpod-rest/src/main/java/com/ttpod/rest/web/support/ControllerSupport.java)
   

### Download jar

<https://bintray.com/shangqingxiaai/maven/ttpod-rest/view>


### Maven

需要添加 [jcenter](http://jcenter.bintray.com/) 仓库

```xml
<dependency>
    <groupId>com.ttpod</groupId>
    <artifactId>ttpod-rest</artifactId>
    <version>${restVersion}</version>
</dependency>
<!-- 激活jdk7  -->
<dependency>
    <groupId>com.ttpod</groupId>
    <artifactId>rest-jdk7</artifactId>
    <version>${restVersion}</version>
</dependency>
```

where ${restVersion} is the lasted ttpod-rest version, current is `1.4.0`.

### Gradle

``` groovy
repositories {
    .....
    jcenter()
    .....
}
  
  compile "com.ttpod:ttpod-rest:${restVersion}"
  compile "com.ttpod:rest-jdk7:${restVersion}" //激活jdk7
```  

  

## 关于性能

这是一个无法逃避的话题。对Groovy代码性能的担忧发表一下个人观点：
损失10%～20%左右的性能，提升5～10倍的开发效率，你怎么看？

至于为什么损失能控制到10%，完全归功于 [@CompileStatic](http://groovy.codehaus.org/Runtime+vs+Compile+time,+Static+vs+Dynamic)

<http://java-performance.info/static-code-compilation-groovy-2-0/>

个人去看了下`CompileStatic`加与不加编译之后的class文件，使用 [jd-gui](jd.benow.ca/#jd-gui-download) 打开，没有CompileStatic的都是callsite这种全反射调用，而加上CompileStatic之后编译的字节码和普通java编译的并无太大区别，都是直接调用。

建议感兴趣的朋友自己动手去试验一下，欢迎一起讨论 ！！


### 关于开源

一直在使用开源，其实回馈之心由来已久，只是苦于能力有限，无法提供有价值的东西给社区。这套rest框架在我们公司 [天天动听](http://www.ttpod.cn) 多个后台API项目如搜索、音效、歌单等项目稳定运行，最长项目已运行一年半以上，对于开发效率的提升还是能有些价值的，另外一点价值就是拦截了每个请求的处理时间，默认超过2秒会在日志打印出来，这个也帮助我们可以尽早发现需要改进的API方法。

```log
23:01:11.992 INFO c.t.r.w.s.ControllerSupport < slow request : /test/search ,cost : 1633 ms >
23:01:12.002 INFO c.t.r.w.s.ControllerSupport < slow request : /test/search ,cost : 2218 ms >
```

欢迎有类似场景需求的朋友参与讨论，如果你打算试用的话，可以通知到我，谢谢！！ 

cyy2cyy@gmail.com

个人博客 <http://bianzi.me>




License is Apache2, see:

  http://www.apache.org/licenses/LICENSE-2.0.html
  






