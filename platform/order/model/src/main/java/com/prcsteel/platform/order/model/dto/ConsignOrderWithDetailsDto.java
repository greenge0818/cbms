package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;

/**
 * @author caochao
 *         2015-08-19
 */
public class ConsignOrderWithDetailsDto extends ConsignOrder {
    List<ConsignOrderItemsInfoDto> consignOrderItems;

    public List<ConsignOrderItemsInfoDto> getConsignOrderItems() {
        return consignOrderItems;
    }

    public void setConsignOrderItems(List<ConsignOrderItemsInfoDto> consignOrderItems) {
        this.consignOrderItems = consignOrderItems;
    }
}
