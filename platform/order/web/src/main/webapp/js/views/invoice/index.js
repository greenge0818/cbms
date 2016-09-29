$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.invoiceCode = $("#invoiceCode").val();
                d.sellerName = $("#seller").val();
                d.status = $("#sltStatus option:selected").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var totalAmount = parseFloat(aData.totalAmount);
            $('td:eq(0)', nRow).html((new Date(aData.invoiceDate).Format("yyyy-MM-dd")));
            $('td:eq(3)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");

            $('td:eq(6)', nRow).html(formatDate(new Date(aData.created)));

            $('td:eq(7)', nRow).html(getStatusName(aData.status));

            // operation button
            var link = Context.PATH + '/invoice/in/'+aData.id+'/details.html';
            $('td:last', nRow).html(generateOptHtml(link));
            if(aData.checkTotalAmount && aData.checkTotalAmount > 0){
            	$('td:eq(3)', nRow).html(formatMoney(aData.checkTotalAmount,2));
            }
        },
        columns: [
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'created'},
            {data: 'status'},
            {defaultContent:""}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
});

function generateOptHtml(link) {
    var optHtml = '<div class="hidden-sm hidden-xs action-buttons">';
    optHtml += "<a href='" + link + "' target='_blank' title='查看详情'>";
    optHtml += "<i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</div>';

    return optHtml;
}

function formatDate(dt) {
    return dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
        ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
}

function getStatusName(v) {
    if (v == "AWAITS") {
        return "待收票";
    } else {
        var name = $("#sltStatus option[value='" + v + "']").text();
        return utils.isEmpty(name) ? "未知" : name;
    }
}
