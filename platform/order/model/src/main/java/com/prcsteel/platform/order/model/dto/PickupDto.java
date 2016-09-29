package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.PickupBill;


/**
 * Created by Rabbit Mao on 2015/7/22.
 */
public class PickupDto {
    PickupBill pickupBills;
    List<ItemDto> items;

    public PickupBill getPickupBills() {
        return pickupBills;
    }

    public void setPickupBills(PickupBill pickupBills) {
        this.pickupBills = pickupBills;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }
}