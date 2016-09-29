package com.prcsteel.platform.acl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.dto.ReportMailSettingDto;
import com.prcsteel.platform.acl.model.model.ReportMailSetting;
import com.prcsteel.platform.acl.persist.dao.ReportMailSettingDao;
import com.prcsteel.platform.acl.service.ReportMailSettingService;

@Service("reportMailSettingService")
public class ReportMailSettingServiceImpl implements ReportMailSettingService {
	private final static Logger logger = Logger.getLogger(ReportMailSettingServiceImpl.class);

	@Resource
	private ReportMailSettingDao reportMailSettingDao;
	
	@Override
	public ReportMailSettingDto selectByKey(long id) {
		return reportMailSettingDao.selectByPrimaryKey(id);
	}

	public List<ReportMailSettingDto> selectByParam(ReportMailSettingDto setting) {
		return reportMailSettingDao.selectByParam(setting);
	}

	@Override
	public void add(ReportMailSetting setting) {
		reportMailSettingDao.insert(setting);
	}

	@Override
	public boolean update(ReportMailSetting setting) {
		try {
			int num = reportMailSettingDao.updateByPrimaryKeySelective(setting);
			if(num>0)
				return true;
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean delete(long id) {
		try {
			reportMailSettingDao.delete(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public int selectByParamTotal(ReportMailSettingDto dto) {
		return reportMailSettingDao.totalByParam(dto);
	}

}
