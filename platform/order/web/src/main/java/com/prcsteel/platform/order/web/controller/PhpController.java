package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.PurchaseRecordDto;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.web.vo.PHPResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PhpController {
	private static final String SUCCESSCODE = "200";
	private static final String FAILCODE = "201";
	private static Logger logger = LoggerFactory.getLogger(PhpController.class);

	@Resource
	ConsignOrderItemsService consignOrderItemsService;
	/**
	 *  cbms接口
	 * 获取采购历史记录
	 * @param phone
	 * @return
	 */
	@ApiOperation("获取采购历史记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query")
	})
	@RequestMapping(value="/market/findpurchaserecord.html",method=RequestMethod.GET)
	@ResponseBody
	public PHPResult findPurchaseRecord(@RequestParam("phone") String phone) {
		PHPResult result = new PHPResult();
		try{
			List<PurchaseRecordDto> purchaseRecord= consignOrderItemsService.findPurchaseRecord(phone);
			result.setData(purchaseRecord);
			result.setStatus(SUCCESSCODE);
		}catch (BusinessException e){
			result.setStatus(FAILCODE);
			result.setMessage(e.getCode());
			logger.info(e.getMsg());
		}
		return result;
	}
	


}
