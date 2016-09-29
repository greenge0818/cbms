package com.prcsteel.platform.order.service.order;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrderContract;

/**
 * Created by caochao on 2015/8/6.
 */
public interface ConsignOrderContractService {
    ConsignOrderContract selectByPrimaryKey(Long id);

    /**
     * 根据订单号查询合同信息集
     * @param orderId
     * @return
     */
    List<ConsignOrderContract> queryByOrderId(Long orderId);

    /**
     * 根据变更订单号查询合同信息集
     * @param changeOrderId
     * @return
     */
    List<ConsignOrderContract>  queryByChangeOrderId(Integer changeOrderId);
}
