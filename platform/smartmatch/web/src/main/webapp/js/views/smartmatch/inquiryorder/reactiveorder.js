$(document).ready(function () {
    $.post(Context.PATH + '/common/organizationList.html','',function(result){
        $("#reactive_manager_org").empty();
        $("#reactive_manager_org").append("<option value='0'>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#reactive_manager_org").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }

        $("#reactive_user_org").empty();
        $("#reactive_user_org").append("<option value='-1'>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#reactive_user_org").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }
    });
    $.post(Context.PATH + '/common/getnetsalers.html','',function(result){
        $("#reactive_iseller_select").empty();
        $("#reactive_iseller_select").append("<option value='0'>-选择网销人员-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#reactive_iseller_select").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }
    });
    $(document).on("click", "#btnreactive", function () {
        var url = Context.PATH + '/smartmatch/purchaseorder/orderAssign.html';
        var order_id = $("#reactive_purchase_orderid").val();
        var org_id = 0,reactiveto=0;
        var reactive_user = $("#reactive_user").not("#reactive_dialog").attr("userid");
        var reactive_iseller = $("#reactive_iseller_select").val();
        var userid = null;

        if ($("#reactiveto_manager").not("#reactive_dialog").is(":checked")) {
            if ($("#reactive_manager_org").val() == '0' || $("#reactive_manager_org").val() == '' || $("#reactive_manager_org").val() == '-1') {
                cbms.alert("请选择服务中心");
                return;
            }
            org_id=$("#reactive_manager_org").val();
            reactiveto = 1;
        }
        else if ($("#reactiveto_trader").not("#reactive_dialog").is(":checked")) {
            if ($("#sorganizationHidden").val()=='-1') {
                cbms.alert("请选择服务中心");
                return;
            }
            if (reactive_user != "0") {
                userid = reactive_user;
            } else {
                cbms.alert("请选择交易员");
                return;
            }
            org_id=$("#sorganizationHidden").val();
            reactiveto = 2;
        }
        else if ($("#reactiveto_iseller").not("#reactive_dialog").is(":checked")) {
            if (reactive_iseller != "0") {
                userid = reactive_iseller;
            }
            else {
                cbms.alert("请选择网销人员");
                return;
            }
            reactiveto = 3;
        } else {
            cbms.alert("请选择指派人员");
            return;
        }
        if (org_id == -1 || org_id == 0) {
            org_id = null;
        }
        $.post(url,
            {
                id: order_id,
                assignto: reactiveto,
                orgId: org_id,
                userId: userid,
                activate: 1
            }, function (re) {
                if (re.success) {
                    cbms.closeDialog();
                    cbms.gritter('激活成功！', true, function () {
                        if (typeof(PurchasePage) != 'undefined' && PurchasePage.dt) {
                            PurchasePage.dt.ajax.reload();
                        } else {
                            location.href = Context.PATH + '/smartmatch/purchaseorder/list.html';
                        }
                    });
                }
                else {
                    cbms.alert(re.data);
                }
            });
    });

    $(document).on("change","#reactive_user_org",function(){
        $(this).closest("div").find("#sorganizationHidden").val($(this).val());
    });

    $(document).on("click", "#reactive_cancel", function () {
        cbms.closeDialog();
    });
});