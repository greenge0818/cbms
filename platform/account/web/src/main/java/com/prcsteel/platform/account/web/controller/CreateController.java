package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.account.web.mq.MarketAddUserSender;
import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.web.support.ShiroVelocity;
import com.prcsteel.platform.account.web.vo.Result;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
/**
 * Created by lichaowei on 2016/1/14.
 */
@Controller
@RequestMapping("/account/")
public class CreateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    @Resource
    AccountService accountService;
    @Resource
    OrganizationService orgService;
    @Resource
    ContactService contactService;
 //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
    @Resource
    SysSettingService sysSettingService;
	@Resource
	private  ProcessService processService;

    @Resource
    MarketAddUserSender marketAddUserSender;//新增联系人时，发送MQ

    @Value("${ecmq.active}")
    Boolean ecMQActive;
 //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
    ShiroVelocity permissionLimit = new ShiroVelocity();
    final String PERMISSION_QUALIFICATE_EDIT = "account:qualificate:edit"; //客户资质权限



    @RequestMapping("create")
    public String create(ModelMap out){
        User user = getLoginUser();
        Role role = getUserRole();
        List<Organization> orgList;
        if (RoleAuthType.ALL.getValue() == role.getType()) {
            orgList = orgService.queryAllBusinessOrg();
        }
        else {
            orgList = new ArrayList<>();
            orgList.add(orgService.queryById(user.getOrgId()));
        }
 //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
        List<SysSetting> customerLabel= sysSettingService.queryCustomerLabel();
        out.put("sellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getSettingValue().split("、"));
        out.put("buyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getSettingValue().split("、"));
        out.put("defaultSellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getDefaultValue());
        out.put("defaultBuyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getDefaultValue());

        out.put("hasQualificate", permissionLimit.hasPermission(PERMISSION_QUALIFICATE_EDIT));
        out.put("user", user);
        out.put("orgList", orgList);
        out.put("businessTypes", AccountBusinessType.values());
        out.put("accountTag", AccountTag.values());
        return "create";
    }

    /**
     * 根据公司名称查询
     *
     * @param name   输入的字符串
     * @return 存在返回true，无返回false
     */
    @ResponseBody
    @RequestMapping(value = "searchaccountbyname", method = RequestMethod.POST)
    public Result searchAccountByName(@RequestParam("name") String name) {
        Result result = new Result();
        if (StringUtils.isNotEmpty(name)) {
            Account account = accountService.selectAccountByName(name);
            if (account != null && account.getId() > 0) {
                result.setData(account);
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    /**
     * 根据公司名称查询
     *
     * @param mobile   输入的电话号码
     * @return 存在返回true，无返回false
     */
    @ResponseBody
    @RequestMapping(value = "searchcontactbymobile", method = RequestMethod.POST)
    public Result searchContactByMobile(@RequestParam("mobile") String mobile) {
        Result result = new Result();
        if (StringUtils.isNotEmpty(mobile)) {
            Contact contact = contactService.queryByTel(mobile);
            if (contact != null && contact.getId() > 0) {
                result.setData(contact);
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    /**
     * 保存账号基本数据
     *
     * @param saveAccount 账号信息
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.CreateConsignOrder)
    @OpParam(name = "account", value = "CreateAccount")
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Result save(SaveAccountDto saveAccount){
        Result result = new Result();
        try {
            //2016-5-7移植风控上线代码，公司名称不能为空 zhoucia@prcsteel.com
            if(saveAccount.getAccount().getName()!=null)
                saveAccount.getAccount().setName(saveAccount.getAccount().getName().replaceAll(" ", ""));
            saveAccount.setUser(getLoginUser());
            accountService.save(saveAccount);
            result.setData("保存成功");
            //add by wangxianjun 20160712 将新增的用户推送到超市系统
            if(ecMQActive!=null && ecMQActive) {
                try {
                    marketAddUserSender.withText(saveAccount.getContact().getTel(), saveAccount.getContact().getName());
                } catch (Exception e) {
                    logger.error("将新增的用户推送到超市系统失败", e);
                }
            }
        }catch(BusinessException be){
            result.setSuccess(Boolean.FALSE);
            result.setData(be.getMsg());
        }
        return result;
    }
}
