package com.prcsteel.platform.account.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.account.model.dto.AccountAssignDto;
import com.prcsteel.platform.account.model.dto.AccountAssignLogDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.query.AccountAssignLogQuery;
import com.prcsteel.platform.account.model.query.AccountContactAssignLogQuery;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;

/**
 * Created by lixiang on 2015/7/16.
 */
public interface AccountAssignService {
	/**
	 * 查询Account表所有记录
	 * 
	 * @return
	 */
	public List<Account> query();

	/**
	 * 联表查询客户表中对应的业务员
	 * 
	 * @return
	 */
	public List<AccountAssignDto> findAll(Map<String, Object> paramMap);

	/**
	 * 划转买家客户增加划转客户记录
	 * 
	 * @param accountIds
	 *            客户ID
	 * @param managerExIds
	 *            前任交易员ID
	 * @param managerExNames
	 *            前任交易员
	 * @param managerNextId
	 *            现任交易员ID
	 * @param managerNextName
	 *            现任交易员
	 * @param type
	 *            分配类型
	 * @param userId
	 *            当前登录者id
	 * @param loginId
	 *            当前登录id
	 * @param userName
	 *            当前登录姓名
	 * @return
	 */
	public boolean transferForBuyerCustomer(String accountIds,
			String managerExIds, String managerExNames, Long managerNextId,
			String managerNextName, User user) throws BusinessException;

	/**
	 * 只查买家联系人
	 * 
	 * @return
	 */
	public List<AccountAssignDto> findByType(Map<String, Object> paramMap);

	public List<User> queryalls(Long orgId);

	/**
	 * 连表查询联系人对应的公司及业务员的名字
	 * 
	 * @return list
	 */
	public List<ContactAssignDto> findByIds(Map<String, Object> paramMap);

	/**
	 * 修改联系人表的manager
	 * 
	 * @param contactIds
	 *            联系人ID
	 * @param managerExIds
	 *            前任交易员ID
	 * @param uid
	 *            现任交易员ID
	 * @param accountIds
	 *            客户ID
	 * @param nameExs
	 *            前任交易员
	 * @param nameNext
	 *            现任交易员
	 * @param contactName
	 *            联系人
	 * @param user
	 * @param type
	 *            类型
	 * @return
	 * @throws BusinessException
	 */
	public void updateContact(String contactIds, String managerExIds,
			Long uid, String accountIds, String nameExs, String nameNext,
			String contactName, User user, String type)
			throws BusinessException;

	/**
	 * 根据客户ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Account getById(Long id);


	/**
	 * 查询客户划转历史记录总数
	 * @param query
	 * @return
	 */
	int queryAccountAssignLogTotalByParam(AccountAssignLogQuery query);

	/**
	 * 根据条件分页查询客户划转历史记录
	 * @param query
	 * @return
	 */
	List<AccountAssignLogDto> queryAccountAssignLogByParam(AccountAssignLogQuery query);
	
	
	/**
	 * 查询联系人划转历史记录总数
	 * @param query
	 * @return
	 */
	int queryAccountContactAssignLogTotalByParam(AccountContactAssignLogQuery query);
	
	/**
	 * 根据条件分页查询联系人划转历史记录
	 * @param query
	 * @return
	 */
	List<AccountAssignLogDto> queryAccountContactAssignLogByParam(AccountContactAssignLogQuery query);
 }
