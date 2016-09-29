package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.model.PaymentOrderSynchLog;

public interface KuandaoPaymentOrderService {

	
	/**
	 * 支付单提交
	 * @param kuandaoPaymentOrderDto
	 * @param paymentOrderSynchLog
	 * @return
	 */
	int orderSubmit(KuandaoPaymentOrderDto kuandaoPaymentOrderDto,PaymentOrderSynchLog paymentOrderSynchLog);
	
	/**
	 * 记录日志
	 * @param paymentOrderSynchLog
	 */
	void insertSynchronizeLog(PaymentOrderSynchLog paymentOrderSynchLog);
	
	/**
	 * 未匹配汇入流水处理
	 * 	1.查询未匹配汇入流水
	 *  2.未匹配汇入流水落地
	 *  3.判断付款方是否开户
	 *  4.判断是否受限银行
	 *  5.判断是否生成过支付单
	 *  6.生成支付单
	 * @param userName
	 */
	void processMclsUnmatch(String userName);
	
	/**
	 * 已匹配汇入流水查询
	 * 1.查询已匹配汇入流水
	 * 2.已匹配汇入流水落地
	 * @param userName
	 */
	void processMclsMatch(String userName);
	
	/**
	 * 半小时未生成支付单汇入流水查询
	 * 1.查询半小时未生成支付单汇入流水
	 * 2.发邮件短信给运营人员
	 */
	void processNonPaymentOrderDeposit();

	/**
	 * 已退款汇入流水查询
	 * 1.查询已退款汇入流水
	 * 2.已退款汇入流水落地
	 * @param userName
	 */
	void processMclsRefund(String userName);

	/**
	 * 到货确认
	 * <ol>1.发送担保支付指令</ol>
	 * <ol>2.成功后更新支付单和汇入流水状态为已完成</ol>
	 * <ol>3.资金账户充值</ol>
	 * @param kuandaoPaymentOrderDto
	 * @return
	 */
	Integer finishOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto);

	/**
	 * 汇入流水与支付单匹配通知接口
	 * 1，更新支付单状态
	 * 2，更新汇入流水状态
	 * 3，执行货确认
	 * @param result
	 * @return
	 */
	String matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode,String transDate, String transAmt);

	/**
	 * 资金账户充值通知
	 * @param chargResultNotifyDto
	 * @return
	 */
	ChargResultResponse chargResultNotify(ChargResultNotifyDto chargResultNotifyDto);

	/**
	 * 资金账户充值
	 * @param id 支付单Id
	 * @return
	 */
	Integer charg(Integer id);

	/**
	 * 根据银行id判断是否受限行
	 * @param bankId
	 * @return
	 */
	boolean isLimitBank(String bankId);
	
}
