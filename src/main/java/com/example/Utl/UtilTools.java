package com.example.Utl;

import java.util.Random;

public class UtilTools {

	public UtilTools() {
		// TODO Auto-generated constructor stub
	}
	
	 public static String getRandomStr(int bytes){
	        StringBuilder sb = new StringBuilder();
	        Random random = new Random();
	        for (int i = 0; i < bytes; i++) {
	            //随机判断判断该字符是数字还是字母
	            String choice = random.nextInt(2) % 2 == 0 ? "char" : "num";
	            if ("char".equalsIgnoreCase(choice)) {
	                //随机判断是大写字母还是小写字母
	                int start = random.nextInt(2) % 2 == 0 ? 65 : 97;
	                sb.append((char) (start + random.nextInt(26)));
	            } else if ("num".equalsIgnoreCase(choice)) {
	                sb.append(random.nextInt(10));
	            }
	        }
	        return sb.toString();
	    }

}
