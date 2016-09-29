package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.impl.CommonCacheServiceImpl;
import com.prcsteel.platform.smartmatch.api.RestOrganizationService;
import com.prcsteel.platform.smartmatch.model.model.RestOrganization;
import com.prcsteel.platform.smartmatch.service.AdvanceOrganizationService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * Created by caochao on 2016/9/8.
 */
@Service("advanceOrganizationService")
public class AdvanceOrganizationServiceImpl implements AdvanceOrganizationService, ApplicationContextAware {

    @Resource
    private CommonCacheServiceImpl commonCacheServiceImpl;

    // spring上下文
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 使用spring上下文获取Rest的SpringBEAN
     *
     * @param serviceName
     * @return
     */
    private Object getRestService(String serviceName) {
        Object bean = applicationContext.getBean(serviceName);
        if (bean != null) {
            return bean;
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "获取不到REST服务：" + serviceName);
        }
    }

    @Override
    public RestOrganization queryById(Long id) {
        Object object = commonCacheServiceImpl.get(InitCacheDataServiceImpl.CACHE_KEY_ORGANIZATION);
        if (object != null) {
            List<Organization> list = (List<Organization>) object;
            Optional<Organization> organizationBean = list.stream().filter(a -> a.getId() == id).findFirst();
            if (organizationBean.isPresent()) {
                RestOrganization restOrganization = new RestOrganization();
                try {
                    BeanUtils.copyProperties(restOrganization, organizationBean.get());
                    return restOrganization;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        //未从缓存中获取到
        return ((RestOrganizationService)getRestService("restOrganizationService")).queryById(id);
    }

    @Override
    public List<Organization> queryAllBusinessOrg() {
        Object object = commonCacheServiceImpl.get(InitCacheDataServiceImpl.CACHE_KEY_ORGANIZATION);
        if (object != null) {
            List<Organization> list = (List<Organization>) object;
            return list;
        }
        //未从缓存中获取到
        return ((RestOrganizationService)getRestService("restOrganizationService")).queryAllBusinessOrg();
    }

}
