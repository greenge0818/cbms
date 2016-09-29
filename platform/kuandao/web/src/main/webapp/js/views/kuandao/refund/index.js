/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});
function initTable() {
    var url = Context.PATH + "/kuandao/refund/queryDepositAndOrder.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.impDate = $("#impDate").val();
                d.payMerName = $("#payMerName").val();
                d.payeeVirtualAcctNo = $("#payeeVirtualAcctNo").val();
                d.impStatus = $("#status").val();
                d.startDate = $("#startDate").val();
                d.endDate = $("#endDate").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'impDate'},
            {data: 'impAcqSsn'},
            {data: 'paymentOrderCode'},
            {data: 'payeeVirtualAcctName'}
            ,{data: 'payMerName'}
            , {data: 'payMerBranchId'}
            , {data: 'payMerAcctNo'}
            , {data: 'remitDetails'}
            , {data: 'transactionAmount'}
            , {data: 'impStatus'}
            , {data: 'operate'}
            
        ]
        , columnDefs: [
           			{
           			    "targets": 8, //第几列 从0开始
           			    "data": "transactionAmount",
           			    "render": function(data, type, full, meta){
           			    	var text = '';
           			    	if(!isNaN(data)){
           			    		text = formatMoney(data,2);
           			    	}else{
           			    		text = data;
           			    	}
           			    	return text;
           			    }
           			},
           			{
           			    "targets": 9, //第几列 从0开始
           			    "data": "impStatus",
           			    "render": function(data, type, full, meta){
           			    	var text = '';
           			    	if(data == '00'){
           			    		text = '未匹配';
           			    	}else if(data == '01'){
           			    		text = '已匹配';
           			    	}else if(data == '02'){
           			    		text = '已退款';
           			    	}else if(data == '03'){
           			    		text = '已完成';
           			    	}
           			    	return text;
           			    }
           			},
           			{
           			    "targets": 10, //第几列 从0开始
           			    "data": "operate",
           			    "render": function(data, type, full, meta){
           			    	var btn = '';
           			    	if(full.impStatus == '00'){
           			    		btn = '<a href="javascript:void(0);" onclick="refund('+full.id+',\''+full.impAcqSsn+'\',\''+full.payMerName+'\',\''+full.payMerBranchId+
           			    		'\',\''+full.payMerAcctNo+'\',\''+full.transactionAmount+'\',\''+full.impDate+'\',\''+full.remitDetails+'\')">退款</a>&nbsp;&nbsp';
           			    		if(full.remitDetails && (!full.paymentOrderCode || full.paymentOrderCode == 'null')){ //附言中有支付单号并且没有生成支付单则生成支付单
           			    			btn += '<a href="javascript:void(0);" onclick="generateOrder(\''+full.impAcqSsn+'\',\''+full.remitDetails+'\',\''+full.transactionAmount+'\',\''+full.payeeVirtualAcctName+
               			    		'\',\''+full.payeeVirtualAcctNo+'\',\''+full.payMerName+'\',\''+full.payMerBranchId+
               			    		'\',\''+full.payMerAcctNo+'\')">生成支付单</a>';
           			    		}
           			    	}else if(full.impStatus == '01'){
           			    		btn = '<a href="javascript:void(0);" onclick="finishOrder('+full.id+',\''+full.impAcqSsn+'\')">到货确认</a>&nbsp;&nbsp';
           			    		if(full.paymentOrderCode && full.paymentOrderCode != 'null'){
           			    			btn += '<a href="javascript:void(0);" onclick="refund('+full.id+',\''+full.impAcqSsn+'\',\''+full.payMerName+'\',\''+full.payMerBranchId+
           			    			'\',\''+full.payMerAcctNo+'\',\''+full.transactionAmount+'\',\''+full.impDate+'\',\''+full.remitDetails+'\')">退货</a>';
           			    		}
           			    	}
           			    	return btn;
           			    }
           			}
         ]
        
    });
}

function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
} 


function search() {
    tradeTable.ajax.reload();
}

function refund(id,impAcqSsn,payMerName,payMerBranchId,payMerAcctNo,transactionAmount,impDate,paymentOrderCode){
	if(!paymentOrderCode || paymentOrderCode == 'null') paymentOrderCode = '';
	var ele = '<div class="dialog-m" id="refundDialog">' +
    '<p><label for="impAcqSsn">汇入流水：</label><input type="text" name="impAcqSsn" id="impAcqSsn" value="'+impAcqSsn+'" size="40" readonly/></p>' +
    '<p><label for="payMerName">买方名称：</label><input type="text" name="payMerName" id="payMerName"  value="'+payMerName+'" size="40" readonly/></p>' +
    '<p><label for="payMerBranchId">买方开户行：</label><input type="text" name="payMerBranchId" id="payMerBranchId"  value="'+payMerBranchId+'" size="38" readonly/></p>' +
    '<p><label for="payMerAcctNo">买方账号：</label><input type="text" name="payMerAcctNo" id="payMerAcctNo"  value="'+payMerAcctNo+'" size="40" readonly/></p>' +
    '<p><label for="transactionAmount">订单金额：</label><input type="text" name="transactionAmount" id="transactionAmount"  value="'+transactionAmount+'" size="40" readonly/></p>' +
    '<p><label for="impDate">汇入日期：</label><input type="text" name="impDate" id="impDate"  value="'+impDate+'" size="40" readonly/></p>' +
    '<p><label for="paymentOrderCode">支付单号：</label><input type="text" name="paymentOrderCode" id="paymentOrderCode"  value="'+paymentOrderCode+'" size="40" readonly/></p>' +
    '<p><label for="refundReason">退款原因：</label><textarea name="refundReason" id="refundReason"  value="" size="400" maxlength="100" /></textarea></p>' +
    '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
	var dia = cbms.getDialog("申请退款/退货", ele);


    $("#refundDialog").on("click", "#cancel", function () {
        cbms.closeDialog();
    });

    $("#refundDialog").on("click", "#commit", function () {
        var refundReason = $("#refundReason").val();
        if(!refundReason || !refundReason.trim()){
        	cbms.alert("请输入退款原因");
        	return;
        }
        if(getLength(refundReason) > 100){
        	cbms.alert("退款原因不能超过100个字符（一个汉字算两个字符 ）");
        	return;
        }
        cbms.closeDialog();
        cbms.loading();
        $.ajax({
            type: "POST",
            url: Context.PATH + '/kuandao/refund/applyRefund.html',
            data: {
            	impId: id,
            	refundReason: refundReason
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	tradeTable.ajax.reload();
                	cbms.alert(response.data);
                } else {
                    cbms.alert(response.data);
                }
                cbms.closeLoading();
            },
            error: function (xhr, textStatus, errorThrown) {
            	cbms.alert("请刷新重试");
            	cbms.closeLoading();
            }
        });
    });
}

function generateOrder(impAcqSsn,paymentOrderCode,transactionAmount,payeeMerName,payeeMerVirAccount,payMerName,payMerBranchId,payMerAcctNo){
	if(!paymentOrderCode || paymentOrderCode == 'null') paymentOrderCode = '';
	var ele = '<div class="dialog-m" id="generateOrderDialog">' +
    '<p><label for="paymentOrderCode">支付单号：</label><input type="text" name="paymentOrderCode" id="paymentOrderCode" value="'+paymentOrderCode+'" size="40" /></p>' +
    '<p><label for="transactionAmount">支付金额：</label><input type="text" name="transactionAmount" id="transactionAmount"  value="'+transactionAmount+'" size="40" /></p>' +
    '<p><label for="payeeMerName">卖家名称：</label><input type="text" name="payeeMerName" id="payeeMerName"  value="'+payeeMerName+'" size="40" readonly/></p>' +
    '<p><label for="payeeMerVirAccount">卖家虚拟账号：</label><input type="text" name="payeeMerVirAccount" id="payeeMerVirAccount"  value="'+payeeMerVirAccount+'" size="36" readonly/></p>' +
    '<p><label for="payMerName">买方名称：</label><input type="text" name="payMerName" id="payMerName"  value="'+payMerName+'" size="40" readonly/></p>' +
    '<p><label for="payMerBranchId">买方开户行：</label><input type="text" name="payMerBranchId" id="payMerBranchId"  value="'+payMerBranchId+'" size="38" readonly/></p>' +
    '<p><label for="payMerAcctNo">买方账号：</label><input type="text" name="payMerAcctNo" id="payMerAcctNo"  value="'+payMerAcctNo+'" size="40" readonly/></p>' +
    '<p><label for="nsortName">商品名称：</label><input type="text" name="nsortName" id="nsortName"  value="" size="40" /></p>' +
    '<p><label for="weight">商品数量：</label><input type="text" name="weight" id="weight"  value="" size="40" /></p>' +
    '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
	var dia = cbms.getDialog("追加付款", ele);

	 $("#generateOrderDialog").on("click", "#cancel", function () {
	        cbms.closeDialog();
    });

    $("#generateOrderDialog").on("click", "#commit", function () {
        var paymentOrderCode = $("#paymentOrderCode").val();
        var transactionAmount = $("#transactionAmount").val();
        var nsortName = $("#nsortName").val();
        var weight = $("#weight").val();
        if(!paymentOrderCode){
        	cbms.alert("请输入正确的支付单号");
        	return;
        }
        if(getLength(paymentOrderCode) < 1 || getLength(paymentOrderCode) > 12){
           	cbms.alert("支付单号只能是1到12位");
         	return;
         }
        if(!transactionAmount|| isNaN(transactionAmount.length) || transactionAmount <= 0){
        	cbms.alert("请输入正确的支付金额");
        	return;
        }
        if(!nsortName){
         	cbms.alert("请输入商品名称");
         	return;
         }
        if(!weight || isNaN(weight) || weight <= 0){
         	cbms.alert("请输入正确的商品数量");
         	return;
         }
        cbms.closeDialog();
        cbms.loading();
        $.ajax({
            type: "POST",
            url: Context.PATH + '/kuandao/refund/generateOrder.html',
            data: {
            	impAcqSsn: impAcqSsn,
             	paymentOrderCode: paymentOrderCode,
             	transactionAmount:transactionAmount,
             	nsortName:nsortName,
             	weight:weight
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	tradeTable.ajax.reload();
                	cbms.alert(response.data);
                } else {
                	cbms.alert(response.data,function(){
                    	tradeTable.ajax.reload();
                    });
                }
                cbms.closeLoading();
            },
            error: function (xhr, textStatus, errorThrown) {
            	cbms.alert("请刷新重试");
            	cbms.closeLoading();
            }
        });
    });
}

function finishOrder(id,impAcqSsn){
	if(!id){
		cbms.alert("请选择一笔汇入流水");
		return;
	}
	cbms.confirm('您确定要执行到货确认吗？',id,function(id){
		cbms.loading();
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/refund/finishOrder.html',
	        data: {
	        	id: id,
	        	impAcqSsn : impAcqSsn
	        },
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	cbms.alert('到货确认完成',function(){
	            		tradeTable.ajax.reload();
	            	});
	            } else {
	                cbms.alert(response.data);
	            }
	            cbms.closeLoading();
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        	cbms.closeLoading();
	        }
	    });
	});
}

function getLength(str)   
{  
    return str.replace(/[^\x00-\xff]/g,"aa").length;  
};