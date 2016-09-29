package com.prcsteel.platform.account.persist.dao;


import org.apache.ibatis.annotations.Param;



public interface ErrAccountEditDao {

     Integer updateAccountName(@Param("errName")String errName, @Param("correctName")String correctName);

}