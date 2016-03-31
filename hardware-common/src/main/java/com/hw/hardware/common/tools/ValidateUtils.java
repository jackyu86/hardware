package com.hw.hardware.common.tools;

import java.util.regex.Pattern;

/**
 * 验证
 * @author cfish
 * @since 2012-9-13
 */
public class ValidateUtils {
    
    private static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final String IP = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
    private static final String MOBILE = "^((13)|(15)|(18))\\d{9}$";
    
    public static boolean checkEmail(String email) {
        return regexp(EMAIL, email);
    }
    
    public static boolean checkIP(String ip) {
        return regexp(IP, ip);
    }
	
	public static boolean checkNumber(String num){
    	try {
    		Integer.parseInt(num, 10);
    		return true;
		} catch (Exception e) {
			return false;
		}
    }
	
	public static boolean checkMobile(String phoneNum) {
		return regexp(MOBILE, phoneNum);
	}
    
    /**
     * 正则验证
     * @param regexp
     * @param input
     * @return
     */
    public static boolean regexp(String regexp,String input){
        try {
            return Pattern.compile(regexp).matcher(input).matches();  
        } catch (Exception e) {
            return false;
        }
    }
}
