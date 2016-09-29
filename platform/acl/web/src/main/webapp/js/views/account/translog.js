/**
 * Created by kongbinheng on 2015/7/16.
 */
$().ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "pageLength": 100,  //每页记录数
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/account/transloginfo.html',
            type: 'POST',
            data: function (d) {
                d.accountId = parseInt($("#accountId").val());
                d.consignOrderCode = $("#code").val();
                d.applyType = $("#type").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var dt = new Date(aData.serialTime);
            $('td:eq(0)', nRow).html(dateFormat(dt, "yyyy-MM-dd hh:mm:ss"));
            var associationTypeText = getAssociationTypeText(parseInt(aData.associationType))
            $('td:eq(1)', nRow).html(associationTypeText);
            var applyTypeText = getApplyTypeText(parseInt(aData.applyType));
            $('td:eq(3)', nRow).html(applyTypeText);
            var cashHappenBalance = parseFloat(aData.cashHappenBalance);
            $('td:eq(4)', nRow).addClass("text-right").html(formatMoney((cashHappenBalance), 2)).addClass("text-right");
            var cashCurrentBalance = parseFloat(aData.cashCurrentBalance);
            $('td:eq(5)', nRow).addClass("text-right").html(formatMoney((cashCurrentBalance), 2)).addClass("text-right");
            var amount = parseFloat(aData.amount);
            $('td:eq(6)', nRow).addClass("text-right").html(formatMoney((amount), 2)).addClass("text-right");
            var currentBalance = parseFloat(aData.currentBalance);
            $('td:eq(7)', nRow).addClass("text-right").html(formatMoney((currentBalance), 2)).addClass("text-right");
        },
        columns: [
            {data: 'serialTime'},
            {data: 'associationType'},
            {data: 'consignOrderCode'},
            {data: 'applyTypeText'},
            {data: 'cashHappenBalance'},
            {data: 'cashCurrentBalance'},
            {data: 'amount'},
            {data: 'currentBalance'},
            {data: 'applyerName'}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });

    // 搜索
    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
    // 导出EXCEL
    $("#exportBtn").on("click", function () {
        exportExcel();
    });
});

// 导出EXCEL
function exportExcel(){
     var form = $("<form>");
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', Context.PATH + "/account/translog/exportexcel.html");
        // 客户ID
        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'accountId');
        input1.attr('value', $("#accountId").val());
        // 流水开始时间
        var input2 = $('<input>');
        input2.attr('type', 'hidden');
        input2.attr('name', 'startTime');
        input2.attr('value', $("#startTime").val());
        // 流水结束时间
        var input3 = $('<input>');
        input3.attr('type', 'hidden');
        input3.attr('name', 'endTime');
        input3.attr('value', $("#endTime").val());
        // 关联单号
        var input4 = $('<input>');
        input4.attr('type', 'hidden');
        input4.attr('name', 'consignOrderCode');
        input4.attr('value', $("#code").val());
        // 类型
        var input5 = $('<input>');
        input5.attr('type', 'hidden');
        input5.attr('name', 'applyType');
        input5.attr('value', $("#type").val());

        $('body').append(form);
        form.append(input1);
        form.append(input2);
        form.append(input3);
        form.append(input4);
        form.append(input5);

        form.submit();
        form.remove();
}

//获取类型
function getApplyTypeText(applyType){
    var applyTypeText = "";
    switch(applyType){
        case 1:
            applyTypeText = "充值到资金账户";
            break;
        case 2:
            applyTypeText = "支付合同货款";
            break;
        case 3:
            applyTypeText = "二次结算";
            break;
        case 4:
            applyTypeText = "资金账户转出";
            break;
        case 5:
            applyTypeText = "抵扣二次结算账户欠款";
            break;
        case 6:
            applyTypeText = "二次结算账户余额转入资金账户";
            break;
        case 7:
            applyTypeText = "抵扣二次结算账户欠款";
            break;
        case 8:
            applyTypeText = "合同款到账";
            break;
        case 9:
            applyTypeText = "二次结算账户余额转入资金账户";
            break;
        case 10:
            applyTypeText = "锁定二次结算账户余额";
            break;
        case 11:
            applyTypeText = "解锁二次结算账户余额";
            break;
        case 12:
            applyTypeText = "锁定资金账户余额";
            break;
        case 13:
            applyTypeText = "解锁资金账户余额";
            break;
        case 14:
            applyTypeText = "二次结算回退";
            break;
        case 15:
            applyTypeText = "资金账户回退";
            break;
        case 16:
            applyTypeText = "银票充值到资金账户";
            break;
        case 17:
            applyTypeText = "取消已充值到资金账户的银票";
            break;
        case 18:
            applyTypeText = "锁定资金账户余额";
            break;
        case 19:
            applyTypeText = "解锁资金账户余额";
            break;
        case 20:
            applyTypeText = "二次结算(折让金额)";
            break;
        case 21:
            applyTypeText = "二次结算(折让金额回滚)";
            break;
        default:
            applyTypeText = "系统平账处理";
    }
    return applyTypeText;
}

//获取关联类型
function getAssociationTypeText(associationType){
    var associationTypeText = "";
    switch(associationType){
        case 1:
            associationTypeText = "付款单号";
            break;
        case 2:
            associationTypeText = "交易单号";
            break;
        case 3:
            associationTypeText = "银行流水号";
            break;
        case 4:
            associationTypeText = "还款流水号";
            break;
        case 5:
            associationTypeText = "银票票号";
            break;
        case 6:
            associationTypeText = "折让单号";
            break;
        default:
            associationTypeText = "交易单号";
    }
    return associationTypeText;
}

function dateFormat(date, fmt) {
    var o = {
        "M+" : date.getMonth()+1,                 //月份
        "d+" : date.getDate(),                    //日
        "h+" : date.getHours(),                   //小时
        "m+" : date.getMinutes(),                 //分
        "s+" : date.getSeconds(),                 //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S"  : date.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}