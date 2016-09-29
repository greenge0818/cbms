package com.prcsteel.platform.order.service.order;

import java.util.List;

import com.prcsteel.platform.order.model.dto.ConsignOrderSettleDto;

/**
 * Created by caochao on 2015/7/22.
 */
public interface ConsignOrderSettleService {

    /**
     * 获取二次结算信息
     * @param type 0买家，1卖家
     * @param id 订单ID
     * @return
     */
    List<ConsignOrderSettleDto> getOrderSettleInfo(int type,Long id);
}
