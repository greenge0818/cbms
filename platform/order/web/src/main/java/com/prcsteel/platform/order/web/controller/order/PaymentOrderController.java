package com.prcsteel.platform.order.web.controller.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.prcsteel.platform.order.model.enums.*;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataReminded;
import com.prcsteel.platform.account.model.enums.AccountPaymentLabel;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.enums.RiskControlType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.PaymentOrderDto;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.PaymentOrderService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * Created by lixiang on 2015/7/20.
 */
@Controller
@RequestMapping("/account/")
public class PaymentOrderController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PaymentOrderController.class);

	@Resource
	private PaymentOrderService paymentOrderService;

	@Resource
	private ConsignOrderItemsService consignOrderItemsService;

	@Resource
	private AccountService accountService;

	@Resource
	private ConsignOrderService consignOrderService;
	
	@Resource
	private BankTransactionInfoService bankTransactionInfoService;
	
	@Resource

	PayRequestService payRequestService;
	@Resource
	ConsignOrderChangeService consignOrderChangeService;//合同变更

	@Resource
	SysSettingService sysSettingService;

	private ShiroVelocity permissionLimit = new ShiroVelocity();

	final String PERMISSION_PAYBILLPRINT = "order:query:confirmpayment:paybillprint"; // 审核关闭

	@RequestMapping("payment/paymentrequest.html")
	public void index(ModelMap out, @RequestParam("id") Long id,
			@RequestParam(value = "print", required = false) String print,
			@RequestParam(value = "requestId", required = false) Long requestId) {
		boolean showRemindedFlag = false;//客户银行账号信息修改提醒默认不提醒
		boolean paymentLabel = false;  //客户预付款标识
		// 获取当前登录用户
		User user = getLoginUser();
		String userName = user.getName();
		List<PaymentOrderDto> list = null;
		String type = "";//5是合同变更申请付款
		if (requestId != null) {
			PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
			type =payRequest.getType();
			//合同变更
			if(PayRequestType.ORDER_CHANGE.getCode().equals(type)){
				list = paymentOrderService.selectChangeRequestByOrderId(id);
			}else {
				list = paymentOrderService.findByOrder(payRequest.getConsignOrderId());
			}

		} else {
			//合同变更流程
			ConsignOrder order = consignOrderService.selectByPrimaryKey(id);
			if(ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getAlterStatus())
					|| ConsignOrderAlterStatus.PENDING_PRINT_PAY.getCode().equals(order.getAlterStatus())
					|| ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(order.getAlterStatus())){
				list = paymentOrderService.selectChangeRequestByOrderId(id);
				type = PayRequestType.ORDER_CHANGE.getCode();
			}else {
				list = paymentOrderService.findByOrder(id);
			}
		}
		if (list == null || list.size() == 0) {
			out.put("error", "该订单已被打回或不存在！");
			logger.error("data is not integrity consignOrderStatusDto:{},organization:{},requestId:{},orgId:{}");
			return;
		}
		Account AccountSellerName= accountService.selectAccountByName(list.get(0).getReceiverName());
		List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_FLOAT_LAYER.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
		out.put("hint", hint.size()==0?null:hint.get(0));

		BigDecimal contractAmount = new BigDecimal(0.00);
		//合同变更
		if(PayRequestType.ORDER_CHANGE.getCode().equals(type)){
			contractAmount = consignOrderChangeService.countChangeContractAmountById(id);// 变更后合同金额
		}else{
			contractAmount = consignOrderItemsService.countContractAmountByOrderId(id); // 合同金额
		}


		List<Long> departmentIds = list.stream().map(a -> a.getReceiverDepartmentId()).collect(Collectors.toList());
		if (departmentIds.size() > 0) {
			List<AccountDto> accountList = accountService.queryForBalance(departmentIds);
			out.put("accountList", accountList);
		}

		String changerName = paymentOrderService.getChangerName(list.get(0)
				.getOrgId());
		String totalAmountBoo = NumberToCNUtils.number2CNMontrayUnit(list
				.get(0).getTotalAmount());// 大写金额
		PaymentOrderDto paymentOrderDto = list.get(0);
		//查询买家是否使用过银票充值支付
		Account account = paymentOrderService.selectByPrimaryKey(paymentOrderDto.getBuyerId());
		if (account.getIsAcceptDraftCharged() == 1) {//如果使用了银票支付
			out.put("isAcceptDraftCharged",
					account.getIsAcceptDraftCharged());
			List<String> draftList = null;
			//合同变更
			if(PayRequestType.ORDER_CHANGE.getCode().equals(type)){
				draftList = consignOrderChangeService.selectChangeAcceptDraftCodeById(id);
			}else{
				draftList =consignOrderItemsService.selectAcceptDraftCodeByOrderId(id);
			}
			List<String> codeList = draftList.stream().filter(a -> !StringUtils.isBlank(a)).collect(Collectors.toList());
			if (codeList != null && codeList.size() != 0) {
				out.put("acceptDraftCodeList", codeList);
			}
		}
		String bankType = bankTransactionInfoService
				.selectByAccountName(paymentOrderDto.getBuyerName());
		if (bankType != null) {
			String bankTypeName = BankType.getName(bankType);
			out.put("bankTypeName", bankTypeName);
		}
		//若卖家列表银行账号信息有修改则提醒
		if (list.stream().filter(a -> (AccountBankDataReminded.Yes.getCode().equals(accountService.selectByPrimaryKey(a.getReceiverId()).getAccount().getBankDataReminded()))).collect(Collectors.toList()).size() > 0)
			showRemindedFlag = true;
		//如果客户预付款标识是银票预付
		if (list.stream().filter(a -> (AccountPaymentLabel.DraftsInAdvance.getCode().equals(accountService.selectByPrimaryKey(a.getReceiverId()).getAccount().getPaymentLabel().toString()))).collect(Collectors.toList()).size() > 0)
			paymentLabel = true;
		out.put("list", list);
		out.put("showRemindedFlag", showRemindedFlag);
		out.put("totalAmountBoo", totalAmountBoo);
		out.put("changerName", changerName);
		out.put("paymentOrderDto", paymentOrderDto);
		out.put("paymentLabel", paymentLabel);  //客户预付款标识

		out.put("contractAmount", contractAmount);
		out.put("print", print);
		out.put("userName", userName);
		out.put("orderid", id);
		if (permissionLimit.hasPermission(PERMISSION_PAYBILLPRINT)) {
			out.put("closeorder", true);
		}
	}

	@RequestMapping("payment/updateprintcounts.html")
	@ResponseBody
	public HashMap<String, Object> updatePrintCounts(
			HttpServletRequest request, ModelMap out,
			@RequestParam("id") Long id, @RequestParam("orderId") Long orderId,
			@RequestParam("printTimes") String printTimes) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String status = paymentOrderService.findByRequestId(id);
		if (PayStatus.ABANDONED.toString().equalsIgnoreCase(status)) {
			result.put("data", "该订单已打回，不能操作!");
			result.put("success", false);
			result.put("code", 1);
			return result; 
		}
		String orderStatus = consignOrderService.findByOrderId(orderId);
		if (ConsignOrderStatus.CLOSEPAY.getCode().equals(orderStatus)
				|| ConsignOrderStatus.CLOSEPAYED.getCode().equals(orderStatus)) {
			result.put("data", "该订单已关闭，不能操作!");
			result.put("success", false);
			result.put("code", 2);
			return result;
		}
		User user = getLoginUser();
		int printCounts = Integer.parseInt(printTimes) + 1;
		try {
			paymentOrderService.updateForId(id, printCounts, Tools.getIpAddr(request), user);
			List<Long> receiverIdList =  paymentOrderService.queryReceiverIdByRequestId(id);
			for(Long receiverId : receiverIdList){
				accountService.updateBankDataStatusByPrimaryKey(receiverId, null, null, AccountBankDataReminded.No.getCode(), getLoginUser().getLoginId());
			}
			result.put("success", true);
		} catch (BusinessException e) {
			result.put("success", false);
			result.put("data", e.getMsg());
		}
		return result;
	}

	@OpLog(OpType.ConfirmInitialPay)
	@OpParam("orderId")
	@OpParam("payRequestId")
	@OpParam(name = "accept", value = "false")
	@RequestMapping("payment/orderclose.html")
	public @ResponseBody Result confirmClose(
			@RequestParam("orderid") Long orderId,
			@RequestParam("payrequestid") Long payRequestId,
	@RequestParam("cause")String cause) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_PAYBILLPRINT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.confirmClose(orderId,
				payRequestId, user,cause);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}
}
