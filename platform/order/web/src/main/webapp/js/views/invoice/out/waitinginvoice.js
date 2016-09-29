$(document).ready(function () {
    var table;
    table = $('#waiting-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/out/querywaitinginvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = 'APPROVED';
                var orgIds = [];
            	$("#orgSelect li").each(function(){
            		var li = $(this);
            		var checkbox = li.find("input[type='checkbox']");
            		if(checkbox.prop('checked')){
            			var orgId = checkbox.val();
            			if(orgId!='all'){
            				orgIds.push(orgId);
            			}
            		}
            	});
            	if(orgIds && orgIds.length>0){
            		if(d) d.orgIds = orgIds.toString();
            	}
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            //aData.actualAmount
            var inputHtml = "<label class='pos-rel'><input type='checkbox' class='ace' name='check' value='" + aData.id + "'><span class='lbl'></span></label>";
            var dt = new Date(aData.created);
            var applyTime = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            var amount = parseFloat(aData.amount);
            var actualAmount = parseFloat(aData.actualAmount);//
            var statusName = amount.sub(actualAmount) > 0 && actualAmount > 0 ? "部分生成开票清单" : "待生成开票清单";
            var link = "<a href='" + Context.PATH + "/invoice/out/generate.html?ids=" + aData.id + "'>生成开票清单</a>";
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(1)', nRow).html(applyTime);
            $('td:eq(3)', nRow).html(formatMoney(amount, 2)).addClass("text-right");
            $('td:eq(4)', nRow).html(formatMoney((amount.sub(actualAmount)), 2)).addClass("text-right");
            $('td:eq(5)', nRow).html(formatMoney(actualAmount, 2)).addClass("text-right");
            $('td:eq(6)', nRow).html(statusName);
//            if($('#allowGenerate').val()=="true") {
                $('td:eq(7)', nRow).html(link);
//            }
        },
        columns: [
            {defaultContent: ''},
            {data: 'created'},
            {data: 'orgName'},
            {data: 'amount'},
            {defaultContent: ''},
            {data: 'actualAmount'},
            {data: 'statusName'},
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
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $(this).prop('checked', true);
        }
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
    });

    /**
     * 生成开票清单按钮
     */
    $("#generateCheckList").click(function () {
        if ($("input[name='check']:checked").length == 0) {
            cbms.alert("请选择待生成的开票清单！");
            return;
        }
        var ids = [];
        $('#waiting-table').find("input[name='check']:checked").each(function () {
            ids.push($(this).val());
        });
        location.href = Context.PATH + "/invoice/out/generate.html?ids=" + ids.join(",");
    });
    
    /**
	 * 点击：选择报表中心，服务中心选择框
	 */
	$("#orgSelectBtn").click(showSelectOptionsBox);
	
	/**
	 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
	 */
	clickSelectAll();
	
	$("#search_btn").click(function(){
		table.ajax.reload();
	});
		
});

/**
 * 点击：选择报表中心，服务中心选择框
 */
function showSelectOptionsBox(){
	var optionbox = $("#orgSelect");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#org_options", function(){
			optionbox.hide();
		});
	}else{
		optionbox.hide();
	}
}

/**
 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
 */
function clickSelectAll(){
	
	$("#selectAllOrg").click(function(){
		var checked=$(this).prop('checked');
		if(checked){
			$("#orgSelect li input[type='checkbox']").removeAttr("checked");
			$(this).prop("checked", "checked");
		}
	})
	
	$("#orgSelect li input[type='checkbox']").not("#selectAllOrg").click(function(){
		var selectAll = $("#selectAllOrg");
		if(selectAll.prop("checked")){
			//$(this).removeAttr("checked");
			selectAll.removeAttr("checked");
		}
	});
	
}
