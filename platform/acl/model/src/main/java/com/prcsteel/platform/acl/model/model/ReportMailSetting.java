package com.prcsteel.platform.acl.model.model;

import java.util.Date;

import com.prcsteel.platform.acl.model.enums.ReportMailPeriodType;

/**
 * 用来保存邮件设置
 * @author tuxianming
 */
public class ReportMailSetting {
	private Long id;
	private String title;		///邮件标题
	private String content;  	//邮件内容
	private String attachment;	//附件类型名称 
	private String sendTime;	//发送时间
	private String receiver;	//邮件接受者:是一个以英文分号（;）分割的email
	private Date lastSend;		//最后发送日期
	
	private ReportMailPeriodType periodType;  //week, month
	
	//当period=week的时候，表示： 第几周的：星期一，星期二，星期三...
	//当type=WEEK时，weeks为数组，当为MONTH2时， weeks:为一个数值
	private String weeks;
	
	//当period=MONTH1的时候, 
	//表示：每隔几个月的第几天
	private Integer monthDuration;
	private Integer day;

	//当period=MONTH2的时候, 
	//表示：每隔几个月的第几个星期的第几天
	private Integer weekDuration;

	private String ext;
	
	private String createdBy;	//规则创建者
	private Date created;		//规则创建时间
	/////系统字段，与具体业务无关/////
    private Date lastUpdated;
    private String lastUpdatedBy;
    private Integer modificationNumber;
    private String rowId;
    private String parentRowId;
	public ReportMailSetting(){}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ReportMailPeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(ReportMailPeriodType periodType) {
		this.periodType = periodType;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public Integer getMonthDuration() {
		return monthDuration;
	}

	public void setMonthDuration(Integer monthDuration) {
		this.monthDuration = monthDuration;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getWeekDuration() {
		return weekDuration;
	}

	public void setWeekDuration(Integer weekDuration) {
		this.weekDuration = weekDuration;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}


	public Date getLastSend() {
		return lastSend;
	}


	public void setLastSend(Date lastSend) {
		this.lastSend = lastSend;
	}


	public Date getLastUpdated() {
		return lastUpdated;
	}


	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	public Integer getModificationNumber() {
		return modificationNumber;
	}


	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}


	public String getRowId() {
		return rowId;
	}


	public void setRowId(String rowId) {
		this.rowId = rowId;
	}


	public String getParentRowId() {
		return parentRowId;
	}


	public void setParentRowId(String parentRowId) {
		this.parentRowId = parentRowId;
	}

}
