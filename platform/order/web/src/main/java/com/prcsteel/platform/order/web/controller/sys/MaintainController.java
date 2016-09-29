package com.prcsteel.platform.order.web.controller.sys;

import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ErrAccountEditService;
import com.prcsteel.platform.common.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 修改错误客户名称
 * Created by Green.Ge on 2015/12/25.
 */

@Controller
@RequestMapping("/sys/maintain/")
public class MaintainController extends BaseController {

    @Resource
    AccountService accountService;
    @Resource
    ErrAccountEditService errAccountEditService;
    //custAccount
    @RequestMapping("erraccountedit")
    public void index() {
    }
    @RequestMapping("erredit.html")
    @ResponseBody
    public Result erredit(@RequestParam("errName") String errName,
                          @RequestParam("correctName") String correctName){
        Result result=new Result();
        if (accountService.selectAccountByName(correctName)!=null){
            result.setData("该客户【" + correctName + "】已经存在！");
            return result;
        }else {
            if (errAccountEditService.updateAccountName(errName,correctName)>0){
                result.setData("修改成功！");
                return result;
            }else {
                result.setData("修改失败！");
                return result;
            }
        }
    }


}
