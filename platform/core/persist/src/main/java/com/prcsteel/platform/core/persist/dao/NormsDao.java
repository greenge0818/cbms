package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.core.model.model.Norms;

public interface NormsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Norms record);

    int insertSelective(Norms record);

    Norms selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Norms record);

    int updateByPrimaryKey(Norms record);
    
    /**
	 * 根据品名uuid获取规格
	 * @param categoryUuid   品名uuid
	 * @return
	 */
	List<Norms> selectNormsByCategoryUUID(String categoryUuid);  //add by peanut on 15-12-9
}