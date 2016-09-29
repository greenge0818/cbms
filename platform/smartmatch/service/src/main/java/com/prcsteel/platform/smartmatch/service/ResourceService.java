package com.prcsteel.platform.smartmatch.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.smartmatch.model.enums.ResourceOperType;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.model.Norms;
import com.prcsteel.platform.smartmatch.model.dto.ExcelTempletDto;
import com.prcsteel.platform.smartmatch.model.dto.HistoryResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.LostResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceBatchDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDailyDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceStatisTotal;
import com.prcsteel.platform.smartmatch.model.model.Resource;
import com.prcsteel.platform.smartmatch.model.query.LostResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.OneKeyOprtResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceStatisQuery;
import com.prcsteel.platform.smartmatch.model.query.RestHotResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestNormalResourceQuery;

/**
 * Created by prcsteel on 2015/11/24.
 */
public interface ResourceService {
	List<String> selectAllWeightConcept();
    List<ResourceDto> selectResourceList(ResourceQuery resourceQuery);
	int countResource(ResourceQuery resourceQuery);
    /**
     *
        * @Title: selectByQueryForUpdate
        * @Description: 判断某一条资源是否存在
        * @param @param resourceQuery
        * @param @return    参数
        * @return List<ResourceDto>    返回类型
        * @throws
     */
    List<ResourceDto> selectByQueryForUpdate(ResourceQuery resourceQuery);
    /**
	 * 保存到资源产品表
	 * @param resDto
	 */
	public void saveBaseProductByResource(com.prcsteel.platform.smartmatch.model.model.Resource resource,String spec);
	/**
	 * 保存到资源产品表
	 * @param resDto
	 */
	public void saveBaseProductByResourceDto(ResourceDto resDto,String resourceStatus);
    int insertSelective(Resource record);
    /**
	 * 获取当前资源默认的城市
	 * @param resource
	 */
	public void getResourceCity(
			com.prcsteel.platform.smartmatch.model.model.Resource resource);
    /**
     * 模版上传
     * @param mFile   模版文件
     * @param user    用户信息
     */
    List<ExcelTempletDto> uploadTemplet(MultipartFile mFile, User user);        // add by peanut on 2015-11-27

    /**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * @return
	 */
    Map<String,Object> getCommonData();  // add by peanut on 2015-12-2

    /**
     * 保存资源对象
     * @param resource  资源对象
     */
	void doSaveResource(ResourceDto resource, User user);// add by peanut on 2015-12-3

	/**
	 * 改变全部资源的状态 : 挂牌-->未挂牌;未挂牌-->挂牌
	 * @param oriStatus   原始状态值
	 * @param toStatus    目的状态值
	 * @param ids      资源id集
	 * @return
	 */
	void changeStatus(String oriStatus, String toStatus, List<Long> ids, List<Long> userIds, User user); // add by peanut on 2015-12-4

	/**
	 * 资源列表批量更新: 批量改价格和批量改库存
	 * @param rbd
	 * @return
	 */
	void batchUpdate(ResourceBatchDto rbd, User user); // add by peanut on 2015-12-7

	/**
	 * 删除资源
	 * @param ids  资源id集
	 */
	void batchDelResourceByIds(List<Long> ids); // add by peanut on 2015-12-9

	/**
	 * 根据状态查询资源记录总数
	 * @param userIds  权限过滤用户
	 */
	Map<String,Integer> selecCountResourceByStatus(List<Long> userIds);

	/**
	 * 根据品名uuid获取规格
	 * @param categoryUuid   品名uuid
	 * @return
	 */
	List<Norms> getNormsByCategoryUuid(String categoryUuid);

	/**
	 * 导入资源数据
	 * @param etDtoList
	 * @param user
	 */
	void doImportResource(List<ExcelTempletDto> etDtoList, User user);
	
	/**
	 * 刷新异常
	 * status 参数作废，使用 is_exception，不需要作为参数传递
	 * @param 
	 */
	void refreshException(User user); 
	
	/**
	 * 一键挂、撤牌操作 
	 */
	void oneKeyOprt(OneKeyOprtResourceQuery okrq);
	
	/**
	 * 获取历史卖家统计数据
	 * @param dt      日期
	 * @param areaId  区域ID（区域ID为空时，查询全国的数据）
	 * @param rType   资源类型
	 * @return        卖家数据结果集
	 */
	List<HistoryResourceDto> getHistorySellerStatisticData(String dt, String areaId,String rType);
	
	/**
	 * 查询缺失资源
	 * @param lostResourceQuery
	 * @return
	 */
	List<LostResourceDto> searchForLostResource( LostResourceQuery lostResourceQuery);
	
	/**
	 * 资源导出
	 * @param resourceQuery  资源查询对象
	 * @return
	 */
	File exportResource(ResourceQuery resourceQuery);
	
	/**
	 * 资源更新统计汇总
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
	 * 新增历史资源，数据从CBMS交易单过来
	 * @param data
	 * @return 
	 */
	void saveHistoryResource(ResourceDto data,String userId);

	List<Resource> queryResourceDetailByIds(List<Long> ids);
	/**
	 *@decription:查询日常资源信息 add by zhoucai 2016-6-20
	 * @param resourceQuery
	  * @return Result
	 */
	List<ResourceDailyDto> queryDailyResource(ResourceQuery resourceQuery);

	/**
	 * Rest接口查询热门资源
	 *
	 * @param restHotResourceQuery 查询对象
	 * @return
	 * @author peanut
	 * @date 2016/6/22
	 * @see RestHotResourceQuery
	 */
	List<ResourceDto> selectRandomHostResource(RestHotResourceQuery restHotResourceQuery);

	/**
	 * Rest接口查询普通资源
	 *
	 * @param restNormalResourceQuery 查询对象
	 * @return
	 * @author peanut
	 * @date 2016/6/24
	 * @see RestNormalResourceQuery
	 */
	List<ResourceDto> selectNormalResource(RestNormalResourceQuery restNormalResourceQuery);
	
	/**
	 * 根据资源id查询
	 * @param resourceId
	 * @return
	 */
	ResourceBaseDto selectById(Long resourceId);

	void batchInsertResourceBase(List<Long> ids, ResourceOperType operType, User user);
}
