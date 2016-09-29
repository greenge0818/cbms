/**
 * 修改账号基本信息
 * Created by lichaowei on 2016/3/2.
 */
var _verify = false;        // 数据验证是否通过

$().ready(function () {
    $("#editForm").verifyForm();

    $("#acountName").change(function () {
        searchAccountByName($.trim($(this).val()));
    });

    // 卖家显示代理钢厂信息
    $("input[name='nature'][value='2']").click(function () {
        if ($(this).prop("checked")) {
            _isSeller = true;
            $(".orgtd").show();
        }
        else {
            _isSeller = false;
            $(".orgtd").hide();
        }
    });

    // 保存
    $("#saveInfoBtn").click(function () {

        if (!setlistensSave() || !verify()) return;

        save(function () {
            location.href = Context.PATH + "/company/" + $("#accountId").val() + "/credentialsinfo.html";
        });
    });
});

/**
 *  根据名称查询公司
 * @param name 名称
 */
function searchAccountByName(name) {
    if (name == "")return;
    if ($("#acountNameHidden").val() == name)return;
    $.ajax({
        type: "POST",
        url: Context.PATH + "/account/searchaccountbyname.html",
        data: {
            "name": name
        },
        success: function (data) {
            if (data.success) {
            		cbms.alert("该账号已经存在！");
            } else {
                _verify = true;
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

/**
 * 获取AccountTag
 * @returns {number}
 */
function getAccountTag() {
    var tag = 0;
    $("input[name='nature']:checked").each(function () {
        var tempTag = parseInt($(this).val(), 10);
        tag = tag | tempTag;
    });
    return tag;
}

/**
 * 验证
 */
function verify() {
    var accountTag = getAccountTag();
    if (accountTag == 0) {
        cbms.alert("请选择客户性质！");
        return false;
    }
    var orgId = $("#org").val();
    if (orgId == "" && _isSeller) {
        cbms.alert("请选择归属服务中心！");
        return false;
    }
    var businessType = $("#businessType").val();
    if (businessType == "") {
        cbms.alert("请选择客户类型！");
        return false;
    }
    return true;
}

/**
 * 保存
 * @param callback 回调函数
 */
function save(callback) {
    var orgId = $("#org").val();
    if(!_isSeller){
        orgId="";
    }
    var accountData = {
        // 公司基本信息
        'account.id': $("#accountId").val(),
        'account.name': $.trim($("#acountName").val()),
        'account.accountTag': getAccountTag(),
        'account.orgId': orgId == "" ? "0" : orgId,
        'account.orgName': orgId == "" ? "" : $.trim($("#org").find("option:selected").text()),
        'account.businessType': $("#businessType").val(),

        //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
        'account.registerCapital':$("#registerCapital").val(),
        'account.supplierLabel': $("#supplierLabel").val(),
        'account.buyerLabel': $("#buyerLabel").val(),
        'account.paymentLabel': $("#paymentLabel").val(),
        'account.registerDate': $("#registerDate").val(),
        'account.sellerSingleTradeWeight':$("#sellerSingleTradeWeight").val(),
        'account.sellerAllTradeQuality':$("#sellerAllTradeQuality").val(),
        'account.sellerAllTradeWeight':$("#sellerAllTradeWeight").val(),
        'account.isSellerPercent':$('input[name="isSellerPercent"]:checked ').val(),
        'account.registerDate': $("#registerDate").val(),


        'accountExt.name': $.trim($("#acountName").val()),
        'accountExt.zip': $.trim($("#zip").val()),
        'accountExt.mailAddr': $.trim($("#mailAddr").val()),
        'accountExt.mobil': $.trim($("#mobile").val()),
        'accountExt.tel': $.trim($("#tel").val()),
        'accountExt.fax': $.trim($("#fax").val()),
        'accountExt.legalPersonName': $.trim($("#legalPersonName").val()),
        'accountExt.webSiteUrl': $.trim($("#webSiteUrl").val()),
        'accountExt.provinceId': $.trim($("#province").val()),
        'accountExt.cityId': $.trim($("#city").val()),
        'accountExt.districtId': $.trim($("#district").val())
    };
    cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/company/saveedit.html",
        data: accountData,
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.gritter('客户资料修改成功！', true, callback());
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("修改失败");
            }
        }
    });
}

