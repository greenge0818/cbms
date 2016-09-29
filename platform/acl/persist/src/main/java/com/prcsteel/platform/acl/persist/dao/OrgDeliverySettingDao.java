package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.OrgDeliverySetting;

import java.util.List;

public interface OrgDeliverySettingDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrgDeliverySetting record);

    int insertSelective(OrgDeliverySetting record);

    OrgDeliverySetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgDeliverySetting record);

    int updateByPrimaryKey(OrgDeliverySetting record);

    List<OrgDeliverySetting> selectByOrgId(Long orgId);
}