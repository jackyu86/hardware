package com.hw.hardware.common.menu;


/**
 * menu
 * 
 * @author cfish
 * @since 2013-09-09
 */
public class Menu {

	private Long id;
	
	private String name;
	
	private Long parent;
	
	private String icon;
	
	private String url;
	
	private String isParent;
	
	private String parentResCode;
	
	private String resCodeStr;
	
	private String resCodeName;
	
	private int sortSequence;
	

	public int getSortSequence() {
		return sortSequence;
	}

	public void setSortSequence(int sortSequence) {
		this.sortSequence = sortSequence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getParentResCode() {
		return parentResCode;
	}

	public void setParentResCode(String parentResCode) {
		this.parentResCode = parentResCode;
	}

	public String getResCodeStr() {
		return resCodeStr;
	}

	public void setResCodeStr(String resCodeStr) {
		this.resCodeStr = resCodeStr;
	}

	public String getResCodeName() {
		return resCodeName;
	}

	public void setResCodeName(String resCodeName) {
		this.resCodeName = resCodeName;
	}
}