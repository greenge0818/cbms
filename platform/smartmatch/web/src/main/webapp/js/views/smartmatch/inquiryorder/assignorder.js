$(document).ready(function () {
    $.post(Context.PATH + '/common/organizationList.html','',function(result){
        $("#assign_manager_org").empty();
        $("#assign_manager_org").append("<option value='0'>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#assign_manager_org").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }

        $("#sorganizationHidden").empty();
        $("#sorganizationHidden").append("<option value='-1'>-选择服务中心-</option>");
        if(result.success){
        	var html = "";
            for (var i = 0; i < result.data.length; i++) {
            	html += "<option value='" + result.data[i].id + "'";
            	
            	try{//如果orgId不为空
	        		if(orgId == result.data[i].id){
	                 	html += " selected='selected' ";
	                }
            	} catch(e){//如果orgId为空则不做处理
            	}
               
                html += " >" + result.data[i].name + "</option>";
            }
            
            $("#sorganizationHidden").append(html);
        }
    });
    $(document).on("click", "#btnassign", function () {
        var url = Context.PATH + '/smartmatch/purchaseorder/orderAssign.html';
        var order_id = $("#assign_purchase_orderid").val();
        var org_id = 0, assignto = 0;
        var assign_user = $("#assign_user").not("#assign_dialog").attr("userid");
        var userid = null;

        if ($("#assignto_manager").not("#assign_dialog").is(":checked")) {
            if ($("#assign_manager_org").val() == '0' || $("#assign_manager_org").val() == '' || $("#assign_manager_org").val() == '-1') {
                cbms.alert("请选择服务中心");
                return;
            }
            org_id=$("#assign_manager_org").val();
            assignto = 1;
        }
        else if ($("#assignto_trader").not("#assign_dialog").is(":checked")) {
            if ($("#sorganizationHidden").val()=='-1') {
                cbms.alert("请选择服务中心");
                return;
            }
            if (assign_user != "0") {
                userid = assign_user;
            } else {
                cbms.alert("请选择交易员");
                return;
            }
            org_id=$("#sorganizationHidden").val();
            if(is_pd_open_bill){
            	assignto = 4;
            }else{
            	assignto = 2;
            }
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
                assignto: assignto,
                orgId: org_id,
                userId: userid
            }, function (re) {
                if (re.success) {
                    cbms.closeDialog();
                    cbms.gritter('指派成功！', true, function () {
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

    $(document).on("click", "#btnassigntonetsale", function () {
        var url = Context.PATH + '/smartmatch/purchaseorder/orderAssign.html';
        var order_id = $("#assigntonetsale_purchase_orderid").val();
        var org_id = 0;
        var assign_iseller = $("#assign_iseller_select").val();
        var userid = null;

        if (assign_iseller != "0") {
            userid = assign_iseller;
        }
        else {
            cbms.alert("请选择人员");
            return;
        }
        assignto = 3;
        if (org_id == -1 || org_id == 0) {
            org_id = null;
        }
        $.post(url,
            {
                id: order_id,
                assignto: assignto,
                orgId: org_id,
                userId: userid
            }, function (re) {
                if (re.success) {
                    cbms.closeDialog();
                    cbms.gritter('退回成功！', true, function () {
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

    $(document).on("click", "#assign_cancel", function () {
        cbms.closeDialog();
    });
    $(document).on("click", "#assigntonetsale_cancel", function () {
        cbms.closeDialog();
    });
});

function initTraders(userId){
    if(userId){
        $.post(Context.PATH + '/smartmatch/purchaseorder/getBackoffUsers.html?userId='+userId,'',function(result){
            $("#assign_iseller_select").empty();
            $("#assign_iseller_select").append("<option value='0'>-选择人员-</option>");
            if(result.success){
                for (var i = 0; i < result.data.length; i++) {
                    $("#assign_iseller_select").append("<option value='" + result.data[i].id + "'"+(userId==result.data[i].id?" selected":"")+">" + result.data[i].name + "</option>");
                }
            }
        });
    }
}