package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.OrderItemsInvoiceInInfoDto;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;

import org.apache.ibatis.annotations.Param;

public interface InvoiceInDetailOrderitemDao {

    int batchInsert(List<InvoiceInDetailOrderItem> records);

    List<InvoiceInDetailOrderItem> selectByDetailIds(List<Long> detailIds);
    
    /**
     * 将订单详情与发票详情的绑定关系设置为未激活
     * @param invoiceDetailId
     */
    int setInactiveByDetailId(Long invoiceDetailId);

    /**
     * 根据订单号查询被关联进项票的明细数
     * @param orderId
     * @return
     */
    int countByOrderId(@Param("orderId") Long orderId);

	List<Long> queryOrderItemIds(AllowanceOrderQuery allowanceOrderQuery);

    /**
     * 根据订单明细ID查询关联进项票发票详情
     * author wangxianjun
     * @param orderItemsId
     * @return 详情
     */
    List<OrderItemsInvoiceInInfoDto> queryOrderItemsInInvoice(Long orderItemsId);
}