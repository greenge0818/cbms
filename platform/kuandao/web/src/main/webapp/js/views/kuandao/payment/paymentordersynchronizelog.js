/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;

function initTable() {
    var url = Context.PATH + "/kuandao/payment/querySynchronizeLog.html";
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
                d.paymentOrderCode = $("#paymentOrderCode").val();
                d.dateCreated = $("#dateCreated").val();
                d.dateEnd = $("#dateEnd").val();
                d.result = $("#result").children("option:selected").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'createDateTime'},
            {data: 'consignOrderCode'},
            {data: 'impAcqSsn'},
            {data: 'paymentOrderCode'}
            ,{data: 'payMerName'}
            , {data: 'payeeMerName'}
            , {data: 'payeeMerVirAcctNo'}
            , {data: 'nsortName'}
            ,{data: 'weight'}
            ,{data: 'transactionAmount'}
            , {data: 'result'}
            , {data: 'errorMsg'}
        ]
        , columnDefs: [
			{
			    "targets": 1, //第几列 从0开始
			    "data": "consignOrderCode",
			    "render": renderConsignOrderCode
			},
			{
			    "targets": 8, //第几列 从0开始
			    "data": "weight",
			    "render": renderWeight
			},
			{
			    "targets": 10, //第几列 从0开始
			    "data": "result",
			    "render": function (data, type, full, meta) {
			    	var text = '';
			        //同步状态  同步成功、同步失败
			    	if(data == 1){
			    		text = '<span>同步成功</span>';
			    	}else{
			    		text = '<span>同步失败</span>';
			    	}
			    	return text;
			    }
			}
        ]
    });
}

