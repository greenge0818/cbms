package com.prcsteel.payment.bdl.model.account;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="historyRes")
public class HistoryQueryRes {

	private String receiptBankNo; //行号
	private String receiptBankName; //行名
	private String receiptAccNo; //账号
	private String receiptAccName; //户名
	private String amount;  //小数点两位
	private String date;	//date
	private String datetime; //time
	private String remark;
	private String flag;  //借贷标志 0：借(转出) 1：贷(转入)
	private String seqNo; //交易流水号

	public HistoryQueryRes() {
	}


	public HistoryQueryRes(String receiptBankNo, String receiptBankName,
			String receiptAccNo, String receiptAccName, String amount,
			String date, String datetime, String remark, String flag,
			String seqNo) {
		super();
		this.receiptBankNo = receiptBankNo;
		this.receiptBankName = receiptBankName;
		this.receiptAccNo = receiptAccNo;
		this.receiptAccName = receiptAccName;
		this.amount = amount;
		this.date = date;
		this.datetime = datetime;
		this.remark = remark;
		this.flag = flag;
		this.seqNo = seqNo;
	}


	public String getReceiptBankNo() {
		return receiptBankNo;
	}


	public void setReceiptBankNo(String receiptBankNo) {
		this.receiptBankNo = receiptBankNo;
	}


	public String getReceiptBankName() {
		return receiptBankName;
	}


	public void setReceiptBankName(String receiptBankName) {
		this.receiptBankName = receiptBankName;
	}


	public String getReceiptAccNo() {
		return receiptAccNo;
	}


	public void setReceiptAccNo(String receiptAccNo) {
		this.receiptAccNo = receiptAccNo;
	}


	public String getReceiptAccName() {
		return receiptAccName;
	}


	public void setReceiptAccName(String receiptAccName) {
		this.receiptAccName = receiptAccName;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getDatetime() {
		return datetime;
	}


	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getSeqNo() {
		return seqNo;
	}


	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}





}
