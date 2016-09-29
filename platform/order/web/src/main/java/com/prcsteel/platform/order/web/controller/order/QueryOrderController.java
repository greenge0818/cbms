package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataReminded;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.acl.model.enums.RiskControlType;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.service.BillSequenceService;
import com.prcsteel.platform.order.model.dto.BankOriginalDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInvoiceDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSettleDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.dto.CustAccountTransLogDto;
import com.prcsteel.platform.order.model.dto.IcbcBdDto;
import com.prcsteel.platform.order.model.dto.OrderContactDto;
import com.prcsteel.platform.order.model.dto.OrderItemDetailDto;
import com.prcsteel.platform.order.model.dto.OrderItemsInvoiceInInfoDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateDto;
import com.prcsteel.platform.order.model.dto.PayRequestItemDto;
import com.prcsteel.platform.order.model.dto.PayRequestItemsInfoDto;
import com.prcsteel.platform.order.model.dto.PayRequestOrgDto;
import com.prcsteel.platform.order.model.dto.PayRequstDto;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.BankTransactionType;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatusForSearch;
import com.prcsteel.platform.order.model.enums.PayRequestType;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.PaymentType;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.PayRequestQuery;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.service.allowance.AllowanceService;
import com.prcsteel.platform.order.service.invoice.InvoiceInService;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.CertificateService;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderProcessService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.ConsignOrderSettleService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.PaymentOrderService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import com.prcsteel.platform.order.service.payment.BdlService;
import com.prcsteel.platform.order.service.payment.PayRequestItemsService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.service.rebate.RebateService;
import com.prcsteel.platform.order.service.reward.RewardReportService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.mq.CbmsRequirementStatusSender;
import com.prcsteel.platform.order.web.mq.MarketAddUserSender;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
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
 * Created by dengxiyan on 2015/7/17. 买家代运营交易管理：查询
 */
@Controller
@RequestMapping("/order/query/")
public class QueryOrderController extends BaseController {

	private static Logger logger = LogManager.getLogger(QueryOrderController.class);

	@Value("${jms.cbms.enabled}")
	private boolean enabled;

	@Resource
	ConsignOrderService consignOrderService;
	@Resource
	PaymentOrderService paymentOrderService;
	@Resource
	ConsignOrderSettleService consignOrderSettleService;
	@Resource
	AccountService accountService;
	@Resource
	OrderStatusService orderStatusService;
	@Resource
	BillSequenceService busiBillSequenceService;
	@Resource
	BankTransactionInfoService bankTransactionInfoService;
	@Resource
	PayRequestService payRequestService;
	@Resource
	PayRequestItemsService payRequestItemsService;
	@Resource
	ConsignOrderItemsService consignOrderItemsService;
	@Resource
	private SecondSettlementLogService secondSettlementLogService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	ConsignOrderProcessService consignOrderProcessService;
	@Resource
	private OrganizationDao organizationDao;
	@Resource
	SysSettingService sysSettingService;
	@Resource
	AcceptDraftService acceptDraftService;
	@Resource
	BdlService bdlService;
	@Resource
	BankOriginalService bankOriginalService;
	@Resource
	AccountContactService accountContactService;
	@Resource
	RewardReportService rewardReportService;
	@Resource
	RebateService rebateService;
	@Resource
	AccountFundService accountFundService;
	@Resource
	AllowanceService allowanceService;
	@Resource
	ConsignOrderAttachmentService attachmentService;


	@Resource
	UserService userService;
	@Resource
	CertificateService certificateService;
	ShiroVelocity permissionLimit = new ShiroVelocity();

	@Autowired
	CbmsRequirementStatusSender cbmsRequirementStatusSender;
	
	@Resource
	InvoiceInService invoiceInService;
	@Resource
	ContactService contactService;

	@Resource
	MarketAddUserSender marketAddUserSender;//新增联系人时，发送MQ
	@Resource
	SysSettingDao sysSettingDao;

	@Value("${ecmq.active}")
	Boolean ecMQActive;

	@Resource
	ConsignOrderChangeService consignOrderChangeService;//合同变更

	final String PERMISSION_PAGEVIEW = "order:query:detail:view"; // 订单详情页
	final String PERMISSION_AUDIT = "order:query:approval:approval"; // 审核
	final String PERMISSION_RELATE = "order:query:associate:process"; // 关联
	final String PERMISSION_APPLYPAY = "order:query:applypayment:process"; // 申请付款
	final String PERMISSION_AUDITPAY = "order:query:payment:approval"; // 审核付款
	final String PERMISSION_PRINTPAY = "order:query:confirmpayment:paybillprint"; // 打印付款申请单
	final String PERMISSION_CONFIRMPAY = "order:query:confirmpayment:confirm"; // 确认已付款(浦发)
	final String PERMISSION_CONFIRMPAY_ICBC = "order:query:confirmpayment:confirmicbc"; // 确认已付款(工行)
	final String PERMISSION_CLOSEAPPLY = "order:query:detail:closeapply"; // 申请关闭
	final String PERMISSION_CLOSEAUDIT = "order:query:detail:closeaudit"; // 审核关闭
	final String PERMISSION_ROLLBACKAPPLY = "order:query:invoice:close"; //二结后申请关闭
	final String PERMISSION_ROLLBACKCONFIRM = "order:query:detail:rollbackconfirm"; // 审核关闭
	final String PERMISSION_PAYBILLPRINT = "order:query:confirmpayment:paybillprint"; // 核算会计订单关闭
	final String PERMISSION_CONFIRMPAYMENT_PRINT = "order:query:confirmpayment:print"; // 打印合同
	final String PERMISSION_CONFIRMPAYMENT_PAYBILLPRINT = "order:query:confirmpayment:paybillprint"; // 打印付款申请单
	final String PERMISSION_DETAIL_FIGHTBACK = "order:query:detail:fightback";    // 订单打回
	final String PERMISSION_CONFIRMPAYMENT_BANKCODE = "order:query:bankcode:confirm";    // 客户银行账号审核
	final String PERMISSION_BANKTRANSACTION_PAYERROR = "order:banktransaction:payerror:view";    // 到账查询查询疑似支付错误处理
	final String PERMISSION_PRINT_DELIVERY_LETTER = "order:query:secondsettlement:printdeliveryletter:view";    // 打印收货确认函
	final String PERMISSION_PAYMENTAPPLICATION_AUDIT = "order:query:paymentapplication:audit";    //待申请付款-预付款申请审核
	final String PERMISSION_CONFIRM_REQUEST_AUDIT = "order:query:confirm:request:confirm";    //待确认已付款 - 预付款确认
	final String PERMISSION_PRINTPAYMENT_PRINT = "order:query:printpayment:print";    //待打印付款申请 - 待打印预付款申请

	public static final Map<String, String> map = new HashMap<String, String>();

	public static final Map<String, String> getConsignOrderFlowNodeMap() {
		if (map.size() <= 0) {
			map.put("1", "新开单");
			map.put("2", "审核通过");
			map.put("4", "已关联");
			map.put("REQUESTED", "已申请付款");
			map.put("APPROVED", "已审核付款");
			map.put("PAYED", "已确认付款");
			map.put("PICKEDUP", "已申请提货");
			map.put("FILLEDUP", "已开放货函");
			map.put("7", "已二次结算");
			map.put("8", "已申请开票");
			map.put("10", "已开票");
		}
		return map;
	}

	/**
	 * 全部Tab
	 *
	 * @param out
	 * @author dengxiyan
	 */
	@RequestMapping("index.html")
	public void index(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.INDEX.toString(), out);

		//初始化交易单管理：options(status, pickup_status, fillup_status, pay_status)
		List<ConsignOrderStatusForSearch> options = ConsignOrderStatusForSearch.getOrderStatusList(AccountType.seller);
		options.add(10, ConsignOrderStatusForSearch.TOBEINVOICEREQUEST);  //待申请开票
		options.remove(0); //删除 all 这个选项
		out.put("options", options);
	}

	/**
	 * 待关联/待收款Tab
	 *
	 * @param out
	 * @author dengxiyan
	 */
	@RequestMapping("associate.html")
	public void associate(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.ASSOCIATE.toString(), out);
	}

	/**
	 * 待审核付款Tab author dengxiyan
	 *
	 * @param out
	 */
	@RequestMapping("payment.html")
	public void payment(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PAYMENT.toString(), out);
	}

	/**
	 * 待申请付款Tab author dengxiyan
	 *
	 * @param out
	 */
	@RequestMapping("applypayment.html")
	public void applypayment(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.APPLYPAYMENT.toString(), out);
	}

	/**
	 * 待打印付款申请Tab author Green.Ge
	 *
	 * @param out
	 */
	@RequestMapping("printpendingpayapply.html")
	public void printpendingpayapply(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY.toString(), out);
	}

	/**
	 * 待确认已付款Tab author dengxiyan
	 *
	 * @param out
	 */
	@RequestMapping("confirmpayment.html")
	public void confirmpayment(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(), out);
	}

	/**
	 * 待审核Tab author dengxiyan
	 *
	 * @return
	 */
	@RequestMapping("approval.html")
	public void approval(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.APPROVAL.toString(), out);
	}

	/**
	 * 待提货Tab author dengxiyan
	 *
	 * @return
	 */
	@RequestMapping("pickup.html")
	public void pickup(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PICKUP.toString(), out);
	}

	/**
	 * 待放货Tab author dengxiyan
	 *
	 * @param out
	 */
	@RequestMapping("fillup.html")
	public void fillup(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.FILLUP.toString(), out);
	}

	/**
	 * 待二次结算Tab author dengxiyan
	 *
	 * @return
	 */
	@RequestMapping("secondsettlement.html")
	public void secondSettlement(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.SECONDSETTLEMENT.toString(), out);
	}

	/**
	 * 已二次结算Tab author dengxiyan
	 * 显示待开票申请、待开票订单
	 * @return
	 */
	@RequestMapping("invoice.html")
	public void invoice(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.INVOICE.toString(), out);
	}

	/**
	 * 交易完成Tab
	 *
	 * @return
	 * @author dengxiyan
	 */
	@RequestMapping("tradecomplete.html")
	public void tradecomplete(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.TRADECOMPLETE.toString(), out);
	}

	/**
	 * 可变更的订单
	 * @param out
	 */
	@RequestMapping("changelist.html")
	public void changelist(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
	}


	/**
	 * 待审核的变更订单
	 * @param out
	 */
	@RequestMapping("changedlist.html")
	public void changedlist(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CHANGELIST.toString(), out);
	}

	/**
	 * 交易关闭Tab
	 *
	 * @return
	 * @author dengxiyan
	 */
	@RequestMapping("tradeclose.html")
	public void tradeclose(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.TRADECLOSE.toString(), out);
	}

	/**
	 * 交易关闭待审核Tab
	 *
	 * @return
	 * @author dengxiyan
	 */
	@RequestMapping("tradecloseapproval.html")
	public void tradecloseapproval(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.TRADECLOSEAPPROVAL.toString(),
				out);
	}

	/**
	 * 分页加载表格数据
	 *
	 * @param dto
	 *            前端参数封装到dto
	 * @param start
	 *            开始记录（分页参数）
	 * @param length
	 *            每页记录数（分页参数）
	 * @return
	 * @author dengxiyan
	 */
	@RequestMapping("loadOrderData.html")
	@ResponseBody
	public PageResult loadOrderData(ConsignOrderDto dto,
									@RequestParam("start") Integer start,
									@RequestParam("length") Integer length) {
		//判断是不是服务中心
		if(organizationDao.queryByName(dto.getOwnerName())!=null){
			dto.setOrderOrgName(dto.getOwnerName());
			dto.setOwnerName(null);
		}
		// 条件处理
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dto", dto);
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("userId", getLoginUser().getId());

		// 状态处理，用于in条件
		if (StringUtils.isNotEmpty(dto.getStatus())) {
			String[] array = dto.getStatus().split(",");
			paramMap.put("array", array);
		}
		// 状态处理，用于in条件
		if (StringUtils.isNotEmpty(dto.getChangeStatus())) {
			String[] changeStatus = dto.getChangeStatus().split(",");
			paramMap.put("changeStatus", changeStatus);
		}

		// 处理交易单状态条件
		if(StringUtils.isNotEmpty(dto.getOrderStatus())){
			buildParamForOrderStatus(dto);
		}

		// 只查看融资订单
		if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
			paramMap.put("financeOrder", 1);
		}

		// 返回数据处理
		List<ConsignOrderDto> list = consignOrderService.selectByConditions(paramMap);
		int total = consignOrderService.totalOrderByConditions(paramMap);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	private void buildParamForOrderStatus(ConsignOrderDto dto) {
		List<String> orderStatus = Arrays.asList(dto.getOrderStatus().split(","));

		List<ConsignOrderDto> orderStatusList = new ArrayList<ConsignOrderDto>();

		//set to dto
		dto.setOrderStatusQuerys(orderStatusList);

		orderStatus.forEach(status -> {
			if (ConsignOrderStatusForSearch.NEW.getCode().equals(status)) {
				//待审核
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"1"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.NEWAPPROVED.getCode().equals(status)) {
				//待关联
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"2"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.TOBEPICKUP.getCode().equals(status)) {
				//待提货
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.TOBEDELINERY.getCode().equals(status)) {
				//待放货
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4", "6"});
				statusItem.setFillupStatus(-1);
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.TOBEPAYREQUEST.getCode().equals(status)) {
				//待申请付款 
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4", "6"});
				statusItem.setPayStatus(Constant.ConsignOrderPayStatus.APPLY.toString());
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.PAYREQUEST.getCode().equals(status)) {
				//待审核付款
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4", "6"});
				statusItem.setPayStatus(Constant.ConsignOrderPayStatus.REQUESTED.toString());
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.TOBEPRINTPAYREQUEST.getCode().equals(status)) {
				//待打印付款申请单
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4", "6"});
				statusItem.setPayStatus(Constant.ConsignOrderPayStatus.APPROVED.toString());
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.COMFIRMEDPAY.getCode().equals(status)) {
				//待确认已付款
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"4", "6"});
				statusItem.setPayStatus(ConsignOrderPayStatus.APPLYPRINTED.toString());
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.SECONDSETTLE.getCode().equals(status)) {
				//待二次结算
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"6"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.TOBEINVOICEREQUEST.getCode().equals(status)) {
				//待开票申请
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"7"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.INVOICEREQUEST.getCode().equals(status)) {
				//待开票
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"8"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.FINISH.getCode().equals(status)) {
				//交易完成
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"10"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.CLOSEREQUEST.getCode().equals(status)) {
				//交易关闭待审核
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"3", "5", "-5", "-6"});
				orderStatusList.add(statusItem);
			} else if (ConsignOrderStatusForSearch.CLOSED.getCode().equals(status)) {
				//交易关闭
				ConsignOrderDto statusItem = new ConsignOrderDto();
				statusItem.setStatusValues(new String[]{"-1", "-2", "-3", "-4", "-7", "-8"});
				orderStatusList.add(statusItem);
			}

		});
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
		paramMap.put("status",PayStatus.REQUESTED.toString());
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

		// 界面默认显示的开单时间
		//10.22 Green edit 鹏哥说默认显示全部，自己去选日期
//		out.put("startTime", format.format(cal.getTime()));
//		out.put("endTime", format.format(new Date()));
	}

	private void setStatusParam(Map<String, Object> paramMap, String... status) {
		List<String> statusList = new ArrayList<String>();
		for (String item : status) {
			statusList.add(item);
		}
		paramMap.put("list", statusList);
	}

	@RequestMapping("detail.html")
	public void orderDetail(ModelMap out,
							@RequestParam(value = "orderid", required = false) Long orderId,
							@RequestParam(value = "menu", required = false) String menu) {
		if (orderId == null
				|| !permissionLimit.hasPermission(PERMISSION_PAGEVIEW))
			return;
		//add by wangxj 订单是否二结后 0 否，1 是
		String isSecondCount = "0";

		// 订单信息
		ConsignOrder order = consignOrderService.queryById(orderId);
		//合同变更订单 add by wangxianjun
		Boolean isChange = false;//订单是否处于变更状态
		if("PENDING_APPR_PAY".equals(order.getAlterStatus()) || "PENDING_PRINT_PAY".equals(order.getAlterStatus()) || "PENDING_CONFIRM_PAY".equals(order.getAlterStatus())) {
			isChange = true;
		}
		if (order != null) {
			//合同变更订单
			if(isChange) {
				order = setOrderByChangeOrder(order);
			}
			order.setFeeTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
			if(order.getOutboundTaker() != null) {
				order.setOutboundTaker(order.getOutboundTaker().equals("seller") ? "卖家" : "买家");
			}
			else{
				order.setOutboundTaker(order.getFeeTaker().equals("seller") ? "卖家" : "买家");
			}
			order.setDeliveryType(order.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
			isSecondCount = (Integer.valueOf(order.getStatus()) > 6) ? "1":"0";//status 大于6是二结后，小于等于6是二结前
		}
		out.put("order", order);
		out.put("isSecondCount", isSecondCount);
		//通过订单ID查询二结后上传图片
		/*Map map = new HashMap();
		map.put("orderId",orderId);
		map.put("type", "order");
		List<ConsignOrderAttachment> attachments = consignOrderService.getAttachmentByOrderId(map);
		if (attachments != null) {
			out.put("attachments", attachments);
		}*/
		if (order != null) {
			Map<String, Boolean> permission = new HashMap<String, Boolean>();
			boolean canAudit = false;
			boolean canRelate = false;
			boolean canapplyPay = false;
			boolean canAuditPay = false;
			boolean canPrintPay = false;
			boolean canConfirmPay = false;
			boolean canConfirmPayIcbc = false;
			boolean canColse = false;
			boolean canAuditclose = false;
			boolean canApplyRollback = false;
			boolean canAuditRollback = false;
			boolean canConfirmRollback = false;
			boolean canPayedRollback = false;
			boolean fightbackClose = false;
			boolean contractPrint = false;
			boolean showPrintPaymentFlag = false;//是否显示打印付款申请单
			if (ConsignOrderStatus.NEW.getCode().equals(order.getStatus())) {
				canAudit = true;
			} else if (ConsignOrderStatus.NEWAPPROVED.getCode().equals(order.getStatus())) {
				canRelate = true;
				canColse = true;
			} else if (ConsignOrderStatus.CLOSEREQUEST1.getCode().equals(order.getStatus())
					|| ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(order.getStatus())) {
				canAuditclose = true;
			} else if (ConsignOrderStatus.RELATED.getCode().equals(order.getStatus())
					|| ConsignOrderStatus.SECONDSETTLE.getCode().equals(order.getStatus())) {
				canColse = true;
				canPayedRollback = true;
				fightbackClose = true;
				contractPrint = true;
			} else if (ConsignOrderStatus.INVOICEREQUEST.getCode().equals(order.getStatus())) {
				canApplyRollback = true;
				canColse = false;
			} else if (ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode().equals(order.getStatus())) {
				canAuditRollback = true;
			} else if (ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode().equals(order.getStatus())) {
				canConfirmRollback = true;
			}


			// 卖家信息

			List<ConsignOrderItems> orderItems = new ArrayList<>();
			if(isChange) {
				orderItems =  consignOrderChangeService.selectByChangeOrderId(order.getChangeOrderId());
			}else{
				orderItems =  consignOrderService
						.queryOrderItemsById(orderId);
			}
			Account AccountSellerName= accountService.selectAccountByName(orderItems.get(0).getSellerName());
			out.put("sellerLabel",AccountSellerName.getSupplierLabel());// add by wangxianjun ,把客户标示传到页面
			out.put("settingValue",sysSettingDao.querySettingValueByName(AccountSellerName.getSupplierLabel()));
			if("applypayment".equals(menu)||("4".equals(order.getStatus().trim())&&"APPLY".equals(order.getPayStatus().trim()))){
				List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.APPLY_PAYMENT.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
				out.put("hint", hint.size()==0?null:hint.get(0));
			}
			if("approval".equals(menu)|| "1".equals(order.getStatus().trim())){
				List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.AUDIT_ORDER.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
				out.put("hint", hint.size()==0?null:hint.get(0));
			}
			if("payment".equals(menu)||("4".equals(order.getStatus().trim())&&"REQUESTED".equals(order.getPayStatus().trim()))){
				List<SysSetting> hint = sysSettingService.queryHint().stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PENDING_PAYMENT.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
				out.put("hint", hint.size()==0?null:hint.get(0));
			}
			if("printpendingpayapply".equals(menu)||("4".equals(order.getStatus().trim())&&("APPROVED".equals(order.getPayStatus().trim())||"APPLYPRINTED".equals(order.getPayStatus().trim())))){
				List<SysSetting> hint = sysSettingService.queryHint();
				List<SysSetting> printFloat=hint.stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_FLOAT_LAYER.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());
				List<SysSetting>printTrans=hint.stream().filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_TRANS_INTERFACE.getCode())).filter(a->a.getSettingValue().equals("1")).filter(a -> a.getDefaultValue().equals(AccountSellerName.getSupplierLabel())).collect(Collectors.toList());

				out.put("hint", printTrans.size()==0?null:printTrans.get(0));
				out.put("print_float_layer", printFloat.size()==0?null:printFloat.get(0));

			}

			out.put("orderItems", orderItems);


			if (getRelatedOrderStatus().contains(order.getStatus())) {
				if (ConsignOrderPayStatus.APPLY.toString().equals(
						order.getPayStatus())) {
					canPayedRollback = false;
					fightbackClose = false;
					contractPrint = false;
					if (!canAuditclose) {// 已申请关闭
						canapplyPay = true;
						List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsService
								.getSellerInfo(orderId);
						out.put("sellerInfoList", sellerInfoList);
					}
				} else {
					List<PayRequest> payRequestAll = null;
					List<PayRequestItemsInfoDto> payRequestList = null;
					PayRequest payRequest = null;
					Long requestId = 0L;
					//变更订单
					if(isChange) {
						payRequest = payRequestService.selectAvailablePaymentByChangeOrderId(order.getChangeOrderId());
						payRequestList = payRequestItemsService.selectChangePayInfoByRequestId(payRequest.getId());
						requestId = payRequest.getId();
						out.put("payRequest", payRequest);
						out.put("payRequestList", payRequestList);
					}else{
						payRequestAll = payRequestService.selectAllPaymentByOrderId(orderId);
						for (PayRequest request : payRequestAll) {
							if ("1".equals(request.getType())) {
								requestId = request.getId();
							}
						}
						List<Long> requestIds = payRequestAll.stream().map(a -> a.getId()).collect(Collectors.toList());
						payRequestList = payRequestItemsService.selectAllPayInfoByRequestId(requestIds);
						out.put("payRequest", payRequestAll);
						out.put("payRequestList", payRequestList);
					}
					out.put("requestId", requestId);
					
					if (ConsignOrderPayStatus.PAYED.toString().equals(
							order.getPayStatus())) {
						canColse = false;
						canPayedRollback = true && canPayedRollback;
						fightbackClose = false;
						contractPrint = false;
					} else {
						canPayedRollback = false;

						if (ConsignOrderPayStatus.APPROVED.toString().equals(
								order.getPayStatus())) {
							canPrintPay = true;
							canColse = false;
						}
						else if (ConsignOrderPayStatus.APPLYPRINTED.toString().equals(
								order.getPayStatus())) {
							canConfirmPay = true;
							canConfirmPayIcbc = true;
							canPrintPay = true;
							canColse = false;
							//合同变更待确认付款展示打回按钮
							if(isChange)
								fightbackClose = true;
							else
								fightbackClose = false;
						} else {
							fightbackClose = false;
							contractPrint = false;
						}
					}
				}
				//已关联后的申请付款、审核付款和确认付款显示亏损提示消息
				if (getRemindLossPayStatus().contains(order.getPayStatus())) {
					out.put("viewlossinfo", true);
				}
			}

			if (ConsignOrderPayStatus.REQUESTED.toString().equals(
					order.getPayStatus())) {
				canAuditPay = canAuditclose ? false : true;  //同时申请了关闭和付款，优先处理关闭
				canColse = false;
			}

			/*if (!StringUtils.isEmpty(op)
					&& (OrderStatusType.PICKUP.toString().toLowerCase()
							.equals(op.toLowerCase()) || OrderStatusType.FILLUP
							.toString().toLowerCase().equals(op.toLowerCase()))) {
				canRelate = false;
				canColse = false;
				canapplyPay = false;
				canAudit = false;
				canAuditclose = false;
				canConfirmPay = false;
				canConfirmPayIcbc = false;
			}*/
			// 权限控制
			canAudit = permissionLimit.hasPermission(PERMISSION_AUDIT) ? canAudit
					: false;
			canRelate = permissionLimit.hasPermission(PERMISSION_RELATE) ? canRelate
					: false;
			canapplyPay = permissionLimit.hasPermission(PERMISSION_APPLYPAY) ? canapplyPay
					: false;
			canAuditPay = permissionLimit.hasPermission(PERMISSION_AUDITPAY) ? canAuditPay
					: false;
			canPrintPay = permissionLimit.hasPermission(PERMISSION_PRINTPAY) ? canPrintPay
					: false;
			canConfirmPay = permissionLimit
					.hasPermission(PERMISSION_CONFIRMPAY) ? canConfirmPay
					: false;
			canConfirmPayIcbc = permissionLimit
					.hasPermission(PERMISSION_CONFIRMPAY_ICBC) ? canConfirmPayIcbc
					: false;
			canAuditclose = permissionLimit
					.hasPermission(PERMISSION_CLOSEAUDIT) ? canAuditclose
					: false;
			canColse = permissionLimit.hasPermission(PERMISSION_CLOSEAPPLY) ? canColse
					: false;
			canApplyRollback = permissionLimit.hasPermission(PERMISSION_ROLLBACKAPPLY) ? canApplyRollback
					: false;
			canAuditRollback = permissionLimit.hasPermission(PERMISSION_CLOSEAUDIT) ? canAuditRollback
					: false;
			canConfirmRollback = permissionLimit.hasPermission(PERMISSION_ROLLBACKCONFIRM) ? canConfirmRollback
					: false;
			canPayedRollback = permissionLimit.hasPermission(PERMISSION_ROLLBACKAPPLY) && canPayedRollback;
			fightbackClose = permissionLimit.hasPermission(PERMISSION_PAYBILLPRINT) ? fightbackClose
					: false;
			contractPrint = permissionLimit.hasPermission(PERMISSION_CONFIRMPAYMENT_PRINT) ? contractPrint
					: false;

			boolean printdeliveryeletter = permissionLimit.hasPermission(PERMISSION_PRINT_DELIVERY_LETTER);
			//add by wangxianjun 合同变更查看订单详情，不展示任何按钮
			if("changelist".equals(menu)){
				permission.put("audit", false);
				permission.put("relate", false);
				permission.put("applypay", false);
				permission.put("auditpay", false);
				permission.put("printpay", false);
				permission.put("payconfirm", false);
				permission.put("canConfirmPayIcbc", false);
				permission.put("auditclose", false);
				permission.put("close", false);
				permission.put("applyrollback", false);
				permission.put("auditrollback", false);
				permission.put("confirmrollback", false);
				permission.put("canPayedRollback", false);
				permission.put("fightbackClose", false);
				permission.put("contractPrint", false);
				permission.put("printdeliveryeletter", false);
			}else {
				permission.put("audit", canAudit);
				permission.put("relate", canRelate);
				permission.put("applypay", canapplyPay);
				permission.put("auditpay", canAuditPay);
				permission.put("printpay", canPrintPay);
				permission.put("payconfirm", canConfirmPay);
				permission.put("canConfirmPayIcbc", canConfirmPayIcbc);
				permission.put("auditclose", canAuditclose);
				permission.put("close", canColse);
				permission.put("applyrollback", canApplyRollback);
				permission.put("auditrollback", canAuditRollback);
				permission.put("confirmrollback", canConfirmRollback);
				permission.put("canPayedRollback", canPayedRollback);
				permission.put("fightbackClose", fightbackClose);
				permission.put("contractPrint", contractPrint);
				permission.put("printdeliveryeletter", printdeliveryeletter);
			}
			permission.put("isChange", isChange);

			out.put("permission", permission);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			out.put("bankAccountStartTime", format.format(new Date()));


			// 卖家合计
			ConsignOrderItems totalItems = new ConsignOrderItems();
			if (orderItems != null && orderItems.size() > 0) {
				int count = 0;
				BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, amount = BigDecimal.ZERO, actualPickWeight = BigDecimal.ZERO;
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
					if (item.getActualPickWeightServer() != null) {
						actualPickWeight = actualPickWeight.add(item.getActualPickWeightServer());
					}
				}
				totalItems.setQuantity(count);
				totalItems.setWeight(weight);
				totalItems.setCostPrice(costprice);
				totalItems.setDealPrice(dealprice);
				totalItems.setAmount(amount);
				totalItems.setActualPickWeightServer(actualPickWeight);
				//带打印付款申请,打印付款申请单不做控制,默认显示,其他菜单根据条件控制
				if(!"printpendingpayapply".equals(menu)){
					//若卖家列表银行账号信息有核算会计不通过则不显示打印付款申请单
					if(orderItems.stream().filter(a->(!"Approved".equals(accountService.selectByPrimaryKey(a.getSellerId()).getAccount().getBankDataStatus()))).collect(Collectors.toList()).size() > 0)
						showPrintPaymentFlag = true;
				}
			}
			out.put("totalItem", totalItems);
			out.put("showPrintPaymentFlag", showPrintPaymentFlag);

			Account account = accountService.queryById(order.getDepartmentId());   //edit by rabbit 此处使用部门账号显示余额等信息
			out.put("account", account);

			//查询部门可用信用额度 add by dengxiyan
			out.put("balanceCreditAmount",consignOrderService.calculateBalanceCreditAmountOfDeptByDeptId(order.getDepartmentId()));

			if (getSettledOrderStatus().contains(order.getStatus())) { // 开完放货函显示二次结算信息

				List<ConsignOrderSettleDto> sellerSettleList = consignOrderSettleService
						.getOrderSettleInfo(1, orderId);

				List<ConsignOrderSettleDto> buyerSettleList = consignOrderSettleService
						.getOrderSettleInfo(0, orderId);
				out.put("buyersettle", buyerSettleList);
				out.put("sellersettle", sellerSettleList);
				out.put("viewsettleinfo", true);
			}

			//待关联状态，查询买家金额容差值
			if (StringUtils.equals(ConsignOrderStatus.NEWAPPROVED.getCode(), order.getStatus())) {
				SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
				out.put("buyerToleranceAmount", sysSetting != null ? sysSetting.getSettingValue() : 0);
			}
			//按照自然日计算3日内付款，则不计算业绩,achievementDate 这个日期是指订单创建日期后两天
			if(PaymentType.DELAY_PAYTYPE.getCode().equals(order.getPaymentType()) && Tools.daysDiff(order.getCreated(),new Date()) <= (order.getDelayNum() - 2)){
				out.put("isDelayPay", "true");
				out.put("achievementDate", Tools.getChineseDate(Tools.getDateAfter(order.getCreated(), order.getDelayNum() - 1), "-"));
			}

			// 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
			if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
				permission.put("financeorder", true);
			}
		}

		// 订单列表tab页
		out.put("menu", menu);
	}
	//用变更后的订单更新原订单信息
	public ConsignOrder setOrderByChangeOrder(ConsignOrder order){
		//PENDING_APPR_PAY待审核付款 PENDING_PRINT_PAY待打印付款申请单  PENDING_CONFIRM_PAY待确认付款
			ConsignOrderChange changeOrder = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
			order.setDeliveryType(changeOrder.getDeliveryType());
			order.setDeliveryEndDate(changeOrder.getDeliveryEndDate());
			order.setFeeTaker(changeOrder.getFeeTaker());
			order.setShipFee(changeOrder.getShipFee());
			order.setOutboundTaker(changeOrder.getOutboundTaker());
			order.setOutboundFee(changeOrder.getOutboundFee());
			order.setContractAddress(changeOrder.getContractAddress());
			order.setTransArea(changeOrder.getTransArea());
		return order;
	}


	@RequestMapping("getstepdata.html")
	public @ResponseBody Result getStepData(
			@RequestParam("orderid") Long orderId) {
		Result result = new Result();
		ConsignOrder order = consignOrderService.queryById(orderId);

		List<ConsignOrderStatusDto> ruleStep = consignOrderProcessService
				.getOrderProcessByUserId(order.getOwnerId()).stream().filter(a-> !"待关联银票".equals(a.getOperationName())).collect(Collectors.toList());
		List<ConsignOrderStatusDto> realStep = orderStatusService
				.getAuditDetailById(orderId).stream().filter(a-> !"待关联银票".equals(a.getOperationName())).collect(Collectors.toList());

		if (ruleStep != null && ruleStep.size() > 0) {
			if (realStep != null) {
				for (ConsignOrderStatusDto step : realStep) {
					for (ConsignOrderStatusDto item : ruleStep) {
						if (step.getStatus().equals(item.getStatus())) {
							item.setOperaterName(step.getOperaterName());
							item.setOperationTime(step.getOperationTime());
							item.setMobile(step.getMobile());
							item.setOperationName(getConsignOrderFlowNodeMap()
									.get(item.getStatus()));
							break;
						}
					}
				}
			}
			result.setData(ruleStep);
			result.setSuccess(true);
		} else {
			result.setData("暂无数据");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 修改备注
	 *
	 * @param orderId
	 * @param note
	 * @return
	 */
	@RequestMapping("setnote.html")
	public @ResponseBody Result setnote(@RequestParam("orderId") Long orderId,
										@RequestParam("note") String note) {
		Result result = new Result();
		if (orderId == null) {
			result.setSuccess(false);
			result.setData("备注修改失败，参数错误");
			return result;
		}
		if (StringUtils.isEmpty(note)) {
			result.setSuccess(true);
			return result;
		}

		if (!permissionLimit.hasPermission(PERMISSION_AUDIT)) {
			result.setSuccess(false);
			result.setData("备注修改失败，没有权限");
			return result;
		}
		ConsignOrder order = new ConsignOrder();
		order.setId(orderId);
		User user = getLoginUser();
		order.setLastUpdatedBy(user.getName());
		order.setLastUpdated(new Date());
		order.setNote(note);
		if (consignOrderService.setNote(order)) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		return result;
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
	 * 审核订单
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditOrder)
	@OpParam("orderId")
	@OpParam(name="accept",value="true")
	@RequestMapping("acceptorder.html")
	public @ResponseBody Result acceptOrder(
			@RequestParam("orderid") Long orderId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_AUDIT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		//判断联系人是否存在
		Boolean contactFlag = false;
		ConsignOrder order = consignOrderService.selectByPrimaryKey(orderId);
		Contact contact= contactService.queryByTel(order.getContactTel());
		if(contact == null){
			contactFlag = true;
		}

		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditOrder(orderId, user);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		//add by wangxianjun 20160712 将新增的用户推送到超市系统

		if(contactFlag && ecMQActive!=null && ecMQActive) {
			try {
				marketAddUserSender.withText(order.getContactTel(), order.getContactName());
			} catch (Exception e) {
				logger.error("将新增的用户推送到超市系统失败", e);
			}
		}
		return result;
	}

	/**
	 * 关联订单
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.RelateOrder)
	@OpParam("orderId")
	@OpParam("secondBalanceTakeout")
	@RequestMapping("relateorder.html")
	public @ResponseBody Result relateOrder(
			@RequestParam("orderid") Long orderId,
			@RequestParam("secondbalancetakeout") Boolean secondBalanceTakeout,
			@RequestParam("orderItemsList") String orderItemsList,
			@RequestParam("creditbalancetakeout") Boolean creditBalanceTakeout) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_RELATE)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.relateOrder(orderId, secondBalanceTakeout, user, orderItemsList, creditBalanceTakeout);

		if(isEnabled()){
			//chengui 订单关联成功，推送MQ到超市系统
			noticeEc(resultDto.isSuccess(),orderId,"FINISHED");
		}

		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 订单审核不通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditOrder)
	@OpParam("orderId")
	@OpParam("note")
	@OpParam(name="accept",value="false")
	@RequestMapping("refuseorder.html")
	public @ResponseBody Result refuseOrder(
			@RequestParam("orderid") Long orderId,
			@RequestParam("note") String note) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_AUDIT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditRefuse(orderId, user,
				note);
		//订单关闭时通知超市
		if(isEnabled()) {
			noticeEc(resultDto.isSuccess(),orderId,"CLOSED");
		}
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 申请付款
	 * modify by wangxianjun 迭代9 支持延时支付 ，jsonIsDelayPay 是否提货后3日内付款 true 为是 ， 空为否
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.InitialApplyPay)
	@OpParam("orderId")
	@OpParam("jsonBalanceList")
	@RequestMapping("applypay.html")
	public @ResponseBody Result applyPay(@RequestParam("orderid") Long orderId,
										 @RequestParam("balancelist") String jsonBalanceList,@RequestParam("isDelayPay") String jsonIsDelayPay) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_APPLYPAY)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		Map<Long, Double> moneyMap = new HashMap<Long, Double>();
		Map<Long, Long> bankMap = new HashMap<Long, Long>();
		Map<Long, Boolean> checkedMap = new HashMap<Long, Boolean>();
		Map<Long, Boolean> refundCredit = new HashMap<Long, Boolean>();

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
			}
		}
		ResultDto resultDto = consignOrderService.applyPay(orderId, bankMap, moneyMap, checkedMap, refundCredit, user, jsonIsDelayPay);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;

	}

	/**
	 * 审核付款申请通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditInitialPay)
	@OpParam("orderId")
	@OpParam("payRequestId")
	@OpParam(name="accept",value="true")
	@RequestMapping("auditpayaccept.html")
	public @ResponseBody Result auditPayAccept(
			@RequestParam("orderid") Long orderId,
			@RequestParam("payrequestid") Long payRequestId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_AUDITPAY)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditPayAccept(orderId,
				payRequestId, user);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 审核付款申请不通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditInitialPay)
	@OpParam("orderId")
	@OpParam("payRequestId")
	@OpParam("note")
	@OpParam(name="accept",value="false")
	@RequestMapping("auditpayrefuse.html")
	public @ResponseBody Result auditPayRefuse(
			@RequestParam("orderid") Long orderId,
			@RequestParam("payrequestid") Long payRequestId,
			@RequestParam("note") String note) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_AUDITPAY)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditPayRefuse(orderId,
				payRequestId, user, note);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}
	/**
	 * 订单打回
	 *
	 * @param orderId 订单Id
	 * @param note 打回理由
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.FightbackOrder)
	@OpParam("orderId")
	@OpParam("payrequestId")
	@OpParam("note")
	@ResponseBody
	@RequestMapping("fightback.html")
	public Result fightback(@RequestParam("orderId") Long orderId, @RequestParam("payRequestId") Long payRequestId, @RequestParam("note") String note) {
		if (!permissionLimit.hasPermission(PERMISSION_DETAIL_FIGHTBACK)) {
			return new Result("您没有作该操作的权限", false);
		}
		User user = getLoginUser();
		try {
			consignOrderService.fightback(orderId, payRequestId, note, user);
			return new Result("操作成功，订单已打回！");
		} catch (BusinessException ex) {
			return new Result(ex.getMsg(), false);
		}
	}

	/**
	 * 确认已付款
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.ConfirmInitialPay)
	@OpParam("orderId")
	@OpParam("payRequestId")
	@OpParam(name="accept",value="true")
	@RequestMapping("confirmpay.html")
	public @ResponseBody Result confirmPay(
			@RequestParam("orderid") Long orderId,
			@RequestParam("payrequestid") Long payRequestId,
			@RequestParam("paymentBank") String paymentBank,
			@RequestParam("bankAccountTime") Date bankAccountTime) {
		Result result = new Result();
		if(permissionLimit.hasPermission(PERMISSION_CONFIRMPAY) || permissionLimit.hasPermission(PERMISSION_CONFIRMPAY_ICBC)){
			User user = getLoginUser();
			ResultDto resultDto = consignOrderService.confirmPayment(orderId, payRequestId, user, paymentBank, bankAccountTime);
			result.setSuccess(resultDto.isSuccess());
			result.setData(resultDto.getMessage());
		}else{
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 订单关闭(确认已付款状态) add by kongbinheng 20150906
	 * @param orderId
	 * @param payRequestId
	 * @param cause  订单关闭原因
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.ConfirmInitialPay)
	@OpParam("orderId")
	@OpParam("payRequestId")
	@OpParam(name="accept",value="false")
	@RequestMapping("confirmclose.html")
	public @ResponseBody Result confirmClose(
			@RequestParam("orderid") Long orderId,
			@RequestParam("payrequestid") Long payRequestId,
			@RequestParam("cause")String cause) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_CONFIRMPAY) && !permissionLimit.hasAnyPermissions(PERMISSION_PAYBILLPRINT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.confirmClose(orderId, payRequestId, user, cause);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 申请关闭订单
	 * @param orderId
	 * @param cause 订单关闭原因
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.CloseOrder)
	@OpParam("orderId")
	@RequestMapping("closeorder.html")
	public @ResponseBody Result closeOrder(@RequestParam("orderid") Long orderId,
										   @RequestParam("cause")String cause) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_CLOSEAPPLY)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.close(orderId, user, cause);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 审核关闭订单
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditCloseOrder)
	@OpParam("orderId")
	@OpParam(name="accept",value="true")
	@RequestMapping("auditcloseorder.html")
	public @ResponseBody Result auditCloseOrder(
			@RequestParam("orderid") Long orderId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_CLOSEAUDIT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditClose(orderId, user);

		ConsignOrder order = consignOrderService.queryById(orderId);
		//关联前关闭的订单才通知超市
		boolean isBeforRelate = ConsignOrderStatus.NEWDECLINED.getCode().equals(order.getStatus())
				|| ConsignOrderStatus.CLOSEAPPROVED.getCode().equals(order.getStatus())
				|| ConsignOrderStatus.SYSCLOSED.getCode().equals(order.getStatus());

		//当有需求单的订单关闭时通知超市
		if(isEnabled() && isBeforRelate) {
			noticeEc(resultDto.isSuccess(),orderId,"CLOSED");
		}

		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 审核关闭订单不通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@OpLog(OpType.AuditCloseOrder)
	@OpParam("orderId")
	@OpParam("note")
	@OpParam(name="accept",value="false")
	@RequestMapping("auditcloserefuse.html")
	public @ResponseBody Result auditCloseRefuse(
			@RequestParam("orderid") Long orderId,
			@RequestParam("note") String note) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_CLOSEAUDIT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.auditCloseRefuse(orderId,
				user, note);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 二次结算后申请关闭订单
	 * @param orderId
	 * @param cause 订单关闭原因
	 * @return
	 */
	@OpAction(key="orderId")
	@RequestMapping("rollbackApply.html")
	public @ResponseBody Result rollbackApply(
			@RequestParam("orderid") Long orderId,
			@RequestParam("cause")String cause) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_ROLLBACKAPPLY)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		
		//tuxianming 已经关联的订单不可以关闭
		int total = invoiceInService.totalInvoiceInDetailItemsByOrderId(orderId);
		if(total>0){
			result.setData("当前订单已关联发票，不可以申请关闭。");
			result.setSuccess(false);
			return result;
		}
		
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.rollbackApply(orderId, user, cause);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 审核关闭订单不通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@RequestMapping("rollbackRefuse.html")
	public @ResponseBody Result rollbackRefuse(
			@RequestParam("orderid") Long orderId,
			@RequestParam("note") String note) {
		Result result = new Result();
		ConsignOrder order = consignOrderService.queryById(orderId);
		if (order == null) {
			result.setSuccess(false);
			result.setData("没有找到对应的订单！");
			return result;
		}
		if ((ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode().equals(order.getStatus()) && permissionLimit.hasPermission(PERMISSION_CLOSEAUDIT))
				|| (ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode().equals(order.getStatus()) && permissionLimit.hasPermission(PERMISSION_ROLLBACKCONFIRM))) {
			User user = getLoginUser();
			ResultDto resultDto = consignOrderService.rollbackRefuse(orderId, note, user);
			result.setSuccess(resultDto.isSuccess());
			result.setData(resultDto.getMessage());
			return result;
		} else {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
	}

	/**
	 * 服总审核通过
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@RequestMapping("rollbackAccept.html")
	public @ResponseBody Result rollbackAccept(
			@RequestParam("orderid") Long orderId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_CLOSEAUDIT)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		ResultDto resultDto = consignOrderService.rollbackAccept(orderId, user);
		result.setSuccess(resultDto.isSuccess());
		result.setData(resultDto.getMessage());
		return result;
	}

	/**
	 * 财务审核关闭订单
	 *
	 * @param orderId
	 * @return
	 */
	@OpAction(key="orderId")
	@RequestMapping("rollbackConfirm.html")
	public @ResponseBody Result rollbackConfirm(
			@RequestParam("orderid") Long orderId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_ROLLBACKCONFIRM)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(false);
			return result;
		}
		User user = getLoginUser();
		try {
			consignOrderService.rollBack(orderId, user);
			result.setSuccess(true);
			result.setData("操作成功，订单已关闭！");
		}catch (BusinessException be){
			result.setSuccess(false);
			result.setData(be.getMsg());
		} catch (Exception ex) {
			logger.error(ex.toString());
			result.setSuccess(false);
			result.setData("订单关闭失败！");
		}
		return result;
	}

	@RequestMapping("getaccountbankinfo.html")
	public @ResponseBody Result getAccountBankInfo(
			@RequestParam("accountid") Long accountId) {
		Result result = new Result();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountId", accountId);
		paramMap.put("bankDataStatus", AccountBankDataStatus.Approved.getCode());
		List<AccountBank> bankList = accountBankService.query(paramMap);
		if (bankList != null && bankList.size() > 0) {
			result.setSuccess(true);
			result.setData(bankList);
		} else {
			result.setSuccess(false);
			result.setData("没有获取到银行账号信息");
		}
		return result;
	}

	/**
	 * 审核提现付款Tab
	 *
	 * @return
	 */
	@RequestMapping("withdrawapp.html")
	public String withdrawApplication(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PAYMENT.toString(), out);
		out.put("status", PayStatus.REQUESTED.toString()); // 已申请 待审核
		return "order/query/withdraw";
	}

	@ResponseBody
	@RequestMapping(value = "getwithdrawdata.html", method = RequestMethod.POST)
	public PageResult getWithdrawData(@RequestParam("start") Integer start,
									  @RequestParam("length") Integer length,
									  @RequestParam("status") String status,
									  @RequestParam("ownerName") String ownerName,
									  @RequestParam("startTime") String startTime,
									  @RequestParam("endTime") String endTime,
									  Integer showPrinted,
									  String orderBy,
									  String order) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", status);
		paramMap.put("type", PayRequestType.WITHDRAW.getCode());
		paramMap.put("userIds", getUserIds());
		paramMap.put("userId", getLoginUser().getId());
		paramMap.put("requesterName", ownerName);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("showPrinted", showPrinted);
		paramMap.put("orderBy", orderBy);
		paramMap.put("order", order);
		List<PayRequest> list = payRequestService.query(paramMap);
		Integer total = payRequestService.queryTotal(paramMap);
		return new PageResult(list.size(),total,list);
	}

	/**
	 * 确认提现付款Tab
	 *
	 * @return
	 */
	@RequestMapping("withdrawconfirm.html")
	public String withdrawPrint(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(), out);
		out.put("status", PayStatus.APPLYPRINTED.toString()); // 已打印申请单
		return "order/query/withdraw";
	}

	/**
	 * 待确认已付款-客户银行账号审核Tab
	 * @return
	 */
	@RequestMapping("bankcodeverify.html")
	public String bankcodeverify(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.ConfirmPaymentBankCode.toString(), out);
		out.put("status", PayStatus.APPLYPRINTED.toString()); // 已打印申请单
		return "order/query/bankcodeverify";
	}

	/**
	 * 待确认已付款-客户银行账号审核列表数据
	 * @param accountName
	 * @param startTime
	 * @param endTime
	 * @param orderBy
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "bankcodeverifydata.html", method = RequestMethod.POST)
	public PageResult getBankCodeVerifyData(@RequestParam("accountName") String accountName,
											@RequestParam("startTime") String startTime,
											@RequestParam("endTime") String endTime,
											@RequestParam("start") Integer start,
											@RequestParam("length") Integer length,
											String orderBy,
											String order) {
		List<Account> list = accountService.selectBankCodeVerifyList(accountName, startTime, endTime, orderBy, order, start, length);
		int total = accountService.selectBankCodeVerifyTotal(accountName, startTime, endTime);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 待确认已付款-审核银行账号详情页面
	 * @param out
	 * @param accountId
	 * @return
	 */
	@RequestMapping("account/{accountId}/bankcodeshow")
	public String showData(ModelMap out, @PathVariable Long accountId) {
		out.put("account", accountService.selectByPrimaryKey(accountId));
		return "order/query/bankcodeshow";
	}

	/**
	 * 待确认已付款-审核银行账号-审核通过
	 * @param out
	 * @param accountId
	 * @return
	 */
	@RequestMapping("account/{accountId}/approve")
	@ResponseBody
	public Result approve(ModelMap out, @PathVariable Long accountId) {
		Result result = new Result();
		if(accountService.updateBankDataStatusByPrimaryKey(accountId, AccountBankDataStatus.Approved.getCode(), null, AccountBankDataReminded.Yes.getCode(), getLoginUser().getLoginId()) > 0){
			result.setSuccess(true);
			result.setData("审核通过操作成功！");
		}else{
			result.setSuccess(false);
			result.setData("审核通过操作失败！");
		}
		return result;
	}

	/**
	 * 待确认已付款-审核银行账号-审核不通过
	 * @param out
	 * @param accountId
	 * @param reason
	 * @return
	 */
	@RequestMapping("account/{accountId}/decline")
	@ResponseBody
	public Result decline(ModelMap out, @PathVariable Long accountId, String reason) {
		Result result = new Result();
		if(accountService.updateBankDataStatusByPrimaryKey(accountId, AccountBankDataStatus.Declined.getCode(), reason, null, getLoginUser().getLoginId()) > 0){
			result.setSuccess(true);
			result.setData("审核不通过操作成功！");
		}else{
			result.setSuccess(true);
			result.setData("审核不通过操作失败！");
		}
		return result;
	}

	/**
	 * 打印付款申请单 提现付款Tab
	 *
	 * @return
	 */
	@RequestMapping("withdrawconfirm2.html")
	public String withdrawPrint2(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY.toString(), out);
		out.put("status", PayStatus.APPROVED.toString()); // 已通过审核
		return "order/query/withdraw2";
	}
	/**
	 * 二次结算审核提现付款Tab lixiang
	 *
	 * @param out
	 */
	@RequestMapping("secondpayaudit.html")
	public void secondpayaudit(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PAYMENT.toString(), out);
	}

	/**
	 * 查询二次结算付款申请待审核列表 lixiang
	 *
	 * @param start
	 *            起始
	 * @param length
	 *            每页记录上数
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @param requesterName
	 *            申请人
	 * @return
	 */
	@RequestMapping("secondpayauditother.html")
	public @ResponseBody PageResult secondpayauditother(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd,
			@RequestParam(value = "requesterName", required = false) String requesterName) {

		// 获取当前登录用户ID
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("dateStart", startDate);
		paramMap.put("dateEnd", endDate);
		paramMap.put("status", PayStatus.REQUESTED.toString());
		paramMap.put("requesterName", requesterName);
		List<PayRequestOrgDto> list = secondSettlementLogService.findPayRequest(paramMap);
		int counts = secondSettlementLogService.getPayCounts(paramMap);
		return new PageResult(list.size(),counts,list);
	}


	/**
	 * 通过客户ID查申请金额及客户名称
	 *
	 * @param out
	 * @param orgId
	 *            服务中心id
	 */
	@OpLog(OpType.AuditSettlePay)
	@OpParam("id")
	@OpParam("orgId")
	@OpParam(name="accept",value="true")
	@RequestMapping("secondpayauditskip.html")
	public void secondpayauditskip(ModelMap out, @RequestParam("requestId") Long requestId,
								   @RequestParam("orgId") Long orgId) {
		BusiPayRequest busiPayRequest = secondSettlementLogService.selectById(requestId);
		if (busiPayRequest != null) {
			out.put("busiPayRequest", busiPayRequest);
			out.put("residueLimit", secondSettlementLogService.queryCreditLimit(orgId));// 已用额度
			out.put("limitAmount", organizationDao.queryById(orgId).getCreditLimit());  // 设定额度
		}
		setDefaultTime(out);
	}

	/**
	 * 查询二次结算记账明细
	 *
	 * @param accountId
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
	@RequestMapping("secondpayauditskipother.html")
	public @ResponseBody PageResult getPaymentData(
			@RequestParam("accountId") Long accountId,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd) {
		PageResult result = new PageResult();
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<CustAccountTransLogDto> list = secondSettlementLogService.queryByPayType(accountId, start, length,startDate, endDate);
		int counts = secondSettlementLogService.queryByPayTypeCount(accountId, startDate, endDate);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 确认审核通过
	 *
	 * @return
	 */

	@RequestMapping("updsecondpayauditskip.html")
	@ResponseBody
	public Result secondpayauditskip(@RequestParam("requestId") Long requestId) {
		Result result = new Result();
		try {
			secondSettlementLogService.updatePayRequest(requestId, null, getLoginUser());
			result.setSuccess(true);
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 二次结算付款申请单点击不通过审核按钮弹框前查询该订单审核状态
	 * @param requestId
	 * @return
	 */
	@RequestMapping("find/requset/status.html")
	@ResponseBody
	public Result findRequsetStatus (@RequestParam("requestId") Long requestId) {
		Result result = new Result();
		try {
			payRequestService.selectStatus(requestId);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}


	/**
	 * 审核不通过
	 *
	 * @return
	 */
	@OpLog(OpType.AuditSettlePay)
	@OpParam("requestId")
	@OpParam("reason")
	@OpParam("totalAmount")
	@OpParam("accountId")
	@OpParam(name="accept",value="false")
	@RequestMapping("updsecondpayauditskipno.html")
	public @ResponseBody Result secondpayauditskipno(
			@RequestParam("requestId") Long requestId,
			@RequestParam("reason") String reason,
			@RequestParam("totalAmount") String totalAmount,
			@RequestParam("accountId") Long accountId) {
		Result result = new Result();
		// 获取当前登录用户ID
		User user = getLoginUser();
		String code = payRequestService.createCode();// 生成关联单号
		BigDecimal amount = new BigDecimal(totalAmount);// 申请金额;
		try {
			secondSettlementLogService.updatePayRequest(
					requestId, reason, getLoginUser());
			//回退二次结算余额
//            if(orderStatusService.updatePayment(accountId, code,
//                    null, balanceWithdraw, null, userId, userName, new Date(),
//                    Type.SECONDWITHDRAWALW_THAW.getCode())){
//                result.setSuccess(true);
//            }else{
//                result.setSuccess(false);
//                result.setData("调用资金解冻接口失败");
//            }
			accountFundService.updateAccountFund(accountId, AssociationType.PAYMEN_ORDER, code,
					AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, amount, amount.negate(),
					BigDecimal.ZERO, BigDecimal.ZERO, PayType.FREEZE, user.getId(), user.getName(), new Date());
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 待打印付款申请单Tab
	 *
	 * @param out
	 */
	@RequestMapping("secondpaysettlementaccounting.html")
	public void secondpaysettlementaccounting(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(), out);
	}

	/**
	 * 查询二次结算付款申请列表
	 *
	 * @param start
	 *            起始页
	 * @param length
	 *            记录数
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @param requesterName
	 *            申请人
	 * @return
	 */
	@RequestMapping("secondpayaccountingprint.html")
	public @ResponseBody PageResult secondpayaccountingprint(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd,
			@RequestParam("requesterName") String requesterName) {
		PageResult pageResult = new PageResult();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		String status = PayStatus.APPROVED.toString();// 付款确认只查询已审核通过的
		String statusTwo = PayStatus.CONFIRMEDPAY.toString();// 和已确认付款的
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("dateStartStr", startDate);
		paramMap.put("dateEndStr", endDate);
		paramMap.put("requesterName", requesterName);
		paramMap.put("status", status);
		paramMap.put("statusTwo", statusTwo);
		List<PayRequestOrgDto> list = secondSettlementLogService
				.findPayRequest(paramMap);
		int counts = secondSettlementLogService.getPayCounts(paramMap);
		pageResult.setRecordsFiltered(counts);
		pageResult.setRecordsTotal(counts);
		pageResult.setData(list);
		pageResult.setRecordsTotal(list.size());
		return pageResult;
	}

	/**
	 * 打印二结付款Tab
	 *
	 * @param out
	 */
	@RequestMapping("secondpaysettlement2.html")
	public void secondpaysettlement2(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY.toString(), out);
	}

	/**
	 * 确认二结付款Tab
	 *
	 * @param out
	 */
	@RequestMapping("secondpaysettlement.html")
	public void secondpaysettlement(ModelMap out) {
		processOutData(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(), out);
	}

	/**
	 * 查询二次结算付款申请列表
	 *
	 * @param start
	 *            起始页
	 * @param length
	 *            记录数
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @param requesterName
	 *            申请人
	 * @return
	 */
	@RequestMapping("secondpaysettlementother2.html")
	public @ResponseBody PageResult secondpaysettlementother2(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd,
			@RequestParam(value = "requesterName", required = false) String requesterName,
			Integer showPrinted,
			String orderBy,
			String order) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", getLoginUser().getId());
		paramMap.put("userIds", getUserIds());
		String status = PayStatus.APPROVED.toString();// 待打印申请单只查询已审核通过的
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("dateStart", startDate);
		paramMap.put("dateEnd", endDate);
		paramMap.put("requesterName", requesterName);
		paramMap.put("status", status);
		paramMap.put("showPrinted", showPrinted);
		paramMap.put("orderBy", orderBy);
		paramMap.put("order", order);
		List<PayRequestOrgDto> list = secondSettlementLogService
				.findPayRequest(paramMap);
		int counts = secondSettlementLogService.getPayCounts(paramMap);
		return new PageResult(list.size(),counts,list);
	}

	/**
	 * 查询二次结算付款申请列表
	 *
	 * @param start
	 *            起始页
	 * @param length
	 *            记录数
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @param requesterName
	 *            申请人
	 * @return
	 */
	@RequestMapping("secondpaysettlementother.html")
	public @ResponseBody PageResult secondpaysettlementother(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd,
			@RequestParam(value = "requesterName", required = false) String requesterName,
			Integer showPrinted,
			String orderBy,
			String order) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		paramMap.put("userId", getLoginUser().getId());
		String status = PayStatus.APPLYPRINTED.toString();// 付款确认只查询已打印付款申请单的
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("dateStart", startDate);
		paramMap.put("dateEnd", endDate);
		paramMap.put("requesterName", requesterName);
		paramMap.put("status", status);
		paramMap.put("orderBy", orderBy);
		paramMap.put("order", order);
		List<PayRequestOrgDto> list = secondSettlementLogService
				.findPayRequest(paramMap);
		int counts = secondSettlementLogService.getPayCounts(paramMap);
		return new PageResult(list.size(),counts,list);
	}

	/**
	 * 通过客户ID查申请金额及客户名称
	 *
	 * @param out
	 */
	@RequestMapping("secondpaysettlementconfirm.html")
	public void secondpaysettlementconfirm(ModelMap out,
										   @RequestParam("requestId") Long requestId) {
		BusiPayRequest busiPayRequest = secondSettlementLogService.selectById(requestId);
		if (busiPayRequest != null) {
			out.put("busiPayRequest", busiPayRequest);
		}
		setDefaultTime(out);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		out.put("bankAccountStartTime", format.format(new Date()));
	}

	/**
	 * 查询二次结算记账明细
	 *
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
	@RequestMapping("secondpaysettlementconfirmGo.html")
	public @ResponseBody PageResult secondpaysettlementconfirmGo(
			@RequestParam("accountId") Long accountId,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd) {
		PageResult result = new PageResult();
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = getDateStart(dateStart);
			endDate = getDateEnd(dateEnd);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<CustAccountTransLogDto> list = secondSettlementLogService
				.queryByPayType(accountId, start, length,
						startDate, endDate);
		int counts = secondSettlementLogService.queryByPayTypeCount(accountId, startDate, endDate);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 确认付款
	 *
	 * @return
	 */
	@OpLog(OpType.ConfirmSettlePay)
	@OpParam("id")
	@OpParam("accountId")
	@OpParam("totalAmount")
	@RequestMapping("updconfirm.html")
	public @ResponseBody Result updconfirm(@RequestParam("requestId") Long requestId,
										   @RequestParam("accountId") Long accountId,
										   @RequestParam("totalAmount") String totalAmount,
										   @RequestParam("paymentBank") String paymentBank,
										   @RequestParam("bankAccountTime") Date bankAccountTime) {
		Result result = new Result();
		// 获取当前登录用户ID
		User user = getLoginUser();
		PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
		BigDecimal amount = new BigDecimal(totalAmount);// 申请金额
		try {
			int num = secondSettlementLogService.updConfirmedPay(requestId, paymentBank, bankAccountTime);
			if (num > 0) {
//				orderStatusService.updatePayment(accountId, payRequest.getCode(), null,
//						balanceWithdraw, null, userId, userName, new Date(),
//						Type.SECONDWITHDRAWALW_THAW.getCode());
//				orderStatusService.updatePayment(accountId, payRequest.getCode(), null,
//						balanceWithdraw, null, userId, userName, new Date(),
//						Type.SECONDWITHDRAWALW_CONFIRM.getCode());
//				orderStatusService.updatePayment(accountId, payRequest.getCode(), null,
//						balanceWithdraw, null, userId, userName, new Date(),
//						Type.SECONDWITHDRAWALW_OUT.getCode());
				//解冻二结余额
				accountFundService.updateAccountFund(accountId, AssociationType.PAYMEN_ORDER, payRequest.getCode(),
						AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, amount, amount.negate(),
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.FREEZE, user.getId(), user.getName(), new Date());
				//二次结算账户余额转入资金账户
				accountFundService.updateAccountFund(accountId, AssociationType.PAYMEN_ORDER, payRequest.getCode(),
						AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT, amount, BigDecimal.ZERO, amount.negate(), BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.SETTLEMENT, user.getId(), user.getName(), new Date());
				//资金账户余额转成，提现成功
				accountFundService.updateAccountFund(accountId, AssociationType.PAYMEN_ORDER, payRequest.getCode(),
						AccountTransApplyType.CAPITAL_ACCOUNT_TRANSFER, amount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.SETTLEMENT, user.getId(), user.getName(), new Date());

				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("调用二次结算提现接口失败！");
			}
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			logger.error("付款失败!", e);
		}
		return result;
	}

	/**
	 * 二次结算付款申请单查询
	 *
	 * @param out
	 */
	@RequestMapping("payrequest.html")
	public void selectByBuyerId(ModelMap out,
								@RequestParam("requestId") Long requestId,
								@RequestParam(value = "print", required = false) String print,
								@RequestParam(value = "type", required = false) String type,
								@RequestParam(value = "from", required = false) String from) {
		BusiPayRequest busiPayRequest = secondSettlementLogService.selectById(requestId);
		//查询买家是否使用过银票充值支付
		Account account = paymentOrderService.selectByPrimaryKey(busiPayRequest.getBuyerId());
		//查询该客户下部门是否欠款
		List<Long> departmentId = new ArrayList<Long>();
		departmentId.add(busiPayRequest.getDepartmentId());
		List<AccountDto> accountList = accountService.queryForBalance(departmentId);
		out.put("accountList", accountList);
		if (account.getIsAcceptDraftCharged() == 1) {//如果使用了银票支付
			out.put("isAcceptDraftCharged", account.getIsAcceptDraftCharged());
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
		out.put("showRemindedFlag", showRemindedFlag);
		out.put("showPrintPaymentFlag", showPrintPaymentFlag);

		PayRequestItemDto payRequestItemDto = secondSettlementLogService.findByBuyerId(requestId, busiPayRequest.getStatus(), type);
		BigDecimal numberOfMoney = busiPayRequest.getTotalAmount();
		String totalAmountCapital = NumberToCNUtils.number2CNMontrayUnit(numberOfMoney);// 大写总金额
		/*
		ConsignOrderStatusDto consignOrderStatusDto = paymentOrderService
				.getByUserIdAndStatus(busiPayRequest.getRequesterId(),
						"APPROVED");
		*/
		if(payRequestItemDto != null){
			out.put("payRequestItemDto", payRequestItemDto);
		}
		Organization organization = paymentOrderService.findById(busiPayRequest.getOrgId());
		String operatorName = "";
		if(organization.getCharger() != null){
			User operatorUser = userService.queryById(organization.getCharger());
			if(operatorUser != null){
				operatorName = operatorUser.getName();
			}
		}
		out.put("paymentLabel", paymentLabel);  //客户预付款标识
		out.put("totalAmountCapital", totalAmountCapital);
		out.put("operatorName", operatorName);//服务中心总经理
		out.put("print", print);
		out.put("status", busiPayRequest.getStatus());
		out.put("orgName", organization.getName());
		out.put("requestId", requestId);
		out.put("clientIp", busiPayRequest.getLastPrintIp());
		if (type != null) {
			out.put("type", type);
		}
		if (from != null) {
			out.put("from", from);
		}
	}

	/**
	 * 核算会计打回二结提现、账户提现申请单到待审核
	 * @param requestId
	 */
	@RequestMapping("callBack/pendingAudit.html")
	@ResponseBody
	public HashMap<String, Object> CallBackToAudit(@RequestParam("requestId") Long requestId) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		User user = getLoginUser();
		try {
			payRequestService.callBackPayRequest(requestId, user);
			result.put("success", true);
			PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
			result.put("type", payRequest.getType());
		} catch (BusinessException e) {
			PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
			result.put("type", payRequest.getType());
			result.put("success", false);
			result.put("data", e.getMsg());
		}
		return result;
	}

	@RequestMapping("select/Ip.html")
	@ResponseBody
	public HashMap<String, Object> selectByRquestId(@RequestParam("requestId") Long requestId) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		BusiPayRequest request = secondSettlementLogService
				.selectById(requestId);
		if(request != null){
			result.put("success", true);
			result.put("ip",request.getLastPrintIp());
			result.put("printName", request.getPrintName());
			result.put("printDate", request.getLastPrintDate());
			return result;
		} else {
			result.put("success", false);
		}

		return result;
	}

	/**
	 * 打印付款申请单修改打印次数
	 *
	 * @param out
	 * @param requestId request_ID
	 * @param printTimes
	 * @return
	 */
	@RequestMapping("updateprintcounts.html")
	@ResponseBody
	public HashMap<String, Object> updatePrintCounts(HttpServletRequest request, ModelMap out,
													 @RequestParam("requestId") Long requestId,
													 @RequestParam("printTimes") Integer printTimes) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		int printCounts = printTimes + 1;
		User user=getLoginUser();
		PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
		try {
			paymentOrderService.updateForId(requestId, printCounts, Tools.getIpAddr(request), user);
			List<Long> receiverIdList =  paymentOrderService.queryReceiverIdByRequestId(requestId);
			for(Long receiverId : receiverIdList){
				accountService.updateBankDataStatusByPrimaryKey(receiverId, null, null, AccountBankDataReminded.No.getCode(), getLoginUser().getLoginId());
			}
			result.put("success", true);
			result.put("type", payRequest.getType());
		} catch (BusinessException e) {
			result.put("success", false);
			result.put("data", e.getMsg());
			result.put("type", payRequest.getType());
		}
		return result;
	}

	/**
	 * 起始日期
	 *
	 * @return
	 * @throws ParseException
	 */
	private Date getDateStart(String startTime) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		if (StringUtils.isNotEmpty(startTime)) {
			dateStart = format.parse(startTime);
		} else {
			return null;
		}
		return dateStart;
	}

	/**
	 * 终止日期
	 *
	 * @return
	 * @throws ParseException
	 */
	private Date getDateEnd(String endTime) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(endTime)) {
			dateEnd = new Date(
					format.parse(endTime).getTime() + 24 * 3600 * 1000);
		} else {
			return null;
		}
		return dateEnd;
	}


	/**
	 * 查询资金账户余额和二次结算余额和可用信用额度
	 *
	 * @return
	 * @author DQ
	 */
	@RequestMapping("queryBalanceById.html")
	public @ResponseBody Result queryBalanceById(
			@RequestParam(value = "accountId", required = false) Long accountId) {
		Result result = new Result();
		if (accountId == null
				|| !permissionLimit.hasPermission(PERMISSION_PAGEVIEW)) {
			result.setSuccess(false);
			return null;
		}
		Account account = accountService.queryById(accountId);
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("balance", account.getBalance().toString());
		resultMap.put("balanceSecondSettlement", account
				.getBalanceSecondSettlement().toString());
		resultMap.put("blanceCredit",consignOrderService.calculateBalanceCreditAmountOfDeptByDeptId(accountId).toString());
		result.setSuccess(true);
		result.setData(resultMap);
		return result;
	}

	//获取可选银票信息
	@RequestMapping("getAvailableDraft.html")
	public @ResponseBody Result getAvailableDraft(
			@RequestParam(value = "accountId") Long accountId) {
		Result result = new Result();

		AcceptDraftQuery query=new AcceptDraftQuery();
		query.setAccountId(accountId);
		query.setStatus(AcceptDraftStatus.CHARGED.getCode());
		try {
			List<AcceptDraft> list = acceptDraftService.queryByParam(query);
			result.setSuccess(true);
			result.setData(list);
		}catch(Exception ex){
			result.setSuccess(false);
			result.setData("获取银票数据失败");
		}
		return result;
	}

	//关联银票信息
	@RequestMapping("addDraft.html")
	public @ResponseBody Result addDraft(@RequestParam(value = "itemId") Long itemId, @RequestParam(value = "draftId") Long draftId,
										 @RequestParam(value = "draftCode") String draftCode) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			ResultDto resultDto = consignOrderService.addDraft(itemId, draftId, draftCode, user);
			result.setSuccess(resultDto.isSuccess());
			result.setData(resultDto.getMessage());
		} catch (BusinessException ex) {
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}

	//获取订单关联后的所有状态
	private List<String> getRelatedOrderStatus() {
		List<String> list = new ArrayList<>();
		list.add(ConsignOrderStatus.RELATED.getCode());
		list.add(ConsignOrderStatus.CLOSEREQUEST2.getCode());
		list.add(ConsignOrderStatus.SECONDSETTLE.getCode());
		list.add(ConsignOrderStatus.INVOICEREQUEST.getCode());
		list.add(ConsignOrderStatus.INVOICE.getCode());
		list.add(ConsignOrderStatus.FINISH.getCode());
		list.add(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
		list.add(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
		return list;
	}

	//获取订单二结后的所有状态
	private List<String> getSettledOrderStatus() {
		List<String> list = new ArrayList<>();
		list.add(ConsignOrderStatus.INVOICEREQUEST.getCode());
		list.add(ConsignOrderStatus.INVOICE.getCode());
		list.add(ConsignOrderStatus.FINISH.getCode());
		list.add(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
		list.add(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
		return list;
	}

	//获取提醒亏损的付款状态
	private List<String> getRemindLossPayStatus() {
		List<String> list = new ArrayList<>();
		list.add(ConsignOrderPayStatus.APPLY.getCode());
		list.add(ConsignOrderPayStatus.REQUESTED.getCode());
		list.add(ConsignOrderPayStatus.APPROVED.getCode());
		list.add(ConsignOrderPayStatus.APPLYPRINTED.getCode());
		return list;
	}

	@RequestMapping("receipts.html")
	public String queryReceipts(ModelMap out, Long requestitemid, Long orderid, 
			@RequestParam(value = "pay", required = false) String pay) {
		ConsignOrder order = consignOrderService.selectByPrimaryKey(orderid);
		if (order != null) {
			out.put("order", order);
		}
		PayRequestItems requestItem = payRequestItemsService.selectByPrimaryKey(requestitemid);
		if (requestItem == null) {
			return null;
		} else {
			out.put("requestItem", requestItem);
		}
		IcbcBdDto icbcBdlDetail = bdlService.queryBankReceipts(requestItem.getPayAmount(), requestItem.getReceiverName(), requestItem.getReceiverAccountCode());
		if (icbcBdlDetail != null) {
			String txAmount = NumberToCNUtils.number2CNMontrayUnit(requestItem.getPayAmount());
			out.put("icbcBdlDetail", icbcBdlDetail);
			out.put("txAmount", txAmount);
			out.put("icbcAmount", new BigDecimal(icbcBdlDetail.getCreditAmount()));//小写
		}
		BankOriginalDto bankOriginalDto = bankOriginalService.queryBankReceipts(requestItem.getPayAmount(), requestItem.getReceiverName(), requestItem.getReceiverAccountCode());
		if (bankOriginalDto != null) {
			String debitAmount = NumberToCNUtils.number2CNMontrayUnit(requestItem.getPayAmount());
			out.put("bankOriginalDto", bankOriginalDto);
			out.put("debitAmount", debitAmount);//大写
			out.put("spdbAmount", new BigDecimal(bankOriginalDto.getTxamount()));//小写
		}
		if (null != pay) {
			out.put("pay", true);
		}
		return "/order/query/receipts";
	}

	/**
	 * 通过交易单号查询订单
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("checkorder.html")
	@ResponseBody
	public Result checkOrder(@RequestParam(value = "code") String code) {
		ConsignOrder order = consignOrderService.selectByCode(code);
		Result result = new Result();
		if(order != null){
			result.setSuccess(Boolean.TRUE);
			result.setData(order);
		}else{
			result.setSuccess(Boolean.FALSE);
			result.setData("该笔交易单号无效，请重新输入！");
		}

		return result;
	}
	/**
	 * 进入设置正确的返利手机号页面
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("modifyordercontact.html")
	public void modifyOrderContact(ModelMap out) {

	}
	/**
	 * 通过交易单号查询订单，及该笔订单对应买家下所有的联系人
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("queryOrderContact.html")
	@ResponseBody
	public Result queryOrderContact(@RequestParam(value = "code") String code) {
		Result result = new Result();
		if(code != null && !"".equals(code)) {
			OrderContactDto orderContact = consignOrderService.queryOrderContactByCode(code);
			List<AccountContact> accountContactList = accountContactService.queryContactListByAccountId(orderContact.getAccountId());
			orderContact.setAccountContactList(accountContactList);
			result.setData(orderContact);
			result.setSuccess(Boolean.TRUE);
		}else {
			result.setSuccess(Boolean.FALSE);
		}
		return  result;
	}
	/**
	 * 通过交易单号更新交易单和返利报表买家联系人信息
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("updateorderRebates.html")
	@ResponseBody
	public Result updateOrderRebates(@RequestParam("orderJson") String orderJson) {
		Result result = new Result();
		User user = getLoginUser();
		ObjectMapper mapper = new ObjectMapper();
		Double rebateAmount;
		String oldContactTel;
		try {
			JsonNode orderNode = mapper.readTree(orderJson);
			rebateAmount = orderNode.path("rebateAmount").asDouble();
			oldContactTel = orderNode.path("oldContactTel").asText();
			ConsignOrder order =  jsonToConsignOrder(orderNode, user);
			if(rebateAmount == 0){//二结前
				if(consignOrderService.updateByCodeSelective(order) > 0){
					result.setSuccess(Boolean.TRUE);
					result.setData("更新交易单买家联系人相关信息成功！");
				}else{
					result.setSuccess(Boolean.FALSE);
					result.setData("更新交易单买家联系人相关信息失败！");
				}
			}else {
				//二结后 调用返利接口
				result = callLvInterface(order, oldContactTel, rebateAmount);
			}
		}catch(BusinessException be){
			result.setSuccess(Boolean.FALSE);
			result.setData(be.getMsg());
		} catch (IOException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}
	private ConsignOrder jsonToConsignOrder(JsonNode orderNode,User user){
		ConsignOrder order = new ConsignOrder();
		try {
			order = consignOrderService.selectByCode(orderNode.path("code").asText());
			order.setContactId(orderNode.path("newContactId").asLong());
			order.setContactName(orderNode.path("newContactName").asText());
			order.setContactTel(orderNode.path("newContactTel").asText());
			order.setLastUpdatedBy(user.getName());
			order.setLastUpdated(new Date());
			order.setModificationNumber(order.getModificationNumber() + 1);
		} catch (Exception e) {
			// handle error
		}
		return order;
	}
	private Result callLvInterface(ConsignOrder order,String oldContactTel,Double rebateAmount) {
		Result result = new Result();
		try {
			int lv = consignOrderService.correctRebateByPhone(oldContactTel, order.getContactTel(), rebateAmount);
			//	0：验证失败，1：成功，-1：服务器错误，-2：手机号码不存在，-3：修改失败，-4：余额不足，-5：参数错误
			if (lv == 1) {
				if (consignOrderService.updateByCodeSelective(order) > 0 && rewardReportService.updateByCodeSelective(order) > 0) {
					result.setSuccess(Boolean.TRUE);
					result.setData("更新交易单和返利报表买家联系人相关信息成功！");
				} else {
					result.setSuccess(Boolean.FALSE);
					result.setData("更新交易单和返利报表买家联系人相关信息失败！");
				}
			} else if (lv == 0) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:数据验证失败！");
			} else if (lv == -1) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:服务器错误！");
			} else if (lv == -2) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:手机号码不存在！");
			} else if (lv == -3) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:修改失败！");
			} else if (lv == -4) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:余额不足！");
			} else if (lv == -5) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用返利接口:参数错误！");
			}
		}catch(Exception ex){
			result.setSuccess(Boolean.FALSE);
			result.setData("调用返利接口错误:" + ex.getMessage());
		}
		return result;
	}
	/**
	 * 进入关联进项票列表页面
	 */
	@RequestMapping("relateininvoiceshow.html")
	@ResponseBody
	public Result relateInInvoiceShow(@RequestParam("orderItemid") String orderItemId) {
		Result result = new Result();
		ConsignOrderItemsInvoiceDto consignOrderItemsInvoiceDto = consignOrderItemsService.queryOrderItemsById(Long.valueOf(orderItemId));
		if(consignOrderItemsInvoiceDto != null) {
			List<OrderItemsInvoiceInInfoDto> orderItemsInvoiceInInfoList = consignOrderItemsService.queryOrderItemsInInvoice(Long.valueOf(orderItemId));
			consignOrderItemsInvoiceDto.setOrderItemsInvoiceInInfoList(orderItemsInvoiceInInfoList);
			result.setData(consignOrderItemsInvoiceDto);
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 通过交易单号修改返利
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("modifyOrderRebates.html")
	@ResponseBody
	public Result modifyOrderRebates(@RequestParam("orderJson") String orderJson) {
		Result result = new Result();
		User user = getLoginUser();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode orderNode = mapper.readTree(orderJson);
			ConsignOrder order  = consignOrderService.selectByCode(orderNode.path("code").asText());
			if(!orderNode.path("newContactTel").asText().equals(order.getContactTel())){
				if(updateConsignOrder(order, orderNode, user) <= 0){
					result.setSuccess(Boolean.FALSE);
					result.setData("更新交易单买家联系人相关信息失败！");
					return result;
				}
			}
			List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderId(order.getId());
			List<Long> custList = new ArrayList<Long>();
			custList.add(order.getAccountId());
			for(ConsignOrderItems item:items){
				if(!custList.contains(item.getSellerId())){
					custList.add(item.getSellerId());
				}
			}
			for(Long sellId:custList){
				rebateService.addRebateByOrder(order.getId(), user,sellId);
			}
			result.setData("修改返利成功！");
			result.setSuccess(true);
		}catch(BusinessException be){
			result.setSuccess(Boolean.FALSE);
			result.setData(be.getMsg());
		} catch (IOException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}
	private int  updateConsignOrder(ConsignOrder order,JsonNode orderNode,User user){
		int count = 0;
		try {
			order.setContactId(orderNode.path("newContactId").asLong());
			order.setContactName(orderNode.path("newContactName").asText());
			order.setContactTel(orderNode.path("newContactTel").asText());
			order.setLastUpdatedBy(user.getName());
			order.setLastUpdated(new Date());
			order.setModificationNumber(order.getModificationNumber() + 1);
			count = consignOrderService.updateByCodeSelective(order);
		} catch (Exception e) {
			// handle error
		}
		return count;
	}
	/**
	 * 通过交易单号回滚返利
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("rebackOrderRebates.html")
	@ResponseBody
	public Result rebackOrderRebates(@RequestParam("orderJson") String orderJson) {
		Result result = new Result();
		User user = getLoginUser();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode orderNode = mapper.readTree(orderJson);
			String newPhone = orderNode.path("newContactTel").asText();
			String contactName = orderNode.path("newContactName").asText();
			Double rebateAmount = orderNode.path("rebateAmount").asDouble();
			ConsignOrder order  = consignOrderService.selectByCode(orderNode.path("code").asText());
			if(!newPhone.equals(order.getContactTel())){
				if(updateConsignOrder(order, orderNode, user) <= 0){
					result.setSuccess(Boolean.FALSE);
					result.setData("更新交易单买家联系人相关信息失败！");
					return result;
				}
			}
			String lvMsg = consignOrderService.addRebateByPhone(0-rebateAmount,newPhone,contactName,order.getAccountId());
			if (!"操作成功".equals(lvMsg)) {
				result.setSuccess(Boolean.FALSE);
				result.setData("调用超市接口增加用户返利失败--" + lvMsg);
				return result;
			}   //超市接口增加返利
			else {
				if(consignOrderService.disableRebateByOrderCode(order.getCode()) <= 0){
					result.setSuccess(Boolean.FALSE);
					result.setData("删除返利数据失败！");
					return result;
				}
			}
			result.setData("返利回滚成功！");
			result.setSuccess(true);
		}catch(BusinessException be){
			result.setSuccess(Boolean.FALSE);
			result.setData(be.getMsg());
		} catch (IOException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}
	/**
	 * 申请付款前，查看合同是否上传
	 *
	 * @return
	 * @author wangxianjun
	 */
	@RequestMapping("checkcontractupload.html")
	@ResponseBody
	public Result checkContractUpload(@RequestParam(value = "orderid") Long orderId,@RequestParam(value = "type",required = false) String type
			,@RequestParam(value = "orderChangId",required = false) Integer orderChangId
			,@RequestParam(value = "applymoney",required = false) BigDecimal applyMoney) {
		int count =0;
		//type为change是变更合同校验合同是否上传
		Result result = new Result();
		if(applyMoney != null && applyMoney.compareTo(BigDecimal.ZERO)==0){
			result.setSuccess(Boolean.TRUE);
			return result;
		}
		if("change".equals(type))
				count = consignOrderService.countUnChangeUploadByOrderId(orderId,orderChangId);
		else
				count = consignOrderService.countUnUploadByOrderId(orderId);

		//chengui 卖家客户（白名单、非白名单、准供应商）付款申请是否需要上传合同，settingValue为0不需要，1需要。
		List<ConsignOrderItems> items = consignOrderService.queryOrderItemsById(orderId);
		Account account = accountService.queryById(items.get(0).getSellerId());
		String settingValue = sysSettingDao.querySettingValueByName(account.getSupplierLabel());


		if(count > 0 && StringUtils.equals("1", settingValue)){
			result.setSuccess(Boolean.FALSE);
			result.setData("必须上传卖家合同后才能申请付款！");

		}else{
			result.setSuccess(Boolean.TRUE);
		}

		return result;
	}
	/**
	 * 二结后订单详情页面上传回单图片
	 * modify by wangxianjun 20160229
	 * @param certId 凭证id,credentName 凭证名称，isBatch 是否批量 ，certtype 凭证类型
	 * @return
	 */
	@RequestMapping("uploadPic.html")
	@ResponseBody
	public Result uploadPic(MultipartHttpServletRequest request,@RequestParam("certid") Long certId,@RequestParam("credentName") String credentName,@RequestParam("isBatch") String isBatch) {
		Result result = new Result();
		if(certId == null){
			result.setData("凭证上传失败,凭证id为空");
			result.setSuccess(false);
		}

		try {
			List<MultipartFile> orderPicList = request
					.getFiles("pic_cert");
			List<MultipartFile> attachmentList = new ArrayList<MultipartFile>();
			if (orderPicList != null && !orderPicList.isEmpty()) {
				for (MultipartFile file : orderPicList) {
					if (!checkUploadAttachment(file, result, attachmentList)) {
						return result;
					}
				}

			}
			
			consignOrderService.updateOrderPic(attachmentList, getLoginUser(),certId,"cert");
			
			if(credentName != null && !"".equals(credentName)){
				//第一次上传凭证，要更新凭证表的凭证名称
				result = updatePrintcateInfo(certId,credentName,isBatch);
				if(!result.isSuccess()){
					return result;
				}
			}
			Map map = new HashMap();
			map.put("orderId",certId);
			map.put("type","cert");
			result.setData(consignOrderService.getAttachmentByOrderId(map));
			result.setSuccess(true);
				

		} catch (BusinessException e) {
			result.setData(e.getMsg());
			result.setSuccess(false);
			logger.info(e.getMessage(), e);
		}catch (Exception e){
			result.setData("上传失败");
			result.setSuccess(false);
			logger.info(e.getMessage(), e);
		}
		return result;
	}
	/**
	 * wangxianjun
	 * 更新凭证信息
	 */
	public Result updatePrintcateInfo(Long certId,String credentName,String isBatch) {
		//保存到Certificate
		Result result = new Result();
		try {
			BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
			credential.setId(certId);
			if("0".equals(isBatch)){
				credential.setName(credentName);
			}else{
				credential.setName("(批)" + credentName);
			}
			if (certificateService.updateByIdSelective(credential) > 0) {
				result.setSuccess(true);
				result.setData("更新凭证名称成功");
			} else {
				result.setSuccess(false);
				result.setData("更新凭证名称失败");
			}
		}catch (Exception e){
			result.setSuccess(false);
			result.setData("更新凭证名称失败：" + e.getMessage());
		}
		return result;
	}

	private boolean checkUploadAttachment(MultipartFile file, Result result,List<MultipartFile> attachmentList) {
		String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

		if (suffix == null
				|| !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
			result.setData("凭证文件格式不正确");
			result.setSuccess(false);
			return false;
		}
		if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
			result.setData( "凭证超过" + Constant.MAX_IMG_SIZE + "M");
			result.setSuccess(false);
			return false;
		}
		attachmentList.add(file);
		return true;
	}

	/**
	 * 删除二结后订单详情页面的回单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */
	@RequestMapping("deletePic.html")
	@ResponseBody
	public Result deletePic(@RequestParam("imgId") Long imgId,@RequestParam("certid") Long certId,@RequestParam(value = "orderByPageNumber",defaultValue = "") String orderByPageNumber) {
		Result result = new Result();
		//String checkResult;
		try {

			boolean isSave = consignOrderService.deletePic(imgId).isSuccess();
			if (isSave) {
				Map map = new HashMap();
				map.put("orderId",certId);
				map.put("type","cert");
				map.put("orderByPageNumber",orderByPageNumber);
				result.setData(consignOrderService.getAttachmentByOrderId(map));
				result.setSuccess(true);
				
				//更新状态
				BusiConsignOrderCredential certi = certificateService.findById(certId);
				
				int total = attachmentService.totalByCredentialId(certi.getId());
				Constant.CREDENTIAL_UPLOAD_STATUS uploadStatus = null;
				if(total>0 && certi.getCredentialNum()>total){
					uploadStatus = Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT;
				}else if(certi.getCredentialNum() == total){
					uploadStatus = Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT;
				}
				
				if(uploadStatus!=null){
					BusiConsignOrderCredential updateCerti = new BusiConsignOrderCredential();
					updateCerti.setUploadStatus(uploadStatus.toString());
					updateCerti.setCredentialCode(certi.getCredentialCode());
					certificateService.updateByCertSelective(updateCerti);
				}
					
				
				
			}
		} catch (BusinessException e) {
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 预付款待审核tab
	 * @author lixiang
	 * @param out
	 */
	@RequestMapping("paymentapplication.html")
	public void queryAuditPayMent(ModelMap out) {
		out.put("audit", permissionLimit.hasPermission(PERMISSION_PAYMENTAPPLICATION_AUDIT));
		out.put("status", PayStatus.REQUESTED.toString());
		processOutData(Constant.ConsignOrderTab.PAYMENT.toString(), out);
	}

	@RequestMapping("get/paymentapplication.html")
	@ResponseBody
	public  PageResult getPaymentapplication(PayRequestQuery payRequestQuery){
		PageResult result = new PageResult();
		payRequestQuery.setStatus(PayStatus.REQUESTED.toString());
		try {
			payRequestQuery.setEndTime(getDateEndStr(payRequestQuery.getEndTime()));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<PayRequstDto> list = payRequestService.queryPayMentAudit(payRequestQuery);
		int counts = payRequestService.queryPayMentAuditCount(payRequestQuery);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	@RequestMapping("printpayment.html")
	public void printPayment(ModelMap out) {
		out.put("permPrint", permissionLimit.hasPermission(PERMISSION_PRINTPAYMENT_PRINT));
		out.put("status", PayStatus.APPROVED.toString());
		processOutData(Constant.ConsignOrderTab.PRINTPENDINGPAYAPPLY.toString(), out);
	}

	@RequestMapping("printpaymentquery.html")
	@ResponseBody
	public PageResult printPaymentQuery(PayRequestQuery payRequestQuery) {
		PageResult result = new PageResult();
		payRequestQuery.setStatus(PayStatus.APPROVED.toString());
		try {
			payRequestQuery.setEndTime(getDateEndStr(payRequestQuery.getEndTime()));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<PayRequstDto> list = payRequestService.queryPayMentAudit(payRequestQuery);
		int counts = payRequestService.queryPayMentAuditCount(payRequestQuery);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 终止日期（String类型）
	 * @author lixiang
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	private String getDateEndStr(String endTime) throws ParseException {
		if (endTime == null || "".equals(endTime)) {
			return null;
		}
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

	/**
	 * 预付款详情
	 * @author lixiang
	 */
	@RequestMapping("{requestId}/requestdetail.html")
	public String requestDetail(ModelMap out, @PathVariable Long requestId) {
		PayRequest payRequest = payRequestService.selectByPrimaryKey(requestId);
		Boolean success = false;
		if (payRequest != null) {
			List<PayRequestItems> list = payRequestItemsService.selectByRequestId(requestId);
			Integer index = 0;  // 预付款申请只有一条记录
			PayRequestItems payRequestItems = list.get(index);
			success = true;
			out.put("departmentNums", allowanceService.queryDepartmentByAccoutId(payRequest.getBuyerId()).size());
			out.put("payRequestItems", payRequestItems);
			out.put("status", payRequest.getStatus());
		}
		out.put("success", success);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		out.put("bankAccountStartTime", format.format(new Date()));
		return "order/query/requestdetail";
	}

	/**
	 * 预付款审核
	 * @author lixiang
	 */
	@RequestMapping(value = "check/advance/payment.html", method = RequestMethod.POST)
	@ResponseBody
	public Result checkAdvancePayment(@RequestParam("requestId") Long requestId,
									  @RequestParam("check") Boolean check,
									  @RequestParam("declineReason") String declineReason) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			payRequestService.checkAdvance(user, requestId, check, declineReason);
			result.setSuccess(true);
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 待确认付款tab
	 * @author lixiang
	 * @param out
	 * @return
	 */
	@RequestMapping("confirm/request.html")
	public String confirmRequest(ModelMap out) {
		boolean s = permissionLimit.hasPermission(PERMISSION_CONFIRM_REQUEST_AUDIT);
		out.put("enter", s);
		out.put("status", PayStatus.APPLYPRINTED.toString());
		processOutData(Constant.ConsignOrderTab.CONFIRMPAYMENT.toString(), out);
		return "order/query/printpayment";
	}

	@RequestMapping("get/confirm/request.html")
	@ResponseBody
	public  PageResult getConfirmRequest(PayRequestQuery payRequestQuery){
		PageResult result = new PageResult();
		try {
			payRequestQuery.setEndTime(getDateEndStr(payRequestQuery.getEndTime()));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		List<PayRequstDto> list = payRequestService.queryPayMentAudit(payRequestQuery);
		int counts = payRequestService.queryPayMentAuditCount(payRequestQuery);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 确认预付款付款
	 */
	@ResponseBody
	@RequestMapping(value = "confirmrequest.html", method = RequestMethod.POST)
	public Result confirmRequest(@RequestParam("id") Long id,
								 @RequestParam("paymentBank") String paymentBank,
								 @RequestParam("bankAccountTime") Date bankAccountTime) {
		Result result = new Result();
		User user = getLoginUser();
		try{
			payRequestService.confirmRequest(user, id, paymentBank, bankAccountTime);
			result.setSuccess(true);
		}catch (BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 交易凭证tab
	 * tuxianming
	 * @param out
	 */
	@RequestMapping("tradecertificate.html")
	public void tradeCertificate(ModelMap out,String accountType) {
		
		/*List<Long> ids = getUserIds();
		Map<String, Object> userIds = new HashMap<>();
		userIds.put("userIds", ids);
		List<User> users = userService.queryByParam(userIds);
		
		out.put("users", users);*/
		if(StringUtils.isBlank(accountType))
			accountType = "seller";

		out.put("user", getLoginUser());
		out.put("accountType", accountType);
		//得到数据
		processOutData(Constant.ConsignOrderTab.TRADECERTIFICATE.toString(), out);
	}

	/**
	 * 交易凭证 --> 批量打印
	 * tuxianming
	 * @param out
	 */
	@RequestMapping("tradecertificateforbatch.html")
	public void tradeCertificateForBatch(ModelMap out, String type) {
		
		/*List<Long> ids = getUserIds();
		Map<String, Object> userIds = new HashMap<>();
		userIds.put("userIds", ids);
		List<User> users = userService.queryByParam(userIds);
		
		out.put("users", users);*/
		out.put("user", getLoginUser());

		out.put("type", type);
		setDefaultTime(out);
	}

	/**
	 * 加载交易凭证 --> 批量打印（买）卖家凭证 和 打印（买）卖家凭证
	 * query:isNeedPage-true 批量加载
	 * tuxianming 
	 * @param
	 */
	@ResponseBody
	@RequestMapping("loadtradecertificate.html")
	public PageResult loadSellerTradeCertificate(OrderTradeCertificateQuery query,
												 @RequestParam("start") Integer start,
												 @RequestParam("length") Integer length) {
		if(query.getCertificateNO() == null)
			query.setUserIds(getUserIds());	//查看权限
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}
		if(query.getNeedPage())
			query.setStart(start).setLength(length);
		else
			query.setStart(null).setLength(null);

		query.setGroupBySeller(true)					//按卖家分组
				.setCredentialNull(true)					//凭证号为空
				.setCredentialNullOfBatch((query.getCredentialNullOfBatch()==null||!query.getCredentialNullOfBatch())?false:true)				//批量凭证号为空
				.setCloseStatus(true);						//不显示关闭的订单

		List<OrderItemDetailDto> orderitems = consignOrderService.selectOrdersByParams(query);
		String certType = query.getAccountType() != null ? query.getAccountType() :query.getCredentialType();
		List<OrderTradeCertificateDto> dtos = buildOrderTradeCertificate(orderitems, query.getMoreInfo(), certType);

		PageResult result = new PageResult();
		result.setData(dtos);
		if(query.getNeedPage()!=null && query.getNeedPage()==true){
			int total = consignOrderService.selectTotalOrdersByParams(query);
			result.setRecordsFiltered(total);
		}
		result.setRecordsTotal(dtos.size());

		return result;
	}

	/**
	 * 加载交易凭证 --> 批量打印（买）卖家凭证 和 打印（买）卖家凭证详情
	 * query:isNeedPage-true 批量加载
	 * tuxianming 
	 * @param
	 */
	@ResponseBody
	@RequestMapping("tradecertificatelist.html")
	public PageResult tradecertificateList(OrderTradeCertificateQuery query,
										   @RequestParam("start") Integer start,
										   @RequestParam("length") Integer length) {
		List<Long> ids = getUserIds();
		query.setUserIds(ids);
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}
		if(query.getNeedPage())
			query.setStart(start).setLength(length);
		else
			query.setStart(null).setLength(null);

		query.setGroupBySeller(true)					//按卖家分组
				.setCredentialNull(true)					//凭证号为空
				.setCredentialNullOfBatch(true)				//批量凭证号为空
				.setCloseStatus(true);						//不显示关闭的订单

		List<OrderTradeCertificateDto> orderitems = consignOrderService.selectTradeCertificateList(query);
		for(OrderTradeCertificateDto dto :orderitems){

			Calendar currDate = Calendar.getInstance();
			Calendar opendDate = (Calendar) currDate.clone();
			opendDate.setTime(Tools.strToDate(dto.getOpenOrderDate(), "yyyy-MM-dd HH:mm:ss"));
			int durationDay = (int) ((currDate.getTimeInMillis()-opendDate.getTimeInMillis())/(1000*3600*24));
			dto.setDurationDay(durationDay);

		}
		PageResult result = new PageResult();
		result.setData(orderitems);
		if(query.getNeedPage()!=null && query.getNeedPage()==true){
			int total = consignOrderService.selectTotalOrdersByParams(query);
			result.setRecordsFiltered(total);
		}
		result.setRecordsTotal(orderitems.size());

		return result;
	}

	/**
	 * 加载交易凭证 --> 打印卖家凭证详情
	 * tuxianming
	 * @param out
	 */
	@RequestMapping("tradecertificatedetail.html")
	public void tradeCertificateDetail(Long orderIds, String type,Long sellerId, ModelMap out) {

		out.put("type", type);

		List<Long> ids = new ArrayList<Long>();
		ids.add(orderIds);
		OrderTradeCertificateQuery query = new OrderTradeCertificateQuery();
		query.setOrderIds(ids);
		query.setSellerId(sellerId);
		//查询卖家和订单所对应的明细
		List<OrderItemDetailDto> orderitems = consignOrderService.selectOrdersByParams(query);

		if(orderitems.size()>0){
			OrderItemDetailDto order = orderitems.get(0);



			order.setCreatedStr(Tools.dateToStr(order.getCreated()));
			order.getItems().forEach(item -> {
				order.setSellerName(item.getSellerName());
				order.setSellerId(item.getSellerId());

				//采购重量
				order.setTotalWeight((order.getTotalWeight()!=null?order.getTotalWeight():BigDecimal.ZERO)
						.add(item.getWeight()!=null?item.getWeight():BigDecimal.ZERO));

				//折让后的 实提重量
				BigDecimal actualWeight = item.getActualPickWeightServer();
				//用allowanceBuyerAmount 代替 实提销售金额   allowanceAmount 代替 实提采购金额 
				if(actualWeight.compareTo(BigDecimal.ZERO) != 0){
					//实提采购单价
					item.setCostPrice(item.getAllowanceAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
					);
					//实提销售单价
					item.setDealPrice(item.getAllowanceBuyerAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
					);
				}


				//实提总重量
				/*order.setActualPickTotalWeight(
					(order.getActualPickTotalWeight()!=null?order.getActualPickTotalWeight():BigDecimal.ZERO)
					.add((item.getActualPickWeightServer()!=null?item.getActualPickWeightServer():BigDecimal.ZERO))
				);*/
				
				/*
				BigDecimal actualPurchaseAmount = (item.getActualPickWeightServer()!=null?item.getActualPickWeightServer():BigDecimal.ZERO)
						.multiply(item.getCostPrice()!=null?item.getCostPrice():BigDecimal.ZERO)
						.setScale(BigDecimal.ROUND_HALF_UP, Constant.MONEY_PRECISION);
				*/
				//用invoiceAmount 代用  实提采购金额
				//item.setInvoiceAmount(actualPurchaseAmount);  

				//实提采购总金额
				/*order.setTotalAmount((order.getTotalAmount()!=null?order.getTotalAmount():BigDecimal.ZERO)
						.add(actualPurchaseAmount));*/
				
				
				/*BigDecimal amount = (item.getActualPickWeightServer()!=null?item.getActualPickWeightServer():BigDecimal.ZERO)
						.multiply(item.getDealPrice()!=null?item.getDealPrice():BigDecimal.ONE)
						.setScale(BigDecimal.ROUND_HALF_UP, Constant.MONEY_PRECISION);
				*/
				//实提销售金额
				/*item.setAmount(
						amount.add(item.getAllowanceBuyerAmount()!=null?item.getAllowanceBuyerAmount():BigDecimal.ZERO)
				);*/

				//实提销售总金额
				/*order.setActualPickTotalAmount(
					(order.getActualPickTotalAmount()!=null?order.getActualPickTotalAmount():BigDecimal.ZERO)
					.add(item.getAmount()!=null?item.getAmount():BigDecimal.ZERO)
				);*/

			});
			order.setTotalWeight(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getWeight().doubleValue()).sum()));//采购重量合计
			order.setActualPickTotalWeight(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getActualPickWeightServer().doubleValue()).sum()));//实提总重量合计
			order.setTotalAmount(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getAllowanceAmount().doubleValue()).sum()));//实提采购金额合计
			order.setActualPickTotalAmount(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getAllowanceBuyerAmount().doubleValue()).sum()));//实提销售金额合计
			out.put("order", order);
		}

	}

	public List<OrderTradeCertificateDto> buildOrderTradeCertificate(List<OrderItemDetailDto> orderitems, boolean moreInfo,String certType){
		List<OrderTradeCertificateDto> dtos = new ArrayList<OrderTradeCertificateDto>();
		orderitems.forEach(order -> {

			OrderTradeCertificateDto dto = new OrderTradeCertificateDto();
			dtos.add(dto);
			Calendar currDate = Calendar.getInstance();
			Calendar opendDate = (Calendar) currDate.clone();
			opendDate.setTime(order.getCreated());
			int durationDay = (int) ((currDate.getTimeInMillis()-opendDate.getTimeInMillis())/(1000*3600*24));

			dto.setCode(order.getCode())
					.setOrderId(order.getId())
					.setOpenOrderDate(Tools.dateToStr(order.getCreated()))
					.setBuyerName(order.getAccountName())
					.setOwnerName(order.getOwnerName())


					.setDurationDay(durationDay)
					.setPrintBuyerTradeCertificateStatus("未打印")  //TODO
					.setQuantity(0)
					.setTotalAmount(BigDecimal.ZERO)
					.setTotalWeight(BigDecimal.ZERO)
					.setActualPickTotalAmount(BigDecimal.ZERO)
					.setActualPickTotalWeight(BigDecimal.ZERO)
			;

			dto.setPayStatus(order.getPayStatus());
			dto.setSecondaryTime(order.getSecondaryTime());
			if(moreInfo){
				dto.setItems(order.getItems());
			}

			order.getItems().forEach(item ->{
				//折让后的 实提重量
				BigDecimal actualWeight = item.getActualPickWeightServer();
				//用allowanceBuyerAmount 代替 实提销售金额   allowanceAmount 代替 实提采购金额    invoiceAmount 代替买家总金额
				if(actualWeight.compareTo(BigDecimal.ZERO) != 0){
					//实提采购单价
					item.setCostPrice(item.getAllowanceAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
					);
					//实提销售单价
					item.setDealPrice(item.getAllowanceBuyerAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
					);
				}

				if("seller".equals(certType)){
					//采购金额
					dto.setTotalAmount(dto.getTotalAmount().add(item.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP))
							.setActualPickTotalAmount(dto.getActualPickTotalAmount().add(item.getAllowanceAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}else{
					//销售金额
					dto.setTotalAmount(dto.getTotalAmount().add(item.getInvoiceAmount()!=null?item.getInvoiceAmount():BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP))
							.setActualPickTotalAmount(dto.getActualPickTotalAmount().add(item.getAllowanceBuyerAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}

				dto.setSellerName(item.getSellerName())
						.setQuantity(dto.getQuantity()+item.getQuantity())
						.setActualPickTotalQuantity(dto.getActualPickTotalQuantity()+item.getActualPickQuantityServer())
						.setTotalWeight(dto.getTotalWeight().add(item.getWeight()))
						.setActualPickTotalWeight(dto.getActualPickTotalWeight().add(actualWeight));

			});

		});
		return dtos;
	}

	
	/**
	 * 风险控制 -- 批量上传凭证
	 * tuxianming 20160524
	 * @param request, 
	 * @return
	 */
	@RequestMapping("uploadcredentialimgs.html")
	@ResponseBody
	public Result uploadCredentialImgs(MultipartHttpServletRequest request, 
				@RequestParam(value = "credentialId", defaultValue = "0") Long credentialId
			) {
		Result result = new Result();

		try {
			List<MultipartFile> orderPicList = request
					.getFiles("credentialImg");
			List<MultipartFile> attachmentList = new ArrayList<MultipartFile>();
			if (orderPicList != null && !orderPicList.isEmpty()) {
				for (MultipartFile file : orderPicList) {
					if (!checkUploadAttachment(file, result, attachmentList)) {
						return result;
					}
				}

			}
			
			List<ConsignOrderAttachment> datas = consignOrderService.updateOrderPic(attachmentList, getLoginUser(),credentialId,"cert");
			result.setData(datas);
			
		} catch (BusinessException e) {
			result.setData(e.getMsg());
			result.setSuccess(false);
			logger.info(e.getMsg(), e);
		} catch (Exception e){
			result.setData("上传失败");
			result.setSuccess(false);
			logger.info(e.getMessage(), e);
		}
		return result;
	}

	public boolean isEnabled() {
		return enabled;
	}
	/**
	 *当有需求单的订单关联或订单关闭时通知超市
	 * @param isSuccess
	 * @param orderId 订单id
	 * @param status 订单状态 CLOSED 关闭 FINISHED 关联完成
	 */
	public void noticeEc(Boolean isSuccess,Long orderId,String status){
		try {
			ConsignOrder order = consignOrderService.queryById(orderId);
			String requirementCode = order.getRequirementCode();
			if (isSuccess && StringUtils.isNotEmpty(requirementCode)) {
				//要把订单ID给超市 modify by wangxianjun
				cbmsRequirementStatusSender.withText(order.getId().toString(), requirementCode, status, orderStatusService.queryCloseReasonByOrderId(orderId));
			}
		} catch (Exception e) {
			logger.error("将订单状态修改推送到超市系统失败", e);
			//return new Result("将订单状态修改推送到超市系统失败", false);
		}
	}


	/**
	 * 风险控制-- 打印买/卖家凭证 按条件全部导出到excel
	 * @param query 查询条件
	 * @return
	 */
	@RequestMapping(value="exporttradecertificatelist.html")
	public ModelAndView exportTradecertificateList(OrderTradeCertificateQuery query){

		//条件设置
		List<Long> ids = getUserIds();
		query.setUserIds(ids);
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}

		query.setGroupBySeller(true)					//按卖家分组
				.setCredentialNull(true)					//凭证号为空
				.setCredentialNullOfBatch(true)				//批量凭证号为空
				.setCloseStatus(true);						//不显示关闭的订单


		//excel表头
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("titles", buildTitlesList("交易单号", "开单时间", "买家全称", "服务中心", "钢为交易员", "卖家全称", "数量(件)"
				, "总重量(吨)", "实提总重量(吨)", "总金额（元）", "实提总金额（元）", "打印状态", "距开单时间已有(天)", "类型"));

		//表内容
		List<OrderTradeCertificateDto> orderitems = consignOrderService.selectTradeCertificateList(query);

		//数据
		dataMap.put("varList",buildTradecertificateListPageDataList(orderitems));//每项数据只能是string,否则不能导出

		ObjectExcelView erv = new ObjectExcelView();
		return new ModelAndView(erv, dataMap);
	}

	/*
	 * 计算距离开单的天数
	 * @param openOrderDate 开单日期
	 * @return
	 */
	private int calculateDurationDay(String openOrderDate){
		Calendar currDate = Calendar.getInstance();
		Calendar opendDate = (Calendar) currDate.clone();
		opendDate.setTime(Tools.strToDate(openOrderDate, "yyyy-MM-dd HH:mm:ss"));
		return (int) ((currDate.getTimeInMillis()-opendDate.getTimeInMillis())/(1000*3600*24));
	}

	private List<String> buildTitlesList(String... titles) {
		List<String> titlesList = new ArrayList<>();
		if (titles != null) {
			for (String title : titles) {
				titlesList.add(title);
			}
		}
		return titlesList;
	}

	private List<PageData> buildTradecertificateListPageDataList(List<OrderTradeCertificateDto> list) {
		List<PageData> varList = new ArrayList<>();
		PageData vpd;
		if (list != null) {
			DecimalFormat amountFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			DecimalFormat weightFormat = new DecimalFormat("0.0000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.
			String tempActualPickTotalWeight;
			String tempActualPickTotalAmount;
			for (OrderTradeCertificateDto dto : list) {
				//实提重量、金额不为0时 显示值，否则显示-
				if (BigDecimal.ZERO.compareTo(dto.getActualPickTotalWeight()) != 0){
					tempActualPickTotalWeight = weightFormat.format(dto.getActualPickTotalWeight());
				}else{
					tempActualPickTotalWeight = "-";
				}

				if (BigDecimal.ZERO.compareTo(dto.getActualPickTotalAmount()) != 0){
					tempActualPickTotalAmount = weightFormat.format(dto.getActualPickTotalAmount());
				}else{
					tempActualPickTotalAmount = "-";
				}

				vpd = new PageData();
				vpd.put("var1",dto.getCode());
				vpd.put("var2",dto.getOpenOrderDate());
				vpd.put("var3",dto.getBuyerName());
				vpd.put("var4",dto.getOrderOrgName());
				vpd.put("var5",dto.getOwnerName());
				vpd.put("var6",dto.getSellerName());
				vpd.put("var7",dto.getQuantity()+"");
				vpd.put("var8",weightFormat.format(dto.getTotalWeight()));
				vpd.put("var9",tempActualPickTotalWeight);
				vpd.put("var10",amountFormat.format(dto.getTotalAmount()));
				vpd.put("var11",tempActualPickTotalAmount);
				vpd.put("var12",dto.getPrintBuyerTradeCertificateStatus());
				vpd.put("var13", calculateDurationDay(dto.getOpenOrderDate()) + "");//计算距离开单的天数
				vpd.put("var14",dto.getSettingValue() != null && dto.getSettingValue() ? "必须审核通过才能开票" : "不须审核通过也能开票" );
				varList.add(vpd);
			}
		}
		return varList;
	}
}
