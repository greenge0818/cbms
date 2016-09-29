package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.ConsignOrderSettleDto;

/**
 * Created by caochao on 2015/7/22.
 */
public interface ConsignOrderSettleDao {

    List<ConsignOrderSettleDto> getBuyerSettleInfo(Long id);

    List<ConsignOrderSettleDto> getSellerSettleInfo(Long id);

}
