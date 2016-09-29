package com.prcsteel.platform.common.enums;
/**
 * 
 * @author zhoukun
 */
public enum OpType {
	None("无操作",OpLevel.Safe),
	CreateConsignOrder("新开交易单",OpLevel.Safe),
	UploadContract("上传合同",OpLevel.Safe),
	AddConsignOrderAttachment("添加合同附件",OpLevel.Safe),
	DeleteConsignOrderAttachment("删除合同附件",OpLevel.Damning),
	ChangeOrderStatus("修改订单状态",OpLevel.Dangerous),
	AddOrderAuditTrail("添加流程审核记录",OpLevel.Safe),

	//客户管理
	SaveAccount("保存客户信息",OpLevel.Safe),
	LockandunlockAccount("锁定或解锁客户",OpLevel.Safe),
	SaveContractTemplate("保存合同模板",OpLevel.Safe),
	UpdateAccount("划转客户",OpLevel.Safe),
	UpdateContact("划转联系人",OpLevel.Safe),
	ContractTemplateEdit("修改合同模板",OpLevel.Safe),
	AddContact("添加客户联系人",OpLevel.Safe),
	ContactEdit("编辑客户联系人",OpLevel.Safe),
	LockAndUnLock("锁定或解锁客户联系人",OpLevel.Safe),
	SaveApply("申请提现",OpLevel.Safe),
	
	//二次结算应收/应付
	SecondDuction("二次结算应收抵扣",OpLevel.Safe),
	SecondWithdrawal("二次结算应付提现",OpLevel.Safe),
	
	//提放货
	SavePickup("保存提货单",OpLevel.Safe),
	DeletePickup("逻辑删除提货单",OpLevel.Warning),
	SaveDelivery("变更放货单",OpLevel.Safe),
	PrintPickup("打印提货单",OpLevel.Safe),
	PrintDelivery("打印放货单",OpLevel.Safe),
	
	//进项票
	InAuthentication("通过、不通过认证或打回发票",OpLevel.Safe),
	CheckInExpress("登记快递单号",OpLevel.Safe),
	SaveInvoice("确认进项发票",OpLevel.Safe),
	OutAuthentication("取消已认证发票",OpLevel.Safe),
	OutCancel("取消已作废发票",OpLevel.Safe),
	DeleteInvoice("删除已作废发票",OpLevel.Dangerous),
	
	//销项票
	UpdateApprove("审核开票资料通过",OpLevel.Safe),
	UpdateDecline("审核开票资料不通过",OpLevel.Safe),
	SaveOutInvoice("申请开票",OpLevel.Safe),
	OutApplyId("审核开票申请通过或不通过",OpLevel.Safe),
	GenerateCheckList("生成开票清单",OpLevel.Safe),
	SubmitInvoice("录入发票",OpLevel.Safe),
	AddCourier("登记快递号",OpLevel.Safe),
	ConfirmInvoice("确认发票",OpLevel.Safe),
	ApproveBuyerInvoiceData("买家开票资料审核通过",OpLevel.Safe),

	//系统管理
	SavePermissions("保存角色权限",OpLevel.Warning),
	SaveToUser("保存用户权限",OpLevel.Warning),
	AddOrganization("添加服务中心",OpLevel.Safe),
	UpdateOrganization("编辑服务中心",OpLevel.Safe),
	AddUser("添加用户",OpLevel.Safe),
	DisableUser("锁定用户",OpLevel.Warning),
	ActivateUser("解锁用户",OpLevel.Warning),
	UpdateUser("编辑用户",OpLevel.Safe),

	//订单管理
	AuditOrder("审核订单",OpLevel.Dangerous),
	RelateOrder("关联订单",OpLevel.Dangerous),
	InitialApplyPay("初次申请付款",OpLevel.Warning),
	SettleApplyPay("二次结算申请付款",OpLevel.Warning),
	WithdrawApplyPay("提现申请付款",OpLevel.Warning),
	AuditInitialPay("审核初次付款",OpLevel.Dangerous),
	AuditSettlePay("审核二次结算付款",OpLevel.Dangerous),
	AuditWithdrawPay("审核提现付款",OpLevel.Dangerous),
	ConfirmInitialPay("确认初次付款",OpLevel.Dangerous),
	ConfirmSettlePay("确认二次结算付款",OpLevel.Dangerous),
	ConfirmWithdrawPay("确认提现付款",OpLevel.Dangerous),
	CloseOrder("申请关闭订单",OpLevel.Warning),
	AuditCloseOrder("审核关闭订单",OpLevel.Dangerous),
	FightbackOrder("打回订单",OpLevel.Dangerous),

	//到账查询
	Charge("充值",OpLevel.Safe),
	Refund("退款",OpLevel.Safe),


	//代运营协议
	AuditConsignAgreement("审核卖家代运营协议",OpLevel.Dangerous),

	//年度采购协议
	AuditAnnualPurchaseAgreement("审核年度采购协议",OpLevel.Dangerous);

	String description;
	OpLevel level;
	OpType(String desc,OpLevel level){
		this.description = desc;
		this.level = level;
	}
	public String getDescription() {
		return description;
	}
	public OpLevel getLevel() {
		return level;
	}
}
