package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrderItems;

/**
 * Created by Rabbit Mao on 2015/7/23.
 */
public class ItemDto {
    Long sellerId;
    String sellerName;
    List<ConsignOrderItems> itemsList;
    int status; //0: 全部录入完; 1: 还没有全部录入

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<ConsignOrderItems> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ConsignOrderItems> itemsList) {
        this.itemsList = itemsList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
