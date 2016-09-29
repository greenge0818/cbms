package com.prcsteel.platform.kuandao.common.constant;

import org.springframework.beans.factory.annotation.Value;

public class PrcsteelAccount {
	
	@Value("${prcsteelAccount.memeberCode}")
	private String memeberCode;
	
	@Value("${prcsteelAccount.memeberName}")
	private String memeberName;
	
	@Value("${prcsteelAccount.bankName}")
	private String bankName;
	
	@Value("${prcsteelAccount.virAcctNo}")
	private String virAcctNo;
	
	@Value("${prcsteelAccount.acctNo}")
	private String acctNo;
	
	@Value("${prcsteelAccount.idNo}")
	private String idNo;
	
	@Value("${prcsteelAccount.mobile}")
	private String mobile;
	
	public String getMemeberCode(){
		return this.memeberCode;
	}
	
	public String getMemeberName(){
		return this.memeberName;
	}
	
	public String getBankName(){
		return this.bankName;
	}
	
	public String getVirAcctNo(){
		return this.virAcctNo;
	}
	
	public String getAcctNo(){
		return this.acctNo;
	}
	
	public String getIdNo(){
		return this.idNo;
	}
	
	public String getMobile(){
		return this.mobile;
	}
}
