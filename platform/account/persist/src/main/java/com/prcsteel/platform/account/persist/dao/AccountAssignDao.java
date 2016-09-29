package com.prcsteel.platform.account.persist.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.AccountAssignDto;
import com.prcsteel.platform.account.model.dto.AccountAssignLogDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.model.dto.ContactLogDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAssignLogs;
import com.prcsteel.platform.account.model.model.AccountCountAssignLogs;
import com.prcsteel.platform.account.model.query.AccountAssignLogQuery;
import com.prcsteel.platform.account.model.query.AccountContactAssignLogQuery;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by lixiang on 2015/7/16.
 */
public interface AccountAssignDao {

	public List<Account> queryAll();

	/**
	 * 划转客户
	 * @param managerId
	 * @param accountIds
	 * @return
	 */
	public int updateByIds(@Param("managerId") Long managerId, @Param("accountIds") List<String> accountIds);

	/**
	 * 划转联系人
	 * @param managerId
	 * @param accountIds
	 * @return
	 */
	public int updateByaccountId(@Param("managerId") Long managerId, @Param("accountIds") List<String> accountIds);

	public List<AccountAssignDto> queryByManagerId(Map<String, Object> paramMap);

	public List<AccountAssignDto> queryByType(Map<String, Object> paramMap);

	public List<User> queryAlls(@Param(value="orgId") Long orgId);

	public int insert(AccountAssignLogs accountAssignLogs);

	public List<ContactAssignDto> queryByIds(Map<String, Object> paramMap);

	/**
	 * 划转联系人
	 * @param managerId
	 * @param contactIds
	 * @return
	 */
	public int updateContactById(@Param("managerId") Long managerId, @Param("contactIds") List<String> contactIds);

	public int insertCountLogs(AccountCountAssignLogs accountCountAssignLogs);

	public Account selectById(Long id);
	
	public Date selectCreatedById(Long id);
	
	/**
	 * 批量插入划转联系人日志
	 * @param contactLogDto
	 * @return
	 */
	public int insertContactLog(ContactLogDto contactLogDto);


	public int insertSelective(AccountAssignLogs accountAssignLogs);


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
