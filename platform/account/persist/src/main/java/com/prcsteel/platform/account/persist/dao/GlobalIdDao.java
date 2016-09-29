package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.GlobalIdModifier;
import com.prcsteel.platform.account.model.model.GlobalId;

/**
 * 
 * @author zhoukun
 */
public interface GlobalIdDao {

	int addGlobalId(GlobalId gid);
	
	GlobalId selectByModuleName(@Param("moduleName") String moduleName);
	
	List<GlobalId> selectAll();
	
	int setCurrentId(GlobalIdModifier gm);
}
