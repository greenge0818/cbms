package com.prcsteel.platform.order.web.controller.invoice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.constants.Constant.ControlPinSettings;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailsDto;
import com.prcsteel.platform.order.model.enums.DistributionStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.service.invoice.InvoiceOutApplyService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * Created by rolyer on 15-8-3.
 */
@Controller
@RequestMapping("/invoice/apply/")
public class ApplyController extends BaseController {

	private static final Logger logger = Logger.getLogger(ApplyController.class);

	ShiroVelocity permissionLimit = new ShiroVelocity();

	// 删除开票申请
	private static final String PERMISSION_INVOICE_APPLY_DELETE = "invoice:out:apply:delete";
	// 撤销开票申请
	private static final String PERMISSION_INVOICE_APPLY_REVOKE = "invoice:out:apply:revoke";
	// 开票申请提交
	private static final String PERMISSION_INVOICE_APPLY_SUBMIT = "invoice:out:apply:submit";

	@Autowired
	private InvoiceOutApplyService invoiceOutApplyService;
	@Autowired
	private SysSettingService sysSettingService;
	@Value("${account.domain}")
	private String accountDomain;

	/*
	 * 销项票申请
	 */
	@RequestMapping("index.html")
	public String index(ModelMap out) {
		out.put("accountDomain", accountDomain);
		return "invoice/apply/list";
	}

	@RequestMapping("audit.html")
	public String audit() {
		return "invoice/apply/audit";
	}

	/**
	 * 输出已申请开票数据
	 *
	 * @return PageResult
	 * @author DQ
	 */
	@RequestMapping("invoicing.html")
	@ResponseBody
	public PageResult invoicing(@RequestParam("status") String status, @RequestParam("startTime") String startTime,
								@RequestParam("endTime") String endTime, @RequestParam("start") Integer start,
								@RequestParam("length") Integer length) {

		List<Long> orgIds = new ArrayList<>();
		orgIds.add(getLoginUser().getOrgId()); //查询登录用户的服务中的 已申请开票数据
		List<String> statusList = new ArrayList<String>();
		if(null == status || "".equals(status)) { //如果status为空，就把服务中心设为空，就是所有服务中心
			orgIds = null;
		} else if(status.equals(InvoiceOutApplyStatus.PENDING_SUBMIT.getValue())) {
			statusList = Arrays.asList(InvoiceOutApplyStatus.PENDING_SUBMIT.getValue(),
					InvoiceOutApplyStatus.PENDING_APPROVAL.getValue(),
					InvoiceOutApplyStatus.APPROVED.getValue(),
					InvoiceOutApplyStatus.DISAPPROVE.getValue());
		} else if(status.equals(InvoiceOutApplyStatus.PENDING_APPROVAL.getValue())) {
			statusList = Arrays.asList(InvoiceOutApplyStatus.PENDING_APPROVAL.getValue());
		}

		List<InvoiceOutApplyDto> list = invoiceOutApplyService.queryInvoiceOutApply(orgIds , startTime, endTime,
				start, length, statusList);
		int total = invoiceOutApplyService.totalInvoiceOutApply(orgIds, startTime, endTime, statusList);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 申请开票提交
	 *
	 * @return Result
	 * @author DQ
	 */
	@OpAction(key="uuid")
	@RequestMapping(value = "submit.html", method = RequestMethod.POST)
	@OpLog(OpType.SaveOutInvoice)
	@OpParam("paramJson")
	public @ResponseBody Result submit(@RequestParam("paramJson") String paramJson,@RequestParam("uuid") String  uuid) {
		Result result = new Result();
		User user = getLoginUser();
		String strId = String.valueOf(user.getId());
		Date date = new Date();
		if (!permissionLimit.hasPermission(PERMISSION_INVOICE_APPLY_SUBMIT)) {
			result.setSuccess(Boolean.FALSE);
			result.setData("您没有作该操作的权限");
			return result;
		}

		// 开票申请主表
		InvoiceOutApplyDto applyDto = new InvoiceOutApplyDto();
		applyDto.setAmount(new BigDecimal(0));
		applyDto.setActualAmount(new BigDecimal(0));
		// 提交人信息
		applyDto.setSubmitterId(user.getId());
		applyDto.setSubmitterName(user.getName());
		// 创建人信息
		applyDto.setCreated(date);
		applyDto.setCreatedBy(strId);
		// 最后修改人信息
		applyDto.setLastUpdated(date);
		applyDto.setLastUpdatedBy(strId);

		// 修改状态为待审核
		applyDto.setStatus(InvoiceOutApplyStatus.PENDING_APPROVAL.getValue());

		ObjectMapper mapper = new ObjectMapper();
		String temp = "";
		try {
			JsonNode paramNode = mapper.readTree(paramJson);
			if (paramNode.size() < 1) {
				result.setSuccess(Boolean.FALSE);
				result.setData("没有可以提供申请的数据,请确认开票资料已经通过审核、二次结算应收金额等于零且交易凭证通过审核!");
				return result;
			}
			JsonNode tempNode = paramNode.get(0);
			// 设置服务中心ID
			temp = tempNode.path("orgId").asText();
			Long orgId = Long.parseLong(temp);
			applyDto.setOrgId(orgId);
			// 获取服务中心名称
			String orgName = tempNode.path("orgName").asText();
			applyDto.setOrgName(orgName);
			for (JsonNode firstLayer : paramNode) {
				// 获取业务员ID
				Long ownerId = Long.parseLong(firstLayer.path("ownerId").asText());
				// 获取业务员名称
				String ownerName = firstLayer.path("ownerName").asText();

				// 开票申请详情表
				List<InvoiceOutApplyDetailDto> detailList = applyDto.getDetailList();

				JsonNode buyerList = firstLayer.path("buyerList");
				for (JsonNode secondLayer : buyerList) {
					temp = secondLayer.path("isOkId").asText();
					if(null == temp || temp.equals(DistributionStatus.No.getCode())) {
						continue;
					}
					// 创建详情表数据
					InvoiceOutApplyDetailDto detail = new InvoiceOutApplyDetailDto();
					detail.setOrgId(orgId);
					detail.setOrgName(orgName);
					detail.setOwnerId(ownerId);
					detail.setOrgName(orgName);
					// 买家ID
					temp = secondLayer.path("buyerId").asText();
					Long buyerId = Long.parseLong(temp);
					detail.setBuyerId(buyerId);
					// 买家名称
					String buyerName = secondLayer.path("buyerName").asText();
					detail.setBuyerName(buyerName);
					// 部门ID
					temp = secondLayer.path("departmentId").asText();
					Long departmentId = Long.parseLong(temp);
					detail.setDepartmentId(departmentId);
					// 部门名称
					String departmentName = secondLayer.path("departmentName").asText();
					detail.setDepartmentName(departmentName);
					// 业务员ID
					detail.setOwnerId(ownerId);
					// 业务员名称
					detail.setOwnerName(ownerName);
					// 服务中心ID
					detail.setOrgId(orgId);
					// 服务中心名称
					detail.setOrgName(orgName);
					// 申请开票金额
					BigDecimal amount = BigDecimal.ZERO;
					temp = secondLayer.path("amount").asText();
					if (StringUtils.isNotEmpty(temp)) {
						amount = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
					}
					detail.setApplyAmount(amount);
					// 未开票金额
					BigDecimal unAmount = BigDecimal.ZERO;
					temp = secondLayer.path("unAmount").asText();
					if(StringUtils.isNotEmpty(temp)){
						unAmount = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
					}
					detail.setUninvoiceAmount(unAmount);
					// 创建人信息
					detail.setCreated(date);
					detail.setCreatedBy(strId);
					// 最后修改人信息
					detail.setLastUpdated(date);
					detail.setLastUpdatedBy(strId);
					// 加入集合
					detailList.add(detail);

					// 开票申请详情关联表
					List<InvoiceOutItemDetailsDto> itemDetailList = detail.getItemDetailList();

					JsonNode orderList = secondLayer.path("itemOrder");
					for (JsonNode thirdLayer : orderList) {

						JsonNode itemList = thirdLayer.path("itemOrderDetail");
						for (JsonNode fourthLayer : itemList) {
							// 设置详情关联表数据
							setInvoiceOutItemDetailsDto(temp, date, strId, fourthLayer, itemDetailList);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("apply json to object error: ", e);
		}
		// 累加已申请开票总金额
		applyDto.setAmount(new BigDecimal(applyDto.getDetailList().stream().mapToDouble(
				a -> a.getApplyAmount().doubleValue()).sum()));
		// 保存申请开票所有数据
		try {
			invoiceOutApplyService.saveNewInvoicing(user, applyDto);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}

	private void setInvoiceOutItemDetailsDto(String temp, Date date, String id, JsonNode fourthLayer,
											 List<InvoiceOutItemDetailsDto> itemDetailList) {
		// 创建详情关联表数据
		InvoiceOutItemDetailsDto itemDetail = new InvoiceOutItemDetailsDto();
		// 订单详情ID
		temp = fourthLayer.path("orderDetailId").asText();
		Long orderDetailId = Long.parseLong(temp);
		itemDetail.setOrderDetailId(orderDetailId);
		// 进项票发票ID
		temp = fourthLayer.path("invoiceInId").asText();
		Long invoiceInId = Long.parseLong(temp);
		itemDetail.setInvInvoiceInId(invoiceInId);
		// 进项票发票详情ID
		temp = fourthLayer.path("invoiceInDetailId").asText();
		Long invoiceInDetailId = Long.parseLong(temp);
		itemDetail.setInvInvoiceInDetailId(invoiceInDetailId);
		// 进项票详情与订单详情关联表ID
		temp = fourthLayer.path("invoiceOrderitemId").asText();
		Long invoiceOrderitemId = Long.parseLong(temp);
		itemDetail.setInvoiceOrderitemId(invoiceOrderitemId);
		// 品名
		String nsortName = fourthLayer.path("nsortName").asText();
		itemDetail.setNsortName(nsortName);
		// 材质
		String material = fourthLayer.path("material").asText();
		itemDetail.setMaterial(material);
		// 规格
		String spec = fourthLayer.path("spec").asText();
		itemDetail.setSpec(spec);
		// 开票重量
		BigDecimal weight = BigDecimal.ZERO;
		temp = fourthLayer.path("weight").asText();
		if (!StringUtils.isEmpty(temp)) {
			weight = new BigDecimal(CbmsNumberUtil.formatWeight(Double.valueOf(temp)));
		}
		itemDetail.setWeight(weight);
		// 单价
		BigDecimal dealPrice = BigDecimal.ZERO;
		temp = fourthLayer.path("dealPrice").asText();
		if (!StringUtils.isEmpty(temp)) {
			dealPrice = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
		}
		itemDetail.setPrice(dealPrice);
		// 不含税金额
		BigDecimal noTaxAmount = BigDecimal.ZERO;
		temp = fourthLayer.path("noTaxAmount").asText();
		if (!StringUtils.isEmpty(temp)) {
			noTaxAmount = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
		}
		itemDetail.setNoTaxAmount(noTaxAmount);
		// 税额
		BigDecimal taxAmount = BigDecimal.ZERO;
		temp = fourthLayer.path("taxAmount").asText();
		if (!StringUtils.isEmpty(temp)) {
			taxAmount = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
		}
		itemDetail.setTaxAmount(taxAmount);
		// 含税金额
		BigDecimal amount = BigDecimal.ZERO;
		temp = fourthLayer.path("amount").asText();
		if (!StringUtils.isEmpty(temp)) {
			amount = new BigDecimal(CbmsNumberUtil.formatMoney(Double.valueOf(temp)));
		}
		itemDetail.setAmount(amount);
		// 创建人信息
		itemDetail.setCreated(date);

		itemDetail.setCreatedBy(id);
		// 最后修改人信息
		itemDetail.setLastUpdated(date);
		itemDetail.setLastUpdatedBy(id);

		// 加入集合
		itemDetailList.add(itemDetail);
	}

	/**
	 * 撤销已申请开票
	 *
	 * @return Result
	 * @author DQ
	 */
	@OpAction(key="outApplyId")
	@ResponseBody
	@RequestMapping(value = "revoke.html", method = RequestMethod.POST)
	public Result revoke(@RequestParam("outApplyId") Long outApplyId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_INVOICE_APPLY_REVOKE)) {
			result.setSuccess(Boolean.FALSE);
			result.setData("您没有作该操作的权限");
			return result;
		}
		if(null == outApplyId){
			result.setSuccess(Boolean.FALSE);
			result.setData("没有该申请开票记录");
			return result;
		}

		User user = getLoginUser();
		try {
			invoiceOutApplyService.revoke(outApplyId, InvoiceOutApplyStatus.PENDING_APPROVAL.getValue(), user);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}

	/**
	 * 审核已申请开票
	 *
	 * @return Result
	 * @author DQ
	 */
	@OpAction(key="outApplyId")
	@ResponseBody
	@RequestMapping(value = "approve.html", method = RequestMethod.POST)
	@OpLog(OpType.OutApplyId)
	@OpParam("outApplyId")
	@OpParam("status")
	public Result approve(@RequestParam("outApplyId") Long outApplyId, @RequestParam("status") String status) {
		Result result = new Result();
		if(null == status || "".equals(status)) {
			result.setSuccess(Boolean.FALSE);
			result.setData("无效的状态");
			return result;
		}

		if(null == outApplyId){
			result.setSuccess(Boolean.FALSE);
			result.setData("没有该申请开票记录");
			return result;
		}

		User user = getLoginUser();
		try {
			invoiceOutApplyService.approve(outApplyId, status, user);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}

	/**
	 * 删除已申请开票
	 *
	 * @return Result
	 * @author DQ
	 */
	@OpAction(key="outApplyId")
	@ResponseBody
	@RequestMapping(value = "delete.html", method = RequestMethod.POST)
	public Result delete(@RequestParam("outApplyId") Long outApplyId) {
		Result result = new Result();
		if (!permissionLimit.hasPermission(PERMISSION_INVOICE_APPLY_DELETE)) {
			result.setData("您没有作该操作的权限");
			result.setSuccess(Boolean.FALSE);
			return result;
		}

		if(null == outApplyId){
			result.setData("没有该申请开票记录");
			result.setSuccess(Boolean.FALSE);
			return result;
		}
		User user = getLoginUser();
		// 删除申请开票所有数据
		try {
			invoiceOutApplyService.delete(outApplyId, user);
			result.setSuccess(Boolean.TRUE);
			return result;
		} catch (BusinessException e) {
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
			return result;
		}
	}

	/**
	 * 组装可申请开票列表数据
	 *
	 * @return
	 * @author DQ
	 */
	@RequestMapping("detail.html")
	public void invoicingDetail(ModelMap out) {
		List<InvoiceOutApplyDetailVoDto> detailVoList = invoiceOutApplyService.queryDetail(getLoginUser().getOrgId(), null);
		out.put("isSubmit", true);

		List<SysSetting> controlInvoice = sysSettingService.getControlPinSettings();
		for (SysSetting sysSetting : controlInvoice) {
			if(ControlPinSettings.with_deal_control.toString().equals(sysSetting.getSettingName())){
				out.put("CredentialCheckSwitch", sysSetting.getSettingValue());

			}else if(ControlPinSettings.with_twice_balance_control.toString().equals(sysSetting.getSettingName())){
				out.put("BalanceSecondSettlementSwitch", sysSetting.getSettingValue());
			}
		}

		// 获取剩余可开票重量
		out.put("totalUnWeight", CbmsNumberUtil.formatWeight(
				detailVoList.stream().mapToDouble(a -> a.getTotalUnWeight().doubleValue()).sum()));
		out.put("list", detailVoList);

		// 防止重复提交
		out.put("uuid", UUID.randomUUID().toString());
	}

	/**
	 * 组装已申请开票列表数据
	 *
	 * @return
	 * @author DQ
	 */
	@RequestMapping("undetail.html")
	public String invoicingUnDetail(ModelMap out, @RequestParam("outApplyId") Long outApplyId) {
		List<InvoiceOutApplyDetailVoDto> detailVoList = invoiceOutApplyService.queryDetail(getLoginUser().getOrgId(), outApplyId);
		out.put("isSubmit", Boolean.FALSE);
		out.put("list", detailVoList);
		return "invoice/apply/detail";
	}

	/**
	 * 组装待审核开票列表数据
	 *
	 * @return
	 * @author DQ
	 */
	@RequestMapping("auditdetail.html")
	public void auditDetail(ModelMap out, @RequestParam("outApplyId") Long outApplyId) {
		List<InvoiceOutApplyDetailVoDto> detailVoList = invoiceOutApplyService.queryDetail(getLoginUser().getOrgId(), outApplyId);
		out.put("outApplyId", outApplyId);
		out.put("list", detailVoList);
	}

	/**
	 * 获取销项票申请开票二次结算额度控制
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "secondbalance.html", method = RequestMethod.POST)
	public Result secondbalance() {
		Result result = new Result();
		SysSetting template = sysSettingService.queryBySettingType(SysSettingType.InvoiceOutApplySecond.getCode());
		if (template == null) {
			result.setData("销项票申请开票二次结算额度控制没有在系统设置！");
			result.setSuccess(Boolean.FALSE);
			return result;
		}
		String settingValue = template.getSettingValue();
		if(StringUtils.isBlank(settingValue)) settingValue = "0";
		result.setData(settingValue);
		result.setSuccess(Boolean.TRUE);
		return result;
	}
}
