package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.enums.RiskControlType;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.acl.model.enums.SysSettingDiscription;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.AllHoliday;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.HolidayQuery;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.acl.persist.dao.AllHolidayDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.acl.service.SysSettingService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Rabbit Mao on 2015/8/6.
 */
@Service("sysSettingService")
public class SysSettingServiceImpl implements SysSettingService {
    private final static Logger logger = Logger.getLogger(SysSettingServiceImpl.class);

    @Resource
    SysSettingDao sysSettingDao;

    @Resource
    private FileService fileService;
    
    @Resource
    private AllHolidayDao allHolidayDao;


    public int add(SysSetting sysSetting, User user) {
        if (sysSetting == null) {
            return 0;
        } else {
            if (sysSetting.getSettingType().equals(SysSettingType.Transaction.getCode())) {
                SysSettingQuery query = new SysSettingQuery();
                query.setType(SysSettingType.Transaction.getCode());
                query.setValue(sysSetting.getSettingValue());
                if (sysSettingDao.selectByParam(query).size() == 0) {
                    sysSetting.setDefaultValue(sysSetting.getSettingValue());
                    sysSetting.setSettingType(SysSettingType.Transaction.getCode());
                    sysSetting.setSettingName(SysSettingType.Transaction.getName());
                    sysSetting.setDescription(SysSettingDiscription.Transaction.getName());
                    sysSetting.setDisplayName(SysSettingDiscription.Transaction.getName());
                    sysSetting.setCreated(new Date());
                    sysSetting.setCreatedBy(user.getLoginId());
                    sysSetting.setLastUpdated(new Date());
                    sysSetting.setLastUpdatedBy(user.getLoginId());
                    sysSetting.setModificationNumber(0);
                    if (sysSettingDao.insertSelective(sysSetting) > 0) {
                        return 1;
                    } else {
                        return -2;
                    }
                } else {
                    return -1;
                }
            }
        }
        return 1;
    }

    public boolean delete(Long id) {
        return sysSettingDao.deleteByPrimaryKey(id) > 0;
    }

    public List<SysSetting> queryByTypeAndValue(String type, String value) {
        SysSettingQuery query = new SysSettingQuery();
        query.setType(type);
        query.setValue(value);
        return sysSettingDao.selectByParam(query);
    }

    public int insertSelective(SysSetting record) {
        return sysSettingDao.insert(record);
    }

    public int updateByPrimaryKey(SysSetting record) {
        return sysSettingDao.updateByPrimaryKey(record);
    }

    //更新列表
    @Transactional
    public int updateByPrimaryKeySelective(SysSetting record) {
        SysSettingQuery query = new SysSettingQuery();
        query.setType(record.getSettingType());
        List<SysSetting> list = sysSettingDao.selectByParam(query);
        long settingCount = list.stream().filter(a -> !record.getId().equals(a.getId()) &&
                record.getSettingType().equals(a.getSettingType())).count();
        if (settingCount > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "已经存在相同类型的配置");
        }
        int flag;
        if ((flag = sysSettingDao.updateByPrimaryKeySelective(record)) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存失败");
        }
        return flag;
    }

    public List<SysSetting> selectByParam(SysSettingQuery query) {
        List<SysSetting> list = sysSettingDao.selectByParam(query);
        return list;
    }

    public int selectByParamTotal(SysSettingQuery query) {
        return sysSettingDao.selectByParamTotal(query);
    }

    public int updateBySettingTypeSelective(SysSetting record) {
        return sysSettingDao.updateBySettingTypeSelective(record);
    }

    public SysSetting queryBySettingType(String settingType) {
        List<SysSetting> res = queryByTypeAndValue(settingType, null);
        if (res != null && res.size() > 0) {
            return res.get(0);
        }
        return null;
    }

    /**
     * 获取系统默认模板
     *
     * @param type
     * @return
     */
    public String getSysTemplate(String type) {
        String content = null;

        SysSetting defaultTemplate = queryBySettingType(type);
        if (defaultTemplate != null && defaultTemplate.getSettingValue() != null) {
            content = readTemplateContent(defaultTemplate.getSettingValue());
        }

        return content;
    }

    /**
     * 读取模板内容
     *
     * @param key
     * @return
     */
    private String readTemplateContent(String key) {

        InputStream in = fileService.getFileData(key);

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return sb.toString();
    }

    @Override
    public SysSetting selectByPrimaryKey(Long id) {
        return sysSettingDao.selectByPrimaryKey(id);
    }
    
    @Override
    public SysSetting queryByType (String settingType){
    	return sysSettingDao.queryByType(settingType);
    }

	@Override
	public boolean modificationTime(Long id, String settingValue, Integer reportOrgDay, User user) {
		SysSetting sys = new SysSetting();
		sys.setLastUpdated(new Date());
		sys.setLastUpdatedBy(user.getName());
		sys.setReportOrgDay(reportOrgDay);
		sys.setSettingValue(settingValue);
		sys.setId(id);
		int num = sysSettingDao.updateByPrimaryKeySelective(sys);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}
    /**
     * 风控 -提示信息设置  查询
     *  -超期时间设置   查询
     *  -设置需要审核的交易凭证  查询
     */
    public List<SysSetting> queryClientType(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.CLIENT_TYPE.getCode());
        return sysSettingDao.querySysSetting(paramMap);


    }
    public List<SysSetting> queryExceedtime(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.EXCEEDTIME.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }
    public List<SysSetting> queryHint(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.HINT.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }
    public List<SysSetting> queryCustomerLabel(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.CUSTOMER_LABEL.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }
    public List<SysSetting> queryBillSetting(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.BILL_SETTING.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }

    public List<SysSetting> queryApplyPaymentContractSetting(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.APPLY_PAYMENT_CONTRACT_SETTING.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }

    public int perfectSysSetting(List<SysSetting> sysSettingList, SysSetting sysSetting, String user){
        int flag=this.delSysSetting();
        if(!addSysSetting(sysSetting,user).equals(OperateStatus.SUCCESS.ordinal())){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置超期时间设置失败！");
        }
        for(SysSetting sysS : sysSettingList){
            if(!addSysSetting(sysS,user).equals(OperateStatus.SUCCESS.ordinal())){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "设置信息失败");
            }
        }
        return flag;
    }
    @Override
    public Integer addSysSetting(SysSetting sysSetting,String user) {
        Integer flag = OperateStatus.FAIL.ordinal();
        sysSetting.setCreated(new Date());
        sysSetting.setCreatedBy(user);
        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(user);
        sysSetting.setModificationNumber(0);
        if (sysSettingDao.insert(sysSetting) > 0) {
            flag = OperateStatus.SUCCESS.ordinal();
        } else {
            flag = OperateStatus.DUPLICATE.ordinal();
        }
        return flag;
    }
    @Override
    public Integer delSysSetting() {
        Integer flag = OperateStatus.FAIL.ordinal();
        if (sysSettingDao.delSysSetting() > 0) {
            flag = OperateStatus.SUCCESS.ordinal();
        } else {
            flag = OperateStatus.DUPLICATE.ordinal();
        }
        return flag;
    }

	@Override
	public List<SysSetting> getControlPinSettings() {
		  Map<String, Object> paramMap = new HashMap<String, Object>();
	        paramMap.put("settingType", RiskControlType.CONTROLPINSETTINGS.getCode());
	        return sysSettingDao.querySysSetting(paramMap);
	}
    /**
     * 基础配置设置
     * @return
     */
    @Override
    public List<SysSetting> getBasicSettings(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("settingType", RiskControlType.BASIC_SETTINGS.getCode());
        paramMap.put("settingName", RiskControlType.PAYMEN_SORT.getCode());
        return sysSettingDao.querySysSetting(paramMap);

    }

    /**
     * 查询 支付名称是否存在
     * @param displayName
     * @return
     */
    @Override
    public  int  selectSavePaymentSort(String displayName){
        return sysSettingDao.selectSavePaymentSort(displayName);
    }

	@Override
	public List<SysSetting> getPayMentType() {
		 Map<String, Object> paramMap = new HashMap<String, Object>();
	     paramMap.put("settingType", RiskControlType.BASIC_SETTINGS.getCode());
	     paramMap.put("settingName", RiskControlType.PAYMEN_TYPE.getCode());
	     return sysSettingDao.querySysSetting(paramMap);
	}

	@Override
	public List<AllHoliday> selectAllHoliday(HolidayQuery holidayQuery) {
		return allHolidayDao.selectAll(holidayQuery);
	}

	@Override
	public int selectAllHolidayCount(HolidayQuery holidayQuery) {
		return allHolidayDao.selectAllCount(holidayQuery);
	}

	@Override
	public int saveHoliday(AllHoliday allHoliday) {
		AllHoliday holiday = allHolidayDao.selectByDate(allHoliday.getHolidayDate());
		if (holiday != null){
		  throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该日期已设置，请不要重复设置！");
		}
		if (StringUtils.isEmpty(allHoliday.getId())) {
			allHoliday.setId(UUID.randomUUID().toString());
			return allHolidayDao.insertSelective(allHoliday);
		} else {
			return allHolidayDao.updateByPrimaryKeySelective(allHoliday);
		}
	}

	@Override
	public AllHoliday selectById(String holidayId) {
		return allHolidayDao.selectByPrimaryKey(holidayId);
	}
}
