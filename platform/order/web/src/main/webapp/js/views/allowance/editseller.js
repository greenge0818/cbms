var indexUrl = Context.PATH + '/allowance/list/seller.html';
var _imgFlag = true;
var _isDeleteImg = false;
$().ready(function () {
    if($("#sellerForm").length > 0){
        $("#sellerForm").verifyForm();
        sumWeightAndAmount();

        $('.orderList-tbody').on("change", ".weightSymbol", function (){
            sumWeightAndAmount();
        });
        $('.orderList-tbody').on("input", ".weightValue", function (){
            var value = parseFloat($.trim($(this).val()));
            if($.trim($(this).val()) === '' || isNaN(value) || value < 0){
                return false;
            }
            sumWeightAndAmount();
        });
        $('.orderList-tbody').on("change", ".amountSymbol", function (){
            sumWeightAndAmount();
        });
        $('.orderList-tbody').on("input", ".amountValue", function (){
            var value = parseFloat($.trim($(this).val()));
            if($.trim($(this).val()) === '' || isNaN(value) || value < 0){
                return false;
            }
            sumWeightAndAmount();
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


        //关闭折让单
        if($("#close").length > 0){
            $("#close").click(closeOrder);
        }

        //提交审核
        $("#submit").click(submitAllowance);

        $("#allowanceImg").change(function () {
            _isDeleteImg = true;
            if (this.files.length > 0) {
                if (!CheckExt(this.files[0]) || !CheckProperty(this.files[0])) {
                    _imgFlag = false;
                }else {
                    _imgFlag = true;
                }
            }
        });
    }
    
    $("#disguiserImg").click(function () {
    	disguiserImg();
    });
    
    $(".img-box").click(function () {
		var src = $("#turnImg").attr("src");
		renderImg(src);
	});

});

function sumWeightAndAmount() {
    var totalActualWeight = 0;
    var totalAllowanceWeight = 0;
    var totalUnAllowanceWeight = 0;
    var totalActualAmount = 0;
    var totalAllowanceAmount = 0;
    var totalUnAllowanceAmount = 0;
    $(".orderList-tbody > tr").each(function () {

        //实提重量计算
        var actualWeight = parseFloat($(this).find(".actualWeight").val().replace(/,/g,""));
        totalActualWeight = totalActualWeight + actualWeight;

        //实提金额计算
        var actualAmount = parseFloat($(this).find(".actualAmount").val().replace(/,/g,""));
        totalActualAmount = totalActualAmount + actualAmount;

        if(_pageContext._displayWeight == 'true') {
            var allowanceWeight = parseFloat($(this).find(".weightSymbol").val()+$(this).find(".weightValue").val().replace(/,/g,""));
            $(this).find(".allowanceWeight").val(allowanceWeight);
            totalAllowanceWeight = totalAllowanceWeight + allowanceWeight;

            //当前行折后重量计算
            var unAllowanceWeight = actualWeight+allowanceWeight;
            $(this).find(".unAllowanceWeight").text(unAllowanceWeight.toFixed(6));

            //折后重量合计
            totalUnAllowanceWeight = totalUnAllowanceWeight +
            parseFloat($(this).find(".unAllowanceWeight").text().replace(/,/g,""));
        }

        if(_pageContext._displayAmount == 'true') {
            var allowanceAmount = parseFloat($(this).find(".amountSymbol").val()+$(this).find(".amountValue").val().replace(/,/g,""));
            $(this).find(".allowanceAmount").val(allowanceAmount);
            totalAllowanceAmount = totalAllowanceAmount + allowanceAmount;

            //当前行折后金额计算
            var unAllowanceAmount = actualAmount+allowanceAmount;
            $(this).find(".unAllowanceAmount").text(formatMoney(unAllowanceAmount,2));

            //折后金额合计
            totalUnAllowanceAmount = totalUnAllowanceAmount + unAllowanceAmount;
        }


    });

    //实提总重量
    $('#totalActualWeight').val(totalActualWeight.toFixed(6));
    $('#totalActualWeightDisplay').text(totalActualWeight.toFixed(6));

    //实提总金额
    $('#totalActualAmount').val(totalActualAmount.toFixed(2));
    $('#totalActualAmountDisplay').text(totalActualAmount.toFixed(2));

    if(_pageContext._displayWeight == 'true'){
        //折扣总重量
        $('#totalAllowanceWeight').val(totalAllowanceWeight.toFixed(6));
        $('#totalAllowanceWeightDisplay').text(totalAllowanceWeight.toFixed(6));

        //折后总重量
        $('#totalUnAllowanceWeight').text(totalUnAllowanceWeight.toFixed(6));
    }

    if(_pageContext._displayAmount == 'true'){
        //折扣总金额
        $('#totalAllowanceAmount').val(totalAllowanceAmount.toFixed(2));
        $('#totalAllowanceAmountDisplay').text(formatMoney(totalAllowanceAmount,2));

        //折后总金额
        $('#totalUnAllowanceAmount').text(formatMoney(totalUnAllowanceAmount,2));
    }
}

function closeOrder() {
    cbms.confirm("确定要关闭该折让单吗？", null, function () {
        var id = $("#allowanceId").val();
        var url = Context.PATH + '/allowance/'+id+'/closeorder.html';
        cbms.loading();
        $.post(url,function (re) {
            cbms.closeLoading();
            if (re.success) {
                setTimeout(function(){cbms.alert(re.data)},1000);
                location.replace(indexUrl);
            } else {
                cbms.alert(re.data);
            }
        });
    });
}

function submitAllowance(){
	var note = $("#allowance_note").val();
    if(!CheckData()) {
        return;
    };

    //表单提交
    var allowance = gather();
    if(!allowance){
        return;
    }

        cbms.confirm("确定提交审核？", null, function () {
            var paramJson = JSON.stringify(allowance);
            cbms.loading();
            var options = {
                type: 'POST',
                data: {
                    "id":$("#allowanceId").val(),
                    "paramJson" : paramJson,
                    "allowanceType" : "seller",
                    "status" : "to_audit",
                    "oldStatus":_pageContext._status,
                    "note" : note,
                    "allowanceManner" : _pageContext._allowance_manner
                    //"isDeleteImg" :_isDeleteImg,
                    //"imgUrl": $("#oldImgUrl").val()
                },
                error: function (s) {
                    cbms.closeLoading();
                },
                success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.alert("提交成功", function(){
                                window.location.href = Context.PATH + "/allowance/list/seller.html";
                            });
                        } else {
                            cbms.alert(result.data);
                        }
                    } else {
                        cbms.alert("提交失败");
                    }
                }
            };

            if(_isDeleteImg){
                options.data.imgUrl = $("#oldImgUrl").val();
            }

            $("#sellerForm").ajaxSubmit(options);
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
        var actualWeight = $(this).find(".actualWeight").val();
        var allowanceWeight = $(this).find(".allowanceWeight").val() || 0;
        if(allowanceWeight == 0 || allowanceWeight == '-0') {
            allowanceWeight = 0;
        }
        var unAllowanceWeight = $(this).find(".unAllowanceWeight").text().replace(/,/g,"") || 0;

        var actualAmount = $(this).find(".actualAmount").val();
        var allowanceAmount = $(this).find(".allowanceAmount").val() || 0;
        if(allowanceAmount == 0 || allowanceAmount == '-0') {
            allowanceAmount = 0;
        }
        var unAllowanceAmount = $(this).find(".unAllowanceAmount").text().replace(/,/g,"") || 0;

        var allowanceManner = _pageContext._allowance_manner;
        if(allowanceManner == "amount") {
        	if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) >= actualAmount ){
        		cbms.alert("金额必须大于等于0小于实提金额");
        		orderList = null;
                return false;
            }
        }else if(allowanceManner == "weight") {
			if(Math.abs(allowanceWeight) < 0 || Math.abs(allowanceWeight) >= actualWeight ){
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

function disguiserImg() {
	$("#disguiserImgDiv").hide();
	$("#allowanceImg").click()
	$("#allowanceImg").show();
	
}