jQuery(function ($) {
    $("#batchConfirm").verifyForm();

    $(document).on("click", "#submit", function () {
        if (!setlistensSave("#batchConfirm")) {
            return;
        }

        $("#confirmTable tbody tr").each(function () {
            var tr = $(this);
            var code = $(this).find(".code").val();
            var amount = $(this).find(".amount").val();
            if(code != "" && amount != "") {
                $.ajax({
                    type: "POST",
                    url: Context.PATH + '/invoice/out/confirm.html',
                    data: {
                        invoiceOutCode: code,
                        invoiceOutAmount: amount
                    },
                    dataType: "json",
                    success: function (response, textStatus, xhr) {
                        if (response.success) {
                            tr.find(".code").val("");
                            tr.find(".amount").val("");
                            tr.find(".prompt").html("发票确认成功");
                        } else {
                            tr.find(".prompt").html("输入的发票号或者金额有误，请联系财务确认");
                        }
                    }
                });
            }else{
                tr.find(".prompt").html("输入数据有误");
            }
            setTimeout('$(".prompt").html("")', 5000);
        });
    });
});