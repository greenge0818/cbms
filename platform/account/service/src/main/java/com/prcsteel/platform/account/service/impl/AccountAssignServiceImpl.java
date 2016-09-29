package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.AccountAssignDto;
import com.prcsteel.platform.account.model.dto.AccountAssignLogDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.model.dto.ContactLogDto;
import com.prcsteel.platform.account.model.enums.AccountStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAssignLogs;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.AccountCountAssignLogs;
import com.prcsteel.platform.account.model.query.AccountAssignLogQuery;
import com.prcsteel.platform.account.model.query.AccountContactAssignLogQuery;
import com.prcsteel.platform.account.persist.dao.AccountAssignDao;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiang on 2015/7/16.
 */
@Service("accountAssignService")
public class AccountAssignServiceImpl implements AccountAssignService {

	@Resource
	private AccountAssignDao accountAssignDao;

	@Resource
	private AccountContactDao accountContactDao;

	@Resource
	private AccountContactService accountContactService;

	/**
	 * 查询cust_account表所有
	 */
	@Override
	public List<Account> query() {
		List<Account> list = accountAssignDao.queryAll();
		return list;
	}

	/**
	 * 联表查询user表中的业务员名字(划转卖家 )
	 */
	@Override
	public List<AccountAssignDto> findAll(Map<String, Object> paramMap) {
		List<AccountAssignDto> list = accountAssignDao
				.queryByManagerId(paramMap);
		return list;
	}

	@Override
	@Transactional
	public boolean transferForBuyerCustomer(String accountIds,
			String managerExIds, String managerExNames, Long managerNextId,
			String managerNextName, User user) throws BusinessException {
		boolean flag = false;
		try {
			int updateNum = 0;
			int n = addAccountLogs(accountIds, managerExIds, managerExNames,
					managerNextId, managerNextName, user);// 增加客户划转记录
			int num = n
					+ insertContactLog(accountIds, managerNextId,
							managerNextName, user);// 增加联系人划转记录
			if (num > 0) {
				updateNum = updateByIds(accountIds, managerNextId);
			}
			if (updateNum > 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"划转客户或联系人时出错了！");
		}
		return flag;
	}

	/**
	 * 划转买家买家时，批量插入联系人划转记录
	 * 
	 * @param accountIds
	 *            客户ID
	 * @param managerNextId
	 *            现任交易员ID
	 * @param managerNextName
	 *            现任交易员名
	 * @param user
	 * @return
	 */
	private int insertContactLog(String accountIds, Long managerNextId,
			String managerNextName, User user) {
		ContactLogDto contactLogDto = new ContactLogDto();
		contactLogDto.setAccountIds(java.util.Arrays.asList(accountIds
				.split(",")));
		contactLogDto.setManagerNextId(managerNextId);// 现任ID
		contactLogDto.setManagerNextName(managerNextName);
		contactLogDto.setAssignerId(user.getId());
		contactLogDto.setAssignerName(user.getName());
		contactLogDto.setCreated(new Date());
		contactLogDto.setCreatedBy(user.getLoginId());
		contactLogDto.setLastUpdated(new Date());
		contactLogDto.setLastUpdatedBy(user.getName());
		contactLogDto.setModificationNumber(1);
		return accountAssignDao.insertContactLog(contactLogDto);
	}

	/**
	 * 划转客户，同时将与客户关联的联系人一起划转
	 * 
	 * @param accountIds
	 *            客户ID
	 * @param uid
	 *            现任交易员ID
	 * @return
	 */
	private int updateByIds(String accountIds, Long uid) {
		int num = 0;
		List<String> accountId = java.util.Arrays.asList(accountIds.split(","));
		int n = accountAssignDao.updateByIds(uid, accountId);// 客户划转
		if (n > 0) {
			num = accountAssignDao.updateByaccountId(uid, accountId);// 客户划转同时也将客户下的用户划转
		}
		return num;
	}

	@Override
	public List<AccountAssignDto> findByType(Map<String, Object> paramMap) {
		// 只查询买家
		List<AccountAssignDto> list = accountAssignDao.queryByType(paramMap);
		return list;
	}

	/**
	 * 增加客户划转记录
	 *
	 * @param accountIds
	 *            客户ID
	 *
	 * @param managerExIds
	 *            前任ID
	 *
	 * @param managerExNames
	 *            前任名字
	 *
	 * @param managerNextId
	 *            现任ID
	 *
	 * @param managerNextName
	 *            现任名字
	 *
	 * @return
	 */

	private int addAccountLogs(String accountIds, String managerExIds,
			String managerExNames, Long managerNextId, String managerNextName,
			User user) {
		int num = 0;
		String[] accountId = accountIds.split(",");// 公司ID
		String[] managerExId = managerExIds.split(",");// 前任ID
		String[] managerExName = managerExNames.split(",");// 前任名字
		for (int i = 0; i < managerExId.length; i++) {
			Account account = accountAssignDao.selectById(Long
					.parseLong(accountId[i]));
			AccountAssignLogs accountAssignLogs = new AccountAssignLogs();
			accountAssignLogs.setManagerExId(Long.parseLong(managerExId[i]));
			accountAssignLogs.setManagerExName(managerExName[i]);
			accountAssignLogs.setManagerNextId(managerNextId);
			accountAssignLogs.setManagerNextName(managerNextName);
			accountAssignLogs.setAccountId(Long.parseLong(accountId[i]));
			accountAssignLogs.setRegTime(account.getRegTime());
			accountAssignLogs.setAssignerId(user.getId());
			accountAssignLogs.setAssignerName(user.getName());
			accountAssignLogs.setType(account.getType());
			accountAssignLogs.setCreated(new Date());
			accountAssignLogs.setCreatedBy(user.getLoginId());
			accountAssignLogs.setLastUpdated(new Date());
			accountAssignLogs.setLastUpdatedBy(user.getName());
			accountAssignLogs.setModificationNumber(1);
			num = accountAssignDao.insert(accountAssignLogs);
		}
		return num;

	}

	@Override
	public List<User> queryalls(Long orgId) {
		return accountAssignDao.queryAlls(orgId);
	}

	@Override
	public List<ContactAssignDto> findByIds(Map<String, Object> paramMap) {
		List<ContactAssignDto> list = accountAssignDao.queryByIds(paramMap);
		return list;
	}
	
	@Override
	@Transactional
	public void updateContact(String contactIds, String managerExIds,
			Long uid, String accountIds, String nameExs, String nameNext,
			String contactName, User user, String type) throws BusinessException {
		String[] contactIdArr = contactIds.split(",");
		String[] accountIdArr = accountIds.split(",");
		String[] managerExIdArr = managerExIds.split(",");

		for (int i = 0; i < contactIdArr.length; i++) {
			AccountContact old = accountContactService.queryByIds(Long.parseLong(accountIdArr[i]), Integer.parseInt(contactIdArr[i]), Long.parseLong(managerExIdArr[i]));

			//chengui 联系人被锁定时，不能划转
			if (old != null && AccountStatus.LOCKED.getCode().equals(old.getStatus())) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "联系人已被锁定。");
			}

			old.setManager(uid);
			old.setLastUpdated(new Date());
			old.setModificationNumber(old.getModificationNumber() + 1);
			old.setLastUpdatedBy(SecurityUtils.getSubject()
					.getPrincipal().toString());

			//chengui 若联系人被划转后的记录已存在时，不能划转
			if(null != accountContactService.queryByIds(old.getAccountId(), old.getContactId(), old.getManager())){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该联系人关系已存在。");
			}

			if(accountContactDao.updateAccountContactById(old) == 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"划转联系人时出错了！");
			}
		}

		//增加联系人划转记录
		addcontactLogs(accountIds, managerExIds, uid, nameExs, nameNext, contactName, user, type);

	}

	

	/**
	 * 增加联系人划转记录
	 * 
	 * @param accountIds
	 *            客户ID
	 * @param managerNextId
	 *            现任业务员ID
	 * @param managerExIds
	 *            前任业务员ID
	 * @param nameExs
	 *            前任业务员名字
	 * @param nameNext
	 *            现任业务员名字
	 * @param contactNames
	 *            联系人
	 * @param user
	 * @return
	 */
	private void addcontactLogs(String accountIds, String managerExIds,
			Long managerNextId, String nameExs, String nameNext,
			String contactNames, User user, String types) {
		String[] strIds = accountIds.split(",");// 客户ID
		String[] managerExId = managerExIds.split(",");// 前任ID
		String[] nameEx = nameExs.split(",");// 前任名字
		String[] contactName = contactNames.split(",");// 联系人
		for (int i = 0; i < managerExId.length; i++) {
			Long accountId = Long.parseLong(strIds[i]);
			AccountCountAssignLogs accountCountAssignLogs = new AccountCountAssignLogs();
			accountCountAssignLogs.setAccountId(accountId);
			accountCountAssignLogs.setContactName(contactName[i]);
			accountCountAssignLogs.setRegTime(accountAssignDao.selectCreatedById(accountId));
			accountCountAssignLogs.setManagerExId(Long
					.parseLong(managerExId[i]));// 前任ID
			accountCountAssignLogs.setManagerExName(nameEx[i]);
			accountCountAssignLogs.setManagerNextId(managerNextId);// 现任ID
			accountCountAssignLogs.setManagerNextName(nameNext);
			accountCountAssignLogs.setAssignerId(user.getId());
			accountCountAssignLogs.setAssignerName(user.getName());
			accountCountAssignLogs.setCreated(new Date());
			accountCountAssignLogs.setCreatedBy(user.getLoginId());
			accountCountAssignLogs.setLastUpdated(new Date());
			accountCountAssignLogs.setLastUpdatedBy(user.getName());
			accountCountAssignLogs.setModificationNumber(1);
			if(accountAssignDao.insertCountLogs(accountCountAssignLogs) == 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"划转联系人时出错了！");
			}
		}
	}

	@Override
	public Account getById(Long id) {
		Account account = accountAssignDao.selectById(id);
		return account;
	}


	/**
	 * 查询客户划转历史记录总数
	 *
	 * @param query
	 * @return
	 */
	@Override
	public int queryAccountAssignLogTotalByParam(AccountAssignLogQuery query) {
		return accountAssignDao.queryAccountAssignLogTotalByParam(query);
	}

	/**
	 * 根据条件分页查询客户划转历史记录
	 *
	 * @param query
	 * @return
	 */
	@Override
	public List<AccountAssignLogDto> queryAccountAssignLogByParam(AccountAssignLogQuery query) {
		return accountAssignDao.queryAccountAssignLogByParam(query);
	}
	
	
	
	/**
	 * 查询联系人划转历史记录总数
	 * @param query
	 * @return
	 */
	@Override
	public int queryAccountContactAssignLogTotalByParam(AccountContactAssignLogQuery query){
		return accountAssignDao.queryAccountContactAssignLogTotalByParam(query);
	}
	
	/**
	 * 根据条件分页查询联系人划转历史记录
	 * @param query
	 * @return
	 */
	@Override
	public List<AccountAssignLogDto> queryAccountContactAssignLogByParam(AccountContactAssignLogQuery query){
		return accountAssignDao.queryAccountContactAssignLogByParam(query);
	}
}
