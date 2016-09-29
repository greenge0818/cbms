package com.prcsteel.platform.smartmatch.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.model.InquiryDetailData;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;
import com.prcsteel.platform.smartmatch.model.model.RestOrganization;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderItemsQuery;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;
import com.prcsteel.platform.smartmatch.model.vo.SmartVo;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderDao;
import com.prcsteel.platform.smartmatch.service.*;
import com.prcsteel.platform.smartmatch.web.support.ShiroVelocity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rolyer on 2015/11/17.
 */
@Controller
@RequestMapping("/smartmatch/purchaseorder/")
public class PurchaseOrderController extends BaseController {

	//增加日志
	private Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

	@Resource
	private AreaService areaService;
	@Resource
	CreatePurchaseOrderService createPurchaseOrderService;
	@Resource
	private PurchaseOrderService purchaseOrderService;
	@Resource
	private QuotationOrderService quotationOrderService;
	@Resource
	private QuotationOrderItemsService quotationOrderItemsService;
	@Resource
	private PurchaseOrderDao purchaseOrderDao;
	@Resource
	private AdvanceOrganizationService advanceOrganizationService;
	//权限控制
	private ShiroVelocity permissionLimit = new ShiroVelocity();
	//待处理的指派按钮
	private final String PERMISSION_AUDIT_ASSI = "smartmatch:purchaseorder:list:assi";
	//新增询价单按钮的权限
	private final String PERMISSION_AUDIT_CREATE_PURCHASE_ORDER = "smartmatch:purchaseorder:list:page";
	//待开单指派链接的权限
    private final String PERMISSION_QUOTATIONINFO_ASSI = "smartmatch:purchaseorder:list:pdassi";
    //待指派
    private final String PERMISSION_PENDING_ASSI = "smartmatch:purchaseorder:p1";
    //已完成
    private final String PERMISSION_FINISH = "smartmatch:purchaseorder:p2";
    //待开单
    private final String PERMISSION_PENDING_OPENBILL = "smartmatch:purchaseorder:p3";
    //待处理
    private final String PERMISSION_PENDING_ACCEPTE = "smartmatch:purchaseorder:pending_accepte";
	private final String IS_ORG_MANAGER = "IsOrgManager";
	/**
	 * 询价详情数量
	 */
	private final String DETAIL_COUNT = "detailCount";
	/**
	 * 报价详情数量
	 */
	private final String QUATATION_COUNT = "quotaCount";

	private final String PURCHASE_ORDERID = "purchaseOrderId";

	/**
	 * 新增采购
	 *
	 * @param out
	 * @param isDetail 是否属于询价单详情页显示
	 * @param id
	 *            采购单编号，用于初始化采购单信息。
	 */
	@RequestMapping("created")
	public void created(ModelMap out, String id,String status, String blockInquiryOrderSellerIds, String isDetail) {

		User user = getLoginUser();
		out.put("user", user);
		out.put("org", getOrganization());
		try {
			out.put(IS_ORG_MANAGER, RoleType.Manager.toString().equals(getUserRole().getRoleType().toString())
					&& getUserRole().getName().contains("服务中心"));
		} catch (Exception e1) {
			out.put(IS_ORG_MANAGER,"");
		}
		if (StringUtils.isNotBlank(blockInquiryOrderSellerIds)) {
			out.put("blockInquiryOrderSellerIds", blockInquiryOrderSellerIds);
		}

		// 当有ID时，取得采购单详情信息。
		if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
			try {
				//待处理的页面点击受理，此询价单状态变成待报价  modify by caosulin 20160704
				if(PurchaseOrderStatus.PENDING_ACCEPTE.getCode().equals(status)){
					purchaseOrderService.updateStatusById(Long.parseLong(id), PurchaseOrderStatus.PENDING_QUOTE.getCode(), this.getLoginUser().getLoginId());
				}
				List<InquiryDetailData> detailList = purchaseOrderService.getInquiryHistoryByPurchaseId(Long.parseLong(id));
				out.put(DETAIL_COUNT, detailList == null ? 0 : detailList.size());
				/*** 报价单详情数据 ***/
				List list = quotationOrderItemsService.selectQuotationByPurchaseOrderId(Long.parseLong(id));
				out.put(QUATATION_COUNT, list == null ? 0 : list.size());
			} catch (Exception e) {
				logger.error("===" + e.getMessage(),e);
			}
			PurchaseOrder order = purchaseOrderDao.selectByPrimaryKey(Long.valueOf(id));
			out.put("id", id);
			out.put("orderStatus", order.getStatus());
			out.put("isDetail", isDetail);
			out.put(PURCHASE_ORDERID, id);
			out.put("requirementCode", order.getRequirementCode());
			out.put("preOwnerId", order.getPreOwnerId());
		}
	}

	/**
	 * 根据询价ID获取采购单详情信息
	 *
	 * @param id
	 *            询价单ID
	 * @return
	 */
	@RequestMapping(value = "retrieve/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Result retrieve(@PathVariable String id) {
		Result result = new Result();
		try {
			// 当有ID时，取得采购单详情信息。
			if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
				PurchaseOrderDetailDto purchaseorder = purchaseOrderService
						.retrievePurchaseOrderById(Long.parseLong(id));
				result.setData(purchaseorder);
			}
		} catch (BusinessException e) {
			logger.error("===" + e.getMessage());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}

		return result;
	}

	/**
	 * 根据询价ID获取采购单详情信息
	 *
	 * @return
	 */
	@RequestMapping(value = "getHistory", method = RequestMethod.POST)
	@ResponseBody
	public Result getHistory(String tel, String accountName) {
		Result result = new Result();

		List<PurchaseOrderItemsDto> items = purchaseOrderService
				.getHistoryPurchaseOrderItemsByTelOrAccount(new PurchaseOrderItemsQuery(tel, accountName));
		if (items != null && !items.isEmpty()) {
			result.setData(items);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 采购单管理页面
	 *
	 * @param out
	 * @return
	 */
	@RequestMapping("list")
	public String list(ModelMap out,Integer tabIndex) {
		//add by caosulin 如果4个tab页签都没有权限则，跳转没有权限的页面。
		Boolean tab0 =  permissionLimit.hasPermission(PERMISSION_PENDING_ACCEPTE);//待处理
		Boolean tab1 =  permissionLimit.hasPermission(PERMISSION_PENDING_ASSI);//待指派
		Boolean tab2 =  permissionLimit.hasPermission(PERMISSION_PENDING_OPENBILL);//待开单
		Boolean tab3 =  permissionLimit.hasPermission(PERMISSION_FINISH);//已完成
		if (!tab0 && !tab1 && !tab2 && !tab3) {//如果都没有权限
			return "/unauth";
		}
		try {
			//增加待指派开单的控制
			Boolean quotaionAssi = permissionLimit.hasPermission(PERMISSION_QUOTATIONINFO_ASSI);
			out.put("quotaionAssi", quotaionAssi);
			
			out.put("tabIndex", tabIndex==null?0:tabIndex);
			out.put(IS_ORG_MANAGER, RoleType.Manager.toString().equals(getUserRole().getRoleType().toString())
					&& getUserRole().getName().contains("服务中心"));
			out.put("orgId",this.getOrganization().getId());

		}catch (Exception e){
			logger.error("====" + e.getMessage());
			out.put(IS_ORG_MANAGER, "");
		}
		//添加权限控制字段
		out.put("assi_permission", permissionLimit.hasPermission(PERMISSION_AUDIT_ASSI));
		return "/smartmatch/purchaseorder/list";
	}

	/**
	 * 分拣URL连接跳转页面
	 *
	 * @param out
	 * @return
	 */
	@RequestMapping("assdirect")
	public String assDirect(ModelMap out,String requirementCode) {
		// 判断权限判断当前登录人是否有当前的跳转权限
		boolean create_permission = permissionLimit.hasPermission(PERMISSION_AUDIT_CREATE_PURCHASE_ORDER);
		if(!create_permission){
			return "/unauth";
		}
		//如果需求单号传递过来为空，跳转到列表页面
		if(requirementCode == null || "".equals(requirementCode)){
			return this.list(out, 0);
		}else{//如果不为空，则跳转到询价页面
			Long orderId = purchaseOrderService.searchPurchaseOrderByrequrimentCode(requirementCode);
			if(orderId == null){
				return this.list(out, 0);
			}else{
				this.created(out, orderId+"", null, null, null);
				return "/smartmatch/purchaseorder/created";
			}

		}
	}

	/**
	 * 根据中心城市获得周边城市 或 周围城市获得中心城市
	 *
	 * @param cityId
	 *            城市 id
	 * @return
	 */
	@RequestMapping("getCitys")
	@ResponseBody
	public Result getCitysById(@RequestParam("cityId") Long cityId) {
		return new Result(areaService.getCitysById(cityId));
	}

	// init
	@RequestMapping("GetSortAndNsort")
	@ResponseBody
	public SmartVo getSortAndNsort() {
		return new SmartVo(createPurchaseOrderService.getSortAndNsort());
	}

	// after select nsort
	@RequestMapping("GetMaterial")
	@ResponseBody
	public SmartVo getMaterial(String nosrtUUID) {
		return new SmartVo(createPurchaseOrderService.getMaterial(nosrtUUID));
	}

	@RequestMapping("GetFactory")
	@ResponseBody
	public SmartVo getFactory(String nosrtUUID) {
		return new SmartVo(createPurchaseOrderService.getFactory(nosrtUUID));
	}

	@RequestMapping("GetAttribute")
	@ResponseBody
	public SmartVo getAttribute(String nosrtUUID) {
		return new SmartVo(createPurchaseOrderService.getAttribute(nosrtUUID));
	}

	// after select material
	@RequestMapping("GetSpec")
	@ResponseBody
	public SmartVo getSpec(String nosrtUUID, String materialUUID) {
		return new SmartVo(createPurchaseOrderService.getSpec(nosrtUUID, materialUUID));
	}

	/**
	 * 查询公司
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAllAccount")
	public Result getAllAccount() {
		Result result = new Result();

		result.setData(purchaseOrderService.getAllAccount());
		result.setSuccess(true);

		return result;
	}

	/**
	 * 根据电话/联系人查询公司
	 *
	 * @param tel
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fetchAccount")
	public Result fetchAccount(String tel, String name) {
		Result result = new Result();
		result.setData(purchaseOrderService.queryAccount(tel, name));
		result.setSuccess(true);

		return result;
	}

	/**
	 * 根据电话/联系人查询联系人
	 *
	 * @param tel
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fetchAccountContact")
	public Result fetchAccountCentact(String tel, String name) {
		Result result = new Result();

		result.setData(purchaseOrderService.queryAccountContact(tel, name));
		result.setSuccess(true);

		return result;
	}

	/**
	 * 保存采购单
	 *
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Result save(PurchaseOrder po, String itemList) {
		Result result = new Result();
		try {
			List<PurchaseOrderItemsDto> list = new Gson().fromJson(itemList,
					new TypeToken<List<PurchaseOrderItemsDto>>() {
					}.getType());
			PurchaseOrder purchaseOrder = purchaseOrderService.save(po, list, getLoginUser());
			if (purchaseOrder != null && purchaseOrder.getId().intValue() > 0) {
				result.setData(purchaseOrder);
				result.setSuccess(true);
			}
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		} catch (JsonSyntaxException e) {
			logger.error("Json解析出错" + e.getMessage());
			result.setSuccess(false);
			result.setData("Json解析出错");
		}
		return result;
	}

	/**
	 * 展示采购单列表
	 *
	 * @param purchaseOrderQuery
	 * @return
	 */
	@RequestMapping("search.html")
	@ResponseBody
	public PageResult search(PurchaseOrderQuery purchaseOrderQuery) {
		purchaseOrderQuery.setUserIds(getUserIds());
		purchaseOrderQuery.setUser(this.getLoginUser());
		if (purchaseOrderQuery.getTabIndex() != null) {
			Role role = getUserRole();
			List<String> statusList = new ArrayList<>();
			String status = purchaseOrderQuery.getStatus();
			if(StringUtils.isNotEmpty(status) && !"-1".equals(status)){
				statusList.add(status);
			}else{
				if ("0".equals(purchaseOrderQuery.getTabIndex())) {
					statusList.add(PurchaseOrderStatus.PENDING_ACCEPTE.getCode());
					statusList.add(PurchaseOrderStatus.PENDING_QUOTE.getCode());
					statusList.add(PurchaseOrderStatus.PENDING_CLERK_ACCEPTE.getCode());
					statusList.add(PurchaseOrderStatus.QUOTED.getCode());
				} else if ("1".equals(purchaseOrderQuery.getTabIndex())) {
					// 指派后不可以重新指派
					statusList.add(PurchaseOrderStatus.PENDING_DIRECTOR_ASSIGNED.getCode());
					statusList.add(PurchaseOrderStatus.PENDING_SERVER_MANAGER_ASSIGNED.getCode());
				} else if ("2".equals(purchaseOrderQuery.getTabIndex())) {
					statusList.add(PurchaseOrderStatus.BILLED.getCode());
					statusList.add(PurchaseOrderStatus.CLOSED.getCode());
				} else if ("3".equals(purchaseOrderQuery.getTabIndex())){
					statusList.add(PurchaseOrderStatus.PENDING_OPEN_BILL.getCode());
				}else {
					PageResult result = new PageResult();
					result.setData(null);
					result.setRecordsFiltered(0);
					result.setRecordsTotal(0);
					return result;
				}
			}
			purchaseOrderQuery.setStatusList(statusList);
		} else {
			PageResult result = new PageResult();
			result.setData(null);
			result.setRecordsFiltered(0);
			result.setRecordsTotal(0);
			return result;
		}
		Integer total = purchaseOrderService.countPurchaseOrder(purchaseOrderQuery);
		List<PurchaseOrderDto> list = purchaseOrderService.selectPurchaseList(purchaseOrderQuery);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(total);
		return result;
	}

	/**
	 * 得到采购单资源明细
	 *
	 * @param purchaseOrderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "{purchaseOrderId}/itemlist")
	public Result itemlist(@PathVariable Long purchaseOrderId) {
		Result result = new Result();
		try {
			List<PurchaseOrderItems> itemList = purchaseOrderService.getItemList(purchaseOrderId);
			result.setData(itemList);
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	/**
	 * 推送报价并且关闭
	 * @param id  采购单编号
	 * 
	 */
	@RequestMapping("pushAndCloseOrder/{id}")
	@ResponseBody
	public Result pushAndCloseOrder(@PathVariable("id") Long id, String reason) {
		try {
			purchaseOrderService.pushAndCloseOrder(id, reason, getLoginUser());
			return new Result();
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			return new Result(e.getMsg(), false);
		}
	}
	/**
	 * 关闭或激活采购单
	 *
	 * @param id
	 *            采购单编号
	 */
	@RequestMapping("{id}/{operation}")
	@ResponseBody
	public Result close(@PathVariable("id") Long id, @PathVariable("operation") String operation, String reason) {
		try {
			purchaseOrderService.closeOrder(id, operation, reason, getLoginUser());
			return new Result();
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			return new Result(e.getMsg(), false);
		}
	}

	@RequestMapping("loadFactoryByCategory")
	@ResponseBody
	public Result loadFactoryByCategory(Long purchaseOrderId) {
		Result result = new Result();
		try {
			result.setData(purchaseOrderService.loadFactoryByCategory(purchaseOrderId));
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 显示采购单详情
	 *
	 * @param out
	 * @param id
	 * @return
	 */
	@RequestMapping("{id}/show")
	public String view(ModelMap out, @PathVariable Long id) {
		out.put("purchaseOrder", purchaseOrderService.queryByIdForShow(id));
		return "/smartmatch/purchaseorder/show";
	}

	/**
	 * 根据采购单Id获取报价单信息
	 *
	 * @param purchaseOrderId
	 * @return
	 */
	@RequestMapping("getdata")
	@ResponseBody
	public Result getData(Long purchaseOrderId) {
		Result result = new Result();
		try {
			Map<String, List> data = new HashMap<>();
			data.put("purchaseOrderItems", purchaseOrderService.getItemsByPurchaseOrderId(purchaseOrderId));
			data.put("quotationOrders", quotationOrderService.selectByPurchaseOrderId(purchaseOrderId));
			result.setData(data);
			result.setSuccess(true);
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 询价单详情
	 *
	 * @param out
	 * @param purchaseOrderId
	 * @return
	 */
	@RequestMapping("{purchaseOrderId}/inquiryorderdetail")
	public String toInquiryOrderDetail(ModelMap out, @PathVariable Long purchaseOrderId) {
		// 询价单个数
		List<InquiryDetailData> detailList = purchaseOrderService.getInquiryHistoryByPurchaseId(purchaseOrderId);
		out.put("detailCount", detailList.size());
		out.put("purchaseOrderId", purchaseOrderId);

		/*** 报价单详情数据 ***/
		List list = quotationOrderItemsService.selectQuotationByPurchaseOrderId(purchaseOrderId);
		out.put("quotaCount", list == null ? 0 : list.size());

		return "/smartmatch/purchaseorder/orderDetail";
	}

	/**
	 * 询价详情
	 *
	 * @param out
	 * @param purchaseOrderId
	 * @return
	 */
	@RequestMapping("{purchaseOrderId}/inquirydetail")
	public String toInquiryDetail(ModelMap out, @PathVariable Long purchaseOrderId) {
		List<InquiryDetailData> detailList = purchaseOrderService.getInquiryHistoryByPurchaseId(purchaseOrderId);
		out.put("detailList", detailList);
		out.put("detailCount", detailList.size());
		out.put("purchaseOrderId", purchaseOrderId);

		/*** 报价单详情数据 ***/
		List list = quotationOrderItemsService.selectQuotationByPurchaseOrderId(purchaseOrderId);
		out.put("quotaCount", list == null ? 0 : list.size());

		return "/smartmatch/purchaseorder/detail";
	}

	/**
	 * 指派采购单
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("orderAssign")
	@ResponseBody
	public Result orderAssign(Long id, Integer assignto, Long orgId, Long userId, Integer activate) {
		Result result = new Result();
		try {
			Long newUserId = userId;
			if (assignto.equals(1)) { // 指派给服总
				RestOrganization org = advanceOrganizationService.queryById(orgId);
				newUserId = org.getCharger();
			}
			purchaseOrderService.assignOrder(id, assignto, orgId, newUserId, (activate != null) && (activate == 1), getLoginUser());
			result.setSuccess(true);
			result.setData("指派成功");
		} catch (BusinessException e) {
			logger.error("指派失败！！" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 改派采购单
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("orderReassign")
	@ResponseBody
	public Result orderReassign(Long id, String remark) {
		Result result = new Result();
		try {
			purchaseOrderService.reassignOrder(id, remark, getLoginUser());
			result.setSuccess(true);
			result.setData("改派成功");
		} catch (BusinessException e) {
			logger.error("改派失败！！" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 报价单推送（已报价）
	 *
	 * @param quotationOrderId 报价单id
	 * @return
	 * @author peanut
	 * @date 2016/6/27
	 */
	@RequestMapping(value = "push.html", method = RequestMethod.POST)
	@ResponseBody
	public Result pushQuotationOrder(Long quotationOrderId, Long orgId, Long userId) {
		Result result = new Result();
		try {
			purchaseOrderService.pushQuotationOrder(quotationOrderId, orgId, userId);
			result.setData("报价单推送成功!");
		} catch (BusinessException e) {
			logger.error("报价单推送失败！！" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 报价单点开单前的处理
	 *
	 * @param quotationOrderId 报价单id
	 * @return
	 * @author peanut
	 * @date 2016/7/1
	 */
	@RequestMapping(value = "preopen.html", method = RequestMethod.POST)
	@ResponseBody
	public Result preOpen(Long quotationOrderId) {
		Result result = new Result();
		try {
			String requrimentCode = purchaseOrderService.preOpen(quotationOrderId);
			result.setData(requrimentCode);
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 询价开单前的处理
	 *
	 * @param quotationOrderId 报价单id
	 * @return
	 * @author peanut
	 * @date 2016/7/1
	 */
	@RequestMapping(value = "preopenpid.html", method = RequestMethod.POST)
	@ResponseBody
	public Result preOpenByPurchaseId(Long purchaseOrderId,String requestCode) {
		Result result = new Result();
		try {
			purchaseOrderService.preOpenPurchaseOrder(purchaseOrderId,requestCode);
			result.setData(requestCode);
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 获取回退人员
	 * @return
	 */
	@RequestMapping(value = "getBackoffUsers.html", method = RequestMethod.POST)
	@ResponseBody
	public Result getNetSalers(Long userId) {
		Result result = new Result();
		List<User> userlist = purchaseOrderService.getBackoffUsers(userId);
		result.setData(userlist);
		result.setSuccess(true);
		return result;
	}
}
