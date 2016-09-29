$(document).ready(function() {
    //金额格式化
    $('.amount').each(function(){
        $(this).html(formatMoney($(this).html()));
    });
});

/**
 * 点击详情
 */
$(document).on("click", ".show-checklist", function () {
    var id = $(this).attr("data-id");
    fetchData(id);
});

/**
 * 通过/不通过按钮
 */
$(document).on("click", ".approval", function() {
    var status = $(this).attr("data-status");

    var ids = [];
    $(".page-content").find("input[name='iptApplyId']").each(function(){
        var id = $(this).val();
        ids.push(id);
    });

    if(ids.length > 0) {
        cbms.loading();
        approving(ids, status);
    }
});

/**
 * 重量格式化
 * @param data
 * @param scale
 * @returns {string}
 */
function formatWeight(data,scale) {
    if (data) {
        return parseFloat((data + "").replace(/[^\d\.-]/g, "")).toFixed(scale) + "";
    }
    return "0.0000";
}

/**
 * 获取详情项
 * @param id
 */
function fetchData(id){
    $.ajax({
        type: "POST",
        url: Context.PATH + '/invoice/out/orderdetail.html',
        data: {
            id: id
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if(response.success){
                console.log(response.data);
                $('#hiddenForm').empty();
                //订单信息
                var orders = response.data;
                if(orders!=null &&orders.length>0) {
                    for (var i = 0; i < orders.length; i++) {

                        $('#hiddenForm').append('<p>交易单号：' + orders[i].contractCode +
                        '&nbsp; &nbsp;开单时间：' + new Date(orders[i].created).Format('yyyy-MM-dd hh:mm:ss') +
                        '&nbsp; &nbsp;实提总重量（吨）：' + formatWeight(orders[i].totalActualPickWeight, 4) +
                        '&nbsp; &nbsp合同总金额（元）：' + formatMoney(orders[i].totalAmount, 2) +
                        '&nbsp;&nbsp;实提总金额（元）：' + formatMoney(orders[i].totalActualPickAmount, 2) + '</p>');
                        //详情项
                        var items = orders[i].items;
                        if (items != null && items.length>0) {
                            $('#hiddenForm').append('<table id="' + orders[i].contractCode + '" class="contable border0">' +
                            '<thead><tr class="bg-gray">' +
                            '<th class="border0">品名</th>' +
                            '<th class="border0">材质</th>' +
                            '<th class="border0">规格</th>' +
                            '<th class="border0">实提重量<br/>（吨）</th>' +
                            '<th class="border0">开票重量</th>' +
                            '<th class="border0">成交价(元/吨)</th>' +
                            '<th class="border0">合同金额(元)</th>' +
                            '<th class="border0">实提金额（元）</th>' +
                            '<th class="border0">开票金额（元）</th>' +
                            '</tr></thead><tbody></tbody></table> <br/> <br/>')
                        }

                        for (var j = 0; j < items.length; j++) {
                            $('#' + orders[i].contractCode).append('<tr>')

                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + items[j].nsortName + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + items[j].material + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + items[j].spec + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatWeight(items[j].actualPickWeight, 4) + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatWeight(items[j].invoiceWeight, 4) + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatMoney(items[j].dealPrice, 2) + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatMoney(items[j].amount, 2) + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatMoney(items[j].actualPickAmount, 2) + '</td>');
                            $('#' + orders[i].contractCode + ' tbody').append('<td>' + formatMoney(items[j].invoiceAmount, 2) + '</td>');

                            $('#' + orders[i].contractCode).append('</tr>')
                        }
                    }
                    cbms.getDialog("详情", $("#hiddenForm").clone().html());
                } else {
                    cbms.getDialog("提示", "没有任何数据。");
                }
            }else{
                cbms.alert(response.data);
            }
        }
    });
}

/**
 * 审批
 * @param ids ID
 * @param status 状态
 */
function approving(ids, status){
    $.ajax({
        type: "POST",
        url: Context.PATH + '/invoice/out/approving.html',
        data:{
            ids:ids,
            status:status
        },
        dataType: "json",
        success:function(res, textStatus, xhr){
            cbms.closeLoading();
            if (res.success) {
                cbms.alert("更新成功");
                window.location.href = window.location.href;
            } else {
                cbms.alert("更新失败");
            }
        },
        error : function(xhr, textStatus, errorThrown) {
            cbms.closeLoading();
            cbms.alert("服务器发生错误。");
        }
    });
}