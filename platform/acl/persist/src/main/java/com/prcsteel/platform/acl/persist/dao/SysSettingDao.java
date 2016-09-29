package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface SysSettingDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysSetting record);

    int insertSelective(SysSetting record);

    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_SETTING_ID + "'+#p0")
    SysSetting selectByPrimaryKey(Long id);

    @CacheEvict(value = Constant.CACHE_NAME, allEntries=true)
    int updateByPrimaryKeySelective(SysSetting record);

    @CacheEvict(value = Constant.CACHE_NAME, allEntries=true)
    int updateByPrimaryKey(SysSetting record);

    List<SysSetting> selectByParam(SysSettingQuery query);

    int selectByParamTotal(SysSettingQuery query);

    @CacheEvict(value = Constant.CACHE_NAME, allEntries=true)
    int updateBySettingTypeSelective(SysSetting record);
    
    SysSetting queryByType(String settingType);

    String  queryCurrentCreditLimit();
    //add by wangxianjun 通过客户标示，查询设置的凭证名称
    String  queryCertNameByLabel(Map map);

    /**
     * settingType  settingName  defaultValue
     * @param paramMap
     * @return
     */
    public List<SysSetting> querySysSetting(Map<String, Object> paramMap);
    int delSysSetting();

    /**
     * 查询 支付名称是否存在
     * @param displayName
     * @return
     */
    int  selectSavePaymentSort(String displayName);

    /**
     * 按设置名称查询 付款申请是否需要上传合同
     * @param settingName
     * @author chengui
     * @return
     */
    String querySettingValueByName(@Param("settingName") String settingName);
}
