var dt;
var _amountLength = 2;
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
            "url": Context.PATH + "/report/finance/loadAccountFundDetail.html"
            , "type": "POST"
            , data: function (d) {
                d.accountId = $("#accountId").val();
                d.startTimeStr = $("#startTime").val();
                d.endTimeStr = $("#endTime").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'dateStr'},
            {data: 'contractCode'},
            {data: 'orderCode'},
            {data: 'serialCode'},
            {data: 'purchaseAmount', "sClass": "text-right"},
            {data: 'saleAmount', "sClass": "text-right"},
            {data: 'bankHappenAmount', "sClass": "text-right"},
            {data: 'currentBalance', "sClass": "text-right"},
            {data: 'remark'}
        ],
        columnDefs: [
            {
                sDefaultContent: '-',
                aTargets: ['_all']
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {

            $('td:eq(4)', nRow).html(renderMoney(aData.purchaseAmount));

            $('td:eq(5)', nRow).html(renderMoney(aData.saleAmount));

            $('td:eq(6)', nRow).html(renderMoney(aData.bankHappenAmount));

            $('td:eq(7)', nRow).html(renderMoney(aData.currentBalance));

            return nRow;
        }
        ,fnDrawCallback : function(setting){
            //期初余额行和期末余额行的第一列：文字加粗
            $("tr:first,tr:last","#dynamic-table tbody").find("td:first").addClass("bolder");
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
    form.attr('action', Context.PATH + "/report/finance/exportAccountFundDetail.html");

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
    input3.attr('name', 'accountId');
    input3.attr('value', $("#accountId").val());

    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);

    form.submit();
    form.remove();
}

function renderMoney(amount){
    if(amount){
        return formatMoney(amount,_amountLength);
    }
    return "-";
}

