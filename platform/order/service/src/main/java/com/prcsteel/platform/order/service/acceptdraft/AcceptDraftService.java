package com.prcsteel.platform.order.service.acceptdraft;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;

/**
 * Created by myh on 2015/11/11.
 */
public interface AcceptDraftService {
    int deleteByPrimaryKey(Long id);

    int insert(AcceptDraft record);

    int insertSelective(AcceptDraft record);

    AcceptDraft selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcceptDraft record);

    int updateByPrimaryKey(AcceptDraft record);

    boolean checkAcceptDarftIsPayed(Long acceptDarftId);

    /**
     * 撤回充值申请
     * @param id 记录Id
     */
    void withdrawAudit(Long id, User user);

    /**
     * 银票保存&&提交审核
     * @param acceptDraft
     * @param file
     * @param operation
     * @return
     */
    String save(AcceptDraft acceptDraft, List<MultipartFile> attachmentfiles, String operation, User user,String imgsId);

    /**
     * 按条件查询
     * @param query
     * @return
     */
    List<AcceptDraft> queryByParam(AcceptDraftQuery query);

    /**
     * 计数
     * @param query
     * @return
     */
    Integer countByParam(AcceptDraftQuery query);


    /**
     * 申请取消充值
     * @param id 票据单id
     */
    void applyCancleCharged(Long id,String reason, User user);

    /**
     * 银票充值审核
     *
     * @param acceptDraft 审核数据
     * @param user        用户
     */
    void confirmRecharge(AcceptDraft acceptDraft, User user);

    /**
     * 通过取消充值申请
     *
     * @param id    银票id
     * @param user        用户
     */
    void rollback(Long id, User user);

    /**
     * 不通过取消充值申请
     *
     * @param id    银票id
     * @param user        用户
     */
    void refuseRollback(Long id, User user);

    List<AcceptDraft> queryByAccountStatus(Long accountId, String status);
}
