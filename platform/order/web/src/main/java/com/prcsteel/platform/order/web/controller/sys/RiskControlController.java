package com.prcsteel.platform.order.web.controller.sys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.acl.model.enums.RiskControlType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.acl.model.model.Reward;
import com.prcsteel.platform.order.model.model.RewardList;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.acl.service.RewardService;
import com.prcsteel.platform.order.service.sys.TargetWeightService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by prcsteel on 2016/3/30.
 */
@Controller
@RequestMapping("/sys/")
public class RiskControlController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(RiskControlController.class);

	@Resource
	private SysSettingService sysSettingService;
	@Resource
	AccountService accountService;
	@Resource
	private RewardService rewardService;
	@Resource
	ConsignOrderItemsService consignOrderItemsService;
	@Resource
	TargetWeightService targetWeightService;
	@Resource
	OrganizationService organizationService;

	@RequestMapping("riskcontrol.html")
	public void riskcontrol(ModelMap out) {
		List<SysSetting> riskcontrol = sysSettingService.queryExceedtime();
		List<SysSetting> customerLabel = sysSettingService.queryCustomerLabel();
		out.put("clientType", sysSettingService.queryClientType());
		out.put("exceedtime", riskcontrol.size() == 0 ? null : riskcontrol.get(0));
		out.put("customerLabel", customerLabel);
		out.put("billSetting", sysSettingService.queryBillSetting());
		List<Organization> organization = organizationService.queryAllBusinessOrg();

		List<Reward> reward = rewardService.getDealParameter();
		List<Object> allOrgRewards = new ArrayList<Object>();
		RewardList rewardOrgList = null;

		String[] splitStr = customerLabel.size() == 0 ? null : customerLabel.get(0).getSettingValue().split("、");
		if (splitStr != null) {
			for (Organization orgList : organization) {
				rewardOrgList = new RewardList();
				List<Reward> rewardList = reward.stream().filter(a -> orgList.getId().intValue() == a.getOrgId())
						.collect(Collectors.toList());
				rewardOrgList.setOrgId(orgList.getId());
				rewardOrgList.setOrgName(orgList.getName());
				if (rewardList.size() == 0) {
					Reward re = null;
					for (int i = 0; i < splitStr.length; i++) {
						BigDecimal init = new BigDecimal(0);
						re = new Reward(orgList.getId().intValue(), orgList.getName(), splitStr[i], "0", init, init,
								init, init, init, init, i + 1);
						rewardList.add(re);
					}
				}
				rewardOrgList.setAllOrgReward(rewardList);
				allOrgRewards.add(rewardOrgList);
			}
		}
		out.put("dealParameter", allOrgRewards);
		List<SysSetting> controlPinSettings = sysSettingService.getControlPinSettings();
		out.put("ControlPinSettings", controlPinSettings);

		// ↓将数据库类型换成对应的枚举类型
		out.put("new_order",
				sysSettingService.queryHint().stream()
						.filter(a -> a.getSettingName().toString().equals(RiskControlType.NEW_ORDER.getCode()))
						.peek(a -> a.setSettingName(RiskControlType.NEW_ORDER.getName())).collect(Collectors.toList()));
		out.put("audit_order", sysSettingService.queryHint().stream()
				.filter(a -> a.getSettingName().toString().equals(RiskControlType.AUDIT_ORDER.getCode()))
				.peek(a -> a.setSettingName(RiskControlType.AUDIT_ORDER.getName())).collect(Collectors.toList()));
		out.put("apply_payment", sysSettingService.queryHint().stream()
				.filter(a -> a.getSettingName().toString().equals(RiskControlType.APPLY_PAYMENT.getCode()))
				.peek(a -> a.setSettingName(RiskControlType.APPLY_PAYMENT.getName())).collect(Collectors.toList()));
		out.put("pending_payment", sysSettingService.queryHint().stream()
				.filter(a -> a.getSettingName().toString().equals(RiskControlType.PENDING_PAYMENT.getCode()))
				.peek(a -> a.setSettingName(RiskControlType.PENDING_PAYMENT.getName())).collect(Collectors.toList()));
		out.put("print_float_layer", sysSettingService.queryHint().stream()
				.filter(a -> a.getSettingName().toString().equals(RiskControlType.PRINT_FLOAT_LAYER.getCode()))
				.peek(a -> a.setSettingName(RiskControlType.PRINT_FLOAT_LAYER.getName())).collect(Collectors.toList()));
		out.put("print_trans_interface",
				sysSettingService.queryHint().stream()
						.filter(a -> a.getSettingName().toString()
								.equals(RiskControlType.PRINT_TRANS_INTERFACE.getCode()))
				.peek(a -> a.setSettingName(RiskControlType.PRINT_TRANS_INTERFACE.getName()))
				.collect(Collectors.toList()));

	}

	/**
	 *
	 * @param out
	 * @param request
	 * @param message
	 * @param exceedtime
	 * @param clientNature
	 * @param dealProof
	 * @param proofName
	 * @param clientIdentifying
	 * @param identifyingName
	 * @param sellerBuyer
	 * @param whetherName
	 * @param messageType
	 * @param messageName
	 * @param whetherStart
	 * @param messageNameEng
	 * @param parameterName
	 * @param tonnage
	 * @param count
	 * @param percent
	 * @return 风控保存
	 */
	@RequestMapping("savaRisk.html")
	@ResponseBody
	public Result saveReward(ModelMap out, HttpServletRequest request, @RequestParam("message") String[] message, // 提示信息
			@RequestParam("exceedtime") String exceedtime, // 超期天数
			@RequestParam("clientNature") String[] clientNature, // 客户性质
			@RequestParam("dealProof") String[] dealProof, // 交易凭证
			@RequestParam("proofName") String[] proofName, // proofName 凭证名称
			@RequestParam("clientIdentifying") String[] clientIdentifying, // 客户标示
			@RequestParam("identifyingName") String[] identifyingName, // 标示名称
			@RequestParam("sellerBuyer") String[] sellerBuyer, @RequestParam("defaultValue") String[] defaultValue,
			@RequestParam("whetherName") String[] whetherName, @RequestParam("messageType") String[] messageType,
			@RequestParam("messageName") String[] messageName, @RequestParam("whetherStart") String[] whetherStart,
			@RequestParam("messageNameEng") String[] messageNameEng,
			@RequestParam("parameterName") String[] parameterName, @RequestParam("tonnage") BigDecimal[] tonnage,
			@RequestParam("count") BigDecimal[] count, @RequestParam("percent") BigDecimal[] percent,
			@RequestParam("orgId") Integer[] orgId, @RequestParam("orgName") String[] orgName,
			@RequestParam("singleTradeWeight") BigDecimal[] singleTradeWeight,
			@RequestParam("allTradeQuality") BigDecimal[] allTradeQuality,
			@RequestParam("allTradeWeight") BigDecimal[] allTradeWeight,
			@RequestParam("ControlPinSettingsType") String[] ControlPinSettingsType,
			@RequestParam("ControlPinSettingsName") String[] ControlPinSettingsName) {

		Result result = new Result();
		SysSetting tempSys;
		for (int j = 0; j < message.length; j++) {
			if (message[j].length() > 100) {
				result.setSuccess(false);
				result.setData("提示信息超长！");
				return result;
			}
		}
		List<SysSetting> sysSettingList = new ArrayList<SysSetting>();
		for (int a = 0; a < clientIdentifying.length; a++) {
			tempSys = new SysSetting();
			tempSys.setSettingType(RiskControlType.CUSTOMER_LABEL.getCode());
			tempSys.setSettingName(sellerBuyer[a]);
			tempSys.setDisplayName(clientIdentifying[a]);
			tempSys.setSettingValue(identifyingName[a]);
			tempSys.setDefaultValue(defaultValue[a]);
			tempSys.setDescription(clientIdentifying[a]);
			tempSys.setSequence(a + 1);
			sysSettingList.add(tempSys);

		}

		// whetherName=aaa;
		for (int i = 0; i < whetherName.length; i++) {
			String whether = request.getParameter("whether" + (i + 1));
			tempSys = new SysSetting();
			tempSys.setSettingType(RiskControlType.BILL_SETTING.getCode());
			tempSys.setSettingName(whetherName[i]);
			tempSys.setDisplayName(whether == null ? "0" : whether);
			tempSys.setSettingValue(whether == null ? "0" : whether);
			tempSys.setDefaultValue(whether == null ? "0" : whether);
			tempSys.setSequence(i + 1);
			sysSettingList.add(tempSys);
		}

		Reward reward;
		int digit = 1;
		List<Reward> rewardList = new ArrayList<Reward>();
		for (int i = 0; i < parameterName.length; i++) {
			String whether = request.getParameter(+orgId[i] + "yesParameter" + digit);
			reward = new Reward(orgId[i], orgName[i], parameterName[i], whether, count[i], tonnage[i], percent[i],
					singleTradeWeight[i], allTradeQuality[i], allTradeWeight[i], digit);
			rewardList.add(reward);
			digit++;
			if (digit == whetherName.length + 1) {
				digit = 1;
			}
		}

		for (int i = 0; i < clientNature.length; i++) {
			String yesPass = request.getParameter("yesPass" + (i + 1));
			tempSys = new SysSetting();
			tempSys.setSettingType(RiskControlType.CLIENT_TYPE.getCode());
			tempSys.setDisplayName(dealProof[i]);//
			tempSys.setSettingName(clientNature[i]);
			tempSys.setSettingValue(yesPass);
			tempSys.setDefaultValue(yesPass);
			tempSys.setDescription(proofName[i]);
			tempSys.setSequence(i + 1);
			sysSettingList.add(tempSys);
		}

		for (int i = 0; i < message.length; i++) {
			tempSys = new SysSetting();
			tempSys.setSettingType(RiskControlType.HINT.getCode());
			tempSys.setSettingName(messageName[i]);
			tempSys.setSettingValue(whetherStart[i]);
			tempSys.setDefaultValue(messageNameEng[i]);
			tempSys.setDescription(message[i]);
			tempSys.setDisplayName(messageType[i]);
			tempSys.setSequence(i + 1);
			sysSettingList.add(tempSys);
		}
		SysSetting sysSetting = new SysSetting(RiskControlType.EXCEEDTIME.getCode(), exceedtime, exceedtime, exceedtime,
				exceedtime, exceedtime, 1);
		for (int i = 0; i < ControlPinSettingsType.length; i++) {
			String pinSettings = request.getParameter(ControlPinSettingsType[i]);
			tempSys = new SysSetting();
			tempSys.setSettingType(RiskControlType.CONTROLPINSETTINGS.getCode());
			tempSys.setSettingName(ControlPinSettingsType[i]);
			tempSys.setSettingValue(pinSettings);
			tempSys.setDefaultValue(pinSettings);
			tempSys.setDescription(pinSettings);
			tempSys.setDisplayName(ControlPinSettingsName[i]);
			tempSys.setSequence(i + 1);
			sysSettingList.add(tempSys);
		}

		// String pinSettings = request.getParameter("ControlPinSettings");
		// SysSetting ControlPinSettings = new
		// SysSetting(RiskControlType.CONTROLPINSETTINGS.getCode(), pinSettings,
		// pinSettings, pinSettings, pinSettings, pinSettings, 1);
		// sysSettingList.add(ControlPinSettings);
		try {
			rewardService.perfectSysSetting(rewardList, getLoginUser().getName());
			sysSettingService.perfectSysSetting(sysSettingList, sysSetting, getLoginUser().getName());
			result.setSuccess(true);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 查询页面的提示语
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/findhint", method = RequestMethod.POST)
	public @ResponseBody Result findhint(@RequestParam("userId") Integer userId, @RequestParam("type") String type,
			@RequestParam("accountName") String accountName) {
		Account account = accountService.selectAccountByName(accountName);
		Result result = new Result();
		BigDecimal singleTradeWeight = new BigDecimal(0.00);//单笔交易吨位上限（吨）
		BigDecimal allTradeQuality = new BigDecimal(0);//总交易笔数上限（笔）
		BigDecimal allTradeWeight = new BigDecimal(0.00);//总交易吨位上限（吨）
		BigDecimal monthTradeQuality =new BigDecimal(0);//月交易笔数上限（笔）
		BigDecimal monthTradeWeight = new BigDecimal(0.00);//月交易吨位上限（吨）
		BigDecimal radio  =  new BigDecimal(0);//已交易量占比服务中心百分比
		try {
			// 查询提示语
			List<SysSetting> hint = sysSettingService.queryHint().stream()
					.filter(a -> a.getSettingName().toString().equals(type))
					//.filter(a -> a.getSettingValue().equals("1"))
					.filter(a -> a.getDefaultValue().equals(account.getSupplierLabel())).collect(Collectors.toList());
			if(hint.get(0).getSettingValue().equals("0")){
				hint.get(0).setDescription("");//add by wangxianjun 风控设置中的“能否开单设置”，不用勾选上新开交易单界面提示相应选项才能起作用
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("accountId", account.getId() + "");
			// 查询月份交易量的 和交易笔数
			AllowanceOrderItemsDto sellerQuantity = consignOrderItemsService.querySellerQuantity(paramMap);
			// 查询总交易量的 和交易笔数
			AllowanceOrderItemsDto sellerAllQuantity = consignOrderItemsService.queryAllSellerQuantity(paramMap);
			paramMap.put("supplierLabel", account.getSupplierLabel() + "");
			//  查询标示（白名单、非白名单）月份总交易量的 和交易笔数
			paramMap.put("accountId", userId + "");
			AllowanceOrderItemsDto sellerOrgQuantity = consignOrderItemsService.querySellerOrgQuantity(paramMap);
			// 查询  服务中心交易量参数
			Reward reward = rewardService.getDealParameter(userId, account.getSupplierLabel());
			// 
			if (account.getSellerAllTradeQuality() != null
					&& account.getSellerAllTradeQuality().compareTo(new BigDecimal(0)) > 0) {
				/**
				 * 判断卖家总交易笔数 是否与 小于查询到的交易总数
				 * hint  Ext1 是判断 是否可以开单
				 * hint Description 提示语
				 */
				allTradeQuality = account.getSellerAllTradeQuality();
				if (sellerAllQuantity.getTotalQuantity() >= account.getSellerAllTradeQuality().intValue()) {
					hint.get(0).setDescription("卖家客户总交易笔数不得超过" + account.getSellerAllTradeQuality() + "(笔)！已交易笔数为"
							+ sellerAllQuantity.getTotalQuantity() + "（笔）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}
			} 
				//判断卖家总交易吨位是否为空  并且要大于0
			if (account.getSellerAllTradeWeight() != null
					&& account.getSellerAllTradeWeight().compareTo(new BigDecimal(0)) >= 0) {
				allTradeWeight = account.getSellerAllTradeWeight();
				if (sellerAllQuantity.getTotalWeight().compareTo(account.getSellerAllTradeWeight()) > 0) {
					hint.get(0).setDescription("卖家客户总交易吨位不得超过" + account.getSellerAllTradeWeight() + "(吨)！已交易吨位为"
							+ sellerAllQuantity.getTotalWeight() + "（吨）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}
			}
			//客户本身的单笔重量设置了，就取客户本身的值
			if (account.getSellerSingleTradeWeight() != null
					&& account.getSellerSingleTradeWeight().compareTo(new BigDecimal(0)) > 0) {
				singleTradeWeight = account.getSellerSingleTradeWeight();
			}

			 //判断  服务中心交易量参数是否为空 ，并且开启    1-- 开启  0--关闭   
			if (reward != null && "1".equals(reward.getCategoryUuid())) {
				//客户本身设置的单笔重量为空，就取风控设置的
				if (account.getSellerSingleTradeWeight() == null) {
					singleTradeWeight = reward.getSingleTradeWeight();
				}
				// 判断服务中心交易量参数 月交易吨位 是否为空  ，实际交易吨位大于服务中心交易量参数 月交易吨位，超出return
				if (reward.getRewardRole() != null&& sellerQuantity.getTotalWeight().compareTo(reward.getRewardRole()) >= 0) {
					hint.get(0).setDescription("卖家月交易吨位不得超过" + reward.getRewardRole().intValue() + "（吨）！当月已交易量为"
							+ sellerQuantity.getTotalWeight().intValue() + "（吨）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}else {
					monthTradeWeight = reward.getRewardRole();
				}
				// 判断服务中心交易量参数 月交易笔数是否为空  ，实际交易笔数大于服务中心交易量参数 月交易笔数，超出return
				if (reward.getSellerLimit() != null
						&& sellerQuantity.getTotalQuantity() >= reward.getSellerLimit().intValue()) {
					hint.get(0).setDescription("卖家月交易数笔不得超过" + reward.getSellerLimit() + "（笔）！当月已交易笔数为"
							+ sellerQuantity.getTotalQuantity() + "（笔）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}else {
					monthTradeQuality = reward.getSellerLimit();
				}
				// 判断服务中心交易量参数总交易笔数是否为空  ，实际交易总笔数大于服务中心交易量参数 总交易笔数，超出return
				if (reward.getAllTradeQuality() != null&&account.getSellerAllTradeQuality() == null
						&& sellerAllQuantity.getTotalQuantity() >= reward.getAllTradeQuality().intValue()) {

					hint.get(0).setDescription("卖家客户总交易笔数不得超过" + reward.getAllTradeQuality() + "(笔)！已交易笔数为"
							+ sellerAllQuantity.getTotalQuantity() + "（笔）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}
				//客户本身设置的总交易笔数上限（笔）为空，就取风控设置的
				if(account.getSellerAllTradeQuality() == null){
					allTradeQuality = reward.getAllTradeQuality();
				}
				// 判断服务中心交易量参数总交易吨位 是否为空  ，实际交易吨位大于服务中心交易量参数 总交易吨位，超出return
				if (reward.getAllTradeWeight() != null&&account.getSellerAllTradeWeight() == null
						&& sellerAllQuantity.getTotalWeight().compareTo(reward.getAllTradeWeight()) >= 0) {
					hint.get(0).setDescription("卖家客户总交易吨位不得超过" + reward.getAllTradeWeight() + "(吨)！已交易量为"
							+ sellerAllQuantity.getTotalWeight() + "（吨）！");
					hint.get(0).setExt1("0");
					result.setData(hint.get(0));
					return result;
				}
				//客户本身设置的总交易吨位上限（吨）为空，就取风控设置的
				if(account.getSellerAllTradeWeight() == null){
					allTradeWeight = reward.getAllTradeWeight();
				}
				// 判断服务中心交易量参数 与目标交易量占比上限  是否为空  ，并且卖家信息目标交易量占比上限是否开启，
				if (reward.getBuyRadio() != null && account.getIsSellerPercent() == 1) {
					// 查询 服务中心交易量 
					BigDecimal monthWeightAll = targetWeightService.selectByUserId(userId);
				     // 去风控设置百分比  获取限制交易参数
					Double weightAll = (reward.getBuyRadio().doubleValue() / 100) * monthWeightAll.doubleValue();
					if (sellerOrgQuantity.getTotalWeight().compareTo(new BigDecimal(weightAll)) >= 0) {
						hint.get(0).setDescription("服务中心当月交易量超过目标总量" + monthWeightAll + "（吨）的" + reward.getBuyRadio()
								+ "% ! 服务中心"+account.getSupplierLabel()+"当月历史交易量为" + sellerOrgQuantity.getTotalWeight() + "（吨）！");
						hint.get(0).setExt1("0");
						result.setData(hint.get(0));
						return result;
					}else {
						if(sellerOrgQuantity.getTotalWeight() != null && monthWeightAll != null)
							radio = sellerOrgQuantity.getTotalWeight().divide(monthWeightAll).setScale(2, BigDecimal.ROUND_HALF_UP);
					}
				}
			}

			//add by wangxianjun 提交审核时提示总交易量及已经交易总量
			String hintMsg = "";
			//判断  服务中心交易量参数是否为空 ，并且开启    1-- 开启  0--关闭
			if (reward != null && "1".equals(reward.getCategoryUuid())) {
				if (singleTradeWeight != null && singleTradeWeight.compareTo(new BigDecimal(0)) > 0) {
					hintMsg += "卖家客户单笔交易吨位上限" + singleTradeWeight + "(吨);";
				}
				if (allTradeQuality != null && allTradeQuality.compareTo(new BigDecimal(0)) > 0) {
					hintMsg += "卖家客户总交易笔数上限" + allTradeQuality + "(笔),已交易笔数为" + sellerAllQuantity.getTotalQuantity() + "（笔）;";
				}
				if (allTradeWeight != null && allTradeWeight.compareTo(new BigDecimal(0)) > 0) {
					hintMsg += "卖家客户总交易吨位上限" + allTradeWeight + "(吨),已交易吨位为" + sellerAllQuantity.getTotalWeight() + "（吨）;";
				}
				if (monthTradeQuality != null && monthTradeQuality.compareTo(new BigDecimal(0)) > 0) {
					hintMsg += "卖家客户月交易笔数上限" + monthTradeQuality + "（笔）,当月已交易笔数为" + sellerQuantity.getTotalQuantity() + "（笔）;";
				}
				if (monthTradeWeight != null && monthTradeWeight.compareTo(new BigDecimal(0)) > 0) {
					hintMsg += "卖家客户月交易吨位上限" + monthTradeWeight.intValue() + "(吨),当月已交易量为" + sellerQuantity.getTotalWeight() + "（吨）;";
				}
				if (reward.getBuyRadio() != null && account.getIsSellerPercent() == 1) {
					hintMsg += "卖家客户服务中心当月交易量占比上限" + reward.getBuyRadio() + "% ,当月已交易量占比为" + radio + "%;";
				}
			}
			hint.get(0).setExt3(hintMsg);

			// 判断是否 可以开单 Ext1 装载过去判断
			List<SysSetting> billSetting = sysSettingService.queryBillSetting().stream()
					.filter(a -> a.getSettingName().equals(account.getSupplierLabel())).collect(Collectors.toList());
			hint.get(0).setExt1(billSetting.get(0).getDefaultValue());
			if("0".equals(billSetting.get(0).getDefaultValue())){
				hint.get(0).setDescription( hint.get(0).getDefaultValue()+ "不能开单！");
			}
			result.setData(hint.get(0));
		} catch (Exception e) {
			result.setSuccess(false);
			result.setData(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/confineorder", method = RequestMethod.POST)
	public @ResponseBody Result confineorder(@RequestParam("userId") Integer userId,
			@RequestParam("accountId") Long accountId, @RequestParam("totalWeight") BigDecimal totalWeight) {
		// 查询 accountId 查到supplierLabel
		Account account = accountService.queryById(accountId);
		Result result = new Result();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountId", accountId + "");
//		List<Reward> outReward = rewardService.getDealParameter();
//		if (outReward == null) {
//			return result;
//		}
		// 判断卖家资料  单笔交易是否为空  不为空  则判断， 
		if (account.getSellerSingleTradeWeight() != null
				&& account.getSellerSingleTradeWeight().compareTo(new BigDecimal(0)) > 0) {
			//  单笔交易量超出 则 return
			if (totalWeight.compareTo(account.getSellerSingleTradeWeight()) > 0) {
				result.setData("单笔交易量不允许超过" + account.getSellerSingleTradeWeight() + "(吨)");
				result.setSuccess(false);
				return result;
			}
		}
		// 查询总交易量的 和交易笔数
		AllowanceOrderItemsDto sellerAllQuantity = consignOrderItemsService.queryAllSellerQuantity(paramMap);
     	//判断卖家总交易笔数不等于空  并且要大于0   实 际交易笔数大于查询交易笔数，超出return
		if (account.getSellerAllTradeQuality() != null
				&& account.getSellerAllTradeQuality().compareTo(new BigDecimal(0)) > 0) {
			if (sellerAllQuantity.getTotalQuantity() >= account.getSellerAllTradeQuality().intValue()) {
				result.setData("卖家客户总交易笔数不得超过" + account.getSellerAllTradeQuality() + "(笔)！已交易笔数为"
						+ sellerAllQuantity.getTotalQuantity() + "（笔）！");
				result.setSuccess(false);
				return result;
			}
		}
		//判断卖家总交易吨位不等于空  并且要大于0   实 际交易吨位大于查询交易吨位，超出return
		if (account.getSellerAllTradeWeight() != null
				&& account.getSellerAllTradeWeight().compareTo(new BigDecimal(0)) > 0) {
			if (sellerAllQuantity.getTotalWeight().compareTo(account.getSellerAllTradeWeight()) > 0) {
				result.setData("卖家客户总交易吨位不得超过" + account.getSellerAllTradeWeight() + "(吨)！已交易吨位为"
						+ sellerAllQuantity.getTotalWeight() + "（吨）！");
				result.setSuccess(false);
				return result;
			}
		}
		// 查询  服务中心交易量参数
		Reward reward = rewardService.getDealParameter(userId, account.getSupplierLabel());
		// Reward reward = outReward.stream().filter(a ->
		// a.getRewardType().equals(account.getSupplierLabel()))
		// .collect(Collectors.toList()).get(0);
		// 目标交易量
		BigDecimal monthWeightAll = null;
		 //判断  服务中心交易量参数是否为空 ，并且开启    1-- 开启  0--关闭   
		if (reward != null && "1".equals(reward.getCategoryUuid())) {
			try {
				// 查询 服务中心交易量 
				monthWeightAll = targetWeightService.selectByUserId(userId);
			} catch (Exception e) {
				result.setData("服务中心目标交易量设置为空！");
				result.setSuccess(false);
				return result;
			}
			// 判断卖家资料  单笔交易是否为空  为空继续判断
			if (account.getSellerSingleTradeWeight() == null) {
				// 判断服务中心交易量参数 单笔交易是否为空 ，单笔交易大于服务中心交易量参数 --单笔交易，超出return
				
				if (reward.getSingleTradeWeight() != null && totalWeight.compareTo(reward.getSingleTradeWeight()) > 0) {
					result.setData("单笔交易量不允许超过" + reward.getSingleTradeWeight() + "(吨)");
					result.setSuccess(false);
					return result;
				}
			}
			// 查询月份交易量的 和交易笔数		
			AllowanceOrderItemsDto sellerQuantity = consignOrderItemsService.querySellerQuantity(paramMap);
			paramMap.put("accountId", userId + "");
			paramMap.put("supplierLabel", account.getSupplierLabel() + "");
		   //  查询标示（白名单、非白名单）月份总交易量的 和交易笔数
			AllowanceOrderItemsDto sellerOrgQuantity = consignOrderItemsService.querySellerOrgQuantity(paramMap);
			if (sellerQuantity == null) {
				result.setData("该卖家找不到标示！");
				result.setSuccess(false);
				return result;
			}
			if (sellerOrgQuantity == null) {
				result.setData("该交易员找不到所属服务中心！");
				result.setSuccess(false);
				return result;
			}

			// if(totalWeight.compareTo(val))
			// 判断服务中心交易量参数 交易吨位是否为空  ，实际交易吨位大于服务中心交易量参数 月交易吨位，超出return
			
			if (reward.getRewardRole() != null
					&& reward.getRewardRole().compareTo(totalWeight.add(sellerQuantity.getTotalWeight())) == -1) {
				result.setData("卖家月交易吨位不得超过" + reward.getRewardRole().intValue() + "（吨）！当月已交易量为"
						+ sellerQuantity.getTotalWeight().intValue() + "（吨）！");
				result.setSuccess(false);
				return result;
			}
			// 判断卖家资料  总交易吨位是否为空  为空继续判断
			
			if (account.getSellerAllTradeWeight() == null) {
				// 判断服务中心交易量参数 总交易吨位是否为空  ，实际总交易吨位大于服务中心交易量参数 总交易吨位，超出return
				if (reward.getAllTradeWeight() != null
						&& sellerAllQuantity.getTotalWeight().compareTo(reward.getAllTradeWeight()) > 0) {
					result.setData("卖家客户总交易吨位不得超过" + reward.getAllTradeWeight() + "(吨)！已交易量为"
							+ sellerAllQuantity.getTotalWeight() + "（吨）！");
					result.setSuccess(false);
					return result;
				}
			}
			// 判断服务中心交易量参数 月交易数笔是否为空  ，实际月交易数笔大于服务中心交易量参数 月交易数笔，超出return
			if (reward.getSellerLimit() != null
					&& sellerQuantity.getTotalQuantity() >= reward.getSellerLimit().intValue()) {
				result.setData("卖家月交易数笔不得超过" + reward.getSellerLimit() + "（笔）！当月已交易笔数为"
						+ sellerQuantity.getTotalQuantity() + "（笔）！");
				result.setSuccess(false);
				return result;
			}
			// 判断卖家资料  总交易笔数是否为空  为空继续判断
			if (account.getSellerAllTradeQuality() == null) {
				// 判断服务中心交易量参数 卖家客户总交易笔是否为空  ，实际卖家客户总交易笔大于服务中心交易量参数 卖家客户总交易笔，超出return
				if (reward.getAllTradeQuality() != null
						&& sellerAllQuantity.getTotalQuantity() >= reward.getAllTradeQuality().intValue()) {
					result.setData("卖家客户总交易笔数不得超过" + reward.getAllTradeQuality() + "(笔)！已交易笔数为"
							+ sellerAllQuantity.getTotalQuantity() + "（笔）！");
					result.setSuccess(false);
					return result;
				}
			}
			// 判断服务中心交易量参数 与目标交易量占比上限是否为空  ，实际交易吨位大于服务中心交易量参数 与目标交易量占比上限，超出return
			if (account.getIsSellerPercent() != null && account.getIsSellerPercent() == 1) {
				if (reward.getBuyRadio() != null && monthWeightAll != null) {
					// weightAll 与目标交易量占比 重量
					Double weightAll = (reward.getBuyRadio().doubleValue() / 100) * monthWeightAll.doubleValue();
					if (totalWeight.add(sellerOrgQuantity.getTotalWeight()).compareTo(new BigDecimal(weightAll)) > 0) {
						result.setData("服务中心当月交易量超过目标总量" + monthWeightAll + "（吨）的" + reward.getBuyRadio()
								+ "% ! 服务中心"+account.getSupplierLabel()+"当月历史交易量为" + sellerOrgQuantity.getTotalWeight() + "（吨）！");
						result.setSuccess(false);
						return result;
					}
				} else {
					if(monthWeightAll == null) {
						result.setData("服务中心目标交易量设置为空！");
						result.setSuccess(false);
						return result;
					}
					if(reward.getBuyRadio()  == null || reward.getBuyRadio().compareTo(new BigDecimal(0)) <= 0){
						result.setData("当服务中心目标交易量设置不为空时，与目标交易量占比上限必须大于0！");
						result.setSuccess(false);
						return result;
					}
				}
			}
		}

		return result;
	}

}
