package org.prcsteel.rest.sdk.activiti.enums;


/**
 * 工作流审核枚举参数
 * @author tangwei
 *
 */
public enum WorkFlowEnum {

	CARD_INFO("card_info",1),//证件资料审核
	INVOICE_DATA("invoice_data",2),//开票资料审核
	BANK_DATA("bank_data",3),//打款资料审核
	ANNUAL_PURCHASE_AGREEMENT("annual_purchase_agreement",4),//年度采购协议
	SELLER_CONSIGN_AGREEMENT("seller_consign_agreement",5),//卖家待运营协议
	CONTRACT_TEMPLATE("contract_template",6),//合同模板
	ACCOUNT_AUDIT_CARDINFO("account:audit:cardinfo",10000),//审核证件资料权限
	ACCOUNT_AUDIT_INVOICE("account:audit:invoice",10001),//审核开票资料权限
	ACCOUNT_AUDIT_BANK("account:audit:bank",10002),//审核打款资料权限
	ACCOUNT_AUDIT_ANNUAL_PURCHASE_MANAGER("account:audit:annual_purchase_manager",10003),//审核年度采购协议总经理权限
	ACCOUNT_AUDIT_ANNUAL_PURCHASE_FINANCE("account:audit:annual_purchase_finance",10004),//审核年度采购协议财务权限
	ACCOUNT_AUDIT_ANNUAL_PURCHASE_SERVICE("account:audit:annual_purchase_service",10005),//审核年度采购协议综服权限
	ACCOUNT_AUDIT_SELLER_CONSIGN_MANAGER("account:audit:seller_manager",10006),//审核卖家代运营协议总经理权限
	ACCOUNT_AUDIT_SELLER_CONSIGN_FINANCE("account:audit:seller_finance",10007),//审核卖家代运营协议财务权限
	ACCOUNT_AUDIT_SELLER_CONSIGN_SERVICE("account:audit:seller_service",10008),//审核卖家代运营协议综服权限
	ACCOUNT_AUDIT_CONTRACT_TEMPLATE("account:audit:contract_template",10009);//审核合同模板权限

	WorkFlowEnum(String name) {
		this.name = name;
	}

	WorkFlowEnum(Integer value) {
		this.value = value;
	}

	WorkFlowEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	private String name;
	private Integer value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static String getNameByValue(int value){
		for(WorkFlowEnum enumm :WorkFlowEnum.values()){
			if(enumm.getValue().intValue() == value){
				return enumm.getName();
			}
		}
		return "";
	}
}