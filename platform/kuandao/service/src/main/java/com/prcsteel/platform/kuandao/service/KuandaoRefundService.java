package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoRefundDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoRefund;

public interface KuandaoRefundService {
	/**
	 * 汇入流水总查询
	 * @param
	 * @return
	 * */
	public int queryToTalDeposit(KuandaoDepositJournalDto dto);
	/**
	 * 根据条件查询汇入流水与相应的支付单
	 * @param
	 * @return
	 */
	public List<KuandaoDepositJournalDto> queryDepositAndOrder(KuandaoDepositJournalDto dto,Integer start,Integer length);
	/**
	 * 退款记录到本地
	 * @param 
	 * @return 
	 * */
	public int insertRefund(KuandaoRefund kuandaoRefund);
	/**
	 * 退款信息查询
	 * @param
	 * @return
	 * */
	public List<KuandaoRefundDto> queryRefundByCondition(KuandaoRefundDto dto,Integer start,Integer length);
	/**
	 * 退款信息总数查询
	 * @param
	 * @return
	 * */
	public int queryTotalRefund(KuandaoRefundDto dto);
	
	/**
	 * 退款申请
	 * 
	 * <ol>1，生成退款记录</ol>
	 * <ol>2，发送退款交易</ol>
	 * <ol>3，修改支付单及汇入流水状态</ol>
	 * @param kuandaoRefundDto
	 * @return
	 */
	public int applyRefund(KuandaoRefundDto kuandaoRefundDto);
	
}
