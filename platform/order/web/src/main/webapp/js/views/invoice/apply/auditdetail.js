var _weightPrecision = 6;
var _amountPrecision = 2;
$(document).ready(function(){
    var table = $(".table-apply");
    // 展开
    var slideFlag = true;
    table.on("click", "p[name='slide']", function () {
    	var currentTable = $(this).closest("table");
    	var index = 0;
		currentTable.find(".table-apply-tbody > tr").each(function(){
			var row = $(this);
			index++;
			if(index>=5) {
				if(slideFlag == true) {
					row.show();
				}else if(slideFlag == false) {
					row.hide();
				}
			}
		});
		if(slideFlag == true) {
			$(this).closest("tfoot").find("td:eq(0)").find("a").text("收起");
			slideFlag = false;
		}else if(slideFlag == false) {
			$(this).closest("tfoot").find("td:eq(0)").find("a").text("展开");
			slideFlag = true;
		}
		
    });
    
    // 查看
    table.on("click", "a[name='view']", function () {
        var currentRow = $(this).closest("tr");
        if($("tbody[name=mergedData] tr",currentRow).size() == 0){
        	// 合并数据并设置到tfoot中去
        	$(".itemOrder",currentRow).each(function(){
            	var dataList = {};
            	var mergeDataCot = $("tbody[name=mergedData]",this);
        		$(".itemsList-tbody tr",this).each(function(){
            		var row = $(this);
            		var orderdetailid = row.attr("orderdetailid");
            		var itemObj = dataList[orderdetailid];
            		if(!itemObj){
            			var cloneRow = row.clone();
            			itemObj = {
            					trRow : cloneRow,
            					weightTd : cloneRow.find("td:eq(4)"),
            					amountTd : cloneRow.find("td:eq(8)"),
            					invoiceWeight : 0,
            					invoiceAmount : 0
            			};
            			dataList[orderdetailid] = itemObj;
            			mergeDataCot.append(itemObj.trRow);
            		}
            		var cw = parseFloat(cbms.convertFloat($("td:eq(4)",row).text(),_weightPrecision));
            		var ca = parseFloat(cbms.convertFloat($("td:eq(8)",row).text(),_amountPrecision));
            		itemObj.invoiceWeight += cw;
            		itemObj.invoiceAmount += ca;
            		itemObj.weightTd.text(itemObj.invoiceWeight.toFixed(_weightPrecision));
            		itemObj.amountTd.text(itemObj.invoiceAmount.toFixed(_amountPrecision));
            	});
        	});
        }
        var itemsList = currentRow.find(".itemsList").html();
        cbms.getDialog(currentRow.find("td:eq(0)").text(),itemsList);
    });

    // 审核通过
    $(document).on("click", "#approve-btn", function(){
        cbms.confirm("确认审核通过？", null, function () {
            applyApprove();
        });
    });
    
    // 审核不通过
    $(document).on("click", "#unapprove-btn", function(){
        cbms.confirm("确认审核不通过？", null, function () {
            applyUnApprove();
        });
    });

    // 返回
    $(document).on("click", "#items-btn", function(){
        cbms.closeDialog();
    });
    
    // 详情搜索
    $(document).on("click","#btnSearch",function(){
        var infoBar = $(this).closest(".info-bar");

        var paramOrderNo = infoBar.find("#seachConsignOrderCode").val();
        var paramStartTime = infoBar.find("#startTime").val();
        var paramEndTime = infoBar.find("#endTime").val();
        var items = infoBar.find(".itemOrder");
        items.show();
        var orderNo,created,createdDate;
        items.each(function(i,e){
            orderNo = $(e).find("label:eq(1) span").text();
            created = $(e).find("label:eq(2) span").text();
            createdDate = created.substr(0,10);
            if($.trim(paramOrderNo)!=""&&orderNo.indexOf(paramOrderNo)==-1){
                $(e).hide();
                return;
            }
            if($.trim(paramStartTime)!=""&&(paramStartTime>createdDate)){
                $(e).hide();
                return;
            }
            if($.trim(paramEndTime)!=""&&(paramEndTime<createdDate)){
                $(e).hide();
                return;
            }
        });
    });

});

// 审核通过
function applyApprove(){
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/invoice/apply/approve.html",
        data: {
        	"status" : "APPROVED",
            "outApplyId" : $("#outApplyId").val()
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("待开票审核成功", function(){
                        window.location.href = Context.PATH + "/invoice/apply/audit.html";
                    });
                } else {
                	cbms.alert(result.data, function(){
                		window.location.href = Context.PATH + "/invoice/apply/audit.html";
                    });
                }
            } else {
                cbms.alert("待开票审核失败");
            }
        }
    });
}

//审核不通过
function applyUnApprove(){
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/invoice/apply/approve.html",
        data: {
        	"status" : "DISAPPROVE",
            "outApplyId" : $("#outApplyId").val()
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("待开票审核不通过成功", function(){
                        window.location.href = Context.PATH + "/invoice/apply/audit.html";
                    });
                } else {
                	cbms.alert(result.data, function(){
                		window.location.href = Context.PATH + "/invoice/apply/audit.html";
                    });
                }
            } else {
                cbms.alert("待开票审核不通过失败");
            }
        }
    });
}
