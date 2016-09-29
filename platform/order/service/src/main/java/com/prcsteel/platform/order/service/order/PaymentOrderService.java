package com.prcsteel.platform.order.service.order;

import java.util.List;

import com.prcsteel.platform.order.model.dto.PaymentOrderDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by lixiang on 2015/7/20.
 */
public interface PaymentOrderService {

	/**
	 * 通过交易订单ID查询
	 * 
	 * @param id
	 * @return
	 */
	public List<PaymentOrderDto> findByOrder(Long id);

	/**
	 * 查询服务中心总经理
	 * 
	 * @param orgId
	 * @return
	 */
	public String getChangerName(Long orgId);

	/**
	 * 通过服务中心ID查询服务中心名
	 * 
	 * @param id
	 * @return
	 */
	public Organization findById(Long id);

	/**
	 * 修改打印次数
	 * 
	 * @param id
	 * @param printTimes
	 * @return
	 */
	public void updateForId(Long id, int printTimes, String ip, User operator);

	/**
	 * 查询付款申请单状态
	 * 
	 * @param requestId
	 *            付款申请单id
	 * @return
	 */
	public String findByRequestId(Long requestId);
	
	public Account selectByPrimaryKey (Long accountId);

	public List<Long> queryReceiverIdByRequestId(Long requestId);

	//通过订单id查询变更后的付款申请单
	List<PaymentOrderDto> selectChangeRequestByOrderId(Long id);
}
