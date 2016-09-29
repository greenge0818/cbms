package com.prcsteel.platform.order.web.controller.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.*;
import com.prcsteel.platform.acl.model.model.BaseFriendlyLink;

import com.prcsteel.platform.order.web.mq.MarketAddUserSender;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.account.model.dto.AccountForAppDto;
import com.prcsteel.platform.account.model.dto.AccountUserDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.SysFeedback;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserPermissionService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.acl.utils.LdapOperUtils;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.NumberTool;
import com.prcsteel.platform.common.utils.StringUtil;
import com.prcsteel.platform.core.service.CommonService;
import com.prcsteel.platform.order.model.AppUser;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.dto.PayRequestItemsInfoDto;
import com.prcsteel.platform.order.model.dto.PayRequestOrgDto;
import com.prcsteel.platform.order.model.dto.PoolInDetailDto;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.PlateForm;
import com.prcsteel.platform.order.model.enums.Type;
import com.prcsteel.platform.order.model.model.AppUpgrade;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.query.InvoiceDetailQuery;
import com.prcsteel.platform.order.model.query.PoolInListQuery;
import com.prcsteel.platform.order.service.AppPushService;
import com.prcsteel.platform.order.service.impl.OrderCacheServiceImpl;
import com.prcsteel.platform.order.service.invoice.PoolInDetailService;
import com.prcsteel.platform.order.service.invoice.PoolInService;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;
import com.prcsteel.platform.order.service.order.ConsignOrderContractService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderProcessService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.payment.PayRequestItemsService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.order.QueryOrderController;
import com.prcsteel.platform.order.web.vo.APPResult;

@Controller
@RequestMapping("/app")
public class APPController {

	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private AccountService accountService;
	@Resource
	private ConsignOrderService consignOrderService;
	@Resource
	private SecondSettlementLogService secondSettlementLogService;
	@Resource
	private PayRequestService payRequestService;
	@Resource
	private ConsignOrderItemsService consignOrderItemsService;
	@Resource
	private UserPermissionService userPermissionService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private ConsignOrderAttachmentService consignOrderAttachmentService;
	@Resource
	private PayRequestItemsService payRequestItemsService;
	@Resource
	private OrderStatusService orderStatusService;
	@Resource
	private ConsignOrderProcessService consignOrderProcessService;
	@Resource
	private PoolInService poolInService;
	@Resource
	private PoolInDetailService poolInDetailService;
	@Resource
	private AccountContactService accountContactService;
	@Resource
	private ConsignOrderContractService consignOrderContractService;
	@Resource
	private CommonService commonService;
	@Resource
	private AppPushService appPushService;
	@Resource
	private FileService fileService;
	@Resource
	private OrderCacheServiceImpl cacheService;
	@Resource
	private AccountFundService accountFundService;
	@Resource
	ContactService contactService;
	@Resource
	MarketAddUserSender marketAddUserSender;//新增联系人时，发送MQ

	@Value("${ecmq.active}")
	Boolean ecMQActive;


	private static final String SUCCESSCODE = "0";
	private static final String FAILCODE = "-1";
	private static final String LOGINFIRST = "请先登录";
	private static final String NOAUTH = "您没有作该操作的权限";
	private static final String CANNOTLOGIN ="您所在用户组暂时无法访问";
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfDate2 = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat sdfMonth2 = new SimpleDateFormat("yyyy/MM");
//	private static final SimpleDateFormat sdfDateOnly = new SimpleDateFormat("MM-dd");
	private static final DecimalFormat dfWeight = new DecimalFormat("#0.000000");

	final String PERMISSION_VISIT_APP = "sys:app:visit"; // 访问APP
	final String PERMISSION_PAGEVIEW = "order:query:detail:view"; // 订单详情页
	final String PERMISSION_AUDIT = "order:query:approval:approval"; // 审核
	final String PERMISSION_RELATE = "order:query:associate:process"; // 关联
	final String PERMISSION_APPLYPAY = "order:query:applypayment:process"; // 申请付款
	final String PERMISSION_AUDITPAY = "order:query:payment:approval"; // 审核付款
	final String PERMISSION_CONFIRMPAY = "order:query:confirmpayment:confirm"; // 确认付款
	final String PERMISSION_CLOSEAPPLY = "order:query:detail:closeapply"; // 申请关闭
	final String PERMISSION_CLOSEAUDIT = "order:query:detail:closeaudit"; // 审核关闭
	final String PERMISSION_WITHDRAWAUDIT = "order:query:withdrawapp:audit"; // 审核提现申请
	final String PERMISSION_VIEWINVOICEINLIST = "invoice:in:awaits:page"; // 查看进项票列表  按公司查询
	final String PERMISSION_VIEWSETTLEMENTLIST = "order:query:secondsettlement:view";// 待二次结算查询
	final String PERMISSION_VIEWBUYER = "account:buyer:view:info";// 浏览买家信息
	final String PERMISSION_VIEWSELLER = "account:seller:view:info";// 浏览卖家信息

	private static Logger logger = LoggerFactory.getLogger(APPController.class);
	//刷新缓存
	@ModelAttribute
    public void touchToken(String authToken) {
		if(StringUtils.isNotEmpty(authToken)){
			cacheService.touch(authToken);
			AppUser user =(AppUser) cacheService.get(authToken);
			if(user!=null){
				cacheService.touch(user.getUser().getLoginId());
			}
		}
	}
	// 3.2 登录
	@RequestMapping("/user/login.html")
	@ResponseBody
	public APPResult login(String deviceNo,String deviceType,String username, String password) {
		APPResult result = new APPResult();
		if(StringUtils.isEmpty(deviceNo)){
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递设备号");
		}else if (StringUtils.isEmpty(deviceType)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递设备类型");
		}else if (StringUtils.isEmpty(username)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请输入用户名");
		}else if (StringUtils.isEmpty(password)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请输入密码");
		} else {
			try {
				logger.info("login start..");
				if (new LdapOperUtils().login(username, password)) {//账号密码校验通过
					logger.info("login success!");
					String token = StringUtil.build(deviceNo+username+System.currentTimeMillis(),"utf-8");
					logger.info("token built!");
					logger.info("fetching appUser from cache...");
					AppUser appUser =null;
					String oldToken = (String) cacheService.get(username);
					
					if(oldToken!=null){
						appUser =(AppUser) cacheService.get(oldToken);
					}
//					AppUser appUser = cacheService.getUserByLoginId(username);//缓存里去取
					logger.info("fetching appUser from cache done");
					User user = null;
					if(appUser!=null){//取到了
						//提取用户信息
						user =appUser.getUser();
					}
					//没取到或者提取出来发现是个空用户
					if(user==null){
						//去数据库里查出来
						logger.info("query user from datatable..");
						user = userService.queryByLoginId(username);
						logger.info("query user from datatable done");
						//数据库里也查不到,返回错误
						if (user == null) {
							result.setStatusCode(FAILCODE);
							result.setMessage("该用户在数据库不存在");
							return result;
						}
					}
					
					//最终拿到user对象后 判断app权限
					logger.info("checking if user have permission to login app system...");
					if(!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VISIT_APP)){
						logger.info("checking if user have permission to login app system...done, yes he has permission");
						cacheService.delete(token);
						result.setStatusCode(FAILCODE);
						result.setMessage(CANNOTLOGIN);
						return result;
					}
					if(appUser!=null){
						//如果登录的设备不是原来缓存中的设备，让原来的设备下线
						if(!appUser.getDeviceNo().equals(deviceNo)){
							logger.info("sending message to kick user off line.");
							appPushService.sendPushMessage(appUser.getUser().getId()+"_"+appUser.getDeviceNo(), "logout");
							logger.info("sending message to kick user off line done");
						}
						//把原来的登录设备信息从缓存中抹去
						logger.info("deleting user info from cache.");
						cacheService.delete(appUser.getAuthToken());
						logger.info("deleting user info from cache.. done");
					}
					
					if(appUser==null){
						appUser = new AppUser();
					}
					appUser.setAuthToken(token);
					appUser.setDeviceNo(deviceNo);
					appUser.setDeviceType(deviceType);
					appUser.setUser(user);
					logger.info("setting appUser to cache");
					//存两组，一组用来查loginid对应的token，查到后再拿token去查appUser
					cacheService.set(token, Constant.MEMCACHESESSIONOUT, appUser);
					cacheService.set(username, Constant.MEMCACHESESSIONOUT, token);
					logger.info("setting appUser to cache done");
					
					Map<String, String> userInfo = new HashMap<String, String>();

					userInfo.put("authToken", token);
					userInfo.put("userId", String.valueOf(user.getId()));//用于注册推送
					userInfo.put("role", userService.isManager(user) ? "1" : "0");// roleService.queryById(user.getRoleId()).getName()
					userInfo.put("realname", user.getName());
					userInfo.put("mobileNumber", user.getTel());
					result.setStatusCode(SUCCESSCODE);
					result.setMessage("登录成功");
					result.setData(userInfo);
				} else {
					result.setStatusCode(FAILCODE);
					result.setMessage("用户名或密码错误");
				}
			} catch (BusinessException e) {
				result.setStatusCode(FAILCODE);
				result.setMessage(e.getMsg());
			}
		}
		logger.info("login complete");
		return result;
	}
	// 3.3	反馈意见收集接口
	@RequestMapping(value="/user/feedback.html",method=RequestMethod.POST)
	@ResponseBody
	public APPResult feedback(String authToken,String content) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		if(StringUtils.isEmpty(content)){
			result.setStatusCode(FAILCODE);
			result.setMessage("反馈意见不能为空");
			result.setData(null);
			return result;
		}
		SysFeedback feedback = new SysFeedback(user.getId(), user.getName(), PlateForm.App.toString(), content, user.getLoginId());
		if(!commonService.saveFeedBack(feedback)){
			result.setStatusCode(FAILCODE);
			result.setMessage("保存反馈意见失败");
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}
	
	// 3.4 注销登录
	@RequestMapping("/user/logout.html")
	@ResponseBody
	public APPResult logout(String authToken) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		if (user != null) {
			cacheService.delete(authToken);
		} else {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到登录人信息");
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		return result;
	}
	// 3.5	安卓检查更新
	@RequestMapping("/upgrade.html")
	@ResponseBody
	public APPResult upgrade(String versionCode) {
		APPResult result = new APPResult();
		if(StringUtils.isEmpty(versionCode)){
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递当前版本号");
			return result;
		}
		AppUpgrade upgrade = new AppUpgrade();
		int iVersionCode = Integer.parseInt(versionCode);
		if(iVersionCode<upgrade.getVersionCode()){
			upgrade.setUpgrade(true);
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(upgrade);
		return result;
	}
	// 3.5	IOS检查更新
		@RequestMapping("/upgrade2.html")
		@ResponseBody
		public APPResult upgrade2() {
			APPResult result = new APPResult();
			
			AppUpgrade upgrade = new AppUpgrade();

			result.setStatusCode(SUCCESSCODE);
			result.setMessage(Constant.SUCCESS);
			result.setData(upgrade);
			return result;
		}
	// 3.6 获取代办事项列表
	@RequestMapping("/order/todolist.html")
	@ResponseBody
	public APPResult todolist(String authToken) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		Map<String, Object> paramMap;
		ConsignOrderDto dto;
		String[] array;
		// 返回数据处理
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		if (userService.isManager(user)) {
			// 待审核
			paramMap = getInitParamMap(user);
			array = new String[] { Constant.ConsignOrderStatus.NEW.getCode() };
			dto = new ConsignOrderDto();
			paramMap.put("dto", dto);
			paramMap.put("userId", user.getId());
			paramMap.put("array", array);
			List<ConsignOrderDto> approval = consignOrderService.selectByConditions(paramMap);
			Collections.reverse(approval);

			// //待审核付款
			paramMap = getInitParamMap(user);
			array = new String[] { Constant.ConsignOrderStatus.RELATED.getCode(),
					Constant.ConsignOrderStatus.SECONDSETTLE.getCode() };
			paramMap.put("array", array);
			paramMap.put("userId", user.getId());
			dto = new ConsignOrderDto();
			dto.setPayStatus(Constant.ConsignOrderPayStatus.REQUESTED.toString());
			paramMap.put("dto", dto);
			List<ConsignOrderDto> payment = consignOrderService.selectByConditions(paramMap);
			Collections.reverse(payment);
			// //待审核二次结算付款
			paramMap = new HashMap<String, Object>();
			paramMap.put("userIds", getUserIds(user));
			paramMap.put("start", 0);
			paramMap.put("length", 999);
			paramMap.put("status", Constant.PayStatus.REQUESTED);
			List<PayRequestOrgDto> secondSettlement = secondSettlementLogService.findPayRequest(paramMap);
			Collections.reverse(secondSettlement);
			// //待审核提现申请
			paramMap = new HashMap<String, Object>();
			paramMap.put("status", Constant.PayStatus.REQUESTED.toString());
			paramMap.put("type", Constant.PayRequestType.WITHDRAW.getCode());
			paramMap.put("userIds", getUserIds(user));
			paramMap.put("start", 0);
			paramMap.put("length", 999);
			List<PayRequest> payRequest = payRequestService.query(paramMap);

			// //待审核关闭申请
			paramMap = getInitParamMap(user);
			array = new String[] { ConsignOrderStatus.CLOSEREQUEST1.getCode(),
					ConsignOrderStatus.CLOSEREQUEST2.getCode(), ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode(),
					ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode()};
			dto = new ConsignOrderDto();
			paramMap.put("dto", dto);
			paramMap.put("userId", user.getId());
			paramMap.put("array", array);
			List<ConsignOrderDto> closeRequest = consignOrderService.selectByConditions(paramMap);
			Collections.reverse(closeRequest);

			list.addAll(toAppData(approval));
			list.addAll(toAppData(payment));
			list.addAll(toAppData(secondSettlement));
			list.addAll(toAppData(payRequest));
			list.addAll(toAppData(closeRequest));
		} else {
			// 待关联
			paramMap = getInitParamMap(user);
			array = new String[] { Constant.ConsignOrderStatus.NEWAPPROVED.getCode() };
			dto = new ConsignOrderDto();
			paramMap.put("dto", dto);
			paramMap.put("userId", user.getId());
			paramMap.put("array", array);
			List<ConsignOrderDto> associate = consignOrderService.selectByConditions(paramMap);
			Collections.reverse(associate);
			// //待申请付款
			paramMap = getInitParamMap(user);
			array = new String[] { Constant.ConsignOrderStatus.RELATED.getCode(),
					Constant.ConsignOrderStatus.SECONDSETTLE.getCode() };
			paramMap.put("array", array);
			paramMap.put("userId", user.getId());
			dto = new ConsignOrderDto();
			dto.setPayStatus(Constant.ConsignOrderPayStatus.APPLY.toString());
			paramMap.put("dto", dto);
			List<ConsignOrderDto> applypayment = consignOrderService.selectByConditions(paramMap);
			Collections.reverse(applypayment);
			list.addAll(toAppData(associate));
			list.addAll(toAppData(applypayment));
		}

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(list);
		
		return result;
	}

	// 3.7 获取审核订单详情
	@RequestMapping("/order/audit_detail.html")
	@ResponseBody
	public APPResult auditDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			return result;
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", order.getId());
		map.put("orderNo", getOrderNoLast6(order.getCode()));
		map.put("orderTime", sdfDateTime.format(order.getCreated()));
		map.put("buyerName", order.getAccountName());
		map.put("contactName", order.getContactName());
		// map.put("contactPhone", order.getContactTel());
		map.put("dealer", order.getOwnerName());
		map.put("totalNum", order.getTotalQuantity());
		map.put("totalWeight", dfWeight.format(order.getTotalWeight()));
		map.put("totalAmount", NumberTool.toThousandth(order.getTotalAmount()));
		map.put("deliveryCostBy", AccountType.valueOf(order.getFeeTaker()).getName()+"承担");
		String deliveryWay = "SELFPICK".equals(order.getDeliveryType()) ? "自提"
				: "DISPATCH".equals(order.getDeliveryType()) ? "配送" : "未知";
		map.put("deliveryWay", deliveryWay);
		// String deliveryTime = sdfDate.format(order.getDeliveryStartDate()) +
		// "至"
		// + sdfDateOnly.format(order.getDeliveryEndDate());
		map.put("deliveryTime", sdfDate.format(order.getDeliveryEndDate()));

		List<ConsignOrderItems> items = consignOrderService.queryOrderItemsById(order.getId());
		if (items == null || items.isEmpty()) {
			result.setStatusCode(FAILCODE);
			result.setData(null);
			result.setMessage("没有找到该订单的资源明细");
			return result;
		}
		map.put("list", toAppData(items));
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.8 确认审核订单
	@RequestMapping(value = "/order/confirm_audit.html",method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmAudit(String authToken, String id, String operation, String comments, String reason) {
		try {
			if (comments != null)
				comments = URLDecoder.decode(comments, "UTF-8");
			if (reason != null)
				reason = URLDecoder.decode(reason, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		if (!op && StringUtils.isEmpty(reason)) {
			result.setStatusCode(FAILCODE);
			result.setData(null);
			result.setMessage("请提供不通过的理由");
			return result;
		}
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_AUDIT)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		ResultDto resultDto;
		if (op) {
			//判断联系人是否存在
			Contact contact= contactService.queryByTel(order.getContactTel());
			resultDto = consignOrderService.auditOrder(order.getId(), user);
			//add by wangxianjun 20160712 将新增的用户推送到超市系统
			if(contact == null && ecMQActive!=null && ecMQActive) {
				try {
					marketAddUserSender.withText(order.getContactTel(), order.getContactName());
				} catch (Exception e) {
					logger.error("将新增的用户推送到超市系统失败", e);
				}
			}
		} else {
			resultDto = consignOrderService.auditRefuse(order.getId(), user, reason);
		}

		if (resultDto.isSuccess()) {
			if (StringUtils.isNotEmpty(comments)) {
				ConsignOrder updateOrder = new ConsignOrder();
				updateOrder.setId(order.getId());
				updateOrder.setLastUpdatedBy(user.getName());
				updateOrder.setLastUpdated(new Date());
				updateOrder.setNote(comments);
				if (!consignOrderService.setNote(updateOrder)) {
					result.setStatusCode(FAILCODE);
					result.setMessage("保存备注失败");
					result.setData(null);
					return result;
				}
			}
		} else {
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;

	}

	// 3.9 获取审核交易关闭订单详情
	@RequestMapping("/order/audit_close_detail.html")
	@ResponseBody
	public APPResult auditCloseDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", order.getId());
		map.put("orderNo", getOrderNoLast6(order.getCode()));
		map.put("orderTime", sdfDateTime.format(order.getCreated()));
		map.put("buyerName", order.getAccountName());
		map.put("contactName", order.getContactName());
		// map.put("contactPhone", order.getContactTel());
		map.put("dealer", order.getOwnerName());
		map.put("totalNum", order.getTotalQuantity());
		map.put("totalWeight", dfWeight.format(order.getTotalWeight()));
		map.put("totalAmount", NumberTool.toThousandth(order.getTotalAmount()));
		map.put("deliveryCostBy", AccountType.valueOf(order.getFeeTaker()).getName()+"承担");
		String deliveryWay = "SELFPICK".equals(order.getDeliveryType()) ? "自提"
				: "DISPATCH".equals(order.getDeliveryType()) ? "配送" : "未知";
		map.put("deliveryWay", deliveryWay);
//		String deliveryTime = sdfDate.format(order.getDeliveryStartDate()) + "至"
//				+ sdfDateOnly.format(order.getDeliveryEndDate());
		map.put("deliveryTime", sdfDate.format(order.getDeliveryEndDate()));
		map.put("comments", order.getNote());
		List<ConsignOrderItems> items = consignOrderService.queryOrderItemsById(order.getId());
		if (items == null || items.isEmpty()) {
			result.setStatusCode(FAILCODE);
			result.setMessage("没有找到该订单的资源明细");
			result.setData(null);
			return result;
		}
		map.put("list", toAppData(items));
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.10 确认审核交易关闭订单
	@RequestMapping(value = "/order/confirm_audit_close.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmAuditClose(String authToken, String id, String operation, String comments, String reason) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		if (!op && StringUtils.isEmpty(reason)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供不通过的理由");
			result.setData(null);
			return result;
		}
		
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_CLOSEAUDIT)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		ResultDto resultDto;
		if (op) {
			resultDto = consignOrderService.auditClose(order.getId(), user);
		} else {
			resultDto = consignOrderService.auditCloseRefuse(order.getId(), user, reason);
		}

		if (!resultDto.isSuccess()) {
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.11 获取关联订单详情
	@RequestMapping("/order/link_detail.html")
	@ResponseBody
	public APPResult linkDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", order.getId());
		map.put("orderNo", getOrderNoLast6(order.getCode()));
		map.put("orderTime", sdfDateTime.format(order.getCreated()));
		map.put("buyerName", order.getAccountName());
		map.put("contactName", order.getContactName());
		// map.put("contactPhone", order.getContactTel());
		map.put("dealer", order.getOwnerName());
		map.put("totalNum", order.getTotalQuantity());
		map.put("totalWeight", dfWeight.format(order.getTotalWeight()));
		map.put("totalAmount", NumberTool.toThousandth(order.getTotalAmount()));
		map.put("deliveryCostBy", AccountType.valueOf(order.getFeeTaker()).getName()+"承担");
		String deliveryWay = "SELFPICK".equals(order.getDeliveryType()) ? "自提"
				: "DISPATCH".equals(order.getDeliveryType()) ? "配送" : "未知";
		map.put("deliveryWay", deliveryWay);
		String deliveryTime = sdfDate.format(order.getDeliveryEndDate());//sdfDate.format(order.getDeliveryStartDate()) + "至"+ 
		map.put("deliveryTime", deliveryTime);
		map.put("comments", order.getNote());
		Account buyer = accountService.queryById(order.getDepartmentId());//按部门来取 afeng 2016-5-24
		if (buyer != null) {
			map.put("bankAccountBalance", NumberTool.toThousandth(buyer.getBalance()));
			map.put("settlementBalance", NumberTool.toThousandth(buyer.getBalanceSecondSettlement()));
		} else {
			result.setStatusCode(FAILCODE);
			result.setMessage("未在系统中找到订单中的买家信息");
			result.setData(null);
			return result;
		}
		List<ConsignOrderItems> items = consignOrderService.queryOrderItemsById(order.getId());
		if (items == null || items.isEmpty()) {
			result.setStatusCode(FAILCODE);
			result.setMessage("没有找到该订单的资源明细");
			result.setData(null);
			return result;
		}
		map.put("list", toAppData(items));
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.12 确认关联订单
	@RequestMapping(value = "/order/confirm_link_detail.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmLinkDetail(String authToken, String id, String operation, String comments,
			String isSettlementChecked) {
		String cause=null;// 关闭原因
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		
		ResultDto resultDto;
		if (op) {// 关联
			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_RELATE)) {
				result.setStatusCode(FAILCODE);
				result.setMessage(NOAUTH);
				result.setData(null);
				return result;
			}
			resultDto = consignOrderService.relateOrder(order.getId(), Boolean.valueOf(isSettlementChecked), user,null,Boolean.FALSE);//信用额度的是否勾选
		} else {// 申请关闭
			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_CLOSEAPPLY)) {
				result.setStatusCode(FAILCODE);
				result.setMessage(NOAUTH);
				result.setData(null);
				return result;
			}
			resultDto = consignOrderService.close(order.getId(), user,cause);
		}

		if (!resultDto.isSuccess()) {
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.13 获取申请付款订单详情
	@RequestMapping("/order/pay_application_detail.html")
	@ResponseBody
	public APPResult payApplicationDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", order.getId());
		map.put("orderNo", getOrderNoLast6(order.getCode()));
		map.put("orderTime", sdfDateTime.format(order.getCreated()));
		map.put("buyerName", order.getAccountName());
		map.put("contactName", order.getContactName());
		map.put("dealer", order.getOwnerName());
		map.put("comments", order.getNote());

		List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsService.getSellerInfo(order.getId());

		map.put("list", toAppData(sellerInfoList));

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.14 查看订单详情
	@RequestMapping("/order/order_detail.html")
	@ResponseBody
	public APPResult orderDetail(String authToken, String orderId, String sellerId) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(orderId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkAccountId(sellerId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		Account seller = (Account) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		double totalAmount = 0;
		List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderId(order.getId());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ConsignOrderItems item : items) {
			if (seller.getId().equals(item.getDepartmentId())) {
				Map<String, Object> m = new LinkedHashMap<String, Object>();
				m.put("typeName", item.getNsortName());
				m.put("material", item.getMaterial());
				m.put("spec", item.getSpec());
				m.put("steel", item.getFactory());
				m.put("warehouse", item.getWarehouse());
				m.put("weightConcept", item.getWeightConcept());
				m.put("num", item.getQuantity());
				m.put("weight", dfWeight.format(item.getWeight()));
				m.put("basePrice", NumberTool.toThousandth(item.getCostPrice()));
				m.put("dealPrice", NumberTool.toThousandth(item.getDealPrice()));
				m.put("amount", NumberTool.toThousandth(item.getAmount()));
				list.add(m);
				totalAmount += item.getAmount().doubleValue();
			}

		}
		if (list.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("指定订单不包含ID为" + seller.getId() + "的卖家信息");
			result.setData(null);
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("id", "");
		map.put("sellerName", seller.getName());
		map.put("totalAmount", NumberTool.toThousandth(totalAmount));
		map.put("list", list);
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}
	
	// 3.15 确认申请付款
	@RequestMapping(value = "/order/confirm_pay_application.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmPayApplication(String authToken, String id, String operation, String comments,
			String info) {
		String cause=null;// 关闭原因
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();

		ResultDto resultDto;
		if (op) {// 申请付款
			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_APPLYPAY)) {
				result.setStatusCode(FAILCODE);
				result.setMessage(NOAUTH);
				result.setData(null);
				return result;
			}
			Map<Long, Double> moneyMap = new HashMap<Long, Double>();
			Map<Long, Long> bankMap = new HashMap<Long, Long>();
			Map<Long, Boolean> checkedMap = new HashMap<Long, Boolean>();
			Map<Long, Boolean> refundCredit = new HashMap<Long, Boolean>();
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode balanceNodes = null;
			try {
				balanceNodes = mapper.readTree(info);
			} catch (IOException ex) {
				result.setStatusCode(FAILCODE);
				result.setMessage("解析提交数据出错");
				result.setData(null);
				return result;
			}
			if (balanceNodes != null) {
				for (JsonNode node : balanceNodes) {
					JsonNode sellerId = node.path("sellerId");
					if (sellerId == null || StringUtils.isEmpty(sellerId.asText())) {
						result.setStatusCode(FAILCODE);
						result.setMessage("未提供卖家ID");
						result.setData(null);
						return result;
					}
					Long lSellerId = sellerId.asLong();
					Account sellerDepartment=accountService.queryById(lSellerId);
					Account seller = accountService.queryById(sellerDepartment.getParentId());
	
					JsonNode applyAmount = node.path("applyAmount");
					if (applyAmount == null || StringUtils.isEmpty(applyAmount.asText())) {
						result.setStatusCode(FAILCODE);
						result.setMessage("卖家" + seller.getName() + "未提供申请付款金额");
						result.setData(null);
						return result;
					}
					double dApplyAmount = 0;
					try {
						dApplyAmount = Double.parseDouble(applyAmount.asText());
						if (dApplyAmount < 0) {
							result.setStatusCode(FAILCODE);
							result.setMessage("卖家" + seller.getName() + "的申请付款金额不是正数");
							result.setData(null);
							return result;
						}
					} catch (NumberFormatException e) {
						result.setStatusCode(FAILCODE);
						result.setMessage("卖家" + seller.getName() + "的申请付款金额格式有误");
						result.setData(null);
						return result;
					}
//					Map<String, Object> paramMap = new HashMap<String, Object>();
					JsonNode bankAccountId = node.path("bankAccountId");
					if (bankAccountId == null || StringUtils.isEmpty(bankAccountId.asText())) {
						result.setStatusCode(FAILCODE);
						result.setMessage("卖家" + seller.getName() + "未提供银行信息");
						result.setData(null);
						return result;
					}
//					paramMap.put("accountId", lSellerId);
//					paramMap.put("bankAccountId", bankAccountId.asText());
//					List<AccountBank> bankinfo = accountBankService.query(paramMap);
					Long bankaccountId = bankAccountId.asLong();
//					if (bankinfo.isEmpty()) {
//						result.setStatusCode(FAILCODE);
//						result.setMessage("卖家" + seller.getName() + "不存在卡号为" + bankAccountNo.asText() + "的银行卡");
//						result.setData(null);
//						return result;
//					} else {
//						bankaccountId = bankinfo.get(0).getId();
//					}
					JsonNode secondChecked = node.path("secondChecked");
					boolean bSecondChecked = false;
					if (secondChecked != null && StringUtils.isNotEmpty(secondChecked.asText())) {
						bSecondChecked = Boolean.valueOf(secondChecked.asBoolean());
					}
					moneyMap.put(lSellerId, dApplyAmount);
					bankMap.put(lSellerId, bankaccountId);
					checkedMap.put(lSellerId,bSecondChecked);
					refundCredit.put(lSellerId, false);
					
					JsonNode contractId = node.path("contractId");
					if (contractId == null || StringUtils.isEmpty(contractId.asText())) {
						result.setStatusCode(FAILCODE);
						result.setMessage("卖家" + seller.getName() + "未提供采购合同ID");
						result.setData(null);
						return result;
					}
					long lContractId = 0L;
					try {
						lContractId = Long.parseLong(contractId.asText());
					} catch (NumberFormatException e) {
						result.setStatusCode(FAILCODE);
						result.setMessage("卖家" + seller.getName() + "的采购合同ID不合法");
						result.setData(null);
						return result;
					}
					String s_contractNo = "";
					JsonNode contractNo = node.path("contractNo");
					if (contractNo != null) {
						s_contractNo = contractNo.asText();
					}
					//新增变更流程，这里传空表示正常流程
					resultDto = consignOrderService.uploadContract(lContractId, s_contractNo, user,"");
					if (!resultDto.isSuccess()) {
						result.setStatusCode(FAILCODE);
						result.setMessage(resultDto.getMessage());
						result.setData(null);
						return result;
					}
				}
			}
			//modify by wangxianjun 影响web端 ，暂时先注释，已和林中沟通
			resultDto = consignOrderService.applyPay(order.getId(), bankMap, moneyMap, checkedMap, refundCredit, user,null);
			if (!resultDto.isSuccess()) {
				result.setStatusCode(FAILCODE);
				result.setMessage(resultDto.getMessage());
				result.setData(null);
				return result;
			}
		} else {// 申请关闭
			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_CLOSEAPPLY)) {
				result.setStatusCode(FAILCODE);
				result.setMessage(NOAUTH);
				result.setData(null);
				return result;
			}
			resultDto = consignOrderService.close(order.getId(), user,cause);
			if (!resultDto.isSuccess()) {
				result.setStatusCode(FAILCODE);
				result.setMessage(resultDto.getMessage());
				result.setData(null);
				return result;
			} 
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.16 获取审核付款订单详情
	@RequestMapping("/order/audit_pay_detail.html")
	@ResponseBody
	public APPResult auditPayDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		PayRequest payRequest = payRequestService.selectAvailablePaymentByOrderId(order.getId());
		if (payRequest == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到该订单的付款申请单");
			result.setData(null);
			return result;
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orderId", payRequest.getConsignOrderId());
		map.put("requestId", payRequest.getId());
		map.put("orderNo", getOrderNoLast6(order.getCode()));
		map.put("orderTime", sdfDateTime.format(order.getCreated()));
		map.put("buyerName", order.getAccountName());
		map.put("contactName", order.getContactName());
		map.put("dealer", order.getOwnerName());
		map.put("totalNum", order.getTotalQuantity());
		map.put("totalWeight", dfWeight.format(order.getTotalWeight()));
		map.put("totalAmount", NumberTool.toThousandth(order.getTotalAmount()));
		map.put("comments", order.getNote());

		List<PayRequestItemsInfoDto> payRequestList = payRequestItemsService
				.selectPayInfoByRequestId(payRequest.getId());

		map.put("list", toAppData(payRequestList));

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.17 确认审核付款
	@RequestMapping(value = "/order/confirm_audit_pay.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmAuditPay(String authToken, String id, String operation, String reason) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkPayRequestId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PayRequest payRequest = (PayRequest) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		if (!op && StringUtils.isEmpty(reason)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供不通过的理由");
			result.setData(null);
			return result;
		}
		
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_AUDITPAY)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		ResultDto resultDto;
		if (op) {
			resultDto = consignOrderService.auditPayAccept(payRequest.getConsignOrderId(), payRequest.getId(), user);
		} else {
			resultDto = consignOrderService.auditPayRefuse(payRequest.getConsignOrderId(), payRequest.getId(), user, reason);
		}
		if (resultDto.isSuccess()) {
			result.setStatusCode(SUCCESSCODE);
			result.setMessage(Constant.SUCCESS);
			result.setData(null);
		}else{
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
		}
		
		return result;
	}


	// 3.18 获取审核二次结算付款订单详情
	@RequestMapping("/order/audit_settlement_detail.html")
	@ResponseBody
	public APPResult auditSettlementDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
//		User user = (User) result.getData();
		result = checkPayRequestId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PayRequest pr = (PayRequest) result.getData();
		// 未卡权限
		// if (!userPermissionService.hasPermission(user.getId(),
		// user.getRoleId(), PERMISSION_XXX)) {
		// result.setStatusCode(FAILCODE);
		// result.setMessage(NOAUTH);
		// return result;
		// }
		
		PayRequestOrgDto payRequest = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("requestId", pr.getId());
		paramMap.put("start", 0);
		paramMap.put("length", 999);
		List<PayRequestOrgDto> payRequestList = secondSettlementLogService.findPayRequest(paramMap);
		if (payRequestList == null || payRequestList.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单");
			result.setData(null);
			return result;
		} else if (payRequestList.size() > 1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("同一申请ID找到多张申请单，SQL有误");
			result.setData(null);
			return result;
		} else {
			payRequest = payRequestList.get(0);
		}
		if (payRequest == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单");
			result.setData(null);
			return result;
		}

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", pr.getId());
		map.put("reserveLimit", NumberTool.toThousandth(payRequest.getCreditLimit()));
		BigDecimal CreditLimitUsed = payRequest.getCreditLimitUsed();
		CreditLimitUsed=CreditLimitUsed.compareTo(BigDecimal.ZERO)==-1?BigDecimal.ZERO:CreditLimitUsed;
		map.put("usedLimit", NumberTool.toThousandth(CreditLimitUsed));
		map.put("remainingLimit",
				NumberTool.toThousandth(payRequest.getCreditLimit().subtract(CreditLimitUsed)));
		map.put("time", sdfDateTime.format(payRequest.getCreated()));
		map.put("companyName", payRequest.getBuyerName());
		map.put("dealer", payRequest.getRequesterName());
		map.put("totalAmount", NumberTool.toThousandth(payRequest.getTotalAmount()));

		map.put("receiptCompanyName", payRequest.getBuyerName());
		List<PayRequestItems> items = payRequestItemsService.selectByRequestId(pr.getId());
		PayRequestItems item = null;
		if (items == null || items.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单明细");
			result.setData(null);
			return result;
		} else if (items.size() > 1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("二次结算付款申请单对应多条付款明细，数据有误,请先确认数据");
			result.setData(null);
			return result;
		} else {
			item = items.get(0);
		}
		map.put("bankName", item.getReceiverBankName() + (item.getReceiverBranchBankName() == null ? " " : item.getReceiverBranchBankName()));
		map.put("bankAccountNo", item.getReceiverAccountCode());

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.19 确认审核二次结算付款订单
	@RequestMapping(value = "/order/confirm_audit_settlement.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmAuditSettlement(String authToken, String id, String operation, String reason) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkPayRequestId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PayRequest pr = (PayRequest) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		if (!op && StringUtils.isEmpty(reason)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供不通过的理由");
			result.setData(null);
			return result;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("requestId", pr.getId());
		paramMap.put("start", 0);
		paramMap.put("length", 999);
		List<PayRequestOrgDto> payRequestList = secondSettlementLogService.findPayRequest(paramMap);
		PayRequestOrgDto payRequest = null;
		if (payRequestList == null || payRequestList.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单");
			result.setData(null);
			return result;
		} else if (payRequestList.size() > 1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("同一申请ID找到多张申请单，SQL有误");
			result.setData(null);
			return result;
		} else {
			payRequest = payRequestList.get(0);
		}
		if (payRequest == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单");
			result.setData(null);
			return result;
		}

		
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_AUDITPAY)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		if (op) {
			try{
				secondSettlementLogService.updatePayRequest(pr.getId(), null, user);
			}catch (BusinessException e){
				result.setStatusCode(FAILCODE);
				result.setMessage(e.getMsg());
				result.setData(null);
				return result;
			}
		} else {
			try{
				secondSettlementLogService.updatePayRequest(pr.getId(), reason, user);
				
				String code = payRequestService.createCode();// 生成关联单号
				
				 	 accountFundService.updateAccountFund(payRequest.getDepartmentId(), AssociationType.PAYMEN_ORDER, code,
						AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, payRequest.getTotalAmount(), payRequest.getTotalAmount().negate(),
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.FREEZE, user.getId(), user.getName(), new Date());
			} catch (BusinessException e) {
				result.setStatusCode(FAILCODE);
				result.setMessage(e.getMsg());
				result.setData(null);
				return result;
			}
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.20 获取审核提现付款订单详情
	@RequestMapping("/order/audit_withdraw_detail.html")
	@ResponseBody
	public APPResult auditWithdrawDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
//		User user = (User) result.getData();
	 
		result = checkPayRequestId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PayRequest payRequest = (PayRequest) result.getData();


		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", payRequest.getId());

		map.put("time", sdfDateTime.format(payRequest.getCreated()));// 申请时间
		map.put("dealer", payRequest.getRequesterName());// 交易员
		map.put("applyAmount", NumberTool.toThousandth(payRequest.getTotalAmount()));// 本次申请提现金额

		map.put("companyName", payRequest.getBuyerName());// 公司全称
		List<PayRequestItems> items = payRequestItemsService.selectByRequestId(payRequest.getId());
		PayRequestItems item = null;
		if (items == null || items.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单明细");
			result.setData(null);
			return result;
		} else if (items.size() > 1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("提现申请单对应多条付款明细，数据有误,请先确认数据");
			result.setData(null);
			return result;
		} else {
			item = items.get(0);
		}
		map.put("bankName", item.getReceiverBankName() + item.getReceiverBranchBankName());// bankName
		map.put("bankAccountNo", item.getReceiverAccountCode());// 银行账号
		map.put("bankAccountName", item.getReceiverName());// 银行账户名称
		Account account = accountService.queryById(payRequest.getDepartmentId());
		map.put("settlementAccountBalance", NumberTool.toThousandth(account.getBalanceSecondSettlement()));// 二次结算账户余额
		map.put("bankAccountBalance", NumberTool.toThousandth(account.getBalance()));// 资金账户余额
		map.put("availableAmount",
				NumberTool.toThousandth(account.getBalance().add(account.getBalanceSecondSettlement())));// 可提现金额

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.21 确认审核提现付款订单
	@RequestMapping(value = "/order/confirm_audit_withdraw.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmAuditWithdraw(String authToken, String id, String operation, String reason) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOperation(operation);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		boolean op = (Boolean) result.getData();
		if (!op && StringUtils.isEmpty(reason)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供不通过的理由");
			result.setData(null);
			return result;
		}
		
		result = checkPayRequestId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PayRequest payRequest = (PayRequest) result.getData();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", payRequest.getId());
		paramMap.put("type", Constant.PayRequestType.WITHDRAW.getCode());
		paramMap.put("start", 0);
		paramMap.put("length", 999);
		List<PayRequest> payRequestList = payRequestService.query(paramMap);
		
		if (payRequestList == null || payRequestList.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的提现申请单");
			result.setData(null);
			return result;
		} else if (payRequestList.size() > 1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("同一申请ID找到多张申请单，SQL有误");
			result.setData(null);
			return result;
		} else {
			payRequest = payRequestList.get(0);
		}
		if (payRequest == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的申请单");
			result.setData(null);
			return result;
		}
		
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_WITHDRAWAUDIT)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}

		if (!payRequestService.checkWithdraw(user, payRequest.getId(), op, reason)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("审核失败");
			result.setData(null);
			return result;
		}

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.22 获取交易单列表
	@RequestMapping("/order/list.html")
	@ResponseBody
	public APPResult list(String authToken, String pageIndex, String pageSize, String date) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		int iPageIndex=1, iPageSize=10;
		result = checkPageIndexAndPageSize(pageIndex, pageSize);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		int[] pageParam =(int[]) result.getData();
		iPageIndex = pageParam[0];
		iPageSize = pageParam[1];
		result = checkDate(date);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		date =(String) result.getData();
		// 查询数据
		List<Map<String, Object>> list = consignOrderService.appOrderList(user.getId(), date,
				(iPageIndex - 1) * iPageSize, iPageSize);
		result.setData(list);

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		
		return result;
	}

	// 3.23 获取交易单详情
	@RequestMapping("/order/status_detail.html")
	@ResponseBody
	public APPResult statusDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_PAGEVIEW)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
			
		if (order == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的订单");
			result.setData(null);
			return result;
		}
		List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderId(order.getId());
		if (items == null || items.size() == 0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("订单明细是空的");
			result.setData(null);
			return result;
		}
		// 查询数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", order.getId());
		data.put("orderNo", getOrderNoLast6(order.getCode()));
		data.put("time", sdfDateTime.format(order.getCreated()));
		data.put("dealer", order.getOwnerName());
		data.put("contactName", order.getContactName());
		// data.put("status", order.getStatus());
		data.put("buyerName", order.getAccountName());

		List<String> sellerNames = new LinkedList<String>();
		for (ConsignOrderItems item : items) {
			sellerNames.add(item.getSellerName());
		}
		data.put("sellerName", sellerNames);
		// data.put("num", order.getTotalQuantity());
		// data.put("weight",
		// NumberTool.toThousandth(order.getTotalWeight()));
		// data.put("totalAmount",
		// NumberTool.toThousandth(order.getTotalAmount()));
		List<ConsignOrderStatusDto> ruleStep = consignOrderProcessService
				.getOrderProcessByUserId(order.getOwnerId());
		List<ConsignOrderStatusDto> realStep = orderStatusService.getAuditDetailById(order.getId());

		if (ruleStep != null && ruleStep.size() > 0) {
			if (realStep != null) {
				for (ConsignOrderStatusDto step : realStep) {
					for (ConsignOrderStatusDto item : ruleStep) {
						if (step.getStatus().equals(item.getStatus())) {
							item.setOperaterName(step.getOperaterName());
							item.setOperationTime(step.getOperationTime());
							item.setMobile(step.getMobile());
							item.setOperationName(
									QueryOrderController.getConsignOrderFlowNodeMap().get(item.getStatus()));
							break;
						}
					}
				}
			}
			data.put("ruleStep", ruleStep);
			result.setData(data);
			result.setStatusCode(SUCCESSCODE);
			result.setMessage(Constant.SUCCESS);
		} else {
			result.setStatusCode(FAILCODE);
			result.setData("暂无数据");
			result.setData(null);
			return result;
		}
		
		return result;
	}

	// 3.24 获取二次结算应收应付列表
	@RequestMapping("/settlement/list.html")
	@ResponseBody
	public APPResult settlementlist(String authToken, String pageIndex, String pageSize) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		int iPageIndex=1, iPageSize=10;
		result = checkPageIndexAndPageSize(pageIndex, pageSize);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		int[] pageParam =(int[]) result.getData();
		iPageIndex = pageParam[0];
		iPageSize = pageParam[1];
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWSETTLEMENTLIST)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds(user));
		paramMap.put("start", (iPageIndex - 1) * iPageSize);
		paramMap.put("length", iPageSize);

		List<AccountUserDto> items = null;

		items = secondSettlementLogService.findBydId(paramMap);

		list.addAll(toAppData(items));
		result.setData(list);
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		
		return result;
	}

	// 3.25 获取二次结算应收详情
	@RequestMapping("/settlement/receipt_detail.html")
	@ResponseBody
	public APPResult receiptDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkAccountId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		Account department = (Account) result.getData();
		Account account=accountService.queryById(department.getParentId());
		//账户体系改版，已无此权限
//		if (AccountType.buyer.toString().equals(account.getType())) {
//			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWBUYER)) {
//				result.setStatusCode(FAILCODE);
//				result.setMessage(NOAUTH);
//				result.setData(null);
//				return result;
//			}
//		} else {
//			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWSELLER)) {
//				result.setStatusCode(FAILCODE);
//				result.setMessage(NOAUTH);
//				result.setData(null);
//				return result;
//			}
//		}

		if (department.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) > -1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("该客户没有应收二次结算余额");
			result.setData(null);
			return result;
		}
		// “id”:”账户id”,
		// “companyName”:”公司全称”，
		// “settlementDebt”:”二次结算账户欠款”，
		// “bankAccountBalance”：”资金账户余额”,

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", department.getId());
		map.put("companyName", account.getName());
		map.put("settlementDebt",
				NumberTool.toThousandth(Math.abs(department.getBalanceSecondSettlement().doubleValue())));
		map.put("bankAccountBalance", NumberTool.toThousandth(department.getBalance()));

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.26 抵扣二次结算应收
	@RequestMapping(value = "/settlement/confirm_receipt.html",method=RequestMethod.POST)
	@ResponseBody
	public APPResult confirmReceipt(String authToken,String id,String deductionSettlementAmount) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkAccountId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		Account account = (Account) result.getData();
		String code = payRequestService.createCode();// 生成关联单号
		
		result = checkDeductionSettlementAmount(deductionSettlementAmount);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		double dDeductionSettlementAmount = (Double) result.getData();
		BigDecimal amount = new BigDecimal(dDeductionSettlementAmount);// 发生额
		BigDecimal balanceSecondSettlemento = account.getBalanceSecondSettlement();//二次结算账户余额
		BigDecimal balance = account.getBalance();//账户余额
		if (amount.compareTo(balance)==1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("您申请抵扣的金额不能大于资金账户余额！");
			result.setData(null);
			return result;
		}
		if (amount.add(balanceSecondSettlemento).compareTo(BigDecimal.ZERO)==1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("您申请抵扣的金额不能大于二次结算欠款！");
			result.setData(null);
			return result;
		}
		if (amount.compareTo(BigDecimal.ZERO)==0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("您申请抵扣的金额不能为零！");
			result.setData(null);
			return result;
		}
		double balanceWithdraw = amount.setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		boolean adds = orderStatusService.updatePayment(account.getId(), code, null,
				balanceWithdraw, null, user.getId(), user.getName(), new Date(),
				Type.DEDUCTIONS.getCode());
		if(!adds){
			result.setStatusCode(FAILCODE);
			result.setMessage("抵扣出错");
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}

	// 3.27	获取二次结算应付详情
	@RequestMapping("/settlement/payment_detail.html")
	@ResponseBody
	public APPResult paymentDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkAccountId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		Account department = (Account) result.getData();
		Account account=accountService.queryById(department.getParentId());
		//cc 2016.05.26 账户体系删除此权限及判断方式
//		if (AccountType.buyer.toString().equals(account.getType())) {
//			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWBUYER)) {
//				result.setStatusCode(FAILCODE);
//				result.setMessage(NOAUTH);
//				result.setData(null);
//				return result;
//			}
//		} else {
//			if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWSELLER)) {
//				result.setStatusCode(FAILCODE);
//				result.setMessage(NOAUTH);
//				result.setData(null);
//				return result;
//			}
//		}

		if (department.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) < 1) {
			result.setStatusCode(FAILCODE);
			result.setData(null);
			result.setMessage("该客户没有应付二次结算余额");
			return result;
		}
		// “id”:”账户id”,
		// “companyName”:”公司全称”，
		// “settlementDebt”:”二次结算账户欠款”，
		// “bankAccountBalance”：”资金账户余额”,

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", department.getId());
		map.put("companyName", account.getName());
		map.put("bankAccountBalance", NumberTool.toThousandth(department.getBalance()));
		map.put("settlementAccountBalance", NumberTool.toThousandth(department.getBalanceSecondSettlement()));
		map.put("availableWithdrawAmount", NumberTool.toThousandth(department.getBalanceSecondSettlement()));
		List<AccountBank> list = accountBankService.queryByAccountId(account.getId());
		list.removeIf(a -> !AccountBankDataStatus.Approved.getCode().equals(a.getBankDataStatus()));// 移除银行状态不为审核通过的 add afeng
		List<Map<String, Object>> bankinfo = new ArrayList<Map<String, Object>>();
		Set<String> bankNameSet = new HashSet<String>();
		for (AccountBank ab : list) {
			String bankName = ab.getBankName()+"<->"+ ab.getBankNameBranch();
			bankNameSet.add(bankName);
		}

		for (String bankName : bankNameSet) {
			Map<String, Object> bankNameInfo = new HashMap<String, Object>();
			String [] arrBankName = bankName.split("<->");
			List<Map<String,Object>> bankAccountNoList = getBankAccountInfoByBankName(arrBankName[0], arrBankName[1], list);
			String bankNamMain =arrBankName[0] , bankNameBranch=arrBankName[1];
			bankNameInfo.put("bankName", ("null".equals(bankNamMain)?"":bankNamMain) + " " + ("null".equals(bankNameBranch)?"":bankNameBranch));
			bankNameInfo.put("bankAccountNoList", bankAccountNoList);
			bankinfo.add(bankNameInfo);
		}

		map.put("bankinfo", bankinfo);
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}
	
	//3.28	申请二次结算提现
	@RequestMapping(value = "/settlement/confirm_payment.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult confirmPayment(String authToken,String id,String amount,String bankAccountId) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkAccountId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		Account account = (Account) result.getData();
	
		result = checkAmount(amount);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		double dAmount = (Double) result.getData();
		BigDecimal totalAmount = new BigDecimal(dAmount);// 提现付款额
		BigDecimal balanceSecondSettlemento = account.getBalanceSecondSettlement();//二次结算账户余额
		
		if (totalAmount.compareTo(balanceSecondSettlemento)==1) {
			result.setStatusCode(FAILCODE);
			result.setMessage("您申请提现的金额不能大于二次结算账户余额！");
			result.setData(null);
			return result;
		}
		if (totalAmount.compareTo(BigDecimal.ZERO)==0) {
			result.setStatusCode(FAILCODE);
			result.setMessage("您申请提现的金额不能为零！");
			result.setData(null);
			return result;
		}
		
		Long bankIdl=0L;
		if(StringUtils.isEmpty(bankAccountId)){
			result.setStatusCode(FAILCODE);
			result.setMessage("客户银行卡ID为空");
			result.setData(null);
			return result;
		}
		try{
			bankIdl = Long.parseLong(bankAccountId);
		}catch(NumberFormatException e){
			result.setStatusCode(FAILCODE);
			result.setMessage("客户银行卡ID有误");
			result.setData(null);
			return result;
		}
		List<UserOrgDto> orgList = secondSettlementLogService
				.findByOrgId(user.getOrgId());
		Long orgDtoId = null;
		String orgDtoName = null;
		for (int i = 0; i < orgList.size();) {
			orgDtoId = orgList.get(i).getId();
			orgDtoName = orgList.get(i).getName();
			break;
		}
		String code = payRequestService.createCode();
		Long departmentId = 0L;
		String departmentName = "";
		Boolean num = secondSettlementLogService.insertPayRequest(user.getId(),
				account.getId(), account.getName(), totalAmount, user.getLoginId(),
				user.getName(), code, orgDtoId, orgDtoName, bankIdl, departmentId, departmentName);
		double balanceWithdraw = totalAmount.setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		boolean success=false;
		if (num && orderStatusService.updatePayment(account.getId(), code,
					null, balanceWithdraw, null, user.getId(), user.getName(), new Date(),
					Type.SECONDWITHDRAWALW.getCode())){
			success = true;
		} else {
			success = false;
		}
		if(!success){
			result.setStatusCode(FAILCODE);
			result.setMessage("提现出错");
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}
		
	// 3.29 获取进项票列表
	@RequestMapping("/invoice/inlist.html")
	@ResponseBody
	public APPResult inlist(String authToken, String pageIndex, String pageSize) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		int iPageIndex=1, iPageSize=10;
		result = checkPageIndexAndPageSize(pageIndex, pageSize);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		int[] pageParam =(int[]) result.getData();
		iPageIndex = pageParam[0];
		iPageSize = pageParam[1];
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWINVOICEINLIST)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("start",(iPageIndex - 1) * iPageSize);
		query.put("length",iPageSize);
		List<Long> userIds =  getUserIds(user);
		query.put("ownerIdList", userIds);
		List<PoolIn> items = poolInService.queryByCompany(query);
		query = new HashMap<String, Object>();
		query.put("ownerIdList", userIds);
		BigDecimal shouldTotalAmount = poolInDetailService.queryShouldTotalAmount(query);
		
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		for(PoolIn pi :items){
			Map<String,Object> map= new LinkedHashMap<String,Object>();
			map.put("id", pi.getId());
			Account account = accountService.queryById(pi.getSellerId());
			User manager = userService.queryById(account.getManagerId());
			if(userService.isManager(user)){
				map.put("managerName", manager.getName());
			}else{
				map.put("managerName", "");
			}
			map.put("companyName", pi.getSellerName());
			map.put("weight", dfWeight.format(pi.getTotalWeight()));
			map.put("amount", NumberTool.toThousandth(pi.getTotalAmount()));
			list.add(map);
		}
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		map.put("totalAmount", NumberTool.toThousandth(shouldTotalAmount));
		map.put("list", list);
		result.setData(map);
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		
		return result;
	}

	// 3.30 获取待收进项票详情
	@RequestMapping("/invoice/in_detail.html")
	@ResponseBody
	public APPResult inDetail(String authToken, String id) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkInvoiceInId(id);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		PoolIn pi=(PoolIn) result.getData();
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWINVOICEINLIST)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", pi.getId());

		map.put("companyName", pi.getSellerName());// 公司全称
		AccountContact ac = accountContactService.queryMainByAccountId(pi.getSellerId());
		if (ac == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到卖家" + pi.getSellerName() + "的主联系人");
			result.setData(null);
			return result;
		}
		map.put("contactName", ac.getName());// 联系人名称
		map.put("contactPhone", ac.getTel());// 联系人电话

		map.put("totalWeight", dfWeight.format(pi.getTotalWeight()));// 合计重量
		map.put("totalAmount", NumberTool.toThousandth(pi.getTotalAmount()));// 合计金额

		PoolInListQuery query = new PoolInListQuery();
		query.setStart(0);
		query.setLength(999);
		query.setSellerId(pi.getSellerId());
		query.setOwnerIdList(getUserIds(user));
		InvoiceDetailQuery invoiceDetailQuery = new InvoiceDetailQuery();
		invoiceDetailQuery.setStart(0);
		invoiceDetailQuery.setLength(999);
		invoiceDetailQuery.setSellerId(pi.getSellerId());
		invoiceDetailQuery.setOwnerIdList(getUserIds(user));
        List<PoolInDetailDto> list = poolInDetailService.query(invoiceDetailQuery);
		map.put("list", toAppData(list));// 品规明细

		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(map);
		
		return result;
	}

	// 3.31 获取销项票列表
//	@RequestMapping("/invoice/outlist.html")
//	@ResponseBody
//	public APPResult outlist(String authToken, String pageIndex, String pageSize) {
//		APPResult result = new APPResult();
//		result = checkUser(authToken);
//		if (FAILCODE.equals(result.getStatusCode())) {
//			return result;
//		}
//		User user = (User) result.getData();
//		result = checkPageIndexAndPageSize(pageIndex, pageSize);
//		if (FAILCODE.equals(result.getStatusCode())) {
//			return result;
//		}
//		int[] pageParam =(int[]) result.getData();
//		int iPageIndex = pageParam[0];
//		int iPageSize = pageParam[1];
//		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_VIEWINVOICEINLIST)) {
//			result.setStatusCode(FAILCODE);
//			result.setMessage(NOAUTH);
//			result.setData(null);
//			return result;
//		}
//
//		List<InvoiceApplicationDto> applications = invoiceOutApplyService.query(null, getUserIds(user), null);
//
//		List<Map<String, Object>> list = toAppData(applications);
//		result.setData(list);
//		result.setStatusCode(SUCCESSCODE);
//		result.setMessage(Constant.SUCCESS);
//
//		
//		return result;
//	}

	// 3.34	上传合同
	@RequestMapping(value = "order/attachment/add.html", method = RequestMethod.POST)
	@ResponseBody
	public APPResult addAttachment(String authToken, MultipartFile file, String orderId, String contractId) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(orderId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkContractId(contractId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrderContract contract = (ConsignOrderContract) result.getData();
		
		if (!order.getId().equals(contract.getConsignOrderId())) {
			result.setStatusCode(FAILCODE);
			result.setMessage("合同与订单不对应");
			result.setData(null);
			return result;
		}
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_APPLYPAY)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		result =checkImg(file);
		if(FAILCODE.equals(result.getStatusCode())){
			return result;
		}
		ResultDto resultDto = consignOrderService.addCustomerContractAttachment(order.getId(), contract.getId(), file,
				user);
		if (!resultDto.isSuccess()) {
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}
	
	// 3.35	删除合同
	@RequestMapping(value = "order/attachment/delete.html")
	@ResponseBody
	public APPResult delAttachment(String authToken, String orderId, String contractId, String attachmentId) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		result = checkOrderId(orderId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrder order = (ConsignOrder) result.getData();
		result = checkContractId(contractId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrderContract contract = (ConsignOrderContract) result.getData();
		
		result = checkAttachmentId(attachmentId);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		ConsignOrderAttachment orderAttachment = (ConsignOrderAttachment) result.getData();
		if (!order.getId().equals(contract.getConsignOrderId())) {
			result.setStatusCode(FAILCODE);
			result.setMessage("合同与订单不对应");
			result.setData(null);
			return result;
		}
		if (!userPermissionService.hasPermission(user.getId(), user.getRoleId(), PERMISSION_APPLYPAY)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(NOAUTH);
			result.setData(null);
			return result;
		}
		ResultDto resultDto = consignOrderService.deleteCustomerContractAttachment(order.getId(), contract.getId(), orderAttachment.getId(), user,"");
		if (!resultDto.isSuccess()) {
			result.setStatusCode(FAILCODE);
			result.setMessage(resultDto.getMessage());
			result.setData(null);
			return result;
		}
		
		result.setStatusCode(SUCCESSCODE);
		result.setMessage(Constant.SUCCESS);
		result.setData(null);
		
		return result;
	}
	
	//3.36	获取服务中心业绩报表（总经理）
	@RequestMapping("/reports/service_center_achievement.html")
	@ResponseBody
	public APPResult serviceCenterAchievement(String authToken, String date) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
//		User user = (User) result.getData();
		result = checkMonth(date);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		date =(String) result.getData();
		
		
		return result;
	}
	
	//3.39	我的客户
	@RequestMapping("/reports/my_customer.html")
	@ResponseBody
	public APPResult myCustomer(String authToken, String type,String pageIndex, String pageSize) {
		APPResult result = new APPResult();
		result = checkUser(authToken);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		User user = (User) result.getData();
		int iPageIndex=1, iPageSize=10;
		result = checkPageIndexAndPageSize(pageIndex, pageSize);
		if (FAILCODE.equals(result.getStatusCode())) {
			return result;
		}
		int[] pageParam =(int[]) result.getData();
		iPageIndex = pageParam[0];
		iPageSize = pageParam[1];

		CompanyQuery query=new CompanyQuery();
		query.setStart((iPageIndex - 1) * iPageSize);
		query.setLength(iPageSize);
		query.setUserIdList(getUserIds(user));

		if(AccountType.buyer.toString().equals(type)){
			query.setAccountTag(AccountTag.buyer.getCode());
		}else if(AccountType.seller.toString().equals(type)){
			query.setAccountTag(AccountTag.seller.getCode());
		}else{
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递客户类型:买家(buyer)/卖家(seller)");
			result.setData(null);
			return result;
		}

        List<AccountForAppDto> accountList=accountService.queryAccountWithSingleContactByManager(query);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(AccountForAppDto account: accountList){
        	Map<String,Object> map = new LinkedHashMap<String,Object>();
			List<AccountTag> tagList = AccountTag.getNameByCode(account.getAccountTag());
        	String name = account.getName();
        	map.put("name", name==null?"":name);
        	map.put("isConsign", tagList.contains(AccountTag.consign));
        	map.put("time", account.getRegTime());
        	String business = account.getNsortName();
        	map.put("business", business==null?"":business);
        	String proxyFactory=account.getProxyFactory();
        	map.put("proxyFactory", proxyFactory==null?"":proxyFactory);
        	map.put("contact", account.getContact());
        	map.put("contact_tel", account.getContactTel());
        	list.add(map);
        }
        
        result.setStatusCode(SUCCESSCODE);
        result.setData(list);
        result.setMessage("");
		return result;
	}
	
	
	/**
     * 文件下载，如果是图片则自动设置content type为image
     * @param response
     * @param key
     */
    @RequestMapping("getfile")
    public void getFile(HttpServletResponse response,String key){
    	InputStream inStream = null;
    	OutputStream ostream = null;
    	try {
    		inStream = fileService.getFileData(key);
			ostream = response.getOutputStream();
			
			// set content type
			String suffix = FileUtil.getFileSuffix(key).toLowerCase();
			if(Constant.IMAGE_SUFFIX.contains("[" + suffix + "]")){
				response.setContentType("image/" + suffix);
			}else{
				//文件下载
				response.setContentType("multipart/form-data");
				response.setHeader("Content-Disposition", "attachment;fileName="+FileUtil.getFileName(key)+"." + suffix); 
			}
			
			byte[] buffer = new byte[1024];
			int count = inStream.read(buffer,0,buffer.length);
			while(count>0){
				ostream.write(buffer,0,count);
				count = inStream.read(buffer,0,buffer.length);
			}
		} catch (Exception e) {
			logger.error("read file failed!",e);
		}finally{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {}
			}
		}
    }
//    private AppUser findByUserId(Long userId){
//    	return cacheService.getUserByUserId(userId);
//    }
    
	private AppUser findByToken(String token) {
		try {
			Object o = cacheService.get(token);
			if(o==null){
				return null;
			}
			if(!(o instanceof AppUser)){
				return null;
			}
			return (AppUser) o;
		} catch (BusinessException e) {
			logger.error("token:" + token + "转换User出错");
			return null;
		}
	}

	private Map<String, Object> getInitParamMap(User user) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("start", 0);
		paramMap.put("length", 999);
		paramMap.put("userId", user.getId());
		return paramMap;
	}

	private List<Map<String, Object>> toAppData(List<?> origData) {
//		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//				+ request.getContextPath() + "/";
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		for (Object obj : origData) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if (obj instanceof ConsignOrderDto) {
				ConsignOrderDto dto = (ConsignOrderDto) obj;
				map.put("id", dto.getId());
				map.put("status", getOrderStatusNameByDto(dto));
				map.put("buyerName", dto.getAccountName());
				// if(dto.getCreated()!=null)
				map.put("time", dto.getCreatedStr());
				map.put("dealer", dto.getOwnerName());
				map.put("num", dto.getTotalQuantity());
				map.put("weight", dfWeight.format(dto.getTotalWeight()));
				map.put("amount", NumberTool.toThousandth(dto.getTotalAmount()));
				map.put("secondAmount", 0);
			} else if (obj instanceof PayRequestOrgDto) {
				PayRequestOrgDto dto = (PayRequestOrgDto) obj;
				map.put("id", dto.getQid());
				map.put("status", "待审核二次结算付款");
				map.put("buyerName", dto.getBuyerName());
				if (dto.getCreated() != null)
					map.put("time", sdfDateTime.format(dto.getCreated()));
				map.put("dealer", dto.getRequesterName());
				map.put("num", null);
				map.put("weight", null);
				map.put("amount", NumberTool.toThousandth(dto.getTotalAmount()));
				BigDecimal balance = accountService.queryById(dto.getBuyerId()).getBalanceSecondSettlement();
				map.put("secondAmount",
						BigDecimal.ZERO.compareTo(balance) == -1 ? NumberTool.toThousandth(balance) : 0);
			} else if (obj instanceof PayRequest) {
				PayRequest dto = (PayRequest) obj;
				map.put("id", dto.getId());
				map.put("status", "待审核提现付款");
				map.put("buyerName", dto.getBuyerName());
				if (dto.getCreated() != null){
					map.put("time", sdfDateTime.format(dto.getCreated()));
				}
				map.put("dealer", dto.getRequesterName());
				map.put("num", null);
				map.put("weight", null);
				map.put("amount", NumberTool.toThousandth(dto.getTotalAmount()));
				// BigDecimal balance
				// =accountService.queryById(dto.getBuyerId()).getBalanceSecondSettlement();
				// map.put("secondAmount",
				// BigDecimal.ZERO.compareTo(balance)==-1?balance.toString():0);
				Account account = accountService.queryById(dto.getBuyerId());
				map.put("secondAmount", NumberTool.toThousandth(account.getBalance().add(account.getBalanceSecondSettlement())));//可提现金额
			} else if (obj instanceof ConsignOrderItems) {
				ConsignOrderItems item = (ConsignOrderItems) obj;
				map.put("name", item.getNsortName());
				map.put("spec", item.getSpec());
				map.put("material", item.getMaterial());
				map.put("steel", item.getFactory());
				map.put("num", item.getQuantity());
				map.put("weight", dfWeight.format(item.getWeight()));
				map.put("weightConcept", item.getWeightConcept());
				map.put("basePrice", NumberTool.toThousandth(item.getCostPrice()));
				map.put("dealPrice", NumberTool.toThousandth(item.getDealPrice()));
				map.put("companyName", item.getSellerName());
			} else if (obj instanceof ConsignOrderSellerInfoDto) {
				ConsignOrderSellerInfoDto item = (ConsignOrderSellerInfoDto) obj;
				map.put("sellerId", item.getSellerDepartmentId());
				Account seller = accountService.queryById(item.getSellerDepartmentId());
				map.put("balanceSecondSettlement", seller.getBalanceSecondSettlement());//二结余额
				map.put("contractId", item.getContractId());
				map.put("company", item.getCompanyName());
				List<Map<String, Object>> bankinfo = new ArrayList<Map<String, Object>>();
				List<AccountBank> bankList = accountBankService.queryByAccountId(item.getSellerId());
				bankList.removeIf(a -> !AccountBankDataStatus.Approved.getCode().equals(a.getBankDataStatus()));// 移除银行状态不为审核通过的 add afeng
				Set<String> bankNameSet = new HashSet<String>();
				for (AccountBank ab : bankList) {
					String bankName = ab.getBankName()+"<->"+ab.getBankNameBranch();
					bankNameSet.add(bankName);
				}

				for (String bankName : bankNameSet) {
					Map<String, Object> bankNameInfo = new HashMap<String, Object>();
					String [] arrBankName = bankName.split("<->");
					List<Map<String,Object>> bankAccountNoList = getBankAccountInfoByBankName(arrBankName[0], arrBankName[1], bankList);
					String bankNamMain =arrBankName[0] , bankNameBranch=arrBankName[1];
					bankNameInfo.put("bankName", ("null".equals(bankNamMain)?"":bankNamMain) + " " + ("null".equals(bankNameBranch)?"":bankNameBranch));
					bankNameInfo.put("bankAccountNoList", bankAccountNoList);
					bankinfo.add(bankNameInfo);
				}

				map.put("bankinfo", bankinfo);
				map.put("address", item.getRegAddress());
				map.put("phone", item.getTel());
				map.put("contractAmount", NumberTool.toThousandth(item.getContractAmount()));
				double balanceSettlement = item.getBalanceSecondSettlement().doubleValue();
				map.put("settlementAmount", NumberTool.toThousandth(balanceSettlement>=0?0:-balanceSettlement));
				map.put("contractStatus", item.getContractStatus() == null || item.getContractStatus() == 0 ? "待上传合同"
						: item.getContractStatus() == 1 ? "合同已上传" : "未知");
				map.put("isPrcsteelContract", false);// 暂时必须输入，等1.1web端实现再做修改
				List<Map<String,Object>> contractPhotoUrl = new LinkedList<Map<String,Object>>();

				for (ConsignOrderAttachment coa : consignOrderAttachmentService
						.getAttachmentByContractId(item.getContractId())) {
					Map<String,Object> url = new LinkedHashMap<String,Object>();
					url.put("id",coa.getId());
					url.put("url","getfile.html?key=" + coa.getFileUrl().replaceAll("\\\\", "\\\\\\\\"));
					contractPhotoUrl.add(url);
				}
				// if(item.getContractStatus().equals(1)){
				map.put("contractPhotoUrl", contractPhotoUrl);
				// }
			} else if (obj instanceof PayRequestItemsInfoDto) {
				PayRequestItemsInfoDto item = (PayRequestItemsInfoDto) obj;
				// map.put("id", item.getId());
				map.put("sellerId", item.getReceiverDepartmentId());
				map.put("company", item.getReceiverName());
				map.put("bankName", item.getReceiverBankName() + item.getReceiverBranchBankName());
				map.put("bankAccountNo", item.getReceiverAccountCode());
				map.put("address", item.getReceiverRegAddr());
				map.put("phone", item.getReceiverTel());
				map.put("applyAmount", NumberTool.toThousandth(item.getPayAmount()));
				map.put("isPrcsteelContract", false);// 暂时必须输入，等1.1web端实现再做修改
				ConsignOrderContract coc = consignOrderContractService.selectByPrimaryKey(item.getContractId());
				map.put("contractNo", coc.getContractCodeCust());
				List<Map<String,Object>> contractPhotoUrl = new LinkedList<Map<String,Object>>();

				for (ConsignOrderAttachment coa : consignOrderAttachmentService
						.getAttachmentByContractId(item.getContractId())) {
					Map<String,Object> url = new LinkedHashMap<String,Object>();
					url.put("id",coa.getId());
					url.put("url","getfile.html?key=" + coa.getFileUrl().replaceAll("\\\\", "\\\\\\\\"));
					contractPhotoUrl.add(url);
				}
				// if(item.getContractStatus().equals(1)){
				map.put("contractPhotoUrl", contractPhotoUrl);

//			} else if (obj instanceof PoolIn) {
//				PoolIn pi = (PoolIn) obj;
//				map.put("id", pi.getId());
//				Account account = accountService.queryById(pi.getSellerId());
//				User user = userService.queryById(account.getManagerId());
//				if(userService.isManager(this.loginUser)){
//					map.put("managerName", user.getName());
//				}else{
//					map.put("managerName", "");
//				}
//				
//				map.put("companyName", pi.getSellerName());
//				map.put("weight", dfWeight.format(pi.getTotalWeight()));
//				map.put("amount", NumberTool.toThousandth(pi.getTotalAmount()));
			} else if (obj instanceof PoolInDetailDto) {
				PoolInDetailDto pidd = (PoolInDetailDto) obj;
				map.put("name", pidd.getNsortName());
				map.put("material", pidd.getMaterial());
				map.put("spec", pidd.getSpec());
				map.put("weight", dfWeight.format(pidd.getTotalWeight()));
				map.put("amount", NumberTool.toThousandth(pidd.getTotalAmount()));
			} else if (obj instanceof AccountUserDto) {
				AccountUserDto aud = (AccountUserDto) obj;
				// “id”:”账户id”,
				// “status”:”二次结算应收/应付”,
				// “time”:”下单时间”,
				// “companyName”：”公司全称”,
				// “dealer”:”交易员姓名”,
				// “bankAccountBalancesecondAmount”：”资金账户余额/账户可提现金额 10,000.00”，
				// “amount”：”应收/应付金额10,000.00”
				map.put("id", aud.getDepartmentId());
				if (aud.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) == 1) {
					map.put("status", "二次结算应付");
				} else {
					map.put("status", "二次结算应收");
				}
				map.put("balance", NumberTool.toThousandth(accountService.queryById(aud.getDepartmentId()).getBalance()));//资金余额

				// map.put("time", aud.get);

				map.put("companyName", aud.getAccountName());
				map.put("dealer", aud.getManagerName());
				
				map.put("settleAmount",
						NumberTool.toThousandth(Math.abs(aud.getBalanceSecondSettlement().doubleValue())));
			}
			// }else if(obj instanceof InvoiceApplicationDto){
			// InvoiceApplicationDto iad = (InvoiceApplicationDto) obj;
			// map.put("id", );
			// map.put("companyName", );
			// map.put("status", );
			// map.put("weight", dfWeight.format(pidd.getTotalWeight()));
			// map.put("amount",
			// NumberTool.toThousandth(pidd.getTotalAmount()));
			// map.put("num", iad.get);
			// }

			list.add(map);
		}
		return list;
	}

	protected List<Long> getUserIds(User user) {

		List<Long> ids = new ArrayList<Long>();

        Role role = roleService.queryById(user.getRoleId());

        if (RoleAuthType.ALL.getValue() == role.getType()) { //所有数据

            return null;
        } else if (RoleAuthType.SAME_LEVEL.getValue() == role.getType()) { //同级＋下级

            List<Long> roles = roleService.queryRoleIds(role.getParentId());
            if (roles.size() > 0) {
                ids = userService.queryUserIdsByRoleIds(roles);
            }
            return ids;
        } else if (RoleAuthType.LOWER_LEVEL.getValue() == role.getType()) { //下级
            List<Long> roles = roleService.queryRoleIds(role.getId());
            if (roles.size() > 0) {
                ids = userService.queryUserIdsByRoleIds(roles);
            }
            ids.add(user.getId());
            return ids;
        } else { //仅自己
            ids.add(user.getId());
            return ids;
        }
	}

	private String getOrderStatusNameByDto(ConsignOrderDto dto) {
		if (Constant.ConsignOrderStatus.NEW.getCode().equals(dto.getStatus())) {
			return "待审核";
		} else if (Constant.ConsignOrderStatus.NEWAPPROVED.getCode().equals(dto.getStatus())) {
			return "待关联";
		} else if ((Constant.ConsignOrderStatus.RELATED.getCode().equals(dto.getStatus())
				|| Constant.ConsignOrderStatus.SECONDSETTLE.getCode().equals(dto.getStatus()))
				&& Constant.ConsignOrderPayStatus.APPLY.toString().equals(dto.getPayStatus())) {
			return "待申请付款";
		} else if ((Constant.ConsignOrderStatus.RELATED.getCode().equals(dto.getStatus())
				|| Constant.ConsignOrderStatus.SECONDSETTLE.getCode().equals(dto.getStatus()))
				&& Constant.ConsignOrderPayStatus.REQUESTED.toString().equals(dto.getPayStatus())) {
			return "待审核付款";
		} else if (Constant.ConsignOrderStatus.CLOSEREQUEST1.getCode().equals(dto.getStatus())
				|| Constant.ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(dto.getStatus()) || 
				ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode().equals(dto.getStatus()) || ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode().equals(dto.getStatus())) {
			return "待审核交易关闭";
		}
		return null;
	}

//	private static List<String> getBankAccountByBankName(String bankName, String bankBranchName,
//			List<AccountBank> origData) {
//		List<String> returnData = new ArrayList<String>();
//		for (AccountBank ab : origData) {
//			if (StringUtils.isEmpty(bankName)) {
//				returnData.add(ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
//			} else {
//				if (StringUtils.isEmpty(bankBranchName)) {
//					if (bankName.trim().equals(ab.getBankName().trim())) {
//						returnData.add(ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
//					}
//				} else {
//					if (bankName.trim().equals(ab.getBankName().trim())
//							&& bankBranchName.trim().equals(ab.getBankNameBranch().trim())) {
//						returnData.add(ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
//					}
//				}
//			}
//		}
//		return returnData;
//	}

	private static List<Map<String,Object>> getBankAccountInfoByBankName(String bankName, String bankBranchName,
			List<AccountBank> origData) {
		List<Map<String,Object>> returnData = new ArrayList<Map<String,Object>>();
		for (AccountBank ab : origData) {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			if (StringUtils.isEmpty(bankName)||"null".equals(bankName)) {
				map.put("id", ab.getId());
				map.put("No", ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
				returnData.add(map);
			} else {
				if (StringUtils.isEmpty(bankBranchName)||"null".equals(bankBranchName)) {
					if (bankName.trim().equals(ab.getBankName().trim())) {
						map.put("id", ab.getId());
						map.put("No", ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
						returnData.add(map);
					}
				} else {
					if (ab.getBankName()!=null&&ab.getBankNameBranch()!=null&&bankName.trim().equals(ab.getBankName().trim())
							&& bankBranchName.trim().equals(ab.getBankNameBranch().trim())) {
						map.put("id", ab.getId());
						map.put("No", ab.getBankAccountCode().replaceAll("([\\d]{4})(?=\\d)", "$1 "));
						returnData.add(map);
					}
				}
			}
		}
		return returnData;
	}
	
	private APPResult checkUser(String authToken) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(authToken)) {
			result.setStatusCode(FAILCODE);
			result.setMessage(LOGINFIRST);
			return result;
		}
		AppUser appUser = findByToken(authToken);
		if(appUser==null){
			result.setStatusCode(FAILCODE);
			result.setMessage(LOGINFIRST);
			return result;
		}
		User user = appUser.getUser();
		if (user == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage(LOGINFIRST);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(user);
		return result;
	}

	private APPResult checkOrderId(String orderId) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(orderId)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递要查询的订单ID");
			return result;
		}
		Long lOrderId = 0L;
		try {
			lOrderId = Long.parseLong(orderId);
		} catch (Exception e) {
			logger.error("转换订单ID出错:" + orderId);
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递有效的订单ID");
			return result;
		}
		ConsignOrder order = consignOrderService.queryById(lOrderId);
		if (order == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的订单");
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(order);
		return result;
	}
	private APPResult checkOperation(String operation) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(operation)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递要操作的代码值：operation");
			return result;
		}
		boolean op;
		if ("true".equals(operation)) {
			op = true;
		} else if ("false".equals(operation)) {
			op = false;
		} else {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递正确的操作代码值operation:" + operation);
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(op);
		return result;
	}
	
	private APPResult checkAccountId(String accountId) {
		APPResult result = new APPResult();
		Long lAccountId = 0L;
		try {
			lAccountId = Long.parseLong(accountId);
		} catch (Exception e) {
			logger.error("转换客户ID出错:" + accountId);
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递有效的客户ID");
			return result;
		}
		Account account = accountService.queryById(lAccountId);
		if (account == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定ID:" + lAccountId + "的客户");
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(account);
		return result;
	}
	private APPResult checkContractId(String contractId) {
		APPResult result = new APPResult();
		Long lContractId = 0L;
		try {
			lContractId = Long.parseLong(contractId);
		} catch (Exception e) {
			logger.error("转换合同ID出错:" + contractId);
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递有效的合同ID");
			return result;
		}
		ConsignOrderContract contract = consignOrderContractService.selectByPrimaryKey(lContractId);
		if (contract == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定ID:" + lContractId + "的合同");
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(contract);
		return result;
	}
	
	private APPResult checkImg(MultipartFile file) {
		APPResult result = new APPResult();
		if(file==null||file.getSize()==0){
			result.setStatusCode(FAILCODE);
			result.setMessage("文件为空");
			return result;
		}
		String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());
		if (!Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
			result.setStatusCode(FAILCODE);
			result.setMessage("文件格式不正确");
			return result;
		}
		if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
			result.setStatusCode(FAILCODE);
			result.setMessage("图片超过" + Constant.MAX_IMG_SIZE + "M");
			return result;
		}
		
		return result;
	}
	
	private APPResult checkPayRequestId(String id) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(id)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未传递付款申请ID");
			return result;
		}
		Long lRequestId = 0L;
		try {
			lRequestId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			result.setStatusCode(FAILCODE);
			result.setMessage("付款申请ID格式错误");
			return result;
		}

		PayRequest payRequest = payRequestService.selectByPrimaryKey(lRequestId);
		if (payRequest == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到ID为：" + lRequestId + "的付款申请单");
			return result;
		}
		if(Constant.PayRequestType.PAYMENT.getCode().equals(payRequest.getType())){
			Long orderId = payRequest.getConsignOrderId();
			if (orderId == null) {
				result.setStatusCode(FAILCODE);
				result.setMessage("指定付款申请单中未找到对应的寄售订单ID");
				return result;
			}
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(payRequest);
		return result;
	}
	
	private APPResult checkPageIndexAndPageSize(String pageIndex,String pageSize){
		APPResult result =new APPResult();
		Integer iPageIndex=1,iPageSize =10;
		if (StringUtils.isEmpty(pageIndex)) {
			iPageIndex = 1;
		} else {
			try {
				iPageIndex = Integer.valueOf(pageIndex);
				if (iPageIndex <= 0) {
					result.setStatusCode(FAILCODE);
					result.setMessage("页码应该是正整数");
					return result;
				}
			} catch (NumberFormatException e) {
				result.setStatusCode(FAILCODE);
				result.setMessage("页码参数格式错误");
				return result;
			}
		}
		if (StringUtils.isEmpty(pageSize)) {
			iPageSize = 10;
		} else {
			try {
				iPageSize = Integer.valueOf(pageSize);
				if (iPageSize <= 0) {
					result.setStatusCode(FAILCODE);
					result.setMessage("每页记录数应该是正整数");
					return result;
				}
			} catch (NumberFormatException e) {
				result.setStatusCode(FAILCODE);
				result.setMessage("每页记录数参数格式错误");
				return result;
			}
		}
		result.setStatusCode(SUCCESSCODE);
		int[] pageParams = new int[]{iPageIndex,iPageSize};
		result.setData(pageParams);
		return result;
	}
	
	private APPResult checkInvoiceInId(String id){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(id)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递要审核的进项票ID");
			return result;
		}
		Long lId = 0L;
		try {
			lId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			logger.error("转换进项票ID出错:" + id);
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递有效的进项票ID");
			return result;
		}

		PoolIn pi = poolInService.selectByPrimaryKey(lId);

		if (pi == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的进项票");
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(pi);
		return result;
	}
	
	private APPResult checkAttachmentId(String id){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(id)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递合同附件ID");
			return result;
		}
		Long lId = 0L;
		try {
			lId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			logger.error("转换合同附件ID出错:" + id);
			result.setStatusCode(FAILCODE);
			result.setMessage("请传递有效的合同附件ID");
			return result;
		}

		ConsignOrderAttachment consignOrderAttachment = consignOrderAttachmentService.queryById(lId);

		if (consignOrderAttachment == null) {
			result.setStatusCode(FAILCODE);
			result.setMessage("未找到指定的合同附件");
			return result;
		}
		result.setStatusCode(SUCCESSCODE);
		result.setData(consignOrderAttachment);
		return result;
	}
	
	private APPResult checkDeductionSettlementAmount(String deductionSettlementAmount){
		APPResult result = new APPResult();
		if(StringUtils.isEmpty(deductionSettlementAmount)){
			result.setStatusCode(FAILCODE);
			result.setMessage("抵扣金额不能为空");
			result.setData(null);
			return result;
		}
		double dDeductionSettlementAmount = 0;
		try{
			dDeductionSettlementAmount = Double.parseDouble(deductionSettlementAmount);
			if(dDeductionSettlementAmount<0){
				result.setStatusCode(FAILCODE);
				result.setMessage("抵扣金额不能为负数");
				result.setData(null);
				return result;
			}
		}catch(NumberFormatException e){
			result.setStatusCode(FAILCODE);
			result.setMessage("抵扣金额类型有误");
			result.setData(null);
			return result;
		}
		result.setData(dDeductionSettlementAmount);
		result.setStatusCode(SUCCESSCODE);
		return result;
	}
	
	private APPResult checkAmount(String amount){
		APPResult result = new APPResult();
		if(StringUtils.isEmpty(amount)){
			result.setStatusCode(FAILCODE);
			result.setMessage("提现金额不能为空");
			result.setData(null);
			return result;
		}
		double dAmount = 0;
		try{
			dAmount = Double.parseDouble(amount);
			if(dAmount<0){
				result.setStatusCode(FAILCODE);
				result.setMessage("提现金额不能为负数");
				result.setData(null);
				return result;
			}
		}catch(NumberFormatException e){
			result.setStatusCode(FAILCODE);
			result.setMessage("提现金额类型有误");
			result.setData(null);
			return result;
		}
		result.setData(dAmount);
		result.setStatusCode(SUCCESSCODE);
		return result;
	}
	
	private APPResult checkDate(String date){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(date)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供查询日期");
			result.setData(null);
			return result;
		} else {
			try {
				Date d = sdfDate.parse(date);
				date = sdfDate.format(d);
			} catch (ParseException e) {
				try {
					Date d = sdfDate2.parse(date);
					date = sdfDate.format(d);
				}catch(ParseException e2){
					result.setStatusCode(FAILCODE);
					result.setMessage("日期格式错误");
					result.setData(null);
					return result;
				}
				
			}
		}
		result.setData(date);
		result.setStatusCode(SUCCESSCODE);
		return result;
	}
	
	private APPResult checkMonth(String month){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(month)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请提供查询月份");
			result.setData(null);
			return result;
		} else {
			try {
				Date d = sdfMonth.parse(month);
				month = sdfMonth.format(d);
			} catch (ParseException e) {
				try {
					Date d = sdfMonth2.parse(month);
					month = sdfMonth.format(d);
				}catch(ParseException e2){
					result.setStatusCode(FAILCODE);
					result.setMessage("月份格式错误");
					result.setData(null);
					return result;
				}
			}
		}
		result.setData(month);
		result.setStatusCode(SUCCESSCODE);
		return result;
	}
	private String getOrderNoLast6(String orderNo){
		if(StringUtils.isEmpty(orderNo)){
			return "";
		}else if(orderNo.length()<6){
			return orderNo;
		}else{
			return orderNo.substring(orderNo.length()-6);
		}
	}
	// 获取所有友情链接给超市 暂时不用
	/*@RequestMapping(value="/order/getLinks",method=RequestMethod.GET)
	@ResponseBody
	public APPResult getAllFriendlyLink() {
		APPResult result = new APPResult();
		try {
			List<BaseFriendlyLink> links = userService.selectAllLink();
			if (links.size() == 0 ) {
				result.setStatusCode(FAILCODE);
				result.setMessage("无友情链接");
				return result;
			}
			result.setStatusCode(SUCCESSCODE);
			result.setMessage("success");
			result.setData(links);
		}catch (BusinessException e) {
			result.setStatusCode(e.getCode());
			result.setMessage(e.getMsg());
			logger.info(e.getMsg());
		}

		return result;
	}
*/
}
