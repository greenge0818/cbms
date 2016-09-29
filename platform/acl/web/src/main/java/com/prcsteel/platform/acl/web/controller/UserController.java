package com.prcsteel.platform.acl.web.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.acl.utils.LdapOperUtils;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.common.vo.TreeView;
import com.prcsteel.platform.core.service.CommonService;

/**
 * Created by rolyer on 15-7-11.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    UserService userService;
    @Resource
    OrganizationService organizationService;
    @Resource
    CommonService commonService;

    /**
     * 锁定用户,参数为用户名
     */
    @RequestMapping(value = "/disable/{id}.html", method = RequestMethod.POST)
    @ResponseBody
    @OpLog(OpType.DisableUser)
    @OpParam("id")
    public Result disableUser(@PathVariable("id") Long id) {
        Result result = new Result();
        User user = new User();
        user.setId(id);
        user.setStatus(Integer.valueOf(Constant.NO));
        result.setSuccess(userService.updateByPrimaryKeySelective(user, getLoginUser().getLoginId()));
        return result;
    }

    /**
     * 解锁用户,参数为用户名
     */
    @RequestMapping(value = "/activate/{id}.html", method = RequestMethod.POST)
    @OpLog(OpType.ActivateUser)
    @OpParam("id")
    @ResponseBody
    public Result activateUser(@PathVariable("id") Long id) {
        Result result = new Result();
        User user = new User();
        user.setId(id);
        user.setStatus(Integer.valueOf(Constant.YES));
        result.setSuccess(userService.updateByPrimaryKeySelective(user, getLoginUser().getLoginId()));
        return result;
    }

    /**
     * 更新用户基本信息
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/update.html", method = RequestMethod.POST)
    @OpLog(OpType.UpdateUser)
    @OpParam("user")
    @ResponseBody
    public Result update(User user) {
        Result result = new Result();
        try{
            userService.updateByPrimaryKeySelective(user, getLoginUser().getLoginId());
            result.setSuccess(true);
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
    /**
     * 按条件搜索用户
     * 初始化页面时传入的User参数为空
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult list(Long orgId,Boolean status, Integer start, Integer length) {
        PageResult result = new PageResult();
        Map<String, Object> param = new HashMap<>();
        if (orgId > 0) {
            List<Long> orgIds = organizationService.getAllChildOrgId(orgId, 0);
            param.put("orgIds", orgIds);
        }
        List<Integer>  statusList = new ArrayList<Integer>();
        if(status){
        	//显示锁定的服务中心
        	statusList.add(0);
        	statusList.add(1);
        }else{
        	//不显示锁定的服务中心
        	statusList.add(1);
        }
        param.put("statusList", statusList);
        param.put("start", start);
        param.put("length", length);
        List<UserDto> userList = userService.queryUserForShow(param);
        int total = userService.countUserForShow(param);
       // List<UserDto> userList = null;
        //result.setData(userList=status?userList:userList.stream().filter(a->a.getStatus()==1).collect(Collectors.toList()));
        result.setData(userList);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(userList.size());
        return result;
    }

    /**
     * 初始化页面上服务中心的下拉框
     *
     * @return
     */
    @RequestMapping(value = "/init/organization.html", method = RequestMethod.POST)
    @ResponseBody
    public Result initOrganization() {
        Result result = new Result();
        List<Organization> organizationList = organizationService.getAllOrganization();
        if (organizationList.size() > 0) {
            result.setData(organizationList);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改用户信息时初始化弹窗数据
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}.html", method = RequestMethod.POST)
    @ResponseBody
    public Result loadUserData(@PathVariable("id") Long id) {
        Result result = new Result();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        List<UserDto> userList = userService.queryUserForShow(param);
        if (userList.size() == 1) {
            result.setSuccess(true);
            result.setData(userList.get(0));
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/save.html", method = RequestMethod.POST)
    @OpLog(OpType.AddUser)
    @OpParam("user")
    @ResponseBody
    public Result save(User user) {
        Result result = new Result();
        if ((new LdapOperUtils()).find(user.getLoginId()) != null) {   //如果不在Ldap库里面直接返回，不执行插入
            try {
                userService.add(user, getLoginUser().getLoginId());
                result.setSuccess(true);
                result.setData("新增用户成功");
            } catch (BusinessException e) {
                result.setSuccess(false);
                result.setData(e.getMsg());
            }
        } else {
            result.setSuccess(false);
            result.setData("请确认试图新增的用户是公司员工");
        }
        return result;
    }

    /**
     * 查找交易员
     *
     * @return
     */
    @RequestMapping(value = "/getuserorg.html", method = RequestMethod.POST)
    @ResponseBody
    public Result getUserOrg(@RequestParam("orgId")Long orgId,@RequestParam("roleType")String roleType) {
        Result result = new Result();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userIds", getUserIds());
        if (orgId > 0) {
        	paramMap.put("orgId", orgId);
        }
        if(null != roleType && !"".equals(roleType)) {
        	paramMap.put("roleType", roleType);
        }
        List<TreeView> resultList = new ArrayList<TreeView>();
        List<UserOrgDto> userOrgList = userService.queryUserOrg(paramMap);
        for (UserOrgDto item : userOrgList) {
            TreeView treeView = new TreeView();
            treeView.setId(item.getId());
            treeView.setpId(item.getParentId());
            treeView.setName(item.getName());
            resultList.add(treeView);
        }
        //添加置空选项
       // TreeView treeView = new TreeView();
        //treeView.setId(1000);
        //treeView.setName("无");
        //treeView.setpId(0);
        //resultList.add(treeView);

        result.setSuccess(true);
        result.setData(resultList);
        return result;
    }
    
    @RequestMapping("query.html")
    @ResponseBody
    public Result query(Long orgId,boolean forUserOrgConfig) {
        Result result = new Result();
        result.setSuccess(true);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orgId", orgId);
        params.put("forUserOrgConfig", forUserOrgConfig);
        result.setData(userService.queryByParam(params));
        return result;
    }
    
    @RequestMapping("queryById.html")
    @ResponseBody
    public Result queryById(Long id) {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(userService.queryById(id));
        return result;
    }
}
