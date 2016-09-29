package com.prcsteel.platform.common.model;

import java.math.BigDecimal;

/**
 * Created by Rolyer on 2016/2/25.
 */
public class OrderItem {
    /**
     * 大类名称
     */
    private String groupName;
    /**
     * 大类UUID
     */
    private String groupUuid;
    /**
     * 品名
     */
    private String categoryName;
    /**
     * 品名UUID
     */
    private String categoryUuid;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 支付方式
     */
    private String payType;

    public OrderItem() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "groupName='" + groupName + '\'' +
            ", groupUuid='" + groupUuid + '\'' +
            ", categoryName='" + categoryName + '\'' +
            ", categoryUuid='" + categoryUuid + '\'' +
            ", weight=" + weight +
            ", payType='" + payType + '\'' +
            '}';
    }
}
