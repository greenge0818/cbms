package com.prcsteel.platform.order.model.model;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务中心提成统计报表
 * wangxiao
 * Created by prcsteel on 2016/1/6.
 */
public class CommissionExcel{
    private String  month;//所属月份

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    private Date secondSettlementTime;//二次结算时间

    private Date invoiceTime;//开票时间

    public Date getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(Date invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    private String  buyerOrgName;//买家服务中心

    private Long  buyerOrgId;//买家服务中心

    public Long getBuyerOrgId() {
        return buyerOrgId;
    }

    public void setBuyerOrgId(Long buyerOrgId) {
        this.buyerOrgId = buyerOrgId;
    }

    public BigDecimal getCurrOrgBuyerTempWeight() {
        return currOrgBuyerTempWeight;
    }

    public void setCurrOrgBuyerTempWeight(BigDecimal currOrgBuyerTempWeight) {
        this.currOrgBuyerTempWeight = currOrgBuyerTempWeight;
    }

    public BigDecimal getCurrOrgBuyerConsignWeight() {
        return currOrgBuyerConsignWeight;
    }

    public void setCurrOrgBuyerConsignWeight(BigDecimal currOrgBuyerConsignWeight) {
        this.currOrgBuyerConsignWeight = currOrgBuyerConsignWeight;
    }

    public BigDecimal getOtherOrgBuyerTempWeight() {
        return otherOrgBuyerTempWeight;
    }

    public void setOtherOrgBuyerTempWeight(BigDecimal otherOrgBuyerTempWeight) {
        this.otherOrgBuyerTempWeight = otherOrgBuyerTempWeight;
    }

    public BigDecimal getOtherOrgBuyerConsignWeight() {
        return otherOrgBuyerConsignWeight;
    }

    public void setOtherOrgBuyerConsignWeight(BigDecimal otherOrgBuyerConsignWeight) {
        this.otherOrgBuyerConsignWeight = otherOrgBuyerConsignWeight;
    }

    private String buyerJobNumber;//买家交易员工号

    private String buyerOwnerName;//买家交易员,

    private Long buyerOwnerId;//买家交易员ID,

    public Long getBuyerOwnerId() {
        return buyerOwnerId;
    }

    public void setBuyerOwnerId(Long buyerOwnerId) {
        this.buyerOwnerId = buyerOwnerId;
    }

    private String sellerOrgName;//卖家服务中心

    private String sellerJobNumber; //卖家交易员工号;

    private String sellerOwnerName;//卖家交易员,

    private String buyerAccountName;//买家客户

    private String sellerAccountName;//卖家客户

    private String cbname; //大类

    private  String nsortName;// 品名

    private String material;//材质

    private String spec;// 规格

    private BigDecimal actualPickWeightServer;//实提重量

    private String  orderCode;// 交易单号

    private  Date orderCreated;// 开单时间

    private BigDecimal sellerCount;

    private BigDecimal buyerCount;

    private BigDecimal lastWeight;//当月之前买家历史采购量

    private String sellConsign;// 卖家类型

    private String buyConsign;// 买家类型

    private BigDecimal profit;// 利润

    private BigDecimal diffPrice;//销项票开票金额与进项票确认金额之差

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(BigDecimal diffPrice) {
        this.diffPrice = diffPrice;
    }

    public BigDecimal getActualPickWeightServer() {
        return actualPickWeightServer;
    }

    public void setActualPickWeightServer(BigDecimal actualPickWeightServer) {
        this.actualPickWeightServer = actualPickWeightServer;
    }

    public BigDecimal getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(BigDecimal sellerCount) {
        this.sellerCount = sellerCount;
    }

    public BigDecimal getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(BigDecimal buyerCount) {
        this.buyerCount = buyerCount;
    }

    public BigDecimal getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(BigDecimal lastWeight) {
        this.lastWeight = lastWeight;
    }

    public String getSellConsign() {
        return sellConsign;
    }

    public void setSellConsign(String sellConsign) {
        this.sellConsign = sellConsign;
    }

    public String getBuyConsign() {
        return buyConsign;
    }

    public void setBuyConsign(String buyConsign) {
        this.buyConsign = buyConsign;
    }

    public BigDecimal getRewardRole() {
        return rewardRole;
    }

    public void setRewardRole(BigDecimal rewardRole) {
        this.rewardRole = rewardRole;
    }

    public BigDecimal getBuyerRewardRole() {
        return buyerRewardRole;
    }

    public void setBuyerRewardRole(BigDecimal buyerRewardRole) {
        this.buyerRewardRole = buyerRewardRole;
    }

    public BigDecimal getSellerRewardRole() {
        return sellerRewardRole;
    }

    public void setSellerRewardRole(BigDecimal sellerRewardRole) {
        this.sellerRewardRole = sellerRewardRole;
    }

    public BigDecimal getSellerDeduct() {
        return sellerDeduct;
    }

    public void setSellerDeduct(BigDecimal sellerDeduct) {
        this.sellerDeduct = sellerDeduct;
    }

    public BigDecimal getBuyerDeduct() {
        return buyerDeduct;
    }

    public void setBuyerDeduct(BigDecimal buyerDeduct) {
        this.buyerDeduct = buyerDeduct;
    }

    private BigDecimal rewardRole;//品类系数

    private BigDecimal buyerRewardRole;//买家提成系数（比例）

    private BigDecimal sellerRewardRole;//卖家提成系数 （比例）

    private BigDecimal sellerDeduct;//卖家交易员提成金额

    private BigDecimal buyerDeduct;//买家交易员提成金额

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private BigDecimal currOrgBuyerTempWeight;

    private BigDecimal currOrgBuyerConsignWeight;

    private BigDecimal otherOrgBuyerTempWeight;

    private BigDecimal otherOrgBuyerConsignWeight;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    private String groupBy;


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getSecondSettlementTime() {
        return secondSettlementTime;
    }

    public void setSecondSettlementTime(Date secondSettlementTime) {
        this.secondSettlementTime = secondSettlementTime;
    }

    public String getBuyerOrgName() {
        return buyerOrgName;
    }

    public void setBuyerOrgName(String buyerOrgName) {
        this.buyerOrgName = buyerOrgName;
    }

    public String getBuyerJobNumber() {
        return buyerJobNumber;
    }

    public void setBuyerJobNumber(String buyerJobNumber) {
        this.buyerJobNumber = buyerJobNumber;
    }

    public String getBuyerOwnerName() {
        return buyerOwnerName;
    }

    public void setBuyerOwnerName(String buyerOwnerName) {
        this.buyerOwnerName = buyerOwnerName;
    }

    public String getSellerOrgName() {
        return sellerOrgName;
    }

    public void setSellerOrgName(String sellerOrgName) {
        this.sellerOrgName = sellerOrgName;
    }

    public String getSellerJobNumber() {
        return sellerJobNumber;
    }

    public void setSellerJobNumber(String sellerJobNumber) {
        this.sellerJobNumber = sellerJobNumber;
    }

    public String getSellerOwnerName() {
        return sellerOwnerName;
    }

    public void setSellerOwnerName(String sellerOwnerName) {
        this.sellerOwnerName = sellerOwnerName;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public String getCbname() {
        return cbname;
    }

    public void setCbname(String cbname) {
        this.cbname = cbname;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderCreated() {
        return orderCreated;
    }

    public void setOrderCreated(Date orderCreated) {
        this.orderCreated = orderCreated;
    }
}
