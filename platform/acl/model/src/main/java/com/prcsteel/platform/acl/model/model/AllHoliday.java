package com.prcsteel.platform.acl.model.model;

import java.util.Date;

public class AllHoliday {
	private String id;
	private String title;
	private Date holidayDate;
	
	public AllHoliday(String id,String title,Date holidayDate) {
		this.id = id;
		this.title = title;
		this.holidayDate = holidayDate;
	}
	
	public AllHoliday() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

}