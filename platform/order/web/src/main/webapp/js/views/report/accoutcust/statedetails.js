/**
 * Created by lixiang on 2015/8/24.
 */

var dt = "";
function fillDataTable() {
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/accoutcust/state/details/data.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	accountId:$("#accountId").val(),//客户ID
                	dateStartStr:$("#startTime").val(),//起始时间
                	dateEndStr:$("#endTime").val()//终止时间
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	 var amount = formatMoney(aData.amount,2);//二次结算账户发生额
        	 var cashHappenBalance = formatMoney(aData.cashHappenBalance,2);//资金账户发生额
        	 
        	 if(parseFloat(amount) == 0){
        		 $('td:eq(7)', nRow).html('-').addClass("text-right"); 
        		 $('td:eq(8)', nRow).html('-').addClass("text-right");
        	 }else if(parseFloat(amount) > 0){
        		 $('td:eq(7)', nRow).html(formatMoney(aData.amountAdd,2)).addClass("text-right");//二次结算账户增加	
        	 }else{
        		 $('td:eq(8)', nRow).html(formatMoney(Math.abs(aData.amountReduce),2)).addClass("text-right");//二次结算账户减少
        	 }
        	 if(parseFloat(cashHappenBalance) == 0){
        		 $('td:eq(4)', nRow).html('-').addClass("text-right");
        		 $('td:eq(5)', nRow).html('-').addClass("text-right");
        	 }else if(parseFloat(cashHappenBalance) > 0){
        		 $('td:eq(4)', nRow).html(formatMoney(aData.balanceAdd,2)).addClass("text-right");//账户余额增加
        	 }else{
        		 $('td:eq(5)', nRow).html(formatMoney(Math.abs(aData.balanceReduce),2)).addClass("text-right");//账户余额减少
        	 }
        	 $('td:eq(6)', nRow).html(formatMoney(aData.cashCurrentBalance,2)).addClass("text-right");//资金账户余额
        	 $('td:eq(9)', nRow).html(formatMoney(aData.currentBalance,2)).addClass("text-right");//二次结算余额
             return nRow;
        },
        columns: [
            {data: 'serialTime'}, //流水时间
            {data: 'associationType'}, //关联类型
            {data: 'consignOrderCode'}, //关联单号
            {data: 'applyType'}, //类型
            {defaultContent: ''}, //资金账户增加
            {defaultContent: ''}, //资金账户减少
            {defaultContent: ''}, //资金账户余额
            {defaultContent: ''}, //二次结算账户增加
            {defaultContent: ''}, //二次结算账户减少
            {defaultContent: ''}, //二次结算账户余额
            {data: 'applyerName'}  //业务员
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
                 "data": "serialTime",
                 "render": renderTime
             }
        ]
    });
    
}

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
    $("#searchForm").on("click","#queryBtn", function() {
        dt.ajax.reload();
        $.ajax({
            type: "POST",
            url: Context.PATH + '/report/accoutcust/query/balance.html',
            data:{id:$("#accountId").val(),dateStartStr:$("#startTime").val()},
            dataType:"json",
            success: function(data){
                if(data.success){                    
	               $("#cashCurrentBalance").html(formatMoney(data.data.cashCurrentBalance,2));       
	               $("#currentBalance").html(formatMoney(data.data.currentBalance,2));
                }
            }
        });
    });
});