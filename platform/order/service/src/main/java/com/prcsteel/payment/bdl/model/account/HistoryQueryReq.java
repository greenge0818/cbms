package com.prcsteel.payment.bdl.model.account;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="HistoryQueryReq")
public class HistoryQueryReq {
	private String acctNo;  //账号
	private String beginDate;   //开始日期YYYYMMDD
	private String endDate; //结束日期YYYYMMDD
	private String beginNumber;//查询的起始笔数，默认为1

	public HistoryQueryReq(String acctNo, String beginDate, String endDate,
			String beginNumber) {
		super();
		this.acctNo = acctNo;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.beginNumber = beginNumber;
	}



	public HistoryQueryReq() {
	}



	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBeginNumber() {
		return beginNumber;
	}
	public void setBeginNumber(String beginNumber) {
		this.beginNumber = beginNumber;
	}

}
