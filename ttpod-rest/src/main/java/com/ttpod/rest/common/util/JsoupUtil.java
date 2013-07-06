package com.ttpod.rest.common.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *http://jsoup.org/apidocs/org/jsoup/select/Selector.html
 *
 *@author congyangyang 2011-8-31
 */
public abstract class JsoupUtil {
	
	/**
	* 根据cssSelect 获取请求URL中的文本
	 * @throws java.io.IOException
	*/
   public static List<String> getUrlList(String url,Evaluator cssSelect) throws IOException{
	   Document doc = getDocViaJsoup(url);
	   if(doc == null){
		   return Collections.emptyList();
	   }
	   Elements elements = getElements(doc, cssSelect);
	   List<String> res = new ArrayList<String>(elements.size());

	   for(Element e: elements){
		   res.add(e.attr("href"));
	   }
	   return res;
   }

   /**
	 * 请求URL，获取HTML文本
 * @throws java.io.IOException
	 */
	public static Document getDocViaJsoup(String url) throws IOException{
		Connection conn = Jsoup.connect(url);
		conn.ignoreHttpErrors(true);
		conn.timeout(8000);
		
		conn.header("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:6.0) Gecko/20100101 Firefox/6.0");
		conn.header("Accept-Language","zh-cn,zh;q=0.5");
		conn.header("Accept-Charset","GB2312,utf-8;q=0.7,*;q=0.7");
		conn.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		Set<Entry> set = headers.entrySet();
// 		for(Entry  entry:set){
//			conn.header((String)entry.getKey(), (String)entry.getValue());
//		}
		return conn.get();
	}

    public static Document getBdDocGBK(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(8000);
        InputStream stream = connection.getInputStream();
        byte[] array = FileCopyUtils.copyToByteArray(stream);

        return Jsoup.parse(new String(array, "GB18030"));
    }

	/**
	* 符和要求的元素的text
	* @param cssSelect css选择器
	* @return
	*/
   public static Elements getElements(Document doc,String cssSelect){
		return doc.select(cssSelect);
   }
   
   /**
	* 符和要求的元素的text
	* @param cssSelect css选择器
	* @return
	*/
  public static Elements getElements(Document doc,Evaluator cssParsed){
		return Collector.collect(cssParsed, doc);
  }
   
   /**
	* 符和要求的元素的text
	* @param cssSelect css选择器
	* @return
	*/
  public static Evaluator parseCssSelect(String cssSelect){
	try {
		return (Evaluator) PARSE_METHOD.invoke(null, cssSelect);
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	}
	  
  }
  private static Method PARSE_METHOD = getParseMethod();
  private static Method getParseMethod(){
		try {
			Class<?> queryParser = Class.forName("org.jsoup.select.QueryParser");
			Method parse= queryParser.getDeclaredMethod("parse", String.class);
			parse.setAccessible(true);
			return parse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
  }
	
	
}
