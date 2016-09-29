package org.prcsteel.rest.sdk.activiti.service;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.prcsteel.rest.sdk.activiti.pojo.ProcessInstance;
import org.prcsteel.rest.sdk.activiti.query.ProcessInstanceQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.result.StartProcessResult;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 流程服务
 * @author zhoukun
 */
@RestApi(value="processService",restServer="activitiRestServer")
public interface ProcessService {
	
	@RestMapping(value="runtime/process-instances",method=RequestMethod.GET)
	PagedResult<ProcessInstance> listProcessInstances(@UrlParam ProcessInstanceQuery query);
	
	/**
	 * 启动一个流程实例
	 * @param process
	 */
	@RestMapping(value="runtime/process-instances",method=RequestMethod.POST)
	StartProcessResult startProcess(StartProcessRequest process);
	
	@RestMapping(value="runtime/process-instances/{processInstanceId}",method=RequestMethod.GET)
	ProcessInstance getProcessInstance(@UrlParam("processInstanceId") String processInstanceId);
	
	@RestMapping(value="runtime/process-instances/{processInstanceId}/diagram",method=RequestMethod.GET)
	byte[] getDiagramForProcessInstance(@UrlParam("processInstanceId") String processInstanceId);
}
