package org.prcsteel.rest.sdk.activiti.service;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.prcsteel.rest.sdk.activiti.pojo.HistoricTaskInstance;
import org.prcsteel.rest.sdk.activiti.query.HistoricQuery;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author zhoukun
 */
@RestApi(value="historicService",restServer="activitiRestServer")
public interface HistoricService {

	@RestMapping(value="history/historic-task-instances",method=RequestMethod.GET)
	PagedResult<HistoricTaskInstance> getHistoricTaskInstances(@UrlParam HistoricQuery query);
}
