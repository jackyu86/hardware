package com.hw.hardware.common.menu;

import java.util.List;

public class MenuObject {
	
	private Integer totalCount;
	
	private List<Menu> menus;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
}
