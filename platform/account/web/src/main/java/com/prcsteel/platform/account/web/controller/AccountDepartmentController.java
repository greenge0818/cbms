package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.api.RestUserService;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.TransactionType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 查找部门的
 * Created by prcsteel on 2016/3/17.
 */
@Controller
@RequestMapping("/api/account")
public class AccountDepartmentController {
    @Resource
    private AccountService accountService;
    @Resource
    private RestUserService restUserService;
    @Resource
    private ContactService contactService;
    @Resource
    private UserDao userDao;
    /**
     *
     *根据客户名称查询 有几个部门
     * @param accountId  客户名称
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryDepartmentByName", method = RequestMethod.POST)
    public int queryDepartmentByName(Long accountId){
        int a=0;
        try {
            a=	accountService.queryDepartmentByName(accountId);
        }catch (BusinessException e){
            return 0;
        }catch (Exception e){
            return 0;
        }
        return a;
    }
    @ResponseBody
    @RequestMapping(value = "/queryDepartment", method = RequestMethod.POST)
    public Long queryDepartment(Long id){
        Long account=null;
        try {
            account=	accountService.queryDepartment(id);
        }catch (BusinessException e){
            return null;
        }catch (Exception e){
            return null;
        }
        return account;
    }
    @ResponseBody
    @RequestMapping(value = "/registerCompany", method = RequestMethod.POST)
    public Result registerCompany(String companyName, String accountCode, String bankName,
                                  String bankCode, String contactName, String tel, Long userId, String userName){
        Result result=new Result();
        try {
            SaveAccountDto saveInfo = new SaveAccountDto();

            Account account = new Account();
            account.setName(new String(companyName.getBytes("iso-8859-1"),"utf-8"));
            account.setAccountTag(AccountTag.buyer.getCode());
            account.setOrgId(0L);
            account.setOrgName("");
            account.setBusinessType(AccountBusinessType.terminal.toString());

            AccountExt accountExt = new AccountExt();
            accountExt.setName(new String(companyName.getBytes("iso-8859-1"),"utf-8"));
            accountExt.setAccountCode(accountCode);
            accountExt.setBankNameMain(new String(bankName.getBytes("iso-8859-1"), "utf-8"));
            accountExt.setBankCode(bankCode);

            saveInfo.setAccount(account);
            saveInfo.setAccountExt(accountExt);
//            User user = new User();
//            user.setId(userId);
//            user.setName(new String(userName.getBytes("iso-8859-1"),"utf-8"));
            saveInfo.setUser(userDao.queryById(userId));

            Contact contact = new Contact();
            contact.setName(new String(contactName.getBytes("iso-8859-1"), "utf-8"));
            contact.setTel(tel);
            saveInfo.setContact(contact);
            accountService.save(saveInfo);
            result.setSuccess(true);
            result.setData(account); //这边getId是部门id，所以取parentId
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }catch (Exception e){
            result.setSuccess(false);
            result.setData(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/queryByAccountId", method = RequestMethod.POST)
    public Account queryByAccountId(Long id){
        try {
            return accountService.queryById(id);
        }catch (BusinessException e){
        }catch (Exception e){
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/queryContactByTel", method = RequestMethod.POST)
    public Contact queryContactByTel(String tel){
        try {
            return contactService.queryByTel(tel);
        }catch (BusinessException e){
        }catch (Exception e){
        }
        return null;
    }
}
