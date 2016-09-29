package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.ContactWithPotentialCustomerDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.query.ContactWithPotentialCustomerQuery;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;

/**
 * Created by dengxiyan on 2016/1/15.
 */
public interface ContactService{
    AccountDto getCompanyById(Long id);

    void saveDept(Account dept,User user);

    void deleteDept(Long deptId,User user);

    Account getAccountById(Long id);

    List<DepartmentDto> getDeptsAndContactsByCompanyId(Long companyId,List<Long> userIds);

    void saveContact(SaveContactDto dto);

    ContactDto getContactById(Integer contactId);

    List<User> queryUserByOrgId(Long orgId);

    Contact queryByTel(String tel);


    /**
     * 查询部门中特定联系人所属的交易员集合
     * @return
     */
    List<User> queryBindUserByDeptIdAndContactId(AccountContact accountContact);

    /**
     * 获得其他兄弟部门的所有联系人（该公司的所有联系人排除本部门已添加的联系人）
     * 用于添加已有联系人
     * @param companyId
     * @param departId
     * @return
     */
    List<ContactDto> getContactsOfSiblingsDepart(Long companyId, Long departId, List<Long> userIds);

    /**
     * 查找业务服务中心及服务中心的所有交易员
     *
     * @param orgId 指定的业务服务中心id
     * @return
     */
    List<UserOrgDto> queryTradersBusinessOrgByOrgIdSelective(Long orgId);

    /**
     * 根据部门ID查询联系人
     * @param departId 部门ID
     * @return 联系人集合
     */
    List<Contact> queryContactsByDepartId(Long departId);

    /**
     * 查询联系人
     * @param query 查询dto
     * @return 联系人集合
     */
    List<ContactDto> queryContactsByContactDto(ContactDto query);

    /**
     * 按超市用户id更新联系电话
     * @param accountContact
     * @author chengui
     */
    int updateTelByEcUserId(Contact accountContact);

    /**
     * chengui 按联系电话更新超市用户id
     * @param accountContact
     * @author chengui
     */
    int updateEcUserIdByTel(Contact accountContact);

    /**
     * 按超市用户id更新联系电话
     * @param ecUserId 超市客户id
     * @author chengui
     */
    Contact queryByEcUserId(Integer ecUserId);

    /**
     * 按超市用户id更新联系电话
     * @param accountContact 超市客户id
     * @author chengui
     */
    int saveContact(Contact accountContact);
    
    void updateById(Contact contact);
    
    /**
 	 * @author tuxianming
 	 * @date 20160704
 	 * @param query
 	 * @return
 	 */
 	List<ContactWithPotentialCustomerDto> queryContactWithPotentialCustomer(ContactWithPotentialCustomerQuery query);
 	
 	/**
 	 * 统计入口用户总数
 	 * @author tuxianming
 	 * @date 20160704
 	 * @param query
 	 * @return
 	 */
 	int totalContactWithPotentialCustomer(ContactWithPotentialCustomerQuery query);
}
