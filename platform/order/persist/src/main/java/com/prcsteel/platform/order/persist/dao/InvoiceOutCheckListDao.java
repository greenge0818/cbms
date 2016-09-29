package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDto;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvoiceOutCheckListDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutCheckList record);

    int insertSelective(InvoiceOutCheckList record);

    InvoiceOutCheckList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutCheckList record);

    int updateByPrimaryKey(InvoiceOutCheckList record);

    /**
     * 查询待开票列表的已生成开票清单
     * @param orgIds
	 * @param status
     * @param start
     * @param length
     * @return
     */
    List<InvoiceOutCheckListDto> selectOutChecklistByStatus(@Param("orgIds") List<String> orgIds,
                                                            @Param("status") String status,
                                                            @Param("start") Integer start,
                                                            @Param("length") Integer length);

    /**
     *查询待开票列表的已生成开票清单数
     * @param orgIds
     * @param status
     * @return
     */
    int totalOutChecklistByStatus(@Param("orgIds") List<String> orgIds, @Param("status") String status);

    /**
     * 根据状态查询
     * @param status
     * @return
     */
    List<InvoiceOutCheckList> queryByStatus(@Param("status") String status);

    /**
     * 跟据编号更新状态
     * @param id
     * @param status
     * @return
     */
    int updateStatusById(@Param("id") Long id, @Param("status") String status);
}