/**
 * Created by caochao on 2015/9/14
 */

jQuery(function ($) {
    fillDataTable();

    $("#btnsearch").on("click", function () {
        dt.ajax.reload();
    });

    $("#exportexcel").on("click", function () {
        exportToExcel();
    });
});

var dt;
function fillDataTable() {
    dt = $('#dynamic-table').DataTable({
        lengthChange: true, //显示pageSize的下拉框
        pageLength: 10,
        serverSide: true, //服务模式
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        ajax: {
            url: Context.PATH + '/report/finance/invoiceoutexpectdata.html',
            type: "POST",
            data: function (d) {
                if ($("#sorganizationHidden").length > 0) {
                    d.orgid = $("#sorganizationHidden").val();
                }
                d.buyerid = $("#accountid").attr("accountid");
                d.sdate = $("#startTime").val();
                d.edate = $("#endTime").val();
            }
        },
        columns: [
            {data: "invoiceOutMainId"},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'material'},
            {data: 'weight'},
            {data: 'price'},
            {data: 'noTaxAmount'},
            {data: 'taxAmount'},
            {data: 'amount'},
            {data: 'code'}
        ]
    })
    ;
}
// 下载excel文件
function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/finance/invoiceoutexpecttoexcel.html");
    $('body').append(form);

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'sdate');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'edate');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'orgid');
    input3.attr('value', $("#sorganizationHidden").val());

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'buyerid');
    input5.attr('value', $("#accountid").attr("accountid"));

    var input6 = $('<input>');
    input6.attr('type', 'hidden');
    input6.attr('name', 'status');
    input6.attr('value', $("#order_status").val());

    form.append(input1)
    form.append(input2);
    form.append(input3);
    form.append(input5);

    form.submit();
    form.remove();
}