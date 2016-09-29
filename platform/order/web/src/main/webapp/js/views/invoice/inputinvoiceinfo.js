/**
 * Created by dengxiyan on 2015/8/4.
 */
var totalAmount = 0, totalInvoice = 1;
jQuery(function ($) {
    initClickEvent();
});

function initClickEvent() {

    //添加尾行
    $("#addForwarder").click(function () {
        var no = getTrNo();
        var ele = '<tr><td>' + no + '</td>' +
            '<td><input class="c-text" verify="numeric" type="text" value="" name="code" must="1" verify="numeric"/></td>' +
            '<td><input class="c-text" verify="numeric" type="text" value="" name="amount" must="1" verify="numeric"/></td>' +
            '<td><a href="javascript:;" title="删除" id="delForwarder"><i class="ace-icon glyphicon glyphicon-minus"></i></a></td></tr>';
        $(this).closest("tbody").find("tr").last("tr").after(ele);

        totalInvoice++;
        modifyTotalInvoice();
    });

    //删除当前行
    $(document).on("click", "#delForwarder", function () {
        var $delIcon = $(this);
        cbms.confirm("确定删除该发票信息?", null, function () {
            $delIcon.closest("tr").remove();
            invoiceTotal();
            totalInvoice--;
            modifyTotalInvoice();
            reCalTrNo();
        });
    })

    // 计算发票金额（元）事件
    $("#dynamic-table").on("keyup", "input[name='amount']", function () {
        invoiceTotal();
    });

    //提交
    $("#submit-btn").click(function () {
        if (!setlistensSave("#invoiceForm"))return;

        var table = $("#dynamic-table tbody");
        var invoiceData = {}, invoiceDetails = [];

        //开票金额与发票总额
        invoiceData.totalAmount = convertFloat(numberVal($("#totalAmount").val()));
        invoiceData.totalInvoiceAmount = convertFloat(numberVal($("#totalInvoiceAmount").text()));

        //校验发票总额和开票总额是否相等
        if (invoiceData.totalAmount != invoiceData.totalInvoiceAmount) {
            cbms.alert("发票金额合计与开票金额合计不相等");
            return;
        }

        var orgId = $("#orgId").val(), orgName = $("#orgName").val(), buyerId = $("#buyerId").val(), buyerName = $("#buyerName").val();
        $(table).find("tr").each(function () {
            var currentRow = $(this);
            var code = $.trim(currentRow.find("input[name='code']").val());
            var amount = convertFloat(currentRow.find("input[name='amount']").val());

            invoiceDetails.push({
                code: code,
                amount: amount,
                orgId: orgId,
                orgName: orgName,
                buyerId: buyerId,
                buyerName: buyerName
            });

        });

        if (invoiceDetails.length == 0) {
            cbms.alert("请录入销项发票详细信息！");
            return;
        }

        invoiceData.invoiceDetails = invoiceDetails;
        invoiceData.ids = $("#ids").val().split();

        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/out/submitinvoice.html",
            data: {
                invoiceJson: JSON.stringify(invoiceData)
            },
            error: function (s) {
            }
            , success: function (result) {
                if (result && result.success) {
                    var content = "<ul>";
                    content += "<li>发票信息已成功录入</li>";
                    content += "<li><em class='red'>5</em>秒后关闭</li>";
                    content += "<li class='btn-bar'>";
                    content += "<button type='button' class='btn btn-sm btn-default' id='close'>知道了</button>";
                    content += "</li>";
                    content += "</ul>";
                    cbms.getDialog("提示信息", content);
                    //5秒关闭 跳转到上一界面
                    setTimeout(function () {
                        cbms.setTmClose();
                        history.go(-1);
                    }, 5000);
                } else {
                    if (result.data) {
                        cbms.alert(result.data);
                    } else {
                        cbms.alert("录入发票失败");
                    }
                }
            }
        });
    });


    //弹出窗口关闭事件
    $("#close").click(function () {
        cbms.setTmClose();
        history.go(-1);
    });


}

/**
 * 获得行号
 * @returns {*}
 */
function getTrNo() {
    var len = $("#dynamic-table tbody").find("tr").length + 1;
    return len > 9 ? len : "0" + len;
}

/**
 * 重新计算行号
 */
function reCalTrNo() {
    var table = $("#dynamic-table tbody");
    $(table).find("tr").each(function (i, tr) {
        var codeTd = $(this).find("td").eq(0);
        var no = i + 1;
        codeTd.text(no > 9 ? no : "0" + no);
    });
}


/**
 * 计算发票总额
 */
function invoiceTotal() {
    var table = $("#dynamic-table tbody");
    totalAmount = 0;
    $(table).find("tr").each(function () {
        var currentRow = $(this);
        var amount = convertFloat(currentRow.find("input[name='amount']").val());
        totalAmount += amount;
    });
    $("#totalInvoiceAmount").text(formatMoney(totalAmount, 2));
}


// 成功返回 Float 值，失败返回0
function convertFloat(str) {
    var value = 0;
    var tempVal = $.trim(str);
    if (tempVal != "") {
        value = parseFloat(tempVal);
        if (isNaN(value))
            value = 0;
    }
    return value;
}

//修改发票张数
function modifyTotalInvoice() {
    $("#totalInvoice").text(totalInvoice);
}

// 删除格式
function numberVal(i) {
    return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
}