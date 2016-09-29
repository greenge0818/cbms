package com.prcsteel.platform.account.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.account.model.model.AccountBank;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lcw on 2015/7/14.
 */
public interface AccountBankService {
	int deleteByPrimaryKey(Long id);

	int insert(AccountBank record);

	int insertSelective(AccountBank record);

	AccountBank selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AccountBank record);

	int updateByPrimaryKey(AccountBank record);

	AccountBank selectByAccountCode(String bankAccountCode);

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
	List<AccountBank> queryAllEnabledBankByAccountId(Long accountId);

	/**
	 * 根据客户账号ID查询银行信息
	 *
	 * @param accountId
	 * @return
	 * */
	List<AccountBank> queryByAccountId(Long accountId);

	/**
	 * 修改客户默认银行
	 * 
	 * @param accountId
	 *            客户ID
	 * 
	 * @param bankId
	 *            银行id
	 * @return
	 */
	public boolean updateBankById(Long accountId, Long bankId)
			throws BusinessException;


	/**
	 * 设置默认银行并将其他银行设为非默认银行
	 * 默认银行只能有一个
	 * @param bankId 银行id
	 * @param user 操作用户
	 */
	void setDefaultBank(Long accountId,Long bankId,User user);

	/**
	 * 保存银行信息
	 * @param image
	 * @param bank
	 * @param user
	 */
	void saveBankInfo(MultipartFile image,AccountBank bank,User user);

	/**
	 * 逻辑删除银行信息
	 * @param bankId
	 * @param accountId
	 * @param user
	 */
	void updateDeletedById(Long bankId,Long accountId,User user);
	
	/**
	 * 批量更新银行信息
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<AccountBank> list);
	
	public int updateBankStatusByAccountId(AccountBank bank);
}
