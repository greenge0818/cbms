package com.prcsteel.platform.account.web.controller.api;


import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.web.vo.EcAccountResult;
import com.prcsteel.platform.common.exception.BusinessException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api")
public class EcAccountController {


	@Resource
	private AccountService accountService;


	private static final String SUCCESSCODE = "0";
	private static final String FAILCODE = "-1";




	private static Logger logger = LoggerFactory.getLogger(EcAccountController.class);


	// 超市根据超市用户ID查询相关联的客户名称
	@ApiOperation("超市根据超市用户ID查询相关联的客户名称")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "超市用户id", dataType = "string", paramType = "query"),
	})
	@RequestMapping(value="/account/getAccountNameByEcId.html",method= RequestMethod.GET)
	@ResponseBody
	public EcAccountResult getAccountNameByEcId(String id) {
		EcAccountResult result = new EcAccountResult();
		if (StringUtils.isEmpty(id)) {
			result.setStatus(FAILCODE);
			result.setMessage("请传递超市用户ID");
			return result;
		}
		try {
			List<String> accountNames = accountService.selectAccountNameByEcId(Integer.parseInt(id));
			if (accountNames.size() == 0 || accountNames.size()  >1 ) {
				result.setStatus(SUCCESSCODE);
				result.setMessage("");
				return result;
			}
			result.setStatus(SUCCESSCODE);
			result.setMessage("success");
			result.setData(accountNames.get(0));
		}catch (BusinessException e) {
			result.setStatus(e.getCode());
			result.setMessage(e.getMsg());
			logger.info(e.getMsg());
		}

		return result;
	}
}
