package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.PaymentOrderDto;
import com.prcsteel.platform.order.model.model.BusiPayRequest;

public interface PaymentOrderDao {

	/**
	 * 通过订单ID查付款申请单及付款申请单明细
	 * 
	 * @param id
	 * @return
	 */
	public List<PaymentOrderDto> selectByOrderId(@Param("id") Long id);

	/**
	 * 修改打印次数
	 * 
	 * @param busiPayRequest
	 * @return
	 */
	public int updateById(BusiPayRequest busiPayRequest);

	/**
	 * 查申请单状态
	 * 
	 * @param requestId
	 *            申请单id
	 * @return
	 */
	public String findByRequestId(Long requestId);

	/**
	 * 查询买家
	 * @param requestId
	 * @return
	 */
	public List<Long> queryReceiverIdByRequestId(Long requestId);
	//通过变更合同id查询变更后的付款申请单
	List<PaymentOrderDto> selectChangeRequestByOrderId(@Param("id") Long id);
}