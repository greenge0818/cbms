package com.prcsteel.platform.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.prcsteel.platform.core.persist.dao.CategoryDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.core.model.dto.CategoryDto;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.dto.CategoryGroupRecordDto;
import com.prcsteel.platform.core.model.model.CategoryGroup;
import com.prcsteel.platform.core.model.model.GroupForCategory;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.GroupForCategoryDao;
import com.prcsteel.platform.core.service.CategoryGroupService;

/**
 * Created by lcw on 2015/8/3.
 */
@Service("categoryGroupService")
public class CategoryGroupServiceImpl implements CategoryGroupService {
	private static final Logger logger = Logger.getLogger(CategoryGroupServiceImpl.class);
    @Autowired
    private CategoryGroupDao categoryGroupDao;
    @Autowired
    private GroupForCategoryDao groupForCategoryDao;
	@Autowired
	private CategoryDao categoryDao;

    
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return categoryGroupDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CategoryGroup record){
        return categoryGroupDao.insert(record);
    }

    @Override
    public int insertSelective(CategoryGroup record){
        return categoryGroupDao.insertSelective(record);
    }

    @Override
    public CategoryGroup selectByPrimaryKey(Integer id){
        return categoryGroupDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CategoryGroup record){
        return categoryGroupDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CategoryGroup record){
        return categoryGroupDao.updateByPrimaryKey(record);
    }

    /**
     * 根据父级UUID查找子集
     *
     * @param parentUuid 父级UUID
     * @return
     */
    @Override
    public List<CategoryGroup> queryByParentUuid(String parentUuid){
        return categoryGroupDao.queryByParentUuid(parentUuid);
    }

	@Override
	public List<CategoryGroupDto> queryAllCategoryGroupInner() {
		List<CategoryGroupDto> groupDto=categoryGroupDao.queryAllCategoryGroupInner();
		List<CategoryGroupDto> groupDtoTemp=new ArrayList<CategoryGroupDto>();
		for (CategoryGroupDto categoryGroupDto : groupDto) {
			CategoryGroupDto group=categoryGroupDto;
			List<CategoryGroupRecordDto> recordList=groupForCategoryDao.queryRecordByCateGroupUUID(categoryGroupDto.getCategoryGroupUuid());
			List<CategoryDto> tempList=new ArrayList<CategoryDto>();
			for (CategoryGroupRecordDto categoryGroupRecordDto : recordList) {
				CategoryDto cateDto=new CategoryDto();
				cateDto.setName(categoryGroupRecordDto.getCategoryName());
				cateDto.setId(categoryGroupRecordDto.getCategoryId());
				tempList.add(cateDto);
			}
			group.setCategoryDtoList(tempList);
			groupDtoTemp.add(group);
		}
		
		return groupDtoTemp;
	}

	@Override
	@Transactional
	public boolean addCategoryGroup(String categoryGroupName, String[] cate_ids,String opt) {
		Integer flag = OperateStatus.SUCCESS.ordinal();
		CategoryGroup categoryGroup=new CategoryGroup();
		categoryGroup.setParentUuid("0");
		String uuid=UUID.randomUUID().toString();
		categoryGroup.setUuid(uuid);
		categoryGroup.setName(categoryGroupName);
		categoryGroup.setSiteUuid("inner_cbms");
		categoryGroup.setIsDeleted(false);
		categoryGroup.setCreatedBy(opt);
		categoryGroup.setLastUpdatedBy(opt);
		int group_id=categoryGroupDao.insertSelective(categoryGroup);
		if (group_id!=0) {
			CategoryGroup group=categoryGroupDao.selectByUUID(uuid);
			for (String cate_uuid : cate_ids) {
				GroupForCategory temp=new GroupForCategory();
				temp.setCategoryGroupUuid(group.getUuid());
				temp.setCategoryUuid(cate_uuid);
				temp.setIsDeleted(false);
				temp.setCreatedBy(opt);
				temp.setLastUpdatedBy(opt);
				groupForCategoryDao.insertSelective(temp);
			}
			
		}else {
			flag= OperateStatus.FAIL.ordinal();
			logger.error("添加失败");
		}
		return false;
	}

	@Override
	public CategoryGroup selectByUUID(String uuid) {
		return categoryGroupDao.selectByUUID(uuid);
	}

	@Override
	public boolean editCategoryGroup(String group_uuid, String categoryGroupName, String[] cate_ids, String opt) {
		CategoryGroup  group =this.categoryGroupDao.selectByUUID(group_uuid);
		group.setName(categoryGroupName);
		group.setCreatedBy(opt);
		group.setLastUpdatedBy(opt);
		categoryGroupDao.updateByPrimaryKey(group);//修改
		Map<String, Object> paramMap =new java.util.HashMap<String, Object>();
		paramMap.put("uuid", group_uuid);
		paramMap.put("opt", opt);
		this.groupForCategoryDao.deleteRecordByGroupUUID(paramMap);//现将老的基础删除
		//在重新添加记录
		for (String cate_uuid : cate_ids) {
			GroupForCategory temp=new GroupForCategory();
			temp.setCategoryGroupUuid(group.getUuid());
			temp.setCategoryUuid(cate_uuid);
			temp.setIsDeleted(false);
			temp.setCreatedBy(opt);
			temp.setLastUpdatedBy(opt);
			groupForCategoryDao.insertSelective(temp);
		}
		return false;
	}

	@Override
	public boolean deleteCategoryGroup(String group_uuid, String opt) {
		CategoryGroup  group =this.categoryGroupDao.selectByUUID(group_uuid);
		group.setIsDeleted(true);
		group.setCreatedBy(opt);
		group.setLastUpdatedBy(opt);
		categoryGroupDao.updateByPrimaryKey(group);//修改
		Map<String, Object> paramMap =new java.util.HashMap<String, Object>();
		paramMap.put("uuid", group_uuid);
		paramMap.put("opt", opt);
		this.groupForCategoryDao.deleteRecordByGroupUUID(paramMap);//现将老的基础删除
		return false;
	}

	@Override
	public List<CategoryGroup> selectNoSelectForRebate() {		
		return categoryGroupDao.selectNoSelectForRebate();
	}

	@Override
	public List<CategoryGroup> selectNoSelectForReward() {		
		return categoryGroupDao.selectNoSelectForReward();
	}

	@Override
	public List<CategoryGroupDto> queryAllParentCategoryGroupInner() {
		return categoryGroupDao.queryAllParentCategoryGroupInner();
	}
	//查找所有CBMS大类给积分系统
	@Override
	public List<com.prcsteel.platform.order.model.wechat.dto.CategoryGroup>  queryAllCategoryGroupToWechat(){
		List<com.prcsteel.platform.order.model.wechat.dto.CategoryGroup>  cateGroupList = categoryGroupDao.queryAllCategoryGroupToWechat();
		for(com.prcsteel.platform.order.model.wechat.dto.CategoryGroup group:cateGroupList){
           List<com.prcsteel.platform.order.model.wechat.dto.Category> categoryList = categoryDao.queryCategoryByGroupUuid(group.getUuid());
			group.setCategoryList(categoryList);
		}
		return cateGroupList;
	}
    
}
