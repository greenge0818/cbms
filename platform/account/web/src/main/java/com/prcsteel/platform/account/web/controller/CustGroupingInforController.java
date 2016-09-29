
package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.CustGroupingInforDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.GroupInforAccountDto;
import com.prcsteel.platform.account.model.enums.CustGroupStatus;
import com.prcsteel.platform.account.model.enums.OwerType;
import com.prcsteel.platform.account.model.model.CustGroupingInfor;
import com.prcsteel.platform.account.model.model.CustGroupingInforAccount;
import com.prcsteel.platform.account.model.model.CustGroupingInforFlow;
import com.prcsteel.platform.account.model.query.CustGroupingInforQuery;
import com.prcsteel.platform.account.model.query.CustGroupingQuery;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.CustGroupingInforService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.account.web.support.ShiroVelocity;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * 信用额度管理
 */
@Controller
@RequestMapping("/account/grouping")
public class CustGroupingInforController extends BaseController {

    @Resource
    private CustGroupingInforService custGroupingInforService;
    @Resource
    OrganizationService organizationService;

    @Resource
    ContactService contactService;


    @Resource
    AccountService accountService;

    private static final Logger logger = Logger.getLogger(CustGroupingInforController.class);
    
    ShiroVelocity permissionLimit = new ShiroVelocity();
    
    final String PERMISSION_AUDIT = "account:grouping:audit:creditlimit:audit"; // 审核额度权限
    
    final String PERMISSION_UPDATE = "account:grouping:updat:cust:creditLimt:update"; // 修改客户额度权限
    
    final String PERMISSION_EDIT = "account:grouping:editgrouplimitinfo"; //客户组编辑权限
    
    final String PERMISSION_ADD = "account:grouping:addAccount"; //添加客户限  
    
    final String PERMISSION_DELETE = "account:grouping:delGroup";//删除客户
    
    final String PERMISSION_EDIT_ACCOUNT = "account:grouping:editAccount"; //编辑客户限  
    
    final String PERMISSION_DELETE_ACCOUNT = "account:grouping:delAccount";//删除客户
    
    final String PERMISSION_ADD_CUST_LIMIT = "account:grouping:addgroupandcustlimit";//添加分组权限
    /**
     * 审核额度tab页
     *
     * @param out
     * @return
     * @author lixiang
     */
    @RequestMapping("/limitManager")
    public String initlimitManager(ModelMap out) {
    	out.put("PERMISSION_AUDIT",permissionLimit.hasPermission(PERMISSION_AUDIT));
    	out.put("PERMISSION_UPDATE",permissionLimit.hasPermission(PERMISSION_UPDATE));
        return "account/grouping/limitManager";
    }

    /**
     * 账户信息流水列表查询
     *
     * @param
     * @return
     */

    @RequestMapping("query/limitManager")
    @ResponseBody
    public PageResult queryLimitManager(CustGroupingInforQuery custGroupingInforQuery) {
        PageResult pageResult = new PageResult();
        custGroupingInforQuery.setUserIds(getUserIds());
        List<CustGroupingInforDto> list = custGroupingInforService.queryGroupInfoAccount(custGroupingInforQuery);
        pageResult.setData(list);
        return pageResult;
    }

    @RequestMapping("/querylimitgrouping")
    public String initLimitGrouping(ModelMap out) {
    	out.put("PERMISSION_EDIT",permissionLimit.hasPermission(PERMISSION_EDIT));
    	out.put("PERMISSION_ADD",permissionLimit.hasPermission(PERMISSION_ADD));
    	out.put("PERMISSION_DELETE",permissionLimit.hasPermission(PERMISSION_DELETE));
    	out.put("PERMISSION_EDIT_ACCOUNT",permissionLimit.hasPermission(PERMISSION_EDIT_ACCOUNT));
    	out.put("PERMISSION_DELETE_ACCOUNT",permissionLimit.hasPermission(PERMISSION_DELETE_ACCOUNT));
    	out.put("PERMISSION_ADD_CUST_LIMIT",permissionLimit.hasPermission(PERMISSION_ADD_CUST_LIMIT));
    	return "account/grouping/queryLimitGrouping";
    }

    @ResponseBody
    @RequestMapping("query/groupinfo")
    public PageResult queryGroupInfo(CustGroupingInforQuery custGroupingInforQuery) {
        PageResult pageResult = new PageResult();
        List<String> groupStatusList = new ArrayList<>();
        groupStatusList.add(CustGroupStatus.APPROVED.toString());
        groupStatusList.add(CustGroupStatus.REQUESTED.toString());
        groupStatusList.add(CustGroupStatus.DECLINED.toString());
        custGroupingInforQuery.setUserIds(getUserIds());
        custGroupingInforQuery.setGroupStatusList(groupStatusList);
        if (custGroupingInforQuery.getAccountName() != null) {
            custGroupingInforQuery.setAccountName(custGroupingInforQuery.getAccountName().trim());
        }
        if (custGroupingInforQuery.getGroupInfoName() != null) {
            custGroupingInforQuery.setGroupInfoName(custGroupingInforQuery.getGroupInfoName().trim());
        }
        int groupCount = custGroupingInforService.queryGroupInfoCount(custGroupingInforQuery);
        List<CustGroupingInforDto> list = null;
        if (groupCount > 0) {
            list = custGroupingInforService.queryGroupInfo(custGroupingInforQuery);
            pageResult.setData(list);
        }
        pageResult.setRecordsTotal(list == null ? 0 : list.size());
        pageResult.setRecordsFiltered(groupCount);
        return pageResult;
    }

    @ResponseBody
    @RequestMapping("/delGroup")
    public Result deleteGroup(Long groupId) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            if (custGroupingInforService.deleteLimitGroup(groupId, user) > 0) {
                result.setSuccess(true);
                result.setData("删除成功！");
            } else {
                result.setSuccess(false);
                result.setData("删除失败！");
            }
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/delAccount")
    public Result deleteAccount(Long id, String remark) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            if (custGroupingInforService.deleteLimitGroupAccount(id, remark, user) > 0) {
                result.setSuccess(true);
                result.setData("删除成功！");
            } else {
                result.setSuccess(false);
                result.setData("删除失败！");
            }
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/addAccount")
    public Result addAccount(Long groupId, Long accountId, String accountName, Long deptId, String deptName, BigDecimal creditLimit, Integer autoRefund) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            if (custGroupingInforService.addLimitGroupAccount(groupId, accountId, accountName, deptId, deptName, creditLimit, autoRefund, user) > 0) {
                result.setSuccess(true);
                result.setData("添加成功！");
            } else {
                result.setSuccess(false);
                result.setData("添加失败！");
            }
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("/editAccount")
    public Result eidtAccount(Long id, BigDecimal creditLimit, Integer autoRefund) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            if (custGroupingInforService.editGroupAccount(id, creditLimit, autoRefund, user) > 0) {
                result.setSuccess(true);
                result.setData("保存成功！");
            } else {
                result.setSuccess(false);
                result.setData("保存失败！");
            }
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 待审核额度tab调整客户待审核额度
     *
     * @param custId      客户关联id
     * @param creditLimit 修改的额度.
     * @return
     * @author afeng
     */
    @RequestMapping("/update/cust/creditLimit")
    @ResponseBody
    public Result updateCustCreditLimit(Long custId, BigDecimal creditLimit) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            custGroupingInforService.updateCreditLimit(custId, creditLimit, user);
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }


    /**
     * @param CustGroupingQuery
     * @return String
     * @description :添加分组，新建分组和公司
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */

    @RequestMapping("/addgroupandcustlimit")
    @ResponseBody
    public Result addGroupAndCustLimit(CustGroupingQuery query) {
        Result result = new Result();
        try {
            Long userId = getLoginUser().getId();
            String userName = getLoginUser().getName();
            query.setUserId(userId);
            query.setUserName(userName);
            custGroupingInforService.addGroupAndCustLimit(query);
            result.setSuccess(Boolean.TRUE);
        } catch (BusinessException e) {
            logger.error("新建分组失败" + e.getMsg());
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * @return Result
     * @auth:zhoucai@prcsteel.com
     * @date:2016-4-13
     * @descrition:根据公司id查部门列表
     */

    @RequestMapping("/querydepartbycompanyid")
    @ResponseBody
    public Result queryDepartByCompanyid(@RequestParam("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        List<DepartmentDto> list = accountdto.getDepartment();
        return new Result(list);
    }

    /**
     * @param
     * @return String
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */

    public synchronized String createCode() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(date);
    }

    /**
     * @param proupId     分组id
     * @param proupSerial 分组流水号
     * @param custSerials 客户流水号
     * @param status      审核类型
     * @return
     * @author afeng
     */
    @OpAction( key = "groupId")
    @RequestMapping("/audit/creditlimit")
    @ResponseBody
    public Result auditCreditLimit(Long groupId, String groupSerial, String custSerials, String status) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            custGroupingInforService.auditCreditLimit(groupId, groupSerial, custSerials, user, status);
            result.setSuccess(true);
            result.setData("操作成功！");
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * @description 根据分组id查询分组西悉尼
     * @return Result
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */
    @RequestMapping("/querygroupinginfobyid")
    @ResponseBody
    public Result queryGroupingInfoById(CustGroupingInfor custGroupingInfor) {
        Result result = new Result();
        try {
            custGroupingInfor = custGroupingInforService.queryGroupingInfoById(custGroupingInfor.getId());
            result.setSuccess(true);
            result.setData(custGroupingInfor);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * @description 根据分组id查询分组西悉尼
     * @return Result
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */
    @RequestMapping("/editgrouplimitinfo")
    @ResponseBody
    @Transactional
    public Result editGroupLimitInfo(CustGroupingInfor custGroupingInfor,Long id) {

        Result result = new Result();
        try {
            custGroupingInforService.editGroupLimitInfo(custGroupingInfor, getLoginUser());
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

  
    
    /**
     * @description 客户管理查看信用额度分组
     * @return Result
     * @author lixiang
     * @version V1.0
     */
    @RequestMapping("/get/accountGroup")
    public String getAccountGroup(ModelMap out, Long id) {
    	CustGroupingInforQuery custGroupingInfor = new CustGroupingInforQuery();
    	custGroupingInfor.setDepartmentId(id);
    	List<CustGroupingInforDto> list = custGroupingInforService.queryGroupInfo(custGroupingInfor);
    	if (list.size() > 0) {
    		out.put("list", list.get(0).getCompanyList());
        	out.put("groupName", list.get(0).getGroupName());//组名称
        	out.put("groupLimit", list.get(0).getGroupLimit());//组信用额度
        	out.put("groupCreditUsed", list.get(0).getGroupCreditUsed());//组已使用额度
        	out.put("groupCreditBalance", list.get(0).getGroupCreditBalance());//组可用额度
    	}
		return "accountinfo/accountManager";
    }

}
