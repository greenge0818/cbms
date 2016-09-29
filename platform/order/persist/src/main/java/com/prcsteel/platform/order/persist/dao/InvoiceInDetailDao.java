package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceInDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceInWithDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceInDetail;
import com.prcsteel.platform.order.model.query.InvoiceInListQuery;

public interface InvoiceInDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceInDetail record);

    int insertSelective(InvoiceInDetail record);

    int batchInsert(List<InvoiceInDetail> records);

    InvoiceInDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceInDetail record);

    int updateByPrimaryKey(InvoiceInDetail record);

    /**
     * 根据发票号查询详情
     *
     * @param invoiceInId 发票ID
     * @return
     */
    List<InvoiceInDetailDto> selectByInvoiceInId(Long invoiceInId);

    /**
     * 发票详情
     *
     * @param invoiceInId 发票ID
     * @return
     */
    List<InvoiceInDetailDto> queryByInvoiceInId(Long invoiceInId);
    
    List<InvoiceInDetail> queryByInvoiceInIds(List<Long> invoiceInIds);

    /**
     * 根据条件查询进项发票清单
     *
     * @param queryParam
     * @return
     */
    List<InvoiceInWithDetailDto> queryInvoiceInDetailListByParams(InvoiceInListQuery queryParam);

    /**
     * 根据条件查询进项发票清单总记录数
     *
     * @param queryParam
     * @return
     */
    int queryInvoiceInDetailTotalByParams(InvoiceInListQuery queryParam);

    /**
     * 根据发票ID查询详情
     *
     * @param invoiceInId 发票ID
     * @return 详情
     */
    List<InvoiceInDetail> selectDetailByInvoiceInId(Long invoiceInId);
    
    List<InvoiceInDetail> queryByIds(List<Long> ids);

	int deleteByInvoiceIds(@Param("inIds")List<Long> inIds);

    int deleteByIds(@Param("ids")List<Long> ids);

}