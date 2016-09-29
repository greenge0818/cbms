package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.PayRequestItemsInfoDto;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PayRequestItemsDao {
    int deleteByPrimaryKey(Long id);

    int insert(PayRequestItems record);

    int insertSelective(PayRequestItems record);

    PayRequestItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRequestItems record);

    int updateByPrimaryKey(PayRequestItems record);

    List<PayRequestItems> queryByOrderId(Long orderId);
    /**
     * 根据付款ID查找付款详情记录
     *
     * @param requestId
     * @return
     */
    List<PayRequestItems> selectByRequestId(Long requestId);

    List<PayRequestItemsInfoDto> selectPayInfoByRequestId(Long requestId);
    
    /**
     * 修改付款申请支付的银行、银行出账时间
     * @param itemsIds
     * @param paymentBank
     * @param bankAccountTime 银行出账时间（updated by chengui）
     * @return
     */
    int updatePaymentBank (@Param("itemsIds") List<Long> itemsIds, @Param("paymentBank") String paymentBank,
                           @Param("bankAccountTime") Date bankAccountTime);

    //add by wangxianjun 查询变更流程付款信息
    List<PayRequestItemsInfoDto> selectChangePayInfoByRequestId(Long requestId);
    
    /**
     * 根据付款ID查找付款详情记录
     *@author lixiang
     * @param requestId
     * @return
     */
    List<PayRequestItemsInfoDto> selectAllPayInfoByRequestId(@Param("requestIds") List<Long> requestIds);
}