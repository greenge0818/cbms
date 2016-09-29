package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.UpdateChangeOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ConsignOrderItemsChangeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderItemsChange record);

    int insertSelective(ConsignOrderItemsChange record);

    ConsignOrderItemsChange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderItemsChange record);

    int updateByPrimaryKey(ConsignOrderItemsChange record);

    List<ConsignOrderItemsChange> queryOrderItemsByChangeOrderId(Integer changeOrderId);

    int batchUpdateAcceptDraft(List<ConsignOrderItemsChange> list);

    int batchUpdateStatus(List<ConsignOrderItemsChange> list);
    
    /**
     * 根据变更订单id查询卖家信息
     * @param orderChangeId 变更订单id
     * @return
     */
    List<ConsignOrderSellerInfoDto> getchangeSellerInfo(Integer orderChangeId);
    
    /**
     * 通过订单id查询付款总金额
     * @param orderId
     * @return
     */
    List<ConsignOrderSellerInfoDto> getPayment(Long orderId);
    

    //通过变更订单ID查询明细
    List<ConsignOrderItems> selectByChangeOrderId(Integer changeOrderId);

    /**
     * 通过变更订单ID修改状态
     * @param updateDto 修改对象
     * @return
     */
    int updateStatusByChangeOrderId(UpdateChangeOrderDto updateDto);
   //通过订单ID查询合同变更后合同金额
    BigDecimal countChangeContractAmountById(@Param("changeOrderId")Long changeOrderId);
    //通过订单ID查询合同变更后使用的银票号
    List<String> selectChangeAcceptDraftCodeById(Long orderId);

    //通过订单ID查询原订单详情数据
    List<ConsignOrderItemsChange> selectOriginalItemsByOrderId(Long orderId);

    List<ConsignOrderItemsChange> queryOrderItemsExceptStatusByChangeOrderId(@Param("changeOrderId")Integer changeOrderId, @Param("status")String status);

    List<ConsignOrderItemsChange> selectOrderItemsChangeByQueryDto(QueryChangeOrderDto query);
}
