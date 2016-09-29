package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.service.WorkFlowCommonService;
import org.prcsteel.rest.sdk.activiti.pojo.ProcessInstance;
import org.prcsteel.rest.sdk.activiti.query.ProcessInstanceQuery;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dengxiyan on 2016/5/6.
 */
@Service("workFlowCommonService")
public class WorkFlowCommonServiceImpl implements WorkFlowCommonService {
    @Resource
    ProcessService  processService;

    /**
     * 根据流程定义的key及业务id查询
     *
     * @param processDefinitionKey 流程定义的key
     * @param businessKey          业务id
     * @return
     */
    @Override
    public ProcessInstance queryOneProcessInstance(String processDefinitionKey, String businessKey) {
        ProcessInstanceQuery query = new ProcessInstanceQuery();
        query.setProcessDefinitionKey(processDefinitionKey);
        query.setBusinessKey(businessKey);
        PagedResult<ProcessInstance> pagedResult =  processService.listProcessInstances(query);
        if (pagedResult != null){
            List<ProcessInstance> list = pagedResult.getData(ProcessInstance.class);
            if (list != null && list.size() > 0){
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 判断流程是否结束
     * 不存在流程实例或结束
     * @param processDefinitionKey 流程定义的key
     * @param businessKey          流程定义的key
     * @return true 结束 false 未结束
     */
    @Override
    public boolean isProcessEnded(String processDefinitionKey, String businessKey) {
        ProcessInstance processInstance =  queryOneProcessInstance(processDefinitionKey,businessKey);
        return processInstance == null || processInstance.getEnded();
    }
}
