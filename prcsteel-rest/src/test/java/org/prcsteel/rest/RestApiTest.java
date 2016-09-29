package org.prcsteel.rest;

import javax.annotation.Resource;

import org.prcsteel.rest.sdk.activiti.pojo.ProcessInstance;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.result.StartProcessResult;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;

import com.google.gson.Gson;

/**
 * 
 * @author zhoukun
 */
public class RestApiTest extends BaseTest {

	@Resource
	ProcessService processService;
	
	public void testAutoProxy(){
		StartProcessRequest process = new StartProcessRequest();
		process.setProcessDefinitionId("process:5:85");
		process.addVariable("instantiationUser", "kermit");
		System.out.println(new Gson().toJson(process));
		StartProcessResult startResult = processService.startProcess(process);
		System.out.println("Start process success,url: " + startResult.getUrl());
		
		PagedResult<ProcessInstance> res = processService.listProcessInstances(null);
		Gson gson = new Gson();
		for (ProcessInstance p : res.getData(ProcessInstance.class)) {
			System.out.println(gson.toJson(p));
		}
	}
	
}
