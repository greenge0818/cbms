package com.prcsteel.platform.smartmatch.web.controller.api;

import com.prcsteel.platform.core.model.dto.ProvinceCityRelationDto;
import com.prcsteel.platform.core.service.CommonService;
import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;
import com.prcsteel.platform.smartmatch.service.RestMarketToSmartMatchService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description:查询城市Rest接口
 * @author: zhoucai@prcsteel.com
 * @date :2016-6-20
 * @version  :1.0
 */
@RestController
@RequestMapping("/api/city/")
public class RestCityQueryController {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RestCityQueryController.class);
    // 接口返回值，成功
    public static final String RESULT_SUCCESS = "0";
    // 接口返回值，失败
    public static final String RESULT_ERROR = "1";

    @Resource
    private CommonService commonService;

    @Resource
    private RestMarketToSmartMatchService restMarketToSmartMatchService;

    /**
     * 查询所有的城市信息
     *
     * @return Result
     */
    @RequestMapping(value = "allcity.html", method = RequestMethod.GET)
    @ApiOperation("查询全部城市信息")
    public RestBaseDto allCity() {
        RestBaseDto result = new RestBaseDto();
        result.setStatus(RESULT_SUCCESS);
        try {
            List<ProvinceCityRelationDto> citylist = commonService.getProvinceCityRelation();
            log.info("=======接口调用成功返回城市信息列表为：" + citylist.toString());
            result.setData(citylist);

        } catch (Exception e) {
            log.error("=======接口调用失败:", e.getMessage());
            result.setStatus(RESULT_ERROR);
        }
        return result;
    }

    /**
     * 根据地区分组获取所有中心城市
     *
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    @RequestMapping(value = "/group.html", method = RequestMethod.GET)
    @ApiOperation("根据地区分组获取所有中心城市")
    public RestCitysDto selectAllCenterCityGroupByZone() {
        return restMarketToSmartMatchService.selectAllCenterCityGroupByZone();
    }

    /**
     * 根据城市名称获取所有中心城市
     *
     * @param cityName 城市名称
     * @return
     * @author peanut
     * @date 2016/6/22
     */
    @RequestMapping(value = "/center.html", method = RequestMethod.GET)
    @ApiOperation("根据城市名称获取所有中心城市")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityName", value = "城市名称,例:杭州", dataType = "String", paramType = "query")
    })
    public RestBaseDto selectCenterCitysByCityName(String cityName) {
        return restMarketToSmartMatchService.selectCenterCitysByCityName(cityName);
    }
}
