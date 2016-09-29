package com.prcsteel.platform.smartmatch.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.smartmatch.model.query.*;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.dto.CategoryAttributeDto;
import com.prcsteel.platform.core.model.dto.MaterialMgtDto;
import com.prcsteel.platform.core.model.model.CategoryAttribute;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.core.model.query.MaterialsMgtQuery;
import com.prcsteel.platform.smartmatch.api.RestUserService;
import com.prcsteel.platform.smartmatch.model.dto.ExcelTempletDto;
import com.prcsteel.platform.smartmatch.model.dto.LostResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBatchDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisTotal;
import com.prcsteel.platform.smartmatch.model.enums.AttributeType;
import com.prcsteel.platform.smartmatch.model.enums.ResourceStatus;
import com.prcsteel.platform.smartmatch.model.enums.WeightConceptType;
import com.prcsteel.platform.smartmatch.model.model.Attribute;
import com.prcsteel.platform.smartmatch.model.model.ResouceForMarket;
import com.prcsteel.platform.smartmatch.service.AreaService;
import com.prcsteel.platform.smartmatch.service.AttributeService;
import com.prcsteel.platform.smartmatch.service.CategoryAttributeService;
import com.prcsteel.platform.smartmatch.service.HistoryTransactionService;
import com.prcsteel.platform.smartmatch.service.MaterialsSerivice;
import com.prcsteel.platform.smartmatch.service.ReportResourceInventoryService;
import com.prcsteel.platform.smartmatch.service.ResourceBaseService;
import com.prcsteel.platform.smartmatch.service.ResourceChangeForMaketService;
import com.prcsteel.platform.smartmatch.service.ResourceService;

/**
 * Created by Green.Ge on 2015/11/17.
 */

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {
	@Resource
	private AttributeService attributeService;
	@Resource
	private CategoryAttributeService categoryAttributeService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private ResourceBaseService resourceBaseService;
	@Resource
	private MaterialsSerivice materialSerivice;
	@Resource
	private FileService fileService;
	@Resource
	private ReportResourceInventoryService reportResourceInventoryService;
	@Resource
	private AreaService areaService;
	@Resource
	private HistoryTransactionService historyTransactionService;
	@Resource
	private RestUserService restUserService;
	@Resource
	private ResourceChangeForMaketService resourceChangeForMaketService;
	@Resource
	private CategoryService categoryService;

	//增加日志
	private Logger logger = LoggerFactory.getLogger(ResourceController.class);

	// modify by zhoucai@prcsteel.com 获取acldomain 2016-5-18
	
	private final String LISTED_COUNT = "listedCount";
	private final String UN_LISTED_COUNT= "unListedCount";
	private final String ALL_COUNT="allCount";
	private final String EXCEPTION_COUNT="exceptionCount";
	private final String IN_QUERY_COUNT="inqueryCount";// 询价资源
	private final String HISTORY_TRANSACTION_COUNT="historyTransactionCount";
	private final String TAB_INDEX="tabIndex";
	private final String ATTR_TYPE_LIST = "attrTypeList";
	private final String ACL_DOMAIN = "aclDomain";
	
	@Value("${acl.domain}")
	private String aclDomain;

	// 大类设置首页
	@RequestMapping("/sort/index")
	public String sortList(ModelMap out, String tabIndex) {
		// 挂牌资源总数
		Map<String, Integer> map = resourceService.selecCountResourceByStatus(getUserIds());
		out.put(LISTED_COUNT, map.get(LISTED_COUNT));// 日常资源
		out.put(UN_LISTED_COUNT, map.get(UN_LISTED_COUNT));// 待审核
		out.put(ALL_COUNT, map.get(ALL_COUNT));// 全部资源
		out.put(IN_QUERY_COUNT, map.get(IN_QUERY_COUNT));// 询价资源
		out.put(EXCEPTION_COUNT, map.get(EXCEPTION_COUNT));// 异常资源
		out.put(HISTORY_TRANSACTION_COUNT, map.get(HISTORY_TRANSACTION_COUNT));// 历史成交
		out.put(TAB_INDEX, tabIndex);// tabindex
		return "resource/sort/list";
	}

	@RequestMapping("/search.html")
	@ResponseBody
	public PageResult search(ResourceQuery resourceQuery) {
		resourceQuery.setUserIds(getUserIds());
		List<ResourceDto> list = resourceService.selectResourceList(resourceQuery);
		PageResult result = new PageResult();
		Integer total = list.get(list.size() - 1).getTotal();
		list.remove(list.size() - 1);
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(total);
		return result;
	}

	/**
	 * 根据品名uuid获取规格
	 * 
	 * @param categoryUuid
	 * @return
	 */
	@RequestMapping("/getNorms")
	@ResponseBody
	public Result getNorms(@RequestParam("categoryUuid") String categoryUuid) {
		return new Result(resourceService.getNormsByCategoryUuid(categoryUuid));
	}

	/**
	 * 根据状态取得总数
	 * 
	 * @return
	 */
	@RequestMapping("/getCountByStatus")
	@ResponseBody
	public Result selecCountResourceByStatus() {
		return new Result(resourceService.selecCountResourceByStatus(getUserIds()));
	}

	/**
	 * 改变资源的状态 : 挂牌-->未挂牌;未挂牌-->挂牌
	 * 
	 * @param oriStatus
	 *            状态值
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public Result changeStatus(@RequestParam("oriStatus") String oriStatus, @RequestParam("toStatus") String toStatus,
			@RequestParam("ids") List<Long> ids) {
		Result result = new Result();
		try {
			String newOriStatus = oriStatus;
			String newToStatus = toStatus;
			// modify by caosulin 转换前台传递的状态为枚举值
			if ("0".equals(oriStatus)) {// 原状态
				newOriStatus = ResourceStatus.DECLINED.getCode();
			} else {
				newOriStatus = ResourceStatus.APPROVED.getCode();
			}
			if ("0".equals(toStatus)) {// 新状态
				newToStatus = ResourceStatus.DECLINED.getCode();
			} else {
				newToStatus = ResourceStatus.APPROVED.getCode();
			}
			User user = getLoginUser();
			resourceService.changeStatus(newOriStatus, newToStatus, ids, getUserIds(), user);
			result.setData("操作成功");

			if(newToStatus.equals(ResourceStatus.APPROVED.getCode())){  //审核资源
				sendToMarketByIds(ids, getLoginUser());
			}
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 一键挂、撤牌操作
	 * 
	 * @return
	 */
	@RequestMapping("/oneKeyOprt")
	@ResponseBody
	public Result oneKeyOprt(OneKeyOprtResourceQuery okrq) {
		Result result = new Result();
		try {
			//modify by caosulin 转换前台传递的状态为枚举值
			if("0".equals(okrq.getOriStatus())){//原状态
				okrq.setOriStatus(ResourceStatus.DECLINED.getCode());
			}else{
				okrq.setOriStatus(ResourceStatus.APPROVED.getCode());
			}
			if("0".equals(okrq.getToStatus())){//新状态
				okrq.setToStatus(ResourceStatus.DECLINED.getCode());
			}else{
				okrq.setToStatus(ResourceStatus.APPROVED.getCode());
			}
			List<com.prcsteel.platform.smartmatch.model.dto.ResourceDto> toBeApprovedList = null;
			if(okrq.getToStatus().equals(ResourceStatus.APPROVED.getCode())){
				toBeApprovedList=resourceService.selectResourceList(okrq);
				if (toBeApprovedList != null && !toBeApprovedList.isEmpty()) {
					toBeApprovedList.remove(toBeApprovedList.size() - 1);
				}
			}
			okrq.setUserIds(getUserIds());
			okrq.getStatus();
			resourceService.oneKeyOprt(okrq);
			result.setData("操作成功");
			if(okrq.getToStatus().equals(ResourceStatus.APPROVED.getCode())) {
				sendToMarketByList(toBeApprovedList, getLoginUser());
			}
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 刷新异常,
	 * 1.获取刷新前异常资源ID列表1
	 * 2.获取刷新后异常资源ID列表2
	 * 3.对比列表1，列表2,在列表1中，不在列表2中的ID视为已更新，批量插入基础资源表
	 * @return
	 */
	@RequestMapping("/refreshException")
	@ResponseBody
	public Result refreshException() {
		Result result = new Result();
		User user = getLoginUser();
		try {
			resourceService.refreshException(user);
			result.setData("操作成功");
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 批量删除资源
	 * 
	 * @param ids
	 *            资源id集
	 * @return
	 */
	@RequestMapping("/delResource")
	@ResponseBody
	public Result delResource(@RequestParam("ids") List<Long> ids) {
		Result result = new Result();
		try {
			resourceService.batchDelResourceByIds(ids);
			result.setData("操作成功");
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 资源列表批量更新: 批量改价格和批量改库存
	 * 
	 * @param rbd
	 * @return
	 */
	@RequestMapping("/batchUpdate")
	@ResponseBody
	public Result batchUpdate(ResourceBatchDto rbd) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			rbd.setLastUpdatedBy(this.getLoginUser().getLoginId());
			rbd.setLastUpdated(new Date());
			rbd.setMgtLastUpdatedBy(this.getLoginUser().getLoginId());
			rbd.setMgtLastUpdated(new Date());
			resourceService.batchUpdate(rbd, user);
			result.setData("操作成功");
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 保存资源对象
	 * 
	 * @param resourceDto
	 * @return
	 */
	@RequestMapping("/saveResource")
	@ResponseBody
	public Result doSaveResource(ResourceDto resourceDto) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			resourceDto.setLastUpdatedBy(this.getLoginUser().getLoginId());
			resourceDto.setUserIds(this.getLoginUser().getId().toString());
			resourceService.doSaveResource(resourceDto, user);
			result.setData("保存成功!");
		} catch (BusinessException e) {
			logger.error("===" + e.getMsg());
			result.setData(e.getMsg());
		}

		return result;
	}

	/**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * 
	 * @return
	 */
	@RequestMapping(value="getCommonData")
	@ResponseBody
	public Result getCommonData() {
		return new Result(resourceService.getCommonData());
	}

	/**************** excel资源模版处理******************** add by peanut 15-11-27 **/
	/**
	 * 模版首页
	 */
	@RequestMapping("/templet/index")
	public String index() {
		return "/resource/templet/index";
	}

	/**
	 * 下载标准模板
	 */
	@RequestMapping("/templet/download")
	public void downloadTemplet(HttpServletResponse response) {
		InputStream inStream = null;
		OutputStream ostream = null;
		try {
			// 设置文件类型
			String suffix = FileUtil.getFileSuffix(Constant.EXCEL_TEMPLET_FILE_NAME).toLowerCase();
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=download." + suffix);

			inStream = fileService.getFileData(Constant.EXCEL_TEMPLET_FILE_NAME);
			ostream = response.getOutputStream();
			// 流转化
			Streams.copy(inStream, ostream, true);
		} catch (IOException e) {
			logger.error("downloadTemplet下载模板报错："+e.getMessage());
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error("===="+e.getMessage());
				}
			}
		}
	}

	/**
	 * 上传模版
	 * 
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping(value = "/templet/upload", method = RequestMethod.POST)
	@ResponseBody
	public Result uploadTemplet(@RequestParam("uploadFile") MultipartFile uploadFile) {
		Result result = new Result();
		try {
			User user = this.getLoginUser();
			List<ExcelTempletDto> list = resourceService.uploadTemplet(uploadFile, user);
			result.setData(list);
		} catch (BusinessException e) {
			logger.error("==="+e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 导入正确的模版数据
	 * 
	 * @return
	 */
	@RequestMapping("/templet/importResource")
	@ResponseBody
	public Result importResource(@RequestBody List<ExcelTempletDto> etDtoList) {
		Result result = new Result();
		try {
			User user = this.getLoginUser();
			resourceService.doImportResource(etDtoList, user);
			result.setData("操作成功!");
		} catch (BusinessException e) {
			logger.error("操作失败"+e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**************** excel资源模版处理 end ********************/

	/**************** 扩展属性设置tab******************** add by peanut 15-11-24 **/
	// 扩展属性设置首页
	@RequestMapping("/attribute/index")
	public String attributeList(ModelMap out) {
		out.put(ATTR_TYPE_LIST, Arrays.asList(AttributeType.values()));
		out.put("allAttr", attributeService.getAllAttr());
		// modify by zhoucai@prcsteel.com 获取acldomain 大类设置跳转acl系统 2016-5-18
		out.put(ACL_DOMAIN, aclDomain);
		return "resource/attribute/list";
	}

	/**
	 * 扩展属性设置tab 加载列表数据
	 * 
	 * @return
	 */
	@RequestMapping("/attribute/search")
	@ResponseBody
	public PageResult searchAttribute(CategoryAttributeQuery caq) {

		Integer total = categoryAttributeService.searchTotalByCategoryNameOrGroupName(caq);

		List<CategoryAttributeDto> list = categoryAttributeService.searchByCategoryNameOrGroupName(caq);

		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 扩展属性设置tab 取所有属性
	 * 
	 * @return
	 */
	@RequestMapping("/getAllAttribute")
	@ResponseBody
	public Result getAllAttr() {
		return new Result(attributeService.getAllAttr());
	}

	/**
	 * 保存属性设置对象
	 * 
	 * @return
	 */
	@RequestMapping("/attribute/save")
	@ResponseBody
	public Result saveAttribute(@RequestParam("categoryUuid") String categoryUuid,
			@RequestParam(value = "attributeUuid[]", required = false) List<String> attributeUuid) {
		Result result = new Result();
		try {
			List<CategoryAttribute> list = categoryAttributeService.findByUuid(categoryUuid);
			if (list != null && !list.isEmpty()) {
				categoryAttributeService.updateMultAttributUuid(list, attributeUuid, this.getLoginUser().getLoginId());
				result.setData("更新成功");
			} else {
				categoryAttributeService.addMultCategoryAttributes(categoryUuid, attributeUuid,
						this.getLoginUser().getLoginId());
				result.setData("新增成功");
			}
		} catch (Exception e) {
			logger.error("失败：" + e.getMessage());
			result.setSuccess(false);
			result.setData(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据品名uuid获取数据
	 * 
	 * @param categoryUuid
	 * @return
	 */
	@RequestMapping("/attribute/getByUuid")
	@ResponseBody
	public Result getCategoryAttributeByUuid(@RequestParam("categoryUuid") String categoryUuid) {
		return new Result(categoryAttributeService.searchByCategoryUuid(categoryUuid));
	}

	/**
	 * 根据品名uuid删除数据
	 * 
	 * @param categoryUuid
	 *            品名uuid
	 * @return
	 */
	@RequestMapping("/attribute/delete")
	@ResponseBody
	public Result delCategoryAttributeByUuid(@RequestParam("categoryUuid") String categoryUuid) {
		Result result = new Result();
		try {
			categoryAttributeService.delCategoryAttributeByUuid(categoryUuid);
			result.setData("删除成功");
		} catch (Exception e) {
			logger.error("删除失败：" + e.getMessage());
			result.setSuccess(false);
			result.setData("删除失败");
		}
		return result;
	}
	/**************** 扩展属性设置tab end ********************/

	/****************
	 * 扩展属性编辑tab******************** add by peanut 15-11-24/ /** 扩展属性编辑tab 首页
	 * 
	 * @param out
	 * @return
	 */
	@RequestMapping("/attribute/edit/index")
	public String attributeEditList(ModelMap out) {
		out.put(ATTR_TYPE_LIST, Arrays.asList(AttributeType.values()));
		// modify by zhoucai@prcsteel.com 获取acldomain 大类设置跳转acl系统 2016-5-18
		out.put(ACL_DOMAIN, aclDomain);
		return "resource/attribute/edit/index";
	}

	/**
	 * 新增跳转
	 * 
	 * @param out
	 * @return
	 */
	@RequestMapping("/attribute/edit/create")
	public void goToaddAttributeEdit(ModelMap out) {
		out.put(ATTR_TYPE_LIST, Arrays.asList(AttributeType.values()));
	}

	/**
	 * 扩展属性编辑tab 记录添加
	 * 
	 * @param attr
	 * @return
	 */
	@RequestMapping("/attribute/edit/add")
	@ResponseBody
	public Result addAttributeEdit(Attribute attr) {

		Result result = new Result();
		if (StringUtils.isEmpty(attr.getUuid())) {
			attr.setUuid(UUID.randomUUID().toString());
		}
		String loginId = this.getLoginUser().getLoginId();
		attr.setLastUpdatedBy(loginId);
		attr.setCreatedBy(loginId);
		attr.setCreated(new Date());
		attr.setLastUpdated(new Date());

		try {
			attributeService.addAttributeEdit(attr);
			result.setData("添加成功");
		} catch (Exception e) {
			logger.error("添加失败：" + e.getMessage());
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * 扩展属性编辑tab 记录修改
	 * 
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/attribute/edit/modify", method = RequestMethod.POST)
	@ResponseBody
	public Result modifyAttributeEdit(Attribute attr) {

		String loginId = this.getLoginUser().getLoginId();
		attr.setLastUpdated(new Date());
		attr.setLastUpdatedBy(loginId);

		Result result = new Result();
		try {
			attributeService.updateAttribute(attr);
			result.setData("属性更新成功");
		} catch (Exception e) {
			logger.error("属性更新失败：" + e.getMessage());
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}

		return result;
	}

	/**
	 * 扩展属性编辑tab 记录删除
	 * 
	 * @param id
	 *            属性表主健id
	 * @return
	 */
	@RequestMapping(value = "/attribute/edit/{id}/del", method = RequestMethod.POST)
	@ResponseBody
	public Result delAttributeEdit(@PathVariable(value = "id") Long id) {

		Result result = new Result();
		try {
			attributeService.deleteByPrimaryKey(id);
			result.setData("属性删除成功");
		} catch (Exception e) {
			logger.error("属性删除失败：" + e.getMessage());
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}

	/**
	 * 扩展属性编辑tab 检查属性名称是否已被使用
	 * 
	 * @param id
	 *            属性id
	 * @param name
	 *            属性名称
	 * @return
	 */
	@RequestMapping("/attribute/edit/checkAttrName")
	@ResponseBody
	public Result checkAttrName(@RequestParam("id") Long id, @RequestParam("name") String name) {

		Result result = new Result();

		Attribute attribute = attributeService.findByNameBesidesId(id, name);
		if (attribute != null) {
			result.setData("已有该属性名称!");
		} else {
			result.setSuccess(Boolean.FALSE);
		}
		return result;
	}

	/**
	 * 扩展属性编辑tab 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/attribute/edit/{id}/detail")
	public String getAttributeById(ModelMap out, @PathVariable("id") Long id) {

		Attribute attribute = attributeService.findById(id);
		out.put("attribute", attribute);

		out.put(ATTR_TYPE_LIST, Arrays.asList(AttributeType.values()));
		return "resource/attribute/edit/create";
	}

	/**
	 * 扩展属性编辑tab 加载列表
	 * 
	 * @param attrName
	 *            属性名称
	 * @param attrType
	 *            属性类型
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/attribute/edit/searchAttr")
	@ResponseBody
	public PageResult searchAttr(@RequestParam("attrName") String attrName, @RequestParam("attrType") String attrType,
			@RequestParam("start") Integer start, @RequestParam("length") Integer length) {

		// 请求参数封装
		AttributeEditQuery attributeEditQuery = new AttributeEditQuery();
		attributeEditQuery.setLength(length);
		attributeEditQuery.setName(attrName);
		attributeEditQuery.setStart(start);
		attributeEditQuery.setType(attrType);

		Integer total = attributeService.totalAttribute(attributeEditQuery);

		List<Attribute> list = attributeService.selectByNameAndType(attributeEditQuery);
		for (Attribute attr : list) {
			attr.setType(AttributeType.getCnName(attr.getType()));
		}
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());

		return result;
	}
	/**************** 扩展属性编辑tab end ********************/

	/**************** 材质管理tab******************** add by peanut 2016-1-13 */
	/**
	 * 材质管理首页
	 */
	@RequestMapping("/materials/index")
	public void indexForMaterialsMgt(ModelMap out) {
		// modify by zhoucai@prcsteel.com 获取acldomain 大类设置跳转acl系统 2016-5-18
		out.put(ACL_DOMAIN, aclDomain);
	}

	/**
	 * 材质管理新增中转
	 */
	@RequestMapping("/materials/create")
	public String createForMaterialsMgt() {
		return "/resource/materials/create";
	}

	/**
	 * 材质管理列表搜索
	 * 
	 * @param mmq
	 * @return
	 */
	@RequestMapping("/materials/search")
	@ResponseBody
	public PageResult searchForMaterialMgt(MaterialsMgtQuery mmq) {
		mmq.setIsVague("true");
		Integer total = materialSerivice.totalForMaterialMgt(mmq);
		List<MaterialMgtDto> list = materialSerivice.selectByCategoryNameAndMaterialName(mmq);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 材质管理保存
	 */
	@RequestMapping("/materials/save")
	@ResponseBody
	public Result saveForMaterialMgt(MaterialMgtDto mmDto) {
		Result result = new Result();
		try {
			materialSerivice.doSaveForMaterialMgt(mmDto, getLoginUser());
			result.setData("保存成功");
		} catch (BusinessException e) {
			logger.error("保存失败：" + e.getMsg());
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 材质管理 根据品名材质关联id查询
	 * 
	 * @param out
	 * @param categoryMaterialId
	 * @return
	 */
	@RequestMapping("/materials/edit/{id}/detail")
	public String getMaterialById(ModelMap out, @PathVariable("id") Long categoryMaterialId) {
		MaterialsMgtQuery mmq = new MaterialsMgtQuery();
		mmq.setCategoryMaterialId(categoryMaterialId);
		List<MaterialMgtDto> list = materialSerivice.selectByCategoryNameAndMaterialName(mmq);
		if (list.isEmpty() || list.size() > 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "材质数据不对应！");
		}
		out.put("info", list.get(0));
		return "resource/materials/create";
	}

	/**
	 * 材质管理 删除记录
	 * 
	 * @param materialId
	 * @return
	 */
	@RequestMapping("/materials/{id}/del")
	@ResponseBody
	public Result delForMaterialMgt(@PathVariable("id") Long materialId) {
		Result result = new Result();
		try {
			materialSerivice.doDeleteMaterial(materialId);
			result.setData("删除成功");
		} catch (BusinessException e) {
			logger.error("删除失败：" + e.getMessage());
			result.setSuccess(Boolean.FALSE);
			result.setData(e.getMessage());
		}
		return result;
	}

	/**************** 材质管理tab end ********************/
	// 获取所有的计重方式
	@RequestMapping("/getallweightConcept")
	@ResponseBody
	public List<String> searchWeightConcept() {
		return Arrays.asList(WeightConceptType.values()).stream().map(e -> e.getName()).collect(Collectors.toList());
	}

	/**
	 * 库存监控job启动测试 @RequestMapping("/doSmartTestJob")
	 * 
	 * @ResponseBody public Result doSmartTestJob(){
	 *               reportResourceInventoryService.dailyStatistics(); return
	 *               new Result("done"); }
	 */

	/**
	 * 历史成交job启动测试
	 */
	@RequestMapping("/doHistoryTransactionJob")
	@ResponseBody
	public Result doHistoryTransactionJob() {
		historyTransactionService.doHistoryTransactionJob();
		return new Result("done");
	}
	/**************** 缺失资源tab start ********************/

	/**
	 * 缺失资源首页
	 */
	@RequestMapping("/lost/index")
	public String indexForLostResource(ModelMap out, String tabIndex) {
		// 各状态资源总数
		Map<String, Integer> map = resourceService.selecCountResourceByStatus(getUserIds());
		out.put(LISTED_COUNT, map.get("listedCount"));
		out.put(UN_LISTED_COUNT, map.get("unListedCount"));
		out.put(ALL_COUNT, map.get("allCount"));
		out.put(EXCEPTION_COUNT, map.get("exceptionCount"));
		out.put(IN_QUERY_COUNT, map.get("inqueryCount"));// 询价资源
		out.put(HISTORY_TRANSACTION_COUNT, map.get("historyTransactionCount"));
		out.put(TAB_INDEX, tabIndex);

		List<City> cities = areaService.queryAllCenterCity();
		out.put("cities", cities);

		return "resource/lost/index";
	}

	/**
	 * 缺失资源搜索
	 * 
	 * @return
	 */
	@RequestMapping("/lost/search")
	@ResponseBody
	public PageResult searchForLostResource(LostResourceQuery lostResourceQuery) {

		List<LostResourceDto> list = resourceService.searchForLostResource(lostResourceQuery);

		PageResult pr = new PageResult();
		Integer total = list.get(list.size() - 1).getTotal();
		list.remove(list.size() - 1);
		pr.setData(list);
		pr.setRecordsFiltered(total);
		pr.setRecordsTotal(total);
		return pr;
	}

	/**************** 缺失资源tab end ********************/

	/**
	 * 资源导出
	 */
	@RequestMapping("/exportRes")
	public void exportResource(HttpServletResponse response, ResourceQuery resourceQuery) {
		InputStream inStream = null;
		OutputStream ostream = null;
		try {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ new String(Constant.EXCEL_EXPORT_FILE_NAME.getBytes("GB2312"), "ISO8859-1"));

			resourceQuery.setUserIds(getUserIds());
			File tempFile = resourceService.exportResource(resourceQuery);
			inStream = new FileInputStream(tempFile);
			ostream = response.getOutputStream();
			// 流转化
			Streams.copy(inStream, ostream, true);
			tempFile.delete();
		} catch (BusinessException e) {
			logger.error("exportResource资源导出：" + e.getMessage());
		} catch (IOException e1) {
			logger.error("exportResource资源导出：" + e1.getMessage());
		} finally {
			IOUtils.closeQuietly(inStream);
			IOUtils.closeQuietly(ostream);
		}
	}

	@RequestMapping("/tostatisres")
	public String toStatisRes(ModelMap out){
		//地区集合
		AreaQuery query = new AreaQuery();
		query.setIsEnable("1");
		//查询所有记录,默认设置查询1000条
		query.setLength(1000);
		List<AreaCityDto> cityList = areaService.query(query);
		//服务中心操作员集合
		List<User>userList = restUserService.queryByRoleType(RoleType.Server.toString());
		//更新时间
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		String beginDate = sdf.format(now) + " 07:00";
		sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");
		String endDate = sdf.format(now);
		out.put("cityList", cityList);
		out.put("userList", userList);
		out.put("beginDate", beginDate);
		out.put("endDate", endDate);
		return "resource/statis/list";
	}
	
	/**
	 * 加载统计列表
	 * @return
	 */
	@RequestMapping("/searchforstatisres")
	@ResponseBody
	public PageResult searchForStatisRes(ResourceStatisQuery statisQuery){
		ResourceStatisTotal  resourctTotal = resourceService.queryStatisResTotal(statisQuery);
		List<ResourceStatisDto> list = new ArrayList<ResourceStatisDto>();
		int total = 0;
		if(resourctTotal != null && resourctTotal.getTotal() > 0){
			 list =resourceService.queryStatisRes(statisQuery);
			 total = resourctTotal.getTotal();
			 if(list != null && list.size() > 0){
				 list.get(0).setStatisTotal(resourctTotal);//默认查询出来的第一条数据存统计数据
			 }
		}
		PageResult pr=new PageResult();
		pr.setData(list);
		pr.setRecordsFiltered(total);
		pr.setRecordsTotal(total);
		return pr;
	}

	/**
	 * 基础资源跳转
	 * 
	 * @return
	 */
	@RequestMapping("/base/index")
	public String resourceBase(ModelMap out, String tabIndex) {// 各状态资源总数
		Map<String, Integer> map = resourceService.selecCountResourceByStatus(getUserIds());
		out.put(LISTED_COUNT, map.get(LISTED_COUNT));
		out.put(UN_LISTED_COUNT, map.get(UN_LISTED_COUNT));
		out.put(ALL_COUNT, map.get(ALL_COUNT));
		out.put(EXCEPTION_COUNT, map.get(EXCEPTION_COUNT));
		out.put(HISTORY_TRANSACTION_COUNT, map.get(HISTORY_TRANSACTION_COUNT));
		out.put(TAB_INDEX, tabIndex);

		List<City> cities = areaService.queryAllCenterCity();
		out.put("cities", cities);
		return "resource/base/index";
	}

	/**
	 * 基础资源查询，added by yjx 2016.6.6
	 */

	@RequestMapping("/base/search.html")
	@ResponseBody
	public PageResult searchResourceBaseList(ResourceBaseQuery resourceBaseQuery) {

		List<ResourceBaseDto> list = resourceBaseService.selectResourceBaseList(resourceBaseQuery);

		PageResult pr = new PageResult();
		Integer total = list.get(list.size() - 1).getTotal();
		list.remove(list.size() - 1);
		pr.setData(list);
		pr.setRecordsFiltered(total);
		pr.setRecordsTotal(total);
		return pr;
	}

	/**
	 * 超市资源首页
	 */
	@RequestMapping("/market/index")
	public void marketResource(ModelMap out, String tabIndex){
		//各状态资源总数
		Map<String,Integer> map=resourceService.selecCountResourceByStatus(getUserIds());
		out.put(LISTED_COUNT, map.get(LISTED_COUNT));
		out.put(UN_LISTED_COUNT, map.get(UN_LISTED_COUNT));
		out.put(ALL_COUNT, map.get(ALL_COUNT));
		out.put(IN_QUERY_COUNT, map.get(IN_QUERY_COUNT));// 询价资源
		out.put(EXCEPTION_COUNT, map.get(EXCEPTION_COUNT));
		out.put(HISTORY_TRANSACTION_COUNT, map.get(HISTORY_TRANSACTION_COUNT));
		out.put(TAB_INDEX, tabIndex);
		List<City> cities = areaService.queryAllCenterCity();
		out.put("cities", cities);
	}

	/**
	 * 超市资源首页
	 */
	@RequestMapping("/market/search")
	@ResponseBody
	public PageResult searchMarketResource(RestNormalResourceQuery restNormalResourceQuery,Integer length){
		PageResult result=new PageResult();
		restNormalResourceQuery.setType(Constant.REST_NORMAL_RESOURCE_TYPE);
		restNormalResourceQuery.setPageSize(length);
		List<ResourceDto> list = resourceService.selectNormalResource(restNormalResourceQuery);

		Integer total = list.get(list.size() - 1).getTotal();
		list.remove(list.size() - 1);
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(total);
		return result;
	}

	/**
	 * 查询参考价范围
	 *
	 * @return
	 * @author caochao
	 */
	@RequestMapping("getpricelimit.html")
	@ResponseBody
	public Result getPriceLimit(@RequestParam(value = "uuid") String categoryUuid) {
		Result result = new Result();
		Category category=categoryService.queryByCategoryUuid(categoryUuid);
		if(category != null){
			result.setSuccess(Boolean.TRUE);
			Map<String,Object> map=new HashedMap();
			map.put("uuid",category.getUuid());
			map.put("priceMin",category.getPriceMin());
			map.put("priceMax",category.getPriceMax());
			result.setData(map);
		}else{
			result.setSuccess(Boolean.FALSE);
			result.setData("未查询到该品种！");
		}

		return result;
	}


	private void sendToMarketByIds(List<Long> ids, User operator) {
		List<com.prcsteel.platform.smartmatch.model.model.Resource> list = resourceService.queryResourceDetailByIds(ids);
		if (list != null && list.size() > 0) {
			List<ResouceForMarket> approvedList = new ArrayList<>();
			for (com.prcsteel.platform.smartmatch.model.model.Resource re : list) {
				BigDecimal initValue = new BigDecimal(99999);
				if (re.getPrice().compareTo(initValue) == 0) {
					continue;
				}
				ResouceForMarket resourceForMarket = new ResouceForMarket();
				resourceForMarket.setResouceId(re.getId().toString());
				resourceForMarket.setSortName("");
				resourceForMarket.setNsortName(re.getCategoryName());
				resourceForMarket.setProviceName("");
				resourceForMarket.setCityName(re.getCityName());
				resourceForMarket.setYieldly(re.getFactoryName());
				resourceForMarket.setMaterials(re.getMaterialName());
				resourceForMarket.setSpecs(re.getExt2());
				resourceForMarket.setPrice(re.getPrice().toString());
				resourceForMarket.setRemark(re.getRemark());
				resourceForMarket.setCreateById(getLoginUser().getId().toString());
				approvedList.add(resourceForMarket);
			}
			if (approvedList.size() > 0)
				resourceChangeForMaketService.send(approvedList, operator);
		}
	}

	private void sendToMarketByList(List<ResourceDto> list, User operator) {
		if (list != null && list.size() > 0) {
			List<ResouceForMarket> approvedList = new ArrayList<>();
			for (ResourceDto re : list) {
				BigDecimal initValue = new BigDecimal(99999);
				if (re.getPrice().compareTo(initValue) == 0) {
					continue;
				}
				ResouceForMarket resourceForMarket = new ResouceForMarket();
				resourceForMarket.setResouceId(re.getId().toString());
				resourceForMarket.setSortName("");
				resourceForMarket.setNsortName(re.getCategoryName());
				resourceForMarket.setProviceName("");
				resourceForMarket.setCityName(re.getCityName());
				resourceForMarket.setYieldly(re.getFactoryName());
				resourceForMarket.setMaterials(re.getMaterialName());
				resourceForMarket.setSpecs(re.getSpec());
				resourceForMarket.setPrice(re.getPrice().toString());
				resourceForMarket.setRemark(re.getRemark());
				resourceForMarket.setCreateById(getLoginUser().getId().toString());
				approvedList.add(resourceForMarket);
			}
			if (approvedList.size() > 0)
				resourceChangeForMaketService.send(approvedList, operator);
		}
	}
}
