package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.CertificateInvoiceDto;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCertificateInvoice;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.query.CertificateInvoiceQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author  peanut
 * @description 需补齐买、卖家凭证的已开票订单表对象
 * @date 2016/04/12
 */
@Repository
public interface BusiConsignOrderCertificateInvoiceDao {
    int deleteByPrimaryKey(Long id);

    int insert(BusiConsignOrderCertificateInvoice record);

    int insertSelective(BusiConsignOrderCertificateInvoice record);

    BusiConsignOrderCertificateInvoice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusiConsignOrderCertificateInvoice record);

    int updateByPrimaryKey(BusiConsignOrderCertificateInvoice record);

    /**
     * 批量插入
     * @param list
     * @date 2016/04/12
     * @return
     */
    void doBatchInsert(List<CertificateInvoiceDto> list);

    /**
     * 根据查询对象检索数据
     * @param query  查询对象
     * @see CertificateInvoiceQuery
     * @author peanut
     * @date 2016/04/13
     * @return
     */
    List<BusiConsignOrderCertificateInvoice> searchCertificateInvoice(CertificateInvoiceQuery query);
}