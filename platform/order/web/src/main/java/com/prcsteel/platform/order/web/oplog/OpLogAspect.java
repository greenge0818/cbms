package com.prcsteel.platform.order.web.oplog;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.prcsteel.platform.acl.model.model.SystemOperationLog;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SystemOprationLogService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.ObjectUtils;
import com.prcsteel.platform.order.web.utils.WebAppContextUtil;

/**
 * @author zhoukun
 */
public class OpLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OpLogAspect.class);

    private static final int PARAMS_MAX_LENGTH = 1000;

    @Resource
    SystemOprationLogService systemOprationLogService;

    public void addOpLog(JoinPoint point, OpLog opLog) {
        try {
            User user = WebAppContextUtil.getLoginUser();
            if (user == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "can't get current user info.");
            }
            // 查找参数配置，将参数的值提取出来
            Map<String, String> paramsDtoList = getParamsValue(point);
            // 保存行为日志
            saveOpLog(opLog, user, paramsDtoList);
        } catch (Exception e) {
            logger.error("Add operation log failed!", e);
        }
    }

    private Map<String, String> getParamsValue(JoinPoint point) {
        Map<String, String> paramsMap = new HashMap<>();
        Object[] args = point.getArgs();
        MethodSignature ms = (MethodSignature) point.getSignature();
        OpParam[] opParams = ms.getMethod().getAnnotationsByType(OpParam.class);
        String[] parametersNames = ms.getParameterNames();
        // 查找参数，填充参数的值
        for (OpParam p : opParams) {
            if (p.index() >= args.length) {
                throw new IndexOutOfBoundsException("OpLog OpParams index out of bounds");
            }
            String paramValue = p.defaultValue();
            Object obj = null;
            if (p.index() > 0) {
                if (args[p.index()] != null) {
                    obj = args[p.index()];
                    paramValue = getParamString(obj);

                }
            } else {
                String pname = p.name();
                if (StringUtils.isBlank(pname)) {
                    pname = p.value();
                }
                for (int i = 0; i < parametersNames.length; i++) {
                    if (parametersNames[i].equals(pname)) {
                        paramValue = getParamString(args[i]);
                        break;
                    }
                }
                if (!StringUtils.isBlank(p.name()) && StringUtils.isEmpty(paramValue)) {  //直接指定value值
                    paramValue = p.value();
                }
            }
            paramsMap.put(StringUtils.isEmpty(p.name()) ? p.value() : p.name(), paramValue);//未指定name，则使用方法参数名作为key
        }
        return paramsMap;
    }

    private void saveOpLog(OpLog opLog, User user, Map<String, String> paramsDtoList) {
        OpType opType = opLog.value();
        SystemOperationLog sysOpLog = new SystemOperationLog();
        sysOpLog.setCreatedBy(user.getName());
        sysOpLog.setLastUpdatedBy(user.getName());
        sysOpLog.setOperationKey(opType);
        sysOpLog.setOperationLevel(opType.getLevel());
        sysOpLog.setOperationLevelValue(opType.getLevel().getLevel());
        sysOpLog.setOperationName(opType.getDescription());
        sysOpLog.setOperatorId(user.getId());
        sysOpLog.setOperatorName(user.getName());
        sysOpLog.setParameters(new Gson().toJson(paramsDtoList));
        if (sysOpLog.getParameters().length() > PARAMS_MAX_LENGTH) {
            sysOpLog.setParameters(sysOpLog.getParameters().substring(0, PARAMS_MAX_LENGTH));
        }
        systemOprationLogService.insert(sysOpLog);
    }

    private String getParamString(Object obj) {
        if (obj == null) {
            return "";
        }
        String str = "";
        if (ObjectUtils.isBaseDataType(obj)) {
            str = obj.toString();
        } else {
            str = new Gson().toJson(obj);
        }
        return str;
    }
}
