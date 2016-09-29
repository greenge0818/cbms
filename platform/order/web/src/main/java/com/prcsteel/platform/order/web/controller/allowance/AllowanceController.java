package com.prcsteel.platform.order.web.controller.allowance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


import com.prcsteel.platform.order.model.dto.CustAccountDto;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.AllowanceDto;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.enums.AllowanceManner;
import com.prcsteel.platform.order.model.enums.AllowanceStatus;
import com.prcsteel.platform.order.model.enums.AllowanceType;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.AllowanceListQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderSave;
import com.prcsteel.platform.order.model.query.AllowanceUpdate;
import com.prcsteel.platform.order.service.allowance.AllowanceOrderDetailItemService;
import com.prcsteel.platform.order.service.allowance.AllowanceService;

import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * @author dq
 * @Title: AllowanceController.java
 * @Package com.prcsteel.cbms.web.controller.allowance
 * @Description: 折让模块入口
 * @date 2015-11-16 11:11:11
 */
@Controller
@RequestMapping("/allowance/")
public class AllowanceController extends BaseController {

	private static final Logger logger = Logger.getLogger(AllowanceController.class);

	ShiroVelocity permissionLimit = new ShiroVelocity();
	
	@Resource
	AllowanceService allowanceService;
	@Resource
	ConsignOrderItemsService consignOrderItemsService;
	
	@Resource
    private AllowanceOrderDetailItemService allowanceOrderDetailItemService;

	final String PERMISSION_VIEWALL = "allowance:list:viewall"; // 订单详情页
	final String PERMISSION_Add = "allowance:list:add"; // 添加折让单
	final String PERMISSION_GENERATE = "allowance:list:generate"; // 生成买家折让
	final String PERMISSION_APPROVAL = "allowance:list:approval"; // 审核
	final String PERMISSION_CANCEL = "allowance:list:cancelaudit"; // 取消通过
	final String PERMISSION_DELETE = "allowance:list:delete"; // 删除
	final String PERMISSION_EDIT = "allowance:list:edit"; // 编辑
	final String ALLOWANCE_ADD_TYPE_ALL = "allowance:add:type:all"; // 显示折让金额和重量的折让方式
	final String ALLOWANCE_ADD_TYPE_WEIGHT = "allowance:add:type:weight"; // 显示折让重量的折让方式
	final String ALLOWANCE_ADD_TYPE_AMOUNT = "allowance:add:type:amount"; // 显示折让金额的折让方式
	final String ALLOWANCE_EDITBUYER_AMOUNT = "allowance:list:editbuyer:amount"; // 显示折让金额的折让方式
	
	@RequestMapping("list/{type}.html")
	public String list(ModelMap out, @PathVariable String type) {
		out.put("type", type);
		out.put("permission_viewall",permissionLimit.hasPermission(PERMISSION_VIEWALL));
		out.put("permission_generate",permissionLimit.hasPermission(PERMISSION_GENERATE));
		out.put("permission_add",permissionLimit.hasPermission(PERMISSION_Add));
		out.put("permission_approval",permissionLimit.hasPermission(PERMISSION_APPROVAL));
		out.put("permission_cancel",permissionLimit.hasPermission(PERMISSION_CANCEL));
		out.put("permission_delete",permissionLimit.hasPermission(PERMISSION_DELETE));
		out.put("permission_edit", permissionLimit.hasPermission(PERMISSION_EDIT));
		return "allowance/list";
	}

	/**
	 * 折让单模块主页面
	 *
	 * @return
	 * @author cc
	 */
	@ResponseBody
	@RequestMapping("listdata/{type}.html")
	public PageResult listdata(@PathVariable String type,
						@RequestParam(value = "allowanceCode", required = false) String allowanceCode,
						@RequestParam(value = "accountId", required = false) Long accountId,
						@RequestParam(value = "status", required = false) String status,
						@RequestParam(value = "sdate", required = true) String sdate,
						@RequestParam(value = "edate", required = true) String edate,
						@RequestParam(value = "start", required = true) Integer start,
						@RequestParam(value = "length", required = true) Integer length) {
		PageResult result = new PageResult();
		AllowanceListQuery query = buildAllowanceListQuery(type, allowanceCode, accountId, status, sdate, edate);
		int total = allowanceService.countAllowanceList(query);
		result.setRecordsFiltered(total);
		if (total > 0) {
			query.setStart(start);
			query.setLength(length);
			List<AllowanceDto> list = allowanceService.queryAllowanceList(query);
			result.setData(list);
			result.setRecordsTotal(list.size());
		} else {
			result.setData(new ArrayList<>());
			result.setRecordsTotal(0);
		}
		return result;
	}

	/*
	 * 折让单数据
	 *
	 * @return
	 * @author cc
	 */
	private AllowanceListQuery buildAllowanceListQuery(String type, String allowanceCode, Long accountId, String status, String sdate, String edate) {
		AllowanceListQuery query = new AllowanceListQuery();
		if (StringUtils.isNotEmpty(type)) {
			query.setAllowanceType(type);
		}
		if (StringUtils.isNotEmpty(allowanceCode)) {
			query.setAllowanceCode(allowanceCode);
		}
		if (accountId != null && accountId > 0) {
			query.setAccountId(accountId);
		}
		if (StringUtils.isNotEmpty(status)) {
			List<String> statusList = new ArrayList<>();
			statusList.add(status);
			query.setStatusList(statusList);
		}
		else if(!permissionLimit.hasPermission(PERMISSION_VIEWALL)){ //是否有查看全部折让单权限，“否”则只显示部分状态
			query.setStatusList(getLimitStatusList());
		}
		if (StringUtils.isNotEmpty(sdate)) {
			query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
		}
		if (StringUtils.isNotEmpty(edate)) {
			query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
		}
		User user = getLoginUser();
		Role role = getUserRole();
		List<Long> orgs = userService.getOrgIdsByRoleIds(role, user);
		if (orgs != null) {
			//只显示有权限的服务中心折让单
			query.setOrgIdList(orgs); 
		}
		return query;
	}

	/*
	 * 服总只显示待审核和已审核
	 *
	 * @return
	 * @author cc
	 */
	private List<String> getLimitStatusList(){
		List<String> statusList = new ArrayList<>();
		statusList.add(AllowanceStatus.ToAudit.getKey());
		statusList.add(AllowanceStatus.Approved.getKey());
		return statusList;
	}

	/**
	 * 删除折让单
	 *
	 * @return
	 * @author cc
	 */
	@OpAction(key="id")
	@ResponseBody
	@RequestMapping("deleteAllowance.html")
	public Result deleteAllowance(@RequestParam("id") Long id) {
		Result result = new Result();
		if (permissionLimit.hasPermission(PERMISSION_DELETE)) {
			try
			{
				if(id!=null&&id>0){
					User user=getLoginUser();
					allowanceService.deleteAllowance(id,user);
					result.setSuccess(true);
					result.setData("操作成功");
				}
				else{
					result.setSuccess(false);
					result.setData("删除失败,参数不正确");
				}
			}
			catch (BusinessException ex){
				result.setSuccess(false);
				result.setData(ex.getMsg());
			}
		} else {
			result.setSuccess(false);
			result.setData("您没有作该操作的权限！");
		}
		return result;
	}

	/**
	 * 新增买家折让单第一步
	 *
	 * @return
	 * @author dq
	 */
	@RequestMapping("buyerorder.html")
	public String buyerOrder() {
		return "allowance/buyerorder";
	}
	
	/**
	 * 新增买家折让单第一步
	 *
	 * @return
	 * @author dq
	 */
	@RequestMapping("sellerorder.html")
	public String sellerOrder(AllowanceOrderQuery allowanceOrderQuery, ModelMap out) {
		//重新关联交易单进来
		if(allowanceOrderQuery != null && allowanceOrderQuery.getId() != null && allowanceOrderQuery.getId() > 0){
			Allowance allowance = allowanceService.selectByPrimaryKey(allowanceOrderQuery.getId());
			out.put("allowance", allowance);
		}
		return "allowance/sellerorder";
	}
	
	/**
	 * 新增卖家折让单第二步
	 *
	 * @return
	 * @author dq
	 */
	@RequestMapping("addseller.html")
	public void addseller(AllowanceOrderQuery allowanceOrderQuery, ModelMap out) {
		if(allowanceOrderQuery.getAllowanceType() == null || !AllowanceType.Seller.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			return ;
		}
		List<AllowanceOrderItemsDto> orderList = allowanceService.queryOrders(allowanceOrderQuery);
		if(null != orderList && orderList.size() > 0) {
			out.put("sellerName", orderList.stream().findFirst().get().getSellerName());
			out.put("sellerInfo",orderList.stream().findFirst().get());
		}
		out.put("orderList", orderList);
		out.put("allowance_add_type_all", permissionLimit.hasPermission(ALLOWANCE_ADD_TYPE_ALL));
		out.put("allowance_add_type_weight", permissionLimit.hasPermission(ALLOWANCE_ADD_TYPE_WEIGHT));
		out.put("allowance_add_type_amount", permissionLimit.hasPermission(ALLOWANCE_ADD_TYPE_AMOUNT));
	}
	
	/**
	 * 新增买家折让单第二步
	 *
	 * @return
	 * @author dq
	 */
	@RequestMapping("addbuyer.html")
	public void addbuyer(AllowanceOrderQuery allowanceOrderQuery, ModelMap out) {
		if(allowanceOrderQuery.getAllowanceType() == null || !AllowanceType.Buyer.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			return ;
		}
		List<AllowanceOrderItemsDto> orderList = allowanceService.queryOrders(allowanceOrderQuery);
		out.put("orderList", orderList);
	}
	
	/**
	 * 新增买家折让单保存
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping(value = "saveallowance.html", method = RequestMethod.POST)
	public Result saveAllowance(MultipartHttpServletRequest request,AllowanceOrderSave allowanceOrderSave) {
		Result result = new Result();
		User user = getLoginUser();
		MultipartFile image = request.getFile("allowanceImg");
		allowanceOrderSave.setUser(user);
		allowanceOrderSave.setDate(new Date());
		List<AllowanceOrderItemsDto> orderList = jsonToAllowanceOrderItemsDtoList(allowanceOrderSave.getParamJson());
		// 保存折让数据
		try {
			allowanceService.save(image,allowanceOrderSave,orderList);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}
	
	/**
	 * 新增买家折让单保存
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping(value = "savebuyallowance.html", method = RequestMethod.POST)
	public Result saveBuyAllowance(AllowanceOrderSave allowanceOrderSave) {
		User user = getLoginUser();
		allowanceOrderSave.setUser(user);
		allowanceOrderSave.setDate(new Date());
		Result result = new Result();
		List<AllowanceOrderItemsDto> orderList = jsonToAllowanceOrderItemsDtoList(allowanceOrderSave.getParamJson());
		// 保存折让数据
		try {
			allowanceService.save(null,allowanceOrderSave,orderList);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}
	
	/**
	 * 增加根据卖家折让单号，自动生成买家折让单接口
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping(value = "savebuyerallowances.html", method = RequestMethod.POST)
	public Result saveBuyerAllowances(AllowanceOrderSave allowanceOrderSave) {
		User user = getLoginUser();
		allowanceOrderSave.setUser(user);
		allowanceOrderSave.setDate(new Date());
		
		Result result = new Result();
		// 保存折让数据
		try {
			allowanceService.saveBuyerAllowances(allowanceOrderSave);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}
	
	/**
	 * 查询卖家相关订单数据
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping("loadsellerorderdata.html")
	public Result loadSellerOrderData(AllowanceOrderQuery allowanceOrderQuery) {
		Result result = new Result();
//		if(allowanceOrderQuery.getSellerId() == null) {
//			return result;
//		}
		User user = getLoginUser();
		Role role = getUserRole();
		List<Long> orgs = userService.getOrgIdsByRoleIds(role, user);
		allowanceOrderQuery.setOrgs(orgs);
		return loadOrderData(allowanceOrderQuery);
	}
	
	/**
	 * 查询买家相关订单数据
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping("loadbuyerorderdata.html")
	public Result loadBuyerOrderData(AllowanceOrderQuery allowanceOrderQuery) {
		Result result = new Result();
//		if(allowanceOrderQuery.getBuyerId() == null) {
//			return result;
//		}
		User user = getLoginUser();
		Role role = getUserRole();
		List<Long> orgs = userService.getOrgIdsByRoleIds(role, user);
		allowanceOrderQuery.setOrgs(orgs);
		return loadOrderData(allowanceOrderQuery);
	}
	
	/*
	 * 查询订单列表数据
	 *
	 * @return
	 * @author dq
	 */
	private Result loadOrderData(AllowanceOrderQuery allowanceOrderQuery) {
		Result result = new Result();
		List<AllowanceOrderItemsDto> orderList = allowanceService.queryOrders(allowanceOrderQuery);
		result.setData(orderList);
		return result;
	}

	/**
	 * 卖家折让详情列表
	 *
	 * @return
	 * @author lx
	 */
	@RequestMapping("auditrebate/{allowanceId}.html")
	public String auditallowanceUpdate (@PathVariable Long allowanceId, ModelMap out) {
		Allowance allowance = new Allowance();
		allowance = allowanceService.selectByPrimaryKey(allowanceId);
		out.put("departmentNums", allowanceService.queryDepartmentByAccoutId(allowance.getAccountId()).size());//查客户的部门数
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		//如果折让方式是重量或者所有的情况下，将关联的买家折让单也查出来
		if(AllowanceType.Seller.getKey().equals(allowance.getAllowanceType())){
			if (!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())) {
				detailItemQuery.setAllowanceId(allowanceId);
				List<AllowanceOrderItemsDto> buyerList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
				out.put("buyerList", buyerList);
			}
		}
		//查询买家是否有关联的卖家
		if (AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())) {
			if (allowance.getAllowanceId() != null) {
				if (!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())) {
					detailItemQuery = new AllowanceDetailItemQuery();
					Allowance sellerAllowance = allowanceService.selectByPrimaryKey(allowance.getAllowanceId());
					if (sellerAllowance != null) {
						detailItemQuery.setId(sellerAllowance.getId());
						List<AllowanceOrderItemsDto> sellerList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
						if (sellerList != null && sellerList.size() > 0) {
							out.put("sellerList", sellerList);
							out.put("sellerAllowance", sellerAllowance);
						}
					}
				}
			}
		}
		detailItemQuery = new AllowanceDetailItemQuery();
		detailItemQuery.setId(allowanceId);
		List<AllowanceOrderItemsDto> list = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
		out.put("list", list);
		out.put("allowance", allowance);
		out.put("permission_approval",permissionLimit.hasPermission(PERMISSION_APPROVAL));
		out.put("permission_cancel",permissionLimit.hasPermission(PERMISSION_CANCEL));
		return "allowance/auditrebate";
	}

	/**
	 * 通过审核折让单
	 *
	 * @return
	 * @author lx
	 */
	@ResponseBody
	@RequestMapping("update/rebate.html")
	public Result passallowanceUpdate (AllowanceUpdate allowanceUpdate) {
		Result result = new Result();
		User user = getLoginUser();
		allowanceUpdate.setStatus(AllowanceStatus.Approved.getKey());
		try {
			allowanceUpdate.setUser(user);
			allowanceService.checkAllowance(allowanceUpdate);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 取消审核折让单
	 *
	 * @return
	 * @author lx
	 */
	@ResponseBody
	@RequestMapping("cancel/rebate.html")
	public Result cancelallowanceUpdate (AllowanceUpdate allowanceUpdate) {
		Result result = new Result();
		User user = getLoginUser();
		allowanceUpdate.setStatus(AllowanceStatus.ToSubmit.getKey());
		try {
			allowanceUpdate.setUser(user);
			allowanceService.checkAllowance(allowanceUpdate);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 不通过审核折让单
	 *
	 * @return
	 * @author lx
	 */
	@ResponseBody
	@RequestMapping("notThrough/audit.html")
	public Result noPassallowanceUpdate (AllowanceUpdate allowanceUpdate) {
		Result result = new Result();
		User user = getLoginUser();
		allowanceUpdate.setStatus(AllowanceStatus.NotThrough.getKey());
		try {
			allowanceUpdate.setUser(user);
			allowanceService.checkAllowance(allowanceUpdate);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 修改买家折让单（审核不通过后）
	 *
	 * @return
	 * @author lcw
	 */
	@RequestMapping("{allowanceId}/editbuyer.html")
	public String editBuyer(ModelMap out, @PathVariable("allowanceId") Long allowanceId,
							  AllowanceOrderQuery allowanceOrderQuery){
		List<AllowanceOrderItemsDto> list;
		if (allowanceOrderQuery == null
				|| allowanceOrderQuery.getAllowanceType() == null
				|| !AllowanceType.Buyer.getKey().equals(allowanceOrderQuery.getAllowanceType())) {
			AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
			detailItemQuery.setId(allowanceId);
			list = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
		} else {
			// 重新选择的列表
			list = allowanceService.queryOrders(allowanceOrderQuery);
		}
		out.put("list", list);
		Allowance allowance = allowanceService.selectByPrimaryKey(allowanceId);
		out.put("allowance", allowance);
		return "allowance/editbuyer";
	}

	/**
	 * 保存买家折让单的修改（审核不通过后）
	 *
	 * @return
	 * @author lcw
	 */
	@ResponseBody
	@RequestMapping("savebuyeredit.html")
	public Result saveBuyerEdit(AllowanceOrderSave allowanceOrderSave) {
		Result result = new Result();
		User user = getLoginUser();
		allowanceOrderSave.setUser(user);
		allowanceOrderSave.setDate(new Date());
		// 保存折让数据
		try {
			List<AllowanceOrderItemsDto> orderList = jsonToAllowanceOrderItemsDtoList(allowanceOrderSave.getParamJson());
			allowanceService.updateAllowance(allowanceOrderSave,orderList);
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 组装json数据
	 *
	 * @return
	 * @author dq
	 */
	private List<AllowanceOrderItemsDto> jsonToAllowanceOrderItemsDtoList(String json){
		ObjectMapper mapper = new ObjectMapper();
		List<AllowanceOrderItemsDto> orderList = new ArrayList<AllowanceOrderItemsDto>();
		try {
			JsonNode paramNode = mapper.readTree(json);
			for (JsonNode param : paramNode) {
				AllowanceOrderItemsDto dto =new AllowanceOrderItemsDto();
				
				if (param.path("buyerId") != null) {
					dto.setBuyerId(param.path("buyerId").asLong());
				}
				if (param.path("sellerId") != null) {
					dto.setSellerId(param.path("sellerId").asLong());
				}
				dto.setOrderId(param.path("orderId").asLong());
				dto.setOrderDetailId(param.path("orderDetailId").asLong());

				if (param.path("contractCode") != null) {
					dto.setContractCode(param.path("contractCode").asText());
				}
				dto.setTotalWeight(new BigDecimal(param.path("totalWeight").asText()));
				dto.setTotalAmount(new BigDecimal(param.path("totalAmount").asText()));
				dto.setTotalActualWeight(new BigDecimal(param.path("totalActualWeight").asText()));
				dto.setTotalActualAmount(new BigDecimal(param.path("totalActualAmount").asText()));

				dto.setOrderTime(Tools.strToDate(param.path("orderTime").asText()));
				dto.setOrderCode(param.path("orderCode").asText());
				if (param.path("buyerName") != null) {
					dto.setBuyerName(param.path("buyerName").asText());
				}
				if (param.path("sellerName") != null) {
					dto.setSellerName(param.path("sellerName").asText());
				}
				dto.setNsortName(param.path("nsortName").asText());
				dto.setMaterial(param.path("material").asText());
				dto.setSpec(param.path("spec").asText());
				
				dto.setTotalQuantity(Integer.parseInt(param.path("totalQuantity").asText()));

				dto.setActualWeight(new BigDecimal(param.path("actualWeight").asText()));
				dto.setAllowanceWeight(new BigDecimal(param.path("allowanceWeight").asText()));
				dto.setUnAllowanceWeight(new BigDecimal(param.path("unAllowanceWeight").asText()));
				dto.setActualAmount(new BigDecimal(param.path("actualAmount").asText()));
				dto.setAllowanceAmount(new BigDecimal(param.path("allowanceAmount").asText()));
				dto.setUnAllowanceAmount(new BigDecimal(param.path("unAllowanceAmount").asText()));

				//买卖家部门
				if(param.path("buyerDeptId") != null){
					dto.setBuyerDepartmentId(param.path("buyerDeptId").asLong());
				}
				if(param.path("buyerDeptName") != null){
					dto.setBuyerDepartmentName(param.path("buyerDeptName").asText());
				}
				if(param.path("sellerDeptId") != null){
					dto.setSellerDepartmentId(param.path("sellerDeptId").asLong());
				}
				if(param.path("sellerDeptName") != null){
					dto.setSellerDepartmentName(param.path("sellerDeptName").asText());
				}

				orderList.add(dto);
			}

		} catch (IOException e) {
			logger.error("apply json to object error: ", e);
		}
		return orderList;
	}

	/**
	 * 卖家折让单编辑
	 *
	 * @return
	 * @author dxy
	 */
    @RequestMapping("{allowanceId}/editseller.html")
    public String editSeller(ModelMap out, @PathVariable("allowanceId") Long allowanceId,AllowanceOrderQuery allowanceOrderQuery) {
        if (allowanceId != null && allowanceId > 0) {
            Allowance allowance = allowanceService.selectByPrimaryKey(allowanceId);
            //判断是否为卖家折让单 状态为待提交或审核不通过
            if (allowance != null && StringUtils.equals(allowance.getAllowanceType(), AllowanceType.Seller.getKey())
                    && (StringUtils.equals(AllowanceStatus.ToSubmit.getKey(), allowance.getStatus())
                    || StringUtils.equals(AllowanceStatus.NotThrough.getKey(), allowance.getStatus()))) {

				//判断是否显示部门
				List<CustAccountDto> depts = allowanceService.queryDepartmentByAccoutId(allowance.getAccountId());
				out.put("isShowDept",depts != null && depts.size() > 1);

                out.put("allowance", allowance);

				//判断是否显示折扣重量或折扣金额
				out.put("displayWeight", StringUtils.equals(AllowanceManner.All.getKey(), allowance.getAllowanceManner())
						|| StringUtils.equals(AllowanceManner.Weight.getKey(), allowance.getAllowanceManner()));
				out.put("displayAmount", StringUtils.equals(AllowanceManner.All.getKey(), allowance.getAllowanceManner())
						|| StringUtils.equals(AllowanceManner.Amount.getKey(), allowance.getAllowanceManner()));

				if (allowanceOrderQuery == null || !StringUtils.equals(AllowanceType.Seller.getKey(),allowanceOrderQuery.getAllowanceType())){
					//查询卖家折让单详情
					List<AllowanceOrderItemsDto> list = allowanceOrderDetailItemService.queryDetails(new AllowanceDetailItemQuery(allowanceId));
					out.put("list", list);
				}else{
					//重新选择关联交易单
					List<AllowanceOrderItemsDto> orderList = allowanceService.queryOrders(allowanceOrderQuery);
					out.put("list", orderList);
				}


            }
        }
        return "allowance/editseller";
    }

    /**
	 * 手动关闭审核不通过的折让单
	 *
	 * @return
	 * @author dxy
	 */
	@OpAction(key="id")
    @ResponseBody
	@RequestMapping("{id}/closeorder.html")
	public Result closeOrder(@PathVariable Long id){
		Result result = new Result();
		if(id != null && id > 0){
			Allowance allowance = allowanceService.selectByPrimaryKey(id);
			if (allowance == null || !StringUtils.equals(AllowanceStatus.NotThrough.getKey(), allowance.getStatus())){
				result.setSuccess(false);
				result.setData("折让单未找到或折让单已被处理");
				return result;
			}

			//买家折让单 不是金额方式（重量、重量和金额方式）不允许关闭，只能到卖家折让单关闭
			if (StringUtils.equals(AllowanceType.Buyer.getKey(),allowance.getAllowanceType())
				&& !StringUtils.equals(AllowanceManner.Amount.getKey(),allowance.getAllowanceManner())){
				result.setSuccess(false);
				result.setData("该买家折让单与卖家折让单有关联，不能单独操作,请到卖家折让单里关闭！");
				return result;
			}

			try{
				allowanceService.closeOrder(this.getLoginUser(),allowance);
				result.setData("折让单关闭成功");
				result.setSuccess(true);
			}catch (BusinessException e) {
				result.setSuccess(false);
				result.setData(e.getMsg());
				logger.error(e.getMsg());
				return result;
			}
		}else{
			result.setSuccess(false);
			result.setData("参数错误");
		}
		return result;
	}

    /**
	 * 保存卖家折让单，如果是重量方式则同步修改卖家折让单
	 *
	 * @return
	 * @author dxy
	 */
    @ResponseBody
	@RequestMapping(value="saveselleredit.html",method = RequestMethod.POST)
	public Result saveSellerEdit(AllowanceOrderSave allowanceOrderSave,MultipartHttpServletRequest request){
		Result result = new Result();
		User user = getLoginUser();
		allowanceOrderSave.setUser(user);
		allowanceOrderSave.setDate(new Date());
		List<AllowanceOrderItemsDto> orderList = jsonToAllowanceOrderItemsDtoList(allowanceOrderSave.getParamJson());
		MultipartFile image = request.getFile("allowanceImg");
		try {
			allowanceService.updateSellerAllowance(image,allowanceOrderSave,orderList);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}

    /**
	 * 折让详情
	 *
	 * @return
	 * @author lx
	 */
	@RequestMapping("detail/{allowanceId}.html")
	public String allowanceDetail (@PathVariable Long allowanceId, ModelMap out) {
		Allowance allowance = new Allowance();
		allowance = allowanceService.selectByPrimaryKey(allowanceId);
		out.put("departmentNums", allowanceService.queryDepartmentByAccoutId(allowance.getAccountId()).size());//查客户的部门数
		AllowanceDetailItemQuery detailItemQuery = new AllowanceDetailItemQuery();
		//如果折让方式是重量或者所有的情况下，将关联的买家折让单也查出来
		if(AllowanceType.Seller.getKey().equals(allowance.getAllowanceType())){
			if (AllowanceManner.Weight.getKey().equals(allowance.getAllowanceManner())
					|| AllowanceManner.All.getKey().equals(allowance.getAllowanceManner())) {
				detailItemQuery.setAllowanceId(allowanceId);
				List<AllowanceOrderItemsDto> buyerList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
				out.put("buyerList", buyerList);
			}
		}
		//查询买家是否有关联的卖家
		if (AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())) {
			if (allowance.getAllowanceId() != null) {
				if (!AllowanceManner.Amount.getKey().equals(allowance.getAllowanceManner())) {
					detailItemQuery = new AllowanceDetailItemQuery();
					Allowance sellerAllowance = allowanceService.selectByPrimaryKey(allowance.getAllowanceId());
					if (sellerAllowance != null) {
						detailItemQuery.setId(sellerAllowance.getId());
						List<AllowanceOrderItemsDto> sellerList = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
						if (sellerList != null && sellerList.size() > 0) {
							out.put("sellerList", sellerList);
							out.put("sellerAllowance", sellerAllowance);
						}
					}
				}
			}
		}
		detailItemQuery = new AllowanceDetailItemQuery();
		detailItemQuery.setId(allowanceId);
		List<AllowanceOrderItemsDto> list = allowanceOrderDetailItemService.queryDetails(detailItemQuery);
		out.put("list", list);
		out.put("allowance", allowance);
		out.put("editAmount", permissionLimit.hasPermission(ALLOWANCE_EDITBUYER_AMOUNT));
		out.put("permission_approval",permissionLimit.hasPermission(PERMISSION_APPROVAL));
		out.put("permission_cancel",permissionLimit.hasPermission(PERMISSION_CANCEL));
		return "allowance/allowancedetail";
	}
	
	/**
	 * 查找卖家对应做过交易的买家数据
	 *
	 * @return
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping(value = "searchbuyer.html", method = RequestMethod.POST)
	public PageResult searchBuyerBySellerId(
			@RequestParam("sellerId") Long sellerId) {
		PageResult result = new PageResult();
		List<Account> list = consignOrderItemsService.listBuyerBySellerId(sellerId);
		result.setData(list);
		return result;
	}
	
	/**
	 * 修改折让金额
	 * @return
	 * @author tuxianming
	 */
	@ResponseBody
	@RequestMapping(value = "updateallowanceamount.html", method = RequestMethod.POST)
	public Result updateAllowanceAmount(@RequestBody List<AllowanceDetailItemQuery> query){
		
		Result result = null;
		try {
			
			allowanceService.updateAllowance(query, getLoginUser());
			result = new Result("修改成功！");
			
		} catch (BusinessException e) {
			result = new Result(e.getMsg(), false);
		}
		
		return result;
	}
	
}
