package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.dto.BaseDataDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.*;
import com.prcsteel.platform.smartmatch.model.model.InquiryDetailData;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderItemsQuery;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by Rolyer on 2015/11/19.
 */
public interface PurchaseOrderService {
    /**
     * 保存采购单
     * @param purchaseOrder
     * @param dto 表体资源信息等
     * @param user 操作人
     * @return
     */
    PurchaseOrder save(PurchaseOrder purchaseOrder, List<PurchaseOrderItemsDto> dto, User user);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    PurchaseOrder queryById(Long id);

    /**
     * 查询公司
     * @return
     */
    List<BaseDataDto> getAllAccount();

    /**
     *  查询联系人
     * @param tel 电话
     * @return
     */
    List<AccountContact> queryAccountContact(String tel, String  name);

    /**
     * 查询公司
     * @param tel 电话
     * @param name 联系人
     * @return
     */
    List<Account> queryAccount(String tel, String  name);

    /**
     * 展示采购单列表
     * @param purchaseOrderQuery
     * @return
     */
    List<PurchaseOrderDto> selectPurchaseList(PurchaseOrderQuery purchaseOrderQuery);

    /**
     * 查询采购单总数
     * @param purchaseOrderQuery
     * @return
     */
    int countPurchaseOrder(PurchaseOrderQuery purchaseOrderQuery);

    /**
     * 根据id查找采购单主表信息用来显示在界面上
     * @param id
     * @return
     */
    PurchaseOrderDtoForShow queryByIdForShow(Long id);

    /**
     *
        * @Title: getItemList
        * @Description: 根据采购单ID查询采购明细
        * @param @param purchaseOrderId
        * @param @return    参数
        * @return List<PurchaseOrderItems>    返回类型
        * @throws
     */
    List<PurchaseOrderItems> getItemList(Long purchaseOrderId);


    /**
     * 点击采购单操作栏的继续询价展示采购单明细
     * @return
     */
    List<PurchaseOrderDto> getListByPurchaseOrderId(Long id);

    /**
     * 根据采购单编号获得详情
     * @param purchaseOrderId 采购单编号 ID
     * @return
     */
    PurchaseOrderDetailDto retrievePurchaseOrderById(Long purchaseOrderId);
    /**
     * 更新指定采购单状态
     * @param id 采购单ID
     * @param status 状态值
     * @param loginId 操作人，即：当前登录人
     * @return
     */
    public int updateStatusById(Long id, String status, String loginId);
    /**
     * 选择买家后加载该买家最近2次的采购单明细
     * @return
     */
    List<PurchaseOrderItemsDto> getHistoryPurchaseOrderItemsByTelOrAccount(PurchaseOrderItemsQuery query);

    /**
     * 关闭采购单
     * @param id
     */
    void closeOrder(Long id, String operation, String reason, User user);

    Map<String,List<BaseDataDto>> loadFactoryByCategory(Long purchaseOrderId);

    /**
     * 根据采购单Id获取采购单明细
     * @param purchaseOrderId
     * @return
     */
    List<SearchResultItemsDto> getItemsByPurchaseOrderId(Long purchaseOrderId);

    /**
     * 根据采购单Id获取询价详情列表
     * @param purchaseOrderId
     * @return
     */
    List<InquiryDetailData> getInquiryHistoryByPurchaseId(Long purchaseOrderId);

    /**
     * 指派采购单
     *
     * @param id
     * @return
     */
    void assignOrder(Long id,Integer assignto, Long orgId, Long userId, boolean isActivate, User operator);

    /**
     * 改派采购单
     *
     * @param id
     * @return
     */
    void reassignOrder(Long id, String remark, User operator);

    /**
     * 根据采购单id报价单推送（已报价）
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/6/27
     */
    void pushQuotationOrder(Long quotationOrderId, Long orgId, Long userId);

    /**
     * 根据需求单号查询询价单
     * @param requirementCode
     */
	public Long searchPurchaseOrderByrequrimentCode(String requirementCode);
    /**
     * 报价单点开单前的处理
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/7/1
     */
    String preOpen(Long quotationOrderId);
    /**
     *  询价单点开单前的处理
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/7/1
     */
    public void preOpenPurchaseOrder(Long purchaseOrderId,String requestCode);
    /***
     * 获取询价单交货地
     * @param orderId
     * @return
     */
    City getCityByOrderId(Long orderId);
    
	/**
	 * Mq 消息推送询价单消息保存
	 * @param msg
	 * @return
	 */
    int insertInquiryMsg(InquiryOrderMessageQueue msg);
    
    /**
     * 获取所有扩展属性
     * @return
     */
    List<PurchaseOrderItemsAttributeDto> getAllAttributes();

    /**
     * 根据报价单详情的ID更新其对应的询价单的状态
     * @param id
     * @param string
     * @param loginId
     */
	public void updatePurchaseOrderStatusByQuotataionItemId(Long id, String string,
			String loginId);

	/**
	 * 关闭并推送报价
	 * @param id
	 * @param reason
	 * @param loginUser
	 */
	public void pushAndCloseOrder(Long id, String reason, User loginUser);

    /**
     * 获取回退人员
     * @return
     */
    List<User> getBackoffUsers(Long userId);

}
