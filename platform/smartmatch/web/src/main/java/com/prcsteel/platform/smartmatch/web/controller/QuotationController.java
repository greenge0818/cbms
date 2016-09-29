package com.prcsteel.platform.smartmatch.web.controller;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.smartmatch.model.dto.BasePriceCustSubscribeInfo;
import com.prcsteel.platform.smartmatch.model.dto.BusiQuotaionCommonSearchDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceSubscriberDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBusinessDto;
import com.prcsteel.platform.smartmatch.model.model.BusinessQueryData;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceMsg;
import com.prcsteel.platform.smartmatch.model.query.BasePriceSubQuery;
import com.prcsteel.platform.smartmatch.model.query.MsgQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceBusinessQuery;
import com.prcsteel.platform.smartmatch.model.query.SingleWeightQuery;
import com.prcsteel.platform.smartmatch.service.AreaService;
import com.prcsteel.platform.smartmatch.service.CategoryWeightService;
import com.prcsteel.platform.smartmatch.service.QuotationService;
import com.prcsteel.platform.smartmatch.service.ResourceService;

/**
 * 询报价管理
 * Created by afeng on 2016/6/20.
 */
@Controller
@RequestMapping("/smartmatch/quote/")
public class QuotationController extends BaseController {
	
	@Resource
	QuotationService quotationService;
	
	@Resource
	AreaService areaService;

	@Resource
	ResourceService resourceService;
	
	@Resource
	AccountContactService accountContactService;

	@Resource
	CategoryWeightService categoryWeightService;
	
	//增加日志
	private Logger logger = LoggerFactory.getLogger(QuotationController.class);
	
	private final String BEGIN_TIME =" 00:00:00";
	
	private final String END_TIME =" 23:59:59";
	
	private final String CITY_NAME = "cityName";
	
	private final String DATE = "yyyy-MM-dd"; 
	
	private final int SPEC_FLAG = 1;

	/**
	 * 买家订阅
	 * @param out
	 * @author afeng
	 */
	@RequestMapping("basepricesub.html")
	public String basePriceSub(ModelMap out){
		String userId = this.getLoginUser().getId()+"";
		String userName = this.getLoginUser().getName();
		Long orgId = this.getLoginUser().getOrgId();
		//获取城市
		Map<String, Object> map = quotationService.getCityNameByuserOrgId(orgId);
		String cityName = null;
		Long cityid = null;
		if(map != null){
			Object cityNameObj = map.get(CITY_NAME);
			cityName = cityNameObj == null ? null : cityNameObj.toString();
			Object cityIdObj = map.get("cityId");
			cityid = cityIdObj == null ? null : (Long) cityIdObj;
		}
		
		out.put("userId", userId);
		out.put("userName", userName);
		out.put(CITY_NAME, cityName);
		out.put("cityId", cityid);
		return "smartmatch/quote/basepricesub";
	}
	/**
	 * 买家订阅列表查询
	 * @param basePriceSubQuery
	 * @author afeng
	 */
	@RequestMapping("/searchbasepricesub.html")
	@ResponseBody
	public PageResult searchBasePriceSub(BasePriceSubQuery basePriceSubQuery) {
		basePriceSubQuery.setUserIdList(getUserIds());
		basePriceSubQuery.setLoginId(getLoginUser().getLoginId());
		Integer total = quotationService.selectBasePriceSubListCount(basePriceSubQuery);
        List<CustBasePriceSubscriberDto> list = quotationService.selectBasePriceSubList(basePriceSubQuery);
        PageResult result=new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(total);
        return result;
	}
	/**
	 * 常搜资源查询
	 * @param out
	 * @author afeng
	 */
	@RequestMapping("/searchCommonSearch.html")
	@ResponseBody
	public Result searchCommonSearch() {
		String loginId = this.getLoginUser().getLoginId();
		List<BusiQuotaionCommonSearchDto> dto = quotationService.getCommonSearchAll(loginId);
        return new Result(dto);
	}
	
	/**
	 * 批量删除订阅信息
	 * 
	 * @param ids
	 *            id集
	 * @return
	 */
	@RequestMapping("/delBasePriceSub")
	@ResponseBody
	public Result delBasePriceSub(@RequestParam("ids") List<Long> ids) {
		Result result = new Result();
		try {
			quotationService.delBasePriceSub(ids);
			result.setData("操作成功");
		} catch (BusinessException e) {
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 删除常搜资源
	 * 
	 * @param ids
	 *            id集
	 * @return
	 */
	@RequestMapping("/delHotSearch")
	@ResponseBody
	public Result delHotSearch(@RequestParam("id") Long id) {
		Result result = new Result();
		try {
			quotationService.delCommonSearch(id);
			//查询新增后的数据，用于刷新前台页面的div
			List<BusiQuotaionCommonSearchDto> datas = quotationService.getCommonSearchAll(this.getLoginUser().getLoginId());
			result.setData(datas);
		} catch (BusinessException e) {
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 保存资源对象
	 * 
	 * @param resourceDto
	 * @return
	 */
	@RequestMapping("/saveHotSearch")
	@ResponseBody
	public Result saveHotSearch(BusiQuotaionCommonSearchDto busiQuotaionCommonSearchDto) {
		Result result = new Result();
		try {
			busiQuotaionCommonSearchDto.setCreated(new Date());
			busiQuotaionCommonSearchDto.setCreatedBy(this.getLoginUser().getLoginId());
			quotationService.saveCommonSearch(busiQuotaionCommonSearchDto);
			//查询新增后的数据，用于刷新前台页面的div
			List<BusiQuotaionCommonSearchDto> datas = quotationService.getCommonSearchAll(this.getLoginUser().getLoginId());
			result.setData(datas);
		} catch (BusinessException e) {
			logger.error("====" + e.getMsg());
			result.setData(e.getMsg());
			result.setSuccess(false);
		}

		return result;
	}
	/**
	 * 获取所有买家
	 * 
	 * @return
	 */
	@RequestMapping("getCommonData")
	@ResponseBody
	public Result getCommonData() {
		return new Result(quotationService.getCommonData());
	}
	/**
	 * 根据城市获取基价的信息
	 * 
	 * @return
	 */
	@RequestMapping("getBasePriceData")
	@ResponseBody
	public Result getBasePriceData(@RequestParam("cityName") String cityName) {
		List<Map<String, Object>>  result = quotationService.getBasePriceDataBycityId(cityName);
		return new Result(result);
	}
	/**
	 * 根据买家ID获取联系人及交易员信息
	 * 
	 * @return
	 */
	@RequestMapping("getContactData")
	@ResponseBody
	public Result getContactData(@RequestParam("accountId") Long accountId) {
		Map<String, Object> data = new HashedMap();
		List<Map<String, Object>> result = quotationService.getContactDataByAccountId(accountId);
		data.put("contactInfo", result);
		List<User> traderlist = accountContactService.queryUserByCompanyId(accountId);
		data.put("traderInfo", traderlist);
		return new Result(data);
	}
	

	/**
	 * 保存订阅
	 * 
	 * @param basePriceSubscriberDto
	 * @return
	 */
	@RequestMapping("/saveBasePriceSub")
	@ResponseBody
	public Result saveBasePriceSub(CustBasePriceSubscriberDto basePriceSubscriberDto) {
		Result result = new Result();
		try {
			Date date = Calendar.getInstance().getTime();
			User user = this.getLoginUser();
			basePriceSubscriberDto.setCreated(date);
			basePriceSubscriberDto.setCreatedBy(user.getLoginId());
			basePriceSubscriberDto.setLastUpdated(date);
			basePriceSubscriberDto.setLastUpdatedBy(user.getLoginId());
			quotationService.saveBasePriceSub(basePriceSubscriberDto);
			result.setData("保存成功!");
		} catch (BusinessException e) {
			logger.error("保存失败" + e.getMsg());
			result.setData(e.getMsg());
			result.setSuccess(false);
		}

		return result;
	}
	/**
	 * 业务找货Tab
	 * @param out
	 * @return
	 */
	@RequestMapping("businesslook.html")
	public String businesslook(ModelMap out){
		Organization org  = getOrganization();
		String cityName = "";
		if(org != null){
			City city = quotationService.selectCity(org.getCityId());
			if(city != null){
				cityName = city.getName();
			}
		}
		out.put(CITY_NAME, cityName);
		return "/smartmatch/quote/businesslook";
	}
	
	
	/**
	 * 基价管理
	 * @param out
	 * @author afeng
	 */
	@RequestMapping("baseprice.html")
	public void baseprice(ModelMap out){
		//地区集合
		AreaQuery query = new AreaQuery();
		query.setIsEnable("1");
		query.setStart(0);
		query.setLength(100);
		List<AreaCityDto> cityList = areaService.query(query);
		User user = getLoginUser();
		Organization org = getOrganization();
		City city = quotationService.selectCity(org.getCityId());
		
		out.put("user", user);
		out.put("city", city);
		out.put("cityList", cityList);
	}
	
	/**
	 * 根据选择的城市获取基价的类别
	 * @param cityId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getBasePriceTypeByCityId.html", method = RequestMethod.POST)
	public Result getBasePriceTypeByCityId(Long cityId) {
		Result result = new Result();
		List<String> types;
		try {
			types = quotationService.getAllBasePriceType(cityId);
			result.setData(types);
		} catch (Exception e) {
			logger.error("根据选择的城市获取基价的类别，发生异常",e);
			result.setSuccess(false);
		}
		return result;
	}
	
	
	/**
	 * 查询基价信息
	 * @param custBasePrice
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getbaseprice.html", method = RequestMethod.POST)
	public PageResult getbaseprice(CustBasePrice custBasePrice) {
		PageResult result = new PageResult();
		List<CustBasePriceDto> list = quotationService.selectBasePrice(custBasePrice);
		result.setData(list);
		return result;
	}
	
	/**
	 * 新增/修改基价管理
	 * @param custBasePrice
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "savebaseprice.html", method = RequestMethod.POST)
	public Result savebaseprice(CustBasePrice custBasePrice) {
		Result result = new Result();
		User user = getLoginUser();
		//add by caosulin 160825处理基价类别空值,空字符串和空格都不存入数据库。
		if(custBasePrice != null){
			if(StringUtils.isBlank(custBasePrice.getBasePriceType())){
				custBasePrice.setBasePriceType(null);
			}
			
			if(custBasePrice.getPrice() != null){
				custBasePrice.setLastUpdated(new Date());
			}else{
				custBasePrice.setPrice(new BigDecimal(0));
			}
		}
		try {
			if (quotationService.saveSelective(custBasePrice, user) > 0) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("保存失败！");
			}
		} catch (BusinessException ex) {
			logger.error("保存失败" + ex.getMsg());
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}
	
	/**
	 * 逻辑删除基价信息
	 * @param basePriceId
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deletebaseprice.html", method = RequestMethod.POST)
	public Result deletebaseprice(Long basePriceId) {
		Result result = new Result();
		try {
			if (quotationService.deleteBybasePriceId(basePriceId) > 0) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("删除基价信息失败！");
			}
		} catch (BusinessException ex) {
			logger.error("保存失败" + ex.getMsg());
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}
	
	/**
	 * 业务找货匹配规格,品名,材质,钢厂列表
	 * @param query
	 * @return
	 */
	@RequestMapping("matchresource.html")
	@ResponseBody
	public List<ResourceBaseDto> matchResource(ResourceBusinessQuery query){
		return quotationService.getResourceListByConditions(query);
	}
	
	 /** 发布报价vm页
	 * @param out
	 * @author afeng
	 */
	@RequestMapping("release.html")
	public String release(ModelMap out){
		return "/smartmatch/quote/release";
	}
	
	/**
	 * 发布基价信息
	 * @param releaseDateList
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "releasequote.html", method = RequestMethod.POST)
	public Result releasequote(String releaseDateList) {
		Result result = new Result();
		try {
			if (quotationService.releaseQuote(releaseDateList, true, getLoginUser())) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("发布报价失败！");
			}
		} catch (BusinessException ex) {
			logger.error("发布报价失败！" + ex.getMsg());
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}
	
	/** 历史短信查询页面
	 * @param out
	 * @author afeng
	 */
	@RequestMapping("historicalmsg.html")
	public String historicalmsg(ModelMap out){
		return "/smartmatch/quote/historicalmsg";
	}
	
	/**
	 * 历史短信数据
	 * @param msgQuery
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "get/historicalmsg.html", method = RequestMethod.POST)
	public PageResult getHistoricalmsg(MsgQuery msgQuery) {
		try {
			msgQuery.setStartTime(getDateStart(msgQuery));
			msgQuery.setEndTime(getDateEnd(msgQuery));
		} catch (ParseException e) {
			logger.error("历史短信查询报错："+e.getMessage());
		}
		PageResult result = new PageResult();
		List<CustBasePriceMsg> list = quotationService.getHistoricalMsg(msgQuery);
		int count = quotationService.getHistoricalMsgCount(msgQuery);
		result.setData(list);
		result.setRecordsFiltered(count);
		result.setRecordsTotal(list.size());
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "searchsinglebusinessresource.html", method = RequestMethod.POST)
	public PageResult searchSingleBusinessResource(ResourceBusinessQuery resourceBusinessQuery){
		converSingleResourceBusinessQuery(resourceBusinessQuery);
		Integer total = quotationService.getSingleBusinessResourceListTotal(resourceBusinessQuery);
		List<ResourceBusinessDto> businessDtolist = new ArrayList<ResourceBusinessDto>();
		PageResult pr=new PageResult();
		if(total > 0){
			businessDtolist = quotationService.getSingleBusinessResourceList(resourceBusinessQuery);
		}
		pr.setData(businessDtolist);
		pr.setRecordsFiltered(total);
		pr.setRecordsTotal(total);
		return pr;
	}
	
	/**
	 * 修改新增业务找货资源信息
	 * @param resourceDateList
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveresource.html", method = RequestMethod.POST)
	public Result saveResource(String resourceDateList) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			ResourceBaseDto dto = quotationService.saveResource(resourceDateList, user);
			if (dto != null) {
				SingleWeightQuery query =new SingleWeightQuery();
				query.setCategoryUuid(dto.getCategoryUuid());
				query.setFactoryId(dto.getFactoryId());
				query.setNorms(dto.getSpec());
				BigDecimal single_weight=categoryWeightService.selectSingleWeightByParamIds(query);
				dto.setSingleWeight(single_weight);
				result.setData(dto);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("修改资源信息失败！");
			}
		} catch (BusinessException ex) {
			logger.error("修改资源信息失败："+ex.getMsg());
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "searchtmultibusinessresource.html", method = RequestMethod.POST)
	public PageResult searchtMultiBusinessResource(ResourceBusinessQuery resourceBusinessQuery){
		converMultiResourceBusinessQuery(resourceBusinessQuery);
		Integer total = quotationService.getMultiBusinessResourceListTotal(resourceBusinessQuery);
		List<ResourceBusinessDto> businessDtolist = new ArrayList<ResourceBusinessDto>();
		PageResult pr=new PageResult();
		if(total > 0){
			businessDtolist = quotationService.getMultiBusinessResourceList(resourceBusinessQuery);
		}
		pr.setData(businessDtolist);
		pr.setRecordsFiltered(total);
		pr.setRecordsTotal(total);
		return pr;
	}
	
	/**
	 * 获取用户联系人名称,联系人电话
	 * @param accountId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "serchcontact.html", method = RequestMethod.GET)
	public Result  serchContact(Long accountId){
		//Session 中获取所有的客户以及客户对应的联系人
		Result result = new Result();
		//rest接口调用频繁,抛链接异常,先本地调用实现
		List<AccountContact> contactList = accountContactService.getContactByAccountId(accountId);;
		result.setData(contactList);
		return result;
	}
	/**
	 * 根据资源id查资源信息
	 * @param resourceId
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "querybyresourceId.html", method = RequestMethod.POST)
	public Result queryByResourceId(Long resourceId) {
		Result result = new Result();
		ResourceBaseDto resource =  resourceService.selectById(resourceId);
		result.setData(resource);
		return result;
	}

	/**
	 * 获取当前操作员关联的买家及订阅状态
	 * @param resourceId
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "querycustsubscribinfo.html", method = RequestMethod.POST)
	public Result queryCustSubscribInfo(Long resourceId) {
		Result result = new Result();
		List<BasePriceCustSubscribeInfo> list=  quotationService.queryCustSubscribInfo(getUserIds());
		result.setData(list);
		return result;
	}
	
	/**
	 * 起始日期
	 * 
	 * @param msgQuery
	 * @return
	 * @throws ParseException
	 */
	private Date getDateStart(MsgQuery msgQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE);
		Date dateStart = null;
		if (StringUtils.isNotEmpty(msgQuery.getStartDate())) {
			dateStart = format.parse(msgQuery.getStartDate());
		}
		return dateStart;
	}

	/**
	 * 终止日期
	 * 
	 * @param msgQuery
	 * @return
	 * @throws ParseException
	 */
	private Date getDateEnd(MsgQuery msgQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE);
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(msgQuery.getEndDate())) {
			dateEnd = new Date(
					format.parse(msgQuery.getEndDate()).getTime() + 24 * 3600 * 1000);
		} 
		return dateEnd;
	}

	/**
	 * 设置查询当天参数
	 * @param resourceBusinessQuery
	 */
	private void setQueryDay(ResourceBusinessQuery resourceBusinessQuery){
		if(resourceBusinessQuery.getIsTodayPrice()){
			String toDay = new SimpleDateFormat(DATE).format(Calendar.getInstance().getTime());
			resourceBusinessQuery.setBeginTime(toDay+BEGIN_TIME);
			resourceBusinessQuery.setEndTime(toDay+END_TIME);
		}
	}
	
	/**
	 * 转换前台传输过来的品名,材质规格等数据(单条件查询)
	 * @param resourceBusinessQuery
	 */
	private void converSingleResourceBusinessQuery(ResourceBusinessQuery resourceBusinessQuery){
		setQueryDay(resourceBusinessQuery);
		//设置规格区间
		if(resourceBusinessQuery.getSpec().indexOf("-") != -1){
			resourceBusinessQuery.setSpecQueryFlag(SPEC_FLAG);
			String [] specs = resourceBusinessQuery.getSpec().split("\\*");
			int index = -1;
			String prefixSpec = "";
			String suffixSpec = "";
			if(specs.length > 0){
				index = specs[0].indexOf("-");
				if(index != -1){
					resourceBusinessQuery.setSpec1Flag(SPEC_FLAG);
					prefixSpec = specs[0].substring(0,index);
					suffixSpec = specs[0].substring(index+1,specs[0].length());
					resourceBusinessQuery.setPrefixSpec1(prefixSpec == "" ? "":prefixSpec);
					resourceBusinessQuery.setSuffixSpec1(suffixSpec == "" ? "":suffixSpec);
				}else{
					resourceBusinessQuery.setPrefixSpec1(specs[0]);
				}
			}
			if(specs.length >1){
				index = specs[1].indexOf("-");
				if(index != -1){
					resourceBusinessQuery.setSpec2Flag(SPEC_FLAG);
					prefixSpec = specs[1].substring(0,index);
					suffixSpec = specs[1].substring(index+1,specs[1].length());
					resourceBusinessQuery.setPrefixSpec2(prefixSpec == "" ? "":prefixSpec);
					resourceBusinessQuery.setSuffixSpec2(suffixSpec == "" ? "":suffixSpec);
				}else{
					resourceBusinessQuery.setPrefixSpec2(specs[1]);
				}
			}
			if(specs.length >2){
				index = specs[1].indexOf("-");
				if(index != -1){
					resourceBusinessQuery.setSpec3Flag(SPEC_FLAG);
					prefixSpec = specs[2].substring(0,index);
					suffixSpec = specs[2].substring(index+1,specs[2].length());
					resourceBusinessQuery.setPrefixSpec3(prefixSpec == "" ? "":prefixSpec);
					resourceBusinessQuery.setSuffixSpec3(suffixSpec == "" ? "":suffixSpec);
				}else{
					resourceBusinessQuery.setPrefixSpec3(specs[2]);
				}
			}
		}
	}
	
	/**
	 * 转换前台传输过来的品名,材质规格等数据(多条件查询)
	 * @param resourceBusinessQuery
	 */
	private void converMultiResourceBusinessQuery(ResourceBusinessQuery resourceBusinessQuery){
		setQueryDay(resourceBusinessQuery);
		if(resourceBusinessQuery.getSpecs() == null || resourceBusinessQuery.getSpecs().length <= 0){
			return;
		}
		int i=0;
		BusinessQueryData data = null;
		for(;i<resourceBusinessQuery.getSpecs().length;i++){
			//规格
			if(StringUtils.isNotEmpty(resourceBusinessQuery.getSpecs()[i])){
				if(data == null){
					data = new BusinessQueryData();
				}
				data.setSpec(resourceBusinessQuery.getSpecs()[i]);
				if(data.getSpec().indexOf("-") != -1){
					resourceBusinessQuery.setSpecQueryFlag(SPEC_FLAG);
					String [] specs = data.getSpec().split("\\*");
					int index = -1;
					String prefixSpec = "";
					String suffixSpec = "";
					if(specs.length > 0){
						index = specs[0].indexOf("-");
						if(index != -1){
							data.setSpec1Flag(SPEC_FLAG);
							prefixSpec = specs[0].substring(0,index);
							suffixSpec = specs[0].substring(index+1,specs[0].length());
							data.setPrefixSpec1(prefixSpec == "" ? "":prefixSpec);
							data.setSuffixSpec1(suffixSpec == "" ? "":suffixSpec);
						}else{
							data.setPrefixSpec1(specs[0]);
						}
					}
					if(specs.length >1){
						index = specs[1].indexOf("-");
						if(index != -1){
							data.setSpec2Flag(SPEC_FLAG);
							prefixSpec = specs[1].substring(0,index);
							suffixSpec = specs[1].substring(index+1,specs[1].length());
							data.setPrefixSpec2(prefixSpec == "" ? "":prefixSpec);
							data.setSuffixSpec2(suffixSpec == "" ? "":suffixSpec);
						}else{
							data.setPrefixSpec2(specs[1]);
						}
					}
					if(specs.length >2){
						index = specs[1].indexOf("-");
						if(index != -1){
							data.setSpec3Flag(SPEC_FLAG);
							prefixSpec = specs[2].substring(0,index);
							suffixSpec = specs[2].substring(index+1,specs[2].length());
							data.setPrefixSpec3(prefixSpec == "" ? "":prefixSpec);
							data.setSuffixSpec3(suffixSpec == "" ? "":suffixSpec);
						}else{
							data.setPrefixSpec3(specs[2]);
						}
					}
				}
			}
			//品名
			if(StringUtils.isNotEmpty(resourceBusinessQuery.getCategorys()[i])){
				if(data == null){
					data = new BusinessQueryData();
				}
				data.setCategoryName(resourceBusinessQuery.getCategorys()[i]);
			}
			//材质
			if(StringUtils.isNotEmpty(resourceBusinessQuery.getMaterials()[i])){
				if(data == null){
					data = new BusinessQueryData();
				}
				data.setMaterialName(resourceBusinessQuery.getMaterials()[i]);
			}
			//钢厂
			if(StringUtils.isNotEmpty(resourceBusinessQuery.getFactorys()[i])){
				if(data == null){
					data = new BusinessQueryData();
				}
				data.setFactoryName(resourceBusinessQuery.getFactorys()[i]);
			}
			//交货地
			if(StringUtils.isNotEmpty(resourceBusinessQuery.getCitys()[i])){
				if(data == null){
					data = new BusinessQueryData();
				}
				data.setCityName(resourceBusinessQuery.getCitys()[i]);
			}
			if(data != null){
				resourceBusinessQuery.getQueryDataList().add(data);
				data = null;
			}
		}
	}
}
