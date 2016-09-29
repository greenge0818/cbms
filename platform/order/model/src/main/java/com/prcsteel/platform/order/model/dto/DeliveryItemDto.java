package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.DeliveryItems;

/**
 * Created by Rabbit Mao on 2015/7/29.
 */
public class DeliveryItemDto {
    DeliveryItems dItems;
    ConsignOrderItems oItems;

    public DeliveryItems getdItems() {
        return dItems;
    }

    public void setdItems(DeliveryItems dItems) {
        this.dItems = dItems;
    }

    public ConsignOrderItems getoItems() {
        return oItems;
    }

    public void setoItems(ConsignOrderItems oItems) {
        this.oItems = oItems;
    }
}
