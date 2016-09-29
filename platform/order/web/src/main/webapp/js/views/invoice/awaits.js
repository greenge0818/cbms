/**
 * Created by lcw on 2015/8/1.
 */
var _weightFixedLength = 6;
var _amountFixedLength = 2;
$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchbycompany.html',
            type: 'POST',
            data: function (d) {
                d.ownerName = $("#owner").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var amount = aData.totalAmount.toFixed(_amountFixedLength);
            var weight = aData.totalWeight.toFixed(_weightFixedLength);
            var displaName = aData.sellerName;
            if(aData.totalDepartment > 1 && aData.departmentName != null){
            	displaName = displaName + '【'+ aData.departmentName +'】';
            }
            $('td:eq(0)', nRow).html(displaName);
            $('td:eq(1)', nRow).html(weight).addClass("text-right");
            $('td:eq(2)', nRow).html(formatMoney(amount)).addClass("text-right");
            $('td:eq(3)', nRow).html(generateOptHtml(aData.departmentId));
        },
        columns: [
            {data: 'sellerName'},
            {data: 'totalWeight'},
            {data: 'totalAmount'},
            {data: 'sellerName'}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
});

function generateOptHtml(departmentId) {
    var invoiceInInput = $("#invoiceInInput").val();
    var optHtml = '<div class="hidden-sm hidden-xs action-buttons">';
    optHtml += "<a href='" + Context.PATH + "/invoice/in/" + departmentId + "/detailbyseller.html' title='查看详情'>";
    optHtml += "<i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    if (invoiceInInput == "true") {
        optHtml += "<a href='" + Context.PATH + "/invoice/in/awaits/" + departmentId + "/inputinvoice.html' title='录入发票信息'>";
        optHtml += "<i class='ace-icon fa fa-pencil bigger-130'></i></a>";
    }
    optHtml += '</div>';
    optHtml += '<div class="hidden-md hidden-lg">';
    optHtml += '<div class="inline pos-rel">';
    optHtml += '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown"';
    optHtml += 'data-position="auto">';
    optHtml += '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button>';
    optHtml += '<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
    optHtml += '<li>';
    optHtml += "<a href='" + Context.PATH + "/invoice/in/" + departmentId + "/detailbyseller.html' title='查看详情'>";
    optHtml += "<i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</li>';
    if (invoiceInInput == "true") {
        optHtml += '<li>';
        optHtml += "<a href='" + Context.PATH + "/invoice/in/awaits/" + departmentId + "/inputinvoice.html' title='录入发票信息'>";
        optHtml += "<i class='ace-icon fa fa-pencil bigger-130'></i></a>";
        optHtml += '</li>';
    }
    optHtml += '</ul>';
    optHtml += '</div>';
    optHtml += '</div>';
    return optHtml;
}