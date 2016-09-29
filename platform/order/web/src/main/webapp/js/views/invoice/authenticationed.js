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
            var totalAmount = parseFloat(aData.totalAmount);
            var sendTime = dateToString(aData.sendTime);
            var checkDate = dateToString(aData.checkDate);
            var invoiceDate = dateToStringTODay(aData.invoiceDate);
            if(aData.invoiceOut){
            	$('td:eq(0)', nRow).html("<label class='pos-rel'><input type='checkbox' class='ace' name='check' value='' disabled='disabled' title='该进项票已开销项票，不能取消认证'/><span class='lbl'></span></label>");
            }else{
            	var inputHtml = "<label class='pos-rel'><input type='checkbox' class='ace' name='check' value='" + aData.id + "' /><span class='lbl'></span></label>";
            	$('td:eq(0)', nRow).html(inputHtml);
            }
            $('td:eq(1)', nRow).html(invoiceDate);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            $('td:eq(7)', nRow).html(sendTime);
            $('td:eq(9)', nRow).html(checkDate);
            if(aData.checkTotalAmount && aData.checkTotalAmount > 0){
            	$('td:eq(4)', nRow).html(formatMoney(aData.checkTotalAmount,2));
            }
            var link = Context.PATH + '/invoice/in/'+aData.id+'/certificatedetails.html';
            $('td:last', nRow).html("<a href='" +link +"' class='blue'>查看关联</a>");;
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
            {defaultContent: '已认证'},
            {defaultContent: ''}
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
            $("input[name='check']").not("[disabled]").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").not("[disabled]").prop('checked', true);
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

    // 取消认证发票
    $("#cancel").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择发票！");
            return;
        }
        outauthentication();
    });

});

//取消认证发票
function outauthentication() {
	var idsJson = {
		invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/outauthentication.html",
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
                	cbms.alert("取消认证成功",function(){
                		location.reload(true);
                	});
                }
                else {
                    var msg = "操作失败，请联系系统管理员";
                    if(result.message){
                        msg = result.message;
                    }
                    cbms.alert(msg);
                    if(result.data)
                    {
                        for (var i = 0; i < result.data.length; i++) {
                            var label = $("#dynamic-table input[value='" + result.data[i] + "']").parent();
                            if(label.find(".bg-circle").length<=0)
                            {
                                label.append("<span class='bg-circle'></span>");
                            }
                        }
                    }
                }
            } else {
                cbms.alert("取消认证失败");
            }
        }
    });
}

