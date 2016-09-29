/**
 * Created by lixiang on 2015/7/31.
 */

var dt = "";
function fillDataTable() {

    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/rebate/userdetaildata.html',
            type: "POST",
            data: function (d) {
                return $.extend({}, d, {
                    sdate: $("#startTime").val(),//起始日期
                    edate: $("#endTime").val(),//终止日期
                    accountid: $("#account_id").val(),//客户ID
                    contactid: $("#contact_id").val()//客户ID
                });
            }
        },
        searching: false,
        processing: true,
        serverSide: true,
        bLengthChange: false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var weight = formatMoney(aData.weight, 4);//实提总重（吨）
            var amount = formatMoney(aData.amount, 2);//实提总金额（元）
            var rebateAmount = formatMoney(aData.rebateAmount, 2);//金额增加（元）
            var moneyReduce = formatMoney(aData.moneyReduce, 2);//金额减少（元）
            var rebateBalance = formatMoney(aData.rebateBalance, 2);//当前余额（元）
            if (aData.type == "返利") {
                $('td:eq(3)', nRow).html(weight).addClass("text-left");
                $('td:eq(4)', nRow).html(amount).addClass("text-right");
                $('td:eq(5)', nRow).html(rebateAmount).addClass("text-right");
                $('td:eq(6)',nRow).html("--").addClass("text-right");
            }
            else
            {
                $('td:eq(2)', nRow).html("--").addClass("text-left");
                $('td:eq(3)', nRow).html("--").addClass("text-left");
                $('td:eq(4)', nRow).html("--").addClass("text-right");
                $('td:eq(5)', nRow).html("--").addClass("text-right");
                $('td:eq(6)',nRow).html(moneyReduce).addClass("text-right");
            }
            $('td:eq(7)', nRow).html(rebateBalance).addClass("text-right");
            return nRow;
        },
        columns: [
            {data: 'rebateTime'},   //时间
            {data: 'type'},  //类型	
            {data: 'code'},   //代运营交易单号
            {data: 'weight'},   //实提总重（吨）
            {data: 'amount'},  //实提总金额（元）
            {data: 'rebateAmount'},  //金额增加（元）
            {data: 'moneyReduce'},   //金额减少（元）
            {data: 'rebateBalance'}  //当前余额（元）
        ],
        oLanguage: {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
		        "sPrevious": "上一页",
		        "sNext": "下一页",
		        "sLast": "尾页"
            }
        },
        columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "rebateTime",
                "render": renderTime
            }
        ]
    });

}

function renderTime(data) {
    return date2String(new Date(data));
}
//js将Date类型转换为String类型：
function date2String(aDate) {
    var year = aDate.getFullYear();
    var month = aDate.getMonth();
    month++;
    var mydate = aDate.getDate();
    var hour = aDate.getHours();
    var minute = aDate.getMinutes();
    var mytime = year + "-" + (month < 10 ? "0" : "") + month + "-" + (mydate < 10 ? "0" : "") + mydate + " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
    return mytime;
}

jQuery(function ($) {

    fillDataTable();

    $("#searchForm").on("click", "#btn", function () {
        dt.ajax.reload();
        window.setTimeout(setTotalRow, 500);
    });
});