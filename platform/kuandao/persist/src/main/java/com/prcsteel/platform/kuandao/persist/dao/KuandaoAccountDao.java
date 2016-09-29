package com.prcsteel.platform.kuandao.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.kuandao.model.dto.AccountContactDto;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.SynchronizeLogDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoCustAccount;
import com.prcsteel.platform.kuandao.model.model.SynchronizeLog;

public interface KuandaoAccountDao {
	
	/**
	 * 会员开户
	 * @param userId  系统内部userId
	 * @return
	 */
	int insert(KuandaoCustAccount kuandaoCustAccount);
	
	/**
	 * 修改会员信息
	 * @param userId 系统内部userId
	 * @return
	 */
	int update(KuandaoCustAccount kuandaoCustAccount);
	
	/**
	 * 删除会员
	 * @param userId 系统内部userId
	 * @return
	 */
	Integer delete(Integer id);

	/**
	 * 分页查询已开户客户列表
	 * @param param
	 * @return
	 */
	List<KuandaoAccountDto> queryOpenedAccount(Map<String,Object> param);

	/**
	 * 查询已开户客户总数量
	 * @param accountDto
	 * @return
	 */
	int totalOpenedAccount(KuandaoAccountDto accountDto);

	/**
	 * 分页查询未开户客户列表
	 * @param accountDto
	 * @return
	 */
	List<KuandaoAccountDto> queryUnOpenedAccount(Map<String,Object> param);

	/**
	 * 查询未开户客户总数量
	 * @param param
	 * @return
	 */
	int totalUnOpenedAccount(KuandaoAccountDto accountDto);

	/**
	 * 查询客户同步日志总数量
	 * @param synLogDto
	 * @return
	 */
	int totalSynchronizeLog(SynchronizeLogDto synLogDto);

	/**
	 * 分页查询客户同步日志列表
	 * @param param
	 * @return
	 */
	List<SynchronizeLogDto> querySynchronizeLog(Map<String, Object> param);

	/**
	 * 根据条件查询款道客户信息(只包含款道账户表中的内容)
	 * @param accountDto
	 * @return
	 */
	List<KuandaoCustAccount> queryKuandaoAccountByCondition(KuandaoAccountDto accountDto);

	/**
	 * 根据条件查询客户信息(包含款道账号表及客户基础信息)
	 * @param queryAccountDto
	 * @return
	 */
	List<KuandaoAccountDto> queryAccountByCondition(KuandaoAccountDto queryAccountDto);

	/**
	 * 记录操作日志
	 * @param synchronizeLog
	 * @return
	 */
	int insertSynchronizeLog(SynchronizeLog synchronizeLog);

	/**
	 * 查询全部开户的客户信息
	 * 
	 * @return
	 */
	List<KuandaoAccountDto> queryAllOpenAccount();

	/***
	 * 查询账户体系客户信息
	 * @param queryCustAccountDto
	 * @return
	 */
	CustAccountDto queryCustAccountByCondition(CustAccountDto queryCustAccountDto);
	
	/**
	 * 根据客户信息查询联系人信息
	 * @param accountId
	 * @return
	 */
	List<AccountContactDto> queryContactByAccountId(Long accountId);
	
}
