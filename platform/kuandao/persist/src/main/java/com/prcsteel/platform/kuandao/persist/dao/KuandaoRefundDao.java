package com.prcsteel.platform.kuandao.persist.dao;
import java.util.List;
import java.util.Map;
import com.prcsteel.platform.kuandao.model.dto.KuandaoRefundDto;
import com.prcsteel.platform.kuandao.model.model.KuandaoRefund;
public interface KuandaoRefundDao {
	/**
	 * 退款记录到本地
	 * @param 
	 * @return 
	 * */
	public int insertRefund(KuandaoRefund kuandaoRefund);
	/**
	 * 根据条件查退款信息明细-多表
	 * @param
	 * @return
	 * */
	public List<KuandaoRefundDto> queryRefund(Map<String,Object> param);
	/**
	 * 根据条件查，退款信息总数查询-多表
	 * @param
	 * @return
	 * */
	public int queryTotalRefund(KuandaoRefundDto dto);
	/**
	 * 根据id查询退款信息
	 * @param
	 * @return
	 * */
	public KuandaoRefund queryRefundById(int id);
	
	/**
	 * 根据条件更新退款
	 * @param
	 * @return
	 * */
	public int updateRefundByCondition(KuandaoRefund kuandaoRefund);
	
	/**
	 * 退款记录查询
	 * @param kuandaoRefundDto
	 * @return
	 */
	public List<KuandaoRefund> queryRefundByCondition(KuandaoRefundDto kuandaoRefundDto);
}
