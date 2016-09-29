var dt;
function fillDataTable() {
    dt = $('#dynamicTable').DataTable({
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
        searching: false,
        processing: true,
        serverSide: true,
        ordering: false,
        bLengthChange: true,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	$('td:eq(4)', nRow).addClass("text-right");
            var payerrorProcess = $("#payerrorProcess").val();
            if(payerrorProcess == "true") {
                var html;
                if (aData.status == 'pending') {
                    html = '<a href="errordeal.html?id=' + aData.id + '" class="blue">处理</a>';
                } else {
                    html = "";
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
            },
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
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
    if(data == 'pending') status='<span>待处理</span>';
    else if(data == 'chargeman') status='<span>已处理<br/>人工处理</span>';
    else if(data == 'chargehand') status='<span>已处理<br/>手动充值 </span>';
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