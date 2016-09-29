package com.prcsteel.platform.order.web.controller.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.PaymentCreateDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataReminded;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.query.PayMentQuery;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.constants.Constant.PayStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.PayMentDto;
import com.prcsteel.platform.order.model.dto.PaymentOrderDto;
import com.prcsteel.platform.order.model.enums.BankType;
import com.prcsteel.platform.order.model.model.PayCustInfo;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.PaymentOrderService;
import com.prcsteel.platform.order.service.payment.PayRequestItemsService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;

import net.sf.json.JSONObject;


/**
 * Created by lixiang on 2016/3/2. 支付管理 >付款申请单
 */
@Controller
@RequestMapping("/order/paymentmanager/")
public class PaymentRequestController extends BaseController{
	
	private static Logger logger = LogManager.getLogger(PaymentRequestController.class);
	
	@Resource
	PayRequestService payRequestService;
	
	@Resource
	BankTransactionInfoService bankTransactionInfoService;
	
	@Resource
	AccountService accountService;
	
	@Resource
	PaymentOrderService paymentOrderService;
	
	@Resource
	ConsignOrderItemsService consignOrderItemsService;
	
	@Resource
	PayRequestItemsService payRequestItemsService;

	@Resource
	ConsignOrderService consignOrderService;
	
    @Resource
    OrganizationDao organizationDao;
	@Resource
	OrganizationService organizationService;
	
	/**
	 * 设置默认时间段：当前月
	 * 
	 * @param out
	 **/
	private void setDefaultTime(ModelMap out) {
		// 日期:开始时间、结束时间(当前月第一天、当前时间)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 界面默认显示的开单时间
		out.put("startTime", format.format(cal.getTime()));
		out.put("endTime", format.format(new Date()));
	}

	
	@RequestMapping("request.html")
	public void index(ModelMap out) {
		setDefaultTime(out);
		out.put("status", PayStatus.REQUESTED.toString());
	}
	
	@RequestMapping("get/request.html")
	@ResponseBody
	public PageResult getStepData(PayMentQuery payMentQuery){
		PageResult result = new PageResult();
		try {
			payMentQuery.setEndTime(getDateEndStr(payMentQuery.getEndTime()));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<PayMentDto>  list  = payRequestService.queryPeyMentRequest(payMentQuery);
    	int counts = payRequestService.queryPeyMentRequestCount(payMentQuery);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}
	
	@RequestMapping("payment.html")
	public void payment(ModelMap out) {
		setDefaultTime(out);
		out.put("status", PayStatus.CONFIRMED.toString());
	}
	
	@RequestMapping("get/payment.html")
	@ResponseBody
	public  PageResult getPayment(PayMentQuery payMentQuery){
		PageResult result = new PageResult();
		try {
			payMentQuery.setEndTime(getDateEndStr(payMentQuery.getEndTime()));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<PayMentDto>  list  = payRequestService.queryPeyMent(payMentQuery);
    	int counts = payRequestService.queryPeyMentCount(payMentQuery);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}
	
	/**
	 * 终止日期（String类型）
	 * 
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	private String getDateEndStr(String endTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(endTime);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, +1); // 设置为后一天
		String day = sdf.format(calendar.getTime());
		return day;
	}
	
	@RequestMapping("paymentrequest.html")
	public void index(ModelMap out, @RequestParam("payItemId") Long payItemId) {
		boolean showRemindedFlag = false;//客户银行账号信息修改提醒默认不提醒
		// 获取当前登录用户
		User user = getLoginUser();
		String userName = user.getName();
		PayRequestItems payRequestItems = payRequestItemsService.selectByPrimaryKey(payItemId);
		PayRequest payRequest = payRequestService.selectByPrimaryKey(payRequestItems.getRequestId());
		Organization organization = organizationService.queryById(payRequest.getOrgId());
		List<PaymentOrderDto> list = paymentOrderService.findByOrder(payRequest.getConsignOrderId());
		List<Long> accountIds = list.stream().map(a -> a.getReceiverId()).collect(Collectors.toList());
		List<AccountDto> accountList = accountService.queryForBalance(accountIds);
		out.put("accountList", accountList);
		String changerName = paymentOrderService.getChangerName(payRequest.getOrgId());
		String totalAmountBoo = NumberToCNUtils.number2CNMontrayUnit(payRequestItems.getPayAmount());// 大写金额
		//查询买家是否使用过银票充值支付
		Account account = paymentOrderService.selectByPrimaryKey(payRequestItems.getReceiverId());
		if (account.getIsAcceptDraftCharged() == 1) {//如果使用了银票支付
			out.put("isAcceptDraftCharged",account.getIsAcceptDraftCharged());
			List<String> codeList = consignOrderItemsService.selectAcceptDraftCodeByOrderId(payRequest.getConsignOrderId()).stream().filter(a-> !StringUtils.isBlank(a)).collect(Collectors.toList());
			if(codeList != null && codeList.size() != 0){
				out.put("acceptDraftCodeList", codeList);
			}
		}
		String bankType = bankTransactionInfoService.selectByAccountName(payRequest.getBuyerName());
		if (bankType != null) {
			String bankTypeName = BankType.getName(bankType);
			out.put("bankTypeName", bankTypeName);
		}
		//若卖家列表银行账号信息有修改则提醒
		if(list.stream().filter(a->(AccountBankDataReminded.Yes.getCode().equals(accountService.selectByPrimaryKey(a.getReceiverId()).getAccount().getBankDataReminded().toString()))).collect(Collectors.toList()).size() > 0)
			showRemindedFlag = true;
		out.put("showRemindedFlag", showRemindedFlag);
		out.put("totalAmountBoo", totalAmountBoo);
		out.put("changerName", changerName);
		out.put("paymentOrderDto", payRequest);
		out.put("payRequestItems", payRequestItems);
		out.put("organization", organization);
		out.put("userName", userName);
	}
	
	@RequestMapping("closedpayment.html")
	@ResponseBody
	public HashMap<String, Object> closedPayment(Long requestId){
		HashMap<String, Object> result = new HashMap<String, Object>();
		User user = getLoginUser();
		PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
		try {
			payRequestService.closedPayment(requestId, user);
			result.put("success", true);
			result.put("status", payRequest.getStatus());
		} catch (BusinessException e)  {
			result.put("success", false);
			result.put("data", e.getMsg());
		}
		return result;
	}

	/**
	 * 进入新增付款页面
	 */
	@RequestMapping("paymentcreate.html")
	public void paymentCreate(ModelMap out) {
		out.put("login", getLoginUser());
		
		Role role = roleService.queryById(getLoginUser().getRoleId());
		String roleCode = "";
		if(role.getCode()!=null && role.getCode().contains("交易员"))
			roleCode = "交易员";
		
		Long orgId = null;
		if(1 != role.getId()){
			orgId = getLoginUser().getOrgId();
		}
		out.put("roleCode", roleCode);
		out.put("users", payRequestService.queryUserNamesByOrgId(orgId));
	}
	
	/**
	 * 新增付款-按卖家名称查询
	 */
	@RequestMapping(value = "paymentCustQuery.html", method = RequestMethod.POST)
	@ResponseBody
	public List<PayCustInfo> paymentCustQuery(String custName) {
		return payRequestService.queryCustInfoByCustName(custName);
	}
	
	/**
	 * 新增付款-提交审核
	 */
	@RequestMapping(value = "paymentNew.html", method = RequestMethod.POST)
	@ResponseBody
	public Result paymentNew(String paymentCreateDto) {
		Result result = new Result();
		PaymentCreateDto dto = (PaymentCreateDto) JSONObject.toBean(JSONObject.fromObject(paymentCreateDto), PaymentCreateDto.class);
		dto.setCreatedBy(getLoginUser().getName());
		Long orgId = getLoginUser().getOrgId();
		Organization organization = organizationDao.queryById(orgId);
		//设置 付款申请单号
		dto.setCode(payRequestService.createdPayCode(organization.getSeqCode(), orgId));
		dto.setOrgId(orgId);
		dto.setOrgName(organization.getName());
		//lixiang修改 2016-3-21
		try{
			if(payRequestService.insertPayRequest(dto)) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("调用二次结算余额接口失败！");
			}
		}catch(BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
			logger.error("付款失败!", e);
		}
		return result;
	}
}
