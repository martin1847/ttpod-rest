#Rest Example

项目原型


## 构建工具 gradle 
 ＊ 下载安装 [Gradle Download](http://www.gradle.org/)，已安装跳过，不想安装看下一条

 ＊ 或者使用 `gradlew` 命令直接运行（仍然需要联网下载gradle相关依赖，但是运行方便，好吧，其实是自动下载）

## 目录结构

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
└── test
    ├── groovy
    │   └── test
    │       ├── BaseTest.groovy
    │       ├── ParentContext.groovy
    │       └── web
    │           └── TestMyController.groovy
    └── resources
        ├── logback-test.xml
        └── test-dispatcher-servlet.xml

19 directories, 15 files 
```
## RUN IT !

* 运行项目需要gradle

安装过gradle之后，直接
``` bash 
$gradle jettyRun
```

没装的

``` bash 
$./gradlew jettyRun
$gradlew.bat jettyRun 
```

* 运行单元测试

``` bash
$gradle test
```








