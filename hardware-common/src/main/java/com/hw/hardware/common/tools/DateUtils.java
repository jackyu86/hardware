package com.hw.hardware.common.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 日期工具类
 * @author cfish
 * @since 2013-09-09
 */
public class DateUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	public static long getServerTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 格式化日期,默认返回yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化显示当前日期
	 * @param format
	 * @return
	 */
	public static String format(String format) {
		return format(new Date(), format);
	}

	/**
	 * 格式化显示当前日期
	 * @param date
	 * @param brFlag
	 * @return
	 */
	public static String format(Date date, boolean brFlag) {
	    if(null == date) {
	        return null; 
	    }
		try {
			String dateStr =  format(date, "yyyy-MM-dd HH:mm:ss");
			if(brFlag) {
				return dateStr.substring(0, 10);
			} else {
				return dateStr.substring(11, 19);
			}
		}catch(Exception e){
			LOGGER.error("日期格式化失败.{}", e.getMessage());
		}
		return null;
	}

	/**
	 * 日期格式化
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if(null!=date){
				return sdf.format(date);
			}
		} catch (Exception e) {
			LOGGER.warn("日期格式化失败", e);
		}
		return null;
	}

	/**
	 * 时间格式化， 传入毫秒
	 * @param time
	 * @return
	 */
	public static String dateFormat(long time) {
		return format(new Date(time), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 字符串转换日期型
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date format(String strDate,String format){
		Date date = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format==null?"yyyy-MM-dd HH:mm:ss":format);
			date = sdf.parse(strDate);
			return date;
		} catch (Exception e) {
			LOGGER.warn("日期格式化失败", e);
			return date;
		}
		
	}

	/** 
     * 获得指定日期的前后某天 
     *  
     * @param curDay ->基础时间字符串
     * 		  days		->跨越的天数
     * @return 
     * @throws Exception 
     */  
    public static String getOtherDay(String curDay, int days) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(curDay);  
        } catch (Exception e) {
        	LOGGER.error("日期格式化失败.{}", e.getMessage());
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + (days));
        String otherDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return otherDay;  
    }
    
    /** 
     * 获得指定日期的前后某天 
     * @param curDay ->基础时间date
     * 		  days		->跨越的天数
     * @return date
     * @throws Exception 
     */  
    public static Date getOtherDay(Date curDay, int days) {
		// 将当前统计时间提前一天
		Calendar c = new GregorianCalendar();
		c.setTime(curDay);
		c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();  
    }
}
