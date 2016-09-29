
package com.prcsteel.platform.smartmatch.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.service.impl.CommonCacheServiceImpl;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.CategoryNorms;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.model.model.Norms;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CategoryNormsDao;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.NormsDao;
import com.prcsteel.platform.core.service.MaterialsService;
import com.prcsteel.platform.order.model.enums.LostResourceSourceType;
import com.prcsteel.platform.order.model.enums.ResourceAdjustType;
import com.prcsteel.platform.smartmatch.api.RestAccountService;
import com.prcsteel.platform.smartmatch.model.dto.ExcelTempletDto;
import com.prcsteel.platform.smartmatch.model.dto.HistoryResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.LostResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBatchDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDailyDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDailyDtoResult;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceHistoryPriceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisTotal;
import com.prcsteel.platform.smartmatch.model.enums.ResourceException;
import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceSourceType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceStatus;
import com.prcsteel.platform.smartmatch.model.enums.WeightConceptType;
import com.prcsteel.platform.smartmatch.model.model.Factory;
import com.prcsteel.platform.smartmatch.model.model.ResourceBase;
import com.prcsteel.platform.smartmatch.model.model.ResourceNorms;
import com.prcsteel.platform.smartmatch.model.model.ResourceNormsBase;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.LostResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.OneKeyOprtResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceHistoryQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceStatisQuery;
import com.prcsteel.platform.smartmatch.model.query.RestHotResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestNormalResourceQuery;
import com.prcsteel.platform.smartmatch.persist.dao.AreaDao;
import com.prcsteel.platform.smartmatch.persist.dao.FactoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceBaseDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsBaseDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.persist.dao.WarehouseDao;
import com.prcsteel.platform.smartmatch.service.BaseProductService;
import com.prcsteel.platform.smartmatch.service.CreatePurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.ResourceService;
import com.prcsteel.platform.smartmatch.service.TempletUtils;

/**
 * Created by wucong on 2015/11/24.
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService, ApplicationContextAware {

	// 增加日志
	private Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
	@Resource
	UserService userService;
	@Resource
	private ResourceDao resourceDao;
	@Resource
	private ResourceBaseDao resourceBaseDao;
	@Resource
	private ResourceNormsBaseDao resourceNormsBaseDao;

	@Resource
	private FileService fileSerivce;

	@Resource
	private MaterialsService materialsService;
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private CategoryNormsDao categoryNormsDao;
	@Resource
	private ResourceNormsDao resourceNormsDao;
	@Resource
	private FactoryDao factoryDao;
	@Resource
	private WarehouseDao warehouseDao;
	@Resource
	private CreatePurchaseOrderService createPurchaseOrderService;
	@Resource
	private NormsDao normsDao;
	@Resource
	private AreaDao areaDao;
	@Resource
	BaseProductService baseProductService;
	@Resource
	CityDao cityDao;

	@Resource(name = "commonCacheServiceImpl")
	private CommonCacheServiceImpl cacheService;

	@Override
	public List<ResourceDto> selectResourceList(ResourceQuery resourceQuery) {
		return resourceDao.selectResourceList(resourceQuery);
	}

	// spring上下文
	private ApplicationContext applicationContext;

	/**
	 * 使用spring上下文获取Rest的SpringBEAN
	 *
	 * @param serviceName
	 * @return
	 */
	private Object getRestService(String serviceName) {
		Object bean = applicationContext.getBean(serviceName);
		if (bean != null) {
			return bean;
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取不到REST服务：" + serviceName);
		}
	}

	/**
	 * 查询资源总数
	 * 
	 * @param resourceQuery
	 * @return
	 */
	@Override
	public int countResource(ResourceQuery resourceQuery) {
		return resourceDao.countResource(resourceQuery);
	}

	@Override
	public List<String> selectAllWeightConcept() {
		return resourceDao.selectAllWeightConcept();
	}

	@Override
	public List<ResourceDto> selectByQueryForUpdate(ResourceQuery resourceQuery) {
		return resourceDao.selectByQueryForUpdate(resourceQuery);
	}

	/**
	 * 保存到资源产品表
	 * 
	 * @param resDto
	 */
	public void saveBaseProductByResourceDto(ResourceDto resDto, String resourceStatus) {
		// 判断数据的类型，如果是待审核的则不入库
		if (ResourceStatus.APPROVED.getCode().equals(resDto.getStatus())
				|| ResourceStatus.APPROVED.getCode().equals(resourceStatus)) {
			// 获取当前资源的规格
			if (resDto.getSpec() == null) {
				String spec = resourceDao.getResourceSpecByResourceId(resDto.getId());
				resDto.setSpec(spec);
			}

			if (resDto.getSpec() == null) {
				return;
			}

			User user = new User();
			user.setLoginId(resDto.getLastUpdatedBy());
			try {
				// if (null == resDto.getId()) {
				baseProductService.saveBaseProduct(resDto, user);
				// }
			} catch (Exception e) {
				// 保存到产品表失败
				logger.error("--------保存到产品表失败:accountName=" + resDto.getAccountName(), e);
			}
		}

	}

	/**
	 * 保存到资源产品表
	 * 
	 * @param resDto
	 */
	public void saveBaseProductByResource(com.prcsteel.platform.smartmatch.model.model.Resource resource, String spec) {
		ResourceDto resDto = new ResourceDto();
		try {
			BeanUtils.copyProperties(resDto, resource);
		} catch (Exception e) {
			// 保存到产品表失败
			logger.error("--------保存到产品表失败:accountName=" + resource.getAccountId(), e);
			resDto = null;
		}

		if (resDto != null) {
			resDto.setSpec(spec);
			saveBaseProductByResourceDto(resDto, resource.getStatus());
		}

	}

	@Override
	public int insertSelective(com.prcsteel.platform.smartmatch.model.model.Resource resource) {
		
		
		//add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
		getResourceCity(resource);

		int resu = resourceDao.insertSelective(resource);

		return resu;
	}

	/**
	 * 获取当前资源默认的城市
	 * 
	 * @param resource
	 */
	public void getResourceCity(com.prcsteel.platform.smartmatch.model.model.Resource resource) {
		// 如果cityName或者cityID为空，则重新查询一遍
		if (resource.getCityId() == null || resource.getCityName() == null) {
			logger.info("----当前保存的资源cityName或者cityID为空，resourceID=" + resource.getId() + ",cityid="
					+ resource.getCityId() + ",cityName=" + resource.getCityName());
			if (resource.getWarehouseId() == null) {
				logger.info("---当前资源的仓库ID为空");
				return;
			}
			City city = resourceDao.getCityByWarehouseId(resource.getWarehouseId());
			if (city != null) {
				resource.setCityId(city.getId());
				resource.setCityName(city.getName());
			} else {
				logger.error("------没有找到当前的仓库" + resource.getWarehouseId() + "对应的城市----");
			}
		}
	}

	/**
	 * 模版处理入口方法<br>
	 *
	 * <p>
	 * 1、校验模板列头正确性 : 默认第一行为列行
	 * </p>
	 *
	 * <p>
	 * 2、取模板数据进行匹配
	 * </p>
	 *
	 */
	@Transactional
	@Override
	public List<ExcelTempletDto> uploadTemplet(MultipartFile mFile, User user) {
		List<ExcelTempletDto> resultList = new ArrayList<ExcelTempletDto>();
		if (mFile == null) {
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "未找到上传文件,请联系管理员!");
		}
		try {
			// 03,07兼容处理
			Workbook wb = null;
			try {
				wb = new XSSFWorkbook(mFile.getInputStream());
			} catch (Exception ex) {
				wb = new HSSFWorkbook(mFile.getInputStream());
			}
			// 步骤1:校验模板列头正确性
			List<String> templetColumns = TempletUtils
					.getTempletColumns(fileSerivce.getFileData(Constant.EXCEL_TEMPLET_FILE_NAME));

			List<String> excelColumns = TempletUtils.getExcelTitleColumns(wb);
			if (!compare(templetColumns, excelColumns)) {
				throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "模板文件格式错误,请与管理员确认!");
			}
			// 步骤2:取模板数据进行匹配
			// 数据内容
			List<String> list = TempletUtils.getExcelContent(wb);

			// 当次上传数据行数不能超过1500条
			Long count = list.stream().filter(e -> StringUtils.isNotBlank(e)).count();
			if (count > 1500) {
				throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "资源文件数据不能超过1500条!");
			}

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).indexOf(Constant.EXCEL_TEMPLET_FIEL_CONTENT_SEPARATOR) >= 0) {
						String[] rows = list.get(i).split(Constant.EXCEL_TEMPLET_FIEL_CONTENT_SEPARATOR);
						ExcelTempletDto etDto = doRowsMatch(rows);
						resultList.add(etDto);
						// doInsert(etDto,user);
					}
				}
			}
		} catch (IOException e) {
			logger.error("模板文件无法解析：" + e.getMessage());
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "模板文件无法解析,请与管理员确认!");
		}
		return resultList;
	}

	/**
	 * 模版数据入库
	 * 
	 * @param etDto
	 */
	public void doInsert(ExcelTempletDto etDto, User user) {
		try {
			// 没有错误信息的数据入库
			if (StringUtils.isEmpty(etDto.getErrorMsg())) {
				ResourceDto resourceDto = new ResourceDto(null, Long.parseLong(etDto.getAccountId()),
						etDto.getAccountName(), etDto.getCategoryUuid(), etDto.getCategoryName(),
						etDto.getMaterialUuid(), etDto.getMaterialName(),
						StringUtils.join(etDto.getNormName(), Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR),
						(etDto.getFactoryId() == null ? null : Long.parseLong(etDto.getFactoryId())),
						etDto.getFactoryName(),
						(etDto.getWarehouseId() == null ? null : Long.parseLong(etDto.getWarehouseId())),
						etDto.getWarehouseName(),
						StringUtils.isBlank(etDto.getWeight()) ? null : new BigDecimal(etDto.getWeight()),
						etDto.getWeightConcept(),
						StringUtils.isBlank(etDto.getQuantity()) ? null : Integer.parseInt(etDto.getQuantity()),
						StringUtils.isBlank(etDto.getPrice()) ? null : new BigDecimal(etDto.getPrice()),
						etDto.getRemark(), etDto.getStatus(), ResourceSourceType.DAILY_COMMON.getCode(),
						user.getLoginId(), user.getId().toString());
				// add by caosulin@prcsteel.com 2016.6.2 增加异常字段处理
				resourceDto.setIsException(etDto.getIsException());
				resourceDto.setCityName(etDto.getCityName());
				doSaveTempletResource(resourceDto, user);
			}
		} catch (BusinessException bex) {
			logger.error("数据导入有误：" + bex.getMessage());
			bex.setMsg("数据导入有误，请重新导入或联系管理员!");
		}
	}

	/**
	 * 数据行匹配 <br>
	 * <p>
	 * 注:目前数据模板列固定,所以用数组指定列简单处理行数据 (后期如有时间再作优化)<br>
	 * </p>
	 * 
	 * @param rows
	 *            一行数据
	 */
	private ExcelTempletDto doRowsMatch(String[] rows) {
		ExcelTempletDto etd = new ExcelTempletDto();
		StringBuffer errorMsg = new StringBuffer();
		if (rows.length > 0) {
			// 第一列 卖家全称
			String accountName = rows[0];
			etd.setAccountName(accountName);
			String accountId = getColumnValue("id", "cust_account", "name", accountName);
			etd.setAccountId(accountId);
			if (StringUtils.isEmpty(accountId)) {
				errorMsg.append("卖家不存在;");
			}
			// 第二列 品名
			String categoryName = rows[1];
			etd.setCategoryName(categoryName);
			String categoryUuid = getColumnValue("uuid", "common_category", "name", categoryName);
			etd.setCategoryUuid(categoryUuid);
			/***
			 * 如果品名出错,则对下面的材质和规格不做过多的校验(不存在关联关系肯定是错的)
			 */
			if (StringUtils.isEmpty(categoryUuid)) {
				// 判断是否输入的是别名
				List<Map<String, Object>> category = getCategoryAliasValue(categoryName);
				String categoryUuid2 = null;
				for (Map<String, Object> a : category) {
					String aliasNames = a.get("alias_name") == null ? "" : a.get("alias_name").toString();
					String[] aliasNameArray = aliasNames.split(",");
					for (String aliasName : aliasNameArray) {
						if (aliasName != null && aliasName.equals(categoryName)) {
							categoryUuid2 = a.get("uuid") == null ? null : a.get("uuid").toString();
							break;
						}
					}
					if (categoryUuid2 != null) {
						break;
					}
				}
				if (categoryUuid2 == null) {
					errorMsg.append("品名不存在;");
				} else {
					etd.setCategoryUuid(categoryUuid2);
				}
			}
			// 第三列 材质
			String materialName = rows[2];
			etd.setMaterialName(materialName);
			if (!StringUtils.isEmpty(etd.getCategoryUuid())) {
				List<Materials> mList = materialsService.queryMaterials(etd.getCategoryUuid());
				String materialUuid = null;
				boolean isMaterialCheck = false;
				if (mList != null && mList.size() > 0) {
					long c = mList.stream().filter(e -> e.getName().equals(materialName)).count();
					if (c == 1) {
						isMaterialCheck = true;
						materialUuid = mList.stream().filter(e -> e.getName().equals(materialName)).findFirst().get()
								.getUuid();
					} else if (c > 1) {
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
								"品名：" + categoryName + "的材质" + materialName + " 设置重复,请联系管理员");
					}
				}
				if (isMaterialCheck) {
					etd.setMaterialUuid(materialUuid);
				} else {
					errorMsg.append("材质不存在;");
				}
			}
			// 第四列 规格 暂时先做简单个数校验,如有需要以后再做区间处理
			String normsName = rows[3];
			if (StringUtils.isBlank(normsName)) {
				errorMsg.append("规格不能为空;");
			} else {
				etd.setNormName(Arrays.asList(normsName.split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR)));
			}
			/**** 功能 #5396 批量导入时限制规格有几个 ***/
			// etd.setNormName(Arrays.asList(normsName.split("\\"+Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR)));
			// if(!StringUtils.isEmpty(categoryUuid)){
			// List<CategoryNorms>
			// cList=categoryNormsDao.getNormCombineByCategoryUuid(categoryUuid);
			// boolean isNormsCheck=false;
			// if(cList !=null && cList.size() >0){
			// if(cList.size()==
			// normsName.split("\\"+Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR).length){
			// isNormsCheck=true;
			// }
			// }
			// if(isNormsCheck){
			// etd.setNormUuidList(cList.stream().map(e->e.getNormsUuid()).collect(Collectors.toList()));
			// }else{
			// errorMsg.append("规格不存在;");
			// }
			// }
			// 第五列 厂家
			String factoryName = rows[4];
			etd.setFactoryName(factoryName);
			if (StringUtils.isBlank(factoryName)) {
				errorMsg.append("厂家不能为空;");
			}
			// 厂家不做处理
			// String
			// factoryId=getColumnValue("id","base_factory","name",factoryName);
			// etd.setFactoryId(factoryId);

			// 第六列 提货仓库
			String warehouseName = rows[5];
			etd.setWarehouseName(warehouseName);
			if (StringUtils.isBlank(warehouseName)) {
				errorMsg.append("仓库不能为空;");
			}
			// String
			// warehouseId=getColumnValue("id","base_warehouse","name",warehouseName);
			// etd.setWarehouseId(warehouseId);
			// //仓库不做处理
			//
			// 第七列 计重方式
			String weightConcept = rows[6];
			etd.setWeightConcept(weightConcept);
			if (Arrays.asList(WeightConceptType.values()).stream().filter(e -> weightConcept.equals(e.getName()))
					.count() <= 0) {
				errorMsg.append("计重方式不存在;");
			}
			// 第八列 件数(件)
			String quantity = rows[7];
			// 去掉小数点和后面的0
			if (StringUtils.isNotBlank(quantity)) {
				if (quantity.endsWith(".0")) {
					quantity = quantity.substring(0, quantity.length() - 2);
				}
			}
			etd.setQuantity(quantity);
			if (StringUtils.isNotBlank(quantity) && !StringUtils.isNumeric(quantity)) {
				errorMsg.append("件数必须为整数;");
			}

			// 第九列 总重(吨)
			String weight = rows[8];
			etd.setWeight(weight);
			// modify by caosulin 20160612去掉重量的校验，等于0的重量不报错，但是导入时过滤掉
			if (StringUtils.isNotBlank(weight) && !this.isNumeric(weight)) {
				errorMsg.append("总重不能小于等于0;");
			}

			// 第十列 单价(元/吨)
			String price = rows[9];
			etd.setPrice(price);
			if (!Tools.isNumeric(price)) {
				etd.setPrice("99999");
			}
			// 第十一列 备注
			String remark = rows[10];
			etd.setRemark(remark);

			etd.setErrorMsg(errorMsg.toString());
		}
		return etd;
	}

	/**
	 * 判断当前的重量是否大于0
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+|[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			if (Double.valueOf(str) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验单元格数据是否和数据库匹配,正确则返回期望字段数据
	 * 
	 * @param value
	 *            值
	 * @param tableName
	 *            数据表
	 * @param tableCoumn
	 *            数据表对应列
	 * @param expect
	 *            期望字段
	 * @return 期望字段数据
	 */
	private String getColumnValue(String value, String tableName, String tableCoumn, String expect) {
		return resourceDao.getColumnValue(value, tableName, tableCoumn, expect);
	}

	/**
	 * 通过别名来获取数据库值
	 * 
	 * @param value
	 * @param tableName
	 * @param tableCoumn
	 * @param expect
	 * @return
	 */
	private List<Map<String, Object>> getCategoryAliasValue(String aliasName) {
		List<Map<String, Object>> result = resourceDao.getDataByAlias(aliasName);
		return result;
	}

	/**
	 * 比较两个列表
	 * 
	 * @param originList
	 *            源list
	 * @param comparedList
	 *            对比的list
	 * @return
	 */
	private static boolean compare(List<String> originList, List<String> comparedList) {
		if (originList.size() != comparedList.size())
			return false;
		for (int i = 0; i < originList.size(); i++) {
			if (!originList.get(i).equals(comparedList.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getCommonData() {
		// 所有卖家
		Object accountRestBean = applicationContext.getBean("smart_restAccountService");
		List<AccountAllDto> accountList = null;
		if (accountRestBean != null) {
			RestAccountService restAccountService = (RestAccountService) accountRestBean;
			accountList = restAccountService.selectAllSellerAccount();
		}
		// 所有钢厂
		List<Factory> factoryList = factoryDao.getAllFactory();

		// 所有仓库
		List<Warehouse> warehouseList = warehouseDao.getAllWarehouse();

		// 所有品名
		List<Map<String, Object>> nsortList = createPurchaseOrderService.getSortAndNsort();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountList", accountList);
		map.put("factoryList", factoryList);
		map.put("warehouseList", warehouseList);
		map.put("nsortList", nsortList);
		return map;
	}

	/**
	 * 保存资源对象
	 * <p>
	 * 资源对象的保存，分为更新和插入; 首先要对对象的品名、材质、规格等参数进行检索判断后再做相应的操作
	 * </p>
	 *
	 * @param resourceDto
	 *            资源对象Dto
	 */
	@Transactional
	private void doSaveTempletResource(ResourceDto resourceDto, User user) {

		if (resourceDto == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "资源参数错误,请核实后再操作!");
		}
		/******* 校验规格长度取值 start *****/
		// 查询出规格
		List<CategoryNorms> list = categoryNormsDao
				.getNormCombineByCategoryUuid(resourceDto.getCategoryUuid().toString());
		// 规格uuid
		List<String> uuidList = list.stream().map(a -> a.getNormsUuid()).collect(Collectors.toList());
		// 规格名称
		String[] normsArrays = resourceDto.getSpec().split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		String searchSpec = "";
		if (uuidList.size() > normsArrays.length) {
			searchSpec = StringUtils.join(normsArrays, "*");
		} else {
			searchSpec = StringUtils.join(Arrays.copyOf(normsArrays, uuidList.size()), "*");
		}

		if (resourceDto.getCityId() == null) {
			if (resourceDto.getCityName() != null) {
				// 添加城市id
				City city = cityDao.queryByCityName(resourceDto.getCityName());
				if (city != null) {
					resourceDto.setCityId(city.getId());
				} else {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "城市名出错，请检查后重试!");
				}
			}
		}

		/****** end *****/

		ResourceQuery rq = new ResourceQuery();

		rq.setAccountId(resourceDto.getAccountId());
		rq.setWarehouseId(resourceDto.getWarehouseId());
		rq.setCategoryUuid(resourceDto.getCategoryUuid());
		rq.setMaterialUuid(resourceDto.getMaterialUuid());
		rq.setFactoryId(resourceDto.getFactoryId());
		rq.setSpec(searchSpec);
		rq.setFactoryName(resourceDto.getFactoryName());
		rq.setWarehouseName(resourceDto.getWarehouseName());
		rq.setSourceType(resourceDto.getSourceType());
		rq.setCityName(resourceDto.getCityName());
		rq.setWeightConcept(resourceDto.getWeightConcept());
		List<ResourceDto> rList = resourceDao.selectByQueryForUpdate(rq);
		// 日常导入资源，查询历史表获取昨天的历史价格，用于计算涨跌值
		ResourceHistoryPriceDto historyPriceDto = null;
		if (ResourceSourceType.DAILY_COMMON.getCode().equals(resourceDto.getSourceType())) {
			historyPriceDto = this.getHistoryPrice(rq);
		} // 如果存在则做更新
		if (rList != null && !rList.isEmpty()) {
			// 数据库里的用户id集
			String u_ids = rList.get(0).getUserIds();
			// 当前操作的用户
			String userId = resourceDto.getUserIds();
			// 不包含当前用户则添加
			long count = Arrays.asList(u_ids.split(",")).stream().filter(e -> e.equals(userId)).count();
			if (count <= 0) {
				u_ids += "," + userId;
			}

			com.prcsteel.platform.smartmatch.model.model.Resource resource = resourceDao
					.selectByPrimaryKey(rList.get(0).getId());
			resource.setCategoryName(resourceDto.getCategoryName());
			resource.setWeightConcept(resourceDto.getWeightConcept());
			resource.setQuantity(resourceDto.getQuantity() == null ? null : resourceDto.getQuantity());
			resource.setWeight(resourceDto.getWeight() == null ? null
					: resourceDto.getWeight().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
							BigDecimal.ROUND_HALF_UP));
			resource.setPrice(resourceDto.getPrice() == null ? null
					: resourceDto.getPrice().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
							BigDecimal.ROUND_HALF_UP));
			resource.setRemark(resourceDto.getRemark());
			resource.setStatus(resourceDto.getStatus());
			resource.setMgtLastUpdated(new Date());
			if (user != null) {
				resource.setMgtLastUpdatedBy(user.getLoginId());
			}
			resource.setUserIds(u_ids);
			resource.setWarehouseName(resourceDto.getWarehouseName());
			resource.setFactoryName(resourceDto.getFactoryName());
			// 计算涨跌值
			if (historyPriceDto != null) {
				BigDecimal priceChange1 = getPriceChange(historyPriceDto, resourceDto.getPrice());
				resource.setPriceChange(priceChange1);
			}else{
				resource.setPriceChange(null);
				
			}
			
			// 添加最近更新时间
			
			// modify by caosulin@prcsteel.com 2016.6.2修改异常字段
			resource.setIsException(resourceDto.getIsException());
			// 所在城市
			resource.setCityName(resourceDto.getCityName());
			resource.setCityId(resourceDto.getCityId());
			// 更新资源表
			// add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
			getResourceCity(resource);

			resourceDao.updateByPrimaryKeySelective(resource);
			insertResourceBase(resource, ResourceOperType.UPDATE, resourceDto);
			// 如果是已审核的数据择还要插入产品表
			if (ResourceStatus.APPROVED.getCode().equals(resource.getStatus())) {
				// 插入资源产品表
				this.saveBaseProductByResourceDto(resourceDto, resource.getStatus());
			}
		} else {
			com.prcsteel.platform.smartmatch.model.model.Resource resource = new com.prcsteel.platform.smartmatch.model.model.Resource(
					resourceDto.getAccountId(), null, resourceDto.getCategoryUuid(), resourceDto.getCategoryName(), resourceDto.getMaterialUuid(),
					resourceDto.getFactoryId(), resourceDto.getWarehouseId(), resourceDto.getWeightConcept(),
					resourceDto.getPrice() == null ? null
							: resourceDto.getPrice().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
									BigDecimal.ROUND_HALF_UP),
					resourceDto.getLastUpdatedBy(), null,
					resourceDto.getQuantity() == null ? null : resourceDto.getQuantity(),
					resourceDto.getWeight() == null ? null
							: resourceDto.getWeight().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
									BigDecimal.ROUND_HALF_UP),
					resourceDto.getRemark(), resourceDto.getSourceType(), resourceDto.getStatus(),
					resourceDto.getUserIds(), resourceDto.getFactoryName(), resourceDto.getWarehouseName(),
					resourceDto.getLastUpdatedBy(), new Date());
			try {
				RestAccountService service=(RestAccountService) this.getRestService("smart_restAccountService");
				resource.setAccountName(service.selectAccountById(resource.getAccountId()).getName());
			} catch (Exception ex) {
				logger.error("保存资源客户不存在");
			}
			resource.setCategoryName(categoryDao.queryByCategoryUuid(resource.getCategoryUuid()).getName());
			// modify by caosulin@prcsteel.com 2016.6.2修改异常字段
			resource.setIsException(resourceDto.getIsException());
			// 所在城市
			resource.setCityName(resourceDto.getCityName());
			// modify by zhoucai@prcsteel.com 2016-7-9 同步cityId
			resource.setCityId(resourceDto.getCityId());
		
			if (user != null) {
				resource.setMgtLastUpdatedBy(user.getLoginId());
			}
			resource.setMgtLastUpdated(new Date());
			resource.setMaterialName(resourceDto.getMaterialName());
			// add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
			getResourceCity(resource);
			// 计算涨跌值
			if (historyPriceDto != null) {
				BigDecimal priceChange1 = getPriceChange(historyPriceDto, resourceDto.getPrice());
				resource.setPriceChange(priceChange1);
			}
			// 插入资源表
			int resu = resourceDao.insertSelective(resource);
			if (resu == 1) {
				// 往基础资源表插入一条记录
				insertResourceBase(resource, ResourceOperType.INSERT, resourceDto);
				// 如果是已审核的数据择还要插入产品表
				if (ResourceStatus.APPROVED.getCode().equals(resource.getStatus())) {
					// 插入资源产品表
					this.saveBaseProductByResourceDto(resourceDto, resource.getStatus());
				}
			}
			resourceDto.setId(resource.getId());
			// 对资源规格表做插入操作
			doInsertNorms(resourceDto);
		}
	}

	/**
	 * 计算涨跌值
	 * 
	 * @param hitoryDto
	 * @param price
	 * @return
	 */
	private BigDecimal getPriceChange(ResourceHistoryPriceDto hitoryDto, BigDecimal price) {

		if (hitoryDto == null) {
			return null;
		}
		return price.subtract(hitoryDto.getPrice());
	}

	/**
	 * 获取历史价格
	 * 
	 * @return
	 */
	private ResourceHistoryPriceDto getHistoryPrice(ResourceQuery rq) {
		// 计算昨天的日期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

		ResourceHistoryQuery query = new ResourceHistoryQuery();
		query.setVersionDate(yesterday);
		// 卖家
		query.setAccountId(rq.getAccountId());
		// 仓库
		query.setWarehouseId(rq.getWarehouseId());
		query.setWarehouseName(rq.getWarehouseName());
		// 品名
		query.setCategoryUuid(rq.getCategoryUuid());
		// 材质
		query.setMaterialUuid(rq.getMaterialUuid());
		// 产地
		query.setFactoryId(rq.getFactoryId());
		query.setFactoryName(rq.getFactoryName());
		// 规格
		query.setSpec(rq.getSpec());
		// 计重方式
		query.setWeightConcept(rq.getWeightConcept());
		// 资源类型
		query.setSourceType(ResourceSourceType.DAILY_COMMON.getCode());
		
		List<ResourceHistoryPriceDto> prices = resourceDao.queryHistoryPrice(query);
		if (prices == null || prices.isEmpty()) {
			return null;
		}
		ResourceHistoryPriceDto dto = prices.get(0);

		return dto;
	}

	/**
	 * 改变全部资源的状态 : 挂牌-->未挂牌;未挂牌-->挂牌
	 * 
	 * @param oriStatus
	 *            原始状态值
	 * @param toStatus
	 *            目的状态值
	 * @param ids
	 *            资源id集
	 * @return
	 */
	@Override
	public void changeStatus(String oriStatus, String toStatus, List<Long> ids, List<Long> userIds, User user) {
		if (StringUtils.isEmpty(oriStatus) || StringUtils.isEmpty(toStatus)) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "状态值不能为空!");
		}
		resourceDao.changeStatus(oriStatus, toStatus, ids, userIds);
		// 更新操作批量插入到ResourceBase中 add by yjx
		batchInsertResourceBase(ids, ResourceOperType.UPDATE, user);
	}

	/**
	 * 资源列表批量更新: 批量改价格和批量改库存
	 * 
	 * @param rbd
	 */
	@Override
	public void batchUpdate(ResourceBatchDto rbd, User user) {
		if (rbd == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "批量修改参数不能为空!");
		}
		String adjustWay = rbd.getAdjustWay();
		switch (adjustWay) {
		case "设为指定值":
			rbd.setAdjustWay(ResourceAdjustType.APPOINT.toString());
			break;
		case "上调指定值":
			rbd.setAdjustWay(ResourceAdjustType.PLUS.toString());
			break;
		case "下调指定值":
			rbd.setAdjustWay(ResourceAdjustType.MINUS.toString());
			break;
		}
		resourceDao.batchUpdate(rbd);
		// 更新操作批量插入到ResourceBase中 add by yjx
		batchInsertResourceBase(rbd.getIds(), ResourceOperType.UPDATE, user);
	}

	/***
	 * 保存资源
	 */
	@Override
	public void doSaveResource(ResourceDto resDto, User user) {
		if (resDto == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "资源保存参数不能为空!");
		}
		Long id = resDto.getId();
		// 钢厂或仓库不存在则为异常数据
		if (resDto.getFactoryId() == null || resDto.getWarehouseId() == null) {
			// modify by caosulin@prcsteel.com 2016.6.3 修改异常状态字段
			resDto.setIsException(ResourceException.EXCEPTION.getCode());
		} else {
			resDto.setIsException(ResourceException.NORMAL.getCode());
		}
		// 添加默认所在城市
		if (resDto.getWarehouseId() != null) {
			String cityName = resourceDao.getCityNameByWarehouseId(resDto.getWarehouseId());
			resDto.setCityName(cityName);

			City city = cityDao.queryByCityName(resDto.getCityName());
			if (city != null) {
				resDto.setCityId(city.getId());
			}
		}
		if (id != null) {
			// 更新资源
			doUpateResource(resDto, user);
		} else {
			// 手动录入
			if (resDto.getSourceType() == null) {
				resDto.setSourceType(ResourceSourceType.DAILY_COMMON.getCode());
			}
			resDto.setStatus(ResourceStatus.DECLINED.getCode());
			// 插入资源
			doSaveTempletResource(resDto, user);
		}

	}

	/**
	 * 更新资源记录
	 * 
	 * @param resDto
	 */
	@Transactional
	public void doUpateResource(ResourceDto resDto, User user) {
		try {
			// 保存编辑之前的资源 add afeng
			List<Long> ids = new ArrayList<Long>();
			ids.add(resDto.getId());
			List<ResourceDto> resourceList = getResourceDto(ids);

			ResourceQuery rq = new ResourceQuery();

			rq.setAccountId(resDto.getAccountId());
			rq.setWarehouseId(resDto.getWarehouseId());
			rq.setCategoryUuid(resDto.getCategoryUuid());
			rq.setCategoryName(resDto.getCategoryName());
			rq.setMaterialUuid(resDto.getMaterialUuid());
			rq.setFactoryId(resDto.getFactoryId());
			rq.setSpec(resDto.getSpec());
			rq.setFactoryName(resDto.getFactoryName());
			rq.setWarehouseName(resDto.getWarehouseName());
			rq.setStatus(resDto.getStatus());
			rq.setSourceType(resDto.getSourceType());
			// rq.setId(resDto.getId());

			List<ResourceDto> rList = resourceDao.selectByQueryForUpdate(rq);

			/***
			 * 1、根据条件查询资源，如果结果集存在且数量大于1，抛出异常
			 * 2、当且仅当结果集数量为1，且该结果集不是其本身时，删除结果集资源和资源规格
			 * 3、更新当前资源数据，并对该资源对应的规格表做删除和重新插入
			 */
			if (rList != null && rList.size() > 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "查询参数不全,请核实后再做保存!");
			}
			if (rList != null) {
				if (rList.size() == 1 && !rList.get(0).getId().equals(resDto.getId())) {
					// 删除资源记录
					batchDelResourceByIds(Arrays.asList(rList.get(0).getId()));
				}
			}

			// 更新当前记录
			com.prcsteel.platform.smartmatch.model.model.Resource resource = resourceDao
					.selectByPrimaryKey(resDto.getId());
			resource.setAccountId(resDto.getAccountId());
			try {
				resource.setAccountName(((RestAccountService) this.getRestService("smart_restAccountService")).selectAccountById(resDto.getAccountId()).getName());
			} catch (Exception ex) {
				logger.error("保存资源客户不存在");
			}
			resource.setCategoryUuid(resDto.getCategoryUuid());
			try {
				resource.setCategoryName(categoryDao.queryByCategoryUuid(resDto.getCategoryUuid()).getName());
			} catch (Exception ex) {
				logger.error("品种名称不存在");
			}
			resource.setFactoryId(resDto.getFactoryId());
			resource.setMaterialUuid(resDto.getMaterialUuid());
			// resource.setSourceType(resDto.getSourceType());
			resource.setWarehouseId(resDto.getWarehouseId());
			resource.setWeightConcept(resDto.getWeightConcept());
			resource.setQuantity(resDto.getQuantity() == null ? null : resDto.getQuantity());
			resource.setWeight(resDto.getWeight() == null ? null
					: resDto.getWeight().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
			resource.setPrice(resDto.getPrice() == null ? null
					: resDto.getPrice().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
			resource.setRemark(resDto.getRemark());
			resource.setMgtLastUpdated(new Date());
			resource.setMgtLastUpdatedBy(user.getLoginId());
	
			resource.setFactoryName(resDto.getFactoryName());
			resource.setWarehouseName(resDto.getWarehouseName());
			resource.setCityName(resDto.getCityName());
			resource.setCityId(resDto.getCityId());

			// 导常数据转为正常数据 ,modified by yjx
			if (resDto.getFactoryId() != null && resDto.getWarehouseId() != null) {
				resource.setIsException(ResourceException.NORMAL.getCode());
			}

			if (resDto.getId() == null) {
				throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "未找到资源!");
			}
			// 数据库里的用户id集
			String u_ids = resourceDao.selectByPrimaryKey(resDto.getId()).getUserIds();
			// 当前操作的用户
			String userId = resDto.getUserIds();
			// 不包含当前用户则添加
			if (!Arrays.asList(u_ids.split(",")).contains(userId)) {
				u_ids += "," + userId;
			}
			resource.setUserIds(u_ids);

			// add by caosulin 如果城市ID或者name为空，根据仓库ID重新查一遍
			getResourceCity(resource);
			// 更新资源表
			resourceDao.updateByPrimaryKeySelective(resource);
			// 往基础资源表插入一条记录
			insertResourceBase(resource, ResourceOperType.INSERT, resDto);
			// add afeng 保存到新的资源表、资源价格表
			this.saveBaseProductByResourceDto(resDto, resource.getStatus());
			// 删除规格
			resourceNormsDao.deleteByResourceIds(Arrays.asList(resDto.getId()));

			doInsertNorms(resDto);
			if (null != resDto.getId()) {
				if (ResourceStatus.APPROVED.getCode().equals(resDto.getStatus())
						|| ResourceStatus.APPROVED.getCode().equals(resource.getStatus())) {
					if (!resourceList.isEmpty()) {
						// 编辑保存之前的资源看看resource表里是否还存在相同的，入存在则不删除，入不存在则删除
						baseProductService.deletProduct(resourceList);
					}
					// 再将编辑后的资源保存
					baseProductService.saveBaseProduct(resDto, user);
				}
			}
		} catch (Exception e) {
			logger.error("======================doUpateResource,更新资源记录，错误信息：=" + e.getMessage(), e);
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
		}
	}

	/**
	 * 对资源规格表做插入操作
	 * 
	 * @param resDto
	 */
	private void doInsertNorms(ResourceDto resDto) {
		// 插入资源规格表
		if (StringUtils.isEmpty(resDto.getSpec())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格参数不存在!");
		}
		if (StringUtils.isEmpty(resDto.getCategoryUuid())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid参数不存在!");
		}
		// //查询出规格
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(resDto.getCategoryUuid().toString());
		// 规格uuid
		List<String> uuidList = list.stream().map(a -> a.getNormsUuid()).collect(Collectors.toList());
		// 规格名称
		String[] normsArrays = resDto.getSpec().split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		// if(uuidList.size()!=normsArrays.length){
		// throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
		// "规格uuid和参数个数不对应!");
		// }
		if (uuidList != null && uuidList.size() > 0) {
			for (int k = 0; k < Math.min(uuidList.size(), normsArrays.length); k++) {
				ResourceNorms rn = new ResourceNorms(resDto.getId(), uuidList.get(k), normsArrays[k], (k + 1),
						new Date(), resDto.getLastUpdatedBy());
				resourceNormsDao.insertSelective(rn);

			}
		}
	}

	/**
	 * 根据id集批量删除资源,同时删除资源规格
	 * 
	 * @param ids
	 *            资源id集
	 */
	@Override
	public void batchDelResourceByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "资源ids集不能为空!");
		}
		List<ResourceDto> resourceList = getResourceDto(ids);
		resourceDao.batchDelResourceByIds(ids);
		resourceNormsDao.deleteByResourceIds(ids);
		baseProductService.deletProduct(resourceList);
	}

	private List<ResourceDto> getResourceDto(List<Long> ids) {
		List<ResourceDto> resourceList = new ArrayList<ResourceDto>();
		ids.forEach(resourceId -> {
			ResourceDto resourceDto = new ResourceDto();
			com.prcsteel.platform.smartmatch.model.model.Resource resource = resourceDao.selectByPrimaryKey(resourceId);
			String spec = resourceNormsDao.queryByResourceId(resourceId);
			resourceDto.setCategoryUuid(resource.getCategoryUuid());
			resourceDto.setCategoryName(resource.getCategoryName());
			resourceDto.setMaterialUuid(resource.getMaterialUuid());
			resourceDto.setFactoryId(resource.getFactoryId());
			resourceDto.setSpec(spec);
			resourceDto.setFactoryName(resource.getFactoryName());
			resourceDto.setWarehouseName(resource.getWarehouseName());
			resourceDto.setWarehouseId(resource.getWarehouseId());
			resourceDto.setWeightConcept(resource.getWeightConcept());
			resourceDto.setCityId(resource.getCityId());
			resourceList.add(resourceDto);
		});
		return resourceList;
	}

	/**
	 * 根据状态查询资源记录总数
	 */
	@Override
	public Map<String, Integer> selecCountResourceByStatus(List<Long> userIds) {
		return resourceDao.selecCountResourceByStatus(userIds);
	}

	/**
	 * 根据品名uuid获取规格
	 * 
	 * @param categoryUuid
	 *            品名uuid
	 * @return
	 */
	@Override
	public List<Norms> getNormsByCategoryUuid(String categoryUuid) {
		if (StringUtils.isEmpty(categoryUuid)) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid为空，无法查询规格!");
		}
		return normsDao.selectNormsByCategoryUUID(categoryUuid);
	}

	/**
	 * 导入资源数据
	 * 
	 * @param etDtoList
	 * @param user
	 */
	@Transactional
	@Override
	public void doImportResource(List<ExcelTempletDto> etDtoList, User user) {
		// addby caosulin 增加日志
		long time1 = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.debug("-----------------资源批量导入开始-------------------");
		}
		if (etDtoList == null || etDtoList.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("导入的数据为空!");
			}
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "导入的数据为空!");
		}
		try {
			List<ExcelTempletDto> tempList = new ArrayList<ExcelTempletDto>();
			for (int k = 0; k < etDtoList.size(); k++) {

				ExcelTempletDto etDto = etDtoList.get(k);
				// modify by caosulin 20160719 判断是否有同一样的资源，如果是一样的则不做处理了。
				if (isContainsResource(tempList, etDto)) {
					continue;
				} else {
					tempList.add(etDto);
				}
				// modify by caosulin 20160612去掉重量的校验，等于0的重量不报错，但是导入时过滤掉
				if ("0".equals(etDto.getWeight()) || "0.0".equals(etDto.getWeight()) || etDto.getWeight() == null
						|| etDto.getWeight().trim().equals("")) {
					if (logger.isDebugEnabled()) {
						logger.debug("---------当前这条资源 :卖家 " + etDto.getAccountName() + "的重量为0，不予导入！！");
					}
					continue;
				}
				/**
				 * 校验仓库和钢厂,如果不存在，则是导常数据
				 */
				Long warehouseId = resourceDao.getIdForWarehouseOrFactoryByNameOrAlias("base_warehouse",
						etDto.getWarehouseName());
				Long factoryId = resourceDao.getIdForWarehouseOrFactoryByNameOrAlias("base_factory",
						etDto.getFactoryName());
				if (warehouseId == null || factoryId == null) {
					// modify by caosulin@prcsteel.com 异常资源，状态改成英文
					etDto.setIsException(ResourceException.EXCEPTION.getCode());
				}
				// 添加默认所在城市
				if (warehouseId != null) {
					String cityName = resourceDao.getCityNameByWarehouseId(warehouseId);
					etDto.setCityName(cityName);
				}

				etDto.setWarehouseId(warehouseId == null ? null : warehouseId.toString());
				etDto.setFactoryId(factoryId == null ? null : factoryId.toString());

				// modify by caosulin@prcsteel.com 导入的资源默认都是待审核资源
				etDto.setStatus(ResourceStatus.DECLINED.getCode());
				doInsert(etDto, user);
			}
		} catch (BusinessException e) {
			logger.error("数据导入有误，请重新导入或联系管理员!", e);
			e.setMsg("数据导入有误，请重新导入或联系管理员!");
		}

		// addby caosulin 增加日志
		if (logger.isDebugEnabled()) {
			long usetime = System.currentTimeMillis() - time1;
			logger.debug("-----------------资源批量导入完成,耗时:" + usetime + "ms-------------------");
		}
	}

	/**
	 * 判断是否是已经处理过的资源
	 * 
	 * @param tempList
	 * @param etDto
	 * @return 如果是已经处理过的返回true，没处理过的则返回false
	 */
	private boolean isContainsResource(List<ExcelTempletDto> tempList, ExcelTempletDto etDto) {
		boolean result = false;
		String spec = StringUtils.join(etDto.getNormName(), Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		for (ExcelTempletDto dto : tempList) {
			String dto_spec = StringUtils.join(dto.getNormName(), Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
			// 判断卖家，品名，材质，规格，计重方式，钢厂，仓库是否一致
			if (etDto.getAccountId().equals(dto.getAccountId()) && etDto.getCategoryUuid().equals(dto.getCategoryUuid())
					&& etDto.getMaterialUuid().equals(dto.getMaterialUuid()) && dto_spec.equals(spec)
					&& etDto.getWeightConcept().equals(dto.getWeightConcept())
					&& etDto.getWarehouseName().equals(dto.getWarehouseName())
					&& etDto.getFactoryName().equals(dto.getFactoryName())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 刷新异常 1.获取刷新前异常资源ID列表1 2.获取刷新后异常资源ID列表2
	 * 3.对比列表1，列表2,在列表1中，不在列表2中的ID视为已更新，批量插入基础资源表
	 */
	@Override
	public void refreshException(User user) {
		// resourceDao.refreshException();
		// 异常资源列表
		List<com.prcsteel.platform.smartmatch.model.model.Resource> exceptionRess = resourceDao
				.selectExceptionResources();
		// 工厂列表
		List<Factory> fyList = factoryDao.getAllFactory();
		// 仓库列表
		List<Warehouse> whList = warehouseDao.getAllWarehouse();
		// 城市列表
		List<City> cityList = cityDao.getAllCity();
		List<com.prcsteel.platform.smartmatch.model.model.Resource> refleshList = refleshList(exceptionRess, fyList,
				whList, cityList, user);

		if (refleshList.size() > 0) {
			// 批量刷新异常
			resourceDao.batchUpdateExceptionRes(refleshList);
			// 刷新异常的资源更新同步到基础资源表
			List<Long> ids = new ArrayList<>();
			for (com.prcsteel.platform.smartmatch.model.model.Resource res : refleshList) {
				ids.add(res.getId());
			}
			batchInsertResourceBase(ids, ResourceOperType.UPDATE, user);
		}

	}

	private List<com.prcsteel.platform.smartmatch.model.model.Resource> refleshList(
			List<com.prcsteel.platform.smartmatch.model.model.Resource> exceptionRess, List<Factory> fyList,
			List<Warehouse> whList, List<City> cityList, User user) {
		List<com.prcsteel.platform.smartmatch.model.model.Resource> refleshList = new ArrayList<com.prcsteel.platform.smartmatch.model.model.Resource>();
		// 工厂名称，别名与工厂ID对应map
		HashMap<String, Long> factoryNameMap = new HashMap<String, Long>();
		HashMap<String, Long> factoryAlaisMap = new HashMap<String, Long>();
		for (Factory fa : fyList) {
			factoryNameMap.put(fa.getName(), fa.getId());
			if (!StringUtils.isEmpty(fa.getAlias())) {
				String[] aliasList = fa.getAlias().split(",");
				for (String a : aliasList) {
					factoryNameMap.put(a, fa.getId());
				}
			}
		}
		// 工厂名称，别名与工厂ID对应map
		HashMap<String, Long> wareHouseNameMap = new HashMap<String, Long>();
		HashMap<String, Long> wareHouseAlaisMap = new HashMap<String, Long>();
		for (Warehouse wa : whList) {
			wareHouseNameMap.put(wa.getName(), wa.getId());
			if (!StringUtils.isEmpty(wa.getAlias())) {
				String[] aliasList = wa.getAlias().split(",");
				for (String a : aliasList) {
					wareHouseAlaisMap.put(a, wa.getId());
				}
			}
		}

		// 仓库ID和城市做关联
		Map<String, City> wareHouseCityMap = new HashMap<String, City>();
		for (Warehouse wa : whList) {
			for (City c : cityList) {
				if (wa.getCityId().equals(c.getId())) {
					wareHouseCityMap.put(wa.getId() + "", c);
					break;
				}
			}
		}
		boolean needReflash = false;
		Date now = new Date();
		for (com.prcsteel.platform.smartmatch.model.model.Resource res : exceptionRess) {
			/**
			 * 1.工厂cust_resource中name, alais 工厂名与别名其中一个与 资源中的工厂名相符即可，
			 * 2仓库base_warehouse 中农name, alais 仓库名与别名其中一个与 的资源中的仓库名相符即可
			 * 3.1,2条件要同时满足
			 */
			if ((factoryNameMap.containsKey(res.getFactoryName()) || factoryAlaisMap.containsKey(res.getFactoryName()))
					&& (wareHouseNameMap.containsKey(res.getWarehouseName())
							|| wareHouseAlaisMap.containsKey(res.getWarehouseName()))) {
				needReflash = true;
			}
			if (needReflash) {
				// 设置工厂id
				if (factoryNameMap.get(res.getFactoryName()) != null) {
					res.setFactoryId(factoryNameMap.get(res.getFactoryName()));
				} else {
					res.setFactoryId(factoryAlaisMap.get(res.getFactoryName()));
				}
				// 设置仓库ID
				if (wareHouseNameMap.get(res.getWarehouseName()) != null) {
					res.setWarehouseId(wareHouseNameMap.get(res.getWarehouseName()));
				} else {
					res.setWarehouseId(wareHouseAlaisMap.get(res.getWarehouseName()));
				}
				// res.setIsException(ResourceException.NORMAL.getCode());
				res.setMgtLastUpdatedBy(user.getLoginId());
				res.setMgtLastUpdated(now);
				City city = wareHouseCityMap.get(res.getWarehouseId() + "");
				if (city != null) {
					res.setCityId(city.getId());
					res.setCityName(city.getName());
				}
				refleshList.add(res);
				needReflash = false;
			}
		}

		return refleshList;
	}

	/**
	 * 一键挂、撤牌操作
	 */
	@Override
	public void oneKeyOprt(OneKeyOprtResourceQuery okrq) {
		resourceDao.oneKeyOprt(okrq);
	}

	/**
	 * 获取历史卖家统计数据
	 * 
	 * @param dt
	 *            日期
	 * @param areaId
	 *            区域ID（区域ID为空时，查询全国的数据）
	 * @param rType
	 *            资源类型
	 * @return
	 */
	@Override
	public List<HistoryResourceDto> getHistorySellerStatisticData(String dt, String areaId, String rType) {
		if (StringUtils.isBlank(dt)) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "查询日期不能为空！");
		}
		// 根据日期和区域查询出所在版本的历史资源数据
		List<HistoryResourceDto> list = resourceDao.queryHistoryDataByDateAndAreaId(dt, areaId, rType);

		/**
		 * 计算所有卖家的资源条数 1、将所有卖家按名称组合 2、按名称组合分组并求各组数量
		 */
		// 卖家名称组合
		List<String> accountNameList = list.stream().map(e -> e.getAccountName()).collect(Collectors.toList());
		// 分组并计算数量
		Map<String, Long> accountCounted = accountNameList.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		/**
		 * 构建返回结果
		 */
		List<HistoryResourceDto> result = new ArrayList<HistoryResourceDto>();
		if (!accountCounted.isEmpty()) {
			Long no = 1l;
			for (Map.Entry<String, Long> s : accountCounted.entrySet()) {

				HistoryResourceDto hr = new HistoryResourceDto();
				hr.setNo(no);

				String accountName = s.getKey();
				hr.setAccountName(accountName);
				hr.setResourceCount(s.getValue());
				// 过滤当前卖家名称资源
				List<HistoryResourceDto> currAccountList = list.stream()
						.filter(e -> e.getAccountName().equals(accountName)).collect(Collectors.toList());

				// 当日正常更新条数
				hr.setDailyNormalUpdateCount(currAccountList.stream()
						.filter(n -> Tools.dateToStr(n.getMgtLastUpdated(), "yyyy-MM-dd").equals(dt)).count());
				// 当日询价更新条数
				hr.setDailyInqueryUpdateCount(currAccountList.stream()
						.filter(n -> Tools.dateToStr(n.getLastUpdated(), "yyyy-MM-dd").equals(dt)).count());
				no++;
				result.add(hr);
			}
		}
		return result;
	}

	/**
	 * 查询时间段内采购单物资明细不在当前挂牌资源中的缺失资源数据
	 */
	@Override
	public List<LostResourceDto> searchForLostResource(LostResourceQuery lostResourceQuery) {
		if (lostResourceQuery == null) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "查询对象不能为空");
		}
		List<LostResourceDto> list = resourceDao.searchForLostResource(lostResourceQuery);
		// 给列表添加来源
		list.stream().forEach(e -> e.setSourceType(LostResourceSourceType.PURCHASEORDER.getName()));
		return list;
	}

	/**
	 * 资源导出
	 * <p>
	 * 根据查询条件查询出资源数据写入excel，并返回excel文件流
	 * </p>
	 * 
	 * @param resourceQuery
	 *            资源查询对象
	 * @return 导出excel文件流
	 */
	public File exportResource(ResourceQuery resourceQuery) {
		if (resourceQuery == null) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "查询对象不能为空!");
		}
		InputStream ins = fileSerivce.getFileData(Constant.EXCEL_TEMPLET_FILE_NAME);
		if (ins == null) {
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "资源模板文件未找到!");
		}
		// 乱码问题，解决不了，先行手动转换一下
		resourceQuery = doEncodeQuery(resourceQuery);
		// 消除分页限制
		resourceQuery.setStart(0);
		resourceQuery.setLength(Integer.MAX_VALUE);
		// 数据源
		List<ResourceDto> list = selectResourceList(resourceQuery);
		// 去除最后一条统计记录
		list.remove(list.size() - 1);
		File file = null;
		// 数据不为空将数据插入模板文件
		OutputStream ops = null;
		if (!list.isEmpty()) {
			try {
				// 创建临时文件
				file = new File(Constant.EXCEL_TEMPLET_FILE_NAME);
				if (!file.exists()) {
					file.createNewFile();
				}
				ops = new FileOutputStream(file);
				// 构建excel
				HSSFWorkbook wb = new HSSFWorkbook(ins);
				doInsertExcel(wb, list);
				// excel写入临时文件
				wb.write(ops);

				// file.delete();
				// ins.close();
			} catch (IOException e) {
				throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "系统错误!");
			} finally {
				IOUtils.closeQuietly(ins);
				IOUtils.closeQuietly(ops);
			}
		}
		return file;
	}

	/**
	 * 对查询对象中文做编码转换
	 * 
	 * @param resourceQuery
	 * @return
	 */
	private ResourceQuery doEncodeQuery(ResourceQuery resourceQuery) {
		try {
			resourceQuery.setAccountName(new String(resourceQuery.getAccountName().getBytes("ISO8859-1"), "UTF-8"));
			resourceQuery.setArea(resourceQuery.getArea() != null
					? new String(resourceQuery.getArea().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setCategoryName(resourceQuery.getCategoryName() != null
					? new String(resourceQuery.getCategoryName().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setFactoryName(resourceQuery.getFactoryName() != null
					? new String(resourceQuery.getFactoryName().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setLastUpdatedBy(resourceQuery.getLastUpdatedBy() != null
					? new String(resourceQuery.getLastUpdatedBy().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setMaterialName(resourceQuery.getMaterialName() != null
					? new String(resourceQuery.getMaterialName().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setSpec(resourceQuery.getSpec() != null
					? new String(resourceQuery.getSpec().getBytes("ISO8859-1"), "UTF-8") : null);
			resourceQuery.setWarehouseName(resourceQuery.getWarehouseName() != null
					? new String(resourceQuery.getWarehouseName().getBytes("ISO8859-1"), "UTF-8") : null);
		} catch (UnsupportedEncodingException e) {
			logger.error("====================ResourceQuery方法报错，错误信息：" + e.getMessage());
		}
		return resourceQuery;
	}

	/**
	 * 数据写入模板文件
	 * 
	 * @param wb
	 * @param list
	 */
	private void doInsertExcel(HSSFWorkbook wb, List<ResourceDto> list) {
		HSSFSheet hSheet = wb.getSheetAt(0);
		// 第一行为列头行
		int startRow = 1;

		for (ResourceDto rDto : list) {
			HSSFRow hRow = hSheet.createRow(startRow);
			HSSFCell hCell = null;
			// 第一列 卖家
			hCell = hRow.createCell(0);
			hCell.setCellValue(rDto.getAccountName());
			// 第二列 品名
			hCell = hRow.createCell(1);
			hCell.setCellValue(rDto.getCategoryName());
			// 第三列 材质
			hCell = hRow.createCell(2);
			hCell.setCellValue(rDto.getMaterialName());
			// 第四列 规格
			hCell = hRow.createCell(3);
			hCell.setCellValue(rDto.getSpec());
			// 第五列 厂家
			hCell = hRow.createCell(4);
			hCell.setCellValue(rDto.getFactoryName());
			// 第六列 提货仓库
			hCell = hRow.createCell(5);
			hCell.setCellValue(rDto.getWarehouseName());
			// 第七列 计重方式
			hCell = hRow.createCell(6);
			hCell.setCellValue(rDto.getWeightConcept());
			// 第八列 件数
			hCell = hRow.createCell(7);
			hCell.setCellValue(rDto.getQuantity() == null ? "空" : String.valueOf(rDto.getQuantity()));
			// 第九列 总重
			hCell = hRow.createCell(8);
			hCell.setCellValue(rDto.getWeight() == null ? ""
					: String.valueOf(rDto.getWeight().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
							BigDecimal.ROUND_HALF_UP)));
			// 第十列 单价
			hCell = hRow.createCell(9);
			hCell.setCellValue(rDto.getPrice() == null ? ""
					: String.valueOf(rDto.getPrice().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,
							BigDecimal.ROUND_HALF_UP)));
			// 第十一列 备注
			hCell = hRow.createCell(10);
			hCell.setCellValue(rDto.getRemark());
			startRow++;
		}
	}

	@Override
	public ResourceStatisTotal queryStatisResTotal(ResourceStatisQuery statisQuery) {
		return resourceDao.queryStatisResTotal(statisQuery);
	}

	@Override
	public List<ResourceStatisDto> queryStatisRes(ResourceStatisQuery statisQuery) {
		return resourceDao.queryStatisRes(statisQuery);
	}

	/**
	 * 插入基础资源表，新增一条资源记录
	 * 
	 * @param resDto
	 */
	private void insertResourceBase(com.prcsteel.platform.smartmatch.model.model.Resource res,
			ResourceOperType operType, ResourceDto resDto) {
		if (ResourceStatus.DECLINED.getCode().equals(res.getStatus())) {
			// 只有已审核的才保存操作记录
			return;
		}
		ResourceBase base = null;
		try {
			base = new ResourceBase();
			BeanUtils.copyProperties(base, res);
		} catch (Exception e) {
			base = null;
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "填写的参数不正确，请核实后再做保存!");
		}

		base.setId(null);
		base.setCustResourceId(res.getId());
		base.setOperType(operType.getCode());
		base.setLastUpdated(res.getMgtLastUpdated());
		base.setLastUpdatedBy(res.getMgtLastUpdatedBy());
		resourceBaseDao.insertSelective(base);

		// 插入资源规格表
		if (StringUtils.isEmpty(resDto.getSpec())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格参数不存在!");
		}
		if (StringUtils.isEmpty(resDto.getCategoryUuid())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid参数不存在!");
		}
		// //查询出规格
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(resDto.getCategoryUuid().toString());
		// 规格uuid
		List<String> uuidList = list.stream().map(a -> a.getNormsUuid()).collect(Collectors.toList());
		// 规格名称
		String[] normsArrays = resDto.getSpec().split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		// if(uuidList.size()!=normsArrays.length){
		// throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
		// "规格uuid和参数个数不对应!");
		// }
		ResourceNormsBase rn = null;
		Date dt = new Date();
		if (uuidList != null && uuidList.size() > 0) {
			for (int k = 0; k < Math.min(uuidList.size(), normsArrays.length); k++) {
				rn = new ResourceNormsBase();
				rn.setResourceId(base.getId());
				rn.setNormUuid(uuidList.get(k));
				rn.setValue(normsArrays[k]);
				rn.setPriority((k + 1));
				rn.setCreated(dt);
				rn.setCreatedBy(resDto.getLastUpdatedBy());
				rn.setLastUpdated(dt);
				rn.setLastUpdatedBy(resDto.getLastUpdatedBy());
				resourceNormsBaseDao.insertSelective(rn);

			}
		}
	}

	/**
	 * 批量插入基础资源表: 批量审批等更新操作，批量导入等操作
	 * 
	 * @param ids
	 * @param operType
	 */
	@Override
	public void batchInsertResourceBase(List<Long> ids, ResourceOperType operType, User user) {
		List<com.prcsteel.platform.smartmatch.model.model.Resource> resources = resourceDao
				.betchQueryResourceByIds(ids);
		if (resources == null || resources.size() == 0) {
			return;
		}

		List<ResourceBase> baseResources = new ArrayList<ResourceBase>(resources.size());
		ResourceBase base = null;
		Date now = new Date();
		for (com.prcsteel.platform.smartmatch.model.model.Resource res : resources) {

			if (ResourceStatus.DECLINED.getCode().equals(res.getStatus())) {
				// 只有已审核的才保存操作记录
				continue;
			}
			base = new ResourceBase();
			try {
				BeanUtils.copyProperties(base, res);
			} catch (Exception e) {
				base = null;
			}
			if (base != null) {
				base.setId(null);
				base.setCustResourceId(res.getId());
				base.setOperType(operType.getCode());
				base.setMgtLastUpdated(now);
				base.setLastUpdated(now);
				baseResources.add(base);
			}
			// add by caosulin 刷新异常，批量改库存，批量审核时调用，插入基础产品表
			if (user != null) {
				base.setMgtLastUpdatedBy(user.getLoginId());
			}
			base.setMgtLastUpdated(now);
			saveBaseProductByResource(res, null);
			//add by zhoucai@prcsteel.com ,修改批量插入为循环插入 2016-8-28
			resourceBaseDao.insertSelective(base);
			//add by zhoucai@prcsteel.com ,审核时插入基础规格表cust_resource_norms_base 2016-8-28
			//查询规格
			String specs = resourceDao.getResourceSpecByResourceId(res.getId());
			//根据id查询norms
			List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(res.getCategoryUuid());
			List<String> uuidList = list.stream().map(a -> a.getNormsUuid()).collect(Collectors.toList());
			String[] spec = specs.split("\\" + Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
			Date dt = new Date();
			ResourceNormsBase resourceNorms = new ResourceNormsBase();
			resourceNorms.setCreatedBy(res.getCreatedBy());
			resourceNorms.setCreated(dt);
			resourceNorms.setLastUpdatedBy(res.getCreatedBy());
			resourceNorms.setLastUpdated(dt);
			resourceNorms.setResourceId(base.getId());
			for (int k = 0; k < spec.length; k++) {
				resourceNorms.setNormUuid(uuidList.get(k));
				resourceNorms.setValue(spec[k]);
				resourceNorms.setPriority(k + 1);
				resourceNormsBaseDao.insertSelective(resourceNorms);
			}

		}
		//if (baseResources.size() > 0) {
		//	resourceBaseDao.batchInsertResourceBase(baseResources);
		//}
	}

	/**
	 * 新增历史资源，数据从CBMS交易单过来
	 * 
	 * @param resDto
	 * @return
	 */
	public void saveHistoryResource(ResourceDto resDto, String userId) {
		if (resDto == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "调用失败，资源不能为空!");
		}
		/**
		 * 校验仓库和钢厂,如果不存在，则是导常数据
		 */
		Long warehouseId = resourceDao.getIdForWarehouseOrFactoryByNameOrAlias("base_warehouse",
				resDto.getWarehouseName());
		Long factoryId = resourceDao.getIdForWarehouseOrFactoryByNameOrAlias("base_factory", resDto.getFactoryName());
		resDto.setWarehouseId(warehouseId);
		resDto.setFactoryId(factoryId);
		if (warehouseId == null || factoryId == null) {
			// modify by caosulin@prcsteel.com 异常资源，状态改成英文
			resDto.setIsException(ResourceException.EXCEPTION.getCode());
		}
		// 添加默认所在城市
		if (resDto.getCityName() == null && warehouseId != null) {
			String cityName = resourceDao.getCityNameByWarehouseId(warehouseId);
			resDto.setCityName(cityName);
		}
		// 添加城市id
		City city = cityDao.queryByCityName(resDto.getCityName());
		if (city != null) {
			resDto.setCityId(city.getId());
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "城市名出错，请检查后重试!");
		}
		// 获取品名
		Category category = categoryDao.selectByCategory(resDto.getCategoryName());
		if (category != null) {
			if (resDto.getPrice().compareTo(category.getPriceMin()) < 0
					|| resDto.getPrice().compareTo(category.getPriceMax()) > 0) { // 不在参考价范围，不保存历史资源
				logger.info("资源不在参考价范围，不记入历史资源[价格:" + resDto.getPrice() + ",品名:" + resDto.getCategoryName() + "]");
				return;
			}
		}
		String categoryUuid = category == null ? "" : category.getUuid();
		resDto.setCategoryUuid(categoryUuid);
		// 材质UUID
		if (!StringUtils.isEmpty(categoryUuid)) {
			List<Materials> mList = materialsService.queryMaterials(categoryUuid);
			String materialUuid = null;
			boolean isMaterialCheck = false;
			if (mList != null && mList.size() > 0) {
				long c = mList.stream().filter(e -> e.getName().equals(resDto.getMaterialName())).count();
				if (c == 1) {
					isMaterialCheck = true;
					materialUuid = mList.stream().filter(e -> e.getName().equals(resDto.getMaterialName())).findFirst()
							.get().getUuid();
				} else if (c > 1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
							"品名：" + resDto.getCategoryName() + "的材质" + resDto.getMaterialName() + " 设置重复,请联系管理员");
				}
			}
			if (isMaterialCheck) {
				resDto.setMaterialUuid(materialUuid);
			} else {
				resDto.setMaterialUuid(null);
				// 没有材质，异常数据
				resDto.setIsException(ResourceException.EXCEPTION.getCode());
			}
		}
		resDto.setSourceType(ResourceSourceType.HISTORY_TRANSACTION.getCode());
		resDto.setStatus(ResourceStatus.APPROVED.getCode());
		doSaveTempletResource(resDto, userService.queryByLoginId(userId));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	@Override
	public List<com.prcsteel.platform.smartmatch.model.model.Resource> queryResourceDetailByIds(List<Long> ids) {
		return resourceDao.queryResourceDetailByIds(ids);
	}

	/**
	 * @decription:查询日常资源信息 add by zhoucai 2016-6-20
	 * @param resourceQuery
	 * @return Result
	 */
	@Override
	public List<ResourceDailyDto> queryDailyResource(ResourceQuery resourceQuery) {

		int start = resourceQuery.getStart();
		int length = resourceQuery.getLength();
		String cityName = resourceQuery.getCityName();
		resourceQuery.setCityName("");
		List<ResourceDailyDto> dailylist = new LinkedList<ResourceDailyDto>();
		List<ResourceDailyDto> returnlist = new LinkedList<ResourceDailyDto>();
		// 判断当前缓存是否有数据，有缓存当前所有的数据，没有将数据进行缓存 add by zhoucai 20160720
		Object resourceObj = cacheService.get(Constant.REST_DAILY_RESOURCE_CACHE_KEY);
		if (resourceObj == null || ((List) resourceObj).isEmpty()) {
			dailylist = removeRepeatRes(resourceDao.searchResourceList(resourceQuery));
			if (dailylist != null && !dailylist.isEmpty()) {
				cacheService.set(Constant.REST_DAILY_RESOURCE_CACHE_KEY,
						Constant.REST_DAILY_RESOURCE_CACHE_FAILURE_TIME, dailylist);
			}
		} else {
			dailylist = (List<ResourceDailyDto>) resourceObj;
		}
		// 对取回的资源进行排序.排序顺序：输入的城市，上海，其他
		if ((null != cityName) && (!cityName.equals(Constant.REST_DAILY_RESOURCE_CITY))) {
			// 将传输进来的城市进行优先

			for (int i = 0; i < dailylist.size(); i++) {
				ResourceDailyDto dailyDto = dailylist.get(i);
				if (null != dailyDto.getCityName() && dailyDto.getCityName().contains(cityName)) {
					returnlist.add(dailyDto);
					dailylist.remove(i);
					i--;
				}
			}
			// 取城市为上海的资源
			for (int i = 0; i < dailylist.size(); i++) {
				ResourceDailyDto dailyDto = dailylist.get(i);
				if (Constant.REST_DAILY_RESOURCE_CITY.equals(dailyDto.getCityName())) {
					returnlist.add(dailyDto);
					dailylist.remove(i);
					i--;
				}
			}
			// 将剩余的资源进行追加
			returnlist.addAll(dailylist);
		} else {
			for (int i = 0; i < dailylist.size(); i++) {
				ResourceDailyDto dailyDto = dailylist.get(i);
				if (Constant.REST_DAILY_RESOURCE_CITY.equals(dailyDto.getCityName())) {
					returnlist.add(dailyDto);
					dailylist.remove(i);
					i--;
				}
			}
			returnlist.addAll(dailylist);
		}
		// 根据分页位置返回对应的资源
		List<ResourceDailyDto> pagelist = new LinkedList<ResourceDailyDto>();
		// 获取返回分页资源循环的位置
		int pageLength = start + length;
		if (start + length > returnlist.size()) {
			pageLength = returnlist.size();
		}
		// 如果当前的起始位置大于当前结果集的位置，直接返回，否则循环取值
		if (!(start > returnlist.size())) {
			for (int i = start; i < pageLength; i++) {
				pagelist.add(returnlist.get(i));
			}
		}
		return pagelist;
	}

	/**
	 * 去除重复资源
	 * 
	 * @param resources
	 * @author yjx 2016.7.28
	 * @return
	 */
	private List<ResourceDailyDto> removeRepeatRes(List<ResourceDailyDtoResult> resources) {
		List<ResourceDailyDtoResult> fList = new ArrayList<ResourceDailyDtoResult>();
		if (resources == null || resources.size() < 2) {
			fList = resources;
		} else {
			ResourceDailyDtoResult resI, resJ;
			List<Integer> remove = new ArrayList<Integer>();// 重复资源下标
			for (int i = 0; i < resources.size() - 1; i++) {
				resI = resources.get(i);
				for (int j = resources.size() - 1; j > i; j--) {
					resJ = resources.get(j);
					/**
					 * 品名、材质、规格、所在城市、钢厂都相同的去重
					 */
					if ((resI.getCategoryUuid() != null && resI.getCategoryUuid().equals(resJ.getCategoryUuid()))
							&& (resI.getMaterialUuid() != null && resI.getMaterialUuid().equals(resJ.getMaterialUuid())
									&& (resI.getSpec() != null && resI.getSpec().equals(resJ.getSpec()))
									&& (resI.getFactoryId() != null && resI.getFactoryId().equals(resJ.getFactoryId()))
									&& (resI.getCityId() != null && resI.getCityId().equals(resJ.getCityId())))) {
						remove.add(j);// 将重复的放入移除列表
						if ((resI.getPrice() == null && resJ.getPrice() != null)
								|| (resI.getPrice() != null && resJ.getPrice() != null
										&& resI.getLastUpdated().getTime() < resJ.getLastUpdated().getTime())) {
							// 当前一个价格为空时设置后面一个价格，当前一个价格的最新更新时间比后一个价格的最新更新时间要早的时候也设置称呼后一个资源的价格
							resI.setPrice(resJ.getPrice());
						}
					}
				}
			}
			HashSet<Integer> h = new HashSet<Integer>(remove);
			if (h.size() > 0) {
				for (int i = 0; i < resources.size(); i++) {
					if (!h.contains(i)) {
						fList.add(resources.get(i));
					}
				}
			} else {
				fList = resources;
			}
		}
		List<ResourceDailyDto> dtoList = new ArrayList<ResourceDailyDto>();
		ResourceDailyDto dto = null;
		for (ResourceDailyDtoResult result : fList) {
			dto = new ResourceDailyDto();
			try {
				BeanUtils.copyProperties(dto, result);
			} catch (IllegalAccessException | InvocationTargetException e) {
			}
			dtoList.add(dto);
		}

		return dtoList;
	}

	/**
	 * Rest接口查询热门资源
	 *
	 *
	 * 1、没有搜索记录，直接指定SpecificCityName 客户端城市名称查询（无名称即全国）length条数据
	 *
	 * 1.1 如果查询到的记录数不足length，则取客户端城市名称的大哥城市(中心城市)作为采购城市，取length条数据
	 *
	 * 1.2 如果大哥城市数据也不足length条，取全国数据
	 *
	 *
	 * 2、有搜索记录： 一系列查询条件，品规材
	 *
	 * 2.1 查询结果少于length，再加上指定SpecificCityName 客户端城市名称查询的数据
	 *
	 * 2.2 如果还不够则加上全国的数据
	 *
	 *
	 *
	 * @param restHotResourceQuery
	 *            查询对象
	 * @return
	 * @author peanut
	 * @date 2016/6/22
	 * @see RestHotResourceQuery
	 */
	@Override
	public List<ResourceDto> selectRandomHostResource(RestHotResourceQuery restHotResourceQuery) {

		List<ResourceDto> list = null;
		List<ResourceDto> restlist;

		List<RestNormalResourceQuery> items = restHotResourceQuery.getItems();

		// 热门资源展示条数
		Integer length = restHotResourceQuery.getLength();
		// 客户端城市名称
		String specificCityName = restHotResourceQuery.getSpecificCityName();

		/**
		 * 根据查询条件中items是否为空判断是否有搜索记录
		 */
		if (items == null || items.isEmpty()) { // 没有搜索记录

			if (length == null || StringUtils.isBlank(specificCityName)) {
				logger.error("select hot resource error: length is null or specificCityName is empty!");
				// return null;
			}

			RestNormalResourceQuery restNormalResourceQuery = new RestNormalResourceQuery();
			// 热门资源
			restNormalResourceQuery.setType(Constant.REST_HOT_RESOURCE_TYPE);
			restNormalResourceQuery.setSpecificCityName(specificCityName);
			restNormalResourceQuery.setPageSize(length);
			restNormalResourceQuery.setPageIndex(Constant.DEFAULT_PAGE_INDEX);
			restNormalResourceQuery.preQuery();

			list = resourceDao.selectRestResource(restNormalResourceQuery);
			if (list != null) {
				// 删除最好一条统计数据
				list.remove(list.size() - 1);
			}
			list = mergeDuplicate(list);
			// 搜索结果少于需满足的条数--加上大哥城市的数据
			if (list != null && list.size() < length) {

				// 找到当前城市的中心城市
				List<Map<String, String>> centerCityList = areaDao.selectCenterCitysByName(specificCityName);
				if (centerCityList != null && !centerCityList.isEmpty()) {
					String purchaseCityIds = StringUtils.join(
							centerCityList.stream().map(e -> String.valueOf(e.get("id"))).collect(Collectors.toList()),
							",");
					// 当前城市的大哥（中心）城市作为采购城市
					restNormalResourceQuery.setPurchaseCityIds(purchaseCityIds);
				}
				// 无大哥城市则取全国
				restNormalResourceQuery.setSpecificCityName(null);

				restlist = resourceDao.selectRestResource(restNormalResourceQuery);

				if (restlist != null) {
					// 删除最后一条统计数据
					restlist.remove(restlist.size() - 1);
				}

				// 和大哥城市的资源合并去重
				list = mergeDuplicate(list, restlist);

				// 资源还不够---查全国
				if (list.size() < length) {
					// 取全国
					restNormalResourceQuery.setPurchaseCityIds(null);
					restNormalResourceQuery.setSpecificCityName(null);

					Object resourceObj = cacheService.get(Constant.REST_HOT_RESOURCE_CACHE_KEY);
					if (resourceObj == null) {
						restlist = resourceDao.selectRestResource(restNormalResourceQuery);
						if (restlist != null && !restlist.isEmpty()) {
							cacheService.set(Constant.REST_HOT_RESOURCE_CACHE_KEY,
									Constant.REST_HOT_RESOURCE_CACHE_FAILURE_TIME, restlist);
						}
					} else {
						restlist = (List<ResourceDto>) resourceObj;
					}

					if (restlist != null) {
						// 删除最后一条统计数据
						restlist.remove(restlist.size() - 1);
					}
					list = mergeDuplicate(list, restlist);
				}
			}

		} else { // 有搜索记录

			for (RestNormalResourceQuery norResourceQuery : items) {

				norResourceQuery.setType(Constant.REST_HOT_RESOURCE_TYPE);

				if (StringUtils.isBlank(norResourceQuery.getOrderBy())) {
					norResourceQuery.setOrderBy(Constant.DEFAULT_ORDERBY);
				}
				if (StringUtils.isBlank(norResourceQuery.getOrderWay())) {
					norResourceQuery.setOrderWay(Constant.DEFAULT_ORDER_WAY);
				}
				if (norResourceQuery.getPageIndex() == null) {
					norResourceQuery.setPageIndex(Constant.DEFAULT_PAGE_INDEX);
				}
				norResourceQuery.setPageSize(length);

				List<ResourceDto> itemList = resourceDao.selectRestResource(norResourceQuery);
				if (itemList != null) {
					// 删除最后一条统计数据
					itemList.remove(itemList.size() - 1);
				}
				if (itemList != null) {
					list = mergeDuplicate(list, itemList);

					if (list != null && list.size() >= length) {
						break;
					}
				}
			}

			// 如果搜索记录的资源不足，取其客户端城市资源
			if (list != null && list.size() < length) {
				RestNormalResourceQuery restNormalResourceQuery = new RestNormalResourceQuery();
				// 热门资源
				restNormalResourceQuery.setType(Constant.REST_HOT_RESOURCE_TYPE);
				restNormalResourceQuery.setSpecificCityName(specificCityName);
				restNormalResourceQuery.setPageSize(length);
				restNormalResourceQuery.setPageIndex(Constant.DEFAULT_PAGE_INDEX);
				restNormalResourceQuery.preQuery();

				restlist = resourceDao.selectRestResource(restNormalResourceQuery);
				if (restlist != null) {
					// 删除最后一条统计数据
					restlist.remove(restlist.size() - 1);
				}
				list = mergeDuplicate(list, restlist);

				// 如果还不够，找全国
				if (list.size() < length) {
					restNormalResourceQuery.setPurchaseCityIds(null);
					restNormalResourceQuery.setSpecificCityName(null);

					Object resourceObj = cacheService.get(Constant.REST_HOT_RESOURCE_CACHE_KEY);
					if (resourceObj == null) {
						restlist = resourceDao.selectRestResource(restNormalResourceQuery);
						if (restlist != null && !restlist.isEmpty()) {
							cacheService.set(Constant.REST_HOT_RESOURCE_CACHE_KEY,
									Constant.REST_HOT_RESOURCE_CACHE_FAILURE_TIME, restlist);
						}
					} else {
						restlist = (List<ResourceDto>) resourceObj;
					}

					if (restlist != null && restlist.size() >= 1) {
						// 删除最后一条统计数据
						restlist.remove(restlist.size() - 1);
					}
					list = mergeDuplicate(list, restlist);
				}
			}
		}

		if (list != null && list.size() > length) {
			list = list.subList(0, length);
		}

		return list;
	}

	/**
	 * 超市Rest接口查询普通资源
	 *
	 * @param restNormalResourceQuery
	 *            查询对象
	 * @return
	 * @author peanut
	 * @date 2016/6/23
	 * @see RestNormalResourceQuery
	 */
	@Override
	public List<ResourceDto> selectNormalResource(RestNormalResourceQuery restNormalResourceQuery) {
		if (restNormalResourceQuery == null) {
			logger.error("select rest resource error: restNormalResourceQuery is null!");
			return null;
		}
		restNormalResourceQuery.preQuery();
		return resourceDao.selectRestResource(restNormalResourceQuery);
	}

	/**
	 * 合并去重
	 *
	 * @param lists
	 * @return
	 */
	private List<ResourceDto> mergeDuplicate(List<ResourceDto>... lists) {
		LinkedHashSet<ResourceDto> resourceSet = new LinkedHashSet();
		for (int k = 0; k < lists.length; k++) {
			if (lists[k] != null) {
				resourceSet.addAll(lists[k]);
			}
		}
		return new LinkedList<>(resourceSet);
	}

	@Override
	public ResourceBaseDto selectById(Long resourceId) {
		return resourceDao.selectByResourceId(resourceId);
	}
}
