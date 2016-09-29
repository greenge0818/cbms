package com.prcsteel.platform.order.model.enums;
/** 
 * 资源状态类型
 * @author peanut <p>2016年2月24日 下午1:50:30</p>  
 */
public enum ResourceStatusType {
	/**全部 **/
	ALL("全部",""),
	/**挂牌 **/
	LISTED("挂牌","1"),
	/**未挂牌**/
	UNLISTED("未挂牌","0"),
	/**异常资源***/
	EXCEPTION("异常资源","-1"),         
	/**历史成交***/
	HISTORYTRANSACTION("历史成交","2"),
	/**正常资源 :挂牌、未挂牌、异常的资源***/
	NORMAL("正常资源","-101");
	
	/****资源名称****/
	private String name;
	/****资源状态****/
	private String status;
	
	ResourceStatusType(String name,String status){
		this.name=name;
		this.status=status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String name,String status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
