package com.prcsteel.platform.account.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Title: AttachmentType.java
 * @Package com.prcsteel.cbms.persist.model
 * @Description: TODO
 * @author Green.Ge
 * @date 2015年7月15日 下午4:01:27
 * @version V1.0
 */
public enum AttachmentType {
	pic_license("license", "营业执照"), 
	pic_org_code("org_code", "组织机构代码证"), 
	pic_tax_reg("tax_reg", "税务登记证"),
	pic_taxpayer_evidence("taxpayer_evidence", "一般纳税人证明"), 
	pic_invoice_data("invoice_data", "开票资料"), 
	pic_legal_ID("legal_ID", "法人身份证"), 
	pic_payment_data("payment_data", "打款资料"),
	pic_consign_contract("consign_contract", "代运营合同"),
	pic_purchase_agreement("purchase_agreement", "年度采购协议"),
	pic_enterprise_survey("enterprise_survey","企业调查表"),
	pic_open_account_license("open_account_license","开户许可证"),
	pic_credit_code("credit_code","统一社会信用代码号"),
	pic_accept_draft("accept_draft","银行承兑汇票");
	// 成员变量
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private String code;

	// 构造方法
	private AttachmentType(String code, String name) {
		this.name = name;
		this.code = code;
	}

	public static AttachmentType getAttachmentTypeByCode(String code) {
		Optional<AttachmentType> res = Stream.of(AttachmentType.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get() : null;
	}
}
