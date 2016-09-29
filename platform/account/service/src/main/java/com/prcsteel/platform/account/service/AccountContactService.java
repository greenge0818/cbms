package com.prcsteel.platform.account.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.account.model.dto.AccountContactDto;
import com.prcsteel.platform.account.model.dto.AccountContactOrgUserDto;
import com.prcsteel.platform.account.model.dto.UpdateAssignPotentialcustomer;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by kongbinheng on 2015/7/12.
 */
public interface AccountContactService {

    /**
     * 根据客户id获取客户的联系人信息总数
     *
     * @param paramMap
     * @return
     */
    int totalContact(Map<String, Object> paramMap);

    /**
     * 根据客户id获取客户的联系人信息
     *
     * @param paramMap
     * @return
     */
    public List<AccountContactDto> queryByAccountId(Map<String, Object> paramMap);

    /**
     * 添加客户的联系人信息
     *
     * @param accountContact
     * @return
     */
    public boolean add(AccountContact accountContact);

    /**
     * 编辑客户的联系人信息
     *
     * @param accountContact
     * @return
     */
    public boolean edit(AccountContact accountContact, String type);

    /**
     * 锁定客户的联系人信息
     *
     * @param accountContact
     * @return
     */
    public boolean disabledById(AccountContact accountContact);

    /**
     * 根据部门Id，联系人Id，交易员Id锁定/解锁联系人信息
     * @param accountContact
     * @return
     */
    public int updateStatusByIds(AccountContact accountContact);

    /**
     * 根据id获取联系人信息
     *
     * @param id
     * @return
     */
    public AccountContact queryById(String id);

    public int totalIsMain(Map<String, Object> paramMap);

    public boolean updateIsMainById(Map<String, Object> paramMap);

    public boolean updateIsMainByIdArr(Map<String, Object> paramMap);

    /**
     * 根据客户id获取客户的主联系人信息
     *
     * @param accountId
     * @return
     */
    public AccountContactDto queryMainByAccountId(Long accountId);

    /**
     * 根据联系人id,客户id获取该联系人下客户的联系人信息
     *
     * @param managerId 管理员Id
     * @param type      用户类型
     * @param accountId 客户Id
     * @return
     */
    public List<AccountContactDto> queryByManagerIdAndAccountId(Long managerId, String type, Long accountId);

    /**
     * 查询联系人信息手机号是否存在
     *
     * @param paramMap
     * @return
     */
    public int totalTel(Map<String, Object> paramMap);

    /**
     * 根据tel获取联系人信息
     *
     * @param tel 手机号码
     * @return 联系人对象
     */
    AccountContact queryByTel(String tel);

    Long updateAccount(Account account, User user);
    /**
     * 获取指定客户的所以联系人信息
     * @param accountId 客户ID
     * @return
     */
    List<AccountContact> queryContactListByAccountId(Long accountId);

    /**
     * 获取微信端相关的联系人信息
     * @param openId 微信端ID
     * @return
     */
    AccountContactOrgUserDto queryAccountContactByOpenId(String openId);

    /**
     * 判断联系人关系是否被锁定
     * @param departmentId 部门ID
     * @param contact      联系人
     * @param managerId    交易员Id
     */
    public void isLockForAccountContact(Contact contact,Long departmentId, Long managerId, String accountName, String departmentName);

    /**
     * 根据accountId，contactId，managerId获取客户的联系人信息
     * @return
     */
    public AccountContact queryByIds(Long accountId,Integer contactId,Long manager);
	
	  /**
	 * @Description: CBMS新增客户联系人时，通知微信端
	 * @author wangxianjun
	 * @param phone  手机号
	 * @param user  当前用户
	 * @date 2016年5月26日
	 *
	 */
	public void noticeWechat(String phone,User user);

    /**
     * 获取客户ID列表
     * @param contactId 联系人ID
     * @author chengui
     * @return
     */
    public List<Long> getAccountIdsByContactId(Integer contactId);

    /**
     * 如果交易员与公司部门的联系人没有关系，创建关系
     * @param departmentId 部门Id
     * @param contactId 联系人Id
     * @param managerId 交易员Id
     * @param user
     */
    void addAccountContact(Long departmentId, Integer contactId, Long managerId, User user);
    
    
    /**
     * 获取客户的联系人
     * @return
     */
    List<AccountContact>getContactByAccountId(Long accountId);
    
    /**
     * 根据联系人id 获取关联信息
     * @param contactId 联系人ID
     * @author tuxianming 
     * @date 20160711
     * @return
     */
    List<AccountContactDto> queryByContactId(Long contactId);
    
    /**
     * 入口用户管理列表 -- 更新关联
     * @author tuxianming 
     * @date 20160711
     * @return
     */
    void assignPotentialcustomer(UpdateAssignPotentialcustomer dto, User user);


    /**
     * 根据公司ID获取交易员
     * @param companyId
     * @return
     */
    List<User> queryUserByCompanyId(Long companyId);

}
