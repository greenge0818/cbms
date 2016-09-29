package com.prcsteel.platform.order.service.order;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kongbinheng on 2015/7/18.
 */
public interface OrderStatusService {

    /**
     * 新增订单流水表
     *
     * @param orderAuditTrail
     * @return
     */
    public int insertOrderAuditTrail(OrderAuditTrail orderAuditTrail);
    public int insertOrderAuditTrail(Long orderId,String statusType,User user, String status);
    /**
     * 更新订单状态
     *
     * @param paramMap
     * @return
     */
    public boolean updateOrderStatus(Map<String, Object> paramMap, OrderAuditTrail orderAuditTrail);

    /**
     * 提现/付款
     * @param accountId        客户id
     * @param consignOrderCode 关联单号
     * @param serialNumber     流水号
     * @param balanceWithdraw  提现的钱
     * @param freeze    二次结算的钱
     * @param applyerId        申请人id
     * @param applyerName      申请人姓名
     * @param applyerDate      申请时间
     * @param flag             见Type枚举
     * @return
     */
    public boolean updatePayment(Long accountId, String consignOrderCode, String serialNumber,
                                              Double balanceWithdraw, Double freeze,
                                              Long applyerId, String applyerName, Date applyerDate, String flag);

    public List<ConsignOrderStatusDto> getAuditDetailById(Long orderId);

    public String queryCloseReasonByOrderId(Long orderId);
}
