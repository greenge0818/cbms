/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

jQuery(function ($) {
    initTable();
    initClickEvent();
});

/**
 * 交易单连接生成
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {String}
 */
function renderConsignOrderCode(data, type, full, meta){
	var text = '';
	if(data && data != 'null' && full.nsortName != "补交二结款"){
		text = '<a href="' + domain.order + '/order/query/detail.html?orderid=' + full.consignOrderId + '&menu=index" target="_blank">' + data + '</a>';
	}
	
    return text;
}

/**
 * 商品数量生成
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns
 */
function renderWeight(data, type, full, meta){
	if(full.nsortName == "补交二结款"){
		return '';
	}else{
		return data;
	}
}

/**
 * 提交状态生成
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {String}
 */
function renderSubmitStatus(data, type, full, meta){
	var text = '';
	 //支付单状态
	if(data == 0){
		text = '<span>待提交</span>';
	}else if(data == 1){
		if(full.chargStatus == 1){
			text = '<span>已充值</span>';
		}else{
			text = '<span>支付完成</span>';
		}
	}else if(data == 2){
		text = '<span>提交失败</span>';
	}else if(data == 3){
		text = '<span>待匹配</span>';
	}else if(data == 4){
		text = '<span>已匹配</span>';
	}else if(data == 5){
		text = '<span>已退款</span>';
	}
	return text;
}

function initClickEvent(){
	//搜索事件
    $("#queryBtn").click(search);
}



function search() {
    tradeTable.ajax.reload();
}
