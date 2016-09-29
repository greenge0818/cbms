package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.acl.model.model.Rebate;
import com.prcsteel.platform.order.model.dto.RebateDto;
import com.prcsteel.platform.order.service.rebate.RebateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date :2016-5-18
 * @decrpiton: 查询客户的返利信息，通过rest调用order，
 * @author :zhoucai@prcsteel.com
 */
@RestController
@RequestMapping("/api/sys/")
public class RestRebateController extends BaseController {
	@Resource
	RebateService rebateService;
   /**
    * 获取所有正在生效的提成方案
    * @param
    * @return List
    */
	@RequestMapping(value = "getallrebatedto", method = RequestMethod.GET)
	public List<RebateDto> getAllRebateDto() {
		return  rebateService.getAllRebateDto();
	}
	/**
	 * 重置新的返利方案
	 * @param
	 * @return List
	 */
	@RequestMapping(value = "refleshrebate", method = RequestMethod.POST)
	public int refleshRebate(@RequestParam("rebateList") List<Rebate> rebateList){
		return  rebateService.refleshRebate(rebateList);
	}
}
