/**
 * 已打回待关联
 * Created by lcw on 2015/9/10.
 */

$(document).ready(function () {
    var table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = $("#status").val();
                d.relationStatus = "toberelation";
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var totalAmount = parseFloat(aData.checkTotalAmount);
            var dt = new Date(aData.created);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + "&nbsp;&nbsp;"
                + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            $('td:eq(0)', nRow).html(time);
            var displaName = aData.sellerName;
            if(aData.totalDepartment > 1 && aData.departmentName != null){
            	displaName = displaName + '【'+ aData.departmentName +'】';
            }
            $('td:eq(2)', nRow).html(displaName);
            $('td:eq(3)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            var link = Context.PATH + "/invoice/in/returntoberelation/" + aData.departmentId +"/inputinvoice.html?invoiceId="+aData.id;
            if($("#invoiceInInput").val()=="true") {
                $('td:eq(5)', nRow).html(generateOptHtml(link));
            }
        },
        columns: [
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'checkTotalAmount'},
            {defaultContent: '待关联'},
            {defaultContent: '操作'}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
});

function generateOptHtml(link) {
    var optHtml = '<div class="hidden-sm hidden-xs action-buttons">';
    optHtml += "<a href='" + link + "' title='关联'>";
    optHtml += "<i class='ace-icon fa fa-pencil bigger-130 blue'></i></a>";
    optHtml += '</div>';
    optHtml += '<div class="hidden-md hidden-lg">';
    optHtml += '<div class="inline pos-rel">';
    optHtml += '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown"';
    optHtml += 'data-position="auto">';
    optHtml += '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button>';
    optHtml += '<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
    optHtml += '<li>';
    optHtml += "<a href='" + link + "' title='关联'><i class='ace-icon fa fa-pencil bigger-130 blue'></i></a>";
    optHtml += '</li>';
    optHtml += '</ul>';
    optHtml += '</div>';
    optHtml += '</div>';
    return optHtml;
}