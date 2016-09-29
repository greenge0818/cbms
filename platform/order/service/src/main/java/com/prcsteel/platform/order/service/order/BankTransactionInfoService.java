package com.prcsteel.platform.order.service.order;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.BankTransactionInfoDto;
import com.prcsteel.platform.order.model.dto.LocalTransactionDataJobDto;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by dengxiyan on 2015/7/19.
 */
public interface BankTransactionInfoService {

    /**
     * 统计某种状态下的到账记录数
     * @param status
     * @param settingType 系统设置类型
     * @return
     */
    int countByStatus(String status, String settingType);

    List<BankTransactionInfoDto> query(Map<String, Object> param);

    BankTransactionInfo queryById(Long id);

    int updateStatusById(BankTransactionInfo bankTransactionInfo, String curUser);

    void charge(Long id, String name, String tel, Long departmentId, User user);

    List<LocalTransactionDataJobDto> queryUnprocessedForJob();
    
	/**
	 * 根据客户名获取最新到账银行信息
	 */
	public String selectByAccountName(String payeeName);

    /**
     * 根据id更新状态
     * @param id
     * @param status
     * @return
     */
    public int updateStatusById(Long id, String status, String loginId);

    /**
     * 疑似支付错误立即处充值
     * @param id
     * @return
     */
    public Boolean errorRecharge(Long id, User user);
}
