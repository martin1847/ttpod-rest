package test

import com.ttpod.rest.web.StaticSpring
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationContext
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.annotation.Resource

/**
 * date: 13-3-11 下午6:13
 * @author: yangyang.cong@ttpod.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/test-dispatcher-servlet.xml",initializers =ParentContext.class)
//@ContextHierarchy([
//@ContextConfiguration(name = "parent", locations = "classpath*:spring/*.xml"),
//@ContextConfiguration(name = "child",  locations = "classpath*:/spring-anno.xml")
//])
abstract class BaseTest{

    @Resource
    ApplicationContext applicationContext

    static final shouldFail = new GroovyTestCase().&shouldFail

//    static {
//        StaticSpring.context = new GenericXmlApplicationContext("classpath*:spring/*.xml")
//    }


    static  void injectParentContext(ApplicationContext parent){
        StaticSpring.context=parent

        MockHttpServletRequest.metaClass['setProperty'] = {String name,Object value->
            ((MockHttpServletRequest)delegate).addParameter(name,value.toString())
        }
    }
//
//
    void setSession(Map<String,String> session){
        OAuth2SimpleInterceptor.sessionHolder.set(session)
    }
}