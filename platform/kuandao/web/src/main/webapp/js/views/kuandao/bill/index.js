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
    var url = Context.PATH + "/kuandao/bill/queryDailyBill.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "800px",
        "sScrollX": "1200px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.paymentOrderCode = $("#paymentOrderCode").val();
                d.payeeVirtualAcctName = $("#payeeMerName").val();
                d.payMerName = $("#payMerName").val();
                d.paymentStatus = $("#status").val();
                d.startDate = $("#startDate").val();
                d.endDate = $("#endDate").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'impAcqSsn'},
            {data: 'payeeAcctNo'},
            {data: 'payeeVirtualAcctNo'},
            {data: 'payeeVirtualAcctName'}
            ,{data: 'isDebit'}
            , {data: 'transactionAmount'}
            , {data: 'balance'}
            ,{data: 'impDate'}
            ,{data: 'digestCode'}
            , {data: 'payMerBranchId'}
            , {data: 'payMerAcctNo'}
            , {data: 'payMerName'}
            , {data: 'paymentOrderCode'}
            , {data: 'paymentStatus'}
        ] 
        , columnDefs: [
					{
						    "targets" : 4, //第几列 从0开始
						    "data": "isDebit",
						    "render": function(data, type, full, meta){
						    	var text = '';
						    	if(data == '1'){
						    		text = '借';
						    	}else{
						    		text = '贷';
						    	}
						    	return text;
						    }
						},
						{
						    "targets": 13, //第几列 从0开始
						    "data": "paymentStatus",
						    "render": function(data, type, full, meta){
						    	var text = '';
						    	if(data == '0'){
						    		text = '未匹配';
						    	}else if(data == '1'){
						    		text = '已匹配';
						    	}else if(data == '2'){
						    		text = '已退款';
						    	}
						    	return text;
						    }
						}
                       ,{
                        	'sDefaultContent': '',
                        	 'targets': [ '_all' ]
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