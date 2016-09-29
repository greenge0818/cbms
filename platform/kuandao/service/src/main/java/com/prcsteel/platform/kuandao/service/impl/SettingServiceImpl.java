package com.prcsteel.platform.kuandao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.kuandao.common.constant.PrcsteelAccount;
import com.prcsteel.platform.kuandao.model.dto.BasesetDto;
import com.prcsteel.platform.kuandao.model.dto.JobCronDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoJobCronDto;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;
import com.prcsteel.platform.kuandao.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService {

	private static final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);

	private static final String SETTING_SMS_MOBILE = "KuandaoSmsMobile";

	private static final String SETTING_SMS_NAME = "异常情况联系人电话";
	
	private static final String SETTING_EMAIL_TOADDRESS = "KuandaoEmailtoAddress";
	
	private static final String SETTING_EMAIL_NAME = "异常情况联系人邮箱";
	
	private static final String SETTING_LIMIT_BANK = "KuandaoLimitBank";
	

	
	@Resource
	private PrcsteelAccount prcsteelAccount;
	
	@Resource
	private SysSettingService sysSettingService;
	
	@Value("${quartz.job.kuandao.syncKuandaoAccountJob.cron}")
	private String accountSyncJobCron;
	
	@Value("${quartz.job.kuandao.queryDailyBillJob.cron}")
	private String dailyBillJobCron;
	
	@Value("${quartz.job.kuandao.submitPaymentOrderJob.cron}")
	private String depositJobCron;
	
	
	@Override
	public List<PrcsteelAccountInfo> queryPrcsteelAccount(Integer start, Integer length) {
		List<PrcsteelAccountInfo> list = Lists.newArrayList();
		PrcsteelAccountInfo prcsteelAccountInfo = new PrcsteelAccountInfo();;
		prcsteelAccountInfo.setMemeberCode(prcsteelAccount.getMemeberCode());
		prcsteelAccountInfo.setMemeberName(prcsteelAccount.getMemeberName());
		prcsteelAccountInfo.setBankName(prcsteelAccount.getBankName());
		prcsteelAccountInfo.setVirAcctNo(prcsteelAccount.getVirAcctNo());
		prcsteelAccountInfo.setAcctNo(prcsteelAccount.getAcctNo());
		prcsteelAccountInfo.setIdNo(prcsteelAccount.getIdNo());
		prcsteelAccountInfo.setMobile(prcsteelAccount.getMobile());
		list.add(prcsteelAccountInfo);
		return list;
	}

	@Override
	public BasesetDto queryBaseset() {
		
		BasesetDto  basesetDto = new BasesetDto();
		KuandaoJobCronDto jobCronDto = new KuandaoJobCronDto();
		jobCronDto.setAccountSyncJobCron(parseJobCron(accountSyncJobCron));
		jobCronDto.setDailyBillJobCron(parseJobCron(dailyBillJobCron));
		jobCronDto.setDepositJobCron(parseJobCron(depositJobCron));
		
		List<SysSetting> mobileSettings = sysSettingService.queryByTypeAndValue(SETTING_SMS_MOBILE, null);
		List<SysSetting> emailSettings = sysSettingService.queryByTypeAndValue(SETTING_EMAIL_TOADDRESS, null);
		List<SysSetting> limitBankSettings = sysSettingService.queryByTypeAndValue(SETTING_LIMIT_BANK, null);
		
		basesetDto.setMobileList(mobileSettings);
		basesetDto.setEmailList(emailSettings);
		basesetDto.setLimitBankList(limitBankSettings);
		basesetDto.setJobCron(jobCronDto);
		return basesetDto;
	}

	/**
	 * spring cron解析
	 * @param cron
	 * @return
	 */
	private JobCronDto parseJobCron(String cron){
		JobCronDto jobCronDto = null;
		if(StringUtils.isNotEmpty(cron)){
			String[] items = cron.split(" ");
			if(items.length < 6){
				logger.error(String.format("parse cron failed cause by incorrect cron %s", cron));
			}else{
				jobCronDto = new JobCronDto();
				jobCronDto.setSecond(items[0]);
				String minute = items[1];
				int minutePos = minute.indexOf('/');
				if(minutePos > 0){
					minute = minute.substring(minutePos + 1, minute.length());
				}
				jobCronDto.setMinute(minute);
				jobCronDto.setHour(items[2]);
				jobCronDto.setDay(items[3]);
				jobCronDto.setMonth(items[4]);
				jobCronDto.setDayOfWeek(items[5]);
				if(items.length == 7){
					jobCronDto.setYear(items[5]);
				}
			}
		}
		return jobCronDto;
	}

	@Override
	public Integer modifyBaseset(String[] email, String[] phonenumber, String[] limitbankname, String[] limitbankID,String username) {
		
		//删除全部配置
		List<SysSetting> deleteSettings = sysSettingService.queryByTypeAndValue(SETTING_SMS_MOBILE, null);
		deleteSettings.addAll(sysSettingService.queryByTypeAndValue(SETTING_EMAIL_TOADDRESS, null));
		deleteSettings.addAll(sysSettingService.queryByTypeAndValue(SETTING_LIMIT_BANK, null));
		deleteSettings.forEach(a -> sysSettingService.delete(a.getId()));
		
		String[] phonenumberName = new String[phonenumber.length];
		for(int i = 0; i < phonenumberName.length; i++){
			phonenumberName[i] = SETTING_SMS_NAME;
		}
		String[] emailName = new String[email.length];
		for(int i = 0; i < emailName.length; i++){
			emailName[i] = SETTING_EMAIL_NAME;
		}
		List<SysSetting> addSettings = generateSetting(SETTING_SMS_MOBILE,phonenumber,phonenumberName,username);
		addSettings.addAll(generateSetting(SETTING_EMAIL_TOADDRESS,email,emailName,username));
		addSettings.addAll(generateSetting(SETTING_LIMIT_BANK,limitbankID,limitbankname,username));
		addSettings.forEach(setting -> sysSettingService.insertSelective(setting));
		return KuandaoResultEnum.success.getCode();
	}

	/**
	 * 
	 * @param type
	 * @param value
	 * @param name
	 * @return
	 */
	private List<SysSetting> generateSetting(String type, String[] value, String[] name,String username) {
		List<SysSetting> sysSettings = Lists.newArrayList();
		for(int i = 0; i < value.length; i++){
			if(StringUtils.isEmpty(value[i])){
				continue;
			}
			SysSetting sysSetting = new SysSetting();
			sysSetting.setSettingType(type);
			sysSetting.setSettingValue(value[i]);
			sysSetting.setDefaultValue(value[i]);
			sysSetting.setSettingName(name[i]);
			sysSetting.setCreated(new Date());
			sysSetting.setCreatedBy(username);
			sysSetting.setLastUpdated(new Date());
			sysSetting.setLastUpdatedBy(username);
			sysSetting.setModificationNumber(0);
			sysSettings.add(sysSetting);
		}
		return sysSettings;
	}
}
