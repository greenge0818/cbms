package com.prcsteel.platform.smartmatch.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
import com.prcsteel.platform.smartmatch.persist.dao.BasePriceRelationDao;
import com.prcsteel.platform.smartmatch.persist.dao.BasePriceRelationDetailDao;

/**
 * Created by dq on 2016/2/15.
 */
public class BasePriceRelationServiceTest extends BaseTest {

	@Resource
	private BasePriceRelationService basePriceRelationService;
	
	private BasePriceRelationDao basePriceRelationDao = null;
	
	private BasePriceRelationDetailDao detailDao = null;
	
	@Before
    public void before() {
		basePriceRelationDao = EasyMock.createMock(BasePriceRelationDao.class);
		detailDao = EasyMock.createMock(BasePriceRelationDetailDao.class);
		
		ReflectionTestUtils.setField(basePriceRelationService, "basePriceRelationDao", basePriceRelationDao);
		ReflectionTestUtils.setField(basePriceRelationService, "detailDao", detailDao);
		
	}
	
	@After
    public void after() {
		basePriceRelationDao = null;
		detailDao = null;
    }
	@Test
	public void testSearchBasePriceRelation() {
		CustBasePriceRelationQuery query = new CustBasePriceRelationQuery();
		List<CustBasePriceRelation> lations = new ArrayList<CustBasePriceRelation>();
		
		CustBasePriceRelation lation = new CustBasePriceRelation();
		lation.setId(1L);
		lation.setAccountId(9088L);
		lation.setAccountName("sy卖家");
		lation.setBasePriceId(1L);
		lation.setCityId(78L);
		lation.setCityName("杭州市");
		lation.setBaseRelationDetailCount(5);
		lations.add(lation);
		EasyMock.expect(basePriceRelationDao.getBasePriceRelationList(query)).andReturn(lations);
		
		EasyMock.replay(basePriceRelationDao);//执行测试
		basePriceRelationService.getBasePriceRelationList(query);
		
		EasyMock.verify(basePriceRelationDao);
	}
	@Test
	public void testUpdateBasePriceRelation() {
		CustBasePriceRelationDto dto = new CustBasePriceRelationDto();
		dto.setId(1L);
		dto.setAccountId(9088L);
		dto.setAccountName("sy卖家");
		dto.setBasePriceId(1L);
		dto.setCityId(78L);
		dto.setCityName("杭州市");
		
		CustBasePriceRelationDetailDto items = new CustBasePriceRelationDetailDto();
		items.setBasePriceRelationId(dto.getId());
		items.setIsDeficiencyInventory(false);
		items.setMaterialUuid("68d7d2cf-3e69-11e5-b8c0-54ee755150b2");
		items.setMaterialUuid("SAPH400");
		items.setSpec("12*190");
		items.setPriceDeviation(new BigDecimal(120));
		List<CustBasePriceRelationDetailDto> details = new ArrayList<CustBasePriceRelationDetailDto>();
		details.add(items);
		
		dto.setDetails(details);
		User user = new User();
		user.setLoginId("admin");
		
		EasyMock.expect(basePriceRelationDao.updateByPrimaryKeySelective(dto)).andReturn(1);
		EasyMock.replay(basePriceRelationDao);//执行测试
		
		EasyMock.expect(detailDao.deleteDetailsByRelationId(items.getBasePriceRelationId())).andReturn(1);
		EasyMock.expect(detailDao.insertSelective(items)).andReturn(1);
		EasyMock.replay(detailDao);//执行测试
		
		try {
			basePriceRelationService.saveBasePriceRelation(dto,user);
        } catch (Exception e) {
        	Assert.fail("修改基价设置失败！！"+e.getMessage());
        }
		EasyMock.verify(basePriceRelationDao);
		EasyMock.verify(detailDao);
	
	}
	
	@Test
	public void testAddBasePriceRelation() {
		CustBasePriceRelationDto dto = new CustBasePriceRelationDto();
		dto.setAccountId(9088L);
		dto.setAccountName("sy卖家");
		dto.setBasePriceId(1L);
		dto.setCityId(78L);
		dto.setCityName("杭州市");
		
		CustBasePriceRelationDetailDto items = new CustBasePriceRelationDetailDto();
		items.setBasePriceRelationId(1L);
		items.setIsDeficiencyInventory(false);
		items.setMaterialUuid("68d7d2cf-3e69-11e5-b8c0-54ee755150b2");
		items.setMaterialUuid("SAPH400");
		items.setSpec("12*190");
		items.setPriceDeviation(new BigDecimal(120));
		List<CustBasePriceRelationDetailDto> details = new ArrayList<CustBasePriceRelationDetailDto>();
		details.add(items);
		
		dto.setDetails(details);
		User user = new User();
		user.setLoginId("admin");
		CustBasePriceRelation result = null;
		
		EasyMock.expect(basePriceRelationDao.getBasePriceRelationByParams(dto)).andReturn(result);
		EasyMock.expect(basePriceRelationDao.insertSelective(dto)).andReturn(1);
		EasyMock.replay(basePriceRelationDao);//执行测试
		
		EasyMock.expect(detailDao.insertSelective(items)).andReturn(1);
		EasyMock.replay(detailDao);//执行测试
		
		try {
			basePriceRelationService.saveBasePriceRelation(dto,user);
        } catch (Exception e) {
        	Assert.fail("新增基价设置失败！！"+e.getMessage());
        }
		EasyMock.verify(basePriceRelationDao);
	}
	
	@Test
	public void testDeleteBasePriceRelation() {
		Long id = 1L;
		EasyMock.expect(basePriceRelationDao.deleteByPrimaryKey(id)).andReturn(1);
		EasyMock.replay(basePriceRelationDao);//执行测试
		try {
			basePriceRelationService.deleteBasePriceRelation(1L);
        } catch (Exception e) {
        	Assert.fail("删除失败！！"+e.getMessage());
        }
		EasyMock.verify(basePriceRelationDao);
	}
	
}
