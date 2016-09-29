/**
 * 关联变更合同
 * Created by chengui on 2016/8/11.
 */
$().ready(function () {

    show_balance();

    $("#cb_blance_second_settlement").change(function () {
        show_balance();
    });

    //信用额度
    $("#cb_blance_credit").change(function () {
        show_balance();
    });

    $("#order_relate").click(function () {

        cbms.confirm("确定关联吗？", null, function () {
            var validate = true;
            var orderItemsList = [];   //银票支付的订单详情表列表
            $(".orderItemsTable").find(".itemsdatabody").find("tr").each(function(){
                var isPayedByAcceptDraft = $(this).find(".isPayedByAcceptDraft").text(); //是否网银支付
                var acceptDraftId = $(this).find("select[name='acceptDraft']").val();  //银票票号ID
                if(isPayedByAcceptDraft == "true"){
                    if(acceptDraftId == 0){
                        validate = false;
                        cbms.alert("订单资源中有选择银票支付但未选择票号，不能关联!");
                        return false;
                    }
                    if(acceptDraftId > 0){
                        var orderItemsObj = {};
                        orderItemsObj.orderItemsChangeId = $(this).find(".orderItemsChangeId").text(); //订单详情ID
                        orderItemsObj.acceptDraftId = acceptDraftId; //银票票号ID
                        orderItemsObj.acceptDraftCode = $(this).find("select[name='acceptDraft']").find("option:selected").text(); //银票票号
                        orderItemsList.push(orderItemsObj);
                    }
                }
            });
            if(validate){
                var order_change_id = $("#order_change_id").text();
                var takeoutSecondBalance = $("#cb_blance_second_settlement").is(":checked");
                var url = Context.PATH + '/order/changecontract/changeAssociateOrder.html';
                var totalAmount = parseFloat($("#total_amount").val());//订单总金额
                var totalRelatedAmount = parseFloat($("#total_related_amount").val());//已关联金额
                var takeoutCreditBalance = $("#cb_blance_credit").is(":checked");
                cbms.loading();
                $.post(url, {orderChangeId: order_change_id,
                    secondbalancetakeout:takeoutSecondBalance,
                    totalAmount: totalAmount,
                    totalRelatedAmount:totalRelatedAmount,
                    orderItemsList:JSON.stringify(orderItemsList),
                    creditbalancetakeout:takeoutCreditBalance

                }, function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        cbms.alert("合同关联成功");
                        var url = Context.PATH + "/order/changecontract/relatelist.html";
                        location.replace(url);
                    } else {
                        cbms.alert(re.data);
                    }
                });
            }
        });
    });


    $("#order_close").click(function () {
        var html="<form id='order_shut_Form'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭本次变更的理由！'></textarea><br>"+
            "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
            "<button type='button'  class='btn btn-info btn-sm' id='close_confirm'>确定</button></div> </div></form>";
        cbms.getDialog("关闭订单",html);

    });

    $(document).on("click","#window_close",function(){
        cbms.closeDialog();
    })

    $(document).on("click","#close_confirm",function(){

        if (!setlistensSave("#order_shut_Form")) return;

        cbms.confirm("确定关闭订单吗？", null, function () {
            var order_change_id = $("#order_change_id").text();
            var url = Context.PATH + '/order/changecontract/closeAssociateOrder.html';
            cbms.loading();
            $.post(
                url,
                {
                    orderChangeId: order_change_id,
                    cause:$("textarea[name='cause']").val()
                },
                function (re) {
                    cbms.closeLoading();
                    if (re.success) {
                        cbms.alert("合同关闭成功");
                        var url = Context.PATH + "/order/changecontract/relatelist.html";
                        location.replace(url);
                    } else {
                        cbms.alert(re.data);
                    }
            });
        });
     });




});


function show_balance() {
    var account_balance = parseFloat($("#account_balance").text());
    var account_blance_second_settlement = parseFloat($("#account_blance_second_settlement").text());
    var cb_blance_second_settlement = $("#cb_blance_second_settlement").is(":checked");
    var order_total_amount =  parseFloat($("#change_relate_amount").val());
    var account_total_balance = $("#account_total_balance");
    var insufficient_fund_balance = $("#insufficient_fund_balance");
    var div_insufficient_fund = $("#insufficient_fund");
    var div_allow_releted_info = $("#allow_releted_info");
    //买家容差值
    var buyerToleranceAmount = parseFloat($("#buyerToleranceAmount").val());
    var contact_releted_amount = $("#contact_releted_amount");

    //信用额度
    var cb_blance_credit = $("#cb_blance_credit").is(":checked");
    var account_blance_credit = parseFloat($("#account_blance_credit").text());

    var temp = account_balance;
    if (cb_blance_second_settlement) {
        temp = account_blance_second_settlement.add(temp);
    }
    if(cb_blance_credit){
        temp = account_blance_credit.add(temp);
    }
    account_total_balance.text(temp.toFixed(2));


    //订单金额 - 可用余额
    var oreder_total_amount_sub_temp = order_total_amount.sub(temp);
    if (oreder_total_amount_sub_temp > 0) {
        //用容差
        //可用金额少于订单金额在容差范围内
        if(oreder_total_amount_sub_temp <= buyerToleranceAmount){
            contact_releted_amount.val(temp.toFixed(2));
            $("#sub_balance").text(oreder_total_amount_sub_temp.toFixed(2));
            div_insufficient_fund.addClass("none");
            div_allow_releted_info.removeClass("none");
            $("#order_relate").removeAttr("disabled");

        }else{
            insufficient_fund_balance.text(oreder_total_amount_sub_temp.toFixed(2));
            div_insufficient_fund.removeClass("none");
            div_allow_releted_info.addClass("none");
            $("#order_relate").prop("disabled", true);
        }
    } else {
        contact_releted_amount.val(order_total_amount.toFixed(2));
        //可用金额>=订单金额 时隐藏提示信息并启用关联按钮
        div_insufficient_fund.addClass("none");
        div_allow_releted_info.addClass("none");
        $("#order_relate").removeAttr("disabled");
    }
}
