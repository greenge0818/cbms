/**
 * Created by lixiang on 2015/7/29.
 */

var dt = "";
function fillDataTable() {
	var paysettlementConfirm = $("#paysettlementConfirm").val();//二次结算确认付款权限(浦发)
	var paysettlementConfirmIcbc = $("#paysettlementConfirmIcbc").val();//二次结算确认付款权限(工行)
	var paysettlementPrintApp = $("#paysettlementPrintApp").val();//打印二次结算付款申请权限
    dt = $('#dynamic-table').DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        ajax: {
            url: Context.PATH + '/order/query/secondpaysettlementother.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	dateStart:$("#startTime").val(),//起始时间
                    dateEnd:$("#endTime").val(),//终止时间
                    requesterName:$("#owner").val(),//交易员
                    orderBy : $("#orderBy").val(),//排序
	                order : $("#order").val()
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	//var creditLimit = formatMoney(aData.creditLimit,2);
        	//var creditLimitUsed = formatMoney(aData.creditLimitUsed,2);
        	//var countLimit = formatMoney((aData.creditLimit.sub(aData.creditLimitUsed)),2);
        	var totalAmount =formatMoney(aData.totalAmount,2);
            //if(parseFloat(aData.creditLimitUsed) < 0){//服务中心已使用额度小于零，显示为零，剩余额度就是服务中心垫付额度
            //    creditLimitUsed = "0.00";
            //    countLimit = formatMoney(aData.creditLimit,2);
            //}
            //$('td:eq(0)', nRow).html(creditLimit).addClass("text-right");
            //$('td:eq(1)', nRow).html(creditLimitUsed).addClass("text-right");
            //$('td:eq(2)', nRow).html(countLimit).addClass("text-right");
         	$('td:eq(4)', nRow).html(totalAmount).addClass("text-right");
             if(aData.status== 'APPLYPRINTED'){
            	 var link ='';
            	 if(paysettlementConfirm=="true" || paysettlementConfirmIcbc=="true"){
            		 link = '<a href="' + Context.PATH + '/order/query/secondpaysettlementconfirm.html?requestId='+aData.qid+'">确认付款</a><br/>';
            	 }

//            	 if(paysettlementPrintApp =="true"){
//            		 if(aData.printTimes == 0){
//            			 link += '<a href="' + Context.PATH + '/order/query/payrequest.html?id='+aData.buyerId+'&requestid='+aData.qid+'&print=true">打印付款申请单</a>';
//            		 }  
//            	 }
             }else if(aData.status== 'CONFIRMEDPAY'){
            	 var link = '<span class="bolder">已付款</span>';
             }
             $('td:eq(-1)', nRow).html(link);
             return nRow;
        },
        columns: [
			//{defaultContent: ''},   //可透支额度
			//{defaultContent: ''},  //已透支额度
			//{defaultContent: ''},	//剩余额度
            {data: 'code'},  //付款申请单编号
            {data: 'lastUpdated'},  //申请单打印时间
            {data: 'buyerName'},    //公司全称
            {data: 'requesterName'},   //交易员
            {defaultContent: ''},   //本次付款金额
            {data: 'status'},  //状态
            {defaultContent: ''}
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "前页",
                "sNext": "后页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
            {
                "targets": 1, //第几列 从0开始
                "data": "created"
//                "render": renderTime
            },
            {
                "targets": 5, //第几列 从0开始
                "data": "status",
                "render": getType
            }
        ]
    });
    
}

   
function getType(data, type, full, meta){
    var status = '--';
    if(data == 'REQUESTED') status='<span>已申请 待审核</span>';
    else if(data == 'APPROVED') status='<span>已通过审核</span>';
    else if(data == 'APPLYPRINTED') status='<span>已打印付款申请</span>';
    else if(data == 'DECLINED') status='<span>未通过 </span>';
    else if(data == 'CONFIRMEDPAY') status='<span>已确认付款</span>';
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
    initOrderBy();
    $("#ser").on("click","#queryBtn", function() {
        dt.ajax.reload();
    });
});

function initOrderBy(){
	$("table thead th:lt(5)").click(function(){
		var orderBy = $(this).attr("orderBy");
		var order=$(this).attr("order");
		if(order==undefined){
			order = "";
		}
		if(order==""||order=="asc"){
			$(this).attr("order","desc");
		}else{
			$(this).attr("order","");
		}
		$("#orderBy").val(orderBy);
		$("#order").val(order);
	});
}

/**
 * 间隔1分钟刷新页面
 */
function beginrefresh() {
	setInterval(function() {
		 location.reload();
	}, 60000);
}
window.onload = beginrefresh