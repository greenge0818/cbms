package com.prcsteel.platform.acl.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.prcsteel.platform.common.enums.OpLevel;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.dto.SystemOperationLogDto;
import com.prcsteel.platform.acl.model.query.OpLogQuery;
import com.prcsteel.platform.common.query.PagedResult;
import com.prcsteel.platform.acl.service.SystemOprationLogService;

/**
 * 
 * @author zhoukun
 */
@Controller
@RequestMapping("/sys/oplog/")
public class OpLogController extends BaseController {

	@Resource
	SystemOprationLogService systemOprationLogService;
	@Resource
	UserService userService;
	
	@RequestMapping("index")
	public String index(ModelMap model, @RequestParam(value = "userId", required = false)Long userId){
		if(userId != null){
			User user = userService.queryById(userId);
			if(user != null) {
				model.put("defaultUserName", user.getName());
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.put("opTypes", OpType.values());
		model.put("opLevels", OpLevel.values());
		model.put("beginDate", sdf.format(new Date()));
		model.put("endDate", sdf.format(new Date()));
		return "sys/oplog";
	}
	
	@ResponseBody
	@RequestMapping(value="list",method=RequestMethod.POST)
	public PagedResult<SystemOperationLogDto> list(OpLogQuery query){
		return systemOprationLogService.queryByOpLogQuery(query);
	}
	
}
