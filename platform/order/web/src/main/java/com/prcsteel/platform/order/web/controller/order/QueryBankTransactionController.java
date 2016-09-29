package com.prcsteel.platform.order.web.controller.order;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.BankTransactionInfoDto;
import com.prcsteel.platform.order.model.enums.BankTransactionType;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.model.model.RefundRequest;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.RefundRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * Created by Rabbit Mao on 2015/7/20.
 */
@Controller
@RequestMapping("/order/banktransaction/")
public class QueryBankTransactionController extends BaseController {
    private static Logger logger = LogManager.getLogger(QueryBankTransactionController.class);

    @Autowired
    BankTransactionInfoService bankTransactionInfoService;

    @Autowired
    RefundRequestService refundRequestService;

    @Autowired
    QueryOrderController orderController;

    @Autowired
    OrganizationService organizationService;
    @Autowired
    AccountService accountService;

    @RequestMapping("list.html")
    public void list(ModelMap out) {
        orderController.processOutData(Constant.ConsignOrderTab.BANKTRANSACTION.toString(), out);
    }

    @RequestMapping("deal.html")
    public void deal(Long id, ModelMap out) {
        BankTransactionInfo bankTransactionInfo = bankTransactionInfoService.queryById(id);
        out.put("transactionInfo", bankTransactionInfo);

        // 查询部门
        String accountName = bankTransactionInfo.getPayeeName().replaceAll(" ", "");
        Account account = accountService.selectAccountByName(accountName);
        if (account != null) {
            List<DepartmentDto> departmentList = accountService.queryDeptByCompanyId(account.getId());
            if (departmentList != null && departmentList.size() > 0) {
                out.put("departmentList", departmentList);
            }
        }
        orderController.processOutData(Constant.ConsignOrderTab.BANKTRANSACTION.toString(), out);
    }

    @RequestMapping("refund.html")
    public void refund(String serialNumber, ModelMap out) {
        RefundRequest refundRequest = refundRequestService.queryByBankSerialNumber(serialNumber);
        User curUser = getLoginUser();
        Long orgId = curUser.getOrgId();
        if (orgId != null) {
            Organization organization = organizationService.queryById(orgId);
            if (organization != null) {
                Long chargerId = organization.getCharger();
                if (chargerId != null) {
                    User charger = userService.queryById(chargerId);
                    if (charger != null) {
                        out.put("charger", charger.getName());
                    }
                }
            }
        }
        out.put("requester", curUser.getName());
        out.put("refundRequest", refundRequest);
        orderController.processOutData(Constant.ConsignOrderTab.BANKTRANSACTION.toString(), out);
    }

    /**
     * 新注册用户账户并充值
     *
     * @param id
     * @param name
     * @param tel
     * @return
     */
//    @OpLog(OpType.Charge)
//    @OpParam("id")
//    @OpParam("name")
//    @OpParam("tel")
    @RequestMapping("charge.html")
    @ResponseBody
    public Result charge(Long id, String name, String tel, Long departmentId) {
        Result result = new Result();
        result.setSuccess(true);
        try {
            bankTransactionInfoService.charge(id, name.replace(" ", "").replace(" ", ""), tel, departmentId, getLoginUser());
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 线下付款申请单
     *
     * @param id
     * @return
     */
//    @OpLog(OpType.Refund)
//    @OpParam("id")
//    @OpParam("reason")
    @RequestMapping(value = "refundTransaction.html", method = RequestMethod.POST)
    @ResponseBody
    public Result refundTransaction(Long id, String reason) {
        Result result = new Result();

        BankTransactionInfo bankTransactionInfo = bankTransactionInfoService.queryById(id);
        bankTransactionInfo.setStatus(Constant.TRANSACTION_TYPE.REFUND.toString());

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setCreatedBy(getLoginUser().getLoginId());
        refundRequest.setCreated(new Date());
        refundRequest.setModificationNumber(0);
        refundRequest.setLastUpdatedBy(getLoginUser().getLoginId());
        refundRequest.setLastUpdated(new Date());
        refundRequest.setAmountLowerCase(bankTransactionInfo.getTransactionAmount());
        refundRequest.setAmountUpperCase(NumberToCNUtils.number2CNMontrayUnit(bankTransactionInfo.getTransactionAmount()));
        refundRequest.setReason(reason);
        refundRequest.setType(Constant.REFUND_TYPE);
        refundRequest.setRefBankSerialNumber(bankTransactionInfo.getSerialNumber());
        refundRequest.setReceiverName(bankTransactionInfo.getPayeeName());
        refundRequest.setReceiverAccountName(bankTransactionInfo.getPayeeBankName());
        //客户银行账号
        refundRequest.setReceiverAccountCode(bankTransactionInfo.getPayeeAcctountNumber());
        refundRequest.setBankCode(bankTransactionInfo.getBankCode());

        try{
            refundRequestService.insert(refundRequest, bankTransactionInfo);
            result.setSuccess(true);
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 加载所有异常充值数据
     *
     * @param bankTransactionInfo
     * @param stransactionTimeFrom
     * @param stransactionTimeTo
     * @return
     */
    @RequestMapping(value = "loadData.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult loadOrderData(BankTransactionInfo bankTransactionInfo,
                                    String stransactionTimeFrom,
                                    String stransactionTimeTo,
                                    BigDecimal transactionAmount,
                                    String statusToSelect,
                                    Integer start,
                                    Integer length) {
        PageResult result = new PageResult();
        Map<String, Object> param = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(bankTransactionInfo.getPayeeBankName())) {
            param.put("payeeBankName", bankTransactionInfo.getPayeeBankName());
        }
        if (StringUtils.isNotBlank(bankTransactionInfo.getPayeeName())) {
            param.put("payeeName", bankTransactionInfo.getPayeeName());
        }
        if (StringUtils.isNotBlank(bankTransactionInfo.getBankType())) {
            param.put("bankType", bankTransactionInfo.getBankType());
        }
        try {
            if (StringUtils.isNotBlank(stransactionTimeFrom)) {
                param.put("transactionTimeFrom", parseTime(stransactionTimeFrom));
            }
            if (StringUtils.isNotBlank(stransactionTimeTo)) {
                param.put("transactionTimeTo", parseTime(stransactionTimeTo));
            }
        } catch (ParseException e) {
            logger.debug(e.getMessage());
            return null;
        }
        if (transactionAmount != null) {
            param.put("transactionAmount", transactionAmount);
        }
        param.put("status", statusToSelect);

        List<BankTransactionInfoDto> bankTransactionInfoList = bankTransactionInfoService.query(param);
        if (!bankTransactionInfoList.isEmpty()) {
            int total = bankTransactionInfoList.size();
            param.put("start", start);
            param.put("length", length);
            bankTransactionInfoList = bankTransactionInfoService.query(param);

            result.setData(bankTransactionInfoList);
            result.setRecordsFiltered(total);
            result.setRecordsTotal(bankTransactionInfoList.size());
        }
        result.setData(bankTransactionInfoList);
        return result;
    }

    /**
     * string类型转date类型
     * controller直接接收data类型会出错
     *
     * @param time
     * @return
     * @throws ParseException
     */
    private Date parseTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_DAY);
        Date d2 = sdf.parse(time);//将String to Date类型
        return d2;
    }

    /**
     * 疑似支付错误列表页面
     * @param out
     */
    @RequestMapping("payerror.html")
    public void payerror(ModelMap out) {
        out.put("error", "error");
        orderController.processOutData(Constant.ConsignOrderTab.BANKTRANSACTION.toString(), out);
    }

    /**
     * 疑似支付错误处理页面
     * @param id
     * @param out
     */
    @RequestMapping("errordeal.html")
    public void errordeal(Long id, ModelMap out) {
        out.put("transactionInfo", bankTransactionInfoService.queryById(id));
        orderController.processOutData(Constant.ConsignOrderTab.BANKTRANSACTION.toString(), out);
    }

    /**
     * 疑似支付错误立即充值
     * @param id
     */
    @RequestMapping(value = "errorRecharge.html", method = RequestMethod.POST)
    @ResponseBody
    public Result errorRecharge(@RequestParam("id") Long id) {
        Result result = new Result();
        if(bankTransactionInfoService.errorRecharge(id, getLoginUser()) &&
                bankTransactionInfoService.updateStatusById(id, BankTransactionType.CHARGEHAND.getCode(), getLoginUser().getLoginId()) > 0){
            result.setSuccess(true);
            result.setData("立即充值成功");
        }else{
            result.setSuccess(false);
            result.setData("立即充值失败");
        }
        return result;
    }

    /**
     * 疑似支付错误立即处理
     * @param id
     */
    @RequestMapping(value = "errorRefund.html", method = RequestMethod.POST)
    @ResponseBody
    public Result errorRefund(@RequestParam("id") Long id) {
        Result result = new Result();
        if(bankTransactionInfoService.updateStatusById(id, BankTransactionType.CHARGEMAN.getCode(), getLoginUser().getLoginId()) > 0){
            result.setSuccess(true);
            result.setData("立即处理成功");
        }else{
            result.setSuccess(false);
            result.setData("立即处理失败");
        }
        return result;
    }
}
