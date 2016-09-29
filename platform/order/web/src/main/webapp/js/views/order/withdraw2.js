/**
 * Created by lcw on 2015/7/27.
 */
var table;
$(document).ready(function () {
    var status = $("#status").val();
    var withdrawApp = $("#withdrawApp").val();
    var withdrawPrint = $("#withdrawPrint").val();
    var withdrawConfirm = $("#withdrawConfirm").val();
    table = $('#dynamic-table').dataTable({
    	searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
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
                d.showPrinted = $("#showPrinted").is(":checked")?1:0;
                d.orderBy = $("#orderBy").val();//排序
                d.order = $("#order").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var dt = new Date(aData.lastUpdated);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + "&nbsp;"
                + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            $('td:eq(1)', nRow).html(time);
            var totalAmount = aData.totalAmount;
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            var statusText = "";
            var operatingHtml = "";
            var printTimes = parseInt(aData.printTimes);
            var status = "";
            if(printTimes > 0){           	
	        	 if(aData.status == 'APPROVED'){
	        		 status='<span>已通过付款申请</span>';
	        	 }
	        	 $('td:eq(5)', nRow).html('<p><span class="bolder">已打印<em class="red" id="dialogCount">'+printTimes+'</em>次</span></p><p>'+status+'</p>');
	        	 $('td',nRow).addClass("bg-yellow-style");
      	 	} else {
	      	 	 statusText = "已通过付款申请";
	      	 	 $('td:eq(5)', nRow).html(statusText);
      	 	}
            if (isNaN(printTimes))
                printTimes = 0;
            if (withdrawPrint == "true") {
                operatingHtml += "<a href='" + Context.PATH + "/payment/" + aData.id + "/paymentrequisition.html?f=applyList'>打印付款申请单</a>&nbsp;&nbsp;";
            }
            $('td:eq(6)', nRow).html(operatingHtml);
            return nRow;
        },
        columns: [
            {data: 'code'},
            {data: 'lastUpdated'},
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
    
    
	$(document).on("click","#showPrinted",function(){
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