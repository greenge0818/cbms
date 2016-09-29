package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.ContactWithPotentialCustomerDto;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.query.ContactWithPotentialCustomerQuery;
import com.prcsteel.platform.acl.model.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);

    Contact selectByTel(String tel);

    Contact queryByTel(String tel);

    Contact queryByOpenId(String openId);

    List<ContactDto> queryContactsByCompanyId(Long companyId);

    List<User> queryUsersByOrgId(Long orgId);

    List<Contact> queryContactsByDepartId(Long departId);

    List<ContactDto> queryContactsByContactDto(ContactDto queryContactDto);

    int updateOpenId(Contact accountContact);

    int updateTelByEcUserId(Contact accountContact);

    int updateEcUserIdByTel(Contact accountContact);

    Contact queryByEcUserId(Integer ecUserId);
    //超市根据超市用户ID查询相关联的客户名称
    List<String> selectAccountNameByEcId(@Param("ecUserId")Integer ecUserId);
    
	/**
	 * 查询入口用户列表
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