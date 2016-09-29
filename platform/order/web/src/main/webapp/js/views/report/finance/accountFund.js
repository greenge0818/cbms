var dt;
var _moneyScale=2;
jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });

    $("#output").click(function () {
        exportExcel();
    });

});

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true,
        "aLengthMenu": [50, 100, 150],
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "ajax": {
            "url": Context.PATH + "/report/finance/loadAccountFund.html"
            , "type": "POST"
            , data: function (d) {
                d.accountName = $.trim($("#accountName").val());
                d.startTimeStr = $("#startTime").val();
                d.endTimeStr = $("#endTime").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'accountName'},
            {data: 'initialBalance', "sClass": "text-right"},
            {data: 'saleAmount', "sClass": "text-right"},
            {data: 'receivedAmount', "sClass": "text-right"},
            {data: 'purchaseAmount', "sClass": "text-right"},
            {data: 'payedAmount', "sClass": "text-right"},
            {data: 'endingBalance', "sClass": "text-right"},
            {defalutvalue: ''}
        ]
        ,columnDefs: [
            {
                "targets": 1,
                "data": "initialBalance",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            }
            , {
                "targets": 2,
                "data": "saleAmount",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            }, {
                "targets": 3,
                "data": "receivedAmount",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            }
            , {
                "targets":4,
                "data": "purchaseAmount",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            }, {
                "targets": 5, //第几列 从0开始
                "data": "payedAmount",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            },
            {
                "targets": 6, //第几列 从0开始
                "data": "endingBalance",
                "render": function (data) {
                    return renderMoney(data,_moneyScale);
                }
            },
            {
                sDefaultContent: '-', //默认内容为空
                aTargets: [ '_all' ]  //所有列
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var url = Context.PATH + "/report/finance/accountFundDetail.html?startTimeStr="+$("#startTime").val()
                       +"&endTimeStr="+$("#endTime").val()+"&accountId="+aData.accountId;
            var operationHtml = "<a href='"+url+"' class='blue'>查看明细</a>";
            var accountNameHtml = "<a href='"+url+"' class='blue'>"+aData.accountName+"</a>";
            $("td:eq(0)",nRow).html(accountNameHtml);
            $("td:last",nRow).html(operationHtml);
        }
        ,"scrollY": $(document.body).height() -10,
        "scrollX":true
    });
}


function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/finance/exportAccountFund.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'startTimeStr');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'endTimeStr');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'accountName');
    input3.attr('value', $.trim($("#accountName").val()));

    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);

    form.submit();
    form.remove();
}

function renderMoney(amount,scale) {
    if (amount) {
        return formatMoney(amount, scale);
    }
    return "-";
}
