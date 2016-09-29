/**
 * Created by dengxiyan on 2015/8/24.
 * 买家采购明细报表
 */

var dt;
var weightScale = 6;   //重量保留6位小数
var moneyScale = 2;
jQuery(function ($) {

    initTable();

    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });

    $("#output").click(function () {
        exportExcel();
    });

});

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "ajax": {
            "url": Context.PATH + "/report/business/loadbuyerorderdetail.html"
            , "type": "POST"
            , data: function (d) {
                d.sellerId = $("#sellerId").val();
                d.strStartTime = $("#startTime").val();
                d.strEndTime = $("#endTime").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'orderDateTime'},
            {data: 'orderNumber'},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'material'},
            {data: 'spec'},
            {data: 'costPrice', "sClass": "text-right"},
            {data: 'weight',name:"weight", "sClass": "text-right"},
            {data: 'actualPickWeight',name: 'actualPickWeight', "sClass": "text-right"},
            {data: 'amount',name: 'amount', "sClass": "text-right"},
            {data: 'actualPickAmount',name: 'actualPickAmount', "sClass": "text-right"}
        ]
        , columnDefs: [
            {
                "targets": 6,
                "data": "costPrice",
                "render": function (data) {
                    return formatWeight(data,moneyScale);
                }
            }
            , {
                "targets": 7,
                "data": "weight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }, {
                "targets": 8,
                "data": "actualPickWeight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }
            , {
                "targets": 9,
                "data": "amount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }, {
                "targets": 10, //第几列 从0开始
                "data": "actualPickAmount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }
        ]
        //生成footer
        , "footerCallback": function (row, data, start, end, display) {
            var api = this.api();
            $("#totalWeight").html(formatWeight(pageTotalColumnByColName(api,"weight"),weightScale));
            $("#totalActualPickWeight").html(formatWeight(pageTotalColumnByColName(api,"actualPickWeight"),weightScale));
            $("#totalAmount").html(renderMoney(pageTotalColumnByColName(api,"amount"),moneyScale));
            $("#totalActualPickAmount").html(renderMoney(pageTotalColumnByColName(api,"actualPickAmount"),moneyScale));
        }
    });
}

/**
 * 去掉格式化
 * @param i
 * @returns {number}
 */
function numberVal(i) {
    return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
}

/**
 * 统计当前页中某一列的总和，默认为0
 * @param api
 * @param colName 列名
 * @returns {*}
 */
function pageTotalColumnByColName(api, colName) {
    return api.column(colName+":name", {page: 'current'}).data().reduce(function (a, b) {
        return numberVal(a) + numberVal(b);
    }, 0);
}



function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/buyerorderdetailexcel.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'strStartTime');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'strEndTime');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'sellerId');
    input3.attr('value', $("#sellerId").val());

    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);


    form.submit();
    form.remove();
}

function formatWeight(data,scale) {
    if (data) {
        return parseFloat((data + "").replace(/[^\d\.-]/g, "")).toFixed(scale) + "";
    }
    return "0.000000";
}

function renderMoney(amount,scale) {
    if (amount) {
        return formatMoney(amount, scale);
    }
    return "0.00";
}

