var _dtQutotionOrderItems;

$(document).ready(function() {
	var url = Context.PATH + "/smartmatch/quotation/search.html";
	_dtQutotionOrderItems = jQuery("#dynamic-table").DataTable({
		"processing" : false,
		"serverSide" : false,
		"searching" : false,
		"ordering" : false,
		"paging" : false,
		"bInfo" : false,
		"autoWidth": true,
		"ajax" : {
			"url" : url,
			"type" : "POST",
			data : function(d) {
				return $.extend({}, d, {
					 id:QuotationPage.ID
				});
			}
		},
		columns : [
		 {data : 'categoryName'},
		 {data : 'materialName'},
		 {data : 'spec'},
		 {data : 'factoryName'},
		 {data : 'warehouseName'},
		 {data : 'weight',render:function (e) {return e.toFixed(3);}},
		 {data : 'dealPrice'},
		 {data : 'totalAmount'},
		 {data : 'remark'}
		],
		footerCallback: function (row, data, start, end, display) {
			$("#dynamic-table th").css("background-color","#E87716").css("color","#000000");
            var api = this.api(), total;
            //当前页汇总
            total = pageTotal(api);
            $(".weight").html(total.pageTotalWeight.toFixed(3));
            $(".price").html(total.pageTotalAmount.toFixed(2));
        },
        "fnFooterCallback": function( nFoot, aData, iStart, iEnd, aiDisplay ){
        	
        	$("#dynamic-table th").css("background-color","#E87716").css("color","#000000");
            var api = this.api(), total;
            //当前页汇总
            total = pageTotal(api);
            $(".weight").html(total.pageTotalWeight.toFixed(3));
            $(".price").html(total.pageTotalAmount.toFixed(2));
            
        	if(aData.length>0){
        		$("#remarkDesc").html("<span>备注:"+(aData[0].remarkDesc == null ? "" :aData[0].remarkDesc)+"</span>");
        	}
        	else{
        		$("#remarkDesc").html("<span>备注:</span>");
        	}
        },
	});

	if (QuotationPage.orderStatus == 'PENDING_CLERK_ACCEPTE') {
		$(".btn-bar .change").text("推送服总");
	}

	//tab点击内容切换
	$(document).on("click", ".quotation-li", function(){
		$(this).closest("ul").find("li").removeClass("active");
		$(this).closest("li").addClass("active");

		if($(this).closest("li").hasClass("current")){
			$(".currentQuotation").removeClass("none");
			$(".historyQuotation").addClass("none");
		}else{
			$(".historyQuotation").removeClass("none");
			$(".currentQuotation").addClass("none");
		}
	});

    //表格表头颜色
	$(".dynamic-table th").css("background-color","#E87716").css("color","#000000");

	//底部按钮处理
	$(document).on("click", ".btn-event", function(){
		var url;
		//返回
		if($(this).hasClass("returnPage")){
			url=Context.PATH +"/smartmatch/inquiryorder/create.html?id="+QuotationPage.purchaseOrderId;
		}
		//开单
		if($(this).hasClass("openOrder")){

			cbms.loading();
			$.ajax({
				type: 'post',
				data: {
					"quotationOrderId":QuotationPage.ID   //报价单id
				},
				url: Context.PATH + "/smartmatch/purchaseorder/preopen.html",
				error: function (s) {
					cbms.closeLoading();
				},
				success: function (result) {
					cbms.closeLoading();
					if(result.success) {
						if(result.success){
							var requrimentcode = result.data;
							url=QuotationPage.orderDomain+"/order/"+QuotationPage.purchaseOrderId+"/create.html";
							if(requrimentcode != null){
								url+='?requirementCode='+requrimentcode;
							}
							window.location.href=url;
						}else{
							cbms.alert(result.data);
						}
					}else{
						cbms.alert(result.data);
					}
				}
			});

		}
		//推送
		if($(this).hasClass("push")){
			cbms.confirm("是否将报价单推送给客户并推送业务员开单？", QuotationPage.ID, pushToAccount);
		}
		//继续询价
		if($(this).hasClass("continueInquiry")){
			url=Context.PATH +"/smartmatch/purchaseorder/created.html?id="+QuotationPage.purchaseOrderId;
		}

		if(url){
			window.location.href=url;
		}
	});

	//关闭
	$(document).on("click", ".closed", function () {
		var ele = '<div class="dialog-m" id="dialog">' +
			'<p><label>关闭原因　　：</label><select id="reasonSelect"><option value="0">价格不合适</option><option value="1">没找到货</option>' +
			'<option value="2">交货地不合适</option><option value="3">货不全</option><option value="4">其他</option></select></p>' +
			'<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason"></textarea></p>' +
			'<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">关闭</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
		cbms.getDialog("采购单关闭", ele);
		
		$("#reasonDiv").hide();

		$("#reasonSelect").change(function () {
			var select = $('#reasonSelect').children('option:selected').val();
			if (select != 4) {
				$("#reasonDiv").hide();
			} else {
				$("#reasonDiv").show();
			}
		});
		$("#dialog").on("click", "#cancel", function () {
			cbms.closeDialog();
		});

		$("#dialog").on("click", "#commit", function () {
			var selected = $("#reasonSelect").children("option:selected");
			var reason;
			if($(selected).val() == 4) {
				reason = $("#reason").val();
			}else{
				reason = $(selected).html();
			}
			if(reason == ""){
				utils.showMsg("请填写关闭理由", null, "error", 2000);
				return false;
			}
			$.ajax({
				type: 'post',
				url: Context.PATH + "/smartmatch/purchaseorder/pushAndCloseOrder/"+ QuotationPage.purchaseOrderId +".html",
				data: {
					reason: reason
				},
				error: function (s) {
				},
				success: function (result) {
					if(result.success) {
						utils.showMsg("关闭成功", null, null, 2000);
						cbms.closeDialog();
						window.location.href=Context.PATH +"/smartmatch/purchaseorder/list.html?tabIndex=2";
					}else{
						cbms.alert(result.data);
					}
				}
			});
		});
	});

	//改派
	$(document).on("click", ".change", function () {
		$("#reassign_purchase_orderid").val(QuotationPage.purchaseOrderId);
		cbms.getDialog("改派理由","#reassign_dialog");
	});
	$(document).on("change", "#assign_reason_select", function () {
		var select = $(this).val();
		if (select != '其他') {
			$("#assign_reason_panel").hide();
		} else {
			$("#assign_reason_panel").show();
		}
	});

	$(document).on("click", "#btnreassign", function () {
		var reason = "";
		var checkReason = $("#assign_reason_select").val();
		if (checkReason == '其他') {
			reason = $("#reassign_reason_other_text").val();
			if (reason == '') {
				cbms.alert("请填写改派理由！");
				return false;
			}
		}
		else {
			reason = checkReason;
		}
		var order_id = $("#reassign_purchase_orderid").val();
		var url = Context.PATH + '/smartmatch/purchaseorder/orderReassign.html';
		$.post(url, {id: order_id, remark: reason}, function (re) {
			if (re.success) {
				location.href = Context.PATH + '/smartmatch/purchaseorder/list.html';
			}
			else {
				cbms.alert(re.data);
			}
		});
	});

	$(document).on("click", "#reassign_cancel", function () {
		cbms.closeDialog();
	});
});


function pageTotal(api) {
    var weight = 5,  amount= 7;
    var total = {
        pageTotalWeight: pageTotalColumn(api, weight),
        pageTotalAmount: pageTotalColumn(api, amount)
    }
    return total;
}

function pushToAccount(id) {
	$("#quotation_orderid").val(id);
	cbms.getDialog("询价单指派","#assign_dialog");
}

$(document).on("click", ".confirm", function(){
	$.ajax({
		type: "POST",
		url: Context.PATH + "/smartmatch/quotation/confirm.html",
		data: {
			id : QuotationPage.ID
		},
		dataType: "json",
		success: function (response) {
			if(response.success){
				location.href = Context.PATH + "/smartmatch/purchaseorder/list.html";
			}else{
				utils.showMsg(response.data, null, "error", 2000);
			}
		}
	});
});
