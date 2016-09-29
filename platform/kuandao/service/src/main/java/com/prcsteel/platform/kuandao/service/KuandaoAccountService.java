package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.SynchronizeLogDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;

public interface KuandaoAccountService {

	/**
	 *分页 查询已开户客户列表
	 * @param accountDto
	 * @return
	 */
	List<KuandaoAccountDto> queryOpenedAccount(KuandaoAccountDto accountDto,int start,int length);
	
	/**
	 * 查询已开户客户总数量
	 * @param accountDto
	 * @return
	 */
	Integer totalOpenedAccount(KuandaoAccountDto accountDto);
	
	/**
	 * 分页查询未开户客户列表
	 * @param accountDto
	 * @return
	 */
	List<KuandaoAccountDto> queryUnOpenedAccount(KuandaoAccountDto accountDto,int start,int length);
	
	/**
	 * 查询未开户客户总数量
	 * @param accountDto
	 * @return
	 */
	int totalUnOpenedAccount(KuandaoAccountDto accountDto);
	
	/**
	 * 查询客户同步日志总数量
	 * @param synLogDto
	 * @return
	 */
	Integer totalSynchronizeLog(SynchronizeLogDto synLogDto);

	/**
	 * 分页查询客户同步日志列表
	 * @param synLogDto
	 * @param start
	 * @param length
	 * @return
	 */
	List<SynchronizeLogDto> querySynchronizeLog(SynchronizeLogDto synLogDto, Integer start, Integer length);


	/**
	 * 修改客户信息
	 * @param kuandaoAccountDto
	 * @return
	 */
	Integer modifyCustAccount(KuandaoAccountDto kuandaoAccountDto);

	
	/**
	 * 款道开户
	 * @param acctId
	 * @param userName
	 * @return
	 */
	Integer openAccount(Long acctId, String userName);

	/**
	 * 同步客户账号信息
	 * @param acctId
	 * @param userName
	 * @return
	 */
	Integer synchronizeAccount(Long acctId, String userName);

	/**
	 * 用job方式同步客户信息
	 * @return
	 */
	void synchronizeAccountByJob();
	
	/**
	 * 同步所有客户信息到本地
	 * @return
	 */
	Integer synchronizeAllToLocal(String userName);

	/**
	 * 同步所有客户信息到浦发
	 * @return
	 */
	Integer synchronizeAllToSpdb(String userName);
	
	/**
	 * 客户批量开户
	 * @param userName
	 * @return
	 */
	Integer batchOpenAccount(String userName,List<Long> acctId);
	
	/**
	 * 删除客户
	 * @param userName
	 * @param acctId
	 * @return
	 */
	Integer closeAccount(String userName, Long acctId);
	
	/**
	 * 根据条件查询客户信息
	 * 
	 * @param queryAccountDto
	 * @return
	 */
	List<KuandaoAccountDto> queryAccountByCondition(KuandaoAccountDto queryAccountDto);
	
	/**
	 * 绑定客户结果通知
	 * @param spdbRequstParam 浦发请求参数
	 * @param membercode
	 * @param operType
	 * @return responseXml
	 */
	String boundNotify(SPDBNotifyRequstParam spdbRequstParam, String membercode, String operType);

}
