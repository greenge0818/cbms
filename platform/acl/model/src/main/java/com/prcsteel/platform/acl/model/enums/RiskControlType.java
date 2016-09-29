package com.prcsteel.platform.acl.model.enums;
/**
 *
 * @author wangxiao
 */
public enum RiskControlType{
    CLIENT_TYPE("client_type","设置需要审核的交易凭证"),
    EXCEEDTIME("exceedtime", "超期时间设置"),
    HINT("hint", "提示信息设置"),
    CUSTOMER_LABEL("customer_label", "客户身份标示设置"),
    BILL_SETTING("bill_setting", "能否开单设置"),
    APPLY_PAYMENT_CONTRACT_SETTING("apply_payment_contract_setting", "付款申请是否需要上传合同设置"),
    CONTROLPINSETTINGS("ControlPinSettings", "根据交易凭证审核状态控制销项申请设置"),
    
    WHITE_CUST("white_cust","白名单客户"),
    NO_WHITE_CUST("no_white_cust","非白名单客户"),


    NEW_ORDER("new_order","新开交易单界面提示"),
    AUDIT_ORDER("audit_order","服总审核订单界面提示"),
    APPLY_PAYMENT("apply_payment","待申请付款单界面提示"),
    PENDING_PAYMENT("pending_payment","待审核付款界面提示"),
    PRINT_FLOAT_LAYER("print_float_layer","待打印交易单付款申请浮层提示"),
    PRINT_TRANS_INTERFACE("print_trans_interface","待打印交易单付款申请界面提示"),

    BASIC_SETTINGS("BasicSettings","基础设置页面"),
    PAYMEN_SORT("paymentSort","开单页面支付方式设置"),
    PAYMEN_TYPE("paymentType","支付管理付款类型配置设置");


    private String code;
    private String name;

    RiskControlType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }






}
