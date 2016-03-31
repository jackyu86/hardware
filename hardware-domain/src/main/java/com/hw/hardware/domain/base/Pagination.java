package com.hw.hardware.domain.base;

public class Pagination {
	private int currPage = 1;// 当前页码

	private int prePageSize = 10;// 每页记录数

	private int totalPage = 1;// 总页数

	private int totalRecord = 0;// 总记录数

	private int beginIndex = 0;// 开始记录数

	private int startPage; // 开始页面

	private int endPage; // 结束页面

	private int displayPageCount = 5; // 显示的页码个数

	public Pagination() {
		this(10, 1);
	}

	/**
	 * 
	 * @param prePageSize 每页记录数，默认：10
	 * @param currPage 当前页码，默认：1
	 */
	public Pagination(int prePageSize, int currPage) {
		this(5, prePageSize, currPage);
	}

	/**
	 * 
	 * @param displayPageCount 显示的页码个数，默认：5
	 * @param prePageSize 每页记录数，默认：10
	 * @param currPage 当前页码，默认：1
	 */
	public Pagination(int displayPageCount, int prePageSize, int currPage) {
		this.displayPageCount = displayPageCount;
		this.prePageSize = prePageSize;
		if (currPage <= 0) {
			currPage = 1;
		}
		this.currPage = currPage;
		this.beginIndex = (currPage - 1) * prePageSize;
	}

	public void countPages() {
		if (currPage - displayPageCount / 2 < 1) {
			startPage = 1;
			endPage = displayPageCount > totalPage ? totalPage : displayPageCount;
		} else if (currPage + displayPageCount / 2 > totalPage) {
			int n = totalPage - displayPageCount + 1;
			startPage = n > 0 ? n : 1;
			endPage = totalPage;
		} else {
			startPage = currPage - displayPageCount / 2;
			endPage = startPage + displayPageCount - 1;
		}
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		if (currPage <= 0) {
			currPage = 1;
		}
		this.currPage = currPage;
		this.beginIndex = (currPage - 1) * prePageSize;
	}

	public int getPrePageSize() {
		return prePageSize;
	}

	public void setPrePageSize(int prePageSize) {
		this.prePageSize = prePageSize;
		this.beginIndex = (currPage - 1) * prePageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		// 计算总记录数
		totalPage = (totalRecord + prePageSize - 1) / prePageSize;
		if (totalPage <= 0) {
			totalPage = 1;
		}
		this.countPages();
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getDisplayPageCount() {
		return displayPageCount;
	}

	public void setDisplayPageCount(int displayPageCount) {
		this.displayPageCount = displayPageCount;
	}

}
