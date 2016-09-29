package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.google.common.collect.Lists;
import com.prcsteel.platform.acl.model.dto.OrganizationDto;
import com.prcsteel.platform.acl.model.model.OrgDeliverySetting;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.persist.dao.OrgDeliverySettingDao;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.service.OrganizationService;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Rabbit Mao on 2015/7/14.
 */
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	@Resource
	OrganizationDao organizationDao;
	@Resource
	UserDao userDao;
	@Resource
	OrgDeliverySettingDao settingDao;

	@Override
	public List<String> selectDeliverySettingByOrgId(Long id) {
		return settingDao.selectByOrgId(id).stream()
				.collect(Collectors.mapping(OrgDeliverySetting::getDeliveryType, Collectors.toList()));
	}

	@Override
	public Organization queryById(Long id) {
		return organizationDao.queryById(id);
	}

	@Override
	public List<Organization> queryByParentId(Long parentId) {
		return organizationDao.queryByParentId(parentId);
	}

	@Override
	public OrganizationDto selectOrgInfoByParam(Map<String, Object> param) {
		OrganizationDto dto = organizationDao.selectOrgInfoByParam(param);
		dto.setDeliveryTypeList(settingDao.selectByOrgId(dto.getOrgId()).stream()
				.collect(Collectors.mapping(OrgDeliverySetting::getDeliveryType, Collectors.toList())));
		return dto;
	}

	@Override
	public void addOrganization(Organization organization, List<String> deliveryTypes, String curUser) {
		if (organizationDao.queryByName(organization.getName()) != null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心名字不能重复");
		}
		if (organizationDao.queryByCode(organization.getSeqCode()) != null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心代码不能重复");
		}
		organization.setStatus(Integer.parseInt(Constant.YES));
		organization.setModificationNumber(0);
		organization.setCreated(new Date());
		organization.setCreatedBy(curUser);
		organization.setLastUpdated(new Date());
		organization.setLastUpdatedBy(curUser);
		organization.setCreditLimitUsed(BigDecimal.ZERO);
		if (organizationDao.insert(organization) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增服务中心失败");
		}
		for (String type : deliveryTypes) {
			OrgDeliverySetting setting = new OrgDeliverySetting(organization.getId(), type, curUser);
			if (settingDao.insert(setting) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置服务中心可用放货单信息失败");
			}

		}
	}

	@Override
	public void addDepartment(Organization organization, String curName) {
		if (organizationDao.queryByName(organization.getName()) != null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门名称不能重复");
		}
		organization.setStatus(Integer.parseInt(Constant.YES));
		organization.setModificationNumber(0);
		organization.setCreated(new Date());
		organization.setCreatedBy(curName);
		organization.setLastUpdated(new Date());
		organization.setLastUpdatedBy(curName);
		organization.setCreditLimitUsed(BigDecimal.ZERO);
		if (organizationDao.insertSelective(organization) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增部门失败");
		}
	}

	@Override
	public void updateOrganization(Organization organization, List<String> deliveryTypes, String curUser) {
		Organization organizationCode = organizationDao.queryByCode(organization.getSeqCode());
		if (organizationCode != null && !organizationCode.getId().equals(organization.getId())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心代码不能重复");
		}
		Organization organizationName = organizationDao.queryByName(organization.getName());
		if (organizationName != null && !organizationName.getId().equals(organization.getId())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心名称不能重复");
		}
		Organization o = organizationDao.queryById(organization.getId());
		if (o.getCreditLimitUsed().compareTo(organization.getCreditLimit()) == 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心已使用的额度已超过设置的额度");
		}
		if (organization.getParentId() != null && !organization.getId().equals(organization.getParentId())) {
			organization.setLastUpdated(new Date());
			organization.setLastUpdatedBy(curUser);
			if (null != o.getModificationNumber()) {
				organization.setModificationNumber(o.getModificationNumber() + 1);
			} else {
				organization.setModificationNumber(1);
			}
		} else {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "不能把服务中心设置成自己的下级服务中心"); // 不能把自己设置成自己的父级
		}
		if (organizationDao.update(organization) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新服务中心失败");
		}
		Map<String, List<Long>> settings = settingDao.selectByOrgId(organization.getId()).stream()
				.collect(Collectors.groupingBy(OrgDeliverySetting::getDeliveryType,
						Collectors.mapping(OrgDeliverySetting::getId, Collectors.toList())));
		for (String type : deliveryTypes) {
			if (settings.containsKey(type)) { // 如果原来就存在
				settings.remove(type);
			} else { // 如果原来不存在
				OrgDeliverySetting setting = new OrgDeliverySetting(organization.getId(), type, curUser);
				if (settingDao.insert(setting) != 1) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置服务中心可用放货单信息失败");
				}
			}
		}
		for (String key : settings.keySet()) {
			if (settingDao.deleteByPrimaryKey(settings.get(key).get(0)) != 1) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除原有服务中心可用放货单信息失败");
			}
		}
	}

	@Override
	public void updateDepartment(Organization organization, String curUser) {
		Organization organizationName = organizationDao.queryByName(organization.getName());
		// if (organizationName != null &&
		// !organizationName.getId().equals(organization.getId())) {
		// throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
		// "部门不能重复");
		// }
		organization.setId(organizationName.getId());
		organization.setLastUpdated(new Date());
		organization.setLastUpdatedBy(curUser);
		if (organizationDao.update(organization) != 1) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新服务中心失败");
		}
	}

	/**
	 * 获取所有子服务中心
	 *
	 * @param parentOrgId
	 *            上级服务中心id
	 * @param mode
	 *            0: 包含上级服务中心自己 1: 不包含上级服务中心自己
	 * @return
	 */
	@Override
	public List<Long> getAllChildOrgId(Long parentOrgId, Integer mode) {
		List<Long> orgIds = new LinkedList<>();
		List<Organization> list = organizationDao.queryAll();

		if (list != null) {
			Map<Long, List<Long>> orgMap = list.stream().collect(Collectors.groupingBy(Organization::getParentId,
					Collectors.mapping(Organization::getId, Collectors.toList())));
			findChildren(parentOrgId, orgMap, orgIds);
		}
		if (mode == 0) {
			orgIds.add(parentOrgId);
		}
		return orgIds;
	}

	/**
	 * 递归查找
	 *
	 * @param id
	 * @param map
	 * @param orgIds
	 */
	private void findChildren(Long id, Map<Long, List<Long>> map, List<Long> orgIds) {
		if (map.containsKey(id)) {
			List<Long> list = map.get(id);
			for (Long i : list) {
				orgIds.add(i);
				findChildren(i, map, orgIds); // 继续查子集的子集
			}
		}
	}

    /**
     * 查询所有业务服务中心：如长沙服务中心
     *
     * @return
     */
    @Override
    public List<Organization> queryAllBusinessOrg() {
        return organizationDao.queryAllBusinessOrg();
    }


    /**
     * 获取所有第二级中心
     * @return
     */
    @Override
    public List<Organization>  getAllSecendOrg(){
        return organizationDao.getAllSecendOrg();
    }

    @Override
    public List<Organization> getAllOrganization() {
        return organizationDao.queryAll();
    }


    /**
     * 查询所有业务服务中心所在城市：如长沙
     * @return
     */
    @Override
    public List<com.prcsteel.platform.acl.model.dto.Organization>  queryBusinessOrgToWeChat(){
        return organizationDao.queryBusinessOrgToWeChat();
    }
    @Override
    public List<com.prcsteel.platform.acl.model.dto.Organization> queryDraftedOrg() {
        List<Organization> orgList = organizationDao.queryDraftedOrg();
        List<com.prcsteel.platform.acl.model.dto.Organization> resultList = Lists.newArrayListWithCapacity(orgList.size());
        try{
            for(Organization org : orgList){
                com.prcsteel.platform.acl.model.dto.Organization orgDto = new com.prcsteel.platform.acl.model.dto.Organization();
                BeanUtils.copyProperties(orgDto, org);
                resultList.add(orgDto);
            }
        }catch(Exception e){
            logger.error("查询服务中心失败", e);
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "查询服务中心失败");
        }
        return resultList;
    }
}
