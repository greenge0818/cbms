package com.prcsteel.platform.order.service.order.changecontract;

import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChangedrecord;

import java.util.List;

/**
 * 合同变更记录服务
 * Created by lichaowei on 2016/8/18.
 */
public interface ConsignOrderItemsChangedrecordService {

    /**
     * 根据条件查询记录
     * @return 集合
     */
    List<ConsignOrderItemsChangedrecord> selectByChangedrecord(QueryChangeOrderDto query);
}
