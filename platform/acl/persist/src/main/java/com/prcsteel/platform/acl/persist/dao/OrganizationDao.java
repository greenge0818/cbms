package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.query.ReportOrgQuery;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.dto.OrganizationDto;
import com.prcsteel.platform.acl.model.model.Organization;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface OrganizationDao {
    Organization queryById(Long id);

    List<Organization> queryByParentId(Long parentId);

    @CacheEvict(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_ALL_ORGANIZATION + "'")
    int insert(Organization organization);

    int insertSelective(Organization record);

    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_ALL_ORGANIZATION + "'")
    List<Organization> queryAll();

    @CacheEvict(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_ALL_ORGANIZATION + "'")
    int update(Organization organization);

    int updateStatusById(Organization organization);

    Organization queryByName(String name);

    Organization queryByCode(String code);

    OrganizationDto selectOrgInfoByParam(Map<String, Object> param);

    /**
     * 获取服务中心总数
     * @return
     */
    int countAll(); 
    /**
     * 获取所有第二级中心
     * @return
     */
    List<Organization>  getAllSecendOrg();
    /**
     * 查找服务中心子集
     * @param parent_id
     * @return
     */
    List<Organization>  getOrgByParentID(long parent_id);

    /**
     * 查询所有业务服务中心：如长沙服务中心
     * @return
     */
    List<Organization>  queryAllBusinessOrg();

    /**
     * 查询所有业务服务中心：如长沙服务中心
     * @return
     */
    List<Organization> queryAllOrg(ReportOrgQuery queryParam);
    /**
     * 查询所有业务服务中心所在城市：如长沙
     * @return
     */
    List<com.prcsteel.platform.acl.model.dto.Organization>  queryBusinessOrgToWeChat();
    
    List<Organization> queryDraftedOrg();
}