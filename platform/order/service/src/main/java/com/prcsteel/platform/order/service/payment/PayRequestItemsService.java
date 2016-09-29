package com.prcsteel.platform.order.service.payment;

import com.prcsteel.platform.order.model.dto.PayRequestItemsInfoDto;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lcw on 2015/7/27.
 */
public interface PayRequestItemsService {
    int deleteByPrimaryKey(Long id);

    int insert(PayRequestItems record);

    int insertSelective(PayRequestItems record);

    PayRequestItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRequestItems record);

    int updateByPrimaryKey(PayRequestItems record);

    /**
     * 根据付款ID查找付款详情记录
     *
     * @param requestId
     * @return
     */
    List<PayRequestItems> selectByRequestId(Long requestId);

    List<PayRequestItemsInfoDto> selectPayInfoByRequestId(Long requestId);
    //add by wangxianjun 查询变更流程付款信息
    List<PayRequestItemsInfoDto> selectChangePayInfoByRequestId(Long requestId);
    
    /**
     * 根据付款ID查找付款详情记录
     *@author lixiang
     * @param requestId
     * @return
     */
    List<PayRequestItemsInfoDto> selectAllPayInfoByRequestId(List<Long> requestIds);
}
