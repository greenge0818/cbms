package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.ConsignOrderDto;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.MatchNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.model.KuandaoPaymentOrder;

public interface KuanDaoProxyService {
	
	/**
	 * 根据客户id获取客户信息
	 * @param acctId
	 * @return
	 */
	CustAccountDto queryCustAccountByAcctId(Long acctId);
	
	/**
	 * 根据客户id获取已完成订单信息
	 * @param acctId
	 * @return
	 */
	List<ConsignOrderDto> queryFinishConsignOrderByAcctId(Long acctId);
	
	/**
	 * 获取汇入流水信息
	 * @param status
	 * @return
	 */
	SPDBResponseResult queryMclsByStatus(String status);
	
	/**
	 * 日终对账单查询模拟接口
	 * @return
	 */
	SPDBResponseResult queryDailyBill();

	/**
	 * 到货确认
	 * <ol>1.发送担保支付指令</ol>
	 * <ol>2.成功后更新支付单和汇入流水状态为已完成</ol>
	 * <ol>3.资金账户充值</ol>
	 * @param kuandaoPaymentOrderDto
	 * @return
	 */
	Integer finishOrder(KuandaoPaymentOrder kuandaoPaymentOrder);

	/**
	 * 汇入流水与支付单匹配通知接口
	 * <ol>1，更新支付单状态</ol>
	 * <ol>2，更新汇入流水状态</ol>
	 * @param in
	 * @return
	 */
	MatchNotifyTransaction matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode, String settDate, String tranAmt);

	/**
	 * 退款模拟接口
	 * @param kuandaoDepositJournalDto
	 * @return
	 */
	SPDBResponseResult refund(KuandaoDepositJournalDto kuandaoDepositJournalDto);
	
	/**
	 * 会员绑定、解绑通知模拟接口
	 * @param spdbRequstParam
	 * @param membercode
	 * @param operType
	 * @return
	 */
	SPDBResponseResult boundNotify(SPDBNotifyRequstParam spdbRequstParam, String membercode, String operType);

	/**
	 * 资金账户充值
	 * <ol>根据支付单中的会员号查询客户信息</ol>
	 * <ol>发送MQ调用账户体系充值</ol>
	 * @param kuandaoPaymentOrder
	 */
	Boolean charg(KuandaoPaymentOrder kuandaoPaymentOrder);
}
