package org.prcsteel.rest.sdk.activiti.service;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.prcsteel.rest.sdk.activiti.pojo.FormInfo;
import org.prcsteel.rest.sdk.activiti.request.FormSubmitRequest;
import org.prcsteel.rest.sdk.activiti.result.FormSubmitResult;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author zhoukun
 */
@RestApi(value="formService",restServer="activitiRestServer")
public interface FormService {

	@RestMapping(value="form/form-data",method=RequestMethod.POST)
	FormSubmitResult submit(FormSubmitRequest form);
	
	@RestMapping(value="form/form-data",method=RequestMethod.GET)
	FormInfo getFormDataByTaskId(@UrlParam("taskId") String taskId);
}
