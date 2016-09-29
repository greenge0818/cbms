package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class DailyBillTransaction extends BaseTransaction implements Serializable{
	private static final long serialVersionUID = -7391733939791222800L;

	private String oSttDate;
	private String setFile;

	public String getoSttDate() {
		return oSttDate;
	}
	public void setoSttDate(String oSttDate) {
		this.oSttDate = oSttDate;
	}
	public String getSetFile() {
		return setFile;
	}
	public void setSetFile(String setFile) {
		this.setFile = setFile;
	}
	
}
