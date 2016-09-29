package com.prcsteel.platform.common.aspect;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.impl.CommonCacheServiceImpl;
import com.prcsteel.platform.common.vo.Result;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 重复请求拦截类
 *
 * Created by Rolyer on 2016/6/20.
 */
@Aspect
public class RequestInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(RequestInterceptor.class);

    @Resource
    private CommonCacheServiceImpl cacheService;

    /**
     * 重复请求控制<br/>
     * 检查缓存中是否有同一操作，不存在请求可继续执行，否则拦截请求并抛出异常。
     * @param joinPoint
     * @param opAction 操作信息
     * @throws Throwable
     */
    @Around(value = "execution(* *..web.controller..*.*(..)) && "
            + "@annotation(opAction)")
    public Object resubmitChecked(ProceedingJoinPoint joinPoint, OpAction opAction) throws Throwable {
        LOG.debug("Invoked: com.prcsteel.platform.order.web.aspect.OrderInterceptor.resubmitChecked()");
        Object obj = null;
        String paramsValue = null;

        try {
            paramsValue = getParamsValue(joinPoint);
            LOG.debug("Param value: {}", paramsValue);
        } catch (Exception ex) {
            // 获取键值失败时，直接放行。
            obj = joinPoint.proceed();
        }

        if (StringUtils.isNotBlank(paramsValue)) {
            String actionKey = getActionKey(joinPoint, paramsValue);
            if (isNotExists(actionKey, opAction)) {
                cacheService.set(actionKey, opAction.exp(), opAction.content());
                obj = joinPoint.proceed();
            } else {
                LOG.info("Reject Resubmit!");
                if (opAction.isAjax()){
                    Result result = new Result();
                    result.setSuccess(false);
                    result.setData("请勿重复请求！");
                    obj = result;
                } else {
                    throw new BusinessException("0000","请勿重复请求！");
                }
            }
        }

        LOG.info("Completed: com.prcsteel.platform.order.web.aspect.OrderInterceptor.resubmitChecked");

        return obj;
    }

    private Object proceed(){
        return null;
    }

    /**
     * 通过缓存验证重复请求
     * @param actionKey 操作键值
     * @param opAction 操作信息
     * @return
     */
    private boolean isNotExists(String actionKey, OpAction opAction){
        LOG.debug("Invoked: com.prcsteel.platform.order.web.aspect.OrderInterceptor.validation()");
        Object val = cacheService.get(actionKey);
        if (val != null && opAction.content().equals(val.toString())) {
            LOG.info("ActionKey = {}, Value = {}， Exp = {}", actionKey, val.toString(), opAction.exp());
            return false;
        }

        return true;
    }

    /**
     * 获取操作键值<br/>
     * 格式：类名_方法名_键值
     * @param joinPoint
     * @param key 键值
     * @return
     */
    private String getActionKey(ProceedingJoinPoint joinPoint, String key){
        String clz = joinPoint.getTarget().getClass().getSimpleName().toUpperCase();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getMethod().getName().toUpperCase();

        return clz + "_" + method + "_" + key;
    }

    /**
     * 获取OpAction中指定的请求参数值
     * @param point
     * @return
     */
    private String getParamsValue(ProceedingJoinPoint point) {
        Object[] args = point.getArgs();
        MethodSignature ms = (MethodSignature) point.getSignature();
        OpAction p = ms.getMethod().getAnnotation(OpAction.class);
        String[] parametersNames = ms.getParameterNames();
        // 查找参数，填充参数的值
        int index = getIndex(parametersNames, p.key());
        if (index < 0) {
            throw new BusinessException("0000","OpAction key "+p.key()+" cannot be found in the parameters");
        }

        if (index >= args.length) {
            throw new BusinessException("0000","OpAction key out of bounds");
        }
        Object o = args[index];
        if (o==null) {
            throw new BusinessException("0000","OpAction key "+p.key()+" value cannot be found in the parameters");
        }

        return args[index].toString();
    }

    /**
     * 获取索引值
     * @param params 参数值
     * @param val 参数名
     * @return
     */
    private int getIndex(String[] params, String val){
        return Arrays.asList(params).indexOf(val);
    }
}
