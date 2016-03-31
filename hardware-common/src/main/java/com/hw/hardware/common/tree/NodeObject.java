package com.hw.hardware.common.tree;

import java.util.List;

public class NodeObject {
	private Integer totalCount;
	private Integer resCount;
	private String resMsg;
	private String resStatus;
	private List<Node> data;
	
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getResCount() {
		return resCount;
	}
	public void setResCount(Integer resCount) {
		this.resCount = resCount;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getResStatus() {
		return resStatus;
	}
	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}
	public List<Node> getData() {
		return data;
	}
	public void setData(List<Node> data) {
		this.data = data;
	}
}
