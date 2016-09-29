package com.prcsteel.platform.order.web.controller.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.model.dto.OrgListDto;
import com.prcsteel.platform.order.model.query.OrgSecondSettlementQuery;
import com.prcsteel.platform.order.service.report.OrgSecondSettlementService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.common.vo.PageResult;

/**
 * @author lixiang
 * @version V1.1
 * @Title: SecondSettlementControllerr
 * @Package com.prcsteel.cbms.web.controller.report
 * @Description: 服务中心二次结算储备金日报
 * @date 2015/8/25
 */

@Controller
@RequestMapping("/report/organization")
public class OrgSecondSettlementController extends BaseController {

	@Resource
	private OrgSecondSettlementService orgSecondSettlementService;

	/**
	 * 服务中心二次结算储备金日报
	 * 
	 * @param out
	 */
	@RequestMapping("secondsettlementorg.html")
	public void SecondSettlementDaily(ModelMap out) {
		setDefaultTime(out);
	}

	@RequestMapping("org/secondsettlement.html")
	@ResponseBody
	public PageResult OrgSecondSettlementDaily(OrgSecondSettlementQuery org) {
		PageResult result = new PageResult();
		org.setUserIds(getUserIds());
		List<OrgListDto> list = orgSecondSettlementService.queryByOrg(org);
		int counts = orgSecondSettlementService.queryByOrgCount(org);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/*
	 * 设置默认时间段：当天
	 * 
	 * @param out
	 */
	private void setDefaultTime(ModelMap out) {
		// 日期:当前时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		out.put("currentTime", format.format(new Date()));
	}
	
	

}
