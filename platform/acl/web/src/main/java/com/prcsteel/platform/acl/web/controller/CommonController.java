package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.service.PermissionService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.District;
import com.prcsteel.platform.core.model.model.Province;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.core.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common/")
public class CommonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private CommonService commonService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private PermissionService permissionService;

    //获取省列表
    @RequestMapping("privincelist")
    @ResponseBody
    public List<Province> getPrivinceList() {
        return commonService.getPrivinceList();
    }

    //获取所有城市
    @RequestMapping("allcity")
    @ResponseBody
    public List<City> allCity() {
        return commonService.allCity();
    }

    //根据省份ID获取城市列表
    @RequestMapping("citylist")
    @ResponseBody
    public List<City> getCityListByProvinceId(Long provinceId) {
        return commonService.getCityListByProvinceId(provinceId);
    }

    //根据城市ID获取区域列表
    @RequestMapping("districtlist")
    @ResponseBody
    public List<District> getDistrictListByCityId(Long cityId) {
        return commonService.getDistrictListByCityId(cityId);
    }

    /**
     * 获得客户端真实IP地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /*
     * 左侧菜单页自动按用户权限跳转
     * 使用：将原先的菜单地址修改为/common/menu/menu_code/tab_code.html
     * menu code中的冒号使用-号代替，menu code就是菜单对应的权限code
     * tab code是想点击这个链接时首选的跳转的tab页对应的权限code,如果用户没有该tab页权限，则跳转到该用户在该菜单下有权限的第一个tab页
     */
    @RequestMapping("menu/{menu}/{tab}")
    public String tabSelect(
            @PathVariable("menu") String menu,
            @PathVariable("tab") String tab,
            ModelMap out) {
        out.remove(Constant.LOGOUT_URL);
        menu = menu.toLowerCase().replace("-", ":");
        tab = tab.toLowerCase().replace("-", ":");
        // 查找用户在该menu下有哪些tab页权限
        List<Permission> allPermission = getUserPermissionList();
        // 查找父菜单和父菜单下的期望菜单
        Permission parentPagePerm = permissionService.queryByCodeAndUrl(menu, null);
        Permission expectPagePerm = permissionService.queryByCodeAndUrl(tab, null);
        if (parentPagePerm == null || expectPagePerm == null) {
            return "notfound";
        }
        String resultUrl = null;
        // 优先选择以page结束的页面
        boolean isView = false;
        // 匹配用户权限与菜单，优先选择期望菜单
        for (Permission p : allPermission) {
            if (p.getCode().equals(expectPagePerm.getCode())) {
                resultUrl = String.format("redirect:%s", expectPagePerm.getUrl());
                break;
            }
            if ((resultUrl == null || isView) && p.getParentId().equals(parentPagePerm.getId())) {
                resultUrl = String.format("redirect:%s", p.getUrl());
                isView = p.getCode().toLowerCase().endsWith(Constant.PERMISSION_VIEW_SUFFIX);
            }
        }
        // 默认的无权限页面
        if (resultUrl == null) {
            return "unauth";
        }
        return resultUrl;
    }

    @RequestMapping("showimage")
    public String showImg(ModelMap out, String src) {
        String[] srcs = {};
        if (src != null) {
            srcs = src.split(",");
        }

        out.put("srcs", srcs);
        return "showimage";
    }

    /**
     * 搜索品类
     */
    @ResponseBody
    @RequestMapping("getnsort")
    public Result getNsort() {
        Map<String, Object> resultData = new HashMap<>();
        Result result = new Result();
        List<Map<String, Object>> data = categoryService.queryAllCategoryData();
        if (data != null && data.size() > 0) {
            resultData.put("statusCode", 0);
            resultData.put("Massage", "请求成功");
            resultData.put("total", data.size());
            resultData.put("isLogin", false);
            resultData.put("data", data);
            result.setSuccess(true);
            result.setData(resultData);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 获取全部服务中心列表
     *
     * @return
     */
    @RequestMapping(value = "organizationList.html", method = RequestMethod.POST)
    @ResponseBody
    public Result getAllOrganization() {
        Result result = new Result();
        List<Organization> organizationList = organizationService.getAllOrganization();
        Organization organization = new Organization();
        organization.setId(Long.valueOf(-1));    //树状结构顶点为0，所以不能设置为0
        organization.setParentId(Long.valueOf(0));
        organization.setName("无");
        organizationList.add(organization);
        result.setData(organizationList);
        result.setSuccess(true);
        return result;
    }
}
