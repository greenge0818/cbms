var dt;

$(document).ready(function(){
    //列表
    initTable();
    //操作
    initClickEvent();
});

// 待申请开票列表
function initTable(){
    dt = $('#list-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/apply/invoicing.html',
            type: 'POST',
            data: {
            	status: 'PENDING_APPROVAL',
                startTime: $('#startTime').val(),
                endTime: $('#endTime').val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            renderOperation(nRow, aData, iDataIndex);
            $('td:eq(0)', nRow).addClass("none");
            $('td:eq(1)', nRow).addClass("none");
            $('td:eq(2)', nRow).addClass("none");
            // 
            var dt = new Date(aData.created);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            $('td:eq(3)', nRow).html(time);
            var amount = parseFloat(aData.amount);
            $('td:eq(5)', nRow).addClass("text-right").html(formatMoney((amount), 2)).addClass("text-right");
        },
        columns: [
            {data: 'id'},
            {data: 'orgId'},
            {data: 'status'},
            {data: 'created'},
            {data: 'orgName'},
            {data: 'amount'},
            {data: 'statusName'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });
}

// 操作
function renderOperation(nRow, aData, iDataIndex){
    var html = '<div class="hiddenh-sm hidden-xs action-buttons">';
	html += '<button type="button" name="approve" class="btn btn-xs btn-info approve-inv">审核</button>';
    html += '</div>';
    $('td:eq(-1)', nRow).html(html).addClass("text-center");
}

// 按钮
function initClickEvent(){
    // 审核
    $('#list-table').on("click", "button[name='approve']", function (){
        var currentRow = $(this).closest('tr');// 当前行
        var outApplyId = currentRow.find("td:eq(0)").text();
        var status = 'PENDING_APPROVAL';
        window.location.href = Context.PATH + '/invoice/apply/auditdetail.html?outApplyId='+outApplyId+'&status='+status;
    });

    // 搜索
    $(document).on("click", "#btnSearch", function(){
    	dt.fnDestroy();
        initTable();
    });

}

