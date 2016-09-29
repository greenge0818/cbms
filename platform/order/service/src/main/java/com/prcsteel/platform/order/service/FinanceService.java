package com.prcsteel.platform.order.service;

import com.prcsteel.donet.iv.finance.model.response.CbmsAmountList;
import com.prcsteel.donet.iv.finance.model.response.DealDetailList;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author lcw
 * @version V1.1
 * @Title: FinanceService.java
 * @Package com.prcsteel.platform.order.service
 * @Description: IV用户资金服务
 * @date 2015年8月20日 下午16:53:27
 */
@WebService(serviceName = "FinanceService", targetNamespace = "http://tempuri.org/")
public interface FinanceService {

    /**
     * IV站 添加返利接口
     *
     * @param key          验证key
     * @param money        返利金额
     * @param phone        手机号码
     * @param userName     用户名
     * @param companyName  公司名
     * @param provinceName 省份名
     * @return 成功返回：充值成功，失败返回：失败消息
     */
    @WebMethod(operationName = "Rebate", action = "http://tempuri.org/Rebate")
    @WebResult(name = "RebateResult", targetNamespace = "http://tempuri.org/")
    String addRebate(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                     @WebParam(name = "money", targetNamespace = "http://tempuri.org/") Double money,
                     @WebParam(name = "phone", targetNamespace = "http://tempuri.org/") String phone,
                     @WebParam(name = "userName", targetNamespace = "http://tempuri.org/") String userName,
                     @WebParam(name = "companyName", targetNamespace = "http://tempuri.org/") String companyName,
                     @WebParam(name = "provinceName", targetNamespace = "http://tempuri.org/") String provinceName);

    /**
     * IV站 查找多条手机号码对应的用户提现总金额
     *
     * @param key       验证key
     * @param phoneList 手机号码
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 成功返回：数据集合，失败返回：null
     */
    @WebMethod(operationName = "GetAmountByPhone", action = "http://tempuri.org/GetAmountByPhone")
    @WebResult(name = "GetAmountByPhoneResult", targetNamespace = "http://tempuri.org/")
    CbmsAmountList getAmountByPhone(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                                    @WebParam(name = "phoneList", targetNamespace = "http://tempuri.org/") String phoneList,
                                    @WebParam(name = "startTime", targetNamespace = "http://tempuri.org/") String startTime,
                                    @WebParam(name = "endTime", targetNamespace = "http://tempuri.org/") String endTime);

    /**
     * IV站 查找多条手机号码对应的用户提现详情记录
     *
     * @param key       验证key
     * @param id 上一次同步的最大ID
     * @param num 获取的记录条数
     * @return 成功返回：数据集合，失败返回：null
     */
    @WebMethod(operationName = "GetAcountList", action = "http://tempuri.org/GetAcountList")
    @WebResult(name = "GetAcountListResult", targetNamespace = "http://tempuri.org/")
    DealDetailList getAcountList(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                                    @WebParam(name = "maxId", targetNamespace = "http://tempuri.org/") int id,
                                    @WebParam(name = "num", targetNamespace = "http://tempuri.org/") int num);

    /**
     * IV站 根据手机号码查询当前cbms余额
     *
     * @param key          验证key
     * @param phone        手机号码
     * @return 返利余额
     */
    @WebMethod(operationName = "RebateAmountByPhone", action = "http://tempuri.org/RebateAmountByPhone")
    @WebResult(name = "RebateAmountByPhoneResult", targetNamespace = "http://tempuri.org/")
    Double rebateAmountByPhone(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                     @WebParam(name = "phone", targetNamespace = "http://tempuri.org/") String phone);

    /**
     * IV站 根据手机号码修正返利金额（旧号码的返利金额转移到新手机号码）
     *
     * @param key          验证key
     * @param oldPhone     旧手机号码
     * @param newPhone     新手机号码
     * @param money        修正金额
     * @return 0：验证失败，1：成功，-1：服务器错误，-2：手机号码不存在，-3：修改失败，-4：余额不足，-5：参数错误
     */
    @WebMethod(operationName = "CorrectRebateByPhone", action = "http://tempuri.org/CorrectRebateByPhone")
    @WebResult(name = "CorrectRebateByPhoneResult", targetNamespace = "http://tempuri.org/")
    Integer correctRebateByPhone(@WebParam(name = "key", targetNamespace = "http://tempuri.org/") String key,
                     @WebParam(name = "oldPhone", targetNamespace = "http://tempuri.org/") String oldPhone,
                     @WebParam(name = "newPhone", targetNamespace = "http://tempuri.org/") String newPhone,
                     @WebParam(name = "money", targetNamespace = "http://tempuri.org/") Double money);
}