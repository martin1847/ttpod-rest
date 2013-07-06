/*
 * Copy Right,fangjia.com 2010-2020
 */
package com.ttpod.rest.common.util;

import java.util.*;

/**
 * 解析字符串中是否包含字典中的关键字.
 * 
 * @author congyangyang 2011-9-13
 */
public final class CharTree {

	//private static final CharTree END = new CharTree();

	private static final CharTree ROOT = new CharTree(10000);

	private final Map<Character, CharTree> childs;
	
	
	private boolean endFlag;
	
	private boolean isEndFlag() {
		return endFlag;
	}
	private void setEndFlag(boolean endFlag) {
		this.endFlag = endFlag;
	}
	
	private CharTree(int size) {
		if( 0 == size){
			childs= new HashMap<Character, CharTree>();
		}else{
			childs= new HashMap<Character, CharTree>(size);
		}
	}
	private CharTree() {
		this(0);
	}

	private CharTree getCildTree(char c) {
		return childs.get(c);
	}

	private void addChar(char c, CharTree t) {
		childs.put(c, t);
	}

	public static CharTree getInstance() {
		return ROOT;
	}

	public static CharTree newInstance(int rootSize) {
		return new CharTree(rootSize);
	}
	/**
	 * 增加字典
	 * @param keyword,关键字
	 */
	public CharTree addDic(String keyword) {

		char[] chars = keyword.toCharArray();
		int len = chars.length;
		CharTree begin = this;
		for (int i = 0; i < len; i++) {
			CharTree ctree = begin.getCildTree(chars[i]);
			if (ctree == null) {
				ctree = new CharTree();
				ctree.setEndFlag(i == len - 1 );
				begin.addChar(chars[i], ctree);
			}
			begin = ctree;
		}
		return this;
	}
	/**
	 * 增加字典
	 * @param keyword,关键字
	 */
	public CharTree addDic(String... dics) {
		for (String keyword : dics) {
			addDic(keyword);
		}
		return this;
	}

	public Collection<String> seg(String text) {
		return seg(text, Integer.MAX_VALUE);
	}
	
	private Comparator<String> STRING_DESC = new Comparator<String>() {
		/**
		 * 倒序排列
		 */
		public int compare(String o1, String o2) {
			return  - o1.compareTo(o2);
		}
	};
	
	public Collection<String> seg(String text,int maxWords) {
		char[] chars = text.toCharArray();
		int len = chars.length;
		Collection<String> res = new TreeSet<String>(STRING_DESC);
		FOR:for (int i = 0; i < len;) {
			CharTree child =this;
			int j = 0;
			int offset=0;
			while (true) {
				offset =  i + j++;
				
				if(offset ==len){//最后一个单词 比如 张三张
					break FOR;
				}
				
				child = child.getCildTree(chars[offset]);
				if (child == null) {
					i++;
					break;
				}
				
				if (child.isEndFlag() ){
					offset++;//往前探测一个字符
					if( (offset ==len) || (null == child.getCildTree(chars[offset]))) {//不使用最多分词，使用最长,提高效率
						res.add(new String(chars, i, j));
						
						if(res.size() == maxWords){
							break FOR;
						}
						i += j;
						break;
					}
				}
			}
		}
		return res;
	}

}
