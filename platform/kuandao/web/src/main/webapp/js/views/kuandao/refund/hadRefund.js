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
    var url = Context.PATH + "/kuandao/refund/queryRefund.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.payMerName = $("#payMerName").val();
                d.payeeVirtualAcctNo = $("#payeeVirtualAcctNo").val();
                d.startDate = $("#startDate").val();
                d.endDate = $("#endDate").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'impDate'},
            {data: 'impAcqSsn'},
            {data: 'payeeVirtualAcctName'}
            ,{data: 'payMerName'}
            , {data: 'payMerBranchId'}
            , {data: 'payMerAcctNo'}
            ,{data: 'paymentOrderCode'}
            , {data: 'transactionAmount'}
            , {data: 'refundReason'}
            , {data: 'impStatus'}
        ]	
        , columnDefs: [
              			{
              			    "targets" : 7, //第几列 从0开始
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