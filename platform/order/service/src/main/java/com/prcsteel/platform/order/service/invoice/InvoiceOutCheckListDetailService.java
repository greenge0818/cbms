package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.ChecklistDetailDto;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;

import java.util.List;

/**
 * Created by rolyer on 15-9-16.
 */
public interface InvoiceOutCheckListDetailService {
    /**
     * 查询开票清单
     * @param query 查询参数/条件
     * @return
     */
    List<ChecklistDetailDto> queryByCondition(ChecklistDetailQuery query);

    /**
     * 统计开票清单数
     * @param query 查询参数/条件
     * @return
     */
    Integer countByCondition(ChecklistDetailQuery query);
 
    public ChecklistDetailDto queryByConditionTotal(ChecklistDetailQuery query);
}
