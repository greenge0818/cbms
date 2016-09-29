package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by caochao on 2015/8/21.
 */
public class ConsignOrderItemsInfoDto extends ConsignOrderItems {
	private String consignType; //add by rabbit for issue 5233
    private String sellerTraderName;
    private Long invoiceInId;
    private Long invoiceInDetailId;
    private BigDecimal noTaxAmount;
    private BigDecimal taxAmount;
    private String orderCode;
	private String contractCodeAuto;
	private Long departmentCount;
	private Long resourceId;//资源ID

	//add by tuxianming : 20160722,用于利润表报
	private String supplierLabel;  	//卖家表示：白名单， 非白名单
	private String acceptDraft; 	//是不是银票支付： 无，银票支付， 银票号
	private String settlementTime;		//结算时间
	private String invoiceTime;			//开票时间
	//end by tuxianming : 20160722,用于利润表报

	private Integer financeOrder; //chengui 是否为融资订单，0否，1是
	
	private List<DepartmentDto> departments; //chengui 卖家的部门及其信息

	public String getConsignType() {
		return consignType;
	}

	public void setConsignType(String consignType) {
		this.consignType = consignType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(BigDecimal noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getAcceptDraft() {
		return acceptDraft;
	}

	public void setAcceptDraft(String acceptDraft) {
		this.acceptDraft = acceptDraft;
	}

	public String getSellerTraderName() {
        return sellerTraderName;
    }

    public void setSellerTraderName(String sellerTraderName) {
        this.sellerTraderName = sellerTraderName;
    }

	public Long getInvoiceInId() {
		return invoiceInId;
	}

	public void setInvoiceInId(Long invoiceInId) {
		this.invoiceInId = invoiceInId;
	}

	public Long getInvoiceInDetailId() {
		return invoiceInDetailId;
	}

	public void setInvoiceInDetailId(Long invoiceInDetailId) {
		this.invoiceInDetailId = invoiceInDetailId;
	}

	public String getContractCodeAuto() {
		return contractCodeAuto;
	}

	public void setContractCodeAuto(String contractCodeAuto) {
		this.contractCodeAuto = contractCodeAuto;
	}

	public Long getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(Long departmentCount) {
		this.departmentCount = departmentCount;
	}

	public List<DepartmentDto> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentDto> departments) {
		this.departments = departments;
	}

	public String getSupplierLabel() {
		return supplierLabel;
	}

	public void setSupplierLabel(String supplierLabel) {
		this.supplierLabel = supplierLabel;
	}

	public String getSettlementTime() {
		return settlementTime;
	}

	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}

	public String getInvoiceTime() {
		return invoiceTime;
	}

	public void setInvoiceTime(String invoiceTime) {
		this.invoiceTime = invoiceTime;
	}

	public Integer getFinanceOrder() {
		return financeOrder;
	}

	public void setFinanceOrder(Integer financeOrder) {
		this.financeOrder = financeOrder;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
}
