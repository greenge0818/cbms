/**
 * 预付款详情操作
 * Created by lixiang on 2016/3/9.
 */
$().ready(function () {

    /**
     * 审核通过
     */
    $("#checkPassed").click(function () {
        cbms.confirm("确定通过审核吗？", null, function () {
            check(true, "");
        });
    });

    /**
     * 审核不通过
     */
    $("#checkNotPassed").click(function () {
        var content = "<div class='page-content dialog-m'>";
        content += "<ul>";
        content += "<li>";
        content += "<span>请填写不通过的理由！</span>";
        content += "</li>";
        content += "<li>";
        content += "<textarea style='width:300px' id='declineReason'></textarea>";
        content += "</li>";
        content += "<li class='table-bar text-center'>";
        content += "<button type='button' id='define' class='btn btn-sm btn-primary'>确定</button>&nbsp;&nbsp;";
        content += "<button type='button' id='cancel' class='btn btn-sm btn-default'>取消</button>";
        content += "</li>";
        content += "</ul>";
        content += "</div>";
        cbms.getDialog("不通过审核", content);
    });

    /**
     * 确认不通过审核
     */
    $(document).on("click", "#define", function () {
        var declineReason = $("#declineReason").val();
        if (declineReason != "") {
            check(false, declineReason);
        }
        else {
            cbms.alert("请输入不通过理由！");
        }
    });

    /**
     * 取消不通过审核
     */
    $(document).on("click", "#cancel", function () {
        cbms.closeDialog();
    });
    
    /**
     * 确认付款
     */
    $(".confirm").click(function () {
        var id = $("#requestId").val();
        var button = $(this);
        var paymentBank = button.attr("vtype");
        var bankAccountTime = $("#bankAccountTime").val(); //银行出账时间
        if("" == bankAccountTime){
            cbms.alert("银行出账时间不能为空");
            return false;
        }
        cbms.confirm("确认付款？", null, function () {
            cbms.loading();
            $.ajax({
                type: 'post',
                url: Context.PATH + "/order/query/confirmrequest.html",
                data: {
                    id: id,
                    paymentBank: paymentBank,
                    bankAccountTime : bankAccountTime
                },
                error: function (s) {
                    cbms.closeLoading();
                },
                success: function (result) {
                    cbms.closeLoading();
                    if (result && result.success) {
                    	cbms.alert("提交成功！", function() {
            				var url = Context.PATH + "/order/query/confirm/request.html";
            				location.replace(url);
            			});
                    } else {
                    	cbms.alert(result.data, function() {
            				var url = Context.PATH + "/order/query/confirm/request.html";
            				location.replace(url);
            			});
                    }
                }
            });
        });
    });

});

/**
 * 审核预付款单
 * @param pass true：通过，false：不通过
 * @param declineReason 不通过理由
 */
function check(pass, declineReason) {
    var requestId = $("#requestId").val();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/query/check/advance/payment.html",
        data: {
        	requestId: requestId,
            check: pass,
            declineReason: declineReason
        },
        error: function (s) {
        },
        success: function (result) {
            if (pass == false) {
                cbms.closeDialog();
            }
            if (result && result.success) {
            	cbms.alert("提交成功！", function() {
    				var url = Context.PATH + "/order/query/paymentapplication.html";
    				location.replace(url);
    			});
            } else {
                cbms.alert(result.data, function() {
    				var url = Context.PATH + "/order/query/paymentapplication.html";
    				location.replace(url);
    			});
            }
        }
    });
}
