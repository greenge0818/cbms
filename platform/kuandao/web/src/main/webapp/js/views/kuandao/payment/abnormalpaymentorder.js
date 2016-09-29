/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;

function initTable() {
    var url = Context.PATH + "/kuandao/payment/queryabnormalpayment.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.consignOrderCode = $("#consignOrderCode").val();
                d.payeeMerName = $("#payeeMerName").val();
                d.payMerName = $("#payMerName").val();
                d.ownerName = $("#ownerName").val();
                d.dateCreated = $("#dateCreated").val();
                d.dateEnd = $("#dateEnd").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'paymentOrderCode'},
            {data: 'consignOrderCode'},
            {data: 'impAcqSsn'},
            {data: 'createDateTime'}
            ,{data: 'payMerName'}
            , {data: 'payeeMerName'}
            , {data: 'nsortName'}
            ,{data: 'weight'}
            ,{data: 'transactionAmount'}
            , {data: 'submitStatus'}
            , {data: 'submitErrorMsg'}
            , {data: 'operate'}
        ]
	    , columnDefs: [
			{
			    "targets": 1, //第几列 从0开始
			    "data": "consignOrderCode",
			    "render": renderConsignOrderCode
			},
			{
			    "targets": 7, //第几列 从0开始
			    "data": "weight",
			    "render": renderWeight
			},
			{
			    "targets": 9, //第几列 从0开始
			    "data": "submitStatus",
			    "render": renderSubmitStatus
			},
			{
			    "targets": 11, //第几列 从0开始
			    "data": "",
			    "render": function (data, type, full, meta) {
			    	var btn = '';
			    	btn = btn + '<a href="javascript:void(0);" onclick="submit('+full.id+')">重新提交</a>';
                	btn = btn + '&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="modify('+full.id+',\''+full.paymentOrderCode+'\','+full.transactionAmount+
                	',\''+full.payMerName+'\',\''+full.payeeMerName+'\',\''+full.payeeMerVirAcctNo+'\',\''+full.nsortName+'\','+full.weight+')">更改</a>';
			    	return btn;
			    }
			}
        ]
    });
}


function submit(id){
	if(!id){
		cbms.alert('请选择记录');
		return;
	}
	cbms.confirm('您要重新提交吗？',id,function(id){
		cbms.loading();
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/payment/submitAgain.html',
	        data: {
	        	id: id
	        },
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	cbms.alert('重新提交成功',function(){
	            		window.location.href=Context.PATH + "/kuandao/payment/index.html";
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

function modify(id,paymentOrderCode,transactionAmount,payMerName,payeeMerName,payeeMerVirAcctNo,nsortName,weight){
	 var ele = '<div class="dialog-m" id="modifyDialog">' +
     '<p><label for="paymentOrderCode">支付单号：</label><input type="text" name="paymentOrderCode" id="paymentOrderCode" value="'+paymentOrderCode+'" size="40"/></p>' +
     '<p><label for="transactionAmount">支付金额：</label><input type="text" name="transactionAmount" id="transactionAmount"  value="'+transactionAmount+'" size="40" />元</p>' +
     '<p><label for="payMerName">付款方名称：</label><input type="text" name="payMerName" id="payMerName"  value="'+payMerName+'" size="38" disabled/></p>' +
     '<p><label for="payeeMerName">收款方名称：</label><input type="text" name="payeeMerName" id="payeeMerName"  value="'+payeeMerName+'" size="38" disabled/></p>' +
     '<p><label for="payeeMerVirAccount">收款方虚拟账号：</label><input type="text" name="payeeMerVirAccount" id="payeeMerVirAccount"  value="'+payeeMerVirAcctNo+'" size="34" disabled/></p>' +
     '<p><label for="nsortName">商品名称：</label><input type="text" name="nsortName" id="nsortName"  value="'+nsortName+'" size="40" /></p>' +
     '<p><label for="weight">商品数量：</label><input type="text" name="weight" id="weight"  value="'+weight+'" size="40" />吨</p>' +
     '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
 var dia = cbms.getDialog("客户信息修改", ele);


 $("#modifyDialog").on("click", "#cancel", function () {
     cbms.closeDialog();
 });

 $("#modifyDialog").on("click", "#commit", function () {
     var paymentOrderCode = $("#paymentOrderCode").val();
     var transactionAmount = $("#transactionAmount").val();
     var nsortName = $("#nsortName").val();
     var weight = $("#weight").val();
     if(!paymentOrderCode || isNaN(paymentOrderCode)){
     	cbms.alert("请输入正确的支付单号");
     	return;
     }
     if(paymentOrderCode.length < 5 || paymentOrderCode.length > 6){
    	cbms.alert("支付单号只能是5或6位");
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
         url: Context.PATH + '/kuandao/payment/modifyOrder.html',
         data: {
         	id: id,
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