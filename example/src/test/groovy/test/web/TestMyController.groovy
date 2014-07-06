package test.web

import com.example.web.MyController
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import test.BaseTest

import javax.annotation.Resource

/**
 * date: 14-7-3 16:24
 * @author: yangyang.cong@ttpod.com
 */
class TestMyController extends BaseTest{

    @Resource
    MyController myController

    @Test
    void testModule() {
        def req = new MockHttpServletRequest()
        req.name = "World"

        assert null != myController.hello(req)
    }
}
