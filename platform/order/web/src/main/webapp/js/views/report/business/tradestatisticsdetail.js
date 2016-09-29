/**
 * Created by caochao on 2015/8/26.
 * 买家交易报表
 */

var dt;

jQuery(function ($) {
    $("#formsearch").verifyForm();

    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        if (!setlistensSave()) return;
        dt.ajax.reload();
    });
    $("#exportexcel").click(function () {
        if (!setlistensSave()) return;
        exportToExcel();
    });
});

function initTable() {
    var url = Context.PATH + "/report/business/buyerstatisticsdata.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
                d.orgid = $("#sorganizationHidden").val();
                d.uid = $("#traderName").attr("userid");
                d.buyerid = $("#accountid").attr("accountid");
                d.sdate = $("#startTime").val();
                d.edate = $("#endTime").val();
            }
        },
        columns: [
            {data: 'companyName'},
            {data: 'contactName'},
            {data: 'orderCount'},
            {data: 'tempOrderCount'},
            {data: 'contactOrderCount'},
            {data: 'frequencyOfContact'},
            {data: 'totalOrderCount'},
            {data: 'frequencyOfCompany'},
            {data: 'firstTradeTime'},
            {data: 'lastestTradeTime'}
        ],
        columnDefs: [
            {
                "targets": 5, //第几列 从0开始
                "data": "firstTradeTime",
                "render": function (data, type, full, meta) {
                    return parseFloat(data).toFixed(2);
                }
            },
            {
                "targets": 7, //第几列 从0开始
                "data": "firstTradeTime",
                "render": function (data, type, full, meta) {
                    return parseFloat(data).toFixed(2);
                }
            },
            {
                "targets": 8, //第几列 从0开始
                "data": "firstTradeTime",
                "render": function (data, type, full, meta) {
                    if (data == null)
                        return "-";
                    else
                        return dateFormat(new Date(data), "yyyy-MM-dd");
                }
            },
            {
                "targets": 9, //第几列 从0开始
                "data": "lastestTradeTime",
                "render": function (data, type, full, meta) {
                    if (data == null)
                        return "-";
                    else
                        return dateFormat(new Date(data), "yyyy-MM-dd");
                }
            }
        ],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        bFilter: false,
        iDisplayLength: 10,
        bLengthChange: false
    });
}

function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/buyerstatisticstoexcel.html");

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

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'uid');
    input4.attr('value', $("#traderName").attr("userid"));

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'buyerid');
    input5.attr('value', $("#accountid").attr("accountid"));


    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);

    form.submit();
    form.remove();
}

