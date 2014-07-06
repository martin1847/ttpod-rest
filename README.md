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


As you see,`my` is the Controller Name (first lowercase) and `hello` is the method Name.  
This is the main **Convention**.  
Don't forget the  `@Rest` annotation on the controller.
In fact,it just equals `@Controller` (SpringMVC) + `@CompileStatic` (Groovy).



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

```
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
XML                              5             27             19            122
Java                             2             11             21             16
Groovy                           1              5              0              9
-------------------------------------------------------------------------------
SUM:                             8             43             40            14
```

* 运行项目需要gradle

``` bash
gradle jettyRun
```









## 获取依赖 

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

where ${restVersion} is the lasted ttpod-rest version, current is `1.3.1`.

### Gradle

``` groovy
repositories {
    .....
    maven { url "http://dl.bintray.com/shangqingxiaai/maven/" }
    .....
}
  
  compile "com.ttpod:ttpod-rest:${restVersion}"
```  

  




License is Apache2, see:

  http://www.apache.org/licenses/LICENSE-2.0.html
  






