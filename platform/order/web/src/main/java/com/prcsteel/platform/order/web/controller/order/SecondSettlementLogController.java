package com.prcsteel.platform.order.web.controller.order;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.account.model.dto.AccountUserDto;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.dto.CustAccountTransLogDto;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;

/**
 * Created by lixiang on 2015/7/27.
 */
@Controller
@RequestMapping("/order/")
public class SecondSettlementLogController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SecondSettlementLogController.class);

	@Resource
	private SecondSettlementLogService secondSettlementLogService;
	@Resource
	private PayRequestService payRequestService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private OrderStatusService orderStatusService;
	@Resource
	private AccountService accountService;
	@Resource
	OrganizationService orgService;

	/**
	 * 二次结算付款待申请列表查询
	 *
	 * @param out
	 */
	@RequestMapping("secondapplyforpayment.html")
	public void index(ModelMap out) {
		User user = getLoginUser();
		Role role = getUserRole();
		List<Organization> orgList;
		if (RoleAuthType.ALL.getValue() == role.getType()) {
			orgList = orgService.queryAllBusinessOrg();
		} else {
			orgList = new ArrayList<>();
			orgList.add(orgService.queryById(user.getOrgId()));
		}
		out.put("user", user);
		out.put("orgList", orgList);
		out.put("businessTypes", AccountBusinessType.values());
		out.put("accountTag", AccountTag.values());

	}

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

	/**
	 *
	 * @param start
	 *            起始页
	 * @param length
	 *            记录数
	 * @param gatheringBar
	 *            类型(应收，应付)
	 * @param accountName
	 *            买家全称
	 * @param orgId
	 *            服务中心
	 * @param userName
	 *            交易员
	 * @return
	 */
	@RequestMapping("secondapplyforpaymentonly.html")
	public @ResponseBody PageResult findById(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("gatheringBar") String gatheringBar,
			@RequestParam("accountName") String accountName,
			@RequestParam(value = "orgId", required = false) Long orgId,
			@RequestParam(value = "userName", required = false) String userName) {
		PageResult result = new PageResult();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("accountName", accountName);
		paramMap.put("orgId", orgId);
		paramMap.put("userName", userName);
		List<AccountUserDto> list = null;
		int counts = 0;
		String balanceSecondSmall = null;// 应收金额
		String balanceSecondBig = null; // 应付金额
		// 判断权限 如果前台传过来的值是receivable，就是应收，则按金额小于0的查询
		if (gatheringBar.equals("receivable")) {
			balanceSecondSmall = gatheringBar;
			paramMap.put("balanceSecondSmall", balanceSecondSmall);
			list = secondSettlementLogService.findBydId(paramMap);
			counts = secondSettlementLogService.findByIdOfCount(paramMap);
		} else if (gatheringBar.equals("payable")) {// 如果前台传过来的值是payable，就是应付，则按金额大于0的查询
			balanceSecondBig = gatheringBar;
			paramMap.put("balanceSecondBig", balanceSecondBig);
			list = secondSettlementLogService.findBydId(paramMap);
			counts = secondSettlementLogService.findByIdOfCount(paramMap);
		} else {
			list = secondSettlementLogService.findBydId(paramMap);
			counts = secondSettlementLogService.findByIdOfCount(paramMap);
		}
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 查询客户公司资金账户余额，二次结算账户欠款，二次结算余额
	 *
	 * @param out
	 * @param id
	 *            客户ID
	 */
	@RequestMapping("secondapplyforpaymentorder.html")
	public void findAccountTransLog(
			ModelMap out,
			@RequestParam("id") Long id) {
		CustAccountDto custAccount = secondSettlementLogService.queryForAccountId(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", custAccount.getAccountId());
		map.put("bankDataStatus", AccountBankDataStatus.Approved.getCode());
		List<AccountBank> bankList = accountBankService.query(map);
		if (custAccount != null) {
			out.put("custAccount", custAccount);
		}
		out.put("id", id);
		out.put("bankList", bankList);
		setDefaultTime(out);
	}

	/**
	 * 查询二次结算记账明细
	 *
	 * @param id
	 * @param start
	 *            起始页
	 * @param length
	 *            每页记录数
	 * @param dateStart
	 *            起始日期
	 * @param dateEnd
	 *            终止日期
	 * @return
	 */
	@RequestMapping("getpaymentdata.html")
	public @ResponseBody PageResult getPaymentData(@RequestParam("id") Long id,
												   @RequestParam("start") Integer start,
												   @RequestParam("length") Integer length,
												   @RequestParam("dateStart") String dateStart,
												   @RequestParam("dateEnd") String dateEnd) {
		PageResult result = new PageResult();
		Date dateStartStr = null;
		Date dateEndStr = null;
		if (!dateStart.isEmpty() && !dateEnd.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dateStartStr = sdf.parse(dateStart);
				dateEndStr = new Date(sdf.parse(dateEnd).getTime() + 24 * 3600 * 1000);
			} catch (ParseException e) {
				logger.error(e.toString());
			}
		}
		List<CustAccountTransLogDto> list = secondSettlementLogService.queryByPayType(id, start, length, dateStartStr, dateEndStr);
		int counts = secondSettlementLogService.queryByPayTypeCount(id, dateStartStr, dateEndStr);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 应收款抵扣
	 *
	 * @param balance
	 *            当前资金余额
	 * @param deductionPay
	 *            申请抵扣金额
	 * @param id
	 *  		     客户部门ID
	 * @return
	 */
	@OpLog(OpType.SecondDuction)
	@OpParam("balance")
	@OpParam("deductionPay")
	@OpParam("accountId")
	@OpParam("balanceSecondSettlemento")
	@RequestMapping(value = "addSecondPay.html", method = RequestMethod.POST)
	public @ResponseBody Result addAccountTransLog(
			ModelMap out,
			@RequestParam("balance") BigDecimal balance,
			@RequestParam("deductionPay") String deductionPay,
			@RequestParam("id") Long id) {
		Result result = new Result();
		// 获取当前登录用户ID
		User user = getLoginUser();
		BigDecimal amount = new BigDecimal(deductionPay); // 发生额
		try {
			secondSettlementLogService.deduction(id, amount,balance, user);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			return result;
		}
		return result;
	}

	/**
	 * 应付款申请
	 *
	 * @param accountId
	 *            客户ID
	 * @param deductionPay
	 *            提现付款额
	 * @return
	 */
	@OpLog(OpType.SecondWithdrawal)
	@OpParam("balance")
	@OpParam("deductionPay")
	@OpParam("accountId")
	@OpParam("bankId")
	@RequestMapping(value = "addSecondPayAmount.html", method = RequestMethod.POST)
	public @ResponseBody Result addOrderPayRequst(
			@RequestParam("accountId") Long accountId,
			@RequestParam("id") Long id,
			@RequestParam("accountName") String accountName,
			@RequestParam("deductionPay") BigDecimal deductionPay,
			@RequestParam("balanceSecondSettlemento") BigDecimal balanceSecondSettlemento,
			@RequestParam("bankId") Long bankId) {
		Result result = new Result();
		try {
			secondSettlementLogService.addSecondPayAmount(id, deductionPay, balanceSecondSettlemento, bankId, getLoginUser(), accountId);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 *
	 * @param gatheringBar
	 *            类型(应收，应付)
	 * @param buyerName
	 *            买家全称
	 * @param orgName
	 *            服务中心
	 * @param userName
	 *            交易员
	 * @return
	 */
	@RequestMapping("exportsecondapplyforpaymentonly.html")
	public ModelAndView exportSecondApplyForPayOnly(
			@RequestParam("gatheringBar") String gatheringBar,
			@RequestParam("buyerName") String buyerName,
			@RequestParam(value = "orgName", required = false) String orgName,
			@RequestParam(value = "userName", required = false) String userName) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		paramMap.put("start", 0);
		paramMap.put("length", 99999);
		paramMap.put("cName", buyerName);
		paramMap.put("oName", orgName);
		paramMap.put("uName", userName);
		List<AccountUserDto> list = null;
		String balanceSecondSmall = null;// 应收金额
		String balanceSecondBig = null; // 应付金额
		// 判断权限 如果前台传过来的值是receivable，就是应收，则按金额小于0的查询
		if (gatheringBar.equals("receivable")) {
			balanceSecondSmall = gatheringBar;
			paramMap.put("balanceSecondSmall", balanceSecondSmall);
			list = secondSettlementLogService.findBydId(paramMap);
		} else if (gatheringBar.equals("payable")) {// 如果前台传过来的值是payable，就是应付，则按金额大于0的查询
			balanceSecondBig = gatheringBar;
			paramMap.put("balanceSecondBig", balanceSecondBig);
			list = secondSettlementLogService.findBydId(paramMap);
		} else {
			list = secondSettlementLogService.findBydId(paramMap);
		}

		//excel表头和数据
		Map<String, Object> dataMap = new HashMap<>();

		//表头
		dataMap.put("titles", Arrays.asList("单位全称","交易员","应收金额(元)","应付金额(元)"));

		List<PageData> varList = new ArrayList<>();
		if (list != null) {
			list.forEach(a->{

				String pay = ""; //付款
				String receipt = ""; //收款
				if(a.getBalanceSecondSettlement().doubleValue()>0){
					receipt = Tools.formatBigDecimal(a.getBalanceSecondSettlement(), 2);
				}else{
					BigDecimal b = a.getBalanceSecondSettlement();
					b = new BigDecimal(Math.abs(b.doubleValue()));
					pay = Tools.formatBigDecimal(b, 2);
				}

				PageData vpd = new PageData();
				vpd.put("var1",a.getcName());
				vpd.put("var2",a.getuName());
				vpd.put("var3",pay);
				vpd.put("var4", receipt);
				varList.add(vpd);
			});
		}

		//数据
		dataMap.put("varList", varList);

		ObjectExcelView erv = new ObjectExcelView();
		return new ModelAndView(erv, dataMap);
	}

}
