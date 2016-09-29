package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.model.District;

import org.springframework.cache.annotation.Cacheable;


public interface DistrictDao {

    @Cacheable(value = Constant.CACHE_NAME, key="'" + Constant.CACHE_DISTRICT_LIST_BY_CITYID + "' + #p0")
    List<District> listByCityId(Long cityId);

    @Cacheable(value = Constant.CACHE_NAME, key="'" + Constant.CACHE_DISTRICT_SELECT_BY_PRIMARYKEY + "' + #p0")
    District selectByPrimaryKey(Long id);
}