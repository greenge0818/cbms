/**
 * Created by kongbinheng on 2015/11/19.
 */

$(document).ready(function () {
    var table;
    var status = $("#status").val();
    var withdrawApp = $("#withdrawApp").val();
    var withdrawPrint = $("#withdrawPrint").val();
    var withdrawConfirm = $("#withdrawConfirm").val();
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "bLengthChange": false,
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            url: Context.PATH + '/order/query/bankcodeverifydata.html',
            type: 'POST',
            data: function (d) {
                d.accountName = $("#accountName").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.orderBy = $("#orderBy").val();//排序
                d.order = $("#order").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            // id隐藏
            $('td:eq(0)', nRow).addClass("none");
            // 编辑时间
            var dt = new Date(aData.lastUpdated);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + "&nbsp;&nbsp;"
                            + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            $('td:eq(2)', nRow).html(time);
            // 操作
            renderOperation(nRow, aData, iDataIndex);
        },
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'lastUpdated'},
            {data: 'lastUpdatedBy'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });

    // 操作
    function renderOperation(nRow, aData, iDataIndex){
        var operatingHtml = "";
        operatingHtml = "<a href='" + Context.PATH + "/order/query/account/" + aData.id + "/bankcodeshow.html'>审核银行账号</a>";
        $('td:eq(-1)', nRow).html(operatingHtml).addClass("text-center");
    }

    // 搜索
    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    // 列表排序
    $("table thead th:lt(2)").click(function(){
		var orderBy = $(this).attr("orderBy");
		var order = $(this).attr("order");
		if(order == undefined){
			order = "";
		}
		if(order == "" || order == "asc"){
			$(this).attr("order", "desc");
		}else{
			$(this).attr("order", "");
		}
		$("#orderBy").val(orderBy);
		$("#order").val(order);
	});
});
