package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.smartmatch.model.query.*;
import com.prcsteel.platform.smartmatch.model.dto.*;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.CategoryInResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.HistoryResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.LostResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBatchDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceHistoryPriceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisTotal;
import com.prcsteel.platform.smartmatch.model.model.Resource;
import com.prcsteel.platform.smartmatch.model.query.DailyResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.LostResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.OneKeyOprtResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceBusinessQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceHistoryQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceStatisQuery;

public interface ResourceDao {
    int deleteByPrimaryKey(Long id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);
    //根据仓库城市查询卖家列表

    List<Account> getAvailableAccountListByCityId(@Param("cityIds")String cityIds,@Param("purchaseOrderId")Long purchaseOrderId);
    List<ResourceDto> selectResourceList (ResourceQuery resourceQuery);
	int countResource(ResourceQuery resourceQuery);
	List<String> selectAllWeightConcept();

	List<ResourceDto> selectByQueryForUpdate(ResourceQuery resourceQuery);

	int updateByDto(ResourceDto record);

    /**
	 * 校验单元格数据是否和数据库匹配,正确则返回期望字段数据
	 * @param value          值
	 * @param tableName      数据表
	 * @param tableCoumn     数据表对应列
     * @param expect         期望字段
	 * @return
	 */
	String getColumnValue(@Param("expect") String expect,@Param("tableName") String tableName,@Param("tableCoumn") String tableCoumn,@Param("value")String value);   // add by peanut on 2015-11-28

	/**
	 * 按别名查询数据
	 * @param value
	 * @param tableName
	 * @param tableCoumn
	 * @param value2
	 * @return
	 */
	List<Map<String, Object>> getDataByAlias(@Param("aliasName") String aliasName);
    /**
     * 获取资源中所有大类和品名
     * @param query
     *         当dt==null时，默认查询当天数据
     * @return
     */
    List<CategoryInResourceDto> queryCategoryInResource(DailyResourceQuery query);

    /**
	 * 改变全部资源的状态 : 挂牌-->未挂牌;未挂牌-->挂牌
	 * @param oriStatus   原始状态值
	 * @param toStatus    目的状态值
	 * @param ids      资源id集
	 * @param userIds  权限用户id集
	 * @return
	 */
	void changeStatus(@Param("oriStatus") String oriStatus,@Param("toStatus") String toStatus,@Param("ids")List<Long> ids,@Param("userIds") List<Long> userIds); // add by peanut on 2015-12-4

	/**
	 * 资源列表批量更新: 批量改价格和批量改库存
	 * @param rbd
	 * @return
	 */
	void batchUpdate(ResourceBatchDto rbd);  // add by peanut on 2015-12-7

	/**
	 * 根据资源id集删除规格记录
	 * @param ids
	 */
	void batchDelResourceByIds(@Param("ids")List<Long> ids); // add by peanut on 2015-12-9

	/**
	 * 根据状态查询资源记录总数
	 */
	Map<String,Integer> selecCountResourceByStatus(@Param("userIds") List<Long> userIds); // add by peanut on 2015-12-9

	/**
	 * 根据钢厂id查询资源记录
	 */
	List<Resource> selectByFactoryId(Long factoryId);

	/**
	 * 根据仓库id查询资源记录
	 */
	List<Resource> selectByWarehouseId(Long warehouseId);
	
	/**
	 * 刷新异常
	 * @param          资源状态
	 */
	void refreshException( );  
	
	/**
	 * 根据名称获取钢厂或仓库id(名称在名称+别名的组合里即可)
	 * 
	 * @param tableName      数据表
     * @param value         校验值
	 * @return    对应记录的id
	 */
	Long getIdForWarehouseOrFactoryByNameOrAlias(@Param("tableName") String tableName,@Param("value")String value);   // add by peanut on 2016-1-4
	
	/**
	 * 一键挂、撤牌操作 
	 */
	void oneKeyOprt(OneKeyOprtResourceQuery okrq); // add by peanut on 2016-1-7
	
	/**
	 * 复制资源表中的数据至历史表中
	 */
	void doCopyResource();
	
	/**
	 * 根据日期和区域ID获取历史资源数据
	 * @param dt          日期
	 * @param areaId      区域ID（区域ID为空时，查询全国的数据）
	 * @param rType       资源类型
	 * @return
	 */
	List<HistoryResourceDto> queryHistoryDataByDateAndAreaId(@Param("dt")String dt,@Param("areaId")String areaId,@Param("rType")String rType);
	
	/**
	 * 查询历史成交(订单状态大于等4)的资源明细
	 * @return
	 */
	List<ResourceDto> queryHistoryTransactionResourceItem();

	/**
	 * 查询时间段内采购单物资明细不在当前挂牌资源中的缺失资源数据
	 * @param lostResourceQuery
	 * @return
	 */
	List<LostResourceDto> searchForLostResource( LostResourceQuery lostResourceQuery);
	
	/**
	 * 资源更新统计总数
	 * @param statisQuery
	 * @return
	 */
	ResourceStatisTotal queryStatisResTotal(ResourceStatisQuery statisQuery);
	     
	/**
	 * 资源更新统计
	 * @param statisQuery
	 * @return
	 */
	List<ResourceStatisDto> queryStatisRes(ResourceStatisQuery statisQuery);
	
	
	/**
	 * 批量查询资源
	 * @param userIds
	 * @return
	 */
	List<Resource> betchQueryResourceByIds(@Param("ids") List<Long> userIds);

	/**
	 * 获取资源的规格
	 * @param resourceId
	 * @return
	 */
	String getResourceSpecByResourceId(Long resourceId);
	/**
	 * 获取历史表中，昨天5点的数据的价格，用于计算涨跌值
	 * @param historyQuery
	 * @return
	 */
	List<ResourceHistoryPriceDto> queryHistoryPrice(ResourceHistoryQuery historyQuery);

	/**
	 * 获取城市名称
	 * @param warehouseId
	 * @return
	 */
	String getCityNameByWarehouseId(@Param("warehouseId") Long warehouseId);
	
	/**
	 * 查询异常资源列表
	 * @return
	 */
	List<Resource> selectExceptionResources();
	/**
	 * 批量更新异常资源-》正常资源
	 * @param resourceList
	 */
	void batchUpdateExceptionRes(@Param("list") List<Resource> resourceList);
	
	/**
	 * 根据条件查询资源搜索信息
	 * @param query
	 * @return
	 */
	List<ResourceBaseDto> getResourceListByConditions(ResourceBusinessQuery query);

	/**
	 * 根据ID查询资源列表（包含品名、规格）
	 * @param ids
	 * @return
     */
	List<Resource> queryResourceDetailByIds(List<Long> ids);

	/**
	 * 单条搜索条件业务找货分页统计
	 * @param query
	 * @return
	 */
	Integer getSingleBusinessResourceListTotal(ResourceBusinessQuery query);

	/**
	 * 单条搜索条件业务找货分页列表
	 * @param query
	 * @return
	 */
	List<ResourceBusinessDto> getSingleBusinessResourceList(ResourceBusinessQuery query);


	/**
	 * @description：查询资源rest接口 add by zhoucai 2016-6-24
	 * @param query
	 * @return
	 */
	List<ResourceDailyDtoResult> searchResourceList(ResourceQuery query);


	/**
	 * 超市Rest接口查询审核和历史的资源
	 *
	 * @param restNormalResourceQuery 查询对象
	 * @return
	 * @author peanut
	 * @date 2016/6/23
	 * @see RestNormalResourceQuery
	 */
	List<ResourceDto> selectRestResource(RestNormalResourceQuery restNormalResourceQuery);
	
	/**
	 * 根据资源id查询
	 * @param resourceId 资源id
	 * @return
	 */
	ResourceBaseDto selectByResourceId(Long resourceId);
	
	/**
	 * 多条搜索条件业务找货分页统计
	 * @param query
	 * @return
	 */
	Integer getMultiBusinessResourceListTotal(ResourceBusinessQuery query);

	/**
	 * 多条搜索条件业务找货分页列表
	 * @param query
	 * @return
	 */
	List<ResourceBusinessDto> getMultiBusinessResourceList(ResourceBusinessQuery query);
	
	/**
	 * 多条搜索条件业务找货分页列表子项
	 * @param query
	 * @return
	 */
	List<ResourceBusinessDto> getMultiBusinessResourceChildList(ResourceBusinessQuery query);

	/**
	 * 根据仓库ID查询仓库对应的默认的城市
	 * @param warehouseId
	 * @return
	 */
	public City getCityByWarehouseId(Long warehouseId);
}
