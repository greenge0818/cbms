package com.prcsteel.platform.order.model.query;

public class FphcQuery {
	private String id;
	private int start = 0;
	private int length = 1000;

	public int getStart() {
		return start;
	}
	public FphcQuery setStart(int start) {
		this.start = start;
		return this;
	}
	public int getLength() {
		return length;
	}
	public FphcQuery setLength(int length) {
		this.length = length;
		return this;
	}
	public String getId() {
		return id;
	}
	public FphcQuery setId(String id) {
		this.id = id;
		return this;
	}
}
