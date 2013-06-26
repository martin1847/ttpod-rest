package com.ttpod.rest.anno;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * date: 13-6-4 下午5:25
 *
 * @author: yangyang.cong@ttpod.com
 */
@java.lang.annotation.Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass("com.ttpod.rest.anno.RestStaticCompileProcessor")
public @interface Rest {
    Class[] value = {Controller.class};
}
/**
 package com.ttpod.rest.groovy

 import groovy.transform.AnnotationCollector
 import groovy.transform.CompileStatic
 import org.springframework.stereotype.Controller
    @Controller
    @CompileStatic
    @AnnotationCollector
    public @interface Rest {

    }
**/