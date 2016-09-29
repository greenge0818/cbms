package com.prcsteel.platform.smartmatch.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.DateUtil;
import com.prcsteel.platform.common.utils.StringUtil;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.MaterialsDao;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.smartmatch.api.RestAccountService;
import com.prcsteel.platform.smartmatch.api.RestSendSmsService;
import com.prcsteel.platform.smartmatch.model.dto.BasePriceCustSubscribeInfo;
import com.prcsteel.platform.smartmatch.model.dto.BusiQuotaionCommonSearchDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceSubscriberDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBusinessChildDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBusinessDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.enums.ResourceException;
import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceSourceType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceStatus;
import com.prcsteel.platform.smartmatch.model.model.BusiQuotaionCommonSearch;
import com.prcsteel.platform.smartmatch.model.model.BusinessQueryData;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceContactRelation;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceMsg;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceSubscriber;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceSubscriberRelation;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceTraderRelation;
import com.prcsteel.platform.smartmatch.model.model.Factory;
import com.prcsteel.platform.smartmatch.model.model.ResouceForMarket;
import com.prcsteel.platform.smartmatch.model.model.ResourceNorms;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.BasePriceSubQuery;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
import com.prcsteel.platform.smartmatch.model.query.MsgQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceBusinessQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.SyncResourceQuery;
import com.prcsteel.platform.smartmatch.persist.dao.BusiQuotaionCommonSearchDao;
import com.prcsteel.platform.smartmatch.persist.dao.CustBasePriceDao;
import com.prcsteel.platform.smartmatch.persist.dao.CustBasePriceMsgDao;
import com.prcsteel.platform.smartmatch.persist.dao.CustBasePriceSubscriberDao;
import com.prcsteel.platform.smartmatch.persist.dao.FactoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.persist.dao.WarehouseDao;
import com.prcsteel.platform.smartmatch.service.BasePriceRelationService;
import com.prcsteel.platform.smartmatch.service.BaseProductService;
import com.prcsteel.platform.smartmatch.service.QuotationService;
import com.prcsteel.platform.smartmatch.service.ResourceChangeForMaketService;
import com.prcsteel.platform.smartmatch.service.ResourceService;

@Service("quotationService")
public class QuotationServiceImpl implements QuotationService ,ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(QuotationServiceImpl.class);
	@Resource
	CustBasePriceDao custBasePriceDao;
	@Resource
	CityDao cityDao;
	@Resource
	private BasePriceRelationService basePriceRelationService;
	@Resource
	FactoryDao factoryDao;
	
	@Resource
	CategoryDao categoryDao;
	
	@Resource
	MaterialsDao materialsDao;
	
	@Resource
	ResourceDao resourceDao;
	
	@Resource
	WarehouseDao warehouseDao;
	
	@Resource
	private CustBasePriceSubscriberDao custBasePriceSubscriberDao;
	
	@Resource
	private BusiQuotaionCommonSearchDao busiQuotaionCommonSearchDao;
	
	@Resource
	CustBasePriceMsgDao custBasePriceMsgDao;
	
	@Resource
	ResourceNormsDao resourceNormsDao;
	
	@Resource
	CategoryService categoryService;

	@Resource
	BaseProductService baseProductService;

	@Resource
	ResourceService resourceService;

	@Resource
	private ResourceChangeForMaketService resourceChangeForMaketService;

	//@Resource
	private RestSendSmsService restSendSmsService;
	
	private ApplicationContext applicationContext = null;

	private Object getRestService(String serviceName) {
		Object bean = applicationContext.getBean(serviceName);
		if (bean != null) {
			return bean;
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取不到REST服务：" + serviceName);
		}
	}
	
	private static final String status = "sent";

	//日志
	private static final Logger log =  LoggerFactory.getLogger(QuotationServiceImpl.class);

	public List<CustBasePriceSubscriberDto> selectBasePriceSubList(BasePriceSubQuery basePriceSubQuery){
		return custBasePriceSubscriberDao.selectBasePriceSubList(basePriceSubQuery);
	}
	
	@Override
	public int selectBasePriceSubListCount(BasePriceSubQuery basePriceSubQuery) {
		return custBasePriceSubscriberDao.selectBasePriceSubListCount(basePriceSubQuery);
	}

	/**
	 * 获取所有的基价类别
	 * @return
	 */
	public List<String> getAllBasePriceType(Long cityId){
		return custBasePriceDao.getAllBasePriceType(cityId);
	}
	@Override
	public void delBasePriceSub(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除的ids集不能为空!");
		}
		//删除主表
		custBasePriceSubscriberDao.batchDelBasePriceSubByIds(ids);
		//删除关联表
		custBasePriceSubscriberDao.batchDelBasePriceSubRelationByIds(ids);
		//删掉联系人关联表
		custBasePriceSubscriberDao.batchDelBasePriceSubContactByIds(ids);
		//删掉交易员关联表
		custBasePriceSubscriberDao.batchDelBasePriceSubTraderByIds(ids);
	}


	@Override
	public Map<String, Object> getCommonData() {
		// 所有买家
		Object accountRestBean = applicationContext.getBean("smart_restAccountService");
		List<AccountAllDto> accountList = null;
		if(accountRestBean != null){
			RestAccountService restAccountService = (RestAccountService) accountRestBean;
			accountList = restAccountService.selectAllBuyerAccount();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountList", accountList);
		return map;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
		
	}


	@Override
	public List<Map<String, Object>> getBasePriceDataBycityId(String cityName) {
		return custBasePriceSubscriberDao.getBasePriceDataBycityId(cityName);
	}


	@Override
	public List<Map<String, Object>> getContactDataByAccountId(Long accountId) {
		return custBasePriceSubscriberDao.getContactDataByAccountId(accountId);
	}


	@Override
	public Map<String, Object> getCityNameByuserOrgId(Long userOrgId) {
		Map<String, Object> result = custBasePriceSubscriberDao.getCityNameByuserOrgId(userOrgId);
		return result;
	}
	
	/**
	 * 保存订阅信息
	 * @param dto
	 */
	public void saveBasePriceSub(CustBasePriceSubscriberDto dto) {
		if (dto == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"保存参数不能为空!");
		}
		CustBasePriceSubscriber record = new CustBasePriceSubscriber();
		// 买家不能为空
		String accountIdStr = dto.getAccountId();
		if (StringUtils.isBlank(accountIdStr)) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"买家不能为空!");
		}
		
		CustBasePriceSubscriber temp = new CustBasePriceSubscriber();
		temp.setAccountId(Long.parseLong(dto.getAccountId()));
		//temp.setCityId(dto.getCityId());
		temp.setId(dto.getId());
		//add by zhoucai@prcsteel.com 统计订阅次数 2016-8-28
		int countcontact =0;
		List<CustBasePriceContactRelation> contactLists = custBasePriceSubscriberDao.selectBasePriceContactByAccountId(temp);
		if (contactLists != null && contactLists.size() > 0) {
			if(!StringUtils.isBlank(dto.getSubBasePriceContactIds())){
				String contactName = null;
				String [] contactIds = dto.getSubBasePriceContactIds().split(",");
				for(String contactId : contactIds){
					for(CustBasePriceContactRelation contact : contactLists){
						if(contactId.equals(contact.getContactId()+"")){
							contactName = contact.getContactName();
							countcontact++;
						}
					}
				}
				//add by zhoucai@prcsteel.com 同一买家下的同一联系人只能订阅一次 2016-8-28
				if(countcontact>1){
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
							"买家联系人"+contactName+"已订阅，不能重复订阅!");
				}
				if(contactName != null){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"买家联系人"+contactName+"已经订阅该地区信息，不能重复订阅!");
				}
			}
			
			
		}
		

		record.setAccountId(Long.parseLong(accountIdStr));

		// 交易员
		record.setOwnerId(dto.getOwnerId());

		// 所在城市地区
		record.setCityId(dto.getCityId());
		
		//ID
		record.setId(dto.getId());
		
		//设置最新的更新时间和更新人
		record.setLastUpdated(dto.getLastUpdated());
		record.setLastUpdatedBy(dto.getLastUpdatedBy());
		
		// 查询原来是否有数据（条件为地区和买家名称还有联系人）
		Long basePriceSubId = -1L;
		//add by caosulin 增加联系人集合
		List<String> contactids = new ArrayList<String>();
		String subBasePriceContactIds = dto.getSubBasePriceContactIds();
		if (!StringUtils.isBlank(subBasePriceContactIds)) {
			String[] subBasePriceContactIdsArray = subBasePriceContactIds
					.split(",");
			for (int i = 0; i < subBasePriceContactIdsArray.length; i++) {
				String contactId = subBasePriceContactIdsArray[i];
				contactids.add(contactId);
			}
		}
		dto.setContactIds(contactids);
		List<CustBasePriceSubscriber> infos = custBasePriceSubscriberDao.selectBasePriceSubByAccountId(dto);
		if("EDIT".equals(dto.getActionType())){
			//根据ID更新本条数据
			custBasePriceSubscriberDao.updateByPrimaryKeySelective(record);
			basePriceSubId = record.getId();
			//修改逻辑，如果查询出原来有数据，地区和买家名称一致的是本条数据（ID相同）则修改本条数据，
			//如果查询出原来有数据，地区和买家名称一致的是本条数据（ID不相同）则修改本条数据，后删除另外一条数据及其关联表。
			if (infos != null && !infos.isEmpty()) {
				CustBasePriceSubscriber data = infos.get(0);
				if(data.getId() != record.getId()){//如果不是本条数据
					Long oldId = data.getId();
					//删除主表数据
					custBasePriceSubscriberDao.deleteByPrimaryKey(oldId);
					//删除2张关联关系表数据
					List<Long> cids = new java.util.ArrayList<Long>();
					cids.add(oldId);
					// 删除联系人
					custBasePriceSubscriberDao.batchDelBasePriceSubContactByIds(cids);
					// 删除交易员
					custBasePriceSubscriberDao.batchDelBasePriceSubTraderByIds(cids);
					// 删除订阅的基价
					custBasePriceSubscriberDao.batchDelBasePriceSubRelationByIds(cids);
				}
			}
		}else{
			//添加逻辑，如果有则更新，没有则新增
			if (infos != null && !infos.isEmpty()) {
				custBasePriceSubscriberDao.updateByPrimaryKey(record);
				CustBasePriceSubscriber data = infos.get(0);
				basePriceSubId = data.getId();
			} else {
				// 设置创建人和创建时间
				record.setCreated(record.getLastUpdated());
				record.setCreatedBy(record.getLastUpdatedBy());
				// 插入主表
				custBasePriceSubscriberDao.insertSelective(record);
				basePriceSubId = record.getId();
			}
		}
		//保存关联关系
		saveBasePriceSubRelations(dto, basePriceSubId);
	}

	/**
	 * 保存订阅信息的关联关系，先删后增
	 * @param dto
	 * @param basePriceSubId
	 */
	private void saveBasePriceSubRelations(CustBasePriceSubscriberDto dto,
			Long basePriceSubId) {
		// 先删除关联关系
		List<Long> cids = new java.util.ArrayList<Long>();
		cids.add(basePriceSubId);
		// 删除联系人
		custBasePriceSubscriberDao.batchDelBasePriceSubContactByIds(cids);
		// 删除交易员
		custBasePriceSubscriberDao.batchDelBasePriceSubTraderByIds(cids);
		// 删除订阅的基价
		custBasePriceSubscriberDao.batchDelBasePriceSubRelationByIds(cids);

		// 插入订阅基价关联表
		String subBasePriceIds = dto.getSubBasePriceIds();
		if (!StringUtils.isBlank(subBasePriceIds)) {
			String[] subBasePriceIdsArray = subBasePriceIds.split(",");
			for (int i = 0; i < subBasePriceIdsArray.length; i++) {
				String id = subBasePriceIdsArray[i];
				CustBasePriceSubscriberRelation relation = new CustBasePriceSubscriberRelation();
				relation.setBasePriceId(Long.parseLong(id));
				relation.setSubscriberId(basePriceSubId);
				relation.setCreated(dto.getCreated());
				relation.setCreatedBy(dto.getCreatedBy());
				relation.setLastUpdated(dto.getLastUpdated());
				relation.setLastUpdatedBy(dto.getLastUpdatedBy());
				custBasePriceSubscriberDao
						.insertCustBasePriceSubscriberRelation(relation);
			}
		}
		// 插入订阅联系人关联表
		String subBasePriceContactIds = dto.getSubBasePriceContactIds();
		if (!StringUtils.isBlank(subBasePriceContactIds)) {
			String[] subBasePriceContactIdsArray = subBasePriceContactIds
					.split(",");
			String[] subBasePriceContactNamesArray = dto.getSubBasePriceContactNames().split(",");
			String[] subBasePriceContactTelsArray = dto.getSubBasePriceContactTels().split(",");
			String contactName ="";
			String tel = "";
			for (int i = 0; i < subBasePriceContactIdsArray.length; i++) {
				String contactId = subBasePriceContactIdsArray[i];
				CustBasePriceContactRelation contact = new CustBasePriceContactRelation();
				contact.setContactId(Long.parseLong(contactId));
				if(subBasePriceContactNamesArray.length > i  && !StringUtils.equals(subBasePriceContactNamesArray[i],"-1")){
					contactName = subBasePriceContactNamesArray[i];
				}
				if(subBasePriceContactTelsArray.length > i  && !StringUtils.equals(subBasePriceContactTelsArray[i],"-1")){
					tel = subBasePriceContactTelsArray[i];
				}
				contact.setContactName(contactName);
				contact.setTel(tel);
				contact.setCreated(dto.getCreated());
				contact.setCreatedBy(dto.getCreatedBy());
				contact.setLastUpdated(dto.getLastUpdated());
				contact.setLastUpdatedBy(dto.getLastUpdatedBy());
				contact.setSubscriberId(basePriceSubId);
				custBasePriceSubscriberDao
						.insertCustBasePriceContactRelation(contact);
			}
		}
		// 插入订阅交易员关联表
		String subBasePriceTraderIds = dto.getSubBasePriceTraderIds();
		if (!StringUtils.isBlank(subBasePriceTraderIds)) {
			String[] subBasePriceTraderIdsArray = subBasePriceTraderIds
					.split(",");
			for (int i = 0; i < subBasePriceTraderIdsArray.length; i++) {
				String contactId = subBasePriceTraderIdsArray[i];
				CustBasePriceTraderRelation trader = new CustBasePriceTraderRelation();
				trader.setTraderId(Long.parseLong(contactId));
				trader.setCreated(new Date());
				trader.setSubscriberId(basePriceSubId);
				custBasePriceSubscriberDao
						.insertCustBasePriceTraderRelation(trader);
			}
		}
	}
	@Override
	public List<CustBasePriceDto> selectBasePrice(CustBasePrice custBasePrice) {
		return custBasePriceDao.selectBasePrice(custBasePrice);
	}

	@Override
	public int saveSelective(CustBasePrice record, User user) {
		// modify by caosulin160822 增加一个地区条件，查询基价。
		CustBasePrice custBasePrice =custBasePriceDao.selectByName(record.getBasePriceName(),record.getCity());
		if (null != custBasePrice && null == record.getId()) {//新增时，判断是否重复
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "基价名称已存在！");
		}else if(null != custBasePrice && record.getId() != null){//修改时，判断是否重复
			if(record.getId() != custBasePrice.getId()){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "基价名称已存在！");
			}
		}
		City city = cityDao.queryByCityName(record.getCity());
		if (null == city) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "地区名字非法，请重新输入！");
		}
		Factory factory = factoryDao.selectByName(record.getFactory());
		if (factory == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的钢厂！");
		}
		record.setCityId(city.getId());
		record.setFactoryId(factory.getId());
		
		List<Materials> materials = materialsDao.selectByMaterialsName(record.getMaterial());
		if (materials.isEmpty()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的材质！");
		}
		record.setMaterialUuid(materials.get(0).getUuid());
	
		Category category = categoryDao.selectByCategory(record.getCategory());
		if (category == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的品名！");
		}
		record.setCategoryUuid(category.getUuid());
		
		int num = 0;
		if (null == record.getId()) {
			record.setCreated(new Date());
			record.setUserId(user.getId());
			record.setBaseLastUpdateBy(user.getLoginId());
			record.setBaseLastUpdated(new Date());
			record.setUserName(user.getName());
			num = custBasePriceDao.insertSelective(record);
		} else {
			record.setBaseLastUpdated(new Date());
			record.setUserName(user.getName());
			num = custBasePriceDao.updateBasePriceById(record);

			//同步关联资源
			List<Long> list = new ArrayList<>();
			list.add(record.getId());
			SyncResourceQuery query = new SyncResourceQuery();
			query.setBasepriceIds(list);
			query.setOperator(user.getLoginId());
			custBasePriceDao.syncResourceByBasePriceIds(query);

			//同步到超市和行情圈
			syncBaseProduct(list, user);
		}
		return num;
	}

	@Override
	public City selectCity(Long id) {
		return cityDao.selectByPrimaryKey(id);
	}
	
	@Override
	public int deleteBybasePriceId(Long id) {
		CustBasePrice custBasePrice =custBasePriceDao.selectByPrimaryKey(id);
		if (null == custBasePrice) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "此信息已被删除，请刷新页面！");
		}
		//modify by caosulin 20160823 判断此基价是否有订阅，如果有订阅则不让删除
		List<CustBasePriceSubscriberRelation> sub = custBasePriceDao.
				getSubByBasePriceId(custBasePrice.getId());
		if(sub != null && !sub.isEmpty()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "此基价已经被订阅不能删除！！");
		}
		
		//modify by caosulin 20160922 判断此基价是否有基价关联设置，如果有则不让删除
		CustBasePriceRelationQuery query = new CustBasePriceRelationQuery();
		query.setBasePriceName(custBasePrice.getBasePriceName());
		List<CustBasePriceRelation> bprelation = basePriceRelationService.getBasePriceRelationList(query);
		if(bprelation != null && !bprelation.isEmpty()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "此基价已设置卖家关联不能删除！");
		}
		
		return custBasePriceDao.deleteBybasePriceId(id);
	}
	

	@Override
	public List<ResourceBaseDto> getResourceListByConditions(
			ResourceBusinessQuery query) {
		return resourceDao.getResourceListByConditions(query);
	}

	/**
	 *
	 * @param releaseDateList 要更新的基价信息
	 * @param sendMessage  是否发送短信 true 发送,false不发送
     * @return
     */
	@Override
	@Transactional
	public boolean releaseQuote(String releaseDateList, boolean sendMessage, User operator) {
		boolean release = false;
		CustBasePrice custBasePrice = new CustBasePrice();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode releaseJsons = null;
		try {
			releaseJsons = mapper.readTree(releaseDateList);
		} catch (IOException e) {
			log.error("======================方法releaseQuote报错，错误信息：="+e.getLocalizedMessage());
		}
		List<Long> basePriceIds = new ArrayList<Long>();
		if (releaseDateList != null) {
			for (JsonNode node : releaseJsons) {
				custBasePrice.setId(node.path("id").asLong());
				custBasePrice.setPrice(new BigDecimal(node.path("price").asDouble()));
				custBasePrice.setLastUpdated(new Date());
				custBasePriceDao.updateByPrimaryKeySelective(custBasePrice);
				basePriceIds.add(node.path("id").asLong());
			}
			release = true;
		}
		syncResourceByBasePriceIds(basePriceIds, operator); //同步日常资源
		syncBaseProduct(basePriceIds, operator);	//同步到超市和行情圈

		if(!sendMessage){
			return true;
		}
		//发送短信basePriceIds
		DateFormat format = new SimpleDateFormat("MM-dd-HH");
		String f = format.format(new Date());
		String[] s = f.split("-");
		List<Map<String,Object>> msgList = custBasePriceSubscriberDao.selectContactByBasePrice(basePriceIds);
		if (!msgList.isEmpty()) {
			for (Map<String, Object> map : msgList) {
				String content = s[0]+"月" +s[1]+ "日" +s[2]+ "点" + map.get("city") + "场参考报价," + map.get("basePriceName");
				String trader = map.containsKey("traderinfo") ? map.get("traderinfo").toString() : "";
				if(!trader.equals("")){
					content += "-" + trader;
				}
				String mobile = (String) map.get("tel");
				//Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(mobile, content));
				HashMap<String, String> params = new HashMap<>();
		        String timestamp = DateUtil.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
		        params.put("timestamp", timestamp);
		        content = content.replaceAll(" ", "");
		        params.put("content", content);
		        String from = Constant.FROM;
		        params.put("from", from);
		        params.put("phone", mobile);
				String token;
		        try {
		            token = StringUtil.getSignature(params, Constant.SECRET);
		           
		        } catch (IOException e) {
		            logger.error("发送短信时生成token失败",e);
		            throw new BusinessException(e.getMessage(), "==");
		        }
				//add by zhoucai@prcsteel.com 2016-9-14 新增客户基联系人相关信息
				Long accoutId = (Long) map.get("accountId");
				Long contactId = (Long) map.get("contactId");
				String accoutName = (String) map.get("accountName");
				String contactName = (String) map.get("name");
		        //获取发送短信的服务
		        getRestSendSmsServiceBean();
		        try {
			        if(this.restSendSmsService != null){
			        	 restSendSmsService.send(timestamp, token, mobile, content, from);
			        }else{
			        	logger.error("--------------------找不到发送短信的服务的bean[beanName:restSendSmsService]");
			        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"找不到发送短信的服务接口！");
			        }
		        } catch (Exception e) {
		            logger.error("发送短信失败!",e);
		            throw new BusinessException(e.getMessage(), "发送短信失败!");
		        }
				CustBasePriceMsg msg = new CustBasePriceMsg();
				msg.setCreated(new Date());
				msg.setTel(mobile);

				msg.setMsgContent(content);
				msg.setLastUpdated(new Date());
				msg.setStatus(status);
				//add by zhoucai@prcsteel.com 2016-9-14 记录客户基联系人相关信息
				msg.setAccountId(accoutId);
				msg.setContactId(contactId);
				msg.setAccountName(accoutName);
				msg.setContactName(contactName);
				if (custBasePriceMsgDao.insertSelective(msg) > 0) {
					release = true;
				} else {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增短信失败！");
				} 
			}
		}
		return release;
	}

	/**
	 * 根据基价ID列表更新日常资源
	 * @param basepriceIds 基价ID列表
     */
	private void syncResourceByBasePriceIds(List<Long> basepriceIds, User operator) {
		if (basepriceIds != null && basepriceIds.size() > 0) {
			SyncResourceQuery query = new SyncResourceQuery();
			query.setBasepriceIds(basepriceIds);
			query.setOperator(operator.getLoginId());
			custBasePriceDao.syncResourceByBasePriceIds(query);
		}
	}

	/**
	 * 同步到超市和行情圈,基础资源
	 * @param basepriceIds
     */
	private void syncBaseProduct(List<Long> basepriceIds, User operator){
		if (basepriceIds != null && basepriceIds.size() > 0) {
			List<ResourceDto> list = custBasePriceDao.queryRelateResourceByBasePriceIds(basepriceIds);
			List<Long> resourceIds = new ArrayList<>();
			if (list != null && list.size() > 0){
				List<ResouceForMarket> approvedList = new ArrayList<>();
				for (ResourceDto item : list) {
					if(resourceIds.contains(item.getId())){
						continue;
					}
					resourceIds.add(item.getId());

					if (!baseProductService.saveBaseProduct(item, operator)) {
						logger.info("基价发布同步到超市失败:" + item.getId());
					}

					BigDecimal initValue = new BigDecimal(99999);
					if (item.getPrice().compareTo(initValue) == 0) {
						continue;
					}
					ResouceForMarket resourceForMarket = new ResouceForMarket();
					resourceForMarket.setResouceId(item.getId().toString());
					resourceForMarket.setSortName("");
					resourceForMarket.setNsortName(item.getCategoryName());
					resourceForMarket.setProviceName("");
					resourceForMarket.setCityName(item.getCityName());
					resourceForMarket.setYieldly(item.getFactoryName());
					resourceForMarket.setMaterials(item.getMaterialName());
					resourceForMarket.setSpecs(item.getSpec());
					resourceForMarket.setPrice(item.getPrice().toString());
					resourceForMarket.setRemark(item.getRemark());
					resourceForMarket.setCreateById(operator.getId().toString());
					approvedList.add(resourceForMarket);
				}
				//插入基础资源
				if (resourceIds.size() > 0) {
					resourceService.batchInsertResourceBase(resourceIds, ResourceOperType.UPDATE, operator);
				}

				if (approvedList.size() > 0){
					try{
						resourceChangeForMaketService.send(approvedList, operator);
					}
					catch(Exception ex){
						logger.error("基价发布同步到行情圈失败");
					}
				}
			}
		}
	}

	/**
	 * 获取短信的bean
	 */
	private void getRestSendSmsServiceBean() {
		if(restSendSmsService == null){
			Object beanObj = this.applicationContext.getBean("restSendSmsService");
			if(beanObj != null){
				restSendSmsService = (RestSendSmsService) beanObj;
			}
		}
	}

	public List<CustBasePriceMsg> getHistoricalMsg(MsgQuery msgQuery) {
		return custBasePriceMsgDao.getHistoricalMsg(msgQuery);
	}

	@Override
	public int getHistoricalMsgCount(MsgQuery msgQuery) {
		return custBasePriceMsgDao.getHistoricalMsgCount(msgQuery);
	}

	public Integer getSingleBusinessResourceListTotal(ResourceBusinessQuery query) {
		return resourceDao.getSingleBusinessResourceListTotal(query);
	}
	/**
	 * 保存常用资源
	 * @param busiQuotaionCommonSearchDto
	 */
	public void saveCommonSearch(
			BusiQuotaionCommonSearchDto dto){
		//转换成保存到数据库的数据对象
		BusiQuotaionCommonSearch record = new BusiQuotaionCommonSearch();
		
		record.setCategoryName(dto.getCategoryName());
		record.setCategoryUuid(dto.getCategoryUuid());
		record.setCityId(dto.getCityId());
		record.setCityName(dto.getCityName());
		record.setMaterialName(dto.getMaterialName());
		record.setMaterialUuid(dto.getMaterialUuid());
		record.setCreated(dto.getCreated());
		record.setCreatedBy(dto.getCreatedBy());
		record.setSpc(dto.getSpc());
		record.setFactoryId(dto.getFactoryId());
		record.setFactoryName(dto.getFactoryName());
		
		// 查询是否有原来的数据
		List<BusiQuotaionCommonSearch> oldData = busiQuotaionCommonSearchDao
				.selectByAllCondition(dto);
		if (oldData != null && !oldData.isEmpty()) {
			BusiQuotaionCommonSearch data = oldData.get(0);
			record.setId(data.getId());
			busiQuotaionCommonSearchDao.updateByPrimaryKeySelective(record);
		}else{
			// 插入数据库
			busiQuotaionCommonSearchDao.insert(record);
		}
	}
	/**
	 * 删除常用资源
	 * @param busiQuotaionCommonSearchDto
	 */
	public void delCommonSearch(Long id){
		busiQuotaionCommonSearchDao.deleteByPrimaryKey(id);
	}
	
	/**
	 * 查询所有的常用资源
	 * 
	 */
	public List<BusiQuotaionCommonSearchDto> getCommonSearchAll(String loginId){
		BusiQuotaionCommonSearchDto dto = new BusiQuotaionCommonSearchDto();
		dto.setCreatedBy(loginId);
		List<BusiQuotaionCommonSearchDto> result = busiQuotaionCommonSearchDao.selectAllDataByUser(dto);
		
		return result;
	}
	
	@Override
	public List<ResourceBusinessDto> getSingleBusinessResourceList(ResourceBusinessQuery query) {
		List<ResourceBusinessDto> list = resourceDao.getSingleBusinessResourceList(query);
		Long now = Calendar.getInstance().getTime().getTime();
		int timeStr;
		int num = 60*1000;
		Map<Long,Double> lowPriceMap = new HashMap<Long, Double>();
		Double lowPrice;
		//设置同一个客户下资源最低价
		for(ResourceBusinessDto dto:list){
			if(dto.getLastUpdated() != null){
				timeStr = Integer.parseInt((now - dto.getLastUpdated().getTime())/num+"");
				dto.setTimeStr(timeStr+"");
			}
			//设置同一个客户下价格最低的资源
			if(dto.getPrice() == null){
				continue;
			}
			lowPrice = lowPriceMap.get(dto.getAccountId());
			if(lowPrice == null || dto.getPrice().doubleValue() < lowPrice){
				lowPrice = dto.getPrice().doubleValue() ;
				lowPriceMap.put(dto.getAccountId(),dto.getPrice().doubleValue());
			}
			dto.setLowPrice(lowPrice);
		}
		list.sort(new ResouceComparator());
		return list;
	}

	@Override
	public Integer getMultiBusinessResourceListTotal(ResourceBusinessQuery query) {
		return resourceDao.getMultiBusinessResourceListTotal(query);
	}

	@Override
	public List<ResourceBusinessDto> getMultiBusinessResourceList(ResourceBusinessQuery query) {
		List<ResourceBusinessDto> list = resourceDao.getMultiBusinessResourceList(query);
		Long now = Calendar.getInstance().getTime().getTime();
		int timeStr;
		int num = 60*1000;
		//计算当前时间和询价时间的时间差
		for(ResourceBusinessDto dto:list){
			if(dto.getLastUpdated() != null){
				timeStr = Integer.parseInt((now - dto.getLastUpdated().getTime())/num+"");
				dto.setTimeStr(timeStr+"");
			}
			query.getAccountIdList().add(dto.getAccountId());
		}
		//添加子项
		List<ResourceBusinessDto> allChildList = resourceDao.getMultiBusinessResourceChildList(query);
		Double totalWeight = 0.00;
		for(ResourceBusinessDto dto:list){
			dto.getChildList().addAll(getSortChildList(dto, query, allChildList));
			//计算总重量
			for(ResourceBusinessDto childDto:allChildList){
				if(childDto.getWeight() != null){
					totalWeight = totalWeight + childDto.getWeight().doubleValue();
				}
			}
			dto.setTotalWeight(totalWeight);
		}
		list.sort(new ResouceComparator());
		return list;
	}
	
	@Transactional
	@Override
	public ResourceBaseDto saveResource(String resourceDateList, User user) {
		if (resourceDateList != null) {
			com.prcsteel.platform.smartmatch.model.model.Resource resource = new com.prcsteel.platform.smartmatch.model.model.Resource();
			ResourceBaseDto resourceDto = (ResourceBaseDto) JSONObject.toBean(JSONObject.fromObject(resourceDateList), ResourceBaseDto.class);
			if(resourceDto.getAccountId()!=null){
				try {
					//modify by caosulin 如果前台没有传卖家名称则，重新查询一遍
					if(StringUtils.isBlank(resourceDto.getAccountName())){
						resource.setAccountName(((RestAccountService) this.getRestService("smart_restAccountService")).selectAccountById(resourceDto.getAccountId()).getName());
					} else{
						resource.setAccountName(resourceDto.getAccountName());
					}
				}
				catch (Exception ex){
					logger.error("保存资源客户不存在");
				}
			}
			//资源ID为空,表示前台没有搜索到符合条件的资源,直接新增一条
			if(resourceDto.getId() == null || resourceDto.getId() == 0){
				resource= saveInquiryResource(resourceDateList, user);
				// 保存到资源同步到超市
				resourceService.saveBaseProductByResource(resource,resourceDto.getSpec());
				try {
					BeanUtils.copyProperties(resourceDto,resource);  //复制的改变的属性到提到的dto
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return resourceDto;
			}
			Date now = Calendar.getInstance().getTime();
			resource.setPrice(resourceDto.getPrice());
			resource.setMaterialName(resourceDto.getMaterialName());
			resource.setFactoryName(resourceDto.getFactoryName());
			resource.setCityName(resourceDto.getCityName());
			resource.setMgtLastUpdated(now);
			resource.setMgtLastUpdatedBy(user.getLoginId());
			resource.setWarehouseName(resourceDto.getWarehouseName());
			resource.setWeightConcept(resourceDto.getWeightConcept());
			com.prcsteel.platform.smartmatch.model.model.Resource oldResource = resourceDao.selectByPrimaryKey(resourceDto.getId());
			Category cate = categoryService.queryByCategoryUuid(oldResource.getCategoryUuid());
			if (resourceDto.getPrice().compareTo(cate.getPriceMin()) == -1 || resourceDto.getPrice().compareTo(cate.getPriceMax()) == 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改后的价格不在该品种参考价范围内!");
			}
			//新增资源沿用以前的库存量和件数,状态
			resource.setWeight(oldResource.getWeight());
			resource.setQuantity(oldResource.getQuantity());
			resource.setStatus(ResourceStatus.APPROVED.getCode());
			resource.setSourceType(ResourceSourceType.INQUIRY.getCode());
			//材质名如果为空表示是修改资源单价后插入一条询价资源
			if (null == resourceDto.getMaterialName()) {
				resource.setMaterialName(oldResource.getMaterialName());
				resource.setFactoryName(oldResource.getFactoryName());
				resource.setCityName(oldResource.getCityName());
				resource.setCategoryUuid(oldResource.getCategoryUuid());
				resource.setCategoryName(oldResource.getCategoryName());
				resource.setMaterialUuid(oldResource.getMaterialUuid());
				resource.setFactoryId(oldResource.getFactoryId());
				resource.setAccountId(oldResource.getAccountId());
				resource.setFactoryName(oldResource.getFactoryName());
				resource.setUserIds(user.getId().toString());
				resource.setModificationNumber(0);
				//给resourceDto赋值查询该资源是否已经存在
				resourceDto.setAccountId(oldResource.getAccountId());
				resourceDto.setCategoryUuid(oldResource.getCategoryUuid());
				resourceDto.setCategoryName(oldResource.getCategoryName());
				resourceDto.setMaterialUuid(oldResource.getMaterialUuid());
				resourceDto.setFactoryId(oldResource.getFactoryId());
				resourceDto.setFactoryName(oldResource.getFactoryName());
			} else {
				if (StringUtils.isEmpty(resourceDto.getCategoryUuid())) {
					Category category = categoryDao.selectByCategory(resourceDto.getCategoryName());
					if (category == null) {
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的品名！");
					}
					resource.setCategoryUuid(category.getUuid());
					// add by caosulin 20160909 增加品名名称
					resource.setCategoryName(category.getName());
				} else {
					resource.setCategoryUuid(resourceDto.getCategoryUuid());
					// add by caosulin 20160909 增加品名名称
					resource.setCategoryName(resourceDto.getCategoryName());
				}
				if (StringUtils.isEmpty(resourceDto.getMaterialUuid())) {
					List<Materials> materials = materialsDao.selectByMaterialsName(resourceDto.getMaterialName());
					if (materials.isEmpty()) {
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的材质！");
					}
					resource.setMaterialUuid(materials.get(0).getUuid());//为防止在数据库存在两个一样的材质名，取其中一个
				} else {
					resource.setMaterialUuid(resourceDto.getMaterialUuid());
				}
				Factory factory = factoryDao.selectByName(resourceDto.getFactoryName());
				if (factory == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的钢厂！");
				}
				resource.setFactoryId(factory.getId());
			}
			//根据页面输入的仓库名称,别名查询仓库并设置id和名称
			if(StringUtils.isNotEmpty(resource.getWarehouseName())){
				Warehouse queryWarehouse = new Warehouse();
				queryWarehouse.setName(resource.getWarehouseName());
				List<Warehouse> warehouseList = warehouseDao.queryByName(queryWarehouse);
				if (warehouseList == null || warehouseList.size() == 0) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
				}
				resource.setWarehouseId(warehouseList.get(0).getId());
				resource.setWarehouseName(warehouseList.get(0).getName());
			}
			ResourceQuery rq = new ResourceQuery();
			rq.setAccountId(resource.getAccountId());
			rq.setWarehouseId(resource.getWarehouseId());
			rq.setCategoryUuid(resource.getCategoryUuid());
			rq.setMaterialUuid(resource.getMaterialUuid());
			rq.setFactoryId(resource.getFactoryId());
			rq.setSpec(resourceDto.getSpec());
			rq.setCityName(resource.getCityName());
			rq.setFactoryName(resource.getFactoryName());
			rq.setWarehouseName(resource.getWarehouseName());
			rq.setSourceType(ResourceSourceType.INQUIRY.getCode());
			List<ResourceDto> rList = resourceDao.selectByQueryForUpdate(rq);
			
			if (rList != null && !rList.isEmpty()) {// 如果存在则做更新
				resource.setId(resourceDto.getId());
				resource.setModificationNumber(resourceDao.selectByPrimaryKey(resource.getId()).getModificationNumber() + 1);
				//add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
				getResourceCity(resource);
				if (resourceDao.updateByPrimaryKeySelective(resource) == 0) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改资源信息失败！");
				} 
			} else {
				resource.setAccountId(resourceDto.getAccountId());
				resource.setCreated(now);
				resource.setCreatedBy(user.getName());
		
				resource.setMgtLastUpdated(now);
				resource.setMgtLastUpdatedBy(user.getLoginId());
				resource.setUserIds(user.getId().toString());
				resource.setModificationNumber(0);
				resource.setSourceType(ResourceSourceType.INQUIRY.getCode());
				resource.setStatus(ResourceStatus.APPROVED.getCode());
				//add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
				getResourceCity(resource);
				//modify by caosulin 如果前台没有传卖家名称则，重新查询一遍
				if(StringUtils.isBlank(resourceDto.getAccountName())){
					resource.setAccountName(((RestAccountService) this.getRestService("smart_restAccountService")).selectAccountById(resourceDto.getAccountId()).getName());
				} else{
					resource.setAccountName(resourceDto.getAccountName());
				}
				//判断当前资源是否是异常资源
				if(resource.getWarehouseId() == null || resource.getCityId() == null||resource.getFactoryId() == null){
					resource.setIsException(ResourceException.EXCEPTION.getCode());
				}
				if (resourceDao.insertSelective(resource) == 0) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增资源信息失败！");
				}
				String spec = resourceDto.getSpec();
				if (StringUtils.isNotEmpty(spec)) {
					saveResourceSpec(spec, user, resource.getId());
				}
			}
			// 保存到资源同步到超市
			resourceService.saveBaseProductByResource(resource,resourceDto.getSpec());
			try {
				BeanUtils.copyProperties(resourceDto,resource);  //复制的改变的属性到提到的dto
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return resourceDto;
		}
		else{
		return new ResourceBaseDto();}
	}

	/**
	 * 获取当前资源默认的城市
	 * @param resource
	 */
	private void getResourceCity(
			com.prcsteel.platform.smartmatch.model.model.Resource resource) {
//		//如果cityName和cityID为空，则重新查询一遍
//		if(resource.getCityId() == null && resource.getCityName() == null){
//			logger.info("----当前保存的资源cityName或者cityID为空，resourceID="+resource.getId()
//					+"cityid="+resource.getCityId()+"Name="+resource.getCityName());
//			if(resource.getWarehouseId() == null){
//				logger.info("---当前资源的仓库ID为空");
//				if(!StringUtils.isBlank(resource.getCityName())){//如果cityId为空，cityName 不为空
//					City city = cityDao.queryByCityName(resource.getCityName());
//					if(city != null){
//						resource.setCityId(city.getId());
//					}
//				}
//				return;
//			}
//			City city = resourceDao.getCityByWarehouseId(resource.getWarehouseId());
//			if(city != null){
//				resource.setCityId(city.getId());
//				resource.setCityName(city.getName());
//			}else{
//				logger.error("------没有找到当前的仓库"+resource.getWarehouseId()+"对应的城市----");
//			}
//		}
		if(StringUtils.isNotEmpty(resource.getCityName())){
			City city = cityDao.queryByCityName(resource.getCityName());
			resource.setCityId(city.getId());
		}
	}
	
	
	/**
	 * 新增询价资源
	 * @param resourceDateList
	 * @param user
	 * @return
	 */
	@Transactional
	@Override
	public com.prcsteel.platform.smartmatch.model.model.Resource saveInquiryResource(String resourceDateList, User user) {
		Date now = Calendar.getInstance().getTime();
		com.prcsteel.platform.smartmatch.model.model.Resource resource = new com.prcsteel.platform.smartmatch.model.model.Resource();
		ResourceBaseDto resourceDto = (ResourceBaseDto) JSONObject.toBean(JSONObject.fromObject(resourceDateList), ResourceBaseDto.class);
		resource.setPrice(resourceDto.getPrice());
		resource.setAccountId(resourceDto.getAccountId());
		resource.setMaterialName(resourceDto.getMaterialName());
		resource.setFactoryName(resourceDto.getFactoryName());
		resource.setCityName(resourceDto.getCityName());
		resource.setMgtLastUpdated(now);
		resource.setMgtLastUpdatedBy(user.getLoginId());
		resource.setWeight(new BigDecimal(1));
		resource.setQuantity(1);
		resource.setWeightConcept(resourceDto.getWeightConcept());
		resource.setSourceType(ResourceSourceType.INQUIRY.getCode());
		resource.setStatus(ResourceStatus.APPROVED.getCode());
		resource.setIsException(ResourceException.NORMAL.getCode());
		resource.setCreated(now);
		resource.setCreatedBy(user.getName());
		resource.setUserIds(user.getId().toString());
		resource.setModificationNumber(0);
		resource.setWarehouseName(resourceDto.getWarehouseName());
		// add by caosulin 20160912 增加账户名称
		if(resourceDto.getAccountId()!=null){
			try {
				//modify by caosulin 如果前台没有传卖家名称则，重新查询一遍
				if(StringUtils.isBlank(resourceDto.getAccountName())){
					resource.setAccountName(((RestAccountService) this.getRestService("smart_restAccountService")).selectAccountById(resourceDto.getAccountId()).getName());
				} else{
					resource.setAccountName(resourceDto.getAccountName());
				}
			}
			catch (Exception ex){
				logger.error("保存资源客户不存在");
			}
		}
		Category category = categoryDao.selectByCategory(resourceDto.getCategoryName());
		if (category == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的品名！");
		}
		resource.setCategoryUuid(category.getUuid());
		//add by caosulin 20160912 增加品名名称为空的判断。
		if(resource.getCategoryName() == null){
			resource.setCategoryName(category.getName());
		}

		List<Materials> materials = materialsDao.selectByMaterialsName(resourceDto.getMaterialName());
		if (materials.isEmpty()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的材质！");
		}
		resource.setMaterialUuid(materials.get(0).getUuid());//为防止在数据库存在两个一样的材质名，取其中一个
		Factory factory = factoryDao.selectByName(resourceDto.getFactoryName());
		if (factory == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的钢厂！");
		}
		resource.setFactoryId(factory.getId());
		//根据页面输入的仓库名称,别名查询仓库并设置id和名称
		if(StringUtils.isNotEmpty(resource.getWarehouseName())){
			Warehouse queryWarehouse = new Warehouse();
			queryWarehouse.setName(resource.getWarehouseName());
			List<Warehouse> warehouseList = warehouseDao.queryByName(queryWarehouse);
			if (warehouseList == null || warehouseList.size() == 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
			}
			resource.setWarehouseId(warehouseList.get(0).getId());
			resource.setWarehouseName(warehouseList.get(0).getName());
		}
		//add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
		getResourceCity(resource);
		if (resourceDao.insertSelective(resource) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增资源信息失败！");
		}
		saveResourceSpec(resourceDto.getSpec(), user, resource.getId());
		return resource;
	}
	
	/**
	 * 保存资源 规格
	 * @param spec
	 */
	private void saveResourceSpec(String spec,User user,Long resourceId){
		ResourceNorms norms; 
		if (StringUtils.isNotEmpty(spec)) {
			String [] specs = spec.split("\\*");
			for (int i = 0; i < specs.length; i++) {
				String uuid = String.valueOf(UUID.randomUUID());
				norms = new ResourceNorms();
				norms.setValue(specs[i]);
				norms.setCreatedBy(user.getLoginId());
				norms.setCreated(new Date());
				norms.setNormUuid(uuid);
				norms.setResourceId(resourceId);
				norms.setPriority(i+1);
				if (resourceNormsDao.insertSelective(norms) == 0) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增规格信息失败！");
				}
			}
		}
	}
	
	/**
	 * 根据搜索条件分组显示客户资源列表
	 * @param dto
	 * @param query
	 * @param allChildList
	 * @return
	 */
	private List<ResourceBusinessChildDto> getSortChildList(ResourceBusinessDto dto,ResourceBusinessQuery query,List<ResourceBusinessDto> allChildList){
		List<ResourceBusinessChildDto>  childList = new LinkedList<ResourceBusinessChildDto>();
		List<ResourceBusinessChildDto>  sortList;
		int showIndex = 1;
		Map<Long,Double> lowPriceMap = new HashMap<Long, Double>();
		Double lowPrice;
		ResourceBusinessChildDto newChildDtoDto = null;
		for(BusinessQueryData queryCon:query.getQueryDataList()){
			sortList = new LinkedList<ResourceBusinessChildDto>();
			for(ResourceBusinessDto childDto:allChildList){
				if(!dto.getAccountId().equals(childDto.getAccountId())){
					continue;
				}
				//设置同一个客户下资源最低价
				if(dto.getPrice() != null && childDto.getPrice() != null){
					lowPrice = lowPriceMap.get(dto.getAccountId());
					if(lowPrice == null || childDto.getPrice().doubleValue() < lowPrice){
						lowPrice = childDto.getPrice().doubleValue() ;
						lowPriceMap.put(dto.getAccountId(),childDto.getPrice().doubleValue());
					}
					dto.setLowPrice(lowPrice);
				}
				//当前资源符合查询条件i,归属到客户资源i组下
				if(isMatchResource(query.getSpecQueryFlag(),queryCon,childDto)){
					//分组序号同样的条件只显示一列
					if(sortList.size() <= 0){
						newChildDtoDto = new ResourceBusinessChildDto(showIndex+"",childDto.getId(), childDto.getAccountId(), childDto.getAccountName(),
								childDto.getCategoryName(),childDto.getMaterialName(), childDto.getSpec(),
								childDto.getFactoryName(), childDto.getCityName(), childDto.getWeightConcept(), childDto.getPrice(), childDto.getCategoryUuid(), childDto.getSingleWeight());
					}else{
						newChildDtoDto = new ResourceBusinessChildDto("",childDto.getId(), childDto.getAccountId(), childDto.getAccountName(),
								childDto.getCategoryName(),childDto.getMaterialName(), childDto.getSpec(),
								childDto.getFactoryName(), childDto.getCityName(), childDto.getWeightConcept(), childDto.getPrice(), childDto.getCategoryUuid(), childDto.getSingleWeight());
					}
					newChildDtoDto.setWarehouseName(childDto.getWarehouseName());
					sortList.add(newChildDtoDto);
				}
			}
			//没有符合条件i的记录,默认给一条空记录
			if(sortList.size() == 0){
				newChildDtoDto = new ResourceBusinessChildDto(showIndex+"",dto.getAccountId());
				sortList.add(newChildDtoDto);
				dto.setIsComplete(Boolean.FALSE);
			}
			childList.addAll(sortList);
			showIndex++;
		}
		return childList;
	}
	
	/**
	 * 匹配查询出来的数据归属于哪组搜索条件之下
	 * @param queryData
	 * @param dto
	 * @return
	 */
	private Boolean isMatchResource(int queryFlag,BusinessQueryData queryData,ResourceBusinessDto dto){
		//规格符合模糊搜索条件,//查询条件是区间查询,需要匹配区间
		if(StringUtils.isNotEmpty(queryData.getSpec())){
			if(StringUtils.isEmpty(dto.getSpec())){
				return false;
			}
			Double prefixSpec = -1D;
			Double suffixSpec = -1D;
			Double resultSpec = -1D;
			String [] spects = dto.getSpec().split("\\*");
			String compareSepc = "";
			//规格区间查询比较分组
			if(queryFlag > 0){
				//规格1比较
				if(spects.length > 0){
					compareSepc = spects[0];
					//区间查询
					if(queryData.getSpec1Flag() > 0){
						if(StringUtils.isNotEmpty(queryData.getPrefixSpec1()) && 
								StringUtils.isNotEmpty(queryData.getSuffixSpec1()) &&
								StringUtils.isNotEmpty(compareSepc)){
							try {
								prefixSpec = Double.parseDouble(queryData.getPrefixSpec1());
								suffixSpec = Double.parseDouble(queryData.getSuffixSpec1());
								resultSpec = Double.parseDouble(compareSepc);
								if(resultSpec < prefixSpec || resultSpec > suffixSpec){
									return false;
								}
							} catch (Exception e) {
								return false;
							}
						}
					}else{
						//规格1不包含区间,模糊匹配
						if(StringUtils.isEmpty(queryData.getPrefixSpec1())){
							if(dto.getSpec().indexOf(queryData.getSpec()) ==-1){
								return false;
							}
						}else{
							if(StringUtils.isNotEmpty(compareSepc)){
								if(!StringUtils.equals(queryData.getPrefixSpec1(),compareSepc)){
									return false;
								}
							}
						}
					}
				}
				//规格2比较
				if(spects.length > 1){
					compareSepc = spects[1];
					if(queryData.getSpec2Flag() > 0 ){
						if(StringUtils.isNotEmpty(queryData.getPrefixSpec2()) && 
								StringUtils.isNotEmpty(queryData.getSuffixSpec2()) &&
								StringUtils.isNotEmpty(compareSepc)){
							try {
								prefixSpec = Double.parseDouble(queryData.getPrefixSpec2());
								suffixSpec = Double.parseDouble(queryData.getSuffixSpec2());
								resultSpec = Double.parseDouble(compareSepc);
								if(resultSpec < prefixSpec || resultSpec > suffixSpec){
									return false;
								}
							} catch (Exception e) {
								return false;
							}
						}
					}else{
						if(StringUtils.isNotEmpty(compareSepc) && StringUtils.isNotEmpty(queryData.getPrefixSpec2())){
							if(!StringUtils.equals(queryData.getPrefixSpec2(),compareSepc)){
								return false;
							}
						}
					}
				}
				//规格2比较
				if(spects.length > 2){
					compareSepc = spects[2];
					if(queryData.getSpec3Flag() > 0 ){
						if(StringUtils.isNotEmpty(queryData.getPrefixSpec3()) && 
								StringUtils.isNotEmpty(queryData.getSuffixSpec3()) &&
								StringUtils.isNotEmpty(compareSepc)){
							try {
								prefixSpec = Double.parseDouble(queryData.getPrefixSpec3());
								suffixSpec = Double.parseDouble(queryData.getSuffixSpec3());
								resultSpec = Double.parseDouble(compareSepc);
								if(resultSpec < prefixSpec || resultSpec > suffixSpec){
									return false;
								}
							} catch (Exception e) {
								return false;
							}
						}
					}else{
						if(StringUtils.isNotEmpty(compareSepc) && StringUtils.isNotEmpty(queryData.getPrefixSpec3())){
							if(!StringUtils.equals(queryData.getPrefixSpec3(),compareSepc)){
								return false;
							}
						}
					}
				}				
			}else{
				if(dto.getSpec().indexOf(queryData.getSpec()) ==-1){
					return false;
				}
			}
		}
		//品名符合模糊搜索条件
		if(StringUtils.isNotEmpty(queryData.getCategoryName())){
			if(StringUtils.isEmpty(dto.getCategoryName())){
				return false;
			}
			if(dto.getCategoryName().indexOf(queryData.getCategoryName()) ==-1){
				return false;
			}
		}
		//材质符合搜索条件
		if(StringUtils.isNotEmpty(queryData.getMaterialName())){
			if(StringUtils.isEmpty(dto.getMaterialName())){
				return false;
			}
			if(!StringUtils.equals(queryData.getMaterialName(), dto.getMaterialName())){
				return false;
			}
		}
		//钢厂符合搜索条件
		if(StringUtils.isNotEmpty(queryData.getFactoryName())){
			if(StringUtils.isEmpty(dto.getFactoryName())){
				return false;
			}
			if(dto.getFactoryName().indexOf(queryData.getFactoryName()) ==-1){
				return false;
			}
		}
		//钢厂符合搜索条件
		if(StringUtils.isNotEmpty(queryData.getCityName())){
			if(StringUtils.isEmpty(dto.getCityName())){
				return false;
			}
			if(dto.getCityName().indexOf(queryData.getCityName()) ==-1){
				return false;
			}
		}
		return true;
	}


	@Override
	public List<BasePriceCustSubscribeInfo> queryCustSubscribInfo(List<Long> userList){
		return custBasePriceSubscriberDao.queryCustSubscribInfo(userList);
	}
	
	/**
	 * 单行搜索条件,资源排序
	 *
	 */
	public  class  ResouceComparator implements Comparator<ResourceBusinessDto>{

		protected  final String PAYLABLE = "1";//预付款
		
		protected final String SUPPLIERLABEL ="1";//白名单
		
		/**
		 * 白名单
		 */
		protected String supLab;
		
		protected String supLab1;
		
		/**
		 * 银票预付
		 */
		protected String payLab;
		
		protected String payLab1;
		
		/**
		 * 卖家相同的最低资源报价
		 */
		protected double lowPrice;
		
		protected double lowPrice1;
		
		@Override
		public int compare(ResourceBusinessDto dto, ResourceBusinessDto dto1) {
			getCompareContent(dto, dto1);
			return doCompare(dto, dto1);
		}
		
		private void getCompareContent(ResourceBusinessDto dto, ResourceBusinessDto dto1){
			payLab = dto.getPayMentLaybel();
			payLab1 = dto1.getPayMentLaybel();
			supLab = dto.getSupplierLabel();
			supLab1 = dto1.getSupplierLabel();
			lowPrice = dto.getLowPrice();
			lowPrice1 = dto1.getLowPrice();
		}
		private int doCompare(ResourceBusinessDto dto, ResourceBusinessDto dto1){
			//如果A卖家货物齐全,B卖家货物不齐全,位置排序
			if(dto.getIsComplete() && !dto1.getIsComplete()){
				return -1;
			}
			//如果A卖家货物不齐全,B卖家货物齐全,不排序
			if(!dto.getIsComplete() && dto1.getIsComplete()){
				return 0;
			}
			//如果A卖家是银票预付,B卖家不是银票预付,位置排序(前置判断不成立,表示客户货物齐全属性一致)
			if(PAYLABLE.equals(payLab) && !PAYLABLE.equals(payLab1)){
				return -1;
			}
			//如果A卖家不是银票预付,B卖家是银票预付,不排序
			if(!PAYLABLE.equals(payLab) && PAYLABLE.equals(payLab1)){
				return 0;
			}
			//如果A卖家是白名单,B卖家不是白名单,位置排序(前置判断不成立,表示客户A,B银票预付属性一致)
			if(SUPPLIERLABEL.equals(supLab) && !SUPPLIERLABEL.equals(supLab1)){
				return -1;
			}
			//如果A卖家不是白名单,B卖家是白名单,不排序
			if(!SUPPLIERLABEL.equals(supLab) && SUPPLIERLABEL.equals(supLab1)){
				return 0;
			}
			//A卖家价格低排序(前置判断不成立,表示客户A,B客户性质一致,资质一致)
			if(lowPrice < lowPrice1){
				return -1;
			}else if(lowPrice > lowPrice1){
				return 1;
			}
			return 0;
		}
	}

 }
