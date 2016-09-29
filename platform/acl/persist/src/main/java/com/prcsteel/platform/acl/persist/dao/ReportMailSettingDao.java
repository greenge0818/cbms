package com.prcsteel.platform.acl.persist.dao;

import java.util.List;

import com.prcsteel.platform.acl.model.dto.ReportMailSettingDto;
import com.prcsteel.platform.acl.model.model.ReportMailSetting;

public interface ReportMailSettingDao {

	public ReportMailSettingDto selectByPrimaryKey(long id);
	public void delete(long id);
	public void insert(ReportMailSetting setting);
	public int updateByPrimaryKeySelective(ReportMailSetting setting);
	public List<ReportMailSettingDto> selectByParam(ReportMailSettingDto query);
	public int totalByParam(ReportMailSettingDto query);
}