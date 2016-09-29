package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.dto.SystemOperationLogDto;
import com.prcsteel.platform.acl.model.model.SystemOperationLog;
import com.prcsteel.platform.acl.model.query.OpLogQuery;

import java.util.List;

/**
 * 
 * @author zhoukun
 */
public interface SystemOperationLogDao {

	int insert(SystemOperationLog log); 
	
	List<SystemOperationLogDto> queryByOpLogQuery(OpLogQuery query);
	
	long countByOpLogQuery(OpLogQuery query);
}
