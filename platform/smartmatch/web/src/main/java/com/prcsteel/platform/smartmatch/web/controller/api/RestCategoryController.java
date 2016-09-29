package com.prcsteel.platform.smartmatch.web.controller.api;

import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.service.CategoryAttributeService;
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

/**
 * 调用Rest获取品名下相应大类，材质，规格信息
 *
 * @author peanut
 * @date 2016/6/21 11:44
 */
@RestController
@RequestMapping("/api")
public class RestCategoryController {

    //日志
    private static final Logger log =  LoggerFactory.getLogger(RestResourceController.class);
    // 接口返回值，成功
    public static final String RESULT_SUCCESS = "0";
    // 接口返回值，失败
    public static final String RESULT_ERROR = "1";

    @Resource
    private RestMarketToSmartMatchService restMarketToSmartMatchService;
    @Resource
    CategoryAttributeService categoryAttributeService;

    /**
     * 查询所有大类和品名及规格
     *
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    @RequestMapping(value = "/nsort/list.html", method = RequestMethod.GET)
    @ApiOperation("查询所有大类和品名及规格")
    public RestBaseDto selectAllNsortAndCategoryAndSpec() {
        return restMarketToSmartMatchService.selectAllNsortAndCategoryAndSpec();
    }

    /**
     * 查询所有品名对应的材质信息
     *
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    @RequestMapping(value = "/nsortmaterials/list.html", method = RequestMethod.GET)
    @ApiOperation("查询所有品名对应的材质信息")
    public RestBaseDto selectAllCategoryMaterials() {
        return restMarketToSmartMatchService.selectAllCategoryMaterials();
    }

    /**
     * 根据品名uuid获取对应品名下的钢厂
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @RequestMapping(value = "/factory/list.html", method = RequestMethod.GET)
    @ApiOperation("根据品名uuid获取对应品名下的钢厂")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "categoryUuid", value = "品名uuid,例：931cd39a-3e66-11e5-b8c0-54ee755150b2", dataType = "String", paramType = "query")
    )
    public RestBaseDto selectFactoryByCategoryUuid(String categoryUuid) {
        //如果参数为空，直接返回
        RestBaseDto restBaseDto = new  RestBaseDto();
        if(categoryUuid==null||"".equals(categoryUuid)){
            restBaseDto.setStatus(RESULT_ERROR);
            log.error("==========请求参数为空，直接返回.");
            return restBaseDto;
        }
        return restMarketToSmartMatchService.selectFactoryByCategoryUuid(categoryUuid);
    }

    /**
     * 根据品名uuid获取对应品名下的材质
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @RequestMapping(value = "/materials/list.html", method = RequestMethod.GET)
    @ApiOperation("根据品名uuid 获取对应品名下的材质")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "categoryUuid", value = "品名uuid,例：931cd39a-3e66-11e5-b8c0-54ee755150b2", dataType = "String", paramType = "query")
    )
    public RestBaseDto selectMaterialsByCategoryUuid(String categoryUuid) {
        //如果参数为空，直接返回
        RestBaseDto restBaseDto = new  RestBaseDto();
        if(categoryUuid==null||"".equals(categoryUuid)){
            restBaseDto.setStatus(RESULT_ERROR);
            log.error("==========请求参数为空，直接返回.");
            return restBaseDto;
        }
        return restMarketToSmartMatchService.selectMaterialsByCategoryUuid(categoryUuid);
    }

    /**
     * 根据品名uuid和材质uuid 获取对应的规格
     *
     * @param categoryUuid  品名uuid
     * @param materialsUuid 材质uuid
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @RequestMapping(value = "/spec/list.html", method = RequestMethod.GET)
    @ApiOperation("根据品名uuid和材质uuid 获取对应的规格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryUuid", value = "品名uuid,例：931cd39a-3e66-11e5-b8c0-54ee755150b2", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "materialsUuid", value = "材质uuid,例：968d8e3e4-3e69-11e5-b8c0-54ee755150b2", dataType = "String", paramType = "query")
    })
    public RestBaseDto selectSpecByCategoryUuidAndMaterialsUuid(String categoryUuid, String materialsUuid) {
        //如果参数为空，直接返回
        RestBaseDto restBaseDto = new  RestBaseDto();
        if(categoryUuid==null||"".equals(categoryUuid)||materialsUuid==null||"".equals(materialsUuid)){
            restBaseDto.setStatus(RESULT_ERROR);
            log.error("==========请求参数为空，直接返回.");
            return restBaseDto;
        }
        return restMarketToSmartMatchService.selectSpecByCategoryUuidAndMaterialsUuid(categoryUuid, materialsUuid);
    }
    /**
     * 根据品名id查询对应的属性集合
     *
     * @param categoryUuid  品名uuid
     * @return
     * @author zhoucai
     * @date 2016/6/23
     */
    @RequestMapping(value = "/categoryUuid/listAttribute.html", method = RequestMethod.GET)
    @ApiOperation("根据品名id查询对应的属性集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryUuid", value = "品名uuid,例：931cd39a-3e66-11e5-b8c0-54ee755150b2", dataType = "String", paramType = "query")
    })
    public RestBaseDto searchAttributeByCategoryUUId(String categoryUuid) {
        log.info("==========查询属性集合请求参数：categoryUuid="+categoryUuid);
        RestBaseDto result = new RestBaseDto();

        try {
            result.setStatus(RESULT_SUCCESS);
            //如果参数为空，直接返回
            if(categoryUuid==null||"".equals(categoryUuid)){
                result.setStatus(RESULT_ERROR);
                log.error("==========请求参数为空，直接返回.");
                return result;
            }
             result.setData(categoryAttributeService.searchAttributeByCategoryUuid(categoryUuid));
            log.info("=======接口调用成功返回属性集合信息为：" + categoryAttributeService.searchAttributeByCategoryUuid(categoryUuid).toString());
        } catch (Exception e) {
            log.error("=======接口调用失败:", e.getMessage());

            result.setStatus(RESULT_ERROR);
        }
        return result;
    }

}
