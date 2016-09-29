package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.dto.AccountContactDto;
import com.prcsteel.platform.account.model.dto.AccountContactOrgUserDto;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AccountContactDao {

    /**
     * 根据客户id获取客户的联系人信息
     * @param paramMap
     * @return
     */
    public List<AccountContactDto> queryByAccountId(Map<String,Object> paramMap);
    
    /**
     * 根据accountId，contactId，managerId获取客户的联系人信息
     * @param paramMap
     * @return
     */
    public List<AccountContact> queryByIds(Map<String,Object> paramMap);

    /**
     * 根据客户id获取客户的联系人信息总数
     * @param paramMap
     * @return
     */
    public int totalContact(Map<String,Object> paramMap);

    /**
     * 根据id更新联系人信息
     * @param accountContact
     * @return
     */
    public int updateAccountContactById(AccountContact accountContact);

    /**
     * 新增联系人信息
     * @param accountContact
     * @return
     */
    public int insert(AccountContact accountContact);

    /**
     * 新增
     * @param accountContact
     * @return
     */
    public int insertSelective(AccountContact accountContact);

    /**
     * 根据Id锁定联系人信息
     * @param accountContact
     * @return
     */
    public int updateStatusById(AccountContact accountContact);

    /**
     * 根据部门Id，联系人Id，交易员Id锁定/解锁联系人信息
     * @param accountContact
     * @return
     */
    public int updateStatusByIds(AccountContact accountContact);

    /**
     * 根据id获取联系人信息
     * @param id
     * @return
     */
    public AccountContact queryById(String id);
    
    public int totalIsMain(Map<String,Object> paramMap);

    public int updateIsMainById(Map<String,Object> paramMap);

    public int updateIsMainByIdArr(Map<String,Object> paramMap);

    public int totalTel(Map<String,Object> paramMap);

    /**
     * 根据tel获取联系人信息
     * @param tel 手机号码
     * @return 联系人对象
     */
    AccountContact queryByTel(String tel);

    /**
     * 获取指定客户的所以联系人信息
     * @param accountId 客户ID
     * @return
     */
    List<AccountContact> queryContactListByAccountId(Long accountId);


    /**
     * 更新客户-联系人-交易员的关系
     * @param accountContact
     * @return
     */
    int updateIsDeletedByAccountIdAndContactId(AccountContact accountContact);

    /**
     * 批量插入客户-联系人-交易员的关系
     * @param list
     * @return
     */
    int batchInsert(List<AccountContact> list);

    /**
     * 查询部门中特定联系人所属的交易员
     * @param accountContact
     * @return
     */
    List<User> queryBindUserByDeptIdAndContactId(AccountContact accountContact);

    /**
     * 获取微信端相关的联系人信息
     * @param openId 微信端ID
     * @return
     */
    AccountContactOrgUserDto queryAccountContactByOpenId(String openId);

    /**
     * 获取客户ID列表
     * @param contactId 联系人ID
     * @author chengui
     * @return
     */
    List<Long> getAccountIdsByContactId(Integer contactId);
    
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
     * 根据id 删除记录
     * @param contactId 联系人ID
     * @author tuxianming 
     * @date 20160711
     * @return
     */
	public void deleteByIds(List<Long> ids);
	
	/***
	 * 是不是存在关联
	 * @param accountId
	 * @param contactId
	 * @param managerId
	 * @return
	 */
	public int existAssign(@Param("accountId") Long accountId, 
				@Param("contactId") Integer contactId,
				@Param("managerId") Long managerId
			);

    //根据公司ID获取交易员
    List<User> queryUserByCompanyId(Long companyId);
}