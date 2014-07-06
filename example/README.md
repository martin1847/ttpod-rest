#Rest Example

项目原型


## 构建工具 gradle 
[Gradle Download](http://www.gradle.org/)

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

``` bash
$gradle jettyRun
```

* 运行单元测试

``` bash
$gradle test
```








