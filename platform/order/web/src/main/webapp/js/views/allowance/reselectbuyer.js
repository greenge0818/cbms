/**
 * 重新选择买家交易单（审核不通过后）
 * Created by lcw on 2015/11/20.
 */
var _buyerDt;
var _orderIds;           // 发票ID集合

$().ready(function () {
    _buyerDt = $('#list-table').dataTable();
    $("#createForm").verifyForm();

    //搜索
    $(document).on("click", "#btnSearch", function () {
        if (!setlistensSave())return;
        if ($('#buyer').attr("accountid") == 0) {
            cbms.alert("请选择买家名称！");
            return;
        }
        _buyerDt.fnDestroy();
        _orderIds = null;
        searchTable();
    });
    $("#btnSearch").click();

    // 全选/全不选
    $("#allCheck").click(function () {
        var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $(this).prop('checked', true);
        }
        orderTotal();
    });

    // 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
        }
        else {
            $(this).prop('checked', true);
        }

        // 如果全部选中，那么全选checkbox选中
        var checkCount = $("input[name='check']").length;
        var checkedCount = $("input[name='check']:checked").length;
        if (checkCount == checkedCount) {
            $("#allCheck").prop('checked', true);
        }
        orderTotal();
    });

    // 下一步
    $("#allowanceNext").click(function () {
        if (_orderIds == null || _orderIds.length == 0) {
            cbms.alert("请选择订单！");
            return;
        }
        allowanceNext();
    });
});

// 查找订单信息
function searchTable() {
    _buyerDt = $('#list-table').dataTable({
        bAutoWidth: false,
        searching: false,
        bPaginate: false,
        paging: false,
        ordering: false,
        ajax: {
            url: Context.PATH + '/allowance/reselectbuyerdata.html',
            type: 'POST',
            data: {
                id: $('#allowanceId').val(),
                contractCode: $('#contractCode').val(),
                buyerId: $('#buyer').attr("accountid"),
                orderCode: $('#orderCode').val(),
                startTime: $('#startTime').val(),
                endTime: $('#endTime').val(),
                allowanceType: 'buyer'
            }
        },
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.orderId + "'>";
            $('td:eq(0)', nRow).html(inputHtml);
            var dt = new Date(aData.orderTime);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            $('td:eq(1)', nRow).html(time);
        },
        columns: [
            {data: ''},
            {data: 'orderTime'},
            {data: 'contractCode'},
            {data: 'orderCode'},
            {data: 'buyerName'},
            {data: 'totalQuantity'},
            {data: 'totalWeight'},
            {data: 'totalAmount'},
            {data: 'totalActualWeight'},
            {data: 'totalActualAmount'}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });
}

//统计选中的订单
function orderTotal() {
    _orderIds = [];
    var checked = $("input[name='check']:checked").not("#allCheck");
    $(checked).each(function () {
        var id = $(this).val();
        _orderIds.push(id);
    });
    var checkedCount = checked.length;
    $('#checkCount').text(checkedCount);
}

//下一步
function allowanceNext() {
    window.location.href = Context.PATH +
    "/allowance/" + $("#allowanceId").val() + "/editbuyer.html?orderIds=" + _orderIds +
    "&allowanceType=buyer" +
    "&buyerId=" + $('#buyer').attr("accountid");
}


