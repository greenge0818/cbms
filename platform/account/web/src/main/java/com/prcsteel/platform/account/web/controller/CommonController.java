package com.prcsteel.platform.account.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prcsteel.platform.account.api.RestPermissionService;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.District;
import com.prcsteel.platform.core.model.model.Province;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.core.service.CommonService;

@Controller
@RequestMapping("/common/")
public class CommonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private static final String IMAGE_SUFFIX = "[jpg][gif][png][bmp][jpeg]";

    @Resource
    private CommonService commonService;
	@Resource
    private CategoryService categoryService;
    @Resource
    private FileService fileService;
    @Resource
    RestPermissionService restPermissionService;
    @Resource
    AccountService accountService;

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

    /**
     * 文件下载，如果是图片则自动设置content type为image
     *
     * @param response
     * @param key
     */
    @RequestMapping("getfile")
    public void getFile(HttpServletResponse response, String key) {
        InputStream inStream = null;
        OutputStream ostream = null;
        try {
            inStream = fileService.getFileData(key);
            ostream = response.getOutputStream();

            // set content type
            String suffix = FileUtil.getFileSuffix(key).toLowerCase();
            if (IMAGE_SUFFIX.contains("[" + suffix + "]")) {
                response.setContentType("image/" + suffix);
            } else {
                //文件下载
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=download." + suffix);
            }

            byte[] buffer = new byte[1024];
            int count = inStream.read(buffer, 0, buffer.length);
            while (count > 0) {
                ostream.write(buffer, 0, count);
                count = inStream.read(buffer, 0, buffer.length);
            }
        } catch (Exception e) {
            if (e.getClass().getName().contains("ClientAbortException")) {
                return;
            }
            logger.error("read file failed!", e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 测试用，测试文件上传接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartHttpServletRequest request) throws IOException {
        MultipartFile fi = request.getFile("file");
        String fname = fi.getOriginalFilename().replace("\\", "/");
        if (fname.contains("/")) {
            fname = fname.substring(fname.lastIndexOf("/") + 1);
        }
        fname = "my/img/" + fname;
        String res = fileService.saveFile(fi.getInputStream(), fname);
        logger.info(res);
        return res;
    }

    @RequestMapping("uploadIndex")
    public String uploadIndex() {
        return "commonUpload";
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
        Permission parentPagePerm = restPermissionService.queryByCodeAndUrl(menu, null);
        Permission expectPagePerm = restPermissionService.queryByCodeAndUrl(tab, null);
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
        try {
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
        }
        catch (Exception ex){
            result.setData(ex.getMessage());
        }
        return result;
    }

    /**
     * 上传文件
     * @param request
     * @return
     */
    @OpLog(OpType.UploadContract)
    @RequestMapping(value = "uploadfile.html", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFile(MultipartHttpServletRequest request) {
        Result result = new Result();
        MultipartFile file = request.getFile("uploadFile");
        try {
            String key = accountService.uploadFile(file);
            result.setData(key);
        } catch (BusinessException ex) {
            result.setData(ex.getMsg());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 根据公司名称模糊查询
     */
    @RequestMapping(value = "searchaccount", method = RequestMethod.POST)
    public @ResponseBody
    PageResult searchAccount(
            @RequestParam("accountName") String accountName,
            @RequestParam("type") String type) {
        PageResult result = new PageResult();

        Integer start = 0;
        Integer length = 10;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", accountName);
        
        paramMap.put("start", start);
        paramMap.put("length", length);
        
        List<Account> list = null;
        if("department".equals(type)){
        	list= accountService.listDeparmentAccount(paramMap);
        }else{
        	if (StringUtils.isNotEmpty(type)) {
                String[] types = { AccountType.both.toString(), type };
                paramMap.put("types", types);
            }
        	list = accountService.listAccountByName(paramMap);
        }
        
        Integer total = 0;// accountService.totalListAccountByName(paramMap);
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
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
