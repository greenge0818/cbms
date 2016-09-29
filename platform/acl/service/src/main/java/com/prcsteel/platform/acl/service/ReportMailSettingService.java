package com.prcsteel.platform.acl.service;

import java.util.List;

import com.prcsteel.platform.acl.model.dto.ReportMailSettingDto;
import com.prcsteel.platform.acl.model.model.ReportMailSetting;

/**
 * 发送报表的邮件的service
 * @author tuxianming
 *
 */
public interface ReportMailSettingService {
	/**
	 * 根据id来查找一个邮件设置
	 * @param id
	 * @author tuxianming
	 * @return
	 */
	public ReportMailSettingDto selectByKey(long id);
	/**
	 * 根据条件来查询一堆邮件设置
	 * @param setting
	 * @author tuxianming
	 * @return
	 */
	public List<ReportMailSettingDto> selectByParam(ReportMailSettingDto setting);
	/**
	 * 新添加一个邮件设置
	 * @param setting
	 * @author tuxianming
	 * @return
	 */
	public void add(ReportMailSetting setting);
	/**
	 * 根据一条id来更新邮件设置
	 * @param setting
	 * @author tuxianming
	 * @return
	 */
	public boolean update(ReportMailSetting setting);
	/**
	 * 根据id删除一个邮件设置
	 * @param id
	 * @author tuxianming
	 * @return
	 */
	public boolean delete(long id);
	public int selectByParamTotal(ReportMailSettingDto dto);
}
