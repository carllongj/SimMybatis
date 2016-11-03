package cn.lj.utils;

public class StringUtils {
      public static String firstToUp(String str){
    	  String str1 = str.charAt(0) + "";
    	return  str1.toUpperCase() + str.substring(1);
      }
      
}
