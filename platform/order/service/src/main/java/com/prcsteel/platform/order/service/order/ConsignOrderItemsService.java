package com.prcsteel.platform.order.service.order;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.query.OrderItemsForInvoiceInQuery;
import com.prcsteel.platform.order.model.query.OrderItemsQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lcw on 2015/7/19.
 */
public interface ConsignOrderItemsService {
    int deleteByPrimaryKey(Long id);

    int insert(ConsignOrderItems record);

    int insertSelective(ConsignOrderItems record);

    ConsignOrderItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsignOrderItems record);

    int updateByPrimaryKey(ConsignOrderItems record);

    /**
     * 根据订单id获取这笔订单使用的银票号
     * @param orderId
     * @return
     */
    List<String> selectAcceptDraftCodeByOrderId(Long orderId);

    /**
     * 根据订单ID删除订单资源
     *
     * @param orderId 订单ID
     * @return
     */
    int deleteByOrderId(Long orderId);

    List<ConsignOrderItems> selectByOrderId(Long id);

    List<ItemDto> querySellersItemsByOrderId(Long orderId, String perm);

    List<ConsignOrderItems> getItemsIdForEdit(Long orderId, Long sellerId, String perm);

    Integer updateOrderItems(Long itemId, int quantity, BigDecimal weight, String perm, User user);

    Integer queryCountNotMatch(Long orderId);

    List<ConsignOrderSellerInfoDto> getSellerInfo(Long orderId) ;

    /**
     * 根据订单ID号，统计针对买家的合同金额（即销售金额：成交价*重量）
     * @return
     */
    BigDecimal countContractAmountByOrderId(Long orderId);
    
    List<OrderItemsInvoiceInDto> queryOrderItemsForInvoiceIn(OrderItemsForInvoiceInQuery query);
    
    int increaseInvoiceIn(IncreaseInvoiceInDto updInvoice);
    
    int restoreOrderitemInvoiceIn(Long invoiceDetailId);
    
    int reEffectiveInvoiceIn(Long invoiceDetailId);
    
    List<ConsignOrderItems> queryOrderItemsByDetailIds(List<Long> detailIds);
    
    List<ConsignOrderItems> queryByIds(List<Long> ids);
    
    /**
     * 销项票清单
     * @param orderItemsQuery
     * @return
     */
    List<OrderItemsDto> selectTicketList(OrderItemsQuery orderItemsQuery);
    
    /**
     * 销项票清单总记录数
     * @param orderItemsQuery
     * @return
     */
    Integer queryTicketTotal(OrderItemsQuery orderItemsQuery);
    
    /**
     * 未开票合计
     * @param orderItemsQuery
     * @return
     */
    OrderItemsDto totalTicketList(OrderItemsQuery orderItemsQuery);

	/**
     * 根据询价单编号查询
     * @param id 询价单编号
     * @return
     */
    List<ConsignOrderItemsInfoDto> buildOrderItemsByPurchaseOrderId(Long id, User user, List<Long> userIds);

    /**
     * 根据资源id查询出所有的开单信息
     * @param ids 资源ID
     * @return
     */
	List<ConsignOrderItemsInfoDto> buildOrderItemsByResourceId(String ids,List<Long> userIds);
    /**
     * 根据订单明细ID查询
     * author wangxianjun
     * @param id
     * @return
     */
    ConsignOrderItemsInvoiceDto queryOrderItemsById(Long id);

    /**
     * 根据订单明细ID查询关联进项票发票详情
     * author wangxianjun
     * @param orderItemsId
     * @return 详情
     */
    List<OrderItemsInvoiceInInfoDto> queryOrderItemsInInvoice(Long orderItemsId);


     List<Account> listBuyerBySellerId(Long sellerId);

    /**
     * tuxianming
     * 更新打印凭证
     * @param item
     */
	void udpateCertificateCode(ConsignOrderItems item);

	List<ConsignOrderItems> selectByOrderIdAndSellerId(Long long1, Long sellerId);

	AllowanceOrderItemsDto querySellerQuantity(Map<String, Object> paramMap);
	
	AllowanceOrderItemsDto querySellerOrgQuantity(Map<String, Object> paramMap);

    AllowanceOrderItemsDto queryAllSellerQuantity(Map<String, Object> paramMap);

    /**
     * cbms接口
     * 获取采购历史记录
     * @param phone
     * @return
     */
    List<PurchaseRecordDto> findPurchaseRecord(String phone);

    List<ConsignOrderItems> selectUnionAllByOrderId(Long orderId);

}
