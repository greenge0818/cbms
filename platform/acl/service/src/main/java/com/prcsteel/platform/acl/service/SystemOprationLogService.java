package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.query.OpLogQuery;
import com.prcsteel.platform.acl.model.dto.SystemOperationLogDto;
import com.prcsteel.platform.acl.model.model.SystemOperationLog;
import com.prcsteel.platform.common.query.PagedResult;

/**
 * 
 * @author zhoukun
 */
public interface SystemOprationLogService {

	void insert(SystemOperationLog log); 
	
	PagedResult<SystemOperationLogDto> queryByOpLogQuery(OpLogQuery query);
}
