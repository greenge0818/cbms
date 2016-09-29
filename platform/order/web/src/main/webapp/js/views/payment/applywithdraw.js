/**
 * Created by lcw on 2015/7/26.
 */
$().ready(function () {
    $("#applyWithdraw").verifyForm();

    // 提交
    $("#submit").click(function () {
        if (!setlistensSave())return;
        var money = 0;
        var tempMoney = $.trim($("#money").val());
        money = parseFloat(cbms.convertFloat(tempMoney,2));
        if (isNaN(money) || money == 0) {
            cbms.alert("请输入正确的提现金额！");
            return;
        }
        var available = parseFloat($("#availableAmount").val());
        money = money.toFixed(2);
        if (money > available) {
            cbms.alert("申请提现的金额超过可提现余额！");
            return;
        }

        var bankId = $("#bankList").val();
        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/payment/saveapply.html",
            data: {
                bankId: bankId,
                money: money,
                balance: available
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result && result.success) {
                    cbms.alert("申请提现成功，请等待审核！");
                    var backUrl = $("#backUrl").attr("href");
                    setTimeout("location='" + backUrl + "';", 3000);
                } else {
                	if (result.data != null) {
                		cbms.alert(result.data);
                	} else {
                		cbms.alert("获取数据失败!");
                	}
                }
            }

        });
    });

    // 选择提现银行
    $("#bankList").change(function () {
        var option = $("#bankList").find("option:selected");
        var bankAccountCode = $(option).attr("code");
        var bankCode = $(option).attr("bankcode");
        $("#bankCode").text(bankCode);
        $("#bankAccountCode").text(bankAccountCode);
    });

    // 默认选中第一个
    $("#bankList").change();
});