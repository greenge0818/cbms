package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.dto.BaseOrganizationDeliverDto;
import com.prcsteel.platform.acl.model.model.BaseOrganizationDeliver;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseOrganizationDeliverDao{
    int deleteByPrimaryKey(Long id);

    int insert(BaseOrganizationDeliver record);

    int insertSelective(BaseOrganizationDeliver record);

    BaseOrganizationDeliver selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseOrganizationDeliver record);

    int updateByPrimaryKey(BaseOrganizationDeliver record);

    List<BaseOrganizationDeliverDto> selectByOrgId(@Param("orgId") Long orgId);

    int batchUpdateOrgDeliver(List<BaseOrganizationDeliver> baseOrganizationDeliver);
}