package com.prcsteel.platform.common.query;
/**
 * 
 * @author zhoukun
 */
public class PagedQuery {

	private int start = 0;

	private int length = 10;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
