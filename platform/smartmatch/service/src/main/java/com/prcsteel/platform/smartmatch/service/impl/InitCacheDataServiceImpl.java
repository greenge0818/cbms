package com.prcsteel.platform.smartmatch.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.common.service.impl.CommonCacheServiceImpl;
import com.prcsteel.platform.smartmatch.model.dto.CacheAccountDto;
import com.prcsteel.platform.smartmatch.persist.dao.CacheDao;
import com.prcsteel.platform.smartmatch.service.CacheManageService;
import com.prcsteel.platform.smartmatch.service.InitCacheDataService;

/**
 * Created by caochao on 2016/9/6.
 */
@Service("initCacheDataService")
public class InitCacheDataServiceImpl implements InitCacheDataService, CacheManageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //所有缓存key列表
    private List<String> CacheKeyList = new ArrayList<>();

    //服务中心缓存key
    public static final String CACHE_KEY_ORGANIZATION = "global_organizations";
    
    private final String CACHE_KEY_ACCOUNT = "global_accounts";
    //服务中心缓存超时时间
    private final int ORGANIZATION_EXP = 24 * 60 * 60;

    @Resource
    private CommonCacheServiceImpl commonCacheServiceImpl = null;

    @Resource
    private OrganizationDao organizationDao;
    
    @Resource
    private CacheDao cacheDao;

    /**
     * 初始化key列表
     */
    @PostConstruct
    private void InitKeyList() {
        CacheKeyList.add(CACHE_KEY_ORGANIZATION);
        CacheKeyList.add(CACHE_KEY_ACCOUNT);
    }

    @Override
    @PostConstruct
    public void InitOrganization() {
        try {
            List<Organization> list = organizationDao.queryAll();
            if (list != null) {
                commonCacheServiceImpl.set(CACHE_KEY_ORGANIZATION, ORGANIZATION_EXP, list);
            }
        } catch (Exception ex) {
            logger.error("初始化服务中心缓存失败:" + ex.toString());
        }
    }

    @Override
    @PostConstruct
    public void InitAccount() {
        try {
            List<CacheAccountDto> accountList = cacheDao.getAccountInfo();
            if(accountList != null){
                commonCacheServiceImpl.set(CACHE_KEY_ACCOUNT, ORGANIZATION_EXP, accountList);
            }
        } catch (Exception ex) {
            logger.error("初始化客户数据缓存失败:" + ex.toString());
        }
    }

    @Override
    public String getCacheKey() {
        return String.join(",", CacheKeyList);
    }

    @Override
    public void refreshCache(String key) {
    	if(StringUtils.equals(key, CACHE_KEY_ORGANIZATION)){
            //刷新服务中心
            List<Organization> list = organizationDao.queryAll();
            if (list != null) {
            	 commonCacheServiceImpl.set(key, ORGANIZATION_EXP, list);
            }
    	}else if (StringUtils.equals(key, CACHE_KEY_ACCOUNT)){
    		  //刷新客户信息
            List<CacheAccountDto> accountList = cacheDao.getAccountInfo();
            if(accountList != null){
            	commonCacheServiceImpl.set(key, ORGANIZATION_EXP, accountList);
            }
    	}
    }
}
