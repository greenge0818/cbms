/**
 * Created by dq on 9/9/2015.
 */

$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = $("#status").val();
                d.invoiceCode = $("#invoiceCode").val();
                d.sendStartTime = $("#startTime").val();
                d.sendEndTime = $("#endTime").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.id + "'>";
            var totalAmount = parseFloat(aData.totalAmount);
            var sendTime = dateToString(aData.sendTime);
            var checkDate = dateToString(aData.checkDate);
            var invoiceDate = dateToStringTODay(aData.invoiceDate);
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(1)', nRow).html(invoiceDate);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            $('td:eq(7)', nRow).html(sendTime);
            $('td:eq(9)', nRow).html(checkDate);
            if(aData.checkTotalAmount && aData.checkTotalAmount > 0){
            	$('td:eq(4)', nRow).html(formatMoney(aData.checkTotalAmount,2));
            }
        },
        columns: [
            {data: 'id'},
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'sendTime'},
            {data: 'checkUserName'},
            {data: 'checkDate'},
            {defaultContent: '已作废'}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

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

        invoiceTotal();
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

        invoiceTotal();
    });

    // 取消作废发票
    $("#cancel").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择发票！");
            return;
        }
        cbms.confirm("确认取消作废所选中的发票？", null, function () {
            outcancel();
        });
    });
    
    // 删除作废发票
    $("#delete").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择发票！");
            return;
        }
        cbms.confirm("确认删除作废所选中的发票？", null, function () {
            deleteinvoice();
        });
    });

});

//取消作废发票
function outcancel() {
	var idsJson = {
		invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/outcancel.html",
        data: {
        	idsJson: JSON.stringify(idsJson)
        },
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                	cbms.alert("操作成功",function(){
                		location.reload(true);
                	});
                }
                else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("取消作废失败");
            }
        }
    });
}

//删除作废发票
function deleteinvoice() {
	var idsJson = {
		invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/deleteinvoice.html",
        data: {
        	idsJson: JSON.stringify(idsJson)
        },
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                	cbms.alert("操作成功",function(){
                		location.reload(true);
                	});
                }
                else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("删除作废发票失败");
            }
        }
    });
}

