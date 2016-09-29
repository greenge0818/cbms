package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;

/**
 *
 * 采购单详情
 *
 * 用于继续询价时展示采购单信息详情
 * Created by Rolyer on 2015/12/8.
 */
public class PurchaseOrderDetailDto {

    /**
     * 采购单（主表信息）
     */
    private PurchaseOrderDto purchaseOrder;

    /**
     * 采购明细(附带属性)
     */
    private List<PurchaseOrderItemsDto> purchaseOrderItems;

    /**
     * 询价记录+（新）匹配结果集
     */
    private SearchResultDto searchResult;

    /**
     * 构造函数
     */
    public PurchaseOrderDetailDto() {
    }

    /**
     * 构造函数
     * @param purchaseOrder
     * @param purchaseOrderItems
     * @param searchResult
     */
    public PurchaseOrderDetailDto(PurchaseOrderDto purchaseOrder, List<PurchaseOrderItemsDto> purchaseOrderItems, SearchResultDto searchResult) {
        this.purchaseOrder = purchaseOrder;
        this.purchaseOrderItems = purchaseOrderItems;
        this.searchResult = searchResult;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDto purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<PurchaseOrderItemsDto> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(List<PurchaseOrderItemsDto> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public SearchResultDto getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResultDto searchResult) {
        this.searchResult = searchResult;
    }
}
