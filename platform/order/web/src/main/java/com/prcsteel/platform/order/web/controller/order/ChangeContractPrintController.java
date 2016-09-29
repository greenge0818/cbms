package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.ConsignOrderContractService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderItemsChangedrecordService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import com.prcsteel.platform.order.web.vo.ContractVo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 预览打印变更合同
 * @author chengui
 * @date 2016年8月24日
 */
@Controller
@RequestMapping("/order/changecontract/print")
public class ChangeContractPrintController extends BaseController{

    private Logger log = Logger.getLogger(ChangeContractPrintController.class);

    ShiroVelocity permissionLimit = new ShiroVelocity();

    @Resource
    ConsignOrderService consignOrderService;

    @Resource
    AccountService accountService;

    @Resource
    SysSettingService sysSettingService;

    @Resource
    SecondSettlementLogService secondSettlementLogService;
    @Resource
    PayRequestService payRequestService;
    @Resource
    BankTransactionInfoService bankTransactionInfoService;
    @Resource
    ConsignOrderChangeService consignOrderChangeService;
    @Resource
    AcceptDraftService acceptDraftService;
    @Resource
    ConsignOrderItemsService consignOrderItemsService;
    @Resource
    ConsignOrderItemsChangedrecordService itemsChangedrecordService;
    @Resource
    AccountContractTemplateService accountContractTemplateService;
    @Resource
    UserService userService;
    @Resource
    OrganizationService organizationService;
    @Resource
    ConsignOrderContractService consignOrderContractService;

    private static final int MIX_ROW_NUMBER = 7; //订单项最小行数
    private static final String CONTRACT_BUYER = "buyer"; //销售
    private static final String CONTRACT_SELLER = "seller"; //采购
    private static final String CONTRACT_TITLE_BUYER = "合同变更补充协议（销售合同）"; //销售
    private static final String CONTRACT_TITLE_SELLER = "合同变更补充协议（采购合同）"; //采购

    /**
     * 变更合同预览/打印
     *
     * @param out
     * @param orderChangeId
     */
    @RequestMapping("contract.html")
    public String contract(ModelMap out, Integer orderChangeId) {
        String url = "order/changecontract/contract";

        ConsignOrderChange order = consignOrderChangeService.selectByPrimaryKey(orderChangeId);//变更订单信息
        ConsignOrder originalOrder = consignOrderService.queryById(order.getOrderId());// 原订单信息

        if (order == null || originalOrder == null || originalOrder.getAccountId() == null) {
           out.put("msg","订单不存在或信息有误");
            return url;
        }
        // TODO: 2016/8/23 详情页增加合同功能后，此处状态需要修改 modify by wangxianjun 有6个变更状态可以打印变更合同
        if (!ConsignOrderAlterStatus.PENDING_RELATE.getCode().equals(order.getStatus()) && !ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(order.getStatus())
            && !ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode().equals(order.getStatus()) && !ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getStatus())
                && !ConsignOrderAlterStatus.PENDING_PRINT_PAY.getCode().equals(order.getStatus()) && !ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(order.getStatus())
                ) {
            out.put("msg","请核对订单状态");
            return url;
        }

        Account account = getAccount(originalOrder.getAccountId());
        String content = getTemplate(account.getId(), AccountType.buyer.toString());
        User owner = userService.queryById(originalOrder.getOwnerId());
        Organization organization = organizationService.queryById(owner.getOrgId());
        if (StringUtils.isBlank(content)) {
            out.put("msg","没有合同模板");
            return url;
        }

        ContractVo vo = new ContractVo();
        vo.setCompanyName(account.getName());
        vo.setBuyer(true);
        vo.setContent(buildContractForBuyer(content, account, orderChangeId, organization));

        List<ContractVo> contracts = new ArrayList<ContractVo>();
        contracts.add(vo);
        contracts.addAll(getContractOfSeller(orderChangeId, organization));

        out.put("contracts", contracts);

        // 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
        if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
            out.put("financeorder", true);
        }

        return url;
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
     * 获取模板
     *
     * @param id
     * @return
     */
    public String getTemplate(long id, String type) {
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
            content = accountContractTemplateService.readTemplate(t, true);
        }

        return content;
    }

    /**
     * 构造买家变更合同信息
     *
     * @param content
     * @param account
     * @return
     */
    private String buildContractForBuyer(String content, Account account, Integer orderChangeId, Organization organization) {

        ConsignOrderChange order = consignOrderChangeService.selectByPrimaryKey(orderChangeId);//变更订单信息
        // 变更订单资源信息
        List<ConsignOrderItemsChange> orderChangeItems = consignOrderChangeService.queryOrderItemsExceptStatusByChangeOrderId(orderChangeId, ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode());
        ConsignOrderItems totalChangeItems = consignOrderChangeService.getChangeSellerTotalItems(orderChangeItems);
        Long orderId = order.getOrderId();

        // 原订单资源信息
        List<ConsignOrderItems> originalOrderItems = consignOrderService.queryOrderItemsById(orderId);
        ConsignOrderItems originalTotalItems = consignOrderChangeService.getSellerTotalItems(originalOrderItems);

        // TODO: 2016/8/26 签订时间改成变更订单审核通过时间
        Calendar cal = Calendar.getInstance();
        Date date = consignOrderService.queryApprovedDateByOrderId(orderId);
        if (date != null) {
            cal.setTime(date);
        }

        content = content.replaceAll("\\r", "");
        content = content.replaceAll("\\n", "");
        content = content.replaceAll("\\t", "");
        content = content.replaceAll("#companyName#", account.getName());
        content = content.replace("#contractNo#", order.getContractCode());
        content = content.replace("<tbody id=\"items\"></tbody>", restoreItems(originalOrderItems, CONTRACT_BUYER));
        content = content.replace("#totalQuantity#", originalTotalItems.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(originalTotalItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(originalTotalItems.getAmount()));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(originalTotalItems.getAmount()));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());

        content = content.replace("<tbody id=\"itemsChange\"></tbody>", restoreChangeItems(orderChangeItems, CONTRACT_BUYER));
        content = content.replace("#totalQuantityChange#", totalChangeItems.getQuantity().toString());
        content = content.replace("#totalWeightChange#", Tools.formatBigDecimal(totalChangeItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmountChange#", Tools.formatBigDecimal(totalChangeItems.getAmount()));
        content = content.replace("#totalCapitalChange#", NumberToCNUtils.number2CNMontrayUnit(totalChangeItems.getAmount()));

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
     * 构造买家合同信息
     * @return
     */
    public String buildChangeContractRecordForBuyer(String content, Account account, ConsignOrderChange orderChange, List<ConsignOrderItemsChange> orderChangeItems, List<ConsignOrderItemsChange> exOrderChangeItems, Organization organization) {
        // 查询参数
        QueryChangeOrderDto query = new QueryChangeOrderDto();
        List<ConsignOrderAlterStatus> alterStatuses = new ArrayList<ConsignOrderAlterStatus>();
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS1);
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS2);
        alterStatuses.add(ConsignOrderAlterStatus.CHANGED_SUCCESS3);
        query.setAlterStatuses(alterStatuses);

        // 变更订单资源信息
        ConsignOrderItems totalChangeItems = consignOrderChangeService.getChangeSellerTotalItems(orderChangeItems);
        Long orderId = orderChange.getOrderId();

        ConsignOrderItems exTotalChangeItems = consignOrderChangeService.getChangeSellerTotalItems(exOrderChangeItems);

        // TODO: 2016/8/26 签订时间改成变更订单审核通过时间
        Calendar cal = Calendar.getInstance();
        Date date = consignOrderService.queryApprovedDateByOrderId(orderId);
        if (date != null) {
            cal.setTime(date);
        }

        content = content.replaceAll("\\r", "");
        content = content.replaceAll("\\n", "");
        content = content.replaceAll("\\t", "");
        content = content.replaceAll("#companyName#", account.getName());
        content = content.replace("#contractNo#", orderChange.getContractCode());
        content = content.replace("<tbody id=\"items\"></tbody>", restoreChangeItems(exOrderChangeItems, CONTRACT_BUYER));
        content = content.replace("#totalQuantity#", exTotalChangeItems.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(exTotalChangeItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(exTotalChangeItems.getAmount()));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(exTotalChangeItems.getAmount()));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());

        content = content.replace("<tbody id=\"itemsChange\"></tbody>", restoreChangeItems(orderChangeItems, CONTRACT_BUYER));
        content = content.replace("#totalQuantityChange#", totalChangeItems.getQuantity().toString());
        content = content.replace("#totalWeightChange#", Tools.formatBigDecimal(totalChangeItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmountChange#", Tools.formatBigDecimal(totalChangeItems.getAmount()));
        content = content.replace("#totalCapitalChange#", NumberToCNUtils.number2CNMontrayUnit(totalChangeItems.getAmount()));

        content = content.replace("#contractAddress#", orderChange.getContractAddress() == null ? "" : orderChange.getContractAddress());
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
     * 构建所有卖家变更合同信息
     * @return
     */
    public List<ContractVo> buildChangeContractRecordForSeller(ConsignOrderChange orderChange, List<ConsignOrderItemsChange> orderChangeItems, List<ConsignOrderItemsChange> exOrderChangeItems, Organization organization) {
        List<ContractVo> contracts = new ArrayList<ContractVo>();

        // 原订单信息
        ConsignOrder originalOrder = consignOrderService.queryById(orderChange.getOrderId());

        //modify by wangxianjun 查询合同信息应该用变更订单ID
        List<ConsignOrderContract> consignOrderContracts = consignOrderContractService.queryByChangeOrderId(orderChange.getId());
        for (ConsignOrderContract coc : consignOrderContracts) {
            Account account = getAccount(coc.getCustomerId());
            String content = getTemplate(account.getId(), AccountType.seller.toString());

            List<ConsignOrderItemsChange> myExChangeItems = exOrderChangeItems.stream().filter(a -> a.getSellerId().equals(coc.getCustomerId())).collect(Collectors.toList());
            List<ConsignOrderItemsChange> myChangeItems = orderChangeItems.stream().filter(a -> a.getSellerId().equals(coc.getCustomerId())).collect(Collectors.toList());

            ContractVo vo = new ContractVo();
            vo.setTitle(CONTRACT_TITLE_SELLER);
            vo.setCompanyName(account.getName());
            vo.setContent(buildChangeContractRecordForSeller(content, account, originalOrder, coc.getContractCodeAuto(), myExChangeItems, myChangeItems, organization));
            contracts.add(vo);
        }
        return contracts;
    }


    /**
     * 构建所有卖家变更合同信息
     * @return
     */
    private List<ContractVo> getContractOfSeller(Integer orderChangeId, Organization organization) {
        List<ContractVo> contracts = new ArrayList<ContractVo>();

        // 变更订单信息
        ConsignOrderChange order = consignOrderChangeService.selectByPrimaryKey(orderChangeId);
        List<ConsignOrderItemsChange> orderChangeItems = consignOrderChangeService.queryOrderItemsExceptStatusByChangeOrderId(orderChangeId, ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode());
        // 原订单信息
        ConsignOrder originalOrder = consignOrderService.queryById(order.getOrderId());
        List<ConsignOrderItems> originalOrderItems = consignOrderService.queryOrderItemsById(order.getOrderId());

        //modify by wangxianjun 查询合同信息应该用变更订单ID
        List<ConsignOrderContract> consignOrderContracts = consignOrderContractService.queryByChangeOrderId(order.getId());
        for (ConsignOrderContract coc : consignOrderContracts) {
            Account account = getAccount(coc.getCustomerId());
            String content = getTemplate(account.getId(), AccountType.seller.toString());

            List<ConsignOrderItems> myOriginalItems = originalOrderItems.stream().filter(a -> a.getSellerId().equals(coc.getCustomerId())).collect(Collectors.toList());
            ConsignOrderItems totalOriginalItems = consignOrderChangeService.getSellerTotalItems(myOriginalItems);

            List<ConsignOrderItemsChange> myChangeItems = orderChangeItems.stream().filter(a -> a.getSellerId().equals(coc.getCustomerId())).collect(Collectors.toList());
            ConsignOrderItems totalChangeItems = consignOrderChangeService.getChangeSellerTotalItems(orderChangeItems);

            ContractVo vo = new ContractVo();
            vo.setCompanyName(account.getName());
            vo.setContent(buildContractForSeller(content, account, originalOrder, coc.getContractCodeAuto(), myOriginalItems, totalOriginalItems, myChangeItems, totalChangeItems, organization));

            contracts.add(vo);
        }

        return contracts;
    }


    /**
     * 生产卖家合同信息
     *
     * @param content
     * @param account
     * @param order
     * @param contractCode
     * @param items
     * @param totalItems
     * @return
     */
    private String buildContractForSeller(String content, Account account, ConsignOrder order, String contractCode, List<ConsignOrderItems> items, ConsignOrderItems totalItems, List<ConsignOrderItemsChange> itemsChange, ConsignOrderItems totalItemsChange, Organization organization) {
        // TODO: 2016/8/26 签订时间改成变更订单审核通过时间
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
        content = content.replace("#contractNo#", contractCode);
        content = content.replace("<tbody id=\"items\"></tbody>", restoreItems(items, CONTRACT_SELLER));
        content = content.replace("#totalQuantity#", totalItems.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(totalItems.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(totalCostAmount));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(totalCostAmount));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());

        BigDecimal totalCostAmountChange = new BigDecimal(itemsChange.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum());
        content = content.replace("<tbody id=\"itemsChange\"></tbody>", restoreChangeItems(itemsChange, CONTRACT_SELLER));
        content = content.replace("#totalQuantityChange#", totalItemsChange.getQuantity().toString());
        content = content.replace("#totalWeightChange#", Tools.formatBigDecimal(totalItemsChange.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmountChange#", Tools.formatBigDecimal(totalCostAmountChange));
        content = content.replace("#totalCapitalChange#", NumberToCNUtils.number2CNMontrayUnit(totalCostAmountChange));

        content = content.replace("#contractAddress#", order.getContractAddress() == null ? "" : order.getContractAddress());
        content = content.replace("#year#", cal.get(Calendar.YEAR) + "");
        content = content.replace("#month#", (cal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#date#", cal.get(Calendar.DATE) + "");
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");

        content = content.replace("#orgName#", organization.getName());
        content = content.replace("#orgAddr#", StringUtils.isEmpty(organization.getAddress()) ? "" : organization.getAddress());
        content = content.replace("#orgFax#", StringUtils.isEmpty(organization.getFax()) ? "" : organization.getFax());
        content = content.replace("#clause#", "");
        return content;

    }


    /**
     * 生产卖家变更合同信息
     * @return
     */
    private String buildChangeContractRecordForSeller(String content, Account account, ConsignOrder order, String contractCode, List<ConsignOrderItemsChange> exItemsChange, List<ConsignOrderItemsChange> itemsChange, Organization organization) {

        ConsignOrderItems exTotalItemsChange = consignOrderChangeService.getChangeSellerTotalItems(exItemsChange);
        ConsignOrderItems totalItemsChange = consignOrderChangeService.getChangeSellerTotalItems(itemsChange);

        Calendar cal = Calendar.getInstance();
        Date date = consignOrderService.queryApprovedDateByOrderId(order.getId());
        if (date != null) {
            cal.setTime(date);
        }

        BigDecimal exTotalCostAmountChange = new BigDecimal(exItemsChange.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum());

        content = content.replaceAll("\\r", "");
        content = content.replaceAll("\\n", "");
        content = content.replaceAll("\\t", "");

        content = content.replaceAll("#companyName#", account.getName());
        content = content.replace("#contractNo#", contractCode);
        content = content.replace("<tbody id=\"items\"></tbody>", restoreChangeItems(exItemsChange, CONTRACT_SELLER));
        content = content.replace("#totalQuantity#", exTotalItemsChange.getQuantity().toString());
        content = content.replace("#totalWeight#", Tools.formatBigDecimal(exTotalItemsChange.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmount#", Tools.formatBigDecimal(exTotalCostAmountChange));
        content = content.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(exTotalCostAmountChange));
        content = content.replace("#fax#", account.getFax() == null ? "" : account.getFax());
        content = content.replace("#legalPersonName#", account.getLegalPersonName() == null ? "" : account.getLegalPersonName());

        BigDecimal totalCostAmountChange = new BigDecimal(itemsChange.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum());
        content = content.replace("<tbody id=\"itemsChange\"></tbody>", restoreChangeItems(itemsChange, CONTRACT_SELLER));
        content = content.replace("#totalQuantityChange#", totalItemsChange.getQuantity().toString());
        content = content.replace("#totalWeightChange#", Tools.formatBigDecimal(totalItemsChange.getWeight(), Constant.WEIGHT_PRECISION));
        content = content.replace("#totalAmountChange#", Tools.formatBigDecimal(totalCostAmountChange));
        content = content.replace("#totalCapitalChange#", NumberToCNUtils.number2CNMontrayUnit(totalCostAmountChange));

        content = content.replace("#contractAddress#", order.getContractAddress() == null ? "" : order.getContractAddress());
        content = content.replace("#year#", cal.get(Calendar.YEAR) + "");
        content = content.replace("#month#", (cal.get(Calendar.MONTH) + 1) + "");
        content = content.replace("#date#", cal.get(Calendar.DATE) + "");
        content = content.replace("#addr#", account.getAddr() != null ? account.getAddr() : "");

        content = content.replace("#orgName#", organization.getName());
        content = content.replace("#orgAddr#", StringUtils.isEmpty(organization.getAddress()) ? "" : organization.getAddress());
        content = content.replace("#orgFax#", StringUtils.isEmpty(organization.getFax()) ? "" : organization.getFax());
        content = content.replace("#clause#", "");
        return content;

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

    /**
     * 订单项组合排版
     *
     * @param items
     * @param type  销售/采购
     * @return
     */
    private String restoreChangeItems(List<ConsignOrderItemsChange> items, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tbody>");
        for (ConsignOrderItemsChange i : items) {
            sb.append(getChangeRow(i, type));
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
     * 单个订单项排版
     *
     * @param item
     * @return
     */
    private String getChangeRow(ConsignOrderItemsChange item, String type) {
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
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getSaleAmount()) + "</td>");
        } else {
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getCostPrice()) + "</td>");
            sb.append("<td class='text-right'>" + Tools.formatBigDecimal(item.getWeight().multiply(item.getCostPrice())) + "</td>");
        }


        sb.append("</tr>");

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


}
