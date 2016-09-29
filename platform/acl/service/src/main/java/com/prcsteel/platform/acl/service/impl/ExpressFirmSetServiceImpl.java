package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.dto.BaseOrganizationDeliverDto;
import com.prcsteel.platform.acl.model.model.BaseDeliver;
import com.prcsteel.platform.acl.model.model.BaseOrganizationDeliver;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.query.BaseDeliverQuery;
import com.prcsteel.platform.acl.persist.dao.BaseDeliverDao;
import com.prcsteel.platform.acl.persist.dao.BaseOrganizationDeliverDao;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.service.ExpressFirmSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
 * 快递 公司 设置
 * @author wangxiao
 */
@Service("expressFirmSetService")
public class ExpressFirmSetServiceImpl implements ExpressFirmSetService {
	@Resource
	public BaseDeliverDao baseDeliverDao;
	@Resource
	public BaseOrganizationDeliverDao baseOrganizationDeliverDao;

	@Resource
	public OrganizationDao organizationDao;
	@Override
    public 	Boolean deleteByPrimaryKey(Long id){
		List<Organization> list = organizationDao.queryAllBusinessOrg();
		int number =baseDeliverDao.deleteByPrimaryKey(id);
		if(number>0){
			baseOrganizationDeliverDao.deleteByPrimaryKey(id);
			return true;
		}else{
			return false;
		}
	}
	@Override
	public int insert(BaseDeliver record){
		return baseDeliverDao.insert(record);
	}
	@Override
	public Boolean insertSelective(BaseDeliver record){
		List<Organization> list = organizationDao.queryAllBusinessOrg();
         int number =baseDeliverDao.insertSelective(record);
          if(number>0){
			  BaseOrganizationDeliver baseOrganizationDeliver=null;
			  for(Organization org:list){
				  baseOrganizationDeliver=new BaseOrganizationDeliver();
				  baseOrganizationDeliver.setOrgId(org.getId());
				  baseOrganizationDeliver.setOrgName(org.getName());
				  baseOrganizationDeliver.setDeliverId(baseDeliverDao.selectByPrima(record.getName()).getId());
				  baseOrganizationDeliver.setDeliverDays(1);
				  baseOrganizationDeliver.setCreated(new Date());
				  baseOrganizationDeliver.setCreatedBy(record.getCreatedBy());
				  baseOrganizationDeliver.setLastUpdated(new Date());
				  baseOrganizationDeliver.setLastUpdatedBy(record.getCreatedBy());
				  baseOrganizationDeliverDao.insertSelective(baseOrganizationDeliver);
			  }
			  return true;
		  }else{
			  return false;
		  }

	}
	@Override
	public BaseDeliver selectByPrimaryKey(Long id){
		return baseDeliverDao.selectByPrimaryKey(id);
	}
	@Override
	public int updateByPrimaryKeySelective(BaseDeliver record){
         return baseDeliverDao.updateByPrimaryKeySelective(record);
	}
	@Override
	public int updateByPrimaryKey(BaseDeliver record){
		return baseDeliverDao.updateByPrimaryKey(record);
	}
	@Override
	public List<BaseDeliver>findByPrimary(BaseDeliverQuery record){
		return baseDeliverDao.findByPrimary(record);
	}
	@Override
	public BaseDeliver selectByPrima(String name){


		return  baseDeliverDao.selectByPrima(name);
	}
	//通过服务中心id查询服务中心对应的快递时间
	@Override
	public  List<BaseOrganizationDeliverDto> selectByOrgId(Long orgId){
		return baseOrganizationDeliverDao.selectByOrgId(orgId);
	}

	//根据服务中心和快递id更新服务中心快递时间表
	@Override
	public int batchUpdateOrgDeliver(List<BaseOrganizationDeliver> baseOrganizationDeliver){
		return baseOrganizationDeliverDao.batchUpdateOrgDeliver(baseOrganizationDeliver);
	}
}
