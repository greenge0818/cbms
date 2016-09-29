package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.CacheAccountDto;

/**
 * 缓存查询Dao
 * @author tangwei
 *
 */
public interface CacheDao {
	
	public List<CacheAccountDto> getAccountInfo();
	
}
