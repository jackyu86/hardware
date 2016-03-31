package com.hw.hardware.common.spring.velocity;

import org.apache.velocity.app.event.implement.EscapeReference;

/**
 * 过滤HTML、XML标签,防止XSS
 * @author cfish
 * @since 2013-09-09
 */
public class EscapeHtmlReference extends EscapeReference {

	protected String escape(Object text) {
		if (!(text instanceof String)) {
			return text.toString();
		}
		StringBuffer str = new StringBuffer();
		char[] cs = text.toString().toCharArray();
		for (char c : cs) {
			if (c == '>') {
				str.append("&gt;");
			} else if (c == '<') {
				str.append("&lt;");
			} else {
				str.append(c);
			}
		}
		return str.toString();
	}
	
	protected String getMatchAttribute() {
		return "eventhandler.escape.html.match";
	}

}
