package com.prcsteel.platform.order.web.controller.cust;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatusForSearch;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: AccountOrderRecordController
 * @Package com.prcsteel.cbms.web.controller.cust
 * @Description: 客户采购记录、销售记录
 * @date 2015/8/1
 */
@Controller
@RequestMapping("/account/")
public class AccountOrderRecordController extends BaseController {
    @Autowired
    AccountService accountService;

    @Autowired
    ConsignOrderService consignOrderService;

    /**
     * 进入采购记录界面
     *
     * @param accountId 买家客户id
     * @param out
     * @return
     */
    @RequestMapping("buyer/{accountId}/purchaselist.html")
    public String purchaseList(@PathVariable Long accountId, ModelMap out) {
        AccountDto account = accountService.selectByPrimaryKey(accountId);
        out.put("account", account);
        out.put("type", AccountType.buyer.toString());  //买家
        account.getAccount().setType(Constant.ACCOUNT_TYPE.BUYER.toString());    // 卖家也是买家

        // 界面默认显示的开单时间
        out.put("startTime", Tools.dateToStr(getFirstDateOfMonth(), Constant.TIME_FORMAT_DAY.toString()));
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY.toString()));

        //订单状态列表
        out.put("statusList", ConsignOrderStatusForSearch.getOrderStatusList(AccountType.buyer));
        return "account/buyer/purchaselist";
    }

    @RequestMapping("loadPurchaseData.html")
    public
    @ResponseBody
    PageResult loadPurchaseData(ConsignOrderDto dto, @RequestParam("start") Integer start,
                                @RequestParam("length") Integer length, @RequestParam("selectedStatus") String selectedStatus) {

        //参数处理
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dto", dto);
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("userId", getLoginUser().getId());
        buildParam(selectedStatus, paramMap);

        //返回结果
        List<ConsignOrderDto> list = consignOrderService.selectPurchaseListByConditions(paramMap);
        int total = consignOrderService.totalPurchaseByConditions(paramMap);
        return new PageResult(list.size(),total,list);
    }

    /**
     * 进入销售记录界面
     *
     * @param accountId 卖家客户id
     * @param out
     * @return
     */
    @RequestMapping("seller/{accountId}/salelist.html")
    public String saleList(@PathVariable Long accountId, ModelMap out) {
        out.put("account", accountService.selectByPrimaryKey(accountId));
        out.put("type", AccountType.seller.toString());  //卖家
        // 界面默认显示的开单时间
        out.put("startTime", Tools.dateToStr(getFirstDateOfMonth(), Constant.TIME_FORMAT_DAY.toString()));
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY.toString()));

        //订单状态列表
        out.put("statusList", ConsignOrderStatusForSearch.getOrderStatusList(AccountType.seller));
        return "account/seller/salelist";
    }

    @RequestMapping("loadSaleData.html")
    public
    @ResponseBody
    PageResult loadSaleData(ConsignOrderDto dto, @RequestParam("start") Integer start,
                            @RequestParam("length") Integer length, @RequestParam("selectedStatus") String selectedStatus) {
        //参数处理
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dto", dto);
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("userId", getLoginUser().getId());
        buildParam(selectedStatus, paramMap);

        //返回结果
        List<ConsignOrderDto> list = consignOrderService.selectSaleListByConditions(paramMap);
        int total = consignOrderService.totalSaleByConditions(paramMap);
        return new PageResult(list.size(),total,list);
    }

    /*
     * 获得当前月的第一天
     * @return
     */
    private Date getFirstDateOfMonth() {
        // 开单日期:开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private void buildParam(String selectedStatus, Map<String, Object> paramMap) {
        List<String> s = new LinkedList<>();
        if ((s = ConsignOrderStatus.getStatusByCode(selectedStatus)) != null) {
            paramMap.put("status", s);
        }
        if (selectedStatus.equals(ConsignOrderStatusForSearch.TOBEPAYREQUEST.getCode())) {
            paramMap.put("pay_status", Arrays.asList(ConsignOrderPayStatus.APPLY.toString()));
        } else if (selectedStatus.equals(ConsignOrderStatusForSearch.PAYREQUEST.getCode())) {
            paramMap.put("pay_status", Arrays.asList(ConsignOrderPayStatus.REQUESTED.toString()));
        }else if(selectedStatus.equals(ConsignOrderStatusForSearch.TOBEPRINTPAYREQUEST.getCode())){
            paramMap.put("pay_status", Arrays.asList(ConsignOrderPayStatus.APPROVED.toString()));//已通过审核待打印付款申请
        } else if (selectedStatus.equals(ConsignOrderStatusForSearch.COMFIRMEDPAY.getCode())) {
            paramMap.put("pay_status", Arrays.asList(ConsignOrderPayStatus.APPLYPRINTED.toString()));//已打印付款申请单 待确认付款
        } else if (selectedStatus.equals(ConsignOrderStatusForSearch.TOBEDELINERY.getCode())) {
            paramMap.put("fillup_status", Arrays.asList(ConsignOrderFillUpStatus.ALL_MATCHES.getCode(), ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode(), ConsignOrderFillUpStatus.PRINT_ALL.getCode()));
        }
    }
}
