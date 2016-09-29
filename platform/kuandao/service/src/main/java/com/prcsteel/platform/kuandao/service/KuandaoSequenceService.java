package com.prcsteel.platform.kuandao.service;

public interface KuandaoSequenceService {

	 /**
     * 生成款道会员编号
     * 
     * 前缀为固定值GW-HZ-
     * 自增长长度8位
     * @param billSequence
     * @return
     */
    public String generateKuandaoAcctCode();
    
    /**
     * 生成款道支付单号
     * 
     * 分系统自动生成和人工取号
     * 人工取号前缀为1，自增长长度为5
     * @param billSequence
     * @return
     */
    public String generateKuandaoPayOrderCode(int occurType);

    /**
     * 生成退款单号
     * 
     * @return
     */
	public String generateKuandaoRefundCode();
    
    
}
