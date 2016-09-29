/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;

function initTable() {
    var url = Context.PATH + "/kuandao/payment/querypayment.html";
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
            	'sDefaultContent': '',
            	 'targets': [ '_all' ]
            }
        ]
    });
}
