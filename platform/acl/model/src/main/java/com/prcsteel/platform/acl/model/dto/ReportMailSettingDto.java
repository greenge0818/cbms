package com.prcsteel.platform.acl.model.dto;

import com.prcsteel.platform.acl.model.model.ReportMailSetting;

/**
 * @author tuxianming
 *
 */
public class ReportMailSettingDto extends ReportMailSetting {
	private String createTimeStr; 
	
	//用来在列表里显示 的字符串
	private String period;
	
	//把time分成如下，前台直接不传送这个值，前台直接传的是：sendTime
	private String hour;
	private String minute;
	
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

	public ReportMailSettingDto(){}

	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
}
