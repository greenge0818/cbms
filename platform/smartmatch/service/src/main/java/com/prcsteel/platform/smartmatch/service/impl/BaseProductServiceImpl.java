package com.prcsteel.platform.smartmatch.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.MaterialsDao;
import com.prcsteel.platform.smartmatch.model.dto.BaseProductDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.enums.ResourceSourceType;
import com.prcsteel.platform.smartmatch.model.model.BaseProduct;
import com.prcsteel.platform.smartmatch.model.model.BaseProductPrice;
import com.prcsteel.platform.smartmatch.model.model.Factory;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.persist.dao.BaseProductDao;
import com.prcsteel.platform.smartmatch.persist.dao.BaseProductPriceDao;
import com.prcsteel.platform.smartmatch.persist.dao.FactoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.persist.dao.WarehouseDao;
import com.prcsteel.platform.smartmatch.service.BaseProductService;

@Service("baseProductService")
public class BaseProductServiceImpl implements BaseProductService {

	@Resource
	BaseProductPriceDao baseProductPriceDao;

	@Resource
	BaseProductDao baseProductDao;

	@Resource
	CategoryDao categoryDao;

	@Resource
	MaterialsDao materialsDao;

	@Resource
	FactoryDao factoryDao;

	@Resource
	WarehouseDao warehouseDao;

	@Resource
	CityDao cityDao;
	
	@Resource
	ResourceDao resourceDao;
	
	@Resource
	ResourceNormsDao resourceNormsDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean saveBaseProduct(ResourceDto resourceDto, User user) {
		Boolean flag = false;
		BaseProduct baseProduct = new BaseProduct();
		BaseProductPrice baseProductPrice = new BaseProductPrice();
		baseProduct.setSpec(resourceDto.getSpec());
		String[] specs = resourceDto.getSpec().split("\\*");
		if (specs.length > 1 || specs.length == 1) {
			resourceDto.setSpec1(specs[0]);
			baseProduct.setSpec1(specs[0]);
		}
		if (specs.length > 2 || specs.length == 2) {
			resourceDto.setSpec2(specs[1]);
			baseProduct.setSpec2(specs[1]);
		}
		if (specs.length == 3) {
			resourceDto.setSpec3(specs[2]);
			baseProduct.setSpec3(specs[2]);
		}
		
		//记重方式
		if (StringUtils.isEmpty(resourceDto.getWeightConcept())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "记重方式不能为空！");
		} else {
			baseProduct.setWeightConcept(resourceDto.getWeightConcept());
		}
		
		// 品名uuid
		if (StringUtils.isEmpty(resourceDto.getCategoryUuid())) {
			if (null != resourceDto.getCategoryName()) {
				Category category = categoryDao.selectByCategory(resourceDto.getCategoryName());
				if (category == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的品名！");
				}
				baseProduct.setCategoryUuid(category.getUuid());
				resourceDto.setCategoryUuid(category.getUuid());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名不能为空！");
			}
		} else {
			baseProduct.setCategoryUuid(resourceDto.getCategoryUuid());
		}
		// 品名
		if (StringUtils.isEmpty(resourceDto.getCategoryName())) {
			if (null != resourceDto.getCategoryUuid()) {
				Category category = categoryDao.queryByCategoryUuid(resourceDto.getCategoryUuid());
				if (category == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的品名！");
				}
				baseProduct.setCategoryName(category.getName());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid不能为空！");
			}
		} else {
			baseProduct.setCategoryName(resourceDto.getCategoryName());
		}
		// 材质uuid
		if (StringUtils.isEmpty(resourceDto.getMaterialUuid())) {
			if (null != resourceDto.getMaterialName()) {
				List<Materials> materials = materialsDao.selectByMaterialsName(resourceDto.getMaterialName());
				if (materials == null||materials.size()<1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的材质！");
				}
				baseProduct.setMaterialUuid(materials.get(0).getUuid());// 为防止在数据库存在两个一样的材质名，取其中一个
				resourceDto.setMaterialUuid(materials.get(0).getUuid());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "材质名不能为空！");
			}
		} else {
			baseProduct.setMaterialUuid(resourceDto.getMaterialUuid());
		}
		// 材质名称
		if (StringUtils.isEmpty(resourceDto.getMaterialName())) {
			if (null != resourceDto.getMaterialUuid()) {
				Materials materials = materialsDao.selectByMaterialUuid(resourceDto.getMaterialUuid());
				if (materials == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的材质！");
				}
				baseProduct.setMaterialName(materials.getName());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "材质uuid不能为空！");
			}
		} else {
			baseProduct.setMaterialName(resourceDto.getMaterialName());
		}
		// 工厂id
		if (null == resourceDto.getFactoryId() || 0L == resourceDto.getFactoryId()) {
			if (null != resourceDto.getFactoryName()) {
				Factory factory = factoryDao.selectByName(resourceDto.getFactoryName());
				if (factory == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的钢厂！");
				}
				baseProduct.setFactoryId(factory.getId());
				resourceDto.setFactoryId(factory.getId());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "钢厂名不能为空！");
			}
		} else {
			baseProduct.setFactoryId(resourceDto.getFactoryId());
		}
		
		// 工厂名
		if (StringUtils.isEmpty(resourceDto.getFactoryName())) {
			if (null != resourceDto.getFactoryId()) {
				Factory factory = factoryDao.selectByPrimaryKey(resourceDto.getFactoryId());
				if (factory == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的钢厂！");
				}
				baseProduct.setFactoryName(factory.getName());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "工厂id不能为空！");
			}
		} else {
			baseProduct.setFactoryName(resourceDto.getFactoryName());
		}

		// 仓库信息
		if (null == resourceDto.getWarehouseId() || 0L == resourceDto.getWarehouseId()) {
			if (null != resourceDto.getWarehouseName()) {
				Warehouse warehouse = new Warehouse();
				warehouse.setName(resourceDto.getWarehouseName());
				List<Warehouse> newWarehouse = warehouseDao.queryByName(warehouse);
				if (newWarehouse == null || newWarehouse.size() < 1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
				} else {
					baseProduct.setWarehouseId(newWarehouse.get(0).getId());
					resourceDto.setWarehouseId(newWarehouse.get(0).getId());
				}
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库名不能为空！");
			}
		} else {
			baseProduct.setWarehouseId(resourceDto.getWarehouseId());
		}
		if (StringUtils.isEmpty(resourceDto.getWarehouseName())) {
			if (null != resourceDto.getWarehouseId()) {
				Warehouse newWarehouse = warehouseDao.selectByPrimaryKey(resourceDto.getWarehouseId());
				if (newWarehouse == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
				} else {
					baseProduct.setWarehouseName(newWarehouse.getName());
				}
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库id不能为空！");
			}
		} else {
			baseProduct.setWarehouseName(resourceDto.getWarehouseName());
		}

		// 城市id
		if (null == resourceDto.getCityId() || 0L == resourceDto.getCityId()) {
			if (null != resourceDto.getCityName()) {
				City city = cityDao.queryByCityName(resourceDto.getCityName());
				if (city == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
				} else {
					baseProduct.setCityId(city.getId());
					resourceDto.setCityId(city.getId());
				}
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "城市名不能为空！");
			}
		} else {
			baseProduct.setCityId(resourceDto.getCityId());
		}
		// 城市名
		if (StringUtils.isEmpty(resourceDto.getCityName())) {
			if (null != resourceDto.getCityName()) {
				City city = cityDao.selectByPrimaryKey(resourceDto.getCityId());
				if (city == null) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没找到对应的仓库！");
				} else {
					baseProduct.setCityName(city.getName());
				}
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库id不能为空！");
			}
		} else {
			baseProduct.setCityName(resourceDto.getCityName());
		}
		
		BaseProductDto baseProductDto = baseProductDao.selectBaseProductForSave(resourceDto);
		
		if (baseProductDto == null) {
			baseProduct.setCreated(new Date());
			baseProduct.setCreatedBy(user.getLoginId()!=null?user.getLoginId():" ");
			baseProduct.setLastUpdated(new Date());
			baseProduct.setLastUpdatedBy(user.getLoginId()!=null?user.getLoginId():" ");
			baseProduct.setModificationNumber(0);
			baseProduct.setPrice(resourceDto.getPrice());
			if (baseProductDao.insertSelective(baseProduct) > 0) {
				flag = true;
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增资源信息失败！");
			}
		} else {
			if (baseProductDto.getPrice().compareTo(resourceDto.getPrice()) != 0) {
				baseProduct.setPrice(resourceDto.getPrice());
			}
			baseProduct.setLastUpdated(new Date());
			baseProduct.setLastUpdatedBy(user.getLoginId()!=null?user.getLoginId():" ");
			baseProduct.setId(baseProductDto.getId());
			baseProduct.setModificationNumber(
					baseProductDao.selectByPrimaryKey(baseProductDto.getId()).getModificationNumber() + 1);
			if (baseProductDao.updateByPrimaryKeySelective(baseProduct) > 0) {
				flag = true;
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改资源信息失败！");
			}
		}
		baseProductPrice.setAccountId(resourceDto.getAccountId());
		baseProductPrice.setWeight(resourceDto.getWeight());
		baseProductPrice.setPrice(resourceDto.getPrice());
		baseProductPrice.setQuantity(resourceDto.getQuantity());
		baseProductPrice.setProductId(baseProduct.getId());
		baseProductPrice.setCreated(new Date());
		baseProductPrice.setCreatedBy(user.getLoginId()!=null?user.getLoginId():" ");
		baseProductPrice.setLastUpdated(new Date());
		baseProductPrice.setLastUpdatedBy(user.getLoginId());
		baseProductPrice.setType(resourceDto.getSourceType());
		if (baseProductPriceDao.insertSelective(baseProductPrice) > 0) {
			flag = true;
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增资源价格信息失败！");
		}

		return flag;
	}

	@Override
	public void deletProduct(List<ResourceDto> resourceList) {
		//删除产品资源表
		if (!resourceList.isEmpty()) {
			for (ResourceDto resourceDto : resourceList) {
				ResourceQuery rq = new ResourceQuery();
				rq.setCategoryUuid(resourceDto.getCategoryUuid());
				rq.setMaterialUuid(resourceDto.getMaterialUuid());
				rq.setFactoryId(resourceDto.getFactoryId());
				rq.setWeightConcept(resourceDto.getWeightConcept());
				rq.setSpec(resourceDto.getSpec());
				rq.setWarehouseId(resourceDto.getWarehouseId());
				rq.setFactoryName(resourceDto.getFactoryName());
				rq.setWarehouseName(resourceDto.getWarehouseName());
				List<ResourceDto> rList = resourceDao.selectByQueryForUpdate(rq);
				// 如果资源表不存在资源的资源，那么相关的产品资源表也要删除，反之不删除
				if (rList.isEmpty()) {
					resourceDto.setCategoryUuid(resourceDto.getCategoryUuid());
					resourceDto.setMaterialUuid(resourceDto.getMaterialUuid());
					resourceDto.setFactoryId(resourceDto.getFactoryId());
					resourceDto.setSpec(resourceDto.getSpec());
					resourceDto.setFactoryName(resourceDto.getFactoryName());
					resourceDto.setWarehouseName(resourceDto.getWarehouseName());
					resourceDto.setWarehouseId(resourceDto.getWarehouseId());
					resourceDto.setWeightConcept(resourceDto.getWeightConcept());
					resourceDto.setCityId(resourceDto.getCityId());
					BaseProductDto baseProductDto = baseProductDao.selectBaseProductForSave(resourceDto);
					if (baseProductDto != null) {
						baseProductDao.deleteByPrimaryKey(baseProductDto.getId());
						baseProductPriceDao.deleteByPrductId(baseProductDto.getId());
					}
				}
			}
		}
	}

}
