package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
/**
 * @author Rabbit
 * @version v2.0_account
 * @Description: 付款申请Api
 * @date 2016/2/18
 */
@RestController
@RequestMapping("/api/payrequest/")
public class RestPayRequestController extends BaseController {
    @Resource
    private PayRequestService payRequestService;
    /**
     * 保存提现申请
     */
    @RequestMapping(value = "saveapply", method = RequestMethod.POST)
    public Result saveApply(@RequestParam("departmentId") Long departmentId, @RequestParam("bankId") Long bankId,
                     @RequestParam("money") Double money, @RequestParam("balance") Double balance,
                            @RequestParam("userId") Long userId) {
        Result result = new Result();
//        User user = getLoginUser();
        try {
            payRequestService.applyWithdraw(userService.queryById(userId), departmentId, bankId, BigDecimal.valueOf(money), BigDecimal.valueOf(balance));
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
            return result;
        }
        return result;
    }
}
