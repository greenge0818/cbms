/*
 * created by lixiang 2016/8/19
 * */

var bankinfoList = [];
var orderHandledMsg = "该交易单已处理！";

jQuery(function ($) {
    var url = Context.PATH + '/order/query/getstepdata.html';
    var order_id = $("#order_id").text();
    $.post(url, {orderid: order_id}, function (re) {});

    if ($("#account_balance").length > 0) {
        var account_blance_second_settlement = parseFloat($("#account_blance_second_settlement").text());
        show_balance();
        $("#cb_blance_second_settlement").change(function () {
            show_balance();
        });

        //信用额度
        $("#cb_blance_credit").change(function () {
            show_balance();
        });
    }

    
    
    if ($("#order_applypay").length > 0) {
        //请求银行信息
        var orderid = $("#order_id").text();
        var orderChangeId = $("#order_change_id").text();
        $(document).on("click", ".applypay_contract_upload", function () {
            var contractid = $(this).parent().parent().attr("contractid");
            var accountname = $(this).parent().parent().find("td").eq(0).text();
            accountname = encodeURIComponent(encodeURIComponent(accountname));
            var sellerLabel = $("#sellerLabel").val();//客户标示
            var settingValue = $("#settingValue").val();//是否需要上传合同
            var contractuploadurl = Context.PATH + '/order/contractupload.html?orderid=' + orderChangeId + '&contractid=' + contractid + '&accountname=' + accountname + '&type=change' + "&";
            //add by wangxianjun 白名单可以不用上传合同
            if(sellerLabel == "白名单"  && settingValue == '0'){
                cbms.confirm("该卖家可无需上传合同，是否继续上传？", null, function () {
                    cbms.getDialog("上传合同（采购合同）", contractuploadurl);
                });
            }else{
                //非白名单
                cbms.getDialog("上传合同（采购合同）", contractuploadurl);
            }
        });
        $(document).on("click", ".applypay_contract_view", function () {
            var contractid = $(this).parent().parent().attr("contractid");
            var contractviewurl = Context.PATH + '/order/contractattachmentview.html?contractid=' + contractid + '&';
            cbms.getDialog("查看合同", contractviewurl);
        });
        var bankinfourl = Context.PATH + '/order/query/getaccountbankinfo.html';
        $("#payment_table tbody tr[accountid]").each(function () {
            var accountId = $(this).attr("accountid");
            var currentrow = $(this);
            $.post(bankinfourl, {accountid: accountId}, function (re) {
                if (re.success) {
                    var optionstr = "";
                    for (var i in re.data) {
                        if (re.data[i].bankCityName == null)
                            re.data[i].bankCityName = "";
                        var temp = {
                            city: re.data[i].bankCityName,
                            code: re.data[i].bankAccountCode
                        };
                        var codelast = re.data[i].bankAccountCode.substr(re.data[i].bankAccountCode.length - 4);
                        var str = "";
                        if(re.data[i].isDefault == '1'){
                            str = " selected ='selected'";
                            currentrow.find("span").eq(0).text(re.data[i].bankAccountCode);
                        }
                        optionstr += "<option value='" + re.data[i].id + "' " + str + ">" + (re.data[i].bankName == null ? "" : re.data[i].bankName) + ' ' + (re.data[i].bankNameBranch == null ? "" : re.data[i].bankNameBranch) + "(" + codelast + ")</option>";
                        bankinfoList[re.data[i].id] = temp;
                        if (i == 0) {
                            currentrow.find("span").eq(0).text(re.data[i].bankAccountCode);
                        }
                    }
                    if (optionstr == "") {
                        optionstr += "<option value=''>未找到银行账号信息</option>";
                    }
                    currentrow.find("select").eq(0).html(optionstr);
                    currentrow.find("select").eq(0).on("change", function () {
                        var id = $(this).val();
                        var infotext = bankinfoList[id].code;
                        $(this).parent().parent().find("span").first().text(infotext);
                    })
                }
            });
            calculatePaymentAmount(currentrow);
        });
        $("#payment_table input[cbsecondsettlement]").change(function () {
            var row = $(this).closest("tr");
            calculatePaymentAmount(row);
        });
        $("#payment_table input[cbrefundcredit]").change(function () {
            var row = $(this).closest("tr");
            calculatePaymentAmount(row);
        });
        $("#order_applypay").click(function () {
            var msg_confirm = "";
            var aMoney;
            $("#payment_table tbody tr").each(function () {
                aMoney = $(this).find("input[name='paymentAmount']").val();
            });
            $.ajax({
                type: 'post',
                url: Context.PATH + "/order/query/checkcontractupload.html",
                data: {
                    orderid: $("#order_id").text(),
                    orderChangId: $("#order_change_id").text(),
                    type:"change", //add by wangxianjun 区分是否是变更流程
                    applymoney :aMoney//申请金额
                },
                error: function (s) {
                }
                , success: function (result) {
                    if (result && result.success) {
                        if($("#paymentType").val() == "0" || $("#isDelayPay").val() ==""){
                            //给卖家付款选择当日全额付款
                            msg_confirm = "确定申请付款吗？";
                        }else{
                            //给卖家付款选择延时付款
                            msg_confirm = '您已选择给卖家付款：<span class="bolder red">提货' +$("#delayNums").val()+ '日内现款全额支付</span><br/>如现在申请付款，则该笔交易单不计算业绩，<br/>确定现在要申请付款？';
                        }
                        cbms.confirm(msg_confirm, null, function () {
                            var order_id = $("#order_id").text();
                            var balanceList = [];
                            var validate = true;
                            var msg = "申请信息填写不正确！";
                            $("#payment_table tbody tr").each(function () {
                                var sid = $(this).attr("departmentId")
                                var applymoney = $(this).find("input[name='paymentAmount']").val();
                                var ordermoney = $(this).find("input[name='paymentAmount']").attr("contractAmount");
                                var bankaccountid = $(this).find("td").eq(1).find("select").val();
                                var secondChecked = $(this).find("input[cbsecondsettlement]").is(":checked");
                                var refundChecked = $(this).find("input[cbrefundcredit]").is(":checked");

                                var secondbalance = parseFloat($(this).find("input[cbsecondsettlement]").attr("cbsecondsettlement"));
                                var creditused = parseFloat($(this).find("input[cbrefundcredit]").attr("cbrefundcredit"));
                                var contract_amount = parseFloat(ordermoney);
                                if (secondChecked && secondbalance < 0) {
                                    contract_amount += secondbalance;
                                }
                                if (refundChecked > 0) {
                                    contract_amount -= creditused;
                                }
                                if (contract_amount < 0) {
                                    contract_amount = 0;
                                }

                                if (parseFloat(applymoney) < 0) {
                                    validate = false;
                                    msg = "申请付款金额必须大于等于0";
                                    return false;
                                }
                                //后台判断，此处不再判断金额
                                if (isNaN(applymoney) || isNaN(ordermoney)) {
                                    validate = false;
                                    msg = "申请付款金额应小于等于应付金额！";
                                    return false;
                                }
                                if (bankaccountid == null || bankaccountid == '') {
                                    validate = false;
                                    msg = "付款银行账号信息不正确！";
                                    return false;
                                }
                                var balance = {
                                    sellerid: sid,
                                    balance: applymoney,
                                    bankaccountid: bankaccountid,
                                    secondChecked: secondChecked,
                                    refundChecked: refundChecked
                                }
                                balanceList.push(balance);
                            });
                            if (validate) {
                                var url = Context.PATH + '/order/changecontract/applypay.html';
                                cbms.loading();
                                $.post(url,
                                    {
                                		orderChangId: $("#order_change_id").text(),
                                        balancelist: JSON.stringify(balanceList)
                                    },
                                    function (re) {
                                        cbms.closeLoading();
                                        if (re.success) {
                                        	cbms.gritter(re.data);
                                            var url = Context.PATH + "/order/changecontract/changelist.html";
                                            location.replace(url);
                                        } else {
                                            var handledCallback = null;
                                            if (re.data == orderHandledMsg) {
                                                handledCallback = orderHanlded;
                                            }
                                            cbms.gritter(re.data, handledCallback);
                                        }
                                    });
                            } else {
                            	cbms.gritter(msg);
                            }
                        });
                    } else {
                    	 cbms.gritter(result.data);
                    }
                }

            });

        });
    } else {
        $(".applypay_contract_view").click(function () {
            var contractid = $(this).closest("tr").attr("contractid");
            var contractviewurl = Context.PATH + '/order/contractattachmentview.html?contractid=' + contractid + '&';
            //window.open(contractviewurl);
            cbms.getDialog("查看合同", contractviewurl);
        });
        $(".receipt_view").click(function () {
            var orderid = $("#orderid").val();
            var requestitemid = $(this).closest("tr").attr("requestitemid");
            var receiptsurl = Context.PATH + '/order/query/receipts.html?requestitemid=' + requestitemid + '&orderid='+ orderid + '';
            location.href=receiptsurl;
        });
    }
    

   
    //弹出页面查看图片
    $(document).on("click", ".img-box", function () {
        var $img = $(this).next("img"), tit = $img.attr("alt");
        var src = $(this).find("img").attr("src");
        renderImg(src);
    });
    //删除图片
    $(document).on("click", ".fa-close", function () {
       var attachmentid =  $(this).attr("id");
        if (!isNaN(attachmentid)) {
            deleteImg(attachmentid, $("#orderid").val());
        }
    });

});

// 申请付款抵扣二次结算账户余额复选框
function calculatePaymentAmount(currentrow) {
    var cbsecondsettlement = currentrow.find("input[cbsecondsettlement]").is(":checked");
    var cbrefundcredit = currentrow.find("input[cbrefundcredit]").is(":checked");
    var secondbalance = 0;
    var input_payment=currentrow.find("input[name='paymentAmount']");
    var amount = parseFloat(input_payment.attr("contractAmount"));
    if(cbsecondsettlement){
        secondbalance = parseFloat(currentrow.find("input[cbsecondsettlement]").attr("cbsecondsettlement"));
    } else{
        secondbalance = 0;
    }
    if (secondbalance < 0) {
        amount += secondbalance;
    }
    var refundamount = 0;
    if (cbrefundcredit) {
        refundamount = parseFloat(currentrow.find("input[cbrefundcredit]").attr("cbrefundcredit"));
    }
    if (refundamount > 0) {
        amount -= refundamount;
    }
    amount = formatMoney(amount, 2).replace(/,/g, "");
    if (amount <= 0) {
        input_payment.val(0).attr("readonly", true);
    }
    else {
        input_payment.val(amount).removeAttr("readonly");
    }
}

function show_balance() {
    var account_balance = parseFloat($("#account_balance").text());
    var account_blance_second_settlement = parseFloat($("#account_blance_second_settlement").text());
    var cb_blance_second_settlement = $("#cb_blance_second_settlement").is(":checked");
    var order_total_amount = parseFloat($("#order_total_amount").val());
    var account_total_balance = $("#account_total_balance");
    var insufficient_fund_balance = $("#insufficient_fund_balance");
    var div_insufficient_fund = $("#insufficient_fund");
    var div_allow_releted_info = $("#allow_releted_info");
    //买家容差值
    var buyerToleranceAmount = parseFloat($("#buyerToleranceAmount").val());
    var contact_releted_amount = $("#contact_releted_amount");

    //信用额度
    var cb_blance_credit = $("#cb_blance_credit").is(":checked");
    var account_blance_credit = parseFloat($("#account_blance_credit").text());

    var temp = account_balance;
    if (cb_blance_second_settlement) {
        temp = account_blance_second_settlement.add(temp);
    }
    if(cb_blance_credit){
        temp = account_blance_credit.add(temp);
    }
    account_total_balance.text(temp.toFixed(2));


    //订单金额 - 可用余额
    var oreder_total_amount_sub_temp = order_total_amount.sub(temp);
    if (oreder_total_amount_sub_temp > 0) {
        //用容差
        //可用金额少于订单金额在容差范围内
        if(oreder_total_amount_sub_temp <= buyerToleranceAmount){
            contact_releted_amount.val(temp.toFixed(2));
            $("#sub_balance").text(oreder_total_amount_sub_temp.toFixed(2));
            div_insufficient_fund.addClass("none");
            div_allow_releted_info.removeClass("none");
            $("#order_relate").removeAttr("disabled");
        }else{
            insufficient_fund_balance.text(oreder_total_amount_sub_temp.toFixed(2));
            div_insufficient_fund.removeClass("none");
            div_allow_releted_info.addClass("none");
            $("#order_relate").prop("disabled", true);
        }
    } else {
        contact_releted_amount.val(order_total_amount.toFixed(2));
        //可用金额>=订单金额 时隐藏提示信息并启用关联按钮
        div_insufficient_fund.addClass("none");
        div_allow_releted_info.addClass("none");
        $("#order_relate").removeAttr("disabled");
    }
}
//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]" +
    "*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]); return null; //返回参数值
}

//查询资金账户余额和二次结算余额、可用信用额度
function queryBalanceById(accountId) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/query/queryBalanceById.html',
        data: {
            accountId : accountId
        },
        dataType: "json",
        success: function (result) {
            if (result && result.success) {
                var account_balance = $("#account_balance");
                var account_blance_second_settlement = $("#account_blance_second_settlement");
                account_balance.text(parseFloat(result.data.balance).toFixed(2));
                account_blance_second_settlement.text(parseFloat(result.data.balanceSecondSettlement).toFixed(2));
                var account_blance_credit = $("#account_blance_credit");
                account_blance_credit.text(parseFloat(result.data.blanceCredit).toFixed(2));

                show_balance();
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function orderHanlded() {
    var menu = getUrlParam('menu');
    var url = Context.PATH + "/order/query/" + menu + ".html";
    location.replace(url);
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
 * 上传图片 显示到浏览器上
 */

function preImg() {
    var imgEle = "", loadingspan1 = "";
    var options = {
        type: "POST",
        success: function (re) {
            var loadingspan = $("#img_pics span[loading=true]");

            var imglength = $("#img_pics img").length;


            if (re) {
                if (re.success) {
                    var appendImg = "";
                    var dataId = ""
                    var rootbase = Context.PATH + '/common/getfile.html?key=';
                    var data = re.data;
                    for (var i = imglength; i < data.length; i++) {
                        dataId = data[i].id;
                        appendImg = '<div id="img_append' + data[i].id + '"><div/>';
                       // loadingspan.attr("cid", data[i].id).removeAttr("loading");
                        loadingspan1 += '<span class="pull-left pos-rel imgload" style="margin-right:10px;margin-top:10px;width:100px;height:70px" loading="true"><a href="javascript:;"  class="img-box"><img src="" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>';

                        imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" id="' + data[i].id + '" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
                    }
                    $("#img_pics").append(appendImg);
                    $("#img_append" + dataId).append(loadingspan1);

                    setTimeout(function () {
                        $("#img_append" + dataId).html(imgEle);
                    }, 250);
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

    $("#orderForm").ajaxSubmit(options);

}

    /**
 * 删除选中的图片，并将剩余的图片显示到浏览器上
 */
function deleteImg(imgId,orderId) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/query/deletePic.html',
        data: {
            imgId : imgId,
            orderId : orderId
        },

        success:function(re){
            if (re) {
                if (re.success) {
                    showImgs(re,"img_pics");
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
function showImgs(result,targetId) {
    var imgPre = document.getElementById(targetId);
    var htmlStr = '';
    var rootbase = Context.PATH + '/common/getfile.html?key=';
    for(var i=0;i<result.data.length;i++){
        htmlStr += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px"><a href="javascript:;" class="img-box"><img src="'+rootbase+result.data[i].fileUrl+'" alt="回单" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);"/></a>';
        htmlStr += '<a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;" id="'+result.data[i].id+'"></a></span>';

    }
    $(imgPre).html(htmlStr);
}



$("#printcontract").click(function(){
    if(!showButtons()){
        return false;
    }
    $("#printcontractok")[0].click();
})

//关闭变更
if ($("#order_pay_close").length > 0) {
    $("#order_pay_close").click(function () {
        var html="<form id='order_shut_editForm'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭本次变更的理由！'></textarea><br>"+
            "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
            "<button type='button'  class='btn btn-info btn-sm' id='order_pay_shut'>确定申请关闭</button></div> </div></form>";
        cbms.getDialog("关闭订单",html);

    });
}

$(document).on("click","#order_pay_shut",function(){
    if (!setlistensSave("#order_shut_editForm")) return;
    var orderChangeId = $("#order_change_id").text();
    var url = Context.PATH + '/order/changecontract/orderchangeclose.html';
    cbms.loading();
    $.post(url, {orderChangeId: orderChangeId,cause:$("textarea[name='cause']").val()}, function (re) {
        cbms.closeLoading();
        if (re.success) {
            cbms.gritter(re.data);
            var url = Context.PATH + "/order/changecontract/changelist.html";
            location.replace(url);
        }
        else {
            var handledCallback = null;
            if (re.data == orderHandledMsg) {
                handledCallback = orderHanlded;
            }
            cbms.gritter(re.data, handledCallback);
        }
    });
})

$(document).on("click","#window_close",function(){
   		cbms.closeDialog();
    })

