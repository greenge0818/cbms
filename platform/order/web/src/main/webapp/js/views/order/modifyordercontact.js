/**
 * create by  wangxianjun
 *  查询订单
 */
var checkTel = '';//选中的正确反利手机号
jQuery(function ($) {
    //搜索事件
    $("#queryBtn").click(function () {
        getBuyerContacts();
        $("#tradeCode").val($("#code").val())
    });
    // 选择交易员列表
    $("#tel").change(function () {
        checkTel = $(this).find("option:selected").text();
        if($(this).val() != ""){
            $("#newContact").show();
        }else{
            $("#newContact").hide();
        }
        var checkList =$(this).val().split("|");
        $("#newContactId").val(checkList[0]);
        $("#newContactName").val(checkList[1]);

    });
    $("#confirmmRebate").click(function () {
        updateOrderRebate();
    });
    $("#modifyRebate").click(function () {
        modifyOrderRebate();
    });
    $("#rebackRebate").click(function () {
        rebackOrderRebate();
    });

});

//获取订单买家联系人信息
function getBuyerContacts() {
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/query/checkorder.html",
        data: {
            code: $("#code").val()
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result && result.success) {
                var url = Context.PATH + "/order/query/queryOrderContact.html";
                $("#orderList").show();
                $("#confirmAmount").show();
                $.ajax({
                    type: 'post',
                    url: url,
                    data: {
                        code:  result.data.code
                    },
                    error: function (s) {
                    }
                    , success: function (result) {
                        if (result && result.success) {
                            $("#accountName").val(result.data.accountName);
                            $("#contactName").val(result.data.contactName);
                            $("#oldContactTel").val(result.data.contactTel);
                            $("#rebateAmount").val(result.data.rebateAmount);
                            if(result.data.rebateAmount == '0'){
                                $("#modifyRebate").hide();
                                $("#rebackRebate").hide();
                            }else{
                                $("#modifyRebate").show();
                                $("#rebackRebate").show();
                            }
                            var telOptions = "";
                            telOptions += "<option value=''>请选择</option>";
                            for (var i = 0; i < result.data.accountContactList.length; i++) {
                                telOptions += "<option value='" + result.data.accountContactList[i].id +"|" + result.data.accountContactList[i].name + "'>" + result.data.accountContactList[i].tel + "</option>";
                            }
                            $("#tel").html(telOptions);
                        }
                    }

                });
            } else {
                //"该笔交易单号无效，请重新输入！"
                cbms.alert(result.data);
                $("#orderList").hide();
                $("#confirmAmount").hide();
            }
        }

    });
}
//更新返利金额和订单中新的买家联系人
function updateOrderRebate(){
    var orderData = {
        code: $("#tradeCode").val(),
        newContactId: $("#newContactId").val(),
        newContactName: $("#newContactName").val(),
        newContactTel: checkTel,
        oldContactTel: $("#oldContactTel").val(),
        rebateAmount: $("#rebateAmount").val()
    };

    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/query/updateorderRebates.html",
        data: {
            orderJson: JSON.stringify(orderData)
        },
        error: function (s) {
        }
        , success: function (result) {
            cbms.alert(result.data,function(){
                if (result.success) {
                    location.href = Context.PATH + "/order/query/modifyordercontact.html";
                }else {
                    $("#orderList").show();
                    $("#confirmAmount").show();
                }
            });

        }

    });
}
//更新返利金额和订单中新的买家联系人
function modifyOrderRebate(){
    if(checkTel == ''){
        cbms.alert("请选择正确返利手机号码！");
        return false;
    }
    var orderData = {
        code: $("#tradeCode").val(),
        newContactId: $("#newContactId").val(),
        newContactName: $("#newContactName").val(),
        newContactTel: checkTel,
        oldContactTel: $("#oldContactTel").val(),
        rebateAmount: $("#rebateAmount").val()
    };

    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/query/modifyOrderRebates.html",
        data: {
            orderJson: JSON.stringify(orderData)
        },
        error: function (s) {
        }
        , success: function (result) {
            cbms.alert(result.data,function(){
                if (result.success) {
                    location.href = Context.PATH + "/order/query/modifyordercontact.html";
                }else {
                    $("#orderList").show();
                    $("#confirmAmount").show();
                }
            });

        }

    });
}

//回滚返利金额和更新订单中新的买家联系人
function rebackOrderRebate(){
    if(checkTel == ''){
        cbms.alert("请选择正确返利手机号码！");
        return false;
    }
    var orderData = {
        code: $("#tradeCode").val(),
        newContactId: $("#newContactId").val(),
        newContactName: $("#newContactName").val(),
        newContactTel: checkTel,
        oldContactTel: $("#oldContactTel").val(),
        rebateAmount: $("#rebateAmount").val()
    };

    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/query/rebackOrderRebates.html",
        data: {
            orderJson: JSON.stringify(orderData)
        },
        error: function (s) {
        }
        , success: function (result) {
            cbms.alert(result.data,function(){
                if (result.success) {
                    location.href = Context.PATH + "/order/query/modifyordercontact.html";
                }else {
                    $("#orderList").show();
                    $("#confirmAmount").show();
                }
            });

        }

    });
}