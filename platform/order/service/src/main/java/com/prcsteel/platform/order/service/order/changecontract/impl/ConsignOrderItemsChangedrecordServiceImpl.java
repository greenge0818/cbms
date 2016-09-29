package com.prcsteel.platform.order.service.order.changecontract.impl;

import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChangedrecord;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsChangedrecordDao;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderItemsChangedrecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 变更记录查询
 * Created by lichaowei on 2016/8/18.
 */
@Service("consignOrderItemsChangedrecordService")
public class ConsignOrderItemsChangedrecordServiceImpl implements ConsignOrderItemsChangedrecordService {

    @Resource
    ConsignOrderItemsChangedrecordDao itemsChangedrecordDao;

    /**
     * 根据条件查询记录
     * @return 集合
     */
    @Override
    public List<ConsignOrderItemsChangedrecord> selectByChangedrecord(QueryChangeOrderDto query){
        return itemsChangedrecordDao.selectByChangedrecord(query);
    }
}
