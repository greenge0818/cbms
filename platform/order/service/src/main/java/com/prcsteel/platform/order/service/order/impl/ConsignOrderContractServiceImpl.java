package com.prcsteel.platform.order.service.order.impl;

import java.util.List;

import com.prcsteel.platform.order.service.order.ConsignOrderContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.persist.dao.ConsignOrderContractDao;

/**
 * Created by caochao on 2015/8/6.
 */
@Service("consignOrderContractService")
public class ConsignOrderContractServiceImpl implements ConsignOrderContractService {
    @Autowired
    ConsignOrderContractDao consignOrderContractDao;

    @Override
   public ConsignOrderContract selectByPrimaryKey(Long id) {
        return consignOrderContractDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ConsignOrderContract> queryByOrderId(Long orderId) {
        return consignOrderContractDao.queryByOrderId(orderId);
    }
    /**
     * 根据变更订单号查询合同信息集
     * @param changeOrderId
     * @return
     */
    @Override
    public List<ConsignOrderContract>  queryByChangeOrderId(Integer changeOrderId){
        return consignOrderContractDao.queryByChangeOrderId(changeOrderId);
    }
}
