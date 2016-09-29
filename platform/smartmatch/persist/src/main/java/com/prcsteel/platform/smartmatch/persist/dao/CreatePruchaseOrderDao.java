package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.core.model.dto.BaseDataDto;
import com.prcsteel.platform.smartmatch.model.dto.AttributeDataDto;

public interface CreatePruchaseOrderDao {
    List<BaseDataDto> selectNormsByCategoryUUID(String categoryUUID);

    List<AttributeDataDto> selectAttributeByCategoryUUID(String categoryUUID);

    List<BaseDataDto> selectMaterialByCategoryUUID(String categoryUUID);

    List<BaseDataDto> selectFactoryByCategoryUUID(String categoryUUID);

    List<String> selectNormsByCategoryUUIDAndMaterialNorms(@Param("categoryUUID") String categoryUUID,
                                                           @Param("materialUUID") String materialUUID, @Param("normUUID") String normUUID);
}