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
├── main
│   ├── groovy
│   │   └── com
│   │       └── example
│   │           └── web
│   │               └── MyController.groovy
│   ├── java
│   │   └── com
│   │       └── example
│   │           ├── session
│   │           │   └── MySessionInterceptor.java
│   │           └── web
│   │               └── Base.java
│   ├── resources
│   │   ├── application.properties
│   │   ├── logback.xml
│   │   ├── spring
│   │   │   ├── application.xml
│   │   │   └── mongodb.xml
│   │   └── test.properties
│   └── webapp
│       └── WEB-INF
│           ├── dispatcher-servlet.xml
│           └── web.xml
```

* 代码行数 :example/src/main

```
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
XML                              5             27             19            122
Java                             2             11             21             16
Groovy                           1              5              0              9
-------------------------------------------------------------------------------
SUM:                             8             43             40            14
```

* 运行构建 gradle

``` bash
gradle jettyRun
```


## 环境依赖

### JDK 7
  
   项目支持 jdk6 和 jdk7 ，默认分支为jdk7，jdk6 需要自行构建，jdk7提供了`dynamic`指令， 已经使用替代反射调用。如果jdk6的话，只能使用反射。
   
   [jdk7 use ControllSupport7 with MethodHandle](https://github.com/mahuabian/ttpod-rest/blob/master/ttpod-rest/src/main/java/com/ttpod/rest/web/support/ControllerSupport7.java)
   
   [jdk6 use ControllSupport with reflect ](https://github.com/mahuabian/ttpod-rest/blob/master/ttpod-rest/src/main/java/com/ttpod/rest/web/support/ControllerSupport.java)
   

### Download jar

<https://bintray.com/shangqingxiaai/maven/ttpod-rest/view>


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
    <version>${restVersion}</version>
</dependency>    
```

where ${restVersion} is the lasted ttpod-rest version, current is `1.3.2`.

### Gradle

``` groovy
repositories {
    .....
    maven { url "http://dl.bintray.com/shangqingxiaai/maven/" }
    .....
}
  
  compile "com.ttpod:ttpod-rest:${restVersion}"
```  

  

## 关于性能

这是一个无法逃避的话题。对Groovy代码性能的担忧发表一下个人观点：
损失10%～20%左右的性能，提升5～10倍的开发效率，你怎么看？

至于为什么损失能控制到10%，完全归功于 [@CompileStatic](http://groovy.codehaus.org/Runtime+vs+Compile+time,+Static+vs+Dynamic)

<http://java-performance.info/static-code-compilation-groovy-2-0/>

个人去看了下`CompileStatic`加与不加编译之后的class文件，使用 [jd-gui](jd.benow.ca/#jd-gui-download) 打开，没有CompileStatic的都是callsite这种全反射调用，而加上CompileStatic之后编译的字节码和普通java编译的并无太大区别，都是直接调用。

建议感兴趣的朋友自己动手去试验一下，欢迎一起讨论！！



License is Apache2, see:

  http://www.apache.org/licenses/LICENSE-2.0.html
  






