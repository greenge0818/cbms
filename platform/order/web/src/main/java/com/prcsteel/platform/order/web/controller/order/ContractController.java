package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;
import com.prcsteel.platform.order.service.order.ConsignOrderContractService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import com.prcsteel.platform.order.web.vo.ContractVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rolyer on 15-7-29.
 */
@Controller
@RequestMapping("/order/")
public class ContractController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    private static final int MIX_ROW_NUMBER = 7; //订单项最小行数
    private static final String CONTRACT_BUYER = "buyer"; //销售
    private static final String CONTRACT_SELLER = "seller"; //采购
    private static final String CONTRACT_TITLE_BUYER = "合同变更补充协议（销售合同）"; //销售
    private static final String CONTRACT_TITLE_SELLER = "合同变更补充协议（采购合同）"; //采购

    ShiroVelocity permissionLimit = new ShiroVelocity();

    @Resource
    private ConsignOrderService consignOrderService;
    @Resource
    OrganizationService organizationService;
    @Resource
    private AccountService accountService;
    @Resource
    private ConsignOrderAttachmentService consignOrderAttachmentService;
    @Resource
    private ConsignOrderContractService consignOrderContractService;
    @Resource
    private SysSettingService sysSettingService;
    @Resource
    private AccountContractTemplateService accountContractTemplateService;
    @Resource
    private ConsignOrderChangeService consignOrderChangeService;
    @Resource
    private ChangeContractPrintController changeContractPrintController;


    /**
     * 合同预览/打印
     *
     * @param out
     * @param id
     */
    @RequestMapping("contract.html")
    public void contract(ModelMap out, String id) {
        List<ContractVo> contracts = new ArrayList<ContractVo>();

        if (!StringUtils.isNumeric(id)) {
            out.put("msg", "参数错误");
            return;
        }

        long orderId = Long.parseLong(id);
        ConsignOrder order = getOrder(orderId);
        if (order == null && order.getAccountId() == null) {
            out.put("msg", "订单不存在或信息有误");
            return;
        }

        if (!StringUtils.isNumeric(order.getStatus()) ||
                Integer.parseInt(order.getStatus()) < Integer.parseInt(ConsignOrderStatus.NEWAPPROVED.getCode())) {
            out.put("msg", "请核对订单状态");
            return;
        }

        Account account = getAccount(order.getAccountId());
        String content = getTemplate(account.getId(), AccountType.buyer.toString());

        User owner = userService.queryById(order.getOwnerId());
        Organization organization = organizationService.queryById(owner.getOrgId());

        if (StringUtils.isBlank(content)) {
            out.put("msg", "没有合同模板");
            return;
        }

        List<ConsignOrderItems> items = new ArrayList<ConsignOrderItems>();

        int count = consignOrderChangeService.querySuccessCountByOrderId(orderId);

        //若订单有合同变更成功的记录，合同内容须取原订单变更前的数据
        if(count > 0){
            // 原订单变更前的数据
            contracts = getContractsOfChangedOrder(orderId, order, account, content, organization);

            //订单变更后每次变更的合同数据
            contracts.addAll(getChangeContractsOfChangeOrder(orderId, account, organization));

        }else{
            items = getItems(orderId);
            ConsignOrderItems totalItems = getTotalItems(items);

            ContractVo vo = new ContractVo();
            vo.setCompanyName(account.getName());
            vo.setBuyer(true);
            vo.setContent(buildContractForBuyer(content, account, order, items, totalItems, organization));

            contracts.add(vo);
            contracts.addAll(getContractOfSeller(order, items, organization));
        }

        out.put("contracts", contracts);

        // 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
        if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
            out.put("financeorder", true);
        }
    }

    /**
     * 构建原订单、及所有变更成功的订单合同信息
     *
     * @return
     */
    private List<ContractVo> getContractsOfChangedOrder(Long orderId, ConsignOrder order, Account account, String content, Organization organization){
        List<ContractVo> contracts = new ArrayList<ContractVo>();

        List<ConsignOrderItems> items = new ArrayList<ConsignOrderItems>();

        //须取变更前原始订单的交提货信息
        ConsignOrderChange query = new ConsignOrderChange();
        query.setStatus(ConsignOrderAlterStatus.ORIGIN_ORDER.getCode());
        query.setOrderId(orderId);
        ConsignOrderChange originalOrder = consignOrderChangeService.selectByConsignOrderChange(query);
        order.setDeliveryType(originalOrder.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
        order.setDeliveryEndDate(originalOrder.getDeliveryEndDate());
        order.setFeeTaker(originalOrder.getFeeTaker());
        order.setShipFee(originalOrder.getShipFee());
        order.setOutboundTaker(originalOrder.getOutboundTaker());
        order.setOutboundFee(originalOrder.getOutboundFee());
        order.setContractAddress(originalOrder.getContractAddress());
        order.setTransArea(originalOrder.getTransArea());

        //须取变更前原订单详情的数据
        List<ConsignOrderItemsChange> orderChangeItems = consignOrderChangeService.selectOriginalItemsByOrderId(orderId);
        items = orderChangeItems.stream().map(a -> new ConsignOrderItems(a, true)).collect(Collectors.toList());

        ConsignOrderItems totalItems = getTotalItems(items);

        ContractVo vo = new ContractVo();

        vo.setCompanyName(account.getName());
        vo.setBuyer(true);
        vo.setContent(buildContractForBuyer(content, account, order, items, totalItems, organization));

        contracts.add(vo);
        contracts.addAll(getContractOfSeller(order, items, organization));

        return contracts;
    }


    private List<ContractVo> getChangeContractsOfChangeOrder(Long orderId, Account account, Organization organization){
        List<ContractVo> contracts = new ArrayList<ContractVo>();
        // 查询本次变更的记录
        QueryChangeOrderDto query = new QueryChangeOrderDto();
        query.setOrderId(orderId);
        List<ConsignOrderAlterStatus> alterStatuses = new ArrayList<ConsignOrderAlterStatus>();
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS1);
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS2);
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS3);
        query.setAlterStatuses(alterStatuses);

        String changeContent = changeContractPrintController.getTemplate(account.getId(), AccountType.buyer.toString());
        List<ConsignOrderChange> changeOrders = consignOrderChangeService.selectByQueryDto(query);

        QueryChangeOrderDto queryItemsDto = new QueryChangeOrderDto();
        queryItemsDto.setAlterStatuses(alterStatuses);
        // 查询参数
        for(int i = 0; i < changeOrders.size(); i++){
            queryItemsDto.setChangeOrderId(changeOrders.get(i).getId());
            ConsignOrderChange orderChange = consignOrderChangeService.selectByPrimaryKey(changeOrders.get(i).getId());//变更订单信息
            // 变更订单资源信息
            List<ConsignOrderItemsChange> orderChangeItems = consignOrderChangeService.selectOrderItemsChangeByQueryDto(queryItemsDto);
            List<ConsignOrderItemsChange> exOrderChangeItems;
            if(0 == i) {
                // 原订单资源信息
                exOrderChangeItems = consignOrderChangeService.selectOriginalItemsByOrderId(orderId);
            }else {
                // 此次变更前的订单资源信息
                queryItemsDto.setChangeOrderId(changeOrders.get(i-1).getId());
                exOrderChangeItems = consignOrderChangeService.selectOrderItemsChangeByQueryDto(queryItemsDto);
            }

            int number = i+1;
            ContractVo vo = new ContractVo();
            vo.setCompanyName(account.getName());
            vo.setBuyer(true);
            vo.setTitle(CONTRACT_TITLE_BUYER);
            vo.setNumber(number);
            vo.setContent(changeContractPrintController.buildChangeContractRecordForBuyer(changeContent, account, orderChange, orderChangeItems, exOrderChangeItems, organization));

            contracts.add(vo);
            List<ContractVo> sellerContractVo = changeContractPrintController.buildChangeContractRecordForSeller(orderChange, orderChangeItems, exOrderChangeItems, organization);
            sellerContractVo.stream().forEach(a -> a.setNumber(number));
            contracts.addAll(sellerContractVo);
        }
        return contracts;
    }

    /**
     * 构建所有卖家合同信息
     *
     * @param order
     * @param items
     * @return
     */
    private List<ContractVo> getContractOfSeller(ConsignOrder order, List<ConsignOrderItems> items, Organization organization) {
        List<ContractVo> contracts = new ArrayList<ContractVo>();

        List<ConsignOrderContract> consignOrderContracts = consignOrderContractService.queryByOrderId(order.getId());

        for (ConsignOrderContract coc : consignOrderContracts) {
            Account account = getAccount(coc.getCustomerId());
            String content = getTemplate(account.getId(), AccountType.seller.toString());

            List<ConsignOrderItems> myItems = getItemsForSeller(items, coc.getCustomerId());
            ConsignOrderItems totalItems = getTotalItems(myItems);

            ContractVo vo = new ContractVo();
            vo.setCompanyName(account.getName());
            vo.setContent(buildContractForSeller(content, account, order, coc, myItems, totalItems, organization));

            contracts.add(vo);
        }

        return contracts;
    }

    /**
     * 获取卖家销售项
     *
     * @param items
     * @param sellerId
     * @return
     */
    private List<ConsignOrderItems> getItemsForSeller(List<ConsignOrderItems> items, Long sellerId) {
        List<ConsignOrderItems> myItems = new ArrayList<ConsignOrderItems>();
        for (ConsignOrderItems i : items) {
            if (i.getSellerId().equals(sellerId)) {
                myItems.add(i);
            }
        }

        return myItems;
    }

    /**
     * 生产卖家合同信息
     *
     * @param content
     * @param account
     * @param order
     * @param consignOrderContract
     * @param items
     * @param totalItems
     * @return
     */
    private String buildContractForSeller(String content, Account account, ConsignOrder order, ConsignOrderContract consignOrderContract, List<ConsignOrderItems> items, ConsignOrderItems totalItems, Organization organization) {

        Calendar cal = Calendar.getInstance();
        Date date = consignOrderService.queryApprovedDateByOrderId(order.getId());
        if (date != null) {
            cal.setTime(date);
        }

        BigDecimal totalCostAmount = getTotalCostAmount(items);

        content = content.replaceAll("\\r", "");
        content = content.replaceAll("\\n", "");
        content = content.replaceAll("\\t", "");

        content = content.replaceAll("#companyName#", account.getName());

        content = content.replace("#contractNo#", consignOrderContract.getContractCodeAuto());
        content = content.replace("<tbody id=\"items\"></tbody>", restoreItems(items, CONTRACT_SELLER));
        content = content.replace("#totalQuantity#", totalItems.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(totalItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(totalCostAmount));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(totalCostAmount));
        content = content.replace("#total#", Tools.formatBigDecimal(totalCostAmount));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());
        content = content.replace("#deliveryType#", order.getDeliveryType());
        content = content.replace("#feeTaker#", order.getFeeTaker().equals("seller") ? "乙方" : "甲方");

        Calendar deliveryCal = Calendar.getInstance();
        if (date != null) {
            deliveryCal.setTime(order.getDeliveryEndDate());
        }
        content = content.replace("#deliveryYear#", deliveryCal.get(Calendar.YEAR) + "");
        content = content.replace("#deliveryMonth#", (deliveryCal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#deliveryDate#", deliveryCal.get(Calendar.DATE) + "");

        //add by wangxianjun 采购合同支付方式修改 支付方式为1 延时支付 否则 当日现款支付
        if("1".equals(order.getPaymentType())){
            content = content.replace("#payTypeFirst#", "display:none");
            content = content.replace("#payTypeSecond#", "");
            content = content.replace("#delayDays#", order.getDelayNum().toString());
        }else{
            content = content.replace("#payTypeFirst#", "");
            content = content.replace("#payTypeSecond#", "display:none");
        }
        // 兼容老数据，outboundTaker为null时，继续用feeTaker
        String outboundTaker = order.getFeeTaker().equals("seller") ? "乙方" : "甲方";
        if (order.getOutboundTaker() != null) {
            outboundTaker = order.getOutboundTaker().equals("seller") ? "乙方" : "甲方";
        }
        content = content.replace("#outboundTaker#",outboundTaker);
        content = content.replace("#contractAddress#", order.getContractAddress() == null ? "" : order.getContractAddress());
        content = content.replace("#year#", cal.get(Calendar.YEAR) + "");
        content = content.replace("#month#", (cal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#date#", cal.get(Calendar.DATE) + "");
        content = content.replace("#bankCode#", account.getBankCode() != null ? account.getBankCode() : "");
        content = content.replace("#bankName#", account.getBankNameMain() != null ? account.getBankNameMain() : "" + account.getBankNameBranch() != null ? account.getBankNameBranch() : "");
        content = content.replace("#taxCode#", account.getTaxCode() != null ? account.getTaxCode() : "");
        content = content.replace("#bankAccount#", account.getAccountCode() != null ? account.getAccountCode() : "");
        content = content.replace("#tel#", account.getTel() != null ? account.getTel() : "");
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");

        content = content.replace("#orgName#", organization.getName());
        content = content.replace("#orgAddr#", StringUtils.isEmpty(organization.getAddress()) ? "" : organization.getAddress());
        content = content.replace("#orgFax#", StringUtils.isEmpty(organization.getFax()) ? "" : organization.getFax());
        content = content.replace("#clause#", "");
        return content;

    }

    /**
     * 构造买家合同信息
     *
     * @param content
     * @param account
     * @param order
     * @param items
     * @param totalItems
     * @return
     */
    private String buildContractForBuyer(String content, Account account, ConsignOrder order, List<ConsignOrderItems> items, ConsignOrderItems totalItems, Organization organization) {

        Calendar cal = Calendar.getInstance();
        Date date = consignOrderService.queryApprovedDateByOrderId(order.getId());
        if (date != null) {
            cal.setTime(date);
        }
        boolean isDraft = false;//是否银票支付
        boolean isDraftCode = false;//是否有具体票号支付
        boolean isCash = false;//是否现款支付

        content = content.replaceAll("\\r", "");
        content = content.replaceAll("\\n", "");
        content = content.replaceAll("\\t", "");
        content = content.replaceAll("#companyName#", account.getName());

        content = content.replace("#contractNo#", order.getContractCode());
        content = content.replace("<tbody id=\"items\"></tbody>", restoreItems(items, CONTRACT_BUYER));
        content = content.replace("#totalQuantity#", totalItems.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(totalItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(totalItems.getAmount()));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(totalItems.getAmount()));
        content = content.replace("#total#", Tools.formatBigDecimal(totalItems.getAmount()));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());
        content = content.replace("#deliveryType#", order.getDeliveryType());
        content = content.replace("#feeTaker#", order.getFeeTaker().equals("seller") ? "乙方" : "甲方");
        content = content.replace("#transArea#", order.getTransArea());

        Calendar deliveryCal = Calendar.getInstance();
        if (date != null) {
            deliveryCal.setTime(order.getDeliveryEndDate());
        }
        content = content.replace("#deliveryYear#", deliveryCal.get(Calendar.YEAR) + "");
        content = content.replace("#deliveryMonth#", (deliveryCal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#deliveryDate#", deliveryCal.get(Calendar.DATE) + "");

        //add by wangxianjun 合同模板参数不开启使用原买家模板,开启则用新的模板
        SysSetting buyerParam = sysSettingService.queryBySettingType(Constant.TEMPLATE_BUYER_PARAM);
        if(buyerParam !=null && "1".equals(buyerParam.getSettingValue())) {
            for (ConsignOrderItems item : items) {
                if (item.getIsPayedByAcceptDraft() == false) {
                    isCash = true;
                } else {
                    if ("".equals(item.getAcceptDraftCode())) {
                        isDraft = true;
                    } else {
                        isDraftCode = true;
                    }
                }
            }
            content = content.replace("#payTypeFirst#", "display:none");
            if(isCash && !isDraft && !isDraftCode){
                //付款方式为当日内 现款 全额支付货款
                content = content.replace("#payTypeSecond#", "");
                content = content.replace("#payTypeThird#", "display:none");
                content = content.replace("#payTypeFourth#", "display:none");
            }else if(!isCash && (isDraft || isDraftCode)){
                //付款方式为银行承兑汇票 支付货款，按供方当日贴息政策执行
                content = content.replace("#payTypeSecond#", "display:none");
                content = content.replace("#payTypeThird#", "");
                content = content.replace("#payTypeFourth#", "display:none");
            }else if(isCash && (isDraft || isDraftCode)){
                //付款方式为现款和银行承兑汇票 支付货款，以银行承兑汇票支付的部分按供方当日贴息政策执行
                content = content.replace("#payTypeSecond#", "display:none");
                content = content.replace("#payTypeThird#", "display:none");
                content = content.replace("#payTypeFourth#", "");
            }
        }else{
            //合同模板参数不开启使用原买家模板
            content = content.replace("#payTypeFirst#", "");
            content = content.replace("#payTypeSecond#", "display:none");
            content = content.replace("#payTypeThird#", "display:none");
            content = content.replace("#payTypeFourth#", "display:none");

        }
        // 兼容老数据，outboundTaker为null时，继续用feeTaker
        String outboundTaker = order.getFeeTaker().equals("seller") ? "乙方" : "甲方";
        if (order.getOutboundTaker() != null) {
            outboundTaker = order.getOutboundTaker().equals("seller") ? "乙方" : "甲方";
        }
        content = content.replace("#outboundTaker#",outboundTaker);
        content = content.replace("#contractAddress#", order.getContractAddress() == null ? "" : order.getContractAddress());
        content = content.replace("#year#", cal.get(Calendar.YEAR) + "");
        content = content.replace("#month#", (cal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#date#", cal.get(Calendar.DATE) + "");

        content = content.replace("#orgName#", organization.getName());
        content = content.replace("#orgAddr#", StringUtils.isEmpty(organization.getAddress()) ? "" : organization.getAddress());
        content = content.replace("#orgFax#", StringUtils.isEmpty(organization.getFax()) ? "" : organization.getFax());
        content = content.replace("#clause#", "");
        return content;
    }

    /**
     * 订单项组合排版
     *
     * @param items
     * @param type  销售/采购
     * @return
     */
    private String restoreItems(List<ConsignOrderItems> items, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tbody>");
        for (ConsignOrderItems i : items) {
            sb.append(getRow(i, type));
        }

        int row = MIX_ROW_NUMBER - items.size();
        if (row > 0) {
            for (int i = 1; i <= row; i++) {
                if (i == 1) {
                    sb.append(getBlankRow("以下空白"));
                } else {
                    sb.append(getBlankRow(null));
                }
            }
        }
        sb.append("</tbody>");
        return sb.toString();
    }

    /**
     * 获取空白行
     *
     * @param mark 　备注
     * @return
     */
    private String getBlankRow(String mark) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");

        sb.append("<td>" + (StringUtils.isBlank(mark) == true ? "" : mark) + "</td>");
        sb.append("<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>");

        sb.append("</tr>");

        return sb.toString();
    }

    /**
     * 单个订单项排版
     *
     * @param item
     * @return
     */
    private String getRow(ConsignOrderItems item, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");

        sb.append("<td>" + item.getNsortName() + "</td>");
        sb.append("<td>" + item.getSpec() + "</td>");
        sb.append("<td>" + item.getMaterial() + "</td>");
        sb.append("<td>" + item.getFactory() + "</td>");
        sb.append("<td>" + item.getWarehouse() + "</td>");
        sb.append("<td class='text-center'>" + item.getQuantity() + "</td>");
        sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getWeight(), Constant.WEIGHT_PRECISION) + "</td>");
        sb.append("<td class='text-center'>" + item.getWeightConcept() + "</td>");
        if (CONTRACT_BUYER.equals(type)) {
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getDealPrice()) + "</td>");
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getAmount()) + "</td>");
        } else {
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getCostPrice()) + "</td>");
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getWeight().multiply(item.getCostPrice())) + "</td>");
        }


        sb.append("</tr>");

        return sb.toString();
    }

    /**
     * 获取模板
     *
     * @param id
     * @return
     */
    private String getTemplate(long id, String type) {
        String content = null;
        AccountContractTemplate act = new AccountContractTemplate();
        act.setAccountId(id);
        act.setType(type);

        act.setEnabled(1);
        List<AccountContractTemplate> list = accountService.selectCTByModel(act);

        if (list.size() > 0) {
            content = list.get(0).getContent();
        } else {
            String t = AccountType.buyer.toString().equals(type) ? Constant.TEMPLATE_BUYER : Constant.TEMPLATE_SELLER;
            content = accountContractTemplateService.readTemplate(t, false);
        }

        return content;
    }

    /**
     * 获取订单信息
     *
     * @param id
     * @return
     */
    private ConsignOrder getOrder(long id) {
        ConsignOrder order = consignOrderService.queryById(id);
        if (order != null) {

            order.setDeliveryType(order.getDeliveryType().equals("SELFPICK") ? "自提" : "配送");
        }

        return order;
    }

    /**
     * 获取公司账号细信息
     *
     * @param id
     * @return
     */
    private Account getAccount(long id) {
        Account account = accountService.queryById(id);
        if (account != null) {
            account.setBalanceFreeze(account.getBalance().add(account.getBalanceSecondSettlement()));
        }

        return account;
    }

    /**
     * 获取订单项
     *
     * @param id
     * @return
     */
    private List<ConsignOrderItems> getItems(long id) {
        return consignOrderService.queryOrderItemsById(id);
    }

    /**
     * 计算总量
     *
     * @param orderItems
     * @return
     */
    private ConsignOrderItems getTotalItems(List<ConsignOrderItems> orderItems) {
        ConsignOrderItems totalItems = new ConsignOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            int count = 0;
            BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, amount = BigDecimal.ZERO;
            for (ConsignOrderItems item : orderItems) {
                count += item.getQuantity();
                if (item.getWeight() != null) {
                    weight = weight.add(item.getWeight());
                }
                if (item.getCostPrice() != null) {
                    costprice = costprice.add(item.getCostPrice());
                }
                if (item.getDealPrice() != null) {
                    dealprice = dealprice.add(item.getDealPrice());
                }
                if (item.getAmount() != null) {
                    amount = amount.add(item.getAmount());
                }
            }
            totalItems.setQuantity(count);
            totalItems.setWeight(weight);
            totalItems.setCostPrice(costprice);
            totalItems.setDealPrice(dealprice);
            totalItems.setAmount(amount);
        }

        return totalItems;
    }

    private BigDecimal getTotalCostAmount(List<ConsignOrderItems> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (ConsignOrderItems item : orderItems) {
            if (item.getWeight() != null && item.getCostPrice() != null) {
                total = total.add(item.getWeight().multiply(item.getCostPrice()).setScale(CbmsNumberUtil.MoneyDigit, BigDecimal.ROUND_HALF_UP));
            }
        }

        return total;
    }

    @RequestMapping(value = "contractupload.html", method = RequestMethod.GET)
    public void contractUpload(ModelMap out,
                               @RequestParam("orderid") Long orderId,
                               @RequestParam("contractid") Long contractId,
                               @RequestParam("accountname") String accountName,
                               @RequestParam(value = "type",required = false) String type) {
        try {
            accountName = URLDecoder.decode(accountName, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        if (orderId != 0 && contractId != 0) {
            ConsignOrderContract contract = consignOrderContractService.selectByPrimaryKey(contractId);
            if (contract != null) {
                List<ConsignOrderAttachment> attachmentList = consignOrderAttachmentService.getAttachmentByContractId(contractId);
                String contractNo = contract.getContractCodeCust();
                out.put("orderid", orderId);
                out.put("contractid", contractId);
                out.put("attachmentlist", attachmentList);
                out.put("accountname", accountName);
                out.put("contractno", contractNo);
                out.put("contractType", type);
            }
        }
    }


    @OpLog(OpType.UploadContract)
    @OpParam("contractId")
    @OpParam("contractNo")
    @RequestMapping(value = "contractupload.html", method = RequestMethod.POST)
    @ResponseBody
    public Result contractUploadSubmit(
            @RequestParam("contractid") Long contractId,
            @RequestParam("contractno") String contractNo,
            @RequestParam("contractType") String contractType) {
        Result result = new Result();
        User user = getLoginUser();
        ResultDto resultDto = consignOrderService.uploadContract(contractId, contractNo, user,contractType);
        result.setSuccess(resultDto.isSuccess());
        result.setData(resultDto.getMessage());
        return result;
    }

    @OpLog(OpType.AddConsignOrderAttachment)
    @OpParam("orderId")
    @OpParam("contractId")
    @RequestMapping(value = "addattachment.html", method = RequestMethod.POST)
    @ResponseBody
    public Result addAttachment(
            MultipartHttpServletRequest request,
            @RequestParam("orderid") Long orderId,
            @RequestParam("contractid") Long contractId,
            @RequestParam("contractType") String contractType) {
        Result result = new Result();
        User user = getLoginUser();
        List<MultipartFile> contractPicList = request
                .getFiles("contractimgs");
        ResultDto resultDto = consignOrderService.addCustomerContractAttachments(orderId, contractId, contractPicList, user,contractType);
        if(resultDto.isSuccess()){
            result.setData(consignOrderAttachmentService.getAttachmentByContractId(contractId));
            result.setSuccess(true);
        }else {
            result.setSuccess(resultDto.isSuccess());
            result.setData(resultDto.getMessage());
        }
        return result;
    }

    @OpLog(OpType.DeleteConsignOrderAttachment)
    @OpParam("orderId")
    @OpParam("contractId")
    @OpParam(name = "attachmentid", value = "attachmentId")
    @RequestMapping(value = "deleteattachment.html", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteAttachment(
            @RequestParam("orderid") Long orderId,
            @RequestParam("contractid") Long contractId,
            @RequestParam("attachmentid") Long attachmentid,
            @RequestParam("contractType") String contractType) {
        Result result = new Result();
        User user = getLoginUser();
        ResultDto resultDto = consignOrderService.deleteCustomerContractAttachment(orderId, contractId, attachmentid, user,contractType);
        if(resultDto.isSuccess()){
            result.setData(consignOrderAttachmentService.getAttachmentByContractId(contractId));
            result.setSuccess(true);
        }else {
            result.setSuccess(resultDto.isSuccess());
            result.setData(resultDto.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "contractattachmentview.html", method = RequestMethod.GET)
    public void contractUpload(ModelMap out,HttpServletRequest request, @RequestParam("contractid") Long contractId) {
        if (contractId != 0) {
            List<ConsignOrderAttachment> attachmentList = consignOrderAttachmentService.getAttachmentByContractId(contractId);
            out.put("attachmentlist", attachmentList);
            /*if(attachmentList!=null){
                for(ConsignOrderAttachment coa:attachmentList){
                    srcs.add(request.getContextPath()+"/common/getfile.html?key="+coa.getFileUrl());
                }
            }*/
        }
    }

}
