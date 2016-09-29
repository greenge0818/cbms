package com.prcsteel.platform.account.persist.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.BankDto;
import com.prcsteel.platform.account.model.model.AccountBank;

/**
 * Created by lcw on 2015/7/14.
 */
public interface AccountBankDao {
	int deleteByPrimaryKey(Long id);

	int insert(AccountBank record);

	int insertSelective(AccountBank record);

	AccountBank selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AccountBank record);

	int updateByPrimaryKey(AccountBank record);

	/**
	 * 查询银行信息
	 *
	 * @param paramMap
	 * @return
	 * */
	List<AccountBank> query(Map<String, Object> paramMap);

	/**
	 * 获取已通过审核且没删除的银行信息，如果有设置默认银行，则默认银行排在第一位
	 * @param accountId
	 * @return
	 */
	List<AccountBank> queryAllEnabledBankByAccountId(@Param("accountId") Long accountId);

	/**
	 * 查询银行信息表的银行账号是否存在
	 * 
	 * @param bankAccountCode
	 * @return
	 */
	int totalBankAccountCode(@Param("bankAccountCode") String bankAccountCode);

	// 根据卡号查
	AccountBank selectByAccountCode(String bankAccountCode);

	/**
	 * 修改客户默认银行
	 * 
	 * @param bankDto
	 * @return
	 */
	public int updateBankById(BankDto bankDto);

	public int updateByAccountId(BankDto bankDto);
	
	/**
	 * 批量更新银行信息
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<AccountBank> list);
	
	public int updateBankStatusByAccountId(AccountBank bank);
}
