package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.dto.BaseOrganizationDeliverDto;
import com.prcsteel.platform.acl.model.model.BaseDeliver;
import com.prcsteel.platform.acl.model.model.BaseOrganizationDeliver;
import com.prcsteel.platform.acl.model.query.BaseDeliverQuery;

import java.util.List;

/**
 * 快递 公司 设置
 * @author wangxiao
 */
public interface ExpressFirmSetService {
	Boolean deleteByPrimaryKey(Long id);

	int insert(BaseDeliver record);

	Boolean insertSelective(BaseDeliver record);

	BaseDeliver selectByPrimaryKey(Long id);

	BaseDeliver selectByPrima(String name);


	int updateByPrimaryKeySelective(BaseDeliver record);

	int updateByPrimaryKey(BaseDeliver record);

	List<BaseDeliver>findByPrimary(BaseDeliverQuery record);

	//通过服务中心id查询服务中心对应的快递时间
	List<BaseOrganizationDeliverDto> selectByOrgId(Long orgId);
	//根据服务中心和快递id更新服务中心快递时间表
	int batchUpdateOrgDeliver(List<BaseOrganizationDeliver> baseOrganizationDeliver);
}
