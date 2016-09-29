/**
 * Created by lixiang on 2015/7/31.
 */

var dt = "";
function fillDataTable() {
	
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/rebate/load.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	startTimeStr:$("#startTime").val(),//起始日期
                	endTimeStr:$("#endTime").val(),//终止日期
                	accountId:$("#account_id").val()//客户ID
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	var type = aData.type;//类型
	       	var weight = formatMoney(aData.weight,4);//实提总重（吨）
	       	var amount = formatMoney(aData.amount,2);//实提总金额（元）
	       	var rebateAmount = formatMoney(aData.rebateAmount,2);//返利金额增加（元）
	       	var rebateBalance = formatMoney(aData.rebateBalance,2);//返利当前余额（元）
	       	var moneyReduce = formatMoney(aData.moneyReduce,2);//金额减少（元）
	       	var code = aData.code;//代运营单号
	       	
	       	if(aData.type == "返利"){
	       		$('td:eq(1)', nRow).html(type);
	       		$('td:eq(2)', nRow).html(code);
	       		$('td:eq(4)', nRow).html(weight).addClass("text-left");
	          	$('td:eq(5)', nRow).html(amount).addClass("text-right");
	       		$('td:eq(6)', nRow).html(rebateAmount).addClass("text-right");
	       		$('td:eq(7)', nRow).html("--").addClass("text-right");
	       		$('td:eq(-1)', nRow).html(rebateBalance).addClass("text-right");
	       	}
	       	if(aData.type == "提现"){
	       		$('td:eq(1)', nRow).html(type);
	       		$('td:eq(2)', nRow).html("--");
	       		$('td:eq(4)', nRow).html("--").addClass("text-left");
	          	$('td:eq(5)', nRow).html("--").addClass("text-right");
	       		$('td:eq(6)', nRow).html("--").addClass("text-right");
	       		$('td:eq(7)', nRow).html(moneyReduce).addClass("text-right");
	       		$('td:eq(-1)', nRow).html(rebateBalance).addClass("text-right");
	       	}
        	return nRow;
       },
        columns: [
            {data: 'rebateTime'},   //时间
            {defaultContent: ''},  //类型	
            {defaultContent: ''},   //代运营交易单号
            {data: 'contactName'},  //联系人
            {data: 'weight'},   //实提总重（吨）
            {data: 'amount'},  //实提总金额（元）
            {defaultContent: ''},  //金额增加（元）
            {defaultContent: ''},   //金额减少（元）
            {defaultContent: ''}  //当前余额（元）        
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上一页",
        		"sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
			{
			    "targets": 0, //第几列 从0开始
			    "data": "rebateTime",
			    "render": renderTime
			},
			{
			    "targets": 1, //第几列 从0开始
			    "data": "type",
			    "render": getType
			}
        ]
    });
    
}

function getType(data, type, full, meta){
    var status = '--';
    if(data == 'rebate') status='<span>返利</span>';
    else
    	status='<span>提现</span>';
    return status;
};

function renderTime(data){
    return date2String(new Date(data));
}
//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}

jQuery(function($) {
	
    fillDataTable();
    
    $("#searchForm").on("click","#btn", function() {
        dt.ajax.reload();
        window.setTimeout(setTotalRow,500);
    });
});