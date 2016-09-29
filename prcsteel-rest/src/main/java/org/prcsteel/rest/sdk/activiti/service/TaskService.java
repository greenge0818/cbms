package org.prcsteel.rest.sdk.activiti.service;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.TaskActionRequest;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author zhoukun
 */
@RestApi(value="taskService",restServer="activitiRestServer")
public interface TaskService {

	@RestMapping(value="runtime/tasks/{taskId}",method=RequestMethod.GET)
	TaskInfo getTask(@UrlParam("taskId") String taskId);
	
	@RestMapping(value="runtime/tasks",method=RequestMethod.GET)
	PagedResult<TaskInfo> listTasks(@UrlParam ListTaskQuery query);
	
	@RestMapping(value="runtime/tasks/{taskId}",method=RequestMethod.PUT)
	TaskInfo updateTask(TaskInfo task, @UrlParam("taskId") String taskId);
	
	@RestMapping(value="runtime/tasks/{taskId}",method=RequestMethod.POST)
	TaskInfo invokeTaskAction(@UrlParam("taskId") String taskId,TaskActionRequest action);
	
}
