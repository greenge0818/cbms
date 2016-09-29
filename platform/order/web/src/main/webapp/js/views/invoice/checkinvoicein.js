/**
 * Created by lcw on 8/5/2015.
 */
var tableId = "dynamic-table";  // table
var totalWeight = 0, totalNoTaxAmount = 0, totalTaxAmount = 0, totalAmount = 0;

$(document).ready(function () {
    var table = $("#" + tableId);
    table.dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "bPaginate": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/checkinvoiceindetail.html',
            type: 'POST',
            data: function (d) {
                d.invoiceInId = $("#invoiceInId").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var shouldWeight = parseFloat(aData.shouldWeight);
            var shouldAmount = parseFloat(aData.shouldAmount);
            var shouldAmountHtml = "<input type='hidden' name='shouldamount' value='" + shouldAmount.toFixed(2)
                + "'><span name='shouldamountText'>" + formatMoney(shouldAmount, 2) + "</span>";
            var weight = parseFloat(aData.weight);
            var amount = parseFloat(aData.amount);
            $(nRow).attr("recordid", aData.id);
            $(nRow).attr("poolinid", aData.poolInId);
            $(nRow).attr("poolindetailid", aData.poolInDetailId);
            $('td:eq(0)', nRow).html("<span name='nsortname'>" + aData.nsortName + "</span>");
            $('td:eq(1)', nRow).html("<span name='material'>" + aData.material + "</span>");
            $('td:eq(2)', nRow).html("<span name='spec'>" + aData.spec + "</span>");
            $('td:eq(3)', nRow).html("<span name='shouldweight'>" + shouldWeight.toFixed(4) + "</span>").addClass("text-right");
            $('td:eq(4)', nRow).html(shouldAmountHtml).addClass("text-right");
            $('td:eq(5)', nRow).html("<span name='weight'>" + weight.toFixed(4) + "</span>").addClass("text-right");
            $('td:eq(6)', nRow).html("<span name='amount'>" + formatMoney(amount, 2) + "</span>").addClass("text-right");
            $('td:eq(7)', nRow).html('<input type="text" value="' + weight.toFixed(4) + '" name="weight" must="1" verify="numeric" class="d-text">');
            $('td:eq(8)', nRow).html('<input type="text" value="' + aData.noTaxAmount.toFixed(2) + '" name="notaxamount" must="1" verify="numeric" class="d-text">');
            $('td:eq(9)', nRow).html('<input type="text" value="' + aData.taxAmount.toFixed(2) + '" name="taxamount" must="1" verify="numeric" class="d-text">');
        },
        "fnInitComplete": function() {
            // 默认加载完成以后统计一次
            invoiceTotal();
        },
        columns: [
            {data: 'nsortName'},
            {data: 'material'},
            {data: 'spec'},
            {data: 'shouldWeight'},
            {data: 'shouldAmount'},
            {data: 'weight'},
            {data: 'amount'},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''}
        ]
    });

    table.on("focus", "input[name='weight'],input[name='notaxamount'],input[name='taxamount']", function () {
        var currentVal = convertFloat($(this).val());
        if (currentVal == 0) {
            $(this).val("");
        }
    });

    table.on("blur", "input[name='weight'],input[name='notaxamount'],input[name='taxamount']", function () {
        var currentVal = $.trim($(this).val());
        if (currentVal == "") {
            $(this).val("0");
        }
    });

    // 计算发票含税金额（元）事件
    table.on("keyup", "input[name='weight'],input[name='notaxamount'],input[name='taxamount']", function () {
        invoiceTotal();
    });

    // 提交核对进项发票
    $("#confirm").click(function () {
        if (!setlistensSave())return;

        var verify = true;
        var currentRow;
        var weight = 0, noTaxAmount = 0, taxAmount = 0, amount = 0, shouldWeight = 0, shouldAmount = 0;
        var invoiceDetailArray = [];  // 发票详情集合
        $(table).find("tr").each(function () {
            currentRow = $(this);
            weight = convertFloat(currentRow.find("input[name='weight']").val());
            weight = parseFloat(weight.toFixed(4));
            noTaxAmount = convertFloat(currentRow.find("input[name='notaxamount']").val());
            taxAmount = convertFloat(currentRow.find("input[name='taxamount']").val());
            noTaxAmount = parseFloat(noTaxAmount.toFixed(2));
            taxAmount = parseFloat(taxAmount.toFixed(2));
            amount = noTaxAmount + taxAmount;
            shouldWeight = convertFloat(currentRow.find("span[name='shouldweight']").text());
            shouldAmount = convertFloat(currentRow.find("input[name='shouldamount']").val());
            shouldAmount = parseFloat(shouldAmount.toFixed(2));
            shouldWeight = parseFloat(shouldWeight.toFixed(4));
            // 重量和金额不能一个为0，一个不为0；要么都为0，要么都大于0；如果剩余的重量为0，则剩余的发票金额不能为0
            if ((weight == shouldWeight && amount != shouldAmount) || (weight != shouldWeight && amount == shouldAmount)) {
                verify = false;
                return false;
            }

            // 只统计重量，金额大于0的发票详情
            if (weight > 0 && noTaxAmount > 0 && taxAmount > 0) {
                var recordId = currentRow.attr("recordid");
                var poolInId = currentRow.attr("poolinid");
                var poolInDetailId = currentRow.attr("poolindetailid");
                var nsortName = currentRow.find("span[name='nsortname']").text();
                var material = currentRow.find("span[name='material']").text();
                var spec = currentRow.find("span[name='spec']").text();

                var invoiceDetail = {};   // 发票详情对象
                invoiceDetail.id = recordId;
                invoiceDetail.poolInId = poolInId;
                invoiceDetail.poolInDetailId = poolInDetailId;
                invoiceDetail.nsortName = nsortName;
                invoiceDetail.material = material;
                invoiceDetail.spec = spec;
                invoiceDetail.checkWeight = weight;
                invoiceDetail.checkNoTaxAmount = noTaxAmount;
                invoiceDetail.checkTaxAmount = taxAmount;
                invoiceDetail.checkAmount = amount;
                invoiceDetailArray.push(invoiceDetail);  // 添加到集合
            }
        });
        if (verify == false) {
            cbms.alert("发票总金额、吨位与品规总金额、吨位不一致，请检查并修改 ！");
            return;
        }
        if (invoiceDetailArray.length == 0) {
            cbms.alert("请输入核实发票的详细信息 ！");
            return;
        }
        var invoiceCode = $.trim($("#invoiceCode").val());
        var invoiceWeight = convertFloat($.trim($("#invoiceWeight").val()));
        var invoiceAmount = convertFloat($.trim($("#invoiceAmount").val()));
        invoiceWeight = parseFloat(invoiceWeight.toFixed(4));
        invoiceAmount = parseFloat(invoiceAmount.toFixed(2));
        var invoiceDate = $.trim($("#invoiceDate").val());
        var invoiceInId = $("#invoiceInId").val();
        var sellerId = $("#sellerId").val();

        if (invoiceWeight == 0 || invoiceAmount == 0) {
            cbms.alert("发票重量与发票含税金额不能为 0 ！");
            return;
        }
        if (invoiceWeight != totalWeight || invoiceAmount != totalAmount) {
            cbms.alert("发票总金额、吨位与品规总金额、吨位不一致，请检查修改！");
            return;
        }

        var invoiceData = {
            code: invoiceCode,
            invoiceInId: invoiceInId,
            sellerId: sellerId,
            checkTotalAmount: totalAmount,
            checkTotalWeight: totalWeight,
            invoiceDate: invoiceDate,
            invoiceDetailArray: invoiceDetailArray
        }

        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/in/savecheck.html",
            data: {
                invoiceJson: JSON.stringify(invoiceData)
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        var nextInvoiceIn = $("#nextInvoiceIn");
                        var nextId = $(nextInvoiceIn).val();
                        var content = "<ul>";
                        content += "<li>成功确认发票号：【" + invoiceCode + "】</li>";
                        if (nextId !== "" && nextId !== "0") {
                            var nextCode = $(nextInvoiceIn).attr("code");
                            var nextSellerName = $(nextInvoiceIn).attr("sellername");
                            var nextAmount = convertFloat($(nextInvoiceIn).attr("amount"));
                            content += "<li>下一张待确认发票号：<span class='bolder'>" + nextCode + "</span></li>";
                            content += "<li>公司全称：<span class='bolder'>" + nextSellerName + "</span></li>";
                            content += "<li>发票金额（元）：<span class='red bolder'>" + formatMoney(nextAmount, 2) + "</span></li>";
                            content += "<li class='btn-bar'>";
                            content += "<button type='button' class='btn btn-sm btn-primary' nextid='" + nextId + "' id='checkNext' >确认下一张发票</button>&nbsp;&nbsp;";
                        }
                        content += "<button type='button' class='btn btn-sm btn-default' id='returnList' >返回待确认列表</button>";
                        content += "</li>";
                        content += "</ul>";
                        cbms.getDialog("提示信息", content);
                    }
                    else {
                        cbms.alert(result.data);
                    }
                } else {
                    cbms.alert("录入发票失败");
                }
            }
        });
    });

    // 继续录入
    $("body").on("click", "#checkNext", function () {
        var nextId = $(this).attr("nextid");
        location.href = Context.PATH + "/invoice/in/" + nextId + "/checkinvoicein.html";
    });

    // 录入完毕
    $("body").on("click", "#returnList", function () {
        location.href = Context.PATH + "/invoice/in/confirm.html";
    });

});

// 发票统计计算
function invoiceTotal() {
    // 重置统计数据
    totalWeight = 0;
    totalNoTaxAmount = 0;
    totalTaxAmount = 0;
    var table = $("#" + tableId + " tbody");
    $(table).find("tr").each(function () {
        var currentRow = $(this);
        var weight = convertFloat(currentRow.find("input[name='weight']").val());
        var noTaxAmount = convertFloat(currentRow.find("input[name='notaxamount']").val());
        var taxAmount = convertFloat(currentRow.find("input[name='taxamount']").val());
        weight = parseFloat(weight.toFixed(4));
        noTaxAmount = parseFloat(noTaxAmount.toFixed(2));
        taxAmount = parseFloat(taxAmount.toFixed(2));
        totalWeight += weight;
        totalNoTaxAmount += noTaxAmount;
        totalTaxAmount += taxAmount;
    });
    totalAmount = totalNoTaxAmount + totalTaxAmount;
    $("#totalWeight").text(totalWeight.toFixed(4));
    $("#totalAmount").text(formatMoney(totalAmount, 2));
}