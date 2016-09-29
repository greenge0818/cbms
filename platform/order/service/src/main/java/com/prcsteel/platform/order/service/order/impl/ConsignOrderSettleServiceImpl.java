package com.prcsteel.platform.order.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.ConsignOrderSettleDto;
import com.prcsteel.platform.order.persist.dao.ConsignOrderSettleDao;
import com.prcsteel.platform.order.service.order.ConsignOrderSettleService;

/**
 * Created by caochao on 2015/7/22.
 */
@Service("consignOrderSettleService")
public class ConsignOrderSettleServiceImpl implements ConsignOrderSettleService {

    @Autowired
    ConsignOrderSettleDao consignOrderSettleDao;

    public List<ConsignOrderSettleDto> getOrderSettleInfo(int type, Long id) {
        if(type==0)
            return consignOrderSettleDao.getBuyerSettleInfo(id);
        else
            return consignOrderSettleDao.getSellerSettleInfo(id);
    }
}
