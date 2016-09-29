package com.prcsteel.platform.order.service.rebate;

import java.util.List;

import com.prcsteel.platform.order.model.dto.RebateDto;
import com.prcsteel.platform.acl.model.model.Rebate;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by chenchen on 2015/8/3.
 */
public interface RebateService {
	
	public int addRebate(Rebate rebate);
	/**
	 * 将所有未生效的返利改为失效
	 * 
	 * @return
	 */
	public int cancleEffectRebate();

	/**
	 * 获取所有正在生效的返利方案
	 * 
	 * @return
	 */
	public List<Rebate> getAllRebate();
	
	 /**
	  * 获取所有正在生效的提成方案
	  * @return
	  */
	 public List<RebateDto> getAllRebateDto();
	/**
	 * 重置新的返利方案
	 * @param rebateList
	 * @return
	 */
	public  int refleshRebate(List<Rebate> rebateList);
	/**
	 * 启用新的返利方案
	 * @return
	 */
	public void startNewRebate();
	
	/**
     * 统计已经到期的还未生效的提成条数
     * @return
     */
     public int countNextMonthEffectRecord();
     
     public void addRebateByOrder(Long orderId,User user,Long sellerId);
}
