
jQuery(function ($) {
    // 立即充值
    $(document).on("click", "#errorRecharge", function(){
        var payeeName = $("#payeeName").val();
        cbms.confirm("确认立即充值？", null, function () {
            errorRecharge();
        });
    });

    // 立即处理
    $(document).on("click", "#errorRefund", function(){
        cbms.confirm("确认立即处理？", null, function () {
            errorRefund();
        });
    });
});

// 立即充值
function errorRecharge(){
    var id = $("#id").val();
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + '/order/banktransaction/errorRecharge.html',
        data: {
            "id" : id
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("立即充值成功", function(){
                        window.location.href = Context.PATH + "/order/banktransaction/payerror.html";
                    });
                } else {
                    cbms.alert(result.data, function(){
                        window.location.href = Context.PATH + "/order/banktransaction/payerror.html";
                    });
                }
            } else {
                cbms.alert("立即充值失败");
            }
        }
    });
}

// 立即处理
function errorRefund(){
var id = $("#id").val();
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + '/order/banktransaction/errorRefund.html',
        data: {
            "id" : id
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("立即处理成功", function(){
                        window.location.href = Context.PATH + "/order/banktransaction/payerror.html";
                    });
                } else {
                    cbms.alert(result.data, function(){
                        window.location.href = Context.PATH + "/order/banktransaction/payerror.html";
                    });
                }
            } else {
                cbms.alert("立即处理失败");
            }
        }
    });
}
