/**
 * Created by shanzejun on 2016/8/3.
 * 已完成支付单
 */

var tradeTable;

function initTable() {
    var url = Context.PATH + "/kuandao/payment/queryfinishedpayment.html";
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
                d.submitStatus = $("#status").children('option:selected').val();
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
			    "targets": 10, //第几列 从0开始
			    "data": "operate",
			    "render": function(data, type, full, meta){
			    	var btn = '';
			    	if(full.submitStatus == '1' && full.chargStatus == '0'){
			    		btn = '<a href="javascript:void(0);" onclick="charg('+full.id+')">充值</a>';
			    	}
			    	return btn;
			    }
			},
            {
            	'sDefaultContent': '',
            	 'targets': [ '_all' ]
            }
        ]
    });
}

function charg(id){
	if(!id){
		cbms.alert("请选择一笔支付单");
		return;
	}
	cbms.confirm('您确定要进行充值操作吗？',id,function(id){
		cbms.loading();
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/payment/charg.html',
	        data: {
	        	id: id
	        },
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	cbms.alert('已发送充值请求，请稍后查看结果',function(){
	            		tradeTable.ajax.reload();
	            	});
	            } else {
	                cbms.alert(response.data,function(){
	            		tradeTable.ajax.reload();
	            	});
	            }
	            cbms.closeLoading();
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        	cbms.closeLoading();
	        }
	    });
	});
}
