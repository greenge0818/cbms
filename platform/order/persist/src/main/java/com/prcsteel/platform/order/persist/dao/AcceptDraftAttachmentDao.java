package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.model.AcceptDraftAttachment;
import org.apache.ibatis.annotations.Param;


public interface AcceptDraftAttachmentDao {
    int deleteByPrimaryKey(Long id);

    int insert(AcceptDraftAttachment record);

    int insertSelective(AcceptDraftAttachment record);

    AcceptDraftAttachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcceptDraftAttachment record);

    int updateByPrimaryKey(AcceptDraftAttachment record);
    
    /**
     * 根据银票Id查询列表
     * @param acceptDraftId 银票Id
     * @return 集合
     */
    List<AcceptDraftAttachment> queryByAcceptDraftId(Long acceptDraftId);

    List<AcceptDraftAttachment> selectByAcceptDraftIdAndType(@Param("acceptDraftId")Long acceptDraftId,@Param("type")String type);

    /**
     * 根据银票id删除
     * @param acceptDraftId
     * @return
     */
    int deleteByAcceptDraftId(Long acceptDraftId);
    /**
     * 根据银票图片id删除
     * @param paramMap
     * @return
     */
    int deleteByAttachmentIds(Map<String, Object> paramMap);
}