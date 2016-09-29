/**
 * Created by dengxiyan on 2015/8/31.
 * 买家返利报表
 */
var table;
var moneyScale = 2;
var weightScale = 4;
$(document).ready(function () {
    initTable();
    initEvent();
});


function initTable() {
    table = $('#dynamic-table').DataTable({
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "ajax": {
            "url": Context.PATH + "/report/business/loadbuyerrebatedata.html"
            , "type": "POST"
            , data: function (d) {
                var ordId = $("#sorganizationHidden").val();
                if (ordId > 0) {
                    d.orgId = ordId;
                }
                var buyerTradeName = $.trim($("#buyerTradeName").val());
                if (buyerTradeName) {
                    d.buyerTradeName = buyerTradeName;
                }
                d.strStartTime = $("#startTime").val();
                d.strEndTime = $("#endTime").val();
            }
        },
        "columns": [{
            "class": 'details-control',
            "orderable": false,
            "data": null,
            "defaultContent": ''
        }, {
            "data": "accountName",
            "render": function (data, type, full, meta) {
                var href = Context.PATH + "/report/rebate/detail.html?account_id=" + full.accountId +"&sdate="+$("#startTime").val()+"&edate="+$("#endTime").val();
                return '<a class="getDataList" href="' + href + '" type="buyer">' + data + '</a><span class="pos-rel"></span>';
            }
        }, {
            "data": "orgName"
        }, {
            "data": "managerName"
        }, {
            "data": "weight",
            "render": function (data) {
                return renderWeight(data);
            }
            , "sClass": "text-right"
}, {
    "data": "amount",
            "render": function (data) {
                return renderMoney(data, moneyScale);
            }
            , "sClass": "text-right"
        }, {
            "data": "previousPeriodBalance",
            "render": function (data) {
                return renderMoney(data, moneyScale);
            }
        }, {
            "data": "rebateAmount",
            "render": function (data) {
                return renderMoney(data, moneyScale);
            }
            , "sClass": "text-right"
        }, {
            "data": "withdrawAmount",
            "render": function (data) {
                return renderMoney(data, moneyScale);
            }
            , "sClass": "text-right"
        }, {
            "data": "thisPeriodBalance",
            "render": function (data) {
                return renderMoney(data, moneyScale);
            }
            , "sClass": "text-right"
        }]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
            var tr = $(nRow).closest('tr');
            tr.attr("buyer_id", aData.accountId);
        }
        //生成footer
        , "footerCallback": function (row, data, start, end, display) {
            //去掉汇总页的图标
            $("#dynamic-table tfoot").find("td.details-control").removeClass("details-control");

            var api = this.api();
            // Update footer
            var ele = '<div class=" recordbar col-xs-12">' +
                '<span class="bolder">当前页汇总</span>' +
                '<span>上期返利余额：<span class="bolder">' + pageTotalColumn(data, "previousPeriodBalance", moneyScale) + '</span>元</span>' +
                '<span>返利金额：<span class="bolder">' + pageTotalColumn(data, "rebateAmount", moneyScale) + '</span>元</span>' +
                '<span>提现金额：<span class="bolder">' + pageTotalColumn(data, "withdrawAmount", moneyScale) + '</span>元</span>' +
                '<span>本期返利金额：<span class="bolder">' + pageTotalColumn(data, "thisPeriodBalance", moneyScale) + '</span>元</span>' +
                '</div>';
            $(api.column(0).footer()).html(ele);


        }
    });
}


function initEvent() {

    $("#queryBtn").click(function () {
        table.ajax.reload();
    });

    $("#output").click(function () {
        exportExcel();
    });

    // Add event listener for opening and closing details
    $('#dynamic-table tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');

        //详情查询条件
        var buyerId = tr.attr("buyer_id");
        var strStartTime = $("#startTime").val();
        var strEndTime = $("#endTime").val();

        if (tr.hasClass("shown")) {
            //已经显示则隐藏
            $("#dynamic-table tr[parent_buyer_id=" + buyerId + "]").addClass("none");
            tr.removeClass('shown');
        } else if (tr.hasClass('hasRequested')) {
            //已经请求过，直接显示数据
            $("#dynamic-table tr[parent_buyer_id=" + buyerId + "]").removeClass("none");
            tr.addClass('shown');
        } else {
            tr.addClass('shown');

            $.post(Context.PATH + "/report/business/loadallcontactsrebatedata.html", {
                buyerId: buyerId,
                strStartTime: strStartTime,
                strEndTime: strEndTime
            }, function (result) {
                if (result.success) {
                    var hrefValue, subtr, tds;
                    //添加联系人数据行
                    $.each(result.data, function (i, row) {
                        hrefValue = Context.PATH + "/report/rebate/userdetail.html?accountid="+buyerId+"&contactid="+row.contactId+"&sdate="+strStartTime+"&edate="+strEndTime;
                        subtr = tr.clone();
                        tds = subtr.find("td");
                        tds.eq(0).text("");//图标列
                        tds.eq(1).html('<a class="getDataList" href="' + hrefValue + '" type="contact" contact_id="' + row.contactId + '">' + row.contactName + '</a><span class="pos-rel"></span>');
                        tds.eq(2).text("");//服务中心
                        tds.eq(3).text(row.managerName);
                        tds.eq(4).text(renderWeight(row.weight));
                        tds.eq(5).text(renderMoney(row.amount));
                        tds.eq(6).text(renderMoney(row.previousPeriodBalance));
                        tds.eq(7).text(renderMoney(row.rebateAmount));
                        tds.eq(8).text(renderMoney(row.withdrawAmount));
                        tds.eq(9).text(renderMoney(row.thisPeriodBalance));

                        subtr.find("td.details-control").removeClass("details-control");
                        subtr.attr("parent_buyer_id", buyerId);
                        tr.after(subtr);
                    });
                    tr.addClass('hasRequested');//标识已经成功请求过
                }
            }, 'json');
        }
    });

    $('#dynamic-table tbody').on('mouseenter', '.getDataList', function () {
        var $this = $(this);
        if ($this.next("span.pos-rel").html().length == 0) {
            var strStartTime = $("#startTime").val();
            var strEndTime = $("#endTime").val();
            if ($this.attr('type') == 'buyer') {
                //买家
                $.post(Context.PATH + "/report/business/loadbuyergroupcategoryrebatedata.html", {
                    buyerId: $this.closest('tr').attr('buyer_id'),
                    strStartTime: strStartTime,
                    strEndTime: strEndTime
                }, function (result) {
                    $this.next("span.pos-rel").html(createCategoryEle(result));
                }, 'json');
            } else {
                //联系人
                $.post(Context.PATH + "/report/business/loadcontactgroupcategoryrebatedata.html", {
                    contactId: $this.attr('contact_id'),
                    strStartTime: strStartTime,
                    strEndTime: strEndTime
                }, function (result) {
                    $this.next("span.pos-rel").html(createCategoryEle(result));
                }, 'json');
            }
        }
        $this.next("span.pos-rel").show();

    });

    $('#dynamic-table tbody').on('mouseleave', '.getDataList', function () {
        $('#dynamic-table tbody').find("span.pos-rel").hide();
    });

}


function exportExcel() {
    var ordId = $("#sorganizationHidden").val();
    var buyerTradeName = $.trim($("#buyerTradeName").val());

    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/contactsrebateexcel.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'strStartTime');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'strEndTime');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'orgId');
    input3.attr('value', ordId);

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'buyerTradeName');
    input4.attr('value', buyerTradeName);

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'excelTitles');
    input5.attr('value', '时间段,买家全称,联系人,服务中心,交易员,实提总重量（吨）,实提总金额（元）,上期返利余额（元）'+
       ',返利金额（元）,提现金额（元）,本期返利余额（元）');

    $('body').append(form);

    form.append(input2);
    form.append(input1);
    if (ordId > 0) {
        form.append(input3);
    }

    if (buyerTradeName) {
        form.append(input4);
    }
    form.append(input5);

    form.submit();
    form.remove();
}


function renderMoney(data, scale) {
    if (data) {
        return formatMoney(data, scale);
    }
    return "0.00";
}

/**
 * 返回当前页的列合计数据
 * 先四舍五入保留小数位再进行计算
 * @param data 当前页数据
 * @param columnName 待计算的列
 * @param scale
 * @returns {*}
 */
function pageTotalColumn(data, columnName, scale) {
    var total = 0;
    var temp;
    for (var i = 0; i < data.length; i++) {
        temp = parseFloat(data[i][columnName]);
        if (!isNaN(temp)) {
            total += parseFloat(temp.toFixed(scale));
        }
    }
    return formatMoney(total, scale);
}

function createCategoryEle(result) {
    var ele = '<div class="pos-abs" style="z-index:99;left:0;top:-50px;width:400px;">' +
        '<table class="table  table-bordered ">' +
        '<thead><tr><td>品类</td><td>实体总重(吨)</td><td>实提总金额(元)</td><td>返利金额(元)</td></tr></thead>' +
        '<tbody>';

    var totalWeight = 0,totalAmount= 0,totalRebateAmount=0;
    if (result.success) {
        var data = result.data;
        $.each(data, function (i, row) {
            ele += '<tr><td>' + row.categoryGroupName + '</td><td class="text-right">' + renderWeight(row.weight) + '</td><td class="text-right">' + renderMoney(row.amount) + '</td><td class="text-right">' + renderMoney(row.rebateAmount) + '</td></tr>';

            //合计
            totalWeight += toFloat(row.weight,weightScale);
            totalAmount += toFloat(row.amount,moneyScale);
            totalRebateAmount += toFloat(row.rebateAmount,moneyScale);
        })
    }
    ele += '</tbody><tfooter><tr><td>合计</td><td class="text-right">'+totalWeight.toFixed(weightScale)+'</td><td class="text-right">'+formatMoney(totalAmount, moneyScale)+'</td><td class="text-right">'+formatMoney(totalRebateAmount,moneyScale)+'</td></tr></tfooter>' +
    '<table>' +
    '</div>';
    return ele;
}


function toFloat(value,scale){
    var temp = parseFloat(value);
    if (!isNaN(temp)) {
       return parseFloat(temp.toFixed(scale));
    }
    return 0;
};