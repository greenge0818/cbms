package com.prcsteel.platform.order.model.query;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.prcsteel.platform.common.query.PagedQuery;

public class ReportSellerInvoiceInDataQuery extends PagedQuery {

	private Long sellerId;

    private String sellerName;
    
    private Date startTime;

    private Date endTime; 

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		if(null != endTime) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			this.endTime = calendar.getTime();
		}
	}
    
    
}
