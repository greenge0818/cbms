/**
 * Created by caochao on 2015/8/26.
 * 买家交易报表
 */

var dt;

jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        $("#timespanvalue").val($("#tradetimespan").val());
        dt.ajax.reload();
    });
    $("#exportexcel").click(function () {
        exportToExcel();
    });
});

function initTable() {
    var url = Context.PATH + "/report/business/tradestatisticsdata.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
                d.timespan = $("#tradetimespan").val();
            }
        },
        columns: [
            {
                "class": 'details-control',
                "data": "null",
                "defaultContent": ''
            },
            {data: 'orgName'},
            {data: 'creditLimit'},
            {data: 'creditLimitUsed'},
            {data: 'traderCount'},
            {data: 'orderCount'},
            {data: 'consignOrderPercent'},
            {data: 'orderTotalAmount'},
            {data: 'orderTotalWeight'},
            {data: 'orderAvgWeight'},
            {data: 'buyerCount'},
            {data: 'frequentTradeCurMonthCount'},
            {data: 'frequentTradePrevMonthCount'},
            {data: 'sellerInCreaseCount'},
            {data: 'buyerInCreaseCount'},
            {data: 'tradeSellerCount'},
            {data: 'sellerTradeCount'},
            {data: 'sellerTradeTotalWeight'},
            {data: 'tempTradeSellerCount'},
            {data: 'tempSellerTradeCount'},
            {data: 'tempSellerTradeTotalWeight'}
        ],
        columnDefs: [
            {
                "targets": 6, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return (data * 100).toFixed(2) + '%';
                }
            },
            {
                "targets": 7, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return formatMoney(data, 2);
                }
            },
            {
                "targets": 8, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 9, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            }
            ,
            {
                "targets": 17, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            }
            ,
            {
                "targets": 20, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var tr = $(nRow).closest('tr');
            tr.attr("org_id", aData.orgId);
        },
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {
            sUrl: Context.PATH + "/js/DT_zh.txt"
        }, //自定义语言包
        bFilter: false,
        iDisplayLength: 5,
        bLengthChange: false
    });
    $('#dynamic-table tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var org_id = tr.attr("org_id");
        var timespan = $("#timespanvalue").val();
        if (tr.hasClass("shown")) {
            tr.removeClass('shown');
            $("#dynamic-table tr[parent_org_id=" + org_id + "]").addClass("none");
        }
        else {
            tr.addClass('shown');
            if (tr.attr("detailloaded") != "true") {
                $.ajax({
                    url: Context.PATH + "/report/business/tradestatisticsdetaildata.html",
                    type: "post",
                    dataType: "json",
                    data: {orgid: org_id, timespan: timespan},
                    success: function (re) {
                        if(re.success)
                        {
                            for (var i = 0; i < re.data.length; i++) {
                                var subtr = tr.clone();
                                var tds = subtr.find("td");
                                tds.eq(0).text("");
                                tds.eq(1).text(re.data[i].userName); //交易员
                                tds.eq(2).text("");
                                tds.eq(3).text("");
                                tds.eq(4).text("");
                                tds.eq(5).text(re.data[i].orderCount);                          //交易总笔数
                                tds.eq(6).text((re.data[i].consignOrderPercent * 100).toFixed(2) + '%');//代运营交易订单占比
                                tds.eq(7).text(formatMoney(re.data[i].orderTotalAmount, 2));//交易总金额
                                tds.eq(8).text(re.data[i].orderTotalWeight.toFixed(6));//交易总重量
                                tds.eq(9).text(re.data[i].orderAvgWeight.toFixed(6));//平均每笔交易重量
                                tds.eq(10).text(re.data[i].buyerCount);//交易买家数
                                tds.eq(11).text(re.data[i].frequentTradeCurMonthCount);//当月三次以上采购买家
                                tds.eq(12).text(re.data[i].frequentTradePrevMonthCount);//上月三次以上采购买家
                                tds.eq(13).text(re.data[i].sellerInCreaseCount);//新增卖家
                                tds.eq(14).text(re.data[i].buyerInCreaseCount);//新增买家
                                tds.eq(15).text(re.data[i].tradeSellerCount);//代运营交易卖家数
                                tds.eq(16).text(re.data[i].sellerTradeCount);//代运营卖家交易笔数
                                tds.eq(17).text(re.data[i].sellerTradeTotalWeight.toFixed(6));//代运营卖家交易重量
                                tds.eq(18).text(re.data[i].tempTradeSellerCount);//非代运营交易卖家数
                                tds.eq(19).text(re.data[i].tempSellerTradeCount);//非代运营卖家交易笔数
                                tds.eq(20).text(re.data[i].tempSellerTradeTotalWeight.toFixed(6));//非代运营卖家交易重量

                                subtr.find("td.details-control").removeClass("details-control");
                                subtr.attr("parent_org_id", org_id);
                                tr.after(subtr);
                            }
                            tr.attr("detailloaded","true");
                        }
                    }
                });
            }
            else
            {
                $("#dynamic-table tr[parent_org_id=" + org_id + "]").removeClass("none");
            }
        }
    });
}

function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/tradestatisticstoexcel.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'timespan');
    input1.attr('value', $("#tradetimespan").val());

    $('body').append(form);
    form.append(input1);

    form.submit();
    form.remove();
}

