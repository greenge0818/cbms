package com.prcsteel.platform.smartmatch.web.controller.api;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDailyDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestHotResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestNormalResourceQuery;
import com.prcsteel.platform.smartmatch.service.ResourceService;
import com.prcsteel.platform.smartmatch.service.RestMarketToSmartMatchService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 基础资源Rest接口
 * @author prcsteel
 * @modifydate:2016-6-20
 * @modifyAuthor:zhoucai
 * @modifyDescription:新增根据城市分页查询日常资源(掌柜APP)
 *
 */
@RestController
@RequestMapping("/api/resource/")
public class RestResourceController {
	//日志
	private static final Logger log =  LoggerFactory.getLogger(RestResourceController.class);
	// 接口返回值，成功
	public static final String RESULT_SUCCESS = "0";
	// 接口返回值，失败
	public static final String RESULT_ERROR = "1";

  
	@Resource
	private ResourceService resourceService;
	@Resource
	private RestMarketToSmartMatchService restMarketToSmartMatchService;

	/**
	 * 保存交易单记录到资源表，资源类型为历史资源
	 * @param orderItems 待关联的资源详情
	 * @return Result
	 */
	@RequestMapping(value = "saveOrderHistoryResource.html", method = RequestMethod.POST)
    @ApiOperation("保存交易单记录到资源表，资源类型为历史资源")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderItems", value = "资源的字符串,例："
			+ "[{\"lastUpdatedBy\":\"cbadmin\",\"quantity\":11,\"accountName\":\"杭州宏达钢铁贸易有限公司_001\","
			+ "\"weightConcept\":\"理计\",\"factoryName\":\"中天\",\"weight\":21.937900,\"categoryName\":\"螺纹钢\","
			+ "\"warehouseName\":\"三里洋\",\"spec\":\"18\",\"accountId\":8482,\"materialName\":\"HRB400E\","
			+ "\"lastUpdated\":1470986451221,\"cityName\":\"杭州市\",\"price\":2573.000000,\"userIds\":1}]"
			, dataType = "String", paramType = "query") })
	public Result saveOrderHistoryResource(@RequestBody	List<ResourceDto> orderItems){
		try {
			if (orderItems != null && !orderItems.isEmpty()) {
				for(ResourceDto resourceDto : orderItems){
					// 调用资源服务
					resourceService.saveHistoryResource(resourceDto,resourceDto.getLastUpdatedBy());					
				}
			}
		} catch (Exception e) {
			log.error("接口调用失败", e);
			return new Result("保存接口调用失败:" + e.getMessage(), false);
		}
		return new Result();
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public static void main(String[] args) {
		// String ss = "resource/saveOrderHistoryResource.html?dataMap={}";
		// log.debug(ss.charAt(48));
		RestResourceController rrr = new RestResourceController();
	/*	Long t1 = System.currentTimeMillis();
		String binStr = "101110000001111,110011110010111,101101101010000";
		log.debug(rrr.BinstrToStr(binStr));
		log.debug(System.currentTimeMillis() - t1);
		*/
		String test2 = "首钢";
	  //  System.out.println(rrr.StrToBinstr(test2));
		
	}

	// 将二进制字符串转换为char
	private char BinstrToChar(String binStr) {
		int[] temp = BinstrToIntArray(binStr);
		int sum = 0;
		for (int i = 0; i < temp.length; i++) {
			sum += temp[temp.length - 1 - i] << i;
		}
		return (char) sum;
	}

	// 将二进制字符串转换成int数组
	private int[] BinstrToIntArray(String binStr) {
		char[] temp = binStr.toCharArray();
		int[] result = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = temp[i] - 48;
		}
		return result;
	}

	// 将二进制字符串转成字符串
	private String BinstrToStr(String binStr) {
		String[] tempStr = StrToStrArray(binStr);
		char[] tempChar = new char[tempStr.length];
		for (int i = 0; i < tempStr.length; i++) {
			tempChar[i] = BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
	}

	// 将初始二进制字符串转换成字符串数组，以空格相隔
	private String[] StrToStrArray(String str) {
		return str.split(",");
	}
	
	
	// 将字符串转换成二进制字符串，以空格相隔  
    private String StrToBinstr(String str) {  
        char[] strChar = str.toCharArray();  
        String result = "";  
        for (int i = 0; i < strChar.length; i++) {  
            result += Integer.toBinaryString(strChar[i]) + ",";  
        }  
        return result;  
    }


	/**
	 *@decription:查询日常资源信息 add by zhoucai 2016-6-20
	 * @param start 分页开始
	 * @param length 长度
	 * @param cityName 资源地名称
	 * @return Result
	 */
	@RequestMapping(value = "queryDailyResource.html", method = RequestMethod.GET)
	@ApiOperation("根据城市分页查询日常资源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "1", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "length", value = "10", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "cityName", value = "杭州", dataType = "String", paramType = "query")
	})
	public RestBaseDto queryDailyResource(String start,String length,String cityName) {
		RestBaseDto result = new RestBaseDto();
		ResourceQuery resourceQuery = new ResourceQuery();

		int startIndex;
		int tLength;
		try{
			startIndex=Integer.parseInt(start);
			if(startIndex==0){
				result.setData("查询起始页需要大于0");
				result.setStatus(RESULT_ERROR);
				return result;
			}

			tLength=Integer.parseInt(length);
			startIndex=(startIndex - 1) * tLength;
		}
		catch (Exception ex){

			result.setData("参数错误");
			result.setStatus(RESULT_ERROR);
			return result;
		}

		try {
			//查询当天资源，取当天时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String BEGIN_TIME =" 00:00:00";
			String END_TIME =" 23:59:59";
			String toDay = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

			String upStart = toDay+BEGIN_TIME;
			String upEnd = toDay+END_TIME;


			if (cityName != null)
				cityName = URLDecoder.decode(cityName, "UTF-8");

			//cityName= new String(cityName.getBytes("iso-8859-1"),"utf-8");
			log.info("==========1查询日常资源请求参数：start="+startIndex+"length:"+tLength+"cityName："+cityName);
			//设置资源类型为日常资源
			resourceQuery.setStatusNum("1");
			resourceQuery.setUpStart(upStart);
			resourceQuery.setUpEnd(upEnd);
			if (startIndex != 0 || tLength != 0) {
				resourceQuery.setStart(startIndex);
				resourceQuery.setLength(tLength);
			}
			resourceQuery.setCityName(cityName);

			result.setStatus(RESULT_SUCCESS);

			List<ResourceDailyDto> resourcelist=resourceService.queryDailyResource(resourceQuery);
			log.info("=======接口调用成功返回日常资源信息为："+resourcelist.toString());
			result.setData(resourcelist);
		} catch (Exception e) {
			log.error("=======接口调用失败:", e.getMessage());
			result.setData("请求错误，服务器异常，请稍后重试");
			result.setStatus(RESULT_ERROR);
		}
		return result;
	}

    /**
     * 根据查询条件查询满足条件的资源列表---超市接口
     *
     * @param content 查询条件内容字符串
     * @return
     * @author peanut
     * @date 2016/6/22
     * @see RestNormalResourceQuery
     */
    @RequestMapping(value = "market/normal.html", method = RequestMethod.GET)
    @ApiOperation("根据查询条件查询满足条件的资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "查询条件内容字符串,例：" +
                    "{\"categoryUuid\":\"931cb217-3e66-11e5-b8c0-54ee755150b2\"," +
                    "\"categoryName\":\"冷轧卷板\",\"materialUuids\":\"68bd0df0-3e69-11e5-b8c0-54ee755150b2\"," +
                    "\"materialNames\":\"DC03\",\"factoryIds\":\"853\"," +
                    "\"factoryNames\":\"邯宝\"," +
                    "\"spec1\":\"0.60,0.6,0.65,0.68,0.7,0.70\"," +
                    "\"spec2\":\"900-2000\"," +
                    "\"orderWay\":\"ASC\"," +
                    "\"start\":0,\"pageIndex\":1," +
                    "\"pageSize\":15}",
                    dataType = "String", paramType = "query")
    })
    public RestBaseDto selectNormalResource(String content) {
        if (StringUtils.isBlank(content)) {
            log.error("select normal resource error: 查询内容为空!");
            return new RestBaseDto("-1");
        }
        RestNormalResourceQuery restNormalResourceQuery;
        try {
            restNormalResourceQuery = new Gson().fromJson(UrlUtils.urlDecode(content), RestNormalResourceQuery.class);
        } catch (Exception e) {
            log.error("select normal resource error: 查询内容转换为 RestNormalResourceQuery 对象错误!");
            return new RestBaseDto("-1");
        }

        restNormalResourceQuery.setType(Constant.REST_NORMAL_RESOURCE_TYPE);

        if (StringUtils.isBlank(restNormalResourceQuery.getOrderBy())) {
            restNormalResourceQuery.setOrderBy(Constant.DEFAULT_ORDERBY);
        }
        if (StringUtils.isBlank(restNormalResourceQuery.getOrderWay())) {
            restNormalResourceQuery.setOrderWay(Constant.DEFAULT_ORDER_WAY);
        }
        if (restNormalResourceQuery.getPageIndex() == null) {
            restNormalResourceQuery.setPageIndex(Constant.DEFAULT_PAGE_INDEX);
        }
        if (restNormalResourceQuery.getPageSize() == null) {
            restNormalResourceQuery.setPageSize(Constant.DEFAULT_NORMAL_RESOURCE_PAGE_SIZE);
        }
		try {
			return restMarketToSmartMatchService.selectNormalResource(restNormalResourceQuery);
		} catch (Exception e) {
			log.error("查询出错："+e.toString());
			return new RestBaseDto("-1");
		}
	}

    /**
     * 获取指定城市的指定条件之外的N条随机资源---超市接口
     *
     * @param content 查询条件json
     * @return
     * @author peanut
     * @date 2016/6/22
     * @see RestHotResourceQuery
     */
    @RequestMapping(value = "market/hot.html", method = RequestMethod.POST)
    @ApiOperation("获取指定城市的指定条件之外的N条随机资源---热门资源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "content", value = "查询条件内容字符串,例：" +
					"{\"items\":[{\"categoryName\": \"string\",\"categoryUuid\": \"string\",\"factoryIds\": \"string\",\"factoryNames\": \"string\"," +
					"\"materialNames\": \"string\",\"materialUuids\": \"string\",\"orderBy\": \"string\",\"orderWay\": \"string\"," +
					"\"pageIndex\": 0,\"pageSize\": 0,\"purchaseCityIds\": \"string\",\"purchaseCityNames\": \"string\",\"spec1\": \"string\"," +
					"\"spec2\": \"string\",\"spec3\":\"string\",\"specificCityName\": \"string\",\"start\": 0,\"type\": \"string\"}]," +
					"\"length\": 0,\"specificCityName\": \"string\" }",
					dataType = "String", paramType = "query")
	})
    public RestBaseDto selectRandomHostResource(@RequestBody String content) {
		RestBaseDto result=new RestBaseDto();
		RestHotResourceQuery restHotResourceQuery;
		try {
			restHotResourceQuery = new Gson().fromJson(UrlUtils.urlDecode(content), RestHotResourceQuery.class);
		} catch (Exception e) {
			log.error("select normal resource error: 查询内容转换为 restHotResourceQuery 对象错误!");
			result.setStatus("-1");
			result.setData("参数错误");
			return result;
		}
		try{
			return restMarketToSmartMatchService.selectRandomHostResource(restHotResourceQuery);
		}
		catch(Exception ex){
			log.error("查询出错：" + ex.toString());
			result.setStatus("-1");
			result.setData("查询出错,请稍后重试！");
			return result;
		}
    }

}
