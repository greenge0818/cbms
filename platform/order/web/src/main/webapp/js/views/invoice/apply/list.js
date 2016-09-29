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
            	status: 'PENDING_SUBMIT',
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
    	
    if($('td:eq(2)', nRow).text() != 'APPROVED') {
		if($("#allowShow").val()=='true'){
	        html += '<button type="button" name="view" class="btn btn-xs btn-info view-inv">查看</button>';
	    }
		if($("#alloDelete").val()=='true') {
			html += '<button type="button" name="delete" class="btn btn-xs btn-info del-inv">删除</button>';
		}
	}
    html += '</div>';
    $('td:eq(-1)', nRow).html(html).addClass("text-center");
}

// 按钮
function initClickEvent(){
    // 查看
    $('#list-table').on("click", "button[name='view']", function (){
        var currentRow = $(this).closest('tr');// 当前行
        var outApplyId = currentRow.find("td:eq(0)").text();
        var status = currentRow.find("td:eq(2)").text();
        window.location.href = Context.PATH + '/invoice/apply/undetail.html?outApplyId='+outApplyId+'&status='+status;
    });

    // 删除
    $('#list-table').on("click", "button[name='delete']", function (){
        var currentRow = $(this).closest('tr');// 当前行
        var outApplyId = currentRow.find("td:eq(0)").text();
        cbms.confirm("确定要删除该开票申请吗？", null, function () {
            cbms.loading();
            $.ajax({
                type: 'POST',
                url: Context.PATH + "/invoice/apply/delete.html",
                data: {"outApplyId" : outApplyId},
                error: function (s) {
                    cbms.closeLoading();
                },
                success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.alert("删除该开票申请成功",function(){
                                window.location.href = Context.PATH + "/invoice/apply/index.html";
                            });
                        } else {
                        	cbms.alert(result.data,function(){
                                window.location.href = Context.PATH + "/invoice/apply/index.html";
                            });
                        }
                    } else {
                        cbms.alert("删除该开票申请失败");
                    }
                }
            });
        });
    });


    // 新增开票申请
    $(document).on("click", "#btnAdd", function(){
    	
        window.location.href = Context.PATH + '/invoice/apply/detail.html';
    });

    // 搜索
    $(document).on("click", "#btnSearch", function(){
    	dt.fnDestroy();
        initTable();
    });

}
