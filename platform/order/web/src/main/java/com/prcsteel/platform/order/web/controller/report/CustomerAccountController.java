package com.prcsteel.platform.order.web.controller.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.account.model.dto.AccountTransLogsDto;
import com.prcsteel.platform.order.model.dto.CustomerAccountDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountStateQuery;
import com.prcsteel.platform.order.service.report.CustomerAccountService;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * @author lixiang
 * @version V1.1
 * @Title: BuyerCustomerAccountController
 * @Package com.prcsteel.cbms.web.controller.report
 * @Description: 财务报表(买家)
 * @date 2015/8/19
 */

@Controller
@RequestMapping("/report/accoutcust")
public class CustomerAccountController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomerAccountController.class);

	@Resource
	private CustomerAccountService customerAccountService;

	/**
	 * 买家账户报表
	 * 
	 * @param out
	 */
	@RequestMapping("buyerstate.html")
	public void buyerTradeReport(ModelMap out) {
		setDefaultTime(out);
	}

	/**
	 * 卖家账户报表
	 * 
	 * @param out
	 */
	@RequestMapping("sellerstate.html")
	public void sellerTradeReport(ModelMap out) {
		setDefaultTime(out);
	}

	@RequestMapping("/state/data.html")
	@ResponseBody
	public PageResult accountStateData(AccountStateQuery account) {
		PageResult result = new PageResult();
		account.setUserIds(getUserIds());
		try {
			account.setDateEndStr(getDateEndStr(account));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<CustomerAccountDto> list = customerAccountService.queryForCustomer(account);
		if (list == null || list.size() == 0) {
			return result;
		}
		int counts = customerAccountService.queryReportCount(account);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;

	}

	/**
	 * 买家账户报表详情
	 * 
	 * @param out
	 */
	@RequestMapping("buyerstatedetails.html")
	public void buyerStateDetails(ModelMap out, AccountStateQuery account) {
		Date dateStart = null;// 起始时间
		try {
			dateStart = getDateStart(account);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		account.setDateStart(dateStart);
		Account accountModel = customerAccountService.getAccountName(account);
		AccountTransLog accountTransLog = customerAccountService.queryForAmount(account);
		out.put("accountId", account.getId());
		out.put("accountName", accountModel.getName());
		if (accountTransLog != null) {
			out.put("currentBalance", accountTransLog.getCurrentBalance());// 二次结算期初余额
			out.put("cashCurrentBalance", accountTransLog.getCashCurrentBalance());// 资金账户期初余额
		} else {
			BigDecimal balance = new BigDecimal("0.00");
			out.put("currentBalance", balance);// 二次结算期初余额
			out.put("cashCurrentBalance", balance);// 资金账户期初余额
		}
		out.put("startTime", account.getDateStartStr());
		out.put("endTime", account.getDateEndStr());
	}

	/**
	 * 卖家账户报表详情
	 * 
	 * @param out
	 */
	@RequestMapping("sellerstatedetails.html")
	public void sellerStateDetails(ModelMap out, AccountStateQuery account) {
		Date dateStart = null;// 起始时间
		try {
			dateStart = getDateStart(account);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		account.setAccountId(Long.parseLong(account.getId()));
		account.setDateStart(dateStart);
		Account accountModel = customerAccountService.getAccountName(account);
		AccountTransLog accountTransLog = customerAccountService.queryForAmount(account);
		out.put("accountId", account.getId());
		out.put("accountName", accountModel.getName());
		if (accountTransLog != null) {
			out.put("currentBalance", accountTransLog.getCurrentBalance());// 二次结算期初余额
			out.put("cashCurrentBalance", accountTransLog.getCashCurrentBalance());// 资金账户期初余额
		} else {
			BigDecimal balance = new BigDecimal("0.00");
			out.put("currentBalance", balance);// 二次结算期初余额
			out.put("cashCurrentBalance", balance);// 资金账户期初余额
		}
		out.put("startTime", account.getDateStartStr());
		out.put("endTime", account.getDateEndStr());
	}

	/**
	 * 按时间搜素查询重新加载资金账户，二次结算账户期初余额
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping("/query/balance.html")
	@ResponseBody
	public Result queryBalance(AccountStateQuery account) {
		Result result = new Result();
		Date dateStart = null;// 起始时间
		try {
			dateStart = getDateStart(account);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		account.setDateStart(dateStart);
		account.setAccountId(Long.parseLong(account.getId()));
		AccountTransLog accountTransLog = customerAccountService.queryForAmount(account);
		result.setSuccess(true);
		result.setData(accountTransLog);
		return result;
	}

	@RequestMapping("/state/details/data.html")
	@ResponseBody
	public PageResult statedetailsdata(AccountStateQuery account) {
		PageResult result = new PageResult();
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = getDateStart(account);
			dateEnd = getDateEnd(account);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		account.setDateStart(dateStart);
		account.setDateEnd(dateEnd);
		List<AccountTransLogsDto> list = customerAccountService.queryByAccountId(account);
		int counts = customerAccountService.queryByAccountIdCount(account);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 起始日期
	 * 
	 * @param account
	 * @return
	 * @throws ParseException
	 */
	private Date getDateStart(AccountStateQuery account) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		if (StringUtils.isNotEmpty(account.getDateStartStr())) {
			dateStart = format.parse(account.getDateStartStr());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			dateStart = cal.getTime();
		}
		return dateStart;
	}

	/**
	 * 终止日期
	 * 
	 * @param account
	 * @return
	 * @throws ParseException
	 */
	private Date getDateEnd(AccountStateQuery account) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(account.getDateEndStr())) {
			dateEnd = new Date(
					format.parse(account.getDateEndStr()).getTime() + 24 * 3600 * 1000);
		} else {
			dateEnd = new Date();
		}
		return dateEnd;
	}

	/*
	 * 设置默认时间段：当前月
	 * 
	 * @param out
	 */
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
	 * 终止日期（String类型）
	 * 
	 * @param account
	 * @return
	 * @throws ParseException
	 */
	private String getDateEndStr(AccountStateQuery account)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(account.getDateEndStr());
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, +1); // 设置为后一天
		String day = sdf.format(calendar.getTime());
		return day;
	}
}
