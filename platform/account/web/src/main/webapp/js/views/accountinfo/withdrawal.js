/**
 * 账户提现页面js
 *
 */

$(document).ready(function() {
    //提现银行选择事件
    $(document).on("change", "#bank", function(){
        var selected = $(this).children("option:selected");
        $("#bankAccountCode").html($(selected).attr("bankAccountCode"));
        $("#bankCode").html($(selected).attr("bankCode"));
    });

    //提现金额输入框输入事件
    $(document).on("input", "input[name='amount']", function(){
        var pattern = /^\d+(\.\d{1,2})?$/;
        var amount = $(this).val();
        if(pattern.test(amount) && (amount*1 <= _balance*1) && (amount*1 > 0)){
            $("#submit").removeAttr("disabled");
        }else{
            $("#submit").attr("disabled", "disabled");
        }
    });

    //提现按钮点击事件
    $(document).on("click", "#submit", function(){
        cbms.loading();
        $.ajax({
            type: "POST",
            url: Context.PATH + "/accountinfo/withdrawalapply.html",
            data: {
                departmentId: _departmentId,
                bankId: $("#bank").children("option:selected").val(),
                amount: $("input[name='amount']").val(),
                balance: _balance
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                cbms.closeLoading();
                if (response.success) {
                    cbms.gritter("已提交成功，请等待办事处总经理的审核", true, function(){
                        window.location.href =  $("a.accountinfo").attr("href");
                    });
                } else {
                    cbms.gritter(response.data, false);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        })
    });
});