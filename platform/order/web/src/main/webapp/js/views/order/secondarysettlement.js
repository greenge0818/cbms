jQuery(function ($) {
    $(".table").on("click", ".edit", function () {
        var strs = $(this).attr("rel").split("_"); //字符分割
        var orderId = strs[0];
        var sellerId = strs[1];
        if(!showButtons(orderId)){
            return false;
        }

        $.ajax({
            type: "POST",
            url: Context.PATH + '/order/secondarysettlement/loadDataForEdit.html',
            data: {
                orderId: orderId,
                sellerId: sellerId
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    var datas = response.data;
                    html = '<div id="editDialog" class="table-bar"><table class="table table-striped table-bordered table-hover" id="editTable"><thead>' +
                    '<tr><th>序号</th><th>品名</th><th>材质</th><th>规格</th><th>厂家</th><th>数量</th><th>重量（吨）</th><th>销售价（元）</th>' +
                    '<th>实提总<br/>数量</th><th>实提总<br/>重量（吨）</th></tr></thead><tbody></tbody></table>' +
                    '</div><div class="btn-bar text-center" style="margin-bottom: 10px;"> <button id="commit" class="btn btn-info btn-sm">确定</button> <button id="cancel" class="btn btn-default btn-sm">取消</button> </div>';
                    cbms.getDialog("输入总实提量", html);
                    var count = 1;
                    for (var i in datas) {
                        var costPeiceHtml = '';
                        if(datas[i].isPayedByAcceptDraft){
                            costPeiceHtml = '<br/><H6 class="bg-yellow-style"><span class="red bolder">银票支付</span></H6>'
                        }
                        $("#editTable tbody").append('<tr> <td>' + count++ + '</td>' +
                        '<td>' + datas[i].nsortName + '</td>' +
                        '<td>' + datas[i].material + '</td>' +
                        '<td>' + datas[i].spec + '</td>' +
                        '<td>' + datas[i].factory + '</td>' +
                        '<td>' + datas[i].quantity + '</td>' +
                        '<td>' + cbms.convertFloat(datas[i].weight) + '</td>' +
                        '<td>' + cbms.convertFloat(datas[i].dealPrice, 2) + costPeiceHtml + '</td>' +
                        '<td><input type="text" verify="number" class="d-text quantity" rel="' + datas[i].id + '"/></td>' +
                        '<td><input type="text" verify="weight" class="d-text weight" /></td> </tr>');
                    }

                    $("#editTable").verifyForm();

                    $(document).on("click", "#cancel", function () {
                        cbms.closeDialog();
                    });

                    $(document).on("click", "#commit", function () {

                        if (!setlistensSave("#editTable")) {
                            return;
                        }
                        var itemList = new Array();
                        var commit = true;
                        $("#editTable tbody tr").each(function (i) {
                            var quantity = $(this).find(".quantity").val();
                            var weight = $(this).find(".weight").val();
                            if(( weight == ""&& quantity !=="") ||
                                (weight != "" && quantity =="")){
                                cbms.alert("请检查输入的数据，实提数量和重量只能同时为空或同时有值");
                                commit = false;
                                return false;
                            }
                            var item = {
                                quantity: quantity,
                                id: $(this).find(".quantity").attr("rel"),
                                weight: weight
                            };
                            itemList.push(item);
                        });
                        if (commit) {
                            $.ajax({
                                type: "POST",
                                url: Context.PATH + '/order/secondarysettlement/inputInfos.html',
                                data: {
                                    itemList: JSON.stringify(itemList),
                                    orderId: orderId
                                },
                                dataType: "json",
                                success: function (response, textStatus, xhr) {
                                    if (response.success) {
                                        cbms.closeDialog();
                                        cbms.alert(response.data, function () {
                                            window.location.reload();
                                        });
                                    } else {
                                        cbms.alert(response.data);
                                    }
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                }
                            });
                        }
                    });
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    });

    $(document).on("click", "#secondary", function () {
        var orderId = $(this).attr("rel");
        if(!showButtons(orderId)){
            return false;
        }
       var a= $(".actual-pick-weight-server");
        var actualWeight=0;
        for(var i=0;i< a.length;i++){
            var x=a.eq(i).text();
            actualWeight=actualWeight+ Math.ceil(x)
        }
        cbms.loading();
        // var orderId = $(this).attr("rel");
        $.ajax({
            type: "POST",
            url: Context.PATH + '/order/secondarysettlement/secondarySettlement.html',
            data: {
                orderId: orderId,
                actualWeight:actualWeight
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                cbms.closeLoading();
                if (response.success) {
                    cbms.alert(response.data, function () {
                        location.replace(Context.PATH + "/order/query/secondsettlement.html");//返回列表
                    });
                } else {
                    cbms.alert(response.data);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                cbms.closeLoading();
            }
        });
    });
});

/**
 * 正在进行合同变更的订单，禁止除“返回”外的按钮操作
 */
function showButtons(orderId) {
    //var type = $("#alterStatus").val();
    //var orderId = $("#consignOrderId").val();
    var returnFlag = true;
    /*if(type == "PENDING_APPROVAL" || type == "PENDING_RELATE" || type=="PENDING_APPLY" || type=="PAYED_DISAPPROVE" ||type=="PENDING_APPR_PAY" || type =="PENDING_PRINT_PAY" || type =="PENDING_CONFIRM_PAY"){
     cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
     return false;
     }else{*/
    $.ajax({
        type: 'post',
        async: false,
        url: Context.PATH + '/order/changecontract/queryorderpaystatus.html',
        data: {
            orderId: orderId,
            origin:"origin"
        },
        error: function (s) {
            cbms.alert("查询订单信息报错！");
            returnFlag = false;
        }
        , success: function (re) {
            if (re.success) {
                if(re.data == "PENDING_APPROVAL" || re.data == "PENDING_RELATE" || re.data=="PENDING_APPLY" || re.data=="PAYED_DISAPPROVE" || re.data=="PENDING_APPR_PAY" || re.data =="PENDING_PRINT_PAY" || re.data =="PENDING_CONFIRM_PAY"){
                    cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
                    returnFlag = false;
                }else{
                    returnFlag = true;
                }
            }else{
                cbms.alert("查询订单信息报错！");
                returnFlag = false;
            }
        }
    });
    //}
    return returnFlag;
}

