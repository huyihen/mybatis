package com.hu66.mybatis.core;
/**
 * 
 * company 逸恒科技
 * @author �?66
 * @data 2020�?10�?25�?
 * Email 906430016@qq.com
 */
public class StringUtil {
	/**
	 * 空字符串判断
	 * 
	 */
	public static boolean checkNull(String ...strs) {
		if(strs == null || strs.length <= 0){
			return true;
		}
		
		for(String str : strs){
			if(str==null||str.equals("")) {
				return true;
			}
		}
		
		return false;
	}

	
}
