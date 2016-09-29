package com.prcsteel.platform.kuandao.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.kuandao.model.dto.BasesetDto;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;
import com.prcsteel.platform.kuandao.service.SettingService;
import com.prcsteel.platform.kuandao.web.vo.PageResult;
import com.prcsteel.platform.kuandao.web.vo.Result;

@Controller
@RequestMapping("/kuandao/setting/")
public class KuandaoSettingController extends BaseController {

	@Resource
	private SettingService settingService;
	
	
	@RequestMapping("index")
	public String index(ModelMap out){
		out.put("tab", "index");
		return "kuandao/setting/index";
	}
	
	@RequestMapping("baseset")
	public String hadRefund(ModelMap out){
		out.put("tab", "baseset");
		BasesetDto basesetDto = settingService.queryBaseset();
		out.put("baseset", basesetDto);
		return "/kuandao/setting/baseset";
	}
	
	@RequestMapping("queryPrcsteelAccount.html")
	@ResponseBody
	public PageResult queryPrcsteelAccount(@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total = 1;
		List<PrcsteelAccountInfo> list = settingService.queryPrcsteelAccount(start, length);
		return extractPageResult(total, list);
	}
	
	
	
	@RequestMapping("modifyBaseset.html")
	@ResponseBody
	public Result modifyBaseset(String[] email, String[] phonenumber, String[] limitBankName, String[] limitBankId){
		Result result = new Result();
		String username = getLoginUser().getName();
		Integer resultCode = settingService.modifyBaseset(email,phonenumber,limitBankName,limitBankId,username);
		result.setData(resultCode);
		return result;
	}
	
	
}
