package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.PaymentOrderSynchLogDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;

/**
 * 支撑平台列表查询类
 * @Title KuandaoPaymentOrderSupportService.java
 * @Package com.prcsteel.platform.kuandao.service
 * @Description 查询支付单、异常支付单、操作日志，修改订单，重新提交
 * @author zjshan
 *
 * @date 2016年6月27日 上午8:18:07
 */
public interface KuandaoPaymentOrderSupportService {
	
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
	List<KuandaoPaymentOrderDto> queryPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto, Integer start, Integer length);
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
	List<KuandaoPaymentOrderDto> queryAbnormalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto, Integer start,Integer length);
	/**
	 * 查询支付单同步日志总数量
	 * @param synLogDto
	 * @return
	 */
	int totalSynchronizeLog(PaymentOrderSynchLogDto synLogDto);
	/**
	 * 分页查询支付单同步日志列表
	 * @param synLogDto
	 * @param start
	 * @param length
	 * @return
	 */
	List<PaymentOrderSynchLogDto> querySynchronizeLog(PaymentOrderSynchLogDto synLogDto, Integer start, Integer length);
	/**
	 * 修改订单
	 * @param dto
	 * @return 
	 * */
	int modifyOrder(KuandaoPaymentOrderDto dto);
	
	/**
	 * 重新提交客户信息
	 * @param userName 
	 * @param dto
	 * @return
	 * */
	int orderSubmitAgain(int id, String userName);

	/**
	 * 自助款道取号
	 * @param
	 * @return prcsteelAccountInfo取号信息
	 * */
	PrcsteelAccountInfo kuandaoTakeNumber();

	/**
	 * 退款管理生成支付单
	 * @param kuandaoRefundDto
	 * @return
	 */
	Integer generateOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto);
	
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
	 * <ol>1，更新支付单状态</ol>
	 * <ol>2，更新汇入流水状态</ol>
	 * <ol>3，执行货确认</ol>
	 * @param result
	 * @return
	 */
	String matchOrderNotify(SPDBNotifyRequstParam spdbRequstParam, String paymentOrderCode, String transDate, String transAmt);
	
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
	
	
}
