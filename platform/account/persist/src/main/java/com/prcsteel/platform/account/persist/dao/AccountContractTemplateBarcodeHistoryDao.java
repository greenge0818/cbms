package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.model.AccountContractTemplateBarcodeHistory;

public interface AccountContractTemplateBarcodeHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(AccountContractTemplateBarcodeHistory record);

    int insertSelective(AccountContractTemplateBarcodeHistory record);

    AccountContractTemplateBarcodeHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountContractTemplateBarcodeHistory record);

    int updateByPrimaryKey(AccountContractTemplateBarcodeHistory record);

    /**
     * 查询某个类型模板最近的一条条码历史记录
     * @param type
     * @return
     */
    AccountContractTemplateBarcodeHistory queryLastRecordByType(String type);

    /**
     * 按条码查询记录（条码唯一）
     * @param barcode
     * @return
     */
	AccountContractTemplateBarcodeHistory queryRecordByBarcode(String barcode);
}