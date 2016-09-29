package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.api.RestQuotationService;
import com.prcsteel.platform.api.RestSmartmatchService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.HttpInvokerUtils;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignSellerInfoDto;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.enums.ConsignType;
import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderProcessService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lcw on 2015/7/17.
 * 新开代运营交易单
 */

@Controller
@RequestMapping("/order/")
public class CreateOrderController extends BaseController {

    private static org.apache.log4j.Logger logger = LogManager.getLogger(CreateOrderController.class);

    @Resource
    AccountService accountService;

    @Resource
    OrganizationService organizationService;

    @Resource
    AccountContactService accountContactService;

    @Resource
    ConsignOrderService consignOrderService;

    @Resource
    ConsignOrderItemsService consignOrderItemsService;
    @Resource
    ConsignOrderProcessService consignOrderProcessService;
    @Resource
    CategoryService categoryService;
    @Resource
    AcceptDraftService acceptDraftService;

    @Resource
    ContactService contactService;
    @Resource
    SysSettingService sysSettingService;

    @Resource
    RestSmartmatchService restSmartmatchService;
    @Resource
    RestQuotationService restQuotationService;

    @Value("${marketSite}")
    private String marketSite;

    @Value("${jms.cbms.enabled}")
    private boolean enabled;

    ShiroVelocity permissionLimit = new ShiroVelocity();

    final String PERMISSION_ORDERCREATESELF = "order:create:self";               // 给自己新开代运营交易单

    /**
     * 创建新开代运营交易单
     */
    @RequestMapping("create")
    public void create(ModelMap out) {
        getUser(out);
        ConsignOrder consignOrder = new ConsignOrder();
        consignOrder.setDeliveryEndDate(new Date());

        // 合同签订地默认读取服务中心里面配置的合同签订地
        User user = getLoginUser();
        Organization organization = organizationService.queryById(user.getOrgId());
        if (organization != null && organization.getContractAddress() != null) {
            consignOrder.setContractAddress(organization.getContractAddress());
        }
        out.put("BasicSettings", sysSettingService.getBasicSettings());
        out.put("consignOrder", consignOrder);
    }
    
    /**
     * 创建新开业务找货交易单
     */
    @RequestMapping("{ids}/businessquotation/create")
    public String createBusinessQuotation(ModelMap out, @PathVariable("ids") String ids, @RequestParam(value = "info", required = false) String info) {

        boolean isAuth = permissionLimit.hasPermission("order:create:page");
    	if(!isAuth)
    		return "unauth";
    	
        getUser(out);
        ConsignOrder consignOrder = new ConsignOrder();
        consignOrder.setDeliveryEndDate(new Date());
        //根据资源id查询出所有的开单信息
        List<ConsignOrderItemsInfoDto> list = consignOrderItemsService.buildOrderItemsByResourceId(ids,getUserIds());
        // 合同签订地默认读取服务中心里面配置的合同签订地
        User user = getLoginUser();
        Organization organization = organizationService.queryById(user.getOrgId());
        if (organization != null && organization.getContractAddress() != null) {
            consignOrder.setContractAddress(organization.getContractAddress());
        }
        if(StringUtils.isNotEmpty(info)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode balanceNodes = null;
            try {
                balanceNodes = mapper.readTree(info);
                if (balanceNodes != null) {
                    Optional<ConsignOrderItemsInfoDto> curItem = null;
                    for (JsonNode node : balanceNodes) {
                        Long rid = node.path("rid").asLong();
                        Integer quality = node.path("quality").asInt();
                        Double weight = node.path("weight").asDouble();
                        curItem = list.stream().filter(a -> a.getResourceId().equals(rid)).findFirst();
                        if (curItem.isPresent()) {
                            if (quality != null && !quality.equals(0)) {
                                curItem.get().setQuantity(quality);
                            }
                            if (weight != null && !weight.equals(0d)) {
                                curItem.get().setWeight(new BigDecimal(weight));
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                logger.error("开单参数异常:" + ex.toString());
            } catch (Exception e) {
                logger.error("开单参数异常:" + e.toString());
            }
        }

        out.put("BasicSettings", sysSettingService.getBasicSettings());
        out.put("buyerDepartment", contactService.getDeptsAndContactsByCompanyId(consignOrder.getAccountId(), getUserIds()));
        
        out.put("consignOrder", consignOrder);
        out.put("list", list);
        //out.put("origin", "SMART_MATCH");  //找货来的开单
        return "order/create";
    }

    /**
     *
     * 为采购单创建订单。
     *
     * @param out
     * @param purchaseOrderId
     * @return
     */
    @RequestMapping("{purchaseOrderId}/create")
    public String createOrderBaseOnQuotation(ModelMap out, @PathVariable("purchaseOrderId") Long purchaseOrderId,
                                             @RequestParam(value="requirementCode", required = false) String requirementCode) {
    	boolean isAuth = permissionLimit.hasPermission("order:create:page");
    	if(!isAuth)
    		return "unauth";

    	getUser(out);

        
        ConsignOrder consignOrder = consignOrderService.buildConsignOrderByPurchaseOrderId(purchaseOrderId);
        List<ConsignOrderItemsInfoDto> list = consignOrderItemsService.buildOrderItemsByPurchaseOrderId(purchaseOrderId, getLoginUser(), getUserIds());
        consignOrder.setDeliveryEndDate(new Date());
        // 合同签订地默认读取服务中心里面配置的合同签订地
        User user = getLoginUser();
        Organization organization = organizationService.queryById(user.getOrgId());
        if (organization != null && organization.getContractAddress() != null) {
            consignOrder.setContractAddress(organization.getContractAddress());
        }

        //此if判断用于cbms是否已合并找货增加需求单号、交货地的代码，防止cbms由询价单开交易单报错，代码合并后此判断可去掉
        if(StringUtils.isNotEmpty(requirementCode) && isEnabled()){
            //询价单交货地
            City city = restSmartmatchService.getCityByOrderId(purchaseOrderId);
            if(null != city){
                out.put("transArea", city.getName());
            }
        }

        out.put("purchaseOrderId", purchaseOrderId);
        out.put("consignOrder", consignOrder);
        out.put("buyerDepartment", contactService.getDeptsAndContactsByCompanyId(consignOrder.getAccountId(), getUserIds()));
        out.put("list", list);
        out.put("requirementCode", requirementCode);

        out.put("origin", "SMART_MATCH");  //找货来的开单
        out.put("BasicSettings", sysSettingService.getBasicSettings());
        
        return "order/create";
    }


    /**
     * 为询价单创建订单
     * @author chengui
     */
    @RequestMapping("{quotationIds}/createByQuotationItems")
    public String createOrderByQuotationItemIds(ModelMap out, @PathVariable("quotationIds") String quotationIds,
                                                @RequestParam(value="requirementCode", required = false) String requirementCode) {

        StringBuilder userIdsStr = new StringBuilder();
        List<Long> userIds = getUserIds();
        if(null != userIds){
            for (Long userIdStr : getUserIds()){
                userIdsStr.append(userIdStr).append("_");
            }
        }
        String str = userIds == null ? null : userIdsStr.substring(0, userIdsStr.length()-1);

        //交易单资源记录
        List<ConsignSellerInfoDto> sellerInfoList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> dtoList = restQuotationService.getQuotationOrderItems(quotationIds, str);
        for (LinkedHashMap<String, Object> dto : dtoList) {
            ConsignSellerInfoDto sellerInfoDto = getConsignSellerInfoDtos(dto);
            sellerInfoList.add(sellerInfoDto);
        }
        out.put("list", sellerInfoList);

        getUser(out);
        Long quotationOrderId = Long.valueOf(dtoList.get(0).get("quotationOrderId").toString());
        Long purchaseOrderId = restQuotationService.getQuotationOrderById(quotationOrderId).getPurchaseOrderId();
        ConsignOrder consignOrder = consignOrderService.buildConsignOrderByPurchaseOrderId(purchaseOrderId);
        consignOrder.setDeliveryEndDate(new Date());

        // 交易单 总数量、总重量、总金额
        BigDecimal totalWeight = CbmsNumberUtil.buildWeight(
                sellerInfoList.stream().mapToDouble(b -> b.getWeight().doubleValue()).sum()
        );
        BigDecimal totalAmount = CbmsNumberUtil.buildMoney(
                sellerInfoList.stream().mapToDouble(b -> b.getAmount().doubleValue()).sum()
        );
        consignOrder.setTotalQuantity(sellerInfoList.stream().mapToInt(b -> b.getQuantity()).sum());
        consignOrder.setTotalWeight(totalWeight);
        consignOrder.setTotalAmount(totalAmount);
        consignOrder.setShipFee(new BigDecimal(0.00));

        // 合同签订地默认读取服务中心里面配置的合同签订地
        User user = getLoginUser();
        Organization organization = organizationService.queryById(user.getOrgId());
        if (organization != null && organization.getContractAddress() != null) {
            consignOrder.setContractAddress(organization.getContractAddress());
        }
        out.put("BasicSettings", sysSettingService.getBasicSettings());
        out.put("consignOrder", consignOrder);

        //跳转回报价单页面参数设置
        out.put("buyerDepartment", contactService.getDeptsAndContactsByCompanyId(consignOrder.getAccountId(), getUserIds()));
        out.put("quotationIds", quotationIds);
        out.put("quotationOrderId", purchaseOrderId);
        out.put("requirementCode", requirementCode);//需求单

        return "order/create";
    }

    private ConsignSellerInfoDto getConsignSellerInfoDtos(LinkedHashMap<String, Object> dto){
        ConsignSellerInfoDto infoDto = new ConsignSellerInfoDto();
        infoDto.setSellerId(Long.valueOf(dto.get("sellerId").toString()));
        infoDto.setSellerName(dto.get("sellerName").toString());
        infoDto.setDepartmentId(Long.valueOf(dto.get("departmentId").toString()));
        infoDto.setNsortName(dto.get("categoryName").toString());
        infoDto.setMaterial(dto.get("materialName").toString());
        infoDto.setSpec(dto.get("spec").toString());
        infoDto.setFactory(dto.get("factoryName").toString());
        infoDto.setCity(dto.get("cityName").toString());
        infoDto.setWarehouse(dto.get("warehouseName").toString());
        infoDto.setQuantity(Integer.valueOf(dto.get("quantity").toString()));
        infoDto.setWeight(new BigDecimal(dto.get("weight").toString()).setScale(BigDecimal.ROUND_HALF_EVEN));
        infoDto.setWeightConcept(dto.get("weightConcept").toString());
        infoDto.setCostPrice(new BigDecimal(dto.get("costPrice").toString()).setScale(BigDecimal.ROUND_HALF_EVEN));
        infoDto.setDealPrice(new BigDecimal(dto.get("dealPrice").toString()).setScale(BigDecimal.ROUND_HALF_EVEN));
        infoDto.setAmount(new BigDecimal(infoDto.getWeight().longValue() * infoDto.getDealPrice().longValue()));
        infoDto.setDepartments(contactService.getDeptsAndContactsByCompanyId(infoDto.getSellerId(), getUserIds()));

        return infoDto;
    }

    /**
     * 重新开代运营交易单
     *
     * @param orderId 订单Id
     */
    @RequestMapping("{orderid}/modify")
    public String modify(ModelMap out, @PathVariable("orderid") Long orderId) {
        ConsignOrder consignOrder = consignOrderService.selectByPrimaryKey(orderId);         // 订单数据
        if (consignOrder != null) {
            List<ConsignSellerInfoDto> sellerInfoList = new ArrayList<>();
            List<ConsignOrderItems> list = consignOrderService.queryOrderItemsById(orderId);    // 订单资源数据
            //资源数据里面增加部门联系人等信息
            for (ConsignOrderItems item:list) {
                ConsignSellerInfoDto sellerInfoDto = new ConsignSellerInfoDto(item);
                sellerInfoDto.setDepartments(contactService.getDeptsAndContactsByCompanyId(item.getSellerId(), getUserIds()));
                sellerInfoList.add(sellerInfoDto);
            }
            consignOrder.setDeliveryEndDate(new Date());

            // 如果这个代运营交易单是临采，那么需要查询代运营卖家的当前状态
            if (ConsignType.temp.toString().equals(consignOrder.getConsignType())
                    && list != null
                    && list.size() > 0) {
                Account firstSeller = accountService.queryById(list.get(0).getSellerId());
                consignOrder.setConsignType(firstSeller.getConsignType());
            }

            // 如果联系人信息出现修改，订单里面的联系人信息清空，需要用户重新输入
            Contact contact = contactService.queryByTel(consignOrder.getContactTel());
            if (contact != null && !Long.valueOf(contact.getId()).equals(consignOrder.getContactId())) {
                consignOrder.setContactId(new Long("0"));
                consignOrder.setContactName("");
                consignOrder.setContactTel("");
                out.put("contactMsg", "该订单的买家联系人信息有更改，请手动输入！");
            }
            out.put("consignOrder", consignOrder);
            //chengui 买家设置部门及其联系人信息
            out.put("buyerDepartment", contactService.getDeptsAndContactsByCompanyId(consignOrder.getAccountId(), getUserIds()));
            out.put("list", sellerInfoList);
            out.put("BasicSettings", sysSettingService.getBasicSettings());
        }
        getUser(out);
        return "order/create";
    }

    /**
     * 获取用户
     */
    public void getUser(ModelMap out) {
        List<User> list = new ArrayList<User>();
        Role role = getUserRole();

        User user = getLoginUser();
        Organization organization = organizationService.queryById(user.getOrgId());
        // 是否支持银票业务
        if (organization != null && organization.getIsDraftAccepted() != null && organization.getIsDraftAccepted().compareTo(1) == 0) {  //edit by zjshan 20160518
            out.put("isShowAcceptDraft", true);
        }
        else{
            out.put("isShowAcceptDraft", false);
        }
        if (RoleAuthType.ONLY_ME.getValue() == role.getType()) {
            // 获取当前用户
            out.put("user", user);
        } else {
            // 查询拥有可以给自己开单权限的用户
            list = userService.queryByPermissionCode(PERMISSION_ORDERCREATESELF, getUserIds());

            // 如果是内勤的话，需要可以给服务中心总经理开单（暂先不实现）
//            Organization organization = organizationService.queryById(user.getOrgId());
//            if (!user.getOrgId().equals(organization.getId())) {
//                User manager = userService.queryById(organization.getCharger());
//                list.add(manager);
//            }
            out.put("userList", list);
        }
        out.put("userSize", list.size());
    }

    /**
     * 获取买家
     *
     * @param name   输入的字符串
     * @param userId 对应的管理员Id
     * @return 用户列表
     */
    @ResponseBody
    @RequestMapping(value = "getbuyer", method = RequestMethod.POST)
    public Result getBuyer(@RequestParam("name") String name, @RequestParam("userId") Long userId) {
        Result result = new Result();
        if (userId > 0 && StringUtils.isNotEmpty(name)) {
            Account query=new Account();
            query.setName(name);
            query.setManagerId(userId);
            List<Account> list = accountService.queryAccountByManager(query);
            result.setData(list);
            result.setSuccess(Boolean.TRUE);
        }
        return result;
    }

    /**
     * 获取部门
     *
     * @param accountId 账号Id
     * @return 用户列表
     */
    @ResponseBody
    @RequestMapping(value = "getdepartment", method = RequestMethod.POST)
    public Result getDepartment(@RequestParam("accountId") Long accountId) {
        Result result = new Result();
        Boolean success = Boolean.FALSE;
        if (accountId != null) {
            List<DepartmentDto> list = new ArrayList<DepartmentDto>();
            if(!CollectionUtils.isEmpty(getUserIds())){
                list = accountService.queryDeptByCompanyIdAndBelongOrg(accountId, getOrganization().getId());
            }else{
                list = accountService.queryDeptByCompanyId(accountId);
            }

            success = Boolean.TRUE;
            result.setData(list);
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * 获取买家联系人
     *
     * @param departmentId 买家客户部门ID
     * @return 买家联系人列表
     */
    @ResponseBody
    @RequestMapping(value = "getbuyercontact", method = RequestMethod.POST)
    public Result getBuyerContact(@RequestParam("departmentId") Long departmentId,@RequestParam("userId") Long userId) {
        Result result = new Result();
        Boolean success = Boolean.FALSE;
        if (departmentId != null) {
            ContactDto query = new ContactDto();
            query.setDeptId(departmentId);
            query.setManagerId(userId);
            List<ContactDto> list = contactService.queryContactsByContactDto(query);
            success = Boolean.TRUE;
            result.setData(list);
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * 获取卖家联系人
     *
     * @param departmentId 客户部门ID
     * @return 买家联系人列表
     */
    @ResponseBody
    @RequestMapping(value = "getsellercontact", method = RequestMethod.POST)
    public Result getSellerContact(@RequestParam("departmentId") Long departmentId) {
        Result result = new Result();
        Boolean success = Boolean.FALSE;
        if (departmentId != null) {
            List<Contact> list = contactService.queryContactsByDepartId(departmentId);
            success = Boolean.TRUE;
            result.setData(list);
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * 获取买家银票列表
     *
     * @param accountId 客户ID
     * @return 银票列表
     */
    @ResponseBody
    @RequestMapping(value = "getacceptdraft", method = RequestMethod.POST)
    public PageResult getAcceptDraft(@RequestParam("accountId") Long accountId) {
        PageResult result = new PageResult();
        List<AcceptDraft> list = new ArrayList<>();
        if (accountId != null) {
            list = acceptDraftService.queryByAccountStatus(accountId, AcceptDraftStatus.CHARGED.getCode());
            result.setData(list);
        }
        result.setData(list);
        result.setRecordsFiltered(list.size());
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 保存订单数据
     *
     * @param orderJson 订单JSON数据
     * @param purchaseOrderId 采购单ID
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.CreateConsignOrder)
    @OpParam(name = "orderJson", value = "CreateOrderJson")
    @ResponseBody
    @RequestMapping(value = "saveorder", method = RequestMethod.POST)
    public Result saveOrder(@RequestParam("orderJson") String orderJson, String purchaseOrderId, String quotationIds) {
        Result result = new Result();
        User user = getLoginUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode orderNode = mapper.readTree(orderJson);
            ConsignOrder consignOrder = jsonToConsignOrder(orderNode);      // 订单数据
            //Long id = consignOrder.getId();
            //Boolean isCreate = id <= 0;

            JsonNode resourceNode = orderNode.path("resourceItems");
            Long idaa=resourceNode.get(0).path("sellerId").asLong();
            try{
                Organization sellerOrg = organizationService.queryById(accountService.queryById(idaa).getOrgId());
                if(sellerOrg == null || sellerOrg.getId() == null || sellerOrg.getName() == null){
                    result.setSuccess(Boolean.FALSE);
                    result.setData("该卖家尚未归属服务中心，请划转后再开单!");
                    return result;
                }
            }catch (Exception e){
                result.setSuccess(Boolean.FALSE);
                result.setData("该卖家尚未归属服务中心，请划转后再开单!");
                return result;
            }
            // 交易单资源
            List<ConsignOrderItems> list = jsonToConsignOrderItems(orderNode);
            // 添加
            Long poid = StringUtils.isNotBlank(purchaseOrderId)&&StringUtils.isNumeric(purchaseOrderId) ? Long.parseLong(purchaseOrderId) : null; //设置采购单ID
            ResultDto resultDto = consignOrderService.add(user, consignOrder, list, poid, quotationIds);
            result.setSuccess(resultDto.isSuccess());

            result.setData(resultDto.getMessage());

        }catch(BusinessException be){
            result.setSuccess(Boolean.FALSE);
            result.setData(be.getMsg());
        } catch (IOException e) {
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMessage());
        }
        return result;
    }

    /**
     * 搜索资源页面
     */
    @RequestMapping("searchresource")
    public void searchResource() {
    }

    /**
     * 搜索资源数据
     */
    @ResponseBody
    @RequestMapping(value = "getresource")
    public PageResult getResource(
            @RequestParam("start") Integer start,
            @RequestParam("sellerCompany") String sellerCompany,
            @RequestParam("nsortName") String nsortName,
            @RequestParam("material") String material,
            @RequestParam("spec") String spec,
            @RequestParam("factory") String factory) {
        PageResult result = new PageResult();
        Integer total = 0;
        List<ConsignOrderItems> list = new ArrayList<ConsignOrderItems>();  // 资源数据
        try {
            Integer pageIndex = 0, pageSize = 10;
            HashMap<String, String> postPars = new HashMap<String, String>();
            postPars.put("companyName", sellerCompany);
            postPars.put("nsortName", nsortName);
            postPars.put("material", material);
            postPars.put("spec", spec);
            postPars.put("factory", factory);
            if (start > 0) {
                pageIndex = start / pageSize;
            }
            postPars.put("pageIndex", pageIndex.toString());
            postPars.put("pageSize", pageSize.toString());
            String requestUrl = marketSite + "/Home/SearchByCompanyName";
            String content = HttpInvokerUtils.readContentFromPost(requestUrl, postPars);
            if (StringUtils.isNotEmpty(content)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode orderNode = mapper.readTree(content);
                total = orderNode.path("total").asInt();
                JsonNode resourceNode = orderNode.path("data");

                ConsignOrderItems item;
                for (JsonNode node : resourceNode) {
                    item = new ConsignOrderItems();
                    item.setId(node.path("resourceID").asLong());
                    item.setSellerName(node.path("seller").asText());
                    item.setNsortName(node.path("nsortName").asText());
                    item.setMaterial(node.path("material").asText());
                    item.setSpec(node.path("spec").asText());
                    item.setFactory(node.path("factory").asText());
                    item.setCity(node.path("city").asText());
                    item.setWarehouse(node.path("warehouse").asText());
                    item.setQuantity(node.path("quantity").asInt());
                    item.setWeight(new BigDecimal(node.path("weight").asLong()));
                    item.setWeightConcept(node.path("weightConcept").asText());
                    item.setCostPrice(new BigDecimal(node.path("price").asLong()));
                    item.setDealPrice(new BigDecimal(node.path("price").asLong()));
                    list.add(item);
                }
            }
        } catch (Exception ex) {
        }
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
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

    private ConsignOrder jsonToConsignOrder(JsonNode orderNode) {
        ConsignOrder consignOrder = new ConsignOrder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            consignOrder.setId(orderNode.path("id").asLong());
            consignOrder.setAccountId(orderNode.path("accountId").asLong());
            consignOrder.setDepartmentId(orderNode.path("departmentId").asLong());
            consignOrder.setDepartmentName(orderNode.path("departmentName").asText());
            consignOrder.setConsignType(orderNode.path("consignType").asText());
            consignOrder.setAccountName(orderNode.path("accountName").asText());
            consignOrder.setOwnerId(orderNode.path("ownerId").asLong());
            consignOrder.setOwnerName(orderNode.path("ownerName").asText());
            consignOrder.setContactId(orderNode.path("contactId").asLong());
            consignOrder.setContactName(orderNode.path("contactName").asText());
            consignOrder.setContactTel(orderNode.path("contactTel").asText());
            consignOrder.setDeliveryType(orderNode.path("deliveryType").asText());
            consignOrder.setDeliveryStartDate(new Date());
            consignOrder.setDeliveryEndDate(sdf.parse(orderNode.path("deliveryEndDate").asText()));
            consignOrder.setFeeTaker(orderNode.path("feeTaker").asText());
            consignOrder.setShipFee(new BigDecimal(orderNode.path("shipFee").asText()));
            consignOrder.setOutboundTaker(orderNode.path("outboundTaker").asText());
            consignOrder.setOutboundFee(new BigDecimal(orderNode.path("outboundFee").asText()));
            consignOrder.setTotalQuantity(orderNode.path("totalQuantity").asInt());
            consignOrder.setTotalWeight(new BigDecimal(orderNode.path("totalWeight").asText()));
            consignOrder.setTotalAmount(new BigDecimal(orderNode.path("totalAmount").asText()));
            consignOrder.setContractAddress(orderNode.path("contractAddress").asText());
            consignOrder.setPaymentSort(orderNode.path("paymentSort").asText());
            consignOrder.setRequirementCode(orderNode.path("requirementCode").asText());
            consignOrder.setTransArea(orderNode.path("transArea").asText());
            consignOrder.setOrigin(orderNode.path("origin").asText());
            //订单所属服务中心
            Organization orderOrg = organizationService.queryById(userService.queryById(orderNode.path("ownerId").asLong()).getOrgId());
            consignOrder.setOrderOrgId(orderOrg.getId());//订单所属服务中心id
            consignOrder.setOrderOrgName(orderOrg.getName());//订单所属服务中心名称
            //买家所属服务中心
            Organization buyerOrg;
            if(accountService.queryById(orderNode.path("accountId").asLong()) != null){
                buyerOrg = organizationService.queryById(userService.queryById(accountService.queryById(orderNode.path("accountId").asLong()).getManagerId()).getOrgId());
            }else{
                buyerOrg = orderOrg;
            }

            consignOrder.setBuyerOrgId(buyerOrg.getId());//买家所属服务中心id
            consignOrder.setBuyerOrgName(buyerOrg.getName());//买家所属服务中心名称
            //申请付款给卖家延时几天付款
            consignOrder.setPaymentType(orderNode.path("paymentType").asText());
            consignOrder.setDelayNum(orderNode.path("delayNum").asInt());
            //新增买家交易员工号
            User buyerUser = userService.queryById(orderNode.path("ownerId").asLong());
            consignOrder.setOwnerJobNumber(buyerUser.getJobNumber());
        } catch (ParseException pex) {
            // handle error
        }
        return consignOrder;
    }

    private List<ConsignOrderItems> jsonToConsignOrderItems(JsonNode orderNode) {
        List<ConsignOrderItems> list = new ArrayList<ConsignOrderItems>();
        JsonNode resourceNode = orderNode.path("resourceItems");
        ConsignOrderItems item;
        for (JsonNode node : resourceNode) {
            item = new ConsignOrderItems();
            item.setSellerId(node.path("sellerId").asLong());
            item.setSellerName(node.path("sellerName").asText());
            item.setDepartmentId(node.path("departmentId").asLong());
            item.setDepartmentName(node.path("departmentName").asText());
            item.setContactId(node.path("contactId").asLong());
            item.setContactName(node.path("contactName").asText());
            item.setNsortName(node.path("nsortName").asText());
            item.setMaterial(node.path("material").asText());
            item.setSpec(node.path("spec").asText());
            item.setFactory(node.path("factory").asText());
            item.setCity(node.path("city").asText());
            item.setWarehouse(node.path("warehouse").asText());
            item.setQuantity(node.path("quantity").asInt());
            item.setWeight(new BigDecimal(node.path("weight").asText()));
            item.setWeightConcept(node.path("weightConcept").asText());
            item.setCostPrice(new BigDecimal(node.path("costPrice").asText()));
            item.setDealPrice(new BigDecimal(node.path("dealPrice").asText()));
            item.setAmount(new BigDecimal(node.path("amount").asText()));
            item.setIsPayedByAcceptDraft(node.path("isPayedByAcceptDraft").asLong() > 0);
            item.setAcceptDraftId(node.path("acceptDraftId").asLong());
            item.setAcceptDraftCode(node.path("acceptDraftCode").asText());
            item.setStrappingNum(node.path("strappingNum").asText());
            //卖家所属服务中心
            Organization sellerOrg = organizationService.queryById(accountService.queryById(node.path("sellerId").asLong()).getOrgId());
            item.setSellerOrgId(sellerOrg.getId());//卖家所属服务中心id
            item.setSellerOrgName(sellerOrg.getName());//卖家所属服务中心名称
            //卖家所属交易员
            ContactDto query=new ContactDto();
            query.setDeptId(node.path("departmentId").asLong());
            query.setId(node.path("contactId").asInt());
            List<ContactDto> contactList = contactService.queryContactsByContactDto(query);
            if (contactList != null && contactList.size() > 0 && contactList.get(0).getManagerId() != null) {
                User sellerUser = userService.queryById(contactList.get(0).getManagerId());
                if (sellerUser != null) {
                    item.setSellerOwnerId(sellerUser.getId());//卖家所属交易员id
                    item.setSellerOwnerName(sellerUser.getName());//卖家所属交易员姓名
                    item.setSellerJobNumber(sellerUser.getJobNumber());//卖家所属交易员工号
                }else{
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家客户没有归属交易员，请划转后再开单!");
                }
            }
            list.add(item);
        }
        return list;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
