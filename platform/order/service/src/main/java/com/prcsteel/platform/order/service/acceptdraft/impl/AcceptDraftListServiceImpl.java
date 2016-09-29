package com.prcsteel.platform.order.service.acceptdraft.impl;

import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.model.AcceptDraftList;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import com.prcsteel.platform.order.persist.dao.AcceptDraftListDao;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftListService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by prcsteel on 2015/12/10.
 */
@Service("acceptDraftListService")
public class AcceptDraftListServiceImpl implements AcceptDraftListService {
    @Resource
    AcceptDraftListDao acceptDraftListDao;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return acceptDraftListDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AcceptDraftList record) {
        return acceptDraftListDao.insert(record);
    }

    @Override
    public int insertSelective(AcceptDraftList record) {
        return acceptDraftListDao.insertSelective(record);
    }

    @Override
    public AcceptDraftList selectByPrimaryKey(Long id) {
        return acceptDraftListDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AcceptDraftList record) {
        return acceptDraftListDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AcceptDraftList record) {
        return acceptDraftListDao.updateByPrimaryKey(record);
    }

    /**
     * 银票查询列表
     */
    @Override
    public List<AcceptDraftList> queryByParam(AcceptDraftQuery query) {

        return acceptDraftListDao.queryByParam(query);
    }

    @Override
    public Integer countByParam(AcceptDraftQuery query) {
        return acceptDraftListDao.countByParam(query);
    }

    @Override
    public List<AcceptDraftList> queryByAccountStatus(@Param("accountId") Long accountId, @Param("status") String status) {
        return acceptDraftListDao.queryByAccountStatus(accountId, status);
    }

    @Override
    public List<AcceptDraftList> findByParam(String code, String startTime, String endTime, String codeIds) {
        List<Long> ids = null;
        if(!StringUtils.isBlank(codeIds)){
            ids = new ArrayList<Long>();
            String[] idsArr = codeIds.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                ids.add(Long.parseLong(idsArr[i]));
            }
        }
        return acceptDraftListDao.findByParam(code, startTime, endTime, ids);
    }

    /**
     * 上传表格  批量插入 and 批量修改
     * wangxiao
     *
     * @param listPageData
     * @param user
     * @return
     */
    public boolean uploadingExcel(List<PageData> listPageData,User user){
        List<AcceptDraftList> insertList = new ArrayList<AcceptDraftList>();
        List<AcceptDraftList> updateList = new ArrayList<AcceptDraftList>();
        for(PageData pageData : listPageData){
            AcceptDraftList acceptDraftList = new AcceptDraftList();
            String code = pageData.getString("var0");//票号
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(pageData.getString("var1")));//余额
            Date acceptanceDate = Tools.strToDate(pageData.getString("var2"), "yyyyMMdd");//承兑日
            Date endDate = Tools.strToDate(pageData.getString("var3"), "yyyyMMdd");//到期日
            String acceptanceBankCode = pageData.getString("var4");//承兑行行号
            String acceptanceBankFullName = pageData.getString("var5");//承兑行全称
            String drawerName = pageData.getString("var6");//出票人名称
            String drawerAccountCode = pageData.getString("var7");//出票人账号
            String drawerBankCode = pageData.getString("var8");//出票人开户行行号
            String drawerBankFullName = pageData.getString("var9");//出票人开户行全称
            String receiverName = pageData.getString("var10");//收款人名称
            String receiverAccountCode = pageData.getString("var11");//收款人账号
            String receiverBankFullName = pageData.getString("var12");//收款人开户行全称
            Integer adjustDateCount = Integer.parseInt(pageData.getString("var13"));//调整天数
            Integer readTimes = Integer.parseInt(pageData.getString("var14"));//背书次数
            String acceptanceAgreementNumber = pageData.getString("var15");//合同号
            String isDifferentBank = pageData.getString("var16");//是否我行代开他行签发票据

            acceptDraftList.setCode(code);//票号
            acceptDraftList.setAmount(amount);//余额
            acceptDraftList.setAcceptanceDate(acceptanceDate);//承兑日
            acceptDraftList.setEndDate(endDate);//到期日
            acceptDraftList.setAcceptanceBankCode(acceptanceBankCode);//承兑行行号
            acceptDraftList.setAcceptanceBankFullName(acceptanceBankFullName);//承兑行全称
            acceptDraftList.setDrawerName(drawerName);//出票人名称
            acceptDraftList.setDrawerAccountCode(drawerAccountCode);//出票人账号
            acceptDraftList.setDrawerBankCode(drawerBankCode);//出票人开户行行号
            acceptDraftList.setDrawerBankFullName(drawerBankFullName);//出票人开户行全称
            acceptDraftList.setReceiverName(receiverName);//收款人名称
            acceptDraftList.setReceiverAccountCode(receiverAccountCode);//收款人账号
            acceptDraftList.setReceiverBankFullName(receiverBankFullName);//收款人开户行全称
            acceptDraftList.setAdjustDateCount(adjustDateCount);//调整天数
            acceptDraftList.setReadTimes(readTimes);//背书次数
            acceptDraftList.setAcceptanceAgreementNumber(acceptanceAgreementNumber);//合同号
            acceptDraftList.setIsDifferentBank(isDifferentBank);//是否我行代开他行签发票据

            // 如果 code 存在  就覆盖，不存在则插入
            if(acceptDraftListDao.countById(code) > 0) {
                updateList.add(acceptDraftList);
            } else {
               acceptDraftList.setCreated(new Date());
                acceptDraftList.setCreatedBy(user.getLoginId());
               acceptDraftList.setLastUpdated(new Date());
                acceptDraftList.setLastUpdatedBy(user.getLoginId());
                insertList.add(acceptDraftList);
            }
        }
        int ins=0;
        int upd=0;
        //  如果没有update  就只执行 插入
        if(updateList.size()==0){
            ins=   acceptDraftListDao.batchInsert(insertList);
        }
        //  如果没有insert 就只执行 修改
        if(insertList.size()==0){
            upd= acceptDraftListDao.batchUpdateAcceptDrafList(updateList);
        }
        if(updateList.size()>0&&insertList.size()>0) {
            ins=  acceptDraftListDao.batchInsert(insertList);
            upd=  acceptDraftListDao.batchUpdateAcceptDrafList(updateList);
        }
        if (ins+upd>0){
            return true;
        }else {
            return false;
        }
    }

    public Integer findById( String code){
       return acceptDraftListDao.countById(code);
    }


}
