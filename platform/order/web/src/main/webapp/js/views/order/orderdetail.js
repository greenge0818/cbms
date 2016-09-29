/*
 * created by caochao 2015.07.20
 * */

var bankinfoList = [];
var orderHandledMsg = "该交易单已处理！";

jQuery(function ($) {
    var url = Context.PATH + '/order/query/getstepdata.html';
    var order_id = $("#order_id").text();
    $.post(url, {orderid: order_id}, function (re) {
        if (re.success) {
            var stepmainlist = [], steppaylist = [];
            var stepindexmain = 0, stepindexpay = 0;
            for (var i in re.data) {
                var tempcontent = re.data[i].operaterName + "(" + re.data[i].roleName + ")" + re.data[i].mobile + "<br>";
                if (re.data[i].operationTime != null) {
                    var date = new Date(re.data[i].operationTime)
                    if (typeof(date) != "undefined") {
                        var datestr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                        tempcontent += datestr;
                    }
                }
                var stepitem = {
                    title: re.data[i].operationName,
                    content: tempcontent
                };
                if (re.data[i].statusType == "1") {
                    steppaylist.push(stepitem);
                    if (re.data[i].operationTime != null) {
                        stepindexpay++;
                    }
                }
                else {
                    stepmainlist.push(stepitem);
                    if (re.data[i].operationTime != null) {
                        stepindexmain++;
                    }
                }
            }
            var stepitem = {
                title: "交易完成",
                content: ""
            };
            stepmainlist.push(stepitem);
            var stepmaindata = $.parseJSON(JSON.stringify(stepmainlist));
            var steppaydata = $.parseJSON(JSON.stringify(steppaylist));
            $(".ystep4").loadStep({
                size: "large",
                color: "blue",
                steps: stepmaindata
            });
            $(".ystep1").loadStep({
                vertical: "1",
                size: "large",
                color: "blue",
                steps: steppaydata
            });

            if (stepindexpay > 0) {
                $(".ystep-vertical").find("span").addClass("ystep-active-highlight");
            }
            if (stepindexmain == 8) {//判断业务节点，如已开票，表示订单已完成。将进度条点亮交易完成
            	stepindexmain++;
            } 
            $(".ystep4").setStep(stepindexmain);
            $(".ystep1").setStep(stepindexpay);
        }
    });

    if ($("#account_balance").length > 0) {
        var account_blance_second_settlement = parseFloat($("#account_blance_second_settlement").text());
        //if (account_blance_second_settlement <= 0) {
        //    $("#cb_blance_second_settlement").attr("checked", true)
        //}
        show_balance();
        $("#cb_blance_second_settlement").change(function () {
            show_balance();
        });

        //信用额度
        $("#cb_blance_credit").change(function () {
            show_balance();
        });
    }

    //审核
    if ($("#order_audit_accept").length > 0) {
        var order_id = $("#order_id").text();
        $("#order_audit_accept").click(function () {
            cbms.confirm("确定通过审核吗？", null, function () {
                var order_id = $("#order_id").text();
                var url = Context.PATH + '/order/query/acceptorder.html';
                cbms.loading();
                $.post(url, {orderid: order_id}, function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        cbms.alert("审核成功");
                        var menu = getUrlParam('menu');
                        var url = Context.PATH + "/order/query/approval.html";
                        if (menu != '') {
                            url = Context.PATH + "/order/query/" + menu + ".html";
                        }
                        location.replace(url);
                    }
                    else {
                        var handledCallback = null;
                        if (re.data == orderHandledMsg) {
                            handledCallback = orderHanlded;
                        }
                        cbms.alert(re.data, handledCallback);
                    }
                });
            });
        });
        $("#order_audit_refuse").click(function () {
            /*cbms.confirm("确定审核不通过吗？", null, function () {
             var order_id = $("#order_id").text();
             var url = Context.PATH + '/order/query/refuseorder.html';
             $.post(url, {orderid: order_id}, function (re) {
             if (re.success) {
             cbms.alert("操作成功");
             location.href = Context.PATH + "/order/query/approval.html";
             }
             else {
             cbms.alert(re.data);
             }
             });
             });*/

            //add by dengxiyan 2015/8/7
            var ele = '<form id="editForm"><div class="amount-box"><p>请填写不通过的理由!</p><p><textarea rows="4" cols="30" class="textarea" must="1" id="refuseNote"></textarea></p><p class="text-center">' +
                '<button type="button" class="btn btn-sm btn-primary" id="order_audit_refuse_confirm">确定</button>&nbsp;' +
                '<button type="button" class="btn btn-sm btn-default" id="order_audit_refuse_cancel">取消</button></p></div></form>'
            cbms.getDialog("不通过审核", ele);
        });

        //审核： 不通过弹出框的按钮事件
        //确定  add by dengxiyan 2015/8/7
        $("body").on('click', '#order_audit_refuse_confirm', function () {
            if (!setlistensSave("#editForm")) return;
            var refuseNote = $("#refuseNote").val();
            var order_id = $("#order_id").text();
            var url = Context.PATH + '/order/query/refuseorder.html';
            $("#order_audit_refuse_confirm").attr("disabled", true);
            $.post(url, {orderid: order_id, note: refuseNote}, function (re) {
                $("#order_audit_refuse_confirm").removeAttr("disabled");
                if (re.success) {
                    //cbms.alert("操作成功");
                    var menu = getUrlParam('menu');
                    var url = Context.PATH + "/order/query/approval.html";
                    if (menu != '') {
                        url = Context.PATH + "/order/query/" + menu + ".html";
                    }
                    location.replace(url);
                }
                else {
                    var handledCallback = null;
                    if (re.data == orderHandledMsg) {
                        handledCallback = orderHanlded;
                    }
                    cbms.alert(re.data, handledCallback);
                }
            });
        });
        //取消 add by dengxiyan 2015/8/7
        $("body").on('click', '#order_audit_refuse_cancel', function () {
            cbms.closeDialog();
        });


    }
    if ($("#order_relate").length > 0) {
        $("#order_relate").click(function () {

            cbms.confirm("确定关联吗？", null, function () {
                var validate = true;
                var orderItemsList = [];   //银票支付的订单详情表列表
                $(".orderItemsTable").find(".itemsdatabody").find("tr").each(function(){
                    var isPayedByAcceptDraft = $(this).find(".isPayedByAcceptDraft").text(); //是否网银支付
                    var acceptDraftId = $(this).find("select[name='acceptDraft']").val();  //银票票号ID
                    if(isPayedByAcceptDraft == "true"){
                        if(acceptDraftId == 0){
                            validate = false;
                            cbms.alert("订单资源中有选择银票支付但未选择票号，不能关联!");
                            return false;
                        }
                        if(acceptDraftId > 0){
                            var orderItemsObj = {};
                            orderItemsObj.orderItemsId = $(this).find(".orderItemsId").text(); //订单详情ID
                            orderItemsObj.acceptDraftId = acceptDraftId; //银票票号ID
                            orderItemsObj.acceptDraftCode = $(this).find("select[name='acceptDraft']").find("option:selected").text(); //银票票号
                            orderItemsList.push(orderItemsObj);
                        }
                    }
                });
                if(validate){
                    var order_id = $("#order_id").text();
                    var takeoutSecondBalance = $("#cb_blance_second_settlement").is(":checked");
                    var url = Context.PATH + '/order/query/relateorder.html';
                    var contactReletedAmount = parseFloat($("#contact_releted_amount").val());//合同关联金额
                    var takeoutCreditBalance = $("#cb_blance_credit").is(":checked");
                    cbms.loading();
                    $.post(url, {orderid: order_id,
                        secondbalancetakeout:takeoutSecondBalance,
                        contactReletedAmount:contactReletedAmount,
                        orderItemsList:JSON.stringify(orderItemsList),
                        creditbalancetakeout:takeoutCreditBalance
                    }, function (re) {
                        cbms.closeLoading();
                        if (re.success) {
                            cbms.alert("合同关联成功");
                            var menu = getUrlParam('menu');
                            var url = Context.PATH + "/order/query/associate.html";
                            if (menu != '') {
                                url = Context.PATH + "/order/query/" + menu + ".html";
                            }
                            location.replace(url);
                        } else {
                            var handledCallback = null;
                            if (re.data == orderHandledMsg) {
                                handledCallback = orderHanlded;
                            }
                            cbms.alert(re.data, handledCallback);
                        }
                    });
                }
            });
        });
        var accountId = $("#accountId").val();
        setInterval("queryBalanceById(" + accountId + ")", 30000);
    }
    if ($("#order_applypay").length > 0) {
        //请求银行信息
        var orderid = $("#order_id").text();
        $(document).on("click", ".applypay_contract_upload", function () {
            var contractid = $(this).parent().parent().attr("contractid");
            var accountname = $(this).parent().parent().find("td").eq(0).text();
            accountname = encodeURIComponent(encodeURIComponent(accountname));
            var sellerLabel = $("#sellerLabel").val();//客户标示
            var settingValue = $("#settingValue").val();//是否需要上传合同
            var contractuploadurl = Context.PATH + '/order/contractupload.html?orderid=' + orderid + '&contractid=' + contractid + '&accountname=' + accountname + '&';
            //add by wangxianjun 白名单可以不用上传合同
            if(sellerLabel == "白名单" && settingValue == '0'){
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
            //cbms.getDialog("查看合同", contractviewurl);
           // window.open(contractviewurl);
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
                        //var infotext = bankinfoList[id].city + ' ' + bankinfoList[id].code;
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
            if(!showButtons()){
                return false;
            }
            var msg_confirm = "";
            $.ajax({
                type: 'post',
                url: Context.PATH + "/order/query/checkcontractupload.html",
                data: {
                    orderid: $("#order_id").text()
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
                                var url = Context.PATH + '/order/query/applypay.html';
                                cbms.loading();
                                $.post(url,
                                    {
                                        orderid: order_id,
                                        balancelist: JSON.stringify(balanceList),
                                        isDelayPay:  $("#isDelayPay").val()
                                    },
                                    function (re) {
                                        cbms.closeLoading();
                                        if (re.success) {
                                            //cbms.alert("操作成功");
                                            var menu = getUrlParam('menu');
                                            var url = Context.PATH + "/order/query/applypayment.html";
                                            if (menu != '') {
                                                url = Context.PATH + "/order/query/" + menu + ".html";
                                            }
                                            location.replace(url);
                                        }
                                        else {
                                            var handledCallback = null;
                                            if (re.data == orderHandledMsg) {
                                                handledCallback = orderHanlded;
                                            }
                                            cbms.alert(re.data, handledCallback);
                                        }
                                    });
                            }
                            else {
                                cbms.alert(msg);
                            }
                        });
                    } else {
                        cbms.alert(result.data);
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
    

    $("#order_payaudit_accept").click(function () {
        cbms.confirm("确定通过审核吗？", null, function () {
            var order_id = $("#order_id").text();
            var payrequest_id = $("#payrequest_id").text();
            var url = Context.PATH + '/order/query/auditpayaccept.html';
            cbms.loading();
            $.post(url, {orderid: order_id, payrequestid: payrequest_id}, function (re) {
                cbms.closeLoading();
                if (re.success) {
                    cbms.alert("操作成功");
                    var menu = getUrlParam('menu');
                    var url = Context.PATH + "/order/query/payment.html";
                    if (menu != '') {
                        url = Context.PATH + "/order/query/" + menu + ".html";
                    }
                    location.replace(url);
                }
                else {
                    var handledCallback = null;
                    if (re.data == orderHandledMsg) {
                        handledCallback = orderHanlded;
                    }
                    cbms.alert(re.data, handledCallback);
                }
            });
        });
    });
    $("#order_payaudit_refuse").click(function () {
        /* cbms.confirm("确定审核不通过吗？", null, function () {
         var order_id = $("#order_id").text();
         var payrequest_id = $("#payrequest_id").text();
         var url = Context.PATH + '/order/query/auditpayrefuse.html';
         $.post(url, {orderid: order_id, payrequestid: payrequest_id}, function (re) {
         if (re.success) {
         cbms.alert("操作成功");
         location.href = Context.PATH + "/order/query/payment.html";
         }
         else {
         cbms.alert(re.data);
         }
         });
         });
         */
        var ele = '<form id="order_payaudit_refuse_editForm"><div class="amount-box"><p>请填写不通过的理由!</p><p><textarea rows="4" cols="30" class="textarea" must="1" id="refuseNote"></textarea></p><p class="text-center">' +
            '<button type="button" class="btn btn-sm btn-primary" id="order_payaudit_refuse_confirm">确定</button>&nbsp;' +
            '<button type="button" class="btn btn-sm btn-default" id="order_payaudit_refuse_cancel">取消</button></p></div></form>'
        cbms.getDialog("不通过审核", ele);


    });

    //不通过弹出框的按钮事件
    //确定
    $("body").on('click', '#order_payaudit_refuse_confirm', function () {
        if (!setlistensSave("#order_payaudit_refuse_editForm")) return;
        var refuseNote = $("#refuseNote").val();
        var order_id = $("#order_id").text();
        var payrequest_id = $("#payrequest_id").text();
        var url = Context.PATH + '/order/query/auditpayrefuse.html';
        $("#order_payaudit_refuse_confirm").attr("disabled", true);
        $.post(url, {orderid: order_id, payrequestid: payrequest_id, note: refuseNote}, function (re) {
            $("#order_payaudit_refuse_confirm").removeAttr("disabled");
            if (re.success) {
                cbms.alert("操作成功");
                location.replace(Context.PATH + "/order/query/payment.html");
            }
            else {
                var handledCallback = null;
                if (re.data == orderHandledMsg) {
                    handledCallback = orderHanlded;
                }
                cbms.alert(re.data, handledCallback);
            }
        });
    });
    //取消
    $("body").on('click', '#order_payaudit_refuse_cancel,#order_fightback_cancel', function () {
        cbms.closeDialog();
    });

    if ($(".order_pay_confirm").length > 0) {
        $(".order_pay_confirm").click(function () {
            var button = $(this);
            var bankAccountTime = $("#bankAccountTime").val(); //银行出账时间
            if("" == bankAccountTime){
                cbms.alert("银行出账时间不能为空");
                return false;
            }
            cbms.confirm("确认已付款完成？", null, function () {
                $(".order_pay_confirm").prop("disabled", true);
                var order_id = $("#order_id").text();
                var paymentBank = button.attr("vtype");
                var payrequest_id = $("#payrequest_id").text();
                var url = Context.PATH + '/order/query/confirmpay.html';
                cbms.loading();
                $.post(url, {orderid: order_id, payrequestid: payrequest_id, paymentBank: paymentBank, bankAccountTime: bankAccountTime}, function (re) {
                    cbms.closeLoading();
                    $(".order_pay_confirm").removeAttr("disabled");
                    if (re.success) {
                        cbms.alert("操作成功");
                        var menu = getUrlParam('menu');
                        var url = Context.PATH + "/order/query/confirmpayment.html";
                        if (menu != '') {
                            url = Context.PATH + "/order/query/" + menu + ".html";
                        }
                        location.replace(url);
                    }
                    else {
                        var handledCallback=null;
                        if (re.data == orderHandledMsg) {
                            handledCallback = orderHanlded;
                        }
                        cbms.alert(re.data,handledCallback);
                    }
                });
            });
        });
    }

    //订单关闭(确认已付款状态)add by kongbinheng 20150906
    if ($("#order_pay_close").length > 0) {
        $("#order_pay_close").click(function () {
            //该交易单正在进行合同变更，请变更完成后操作！
            if(!showButtons()){
                return false;
            }
            var html="<form id='order_shut_editForm'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭订单的理由！'></textarea><br>"+
                "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
                "<button type='button'  class='btn btn-info btn-sm' id='order_pay_shut'>确定</button></div> </div></form>";
            cbms.getDialog("关闭订单",html);

        });
    }
    //订单关闭(确认已付款状态)
    $(document).on("click","#order_pay_shut",function(){
        if (!setlistensSave("#order_shut_editForm")) return;
        var order_id = $("#order_id").text();
        var payrequest_id = $("#payrequest_id").text();
        var url = Context.PATH + '/order/query/confirmclose.html';
        cbms.loading();
        $.post(url, {orderid: order_id, payrequestid: payrequest_id,cause:$("textarea[name='cause']").val()}, function (re) {
            cbms.closeLoading();
            if (re.success) {
                cbms.alert("操作成功");
                var menu = getUrlParam('menu');
                var url = Context.PATH + "/order/query/confirmpayment.html";
                if (menu != '') {
                    url = Context.PATH + "/order/query/" + menu + ".html";
                }
                location.replace(url);
            }
            else {
                var handledCallback = null;
                if (re.data == orderHandledMsg) {
                    handledCallback = orderHanlded;
                }
                cbms.alert(re.data, handledCallback);
            }
        });
    })
    //订单打回(待确认付款状态)add by lichaowei 20151029
    if ($("#order_fightback").length > 0) {
        $("#order_fightback").click(function () {
            var ele = '<form id="order_fightback_editForm">' +
                '<div class="amount-box">' +
                '<p>请填写打回的理由!</p>' +
                '<p><textarea rows="4" cols="30" class="textarea" must="1" id="fightbackNote"></textarea></p>' +
                '<p class="text-center">' +
                '<button type="button" class="btn btn-sm btn-primary" id="order_fightback_confirm">确定</button>&nbsp;' +
                '<button type="button" class="btn btn-sm btn-default" id="order_fightback_cancel">取消</button>' +
                '</p>' +
                '</div>' +
                '</form>';
            cbms.getDialog("打回交易单", ele);
        });
        //订单打回弹出框的按钮事件
        //确定
        $(document).on("click","#order_fightback_confirm",function(){
            if (!setlistensSave("#order_fightback_editForm")) return;
            var order_id = $("#order_id").text();
            var payrequest_id = $("#payrequest_id").text();
            var fightbackNote = $("#fightbackNote").val();
            var url = Context.PATH + '/order/query/fightback.html';
            $("#order_fightback_confirm").attr("disabled", "disabled");
            $.post(url, {orderId: order_id, payRequestId: payrequest_id, note: fightbackNote}, function (re) {
                if (re.success) {
                    $.gritter.add({
                        title: '',
                        text: '操作成功',
                        class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
                    });
                    location.replace(Context.PATH + "/order/query/printpendingpayapply.html");
                }
                else {
                    var handledCallback = null;
                    if (re.data == orderHandledMsg) {
                        handledCallback = orderHanlded;
                    }
                    cbms.alert(re.data, handledCallback);
                }
                $("#order_fightback_confirm").removeAttr("disabled");
            });
        });
    }
    $(document).on("click","#window_close",function(){
   		cbms.closeDialog();
    })

    // 订单关闭
    $(document).on("click","#order_shut",function(){
        if (!setlistensSave("#order_shut_editForm")) return;
        var order_id = $("#order_id").text();
        var url = Context.PATH + '/order/query/closeorder.html';
        cbms.loading();
        $.post(url, {orderid: order_id,cause:$("textarea[name='cause']").val()}, function (re) {
            if (re.success) {
                var menu = getUrlParam('menu');
                cbms.alert("申请关闭已提交成功,请等待审核");
                var url = Context.PATH + "/order/query/tradecloseapproval.html";
                if (menu != '') {
                    url = Context.PATH + "/order/query/" + menu + ".html";
                }
                location.replace(url);
            }
            else {
                var handledCallback = null;
                if (re.data == orderHandledMsg) {
                    handledCallback = orderHanlded;
                }
                cbms.alert(re.data, handledCallback);
            }
            //cbms.closeLoading();
        	$("#loading").remove();
            if($(".d-mask").length > 1){ 	
            	$(".d-mask").eq(1).remove();
            }
        });

    })

    if ($("#order_close").length > 0) {
        $("#order_close").click(function () {
            if(getUrlParam('menu') != "associate" && !showButtons()){
                return false;
            }

            var html="<form id='order_shut_editForm'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭订单的理由！'></textarea><br>"+
                "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
                "<button type='button'  class='btn btn-info btn-sm' id='order_shut'>确定</button></div> </div></form>";
           cbms.getDialog("关闭订单",html);

        });
    }
    if ($("#order_close_accept").length > 0) {
        $("#order_close_accept").click(function () {
            cbms.confirm("确定通过审核吗？", null, function () {
                var order_id = $("#order_id").text();
                var url = Context.PATH + '/order/query/auditcloseorder.html';
                cbms.loading();
                $.post(url, {orderid: order_id}, function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        var menu = getUrlParam('menu');
                        cbms.alert("操作成功");
                        var url = Context.PATH + "/order/query/tradecloseapproval.html";
                        if (menu != '') {
                            url = Context.PATH + "/order/query/" + menu + ".html";
                        }
                        location.replace(url);
                    }
                    else {
                        var handledCallback = null;
                        if (re.data == orderHandledMsg) {
                            handledCallback = orderHanlded;
                        }
                        cbms.alert(re.data, handledCallback);
                    }
                });
            });
        });
        $("#order_close_refuse").click(function () {
            /* cbms.confirm("确定审核不通过吗？", null, function () {
             var order_id = $("#order_id").text();
             var url = Context.PATH + '/order/query/auditcloserefuse.html';
             $.post(url, {orderid: order_id}, function (re) {
             if (re.success) {
             cbms.alert("操作成功");
             location.href = Context.PATH + "/order/query/tradecloseapproval.html";
             }
             else {
             cbms.alert(re.data);
             }
             });
             });*/


            var ele = '<form id="order_close_refuse_editForm"><div class="amount-box"><p>请填写不通过的理由!</p><p><textarea rows="4" cols="30" class="textarea" must="1" id="refuseNote"></textarea></p><p class="text-center">' +
                '<button type="button" class="btn btn-sm btn-primary" id="order_close_refuse_confirm">确定</button>&nbsp;' +
                '<button type="button" class="btn btn-sm btn-default" id="order_close_refuse_cancel">取消</button></p></div></form>'
            cbms.getDialog("不通过审核", ele);

        });

        //不通过弹出框的按钮事件
        //确定
        $("body").on('click', '#order_close_refuse_confirm', function () {
            if (!setlistensSave("#order_close_refuse_editForm")) return;
            var refuseNote = $("#refuseNote").val();
            var order_id = $("#order_id").text();
            var url = Context.PATH + '/order/query/auditcloserefuse.html';
            $("#order_close_refuse_confirm").attr("disabled", true);
            $.post(url, {orderid: order_id, note: refuseNote}, function (re) {
                $("#order_close_refuse_confirm").removeAttr("disabled");
                if (re.success) {
                    var menu = getUrlParam('menu');
                    cbms.alert("操作成功");
                    var url = Context.PATH + "/order/query/tradecloseapproval.html";
                    if (menu != '') {
                        url = Context.PATH + "/order/query/" + menu + ".html";
                    }
                    location.replace(url);
                }
                else {
                    var handledCallback = null;
                    if (re.data == orderHandledMsg) {
                        handledCallback = orderHanlded;
                    }
                    cbms.alert(re.data, handledCallback);
                }
            });
        });
        //取消
        $("body").on('click', '#order_close_refuse_cancel', function () {
            cbms.closeDialog();
        });
    }
    //申请回滚二结后的订单
    $(document).on("click","#order_twice_shut",function(){
        if (!setlistensSave("#order_shut_editForm")) return;
        var order_id = $("#order_id").text();
        var url = Context.PATH + '/order/query/rollbackApply.html';
        cbms.loading();
        $.post(url, {orderid: order_id,cause:$("textarea[name='cause']").val()}, function (re) {
            if (re.success) {
                cbms.alert("操作成功");
                var menu = getUrlParam('menu');
                var url = Context.PATH + "/order/query/invoice.html";
                if (menu != '') {
                    url = Context.PATH + "/order/query/" + menu + ".html";
                }
                location.replace(url);
            }
            else {
                var handledCallback = null;
                if (re.data == orderHandledMsg) {
                    handledCallback = orderHanlded;
                }
                cbms.alert(re.data, handledCallback);
            }
            //cbms.closeLoading();
        	$("#loading").remove();
            if($(".d-mask").length > 1){ 	
            	$(".d-mask").eq(1).remove();
            }
            
        });

    })

    //申请回滚二结后的订单
    if ($("#order_rollback").length > 0) {
        $("#order_rollback").click(function () {
            if(!showButtons()){
                return false;
            }

            var html="<form id='order_shut_editForm'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭订单的理由！'></textarea><br>"+
                "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
                "<button type='button'  class='btn btn-info btn-sm' id='order_twice_shut'>确定</button></div> </div></form>";
            cbms.getDialog("关闭订单",html);
        });
    }

    //总经理审核回滚
    if ($("#order_rollback_audit_accept").length > 0) {
        $("#order_rollback_audit_accept").click(function () {
            cbms.confirm("确定通过审核吗？", null, function () {
                var order_id = $("#order_id").text();
                var url = Context.PATH + '/order/query/rollbackAccept.html';
                cbms.loading();
                $.post(url, {orderid: order_id}, function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        cbms.alert("操作成功");
                        var menu = getUrlParam('menu');
                        var url = Context.PATH + "/order/query/tradecloseapproval.html";
                        if (menu != '') {
                            url = Context.PATH + "/order/query/" + menu + ".html";
                        }
                        location.replace(url);
                    }
                    else {
                        var handledCallback = null;
                        if (re.data == orderHandledMsg) {
                            handledCallback = orderHanlded;
                        }
                        cbms.alert(re.data, handledCallback);
                    }
                });
            });
        });
    }
    //财务审核回滚
    if ($("#order_rollback_confirm_accept").length > 0) {
        $("#order_rollback_confirm_accept").click(function () {

            cbms.confirm("确定关闭订单吗？", null, function () {
                var order_id = $("#order_id").text();
                var url = Context.PATH + '/order/query/rollbackConfirm.html';
                cbms.loading();
                $.post(url, {orderid: order_id}, function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        cbms.alert("操作成功");
                        var menu = getUrlParam('menu');
                        var url = Context.PATH + "/order/query/tradecloseapproval.html";
                        if (menu != '') {
                            url = Context.PATH + "/order/query/" + menu + ".html";
                        }
                        location.replace(url);
                    }
                    else {
                        var handledCallback = null;
                        if (re.data == orderHandledMsg) {
                            handledCallback = orderHanlded;
                        }
                        cbms.alert(re.data, handledCallback);
                    }
                });
            });
        });
    }

    //回滚审核不通过
    if ($("#order_rollback_audit_refuse").length > 0 || $("#order_rollback_confirm_refuse").length > 0){
        $("#order_rollback_audit_refuse,#order_rollback_confirm_refuse").click(function () {
            if(!showButtons()){
                return false;
            }

            var ele = '<form id="order_close_refuse_editForm"><div class="amount-box"><p>请填写不通过的理由!</p><p><textarea rows="4" cols="30" class="textarea" must="1" id="refuseNote"></textarea></p><p class="text-center">' +
                '<button type="button" class="btn btn-sm btn-primary" id="order_rollback_refuse_confirm">确定</button>&nbsp;' +
                '<button type="button" class="btn btn-sm btn-default" id="order_rollback_refuse_cancel">取消</button></p></div></form>'
            cbms.getDialog("不通过审核", ele);
        });
        $("body").on('click', '#order_rollback_refuse_confirm', function () {
            if (!setlistensSave("#order_close_refuse_editForm")) return;
            var refuseNote = $("#refuseNote").val();
            var order_id = $("#order_id").text();
            var url = Context.PATH + '/order/query/rollbackRefuse.html';
            $("#order_rollback_refuse_confirm").attr("disabled", true);
            $.post(url, {orderid: order_id, note: refuseNote}, function (re) {
                $("#order_rollback_refuse_confirm").removeAttr("disabled");
                if (re.success) {
                    cbms.alert("操作成功");
                    var menu = getUrlParam('menu');
                    var url = Context.PATH + "/order/query/tradecloseapproval.html";
                    if (menu != '') {
                        url = Context.PATH + "/order/query/" + menu + ".html";
                    }
                    location.replace(url);
                }
                else {
                    var handledCallback = null;
                    if (re.data == orderHandledMsg) {
                        handledCallback = orderHanlded;
                    }
                    cbms.alert(re.data, handledCallback);
                }
            });
        });
        //取消
        $("body").on('click', '#order_rollback_refuse_cancel', function () {
            cbms.closeDialog();
        });
    }

    if($.trim($("#order_note").attr("modify"))=="true")
    {
        $("#order_note").blur(function () {
            var note = $(this).val();
            var order_id = $("#order_id").text();
            if (note != '') {
                var url = "";
                $("#note_message").text("备注修改中...");
                $.ajax({
                    type: "POST",
                    url: Context.PATH + '/order/query/setnote.html',
                    data: {
                        orderId: order_id,
                        note: note
                    },
                    dataType: "json",
                    success: function (re) {
                        if (!re.success) {
                            $("#note_message").text("备注未成功保存！");
                        }
                        else {
                            $("#note_message").text("备注已保存");
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        $("#note_message").text("备注未成功保存！");
                    }
                });
            }
        });
    }
    else
    {
        $("#order_note").attr("disabled","disabled")
        $("#note_message").html("<br />");
    }
    $("*[dt=money]").each(function(){
        $(this).text(formatMoney($(this).text(), 2));
    });
    //add by wangxianjun 20160104 查看关联进项票
    $(".queryInInvoiceView").click(function(){
        var itemid =  $(this).parent().parent().attr("itemid");
        queryInvoiceInShow(itemid);
    });
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
    //add by wangxianjun 20160317 查看买家提单
    $("#queryPickup").click(function(){
        var orderid = $("#orderid").val();
        var contractviewurl = Context.PATH + '/order/pickup/pickupattachmentview.html?orderid=' + orderid + '&';
        cbms.getDialog("查看提货单", contractviewurl);
    });

 //var riskAccountName=   $("#itemsdatabody tr:first td:eq(1)")[0].innerText;
 //   riskHint(riskAccountName);

});

function queryInvoiceInShow(itemid){
    var invoiceurl = Context.PATH + "/order/query/relateininvoiceshow.html";
    $.ajax({
        type: 'post',
        url: invoiceurl,
        data: {
            orderItemid: itemid
        },
        error: function (s) {
        }
        , success: function (result) {
            var htmlStr = '<div class="table-bar"><table class="table table-striped table-bordered table-hover orderItemTable">'
                + '<thead><tr><th>开单时间</th><th>交易单号</th><th>合同号</th><th>买家全称</th><th>品名</th><th>材质</th><th>规格</th><th>采购单价</th>'
                + '<th>实提重量</th><th>实提采购金额</th><th>未到票重量</th><th>未到票金额</th></tr></thead><tbody class="itemsdatabody">'
            if (result && result.success) {
                htmlStr += '<tr><td>' + result.data.created + '</td>' + '<td>' + result.data.code + '</td>' + '<td>' + result.data.contractCode + '</td>'
                htmlStr += '<td>' + result.data.accountName + '</td>' + '<td>' + result.data.nsortName + '</td>' + '<td>' + result.data.material + '</td>'
                htmlStr += '<td>' + result.data.spec + '</td>' + '<td align="right">' + formatMoney(result.data.costPrice, 2) + '</td>' + '<td>' + result.data.actualPickWeightServer.toFixed(6) + '</td>'
                htmlStr += '<td align="right">' + formatMoney(result.data.actualPickAmount, 2) + '</td>' + '<td>' + result.data.unarriveInvoiceWeight.toFixed(6) + '</td>' + '<td align="right">' + formatMoney(result.data.unarriveInvoiceAmount, 2) + '</td></tr>'
            }
            htmlStr += '</tbody></table><table class="table table-striped table-bordered table-hover invoiceItemsTable"><thead><tr><th>录票时间</th><th>区位码</th><th>票号</th><th>卖家全称</th><th>货物或应税劳务名称</th>'
            htmlStr += '<th>规格型号</th><th>发票单价</th><th>票面数量/重量<br/>（吨）</th><th>票面价税合计</th><th>关联重量</th><th>关联价税合计</th></tr></thead><tbody class="itemsdatabody">';
            if (result && result.success) {
                for (var i = 0; i < result.data.orderItemsInvoiceInInfoList.length; i++) {
                    htmlStr += '<tr><td>' + result.data.orderItemsInvoiceInInfoList[i].created + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].areaCode + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].code + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].sellerName + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].nsortNameComb + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].typeOfSpec + '</td>' + '<td align="right">' + formatMoney(result.data.orderItemsInvoiceInInfoList[i].price,7) + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].checkWeight.toFixed(6) + '</td>' + '<td align="right">' + formatMoney(result.data.orderItemsInvoiceInInfoList[i].amount,2) + '</td>' + '<td>' + result.data.orderItemsInvoiceInInfoList[i].weight.toFixed(6) + '</td>' + '<td align="right">' + formatMoney(result.data.orderItemsInvoiceInInfoList[i].amount,2) + '</td></tr>';
                }
            } else {
                htmlStr += '</tbody></table></div>';
            }
            $("#invoiceView").html(htmlStr);
            cbms.getDialog("查看关联", "#invoiceView");
        }
    });
}
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

$("#printpendingpayapplystrike").click(function(){
	var printFloat=$("#print_float_layer").val();
	if(printFloat==''||printFloat==null){
		printFloat="您确定打印付款申请单？"
	}
	 cbms.confirm(printFloat, null, function () {
	        $("#printpendingpayapplyok")[0].click();
	    });
	
})

$("#printcontract").click(function(){
    $("#printcontractok")[0].click();
})


/**
 * 正在进行合同变更的订单，禁止除“返回”外的按钮操作
 */
function showButtons() {
    //var type = $("#alterStatus").val();
    var orderId = $("#orderid").val();
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

