package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.enums.RiskControlType;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderListDto;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.SaveConsignOrderChangeDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.enums.BankTransactionType;
import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.PayRequestType;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChangedrecord;
import com.prcsteel.platform.order.model.query.PayRequestQuery;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.PickupItemService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderItemsChangedrecordService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 合同变更控制器
 * @author lichaowei
 * @date 2016年8月9日
 */
@Controller
@RequestMapping("/order/changecontract/")
public class ChangeContractController extends BaseController{

    private Logger log = Logger.getLogger(ChangeContractController.class);
    ShiroVelocity permissionLimit = new ShiroVelocity();

    @Resource
    ConsignOrderService consignOrderService;

    @Resource
    AccountService accountService;

    @Resource
    SysSettingService sysSettingService;

    @Resource
    SecondSettlementLogService secondSettlementLogService;
    @Resource
    PayRequestService payRequestService;
    @Resource
    BankTransactionInfoService bankTransactionInfoService;
    @Resource
    ConsignOrderChangeService consignOrderChangeService;
    @Resource
    AcceptDraftService acceptDraftService;
    @Resource
    ConsignOrderItemsService consignOrderItemsService;
    @Resource
    ConsignOrderItemsChangedrecordService itemsChangedrecordService;
    @Resource
    PickupItemService pickupItemService;
    @Resource
    SysSettingDao sysSettingDao;

    final String PERMISSION_BANKTRANSACTION_PAYERROR = "order:banktransaction:payerror:view";    // 到账查询查询疑似支付错误处理
    final String PERMISSION_CONFIRMPAYMENT_BANKCODE = "order:query:bankcode:confirm";    // 客户银行账号审核
    final String PERMISSION_CHANGE = "order:query:change"; // 订单变更
    final String PERMISSION_AUDIT = "order:query:audit"; //审核
    final String PERMISSION_RELATE = "order:query:relate"; // 关联
    final String PERMISSION_PAYAPPLY = "order:query:payapply"; // 申请付款
    final String PERMISSION_PRINT = "order:query:printcontract"; // 打印合同
    final String PERMISSION_CHANGECONTRACT = "order:changecontract:change:secondedit"; // 二结以后修改订单
    /**
     * 合同变更操作页面
     *
     * @param out
     * @param orderId
     */
    @RequestMapping("{orderId}/change.html")
    public String change(ModelMap out, @PathVariable Long orderId) {
        getOrderInfo(out, orderId);
        return "order/changecontract/change";
    }

    /**
     * 保存订单合同变更数据
     *
     * @param saveDto    订单合同变更JSON数据
     * @return 成功返回true，是否返回false
     */
    @OpParam(name = "saveDto", value = "saveChangeDto")
    @ResponseBody
    @RequestMapping(value = "savechange", method = RequestMethod.POST)
    public Result saveChange(SaveConsignOrderChangeDto saveDto) {
        Result result = new Result();
        saveDto.setUser(getLoginUser());
        try {
            consignOrderChangeService.saveOrderChange(saveDto);
            result.setSuccess(true);
        }
        catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 合同变更审核/详情页面
     *
     * @param out
     * @param orderChangeId 合同变更Id
     */
    @RequestMapping("{orderChangeId}/{type}.html")
    public String audit(ModelMap out, @PathVariable Integer orderChangeId,@PathVariable String type) {
        ConsignOrderChange consignOrderChange = consignOrderChangeService.selectByPrimaryKey(orderChangeId);
        if (consignOrderChange != null) {
            getOrderInfo(out, consignOrderChange.getOrderId());
            // 查询变更详情记录
            List<ConsignOrderItemsChange> orderItemsChanges = consignOrderChangeService.queryOrderItemsByChangeOrderId(orderChangeId);
            if(orderItemsChanges!=null){
                Integer totalQuantity = orderItemsChanges.stream().mapToInt(a -> a.getQuantity()).sum();
                Double totalWeight = orderItemsChanges.stream().mapToDouble(a -> a.getWeight().doubleValue()).sum();
                Double totalPurchaseAmount = orderItemsChanges.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum();
                Double totalSaleAmount = orderItemsChanges.stream().mapToDouble(a -> a.getSaleAmount().doubleValue()).sum();
                out.put("totalQuantity", totalQuantity);
                out.put("totalWeight", totalWeight);
                out.put("totalPurchaseAmount", totalPurchaseAmount);
                out.put("totalSaleAmount", totalSaleAmount);
            }
            out.put("orderItemsChanges", orderItemsChanges);
        }
        out.put("orderChange", consignOrderChange);
        out.put("page",type);
        return "order/changecontract/audit";
    }

    /**
     * 审核订单合同变更记录
     *
     * @return 成功返回true，是否返回false
     */
    @OpAction(key="orderChangeId")
    @ResponseBody
    @RequestMapping(value = "submitaudit", method = RequestMethod.POST)
    public Result submitAudit(Integer orderChangeId, Boolean auditStatus, String note) {
        Result result = new Result();
        try {
            consignOrderChangeService.auditChange(orderChangeId, auditStatus, note, getLoginUser());
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 根据订单Id查询变更成功的记录
     * @param orderItemId 订单详情Id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getchangerecord", method = RequestMethod.POST)
    public Result getChangeRecord(Long orderItemId, Integer itemChangeId) {
        Result result = new Result();
        // 查询本次变更的记录
        QueryChangeOrderDto query = new QueryChangeOrderDto();
        query.setItemChangeId(itemChangeId);
        List<ConsignOrderItemsChangedrecord> changedRecords = itemsChangedrecordService.selectByChangedrecord(query);

        // 查询以前的变更成功与原订单记录
        if(orderItemId > 0) {
            query = new QueryChangeOrderDto();
            query.setOrderItemId(orderItemId);
            List<ConsignOrderAlterStatus> statusList = new ArrayList<ConsignOrderAlterStatus>();
            statusList.add(ConsignOrderAlterStatus.CHANGED_SUCCESS1);
            statusList.add(ConsignOrderAlterStatus.CHANGED_SUCCESS2);
            statusList.add(ConsignOrderAlterStatus.CHANGED_SUCCESS3);
            statusList.add(ConsignOrderAlterStatus.ORIGIN_ORDER);
            query.setAlterStatuses(statusList);
            List<ConsignOrderItemsChangedrecord> records = itemsChangedrecordService.selectByChangedrecord(query);
            records.removeIf(
                    a -> changedRecords.stream().anyMatch(b -> {
                        return a.getId().equals(b.getId());
                    })
            );
            changedRecords.addAll(records);
            changedRecords.sort((a, b) -> a.getId().compareTo(b.getId()));
        }

        result.setData(changedRecords);
        return result;
    }

    private void getOrderInfo(ModelMap out, Long orderId){
        // 订单是否二结后 0 否，1 是
        String isSecondCount = "0";
        // 订单信息
        ConsignOrder order = consignOrderService.queryById(orderId);
        if (order != null) {
            order.setFeeTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
            if(order.getOutboundTaker() != null) {
                order.setOutboundTaker(order.getOutboundTaker().equals("seller") ? "卖家" : "买家");
            }
            else{
                order.setOutboundTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
            }
            order.setDeliveryType(order.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
            isSecondCount = (Integer.valueOf(order.getStatus()) > 6) ? "1":"0";//status 大于6是二结后，小于等于6是二结前
            out.put("isSecondCount", isSecondCount);
            // 银票
            List<AcceptDraft> acceptDraftList = acceptDraftService.queryByAccountStatus(order.getAccountId(), AcceptDraftStatus.CHARGED.getCode());
            out.put("acceptDraftList", acceptDraftList);
        }
        out.put("order", order);
        // 订单详情
        List<ConsignOrderItems> orderItems = consignOrderService.queryOrderItemsById(orderId);
        out.put("orderItems", orderItems);
        // 卖家合计
        ConsignOrderItems totalItems = new ConsignOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            int count = 0;
            BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, amount = BigDecimal.ZERO,costAmount = BigDecimal.ZERO, actualPickWeight = BigDecimal.ZERO;
            for (ConsignOrderItems item : orderItems) {
                count += item.getQuantity();
                if (item.getWeight() != null) {
                    weight = weight.add(item.getWeight());
                }
                if (item.getCostPrice() != null) {
                    costprice = costprice.add(item.getCostPrice());
                }
                if (item.getDealPrice() != null) {
                    dealprice = dealprice.add(item.getDealPrice());
                }
                if (item.getAmount() != null) {
                    amount = amount.add(item.getAmount());
                }
                if (item.getCostAmount() != null) {
                    costAmount = costAmount.add(item.getCostAmount());
                }
                if (item.getActualPickWeightServer() != null) {
                    actualPickWeight = actualPickWeight.add(item.getActualPickWeightServer());
                }
            }
            totalItems.setQuantity(count);
            totalItems.setWeight(weight);
            totalItems.setCostPrice(costprice);
            totalItems.setDealPrice(dealprice);
            totalItems.setAmount(amount);
            totalItems.setCostPrice(costprice);
            totalItems.setActualPickWeightServer(actualPickWeight);
            Double totalCostAmount = orderItems.stream().mapToDouble(a -> a.getCostAmount().doubleValue()).sum();
            out.put("totalCostAmount", totalCostAmount);
        }
        out.put("totalItem", totalItems);

        getHint(out, order, orderItems);
    }

    private void getHint(ModelMap out,ConsignOrder order,List<ConsignOrderItems> orderItems){
        Account AccountSellerName= accountService.selectAccountByName(orderItems.get(0).getSellerName());
        if(("4".equals(order.getStatus().trim())&&"APPLY".equals(order.getPayStatus().trim()))){
            List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.APPLY_PAYMENT.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
            out.put("hint", hint.size()==0?null:hint.get(0));
        }
        if("1".equals(order.getStatus().trim())){
            List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.AUDIT_ORDER.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
            out.put("hint", hint.size()==0?null:hint.get(0));
        }
        if(("4".equals(order.getStatus().trim())&&"REQUESTED".equals(order.getPayStatus().trim()))){
            List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PENDING_PAYMENT.getCode())).filter(a -> a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
            out.put("hint", hint.size()==0?null:hint.get(0));
        }
        if(("4".equals(order.getStatus().trim())&&("APPROVED".equals(order.getPayStatus().trim())||"APPLYPRINTED".equals(order.getPayStatus().trim())))){
            List<SysSetting> hint = sysSettingService.queryHint();
            List<SysSetting> printFloat=hint.stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_FLOAT_LAYER.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
            List<SysSetting>printTrans=hint.stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_TRANS_INTERFACE.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());

            out.put("hint", printTrans.size()==0?null:printTrans.get(0));

        }

    }
    /**
     * 合同变更-可变更交易单
     * @param out
     */
    @RequestMapping("changelist.html")
    public void changelist(ModelMap out) {
        out.put("permission_change",permissionLimit.hasPermission(PERMISSION_CHANGE));
        out.put("startTime", Tools.getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
    }

    /*
	 * 处理所有tab页的界面输出数据model
	 *
	 * @author dengxiyan
	 *
	 * @param tab tab页标识
	 *
	 * @param out
	 */
    protected void processOutData(String tab, ModelMap out) {

        // 设置tab页导航数字
        setTabTotal(out);

        // 开单日期:开始时间、结束时间(当前月第一天、当前时间)
        setDefaultTime(out);

        // 请求的TAB页
        out.put("tab", tab);

        // 当前登录人为交易员角色时，前台交易员显示框隐藏
        out.put("hidden",
                permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN));


    }

    public void setTabTotal(ModelMap out) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", getLoginUser().getId());

        // 待审核、待关联、待二次结算、交易完成、交易关闭待审核、交易关闭、待开票、待提货
        List<Map<String, Object>> orderTotalList = consignOrderService
                .getOrderTotalGroupByStatus(paramMap);

        // 待审核
        int approval = getOrderTotal(orderTotalList,
                ConsignOrderStatus.NEW.getCode());

        // 待关联
        int associate = getOrderTotal(orderTotalList,
                ConsignOrderStatus.NEWAPPROVED.getCode());

        // 待二次结算
        int secondSettle = getOrderTotal(orderTotalList,
                ConsignOrderStatus.SECONDSETTLE.getCode());

        // 交易关闭待审核
        int tradeCloseApproval = getOrderTotal(orderTotalList,
                ConsignOrderStatus.CLOSEREQUEST1.getCode(),
                ConsignOrderStatus.CLOSEREQUEST2.getCode(),
                ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode(),
                ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());

        // 待提货：统计已关联合同的订单（(暂不实现：过滤掉提货单图片全部上传的订单)
        int pickup = getOrderTotal(orderTotalList,
                ConsignOrderStatus.RELATED.getCode());

        // 待放货：统计已关联合同且提货单已上传图片的订单(过滤掉放货单已全打印：放货单已全打印即订单状态为待二次结算)
        setStatusParam(paramMap, ConsignOrderStatus.RELATED.getCode());
        paramMap.put("fillUpStatus", ConsignOrderFillUpStatus.INITIAL.getCode());
        int fillUp = consignOrderService.getOrderTotalByStatus(paramMap);

        // 待申请付款、待审核付款 、待确认付款 : 统计已关联合同、待二次结算的订单 付款状态：待申请付款、待审核付款 、待确认付款
        setStatusParam(paramMap, ConsignOrderStatus.RELATED.getCode(),
                ConsignOrderStatus.SECONDSETTLE.getCode());
        List<Map<String, Object>> payOrderTotalList = consignOrderService
                .getPayOrderTotalList(paramMap);

        // 待申请付款
        int applyPayment = getPayOrderTotal(payOrderTotalList,
                Constant.ConsignOrderPayStatus.APPLY.toString());

        // 待审核付款
        int approvalPayment = getPayOrderTotal(payOrderTotalList,
                ConsignOrderPayStatus.REQUESTED.toString());


        //待审核付款
        //二次结算付款申请
        paramMap = new HashMap<String, Object>();
        paramMap.put("userIds", getUserIds());
        paramMap.put("status", PayStatus.REQUESTED.toString());
        int auditSecondsettle = secondSettlementLogService.getPayCounts(paramMap);

        //提现付款申请
        paramMap = new HashMap<String, Object>();
        paramMap.put("userIds", getUserIds());
        paramMap.put("type", PayRequestType.WITHDRAW.getCode());
        paramMap.put("status",PayStatus.REQUESTED.toString());
        Integer auditCashPayment = payRequestService.queryTotal(paramMap);

        //TODO
        //预付款申请
        PayRequestQuery payRequestQuery = new PayRequestQuery();
        payRequestQuery.setStatus(PayStatus.REQUESTED.toString());
        Integer payMentAudit = payRequestService.queryPayMentAuditCount(payRequestQuery);

        //预付款申请待打印
        payRequestQuery = new PayRequestQuery();
        payRequestQuery.setStatus(PayStatus.APPROVED.toString());
        Integer paymentPrint = payRequestService.queryPayMentAuditCount(payRequestQuery);

        // 待打印付款申请
        //订单付款
        int printpendingpayapplyOrder = getPayOrderTotal(payOrderTotalList,
                ConsignOrderPayStatus.APPROVED.toString());
        //二结付款
        paramMap = new HashMap<String, Object>();
        paramMap.put("userId", getLoginUser().getId());
        paramMap.put("userIds", getUserIds());
        String status = PayStatus.APPROVED.toString();// 待打印申请单只查询已审核通过的
        paramMap.put("status", status);
        int printpendingpayapplySecondsettle = secondSettlementLogService.getPayCounts(paramMap);;
        //提现付款
        paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("type", PayRequestType.WITHDRAW.getCode());
        paramMap.put("userIds", getUserIds());
        paramMap.put("userId", getLoginUser().getId());

        int printpendingpayapplyWithdraw = payRequestService.queryTotal(paramMap);

        // 待确认付款
        int confirmPaymentOrder = getPayOrderTotal(payOrderTotalList,
                ConsignOrderPayStatus.APPLYPRINTED.toString());
        //二结付款
        paramMap = new HashMap<String, Object>();
        paramMap.put("userId", getLoginUser().getId());
        paramMap.put("userIds", getUserIds());
        status = PayStatus.APPLYPRINTED.toString();// 待打印申请单只查询已审核通过的
        paramMap.put("status", status);
        int confirmPaymentSecondsettle = secondSettlementLogService.getPayCounts(paramMap);;
        //提现付款
        paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("type", PayRequestType.WITHDRAW.getCode());
        paramMap.put("userIds", getUserIds());
        paramMap.put("userId", getLoginUser().getId());
        int confirmPaymentWithdraw = payRequestService.queryTotal(paramMap);

        //待确认预付款
        payRequestQuery = new PayRequestQuery();
        payRequestQuery.setStatus(PayStatus.APPLYPRINTED.toString());
        Integer payMentConfirm = payRequestService.queryPayMentAuditCount(payRequestQuery);
        //客户银行账号审核
        int confirmPaymentBankCode = 0;
        confirmPaymentBankCode = permissionLimit.hasPermission(PERMISSION_CONFIRMPAYMENT_BANKCODE) ? accountService.selectBankCodeVerifyTotal(null, null, null) : 0;

        // 统计到账异常处理:统计未处理的且未设置黑名单
        int bankTransactionTransfer = bankTransactionInfoService.countByStatus(Constant.TRANSACTION_TYPE.UNPROCESSED.toString(), SysSettingType.Transaction.getCode());
        //到账查询查询疑似支付错误处理
        int payError = permissionLimit.hasPermission(PERMISSION_BANKTRANSACTION_PAYERROR) ? bankTransactionInfoService.countByStatus(BankTransactionType.PENDING.getCode(), SysSettingType.Transaction.getCode()) : 0;
        int bankTransaction = bankTransactionTransfer + payError;

        // 导航TAB的数字
        out.put(Constant.ConsignOrderTab.APPROVAL.toString(),
                processTabNumber(approval));
        out.put(Constant.ConsignOrderTab.BANKTRANSACTION.toString(),
                processTabNumber(bankTransaction));
        out.put(Constant.ConsignOrderTab.BANKTRANSACTION_TRANSFER.toString(),
                processTabNumber(bankTransactionTransfer));
        out.put(Constant.ConsignOrderTab.BANKTRANSACTION_ERROR.toString(),
                processTabNumber(payError));
        out.put(Constant.ConsignOrderTab.ASSOCIATE.toString(),
                processTabNumber(associate));
        out.put(Constant.ConsignOrderTab.APPLYPAYMENT.toString(),
                processTabNumber(applyPayment));
        out.put(Constant.ConsignOrderTab.PAYMENT.toString(),
                processTabNumber(auditSecondsettle + approvalPayment + auditCashPayment + payMentAudit));
        out.put(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY_ORDER.toString(),
                processTabNumber(printpendingpayapplyOrder));
        out.put(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY_SECONDSETTLE.toString(),
                processTabNumber(printpendingpayapplySecondsettle));
        out.put(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY_WITHDRAW.toString(),
                processTabNumber(printpendingpayapplyWithdraw));
        out.put(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY.toString(),
                processTabNumber(printpendingpayapplyOrder+printpendingpayapplySecondsettle+printpendingpayapplyWithdraw+paymentPrint));

        out.put(Constant.ConsignOrderTab.CONFIRMPAYMENT_ORDER.toString(),
                processTabNumber(confirmPaymentOrder));
        out.put(Constant.ConsignOrderTab.CONFIRMPAYMENT_SECONDSETTLE.toString(),
                processTabNumber(confirmPaymentSecondsettle));
        out.put(Constant.ConsignOrderTab.CONFIRMPAYMENT_WITHDRAW.toString(),
                processTabNumber(confirmPaymentWithdraw));
        out.put(Constant.ConsignOrderTab.PAYMENTCONFIRM.toString(),
                processTabNumber(payMentConfirm));//预付款待确认
        out.put(Constant.ConsignOrderTab.ConfirmPaymentBankCode.toString(),
                processTabNumber(confirmPaymentBankCode));//待确认已付款 客户银行账号审核
        out.put(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(),
                processTabNumber(confirmPaymentOrder + confirmPaymentSecondsettle + confirmPaymentWithdraw + payMentConfirm + confirmPaymentBankCode));
        out.put(Constant.ConsignOrderTab.PICKUP.toString(),
                processTabNumber(pickup));
        out.put(Constant.ConsignOrderTab.FILLUP.toString(),
                processTabNumber(fillUp));
        out.put(Constant.ConsignOrderTab.SECONDSETTLEMENT.toString(),
                processTabNumber(secondSettle));

        out.put(Constant.ConsignOrderTab.AUDIT_SECONDSETTLE.toString(), processTabNumber(auditSecondsettle));
        out.put(Constant.ConsignOrderTab.REVIEW_PAYMENT.toString(), processTabNumber(approvalPayment));
        out.put(Constant.ConsignOrderTab.AUDIT_CASH_PAYMENT.toString(), processTabNumber(auditCashPayment));
        out.put(Constant.ConsignOrderTab.PAYMENTADVANCE.toString(), processTabNumber(payMentAudit));
        out.put(Constant.ConsignOrderTab.PAYMENTPRINT.toString(), processTabNumber(paymentPrint));
        out.put(Constant.ConsignOrderTab.TRADECLOSEAPPROVAL.toString(),
                processTabNumber(tradeCloseApproval));
    }

    private int getOrderTotal(List<Map<String, Object>> totalList,
                              String... status) {
        int total = 0;
        if (totalList != null && totalList.size() > 0) {
            for (String item : status) {
                for (Map<String, Object> map : totalList) {
                    if (StringUtils.equals(item, (String) map.get("status"))) {
                        total += ((Long) map.get("total")).intValue();
                        break;
                    }
                }
            }
        }
        return total;
    }

    private int getPayOrderTotal(List<Map<String, Object>> totalList,
                                 String payStatus) {
        if (totalList != null) {
            for (Map<String, Object> map : totalList) {
                if (StringUtils.equals((String) map.get("pay_status"),
                        payStatus)) {
                    return ((Long) map.get("total")).intValue();
                }
            }
        }
        return 0;
    }

    private void setDefaultTime(ModelMap out) {
        // 开单日期:开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    }
    private void setStatusParam(Map<String, Object> paramMap, String... status) {
        List<String> statusList = new ArrayList<String>();
        for (String item : status) {
            statusList.add(item);
        }
        paramMap.put("list", statusList);
    }
    /*
	 * 处理导航条的数字显示：如果大于0显示，否则返回空字符串
	 *
	 * @param number 待处理的数字
	 *
	 * @author dengxiyan
	 *
	 * @return
	 */
    private String processTabNumber(int number) {
        return number > 0 ? "(" + number + ")" : "";
    }

    /**
     * 分页加载表格数据
     *
     * @param query
     *            前端参数封装到dto
     * @return
     * @author wangxianjun
     */
    @RequestMapping(value = "loadOrderData.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult loadOrderData(ChangeOrderDto query) {
        PageResult result = new PageResult();
        // 条件处理
        if (StringUtils.isNotEmpty(query.getStartTime())) {
            query.setBeginTime(Tools.strToDate(query.getStartTime(), Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            query.seteTime(Tools.getAfterDate(Tools.strToDate(query.getEndTime(), Constant.TIME_FORMAT_DAY), 1));
        }
        List<String> statusList = new ArrayList<>();
        //变更成功有三种状态 CHANGED_SUCCESS1变更成功（审核通过） CHANGED_SUCCESS2变更成功（关联成功） CHANGED_SUCCESS3变更成功（已申请付款）
        if("CHANGED_SUCCESS".equals(query.getAlterStatus())){
            statusList.add("CHANGED_SUCCESS1");
            statusList.add("CHANGED_SUCCESS2");
            statusList.add("CHANGED_SUCCESS3");
            query.setAlterStatusList(statusList);
            query.setAlterStatus("");
        }

        Boolean canModify = permissionLimit.hasPermission(PERMISSION_CHANGECONTRACT) ? true
                : false;//二结以后修改订单,如果没有权限，可变更列表不展示二结以后的订单
        query.setCanModify(canModify);
        query.setCode(query.getCode().trim());
        query.setOwnerName(query.getOwnerName().trim());
        int total = consignOrderChangeService.countByConditions(query);
        List<ChangeOrderListDto> changeOrderList = new ArrayList<>();
        if(total > 0){
            changeOrderList = consignOrderChangeService.selectByConditions(query);
        }

        // 返回数据处理

        result.setData(changeOrderList);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(changeOrderList.size());
        return result;
    }

    /**
     * 合同变更操作页面
     *
     * @param out
     * @param id
     */
    @RequestMapping("/changeassociatedetail.html")
    public void changeAssociateDetail(ModelMap out, @RequestParam("id") Integer id) {

        // 变更订单信息
        ConsignOrderChange orderChange = consignOrderChangeService.selectByPrimaryKey(id);
        if (orderChange != null) {
            orderChange.setFeeTaker(orderChange.getFeeTaker().equals("seller") ? "卖家" : "买家");
            if(orderChange.getOutboundTaker() != null) {
                orderChange.setOutboundTaker(orderChange.getOutboundTaker().equals("seller") ? "卖家" : "买家");
            }
            else{
                orderChange.setOutboundTaker(orderChange.getFeeTaker().equals("seller") ? "卖家" : "买家");
            }
            orderChange.setDeliveryType(orderChange.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
        }

        // 原订单信息
        ConsignOrder order = consignOrderService.queryById(orderChange.getOrderId());
        // 原订单资源信息
        List<ConsignOrderItems> orderItems = consignOrderService
                .queryOrderItemsById(orderChange.getOrderId());
        // 变更订单资源信息
        List<ConsignOrderItemsChange> orderChangeItems = consignOrderChangeService.queryOrderItemsExceptStatusByChangeOrderId(orderChange.getId(), ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode());

        out.put("orderId", orderChange.getOrderId());
        out.put("order", orderChange);
        out.put("orderItems", orderChangeItems);

        out.put("totalItem", consignOrderChangeService.getChangeSellerTotalItems(orderChangeItems));
        out.put("originalOrder", order);
        out.put("originalOrderItems", orderItems);
        out.put("originalTotalItem", consignOrderChangeService.getSellerTotalItems(orderItems));
        out.put("account", accountService.queryById(order.getDepartmentId()));
        //部门可用信用额度
        out.put("balanceCreditAmount", consignOrderService.calculateBalanceCreditAmountOfDeptByDeptId(order.getDepartmentId()));

        SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
        out.put("buyerToleranceAmount", sysSetting != null ? sysSetting.getSettingValue() : 0);

    }




    /**
     * 变更合同关联
     * @param orderChangeId
     */
    @OpAction(key="orderChangeId")
    @RequestMapping("changeAssociateOrder.html")
    @ResponseBody
    public Result changeAssociateOrder(@RequestParam("orderChangeId") Integer orderChangeId,
                                       @RequestParam("totalAmount") BigDecimal totalAmount,
                                       @RequestParam("totalRelatedAmount") BigDecimal totalRelatedAmount,
                                       @RequestParam("secondbalancetakeout") Boolean secondBalanceTakeout,
                                       @RequestParam("orderItemsList") String orderItemsChangeList,
                                       @RequestParam("creditbalancetakeout") Boolean creditBalanceTakeout) {
        Result result = new Result();
        if (!permissionLimit.hasPermission(PERMISSION_RELATE)) {
            result.setData("您没有作该操作的权限");
            result.setSuccess(false);
            return result;
        }
        User user = getLoginUser();
        ResultDto resultDto = consignOrderChangeService.relateOrder(orderChangeId, totalAmount, totalRelatedAmount, secondBalanceTakeout, user, orderItemsChangeList, creditBalanceTakeout);

        result.setSuccess(resultDto.isSuccess());
        result.setData(resultDto.getMessage());
        return result;

    }

    /**
     * 合同变更-变更待审核交易单
     * @param out
     */
    @RequestMapping("auditlist.html")
    public void auditlist(ModelMap out) {
        out.put("permission_audit",permissionLimit.hasPermission(PERMISSION_AUDIT));
        out.put("startTime", Tools.getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
    }
    /**
     * 合同变更-变更待关联交易单
     * @param out
     */
    @RequestMapping("relatelist.html")
    public void relatelist(ModelMap out) {
        out.put("permission_relate",permissionLimit.hasPermission(PERMISSION_RELATE));
        out.put("permission_print",permissionLimit.hasPermission(PERMISSION_PRINT));
        out.put("startTime", Tools.getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
    }
    /**
     * 合同变更-变更待申请付款交易单
     * @param out
     */
    @RequestMapping("payapplylist.html")
    public void payapplylist(ModelMap out) {
        out.put("permission_payapply",permissionLimit.hasPermission(PERMISSION_PAYAPPLY));
        out.put("permission_print",permissionLimit.hasPermission(PERMISSION_PRINT));
        out.put("startTime", Tools.getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
    }
    /**
     * 查询变更的交易单
     *
     * @param query
     *            前端参数封装到dto
     * @return
     * @author wangxianjun
     */
    @RequestMapping(value = "queryChangeOrderData.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult queryChangeOrderData(ChangeOrderDto query) {
        PageResult result = new PageResult();
        // 条件处理
        if (StringUtils.isNotEmpty(query.getStartTime())) {
            query.setBeginTime(Tools.strToDate(query.getStartTime(), Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(query.getEndTime())) {
            query.seteTime(Tools.getAfterDate(Tools.strToDate(query.getEndTime(), Constant.TIME_FORMAT_DAY), 1));
        }
        if (StringUtils.isNotEmpty(query.getAlterStatus())) {
            query.setAlterStatusList(Arrays.asList(query.getAlterStatus().split(",")));
        }
        query.setCode(query.getCode().trim());
        query.setOwnerName(query.getOwnerName().trim());
        int total = consignOrderChangeService.countChangeOrderByConditions(query);
        List<ChangeOrderListDto> changeOrderList = new ArrayList<>();
        if(total > 0){
            changeOrderList = consignOrderChangeService.selectChangeOrderByConditions(query);
        }

        // 返回数据处理

        result.setData(changeOrderList);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(changeOrderList.size());
        return result;
    }
    
    /**
     * 关联时变更合同关闭
     * @param orderChangeId
     */
    @OpAction(key="orderChangeId")
    @RequestMapping("closeAssociateOrder.html")
    @ResponseBody
    public Result closeAssociateOrder(@RequestParam("orderChangeId") Integer orderChangeId, @RequestParam("cause") String cause) {
        Result result = new Result();
        User user = getLoginUser();
        ResultDto resultDto = consignOrderChangeService.closeAssociateOrder(orderChangeId, cause, user);

        result.setSuccess(resultDto.isSuccess());
        result.setData(resultDto.getMessage());
        return result;

    }
   
    /**
     * 待申请付款详情（合同变更付款）
     * @author lixiang
     * @param out
     */
	@RequestMapping("{orderChangeId}/payapplydetail.html")
    public String payapplydetail(ModelMap out,  @PathVariable Integer orderChangeId) {
        //订单变更信息
    	ConsignOrderChange orderChange = consignOrderChangeService.selectByPrimaryKey(orderChangeId);
    	if (orderChange != null) {
    		// 订单信息
        	ConsignOrder order = consignOrderService.queryById(orderChange.getOrderId());
			order.setFeeTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
			if(order.getOutboundTaker() != null) {
				order.setOutboundTaker(order.getOutboundTaker().equals("seller") ? "卖家" : "买家");
			}
			else{
				order.setOutboundTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
			}
			order.setDeliveryType(order.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
			//订单是否二结后 0 否，1 是
			String isSecondCount = (Integer.valueOf(order.getStatus()) > 6) ? "1":"0";//status 大于6是二结后，小于等于6是二结前
			out.put("isSecondCount", isSecondCount);
        	//资源信息
        	List<ConsignOrderItemsChange> orderItemsChange = consignOrderChangeService.queryOrderItemsByChangeOrderId(orderChangeId);
        	//付款信息
        	List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderChangeService.getchangeSellerInfo(order.getId(), orderChangeId);
        	
        	out.put("sellerInfoList", sellerInfoList);
        	out.put("order", order);
    		out.put("orderChange", orderChange);
    		out.put("orderItemsChange", orderItemsChange);
    		
    		Account AccountSellerName= accountService.selectAccountByName(orderItemsChange.get(0).getSellerName());
            out.put("sellerLabel",AccountSellerName.getSupplierLabel());// add by wangxianjun ,把客户标示传到页面
            out.put("settingValue",sysSettingDao.querySettingValueByName(AccountSellerName.getSupplierLabel()));
            List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.APPLY_PAYMENT.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
			out.put("hint", hint.size()==0?null:hint.get(0));
        	
        	// 卖家合计
        	ConsignOrderItemsChange totalItems = new ConsignOrderItemsChange();
			if (orderItemsChange != null && orderItemsChange.size() > 0) {
				int count = 0;
				BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, purchaseAmount = BigDecimal.ZERO, saleAmount = BigDecimal.ZERO;
				for (ConsignOrderItemsChange item : orderItemsChange) {
					count += item.getQuantity();
					//重量
					if (item.getWeight() != null) {
						weight = weight.add(item.getWeight());
					}
					//销售价
					if (item.getCostPrice() != null) {
						costprice = costprice.add(item.getCostPrice());
					}
					//采购价
					if (item.getDealPrice() != null) {
						dealprice = dealprice.add(item.getDealPrice());
					}
					//销售金额
					if (item.getPurchaseAmount() != null) {
						purchaseAmount = purchaseAmount.add(item.getPurchaseAmount());
					}
					//采购金额
					if (item.getSaleAmount() != null) {
						saleAmount = saleAmount.add(item.getSaleAmount());
					}
				}
				totalItems.setQuantity(count);
				totalItems.setWeight(weight);
				totalItems.setCostPrice(costprice);
				totalItems.setDealPrice(dealprice);
				totalItems.setPurchaseAmount(purchaseAmount);
				totalItems.setSaleAmount(saleAmount);
				out.put("totalItem", totalItems);
			}
    	}
    	return "order/changecontract/payapplydetail";
	}
	
	/**
	 * 变更订单申请付款
	 * @author lixiang
	 * @param orderChangId
	 * @param jsonBalanceList
	 * @return
	 */
	@OpAction(key="orderChangeId")
	@RequestMapping("applypay.html")
	@ResponseBody
	public Result applyPay(@RequestParam("orderChangId") Integer orderChangId, @RequestParam("balancelist") String jsonBalanceList) {
		Result result = new Result();
		
		User user = getLoginUser();
		Map<Long, Double> moneyMap = new HashMap<Long, Double>();
		Map<Long, Long> bankMap = new HashMap<Long, Long>();
		Map<Long, Boolean> checkedMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> refundCredit = new HashMap<Long, Boolean>();
		Boolean settleChecked = false;//抵扣二结欠款
		Boolean creditChecked = false;//还款信用额度

		ObjectMapper mapper = new ObjectMapper();
		JsonNode balanceNodes = null;
		try {
			balanceNodes = mapper.readTree(jsonBalanceList);
		} catch (IOException ex) {
		} catch (Exception e) {
		}
		if (balanceNodes != null) {
			for (JsonNode node : balanceNodes) {
				Long sellerId = node.path("sellerid").asLong();
				moneyMap.put(sellerId, node.path("balance").asDouble());
				bankMap.put(sellerId, node.path("bankaccountid").asLong());
				checkedMap.put(sellerId, node.path("secondChecked").asBoolean());
				refundCredit.put(sellerId, node.path("refundChecked").asBoolean());
				settleChecked = node.path("secondChecked").asBoolean();
				creditChecked = node.path("secondChecked").asBoolean();
			}
		}
		try{
			consignOrderChangeService.applyPay(orderChangId, bankMap, moneyMap, checkedMap, refundCredit, user, settleChecked, creditChecked);
			result.setSuccess(true);
			result.setData("申请成功！");
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		
		return result;
	}
	
	/**
	 * 订单关闭
	 * @author lixiang 
	 * @param orderChangeId
	 * @param cause  订单关闭原因
	 * @return
	 */
	@OpAction(key="orderChangeId")
	@RequestMapping("orderchangeclose.html")
	@ResponseBody
	public Result orderChangeClose( @RequestParam("orderChangeId") Integer orderChangeId, @RequestParam("cause")String cause) {
		Result result = new Result();
		User user = getLoginUser();
		try{
			consignOrderChangeService.closedOrderChange(orderChangeId, cause, user);
			result.setSuccess(true);
			result.setData("关闭成功！");
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
    /**
     * 查询订单付款状态
     * @author wangxianjun
     * @param orderId
     * @param
     * @return
     */

    @RequestMapping("queryorderpaystatus.html")
    @ResponseBody
    public Result queryOrderPayStatus( @RequestParam("orderId") Long orderId,@RequestParam(value = "origin",required = false) String origin) {
        Result result = new Result();
        String payStatus = "";
        try {
            ConsignOrder order = consignOrderService.selectByPrimaryKey(orderId);
            if("origin".equals(origin))
                payStatus = order.getAlterStatus();
            else
                payStatus = order.getPayStatus();
            result.setSuccess(true);
            result.setData(payStatus);
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(payStatus);
        }

        return result;
    }

    /**
     * 变更记录交易详情页面
     *
     * @param out
     * @param orderId
     */
    @RequestMapping("{orderId}/changeddetail.html")
    public String changeddetail(ModelMap out, @PathVariable Long orderId) {
        ConsignOrder consignOrder = consignOrderService.selectByPrimaryKey(orderId);
        if (consignOrder != null) {
            // 原订单资源信息
            List<ConsignOrderItems> orderItems = consignOrderItemsService
                    .selectUnionAllByOrderId(consignOrder.getId());

            if(orderItems!=null){
                Integer totalQuantity = orderItems.stream().mapToInt(a -> a.getQuantity() == null ? 0 : a.getQuantity()).sum();
                Double totalWeight = orderItems.stream().mapToDouble(a -> a.getWeight() == null ?  0: a.getWeight().doubleValue()).sum();
                Double totalPurchaseAmount = orderItems.stream().mapToDouble(a -> a.getWeight() == null ? 0 : a.getCostAmount().doubleValue()).sum();
                Double totalSaleAmount = orderItems.stream().mapToDouble(a -> a.getAmount() == null ? 0 : a.getAmount().doubleValue()).sum();
                out.put("totalQuantity", totalQuantity);
                out.put("totalWeight", totalWeight);
                out.put("totalPurchaseAmount", totalPurchaseAmount);
                out.put("totalSaleAmount", totalSaleAmount);
            }
            out.put("orderItems", orderItems);
        }
        out.put("order", consignOrder);
        return "order/changecontract/changeddetail";
    }

    /**
     * 根据订单Id查询变更成功的记录
     * @param orderItemId 订单详情Id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getchangerecorddetail", method = RequestMethod.POST)
    public Result getChangeRecordDetail(Long orderItemId) {
        Result result = new Result();
        QueryChangeOrderDto query = new QueryChangeOrderDto();
        query.setOrderItemId(orderItemId);
        query.setAlterStatuses(Arrays.asList(ConsignOrderAlterStatus.CHANGED_SUCCESS1,
                ConsignOrderAlterStatus.CHANGED_SUCCESS2,
                ConsignOrderAlterStatus.CHANGED_SUCCESS3,
                ConsignOrderAlterStatus.DEL,
                ConsignOrderAlterStatus.ORIGIN_ORDER));
        List<ConsignOrderItemsChangedrecord> changedRecords = itemsChangedrecordService.selectByChangedrecord(query);
        result.setData(changedRecords);
        return result;
    }
    
    /**
     * 根据订单明细Id查询已提数量
     * @author lixiang
     * @param orderId 订单详情Id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getPickedQuantity", method = RequestMethod.POST)
    public Result getPickedQuantity(Long orderItemId) {
    	Result result = new Result();
    	int pickupNum = pickupItemService.selectPickedQtyByOrderItemId(orderItemId);
    	if (pickupNum > 0) {
    		result.setSuccess(false);
    		result.setData("该条资源详情已录入提货数量，不能删除！");
    	} else {
    		result.setSuccess(true);
    	}
		return result;
    }
}

