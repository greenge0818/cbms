/**
 * Created by dq on 9/9/2015.
 */

$(document).ready(function () {
    window.currentDataTable = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = $("#status").val();
                d.relationStatus = "hasrelation";
                d.invoiceCode = $("#invoiceCode").val();
                d.sendStartTime = $("#startTime").val();
                d.sendEndTime = $("#endTime").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var inputHtml = "<label class='pos-rel'><input type='checkbox' class='ace' name='check' value='" + aData.id + "'><span class='lbl'></span></label>";
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
         // operation button
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
            {defaultContent: '待认证'},
            {defaultContent: ''}
        ]
    });

    $("#queryBtn").on("click", function () {
    	window.currentDataTable.fnDraw();
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

    // 通过认证
    $("#pass").click(function () {
        if(checkSelect()){
        	cbms.confirm("确定认证通过选中的发票吗",null,function(){
        		inauthentication("pass");
        	});
        }
    });
    
	// 未通过认证
    $("#refuse").click(function () {
    	if(checkSelect()){
        	cbms.confirm("确定作废掉选中的发票吗",null,function(){
        		inauthentication("refuse");
        	});
        }
    });
    
    $("#roollback").click(function(){
    	if(checkSelect()){
        	cbms.confirm("确定打回选中的发票重新关联吗",null,function(){
        		inauthentication("rollback");
        	});
        }
    });
});

function checkSelect(){
	if (invoiceIds == null || invoiceIds.length == 0) {
        cbms.alert("请选择发票！");
        return false;
    }
	return true;
}

// 进行认证发票
function inauthentication(flag) {
	var paramsJson = {
		flag: flag,
        invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/inauthentication.html",
        data: {
        	paramsJson: JSON.stringify(paramsJson)
        },
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                	location.reload(true);
                	/*$("#queryBtn").click();
                	var tabTxt = $(".tabbar:first li.active:first .red:first");
                	var count = parseInt(tabTxt.text(),10);
                	if(count && count > 0){
                		count -= invoiceIds.length;
                		tabTxt.text(count+" ");
                	}*/
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
                cbms.alert("认证失败");
            }
        }
    });
}

