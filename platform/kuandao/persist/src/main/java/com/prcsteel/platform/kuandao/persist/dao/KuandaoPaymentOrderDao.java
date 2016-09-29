package com.prcsteel.platform.kuandao.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.kuandao.model.dto.ConsignOrderDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.PaymentOrderSynchLogDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;
import com.prcsteel.platform.kuandao.model.model.PaymentOrderSynchLog;

public interface KuandaoPaymentOrderDao {
	/**
	 * 查询全部支付单
	 * @param paymentOrderDto
	 * @return
	 */
	int totalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto);
	/**
	 *分页 查询支付单
	 * @param paymentOrderDto
	 * @return
	 */
	List<KuandaoPaymentOrderDto> queryPaymentOrders(Map<String, Object> param);
	/**
	 * 查询异常订单总数量
	 * @param paymentOrderDto
	 * @return
	 */
	int totalAbnormalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto);
	/**
	 * 分页查询异常订单列表
	 * @param paymentOrderDto
	 * @return
	 */
	List<KuandaoPaymentOrderDto> queryAbnormalPaymentOrders(Map<String, Object> param);
	/**
	 * 查询订单日志总数量
	 * @param paymentOrderDto
	 * @return
	 */
	int totalSynchronizeLog(PaymentOrderSynchLogDto synLogDto);
	/**
	 * 分页查询订单日志列表
	 * @param paymentOrderDto
	 * @return
	 */
	List<PaymentOrderSynchLogDto> querySynchronizeLog(Map<String, Object> param);
	/**
	 * 根据订单号id查询订单信息
	 * @param id
	 * @return KuandaoPaymentOrderDto
	 */
	KuandaoPaymentOrderDto selectByPrimaryKey(int id);
	/**
	 * 新增支付单日志 
	 * @param synchronizeLog
	 * @return
	 */
	void insertSynchronizeLog(PaymentOrderSynchLog synchronizeLog);
	/**
	 * 查询去重后的交易单重商品名到busnni_consign_order_items中
	 * @param code交易单号
	 * @return List<String>
	 */
	List<String> queryNsorts(String code);
	
	/**落地支付单
	 * @param paymentOrder
	 * @return
	 * */
	int insertPaymentOrder(KuandaoPaymentOrder paymentOrder);
	
	/**
	 * 更新支付单
	 * @param paymentOrder
	 * @return 
	 */
	int update(KuandaoPaymentOrder paymentOrder);
	
	/**根据条件查找支付单
	 * @param paymentOrder
	 * @return 
	 * */
	List<KuandaoPaymentOrder> queryPaymentOrderByCondition(KuandaoPaymentOrderDto dto);
	
	/**
	 * 查询交易单信息，供模拟接口使用
	 * @param consignOrderDto
	 * @return
	 */
	List<ConsignOrderDto> queryFinishConsignOrderByCondition(ConsignOrderDto consignOrderDto);
	
	User queryContractByOrderId(Long consignOrderId);
	
}
