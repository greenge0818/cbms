/*  xuefei20150720  ystep*/
var flag =true;
var picNum = 0;
var sellerPicNum = 0;
var billType = $("#billType").val();
if(billType== 'ladbill'){//买家提单
    picNum = $("#uploadNum").val();
	sellerPicNum = $("#uploadSellerNum").val();
}
jQuery(function($) {
	$(".addForwarder")
			.click(
					function() {
						var ele = '<li>' ;
						if($("#deliveryType").val() == 'TRANSFER' || $("#deliveryType").val() == 'ORIGIN'){
							ele += '<label><span class="red">*</span>身份证号：<input  type="text" value="" must="1" name="idcardNumber" verify="IDCard" msg="请输入正确的身份证号码"/></label>&nbsp;';
						}else{
							ele += '<input  type="hidden" value=""  name="idcardNumber"/>';
						}
						 ele = ele + '<label><span class="red">*</span>车船号：<input  type="text" value="" must="1" name="validCode" maxlength="10"/></label>&nbsp;'
								+ '<label>司机姓名：<input class="fowarder-name"  type="text" value="" name="name"/></label>&nbsp;'
								+ '<label>联系方式：<input type="text" value="" verify="mobile" name="mobil"/></label>&nbsp;'
								+ '<a href="javascript:;" title="删除" class="delForwarder"><i class="ace-icon glyphicon glyphicon-minus"></i></a></li>';
						$(this).closest("ul").find("li").last("li").after(ele);
					});

	$(document).on("click", ".delForwarder", function() {
		var v = $(this).closest(".forwarder-ul").find("li");
		if (v.length == 1)
			return false;
		$(this).closest("li").remove();
	});

	$("#deliveryType").change(
			function() {
				if ($(this).val() == "CAR") {
					$(".forwarder-ul:eq(0)").show().find("input").removeAttr(
							"disabled");
					$(".forwarder-ul:eq(1)").hide().find("input").attr(
							"disabled", "disabled");
					$(".forwarder-ul:eq(2)").hide().find("input").attr(
							"disabled", "disabled");
				} else if ($(this).val() == "IDCARD") {
					$(".forwarder-ul:eq(1)").show().find("input").removeAttr(
							"disabled");
					$(".forwarder-ul:eq(0)").hide().find("input").attr(
							"disabled", "disabled");
					$(".forwarder-ul:eq(2)").hide().find("input").attr(
							"disabled", "disabled");
				}else{
					
					$(".forwarder-ul:eq(2)").show().find("input").removeAttr(
					"disabled");
					$(".forwarder-ul:eq(0)").hide().find("input").attr(
							"disabled", "disabled");
					$(".forwarder-ul:eq(1)").hide().find("input").attr(
							"disabled", "disabled");
					$(".forwarder-ul:eq(2)").find("li:eq(0)").nextAll().remove();
				}
			});

	$("#pickupForm").verifyForm();
	
	$("input[name='pickQuantity']").change(function() {
		var itemId = $(this).closest("tr").find("td:eq(0) input").val();
		var pickQuantity = 1.0 * $(this).val();
		var totalWeight = 1.0*$(this).closest("tr").find("input[name='totalWeight']").val();
		var singleWeight = 1.0*$(this).closest("tr").find("td:eq(7)").text();
		var left_qty = 1.0 * $(this).closest("tr").find("td:eq(10)").text();
		var pick_weight = 0;
		// issue 3463 Green add 09/21
//		if(pickQuantity == 0){
//			cbms.alert("提货数量不能为0！");
//			flag=false;
//			$(this).focus().val(left_qty);
//			return false;
//		}
		if (pickQuantity > left_qty) {
			cbms.alert("不能超过可提数量");
			flag=false;
			$(this).focus().val(left_qty);
			return false;
		}else if(pickQuantity == left_qty){
			var pickedWeight = 0;
			$.ajax({
				url:Context.PATH+"/order/pickup/orderitem/"+itemId+"/pickedweight.html",
				async: false,
				success:function(data){
					pick_weight= totalWeight.sub(1.0*data);
				}
			});
		}else{
			pick_weight= singleWeight.mul(pickQuantity);
		}
		
		$(this).closest("tr").find("span:last").text(cbms.convertFloat(pick_weight));
	});

	$('#pickupForm').submit(function() {
		//modify by wangxianjun列表中已控制
		if(!showButtons()){
			return false;
		}

		if (setlistensSave("#pickupForm") && checkForm()) {
			 cbms.loading();
			//var billType = $("#billType").val();
				if(billType== 'ladbill'){//买家提单保存url
					$("#pickupForm").attr("action",Context.PATH + '/order/pickup/save.html');
				}else{
					//放货单保存url
					$("#pickupForm").attr("action",Context.PATH + '/order/pickup/delivery/save.html');
				}

			$(this).ajaxSubmit({
				success : function(data) {
					if (data.success) {
						var content="<p class='text-left'>"+data.data+"</p><br/> <p class='text-center'><em class='red' id='sec'>5</em>秒钟后自动关闭</p>";
						cbms.getDialog("提示信息",content);
						var i =5;
						var interval = setInterval(function(){
							$("#sec").text(--i);
							if(i==0){
								clearInterval(interval);
								cbms.closeDialog();
								if(!$("input[name='id']").length>0){
									location.href = Context.PATH+ "/order/query/pickup.html";
									return;
								}
								var orderPickUpStatus = data.orderPickUpStatus;
								if (orderPickUpStatus == 3) {
									location.href = Context.PATH+ "/order/query/pickup.html";
								} else {
									location.reload();
								}
							}
						},1000);
					}else{
						cbms.alert(data.data);
					}
//					cbms.alert(data.data,function() {
//						if (data.success) {
//							var orderPickUpStatus = data.orderPickUpStatus;
//							if (orderPickUpStatus == 3) {
//								location.href = Context.PATH+ "/order/query/pickup.html";
//							} else {
//								location.reload();
//							}
//						}});
				}
			});
		} else {

		}
		return false;
	});
	//弹出页面查看图片
	$(document).on("click", ".img-box", function () {
		var $img = $(this).next("img"), tit = $img.attr("alt");
		var src =  $(this).find("img").attr("src");
		renderImg(encodeURI(src));
	});
	//删除图片
	$(document).on("click", ".buyer-del", function () {
		var attachmentid =  $(this).attr("id");
		var billId;
		//var billType = $("#billType").val();
		if (!isNaN(attachmentid)) {
			if(billType== 'ladbill'){//买家提单id
				billId = $("#pickupId").val();
			}else{
				//放货单id
				billId = $("#billId").val();
			}
			deleteImg(attachmentid, billId,billType,'buyer');
		}
	});
	//删除图片
	$(document).on("click", ".seller-del", function () {
		var attachmentid =  $(this).attr("id");
		var billId;
		//var billType = $("#billType").val();
		if (!isNaN(attachmentid)) {
			if(billType== 'ladbill'){//买家提单id
				billId = $("#pickupId").val();
			}else{
				//放货单id
				billId = $("#billId").val();
			}

			deleteImg(attachmentid, billId,'sellerladbill','seller');
		}
	});
	if(billType == 'ladbill'){//买家提单
		$("#isBillBuyercert").click(function(){
			var checked=$(this).prop('checked');
			if(checked){
				$("#isBuyercert").val("1");
				$("#credent_name").attr("disabled",false);
			}else{
				$("#isBuyercert").val("0");
				$("#credent_name").attr("disabled",true);
			}
		});
		//卖家提单
		$("#isBillSellercert").click(function(){
			var checked=$(this).prop('checked');
			if(checked){
				$("#isSellercert").val("1");
				$("#seller_credent_name").attr("disabled",false);
			}else{
				$("#isSellercert").val("0");
				$("#seller_credent_name").attr("disabled",true);
			}
		});
	}
	
});

function checkForm() {
	if(flag==false){
		flag=true;
		return false;
	}
	
	// 校验车船号
	var deliveryType = $("#deliveryType").val();
	if (deliveryType == "CAR") {
		var validCodeArr = [], mobileArr = [],idCardArr = [];
		$("input[name='validCode']").each(function(i, e) {
			validCodeArr.push($.trim($(e).val()));
		});
		$("input[name='mobil']").each(function(i, e) {
			if($.trim($(e).val()) !== "") {
				mobileArr.push($.trim($(e).val()));
			}
		});
		$("input[name='idcardNumber']").each(function(i, e) {
			if($.trim($(e).val()) !== "") {
				idCardArr.push($.trim($(e).val()));
			}
		});
		if (isRepeat(idCardArr)) {
			cbms.alert("身份证号有重复");
			return false;
		}
		if (isRepeat(validCodeArr)) {
			cbms.alert("车船号有重复");
			return false;
		}
		/*if (isRepeat(mobileArr)) {
			cbms.alert("提货人联系方式有重复");
			return false;
		}*/
	}
	var isDelivery = $("#isDelivery").val();
	if (isDelivery != "1") {
		var totalPickQuantity = 0;
		var pickQuantity = $("input[name='pickQuantity']");
		if(pickQuantity.length > 0) {
			pickQuantity.each(function () {
				totalPickQuantity = totalPickQuantity.add(1 * $(this).val());
			});
			if (totalPickQuantity == 0) {//&& $("#id").val() == ""
				cbms.alert("提货数量不能都为0");
				return false;
			}
		}
	}
	if(billType == 'ladbill'){//买家提单
		if($("#isBillBuyercert").prop('checked') &&  picNum == 0){
			cbms.alert("提货单作为买家凭证，必须上传买家提单");
			return false;
		}
		//只有凭证名称可选时才进行
		if(!$("#credent_name").prop('disabled')){
			$("#certName").val($("#credent_name").val());
		}
		if($("#isBillSellercert").prop('checked') &&  sellerPicNum == 0){
			cbms.alert("提货单作为卖家凭证，必须上传卖家提单");
			return false;
		}
		//只有凭证名称可选时才进行
		if(!$("#seller_credent_name").prop('disabled')){
			$("#sellerCertName").val($("#seller_credent_name").val());
		}

	}
	

	return true;
}

// function isRepeat(arr) {
// var hash = {};
// for ( var i in arr) {
// if (hash[arr[i]])
// return true;
// hash[arr[i]] = true;
// }
// return false;
//
// }

function isRepeat(a) {
	return /(\x0f[^\x0f]+)\x0f[\s\S]*\1/.test("\x0f" + a.join("\x0f\x0f")
			+ "\x0f");
}

/**
 * 上传图片 显示到浏览器上
 */
function uploadImg(type) {
	//var filelist = getFileUrl("pic");
	if(type != 'buyer'){
		$("#billType").val("sellerladbill");//卖家提单标示
	}
	$("#pickupForm").attr("action",Context.PATH + '/order/pickup/uploadPic.html');
	var imgEle = "", loadingspan1 = "";
	var options = {
		type: "POST",
		success: function (re) {
			var loadingspan;
			var imglength;
			if(type == 'buyer') {
				loadingspan= $("#img_pics span[loading=true]");
				imglength= $("#img_pics img").length;
			}else{
				loadingspan= $("#seller-img-pics span[loading=true]");
				imglength= $("#seller-img-pics img").length;
			}




			if (re) {
				if (re.success) {
					var appendImg = "";
					var dataId = ""
					var rootbase = Context.PATH + '/common/getfile.html?key=';
					var data = re.data;
					for (var i = imglength; i < data.length; i++) {
						dataId = data[i].id;
						if(type == 'buyer'){
						    appendImg = '<div id="img_append' + data[i].id + '"><div/>';
							imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" id="' + data[i].id + '" class="pos-abs fa fa-lg fa-close red buyer-del" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
						}else{
							appendImg = '<div id="seller_img_append' + data[i].id + '"><div/>';
							imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" id="' + data[i].id + '" class="pos-abs fa fa-lg fa-close red seller-del" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
						}
						// loadingspan.attr("cid", data[i].id).removeAttr("loading");
						loadingspan1 += '<span class="pull-left pos-rel imgload" style="margin-right:10px;margin-top:10px;width:100px;height:70px" loading="true"><a href="javascript:;"  class="img-box"><img src="" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>';


					}
					if(type == 'buyer') {
						picNum = data.length;
						$("#img_pics").append(appendImg);
						$("#img_append" + dataId).append(loadingspan1);

						setTimeout(function () {
							$("#img_append" + dataId).html(imgEle);
						}, 250);
					}else{
						sellerPicNum = data.length;
						$("#seller-img-pics").append(appendImg);
						$("#seller_img_append" + dataId).append(loadingspan1);

						setTimeout(function () {
							$("#seller_img_append" + dataId).html(imgEle);
						}, 250);
						$("#billType").val("ladbill");//卖家提单图片上传后，类型改成默认值
					}
				}
				else {
					loadingspan.remove();
					cbms.alert("上传失败:" + re.data);
				}
			}
			else {
				loadingspan.remove();
				cbms.alert("上传失败，服务器异常");
			}
		},
		error: function (re) {
			var loadingspan = $("#img_pics span[loading=true]");
			loadingspan.remove();
			cbms.alert("上传失败，服务器异常");
		}
	};
	$("#pickupForm").ajaxSubmit(options);

}
/**
 * 删除选中的图片，并将剩余的图片显示到浏览器上
 */
function deleteImg(imgId,pbId,bType,type) {

	$.ajax({
		type: "POST",
		url: Context.PATH + '/order/pickup/deletePic.html',
		data: {
			imgId : imgId,
			pbId : pbId,
			bType: bType
		},

		success:function(re){
			if (re) {
				if (re.success) {
					if(type == 'buyer'){
						showImgs(re,"img_pics",type);
					}else{
						showImgs(re,"seller-img-pics",type);
					}

				}
				else {
					cbms.alert("删除失败:"+re.data);
				}
			}
			else {
				cbms.alert("删除失败，服务器异常");
			}
		}
		,
		error: function (re) {
			cbms.alert("删除失败，服务器异常");
		}
	});
}

/**
 * 将图片显示到浏览器上
 */
function showImgs(result,targetId,type) {
	var imgPre = document.getElementById(targetId);
	var htmlStr = '';
	var rootbase = Context.PATH + '/common/getfile.html?key=';
	for(var i=0;i<result.data.length;i++){
		htmlStr += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px"><a href="javascript:;" class="img-box"><img src="'+rootbase+result.data[i].fileUrl+'" alt="回单" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);"/></a>';
		if(type == 'buyer') {
			htmlStr += '<a href="javascript:;" class="pos-abs fa fa-lg fa-close red buyer-del" style="right:-5px;top:-5px;z-index: 99;" id="' + result.data[i].id + '"></a></span>';
		}else{
			htmlStr += '<a href="javascript:;" class="pos-abs fa fa-lg fa-close red seller-del" style="right:-5px;top:-5px;z-index: 99;" id="' + result.data[i].id + '"></a></span>';
		}
	}
	if(type == 'buyer') {
		picNum = result.data.length;
	}else{
		sellerPicNum = result.data.length;
	}
	$(imgPre).html(htmlStr);
}
/**
 * 从 file 域获取 本地图片 url
 */
function getFileUrl(sourceId) {
	var url;
	if (navigator.userAgent.indexOf("MSIE")>=1) { // IE
		url = document.getElementById(sourceId).value;
	} else if(navigator.userAgent.indexOf("Firefox")>0) { // Firefox
		url = document.getElementById(sourceId).files;
	} else if(navigator.userAgent.indexOf("Chrome")>0) { // Chrome
		// url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
		url = document.getElementById(sourceId).files;
	}

	return url;
}

/**
 * 将本地图片 显示到浏览器上
 */
function preImg(sourceId, targetId) {
	var filelist = getFileUrl(sourceId);
	var imgPre = document.getElementById(targetId);
	var htmlStr = '';
	for(var i=0;i<filelist.length;i++){

		htmlStr += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px"><a href="javascript:;"  class="img-box"><img src="'+window.URL.createObjectURL(filelist.item(i))+'" alt="发票" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);"/></a></span>'
	}
	if(targetId=='img_pics'){
		picNum = filelist.length;
	}else{
		sellerPicNum = filelist.length;
	}

	$(imgPre).html(htmlStr);
}

/**
 * 正在进行合同变更的订单，禁止除“返回”外的按钮操作
 */
/**
 * 正在进行合同变更的订单，禁止除“返回”外的按钮操作
 */
function showButtons() {
	//var type = $("#alterStatus").val();
	var orderId = $('input[name="consignOrderId"]').val() ;
	var returnFlag = true;
	/*if(type == "PENDING_APPROVAL" || type == "PENDING_RELATE" || type=="PENDING_APPLY" || type=="PAYED_DISAPPROVE" ||type=="PENDING_APPR_PAY" || type =="PENDING_PRINT_PAY" || type =="PENDING_CONFIRM_PAY"){
	 cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
	 return false;
	 }else{*/
	$.ajax({
		type: 'post',
		async: false,
		url: Context.PATH + '/order/changecontract/queryorderpaystatus.html',
		data: {
			orderId: orderId,
			origin:"origin"
		},
		error: function (s) {
			cbms.alert("查询订单信息报错！");
			returnFlag = false;
		}
		, success: function (re) {
			if (re.success) {
				if(re.data == "PENDING_APPROVAL" || re.data == "PENDING_RELATE" || re.data=="PENDING_APPLY" || re.data=="PAYED_DISAPPROVE" || re.data=="PENDING_APPR_PAY" || re.data =="PENDING_PRINT_PAY" || re.data =="PENDING_CONFIRM_PAY"){
					cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
					returnFlag = false;
				}else{
					returnFlag = true;
				}
			}else{
				cbms.alert("查询订单信息报错！");
				returnFlag = false;
			}
		}
	});
	//}
	return returnFlag;
}