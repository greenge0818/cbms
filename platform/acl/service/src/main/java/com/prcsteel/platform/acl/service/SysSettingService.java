package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.model.AllHoliday;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.HolidayQuery;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by Rabbit Mao on 2015/8/6.
 */
public interface SysSettingService {
    int add(SysSetting sysSetting, User user);

    boolean delete(Long id);

    List<SysSetting> queryByTypeAndValue(String type, String value);

    //@Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_SETTING_TYPE + "'+#p0")
    SysSetting queryBySettingType(String settingType);

    int insertSelective(SysSetting record);

    int updateByPrimaryKey(SysSetting record);

    int updateByPrimaryKeySelective(SysSetting record);

    int updateBySettingTypeSelective(SysSetting record);

    List<SysSetting> selectByParam(SysSettingQuery query);

    int selectByParamTotal(SysSettingQuery query);

    String getSysTemplate(String type);

    SysSetting selectByPrimaryKey(Long id);
    
    SysSetting queryByType(String settingType);
    
    boolean modificationTime(Long id, String settingValue, Integer reportOrgDay, User user);

    /**
     * 风控 -提示信息设置  查询
     *  -超期时间设置   查询
     *  -设置需要审核的交易凭证  查询
     *  customer_label
     */
    List<SysSetting> queryClientType();

    List<SysSetting> queryExceedtime();

    List<SysSetting> queryHint();

    List<SysSetting> queryCustomerLabel();

    List<SysSetting> queryBillSetting();

    List<SysSetting> queryApplyPaymentContractSetting();
    
    List<SysSetting> getControlPinSettings();

    Integer addSysSetting(SysSetting sysSetting,String user);

    int perfectSysSetting(List<SysSetting> sysSettingList, SysSetting sysSetting, String user);

    Integer delSysSetting();

    /**
     * 基础配置设置
     * @return
     */
    List<SysSetting> getBasicSettings();

    /**
     * 查询 支付名称是否存在
     * @param displayName
     * @return
     */
   int  selectSavePaymentSort(String displayName);
   
   /**
    * 基础配置设置--支付类型
    * @return
    */
   List<SysSetting> getPayMentType();
   
   /**
    * 查询所有的节假日
    * @return
    */
   List<AllHoliday> selectAllHoliday(HolidayQuery holidayQuery);
   
   /**
    * 查询所有的节假日记录数
    * @return
    */
   int selectAllHolidayCount(HolidayQuery holidayQuery);
   
   /**
    * 保存假期
    * @param allHoliday
    * @return
    */
   int saveHoliday(AllHoliday allHoliday);
   
   /**
    * 通过id查询假期
    * @param holidayId
    * @return
    */
   AllHoliday selectById(String holidayId);
}
