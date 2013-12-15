package test.json
import com.ttpod.rest.common.util.JSONUtil
import com.ttpod.rest.common.util.jackson.JsonRawString
/**
 * date: 13-12-15 下午2:53
 * @author: yangyang.cong@ttpod.com
 */
class TestJsonRaw extends GroovyTestCase{


    void testRaw(){
        def raw = new JsonRawString("[123,456]")
        def obj = ["name":"jackson","raw":raw]
        assert '{"name":"jackson","raw":[123,456]}' ==  JSONUtil.beanToJson(obj)
    }

    void testRawExecption(){
        shouldFail   {
            new JsonRawString('[123,aa]')
        }

        shouldFail   {
            new JsonRawString('"[123,aa]')
        }

        shouldFail   {
            new JsonRawString('{123,aa}')
        }

        shouldFail   {
            new JsonRawString('[123:456]')
        }

//        new JsonRawString("[123,aa]")
//        println JSONUtil.jsonToBean("[123,as]7", ArrayList.class);

    }
}
