/**
 * Created by Rabbit Mao on 2015/7/20.
 */

var dt;
function fillDataTable() {
    dt = $('#dynamicTable').DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        ajax: {
            url: Context.PATH + '/order/banktransaction/loadData.html',
            type: "POST",
            data: function (d) {
                return $.extend({}, d, {
                    payeeBankName:  $("#spayee_bank_name").children('option:selected').val(),
                    transactionAmount: $("#stransaction_amount").val(),
                    statusToSelect:  $("#sstatus").children('option:selected').val(),
                    payeeName: $("#spayee_name").val(),
                    bankType:  $("#bank_type").children('option:selected').val(),
                    stransactionTimeFrom: $("#stransaction_timefrom").val(),
                    stransactionTimeTo: $("#stransaction_timeto").val()
                });
            }
        },

        bAutoWidth:false,
        searching: false,
        processing: true,
        serverSide: true,
        ordering: false,
        bLengthChange: true,
        "scrollY": "400px",
        "scrollCollapse": "true",
        "iDisplayLength": 50,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	$('td:eq(4)', nRow).addClass("text-right");
            //0已关联 1待操作 2线下付款 3注册并充值
            var processPermission = $("#processPermission").val();
            if(processPermission == "true") {
                var html;
                if (aData.status == 'normal') {
                    html = "";
                } else if (aData.status == 'unprocessed') {
                    html = '<a href="deal.html?id=' + aData.id + '" class="blue">处理</a>';
                } else if (aData.status == 'refund') {
                    html = '<a href="refund.html?serialNumber=' + aData.serialNumber + '"class="blue">查看退款申请单</a>';
                } else if (aData.status == 'charge') {
                    html = "--";
                }
                $('td:eq(-1)', nRow).html(html);
            }
            return nRow;
        },
        columns: [
            {data: 'serialNumber'},  //银行流水
            {data: 'payeeBankName'},  //付款银行
            {data: 'payeeName'},  //客户名称
            {data: 'payeeAcctountNumber'},  //银行账号
            {data: 'transactionAmount'},   //到账金额
            {data: 'bankTypeName'},   //到账银行
            {data: 'transactionTime'},    //到账时间
            {defaultContent: '转账'},   //状态
            {data: 'note'},    //备注
            {data: 'status'},   //状态
            {defaultContent: ''}  //操作
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "前页",
                "sNext": "后页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
            {
                "targets": 4, //第几列 从0开始
                "data": ",transactionAmount",
                "render": renderAmount
            },
            {
                "targets": 6, //第几列 从0开始
                "data": "transactionTime",
                "render": renderTime
            },
            {
                "targets": 9, //第几列 从0开始
                "data": "status",
                "render": getBankTransactionType
            }
        ]
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return "<span class='bolder red'>" + formatMoney(data, 2) + "</span>";
    }
    return "";
}

function getBankTransactionType(data, type, full, meta){
    var status = '--';
    if(data == 'normal') status='<span>正常</span>';
    else if(data == 'unprocessed') status='<span>待处理</span>';
    else if(data == 'refund') status='<span>已处理<br/>线下付款 </span>';
    else if(data == 'charge') status='<span>已处理<br/>注册并充值 </span>';
    return status;
};


function renderTime(data, type, full, meta){
    return date2String(new Date(data));
}
//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}

jQuery(function($) {

    fillDataTable();

    $("#searchForm").on("click","#search", function() {
        dt.ajax.reload();
    });
});