package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.ConfirmedInvoiceOutDto;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceOut;

public interface InvoiceOutDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOut record);

    int insertSelective(InvoiceOut record);

    InvoiceOut selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOut record);

    int updateByPrimaryKey(InvoiceOut record);

    List<InvoiceOut> selectByParams(Map<String, Object> param);

    Integer countByParams(Map<String, Object> param);

    InvoiceOut selectByCode(String code);

    List<ConfirmedInvoiceOutDto> selectByIds(List<Long> ids);

    public int updateExpressIdById(@Param("id") Long id, @Param("expressId") Long expressId);

    public List<Express> query(@Param("orgId") Long orgId, @Param("buyerName") String buyerName, @Param("startTime") String startTime, @Param("endTime") String endTime);
}