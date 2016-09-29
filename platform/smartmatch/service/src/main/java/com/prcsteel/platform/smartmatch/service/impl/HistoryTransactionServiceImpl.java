package com.prcsteel.platform.smartmatch.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.model.CategoryNorms;
import com.prcsteel.platform.core.persist.dao.CategoryNormsDao;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.model.ResourceNorms;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceNormsDao;
import com.prcsteel.platform.smartmatch.service.HistoryTransactionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.enums.ResourceStatusType;

/** 
 * 订单历史成交service
 * @author peanut <p>2016年2月24日 上午11:53:49</p>  
 */
@Service
public class HistoryTransactionServiceImpl implements HistoryTransactionService {
	
	private static final String CBADMIN_JOB = "cbadmin job";
	@Resource
	private ResourceDao resourceDao;
	@Resource
	private ResourceNormsDao resourceNormsDao;
	@Resource
	private CategoryNormsDao categoryNormsDao;
	
	/**
	 * 执行历史成交任务
	 * 
	 * <p>1、从系统订单表中查取订单完成(已关联)的订单资源明细数据</p>
	 * 
	 * <p>2、查看资源明细是否已在资源库中存在，存在则对仓库、价格、正常更新时间做更新，否则将资源明细插入至资源表(包括资源规格表)，其资源状态为历史成交资源</p>
	 * 
	 */
	@Override
	public void doHistoryTransactionJob() {
	//modify by caosulin 20160912 注释历史成交资源的定时任务，现历史成交资源通过rest接口实时存资源表了，不走批量了。	
	/*
		//历史成交资源明细
    List<ResourceDto> list=queryHistoryTransactionResourceItem();
        
        if(list !=null && !list.isEmpty()){
        	//明细列表各个资源设置状态至历史成交
        	list.stream().forEach(e->e.setStatus(ResourceStatusType.HISTORYTRANSACTION.getStatus()));
        	
        	for(ResourceDto resourceDto:list){
        		
        		String spec=getMinSpec(resourceDto);
        		ResourceQuery rq=new ResourceQuery(
        				resourceDto.getAccountId(),
        				resourceDto.getCategoryUuid(),
        				resourceDto.getMaterialUuid(),
        				resourceDto.getMaterialName(),
        				resourceDto.getFactoryId(),
        				resourceDto.getFactoryName(),
        				null,
        				null,
        				resourceDto.getStatus(),
        				spec
        			);
//        		if(resourceDto.getAccountId().equals(908L) && "4.75*1500".equals(spec) ){
//					System.out.println("-------------------");
//				}
        			List<ResourceDto> rList=resourceDao.selectByQueryForUpdate(rq);
        			
        			//如果存在则做更新
        			if(rList!=null && !rList.isEmpty()){
        				if(rList.size()==1){
        					com.prcsteel.platform.smartmatch.model.model.Resource resource=resourceDao.selectByPrimaryKey(rList.get(0).getId());
        					resource.setWarehouseId(resourceDto.getWarehouseId()==null?-1:resourceDto.getWarehouseId());
        					resource.setWarehouseName(resourceDto.getWarehouseName());
        					resource.setPrice(resourceDto.getPrice());

        					resource.setMgtLastUpdated(new Date());
        					resource.setMgtLastUpdatedBy(CBADMIN_JOB);
        					//更新资源表
        					resourceDao.updateByPrimaryKeySelective(resource);
        				}else{
        					*//*******根据资源参数查询出的结果不为1,是因为根据规格参数查询出的资源记录为多条*****//*
        					throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "查询参数不全,请核实后再做保存!");
        				}
        			}else{
        				com.prcsteel.platform.smartmatch.model.model.Resource resource= new com.prcsteel.platform.smartmatch.model.model.Resource(
        						resourceDto.getAccountId(),
								resourceDto.getAccountName(),
        						resourceDto.getCategoryUuid(),
								resourceDto.getCategoryName(),
        						resourceDto.getMaterialUuid(),
        						resourceDto.getFactoryId(),
								resourceDto.getWarehouseId()==null?-1:resourceDto.getWarehouseId(),
        						resourceDto.getWeightConcept(),
        						resourceDto.getPrice()==null?null:resourceDto.getPrice().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,BigDecimal.ROUND_HALF_UP),
        						CBADMIN_JOB, // job -->管理员
        						null,
        						resourceDto.getQuantity()==null?null:resourceDto.getQuantity(),
        						resourceDto.getWeight()==null?null:resourceDto.getWeight().setScale(Constant.EXCEL_TEMPLET_BIGDECIMAL_SCALE,BigDecimal.ROUND_HALF_UP),
        						resourceDto.getRemark(),
        						resourceDto.getSourceType(),
        						resourceDto.getStatus(),
        						"1", // job -->管理员
        						resourceDto.getFactoryName(),
        						resourceDto.getWarehouseName(),
        						CBADMIN_JOB, // job -->管理员
        						new Date());
        				resource.setMaterialName(resourceDto.getMaterialName());
						resource.setMgtLastUpdated(new Date());
						resource.setMgtLastUpdatedBy(CBADMIN_JOB);
        				//插入资源表
        				resourceDao.insertSelective(resource);
        				resourceDto.setId(resource.getId());
        				//对资源规格表做插入操作
        				doInsertNorms(resourceDto);
        			}
        	}
        }
	*/}
	
	/**
	 * 计算传入资源与系统定义的品名规格长度之间较短的规格
	 * <p>如：传入资源规格:1*2*3 , 系统定义: 11*222 则取 1*2 的规格 ; </p>
	 * <p>或</p>
	 * <p>传入资源规格:11*223 , 系统定义: 13*24*35  则取 11*223 的规格 ; </p>
	 * @param resDto 
	 * @return
	 */
	private String getMinSpec(ResourceDto resDto) {
		if(StringUtils.isEmpty(resDto.getSpec())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格参数不存在!");
		}
		if(StringUtils.isEmpty(resDto.getCategoryUuid())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid参数不存在!");
		}
//		//查询出规格
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(resDto.getCategoryUuid().toString());
    	//规格uuid
		List<String> uuidList=list.stream().map(a->a.getNormsUuid()).collect(Collectors.toList());
//		规格名称
		String[] normsArrays=resDto.getSpec().split("\\"+Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
		
		//取最小值
		int len=Math.min(uuidList.size(), normsArrays.length);
		
		return StringUtils.join(Arrays.copyOf(normsArrays, len), Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
	}

	/**
	 * 查询订单历史成交的资源明细 
	 */
	@Override
	public List<ResourceDto> queryHistoryTransactionResourceItem() {
		return resourceDao.queryHistoryTransactionResourceItem();
	}
	
	/**
	 * 对资源规格表做插入操作
	 * @param resDto
	 */
	private void doInsertNorms(ResourceDto resDto){
		//插入资源规格表
		if(StringUtils.isEmpty(resDto.getSpec())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格参数不存在!");
		}
		if(StringUtils.isEmpty(resDto.getCategoryUuid())){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名uuid参数不存在!");
		}
//		//查询出规格
		List<CategoryNorms> list = categoryNormsDao.getNormCombineByCategoryUuid(resDto.getCategoryUuid().toString());
    	//规格uuid
		List<String> uuidList=list.stream().map(a->a.getNormsUuid()).collect(Collectors.toList());
//		规格名称
		String[] normsArrays=resDto.getSpec().split("\\"+Constant.EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR);
//		if(uuidList.size()!=normsArrays.length){
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "规格uuid和参数个数不对应!");
//		} 
		if(uuidList !=null && uuidList.size()>0){
			for(int k=0;k<Math.min(uuidList.size(), normsArrays.length);k++){
				ResourceNorms rn=new ResourceNorms(
					resDto.getId(),
					uuidList.get(k),
					normsArrays[k],
					(k+1),
					new Date(),
					CBADMIN_JOB
				);
				resourceNormsDao.insertSelective(rn);
			}
		}
	}
}
