var _sellerDt;
var _imgFlag = false;

$().ready(function () {
	$("#allowanceFrom").verifyForm();
	changeAllowanceManner();
	initAmountAndWeight();
	sumWeightAndAmount();
	$('.orderList-tbody').on("change", ".weightSymbol", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("input", ".weightValue", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("change", ".amountSymbol", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("input", ".amountValue", function (){
		sumWeightAndAmount();
    });
	
	// 修改折让方式
	$(document).on("change", "#allowanceManner", function(){
		initAmountAndWeight();
		changeAllowanceManner();
		sumWeightAndAmount();
    });
	
    // 提交审核
    $(document).on("click", "#allowanceSubmit", function(){
    	if(!CheckData()) {
    		return;
    	};
    	var allowanceManner = $("#allowanceManner").val();
    	if(allowanceManner == "amount") {
    		createBuyerAllowance();
    	}else {
    		cbms.confirm("确认提交审核？", null, function () {
    			allowanceSave("to_audit","提交审核",null);
    		});
    	}
    });
    // 保存
    $(document).on("click", "#allowanceSave", function(){
    	if(!CheckData()) {
    		return;
    	};
        cbms.confirm("确认保存？", null, function () {
        	allowanceSave("to_submit","保存",null);
        });
    });

    $("#allowanceImg").change(function () {
        if (this.files.length > 0) {
            if (!CheckExt(this.files[0]) || !CheckProperty(this.files[0])) {
            	_imgFlag = false;
            }else {
            	_imgFlag = true;
            }
        }
    });
    
    // 确定
    $(document).on("click", "#buyerConfirm", function(){
    	var buyerDeptIds  = [];
    	$("input[name='buyerBox']:checked").each(function () {
    		var id = $(this).val();
			buyerDeptIds.push(id);
    	});
    	cbms.confirm("确认提交审核？", null, function () {
        	allowanceSave("to_audit","提交审核",buyerDeptIds.toString().replace(" "));
        	cbms.closeDialog();
        });
    });
    
    // 取消
    $(document).on("click", "#buyerCancel", function(){
    	cbms.closeDialog();
    });
    //失去焦点 如果输入为空 默认为0
    $('.orderList-tbody').on("blur", ".weightValue", function (){
        if($.trim($(this).val()) === ''){
            $(this).val('0.000000');
            sumWeightAndAmount();
        }
    });

    //失去焦点 如果输入为空 默认为0
    $('.orderList-tbody').on("blur", ".amountValue", function (){
        if($.trim($(this).val()) === ''){
            $(this).val('0.00');
            sumWeightAndAmount();
        }
    });
	
});

function sumWeightAndAmount() {
	var totalActualWeight = 0;
	var totalAllowanceWeight = 0;
	var totalUnAllowanceWeight = 0;
	var totalActualAmount = 0;
	var totalAllowanceAmount = 0;
	var totalUnAllowanceAmount = 0;
	var allowanceManner = $("#allowanceManner").val();
	$(".orderList-tbody > tr").each(function () {
		var actualAmount = parseFloat($(this).find(".actualAmount").text().replace(/,/g,""))
		totalActualAmount = totalActualAmount + actualAmount;
		var allowanceAmount = parseFloat($(this).find(".amountSymbol").val()+$(this).find(".amountValue").val().replace(/,/g,""));
		$(this).find(".allowanceAmount").text(allowanceAmount);
		totalAllowanceAmount = totalAllowanceAmount + allowanceAmount;
		var unAllowanceAmount = actualAmount+allowanceAmount;
		$(this).find(".unAllowanceAmount").text(formatMoney(unAllowanceAmount,2));
		totalUnAllowanceAmount = totalUnAllowanceAmount +unAllowanceAmount;
		
		var actualWeight = parseFloat($(this).find(".actualWeight").text().replace(/,/g,""));
		totalActualWeight = totalActualWeight + actualWeight;
		var allowanceWeight = parseFloat($(this).find(".weightSymbol").val()+$(this).find(".weightValue").val().replace(/,/g,""));
		$(this).find(".allowanceWeight").text(allowanceWeight);
		totalAllowanceWeight = totalAllowanceWeight + allowanceWeight;
		var unAllowanceWeight = actualWeight+allowanceWeight;
		$(this).find(".unAllowanceWeight").text(unAllowanceWeight.toFixed(6));
		totalUnAllowanceWeight = totalUnAllowanceWeight + unAllowanceWeight;
	});
	$('#totalActualWeight').text(totalActualWeight.toFixed(6));
	$('#totalAllowanceWeight').text(totalAllowanceWeight.toFixed(6));
	$('#totalUnAllowanceWeight').text(totalUnAllowanceWeight.toFixed(6));
	$('#totalActualAmount').text(formatMoney(totalActualAmount,2));
	$('#totalAllowanceAmount').text(formatMoney(totalAllowanceAmount,2));
	$('#totalUnAllowanceAmount').text(formatMoney(totalUnAllowanceAmount,2));
}

function initAmountAndWeight() {
	var allowanceManner = $("#allowanceManner").val();
	$(".weightSymbol").val("-");
	$(".amountSymbol").val("-");
	$(".orderList-tbody > tr").each(function () {
		$(this).find(".weightValue").val(0);
		$(this).find(".amountValue").val(0);
	});
}

function CheckData() {
	if(!_imgFlag) {
		cbms.alert("没有上传附件或附件不符合规定！");
		return false;
	}
	if (!setlistensSave())return false;
	return true;
}

function allowanceSave(status,msg,buyerDeptIds) {
	var allowance = gather();
	if(!allowance){
		return;
	}
	var note = $("#allowance_note").val();
    var paramJson = JSON.stringify(allowance);
    cbms.loading();
    var options = {
            type: "POST",
            data: {
            	"paramJson" : paramJson,
                "allowanceType" : "seller",
                "status" : status,
                "note" : note,
                "allowanceManner" : $("#allowanceManner").val(),
                "buyerDeptIds" : buyerDeptIds
            },
            success: function (result) {
            	cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        cbms.alert(msg+"成功", function(){
                            window.location.href = Context.PATH + "/allowance/list/seller.html";
                        });
                    } else {
                    	cbms.alert(result.data);
                    }
                } else {
                    cbms.alert(msg+"失败");
                }
            },
            error: function (s) {
            	cbms.closeLoading();
            }
        };
    $("#sellerForm").ajaxSubmit(options);
}

function gather() {
	var orderList = [];
	$(".orderList-tbody > tr").each(function () {
		var orderObj = {};
		// 行属性
		var orderId = $(this).attr("orderid");
		var orderDetailId = $(this).attr("orderdetailid");
		var contractCode = $(this).attr("contractcode");
		var buyerId = $(this).attr("buyerid");
		var sellerId = $(this).attr("sellerid");
		var sellerName = $(this).attr("sellername");
		var totalWeight = $(this).attr("totalweight");
		var totalAmount = $(this).attr("totalamount");
		var totalActualWeight = $(this).attr("totalactualweight");
		var totalActualAmount = $(this).attr("totalactualamount");
		var totalQuantity = $(this).attr("totalquantity");
		var buyerName = $(this).attr("buyername");
		var buyerDeptId = $(this).attr("buyerdeptid");
		var buyerDeptName = $(this).attr("buyerdeptname");
		var sellerDeptId = $(this).attr("sellerdeptid");
		var sellerDeptName = $(this).attr("sellerdeptname");


		// 行参数
		var orderTime = $(this).find(".orderTime").text();
		var orderCode = $(this).find(".orderCode").text();
		var nsortName = $(this).find(".nsortName").text();
		var material = $(this).find(".material").text();
		var spec = $(this).find(".spec").text();
		var actualWeight = $(this).find(".actualWeight").text();
		var allowanceWeight = $(this).find(".allowanceWeight").text();
		if(allowanceWeight == 0 || allowanceWeight == '-0') {
			allowanceWeight = 0;
		}
		var unAllowanceWeight = $(this).find(".unAllowanceWeight").text().replace(/,/g,"");
		
		var actualAmount = $(this).find(".actualAmount").text();
		var allowanceAmount = $(this).find(".allowanceAmount").text();
		if(allowanceAmount == 0 || allowanceAmount == '-0') {
			allowanceAmount = 0;
		}
		var unAllowanceAmount = $(this).find(".unAllowanceAmount").text().replace(/,/g,"");
		
		var allowanceManner = $("#allowanceManner").val();
		if(allowanceManner == "amount") {
        	if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) > actualAmount ){
        		cbms.alert("金额必须大于等于0小于实提金额");
        		orderList = null;
                return false;
            }
        }else if(allowanceManner == "weight") {
			if(Math.abs(allowanceWeight) < 0 || Math.abs(allowanceWeight) > actualWeight ){
				cbms.alert("重量必须大于等于0小于实提重量");
				orderList = null;
				 return false;
            }
		}else {
			if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) > actualAmount ){
				cbms.alert("金额必须大于等于0小于等于实提金额");
				orderList = null;
				 return false;
            }
			if(Math.abs(allowanceWeight) < 0 || Math.abs(allowanceWeight) > actualWeight ){
				cbms.alert("重量必须大于等于0小于等于实提重量");
				orderList = null;
				 return false;
            }
		}
        
		orderObj.orderId = orderId;
		orderObj.orderDetailId = orderDetailId;
		orderObj.contractCode = contractCode;
		orderObj.buyerId = buyerId;
		orderObj.sellerId = sellerId;
		orderObj.sellerName = sellerName;
		orderObj.totalWeight = totalWeight;
		orderObj.totalAmount = totalAmount;
		orderObj.totalActualWeight = totalActualWeight;
		orderObj.totalActualAmount = totalActualAmount;
		orderObj.totalQuantity = totalQuantity;
		orderObj.buyerName = buyerName;
		orderObj.buyerDeptId = buyerDeptId;
		orderObj.buyerDeptName = buyerDeptName;
		orderObj.sellerDeptId = sellerDeptId;
		orderObj.sellerDeptName = sellerDeptName;
		
		orderObj.orderTime = orderTime;
		orderObj.orderCode = orderCode;
		orderObj.nsortName = nsortName;
		orderObj.material = material;
		orderObj.spec = spec;
		orderObj.actualWeight = actualWeight;
		orderObj.allowanceWeight = allowanceWeight;
		orderObj.unAllowanceWeight = unAllowanceWeight;
		orderObj.actualAmount = actualAmount;
		orderObj.allowanceAmount = allowanceAmount;
		orderObj.unAllowanceAmount = unAllowanceAmount;
		
		orderList.push(orderObj);
	});
	return orderList;
}

function changeAllowanceManner() {
	var allowanceManner = $("#allowanceManner").val();
	if(allowanceManner == 'amount') {
		$(".allowanceMannerWeight").hide();
		$(".allowanceMannerAmount").show();
	}else if(allowanceManner == 'weight') {
		$(".allowanceMannerWeight").show();
		$(".allowanceMannerAmount").hide();
	}else if(allowanceManner == 'all') {
		$(".allowanceMannerWeight").show();
		$(".allowanceMannerAmount").show();
	}
}

/**
 * 从 file 域获取 本地图片 url
 */
function getFileUrl(sourceId) {
    var url;
    if (navigator.userAgent.indexOf("MSIE")>=1) { // IE
        url = document.getElementById(sourceId).value;
    } else if(navigator.userAgent.indexOf("Firefox")>0) { // Firefox
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    } else if(navigator.userAgent.indexOf("Chrome")>0) { // Chrome
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    }
    return url;
}

/**
 * 将本地图片 显示到浏览器上
 */
function preImg(sourceId, targetId) {
    var url = getFileUrl(sourceId);
    var imgPre = document.getElementById(targetId);
    imgPre.src = url;
}

var AllowExt = ".jpeg|.jpg|.png|.gif|.bmp|" //允许上传的文件类型 ?为无限制 每个扩展名后边要加一个"|" 小写字母表示
var AllowImgFileSize = 2048;    //允许上传图片文件的大小 0为无限制 单位：KB

function CheckProperty(file)    //检测图像属性
{
    var ErrMsg = "";
    var ImgFileSize = Math.round(file.size / 1024 * 100) / 100;//取得图片文件的大小
    if (AllowImgFileSize != 0 && AllowImgFileSize < ImgFileSize) {
        ErrMsg = "请上传小于" + AllowImgFileSize + "KB的文件";
        cbms.alert(ErrMsg);
        return false;
    }
    else
        return true;
}

function CheckExt(file) {
    var ErrMsg = "";
    if (file.name == "") return false;
    var FileExt = file.name.substr(file.name.lastIndexOf(".")).toLowerCase();
    if (AllowExt != 0 && AllowExt.indexOf(FileExt + "|") == -1) //判断文件类型是否允许上传
    {
        ErrMsg = "不允许上传的文件类型" + FileExt;
        cbms.alert(ErrMsg);
        return false;
    }
    else
        return true;
}

function createBuyerAllowance() {
	//原按买家 现改为按买家部门 buyerMap替换成deptMap
    var buyerAllowanceHtml = "";
    //var buyerMap = {};
	var deptMap = {};
    $(".orderList-tbody > tr").each(function () {
    	/*if(buyerMap[$(this).attr("buyerid")]){
    		return;
    	}*/

		if(deptMap[$(this).attr("buyerdeptid")]){
			return;
		}

    	//buyerMap[$(this).attr("buyerid")] = $(this).find(".buyerName").text();

		//买家+部门（多个部门则显示 一个部门则不显示）
		deptMap[$(this).attr("buyerdeptid")] = $(this).find(".buyerName").text();
    });
    for(var key in deptMap) {
    	buyerAllowanceHtml += '<input name="buyerBox" value="'+key+'" type="checkbox"> <label>';
    	buyerAllowanceHtml += '<span>'+deptMap[key]+'</span>';
        buyerAllowanceHtml += '</label>';
    }
    
    $("#buyerList").html(buyerAllowanceHtml);
    cbms.getDialog("提示信息",$("#buyerAllowance").html());
}


