/**
 * Created by lixiang on 2016/03/03.
 * 待确认预付款订单列表
 */

var dt;
$(document).ready(function () {
		var url = "";
		if ($("#status").val() == "APPLYPRINTED") {
			url = Context.PATH + '/order/query/get/confirm/request.html'
		} else {
			url = Context.PATH + '/order/query/printpaymentquery.html'
		}
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: url,
            type: "POST", 
            data: function (d) {
            	d.requesterName = $("#owner").val();//客户名称
            	d.startTime = $("#startTime").val();//起始时间
            	d.endTime = $("#endTime").val();//终止时间
            	d.status = $("#status").val();
            	d.showPrinted = $("#showPrinted").is(":checked")?1:0;
            }
        },
        aLengthMenu: [10, 50, 100],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        bFilter: false,
        bLengthChange: true, //不显示每页长度的选择条
        fnRowCallback: function (nRow, aData, iDataIndex) {
             $('td:eq(4)', nRow).html(formatMoney(aData.totalAmount,2)).addClass("text-right");
             var showPrinted = $("#showPrinted").is(":checked")?1:0;
             var link = "";
             if (aData.status == "APPLYPRINTED" && showPrinted == 0 && _confirm) {
            	 link = '<a href="'+Context.PATH+'/order/query/'+aData.id+'/requestdetail.html">确认付款</a>';
             } else if (aData.status == "APPLYPRINTED" && showPrinted == 1 && _permPrint) {
            	 link += '<a href="' + Context.PATH + '/order/query/payrequest.html?requestId='+aData.id+'&print=true&type=4&from=1">打印付款申请单</a>';
            	 var status = "<span class='bolder'>已打印<em class='red'>"+aData.printTimes+"</em>次</span>";
            	 $('td:eq(5)', nRow).html(status);
             } else if (aData.status == "APPROVED" && _permPrint){
            	 link += '<a href="' + Context.PATH + '/order/query/payrequest.html?requestId='+aData.id+'&print=true&type=4&from=1">打印付款申请单</a>';
             }
             $('td:eq(-1)', nRow).html(link);
             return nRow;
        },
        columns: [
            {data: 'payCode'},   //付款申请单号
            {data: 'lastUpdated'}, //审核时间
            {data: 'buyerName'},  //公司全称
            {data: 'requesterName'},  //钢为交易员
            {defaultContent: ''}, //本次申请付款金额
            {data: 'status'},// 付款申请类型
            {defaultContent: ''}//操作
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
                 "targets": 1, //第几列 从0开始
                 "data": "lastUpdated",
                 "render": formatDay
             }
             ,{
                 "targets": 5, //第几列 从0开始
                 "data": "status",
                 "render": toStatus
             }
        ]
    });
	    
    function formatDay(data) {
    	var dt = new Date(data);
    	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + 
		((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" 
		+ ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    	return time;
    }
    
    function toStatus(data) {
    	var status ="";
    	if(data == "REQUESTED") {
    		status = "已申请付款";
    	} else if (data == "APPLYPRINTED") {
    		status = "已通过付款申请";
    	} else if (data == "APPROVED") {
    		status = "已通过审核";
    	}
    	return status;
    }
});

$("#queryBtn").click(function(){
	dt.ajax.reload();
});

$(document).on("click","#showPrinted",function(){
	dt.ajax.reload();
});

