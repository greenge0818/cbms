package com.prcsteel.payment.bdl.model.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="PaymentRet")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentRet implements Serializable {

	private String recAccountNo;
	private String recAccountName;
	private String amt;

	public PaymentRet() {
	}
	public PaymentRet(String recAccountNo, String recAccountName, String amt) {
		super();
		this.recAccountNo = recAccountNo;
		this.recAccountName = recAccountName;
		this.amt = amt;
	}
	public String getRecAccountNo() {
		return recAccountNo;
	}
	public void setRecAccountNo(String recAccountNo) {
		this.recAccountNo = recAccountNo;
	}
	public String getRecAccountName() {
		return recAccountName;
	}
	public void setRecAccountName(String recAccountName) {
		this.recAccountName = recAccountName;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}





}
