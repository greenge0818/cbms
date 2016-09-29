$("#form1").verifyForm();
$("#form2").verifyForm();

$(document).on("click", "#nextStepBtn", function () {
    if (setlistensSave("#form1")) {
        $("#nextStepBtn").css("display", "none");
        $("#step_one_div").css("display", "none");
        $("#saveInfoBtn").css("display", "inline-block");
        $("#step_two_div").css("display", "block");
        $("#preStepBtn").css("display", "inline-block");
        $("#step_two_span").css("color", "red");
        $("#step_one_span").css("color", "black");
    }
});

$(document).on("click", "#preStepBtn", function () {
    $("#nextStepBtn").css("display", "inline-block");
    $("#step_one_div").css("display", "block");
    $("#saveInfoBtn").css("display", "none");
    $("#step_two_div").css("display", "none");
    $("#preStepBtn").css("display", "none");
    $("#step_two_span").css("color", "black");
    $("#step_one_span").css("color", "red");
});

$(document).on("click", "#saveInfoBtn", function () {
    if (setlistensSave("#form2")) {
        $("input[name='contactName']").val($("#contactName").val());
        $("input[name='contactTel']").val($("#contactTel").val());
        $("input[name='contactDeptName']").val($("#contactDeptName").val());
        $("input[name='contactQq']").val($("#contactQq").val());
        $("input[name='contactEmail']").val($("#contactEmail").val());
        $("input[name='contactNote']").val($("#contactNote").val());
        $("input[name='isconfirm']").val("false");
        $('#form1').ajaxSubmit({
            success: function (data) {
                if (data) {
                    if (data.success) {
                        cbms.alert("添加成功！");
                        location.href = Context.PATH + "/account/" + moduleType + "/" + data.data + ".html";
                    } else {
                        cbms.alert(data.data);
                    }
                }
            }
        });
    }
});

//交点移开后触发
$("#acount_name").blur(function () {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/account/selectAcoount.html",
        data: {
            "name": $("#acount_name").val(),
            "type": $("#type").val()
        },
        success: function (data) {
            if (data.code == 0) {
                cbms.confirm(data.data, null, threeAjaxFunc);
            } else if (data.code == 1) {
                cbms.confirm(data.data, null, sendAjaxFunc);
            } 
//            else {
//                cbms.alert(data.data);
//            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });

    function sendAjaxFunc() {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/account/update/buyer.html",
            data: {
                "name": $("#acount_name").val(),
                "type": $("#type").val()
            },
            success: function (data) {
                if (data.success) {
                    cbms.alert("添加成功！");
                    location.href = Context.PATH + "/account/" + moduleType + "/" + data.data + ".html";
                } else {
                    cbms.alert("添加失败！");
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    }

    function threeAjaxFunc() {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/account/update/seller.html",
            data: {
                "name": $("#acount_name").val(),
                "type": $("#type").val()
            },
            success: function (data) {
                $("#account_id").val(data.data.id);
                $("#type").val("both");
                $("#addr").val(data.data.addr);
                $("#zip").val(data.data.zip);
                $("#tel").val(data.data.tel);
                $("#fax").val(data.data.fax);
                $("#legalPersonName").val(data.data.legalPersonName);
                $("#mobil").val(data.data.mobil);
                $("#business").val(data.data.business);
                $("#webSiteUrl").val(data.data.webSiteUrl);
                $("#proxyFactory").val(data.data.proxyFactory);
                $("#proxyQty").val(data.data.proxyQty);
                $("#licenseCode").val(data.data.licenseCode);
                $("#regAddress").val(data.data.regAddress);
                $("#orgCode").val(data.data.orgCode);
                $("#bankNameMain").val(data.data.bankNameMain);
                $("#bankNameBranch").val(data.data.bankNameBranch);
                $("#bankCode").val(data.data.bankCode);
                $("#taxCode").val(data.data.taxCode);
                $("#accountCode").val(data.data.accountCode);
                $("#businessType").val(data.data.businessType);
                $("#province").val(data.data.provinceId);
                $("#province").trigger("change");
                $("#city").val(data.data.cityId);
                $("#city").trigger("change");
                $("#district").val(data.data.districtId);
                $("#bankProvince").val(data.data.bankProvinceId);
                $("#bankCity").val(data.data.bankCityId);

            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    }
});

$(document).on("input", "#contactName", function () {
    checkContact();
});

$(document).on("input", "#contactTel", function () {
    checkContact();
});

function checkContact() {
    var name = $("#contactName").val();
    var tel = $("#contactTel").val();
    if (name.length >  0 && tel.length == 11) {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/account/selectContact.html",
            data: {
                "name": name,
                "tel": tel
            },
            success:  function (response, textStatus, xhr) {
                if (response.success) {
                    info = response.data;
                    cbms.alert("检测到该联系人，为您自动添加该联系人信息");
                    $("#contactDeptName").val(info.deptName);
                    $("#contactQq").val(info.qq);
                    $("#contactEmail").val(info.email);
                    $("#contactNote").val(info.note);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    }
}
