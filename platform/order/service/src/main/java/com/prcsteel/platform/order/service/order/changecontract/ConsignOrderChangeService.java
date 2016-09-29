package com.prcsteel.platform.order.service.order.changecontract;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderListDto;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.SaveConsignOrderChangeDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 合同变更服务
 * Created by lichaowei on 2016/8/12.
 */
public interface ConsignOrderChangeService {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsignOrderChange record);

    int insertSelective(ConsignOrderChange record);

    ConsignOrderChange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsignOrderChange record);

    int updateByPrimaryKey(ConsignOrderChange record);

    //查询可变更交易单
    List<ChangeOrderListDto> selectByConditions(ChangeOrderDto query);
    int countByConditions(ChangeOrderDto query);

    List<ConsignOrderItemsChange> queryOrderItemsByChangeOrderId(Integer changeOrderId);

    /**
     * 保存订单变更数据
     * @param saveDto 变更数据
     */
    void saveOrderChange(SaveConsignOrderChangeDto saveDto);

    List<ChangeOrderListDto> selectChangeOrderByConditions(ChangeOrderDto query);

    int countChangeOrderByConditions(ChangeOrderDto query);

    /**
     * 关联变更订单
     * @param orderChangeId
     * @param operator
     * @return
     */
    ResultDto relateOrder(Integer orderChangeId, BigDecimal totalAmount, BigDecimal totalRelatedAmount, Boolean takeoutSecondBalance, User operator, String orderItemsChangeList, Boolean takeoutCreditBalance);

    ResultDto closeAssociateOrder(Integer orderChangeId, String cause, User operator);
    
    /**
     * 根据订单id查询卖家信息
     * @param orderId 订单id
     * @return
     */
    List<ConsignOrderSellerInfoDto> getchangeSellerInfo(Long orderId, Integer orderChangId);

    /**
     * 审核变更
     */
    void auditChange(Integer orderChangeId, Boolean auditStatus, String note, User user);
    
    /**
     * 变更合同申请付款
     * @author lixiang
     * @param orderChangeId 变更合同id
     * @param bankAccount 银行信息
     * @param applyMoney 申请付款金额
     * @param checkedMap
     * @param refundCredit
     * @param operator
     * @return
     */
    void applyPay(Integer orderChangeId, Map<Long, Long> bankAccount, Map<Long, Double> applyMoney, Map<Long, Boolean> checkedMap,
            Map<Long, Boolean> refundCredit, User operator, Boolean settleChecked, Boolean creditChecked);

    ConsignOrderItems getSellerTotalItems(List<ConsignOrderItems> orderItems);

    ConsignOrderItems getChangeSellerTotalItems(List<ConsignOrderItemsChange> orderItems);
    
    /**
     * 关闭本次变更
     * @author lixiang
     * @param orderChangeId
     * @param cause
     * @param user
     */
    void closedOrderChange(Integer orderChangeId, String cause, User user);

    //通过变更订单ID查询明细
    List<ConsignOrderItems> selectByChangeOrderId(Integer changeOrderId);

    /**
     * 修改合同变更表的状态，并且添加对应的变更记录
     */
    void updateChangeStatus(ConsignOrderChange consignOrderChange, String alterStatus, String note, User user);

    /**
     * 合同变更成功以后把对应的数据反写到订单表
     */
    void updateOrderByChange(ConsignOrder consignOrder,ConsignOrderChange orderChange, String alterStatus, List<ConsignOrderItemsChange> orderItemsChanges, User user);

    /**
     * 根据订单Id查询变更记录
     * @param query 查询参数
     * @return
     */
    ConsignOrderChange selectByConsignOrderChange(ConsignOrderChange query);

    //通过变更订单ID查询合同变更后合同金额
    BigDecimal countChangeContractAmountById(Long changeOrderId);

    //通过订单ID查询合同变更后使用的银票号
    List<String> selectChangeAcceptDraftCodeById(Long orderId);

    int querySuccessCountByOrderId(Long id);

    List<ConsignOrderItemsChange> selectOriginalItemsByOrderId(Long orderId);

    List<ConsignOrderItemsChange> queryOrderItemsExceptStatusByChangeOrderId(Integer changeOrderId, String status);

    List<ConsignOrderChange> selectByQueryDto(QueryChangeOrderDto query);

    List<ConsignOrderItemsChange> selectOrderItemsChangeByQueryDto(QueryChangeOrderDto query);

}
