package com.prcsteel.platform.smartmatch.model.query;


/**
 * Created by peanut on 2016/1/7.
 */
public class OneKeyOprtResourceQuery extends ResourceQuery{
	private String oriStatus;
	private String toStatus;

	public String getOriStatus() {
		return oriStatus;
	}
	public void setOriStatus(String oriStatus) {
		this.oriStatus = oriStatus;
	}
	public String getToStatus() {
		return toStatus;
	}
	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}

}
