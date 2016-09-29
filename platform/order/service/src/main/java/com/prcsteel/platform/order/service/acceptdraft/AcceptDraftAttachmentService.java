package com.prcsteel.platform.order.service.acceptdraft;

import com.prcsteel.platform.order.model.model.AcceptDraftAttachment;

import java.util.List;

/**
 * Created by lichaowei on 2015/11/11.
 */
public interface AcceptDraftAttachmentService{
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

    /**
     * modify by wangxianjun 20151204 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     * 根据银票Id查询列表
     * @param acceptDraftId 银票Id
     * @return 集合
     */
    List<AcceptDraftAttachment> queryMainByAcceptDraftId(Long acceptDraftId);
}
