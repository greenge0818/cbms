package com.prcsteel.platform.order.service.allowance;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.order.model.dto.AllowanceDto;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.query.AllowanceListQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderSave;
import com.prcsteel.platform.order.model.query.AllowanceUpdate;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
public interface AllowanceService {

	Allowance selectByPrimaryKey(Long id);
	
	List<AllowanceOrderItemsDto> queryOrders(AllowanceOrderQuery allowanceOrderQuery);

	public void save(MultipartFile image, AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList);
	
	/**
	 * 修改折让状态
	 * @param rebateAuditQuery
	 * @param uer
	 * @author lixiang
	 */
    void checkAllowance (AllowanceUpdate allowanceUpdate);


	/**
	 * 修改折让单
	 *
	 * @param allowanceOrderSave 折让单修改数据
	 * @param orderList          折让单订单详情数据
	 */
	void updateAllowance(AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList);

	List<AllowanceDto> queryAllowanceList(AllowanceListQuery query);

	int countAllowanceList(AllowanceListQuery query);

	void closeOrder(User user,Allowance allowance);

	void deleteAllowance(Long id,User user);

	/**
	 * 修改卖家折让单，如果是重量方式则同步修改买家折让单
	 * @param image
	 * @param allowanceOrderSave
	 * @param orderList
	 */
	void updateSellerAllowance(MultipartFile image, AllowanceOrderSave allowanceOrderSave,List<AllowanceOrderItemsDto> orderList);

	void saveBuyerAllowances(AllowanceOrderSave allowanceOrderSave);
	
	/**
	 * 自动关闭折让单
	 * @return
	 */
	void automaticClose();
	
		/**
	 * 在以重量与金额方式折让时， 可以修改折让金额，
	 * @param allowanceItemId
	 * @param amount
	 * tuxianming
	 */
	void updateAllowance(List<AllowanceDetailItemQuery> query, User login);

	/**
     * 通过客户id查询其名下部门
     * @param accountId
     * @author lixiang
     * @return
     */
    List<CustAccountDto> queryDepartmentByAccoutId(Long accountId);

}
