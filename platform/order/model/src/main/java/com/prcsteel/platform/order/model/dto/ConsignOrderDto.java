package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;

/**
 * Created by dengxiyan on 2015/7/18.
 */
public class ConsignOrderDto extends ConsignOrder {

    private String createdStr;

    private String sellerName;

    private BigDecimal pickupTotalQuantity;

    private BigDecimal pickupTotalWeight;

    private BigDecimal pickupTotalAmount;

    private String createdStartTime;

    private String createdEndTime;

    private String secondarySettlementStartTime; //二结开始时间 Rabbit add for issue5632

    private String secondarySettlementEndTime; //二结结束时间 Rabbit add for issue5632

    private String nsortName;

    private Long sellerId;

    private String orderBy;//排序 Green add for issue3855
    
    private String order;//排序Green
    
    private String payApprovedTime;//付款审核通过时间 Greeen add for issue3855
    
    private Integer showPrinted;//待打印付款申请单界面中勾选是否显示已打印的开关
    
    private String hasChanged;
    
    private String printTimes;
    
    private Boolean isPayedByAcceptDraft;  //是否为银票支付：  null：不作用条件,  0： 现金支付, 1： 银票支付

    private String orderStatus; //交易单状态: 多选条件，前台传参使用
    private List<ConsignOrderDto> orderStatusQuerys;  //交易单状态：主要用：两属性：payStatus,fillupStatus
    private String[] statusValues;		//交易单状态: 用于存放status属性

    private String secondaryStr;//二结时间

    private String alterStatus;//订单变更 状态


    public String getAlterStatus() {
        return alterStatus;
    }

    public void setAlterStatus(String alterStatus) {
        this.alterStatus = alterStatus;
    }

    public String getSecondaryStr() {
        return secondaryStr;
    }

    public void setSecondaryStr(String secondaryStr) {
        this.secondaryStr = secondaryStr;
    }

    public List<ConsignOrderDto> getOrderStatusQuerys() {
		return orderStatusQuerys;
	}

	public void setOrderStatusQuerys(List<ConsignOrderDto> orderStatusQuerys) {
		this.orderStatusQuerys = orderStatusQuerys;
	}
	
	public String[] getStatusValues() {
		return statusValues;
	}

	public void setStatusValues(String[] statusValues) {
		this.statusValues = statusValues;
	}

	public Boolean getIsPayedByAcceptDraft() {
		return isPayedByAcceptDraft;
	}

	public void setIsPayedByAcceptDraft(Boolean isPayedByAcceptDraft) {
		this.isPayedByAcceptDraft = isPayedByAcceptDraft;
	}

	public String getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(String printTimes) {
		this.printTimes = printTimes;
	}

	public String getCreatedStr() {
        return createdStr;
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getPickupTotalQuantity() {
        return pickupTotalQuantity;
    }

    public void setPickupTotalQuantity(BigDecimal pickupTotalQuantity) {
        this.pickupTotalQuantity = pickupTotalQuantity;
    }

    public BigDecimal getPickupTotalAmount() {
        return pickupTotalAmount;
    }

    public void setPickupTotalAmount(BigDecimal pickupTotalAmount) {
        this.pickupTotalAmount = pickupTotalAmount;
    }

    public String getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(String createdStartTime) {
        this.createdStartTime = createdStartTime;
    }

    public String getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(String createdEndTime) {
        this.createdEndTime = createdEndTime;
    }

    public String getSecondarySettlementStartTime() {
        return secondarySettlementStartTime;
    }

    public void setSecondarySettlementStartTime(String secondarySettlementStartTime) {
        this.secondarySettlementStartTime = secondarySettlementStartTime;
    }

    public String getSecondarySettlementEndTime() {
        return secondarySettlementEndTime;
    }

    public void setSecondarySettlementEndTime(String secondarySettlementEndTime) {
        this.secondarySettlementEndTime = secondarySettlementEndTime;
    }

    public BigDecimal getPickupTotalWeight() {
        return pickupTotalWeight;
    }

    public void setPickupTotalWeight(BigDecimal pickupTotalWeight) {
        this.pickupTotalWeight = pickupTotalWeight;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getPayApprovedTime() {
		return payApprovedTime;
	}

	public void setPayApprovedTime(String payApprovedTime) {
		this.payApprovedTime = payApprovedTime;
	}

	public Integer getShowPrinted() {
		return showPrinted;
	}

	public void setShowPrinted(Integer showPrinted) {
		this.showPrinted = showPrinted;
	}

    public String getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(String hasChanged) {
        this.hasChanged = hasChanged;
    }

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
