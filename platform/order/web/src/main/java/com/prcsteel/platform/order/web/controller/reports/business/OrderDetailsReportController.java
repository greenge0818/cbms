package com.prcsteel.platform.order.web.controller.reports.business;

import java.util.LinkedList;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.model.dto.ConsignOrderDetailReportDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDetailsCombinationDto;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.service.order.ConsignOrderReportService;

/**
 * 
 * @author zhoukun
 *
 */
@Controller
@RequestMapping("/reports/business/order/")
public class OrderDetailsReportController extends BaseController {

	@Resource
	ConsignOrderReportService consignOrderReportService;
	
	@RequestMapping("details")
	public String details(ModelMap model){
		ConsignOrderDetailQuery query = new ConsignOrderDetailQuery();
		ConsignOrderDetailReportDto res =consignOrderReportService.queryOrderDetailReport(query);
		if(res.getListData() == null){
			res.setListData(new LinkedList<ConsignOrderDetailsCombinationDto>());
		}
		model.put("dataList", res.getListData());
		model.put("format", "html");
		return "orderDetailsReport";
	}
	
}
