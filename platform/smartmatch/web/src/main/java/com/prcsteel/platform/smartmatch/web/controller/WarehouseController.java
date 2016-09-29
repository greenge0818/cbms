package com.prcsteel.platform.smartmatch.web.controller;

import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseDto;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.WarehouseQuery;

import com.prcsteel.platform.smartmatch.service.WarehouseService;

import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Rolyer on 2015/11/12.
 */
@Controller
@RequestMapping("/smartmatch/warehouse/")
public class WarehouseController extends BaseController {
    @Resource
    private WarehouseService warehouseService;

    /**
     * 仓库管理首页
     */
    @RequestMapping("index")
    public String index(ModelMap out) {

        return "smartmatch/warehouse/index";
    }

    /**
     * 查询/加载仓库数据
     *
     * @param name
     * @param area
     * @return
     */
    @RequestMapping("search.html")
    public
    @ResponseBody
    PageResult search(WarehouseQuery warehouseQuery) {
        Integer total = warehouseService.countByWarehouseNameAndAera(warehouseQuery);

        List<WarehouseDto> list = warehouseService.queryByConditions(warehouseQuery);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(total);
        result.setPageSize(list.size());
        return result;
    }

    /**
     * 删除仓库
     *
     * @param id
     * @return
     */
    @RequestMapping("del.html")
    @ResponseBody
    public Result del(String id) {
        Result result = new Result();
        try {
            warehouseService.deleteById(Long.parseLong(id));
            result.setSuccess(true);
            result.setData("删除成功！");
        }catch(BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 新建/修改仓库信息
     *
     * @param warehouse
     * @return
     */
    @RequestMapping("save.html")
    @ResponseBody
    public Result save(Warehouse warehouse) {
        Result result = new Result();
        User user = getLoginUser();
        try {
            if (warehouse.getId() == null) {
                warehouseService.add(warehouse,user);
            } else {
                warehouseService.update(warehouse,user);
            }
            result.setSuccess(true);
            result.setData("保存成功！");
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 获取单个仓库信息（用于初始化修改仓库信息的Form表单）
     *
     * @param id
     * @return
     */
    @RequestMapping("edit.html")
    @ResponseBody
    public Result query(Long id) {
        Result result = new Result();
        Warehouse ware = warehouseService.query(id);
        result.setData(ware);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    // 获取仓库列表
    @ResponseBody
    @RequestMapping(value = "getAllWarehouse", method = RequestMethod.POST)
    public Result getWarehouse() {
        Result result = new Result();
        result.setData(warehouseService.getAllWarehouse());
        return result;
    }

    // 获取所有的仓库用于首字母查找
    @ResponseBody
    @RequestMapping(value = "getAllWarehouseForPuzzyMatch", method = RequestMethod.POST)
    public Result getAllWarehouseForPuzzyMatch() {
        return new Result(warehouseService.getAllWarehouseForPuzzyMatch());
    }
}
