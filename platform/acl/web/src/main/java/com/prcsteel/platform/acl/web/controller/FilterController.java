package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.query.SysSettingQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by Rabbit Mao on 2015/8/6.
 */
@Controller
@RequestMapping("/sys/")
public class FilterController extends BaseController {
    @Autowired
    SysSettingService sysSettingService;

    @RequestMapping("filterClient.html")
    public void filter(){
    }

    @RequestMapping("loadFilter.html")
    @ResponseBody
    public Result load(){
        Result result = new Result();
        SysSettingQuery param = new SysSettingQuery();
        param.setType(SysSettingType.Transaction.getCode());
        result.setData(sysSettingService.selectByParam(param));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("deleteFilter.html")
    @ResponseBody
    public Result delete(Long id){
        Result result = new Result();

        result.setSuccess(sysSettingService.delete(id));
        return result;
    }

    @RequestMapping("addFilter.html")
    @ResponseBody
    public Result add(String settingValue){
        Result result = new Result();

        SysSetting setting = new SysSetting();
        setting.setSettingValue(settingValue);
        setting.setSettingType(SysSettingType.Transaction.getCode());
        int returnValue = sysSettingService.add(setting, getLoginUser());
        if(returnValue == 1){
            result.setSuccess(true);
        }else if(returnValue == 0){
            result.setSuccess(false);
            result.setData("不能插入空数据");
        }else if(returnValue == -1){
            result.setSuccess(false);
            result.setData("该公司已经在黑名单里");
        }else if(returnValue == -2){
            result.setSuccess(false);
            result.setData("加入黑名单失败，请重试");
        }
        return result;
    }
}
