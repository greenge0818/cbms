package com.prcsteel.platform.order.service.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceApplicationDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceApplicationDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailVoDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.query.InvoiceOutApplyQuery;

/**
 * Created by rolyer on 15-8-3.
 */
public interface InvoiceOutApplyService {

	/**
	 * 分页查询申请开票数据(主表)
	 * 
	 * @param orgIds 服务中心Id集合
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param start 开始位置
	 * @param length 数据量
	 * @param statusList 状态集合
	 * @return List<InvoiceOutApplyDto>
	 * @author DQ
	 */
	public List<InvoiceOutApplyDto> queryInvoiceOutApply(List<Long> orgIds, String startTime, String endTime, Integer start, Integer length, List<String> statusList);
	
	/**
	 * 查询申请开票数据总量
	 * 
	 * @param orgIds 服务中心Id集合
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param statusList 状态集合
	 * @return int
	 * @author DQ
	 */
	public int totalInvoiceOutApply(List<Long> orgIds, String startTime, String endTime, List<String> statusList);
	
	/**
	 * 查询申请开票详情所有数据(附带关联关系)
	 * 
	 * @param outApplyId 申请开票ID
	 * @param status 状态
	 * @return List<InvoiceOutApplyDetailVoDto>
	 * @author DQ
	 */
	public List<InvoiceOutApplyDetailVoDto> queryDetail(Long orgId, Long outApplyId);
	
	/**
	 * (事物)
	 * 保存申请开票详情所有数据(附带关联关系)
	 * 
	 * @param user 用户对象
	 * @param invoiceOutApplyDto 申请开票详情所有数据(附带关联关系)
	 * @return void
	 * @throws RuntimeException
	 * @author DQ
	 */
	public void saveNewInvoicing(User user, InvoiceOutApplyDto invoiceOutApplyDto) throws RuntimeException;
	
	/**
	 * (事物)
	 * 逻辑删除申请开票详情所有数据
	 * 
	 * @param id 买家ID
	 * @param user 用户对象
	 * @return void
	 * @author DQ
	 */
	public void delete(Long id,User user) throws RuntimeException;
	
	/**
	 * 打回申请开票
	 * 
	 * @param id 买家ID
	 * @param user 用户对象
	 * @param status 状态
	 * @return void
	 * @author DQ
	 */
	public void revoke(Long id, String status,User user);

	/**
	 * 审核申请开票
	 * 
	 * @param id 买家ID
	 * @param user 用户对象
	 * @param status 状态
	 * @return void
	 * @author DQ
	 */
	public void approve(Long id, String status,User user);
	
	/**
	 * 批量更新状态
	 * 
	 * @param ids 被新的id集合
	 * @param status 状态
	 * @return
	 * @author DQ
	 */
	public int batchUpdateStatusById(List<Long> ids, String status);

	/**
	 * 查询申请开票总额
	 * 
	 * @param invOutApplyQuery 查询条件
	 * @return 申请总额
	 * @author LCW
	 */
	BigDecimal queryTotalApplyAmount(InvoiceOutApplyQuery invOutApplyQuery);
	
	/**
	 * 进项票打回
	 * 
	 * @param invoiceId 进项票ID
	 * @param user 用户对象
	 * @return void
	 * @author DQ
	 */
	void invoiceInNegative(Long invoiceId,User user);


}
