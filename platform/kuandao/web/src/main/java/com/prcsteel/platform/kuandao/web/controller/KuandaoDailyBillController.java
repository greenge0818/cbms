package com.prcsteel.platform.kuandao.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDailyBillDto;
import com.prcsteel.platform.kuandao.service.KuandaoDailyBillService;
import com.prcsteel.platform.kuandao.web.vo.PageResult;
import com.prcsteel.platform.kuandao.web.vo.Result;

@Controller("kuandaoDailyBillController")
@RequestMapping("/kuandao/bill/")
public class KuandaoDailyBillController  extends BaseController {
	@Resource(type=KuandaoDailyBillService.class)
	private KuandaoDailyBillService kuandaoDailyBillService;
	
	@RequestMapping("index")
	public String index(ModelMap out){
		out.put("tab", "index");
		return "/kuandao/bill/index";
	}
	
	@RequestMapping("queryDailyBill.html")
	@ResponseBody
	public PageResult queryDailyBill(KuandaoDailyBillDto dto,
			@RequestParam("start")Integer start,
			@RequestParam("length")Integer length){
		
		String startDate = dto.getStartDate();
		if(StringUtils.isNotEmpty(startDate)){
			startDate = startDate.replaceAll("-", "");
		}
		String endDate = dto.getEndDate();
		if(StringUtils.isNotEmpty(endDate)){
			endDate = endDate.replaceAll("-", "");
		}
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		//日期转字符串
		int total=kuandaoDailyBillService.queryTotalDailyBill(dto);
		List<KuandaoDailyBillDto> list=kuandaoDailyBillService.queryDailyBill(dto, start, length);
		return extractPageResult(total, list);
	}
	
	@RequestMapping("downLoadDailyBill.html")
	@ResponseBody
	public Result insertDailyBill(){
		Result result=new Result();
		String username = getLoginUser().getName();
		kuandaoDailyBillService.insertDailyBill(username);
		return result;
	}
}
