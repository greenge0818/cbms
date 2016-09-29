package com.prcsteel.platform.acl.model.enums;
/**
 * 
 * @author zhoukun
 */
public enum SysSettingType {
    Invoice("Invoice", "发票设置"),
    Transaction("Transaction", "到账异常黑名单设置"),
    BuyerAllowAmountTolerance("BuyerAllowAmountTolerance", "买家金额容差设置"),
    BuyerAllowAmountToleranceAfterContractChanged("BuyerAllowAmountToleranceAfterContractChanged", "合同变更后是否重新关联买家金额容差设置"),
    SmsTemplateAddOrder("SmsTemplateAddOrder", "新开单通知审核人"),
    SmsTemplateOrderAccept("SmsTemplateOrderAccept", "订单审核通过通知交易员"),
    SmsTemplateApplyPay("SmsTemplateApplyPay", "申请付款通知审核人"),
    SmsTemplatePayAccept("SmsTemplatePayAccept", "审核付款通过通知财务确认"),
    SmsTemplateRefusePay("SmsTemplateRefusePay", "审核付款不通过通知申请者"),
    SmsTemplateOrderRefuse("SmsTemplateOrderRefuse", "订单审核不通过通知交易员"),
    SmsTemplateOrderClose("SmsTemplateOrderClose", "订单关闭通知交易员"),
    SmsTemplateTypeInpickup("SmsTemplateTypeInpickup", "双敲不匹配通知"),
    SmsTemplatePayConfirm("SmsTemplatePayConfirm", "财务确认付款通知交易员"),
    SmsTemplateSettle("SmsTemplateSettle", "二结完成通知交易员"),
    SmsTemplateCallBack("SmsTemplateCallBack", "核算会计打回通知交易员"),
    InvoiceOutApplySecond("InvoiceOutApplySecond", "销项票申请开票二次结算额度控制"),
    CreditLimit("credit_limit", "信用额度"),
    ReportOrgDay("ReportOrgDay", "代运营服务中心日报Job信息设置"),
    PayError("PayError", "出纳确认付款错误"),
    InvoiceOutLastRecord("InvoiceOutLastRecord", "发票回传的最大id"),

	SMART_WEIGHT_WARNING("smart_weight_warning", "智能匹配重量容差报警设置");
    // 成员变量
    private String code;

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

    // 构造方法
    private SysSettingType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
