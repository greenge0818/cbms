package com.prcsteel.platform.order.web.controller.demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.prcsteel.rest.sdk.activiti.constant.ActivitiConstant;
import org.prcsteel.rest.sdk.activiti.pojo.FormInfo;
import org.prcsteel.rest.sdk.activiti.pojo.HistoricTaskInstance;
import org.prcsteel.rest.sdk.activiti.pojo.ProcessInstance;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.HistoricQuery;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.FormSubmitRequest;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.result.FormSubmitResult;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.result.StartProcessResult;
import org.prcsteel.rest.sdk.activiti.service.FormService;
import org.prcsteel.rest.sdk.activiti.service.HistoricService;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.prcsteel.platform.order.model.dto.ActivitiTaskFormDto;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * 
 * @author zhoukun
 */
@Controller
@RequestMapping("/activiti")
public class ActivitiDemoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ActivitiDemoController.class);
	
	@Resource
	private ProcessService processService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private FormService formService;
	
	@Resource
	private HistoricService historicService;
	
	@RequestMapping("/task/mine")
	public String index(ModelMap model){
		ListTaskQuery query = new ListTaskQuery();
		query.setAssignee(getLoginUser().getLoginId());
		PagedResult<TaskInfo> tasks = taskService.listTasks(query);
		model.put("taskLists", tasks.getData(TaskInfo.class));
		return "demo/my_tasks";
	}
	
	@RequestMapping("/order/create")
	public String createOrder(){
		return "demo/create_order";
	}
	
	@RequestMapping("/order/save")
	public String saveOrder(
			@RequestParam("manager") String manager,
			@RequestParam("trader") String trader,
			@RequestParam("secretary") String secretary,
			@RequestParam("treasurer") String treasurer,
			@RequestParam("cashier") String cashier,
			ModelMap model){
		StartProcessRequest startRequest = new StartProcessRequest();
		startRequest.addVariable("manager", manager);
		startRequest.addVariable("trader", trader);
		startRequest.addVariable("secretary", secretary);
		startRequest.addVariable("treasurer", treasurer);
		startRequest.addVariable("cashier", cashier);
		startRequest.addVariable(ActivitiConstant.PROCESS_TITLE, getLoginUser().getName() + "提交的订单流程");
		startRequest.setProcessDefinitionId("process:12:15015");
		StartProcessResult res = processService.startProcess(startRequest);
		model.put("res", new Gson().toJson(res));
		return "demo/success";
	}
	
	@RequestMapping("/process/detail")
	public String processDetail(
			@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("taskId") String taskId,
			ModelMap model){
		ProcessInstance process = processService.getProcessInstance(processInstanceId);
		HistoricQuery historicQuery = new HistoricQuery();
		historicQuery.setProcessInstanceId(processInstanceId);
		PagedResult<HistoricTaskInstance> hisoricTasks = historicService.getHistoricTaskInstances(historicQuery);
		model.put("taskId", taskId);
		//model.put("task", taskService.getTask(taskId));
		model.put("processTitle", process.getVariable(ActivitiConstant.PROCESS_TITLE));
		model.put("processInstanceId", processInstanceId);
		model.put("hisoricTasks", hisoricTasks.getData(HistoricTaskInstance.class));
		if(!process.getCompleted()){
			FormInfo formInfos = formService.getFormDataByTaskId(taskId);
			model.put("formInfos", formInfos);
		}
		model.put("dateFormaterIso8601", new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss"));
		return "demo/process_detail";
	}
	
	@RequestMapping("/process/{processInstanceId}/diagram")
	@ResponseBody
	public void processInstanceDiagram(@PathVariable("processInstanceId") String processInstanceId,HttpServletResponse response){
		byte[] img = processService.getDiagramForProcessInstance(processInstanceId);
		response.setContentType("image/png");
		try {
			response.getOutputStream().write(img);
		} catch (IOException e) {
			logger.error("Get process instance diagram failed.",e);
		}
	}
	
	@RequestMapping("/task/execute")
	public String taskExecute(ActivitiTaskFormDto taskForm,ModelMap model){
		FormSubmitRequest form = new FormSubmitRequest();
		form.setTaskId(taskForm.getTaskId());
		if(taskForm.getFormProperties() != null){
			for (Entry<String, String> kv : taskForm.getFormProperties().entrySet()) {
				form.addFormProperties(kv.getKey(), kv.getValue());
			}
		}
		FormSubmitResult result = formService.submit(form);
		model.put("res", result);
		return "demo/success";
	}
}
