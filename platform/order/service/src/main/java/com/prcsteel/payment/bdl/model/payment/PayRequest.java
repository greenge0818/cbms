package com.prcsteel.payment.bdl.model.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="PayRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayRequest implements Serializable {

	String payAccountNo;
	String payAccountName;
	String recAccountNo;
	String recAccountName;
	String recBankName;
	String recBankNo;
	String recBankCity;
	boolean isSameBank;
	long payAmount; //精确到分

	public PayRequest() {
	}

	public PayRequest(String payAccountNo, String payAccountName,
			String recAccountNo, String recAccountName, String recBankName,
			String recBankNo, String recBankCity, boolean isSameBank,
			long payAmount) {
		super();
		this.payAccountNo = payAccountNo;
		this.payAccountName = payAccountName;
		this.recAccountNo = recAccountNo;
		this.recAccountName = recAccountName;
		this.recBankName = recBankName;
		this.recBankNo = recBankNo;
		this.recBankCity = recBankCity;
		this.isSameBank = isSameBank;
		this.payAmount = payAmount;
	}

	public String getPayAccountNo() {
		return payAccountNo;
	}

	public void setPayAccountNo(String payAccountNo) {
		this.payAccountNo = payAccountNo;
	}

	public String getPayAccountName() {
		return payAccountName;
	}

	public void setPayAccountName(String payAccountName) {
		this.payAccountName = payAccountName;
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

	public String getRecBankName() {
		return recBankName;
	}

	public void setRecBankName(String recBankName) {
		this.recBankName = recBankName;
	}

	public String getRecBankNo() {
		return recBankNo;
	}

	public void setRecBankNo(String recBankNo) {
		this.recBankNo = recBankNo;
	}

	public String getRecBankCity() {
		return recBankCity;
	}

	public void setRecBankCity(String recBankCity) {
		this.recBankCity = recBankCity;
	}

	public boolean isSameBank() {
		return isSameBank;
	}

	public void setSameBank(boolean isSameBank) {
		this.isSameBank = isSameBank;
	}

	public long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}


}
