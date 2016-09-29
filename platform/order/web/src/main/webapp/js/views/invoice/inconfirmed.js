/**
 * Created by lcw on 8/4/2015.
 */

$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = $("#status").val();
                d.invoiceCode = $("#invoiceCode").val();
                d.sendStartTime = $("#startTime").val();
                d.sendEndTime = $("#endTime").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.id + "'>";
            var totalAmount = parseFloat(aData.totalAmount);
            var sendTime = dateToString(aData.sendTime);
            var checkDate = dateToString(aData.checkDate);
            var invoiceDate = dateToStringTODay(aData.invoiceDate);
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(1)', nRow).html(invoiceDate);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            $('td:eq(7)', nRow).html(sendTime);
            $('td:eq(9)', nRow).html(checkDate);
        },
        columns: [
            {data: 'id'},
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'sendTime'},
            {data: 'checkUserName'},
            {data: 'checkDate'},
            {defaultContent: '已确认'}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    // 全选/全不选
    $("#allCheck").click(function () {
        var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $(this).prop('checked', true);
        }

        invoiceTotal();
    });

    // 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
        }
        else {
            $(this).prop('checked', true);
        }

        // 如果全部选中，那么全选checkbox选中
        var checkCount = $("input[name='check']").length;
        var checkedCount = $("input[name='check']:checked").length;
        if (checkCount == checkedCount) {
            $("#allCheck").prop('checked', true);
        }

        invoiceTotal();
    });

    // 导出到Excel
    $("#importExcel").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择发票！");
            return;
        }
        downloadExcelForm();
    });

});

// 下载excel文件
function downloadExcelForm() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/invoice/in/importexcel.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'invoiceIdsJson');
    input1.attr('value', JSON.stringify(invoiceIds));

    $('body').append(form);
    form.append(input1);

    form.submit();
    form.remove();
}