package com.prcsteel.platform.order.web.controller.api;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.service.order.HolidaySettingService;
import com.prcsteel.platform.order.web.vo.EcOrderResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api")
public class HolidayController {


	@Resource
	private HolidaySettingService holidaySettingService;


	private static final String work = "1";
	private static final String holiday = "0";
	private static final String error = "-1";
	
	/**
	 * 通过当前时间查询是否是工作日
	 * 日期格式为：yyyy-MM-dd_HH:mm:ss
	 * 结果为1：为工作时间
	 * 结果为0：非工作时间
	 * @param workDate 当前时间
	 * @return
	 */
	@ApiOperation("超市传递当前时间查询是否在工作时间")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "workDate", value = "当前时间", dataType = "string", paramType = "query"), })
	@RequestMapping(value = "/order/getHoliday.html", method = RequestMethod.GET)
	@ResponseBody
	public EcOrderResult getHoliday(String workDate) {
		EcOrderResult result = new EcOrderResult();
		if (StringUtils.isEmpty(workDate)) {
			result.setStatus(error);
			result.setMessage("请传递当前日期");
			return result;
		}
		if (holidaySettingService.workOrHoliday(workDate) == 1) {
			result.setStatus(work);
		} else {
			result.setStatus(holiday);
		}
		return result;
	}
}
