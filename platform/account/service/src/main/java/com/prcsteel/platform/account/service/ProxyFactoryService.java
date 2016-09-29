package com.prcsteel.platform.account.service;

import java.util.List;

import com.prcsteel.platform.account.model.model.ProxyFactory;
import com.prcsteel.platform.acl.model.model.User;

/**
 * 代理钢厂服务
 * @author tangwei
 *
 */
public interface ProxyFactoryService {

	/**
	 * 根据客户Id查询代理钢厂
	 * @param accountId
	 * @return
	 */
	public List<ProxyFactory> findProxyFactoryByAccountId(long accountId);
	
	/**
	 * 新增保存代理钢厂
	 * @param record
	 * @param user
	 * @return
	 */
	public int addSaveProxyFactory(ProxyFactory record, User user);
	
	/**
	 * 编辑保存代理钢厂
	 * @param record
	 * @param user
	 * @return
	 */
	public int editSaveProxyFactory(ProxyFactory record, User user);
	
	/**
	 * 删除代理钢厂
	 * @param id
	 * @param user
	 * @return
	 */
	public int deleteProxyFactory(long id, User user);
}
