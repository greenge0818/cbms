package com.prcsteel.platform.account.web.controller;


import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.enums.AccountTabId;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.web.mq.MarketAddUserSender;
import com.prcsteel.platform.account.web.mq.MarketDisableUserSender;
import com.prcsteel.platform.account.web.mq.MarketUpdateUserSender;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dengxiyan on 2016/1/15.
 */
@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Resource
    ContactService contactService;
    @Resource
    AccountContactService accountContactService;

    @Resource
    MarketAddUserSender marketAddUserSender;
    @Resource
    MarketUpdateUserSender marketUpdateUserSender;
    @Resource
	MarketDisableUserSender marketDisableUserSender;
    
    @Value("${ecmq.active}")
    Boolean ecMQActive;
    
    /**
     * 查看联系人界面
     */
    @RequestMapping("/{companyId}/viewContact")
    public String viewContact(@PathVariable Long companyId,ModelMap out){
        AccountDto dto = contactService.getCompanyById(companyId);
        out.put("accountdto", dto);
        out.put("tabId", AccountTabId.contactInfo);

        List<Organization> list = new ArrayList<Organization>();
        if(!CollectionUtils.isEmpty(getUserIds())){
            out.put("permissionOrgId", getOrganization().getId());
        }
        list = organizationService.queryAllBusinessOrg();
        List<Long> orgIds = list.stream().map(a -> a.getId()).collect(Collectors.toList());
        out.put("orgs", list);
        out.put("orgIdsStr", StringUtils.join(orgIds, ","));

        return "contact/view";
    }

    /**
     * 加载部门和联系人数据
     * @return
     */
    @RequestMapping("/{companyId}/loadDeptsAndContacts")
    @ResponseBody
    public Result loadDeptsAndContacts(@PathVariable Long companyId){
        List<DepartmentDto> list =  contactService.getDeptsAndContactsByCompanyId(companyId,getUserIds());

        return new Result(list);
    }

    @RequestMapping("/saveDept")
    @ResponseBody
    public Result saveDept(Account dept){
        try{
            contactService.saveDept(dept,getLoginUser());
            return new Result("部门保存成功",Boolean.TRUE);
        }catch (BusinessException e){
            logger.error(e.getMsg(), e);
            return new Result(e.getMsg(),Boolean.FALSE);
        }
    }

    @RequestMapping("/{id}/getDept")
    @ResponseBody
    public Result getDept(@PathVariable("id") Long id){
        try{
            Account account = contactService.getAccountById(id);
            return new Result(account,Boolean.TRUE);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new Result(e.getMessage(),Boolean.FALSE);
        }
    }

    @RequestMapping("/saveContact")
    @ResponseBody
    public Result saveContact(SaveContactDto dto){
        try{
            dto.setUser(getLoginUser());
            contactService.saveContact(dto);
            
            //add by tuxianming 20160701 将新增的用户推送到超市系统
            if(ecMQActive!=null && ecMQActive){
            	
            	if(dto.isInEditMode()){
            		ContactDto contact = contactService.getContactById(dto.getContact().getId());
            		if(contact.getEcUserId()!=null && contact.getEcUserId()>0){
            			try {
                			marketUpdateUserSender.withText(new Long(contact.getEcUserId()), 
                					dto.getContact().getTel(), dto.getContact().getName());
         	   			} catch (Exception e) {
         	   				logger.info("将修改的用户推送到超市系统失败", e);
         	   			}
            		}
            	}else{
            		try {
      	                 marketAddUserSender.withText(dto.getContact().getTel(), dto.getContact().getName());
      	   			} catch (Exception e) {
      	   				logger.info("将新增的用户推送到超市系统失败", e);
      	   			}
            	}
            	
            }
            //end txm
    
            
            return new Result("用户保存成功",Boolean.TRUE);
        }catch (BusinessException e){
            logger.error(e.getMsg(), e);
            return new Result(e.getMsg(),Boolean.FALSE);
        }
    }
    @OpAction( key = "contactId")
    @RequestMapping("/lockAndUnlockContact")
    @ResponseBody
    public Result lockAndUnlockContact(AccountContact accountContact,Long contactId){
        try{
            accountContact.setLastUpdatedBy(getLoginUser().getName());
            accountContact.setLastUpdated(new Date());
            accountContactService.updateStatusByIds(accountContact);
            
            //add by tuxianming 20160708 将新增的用户推送到超市系统
            if(ecMQActive!=null && ecMQActive){
            	try {
            		Contact contact = contactService.getContactById(accountContact.getContactId());
            		if(contact.getEcUserId()!=null){
            			marketDisableUserSender.withText(contact.getEcUserId(), accountContact.getStatus()+"");
            		}
				} catch (Exception e) {
					logger.info(e.getMessage(), e);
				}
            }
			//end txm
            
            return new Result("锁定/解锁成功",Boolean.TRUE);
        }catch (BusinessException e){
            logger.error(e.getMsg(), e);
            return new Result(e.getMsg(),Boolean.FALSE);
        }
    }

    @RequestMapping("/getContact")
    @ResponseBody
    public Result getContact(AccountContact accountContact){
        try{
            ContactDto contactDto = new ContactDto();
            ContactDto query = new ContactDto();
            query.setId(accountContact.getContactId());
            query.setDeptId(accountContact.getAccountId());
            List<ContactDto> list = contactService.queryContactsByContactDto(query);
            if (list != null && list.size() > 0) {
                contactDto = list.get(0);
            }
            List<User> users = contactService.queryBindUserByDeptIdAndContactId(accountContact);
            Map map = new HashMap<>();
            map.put("contact", contactDto);
            map.put("users", users);
            map.put("userIds", getUserIds());
            return new Result(map,Boolean.TRUE);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new Result(e.getMessage(),Boolean.FALSE);
        }
    }

    @RequestMapping("/{id}/deleteDept")
    @ResponseBody
    public Result deleteDept(@PathVariable("id") Long id){
        try{
            contactService.deleteDept(id,getLoginUser());
            return new Result("部门删除成功",Boolean.TRUE);
        }catch (BusinessException e){
            logger.error(e.getMsg(),e);
            return new Result(e.getMsg(),Boolean.FALSE);
        }
    }

    /**
     * 获取公司其他部门已经存在的联系人列表数据过滤掉本部门已经添加的联系人
     * @return
     */
    @RequestMapping("/getContactData")
    @ResponseBody
    public Result getContactData(@RequestParam Long companyId,@RequestParam Long deptId ){
        try{
            List<ContactDto> contacts = contactService.getContactsOfSiblingsDepart(companyId, deptId, getUserIds());
            return new Result(contacts,Boolean.TRUE);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new Result(e.getMessage(),Boolean.FALSE);
        }
    }

    /**
     * 获取服务中心交易员组织数据
     * @return
     */
    @RequestMapping("/getTradersBusinessOrg")
    @ResponseBody
    public Result getTradersBusinessOrg(){
        try{
            List<UserOrgDto> users = contactService.queryTradersBusinessOrgByOrgIdSelective(null);
            return new Result(users,Boolean.TRUE);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new Result(e.getMessage(),Boolean.FALSE);
        }
    }




}
