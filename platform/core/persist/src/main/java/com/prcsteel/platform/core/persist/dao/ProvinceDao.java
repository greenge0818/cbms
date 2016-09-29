package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.model.Province;

import org.springframework.cache.annotation.Cacheable;

public interface ProvinceDao {
    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_ALL_PROVINCE_LIST + "'")
    List<Province> listAll();

    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_PROVINCE_SELECT_BY_PRIMARYKEY + "' + #p0")
    Province selectByPrimaryKey(Long id);
}