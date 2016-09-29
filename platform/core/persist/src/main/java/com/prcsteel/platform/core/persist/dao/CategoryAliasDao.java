package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.model.query.CategoryAliasQuery;

public interface CategoryAliasDao {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryAlias record);

    int insertSelective(CategoryAlias record);

    CategoryAlias selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryAlias record);

    int updateByPrimaryKey(CategoryAlias record);

	List<CategoryAlias> selectAll();

	List<CategoryAlias> seletcByParams(CategoryAliasQuery query);
}