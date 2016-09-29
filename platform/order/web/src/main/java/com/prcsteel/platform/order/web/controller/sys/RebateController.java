package com.prcsteel.platform.order.web.controller.sys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.acl.model.model.Rebate;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.acl.model.enums.RewardStatus;
import com.prcsteel.platform.order.service.rebate.RebateService;


/**
 * Created by chenchen on 15-7-31.
 */
@Controller
@RequestMapping("/rebate/")
public class RebateController extends BaseController {

	@Autowired
	RebateService rebateService;
	@Autowired
	CategoryGroupService categoryGroupService;


	@RequestMapping("showrebate.html")
	public String setrebate(ModelMap out) {
		out.put("tab", "rebate");
		out.put("rebate", rebateService.getAllRebateDto());
		out.put("init_rebate", categoryGroupService.selectNoSelectForRebate());
		return "sys/rebate";

	}

	@RequestMapping("saveRebate.html")
	public String saveReward(ModelMap out, @RequestParam("cat_uuid") String[] cat_uuid,
			@RequestParam("rebateRole") String[] rebateRole) {
		List<Rebate> rebateList = new ArrayList<Rebate>();
		Rebate temp;
		/*
		 * 循环封装reward对象
		 */
		for (int i = 0; i < cat_uuid.length; i++) {
			temp=new Rebate();
			temp.setCategoryUuid(cat_uuid[i]);
			temp.setCreatedBy(this.getLoginUser().getName());
			temp.setLastUpdatedBy(this.getLoginUser().getName());
			temp.setRebateRole(new BigDecimal(rebateRole[i]));
			temp.setRebateStatus(RewardStatus.EFFECT_NEXT_MONTH.getName());
			rebateList.add(temp);
		}
		this.rebateService.refleshRebate(rebateList);
		return this.setrebate(out);
	}

	@RequestMapping("savaReward1.html")
	public @ResponseBody Result savaReward1() {
		Result result = new Result();
		
		return result;
	}

}
