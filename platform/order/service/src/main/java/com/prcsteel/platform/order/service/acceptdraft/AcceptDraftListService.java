package com.prcsteel.platform.order.service.acceptdraft;

import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.order.model.model.AcceptDraftList;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by prcsteel on 2015/12/10.
 */
public interface AcceptDraftListService {
    int deleteByPrimaryKey(Long id);

    int insert(AcceptDraftList record);

    int insertSelective(AcceptDraftList record);

    AcceptDraftList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcceptDraftList record);

    int updateByPrimaryKey(AcceptDraftList record);

    List<AcceptDraftList> queryByParam(AcceptDraftQuery query);

    Integer countByParam(AcceptDraftQuery query);

    List<AcceptDraftList> queryByAccountStatus(@Param("accountIsd") Long accountId, @Param("status") String status);

    List<AcceptDraftList>  findByParam(String code,String startTime,String endTime,String codeIds);

     boolean uploadingExcel(List<PageData> listPageData,User user);

    Integer findById( String code);



}
