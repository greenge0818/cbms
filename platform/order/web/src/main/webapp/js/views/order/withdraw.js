/**
 * Created by lcw on 2015/7/27.
 */

$(document).ready(function () {
    var table;
    var status = $("#status").val();
    var withdrawApp = $("#withdrawApp").val();
    var withdrawPrint = $("#withdrawPrint").val();
    var withdrawConfirm = $("#withdrawConfirm").val();
    var withdrawConfirmIcbc = $("#withdrawConfirmIcbc").val();
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "bLengthChange": false,
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            url: Context.PATH + '/order/query/getwithdrawdata.html',
            type: 'POST',
            data: function (d) {
                d.status = status;
                d.ownerName = $("#owner").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.orderBy = $("#orderBy").val();//排序
                d.order = $("#order").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	var dt;
        	if(status=="REQUESTED"){
        		dt = new Date(aData.created);
        	}else if(status=="APPLYPRINTED"){
        		dt = new Date(aData.lastUpdated);
        	}
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + "&nbsp;"
                + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            $('td:eq(1)', nRow).html(time);
            var totalAmount = aData.totalAmount;
            var departmentName="("+aData.departmentName+")"
            if(aData.departmentName==null)
                departmentName=""
            if(aData.buyerName==null)
                aData.buyerName=""
            $('td:eq(2)', nRow).html(aData.buyerName+departmentName);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            var statusText = "";
            var operatingHtml = "";
            var printTimes = parseInt(aData.printTimes);
            if (isNaN(printTimes))
                printTimes = 0;

            if (status == "REQUESTED") {
                statusText = "已申请付款";
                if (withdrawApp == "true") {
                    operatingHtml = "<a href='" + Context.PATH + "/payment/" + aData.id + "/withdrawdetail.html'>审核</a>";
                }
            } else if (status == "APPROVED") {
                statusText = "已通过付款申请";
                if (withdrawPrint == "true") {
                    operatingHtml += "<a href='" + Context.PATH + "/payment/" + aData.id + "/paymentrequisition.html?f=tab'>打印付款申请单</a>&nbsp;&nbsp;";
                }
            } else if (status == "APPLYPRINTED") {
                statusText = "已打印付款申请";
                if (withdrawConfirm == "true" || withdrawConfirmIcbc == "true") {
                    operatingHtml += "<a href='" + Context.PATH + "/payment/" + aData.id + "/withdrawdetail.html'>确认已付款</a>";
                }
            }
            $('td:eq(5)', nRow).html(statusText);
            $('td:eq(6)', nRow).html(operatingHtml);
        },
        columns: [
            {data: 'code'},
            {data: 'lastUpdated'},//申请单打印时间
            {data: 'buyerName'},
            {data: 'requesterName'},
            {data: 'totalAmount'},
            {data: 'status'},
            {data: 'id'}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
    
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
});

 /**
 * 间隔1分钟刷新页面
 */
function beginrefresh() {
	setInterval(function() {
		 location.reload();
	}, 60000);
}
window.onload = beginrefresh