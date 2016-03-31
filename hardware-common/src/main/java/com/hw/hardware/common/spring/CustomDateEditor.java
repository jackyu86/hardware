package com.hw.hardware.common.spring;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * 日期绑定
 * @author cfish
 * @since 2013-09-09
 */
public class CustomDateEditor extends PropertyEditorSupport {
	private final boolean allowEmpty;
	private DateFormat dateFormat;
	
	public CustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}
	
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
			return;
		}
		try {
			if(text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
				this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if(text.matches("\\d{4}年\\d{2}月\\d{2}日")) {
				this.dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			} else if(text.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
				this.dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			} else {
				this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			}
			this.dateFormat.setLenient(false);
			setValue(this.dateFormat.parse(text));
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
		}
	}
	
	public String getAsText() {
		Date value = (Date) getValue();
		if(this.dateFormat != null) {
			this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}
		return (value != null ? this.dateFormat.format(value) : "");
	}
}
