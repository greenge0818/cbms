package com.prcsteel.platform.order.service.acceptdraft.impl;


import com.prcsteel.platform.order.model.enums.AcceptDraftAttachmentType;

import com.prcsteel.platform.order.model.model.AcceptDraftAttachment;
import com.prcsteel.platform.order.persist.dao.AcceptDraftAttachmentDao;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftAttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lichaowei on 2015/11/11.
 */
@Service("acceptDraftAttachmentService")
public class AcceptDraftAttachmentServiceImpl implements AcceptDraftAttachmentService {
    @Resource
    AcceptDraftAttachmentDao draftAttachmentDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return draftAttachmentDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AcceptDraftAttachment record) {
        return draftAttachmentDao.insert(record);
    }

    @Override
    public int insertSelective(AcceptDraftAttachment record) {
        return draftAttachmentDao.insertSelective(record);
    }

    @Override
    public AcceptDraftAttachment selectByPrimaryKey(Long id) {
        return draftAttachmentDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AcceptDraftAttachment record) {
        return draftAttachmentDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AcceptDraftAttachment record) {
        return draftAttachmentDao.updateByPrimaryKey(record);
    }

    /**
     * 根据银票Id查询列表
     *
     * @param acceptDraftId 银票Id
     * @return 集合
     */
    @Override
    public List<AcceptDraftAttachment> queryByAcceptDraftId(Long acceptDraftId) {
        return draftAttachmentDao.queryByAcceptDraftId(acceptDraftId);
    }

    /**
     * 根据银票Id查询列表
     * modify by wangxianjun 20151204 迭代7 银票由只能上传一张照片改为银票可以最多上传10张图片
     * @param acceptDraftId 银票Id
     * @return 集合
     */
    @Override
    public  List<AcceptDraftAttachment> queryMainByAcceptDraftId(Long acceptDraftId) {
        List<AcceptDraftAttachment> list = draftAttachmentDao.selectByAcceptDraftIdAndType(acceptDraftId, AcceptDraftAttachmentType.MAIN.getCode());
        //return (list != null && list.size() > 0) ? list.get(0) : null;
        return list;
    }

}
