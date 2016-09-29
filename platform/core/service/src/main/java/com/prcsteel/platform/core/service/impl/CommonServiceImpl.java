package com.prcsteel.platform.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.dto.ProvinceCityRelationDto;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysFeedback;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.SysFeedbackDao;
import com.prcsteel.platform.common.service.SmsService;
import com.prcsteel.platform.core.model.dto.CityDto;
import com.prcsteel.platform.core.model.dto.DistrictDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.District;
import com.prcsteel.platform.core.model.model.Province;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.DistrictDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.core.service.CommonService;


/**
 * @author Green.Ge
 * @version V1.0
 * @Title: CommonServiceImpl.java
 * @Package com.prcsteel.platform.order.service.impl
 * @Description: 通用服务实现
 * @date 2015年7月14日 下午1:28:08
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Resource
    ProvinceDao provinceDao;
    @Resource
    CityDao cityDao;
    @Resource
    DistrictDao districtDao;

    @Resource
    OrganizationDao organizationDao;
    @Resource
    SysFeedbackDao sysFeedbackDao;
    @Value("${smsService}")
    private String smsServiceAddress;  // 短信服务地址
    @Value("${smsService.switch}")
    private boolean smsServiceSwitch;  // 短信发送开关

    private static final String KEY = "@gtxh@";
    private static final String FROM = "cbms";

    @Override
    public List<Province> getPrivinceList() {
        return provinceDao.listAll();
    }

    @Override
    public List<City> getCityListByProvinceId(Long provinceId) {
        return cityDao.listByProvinceId(provinceId);
    }

    @Override
    public List<District> getDistrictListByCityId(Long cityId) {
        return districtDao.listByCityId(cityId);
    }

    @Override
    public DistrictDto findDistrictById(Long id) {
        District district = districtDao.selectByPrimaryKey(id);

        DistrictDto dto = modelToDTO(district);
        CityDto city = findCityById(district.getCityId());
        dto.setCity(city);
        return dto;
    }

    private DistrictDto modelToDTO(District model) {
        DistrictDto dto = new DistrictDto();
        dto.setDistrict(model);
        dto.setCity(findCityById(model.getCityId()));
        return dto;
    }

    private CityDto modelToDTO(City model) {
        CityDto dto = new CityDto();
        dto.setCity(model);
//		BeanCopier.create(model.getClass(), dto.getClass(), false).copy(model, dto, null);
        dto.setProvince(provinceDao.selectByPrimaryKey(model.getProvinceId()));
        return dto;
    }

    @Override
    public CityDto findCityById(Long id) {
        City model = cityDao.selectByPrimaryKey(id);
        return modelToDTO(model);
    }

    @Override
    public List<Organization> getAllOrganization() {
        return organizationDao.queryAll();
    }

    /**
     * 发送短信
     *
     * @param phone   手机号码
     * @param content 发送内容
     * @return
     */
    @Override
    public Boolean sendSMS(String phone, String content) {
    	if(!smsServiceSwitch) return true;
        //创建客户端代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(SmsService.class);
        //设置地址
        factory.setAddress(smsServiceAddress);
        SmsService smsService = (SmsService) factory.create();
        String smsResult = smsService.sendMessage(KEY, phone, content, FROM);

        String status = "短信发送成功";
        if (status.equals(smsResult)) {
            return true;
        } else {
            logger.info("短信返回内容：{}", smsResult);
            return false;
        }
    }

	@Override
	public boolean saveFeedBack(SysFeedback sysFeedback) {
		return sysFeedbackDao.insertSelective(sysFeedback)>0;
	}
	/**
	 * 获取所有城市
	 * @return
	 */
	@Override
	public List<City> allCity() {
		return cityDao.getAllCity();
	}

    /**
     * @description获取所有城市-省关系
     * @author：zhoucai
     * @date ：2016-6-23
     * @return
     */
    @Override
    public List<ProvinceCityRelationDto> getProvinceCityRelation(){
        return cityDao.getProvinceCityRelation();
    }

}
 