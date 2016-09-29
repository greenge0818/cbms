package com.prcsteel.platform.order.web.controller.payment;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataReminded;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.service.order.PaymentOrderService;
import com.prcsteel.platform.order.service.payment.PayRequestItemsService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lcw on 2015/7/27. 提现操作相关
 */
@Controller
@RequestMapping("/payment/")
public class WithdrawController extends BaseController {
	@Autowired
	PayRequestService payRequestService;
	@Autowired
	PayRequestItemsService payRequestItemsService;
	@Autowired
	AccountService accountService;
	@Autowired
	AccountBankService accountBankService;
	@Autowired
	OrganizationService organizationService;
	@Autowired
	PaymentOrderService paymentOrderService;

	ShiroVelocity permissionLimit = new ShiroVelocity();

	final String PERMISSION_WITHDRAWPRINT = "order:query:withdrawconfirm:print"; // 打印提现付款单

	/**
	 * 申请提现
	 */
	@RequestMapping("{accountId}/applywithdraw")
	public String applyWithdraw(ModelMap out, @PathVariable Long accountId,
			@RequestParam("type") String type) {
		AccountDto account = accountService.selectByPrimaryKey(accountId);
		List<AccountBank> list = accountBankService.queryByAccountId(accountId);
		out.put("account", account);
		out.put("type", type);
		out.put("list", list);
		return "payment/applywithdraw";
	}

//	/**
//	 * 保存提现申请
//	 */
//	@RequestMapping(value = "saveapply", method = RequestMethod.POST)
//	@OpLog(OpType.SaveApply)
//	@OpParam("bankId")
//	@OpParam("money")
//	public @ResponseBody Result saveApply(@RequestParam("bankId") Long bankId,
//			@RequestParam("money") BigDecimal money, @RequestParam("balance") BigDecimal balance) {
//		Result result = new Result();
//		User user = getLoginUser();
//		try {
//			payRequestService.applyWithdraw(user, bankId, money, balance);
//		} catch (BusinessException e) {
//			result.setSuccess(false);
//			result.setData(e.getMsg());
//			return result;
//		}
//		return result;
//	}

    /**
     * 提现付款详情
     */
    @RequestMapping("{id}/withdrawdetail")
    public String withdrawDetail(ModelMap out, @PathVariable Long id) {
        PayRequest payRequest = payRequestService.selectByPrimaryKey(id);
        Boolean success = false;
        if (payRequest != null
                && (PayStatus.REQUESTED.toString().equals(payRequest.getStatus())
                || PayStatus.APPLYPRINTED.toString().equals(payRequest.getStatus())
                || PayStatus.APPROVED.toString().equals(payRequest.getStatus()))) {
            List<PayRequestItems> list = payRequestItemsService.selectByRequestId(id);
            Integer index = 0;  // 提现申请只有一条记录
            PayRequestItems payRequestItems = list.get(index);
            success = true;
            out.put("payRequestItems", payRequestItems);
            out.put("status", payRequest.getStatus());
        }
        out.put("success", success);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		out.put("bankAccountStartTime", format.format(new Date()));
        return "payment/withdrawdetail";
    }

	/**
	 * 审核提现申请
	 */
	@OpLog(OpType.AuditWithdrawPay)
	@OpParam("id")
	@OpParam("check")
	@OpParam("declineReason")
	@ResponseBody
	@RequestMapping(value = "checkwithdraw.html", method = RequestMethod.POST)
	public Result checkWithdraw(@RequestParam("id") Long id,
			@RequestParam("check") Boolean check,
			@RequestParam("declineReason") String declineReason) {
		User user = getLoginUser();
		Boolean success = payRequestService.checkWithdraw(user, id, check,
				declineReason);
		Result result = new Result();
		result.setSuccess(success);
		return result;
	}

	/**
	 * 确认提现已付款
	 */
	@OpLog(OpType.ConfirmWithdrawPay)
	@OpParam("id")
	@ResponseBody
	@RequestMapping(value = "confirmwithdraw.html", method = RequestMethod.POST)
	public Result confirmWithdraw(@RequestParam("id") Long id, 
			@RequestParam("paymentBank") String paymentBank,
		  	@RequestParam("bankAccountTime") Date bankAccountTime) {
		User user = getLoginUser();
		Boolean success = payRequestService.confirmWithdraw(user, id, paymentBank, bankAccountTime);
		Result result = new Result();
		result.setSuccess(success);
		return result;
	}

	/**
	 * 付款申请单
	 */
	@RequestMapping("{id}/paymentrequisition")
	public String paymentRequisition(ModelMap out, @PathVariable Long id,
			@RequestParam(value = "f", required = false) String from, 
			@RequestParam(value = "type", required = false) String type) {
        PayRequest payRequest = payRequestService.selectByPrimaryKey(id);
        //查询买家是否使用过银票充值支付
		Account account = paymentOrderService.selectByPrimaryKey(payRequest.getBuyerId());
		if (account.getIsAcceptDraftCharged() == 1) {//如果使用了银票支付
			out.put("isAcceptDraftCharged",
					account.getIsAcceptDraftCharged());
		}
		//客户预付款标识
		String paymentLabel = account.getPaymentLabel();

		//客户银行账户信息修改提醒
		boolean showRemindedFlag = false;
		if(AccountBankDataReminded.Yes.getCode().equals(account.getBankDataReminded())){
			showRemindedFlag = true;
		}
		//若客户银行账号信息有核算会计不通过则不显示打印付款申请单
		boolean showPrintPaymentFlag = false;
		if(!AccountBankDataStatus.Approved.getCode().equals(account.getBankDataStatus())){
			showPrintPaymentFlag = true;
		}
        Boolean success = false;
        if (payRequest != null) {
			List<PayRequestItems> list = payRequestItemsService.selectByRequestId(id); // 付款详情记录
			Organization organization = organizationService.queryById(payRequest.getOrgId());
			List<Long> departmentIds = list.stream().map(a -> a.getReceiverDepartmentId()).collect(Collectors.toList());
			if (departmentIds.size() > 0) {
				List<AccountDto> accountList = accountService.queryForBalance(departmentIds);
				out.put("accountList", accountList);
			}
			BigDecimal numberOfMoney = payRequest.getTotalAmount();
			String totalAmountCapital = NumberToCNUtils
					.number2CNMontrayUnit(numberOfMoney);// 大写总金额
			User charger = userService.queryById(organization.getCharger()); // 负责人

			out.put("payRequest", payRequest);
			out.put("list", list);
			out.put("organization", organization);
			out.put("totalAmountCapital", totalAmountCapital);
			out.put("operatorName", charger.getName());
			success = true;
		}
		if (StringUtils.isNotEmpty(from)) {
			out.put("from", from);
		}
		out.put("paymentLabel", paymentLabel);  //客户预付款标识
		out.put("showRemindedFlag", showRemindedFlag);
		out.put("showPrintPaymentFlag", showPrintPaymentFlag);
		out.put("success", success);
		out.put("authority", PERMISSION_WITHDRAWPRINT);
		if (type != null) {
			out.put("type", type);
		}
		return "payment/paymentrequisition";
	}

	/**
	 * 更新打印状态
	 */
	@ResponseBody
	@RequestMapping(value = "updateprintstatus.html", method = RequestMethod.POST)
	public Result updatePrintStatus(HttpServletRequest request,
			@RequestParam("id") Long id) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_WITHDRAWPRINT)) {
			result.setData("您没有作该操作的权限");
		} 
		try {
			User user = getLoginUser();
			payRequestService.updatePrintStatus(user, id, Tools.getIpAddr(request));
			result.setSuccess( true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
}