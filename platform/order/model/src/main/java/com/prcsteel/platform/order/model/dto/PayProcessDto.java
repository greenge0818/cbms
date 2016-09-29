package com.prcsteel.platform.order.model.dto;

public class PayProcessDto {
	private String requesterName;

	private String userName;

	private String operatorName;

	private String operatorRoleName;

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorRoleName() {
		return operatorRoleName;
	}

	public void setOperatorRoleName(String operatorRoleName) {
		this.operatorRoleName = operatorRoleName;
	}

}
