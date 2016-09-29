/**
 * Created by Rabbit Mao on 2015/7/20.
 */

var dt;
function fillDataTable() {
    dt = $('#forShow').DataTable({
        lengthChange: false, //显示pageSize的下拉框
        //pageLength: 10,
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        ajax: {
            url: Context.PATH + '/report/business/sellerStatistics/list.html',
            type: "POST",
            data: function (d) {
                return $.extend({}, d, {
                    organization: $("#sorganizationHidden").val(),
                    ownerName: $("#ownerName").val(),
                    accountName: $("#accountName").val(),
                    timeFrom: $("#timeFrom").val(),
                    timeTo: $("#timeTo").val()
                });
            }
        },
        columns: [
            {data: 'sellerName'},
            {data: 'ownerName'},
            {defaultContent: ""},
            {defaultContent: "", "sClass": "text-right"},
            {defaultContent: "", "sClass": "text-right"},
            {defaultContent: "", "sClass": "text-right"},   //当月交易重量占比
            {data: 'dealCount', "sClass": "text-right"},   //当月交易总笔数
            {defaultContent: "", "sClass": "text-right"},
            //{data: 'publishCount', "sClass": "text-right"},
            {defaultContent: ""}
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var consignType = renderType(aData.consignType);
            var weightRange = renderWeight(aData.weightRange);
            var amountRange =aData.amountRange?formatMoney(aData.amountRange,2):"0.00";
            var weightAll = renderWeight(aData.weightAll);
            var sellerId = renderInput(aData.sellerId);
            var weightPercent = (aData.weightRange/aData.weightAll*100).toFixed(2) +"%";

            $('td:eq(2)', nRow).html(consignType);
            $('td:eq(3)', nRow).html(weightRange);
            $('td:eq(4)', nRow).html(amountRange);
            $('td:eq(5)', nRow).html(weightPercent);
            $('td:eq(7)', nRow).html(weightAll);
            $('td:eq(8)', nRow).html(sellerId);
            return nRow;
        }
    });
}

// 下载excel文件
function downloadExcelForm() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/sellerStatistics/output.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'accountName');
    input1.attr('value', $("#accountName").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'timeFrom');
    input2.attr('value', $("#timeFrom").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'timeTo');
    input3.attr('value', $("#timeTo").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'ownerName');
    input4.attr('value', $("#ownerName").val());

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'organization');
    input5.attr('value', $("#sorganizationHidden").val());

    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);

    form.submit();
    form.remove();
}

function renderInput(data){
    var timeFrom = $("#timeFrom").val();
    var timeTo =  $("#timeTo").val();
    return '<a class="button btn-sm btn-info" href="' + Context.PATH + '/report/business/buyerdetail.html?' +
        'accountId='+data+'&strStartTime='+timeFrom+'&strEndTime='+timeTo+'">查看</a>';
}

jQuery(function ($) {
    fillDataTable();

    $(document).on("click", "#search", function () {
        dt.ajax.reload();
    });

    $(document).on("click", "#output", function () {
        downloadExcelForm();
    });

    $("#time").change(function(){
        var timeSelect = $("#time").children('option:selected').val();    //选中的月份
        var now = new Date();
        var nowString = now.getFullYear() + ((now.getMonth()+1)<10?"0"+ (now.getMonth()+1): (now.getMonth()+1));
        var year = timeSelect.substring(0, 4);
        var month = parseInt(timeSelect.substring(4, 6));

        var timeFrom = new Date(year, month-1, 1);   //被选中当月第一天
        $("#timeFrom").val(timeFrom.getFullYear() + "-" + (timeFrom.getMonth()+1) + "-" + timeFrom.getDate());
        if(timeSelect == nowString){
            $("#timeTo").val(now.getFullYear() + "-" + (now.getMonth()+1) + "-" + now.getDate());   //如果被选中的月份是当前月份，结束时间为今天
        }else{
            var temp = new Date(year, month, 0);   //被选中当月最后一天
            $("#timeTo").val(temp.getFullYear() + "-" + (temp.getMonth()+1) + "-" + temp.getDate());
        }
    });
});
