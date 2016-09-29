package com.prcsteel.platform.order.web.controller.cust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountTransLogDto;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;

/**
 * Created by kongbinheng on 2015/7/16.
 */
@Controller
@RequestMapping("/account/")
public class AccountTransLogController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountTransLogController.class);

    private static final int AMOUNT_LIMIT = 2;

    @Autowired
    AccountService accountService;
    @Autowired
    AccountTransLogService accountTransLogService;
    @Autowired
    AccountBankService accountBankService;
    @Autowired
    PayRequestService payRequestService;

    @ResponseBody
    @RequestMapping(value = "transloginfo", method = {RequestMethod.POST})
    public PageResult transLogInfo(@RequestParam("accountId") Long accountId,
                                   @RequestParam("consignOrderCode") String consignOrderCode,
                                   @RequestParam("applyType") String applyType,
                                   @RequestParam("startTime") String startTime,
                                   @RequestParam("endTime") String endTime,
                                   @RequestParam("start") Integer start,
                                   @RequestParam("length") Integer length) {
        PageResult result = new PageResult();

        List<AccountTransLogDto> list = accountTransLogService.queryTransLogByAccountId(accountId, consignOrderCode, applyType, startTime, endTime, start, length);
        Integer total = accountTransLogService.totalTransLog(accountId, consignOrderCode, applyType, startTime, endTime);

        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());

        return result;
    }

    @RequestMapping("buyer/{accountId}/translog")
    public String buyerTransLog(ModelMap out, @PathVariable Long accountId) {
        AccountDto account = accountService.selectByPrimaryKey(accountId);
        account.getAccount().setType(Constant.ACCOUNT_TYPE.BUYER.toString());
        out.put("account", account);
        out.put("applyTypes", ApplyType.values());
        out.put("type", AccountType.buyer.toString());  //买家
        return "account/translog";
    }

    @RequestMapping("seller/{accountId}/translog")
    public String sellerTransLog(ModelMap out, @PathVariable Long accountId) {
        AccountDto account = accountService.selectByPrimaryKey(accountId);
        out.put("account", account);
        out.put("applyTypes", ApplyType.values());
        out.put("type", AccountType.seller.toString());  //卖家
        return "account/translog";
    }

    @RequestMapping("translog/exportexcel.html")
    @ResponseBody
    public ModelAndView exportExcel(@RequestParam(value = "accountId", required = false) String accountId,
                                    @RequestParam(value = "consignOrderCode", required = false) String consignOrderCode,
                                    @RequestParam(value = "applyType", required = false) String applyType,
                                    @RequestParam(value = "startTime", required = false) String startTime,
                                    @RequestParam(value = "endTime", required = false) String endTime) {
        ModelAndView mv = null;
        try {
            List<AccountTransLogDto> list = accountTransLogService.queryTransLogByAccountId(Long.parseLong(accountId), consignOrderCode, applyType, startTime, endTime, null, null);
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("流水时间");
            titles.add("关联类型");
            titles.add("关联单号");
            titles.add("类型");
            titles.add("资金账户发生额(元)");
            titles.add("资金账户余额(元)");
            titles.add("二次结算账户发生额(元)");
            titles.add("二次结算账户余额(元)");
            titles.add("操作员");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            PageData vpd = null;
            for (AccountTransLogDto transLogDto : list) {
                vpd = new PageData();
                vpd.put("var1", Tools.dateToStr(transLogDto.getSerialTime(), "yyyy-MM-dd HH:mm:ss"));//流水时间
                vpd.put("var2", AssociationType.getNameByCode(transLogDto.getAssociationType()));//关联类型
                vpd.put("var3", transLogDto.getConsignOrderCode());//关联单号
                vpd.put("var4", ApplyType.getNameByCode(transLogDto.getApplyType()));//类型
                vpd.put("var5", Tools.formatBigDecimal(transLogDto.getCashHappenBalance(), AMOUNT_LIMIT));//资金账户发生额
                vpd.put("var6", Tools.formatBigDecimal(transLogDto.getCashCurrentBalance(), AMOUNT_LIMIT));//资金账户余额
                vpd.put("var7", Tools.formatBigDecimal(transLogDto.getAmount(), AMOUNT_LIMIT));//二次结算账户发生额
                vpd.put("var8", Tools.formatBigDecimal(transLogDto.getCurrentBalance(), AMOUNT_LIMIT));//二次结算账户余额
                vpd.put("var9", transLogDto.getApplyerName());//操作员
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();//执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            logger.debug("账户信息导出EXCEL出错：", e);
        }
        return mv;
    }
}
 