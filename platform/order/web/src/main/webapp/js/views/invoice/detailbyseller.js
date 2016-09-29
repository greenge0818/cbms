/**
 * Created by lcw on 2015/8/2.
 */

$(document).ready(function () {
    var table;
    table = $('#dynamic-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchdetail.html',
            type: 'POST',
            data: function (d) {
                d.sellerId = $("#sellerId").val(),
                d.departmentId = $("#departmentId").val(),
                d.orgName = $("#sorganization").val(),
                d.accountName = $("#buyer_name").val(),
                d.startTime = $("#startTime").val(),
                d.code = $("#code").val(),
                d.endTime = $("#endTime").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var viewName = aData.accountName;
            if (aData.totalDepartment > 1) {
                viewName += "【" + aData.departmentName + "】";
            }
            var amount = parseFloat(aData.totalAmount);
            var weight = parseFloat(aData.totalWeight);
            var allowanceWeight = parseFloat(aData.allowanceWeight);
            var allowanceAmount = parseFloat(aData.allowanceAmount);
            $('td:eq(2)', nRow).html(viewName);
            $('td:eq(6)', nRow).html((weight).toFixed(6)).addClass("text-right");
            $('td:eq(8)', nRow).html(formatMoney((amount), 2)).addClass("text-right");
            $('td:eq(7)', nRow).html((allowanceWeight).toFixed(6)).addClass("text-right");
            $('td:eq(9)', nRow).html(formatMoney((allowanceAmount), 2)).addClass("text-right");
        },
        columns: [
			{data: 'creationTime'},
			{data: 'code'},
			{data: 'accountName'},
            {data: 'nsortName'},
            {data: 'material'},
            {data: 'spec'},
            {data: 'totalWeight'},
            {data: 'allowanceWeight'},
            {data: 'totalAmount'},
            {data: 'allowanceAmount'}
        ],
        columnDefs: [
             {
                 "targets": 0, //第几列 从0开始
                 "data": "created",
                 "render": renderTime
             }
        ]
    });
    $("#searchForm").on("click","#btn", function() {
    	table.ajax.reload();
    	$.ajax({
			type : "POST",
			url : Context.PATH + '/invoice/in/query/total.html',
			data : {
			    "sellerId" : $("#sellerId").val(),
                "departmentId" : $("#departmentId").val(),
                "orgName" : $("#sorganization").val(),
                "accountName" : $("#buyer_name").val(),
                "startTime" : $("#startTime").val(),
                "code" : $("#code").val(),
                "endTime" : $("#endTime").val()
			},
		    success: function (result) {
		    	if(result.success){
		    		if (result.data == null) {
		    			$("#totalAmount").text("0.00");
		    			$("#totalWeight").text("0.00");
		    		}else{
			    		$("#totalAmount").text(formatMoney(result.data.totalAmount,2));
						$("#totalWeight").text(formatMoney(result.data.totalWeight,6));
		    		}
		    	}
		    }
		});
    });
});

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
