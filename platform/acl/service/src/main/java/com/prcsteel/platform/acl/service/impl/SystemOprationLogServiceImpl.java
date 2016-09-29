package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.query.OpLogQuery;
import com.prcsteel.platform.common.query.PagedResult;
import com.prcsteel.platform.acl.model.dto.SystemOperationLogDto;
import com.prcsteel.platform.acl.model.model.SystemOperationLog;
import com.prcsteel.platform.acl.persist.dao.SystemOperationLogDao;
import com.prcsteel.platform.acl.service.SystemOprationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * @author zhoukun
 */
@Service("systemOprationLogService")
public class SystemOprationLogServiceImpl implements SystemOprationLogService {

	@Resource
	SystemOperationLogDao systemOprationLogDao;
	
	@Override
	public void insert(SystemOperationLog log) {
		systemOprationLogDao.insert(log);
	}

	@Override
	public PagedResult<SystemOperationLogDto> queryByOpLogQuery(OpLogQuery query) {
		PagedResult<SystemOperationLogDto> res = new PagedResult<>();
		res.setQuery(query);
		res.setData(systemOprationLogDao.queryByOpLogQuery(query));
		res.setTotalRows(systemOprationLogDao.countByOpLogQuery(query));
		return res;
	}

}
