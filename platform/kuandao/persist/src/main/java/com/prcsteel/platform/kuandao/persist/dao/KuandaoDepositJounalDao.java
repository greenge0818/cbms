package com.prcsteel.platform.kuandao.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoDepositJournal;

public interface KuandaoDepositJounalDao {
	/**
	 * 汇入流水落入本地
	 * @param
	 * @return
	 */
	int insertDepositJournal(KuandaoDepositJournal depositJournal);
	/**
	 * 根据条件查询汇入流水
	 * @param
	 * @return
	 */
	List<KuandaoDepositJournal> queryDepositByCondition(KuandaoDepositJournalDto dto);
	/**
	 * 更新汇入流水
	 * @param
	 * @return
	 */
	int update(KuandaoDepositJournal dj);
	
	/**
	 * 查询半小时未生成支付单的汇入流水
	 * 在过去的(1,0.5]小时这个 区间
	 * @return
	 */
	List<KuandaoDepositJournal> queryNonPaymentOrderDeposit();
	
	/**
	 * 查询模拟测试数据
	 * @param kuandaoDepositJournalDto
	 * @return
	 */
	List<KuandaoDepositJournal> queryTestDataByCondition(KuandaoDepositJournalDto kuandaoDepositJournalDto);
	
	/**
	 * 根据条件查询汇入流水与相应的支付单
	 * @param
	 * @return
	 */
	List<KuandaoDepositJournalDto> queryDepositAndOrder(Map<String,Object> param);
	/**
	 * 根据统计
	 * @param
	 * @return
	 */
	int queryToTalDeposit(KuandaoDepositJournalDto dto);
	
	/**
	 * 根据汇入流水ID查询汇入流水和订单信息
	 * @param queryDepositJournalDto
	 * @return
	 */
	List<KuandaoDepositJournalDto> queryDepositAndOrderByCondition(KuandaoDepositJournalDto queryDepositJournalDto);
	
	/**
	 * 获取数据库中还没有匹配的汇入流水
	 * @param kuandaoDepositJournalDto
	 * @return
	 */
	List<KuandaoDepositJournal> queryDepositWithoutOrder(KuandaoDepositJournalDto kuandaoDepositJournalDto);
	
	
}
